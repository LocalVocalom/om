package com.vocal.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ReferralLogic")
public class ReferralLogic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private long startCounter;
	
	private long endCounter;
	
	private long timeTrue;
	
	private long timeFalse;

	public long getStartCounter() {
		return startCounter;
	}

	public void setStartCounter(long startCounter) {
		this.startCounter = startCounter;
	}

	public long getEndCounter() {
		return endCounter;
	}

	public void setEndCounter(long endCounter) {
		this.endCounter = endCounter;
	}

	public long getTimeTrue() {
		return timeTrue;
	}

	public void setTimeTrue(long timeTrue) {
		this.timeTrue = timeTrue;
	}

	public long getTimeFalse() {
		return timeFalse;
	}

	public void setTimeFalse(long timeFalse) {
		this.timeFalse = timeFalse;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "\n[startCounter=" + startCounter + ", endCounter=" + endCounter
				+ ", timeTrue=" + timeTrue + ", timeFalse=" + timeFalse + ", id=" + id +  "]";
	}
	
	
}
