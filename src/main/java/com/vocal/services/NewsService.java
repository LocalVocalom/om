package com.vocal.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.vocal.entities.User;
import com.vocal.viewmodel.MoreNewsDto;
import com.vocal.viewmodel.NewsDtoAll;
import com.vocal.viewmodel.NewsDtoUser;
import com.vocal.viewmodel.NewsDtoInnerAll;
import com.vocal.viewmodel.NewsTutorialsDto;
import com.vocal.viewmodel.StatusDto;

public interface NewsService {

	List<Object> getNews(int category, long lastNewsId, int langId, int count, String appVersion);

	NewsDtoAll getNewsForAll(int category, long lastNewsId, int langId, int count, String appVersion);
		
	NewsDtoUser getNewsForUser(int category, long lastNewsId, long userId, int count, String appVersion);
	
	List<MoreNewsDto> getMoreNewsUrls(int languageId);
	
	StatusDto handleUploadedData(String type, User user, String fileUrl,int cityId, int languageId, double latitude, double longitude, 
			String headlines, String name, String nominee, String email, Date dob, String gender);

	boolean createUserInsurance(long userId, String name, String nominee, Date dob, String email, String profilePic,
			Date datetime, Date insuranceStartDate, String status, String gender); // Added gender parameter

	StatusDto addComment(long userId, long newsId, String commentText);
	
	List<NewsTutorialsDto> getNewsTutorials(int langId);

	NewsDtoAll getLocalizedNews(double userLatitude, double userLongitude, int category, long lastNewsId, int langId, int count, String appVersion);
	
	NewsDtoAll getLocalizedNewsV2(double userLatitude, double userLongitude, int category, long lastNewsId, int langId, int count, String appVersion);

	NewsDtoAll getNewsByReporter(long reporterId, boolean isReporter, long lastNewsId, int langId, String appVersion);
	
	//List<Object> latestMiscellaneousCategories(String commaSeparatedCategories, int languageId, String appVersion);

	NewsDtoInnerAll getSingleNews(long newsId);

	List<Object> latestMiscellaneousCategories(List<Integer> categories, int languageId, String appVersion);

	List<Object> getNewsNoFallbackToHome(int category, long lastNewsId, int langId, int count, String appVersion);

	List<Object> getLocalizedNewsUptoPreviousNDays(long lastNewsId, int languageId, double latitude, double longitude, int prevDays);
	
}
