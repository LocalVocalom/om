package com.vocal.viewmodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SummaryDto {

	@JsonFormat(pattern="yyyy/MM/dd HH:mm")
	private Date dateTime;
	
	private String remark;
	
	private double amount;
	
	public SummaryDto() {
		super();
	}

	public SummaryDto(Date dateTime, String remark, double amount) {
		super();
		this.dateTime = dateTime;
		this.remark = remark;
		this.amount = amount;
	}

	public Date getDateTime() {
		return dateTime;
	}

	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
