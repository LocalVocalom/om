package com.vocal.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAccount {
	@Id
	private long userId;
	
	private double currentBalance;
	
	private double dailyEarning;
	
	private double allowedAmount;
	
	private long inviteCounter;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public double getDailyEarning() {
		return dailyEarning;
	}

	public void setDailyEarning(double dailyEarning) {
		this.dailyEarning = dailyEarning;
	}

	public double getAllowedAmount() {
		return allowedAmount;
	}

	public void setAllowedAmount(double allowedAmount) {
		this.allowedAmount = allowedAmount;
	}

	public long getInviteCounter() {
		return inviteCounter;
	}

	public void setInviteCounter(long inviteCounter) {
		this.inviteCounter = inviteCounter;
	}
}
