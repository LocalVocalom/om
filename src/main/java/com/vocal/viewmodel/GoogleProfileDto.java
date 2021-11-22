package com.vocal.viewmodel;

public class GoogleProfileDto {
	
	private String name;
	
	private String email;
	
	private String email_verified;
	
	private String azp;
	
	private String picture;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail_verified() {
		return email_verified;
	}

	public void setEmail_verified(String email_verified) {
		this.email_verified = email_verified;
	}

	public String getAzp() {
		return azp;
	}

	public void setAzp(String azp) {
		this.azp = azp;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return "GoogleProfileDto [name=" + name + ", email=" + email + ", email_verified=" + email_verified + ", azp="
				+ azp + ", picture=" + picture + "]";
	}

}
