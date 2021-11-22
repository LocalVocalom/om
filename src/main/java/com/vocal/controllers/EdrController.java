package com.vocal.controllers;



import java.util.Date;

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

import com.vocal.entities.EDR;
import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.EDRRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.EdrService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.viewmodel.StatusDto;

@CrossOrigin
@RestController
public class EdrController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EdrService edrService;
	
	@Autowired
	private EDRRepo edrRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	private static Logger LOGGER = LoggerFactory.getLogger(EdrController.class);

	@PostMapping(value = "/EDR")
	public ResponseEntity<?> edrHandler(
			@RequestParam(name = "newsID") long newsId,
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.REPORT_ID, required = false) String reportId,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.TYPE) int type,
			HttpServletRequest request) {
		LOGGER.info("/EDR type = {}, newsId = {}, userId = {}, otp = {}, reportId = {}, langId = {}", 
				type, newsId, userId, otp, reportId, langId);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(edrService.handleEdr(userId, newsId, type, reportId), HttpStatus.OK);
	}
	
	@PostMapping(value = "/videoEdr")
	public ResponseEntity<?> handleVideoEdr(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = "newsId") long newsId,
			@RequestParam(name = "durationInSeconds") long durationInSeconds,
			HttpServletRequest request
			) {
		LOGGER.info("/videoEdr userId={}, otp={}, newsId={}, durationInSeconds={}", userId, otp, newsId, durationInSeconds);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		boolean status = false;
		try {
			EDR edr = new EDR();
			edr.setUserid(userId);
			edr.setNewsid(newsId);
			edr.setDateTime(new Date());
			edr.setType(6);
			edr.setRemarks(durationInSeconds);
			edrRepo.save(edr);
			status = true;
		} catch(Exception ex) {
			LOGGER.info("videoEdr operation allowed only once per user");
		}
		return ResponseEntity.ok(new StatusDto(status ? "success" : "fail"));
	}

}
