package com.vocal.controllers;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Utils;
import com.vocal.utils.UtmParameters;
import com.vocal.viewmodel.InsuranceDto;
import com.vocal.viewmodel.LoginResponseDto;

@RestController
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/LoginType")
	public ResponseEntity<?> loginType(HttpServletRequest request) {
		LOGGER.info("/LoginType ");
		return new ResponseEntity<>(userService.getLoginOptions(), HttpStatus.OK);
	}

	@PostMapping(value = "/Login")
	public ResponseEntity<?> login(
			@RequestParam(value = CommonParams.EMAIL) String email,
			@RequestParam(value = CommonParams.TOKEN) String token,
			@RequestParam(value = CommonParams.IP) String ip,
			@RequestParam(value = CommonParams.DEVICE_TOKEN) String deviceToken,
			@RequestParam(value = CommonParams.LOGIN_TYPE) String loginType, 
			@RequestParam(value = CommonParams.UTM_SOURCE) String utmSource,
			@RequestParam(value = CommonParams.UTM_MEDIUM) String utmMedium, 
			@RequestParam(value = CommonParams.UTM_TERM) String utmTerm,
			@RequestParam(value = CommonParams.UTM_CAMPAIGN) String utmCampaign,
			@RequestParam(value = CommonParams.APP_VERSION) String appVersion, 
			@RequestParam(value = "deviceid") String deviceId,
			@RequestParam(value = CommonParams.NAME) String name, 
			@RequestParam(value = "profilepicURL") String profilePicUrl,
			@RequestParam(value = CommonParams.ANDROID_VERSION) String androidVersion,
			@RequestParam(value = CommonParams.DEVICE_NAME) String deviceName, 
			@RequestParam(value = CommonParams.MAKE) String make,
			@RequestParam(value = CommonParams.MODEL) String model, 
			@RequestParam(value = CommonParams.TIME_OF_DEVICE) String timeOfDevice,
			@RequestHeader(name = "User-Agent") String userAgent,
			HttpServletRequest request) {
		ip =  Utils.getClientIp(request);
		LOGGER.info(
				"/Login email =	{},	token = {}, ip = {}, deviceToken = {}, loginType = {} , utm_source = {}, utm_medium = {}, utm_term = {},  utm_campagion = {}, appVersion = {},  deviceid = {}, name = {}, profilepicURL = {}, androidVersion = {}, deviceName = {} , make = {}, model = {}, timeOfDevice = {}, userAgent={}",
				email, token, ip, deviceToken, loginType, utmSource, utmMedium, utmTerm, utmCampaign, appVersion, deviceId,
				name, profilePicUrl, androidVersion, deviceName, make, model, timeOfDevice, userAgent);
		// TODO: To be completely removed later
		if(appVersion.equals("1") && loginType.equals("facebook")) {
			appVersion = "1.1";
			LOGGER.info("Updating appVersion to->{}", appVersion);
		}
		
		// UTM PARAMETER EXTRACTION AND RE-ASSIGNMENT
		UtmParameters utmParams = Utils.extractUtmParameters(utmSource, utmMedium, utmTerm, utmCampaign);
		utmSource = utmParams.getUtmSource();
		utmMedium = utmParams.getUtmMedium();
		utmTerm = utmParams.getUtmTerm();
		utmCampaign = utmParams.getUtmCampaign();
		
		LoginResponseDto loginResponse = userService.handleUserLogin(email, token, ip, deviceToken, loginType, utmSource,
				utmMedium, utmTerm, utmCampaign, appVersion, deviceId, name, profilePicUrl, androidVersion, deviceName,
				make, model, timeOfDevice);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/getInsurence")
	ResponseEntity<?> getInsurance(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int languageId,
			HttpServletRequest request
			) {
		LOGGER.info("/getInsurence userId={}, otp={}", userId, otp);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		InsuranceDto insuranceDto = userService.getInsurance(userId, languageId);
		return ResponseEntity.ok(insuranceDto);
	}
	
}
