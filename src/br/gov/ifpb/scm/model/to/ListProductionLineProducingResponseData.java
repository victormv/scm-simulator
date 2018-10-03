package br.gov.ifpb.scm.model.to;

public class ListProductionLineProducingResponseData extends ResponseData {
	
	private Integer idOp;
	private String sequential;
	
	public Integer getIdOp() {
		return idOp;
	}
	public void setIdOp(Integer idOp) {
		this.idOp = idOp;
	}
	public String getSequential() {
		return sequential;
	}
	public void setSequential(String sequential) {
		this.sequential = sequential;
	}
}
