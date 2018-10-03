package br.gov.ifpb.scm.model.to;

public class ResponseData {
	
	private String message;
	private boolean success = false;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
