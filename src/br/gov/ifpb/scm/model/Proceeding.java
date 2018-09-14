package br.gov.ifpb.scm.model;

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
@Table(name="proceedings", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_proceedings", name = "seq")
public class Proceeding	{
	private Long id;
	private Long idProductionLine;
	private Date momentStarted;
	private Date momentEnded;
	private Integer idSectorOrigin;
	private Integer idSectorDestination;
	private Integer idUser;
	private Integer idSectorFailure;
	private Integer stage;
	private Character type;
	
	public Proceeding() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="id_production_line")
	public Long getIdProductionLine() {
		return idProductionLine;
	}

	public void setIdProductionLine(Long idProductionLine) {
		this.idProductionLine = idProductionLine;
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

	@Column(name="id_sector_origin")
	public Integer getIdSectorOrigin() {
		return idSectorOrigin;
	}

	public void setIdSectorOrigin(Integer idSectorOrigin) {
		this.idSectorOrigin = idSectorOrigin;
	}

	@Column(name="id_sector_destination")
	public Integer getIdSectorDestination() {
		return idSectorDestination;
	}

	public void setIdSectorDestination(Integer idSectorDestination) {
		this.idSectorDestination = idSectorDestination;
	}
	
	@Column(name="id_user")
	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	@Column(name="id_sector_failure")
	public Integer getIdSectorFailure() {
		return idSectorFailure;
	}

	public void setIdSectorFailure(Integer idSectorFailure) {
		this.idSectorFailure = idSectorFailure;
	}

	@Column(name="stage")
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	@Column(name="type")
	public Character getType() {
		return type;
	}

	public void setType(Character type) {
		this.type = type;
	}
}
