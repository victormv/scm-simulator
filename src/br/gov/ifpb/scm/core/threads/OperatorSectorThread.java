package br.gov.ifpb.scm.core.threads;

import java.util.ArrayList;
import java.util.List;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.core.CoreProceeding;
import br.gov.ifpb.scm.core.CoreRework;
import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.Proceeding;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Sector;
import br.gov.ifpb.scm.util.Util;
import hibernate.util.JpaUtil;

public class OperatorSectorThread extends AbstractOperatorThread {

	private DAO dao;
	private Sector sector;
	private List<Integer> listProductionLineStatus;

	public OperatorSectorThread(Sector sector) {

		this.sector = sector;

		this.dao = new DAO(JpaUtil.createEntityManager());

		this.listProductionLineStatus = new ArrayList<Integer>();
		this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
		this.listProductionLineStatus.add(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
	}

	@Override
	public void run() {

		System.out.println("SECTOR [" + this.sector.getId() + "] working...");

		Proceeding proceeding = null;
		while(true) {

			ProductionLine productionLine = null;
			OrderServiceProduct orderServiceProduct = null;

			try {
				proceeding = CoreProceeding.getAvaliableProceedingInSector(this.sector.getId(), this.listProductionLineStatus, this.dao);

				if(proceeding != null) {

					productionLine = this.dao.getEntity(proceeding.getIdProductionLine(), ProductionLine.class);		
					orderServiceProduct = this.dao.getEntity(productionLine.getIdOrderServiceProduct(), OrderServiceProduct.class);

					// START TASK
					proceeding = this.startSectorTask(proceeding);

					// REWORK
					if(CoreRework.checkRework()) {
						CoreRework.processRework(proceeding, productionLine, orderServiceProduct, this.dao);
						continue;
					}

					// DELAY
					Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_TASK_MAX));

					// END TASK
					this.endSectorTask(proceeding, productionLine, orderServiceProduct);
				}
			} catch(InterruptedException e) {
				this.out("THREAD DELAY ERROR");
			} finally {
				try {
					Thread.sleep(CoreConstants.SECTOR_CHECK_DEMAND_SLEEP_MS);
				} catch (InterruptedException e) {
					this.out("THREAD DELAY ERROR");
				}
			}
		}
	}

	private void endSectorTask(Proceeding proceeding, ProductionLine productionLine, OrderServiceProduct orderServiceProduct) throws InterruptedException {

		System.out.println("SECTOR [" + this.sector.getId() + "] ending task...");
		CoreProceeding.endProceeding(proceeding, productionLine, orderServiceProduct, this.dao);
	}

	private Proceeding startSectorTask(Proceeding proceeding) {

		System.out.println("SECTOR [" + this.sector.getId() + "] starting task...");
		return CoreProceeding.startProceeding(proceeding, CoreConstants.USER_OPERATION_ID, this.dao);
	}
}