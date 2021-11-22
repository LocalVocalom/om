package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserQuality implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private long userId;
	
	private long activeCount;
	
	private long notRegCount;
	
	private long totalCount; 
	
	private double retainPercent;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(long activeCount) {
		this.activeCount = activeCount;
	}

	public long getNotRegCount() {
		return notRegCount;
	}

	public void setNotRegCount(long notRegCount) {
		this.notRegCount = notRegCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public double getRetainPercent() {
		return retainPercent;
	}

	public void setRetainPercent(double retainPercent) {
		this.retainPercent = retainPercent;
	}
	
}
