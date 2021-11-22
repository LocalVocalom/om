package com.vocal.controllers;

import java.util.Date;

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

import com.vocal.entities.BeforeLogin;
import com.vocal.repos.jpa.BeforeLoginRepo;
import com.vocal.services.BlacklistedIpService;
import com.vocal.services.DbConfigService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Properties;
import com.vocal.utils.Utils;
import com.vocal.utils.UtmParameters;
import com.vocal.viewmodel.LoginResponseDto;
import com.vocal.viewmodel.LoginTypeDto;
import com.vocal.viewmodel.StatusDto;

@RestController
public class LoginControllerV1 {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BeforeLoginRepo beforeLoginRepo;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private BlacklistedIpService blacklistedIpService;

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginControllerV1.class);
	
	@PostMapping(value = "/v1/LoginType")
	public ResponseEntity<?> beforeLogin(
			@RequestParam(value = "deviceId", required = false, defaultValue = "") String deviceId,
			@RequestParam(value = "deviceType", required = false, defaultValue = "") String deviceType,
			@RequestParam(value = "deviceToken", required = false, defaultValue = "") String deviceToken,
			@RequestParam(value = "appVersion", required = false, defaultValue = "") String appVersion,
			@RequestParam(value = "timeZoneInSeconds", required = false, defaultValue = "0") long timeZoneInSeconds,
			@RequestParam(value = "androidVersion", required = false, defaultValue = "") String androidVersion,
			@RequestParam(value = "deviceName", required = false, defaultValue = "") String deviceName,
			@RequestParam(value = "networkType", required = false, defaultValue = "") String networkType,
			@RequestParam(value = "source", required = false, defaultValue = "") final String source,
			@RequestParam(value = CommonParams.UTM_SOURCE, required = false, defaultValue = "") String utmSource,
			@RequestParam(value = CommonParams.UTM_MEDIUM, required = false, defaultValue = "") String utmMedium,
			@RequestParam(value = CommonParams.UTM_TERM, required = false, defaultValue = "")  String utmTerm,
			@RequestParam(value = CommonParams.UTM_CAMPAIGN, required = false, defaultValue = "")  String utmCampaign,
			@RequestParam(value = "advertisingId", required = false, defaultValue = "") final String advertisingId,
			@RequestParam(value = "androidId", required = false, defaultValue = "") final String androidId,
			@RequestParam(value = "email", required = false, defaultValue = "") final String email,
			@RequestHeader(name = "User-Agent") String userAgent,
			HttpServletRequest request
			) { // "X-FORWARDED-FOR"
		LOGGER.info(
				"/v1/LoginType API Before Register for login options deviceId={},deviceType={},deviceToken={},appVersion={},timeZoneInSeconds={},androindVersion={},deviceName={},networkType={},source={},utm_source={},utm_medium={},utm_term={},advertisingId={},androidId={},email={}, userAgent={}",
				deviceId, deviceType, deviceToken, appVersion, timeZoneInSeconds, androidVersion, deviceName,
				networkType, source, utmSource, utmMedium, utmTerm, advertisingId, androidId, email, userAgent);
		
		// Check that the IP is not blocked if blocked don't give him loginOptions
		/*
		String clientIp = Utils.getClientIp(request);
		if(blacklistedIpService.isBlackedIp(clientIp)) {
			return new ResponseEntity<>("Not allowewd from this IP", HttpStatus.METHOD_NOT_ALLOWED);
		}
		*/
		
		// UTM PARAMETER EXTRACTION AND RE-ASSIGNMENT
		UtmParameters params = Utils.extractUtmParameters(utmSource, utmMedium, utmTerm, utmCampaign);
		utmSource = params.getUtmSource();
		utmMedium = params.getUtmMedium();
		utmTerm = params.getUtmTerm();
		utmCampaign = params.getUtmCampaign();
		
		// FIRST FIND BY DEVICE ID THEN TRY TO FIND BY ANDROID ID
		BeforeLogin beforeLogin = beforeLoginRepo.findByDeviceId(deviceId);
		if(beforeLogin == null) {
			beforeLogin = beforeLoginRepo.findByAndroidId(androidId);
		}
		
		if(beforeLogin == null) {
			// creating before login details
			beforeLogin = new BeforeLogin();
			beforeLogin.setAdvertisingId(advertisingId);
			beforeLogin.setAndroidId(androidId);
			beforeLogin.setAndroidVersion(androidVersion);
			beforeLogin.setAppVersion(appVersion);
			beforeLogin.setCreatedTime(new Date());
			beforeLogin.setDeviceId(deviceId);
			beforeLogin.setDeviceName(deviceName);
			beforeLogin.setDeviceToken(deviceToken);
			beforeLogin.setDeviceType(deviceType);
			beforeLogin.setEmail(email);
			beforeLogin.setIp(Utils.getClientIp(request));
			beforeLogin.setNetworkType(networkType);
			beforeLogin.setSource(source);
			beforeLogin.setTimeZoneInSeconds(String.valueOf(timeZoneInSeconds));
			beforeLogin.setUpdatedTime(new Date());
			beforeLogin.setUtmMedium(utmMedium);
			beforeLogin.setUtmSource(utmSource);
			beforeLogin.setUtmTerm(utmTerm);
			beforeLogin.setRetries(1);
		} else {
			// updating updatedTime and incrementing the counter
			beforeLogin.setUpdatedTime(new Date());
			beforeLogin.setRetries(beforeLogin.getRetries() + 1);
		}
		beforeLoginRepo.save(beforeLogin);
		LoginTypeDto loginOptions = userService.getLoginOptions();
		LOGGER.info("got login options for deviceId={}, response={}", deviceId, loginOptions);
		return ResponseEntity.ok(loginOptions);
	}

	@PostMapping(value = "/sendOTP")
	public ResponseEntity<?> sendOtp(
			@RequestParam(name = CommonParams.MOBILE) String mobileNum,
			HttpServletRequest request) {
		LOGGER.info("/sendOTP mobileNum={}, ip={}", mobileNum, Utils.getClientIp(request)); // mobileNum changed to String from long
		
		// sent otp only and only if mobile login is enabled
		boolean isMobileLoginEnabled = dbConfigService.getBooleanProperty(Properties.IS_MOBILE_NUMBER_LOGIN_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_MOBILE_NUMBER_LOGIN_ENABLED.getDefaultValue()));
		if(!isMobileLoginEnabled) {
			LOGGER.error("malicious activity with mobile number = {}", mobileNum);
			return new ResponseEntity<>("Not Allowed", HttpStatus.METHOD_NOT_ALLOWED);
		}
		
		long mobileNumber = 0l;
		try {
			mobileNumber = Long.parseLong(mobileNum);
			String tempMobile=mobileNum;
			if( mobileNum.length() > 10) {
				tempMobile = mobileNum.substring(mobileNum.length() - 10);
				mobileNumber = Long.parseLong(tempMobile);
				LOGGER.info("mobile after extracting={}", mobileNumber);
			}
			
			int firstDigit = Integer.parseInt(tempMobile.substring(0, 1));
			if(firstDigit < 6) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch(NumberFormatException nfe) {
			LOGGER.error("failed to parse mobile number,mobile={}", mobileNumber);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(new StatusDto(userService.sendOtp(mobileNumber)));
	}

	@PostMapping(value = "/v1/Login")
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
			@RequestParam(value = CommonParams.MOBILE, defaultValue = "0") String mobile,
			@RequestParam(value = CommonParams.OTP, defaultValue = "") String otp,
			@RequestParam(value = CommonParams.PAYLOAD, defaultValue = "N/A") String payload,
			@RequestParam(value = CommonParams.SIGNATURE, defaultValue = "N/A") String signedString,
			@RequestParam(value = CommonParams.SIGNATURE_ALGO, defaultValue = "N/A") String signatureAlgo,
			@RequestParam(value = "androidId", defaultValue = "") String androidId,
			@RequestHeader(name = "User-Agent") String userAgent,
			HttpServletRequest request) {
		ip = Utils.getClientIp(request);
		long mobileNumber = 0l;
		try {
			mobileNumber = Long.parseLong(mobile);
			if( mobile.length() > 10) {
				String tempMobile = mobile.substring(mobile.length() - 10);
				mobileNumber = Long.parseLong(tempMobile);
				LOGGER.info("From trueCaller, mobile after extracting={}", mobileNumber);
				// Checking first digit of mobile number, should be greater than or equal to 6
				int firstDigit = Integer.parseInt(tempMobile.substring(0, 1));
				if(firstDigit < 6) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
		} catch(NumberFormatException nfe) {
			LOGGER.error("failed to parse mobile number,mobile={}", mobile);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		LOGGER.info(
				"/v1/Login email =	{},	token = {}, ip = {}, deviceToken = {}, loginType = {} , utm_source = {}, utm_medium = {}, utm_term = {},  utm_campagion = {}, appVersion = {},  deviceid = {}, name = {}, profilepicURL = {}, androidVersion = {}, deviceName = {} , make = {}, model = {}, timeOfDevice = {}, mobile={}, otp={}, payload={}, signedString={}, signatureAlgo={}, androidId={}, userAgent={}",
				email, token, ip, deviceToken, loginType, utmSource, utmMedium, utmTerm, utmCampaign, appVersion,
				deviceId, name, profilePicUrl, androidVersion, deviceName, make, model, timeOfDevice, mobileNumber, otp,
				payload, signedString, signatureAlgo, androidId, userAgent);
		

		LoginResponseDto loginResponse = userService.handleUserLoginV1(email, token, ip, deviceToken, loginType,
				utmSource, utmMedium, utmTerm, utmCampaign, appVersion, deviceId, androidId, name, profilePicUrl, androidVersion,
				deviceName, make, model, timeOfDevice, mobileNumber, otp, payload, signedString, signatureAlgo);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

}
