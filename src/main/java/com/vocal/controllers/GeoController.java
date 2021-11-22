package com.vocal.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.GeoService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.viewmodel.CityListDto;
import com.vocal.viewmodel.StatesLanguageDto;

@CrossOrigin
@RestController
public class GeoController {
	
	@Autowired
	private GeoService geoService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoController.class);

	@PostMapping(value = "/getStates")
	public ResponseEntity<?> getStates(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.LANGUAGE_ID, defaultValue = "1") int languageId,
			HttpServletRequest request) {
		LOGGER.info("/getStates userId={}, otp={}, languageId={}", userId, otp, languageId);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(new StatesLanguageDto(geoService.getStates(languageId), geoService.getLanguages()), HttpStatus.OK);
	}

	@PostMapping(value = "/getCity")
	public ResponseEntity<?> getCity(
			@RequestParam(value = "state") int stateId,
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			HttpServletRequest request) {
		LOGGER.info("/getCity userId={}, otp={}, stateId= {}", userId, otp, stateId);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(new CityListDto(geoService.getCitiesByStateId(stateId)), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/getCityWithLang")
	public ResponseEntity<?> getCityWithLang(
			@RequestParam(value = "state") int stateId,
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.LANGUAGE_ID, defaultValue = "1") int languageId,
			HttpServletRequest request) {
		LOGGER.info("/getCityWithLang userId={}, otp={}, stateId= {}, languageId={}", userId, otp, stateId,languageId);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(new CityListDto(geoService.getCitiesByStateIdAndlanguageid(stateId,languageId)), HttpStatus.OK);
	}
	
	
	
	@PostMapping(value = "/setState")
	ResponseEntity<?> setState(
			@RequestParam(name = CommonParams.USERID) long userId, 
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId, 
			@RequestParam(name = "stateId") int stateId,
			HttpServletRequest request
			) {
		LOGGER.info("/setState userId={}, otp={}, languageId={}, stateId={}", userId, otp, langId, stateId);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return ResponseEntity.ok(userService.setState(userId, stateId, langId));
	}

	@PostMapping(value = "/setStateWithCity")
	ResponseEntity<?> setStateWithCity(
			@RequestParam(name = CommonParams.USERID) long userId, 
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId, 
			@RequestParam(name = "stateId") int stateId,
			@RequestParam(name = "cityId") int cityId,
			HttpServletRequest request
			) {
		LOGGER.info("/setStateWithCity userId={}, otp={}, languageId={}, stateId={},cityId={}", userId, otp, langId, stateId,cityId);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return ResponseEntity.ok(userService.setStateWithCity(userId, stateId,cityId, langId));
	}
}
