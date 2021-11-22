package com.vocal.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"payload", "signature", "signatureAlgo" } ) })
public class TrueCaller {

	@Id
	private long mobile;
	
	@Column(length = 500)
	private String payload;
	
	@Column(length = 500)
	private String signature;
	
	@Column(length = 25)
	private String signatureAlgo;
	
	private boolean loginSuccess;
	
	private int failedLogins;
	
	public TrueCaller() {
		super();
	}
	
	public TrueCaller(long mobile, String payload, String signature, String signatureAlgo, boolean loginSuccess) {
		super();
		this.mobile = mobile;
		this.payload = payload;
		this.signature = signature;
		this.signatureAlgo = signatureAlgo;
		this.loginSuccess = loginSuccess;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignatureAlgo() {
		return signatureAlgo;
	}

	public void setSignatureAlgo(String signatureAlgo) {
		this.signatureAlgo = signatureAlgo;
	}

	public boolean isLoginSuccess() {
		return loginSuccess;
	}

	public void setLoginSuccess(boolean loginSuccess) {
		this.loginSuccess = loginSuccess;
	}

	public int getFailedLogins() {
		return failedLogins;
	}

	public void setFailedLogins(int failedLogins) {
		this.failedLogins = failedLogins;
	}
}
