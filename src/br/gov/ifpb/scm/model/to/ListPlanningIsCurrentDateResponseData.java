package br.gov.ifpb.scm.model.to;

public class ListPlanningIsCurrentDateResponseData extends ResponseData {
	
	private Long idOp;
	private String codeOp;
	
	public Long getIdOp() {
		return idOp;
	}
	public void setIdOp(Long idOp) {
		this.idOp = idOp;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
}
