package com.vocal.controllers;

import java.util.Date;
import java.util.UUID;

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

import com.vocal.entities.PayTmDetails;
import com.vocal.entities.User;
import com.vocal.entities.UserAccount;
import com.vocal.entities.UserDevice;
import com.vocal.entities.UserRedeem;
import com.vocal.repos.jpa.PayTmDetailsRepo;
import com.vocal.repos.jpa.UserAccountRepo;
import com.vocal.repos.jpa.UserBlackListRepo;
import com.vocal.repos.jpa.UserDeviceRepo;
import com.vocal.repos.jpa.UserRedeemRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.BlacklistedIpService;
import com.vocal.services.DbConfigService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Properties;
import com.vocal.utils.Utils;
import com.vocal.viewmodel.UrlAndActionDto;

@RestController
public class RedeemController {

	@Autowired
	private PayTmDetailsRepo payTmDetailsRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private UserRedeemRepo userRedeemRepo;

	@Autowired
	private DbConfigService dbConfigService;

	@Autowired
	private UserDeviceRepo userDeviceRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BlacklistedIpService blacklistedIpService;
	
	@Autowired
	private UserBlackListRepo userBlackListRepo;
	

	private static final Logger LOGGER = LoggerFactory.getLogger(RedeemController.class);

	@PostMapping("/user/redeemPaytmId")
	public ResponseEntity<?> redeem(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = "msisdn") String msisdn,
			@RequestParam(name = "type") String type, 
			@RequestParam(name = "deviceId") String deviceId,
			@RequestHeader(name = "User-Agent") String userAgent, 
			HttpServletRequest request) {
		String ip = Utils.getClientIp(request);
		LOGGER.info("/user/redeemPaytmId userId={}, otp={}, msisdn={}, type={}, deviceId={}, userAgent={}, ip={}", userId, otp,
				msisdn, type, deviceId, userAgent, ip);
		UrlAndActionDto dto = new UrlAndActionDto();
		String vocalPayUrl = dbConfigService.getProperty(Properties.VOCAL_PAYTM_PAY.getProperty(),
				Properties.VOCAL_PAYTM_PAY.getDefaultValue());
		dto.setUrl(vocalPayUrl+UUID.randomUUID().toString());
		dto.setAction("web");
		dto.setMessage("Success");
		
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			// throw new AuthorizationException("");
			return ResponseEntity.ok(dto);
		}
		
		boolean isBlackListedUser = userBlackListRepo.existsUserBlackListByUserId(userId);
		if(isBlackListedUser) {
			LOGGER.error("blacklisted user with userId={} is trying to redeem from ip={}", userId, ip);
			// return new ResponseEntity<>(dto, HttpStatus.METHOD_NOT_ALLOWED);
			return ResponseEntity.ok(dto);
		}
		
		boolean isBlackedIp = blacklistedIpService.isBlackedIp(ip);
		if(isBlackedIp) {
			LOGGER.error("blacklisted ip={} is trying to redeem for userId={}", ip, userId);
			// return new ResponseEntity<>(dto, HttpStatus.METHOD_NOT_ALLOWED);
			return ResponseEntity.ok(dto);
		}
		
		
		boolean isIssueInRedeem = dbConfigService.getBooleanProperty(Properties.IS_ISSUE_IN_REDEEM.getProperty(), Boolean.valueOf(Properties.IS_ISSUE_IN_REDEEM.getDefaultValue()));
		if(isIssueInRedeem) {
			LOGGER.error("There is issue in redeem");
			dto.setUrl(""); // url in case of redeem disabled
			dto.setAction(""); // no action when redeem disabled
			String redeemDisableMsg = dbConfigService.getProperty(Properties.REDEEM_DISABLED_MSG.getProperty(), Properties.REDEEM_DISABLED_MSG.getDefaultValue());
			dto.setMessage(redeemDisableMsg);
			return ResponseEntity.ok(dto);
		}

		LOGGER.info("Checking whether the request is being requested in secured way");
		if (!request.isSecure()) {
			LOGGER.error("The request was not secured..");
			// dto.setMessage("Unsecured Access");
			return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
		}

		// Checking redeem timing
		UserRedeem userRedeem = userRedeemRepo.findFirstByUserIdAndStatusOrderByCreatedTimeDesc(userId, "SUCCESS");
		Integer allowedMinutesDiff = dbConfigService.getIntProperty("REDEEM_DIFF_MINUTES", 0);

		// or replace with redeemCounter of user
		Integer redeemCount = user.getRedeemCount();
		LOGGER.info("Checking time difference between last redeem and this");
		if (redeemCount != null && redeemCount != 0) {
			// already has redeemed, check the time difference between this redeem and the
			// previous
			Date createdTime = userRedeem.getCreatedTime();
			long differenceInMinutes = (System.currentTimeMillis() - createdTime.getTime()) / (1000 * 60);
			if (differenceInMinutes < allowedMinutesDiff) {
				// dto.setMessage("Frequent Redeem Not Allowed, try after " + allowedMinutesDiff + " minutes");
				LOGGER.error("Frequent Redeem Not Allowed, try after " + allowedMinutesDiff + " minutes");
				// return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST); // need to confirm,																					// 405 not allowed
				return ResponseEntity.ok(dto);
			}
		}

		// Checking the amount in user account that it is more than or equal to
		// threshold
		Double redeemThreshold = dbConfigService.getDoubleProperty(Properties.REDEEM_THRESHOLD.getProperty(),
				Double.valueOf(Properties.REDEEM_THRESHOLD.getDefaultValue()));
		UserAccount userAccount = userAccountRepo.findByUserId(userId);
		if(userAccount == null) {
			LOGGER.error("user account doesn't exist");
			// dto.setMessage("user account doesn't exist");
			// return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
			return ResponseEntity.ok(dto);
		}
		
		if (userAccount.getCurrentBalance() < redeemThreshold) {
			LOGGER.error("current balance in user account is less than  the threshold");
			// dto.setMessage("Threshold Violated");
			LOGGER.error("Threshold Violated");
			return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
		}
		
		boolean isMsisdnCheckEnabled = dbConfigService.getBooleanProperty(Properties.IS_MSISDN_CHECK_ENABLED_AT_REDEEM.getProperty(), Boolean.valueOf(Properties.IS_MSISDN_CHECK_ENABLED_AT_REDEEM.getDefaultValue()));
		
		if(isMsisdnCheckEnabled) {
			// Mobile check
			if(msisdn.length() > 10) {
				msisdn = msisdn.substring(msisdn.length()-10);
				int firstDigit = Integer.parseInt(msisdn.substring(0, 1));
				if(firstDigit < 6) {
					LOGGER.error("Bad MSISDN, first digit is less than 6");
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}
			long mobile = 0l;
			LOGGER.info("Checking the mobile number..");
			try {
				mobile = Long.parseLong(msisdn);
			} catch (NumberFormatException nfe) {
				LOGGER.error("msisdn parsing erorr, msisdn={}", msisdn);
				dto.setMessage("Bad MSISDN, not parsed to long");
				return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
			}
			
			if(mobile == 0 || msisdn.equals("") || !user.getMobile().equals(""+msisdn)  ) {
				dto.setMessage("Bad MSISDN");
				LOGGER.error("Bad MSISDN or mismatched MSISDN");
				return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
			}
		}
		
		
		long parsedOtp = 0;
		LOGGER.info("Checking the otp..");
		try {
			parsedOtp = Long.parseLong(otp);
		} catch (NumberFormatException nex) {
			LOGGER.error("failed to parse otp as long");
			dto.setMessage("Unresolved Authentication");
			return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
		}
		
		// checking the deviceId
		LOGGER.info("Checking the device id..");
		long countsByDevice = userDeviceRepo.countByDeviceId(deviceId);
		UserDevice userDevice = userDeviceRepo.findByUserId(userId);
		if(countsByDevice > 2) {
			LOGGER.error("more than 2 users with same device found");
			//dto.setMessage("Ambiguous User/UserDevice");
			// dto.setMessage("Fraud");
			return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
		}
		
		if (userDevice == null || userDevice.getDeviceId() == null || userDevice.getDeviceId().equals("") || !userDevice.getDeviceId().equals(deviceId)) {
			
			try {
				LOGGER.error("mismatched deviceId, passed={}, while actually={}", deviceId, userDevice.getDeviceId());
			} catch (Exception ex){
				LOGGER.error("mismatched deviceId, passed={}", deviceId);
			}
			
			dto.setMessage("Fraud");
			return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
		}
		
		//userSourceRepo.countByUserSource();

		UUID uuid = UUID.randomUUID();
		
		boolean isRedeemEnabled = dbConfigService.getBooleanProperty(Properties.IS_REDEEM_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_REDEEM_ENABLED.getDefaultValue()));
		if(isRedeemEnabled) {
			PayTmDetails payTmDetails = new PayTmDetails();
			payTmDetails.setDeviceId(deviceId);
			
			long tempMobile = 0;
			try {
				tempMobile = Long.parseLong(msisdn);
			} catch (Exception ex) {
				LOGGER.warn("exception occured when parsing msisdn to long msisdn={}, exception={}", msisdn, ex.getMessage());
			}
			
			if(tempMobile != 0) {
				payTmDetails.setMsisdn(tempMobile);
			} else {
				try {
					payTmDetails.setMsisdn(Long.parseLong(msisdn, 16));
				} catch (Exception ex) {
					LOGGER.error("exception while setting msisdn={}, ex={}", msisdn, ex.getMessage());
				}
			}
			
			payTmDetails.setOtp(parsedOtp);
			payTmDetails.setStatus(0);
			payTmDetails.setTid(uuid.toString()); // need to confirm
			payTmDetails.setUserId(userId);
			payTmDetailsRepo.save(payTmDetails);
		}

		
		// appeding UUID to url
		vocalPayUrl = vocalPayUrl + uuid.toString();
		LOGGER.info("vocalPayTmPayUrl={}", vocalPayUrl);
		if(isRedeemEnabled) {
			dto.setUrl(vocalPayUrl);
			dto.setAction("web");
			dto.setMessage("Success");
		}
		
		LOGGER.info("PayTmDetails successfully generated.");
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/user/earnMore")
	public ResponseEntity<?> earnMore(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = "msisdn") String msisdn,
			@RequestParam(name = "type") String type, 
			@RequestParam(name = "deviceId") String deviceId,
			@RequestHeader(name = "User-Agent") String userAgent, 
			HttpServletRequest request) {
		LOGGER.info("/user/earnMore userId={}, otp={}, msisdn={}, type={}, deviceId={}, userAgent={}", userId, otp,
				msisdn, type, deviceId, userAgent);
		User user = userRepo.findByUserId(userId);
		UrlAndActionDto dto = new UrlAndActionDto();
		if (user == null || otp == null || otp.equals("") || user.getOtp().equals("") || !otp.equals(user.getOtp())
				|| user.getOtpVerify() == null || user.getOtpVerify() == 0) {
			dto.setMessage("Unauthorized Access");
			return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
		}

		String earnMoreUrl = dbConfigService.getProperty(Properties.VOCAL_EARN_MORE.getProperty(),
				Properties.VOCAL_EARN_MORE.getDefaultValue());
		earnMoreUrl = earnMoreUrl + "id=" + userId;
		String earnMoreAction = dbConfigService.getProperty(Properties.EARN_MORE_ACTION.getProperty(), Properties.EARN_MORE_ACTION.getDefaultValue());
		LOGGER.info("vocalEarnMoreUrl={}, earnMoreAction={}", earnMoreUrl, earnMoreAction);

		dto.setUrl(earnMoreUrl);
		dto.setAction(earnMoreAction); // //dto.setAction("web"); 
		dto.setMessage("Success");

		return ResponseEntity.ok(dto);
	}
}
