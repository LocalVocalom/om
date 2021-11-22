package com.vocal.services.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.vocal.services.SendMailService;

@Service
public class SendMailServiceImpl implements SendMailService {
	
	private JavaMailSenderImpl mailSender;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SendMailServiceImpl.class);
	
	public SendMailServiceImpl(Environment environment) {
		mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(environment.getProperty("spring.mail.host"));
		mailSender.setPort(Integer.parseInt(environment.getProperty("spring.mail.port")));
		mailSender.setUsername(environment.getProperty("spring.mail.username"));
		mailSender.setPassword(environment.getProperty("spring.mail.password"));
		
		// Added additionally for GMAIL SMTP
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
	}

	@Override
	public void sendFeedback(String to, String from, String name, String msg) {
		LOGGER.debug("to={}, from={}, name={}, msg", to, from, name, msg);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(from);
		
		message.setSubject("New Feedback from " + name);
		message.setText(msg);
		
		this.mailSender.send(message);
	}
}