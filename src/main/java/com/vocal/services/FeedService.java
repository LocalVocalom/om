package com.vocal.services;

import java.util.HashMap;
import java.util.List;

import com.vocal.entities.User;
import com.vocal.viewmodel.BreakingDto;

public interface FeedService {
	
	public List<BreakingDto> getBreakingHardcoded(int languageId);
	
	public List<BreakingDto> getNationalBreakingOrBreafing(int languageId);
	
	public List<BreakingDto> getStateBreakingOrBreafing(int languageId, long userId);

	public List<Object> getPersonalizedFeed(int languageId, long userId);

	List<Object> getLocalNewsIfEnabledForUserCity(int languageId, long userId);

	List<Object> getPersonalizedFeed(int languageId, User user);
	
	List<Object> getPersonalizedFeed(int languageId, User user, String appVersion, List<Integer> personalizedCategories);

	List<Object> getPersonalizedFeed(int languageId, String appVersion, String personalizedCategories);

	List<Object> getPersonalizedFeedLimitedOverall(int languageId, String appVersion,List<Integer> personalizedCategories);
	
	//new add method sk Singh
	//List<Object> getPersonalizedFeedLimitedOverall(int languageId, String appVersion,
	//		List<Integer> personalizedCategories, HashMap<Integer, String> catNameDefault );

	List<Object> getLocalNewsIfEnabledForUserCity(int languageId, User user);

	List<BreakingDto> getBreakingLanguageWise(int languageId);

	List<Object> getLocalNewsInPreviousNDays(long userId, int languageId, double latitude, double longitude);
}
