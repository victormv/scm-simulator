package br.gov.ifpb.scm.model.to;

public class ListProductionLineProducingResponseData extends ResponseData {
	
	private Long idOp;
	private String sequential;

	public String getSequential() {
		return sequential;
	}
	public void setSequential(String sequential) {
		this.sequential = sequential;
	}
	
	public Long getIdOp() {
		return idOp;
	}
	public void setIdOp(Long idOp) {
		this.idOp = idOp;
	}
}
