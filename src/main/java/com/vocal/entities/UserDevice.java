package com.vocal.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user_device")
public class UserDevice implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "userId")
	private long userId;
	
	@Column(name = "createdTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "device_id")
	private String deviceId;
	
	@Column(name = "device_name")
	private String deviceName;
	
	private String make;
	
	private String model;
	
	@Column(name = "time_of_device")
	private String timeOfDevice;
	
	@Column(name = "android_id")
	private String androidId;
	
	@Column(name = "device_token")
	private String deviceToken;
	
	private boolean active;

	public UserDevice() {
		super();
	}
	
	public UserDevice(long userId, String deviceId, String deviceName, String make, String model,
			String timeOfDevice, String deviceToken) {
		super();
		this.userId = userId;
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.make = make;
		this.model = model;
		this.timeOfDevice = timeOfDevice;
		this.deviceToken = deviceToken;
	}

	@PrePersist
	public void createdTime()
	{
		this.createdTime = new Date();
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTimeOfDevice() {
		return timeOfDevice;
	}

	public void setTimeOfDevice(String timeOfDevice) {
		this.timeOfDevice = timeOfDevice;
	}

	public String getAndroidId() {
		return androidId;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "UserDevice [userId=" + userId + ", createdTime=" + createdTime + ", deviceId=" + deviceId
				+ ", deviceName=" + deviceName + ", make=" + make + ", model=" + model + ", timeOfDevice="
				+ timeOfDevice + ", androidId=" + androidId + ", deviceToken=" + deviceToken + "]";
	}
	
}