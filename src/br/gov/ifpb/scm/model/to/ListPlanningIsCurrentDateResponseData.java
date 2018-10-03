package br.gov.ifpb.scm.model.to;

public class ListPlanningIsCurrentDateResponseData extends ResponseData {
	
	private Integer idOp;
	private String codeOp;
	
	public Integer getIdOp() {
		return idOp;
	}
	public void setIdOp(Integer idOp) {
		this.idOp = idOp;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
}
