package com.vocal.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
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
import com.vocal.entities.UserProfile;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.CachedLangWiseConfigService;
import com.vocal.services.FeedService;
import com.vocal.services.NewsService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.viewmodel.GeoRecommendationDto;
import com.vocal.viewmodel.HomeFeedDto;
import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.NewsCategoryDto;
import com.vocal.viewmodel.NewsCategoryFeedDto;
import com.vocal.viewmodel.StateDto;

@RestController
public class NewsFeedController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserProfileRepo userProfileRepo;
	
	@Autowired
	private CachedLangWiseConfigService cachedLangWiseConfigService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private NewsService newsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsFeedController.class);
	
	private static long lastNewsId = 999_999_99L;
	
	private static List<Integer> defaultPersonalizedCategories = new ArrayList<>();
	
	@PostConstruct
	public void fillPersonlizedCategories() {
		defaultPersonalizedCategories.add(0);
		defaultPersonalizedCategories.add(98);
	}
	
	// TODO: TO enable/disable views in app. 
	@PostMapping("/personalizedFeed")
	public ResponseEntity<?> getFeed(
			@RequestParam(CommonParams.USERID) long userId,
			@RequestParam(CommonParams.OTP) String otp,
			@RequestParam(CommonParams.LANGUAGE_ID) int languageId,
			@RequestParam(CommonParams.APP_VERSION) String appVersion,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request) {
		LOGGER.info("/personalizedFeed  userId={}, otp={}, langId={}, lat={}, long={}", userId, otp, languageId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return ResponseEntity.ok(getFeed(languageId, user, appVersion, latitude, longitude));
	}
	
	private HomeFeedDto getFeed(int languageId, User user, String appVersion, double latitude, double longitude) {
		HomeFeedDto dto = new HomeFeedDto();
		try {
			// TODO: get breaking news, langwise, statewise
			//dto.setBreaking(feedService.getBreakingHardcoded(languageId));
			dto.setBreaking(feedService.getBreakingLanguageWise(languageId));
			
			
			// TODO: local content if enabled for this city
			//dto.setLocal(feedService.getLocalNewsIfEnabledForUserCity(languageId, user.getUserId()));
			dto.setLocal(feedService.getLocalNewsInPreviousNDays(user.getUserId(), languageId, latitude, longitude));
			
			List<Integer> personlizedCats = new ArrayList<>();
			UserProfile userProfile = userProfileRepo.findByUserId(user.getUserId());
			String personalizedCategories = userProfile.getPersonalizedCategories();
			
			// TODO: subscribed categories
			List<NewsCategoryFeedDto> subsCats = new ArrayList<>();
			NewsCategoryFeedDto catDto = null;
			
			if(personalizedCategories != null) {
				try {
					personalizedCategories = personalizedCategories.trim();
					for(String ele : personalizedCategories.split(",")) {
						int category = Integer.parseInt(ele.trim());
						personlizedCats.add(category);
						
						catDto = new NewsCategoryFeedDto();
						NewsCategoryDto categoryDto = cachedLangWiseConfigService.getCategoryDtosFromCategoryId(category, languageId);
						if(categoryDto != null) {
							catDto.setCategoryId(categoryDto.getCategoryId());
							catDto.setCategoryName(categoryDto.getCategoryName());
							catDto.setCatNews(newsService.getNews(category, lastNewsId, languageId, 5, appVersion));
							
							subsCats.add(catDto);
						}
					}
				} catch(Exception ex) {
					LOGGER.info("exception occurred while parsing comma separated personalized categores, ex={}", ex.getMessage());
					personlizedCats = defaultPersonalizedCategories;
				}
			} else {
				LOGGER.info("no personalized categories found, the value of personalized categories={}", personalizedCategories);
				List<NewsCategoryDto> activeNewsCategories = cachedLangWiseConfigService.getActiveNewsCategories(languageId);
				if(activeNewsCategories == null || activeNewsCategories.size() == 0) {
					LOGGER.error("No active categories for languageId={}", languageId);
					activeNewsCategories = new ArrayList<>();
				}
			
				for(NewsCategoryDto categoryDto : activeNewsCategories) {
					int category = (int) categoryDto.getCategoryId();
					personlizedCats.add(category);
					
					catDto = new NewsCategoryFeedDto();
					
					catDto.setCategoryId(category);
					catDto.setCategoryName(categoryDto.getCategoryName());
					catDto.setCatNews(newsService.getNews(category, lastNewsId, languageId, 5, appVersion));
					
					subsCats.add(catDto);
				}
			}
			
			// Add category name from getStaticCategoryName method
			//HashMap<Integer, String> catNameDefault = new HashMap<Integer, String>();
			
			// doesn't matter whether or not they selected or  not, mandatorily home and headlines categories to be appear in new home page
			for(int i = 0; i < defaultPersonalizedCategories.size(); i++) {
					catDto = new NewsCategoryFeedDto();
					catDto.setCategoryId(defaultPersonalizedCategories.get(i));
					
					// get name of static category, if not found, then show corresponding english name
					String categoryName = cachedLangWiseConfigService.getStaticCategoryName(languageId, defaultPersonalizedCategories.get(i));
					if(categoryName == null) {
						categoryName = cachedLangWiseConfigService.getStaticCategoryName(2, defaultPersonalizedCategories.get(i));
					}
					
					
					// add code static name set for default news id like 0 or 98
					//catNameDefault.put(defaultPersonalizedCategories.get(i), categoryName);
									
					catDto.setCategoryName(categoryName);
					catDto.setCatNews(newsService.getNews(defaultPersonalizedCategories.get(i), lastNewsId, languageId, 5, appVersion));
					subsCats.add(catDto);
			}
			dto.setSubCats(subsCats);
			
			// TODO: Personalized News
			//dto.setPersonalized(feedService.getPersonalizedFeed(languageId, user, appVersion, personlizedCats));
			personlizedCats.add(0);
			personlizedCats.add(98);
			
			//dto.setPersonalized(feedService.getPersonalizedFeedLimitedOverall(languageId, appVersion, personlizedCats));
			dto.setPersonalized(feedService.getPersonalizedFeedLimitedOverall(languageId, appVersion, personlizedCats));
			
		} catch (Exception ex) {
			LOGGER.error("exception occurred exited with ex={}", ex.getMessage());
			for(StackTraceElement element : ex.getStackTrace()) {
				LOGGER.error("{}",element.toString());
			}
		}
		return dto;
	}
	
	/**
	 * Not used
	 * @param userId
	 * @param otp
	 * @param request
	 * @return
	 */
	@PostMapping("/adviceGeoBased")
	public ResponseEntity<?> getGeoRecommendation(
			@RequestParam(CommonParams.USERID) long userId,
			@RequestParam(CommonParams.OTP) String otp,
			HttpServletRequest request
			) {
		LOGGER.info("/adviceLangAndState userId={}, otp={}", userId, otp);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		GeoRecommendationDto dto = new GeoRecommendationDto();
		List<LanguageDto> activeLanguages = cachedLangWiseConfigService.getActiveLanguages();
		List<StateDto> states = cachedLangWiseConfigService.getStates(2);
		
		String state = user.getState();
		if(state == null) {
			// STATE IS NOT SET FOR THIS USER, THEN PROVIDE DEFAULT CONFIGURATION ONLY
			dto.setAllEnabled(activeLanguages);
			//dto.setAllStates(states);
		} else {
			//List<LanguageDto> configuredLanguageBasedOnState = cachedLangWiseConfigService.getConfiguredLanguageBasedOnState(state);
			LanguageDto configuredLanguageBasedOnState = cachedLangWiseConfigService.getConfiguredLanguageBasedOnState(state).get(0);
			// THERE ARE NO CONFIGURED LANGUAGES FOUND FOR THIS STATE, THEN PROVIDING DEFAULT
			if(configuredLanguageBasedOnState == null) { // || configuredLanguageBasedOnState.size() == 0
				dto.setAllEnabled(activeLanguages);
				//dto.setAllStates(states);
			} else {
				// REMOVING THE CONFIGURED LANGUAGES FROM ALL ACTIVE LANGUAGES, SO THAT THEY DON'T APPEAR IN BOTH FIELDS
				/*
				for(LanguageDto lang: configuredLanguageBasedOnState) {
					activeLanguages.remove(lang);
				}
				*/
				activeLanguages.remove(configuredLanguageBasedOnState);
				StateDto userState = cachedLangWiseConfigService.getStateDtoByStateName(state);
				//dto.setActiveStateByLocation(userState);
				dto.setByLocation(configuredLanguageBasedOnState);
				dto.setAllEnabled(activeLanguages);
				// REMOVING USER's STATE FROM ALL STATE LIST
				states.remove(userState);
				//dto.setAllStates(states);
			}
		}
		return ResponseEntity.ok(dto);
	}
	
	/**
	 * Not used
	 * @param userId
	 * @param otp
	 * @param languageId
	 * @param request
	 * @return
	 */
	/*
	@PostMapping("/saveLangAndAdviceSubs")
	public ResponseEntity<?> saveLanguageAndGetSubscriptionCategories(
			@RequestParam(CommonParams.USERID) long userId,
			@RequestParam(CommonParams.OTP) String otp, 
			@RequestParam(CommonParams.LANGUAGE_ID) int languageId,
			HttpServletRequest request
			) {
		LOGGER.info("/saveLangAndState userId={}, otp={}, selectedLangId={}", userId, otp, languageId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		UserProfile userProfile = userProfileRepo.findByUserId(userId);
		// if user profile doesn't exist then create a new one with some defaults
		if (userProfile == null) {
			LOGGER.error("userProfile not found for userId={}, creating a new one", userId);
			userProfile = new UserProfile();
			userProfile.setUserId(userId);
			// deafults
		}

		// CHECK THAT THE PASSED LANGUAGE  IS A VALID LANGUAGE ID AND CURRENTLY ACTIVE
		if (!cachedLangWiseConfigService.isValiedActiveLanguageId(languageId)) {
			StatusDto statusDto = new StatusDto("fail");
			return new ResponseEntity<>(statusDto, HttpStatus.BAD_REQUEST);
		} else {
			userProfile.setLanguageId(languageId);
			userProfileRepo.save(userProfile);
			
			List<NewsCategoryDto> activeNewsCategories = cachedLangWiseConfigService.getActiveNewsCategories(languageId);
			if(activeNewsCategories.size() == 0) {
				LOGGER.error("no active news categories for languageId={} while advicing subscribe categories", languageId);
			}
			
			NewsCategoriesDto dto = new NewsCategoriesDto();
			dto.setActiveCategories(activeNewsCategories);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		}
	}
	*/
	
	/**
	 * Not used
	 * @param userId
	 * @param otp
	 * @param subscribedCategories
	 * @param request
	 * @return
	 */
	@PostMapping("/saveSubsCats")
	public ResponseEntity<?> savePersonalizedCategories(
			@RequestParam(CommonParams.USERID) long userId,
			@RequestParam(CommonParams.OTP) String otp,
			@RequestParam("subs") List<Integer> subscribedCategories,
			@RequestParam(CommonParams.APP_VERSION) String appVersion,
			HttpServletRequest request
			) {
		LOGGER.info("/savePersCats userId={}, otp={}, subs={}", userId, otp, subscribedCategories);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		UserProfile userProfile = userProfileRepo.findByUserId(userId);
		if(userProfile == null) {
			LOGGER.error("userProfile not found for userId={}, while saving subscription categories, creating a new one", userId);
			// may be a fallback
		} else {
			// TODO: first validate that all the passed categories are active and valid
			
			try {
				userProfileRepo.save(userProfile);
				return new ResponseEntity<>(getFeed(userProfile.getLanguageId(), user, appVersion, 0, 0),  HttpStatus.OK);
			} catch (Exception ex) {
				LOGGER.info("Exception while saving userProfile, ex=", ex.getMessage());
			}
		}
		return ResponseEntity.ok("noOutputFromCats");
	}
}
