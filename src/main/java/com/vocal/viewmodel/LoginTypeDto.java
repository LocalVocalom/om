package com.vocal.viewmodel;

public class LoginTypeDto {
	
	private String facebook;
	
	private String google;
	
	private String trueCaller;
	
	private String mobile;

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getGoogle() {
		return google;
	}

	public void setGoogle(String google) {
		this.google = google;
	}

	public String getTrueCaller() {
		return trueCaller;
	}

	public void setTrueCaller(String trueCaller) {
		this.trueCaller = trueCaller;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "LoginTypeDto [facebook=" + facebook + ", google=" + google + ", trueCaller=" + trueCaller + ", mobile="
				+ mobile + "]";
	}
}
