package com.vocal.viewmodel;

import com.vocal.entities.UserProfile.LoginMedium;

public class SocialResponse {
	
	private String socialName;
	private String socialPicUrl;
	private String socialId;
	private boolean isEmailVerified;
	private LoginMedium loginMedium;
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LoginMedium getLoginMedium() {
		return loginMedium;
	}
	public void setLoginMedium(LoginMedium loginMedium) {
		this.loginMedium = loginMedium;
	}
	public String getSocialName() {
		return socialName;
	}
	public void setSocialName(String socialName) {
		this.socialName = socialName;
	}
	public String getSocialPicUrl() {
		return socialPicUrl;
	}
	public void setSocialPicUrl(String socialPicUrl) {
		this.socialPicUrl = socialPicUrl;
	}
	public String getSocialId() {
		return socialId;
	}
	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}
	public boolean isEmailVerified() {
		return isEmailVerified;
	}
	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}
	@Override
	public String toString() {
		return "SocialResponse [socialName=" + socialName + ", socialPicUrl=" + socialPicUrl + ", socialId=" + socialId
				+ ", isEmailVerified=" + isEmailVerified + "]";
	}
}
