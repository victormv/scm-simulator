package br.gov.ifpb.scm.core.threads;

import java.util.Date;

import br.gov.ifpb.scm.core.CoreConstants;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.PlanningWeekly;
import br.gov.ifpb.scm.util.Util;

public class OperatorLeaderThread extends AbstractOperatorThread {
	
	public OperatorLeaderThread() {
		//Cria um plajenamento semanal responsavel pela semana
//		Calendar calendario = new GregorianCalendar();
//		calendario.add(Calendar.DAY_OF_MONTH, 7);
//		this.dao.persistPlanningWeekly(new Date(), calendario.getTime());
	}
	
	@Override
	public void run() {
		
		super.out("Executing Operator Leader...");
		
		OrderService os = null;
		
		// Create O.S. 
		for(int i = 0; i < CoreConstants.ORDER_SERVICES_LIMIT; i++) {
			
			os = this.dao.persistOrderService(this.getCodeOrderService());
			System.out.println("[LOG] Criando OP " + os.getId());
			
			PlanningWeekly pwAtual = this.dao.getActualPlanningWeekly(new Date());
			
			int quantidadeDeProdutos = Util.getRandom(CoreConstants.ORDER_SERVICES_PRODUCT_MIN, CoreConstants.ORDER_SERVICES_PRODUCT_MAX);
			
			this.dao.persistPlanningDailyTime(
				pwAtual.getId(),
				os.getId(),
				new Date(),
				"10:00",
				"11:00",
				quantidadeDeProdutos);
			
			// Create O.S.P
			this.dao.persistOrderServiceProduct(os, quantidadeDeProdutos, 
				CoreConstants.AREA_SPECIFIC_ID, this.dao.getIdProductRandom(null));
			
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
