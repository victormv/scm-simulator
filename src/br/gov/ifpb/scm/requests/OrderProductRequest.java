package br.gov.ifpb.scm.requests;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.internal.StringMap;

import br.gov.ifpb.scm.model.to.ListPlanningIsCurrentDateResponseData;
import br.gov.ifpb.scm.model.to.ListProductionLineProducingResponseData;
import hibernate.util.ResponseUtil;

public class OrderProductRequest {

	@SuppressWarnings("unchecked")
	public static ListPlanningIsCurrentDateResponseData listPlanningOpInCurrentDateGet(String token) {

		Map<String, Object> planningOpsCurrentDateResponse = GenericRequest.get("planning-weekly/listPlanningIsCurrentDate", token, null);

		ListPlanningIsCurrentDateResponseData data = new ListPlanningIsCurrentDateResponseData();

		ArrayList<Object> al = (ArrayList<Object>) planningOpsCurrentDateResponse.get("data");
		if(al != null && !al.isEmpty()) {
			StringMap<Object> sm = (StringMap<Object>) al.get(0);
			if(sm != null && !sm.isEmpty()) {
				Double d = Double.parseDouble(sm.get("id").toString());
				data.setIdOp(d.longValue());
				data.setCodeOp(sm.get("code").toString());
			}
		}
		return data;
	}

	@SuppressWarnings({ "unchecked" })
	public static ListProductionLineProducingResponseData listProductionLineProducingGet(String token, Map<String, Object> params) {

		Map<String, Object> productionLinesProducingResponse = GenericRequest.get("production-lines/producing", token, params);

		ListProductionLineProducingResponseData data = new ListProductionLineProducingResponseData();

		ResponseUtil.handleResponse(productionLinesProducingResponse, data);

		if(data.isSuccess()) {
			if(productionLinesProducingResponse.get("data") != null) {
				ArrayList<Object> al = (ArrayList<Object>) productionLinesProducingResponse.get("data");
				if(al != null && !al.isEmpty()) {
					StringMap<Object> sm = (StringMap<Object>) al.get(0);

					Double d = Double.parseDouble(sm.get("osp").toString());
					data.setIdOp(d.longValue());
					data.setSequential(sm.get("sequential").toString());
				}
			}
		}

		return data;
	}
}
