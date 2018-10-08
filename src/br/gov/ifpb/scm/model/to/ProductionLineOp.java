package br.gov.ifpb.scm.model.to;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="production_lines", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_production_lines", name = "seq")
public class ProductionLineOp	{
	
	private Long id;
	private Long idOrderServiceProduct;
	private Long idOrderService;
	private Date momentStarted;
	private Date momentEnded;
	private Integer idProductionLineStatus;
	private String sequential;
	
	public ProductionLineOp() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="id_order_service_product")
	public Long getIdOrderServiceProduct() {
		return idOrderServiceProduct;
	}

	public void setIdOrderServiceProduct(Long idOrderServiceProduct) {
		this.idOrderServiceProduct = idOrderServiceProduct;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="moment_started")
	public Date getMomentStarted() {
		return momentStarted;
	}

	public void setMomentStarted(Date momentStarted) {
		this.momentStarted = momentStarted;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="moment_ended")
	public Date getMomentEnded() {
		return momentEnded;
	}

	public void setMomentEnded(Date momentEnded) {
		this.momentEnded = momentEnded;
	}

	@Column(name="id_production_line_status")
	public Integer getIdProductionLineStatus() {
		return idProductionLineStatus;
	}

	public void setIdProductionLineStatus(Integer idProductionLineStatus) {
		this.idProductionLineStatus = idProductionLineStatus;
	}

	@Column(name="sequential")
	public String getSequential() {
		return sequential;
	}

	public void setSequential(String sequential) {
		this.sequential = sequential;
	}

	@Column(name="id_order_service")
	public Long getIdOrderService() {
		return idOrderService;
	}

	public void setIdOrderService(Long idOrderService) {
		this.idOrderService = idOrderService;
	}
}
