package br.gov.ifpb.scm.core;

import br.gov.ifpb.scm.dao.DAO;
import br.gov.ifpb.scm.model.OrderService;
import br.gov.ifpb.scm.model.OrderServiceProduct;

public class CoreOrderService {

	public static void checkAndUpdateOrderServiceStatusProducing(OrderService orderService, DAO dao) {

		if(orderService.getIdOrderServiceStatus().equals(CoreConstants.ORDER_SERVICES_STATUS_OPENNED)) {
			orderService.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_PRODUCING);
			orderService = dao.updateEntity(orderService);
		}
	}

	public static void checkAndUpdateOrderServiceProduced(OrderServiceProduct orderServiceProduct, DAO dao) {

		int amountOrderService = dao.getAmountOrderService(orderServiceProduct.getIdOrderService());
		int producedOrderService = dao.getProductionLineProducedFromOrderService(orderServiceProduct.getIdOrderService());

		if(producedOrderService >= amountOrderService) {
			OrderService orderService = dao.getEntity(orderServiceProduct.getIdOrderService(), OrderService.class);
			orderService.setIdOrderServiceStatus(CoreConstants.ORDER_SERVICES_STATUS_PRODUCED);
			dao.updateEntity(orderService);
		}
	}
}
