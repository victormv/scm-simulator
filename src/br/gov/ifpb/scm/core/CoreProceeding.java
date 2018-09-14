package br.gov.ifpb.scm.core;

import java.util.Date;
import java.util.List;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.Proceeding;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Workflow;
import br.gov.ifpb.scm.util.Util;

public class CoreProceeding {

	public static Proceeding getAvaliableProceedingInSector(Integer idSector, List<Integer> productionLineStatus, DAO dao) {
		return dao.getAvaliableProceedingInSector(idSector, productionLineStatus);
	}

	public static Proceeding startProceeding(Proceeding proceeding, Integer idUser, DAO dao) {

		ProductionLine productionLine = dao.getEntity(proceeding.getIdProductionLine(), ProductionLine.class);

		Date dateNow = new Date();
		proceeding.setMomentStarted(dateNow);
		proceeding.setIdUser(idUser);
		proceeding = dao.updateEntity(proceeding);

		OrderServiceProduct orderServiceProduct = dao.getEntity(productionLine.getIdOrderServiceProduct(), OrderServiceProduct.class);
		OrderService orderService = dao.getEntity(orderServiceProduct.getIdOrderService(), OrderService.class);

		CoreProductionLine.checkAndUpdateProductionLineStatusProducing(productionLine, dateNow, dao);
		CoreOrderService.checkAndUpdateOrderServiceStatusProducing(orderService, dao);

		return proceeding;
	}

	public static void endProceeding(Proceeding proceeding, ProductionLine productionLine, OrderServiceProduct orderServiceProduct, DAO dao) throws InterruptedException {

		Date dateNow = new Date();
		proceeding.setMomentEnded(dateNow);
		proceeding = dao.updateEntity(proceeding);

		Thread.sleep(Util.getRandom(CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MIN, CoreConstants.PROCEEDINGS_SLEEP_MS_BETWEEN_DISPATCHED_ARRIVED_MAX));

		CoreProceeding.prepareNextProceeding(proceeding, productionLine, orderServiceProduct, dateNow, dao);
	}

	public static void prepareNextProceeding(Proceeding proceeding, ProductionLine productionLine, OrderServiceProduct orderServiceProduct, Date dateNow, DAO dao) {

		Workflow wf = CoreWorkflow.getInstance().getNextWorkflow(proceeding.getIdSectorDestination(), proceeding.getStage(), CoreConstants.AREA_SPECIFIC_ID);
		if(wf == null) {
			CoreProductionLine.endProductionLine(productionLine, orderServiceProduct, dateNow, dao);
		} else {
			dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), wf.getStage(), null, 'O');
		}
	}
}
