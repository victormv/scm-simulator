package br.gov.ifpb.scm.core;

import java.util.Date;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Workflow;

public class CoreProductionLine {

	public static void createNewProductionLineAndPersistFirstProceeding(OrderServiceProduct orderServiceProduct, DAO dao) {

		ProductionLine productionLine = dao.persistProductionLine(orderServiceProduct.getId(), CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);

		Workflow wf = CoreWorkflow.getInstance().getFirstWorkflowByProductAndArea(CoreConstants.AREA_SPECIFIC_ID, orderServiceProduct.getIdProduct());
		if(wf != null && wf.getId() != null) {
			dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), CoreConstants.PROCEEDINGS_STAGE_INIT, null);
		}
	}

	public static void checkAndUpdateProductionLineStatusProducing(ProductionLine productionLine, Date dateNow, DAO dao) {

		if(productionLine.getIdProductionLineStatus().equals(CoreConstants.PRODUCTION_LINE_STATUS_OPENNED) || productionLine.getMomentStarted() == null) {

			productionLine.setIdProductionLineStatus(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCING);
			productionLine.setMomentStarted(dateNow);

			productionLine = dao.updateEntity(productionLine);
		}
	}

	public static void endProductionLine(ProductionLine productionLine, OrderServiceProduct orderServiceProduct, Date dateNow, DAO dao) {

		productionLine.setIdProductionLineStatus(CoreConstants.PRODUCTION_LINE_STATUS_PRODUCED);
		productionLine.setMomentEnded(dateNow);
		dao.updateEntity(productionLine);

		CoreOrderService.checkAndUpdateOrderServiceProduced(orderServiceProduct, dao);
	}
}
