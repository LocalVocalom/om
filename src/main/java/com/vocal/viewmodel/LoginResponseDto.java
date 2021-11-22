package com.vocal.viewmodel;

import java.util.List;

public class LoginResponseDto {
	
	private long userid;
	
	private String otp;
	
	//private List<NewsDtoInnerAll> news;
	
	private List<Object> news;
	
	private boolean isKycVerified;
	
	public LoginResponseDto() {
		super();
	}

	public LoginResponseDto(long userid, String otp, List<Object> news, boolean isKycVerified) {
		super();
		this.userid = userid;
		this.otp = otp;
		this.news = news;
		this.isKycVerified = isKycVerified;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public List<Object> getNews() {
		return news;
	}

	public void setNews(List<Object> news) {
		this.news = news;
	}

	public boolean isKycVerified() {
		return isKycVerified;
	}

	public void setKycVerified(boolean isKycVerified) {
		this.isKycVerified = isKycVerified;
	}
	
}
