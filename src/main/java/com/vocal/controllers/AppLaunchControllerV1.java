package com.vocal.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.City;
import com.vocal.entities.CityMapping;
import com.vocal.entities.NewsCategory;
import com.vocal.entities.PinCodeMapping;
import com.vocal.entities.State;
import com.vocal.entities.StateMapping;
import com.vocal.entities.User;
import com.vocal.entities.UserAccount;
import com.vocal.entities.UserLocation;
import com.vocal.entities.UserProfile;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.mapper.Mapper;
import com.vocal.repos.jpa.CityMappingRepo;
import com.vocal.repos.jpa.CityRepo;
import com.vocal.repos.jpa.NewsCategoryRepo;
import com.vocal.repos.jpa.PinCodeMappingRepo;
import com.vocal.repos.jpa.StateMappingRepo;
import com.vocal.repos.jpa.StateRepo;
import com.vocal.repos.jpa.UserAccountRepo;
import com.vocal.repos.jpa.UserLocationRepo;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.AppPopupService;
import com.vocal.services.DbConfigService;
import com.vocal.services.UserService;
import com.vocal.utils.AES;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Constants;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.AppLaunchDto;
import com.vocal.viewmodel.AppPopupDto;
import com.vocal.viewmodel.CreateNewsPopupDto;
import com.vocal.viewmodel.KycDto;
import com.vocal.viewmodel.LevelMessage;
import com.vocal.viewmodel.NewsCategoryDto;
import com.vocal.viewmodel.ReportDto;
import com.vocal.viewmodel.Ticker;

@RestController
public class AppLaunchControllerV1 {
	
	@Autowired
	private NewsCategoryRepo newsCategoryRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private AppPopupService appPopupService;

	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private StateMappingRepo stateMappingRepo;
	
	@Autowired
	private UserAccountRepo userAccountRepo;
	
	@Autowired
	private UserProfileRepo userProfileRepo;
	
	@Autowired
	private UserLocationRepo userLocationRepo;
	
	@Autowired
	private CityMappingRepo cityMapingRepo;
	
	@Autowired
	private PinCodeMappingRepo pinCodeMappingRepo;
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private Mapper mapper;

	@Value("${HOST_URL}")
	private String HOST_URL;

	@Value("${HOST_DIR}")
	private String HOST_DIR;
	
