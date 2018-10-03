package br.gov.ifpb.scm.requests;

import java.util.Map;

import br.gov.ifpb.scm.model.to.LoginResponseData;
import hibernate.util.ResponseUtil;

public class LoginRequest extends GenericRequest {
	
	public static LoginResponseData loginPost(Map<String,Object> loginParams) {
		
		Map<String, Object> loginResponse = GenericRequest.post("login", null, loginParams);
		
		LoginResponseData data = new LoginResponseData();
		
		ResponseUtil.handleResponse(loginResponse, data);
		if(data.isSuccess()) {
			data.setToken(loginResponse.get("token").toString());
		}
		
		return data;
	}
}
