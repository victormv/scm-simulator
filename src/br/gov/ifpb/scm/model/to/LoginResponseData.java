package br.gov.ifpb.scm.model.to;

public class LoginResponseData extends ResponseData {
	
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
