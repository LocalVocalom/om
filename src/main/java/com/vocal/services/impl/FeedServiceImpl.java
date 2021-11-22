package com.vocal.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vocal.entities.Breaking;
import com.vocal.entities.City;
import com.vocal.entities.CityMapping;
import com.vocal.entities.NewsDetails;
import com.vocal.entities.User;
import com.vocal.entities.UserLocation;
import com.vocal.repos.jpa.BreakingRepo;
import com.vocal.repos.jpa.CityMappingRepo;
import com.vocal.repos.jpa.CityRepo;
import com.vocal.repos.jpa.NewsDetailsRepo;
import com.vocal.repos.jpa.UserLocationRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.FeedService;
import com.vocal.services.NewsService;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.BreakingDto;

@Service
public class FeedServiceImpl implements FeedService {
	
	@Autowired
	private NewsDetailsRepo newsDetailsRepo;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private UserLocationRepo userLocationRepo;
	
	@Autowired
	private CityMappingRepo cityMappingRepo;
	
	@Autowired
	private BreakingRepo breakingRepo;
	
	@Autowired
	private CityRepo cityRepo;
	
	private static long last_news_id = 999_999_99;
	
	private Map<Integer, List<Long>> tempNewsIdsLangWise = new HashMap<>();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedServiceImpl.class);
	
	@PostConstruct
	public void initiateTempNewsIds() {
		
		List<Long> tempNewsIds = new ArrayList<>();
		tempNewsIds.add(2241728L);
		tempNewsIds.add(2241495L);
		tempNewsIds.add(2241867L);
		tempNewsIds.add(2241944L);
		
		tempNewsIdsLangWise.put(1, tempNewsIds);
		
		tempNewsIds = new ArrayList<>();
		tempNewsIds.add(2271287L);
		tempNewsIds.add(2271431L);
		tempNewsIds.add(2271435L);
		tempNewsIds.add(2271434L);
		
		tempNewsIdsLangWise.put(2, tempNewsIds);
	}

	@Override
	public List<BreakingDto> getBreakingHardcoded(int languageId) {
		List<Long> list = tempNewsIdsLangWise.get(languageId);
		List<BreakingDto> dtoList = new ArrayList<>();
		if(list != null && list.size() != 0) {
			List<NewsDetails> newsDetails = newsDetailsRepo.findAllById(list);
			for(NewsDetails detail : newsDetails) {
				BreakingDto dto = new BreakingDto();
				dto.setHeadline(detail.getNewsHeadline());
				dto.setActionUrl(detail.getNewsUrl());
				
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	
	@Override
	public List<BreakingDto> getBreakingLanguageWise(int languageId) {
		Calendar cal = Calendar.getInstance();
		Integer breakingHours = dbConfigService.getIntProperty(Properties.BREAKING_HOURS.getProperty(), Properties.BREAKING_HOURS.getDefaultValueAsInt());
		cal.add(Calendar.HOUR, -breakingHours);
		
		Date dateTimeBeforeTwoHours = cal.getTime();
		List<BreakingDto> dtoList = new ArrayList<>();
		try {
			List<Breaking> breakings = breakingRepo.findAllByLanguageIdAndStatusAndCreatedTimeGreaterThanOrderByPriorityDescCreatedTimeDesc(languageId, 1, dateTimeBeforeTwoHours);
			// if no breakings found just return hardcoded
			if(breakings == null) {
				LOGGER.error("no breakings found for languageId={} in previous {} hours", languageId, breakingHours);
				//dtoList = getBreakingHardcoded(languageId);
				breakings = new ArrayList<>();
			}
			
			for(Breaking breaking : breakings) {
				BreakingDto dto = new BreakingDto();
				dto.setHeadline(breaking.getBreakingHead());
				dto.setActionUrl(breaking.getActionUrl());
				dtoList.add(dto);
			}
			
		} catch (Exception ex) {
			LOGGER.error("exception occured while getting breaking for languageId={}, createdTime={} exited with ex={}", languageId, dateTimeBeforeTwoHours, ex.getMessage());
			dtoList = getBreakingHardcoded(languageId);
		}
		return dtoList;
	}

	@Override
	public List<BreakingDto> getNationalBreakingOrBreafing(int languageId) {
		
		return null;
	}

	@Override
	public List<BreakingDto> getStateBreakingOrBreafing(int languageId, long userId) {
		
		return null;
	}

	@Override
	public List<Object> getPersonalizedFeed(int languageId, User user, String appVersion, List<Integer> personalizedCategories) {
		List<Object> dtos = new ArrayList<>();
		for(Integer category : personalizedCategories) {
			List<Object> newsDtos = newsService.getNews(category, last_news_id, languageId, 10, Properties.LATEST_APP_VERSION.getDefaultValue());
			dtos.addAll(newsDtos);
			break;
		}
		
		return dtos;
	}
	
	@Override
	public List<Object> getPersonalizedFeed(int languageId, String appVersion, String personalizedCategories) {
		return null;
	}
	
	//code comment SK
	
	/*
	 * @Override public List<Object> getPersonalizedFeedLimitedOverall(int
	 * languageId, String appVersion, List<Integer> personalizedCategories) {
	 * List<Object> dtos = new ArrayList<>(); try { List<Object>
	 * latestMiscellaneousCategories =
	 * newsService.latestMiscellaneousCategories(personalizedCategories, languageId,
	 * appVersion); dtos.addAll(latestMiscellaneousCategories); } catch (Exception
	 * ex) { LOGGER.
	 * error("failed to get miscellaneous feed for categories={}, exception={}",
	 * personalizedCategories, ex.getMessage()); } return dtos; }
	 */
	
	
	@Override
	public List<Object> getPersonalizedFeedLimitedOverall(int languageId, String appVersion, List<Integer> personalizedCategories) {
		List<Object> dtos = new ArrayList<>();
		try {
			List<Object> latestMiscellaneousCategories = newsService.latestMiscellaneousCategories(personalizedCategories, languageId, appVersion);
			dtos.addAll(latestMiscellaneousCategories);
		} catch (Exception ex) {
			LOGGER.error("failed to get miscellaneous feed for categories={}, exception={}", personalizedCategories, ex.getMessage());
		}
		return dtos;
	}
	
	
	@Override
	public List<Object> getPersonalizedFeed(int languageId, User userId) {
		//Date previousUpdatedTime = user.getUpdatedTime();
		//Date currentTime = new Date();
		
		return null;
	}
	
	@Override
	public List<Object> getPersonalizedFeed(int languageId, long userId) {
		//Date previousUpdatedTime = user.getUpdatedTime();
		//Date currentTime = new Date();
		
		return null;
	}
	
	@Override
	public List<Object> getLocalNewsIfEnabledForUserCity(int languageId, long userId) {
		// INCLUDING CITY AS CAGEGORY DTO
		UserLocation userLocation = userLocationRepo.findUserLocationByUserId(userId);
		int categoryId = 0;
		if(userLocation != null) {
			String city = userLocation.getCity();
			CityMapping mappedCity = null;
			if(city != null && !city.equals("")) {
				mappedCity = cityMappingRepo.findByAlternateNamesContaining(city);
			}
			
			if(mappedCity != null) {
				//City cityCat = cityRepo.findByCityIdAndLanguageId(mappedCity.getCityId(), languageid);
				City cityCat = cityRepo.findByLanguageIdAndId(languageId, mappedCity.getCityId());
				categoryId = cityCat.getCategoryId();
			}
		}
		List<Object> newsDtos = new ArrayList<>();
		if(categoryId != 0) {
			newsDtos = newsService.getNewsNoFallbackToHome(categoryId, last_news_id, languageId, 8, Properties.LATEST_APP_VERSION.getDefaultValue());
		}
		return newsDtos;
	}
	
	@Override
	public List<Object> getLocalNewsInPreviousNDays(long userId, int languageId, double latitude, double longitude) {
		List<Object> newsDtos = new ArrayList<>();
		
		if(latitude == 0 && longitude == 0) {
			UserLocation userLocation = userLocationRepo.findUserLocationByUserId(userId);
			if(userLocation != null && userLocation.getLatitude() != null && userLocation.getLongitude() != null) {
				latitude = userLocation.getLatitude();
				longitude = userLocation.getLongitude();
				LOGGER.info("coordinates updated to latitude={}, longitude={}", latitude, longitude);
			}
		}
		
		if(latitude != 0 && longitude != 0) {
			newsDtos = newsService.getLocalizedNewsUptoPreviousNDays(last_news_id, languageId, latitude, longitude, 5);
		}
		return newsDtos;
	}
	
	@Override
	public List<Object> getLocalNewsIfEnabledForUserCity(int languageId, User user) {
		List<Object> newsDtos = newsService.getNewsNoFallbackToHome(100000, last_news_id, languageId, 8, Properties.LATEST_APP_VERSION.getDefaultValue());
		return newsDtos;
	}
	
	

}
