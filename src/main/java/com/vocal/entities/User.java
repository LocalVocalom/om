package com.vocal.entities;

import java.io.Serializable;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "userId",length = 20)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(length = 12)
	private String mobile;

	@Column(name = "emailId", unique = true)
	//@Index(name = "emailId")
	private String emailId;

	@Column(length = 10)
	private String otp;

	@Column(name= "otpVerify", length = 2)
	private Integer otpVerify;

	@Column(length = 2)
	private Integer status;

	@Column(name = "appVersion")
	private String appVersion;

	@Column(length = 20)
	private Long followers;

	@Column(length = 20)
	private Long following;

	@Column(length = 10, name = "post_published")
	private Integer postPublished;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdTime")
	private Date createdTime;

	// The following four properties were defined with CHARACTER SET utf8 COLLATE utf8_bin
	@Column(name = "State")
	private String state;

	@Column(name = "Dist")
	private String dist;

	@Column(name = "Block")
	private String block;

	@Column(name = "GramSabha")
	private String gramSabha;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedTime")
	private Date updatedTime;
	
	private Integer redeemCount;
	
	private int uploadStatus;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastUpdatedTime")
	private Date lastUpdatedTime;
	
	
	@PrePersist
	public void autoSetCreatedTime() {
		this.createdTime = new Date();
		this.updatedTime = new Date();
		//this.lastUpdatedTime = new Date();
	}
	
	@PreUpdate
	public void setUpdatedTime() {
		this.updatedTime = new Date();
	}
	

	public User() {
		super();
	}
	
	public User(Builder builder) {
		this.mobile = builder.mobile;
		this.emailId = builder.emailId;
		this.otp = builder.otp;
		this.otpVerify = builder.otpVerify;
		this.status = builder.status;
		this.appVersion = builder.appVersion;
		this.followers = builder.followers;
		this.following = builder.following;
		this.postPublished = builder.postPublished;
		this.state = builder.state;
		this.dist = builder.dist;
		this.block = builder.block;
		this.gramSabha = builder.gramSabha;
		this.redeemCount = builder.redeemCount;
	}

	
	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Integer getOtpVerify() {
		return otpVerify;
	}

	public void setOtpVerify(Integer otpVerify) {
		this.otpVerify = otpVerify;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Long getFollowers() {
		return followers;
	}

	public void setFollowers(Long followers) {
		this.followers = followers;
	}

	public Long getFollowing() {
		return following;
	}

	public void setFollowing(Long following) {
		this.following = following;
	}

	public Integer getPostPublished() {
		return postPublished;
	}

	public void setPostPublished(Integer postPublished) {
		this.postPublished = postPublished;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getGramSabha() {
		return gramSabha;
	}

	public void setGramSabha(String gramSabha) {
		this.gramSabha = gramSabha;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getRedeemCount() {
		return redeemCount;
	}

	public void setRedeemCount(Integer redeemCount) {
		this.redeemCount = redeemCount;
	}

	public int getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(int uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", mobile=" + mobile + ", emailId=" + emailId + ", otp=" + otp
				+ ", otpVerify=" + otpVerify + ", status=" + status + ", appVersion=" + appVersion + ", followers="
				+ followers + ", following=" + following + ", postPublished=" + postPublished + ", createdTime="
				+ createdTime + ", state=" + state + ", dist=" + dist + ", block=" + block + ", gramSabha=" + gramSabha
				+ ", updatedTime=" + updatedTime + "]";
	}
	
	public static class Builder {
		private String mobile;
		private String emailId;
		private String otp;
		private Integer otpVerify;
		private Integer status;
		private String appVersion;
		private Long followers;
		private Long following;
		private Integer postPublished;
		private String state;
		private String dist;
		private String block;
		private String gramSabha;
		private Integer redeemCount;
		
		public Builder mobile(String mobile) {
			this.mobile = mobile;
			return this;
		}
		
		public Builder emailId(String emailId) {
			this.emailId = emailId;
			return this;
		}
		
		public Builder otp(String otp) {
			this.otp = otp;
			return this;
		}
		
		public Builder otpVerify(int otpVerify) {
			this.otpVerify = otpVerify;
			return this;
		}
		
		public Builder status(int status) {
			this.status = status;
			return this;
		}
		
		public Builder appVersion(String appVersion) {
			this.appVersion = appVersion;
			return this;
		}
		
		public Builder followers(long followers){
			this.followers = followers;
			return this;
		}
		
		public Builder following(long following) {
			this.following = following;
			return this;
		}
		
		public Builder postPublished(int postPublished) {
			this.postPublished = postPublished;
			return this;
		}
		
		public Builder state(String state) {
			this.state = state;
			return this;
		}
		
		public Builder dist(String dist) {
			this.dist = dist;
			return this;
		}
		
		public Builder block(String block) {
			this.block = block;
			return this;
		}
		
		public Builder gramSabha(String gramSabha) {
			this.gramSabha = gramSabha;
			return this;
		}
		
		public Builder redeemCount(int redeemCount) {
			this.redeemCount = redeemCount;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
	}

}