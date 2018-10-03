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
@Table(name="planning_daily_time", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_planning_daily", name = "seq")
public class PlanningDailyTime	{
	
	private Long id;
	private Long idPlanningWeekly;
	private Long idOrderService;
	private Date date;
	private Date timeBegin;
	private Date timeEnd;
	private Integer amount;
	private Integer idUserCreated;
	
	public PlanningDailyTime() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="id_planning_weekly")
	public Long getIdPlanningWeekly() {
		return idPlanningWeekly;
	}

	public void setIdPlanningWeekly(Long idPlanningWeekly) {
		this.idPlanningWeekly = idPlanningWeekly;
	}

	@Column(name="id_order_service")
	public Long getIdOrderService() {
		return idOrderService;
	}

	public void setIdOrderService(Long idOrderService) {
		this.idOrderService = idOrderService;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Temporal(TemporalType.TIME)
	@Column(name="time_begin")
	public Date getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(Date timeBegin) {
		this.timeBegin = timeBegin;
	}

	@Temporal(TemporalType.TIME)
	@Column(name="time_end")
	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	@Column(name="amount")
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(name="id_user_created")
	public Integer getIdUserCreated() {
		return idUserCreated;
	}

	public void setIdUserCreated(Integer idUserCreated) {
		this.idUserCreated = idUserCreated;
	}
}
