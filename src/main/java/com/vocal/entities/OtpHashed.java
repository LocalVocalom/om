package com.vocal.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OtpHashed {
	
	@Id
	private String mobileNum;
	
	private String otp;
	
	private int mismatchedNums;

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public int getMismatchedNums() {
		return mismatchedNums;
	}

	public void setMismatchedNums(int mismatchedNums) {
		this.mismatchedNums = mismatchedNums;
	}
	
}
