package com.vocal.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.AppLaunchService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;

@CrossOrigin
@RestController
public class AppLaunchController {

	@Autowired
	private AppLaunchService appLaunchService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLaunchController.class);

	@PostMapping(value = "/AppLaunch")
	public ResponseEntity<?> appLaunch(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp, 
			@RequestParam(value = CommonParams.LANGUAGE_ID, defaultValue = "1") int langId,
			@RequestParam(value = CommonParams.LANG_CHANGE, defaultValue="false") boolean langChange,
			@RequestParam(value = CommonParams.VERSION) String appVersion,
			HttpServletRequest request) { // throws UnsupportedEncodingException
		LOGGER.info("/AppLaunch userid = {}, otp = {}, langId = {}, appVersion={}", userId, otp, langId, appVersion);
		User user =  userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(appLaunchService.appLaunch(userId, langId, appVersion, langChange), HttpStatus.OK);
	}
	
	/*
	@GetMapping(value = "/ref")
	public void ref(
			@RequestParam(value = "id") long userId, 
			HttpServletRequest req, 
			HttpServletResponse res) {
		LOGGER.info("/ref userId={}", userId);
		String os = getUserAgentOS(req);
		try {
			if (os.equals("Android")) {
				res.sendRedirect("market://details?id=com.localvocal&referrer=utm_campaign%3DINVITE%26utm_source%3D" + userId + "%26utm_medium%3DSHARE%26utm_term%3DINVITE");
			} else {
				res.sendRedirect(
						"https://play.google.com/store/apps/details?id=com.localvocal&referrer=utm_campaign%3DINVITE%26utm_source%3DINVITE%26utm_medium%3DSHARE%26utm_term%3DINVITE");
			}
		} catch (IOException e) {
			LOGGER.error("Failed to redirect:{}", e.getMessage());
		}
	}
	
	private String getUserAgentOS(HttpServletRequest req) {
		String userAgentRawString = req.getHeader("User-Agent");
		LOGGER.debug("User-Agent header={}", userAgentRawString);
		String userAgent = userAgentRawString.toLowerCase();
		String os; 
		if(userAgent.indexOf("android") >= 0) {
			os = "Android";
		} else if(userAgent.indexOf("windows") >= 0) {
			os = "Windows";
		} else if(userAgent.indexOf("mac") >= 0) {
			os = "Mac";
		} else if(userAgent.indexOf("x11") >= 0) {
			os = "Unix";
		} else {
			os = "Unknown";
		}
		LOGGER.debug("extracted OS from User-Agent={}", os);
		return os;
	}
	*/

}
