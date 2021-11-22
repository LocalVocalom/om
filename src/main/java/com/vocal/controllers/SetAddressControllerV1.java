package com.vocal.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

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
import com.vocal.services.CachedLangWiseConfigService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Constants;
import com.vocal.viewmodel.AddressModel;
import com.vocal.viewmodel.LangAndCategories;
import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.LanguageDtoV1;
import com.vocal.viewmodel.NewsCategoryDtoV1;
import com.vocal.viewmodel.SetAddressV1Dto;

@RestController
public class SetAddressControllerV1 {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CachedLangWiseConfigService cachedLangWiseConfigService;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(SetAddressControllerV1.class);
	
	@PostMapping(value = "/v1/setAddress")
	ResponseEntity<?> setAddressV1(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.ADDRESS) String address,
			HttpServletRequest request
			)  {
		LOGGER.info("/v1/setAddress userId={}, otp={}, address={}", userId, otp, address);
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
		SetAddressV1Dto addressV1Dto = getAvailableLanguagesAndTheirCategories(user);
		
		try {
			boolean isSuccess = userService.setAddress(userId, user, addressModel);
			addressV1Dto.setStatus(isSuccess ? "success" : "fail");
		} catch(Exception ex) {
			LOGGER.error("some exception occured while calling set address service , exception = {}", ex.getMessage());
			addressV1Dto.setStatus("fail");
		}
		
		return ResponseEntity.ok(addressV1Dto);
	}
	
	private SetAddressV1Dto getAvailableLanguagesAndTheirCategories(User user) {
		SetAddressV1Dto addressV1Dto = new SetAddressV1Dto();
		List<LanguageDtoV1> activeLanguages = cachedLangWiseConfigService.getActiveLanguagesV1();
		List<LangAndCategories> languageAndTheirCategories = new ArrayList<>();
		LangAndCategories eles = null;
		for(LanguageDtoV1 ele : activeLanguages) {
			List<NewsCategoryDtoV1> newsCategories = cachedLangWiseConfigService.getActiveNewsCategoriesV1(ele.getId());
			if(newsCategories != null && newsCategories.size() != 0) {
				eles = new LangAndCategories();
				eles.setLangId(ele.getId());
				eles.setLangName(ele.getLanguage());
				eles.setCode(ele.getCode());
				eles.setCats(newsCategories);
				eles.setColor(ele.getColor());
				eles.setInit(ele.getLanguage().substring(0, 2));
				eles.setDefaultCatIds(cachedLangWiseConfigService.getDefaultCategoryIdsForALanguage(ele.getId()));
				languageAndTheirCategories.add(eles);
			}
		}
		
		String state = user.getState();
		addressV1Dto.setLangCats(languageAndTheirCategories);
		
		if(state == null) {
			LOGGER.info("no mapped states found, hence unable to decide the default language");
			addressV1Dto.setLangCats(languageAndTheirCategories);
		} else {
			List<LanguageDto> languagesBasedOnState = cachedLangWiseConfigService.getConfiguredLanguageBasedOnState(state);
			LanguageDto configuredLanguageBasedOnState = null;
			if(languagesBasedOnState != null && languagesBasedOnState.size() != 0) {
				configuredLanguageBasedOnState = languagesBasedOnState.get(0);
				addressV1Dto.setDefaultLangId(configuredLanguageBasedOnState.getId());
			} else {
				LOGGER.error("no mapped language found for the state={} while trying to get language based on state", state);
			}
		}
		return addressV1Dto;
	}
	
	
	@PostMapping(value = "/getLangsAndCats")
	public ResponseEntity<?> getAvailableLanguagesAndTheirCategories(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			HttpServletRequest request
			) {
		LOGGER.info("/getLangsAndCats userId={}, otp={}", userId, otp);
		User user = userRepo.findByUserId(userId);
		if (userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		SetAddressV1Dto setAddressV1Dto = getAvailableLanguagesAndTheirCategories(user);
		setAddressV1Dto.setStatus("success");
		return ResponseEntity.ok(setAddressV1Dto);
	}
}
