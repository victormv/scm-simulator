package br.gov.ifpb.scm.core.threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.Proceeding;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Sector;
import br.gov.ifpb.scm.model.Workflow;
import br.gov.ifpb.scm.util.Util;
import hibernate.util.JpaUtil;

public class OperatorSectorThread extends AbstractOperatorThread
{
	private DAO dao;
	private Sector sector;
	private List<Workflow> listWorkflows;
	private List<Integer> listProductionLineStatus;

	public OperatorSectorThread(Sector sector) 
	{
		this.sector = sector;

		this.dao = new DAO(JpaUtil.createEntityManager());

		this.listWorkflows = this.dao.getWorkflowsFromSector(this.sector.getId(), CoreConstants.AREA_SPECIFIC_ID);

		this.listProductionLineStatus = new ArrayList<Integer>();
		if(this.sector.isRework())
		{
			this.listProductionLineStatus = new ArrayList<Integer>();
			this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_REWORK);
		}
		else
		{
			this.listProductionLineStatus = new ArrayList<Integer>();
			this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
			this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
		}
	}

	@Override
	public void run()
	{
		System.out.println("SECTOR [" + this.sector.getId() + "] working...");

		Proceeding proceeding = null;
		while(true)
		{
			proceeding = this.dao.getAvaliableProceedingInSector(this.sector.getId(), this.listProductionLineStatus);

			if(proceeding != null)
			{
				proceeding = this.startSectorTask(proceeding);
				
				if(proceeding == null)
				{
					continue;
				}

				try 
				{
					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_STARTED_DISPATCHED_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_STARTED_DISPATCHED_MAX));
				} 
				catch (InterruptedException e) 
				{
					this.out("ERROR");
				}

				proceeding = this.endSectorTask(proceeding);
				
				try 
				{
					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MAX));
				} 
				catch (InterruptedException e) 
				{
					this.out("ERROR");
				}
				
				this.prepareNextProceeding(proceeding);
			}

			try 
			{
				Thread.sleep(CoreConstants.SECTOR_CHECK_DEMAND_SLEEP_MS);
			} 
			catch (InterruptedException e) 
			{
				this.out("ERROR");
			}
		}
	}

	private Workflow getWorkflow(long idProduct, int stage, Proceeding proceeding, Integer idArea)
	{
		if(this.sector.isRework())
		{
			Workflow workflow = new Workflow();
			workflow.setIdProduct(idProduct);
			workflow.setIdSectorDestination(proceeding.getIdSectorOrigin());
			workflow.setIdSectorOrigin(proceeding.getIdSectorDestination());
			workflow.setStage(stage);
			workflow.setIdArea(idArea);
			
			return workflow;
		}
		else
		{
			int newStage = stage+1;
			for(Workflow wf : this.listWorkflows)
			{
				if(wf.getIdProduct().equals(idProduct) && wf.getStage().equals(newStage))
				{
					return wf;
				}
			}
			return null;
		}
	}
	
	private Workflow prepareRework(Workflow workflow, Proceeding proceeding)
	{
		Sector sectorRework = this.dao.getSectorRework(workflow.getIdArea());
		if(sectorRework != null)
		{
			workflow = new Workflow();
			workflow.setIdSectorDestination(sectorRework.getId());
			workflow.setStage(proceeding.getStage());
			workflow.setIdSectorOrigin(proceeding.getIdSectorDestination());
		}
		return workflow;
	}

	private void prepareNextProceeding(Proceeding proceeding)
	{
		ProductionLine productionLine = this.dao.getEntity(proceeding.getIdProductionLine(), ProductionLine.class);		

		OrderServiceProduct orderServiceProduct = this.dao.getEntity(productionLine.getIdOrderServiceProduct(), OrderServiceProduct.class);

		Workflow wf = this.getWorkflow(orderServiceProduct.getIdProduct(), proceeding.getStage(), proceeding, orderServiceProduct.getIdArea());

		if(wf == null)
		{
			this.endProductionLine(productionLine, orderServiceProduct);
		}
		else
		{
			Integer idSectorFailure = null;
			if(Util.getRandom(0, 100) < CoreConstants.PRODUCTION_LINE_REWORK_PROBABILITY && !this.sector.isRework())
			{
				wf = this.prepareRework(wf, proceeding);
				idSectorFailure = this.dao.getIdSectorFailureRandom(wf.getIdSectorOrigin());
			}
			this.dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), wf.getStage(), idSectorFailure);
			
			if(idSectorFailure == null)
			{
				this.dao.updateProductionLineStatus(productionLine.getId(), CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
			}
			else
			{
				this.dao.updateProductionLineStatus(productionLine.getId(), CoreConstants.PRODUCTION_LINE_STATUS_REWORK);
			}
		}
	}

	private Proceeding endSectorTask(Proceeding proceeding)
	{
		System.out.println("SECTOR [" + this.sector.getId() + "] ending task...");
		proceeding.setMomentEnded(new Date());
		proceeding = this.dao.updateEntity(proceeding);

		int randomProductQuality = this.dao.getIdProductQualityRandom();
		this.dao.updateProductQualityFromProductionLine(proceeding.getIdProductionLine(), randomProductQuality);

		return proceeding;
	}

	private void endProductionLine(ProductionLine productionLine, OrderServiceProduct orderServiceProduct)
	{
		System.out.println("SECTOR [" + this.sector.getId() + "] ending production line...");
		productionLine.setIdProductionLineStatus(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCED);
		productionLine.setMomentEnded(new Date());
		this.dao.updateEntity(productionLine);

		this.checkAndUpdateOrderServiceProduced(orderServiceProduct);
	}

	private void checkAndUpdateOrderServiceProduced(OrderServiceProduct orderServiceProduct)
	{
		int amountOrderService = this.dao.getAmountOrderService(orderServiceProduct.getIdOrderService());
		int producedOrderService = this.dao.getProductionLineProducedFromOrderService(orderServiceProduct.getIdOrderService());

		if(producedOrderService >= amountOrderService)
		{
			OrderService orderService = this.dao.getEntity(orderServiceProduct.getIdOrderService(), OrderService.class);
			orderService.setDateEnded(new Date());
			orderService.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_PRODUCED);
			this.dao.updateEntity(orderService);

			System.out.println("SECTOR [" + this.sector.getId() + "] produced ORDER SERVICE...");
		}
	}

	private Proceeding startSectorTask(Proceeding proceeding)
	{
		ProductionLine productionLine = this.dao.getEntity(proceeding.getIdProductionLine(), ProductionLine.class);
		System.out.println("SECTOR [" + this.sector.getId() + "] starting task...");
		
		if(this.sector.isRework())
		{
			int timesReworked = this.dao.getTimesOverRework(productionLine.getId(), this.sector.getId());
			if(timesReworked >= CoreConstants.PRODUCTION_LINE_REWORK_MAX)
			{
				Date dataAtual = new Date();
				proceeding.setMomentStarted(dataAtual);
				proceeding.setMomentEnded(dataAtual);
				proceeding.setIdUser(CoreConstants.USER_OPERATION_ID);
				proceeding = this.dao.updateEntity(proceeding);
				
				this.dao.updateProductionLineStatus(productionLine.getId(), CoreConstants.PRODUCTION_LINE_STATUS_DISCARDED);
				this.createNewProductionLine(productionLine);
				return null;
			}
		}
		
		proceeding.setMomentStarted(new Date());
		proceeding.setIdUser(CoreConstants.USER_OPERATION_ID);
		proceeding = this.dao.updateEntity(proceeding);

		this.checkAndUpdateProductionLineStatusProducing(productionLine);
		this.checkAndUpdateOrderServiceStatusProducing(productionLine);

		return proceeding;
	}

	private void checkAndUpdateProductionLineStatusProducing(ProductionLine productionLine)
	{
		if(productionLine.getIdProductionLineStatus().equals(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED) || productionLine.getMomentStarted() == null)
		{
			System.out.println("SECTOR [" + this.sector.getId() + "] set producing PRODUCTION LINE...");
			productionLine.setIdProductionLineStatus(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
			productionLine.setMomentStarted(new Date());

			productionLine = this.dao.updateEntity(productionLine);
		}
	}

	private void checkAndUpdateOrderServiceStatusProducing(ProductionLine productionLine)
	{
		OrderServiceProduct orderServiceProduct = this.dao.getEntity(productionLine.getIdOrderServiceProduct(), OrderServiceProduct.class);
		OrderService orderService = this.dao.getEntity(orderServiceProduct.getIdOrderService(), OrderService.class);

		if(orderService.getIdOrderServiceStatus().equals(CoreConstants.ORDER_SERVICES_STATUS_OPENNED) || orderService.getDateStarted() == null)
		{
			System.out.println("SECTOR [" + this.sector.getId() + "] set producing ORDER SERVICE...");
			orderService.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_PRODUCING);
			orderService.setDateStarted(new Date());

			orderService = this.dao.updateEntity(orderService);
		}
	}
	
	private void createNewProductionLine(ProductionLine plDiscarded)
	{
		ProductionLine productionLine = this.dao.persistProductionLine(plDiscarded.getIdOrderServiceProduct(), CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
		Workflow wf = this.dao.getFirstWorkflowFromProductionLine(CoreConstants.AREA_SPECIFIC_ID, plDiscarded.getIdOrderServiceProduct());
		if(wf != null && wf.getId() != null)
		{
			this.dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), CoreConstants.PROCEEDINGS_STAGE_INIT, null);
		}
	}
}