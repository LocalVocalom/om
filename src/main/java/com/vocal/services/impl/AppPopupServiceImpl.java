package com.vocal.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.AppPopup;
import com.vocal.entities.RatePopupEdr;
import com.vocal.mapper.Mapper;
import com.vocal.repos.jpa.AppPopupRepo;
import com.vocal.repos.jpa.RatePopupEdrRepo;
import com.vocal.repos.jpa.UserInsuranceRepo;
import com.vocal.repos.jpa.UserRedeemRepo;
import com.vocal.services.AppPopupService;
import com.vocal.services.DbConfigService;
import com.vocal.utils.Properties;
import com.vocal.utils.Utils;
import com.vocal.viewmodel.AppPopupDto;

@Service
public class AppPopupServiceImpl implements AppPopupService {
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private AppPopupRepo appPopupRepo;
	
	@Autowired
	private UserInsuranceRepo userInsuranceRepo;
	
	@Autowired
	private UserRedeemRepo userRedeemRepo;
	
	@Autowired
	private RatePopupEdrRepo ratePopupEdrRepo;
	
	@Autowired
	private Mapper mapper;
	
	private Map<Integer, Map<Integer, AppPopupDto>> appPopupDtos;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AppPopupServiceImpl.class);
	
	
	@Override
    @PostConstruct
    @Scheduled(fixedDelay=3600000)
	public void refresh() {
		Map<Integer, Map<Integer, AppPopupDto>> tempPopupDtos = new HashMap<Integer, Map<Integer, AppPopupDto>>(4);
		for(AppPopup popup : appPopupRepo.findAll()) {
			int popupId = popup.getPopupId();
			if(tempPopupDtos.containsKey(popupId)) {
				Map<Integer, AppPopupDto> mapped = tempPopupDtos.get(popupId);
				mapped.put(popup.getLanguageId(), mapper.getAppPopupDto(popup));
			} else {
				Map<Integer, AppPopupDto> toBeMapped = new HashMap<Integer, AppPopupDto>(2);
				toBeMapped.put(popup.getLanguageId(), mapper.getAppPopupDto(popup));
				tempPopupDtos.put(popupId, toBeMapped);
			}
		}
		appPopupDtos = tempPopupDtos;
		LOGGER.info("cached app popup = {}", appPopupDtos);
	}
	
	
	public AppPopupDto getCachedAppPopupDto(int popupId, int languageId) {
		Map<Integer, AppPopupDto> mappedAgainstPopupId = appPopupDtos.get(popupId);
		AppPopupDto appPopupDto = null;
		if(mappedAgainstPopupId != null) {
			appPopupDto = mappedAgainstPopupId.get(languageId);
			if(appPopupDto == null) {
				LOGGER.info("popup not found for languageId = {}, falling back to english", languageId);
				appPopupDto = mappedAgainstPopupId.get(2);
			} else {
				LOGGER.info("popup found in cache with popupId={}, languageId={}", popupId, languageId);
			}
		}
		return appPopupDto;
	}
	
	
	@Override
	public AppPopupDto getPrioritizedPopupDto(long userId, String appVersion, int languageId, Date createdTime) {
		// Space separated popup priorities
		String popupPriorities = dbConfigService.getProperty(Properties.POP_UP_PRIORITY.getProperty(),
				Properties.POP_UP_PRIORITY.getDefaultValue());
		AppPopupDto appPopup = null; // initially no popup applicable 
		for (String priority : popupPriorities.split(" ")) {
			switch (priority.trim()) {
			case "INSURANCE":
				appPopup = getInsuranceAppPopup(userId, languageId, createdTime);
				break;
			case "RATE":
				appPopup = getRateUsAppPopup(userId, languageId, createdTime);
				break;
			case "UPDATE":
				appPopup = getUpdateAppPopup(appVersion, languageId);
				break;
			}
			
			if(appPopup != null) {
				break;
			}
		}
		return appPopup;
	}
	
	@Override
	public AppPopupDto getUpdateAppPopup(String appVersion, int languageId) {
		String latestVersion = dbConfigService.getProperty(Properties.LATEST_APP_VERSION.getProperty(), Properties.LATEST_APP_VERSION.getDefaultValue());
		String forcedUpdateVersions = dbConfigService.getProperty(Properties.FORCED_UPDATE_VERSIONS.getProperty(), Properties.FORCED_UPDATE_VERSIONS.getDefaultValue());
		
		AppPopupDto updateAppPopup = null;
		// First check if version is to be updated forcefully, or just a update reminder
		if(forcedUpdateVersions.contains(appVersion)) {
			int forceUpdatePopupId = dbConfigService.getIntProperty(Properties.FORCE_UPDATE_POPUP_ID.getProperty(), Properties.FORCE_UPDATE_POPUP_ID.getDefaultValueAsInt());
			updateAppPopup = getCachedAppPopupDto(forceUpdatePopupId, languageId);
		}
		else if(!appVersion.equals(latestVersion)) {
			// If the version is not the latest version then not forced popup
			int updatePopupId = dbConfigService.getIntProperty(Properties.UPDATE_POPUP_ID.getProperty(), Properties.UPDATE_POPUP_ID.getDefaultValueAsInt());
			updateAppPopup = getCachedAppPopupDto(updatePopupId, languageId);
		}
		return updateAppPopup;
	}
	
	
	@Override
	public AppPopupDto getInsuranceAppPopup(long userId, int languageId, Date createdTime) {
		// if(!userInsuranceRepo.existsUserInsuranceByUserId(userId) && isSameDateRegardlessOfTime(createdTime) 
		if(!userInsuranceRepo.existsUserInsuranceByUserId(userId)) {
			int insurancePopupId = dbConfigService.getIntProperty(Properties.INSURANCE_POPUP_ID.getProperty(), Properties.INSURANCE_POPUP_ID.getDefaultValueAsInt());
			return getCachedAppPopupDto(insurancePopupId, languageId);
		}
		return null;
	}
	
	@Override
	public AppPopupDto getRateUsAppPopup(long userId, int languageId, Date createdTime) {
		RatePopupEdr ratePopupEdr = ratePopupEdrRepo.findByUserId(userId);
		int newUserRatePopupDays = dbConfigService.getIntProperty(Properties.NEW_USER_RATE_POP_UP_DAYS.getProperty(), Properties.NEW_USER_RATE_POP_UP_DAYS.getDefaultValueAsInt());
		
		if (ratePopupEdr == null) {
			long differenceFromCreatedtime = Utils.getDifferenceInDays(createdTime, new Date());
			boolean eligibleForRatePopup = (differenceFromCreatedtime >= newUserRatePopupDays);
			if(!eligibleForRatePopup) {
				eligibleForRatePopup = userRedeemRepo.existsUserRedeemByUserId(userId);
			}
			if(eligibleForRatePopup) {
				ratePopupEdr = new RatePopupEdr();
				ratePopupEdr.setUserId(userId);
				ratePopupEdr.setCreatedTime(new Date());
				ratePopupEdr.setUpdatedTime(new Date());
				ratePopupEdr.setPopupCount(0);
				ratePopupEdr.setSessionCounter(1);
				LOGGER.info("rate popup for the first time for user, difference in days = {} from created time",
						differenceFromCreatedtime);
			} else {
				LOGGER.info("not eligible for rate popup");
				return null;
			}
		} else {
			int configuredSessionCounter = dbConfigService.getIntProperty(
					Properties.RATE_US_SESSION_COUNTER.getProperty(),
					Properties.RATE_US_SESSION_COUNTER.getDefaultValueAsInt());
			int configuredRatePopupIntervalDays = dbConfigService.getIntProperty(
					Properties.RATE_POP_UP_INTERVAL.getProperty(),
					Properties.RATE_POP_UP_INTERVAL.getDefaultValueAsInt());

			Date previousRatePopupEdrDate = ratePopupEdr.getUpdatedTime();
			long differenceInDays = Utils.getDifferenceInDays(previousRatePopupEdrDate, new Date());
			int sessionCounter = ratePopupEdr.getSessionCounter();
			LOGGER.info(
					"difference in days = {}, sessionCounter ={}, configuredSessionCounter = {}, configuredInterval = {}",
					differenceInDays, sessionCounter, configuredSessionCounter, configuredRatePopupIntervalDays);
			if (differenceInDays == 0 && sessionCounter < configuredSessionCounter) {
				// don't update the updated time and provide a popup
				ratePopupEdr.setSessionCounter(ratePopupEdr.getSessionCounter() + 1);
			} else if (differenceInDays >= configuredRatePopupIntervalDays) {
				// provide a popup and update the updatedtime
				ratePopupEdr.setUpdatedTime(new Date());
				ratePopupEdr.setPopupCount(ratePopupEdr.getPopupCount() + ratePopupEdr.getSessionCounter());
				ratePopupEdr.setSessionCounter(1);
				LOGGER.info("again rate popup, differenceInDays = {}", differenceInDays);
			} else {
				// not eligible for rate pop up
				LOGGER.info("not eligible for rate app popup, differenceInDays={}", differenceInDays);
				return null;
			}
		}
		ratePopupEdrRepo.save(ratePopupEdr);
		
		int rateUsPopupId = dbConfigService.getIntProperty(Properties.RATE_US_POPUP_ID.getProperty(), Properties.RATE_US_POPUP_ID.getDefaultValueAsInt());
		AppPopupDto rateAppPopup = getCachedAppPopupDto(rateUsPopupId, languageId);
		return rateAppPopup;
	}
	
	@SuppressWarnings({ "deprecation", "unused"})
	private boolean isSameDateRegardlessOfTime(Date userDate) {
		Date currentDate = new Date();
		boolean isSameDate = false;
		isSameDate = ( currentDate.getDate() == userDate.getDate() &&
				currentDate.getMonth() == userDate.getMonth() &&
				currentDate.getYear() == userDate.getYear() );
		return isSameDate;
	}
}
