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
@Table(name="sectors", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_sectors", name = "seq")
public class Sector	
{
	private Integer id;
	private String name;
	private String acronym;
	private String description;
	private Integer idUserLeader;
	private Integer idUserCreated;
	private Integer idUserChanged;
	private Date momentCreated;
	private Date momentChanged;
	private Integer idArea;
	private boolean rework;
	
	public Sector() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="acronym")
	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="id_user_leader")
	public Integer getIdUserLeader() {
		return idUserLeader;
	}

	public void setIdUserLeader(Integer idUserLeader) {
		this.idUserLeader = idUserLeader;
	}

	@Column(name="id_user_created")
	public Integer getIdUserCreated() {
		return idUserCreated;
	}

	public void setIdUserCreated(Integer idUserCreated) {
		this.idUserCreated = idUserCreated;
	}

	@Column(name="id_user_changed")
	public Integer getIdUserChanged() {
		return idUserChanged;
	}

	public void setIdUserChanged(Integer idUserChanged) {
		this.idUserChanged = idUserChanged;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="moment_created")
	public Date getMomentCreated() {
		return momentCreated;
	}

	public void setMomentCreated(Date momentCreated) {
		this.momentCreated = momentCreated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="moment_changed")
	public Date getMomentChanged() {
		return momentChanged;
	}

	public void setMomentChanged(Date momentChanged) {
		this.momentChanged = momentChanged;
	}

	@Column(name="id_area")
	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	@Column(name="rework")
	public boolean isRework() {
		return rework;
	}

	public void setRework(boolean rework) {
		this.rework = rework;
	}
}
