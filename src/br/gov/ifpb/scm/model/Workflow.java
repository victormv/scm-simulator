package br.gov.ifpb.scm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="workflows", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_workflows", name = "seq")
public class Workflow {
	
	private Long id;
	private Integer stage;
	private Integer idSectorOrigin;
	private Integer idSectorDestination;
	private Integer idUserCreated;
	private Integer idArea;
	
	public Workflow() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="stage", updatable=false)
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	@Column(name="id_sector_origin", updatable=false)
	public Integer getIdSectorOrigin() {
		return idSectorOrigin;
	}

	public void setIdSectorOrigin(Integer idSectorOrigin) {
		this.idSectorOrigin = idSectorOrigin;
	}

	@Column(name="id_sector_destination", updatable=false)
	public Integer getIdSectorDestination() {
		return idSectorDestination;
	}

	public void setIdSectorDestination(Integer idSectorDestination) {
		this.idSectorDestination = idSectorDestination;
	}

	@Column(name="id_user_created", updatable=false)
	public Integer getIdUserCreated() {
		return idUserCreated;
	}

	public void setIdUserCreated(Integer idUserCreated) {
		this.idUserCreated = idUserCreated;
	}

	@Column(name="id_area", updatable=false)
	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
}
