package com.vocal.controllers;

import javax.servlet.http.HttpServletRequest;

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
import com.vocal.services.RecommendationService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;

@CrossOrigin
@RestController
public class RecommendationController {
	
	@Autowired
	private RecommendationService recommendationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationController.class);
	
	@GetMapping(value = "/getRecommendedNews")
	public ResponseEntity<?> getNewsRecommendation(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info(
				"/getRecommendedNews userid = {}, otp = {}, catagory = {}, languageid = {}, Lnewsid = {}, latitude = {}, longitude = {}",
				userId, otp, category, langId, newsId, longitude, latitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return new ResponseEntity<>(recommendationService.recommendRecentlyUploadedNews(category, langId, newsId, latitude, longitude), HttpStatus.OK);
	}
	
	@PostMapping(value = "/getRecentlyAdded")
	public ResponseEntity<?> getRecentlyAdded(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info("/getRecentlyAdded userid={}, otp={}, category={}, languageId={}, Lnewsid={}, latitude={}, longitude={}", userId, otp, category, langId, newsId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return  ResponseEntity.ok(recommendationService.recommendRecentlyUploadedNews(category, langId, newsId, latitude, longitude));
	}
	
	@PostMapping(value = "/getMostViewed")
	public ResponseEntity<?> getRecommendationByMostViewed(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info("/getMostViewed userid={}, otp={}, category={}, languageId={}, Lnewsid={}, latitude={}, longitude={}", userId, otp, category, langId, newsId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return ResponseEntity.ok(recommendationService.recommendNewsByMostViewed(category, langId, newsId, latitude, longitude));
	}
	
	@PostMapping(value = "/getMostShared")
	public ResponseEntity<?> getRecommendationByMostShared(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info("/getMostShared userid={}, otp={}, category={}, languageId={}, Lnewsid={}, latitude={}, longitude={}", userId, otp, category, langId, newsId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return ResponseEntity.ok(recommendationService.recommendNewsByMostShared(category, langId, newsId, latitude, longitude));
	}
	
	@PostMapping(value = "/getMostLiked")
	public ResponseEntity<?> getRecommendationByMostLiked(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info("/getMostLiked userid={}, otp={}, category={}, languageId={}, Lnewsid={}, latitude={}, longitude={}", userId, otp, category, langId, newsId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return ResponseEntity.ok(recommendationService.recommendNewsByMostLikes(category, langId, newsId, latitude, longitude));
	}
	
	@PostMapping(value = "/getMostCommented")
	public ResponseEntity<?> getRecommendationByMostCommented(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info("/getMostCommented userId={}, otp={}, category={}, languageId={}, Lnewsid={}, latitude={}, longitude={}", userId, otp, category, langId, newsId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		return ResponseEntity.ok(recommendationService.recommendNewsByMostCommented(category, langId, newsId, latitude, longitude));
	}
	
	@PostMapping(value = "/getTrending")
	public ResponseEntity<?> getRecommendationByTrend(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LAST_NEWS_ID) long newsId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			HttpServletRequest request
			) {
		LOGGER.info("/getTrending userId={}, otp={}, category={}, languageId={}, Lnewsid={}, latitude={}, longitude={}", userId, otp, category, langId, newsId, latitude, longitude);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return ResponseEntity.ok(recommendationService.recommendTrendingNews(category, langId, newsId, latitude, longitude));
	}

}
