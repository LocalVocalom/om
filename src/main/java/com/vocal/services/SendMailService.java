package com.vocal.services;

public interface SendMailService {
	
	public void sendFeedback(String to, String from, String name, String msg);
	
}