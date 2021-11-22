package com.vocal.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserBlackList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private long userId;
	
	private int blackListCounter;
	
	private int type;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	private String remarks;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getBlackListCounter() {
		return blackListCounter;
	}

	public void setBlackListCounter(int blackListCounter) {
		this.blackListCounter = blackListCounter;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
