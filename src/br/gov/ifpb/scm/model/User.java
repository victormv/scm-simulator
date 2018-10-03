package br.gov.ifpb.scm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="users", schema="dashboard")
@SequenceGenerator(sequenceName="dashboard.seq_users", name = "seq")
public class User {
	
	private Long id;
	private String name;
	private String email;
	private String password;
	private Integer idUserCreated;
	private Integer idArea;
	private boolean blocked;
	private String matriculation;
	private Long idAccessProfile;
	
	public User() {}

	@Id
	@Column(name="id")
	@GeneratedValue(generator="seq", strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="id_user_created")
	public Integer getIdUserCreated() {
		return idUserCreated;
	}

	public void setIdUserCreated(Integer idUserCreated) {
		this.idUserCreated = idUserCreated;
	}

	@Column(name="id_area")
	public Integer getIdArea() {
		return idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	@Column(name="blocked")
	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	@Column(name="matriculation")
	public String getMatriculation() {
		return matriculation;
	}

	public void setMatriculation(String matriculation) {
		this.matriculation = matriculation;
	}

	@Column(name="id_access_profile")
	public Long getIdAccessProfile() {
		return idAccessProfile;
	}

	public void setIdAccessProfile(Long idAccessProfile) {
		this.idAccessProfile = idAccessProfile;
	}
}
