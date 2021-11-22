package com.vocal.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class UserRedeem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private double amount;
	
	private String circle;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	private Long userId;
	
	private Long msisdn;
	
	private String operator;
	
	private String status;
	
	private String type;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	private Long redeemType;
	
	private String vender;
	
	private double postBalance;
	
	@Column(name = "trans_id")
	private String transId;
	
	@Column(name = "trans_id_vokal")
	private String transIdVokal;
	
	private Double fee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(Long msisdn) {
		this.msisdn = msisdn;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getRedeemType() {
		return redeemType;
	}

	public void setRedeemType(Long redeemType) {
		this.redeemType = redeemType;
	}

	public String getVender() {
		return vender;
	}

	public void setVender(String vender) {
		this.vender = vender;
	}

	public double getPostBalance() {
		return postBalance;
	}

	public void setPostBalance(double postBalance) {
		this.postBalance = postBalance;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransIdVokal() {
		return transIdVokal;
	}

	public void setTransIdVokal(String transIdVokal) {
		this.transIdVokal = transIdVokal;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}
	
}
