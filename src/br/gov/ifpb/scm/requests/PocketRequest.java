package br.gov.ifpb.scm.requests;

import java.util.Map;

import br.gov.ifpb.scm.model.to.ResponseData;
import hibernate.util.ResponseUtil;

public class PocketRequest {

	public static ResponseData startTaskPost(Map<String,Object> startTaskParams, String token) {

		Map<String, Object> startTaskResponse = GenericRequest.post("pocket/start-task", token, startTaskParams);

		ResponseData data = new ResponseData();

		ResponseUtil.handleResponse(startTaskResponse, data);

		return data;
	}

	public static ResponseData checkInPost(Map<String,Object> checkInParams, String token) {

		Map<String, Object> checkInResponse = GenericRequest.post("pocket/checkin", token, checkInParams);

		ResponseData data = new ResponseData();

		ResponseUtil.handleResponse(checkInResponse, data);
		return data;
	}

	public static ResponseData endTaskPost(Map<String,Object> endTaskParams, String token) {

		Map<String, Object> endTaskResponse = GenericRequest.post("pocket/end-task", token, endTaskParams);

		ResponseData data = new ResponseData();

		ResponseUtil.handleResponse(endTaskResponse, data);

		return data;
	}
}
