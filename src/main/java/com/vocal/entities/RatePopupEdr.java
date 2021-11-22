package com.vocal.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RatePopupEdr {

	@Id
	private long userId;
	
	private Date createdTime;
	
	private Date updatedTime;
	
	private int popupCount;
	
	private int sessionCounter;

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

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getPopupCount() {
		return popupCount;
	}

	public void setPopupCount(int popupCount) {
		this.popupCount = popupCount;
	}

	public int getSessionCounter() {
		return sessionCounter;
	}

	public void setSessionCounter(int sessionCounter) {
		this.sessionCounter = sessionCounter;
	}
	
}
