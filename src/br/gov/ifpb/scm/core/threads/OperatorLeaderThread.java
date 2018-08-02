package br.gov.ifpb.scm.core.threads;

import java.util.ArrayList;
import java.util.List;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.core.CoreWorkflow;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.model.ProductionLine;
import br.gov.ifpb.scm.model.Workflow;
import br.gov.ifpb.scm.util.Util;

public class OperatorLeaderThread extends AbstractOperatorThread {	
	
	@Override
	public void run() {
		
		super.out("Executing Operator Leader...");
		
		OrderService os = null;
		OrderServiceProduct osp = null;
		ProductionLine pl = null;
		Workflow wf = null;
		
		Long idProduct;
		int limitOSP = 1;
		
		List<Long> listIdProductUsed = new ArrayList<>();
		
		if(CoreConstants.PRODUCT_SPECIFIC) {
			idProduct = CoreConstants.PRODUCT_ID;
			limitOSP = 1;
		} else {
			idProduct = this.dao.getIdProductRandom(listIdProductUsed);
			limitOSP = Util.getRandom(CoreConstants.ORDER_SERVICES_OSP_MIN, CoreConstants.ORDER_SERVICES_OSP_MAX);
		}
		
		// Create O.S. 
		for(int i = 0; i < CoreConstants.ORDER_SERVICES_LIMIT; i++) {
			os = this.dao.persistOrderService(this.getCodeOrderService());
			
			listIdProductUsed.clear();
			
			// Create O.S.P
			for(int j = 0; j < limitOSP; j++) {
				osp = this.dao.persistOrderServiceProduct(os, Util.getRandom(CoreConstants.ORDER_SERVICES_PRODUCT_MIN, CoreConstants.ORDER_SERVICES_PRODUCT_MAX), 
					CoreConstants.AREA_SPECIFIC_ID, idProduct);
				
				listIdProductUsed.add(osp.getIdProduct());
				
				// Create Production Lines
				for(int z = 0; z < osp.getAmount(); z++) {
					pl = this.dao.persistProductionLine(osp.getId(), CoreConstants.PRODUCTION_LINE_STATUS_OPENNED);
					wf = CoreWorkflow.getInstance().getFirstWorkflowByProductAndArea(CoreConstants.AREA_SPECIFIC_ID, osp.getIdProduct());
					if(wf != null && wf.getId() != null) {
						this.dao.persistProceeding(pl.getId(), wf.getIdSectorDestination(), wf.getIdSectorOrigin(), CoreConstants.PROCEEDINGS_STAGE_INIT, null);
					}
				}
			}
			
			try {
				Thread.sleep(Util.getRandom(CoreConstants.ORDER_SERVICES_CREATE_SLEEP_MS_MIN, CoreConstants.ORDER_SERVICES_CREATE_SLEEP_MS_MAX));
			} catch (InterruptedException e) {
				this.out("THREAD ERROR");
			}
		}
	}
	
	
	
	public String getCodeOrderService()	{
		return String.format("%010d", this.dao.getLastIdOrderService());
	}
}
