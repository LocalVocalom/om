package com.vocal.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.NewsService;
import com.vocal.services.StorageService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Constants;
import com.vocal.viewmodel.StatusDto;

@CrossOrigin
@RestController
public class UploadController {

	private static Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
	
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private StorageService storageService;

	@PostMapping(value = "/UploadedData")
	public ResponseEntity<?> uploadedData(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = CommonParams.UPLOADED_FILE_URL) String fileUrl,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int languageId,
			@RequestParam(name = CommonParams.TYPE) String type, 
			@RequestParam(name = CommonParams.HEADLINES) String headlines,
			@RequestParam(name = CommonParams.LATITUDE) double latitude,
			@RequestParam(name = CommonParams.LONGITUDE) double longitude,
			@RequestParam(name = CommonParams.CITY_ID, defaultValue =  "0") int cityId, 
			@RequestParam(name = CommonParams.NAME, required = false) String name,
			@RequestParam(name = CommonParams.NOMINEE, required = false) String nominee, 
			@RequestParam(name = CommonParams.EMAIL, required = false) String email,	
			@RequestParam(name = CommonParams.DOB, required = false) @DateTimeFormat(iso = ISO.DATE) Date dob,
			@RequestParam(name = CommonParams.ADDRESS, required = false) String address, 
			@RequestParam(name = CommonParams.GENDER, required = false) String gender,
			@RequestParam(name = CommonParams.ADHAR_NUMBER, required = false) String adharNumber, 
			HttpServletRequest request) {
		LOGGER.info("/UploadedData type={},userId={},otp={},fileUrl={},cityId={},languageId={},latitude={}, longitude={}, name={}, nominee={},email={},dob={},address={},adharNumber={}",
				type, userId, otp, fileUrl, cityId, languageId, latitude, longitude, name, nominee, email, dob, address, adharNumber);
		
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		/*
		if (userService.isUnauthorizedUser(userId, otp)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		*/
		
		StatusDto statusDto = newsService.handleUploadedData(type, user, fileUrl, cityId, languageId, latitude, longitude, headlines, name, nominee, email, dob, gender);
		return new ResponseEntity<>(statusDto, HttpStatus.OK);
	}

	@PostMapping(value = "/UploadServlet")
	public ResponseEntity<?> uploadServlet(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = CommonParams.TYPE) String type,
			@RequestParam(name = CommonParams.HEADLINES, defaultValue = "") String headlines, 
			@RequestParam(name = CommonParams.ADHAR_NUMBER, defaultValue = "" ) String adharNumber,
			@RequestParam(name = CommonParams.NAME, defaultValue = "") String name, 
			@RequestParam(name = CommonParams.NOMINEE, defaultValue = "") String nominee,
			@RequestParam(name = CommonParams.EMAIL, defaultValue = "") String email,
			@RequestParam(name = CommonParams.DOB, required = false) @DateTimeFormat(iso = ISO.DATE) Date dob,
			@RequestParam(name = CommonParams.ADDRESS, defaultValue = "") String address,
			@RequestParam(name = CommonParams.GENDER, defaultValue	 = "") String gender,
			@RequestParam(name = CommonParams.LANGUAGE_ID, defaultValue = "1") int languageId,
			@RequestParam(name = CommonParams.FILENAME) MultipartFile filename,
			HttpServletRequest request) throws UnsupportedEncodingException {
		LOGGER.info("/UploadServlet type={},userId={},otp={},headlines={},adharNumber={},name={},nominee={},email={},dob={},address={}, gender={} languageId={}",
				type, userId, otp, headlines, adharNumber, name, nominee, email, dob, address, gender, languageId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		try {
			LOGGER.debug("Before conversion, name = {}, nominee={}", name, nominee );
			String name1 = new String (name.getBytes("ISO-8859-1"), Constants.UTF8_ENCODING);
			String nominee1 = new String (nominee.getBytes("ISO-8859-1"), Constants.UTF8_ENCODING);
			LOGGER.debug("After encoding conversion: name = {}, nominee={}", name1, nominee1 );
		} catch (Exception ex) {
			LOGGER.info("Exception in upload servlet, exception={}", ex.getMessage());
		}
		
		return new ResponseEntity<>(storageService.handleFileUpload(user, type, headlines, adharNumber, name, nominee, email, dob, address, gender, languageId ,filename, request), HttpStatus.OK);
	}

}
