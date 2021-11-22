package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name = "user_source", indexes = {@Index( columnList = ("ip"), name = "ip", unique = true)})
public class UserSource implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "userId")
	private long userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdTime")
	private Date createdTime;

	private String source;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedTime")
	private Date updatedTime;

	@Column(name = "utmCampaign")
	private String utmCampaign;

	@Column(name = "utmContent")
	private String utmContent;

	@Column(name = "utmMedium")
	private String utmMedium;

	@Column(name = "utmSource")
	private String utmSource;

	@Column(name = "utmTerm")
	private String utmTerm;
	
	//@Index(name = "ip")
	private String ip;
	
	@Column(name = "device_id")
	private String deviceId;
	
	private int refStatus;
	
	public UserSource() {
		super();
	}

	public UserSource(long userId, String source, String utmCampaign, String utmMedium, String utmSource, String utmTerm, String ip, String deviceId) {
		super();
		this.userId = userId;
		this.source = source;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
		this.utmSource = utmSource;
		this.utmTerm = utmTerm;
		this.ip = ip;
		this.deviceId = deviceId;
	}

	@PrePersist
	protected void insertDate() {
		createdTime = new Date();
		updatedTime = new Date();
	}
	
	@PreUpdate
	protected void updateDate() {
		updatedTime = new Date();
	}
	

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUtmCampaign() {
		return this.utmCampaign;
	}

	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}

	public String getUtmContent() {
		return this.utmContent;
	}

	public void setUtmContent(String utmContent) {
		this.utmContent = utmContent;
	}

	public String getUtmMedium() {
		return this.utmMedium;
	}

	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}

	public String getUtmSource() {
		return this.utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public String getUtmTerm() {
		return this.utmTerm;
	}

	public void setUtmTerm(String utmTerm) {
		this.utmTerm = utmTerm;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getRefStatus() {
		return refStatus;
	}

	public void setRefStatus(int refStatus) {
		this.refStatus = refStatus;
	}
	
}