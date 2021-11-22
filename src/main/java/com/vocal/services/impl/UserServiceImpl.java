package com.vocal.services.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vocal.entities.City;
import com.vocal.entities.CityMapping;
import com.vocal.entities.Follow;
import com.vocal.entities.OtpHashed;
import com.vocal.entities.PinCodeMapping;
import com.vocal.entities.State;
import com.vocal.entities.TrueCaller;
import com.vocal.entities.UnauthorizedAccess;
import com.vocal.entities.User;
import com.vocal.entities.UserAccount;
import com.vocal.entities.UserAccountSummary;
import com.vocal.entities.UserBlackList;
import com.vocal.entities.UserDevice;
import com.vocal.entities.UserInsurance;
import com.vocal.entities.UserLocation;
import com.vocal.entities.UserProfile;
import com.vocal.entities.UserQuality;
import com.vocal.entities.UserSource;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.exceptions.NoContentAvailableException;
import com.vocal.mapper.Mapper;
import com.vocal.repos.jpa.BeforeLoginRepo;
import com.vocal.repos.jpa.CityMappingRepo;
import com.vocal.repos.jpa.CityRepo;
import com.vocal.repos.jpa.FollowRepo;
import com.vocal.repos.jpa.OtpHashedRepo;
import com.vocal.repos.jpa.PinCodeMappingRepo;
import com.vocal.repos.jpa.StateRepo;
import com.vocal.repos.jpa.TrueCallerRepo;
import com.vocal.repos.jpa.UnauthorizedAccessRepo;
import com.vocal.repos.jpa.UserAccountRepo;
import com.vocal.repos.jpa.UserAccountSummaryRepo;
import com.vocal.repos.jpa.UserBlackListRepo;
import com.vocal.repos.jpa.UserDeviceRepo;
import com.vocal.repos.jpa.UserInsuranceRepo;
import com.vocal.repos.jpa.UserLocationRepo;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.repos.jpa.UserQualityRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.repos.jpa.UserSourceRepo;
import com.vocal.services.BlacklistedIpService;
import com.vocal.services.DbConfigService;
import com.vocal.services.LeaderboardService;
import com.vocal.services.NewsService;
import com.vocal.services.OtpService;
import com.vocal.services.PushMangerService;
import com.vocal.services.QualityLogicService;
import com.vocal.services.ReferralLogicService;
import com.vocal.services.UserService;
import com.vocal.services.ValidatorService;
import com.vocal.utils.AES;
import com.vocal.utils.Constants;
import com.vocal.utils.Properties;
import com.vocal.utils.RefStatusCodes;
import com.vocal.utils.Utils;
import com.vocal.utils.UtmParameters;
import com.vocal.viewmodel.AddressModel;
import com.vocal.viewmodel.InsuranceDto;
import com.vocal.viewmodel.LoginResponseDto;
import com.vocal.viewmodel.LoginTypeDto;
import com.vocal.viewmodel.SocialResponse;
import com.vocal.viewmodel.StatusDto;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PinCodeMappingRepo pinCodeMappingRepo;
	
	@Autowired
	private CityRepo cityRepo;

	@Autowired
	private UserDeviceRepo userDeviceRepo;

	@Autowired
	private UserSourceRepo userSourceRepo;

	@Autowired
	private UserProfileRepo userProfileRepo;
	
	@Autowired
	private UserLocationRepo userLocationRepo;
	
	@Autowired
	private UserInsuranceRepo userInsuranceRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private UserAccountRepo userAccountRepo;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private ValidatorService validatorService;
	
	@Autowired
	private PushMangerService pushManagerService;
	
	@Autowired
	private  OtpService otpService;
	
	@Autowired
	private ReferralLogicService referralLogicService;
	
	@Autowired
	private TrueCallerRepo trueCallerRepo;
	
	@Autowired
	private BeforeLoginRepo beforeLoginRepo;
	
	@Autowired
	private OtpHashedRepo otpHashedRepo;
	
	@Autowired
	private UserAccountSummaryRepo userAccountSummaryRepo;
	
	@Autowired
	private FollowRepo followRepo;
	
	@Autowired
	private BlacklistedIpService blacklistedIpService;
	
	@Autowired
	private CityMappingRepo cityMappingRepo;
	
	@Autowired
	private UserBlackListRepo userBlackListRepo;
	
	@Autowired
	private UnauthorizedAccessRepo unauthorizedAccessRepo;
	
	@Autowired
	private UserQualityRepo userQualityRepo;
	
	@Autowired
	private QualityLogicService qualityLogicService;
	
	@Autowired
	private Mapper mapper;
	
	@Autowired
	private LeaderboardService leaderboardService;
		
	@Value("${SMS_FORMAT}")
	private String msgTemplate;
	
	@Value("${SMS_PROCESS_IP}")
	private String ip;
	
	@Value("${SMS_PROCESS_PORT}")
	private String port;
	
	@Value("${SECRET_KEY}")
	private String encryptionKey;
	
	private Set<Long> postalCodesOfCityGhz;
	
	private Random random = new Random();
	
	//private ExecutorService executorService = Executors.newFixedThreadPool(10);

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	
	
	@PostConstruct
	public void initializedPostalCodesOfCityGhz() {
		if(postalCodesOfCityGhz == null) {
			postalCodesOfCityGhz = new HashSet<>();
			LOGGER.info("initializing postalCodesOfCityGhz");
			long[] postalCodes = new long[] { 201206, 245101, 201015, 245208, 245201, 245304, 201009, 245101, 201001,
					245205, 201003, 245304, 201003, 201009, 245201, 245201, 201102, 245205, 245208, 245101, 245304,
					245201, 201206, 245207, 201204, 201102, 245304, 201206, 245208, 245205 };
			for(long postalCode : postalCodes) {
				LOGGER.info("added postalCode={} to postCodesOfCityGhz", postalCode);
				postalCodesOfCityGhz.add(postalCode);
			}
		}
	}
	
	@Override
	public LoginTypeDto getLoginOptions() {
		LoginTypeDto dto = new LoginTypeDto();
		boolean isFacebookLoginEnabled = dbConfigService.getBooleanProperty(Properties.IS_FACEBOOK_LOGIN_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_FACEBOOK_LOGIN_ENABLED.getDefaultValue()));
		boolean isGoogleLoginEnabled = dbConfigService.getBooleanProperty(Properties.IS_GOOGLE_LOGIN_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_GOOGLE_LOGIN_ENABLED.getDefaultValue()));
		boolean isTrueCallerLoginEnabled = dbConfigService.getBooleanProperty(Properties.IS_TRUE_CALLER_LOGIN_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_TRUE_CALLER_LOGIN_ENABLED.getDefaultValue()));
		boolean isManualLoginEnabled = dbConfigService.getBooleanProperty(Properties.IS_MOBILE_NUMBER_LOGIN_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_MOBILE_NUMBER_LOGIN_ENABLED.getDefaultValue()));
		LOGGER.debug("facebookEnabled = {}, googleEnabled = {}, trueCallerEnabled = {}, mobileEnabled = {}",
				isFacebookLoginEnabled, isGoogleLoginEnabled, isTrueCallerLoginEnabled, isManualLoginEnabled);
		dto.setFacebook(String.valueOf(isFacebookLoginEnabled));
		dto.setGoogle(String.valueOf(isGoogleLoginEnabled));
		dto.setTrueCaller(String.valueOf(isTrueCallerLoginEnabled));
		dto.setMobile(String.valueOf(isManualLoginEnabled));
		
		return dto;
	}

	@Override
	public boolean isUnauthorizedUser(long userid, String otp) {
		User user =  userRepo.findByUserId(userid);
		
		// TODO : to replace with Apache StringUtils 
		if (user == null || otp == null || otp.equals("") || !otp.equals(user.getOtp())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isUnauthorizedUser(User user, String otp) {
		if (user == null || otp == null || otp.equals("") ||  !otp.equals(user.getOtp())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isUnauthorizedUserV1(User user, String otp, HttpServletRequest request) {
		boolean isUnauthorizedAccess = isUnauthorizedUser(user, otp);
		if(isUnauthorizedAccess) {
			String suspiciousIp = Utils.getClientIp(request);
			LOGGER.error("Unauthorized Access for userId={}, otp={} from ip={}", user.getUserId(), otp, suspiciousIp);
			// BLACK-LISTING THIS USER FROM FURTHER REDEEM
			UserBlackList userBlackList = userBlackListRepo.findByUserId(user.getUserId());
			if(userBlackList == null) {
				userBlackList = new UserBlackList();
				userBlackList.setUserId(user.getUserId());
				userBlackList.setBlackListCounter(1);
				userBlackList.setRemarks("Blacklisted for UnauthorizedAccess");
				userBlackList.setUpdatedTime(new Date());
				userBlackList.setType(2);
			} else {
				userBlackList.setBlackListCounter(userBlackList.getBlackListCounter() + 1);
				userBlackList.setType(2);
			}
			userBlackListRepo.save(userBlackList);
			// GENERATING/UPDATING UnauthorizedAccess RECORD
			UnauthorizedAccess unauthorizedAccess = unauthorizedAccessRepo.findByUserIdAndIp(user.getUserId(), suspiciousIp);
			if(unauthorizedAccess == null) {
				unauthorizedAccess = new UnauthorizedAccess();
				unauthorizedAccess.setUserId(user.getUserId());
				unauthorizedAccess.setCreatedTime(new Date());
				unauthorizedAccess.setUpdatedTime(new Date());
				unauthorizedAccess.setHitCounter(1);
				unauthorizedAccess.setIp(suspiciousIp);
				unauthorizedAccess.setOtp(otp);	
			} else {
				unauthorizedAccess.setUpdatedTime(new Date());
				unauthorizedAccess.setHitCounter(unauthorizedAccess.getHitCounter() + 1);
			}
			unauthorizedAccessRepo.save(unauthorizedAccess);
			// INFORMING THE configured Ids about this unauthorized access
			String commaSeparatedMaintainersId = dbConfigService.getProperty(Properties.MAINTAINERS_ID.getProperty(), Properties.MAINTAINERS_ID.getDefaultValue());
			for(String maintainerId: commaSeparatedMaintainersId.trim().split(",")) {
				try {
					long maintainerUserId = Long.parseLong(maintainerId.trim());
					String msg = "Unauthorized Access for userId=" + user.getUserId() + ", otp=" + otp + ", from suscipious ip=" + suspiciousIp;
					pushManagerService.sendPush(maintainerUserId, msg, "", "", 5);
				} catch(Exception ex){
					LOGGER.error("failed to notify maintainer id={} about unauthorized access, exception={}", maintainerId, ex.getMessage());
				}
			}
		}
		return isUnauthorizedAccess;
	}

	@Override
	public StatusDto handleDeviceTokenUpdation(long userId, String deviceId, String androidId, String token) {
		StatusDto dto = new StatusDto();
		UserDevice userDevice = userDeviceRepo.findByUserId(userId);
		if(userDevice == null) {
			LOGGER.error("user device not found for userId={}", userId);
		} else {
			userDevice.setDeviceToken(token);
			userDevice.setActive(true);
		}
		
		try {
			userDeviceRepo.save(userDevice);
			dto.setStatus("success");
			LOGGER.info("user device token updated for userId={}", userId);
		} catch (Exception ex) {
			LOGGER.error("failed to update token, exception = {}", ex.getMessage());
			dto.setStatus("fail");
		}
		return dto;
	}
	
	private void sendTheWelcomeBackMsg(long userId, String deviceToken) {
		// Sending the welcome back message
		try {
			UserProfile userProfile = userProfileRepo.findByUserId(userId);
			if(userProfile != null && userProfile.getLanguageId() != null ) {
				String oldUserPushText = dbConfigService.getPropertyByLanguageId(Properties.OLD_USER_WELCOME_TEXT.getProperty(), userProfile.getLanguageId());
				if(oldUserPushText == null) {
					oldUserPushText = dbConfigService.getPropertyByLanguageId(Properties.OLD_USER_WELCOME_TEXT.getProperty(), Properties.OLD_USER_WELCOME_TEXT.getDefaultValue(), 2);
				}
				pushManagerService.sendPush(deviceToken, oldUserPushText, "", "", 5);
			}
		} catch (Exception ex) {
			LOGGER.error("exception while sending welcome back message to old user, ex={}", ex.getMessage());
		}
	}

	@Override
	public LoginResponseDto handleUserLogin(String email, String token, String ip, String deviceToken, String loginType,
			String utmSource, String utmMedium, String utmTerm, String utmCampaign, String appVersion, String deviceId,
			String name, String profilePicUrl, String androidVersion, String deviceName, String make, String model,
			String timeOfDevice)  {
		boolean isAuthorizedToken = false;
		// Checking the authenticity  of the received token
		SocialResponse socialResponse = validatorService.getSocialLoginDetails(loginType, token);
		if(socialResponse != null && socialResponse.getEmail() != null && !socialResponse.getEmail().equals("")) {
			isAuthorizedToken = true;
			LOGGER.debug("login medium={}, social response = {},", loginType, socialResponse);
		}
		
		LoginResponseDto loginResponse = new LoginResponseDto();
		if(isAuthorizedToken) {
			User user = userRepo.findByEmailId(email);
			if(user == null) {
				// CREATING  A NEW USER
				boolean beforeLoginExisted = false;
				try {
					beforeLoginRepo.deleteByDeviceId(deviceId);
					LOGGER.info("before login details deletion success");
					beforeLoginExisted = true;
				} catch (Exception ex) {
					LOGGER.debug("deletion of before login failed, exception={}", ex.getMessage());
				}
				
				String otp = String.valueOf(Utils.generateOtp());
				// String otp = RandomStringUtils.randomAlphanumeric(10);
				//long userId = createUser(email, otp, ""+1, appVersion, deviceId); // added deviceid in place of mobile
				user = new User.Builder().emailId(email).mobile(deviceId).otp(otp).status(1).appVersion(appVersion)
						.followers(0).following(0).postPublished(0).build();
				user.setOtpVerify(1);
				user = userRepo.save(user);
				long userId = user.getUserId();
				LOGGER.info("created new user with userid={}, otp = {}", userId, otp);
				loginResponse.setUserid(userId);
				loginResponse.setOtp(otp);
				
				UserSource userSource = createUserSource(userId, utmSource, utmMedium, utmTerm, utmCampaign, ip, deviceId); // UserSource userSource = 
				createUserProfile(userId, 1, profilePicUrl, name, loginType, androidVersion, token);
				
				boolean isExistingDeviceToken = userDeviceRepo.existsUserDeviceByDeviceToken(deviceToken);
				// CREATION/UPDATION OF USER DEVICE RECORDS
				createOrUpdateUserDevice(userId, deviceId, deviceName, make, model, timeOfDevice, deviceToken);
				
				// Send welcome message, by default message language is English(i.e., id 2)
				String pushText = dbConfigService.getPropertyByLanguageId(Properties.NEW_USER_WELCOME_TEXT.getProperty(), Properties.NEW_USER_WELCOME_TEXT.getDefaultValue(), 1);
				pushManagerService.sendPush(deviceToken, pushText, "", "", 5);
				
				boolean isReferralEnabled = dbConfigService.getBooleanProperty(Properties.IS_REFERRAL_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_REFERRAL_ENABLED.getDefaultValue()));
				
				// referral flow called for google login
				if(isReferralEnabled) {
					boolean isReferralForGoogleEnabled = dbConfigService.getBooleanProperty(Properties.IS_REFERRAL_FOR_GOOGLE_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_REFERRAL_FOR_GOOGLE_ENABLED.getDefaultValue()));
					if(loginType.equals(Constants.GOOGLE)) {
						if(isReferralForGoogleEnabled ) {
							handleReferralFlow(beforeLoginExisted, isExistingDeviceToken, deviceId, deviceToken, appVersion, loginType, utmTerm, utmMedium, utmCampaign, utmSource, userId, userSource);
						} else {
							LOGGER.info("userId={} not eligible for referral because referral for google is disabled, isReferralForGoogleDisabled={}", user.getUserId(), isReferralForGoogleEnabled);
							userSource.setRefStatus(RefStatusCodes.REFERRAL_DISABLED_FOR_GOOGLE);
							userSourceRepo.save(userSource);
						}
					} else {
						userSource.setRefStatus(RefStatusCodes.NO_REFERRAL_FLOW_FOR_THIS_LOGIN_TYPE);
						userSourceRepo.save(userSource);
					}
				} else {
					LOGGER.info("userId={} not eligible for referral because referral disabled, isReferralEnabled={}", user.getUserId(), isReferralEnabled);
					userSource.setRefStatus(RefStatusCodes.REFERRAL_DISABLED);
					userSourceRepo.save(userSource);
				}
			} else {
				// FOR OLD USER
				long userId = user.getUserId();
				String otp = user.getOtp();
				LOGGER.debug("Old user with userId={}, otp={}", userId, otp);
				
				try {
					beforeLoginRepo.deleteByDeviceId(deviceId);
					LOGGER.info("before login details deletion success");
				} catch (Exception ex) {
					LOGGER.debug("deletion of before login failed, exception={}", ex.getMessage());
				}
				
				// TODO: if appVersion == 1.2 then enter userId as mobile
				
				// Updating user columns
				user.setAppVersion(appVersion);
				user.setUpdatedTime(new Date());
				user.setOtpVerify(1);
				userRepo.save(user);
				if(userInsuranceRepo.existsUserInsuranceByUserId(userId)) {
					LOGGER.debug("UserInsurence for userid={} already exists", userId);
					loginResponse.setKycVerified(true);
				}
				
				createOrUpdateUserDevice(userId, deviceId, deviceName, make, model, timeOfDevice, deviceToken);

				loginResponse.setUserid(userId);
				loginResponse.setOtp(otp);
				// send the old welcom user msg
				sendTheWelcomeBackMsg(userId, deviceToken);
			}
		} else {
			LOGGER.error("Could not verify the authenticity of the token={}", token);
			throw new AuthorizationException("Could not verify the authenticity of the token");
		}
		loginResponse.setNews(newsService.getNews(0, 0, 1, 10, appVersion)); // Util.GetNews(99999999,1,1,10,"2025",0)
		return loginResponse;
	}
	
	
	public LoginResponseDto handleUserLoginV1(String email, String token, String ip, String deviceToken,
			String loginType, String utmSource, String utmMedium, String utmTerm, String utmCampaign, String appVersion,
			String deviceId, String androidId, String name, String profilePicUrl, String androidVersion, String deviceName, String make,
			String model, String timeOfDevice, long mobileNumber, String otp, String payload, String signedString,
			String signatureAlgo) {
		boolean isAuthorizedMobileNumber = false;
		if(otp.equals("") || otp.equals("N/A") || loginType.equalsIgnoreCase(Constants.TRUE_CALLER) ) {
			isAuthorizedMobileNumber = validatorService.trueCallerValidation(payload, signedString, signatureAlgo);
			// ADDING TRUE CALLER ENTRY
			TrueCaller trueCaller = new TrueCaller();
			trueCaller.setMobile(mobileNumber);
			trueCaller.setPayload(payload);
			trueCaller.setSignature(signedString);
			trueCaller.setSignatureAlgo(signatureAlgo);
			trueCaller.setLoginSuccess(isAuthorizedMobileNumber);
			trueCallerRepo.save(trueCaller);
			LOGGER.debug("trueCaller validation={}", isAuthorizedMobileNumber);
		} else if(loginType.equalsIgnoreCase("mobile")) {
			String mobileNum = String.valueOf(mobileNumber);
			String loadedOtp = "";
			OtpHashed otpHashed = null;
			Boolean isOtpCacheEnabled = dbConfigService.getBooleanProperty(Properties.IS_OTP_CACHE_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_OTP_CACHE_ENABLED.getDefaultValue()));
			if(isOtpCacheEnabled) {
				loadedOtp = otpService.getOtp(mobileNum);
			} else {
				otpHashed = otpHashedRepo.findByMobileNum(mobileNum);
				if(otpHashed != null) loadedOtp = otpHashed.getOtp();
			}
			isAuthorizedMobileNumber = (otp != null) &&  (loadedOtp != null) && otp.equals(loadedOtp) && !otp.equals("") && !loadedOtp.equals("");
			LOGGER.debug("mobile validation={}", isAuthorizedMobileNumber);
			if(isAuthorizedMobileNumber && !isOtpCacheEnabled) {
				// DELETE THE OTP ENTRY, or some additional logic
				otpHashedRepo.deleteById(mobileNum);
				LOGGER.info("deleted the otpHashed record");
			}
			if(!isAuthorizedMobileNumber) {
				// 401
				// MOBILE NUMBER AND OTP MISMATCH, THEN Suspicion FLOW
				if(otpHashed!= null) {
					otpHashed.setMismatchedNums(otpHashed.getMismatchedNums() + 1);
					otpHashedRepo.save(otpHashed);
				}
				throw new AuthorizationException("UnauthorizedAccess");
			}
		}
		
		LoginResponseDto loginResponse = new LoginResponseDto();
		String mobile = String.valueOf(mobileNumber);
		if (isAuthorizedMobileNumber) {
			User user = userRepo.findByMobile(mobile);
			if(user == null && email != null && !email.equals("")) {
				user = userRepo.findByEmailId(email);
			}
			
			if(user == null) {
				// NEW USER FLOW
				
				// DELETING BeforeLogin details
				boolean beforeLoginExisted = false;
				try {
					long deleteCountBeforeLogin = beforeLoginRepo.deleteByDeviceId(deviceId);
					LOGGER.info("before login details deletion success, return count={}", deleteCountBeforeLogin);
					if(deleteCountBeforeLogin > 0) beforeLoginExisted = true;
				} catch (Exception ex) {
					LOGGER.debug("deletion of before login failed, exception={}", ex.getMessage());
				}
				
				String newOtp = String.valueOf(Utils.generateOtp());
				// String otp = RandomStringUtils.randomAlphanumeric(10);
				// HANDLING EMAIL UNIQUE-NESS, NO EMAIL IS PROVIDED THEN INSERT DEVICE ID AS EMAIL
				if(email.equals("")) {
					email = deviceId;
					LOGGER.debug("updated email with the value of deviceId={}", deviceId);
				}
				
				user = new User.Builder().mobile(mobile).emailId(email).otp(newOtp).otpVerify(1)
						.status(1).appVersion(appVersion).followers(0).following(0).postPublished(0)
						.build();
				user = userRepo.save(user);
				long userId = user.getUserId();
				LOGGER.info("New User creation with userid={}, otp = {}", userId, newOtp);
				
				loginResponse.setUserid(userId);
				loginResponse.setOtp(newOtp);
				
				// EXTRACTS and WRAPS UTM PARAMETERS IN UtmParameters class RE-ASSIGNMENT WITH EXTRACTED VALUE. 
				UtmParameters utmParams = Utils.extractUtmParameters(utmSource, utmMedium, utmTerm, utmCampaign);
				utmSource = utmParams.getUtmSource();
				utmMedium = utmParams.getUtmMedium();
				utmTerm = utmParams.getUtmTerm();
				utmCampaign = utmParams.getUtmCampaign();
				
				UserSource userSource = createUserSource(userId, utmSource, utmMedium, utmTerm, utmCampaign, ip, deviceId);
				// language HARDCoded to 1
				createUserProfile(userId, 1, profilePicUrl, name, loginType, androidVersion, token);
				
				boolean isExistingDeviceToken = userDeviceRepo.existsUserDeviceByDeviceToken(deviceToken);
				
				createOrUpdateUserDevice(userId, deviceId, deviceName, make, model, timeOfDevice, deviceToken);
				double registrationBonus = dbConfigService.getDoubleProperty(Properties.REGISTRATION_GIFT_AMT.getProperty(), Double.valueOf(Properties.REGISTRATION_GIFT_AMT.getDefaultValue()));
				createUserAccount(userId, registrationBonus);
				createUserAccountSummary(userId, registrationBonus, "Registration", "Registration bonus");
				
				// Send welcome message, by default message language is HINDI(i.e., id 1)
				String pushText = dbConfigService.getPropertyByLanguageId(Properties.NEW_USER_WELCOME_TEXT.getProperty(), Properties.NEW_USER_WELCOME_TEXT.getDefaultValue(), 1);
				pushManagerService.sendPush(deviceToken, pushText, "imageUrl", "actionUrl", 5);
				
				// REFERRAL FLOW
				boolean isReferralEnabled = dbConfigService.getBooleanProperty(Properties.IS_REFERRAL_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_REFERRAL_ENABLED.getDefaultValue()));
				if(isReferralEnabled) {
					handleReferralFlow(beforeLoginExisted, isExistingDeviceToken, deviceId, deviceToken, appVersion, loginType, utmTerm, utmMedium, utmCampaign, utmSource, userId, userSource);
				} else {
					LOGGER.info("userId={} not eligible for referral because referral disabled, isReferralEnabled={}", user.getUserId(), isReferralEnabled);
					userSource.setRefStatus(RefStatusCodes.REFERRAL_DISABLED);
					userSourceRepo.save(userSource);
				}
				
			} else {
				// OLD USER FLOW
				long userId = user.getUserId();
				String userOtp = user.getOtp();
				LOGGER.debug("Existing user with userId={}, otp={}", userId, userOtp);
				
				try {
					long deleteCountBeforeLogin = beforeLoginRepo.deleteByDeviceId(deviceId);
					LOGGER.info("before login details deletion success, for old userId={} and deviceId={}, deleteCount={}", userId, deviceId, deleteCountBeforeLogin);
				} catch (Exception ex) {
					LOGGER.debug("deletion of before login failed, exception={}", ex.getMessage());
				}
				
				// UPDATING MOBILE ENTRY FOR OLD USER AND OTP VERIFY STATUS
				if(user.getOtpVerify() == null || user.getOtpVerify() == 0) {
					LOGGER.info("old user marked as verified with mobile number={}", mobile);
					user.setMobile(mobile);
					user.setOtpVerify(1);
				}
				
				// UPDATING USER COLUMNS
				user.setAppVersion(appVersion);
				user.setUpdatedTime(new Date());
				userRepo.save(user);
				// ADDING USER INSURANCE STATUS OF CURRENT USER
				if(userInsuranceRepo.existsUserInsuranceByUserId(userId)) {
					LOGGER.debug("UserInsurence for userid={} already exists", userId);
					loginResponse.setKycVerified(true);
				}
				// UPDATION of user entries like android version,  deviceToken etc. 
				//createUserProfile(userId, 1, profilePicUrl, name, loginType, androidVersion, token);
				createOrUpdateUserDevice(userId, deviceId, deviceName, make, model, timeOfDevice, deviceToken);

				loginResponse.setUserid(userId);
				loginResponse.setOtp(userOtp);
				
				// send the old welcom user msg
				sendTheWelcomeBackMsg(userId, deviceToken);
			}
		} else {
			// TODO: Suspicion flow to be added
			LOGGER.error("failed to validate the mobile number, number={}", mobileNumber);
			throw new AuthorizationException("could not validate the mobile number");
		}
		loginResponse.setNews(newsService.getNews(0, 0, 1, 10, appVersion));
		return loginResponse;
	}
	
	/**
	 * This method performs decryption and basic tests to initiate referral flow
	 */
	private void handleReferralFlow(boolean beforeLoginExisted, boolean isExistingDeviceToken, String deviceId, String deviceToken, String appVersion, String loginType, String utmTerm, String utmMedium, String utmCampaign, String utmSource, long currentUserId, UserSource userSource) {		
		// utmTerm check condition
		boolean utmTermsEqualsToInvite = false;
		if(!utmTerm.equals("INVITE")) {
			LOGGER.info("userId={} not eligible for referral because utm term is not equal to 'INVITE', utmTerm={}", currentUserId, utmTerm);
			userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_UTM_TERM);
			userSourceRepo.save(userSource);
			return;
		} else {
			utmTermsEqualsToInvite = true;
		}
		
		// appVersion and alternate app version condition
		String baseAppVersion = dbConfigService.getProperty(Properties.BASE_APP_VERSION.getProperty(), Properties.BASE_APP_VERSION.getDefaultValue());
		String alternateAppVersion = dbConfigService.getProperty(Properties.ALTERNATE_APP_VERSION.getProperty(), Properties.ALTERNATE_APP_VERSION.getDefaultValue());
		boolean isAppVersionEqualBaseAppVersionOrLatestAppVersion = false;
		if(appVersion.equals(baseAppVersion) || appVersion.equals(alternateAppVersion)) {
			isAppVersionEqualBaseAppVersionOrLatestAppVersion = true;
		} else {
			LOGGER.info("userId={} not eligible for referral because appVersion={} while base appVersion={}, alternate appVersion={}", currentUserId, appVersion, baseAppVersion, alternateAppVersion);
			userSource.setRefStatus(RefStatusCodes.APP_VERSION_NOT_LATEST);
			userSourceRepo.save(userSource);
			return;
		}
		
		
		// before login record existance check
		/*
		if(!beforeLoginExisted) {
			LOGGER.info("userId={} not eligible for referral because his beforeLogin details doesn't exist", currentUserId);
			userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_B4LOGIN);
			userSourceRepo.save(userSource);
			return;
		}
		*/
		
		// boolean isAlreadyExistingDeviceId = userDeviceRepo.existsUserDeviceByDeviceId(deviceId);
		int duplicateDeviceCounter = dbConfigService.getIntProperty(Properties.DUPLICATE_DEVICE_COUNTER.getProperty(), Properties.DUPLICATE_DEVICE_COUNTER.getDefaultValueAsInt());
		long countByDeviceId = userDeviceRepo.countByDeviceId(deviceId);
		boolean isDeviceCountWithinAllowedCount = false;
		if(countByDeviceId > duplicateDeviceCounter) {
			LOGGER.info("userId={} not eligible for referral because of existing device id counter={} and allowedCountByDeviceId = {}", currentUserId, countByDeviceId, duplicateDeviceCounter);
			userSource.setRefStatus(RefStatusCodes.MORE_THAN_ALLOWED_DUPLICATE_DEVICES);
			userSourceRepo.save(userSource);
			return;
		} else {
			isDeviceCountWithinAllowedCount = true;
		}
		
		if(isExistingDeviceToken) {
			LOGGER.info("userId={} not eligible for referral because his deviceToken={} already exists", currentUserId, deviceToken);
			userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBEL_DUE_TO_NULL_EMPTY_OR_EXISTING_DEVICE_TOKEN);
			userSourceRepo.save(userSource);
			return;
		}
		
		if (utmTermsEqualsToInvite) {
			try {
				String decodedUtmMedium = utmMedium.replaceAll(" ", "+");
				String  decryptedUtmMedium = AES.decrypt(decodedUtmMedium, encryptionKey);
				String decodedUtmCampaign = utmCampaign.replaceAll(" ", "+");
				String decryptedUtmCampaign = AES.decrypt(decodedUtmCampaign, encryptionKey);
				
				try {
					String[] values = decryptedUtmMedium.split("\\^");
					long decryptedUserId = Long.valueOf(values[0]);
					long timeStampInMillies = Long.valueOf(values[1]);
					long currentTimeInMillies = System.currentTimeMillis();
					long minutesDiff = (currentTimeInMillies - timeStampInMillies) / (1000 * 60);
					double allowedMinutes= dbConfigService.getIntProperty(Properties.FULL_COVER_MINUTES.getProperty(), Properties.FULL_COVER_MINUTES.getDefaultValueAsInt());
					long allowedCampaignMinutes = dbConfigService.getIntProperty(Properties.ALLOWED_CAMPAIGN_MINUTES.getProperty(), Properties.ALLOWED_CAMPAIGN_MINUTES.getDefaultValueAsInt());
					
					DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
					Date earlierTime = df.parse(decryptedUtmCampaign);
					Date currentTime = new Date();
					long campaignMinutesElapsed= ( currentTime.getTime() - earlierTime.getTime() ) / (1000 * 60);
					boolean isWithinCampaignTime = campaignMinutesElapsed < allowedCampaignMinutes;
					boolean isDecryptedUserIdAndUtmSourceSame = ( Long.valueOf(utmSource) == decryptedUserId);
					//LOGGER.info("campaignTime={}, currentTime={}, campaignDiff in minutes={}, isWithinCampaignTime={}", df.format(earlierTime), df.format(currentTime), campaignMinutesElapsed, isWithinCampaignTime);
					LOGGER.info(
							"utmSource={}, decryptedUserId={}, currentUserId={}, currentTimeInMillies={}, timeStampInMillies={}, minutesDiff={}, allowedMinutes={}, campaignTime={}, currentTime={}, campaignDiff in minutes={}, allowedCampaignDiffMinutes={}, isWithinCampaignTime={}, beforeLoginExists={}",
							utmSource, decryptedUserId, currentUserId, currentTimeInMillies,
							timeStampInMillies, minutesDiff, allowedMinutes, df.format(earlierTime),
							df.format(currentTime), campaignMinutesElapsed, allowedCampaignMinutes,
							isWithinCampaignTime, beforeLoginExisted);
					// LEADERBOARD Specific to daily and global
					if (leaderboardService.incrementReferralCounterOfUser(decryptedUserId)) {
						LOGGER.info("successfully incremented referrer counter in leaderboard for utmSourceUserId={}",
								decryptedUserId);
					} else {
						LOGGER.error("failed to increment leaderboard counter for decryptedUserId={}", decryptedUserId);
					}
					
					if(!isWithinCampaignTime) {
						LOGGER.info("userId={} not eligible for referral because he came after ={} while allowed comapign time={}", currentUserId, campaignMinutesElapsed, allowedCampaignMinutes);
						userSource.setRefStatus(RefStatusCodes.APP_OPEN_AFTER_CAMPAIGN_TIME);
						userSourceRepo.save(userSource);
						return;
					}
					
					if(!isDecryptedUserIdAndUtmSourceSame) {
						LOGGER.info("userId={} not eligible for referral because utmSource and decryptedUserId are not same, utmSource={}, decryptedUserId={}", currentUserId, utmSource, decryptedUserId);
						userSource.setRefStatus(RefStatusCodes.ID_AND_DECRYPTED_ID_MISMATCH);
						userSourceRepo.save(userSource);
						return;
					}
					
					if (isDecryptedUserIdAndUtmSourceSame && isWithinCampaignTime && isAppVersionEqualBaseAppVersionOrLatestAppVersion
							&& isDeviceCountWithinAllowedCount ) { //  && beforeLoginExisted
						LOGGER.info("userSource's and decrypted userId is same");
						boolean isWithinAllowedTime = minutesDiff < allowedMinutes;
						// followTheReferrerUser(userInner, decryptedUserId);
						performReferralChecks(currentUserId, decryptedUserId, isWithinAllowedTime, ip, deviceToken,
								loginType, userSource);// check
					}
				} catch (Exception ex) {
					LOGGER.error("Exception while extracting utmCampaign, exception={}", ex.getMessage());
					userSource.setRefStatus(RefStatusCodes.UTM_PARAMS_DECRYPTION_OR_PARSING_ERROR);
					userSourceRepo.save(userSource);
					return;
				}
			} catch (GeneralSecurityException | IOException  e) {
				LOGGER.error("Error occured while decrypting utmMedium, exception={}", e.getMessage());
				userSource.setRefStatus(RefStatusCodes.UTM_PARAMS_DECRYPTION_OR_PARSING_ERROR);
				userSourceRepo.save(userSource);
				return;
			}
		} // ends here
	}
	
	private void performReferralChecks(long currentUserId, long utmSourceUserId, boolean isWithinTime, String ip, String deviceToken, String loginType, UserSource userSource) {
		double referralAmt = dbConfigService.getDoubleProperty(Properties.REFERRAL_AMT_DEFAULT.getProperty(), Double.valueOf(Properties.REFERRAL_AMT_DEFAULT.getDefaultValue()));
		// String pushText = dbConfigService.getProperty(Properties.REFERRAL_MSG.getProperty(), Properties.REFERRAL_MSG.getDefaultValue());
		// boolean ipCheckEnabled = dbConfigService.getBooleanProperty(Properties.IS_IP_CHECK_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_IP_CHECK_ENABLED.getDefaultValue()));
		
		// LOGGER.debug("pushText={}", pushText);
		// pushText = String.format(pushText, referralAmt);
		// LOGGER.debug("formattedPushText={}", pushText);
		
		LOGGER.info("referralAmt={}", referralAmt);
		try {
			UserAccount userAccount = userAccountRepo.findByUserId(utmSourceUserId);
			if(userAccount == null) {
				LOGGER.debug("User Account doesn't exist, creating an account");
				userAccount = new UserAccount();
				userAccount.setUserId(utmSourceUserId);
				// Check that referrer user is eligible for referrer 
				boolean isEligible = referralLogicService.randomSelectionForReferral(0, isWithinTime);
				if(!isEligible) {
					LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because of random referralLogic", currentUserId, utmSourceUserId);
					userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_RANDOMNESS);
					userSourceRepo.save(userSource);
				}
				
				LOGGER.info("userId={} referred by utmSourceUserId={} isEligible for referrer={}", currentUserId, utmSourceUserId, isEligible);
				if(isEligible) {
					boolean isBlackedIp = blacklistedIpService.isBlackedIp(ip);
					isEligible = !isBlackedIp;
					if(!isEligible) {
						LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because of blacklisted ip={}", currentUserId, utmSourceUserId, ip);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_BLACKLISTED_IP);
						userSourceRepo.save(userSource);
					}
				}
				
				if(isEligible) {
					long userSourceCountByUtmSourceAndIp = userSourceRepo.countByUtmSourceAndIp(utmSourceUserId+"", ip);
					if(userSourceCountByUtmSourceAndIp > 2) {
						//userSourceRepo.findAllByUtmSourceAndIp(userId, ip).size() > 2;
						// userSourceRepo.findAllByUtmSourceAndIp(userId+"", ip).size() ) > 2
						isEligible = false;
						LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because of more than 2 user from same utmSource and same ip, utmSource={}, ip={}", currentUserId, utmSourceUserId, utmSourceUserId+"", ip);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_MULTI_USER_SAME_IP_SAME_UTM_SOURCE);
						userSourceRepo.save(userSource);
					}
				}
				
				if(isEligible) {
					LOGGER.info("userId={} referred by utmSourceUserId={} is eligible for referrer after all the checks={}", currentUserId, utmSourceUserId, isEligible);
					//userAccount.setCurrentBalance(referralAmt);
					//createUserAccountSummary(userId, referralAmt, "Referral", "Credited for referral");
					// SENDING A MSG
					// pushManagerService.sendPush(userId, pushText, "", "", 5);
					//followTheReferrerUser(userInner, userId);
					//userSource.setRefStatus(1); // changed to COUNTABLE_REF_STATUS
					userSource.setRefStatus(RefStatusCodes.COUNTABLE_REF_STATUS);
					userSourceRepo.save(userSource);
				}
				
				userAccount.setInviteCounter(1);
				userAccountRepo.save(userAccount);
			} else {
				long inviteCounter = userAccount.getInviteCounter();
				
				boolean isQualityCheckEnabled = dbConfigService.getBooleanProperty(Properties.IS_QUALITY_CHECK_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_QUALITY_CHECK_ENABLED.getDefaultValue()));
				int qualityCheckCountLimit = dbConfigService.getIntProperty(Properties.QUALITY_CHECK_COUNTER_LIMIT.getProperty(), Properties.QUALITY_CHECK_COUNTER_LIMIT.getDefaultValueAsInt());
				int notifyCounterStartLimit = dbConfigService.getIntProperty(Properties.NOTIFY_COUNTER_START_LIMIT.getProperty(), Properties.NOTIFY_COUNTER_START_LIMIT.getDefaultValueAsInt());
				int notifyCounterEndLimit = dbConfigService.getIntProperty(Properties.NOTIFY_COUNTER_END_LIMIT.getProperty(), Properties.NOTIFY_COUNTER_END_LIMIT.getDefaultValueAsInt());
				
				if( (inviteCounter == notifyCounterStartLimit ) || (  (inviteCounter % 5 == 0) && inviteCounter != 0  && inviteCounter < notifyCounterEndLimit)) {
					LOGGER.info("sending notification because counter={} of utmSourceUserId={} of userId={}", inviteCounter, utmSourceUserId, currentUserId);
					//List<Object[]> deviceTokensOfUsersWhoHaveBeenReferredByUtmSourceId = userDeviceRepo.getDeviceTokensOfUsersWhoHaveBeenReferredByUtmSourceId(utmSourceUserId+"");
					List<Object[]> deviceTokensOfUsersWhoHaveBeenReferredByUtmSourceId = userDeviceRepo.getDeviceTokensOfUsersWhoHaveBeenReferredByUtmSourceIdWithLanguageId(utmSourceUserId+"");
					try {
						//pushManagerService.sendPushBatchMostRecentNewsOfLanguageId(deviceTokensOfUsersWhoHaveBeenReferredByUtmSourceId, 5);
						pushManagerService.sendPushBatchNotificationBySeparatingTokensOfHindiAndNonHindiUsers(deviceTokensOfUsersWhoHaveBeenReferredByUtmSourceId);
					} catch (Exception ex) {
						LOGGER.info("exception in batch send push for utmSourceUserId={} of userId={}, ex={}", utmSourceUserId, currentUserId, ex.getMessage());
					}
				}
				
				// Quality calculation and insertion / updation
				if(inviteCounter == notifyCounterStartLimit + 1  || ( (inviteCounter % 5 == 1) && inviteCounter != 1 && (inviteCounter < notifyCounterEndLimit + 1) ) ) {
					try {
						long referredUsersByUtmSourceAndActive = userSourceRepo.getCountReferredUsersUtmSourceActive(utmSourceUserId+"", true);
						long referredUsersByUtmSource = userSourceRepo.getCountReferredUsersUtmSource(utmSourceUserId+"");
						double retentionPercent = 0.0;
						if(referredUsersByUtmSourceAndActive != 0) {
							retentionPercent = (referredUsersByUtmSourceAndActive * 100) / (double) referredUsersByUtmSource;
							DecimalFormat df = new DecimalFormat("#.##");
							df.format(retentionPercent);
							
						}
						LOGGER.info("calculated user quality for  utmSourceUserId={} and currentInviteCounter={}, total={}, active={}, retainPercent={}", utmSourceUserId, inviteCounter, referredUsersByUtmSource, referredUsersByUtmSourceAndActive, retentionPercent);
						UserQuality userQuality = new UserQuality();
						userQuality.setActiveCount(referredUsersByUtmSourceAndActive);
						userQuality.setNotRegCount(referredUsersByUtmSource - referredUsersByUtmSourceAndActive);
						userQuality.setRetainPercent(retentionPercent);
						userQuality.setTotalCount(referredUsersByUtmSource);
						userQuality.setUserId(utmSourceUserId);
						userQualityRepo.save(userQuality);
					} catch (Exception ex) {
						LOGGER.error("exception occured while calculating and updating user quality for utmSourceUserId={} of userId={}, ex={}", utmSourceUserId, currentUserId, ex.getMessage());
					}
				}
				
				boolean isEligible;
				LOGGER.info("quality check enabled={}, qualityCheckCountLimit={}", isQualityCheckEnabled, qualityCheckCountLimit);
				if (isQualityCheckEnabled && inviteCounter > qualityCheckCountLimit) {
					UserQuality userQuality = userQualityRepo.findByUserId(utmSourceUserId);
					if (userQuality != null) {
						isEligible = qualityLogicService.getQulityBasedOnRetention(userQuality.getRetainPercent(),
								inviteCounter);
						// ignoring random if counter is less than ignoreCounter 
						int ignoreCounter = dbConfigService.getIntProperty(Properties.IGNORE_COUNTER_LIMIT.getProperty(), Properties.IGNORE_COUNTER_LIMIT.getDefaultValueAsInt());
						if(!isEligible && inviteCounter < ignoreCounter) {
							LOGGER.info("quality logic ignored because inviteCounter={} is less than the ignorableCounter={} for utmSourceUserId={} of userId={}", inviteCounter, ignoreCounter, utmSourceUserId, currentUserId);
							isEligible = true;
						}
						
						if (!isEligible) {
							LOGGER.info(
									"userId={} referred by utmSourceId={} not eligible for referral because of quality check logic",
									currentUserId, utmSourceUserId);
							userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_DUE_TO_RAND_QUALITY_CHECK_LOGIC);
							userSourceRepo.save(userSource);
						}
					} else {
						// the account not found in user quality table, hence random logic
						LOGGER.info("utmSouceUserId={} for userId={} not found in quality check table falling back to random", utmSourceUserId, currentUserId);
						isEligible = referralLogicService.randomSelectionForReferral(inviteCounter, isWithinTime);
						if (!isEligible) {
							LOGGER.info(
								"userId={} referred by utmSourceUserId={} not eligible for referral because of random referralLogic",
								currentUserId, utmSourceUserId);
							userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_RANDOMNESS);
							userSourceRepo.save(userSource);
						}
					}
				} else {
					isEligible = referralLogicService.randomSelectionForReferral(inviteCounter, isWithinTime);
					if (!isEligible) {
						LOGGER.info(
								"userId={} referred by utmSourceUserId={} not eligible for referral because of random referralLogic",
								currentUserId, utmSourceUserId);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_RANDOMNESS);
						userSourceRepo.save(userSource);
					}
				}
				
				// Check that the referrer user is eligible for referrer and the total amount allowed is not exceeded
				/*
				isEligible = referralLogicService.randomSelectionForReferral(inviteCounter, isWithinTime);
				if(!isEligible) {
					LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because of random referralLogic", currentUserId, utmSourceUserId);
					userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_RANDOMNESS);
					userSourceRepo.save(userSource);
				}
				*/
				LOGGER.info("isEligible for referrer={}", isEligible);
				
				// allowed referral count check for google
				if(loginType.equals(Constants.GOOGLE) && isEligible) {
					long allowedGoogleCounter = dbConfigService.getIntProperty(Properties.ALLOWED_GOOGLE_COUNTER.getProperty(), Properties.ALLOWED_GOOGLE_COUNTER.getDefaultValueAsInt());
					if(inviteCounter > allowedGoogleCounter) {
						isEligible = false;
						LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because invite counter greater than allowed for google, allowedGooglecounter={}", currentUserId, utmSourceUserId, allowedGoogleCounter);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_EXCEEDING_ALLOWED_GOOGLE_COUNTER);
						userSourceRepo.save(userSource);
					}
				}
				
				// allowed referral count check for trueCaller
				if(loginType.equals(Constants.TRUE_CALLER) && isEligible) {
					boolean isReferralForTrueCallerEnabled = dbConfigService.getBooleanProperty(Properties.IS_REFERRAL_FOR_TRUE_CALLER_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_REFERRAL_FOR_TRUE_CALLER_ENABLED.getDefaultValue()));
					if(isReferralForTrueCallerEnabled) {
						int allowedTrueCallerCounter = dbConfigService.getIntProperty(Properties.ALLOWED_TRUE_CALLER_COUNTER.getProperty(), Properties.ALLOWED_TRUE_CALLER_COUNTER.getDefaultValueAsInt());
						if(inviteCounter > allowedTrueCallerCounter) {
							isEligible = false;
							// referral exceeds allowed true caller count
							LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because invite counter exceeds allowedTrueCallerCounter={}", currentUserId, utmSourceUserId, allowedTrueCallerCounter);
							userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_DUE_TO_EXCEED_ALLOWED_TRUE_CALLER_COUNTER);
							userSourceRepo.save(userSource);
						} 
					} else {
						// referral for true caller disabled
						isEligible = false;
						LOGGER.info("userId={} referred by utmSourceUserId={} not eligible for referral because referral is disabled for trueCaller, isReferralForTrueCallerEnabled={}", currentUserId, utmSourceUserId, isReferralForTrueCallerEnabled);
						userSource.setRefStatus(RefStatusCodes.REFERRAL_DISABLED_FOR_TRUE_CALLER);
						userSourceRepo.save(userSource);
					}
				}
				
				if(isEligible) {
					boolean isBlackedIp = blacklistedIpService.isBlackedIp(ip);
					isEligible = !isBlackedIp;
					if(!isEligible) {
						LOGGER.error("userId={} referred by utmSourceUserId={} not eligible for referral because of blacklisted ip={}", currentUserId, utmSourceUserId, ip);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_BLACKLISTED_IP);
						userSourceRepo.save(userSource);
					}
				}
				
				if(isEligible ) {
					long userSourceCountByUtmSourceAndIp = userSourceRepo.countByUtmSourceAndIp(utmSourceUserId+"", ip);
					//userSourceRepo.findAllByUtmSourceAndIp(userId, ip).size() > 2;
					//  && (userSourceRepo.findAllByUtmSourceAndIp(utmSourceUserId+"", ip).size() ) > 2
					if(userSourceCountByUtmSourceAndIp > 2) {
						isEligible = false;
						LOGGER.error("userId={} referred by utmSourceUserId={} not eligible for referral because of more than 2 user from same utmSource and same ip, utmSource={}, ip={}", currentUserId, utmSourceUserId, utmSourceUserId+"", ip);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_BY_MULTI_USER_SAME_IP_SAME_UTM_SOURCE);
						userSourceRepo.save(userSource);
					}
				}
				
				LOGGER.info("userId={} referred by utmSourceUserId={} is eligible for referrer after all the checks={}", currentUserId, utmSourceUserId, isEligible);
				if(isEligible) {
					double newTotal = userAccount.getCurrentBalance() + referralAmt;
					double allowedAmt = dbConfigService.getDoubleProperty(Properties.MAX_ALLOWED_AMT.getProperty(), Double.valueOf(Properties.MAX_ALLOWED_AMT.getDefaultValue()));
					//LOGGER.info("new total={}", newTotal);
					if(newTotal <= allowedAmt) {
						//userAccount.setCurrentBalance(newTotal);
						//createUserAccountSummary(userId, referralAmt, "Referral", "Credited for referral");
						// SENDING A MSG
						//followTheReferrerUser(userInner, userId);
						//pushManagerService.sendPush(userId, pushText, "", "", 5);
						// userSource.setRefStatus(1); // changed to COUNTABLE_REF_STATUS
						userSource.setRefStatus(RefStatusCodes.COUNTABLE_REF_STATUS);
						userSourceRepo.save(userSource);
					} else {
						LOGGER.error("The newTotal={} after adding ref amount exceeds the allowed ref amount={}", newTotal, allowedAmt);
						// Send a message that you have exceeded the limit of referral amount
						// pushManagerService.sendPush(userId, "Referral Amount Limit Exceeded for your account", "", "", 5);
						LOGGER.error("userId={} referred by utmSourceUserId={} is eligible but referral amount not added due to exceeding allowed amount, newTotal={}, allowedAmount={}", currentUserId, utmSourceUserId, newTotal, allowedAmt);
						userSource.setRefStatus(RefStatusCodes.NOT_ELIGIBLE_DUE_TO_NEW_TOTAL_EXCEEDING_ALLOWED_TOTAL);
						userSourceRepo.save(userSource);
					}
				}
				userAccount.setInviteCounter(inviteCounter + 1);
				userAccountRepo.save(userAccount);
			}
		} catch(Exception ex) {
			LOGGER.error("Exception occured while processing referral, exception={}", ex.getMessage());
		}
	}
	
	@Override
	public boolean setAddress(long userId, User user, AddressModel address) {
		UserLocation userLocation = userLocationRepo.findUserLocationByUserId(userId);
		boolean isSuccess = false;
		// StatusDto dto = new StatusDto();
		// create user location for the first time
		if (userLocation == null) {
			userLocation = new UserLocation();
			userLocation.setUserId(userId);
		}

		// Check if the user is from Gzb
		boolean isPostalCodeFromGhz = false;
		if (address.getPostalCode() != null) {
			try {
				long postalCode = Long.parseLong(address.getPostalCode());
				if (postalCodesOfCityGhz.contains(postalCode)) {
					LOGGER.info("postalCode={} mapped to Ghz Ghaziabad, city updating from {} to {}", postalCode,
							address.getCity(), Constants.GZB);
					address.setCity(Constants.GZB);
					isPostalCodeFromGhz = true;
				}
			} catch (Exception ex) {
				LOGGER.error("failed to parse postalCode={} as long, exception={}", address.getPostalCode(),
						ex.getMessage());
			}
		}

		// update the user location
		userLocation.setCity(address.getCity());
		userLocation.setCountry(address.getCountry());
		userLocation.setDateTime(new Date());
		userLocation.setLatitude(address.getLatitude());
		userLocation.setLongitude(address.getLongitude());
		userLocation.setPostalCode(address.getPostalCode());
		userLocation.setState(address.getState());

		//User user = userRepo.findByUserId(userId);
		if (address.getState() != null) {
			user.setState(address.getState());
		}

		if (address.getCity() != null) {
			CityMapping mappedCity = cityMappingRepo.findByAlternateNamesContaining(address.getCity());
			if (mappedCity != null) {
				user.setDist(mappedCity.getCityId() + ""); // mappedCity.getCityId();
			}
		}
		user = userRepo.save(user);

		try {
			userLocationRepo.save(userLocation);
			isSuccess = true;
			//dto.setStatus("success");
		} catch (Exception ex) {
			LOGGER.error("failed to save address to UserLocation");
			//dto.setStatus("fail");
		}

		try {
			UserSource userSource = userSourceRepo.findByUserId(userId);
			int refStatus = userSource.getRefStatus();
			String utmSource = userSource.getUtmSource();

			long utmSourceId = 0;
			try {
				utmSourceId = Long.valueOf(utmSource);
			} catch (Exception ex) {
				LOGGER.error("Error while parsing utmSource, exception={}", ex.getMessage());
			}

			// Check if postal code of utmSourceId is also from Ghz
			boolean isAlsoPostalcodeOfUtmSourceFromGhz = false;
			if (utmSourceId != 0) {
				try {
					UserLocation userLocationOfUtmSourceUserId = userLocationRepo.findUserLocationByUserId(utmSourceId);
					if (userLocationOfUtmSourceUserId != null
							&& userLocationOfUtmSourceUserId.getPostalCode() != null) {
						long postalCodeOfUtmSourceId = Long.parseLong(userLocationOfUtmSourceUserId.getPostalCode());
						isAlsoPostalcodeOfUtmSourceFromGhz = postalCodesOfCityGhz.contains(postalCodeOfUtmSourceId);
						LOGGER.info("The utmSourceId={} of userId={} is also from Ghz", utmSourceId, userId);
					}
				} catch (Exception ex) {
					LOGGER.error(
							"something went wrong when querying for user location of utmSourceId={} of userId={}, exception={}",
							utmSourceId, userId, ex.getMessage());
				}
			}

			LOGGER.info("userSource's refStatus for this user={}", refStatus); // if(refStatus == 1)

			boolean isElectionCampaignEnabled = dbConfigService.getBooleanProperty(Properties.IS_ELECTION_CAMPAIGN_ON.getProperty(), Boolean.valueOf(Properties.IS_ELECTION_CAMPAIGN_ON.getDefaultValue()));
			String exceptionalCities = dbConfigService.getProperty(Properties.EXCEPTION_CITIES.getProperty(), Properties.EXCEPTION_CITIES.getDefaultValue());
			String exceptionalState = dbConfigService.getProperty(Properties.EXCEPTIONAL_STATE.getProperty(), Properties.EXCEPTIONAL_STATE.getDefaultValue());
			LOGGER.info("exceptionCities={}, exceptionalState={}", exceptionalCities, exceptionalState);

			// ADDITIONAL condition of ignoring randomness for gzb city and pushing to
			// leaderboard added.
			if (refStatus == RefStatusCodes.COUNTABLE_REF_STATUS || (isElectionCampaignEnabled
					&& (refStatus == RefStatusCodes.NOT_ELIGIBLE_BY_RANDOMNESS
							|| refStatus == RefStatusCodes.NOT_ELIGIBLE_DUE_TO_RAND_QUALITY_CHECK_LOGIC)
					&& isPostalCodeFromGhz && isAlsoPostalcodeOfUtmSourceFromGhz)) { // address.getCity().equals(Constants.GZB)

				if (utmSourceId != 0) {
					// double referralAmt =
					// dbConfigService.getDoubleProperty(Properties.REFERRAL_AMT.getProperty(),
					// Double.valueOf(Properties.REFERRAL_AMT.getDefaultValue()));
					String pushText = dbConfigService.getProperty(Properties.REFERRAL_MSG.getProperty(), Properties.REFERRAL_MSG.getDefaultValue());
					// pushText = String.format(pushText, referralAmt);

					// referral flow
					LOGGER.info("successfully parsed utmSource as long, utmSourceId={}, formattedPushText={}",
							utmSourceId, pushText);
					UserAccount userAccount = userAccountRepo.findByUserId(utmSourceId);

					if (userAccount == null) {
						// User Account doesn't exist, also it's for first time
						userAccount = new UserAccount();
						userAccount.setUserId(utmSourceId);

						int amountStartRangeForFirstInviteCounter = dbConfigService.getIntProperty(Properties.REF_AMT_FIRST_COUNTER_RANGE_START.getProperty(), Properties.REF_AMT_FIRST_COUNTER_RANGE_START.getDefaultValueAsInt());
						int amountEndRangeForFirstInviteCounter = dbConfigService.getIntProperty(Properties.REF_AMT_FIRST_COUNTER_RANGE_END.getProperty(), Properties.REF_AMT_FIRST_COUNTER_RANGE_END.getDefaultValueAsInt());
						int referralAmount = random.nextInt(amountEndRangeForFirstInviteCounter - amountStartRangeForFirstInviteCounter + 1) + amountStartRangeForFirstInviteCounter;
						LOGGER.info("referral amount for utmSourceUserId={} for fist time randomally given is={}",
								utmSourceId, referralAmount);
						userAccount.setCurrentBalance(referralAmount);

						createUserAccountSummary(utmSourceId, referralAmount, "Referral", "Credited for referral");
						
						// disabling follower functionality for non upload status users.
						if(user.getUploadStatus() == 1)
						{
							followTheReferrerUser(user, utmSourceId);
						}
						userAccountRepo.save(userAccount);
						// formatting push text corresponding to referral amount
						pushText = String.format(pushText, referralAmount);
						pushManagerService.sendPush(utmSourceId, pushText, "", "", 5);
					} else {
						long inviteCounter = userAccount.getInviteCounter();
						int qualityCheckCountLimit = dbConfigService.getIntProperty(Properties.QUALITY_CHECK_COUNTER_LIMIT.getProperty(), Properties.QUALITY_CHECK_COUNTER_LIMIT.getDefaultValueAsInt());

						double referralAmount = 0;
						if (inviteCounter == 1) {
							int amountStartRangeForFirstInviteCounter = dbConfigService.getIntProperty(Properties.REF_AMT_FIRST_COUNTER_RANGE_START.getProperty(), Properties.REF_AMT_FIRST_COUNTER_RANGE_START.getDefaultValueAsInt());
							int amountEndRangeForFirstInviteCounter = dbConfigService.getIntProperty(Properties.REF_AMT_FIRST_COUNTER_RANGE_END.getProperty(), Properties.REF_AMT_FIRST_COUNTER_RANGE_END.getDefaultValueAsInt());
							referralAmount = random.nextInt(amountEndRangeForFirstInviteCounter - amountStartRangeForFirstInviteCounter + 1) + amountStartRangeForFirstInviteCounter;
							LOGGER.info("random referral amount={} for one invite counter of utmSourceUserId={}",
									referralAmount, utmSourceId);
						} else if (inviteCounter <= qualityCheckCountLimit) {
							int amountStartRangeForLessThanThreeInviteCounter = dbConfigService.getIntProperty(Properties.REF_AMT_OTHER_COUNTER_RANGE_START.getProperty(), Properties.REF_AMT_OTHER_COUNTER_RANGE_START.getDefaultValueAsInt());
							int amountEndRangeForLessThanThreeInviteCounter = dbConfigService.getIntProperty(Properties.REF_AMT_OTHER_COUNTER_RANGE_END.getProperty(), Properties.REF_AMT_OTHER_COUNTER_RANGE_END.getDefaultValueAsInt());
							referralAmount = random.nextInt(amountEndRangeForLessThanThreeInviteCounter - amountStartRangeForLessThanThreeInviteCounter + 1) + amountStartRangeForLessThanThreeInviteCounter;
							LOGGER.info(
									"random referral amount={} for more than one invite counter and upto={} for utmSourceUserId={}",
									referralAmount, qualityCheckCountLimit, utmSourceId);
						} else {
							boolean isQualityCheckEnabled = dbConfigService.getBooleanProperty(Properties.IS_QUALITY_CHECK_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_QUALITY_CHECK_ENABLED.getDefaultValue()));
							// Check for user quality having invite counter greater than configured qualityCheckCountLimit and if enabled
							if(isQualityCheckEnabled) {
								UserQuality userQuality = userQualityRepo.findByUserId(utmSourceId);
								if (userQuality == null) {
									// the user quality records doesn't exist hence falling back to default amount
									referralAmount = dbConfigService.getDoubleProperty(Properties.REFERRAL_AMT_DEFAULT.getProperty(), Double.valueOf(Properties.REFERRAL_AMT_DEFAULT.getDefaultValue()));
									LOGGER.info(
											"default referral amount={} for utmSourceUserId={} with no  user quality record",
											referralAmount, utmSourceId);
								} else {
									// give amount according to retentention percent
									double retainPercent = userQuality.getRetainPercent();
									referralAmount = qualityLogicService.getAmountBasedOnRetention(retainPercent);
									LOGGER.info(
											"fixed referral amount={} for this utmSourceUserId={} with retaintion percent={}",
											referralAmount, utmSourceId, retainPercent);
								}
							} else {
								referralAmount = dbConfigService.getDoubleProperty(Properties.REFERRAL_AMT_DEFAULT.getProperty(), Double.valueOf(Properties.REFERRAL_AMT_DEFAULT.getDefaultValue()));
								LOGGER.info(
										"default referral amount={} for utmSourceUserId={} with quality check disabled",
										referralAmount, utmSourceId);
							}
						}

						if (referralAmount != 0) {
							double newTotal = userAccount.getCurrentBalance() + referralAmount;
							LOGGER.info("newTotal={} in setAddr for utmSourceId={}", newTotal, utmSourceId);
							userAccount.setCurrentBalance(newTotal);

							createUserAccountSummary(utmSourceId, referralAmount, "Referral", "Credited for referral");
							
							// disabling follower functionality for non upload status users.
							if(user.getUploadStatus() == 1)
							{
								followTheReferrerUser(user, utmSourceId);
							}
							userAccountRepo.save(userAccount);
							// formatting push text corresponding to referral amount
							pushText = String.format(pushText, referralAmount);
							pushManagerService.sendPush(utmSourceId, pushText, "", "", 5);
						} else {
							LOGGER.error(
									"failed to determine the referral amount for utmSourceUserId={} of userId={}, check for exception",
									utmSourceId, userId);
						}

					}

					// ADDED for GZB leaderboard
					if (isElectionCampaignEnabled && isPostalCodeFromGhz && isAlsoPostalcodeOfUtmSourceFromGhz
							&& utmSourceId > Constants.THE_FIRST_USER_ID_ON_FEB_23) {
						if (leaderboardService.incrementReferralCounterOfUserOfSpecificCity(utmSourceId,
								Constants.GZB)) {
							LOGGER.info(
									"successfully incremented leaderboard counter for utmSourceId={} for city={}, exceptionalCities={}, exceptionalState={}",
									utmSourceId, address.getCity(), exceptionalCities, exceptionalState);
						} else {
							LOGGER.error(
									"failed to incremnt leaderboard counter for utmSourceId={} for city={}, exceptionalCities={}, exceptionalState={}",
									utmSourceId, address.getCity(), exceptionalCities, exceptionalState);
						}
					}
				}
				// userSource.setRefStatus(200);
				// CHANGED FROM 200 TO 201 FOR DIFFERENTIATING THE ONES WHO CAME AFTER
				// 16-10-2020
				userSource.setRefStatus(RefStatusCodes.SUCCESSFULLY_PROCESSED_REF_STATUS);
				userSourceRepo.save(userSource);
			}

		} catch (Exception ex) {
			LOGGER.error("Error occured while processing referral in set address, exception={}", ex.getMessage());
		}

		//LOGGER.info("setAddress status = {} for userId = {} ", dto.getStatus(), userId);
		LOGGER.info("setAddress isSuccess={} for userId={}", isSuccess, userId);
		//return dto;
		return isSuccess;
	}
	
	@Override
	public InsuranceDto getInsurance(long userId, int languageId) {
		UserInsurance userInsurence = userInsuranceRepo.findByUserId(userId);
		if (userInsurence == null) {
			LOGGER.error("User with userId={} was not found in UserInsurance ", userId);
			//throw new RecordNotFoundException("User with userId=" + userId + " was not found in UserInsurance");
			throw new NoContentAvailableException("User with userId=" + userId + " was not found in UserInsurance");
		}

		String termsLink = dbConfigService.getProperty(Properties.TERMS_LINK.getProperty(),
				Properties.TERMS_LINK.getDefaultValue());
		List<String> termsPoints = dbConfigService.getPropertyListByLanguageId(Properties.TNC_POPUP_LIST.getProperty(),
				Arrays.asList(Properties.TNC_POPUP_LIST.getDefaultValue().split("\n")), languageId);
		String policyDetailUrl = dbConfigService.getProperty(Properties.POLICY_DETAIL_URL.getProperty(),
				Properties.POLICY_DETAIL_URL.getDefaultValue());
		String policyNumber = dbConfigService.getProperty(Properties.POLICY_NUMBER.getProperty(),
				Properties.POLICY_NUMBER.getDefaultValue());
		String helplineNumber = dbConfigService.getProperty(Properties.HELPLINE_NUMBER.getProperty(),
				Properties.HELPLINE_NUMBER.getDefaultValue());
		String helplineEmail = dbConfigService.getProperty(Properties.HELPLINE_EMAIL.getProperty(),
				Properties.HELPLINE_EMAIL.getDefaultValue());

		InsuranceDto dto = mapper.userInsurenceToInsuranceDto(userInsurence, termsLink, termsPoints, policyDetailUrl,
				policyNumber, helplineNumber, helplineEmail);
		String sumInsuredAmount = dbConfigService.getProperty(Properties.SUM_INSURED_AMOUNT.getProperty(), Properties.SUM_INSURED_AMOUNT.getDefaultValue());
		dto.setSumInsuredAmount(sumInsuredAmount);
		LOGGER.debug("Added sum insured amount");
		
		return dto;
	}
	
	@Override
	public StatusDto setState(long userId, int stateId, int languageId) {
		StatusDto dto = new StatusDto();
		State state = stateRepo.findByLanguageIdAndStateId(languageId, stateId);
		if(state == null) {
			LOGGER.debug("state for languageId={} and stateId={} doesn't exist", languageId, stateId);
			dto.setStatus("fail");
			return dto;
		}
		User user = userRepo.findByUserId(userId);  
		user.setState(state.getStateName());
		userRepo.save(user);     
		dto.setStatus("success");
		return dto;
	}
	
	
	@Override
	public StatusDto setStateWithCity(long userId, int stateId,  int cityId,int languageId) {
		//userId, stateId,cityId, langId
		StatusDto dto = new StatusDto();
		State state = stateRepo.findByLanguageIdAndStateId(languageId, stateId);
		if(state == null) {
			LOGGER.debug("setStateWithCity for languageId={} and stateId={} doesn't exist", languageId, stateId);
			dto.setStatus("fail");
			return dto;
		}
		UserLocation userLocation = userLocationRepo.findUserLocationByUserId(userId);
		
		if(userLocation != null) {
		
			City city=cityRepo.findByCityId(cityId);
			
			List<PinCodeMapping> pin= pinCodeMappingRepo.findByCityId(city.getId());
			if(pin.size()>0)
			{
				
				userLocation.setPostalCode(String.valueOf(pin.get(0).getPinCode()));
				userLocationRepo.save(userLocation);
			}
			//userLocation.setPostalCode("");
			//userLocationRepo.save(userLocation);;
		}
		
		User user = userRepo.findByUserId(userId);  
		user.setState(state.getStateName());
		userRepo.save(user);     
		dto.setStatus("success");
		return dto;
	}
	
	@Override
	public String sendOtp(long mobileNumber) {
		if(!Utils.isTenDigitLongNumber(mobileNumber)) {
			LOGGER.error("invalid mobile number={}", mobileNumber);
			throw new IllegalArgumentException("invalid mobile number");
		}
		String otp = String.valueOf(otpService.generateOtp(String.valueOf(mobileNumber)) );
		LOGGER.debug("otp in sendOtp method={}", otp);	
		String msg = String.format(msgTemplate, otp);
		LOGGER.debug("formatted msg={}", msg);
		boolean sentStatus = false;
		try {
			Utils.sendUDP(mobileNumber + "#" +  msg + "#RESEND", ip, port);
			sentStatus = true;
		} catch (Exception ex) {
			LOGGER.error("error while sending UDP, exception={}", ex.getMessage());
		}
		return sentStatus ? "success" : "failure";
	}
	
	private UserSource createUserSource(long userId, String utmSource, String utmMedium, String utmTerm,
			String utmCampaign, String ip, String deviceId) {
		UserSource userSource = new UserSource(); 
		//new UserSource(userId, utmSource, utmCampaign, utmMedium, utmSource, utmTerm, ip, deviceId);
		userSource.setUserId(userId);
		userSource.setUtmSource(utmSource);
		userSource.setUtmMedium(utmMedium);
		userSource.setUtmTerm(utmTerm);
		userSource.setUtmCampaign(utmCampaign);
		userSource.setDeviceId(deviceId);
		
		// Added IP to be inserted
		userSource.setIp(ip);
		
		userSource.setRefStatus(0);
		
		userSource = userSourceRepo.save(userSource);
		LOGGER.debug("creation of user source success");
		return userSource;
	}

	private void createUserProfile(long userId, int languageId, String profilePick, String name,
			String loginType, String androidVersion, String token) {
		UserProfile userProfile = new UserProfile();
		//UserProfile userProfile = new UserProfile(userId, languageId, profilePick, name, loginType, androidVersion, token);
		userProfile.setUserId(userId);
		userProfile.setLanguageId(languageId);
		userProfile.setProfilePick(profilePick);
		userProfile.setName(name);
		userProfile.setLoginType(loginType);
		userProfile.setAndroidVersion(androidVersion);
		//userProfile.setToken(""); // to be discontinued later
		
		try {
			userProfileRepo.save(userProfile);
			LOGGER.debug("creation of user profile success");
		} catch (GenericJDBCException ex) {
			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(name);
			boolean b = m.find();
			if(name != null && name != "" && b) {
				userProfile.setName("");
				userProfileRepo.save(userProfile);
			}
		}
		
	}
	
	private void createUserAccount(long userId, double initialBalance) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUserId(userId);
		userAccount.setCurrentBalance(initialBalance);
		userAccount.setInviteCounter(0);
		
		try {
			userAccountRepo.save(userAccount);
			LOGGER.debug("creation of user account success");
		} catch (Exception ex) {
			LOGGER.error("failed to create user account, exception={}", ex.getMessage());
		}
	}
	
	private void createUserAccountSummary(long userId, double amount, String offerName, String remarks) {
		UserAccountSummary userAccountSummary = new UserAccountSummary();
		userAccountSummary.setUserId(userId);
		userAccountSummary.setCreatedTime(new Date());
		userAccountSummary.setAmount(amount);
		userAccountSummary.setOfferName(offerName);
		userAccountSummary.setRemarks(remarks);

		try {
			userAccountSummaryRepo.save(userAccountSummary);
			LOGGER.debug("creation/ entering of user account summary success");
		} catch (Exception ex) {
			LOGGER.error("error while entering user account summary, exception={}", ex.getMessage());
		}
	}

	private void createOrUpdateUserDevice(long userId, String deviceId, String deviceName, String make, String model,
			String timeOfDevice, String deviceToken) {
		UserDevice userDevice = userDeviceRepo.findByUserId(userId);
		if(userDevice == null) {
			userDevice = new UserDevice();
			userDevice.setUserId(userId);
		}
		userDevice.setDeviceId(deviceId);
		userDevice.setDeviceName(deviceName);
		userDevice.setMake(make);
		userDevice.setModel(model);
		userDevice.setTimeOfDevice(timeOfDevice);
		userDevice.setDeviceToken(deviceToken);
		userDevice.setActive(true);
		
		try {
			userDeviceRepo.save(userDevice);
			LOGGER.debug("creation/updation of user device success");
		} catch(Exception ex) {
			LOGGER.debug("failed to save user device creation/updation, ex={}", ex.getMessage());
		}
	}
	
	@Transactional
	private void followTheReferrerUser(User newUser, long decryptedSourceUserId) {
		// Increment the follower Count of decrytedSourceUserId
		User user = userRepo.findByUserId(decryptedSourceUserId);
		Long tempCount2 = user.getFollowers();
		long followCount;
		if(tempCount2 == null) {
			followCount = 1;
		} else {
			followCount = tempCount2 + 1;
		}
		user.setFollowers(followCount);
		userRepo.save(user);
		LOGGER.debug("successfully incremented follower count of referrerUser={}", decryptedSourceUserId);
		
		
		// Increment the following count for newUser
		Long tempCount1 = newUser.getFollowing();
		if(tempCount1 == null) {
			tempCount1 = 0l;
		}
		newUser.setFollowing(tempCount1 + 1);
		userRepo.save(newUser);
		LOGGER.debug("successfully incremented following count of followerUser={}", newUser.getUserId());
		
		// The new user should be following the source user
		Follow follow = new Follow();
		follow.setUserId(decryptedSourceUserId);
		follow.setFollower(newUser.getUserId());
		follow.setDateTime(new Date());
		follow.setFollowType(0l); // user to user, not reporter, hence 0
		
		followRepo.save(follow);
	}
}


