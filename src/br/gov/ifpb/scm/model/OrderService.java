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
@Table(name="orders_service", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_orders_service", name = "seq")
public class OrderService	
{
	private Long id;
	private Integer idCustomer;
	private Date dateStarted;
	private Date dateEnded;
	private Date dateEstimated;
	private Integer idUserCreated;
	private Integer idUserChanged;
	private Date momentCreated;
	private Date momentChanged;
	private Integer idOrderServiceStatus;
	private String code;
	
	public OrderService() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="id_customer")
	public Integer getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(Integer idCustomer) {
		this.idCustomer = idCustomer;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="date_started")
	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="date_ended")
	public Date getDateEnded() {
		return dateEnded;
	}

	public void setDateEnded(Date dateEnded) {
		this.dateEnded = dateEnded;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="date_estimated")
	public Date getDateEstimated() {
		return dateEstimated;
	}

	public void setDateEstimated(Date dateEstimated) {
		this.dateEstimated = dateEstimated;
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
	@Column(name="moment_created", insertable=false, updatable=false)
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

	@Column(name="id_order_service_status")
	public Integer getIdOrderServiceStatus() {
		return idOrderServiceStatus;
	}

	public void setIdOrderServiceStatus(Integer idOrderServiceStatus) {
		this.idOrderServiceStatus = idOrderServiceStatus;
	}

	@Column(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
