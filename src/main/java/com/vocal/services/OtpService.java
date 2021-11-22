package com.vocal.services;

public interface OtpService {
	
	public String generateOtp(String key);

	public String getOtp(String key);

	public void clearOtp(String key);

}
