package com.vocal.controllers;

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
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;

@RestController
public class UpdateDeviceTokenController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	
	private static Logger LOGGER = LoggerFactory.getLogger(UpdateDeviceTokenController.class);
	
	@PostMapping(value = "/UpdateDeviceToken")
	public ResponseEntity<?> updateDeviceToken(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = "ID") String androidId,
			@RequestParam(value = "DeviceID") String deviceId,
			@RequestParam(value = CommonParams.TOKEN) String token,
			HttpServletRequest request
			) {
		LOGGER.info("/UpdateDeviceToken userId={}, otp={}, androidId = {}, deviceId = {}, token = {}", userId, otp, androidId, deviceId, token);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return new ResponseEntity<>(userService.handleDeviceTokenUpdation(userId, deviceId, androidId, token), HttpStatus.OK);
	}

}
