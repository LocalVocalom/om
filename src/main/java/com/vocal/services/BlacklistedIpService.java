package com.vocal.services;

public interface BlacklistedIpService {
	
	public void refresh();
	
	boolean isBlackedIp(String ip);
	
}
