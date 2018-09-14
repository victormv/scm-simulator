package br.gov.ifpb.scm.core;

import java.util.Date;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.Proceeding;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Workflow;
import br.gov.ifpb.scm.util.Util;

public class CoreRework {
	
	public static boolean checkRework() {
		return (Util.getRandom(0, 100) < CoreConstants.PRODUCTION_LINE_REWORK_PROBABILITY);
	}
	
	public static void processRework(Proceeding proceeding, ProductionLine productionLine, OrderServiceProduct orderServiceProduct, DAO dao) {
		
		Integer idSectorFailure = dao.getIdSectorFailureRandom(proceeding.getIdSectorDestination());
		
		proceeding.setMomentEnded(new Date());
		proceeding.setIdSectorFailure(idSectorFailure);
		dao.updateEntity(proceeding);
				
		int timesReworked = dao.getTimesOverRework(productionLine.getId());
		if(timesReworked >= CoreConstants.PRODUCTION_LINE_REWORK_MAX) {
			CoreRework.processDiscard(productionLine, orderServiceProduct, dao);
			return;
		} else {
			Workflow wf = CoreWorkflow.getInstance().
				getFirstWorkflowByProductAndArea(CoreConstants.AREA_SPECIFIC_ID, orderServiceProduct.getIdProduct());
			dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), wf.getStage(), null, 'D');
		}
	}
	
	private static void processDiscard(ProductionLine productionLine, 
			OrderServiceProduct orderServiceProduct, DAO dao) {
		
		dao.updateProductionLineStatus(productionLine.getId(), CoreConstants.PRODUCTION_LINE_STATUS_DISCARDED);
		CoreProductionLine.createNewProductionLineAndPersistFirstProceeding(orderServiceProduct, dao);
	}
}
