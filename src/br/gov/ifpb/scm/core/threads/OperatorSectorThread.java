package br.gov.ifpb.scm.core.threads;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.core.CoreProceeding;
import br.gov.ifpb.scm.core.CoreRework;
import br.gov.ifpb.scm.core.CoreWorkflow;
import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.Proceeding;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Sector;
import br.gov.ifpb.scm.model.Workflow;
import br.gov.ifpb.scm.util.Util;
import hibernate.util.JpaUtil;

public class OperatorSectorThread extends AbstractOperatorThread {
	
	private DAO dao;
	private Sector sector;
	private List<Integer> listProductionLineStatus;
	private CoreWorkflow workflowCore;

	public OperatorSectorThread(Sector sector) {
		
		this.sector = sector;

		this.dao = new DAO(JpaUtil.createEntityManager());
		
		this.workflowCore = CoreWorkflow.getInstance();

		this.listProductionLineStatus = new ArrayList<Integer>();
		this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
		this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
	}

	@Override
	public void run() {
		
		System.out.println("SECTOR [" + this.sector.getId() + "] working...");

		Proceeding proceeding = null;
		while(true) {
			proceeding = CoreProceeding.getAvaliableProceedingInSector(this.sector.getId(), this.listProductionLineStatus, this.dao);
			
			ProductionLine productionLine = null;
			OrderServiceProduct orderServiceProduct = null;

			if(proceeding != null) {
				// START
				proceeding = this.startSectorTask(proceeding);
				
				productionLine = this.dao.getEntity(proceeding.getIdProductionLine(), ProductionLine.class);		
				orderServiceProduct = this.dao.getEntity(productionLine.getIdOrderServiceProduct(), OrderServiceProduct.class);
				
				
				// REWORK
				if(CoreRework.checkRework()) {
					CoreRework.processRework(proceeding, productionLine, orderServiceProduct, this.dao);
					continue;
				}

				// DELAY
				try {
					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_STARTED_DISPATCHED_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_STARTED_DISPATCHED_MAX));
				} catch (InterruptedException e) {
					this.out("THREAD ERROR");
				}

				
				// END
				this.endSectorTask(proceeding, productionLine, orderServiceProduct);
			}

			// DELAY
			try {
				Thread.sleep(CoreConstants.SECTOR_CHECK_DEMAND_SLEEP_MS);
			} catch (InterruptedException e) {
				this.out("THREAD ERROR");
			}
		}
	}
	
	private void prepareNextProceeding(Proceeding proceeding, ProductionLine productionLine, OrderServiceProduct orderServiceProduct) {
		
		Workflow wf = this.workflowCore.getNextWorkflow(orderServiceProduct.getIdProduct(), proceeding.getIdSectorDestination(), proceeding.getStage(), CoreConstants.AREA_SPECIFIC_ID);
		if(wf == null) {
			this.endProductionLine(productionLine, orderServiceProduct);
		} else {
			this.dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), wf.getStage(), null);
		}
	}

	private void endSectorTask(Proceeding proceeding, ProductionLine productionLine, OrderServiceProduct orderServiceProduct) {
		
		System.out.println("SECTOR [" + this.sector.getId() + "] ending task...");
		proceeding.setMomentEnded(new Date());
		proceeding = this.dao.updateEntity(proceeding);

		int randomProductQuality = this.dao.getIdProductQualityRandom();
		this.dao.updateProductQualityFromProductionLine(proceeding.getIdProductionLine(), randomProductQuality);
		
		// DELAY
		try {
			Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MAX));
		} catch (InterruptedException e) {
			this.out("THREAD ERROR");
		}

		// WORKFLOW
		this.prepareNextProceeding(proceeding, productionLine, orderServiceProduct);
	}

	private void endProductionLine(ProductionLine productionLine, OrderServiceProduct orderServiceProduct) {
		
		System.out.println("SECTOR [" + this.sector.getId() + "] ending production line...");
		productionLine.setIdProductionLineStatus(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCED);
		productionLine.setMomentEnded(new Date());
		this.dao.updateEntity(productionLine);

		this.checkAndUpdateOrderServiceProduced(orderServiceProduct);
	}

	private void checkAndUpdateOrderServiceProduced(OrderServiceProduct orderServiceProduct) {
		
		int amountOrderService = this.dao.getAmountOrderService(orderServiceProduct.getIdOrderService());
		int producedOrderService = this.dao.getProductionLineProducedFromOrderService(orderServiceProduct.getIdOrderService());

		if(producedOrderService >= amountOrderService) {
			OrderService orderService = this.dao.getEntity(orderServiceProduct.getIdOrderService(), OrderService.class);
			orderService.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_PRODUCED);
			this.dao.updateEntity(orderService);

			System.out.println("SECTOR [" + this.sector.getId() + "] produced ORDER SERVICE...");
		}
	}

	private Proceeding startSectorTask(Proceeding proceeding) {
		
		ProductionLine productionLine = this.dao.getEntity(proceeding.getIdProductionLine(), ProductionLine.class);
		System.out.println("SECTOR [" + this.sector.getId() + "] starting task...");

		proceeding.setMomentStarted(new Date());
		proceeding.setIdUser(CoreConstants.USER_OPERATION_ID);
		proceeding = this.dao.updateEntity(proceeding);

		this.checkAndUpdateProductionLineStatusProducing(productionLine);
		this.checkAndUpdateOrderServiceStatusProducing(productionLine);

		return proceeding;
	}

	private void checkAndUpdateProductionLineStatusProducing(ProductionLine productionLine) {
		
		if(productionLine.getIdProductionLineStatus().equals(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED) || productionLine.getMomentStarted() == null) {
			System.out.println("SECTOR [" + this.sector.getId() + "] set producing PRODUCTION LINE...");
			productionLine.setIdProductionLineStatus(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
			productionLine.setMomentStarted(new Date());

			productionLine = this.dao.updateEntity(productionLine);
		}
	}

	private void checkAndUpdateOrderServiceStatusProducing(ProductionLine productionLine) {
		
		OrderServiceProduct orderServiceProduct = this.dao.getEntity(productionLine.getIdOrderServiceProduct(), OrderServiceProduct.class);
		OrderService orderService = this.dao.getEntity(orderServiceProduct.getIdOrderService(), OrderService.class);

		if(orderService.getIdOrderServiceStatus().equals(CoreConstants.ORDER_SERVICES_STATUS_OPENNED)) {
			System.out.println("SECTOR [" + this.sector.getId() + "] set producing ORDER SERVICE...");
			orderService.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_PRODUCING);

			orderService = this.dao.updateEntity(orderService);
		}
	}
}