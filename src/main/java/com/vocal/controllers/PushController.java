package com.vocal.controllers;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.PushMangerService;
import com.vocal.services.SendMailService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;

@RestController
public class PushController {
	
	@Autowired
	private PushMangerService pushMangerService;
	
	@Autowired
	private SendMailService sendMailService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PushController.class);
	
	@PostMapping(value = "/sendPush")
	public ResponseEntity<?> sendPush(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP, required = false) String otp, 
			@RequestParam(name = CommonParams.TYPE) long type,
			@RequestParam(name = CommonParams.TEXT) String text, 
			@RequestParam(name = CommonParams.ACTION_URL) String actionUrl,
			@RequestParam(name = "imageURL") String imageUrl,
			HttpServletRequest request) {
		LOGGER.info("/sendPush type = {}, text = {}, actionUrl = {}, imageUrl = {}", 
				type, text, actionUrl, imageUrl);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return new ResponseEntity<>(pushMangerService.sendPush(userId, text, imageUrl, actionUrl, type), HttpStatus.OK);
	}
	
	@PostMapping(value = "/sendTopic")
	public ResponseEntity<?> sendTopic(
			@RequestParam(name = "ids") List<Long> ids) {
		LOGGER.info("/sendTopic newsId={}", ids);
		if(ids == null || ids.size() == 0) {
			LOGGER.error("empty or null list");
			return new ResponseEntity<>("null/empty list", HttpStatus.BAD_REQUEST);
		}
		try {
			return ResponseEntity.ok(pushMangerService.sendTopic(ids));
		} catch (Exception e) {
			LOGGER.error("exception=", e.getMessage());
			return null;
		}
	}
	
	@PostMapping(value = "/sendMail")
	public ResponseEntity<?> sendMail(
			@RequestParam("to") String to,
			@RequestParam("from") String from,
			@RequestParam("name") String name,
			@RequestParam("msg") String msg
			) {
		LOGGER.info("/sendMail to={}, from={}, name={}, msg={}", to, from, name, msg);
		try  {
			sendMailService.sendFeedback(to, from, name, msg);
			LOGGER.debug("sent mail to={}", to);
		} catch (Exception ex) {
			LOGGER.error("exception when sending mail, exp=", ex.getMessage());
		}
		return ResponseEntity.ok("sent mail");
	}
	
}
