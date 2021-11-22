package com.vocal.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Constants;
import com.vocal.viewmodel.AddressModel;
import com.vocal.viewmodel.StatusDto;

@RestController
public class SetAddressController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SetAddressController.class);

	@PostMapping(value = "/setAddress")
	ResponseEntity<?> setAddress(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.ADDRESS) String address,
			HttpServletRequest request
			)  {
		LOGGER.info("/setAddress userId={}, otp={}, address={}", userId, otp, address);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		String decodedAddress = "";
		try {
			decodedAddress = URLDecoder.decode(address, Constants.UTF8_ENCODING);
		} catch (UnsupportedEncodingException e1) {
			LOGGER.error("error while url decoding, exception={}", e1.getMessage());
		}
		
		AddressModel addressModel = new AddressModel();
		try {
			addressModel = objectMapper.readValue(decodedAddress, AddressModel.class);
		} catch (JsonProcessingException e) {
			LOGGER.error("error while parsing address exception = {}", e.getMessage());
		}

		LOGGER.info("Parsed address is={}", addressModel.toString());
		StatusDto statusDto;
		try {
			boolean isSuccess = userService.setAddress(userId, user, addressModel);
			statusDto = new StatusDto(isSuccess ? "success" : "fail");
		} catch(Exception ex) {
			LOGGER.error("some exception occured while calling set address service , exception = {}", ex.getMessage());
			statusDto = new StatusDto("fail");
		}
		
		return ResponseEntity.ok(statusDto);
	}
}
