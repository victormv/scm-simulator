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
@Table(name="planning_weekly", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_planning_weekly", name = "seq")
public class PlanningWeekly	{
	
	private Long id;
	private Date dateWeekStart;
	private Date dateWeekEnd;
	private Integer idUserCreated;
	
	public PlanningWeekly() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="date_week_start")
	@Temporal(TemporalType.DATE)
	public Date getDateWeekStart() {
		return dateWeekStart;
	}

	public void setDateWeekStart(Date dateWeekStart) {
		this.dateWeekStart = dateWeekStart;
	}

	@Column(name="date_week_end")
	@Temporal(TemporalType.DATE)
	public Date getDateWeekEnd() {
		return dateWeekEnd;
	}

	public void setDateWeekEnd(Date dateWeekEnd) {
		this.dateWeekEnd = dateWeekEnd;
	}

	@Column(name="id_user_created")
	public Integer getIdUserCreated() {
		return idUserCreated;
	}

	public void setIdUserCreated(Integer idUserCreated) {
		this.idUserCreated = idUserCreated;
	}
}
