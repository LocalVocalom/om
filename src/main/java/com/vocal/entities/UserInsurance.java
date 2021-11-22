package com.vocal.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "User_Insurence")
public class UserInsurance implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "userid", unique = true, nullable = false)
	private long userId;
	
	@Column(length = 250, name = "name")
	private String name;
	
	@Column(length = 250, name = "nominee")
	private String nominee;
	
	@Temporal(TemporalType.DATE)
	private Date dob;
	
	private String email;
	
	@Column(name = "profile_pic")
	private String profilePic;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_time")
	private Date dateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "insurence_start_date")
	private Date insurenceStartDate;
	
	private String status;
	
	@Column(name = "gender", nullable = true)
	private String gender;
	
	public UserInsurance() {
		super();
	}

	public UserInsurance(long userId, String name, String nominee, Date dob, String email, String profilePic,
			Date dateTime, Date insurenceStartDate, String status) {
		super();
		this.userId = userId;
		this.name = name;
		this.nominee = nominee;
		this.dob = dob;
		this.email = email;
		this.profilePic = profilePic;
		this.dateTime = dateTime;
		this.insurenceStartDate = insurenceStartDate;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNominee() {
		return nominee;
	}

	public void setNominee(String nominee) {
		this.nominee = nominee;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getInsurenceStartDate() {
		return insurenceStartDate;
	}

	public void setInsurenceStartDate(Date insurenceStartDate) {
		this.insurenceStartDate = insurenceStartDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
