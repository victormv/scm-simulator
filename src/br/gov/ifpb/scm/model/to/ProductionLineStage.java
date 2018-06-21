package br.gov.ifpb.scm.model.to;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class ProductionLineStage
{
	private Long id;
	private Long idOrderServiceProduct;
	private Date momentStarted;
	private Date momentEnded;
	private Integer idProductQuality;
	private Integer idProductionLineStatus;
	private Integer stage;

	public ProductionLineStage() {}

	@Id
	@Column(name="id")
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

	@Column(name="id_product_quality")
	public Integer getIdProductQuality() {
		return idProductQuality;
	}

	public void setIdProductQuality(Integer idProductQuality) {
		this.idProductQuality = idProductQuality;
	}

	@Column(name="id_production_line_status")
	public Integer getIdProductionLineStatus() {
		return idProductionLineStatus;
	}

	public void setIdProductionLineStatus(Integer idProductionLineStatus) {
		this.idProductionLineStatus = idProductionLineStatus;
	}

	@Column(name="stage")
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
}
