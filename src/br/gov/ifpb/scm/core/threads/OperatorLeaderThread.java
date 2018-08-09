package br.gov.ifpb.scm.core.threads;

import java.util.ArrayList;
import java.util.List;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.core.CoreProductionLine;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;
import br.gov.ifpb.scm.util.Util;

public class OperatorLeaderThread extends AbstractOperatorThread {	
	
	@Override
	public void run() {
		
		super.out("Executing Operator Leader...");
		
		OrderService os = null;
		OrderServiceProduct osp = null;
		
		List<Long> listIdProductUsed = new ArrayList<>();
		
		int limitOSP = Util.getRandom(CoreConstants.ORDER_SERVICES_OSP_MIN, CoreConstants.ORDER_SERVICES_OSP_MAX);
		
		// Create O.S. 
		for(int i = 0; i < CoreConstants.ORDER_SERVICES_LIMIT; i++) {
			
			os = this.dao.persistOrderService(this.getCodeOrderService());
			
			listIdProductUsed.clear();
			
			// Create O.S.P
			for(int j = 0; j < limitOSP; j++) {
				osp = this.dao.persistOrderServiceProduct(os, Util.getRandom(CoreConstants.ORDER_SERVICES_PRODUCT_MIN, CoreConstants.ORDER_SERVICES_PRODUCT_MAX), 
					CoreConstants.AREA_SPECIFIC_ID, this.dao.getIdProductRandom(listIdProductUsed));
				
				listIdProductUsed.add(osp.getIdProduct());
				
				// Create Production Lines and first proceeding
				for(int z = 0; z < osp.getAmount(); z++) {
					CoreProductionLine.createNewProductionLineAndPersistFirstProceeding(osp, this.dao);
				}
			}
			
			// DELAY
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