	@Value("${SECRET_KEY}")
	private String encryptionKey;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLaunchControllerV1.class);
	
	@PostMapping(value = "/v1/AppLaunch")
	public ResponseEntity<?> appLaunch(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp, 
			@RequestParam(value = CommonParams.LANGUAGE_ID, defaultValue = "1") int languageId,
			@RequestParam(value = CommonParams.LANG_CHANGE, defaultValue="false") boolean langChange,
			@RequestParam(value = CommonParams.VERSION) String appVersion,
			HttpServletRequest request)
			throws UnsupportedEncodingException {
		LOGGER.info("/v1/AppLaunch userid = {}, otp = {}, langId = {}, appVersion={}", userId, otp, languageId, appVersion);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		// UPDATING updatedTime and appVersion FIELD OF THE USER
		user.setUpdatedTime(new Date());
		user.setAppVersion(appVersion);
		String stateName = user.getState();
		Integer otpVerify = user.getOtpVerify();
		String mobile = user.getMobile();
		userRepo.save(user);
		
		LOGGER.debug("State from user table={}", stateName);

		// GETTING CATEGORIES FROM news_category
		List<NewsCategory> newsCategory = newsCategoryRepo.findByLanguageIdAndStatusOrderByPriorityAsc(languageId, 1);
		// TODO : replace with Apache-common-langs, StringUtils, CollectionUtils
		if (newsCategory == null || newsCategory.size() == 0) {
			LOGGER.error("No categories found for languageid={}", languageId);
		}
		
		List<StateMapping> mappedStates = null;
		if(stateName != null) {
			LOGGER.debug("Trying to fetch states");
			mappedStates = stateMappingRepo.findByAlternateNamesContaining(stateName);
			LOGGER.debug("Successfully fetched, mappedstates={}", mappedStates.toString());
		}
		State state = null; 
		if(mappedStates == null || mappedStates.size() == 0 ) {
			LOGGER.error("No mapping found for the state={} in state_mapping", stateName);
		} 
		else if(mappedStates.size() > 1) {
			LOGGER.warn("More than one mapping found for the state={}", stateName);
		} else {
			state = stateRepo.findByLanguageIdAndStateId(languageId, mappedStates.get(0).getStateId());
		}

		// PREPAIRING CATEGORY DTO
		List<NewsCategoryDto> categoryDtoList;
		if (state == null) {
			LOGGER.warn("State with alternateName={} for languageId={} doesn't exist", stateName, languageId);
			categoryDtoList = mapper.newsCategoryListToNewsCategoryDtoList(newsCategory);
		} else {
			// INCLUDING STATE AS A CATEGORY 
			categoryDtoList = mapper.newsCategoryListToNewsCategoryDtoList(newsCategory, state); 
		}
		
		// INCLUDING CITY AS CAGEGORY DTO
		UserLocation userLocation = userLocationRepo.findUserLocationByUserId(userId);
		/*if(userLocation != null) {
			String city = userLocation.getCity();
			//NewsCategory cityMappedCat = null;
			CityMapping mappedCity = null;
			if(city != null && !city.equals("")) {
				//cityMappedCat = newsCategoryRepo.findByCategoryName(city);
				mappedCity = cityMapingRepo.findByAlternateNamesContaining(city);
			}
			/*if(cityMappedCat != null) {
				categoryDtoList.add(3, new NewsCategoryDto(cityMappedCat.getCategoryId(), cityMappedCat.getCategoryName(), "News", ""));
			}*/
			/*if(mappedCity != null) {
				//City cityCat = cityRepo.findByCityIdAndLanguageId(mappedCity.getCityId(), languageid);
				City cityCat = cityRepo.findByLanguageIdAndId(languageId, mappedCity.getCityId());
				if(cityCat != null) {
					categoryDtoList.add(3, new NewsCategoryDto(cityCat.getCategoryId(), cityCat.getCityName(), "News", ""));
				}
			}
		}*/
		
		if(userLocation != null) {
			
			String pinCode = userLocation.getPostalCode();
			
			PinCodeMapping mappedPin = null;
			if(pinCode != null && !pinCode.equals("")) {
				
				mappedPin = pinCodeMappingRepo.findByPinCode(Long.parseLong(pinCode));
			}
		
			if(mappedPin != null) {
				
				City cityCat = cityRepo.findByLanguageIdAndId(languageId, mappedPin.getCityId());
				if(cityCat != null) {
					categoryDtoList.add(0, new NewsCategoryDto(cityCat.getCategoryId(), cityCat.getCityName(), "News", ""));
				}
			}
		}

		// GETTING REPORT OPTIONS 
		List<String> reportList = dbConfigService.getPropertyListByLanguageId(
				Properties.REPORT_REASONS_LIST.getProperty(),
				Arrays.asList(Properties.REPORT_REASONS_LIST.getDefaultValue().split(",")), languageId);
		List<ReportDto> reportDtoList = mapper.propertyListToReportDtoList(reportList);

		// GETTING UPDATED VALUES FOR PROPERTIES, IF UNAVAILABLE THEN USE HARDCODED ONES
		// FROM ENUM FILE
		String shareText = dbConfigService.getProperty(Properties.NEWS_SHARE_URL.getProperty(),
				Properties.NEWS_SHARE_URL.getDefaultValue());

		int min = dbConfigService.getIntProperty(Properties.MINIMUM.getProperty(),
				Properties.MINIMUM.getDefaultValueAsInt());

		int max = dbConfigService.getIntProperty(Properties.MAXIMUM.getProperty(),
				Properties.MAXIMUM.getDefaultValueAsInt());

		String inviteUrl = dbConfigService.getProperty(Properties.INVITE_URL.getProperty(),
				Properties.INVITE_URL.getDefaultValue());
		
		// for an encrypted timeStamp and key
		String key = "";
		try {
			inviteUrl= inviteUrl + "&key=" + AES.encrypt(userId + "^" + String.valueOf(System.currentTimeMillis()), encryptionKey);
			key= "key=" + AES.encrypt(userId + "_" + otp + "_" + String.valueOf(System.currentTimeMillis()), encryptionKey);
		} catch (Exception e) {
			LOGGER.error("Exception while adding encrypted timeStamp, exception={}", e.getMessage());
		}

		String inviteText = dbConfigService.getPropertyByLanguageId(Properties.INVITE_TEXT.getProperty(),
				Properties.INVITE_TEXT.getDefaultValue(), languageId);

		String reportText = dbConfigService.getPropertyByLanguageId(Properties.REPORT_TEXT.getProperty(),
				Properties.REPORT_TEXT.getDefaultValue(), languageId);

		// Preparing CreateNewsPopup field
		String camera = dbConfigService.getPropertyByLanguageId(Properties.CAMERA.getProperty(),
				Properties.CAMERA.getDefaultValue(), languageId);

		String galary = dbConfigService.getPropertyByLanguageId(Properties.GALARY.getProperty(),
				Properties.GALARY.getDefaultValue(), languageId);

		String chooseOptions = dbConfigService.getPropertyByLanguageId(Properties.CHOOSE_OPTIONS.getProperty(),
				Properties.CHOOSE_OPTIONS.getDefaultValue(), languageId);

		CreateNewsPopupDto popUpDto = new CreateNewsPopupDto(camera, galary, chooseOptions);

		// Preparing KYC field
		String popUpTitle = dbConfigService.getPropertyByLanguageId(Properties.TNC_HEADING.getProperty(),
				Properties.TNC_HEADING.getDefaultValue(), languageId);

		List<String> popUp = dbConfigService.getPropertyListByLanguageId(Properties.TNC_POPUP_LIST.getProperty(),
				Arrays.asList(Properties.TNC_POPUP_LIST.getDefaultValue().split("\n")), languageId);

		String kycDone = dbConfigService.getPropertyByLanguageId(Properties.KYC_DONE_MSG.getProperty(),
				Properties.KYC_DONE_MSG.getDefaultValue(), languageId);
		
		String tnc = dbConfigService.getProperty(Properties.TERMS_LINK.getProperty(), Properties.TERMS_LINK.getDefaultValue());

		KycDto kycDto = new KycDto(popUpTitle, popUp, kycDone);
		kycDto.setTnc(tnc);

		String recommendationUrl = HOST_URL + HOST_DIR + "/getRecommendedNews";
		recommendationUrl = dbConfigService.getProperty(Constants.RECOMMENDATION_URL, recommendationUrl);
		LOGGER.debug("recommendation url={}", recommendationUrl);

		AppLaunchDto appLaunchDto = new AppLaunchDto(shareText, categoryDtoList, min, inviteUrl, max, popUpDto,
				reportDtoList, inviteText, reportText, recommendationUrl);
		appLaunchDto.setKyc(kycDto);
		
		boolean adsEnabled = dbConfigService.getBooleanProperty(Properties.BANNER_ADS_ENABLED.getProperty(), Boolean.valueOf(Properties.BANNER_ADS_ENABLED.getDefaultValue()));
		LOGGER.debug("BannerAds enabled = {}", adsEnabled);
		appLaunchDto.setAdsEnabled(adsEnabled);
		
		// get Appropriate AppPopup dto from app popup service
		// AppPopupDto appPopup = appPopupService.getAppropriatePopupDto(userId, appVersion, languageId, currentUser.getCreatedTime());
		try {
			AppPopupDto appPopup = appPopupService.getPrioritizedPopupDto(userId, appVersion, languageId, user.getCreatedTime());
			appLaunchDto.setApp_popup(appPopup);
		} catch (Exception ex) {
			LOGGER.warn("some exception occured while adding app popup, exception={}", ex.getMessage());
		}
		
		
		String stateNames = "Uttar Pradesh Bihar बिहार उत्तप्रदेश उत्तर प्रदेश  ";
		if(stateName != null && !stateName.equals("") && stateNames.contains(stateName)) {
			LOGGER.debug("User is either from Bihar or Uttar Pradesh, state={}", stateName);
			// Getting Level and Message
			List<LevelMessage> levelList = new ArrayList<LevelMessage>();
			String levelOneMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_ONE_SPL_MSG.getProperty(), Properties.LEVEL_ONE_SPL_MSG.getDefaultValue(), languageId);
			String levelTwoMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_TWO_SPL_MSG.getProperty(), Properties.LEVEL_TWO_SPL_MSG.getDefaultValue(), languageId);
			String levelThreeMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_THREE_SPL_MSG.getProperty(), Properties.LEVEL_THREE_SPL_MSG.getDefaultValue(), languageId);
			String levelFourMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_FOUR_SPL_MSG.getProperty(), Properties.LEVEL_FOUR_SPL_MSG.getDefaultValue(), languageId);
			// Added the field headline with levelMessage as on 30Sep 
			String levelOneHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_ONE_SPL_HEAD.getProperty(), Properties.LEVEL_ONE_SPL_HEAD.getDefaultValue(), languageId);
			String levelTwoHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_TWO_SPL_HEAD.getProperty(), Properties.LEVEL_TWO_SPL_HEAD.getDefaultValue(), languageId);
			String levelThreeHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_THREE_SPL_HEAD.getProperty(), Properties.LEVEL_THREE_SPL_HEAD.getDefaultValue(), languageId);
			String levelFourHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_FOUR_SPL_HEAD.getProperty(), Properties.LEVEL_FOUR_SPL_HEAD.getDefaultValue(), languageId);
			levelList.add(new LevelMessage(1, levelOneMsg, levelOneHeadline));
			levelList.add(new LevelMessage(2, levelTwoMsg, levelTwoHeadline));
			levelList.add(new LevelMessage(3, levelThreeMsg, levelThreeHeadline));
			levelList.add(new LevelMessage(4, levelFourMsg, levelFourHeadline));
			appLaunchDto.setReporterLevelMessage(levelList);
		} else {
			LOGGER.debug("User is not from Bihar or Uttar Pradesh, state={}", stateName);
			// Getting Level and Message
			List<LevelMessage> levelList = new ArrayList<LevelMessage>();
			String levelOneMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_ONE_MSG.getProperty(), Properties.LEVEL_ONE_MSG.getDefaultValue(), languageId);
			String levelTwoMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_TWO_MSG.getProperty(), Properties.LEVEL_TWO_MSG.getDefaultValue(), languageId);
			String levelThreeMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_THREE_MSG.getProperty(), Properties.LEVEL_THREE_MSG.getDefaultValue(), languageId);
			String levelFourMsg = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_FOUR_MSG.getProperty(), Properties.LEVEL_FOUR_MSG.getDefaultValue(), languageId);
			// Added the field headline with levelMessage as on 30Sep 
			String levelOneHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_ONE_HEAD.getProperty(), Properties.LEVEL_ONE_HEAD.getDefaultValue(), languageId);
			String levelTwoHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_TWO_HEAD.getProperty(), Properties.LEVEL_TWO_HEAD.getDefaultValue(), languageId);
			String levelThreeHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_THREE_HEAD.getProperty(), Properties.LEVEL_THREE_HEAD.getDefaultValue(), languageId);
			String levelFourHeadline = dbConfigService.getPropertyByLanguageId(Properties.LEVEL_FOUR_HEAD.getProperty(), Properties.LEVEL_FOUR_HEAD.getDefaultValue(), languageId);
			levelList.add(new LevelMessage(1, levelOneMsg, levelOneHeadline));
			levelList.add(new LevelMessage(2, levelTwoMsg, levelTwoHeadline));
			levelList.add(new LevelMessage(3, levelThreeMsg, levelThreeHeadline));
			levelList.add(new LevelMessage(4, levelFourMsg, levelFourHeadline));
			appLaunchDto.setReporterLevelMessage(levelList);
		}
		
		// Adding additional fields for referrals
		//String referralAmt = dbConfigService.getProperty(Properties.REFERRAL_AMT.getProperty(), Properties.REFERRAL_AMT.getDefaultValue());
		String referralAmtUpto = dbConfigService.getProperty(Properties.REFERRAL_UPTO_AMT.getProperty(), Properties.REFERRAL_UPTO_AMT.getDefaultValue());
		String referralPerSuccessfulRegister = dbConfigService.getProperty(Properties.REFERRAL_AMT.getProperty(), Properties.REFERRAL_AMT.getDefaultValue());
		String referralAmtAdditional = dbConfigService.getProperty(Properties.REFERRAL_AMT_ADDITIONAL.getProperty(), Properties.REFERRAL_AMT_ADDITIONAL.getDefaultValue());
		String referralText = dbConfigService.getPropertyByLanguageId(Properties.REFERRAL_TEXT.getProperty(), null, languageId);
		// if the referral text of the user's language is not available then provide in English
		if(referralText == null) {
			referralText = dbConfigService.getPropertyByLanguageId(Properties.REFERRAL_TEXT.getProperty(), Properties.REFERRAL_TEXT.getDefaultValue(), 2);
		}
		referralText = referralText.replaceFirst("AMT", referralPerSuccessfulRegister);
		referralText= referralText.replaceFirst("AMT", referralAmtAdditional);
		LOGGER.info("referralAmtUpto={}, referralPerSuccfulRegister={}, referralAmtAdditional={}, formattedReferralText={}", referralAmtUpto, referralPerSuccessfulRegister, referralAmtAdditional, referralText);
		
		//appLaunchDto.setReferralAmount(referralAmt);
		appLaunchDto.setReferralAmount(referralAmtUpto);
		appLaunchDto.setReferralText(referralText);
		
		// attaching current user's balance
		Optional<UserAccount> userAccount = userAccountRepo.findById(userId);
		if(userAccount.isPresent()) {
			userAccount.ifPresent(acc -> appLaunchDto.setCurrentBalance( String.valueOf( (long) acc.getCurrentBalance())));
		} else {
			appLaunchDto.setCurrentBalance("0");
		}
		
		// Adding EarnMore and RedeemUrl
		String earnMoreUrl = dbConfigService.getProperty(Properties.EARN_MORE_URL.getProperty(), Properties.EARN_MORE_URL.getDefaultValue());
		String redeemUrl = dbConfigService.getProperty(Properties.REDEEM_URL.getProperty(), Properties.REDEEM_URL.getDefaultValue());
		appLaunchDto.setEarnMoreUrl(earnMoreUrl + key); 
		appLaunchDto.setRedeemUrl(redeemUrl + key);
		
		// Adding otpVerifed status
		boolean otpStatus = false;
		if(otpVerify != null && otpVerify == 1) {
			otpStatus = true;
			appLaunchDto.setMobile(mobile);
		}
		appLaunchDto.setOtpVerified(otpStatus);
		
		try {
			// Is User's Profile okay
			UserProfile userProfile = userProfileRepo.findByUserId(userId);
			String profilePicUrl = userProfile.getProfilePick();
			String name = userProfile.getName();
			if(profilePicUrl != null && name != null && !profilePicUrl.equals("") && !name.equals("")) {
				appLaunchDto.setProfileOk(true);
			} else {
				appLaunchDto.setProfileOk(false);
			}
			// If changing the language
			if(langChange) {
				userProfile.setLanguageId(languageId);
				userProfileRepo.save(userProfile);
				LOGGER.debug("updated languageId change");
			}
		} catch (Exception ex) {
			appLaunchDto.setProfileOk(false);
		}
		
		// Adding redeem threshold
		String redeemThreshold = dbConfigService.getProperty(Properties.REDEEM_THRESHOLD.getProperty(), Properties.REDEEM_THRESHOLD.getDefaultValue());
		appLaunchDto.setRedeemThreshold(redeemThreshold);
		
		// Adding ticker field
		String tickerText = dbConfigService.getPropertyByLanguageId(Properties.TICKER_TEXT.getProperty(), languageId);
		if(tickerText == null) {
			// If tickerText of the passed language is not available then 
			tickerText = dbConfigService.getPropertyByLanguageId(Properties.TICKER_TEXT.getProperty(), 2);
		}
		// possible values of ticker actions are invite, insurance, profile, rate etc.
		String tickerAction = dbConfigService.getProperty(Properties.TICKER_ACTION.getProperty(), Properties.TICKER_ACTION.getDefaultValue());
		String tickerColor = dbConfigService.getProperty(Properties.TICKER_COLOR.getProperty(), Properties.TICKER_COLOR.getDefaultValue());
		appLaunchDto.setTicker(new Ticker(tickerText, tickerAction, tickerColor));
		
		// adding user state field
		appLaunchDto.setUserState(state == null ? null : state.getStateName());
		
		
		if(user.getUploadStatus() == 1) {
			LOGGER.info("allowed for upload");
			appLaunchDto.setNewsUploadAllowed("1");
			appLaunchDto.setUploadNotAllowedMessage("");
		} else {
			LOGGER.info("not allowed for upload");
			appLaunchDto.setNewsUploadAllowed("0");
			String uploadNotAllowedMsg = dbConfigService.getPropertyByLanguageId(Properties.UPLOAD_NOT_ALLOWED_MSG.getProperty(), null, languageId);
			if(uploadNotAllowedMsg == null) {
				uploadNotAllowedMsg = dbConfigService.getPropertyByLanguageId(Properties.UPLOAD_NOT_ALLOWED_MSG.getProperty(), Properties.UPLOAD_NOT_ALLOWED_MSG.getDefaultValue(), 2);
			}
			appLaunchDto.setUploadNotAllowedMessage(uploadNotAllowedMsg == null ? "" : uploadNotAllowedMsg);
		}
		
		return ResponseEntity.ok(appLaunchDto);
	}
}
