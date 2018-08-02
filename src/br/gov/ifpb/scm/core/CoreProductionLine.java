package br.gov.ifpb.scm.core;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Workflow;

public class CoreProductionLine {

	public static void createNewProductionLine(ProductionLine plDiscarded, OrderServiceProduct orderServiceProduct, DAO dao) {
		
		ProductionLine productionLine = dao.persistProductionLine(plDiscarded.getIdOrderServiceProduct(), CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
		
		Workflow wf = CoreWorkflow.getInstance().getFirstWorkflowByProductAndArea(CoreConstants.AREA_SPECIFIC_ID, orderServiceProduct.getIdProduct());
		if(wf != null && wf.getId() != null) {
			dao.persistProceeding(productionLine.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), CoreConstants.PROCEEDINGS_STAGE_INIT, null);
		}
	}
}
