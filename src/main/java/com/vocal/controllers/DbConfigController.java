package com.vocal.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.vocal.services.AppPopupService;
import com.vocal.services.BlacklistedIpService;
import com.vocal.services.CachedLangWiseConfigService;
import com.vocal.services.CachedSponsorService;
import com.vocal.services.DbConfigService;
import com.vocal.services.QualityLogicService;
import com.vocal.services.ReferralLogicService;

@Controller
public class DbConfigController {
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private ReferralLogicService referralLogicService;
	
	@Autowired
	private BlacklistedIpService blacklistedIpService;
	
	@Autowired
	private AppPopupService appPopupService;
	
	@Autowired
	private QualityLogicService qualityLogicService;
	
	@Autowired
	private CachedSponsorService cachedSponsorService;
	
	@Autowired
	private CachedLangWiseConfigService cachedLangWiseConfigService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigController.class);
		
	@GetMapping(value = "/refreshDbConfig")
	ResponseEntity<?> reloadProperties() {
		LOGGER.info("/refreshDbConfig");
		try {
			dbConfigService.refresh();
			LOGGER.debug("refreshed db_config");
			
			referralLogicService.refresh();
			LOGGER.info("refreshed referral logic");
			
			
			blacklistedIpService.refresh();
			LOGGER.info("refreshed blacklisted ip");
			
			appPopupService.refresh();
			LOGGER.info("refreshed cached app popup");
			
			qualityLogicService.refresh();
			LOGGER.info("refreshed cached quality logic");
			
			cachedSponsorService.reloadOrRefresh();
			LOGGER.info("refreshed cached sponsered content");
			
			cachedLangWiseConfigService.load();
			LOGGER.info("refreshed languageWise properties");
			
			return ResponseEntity.ok("successfully refreshed/reloaded property configs.");
		} catch (Exception ex) {
			LOGGER.error("failed to refreshed db_config");
			return new ResponseEntity<>("Failed to refresh DbConfig", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
