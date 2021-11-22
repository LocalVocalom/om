package com.vocal.services;

import java.util.List;

import com.vocal.viewmodel.RecommendationDto;

public interface RecommendationService {

	List<RecommendationDto> recommendNewsByMostViewed(int categoryId, int languageId, long newsId, double latitude, double longitude);
	
	List<RecommendationDto> recommendNewsByMostLikes(int categoryId, int languageId, long newsId, double latitude, double longitude);
	
	List<RecommendationDto> recommendNewsByMostCommented(int categoryId, int languageId, long newsId, double latitude, double longitude);
		
	List<RecommendationDto> recommendNewsByMostShared(int categoryId, int languageId, long newsId, double latitude, double longitude);
	
	List<RecommendationDto> recommendRecentlyUploadedNews(int categoryId, int languageId, long newsId, double latitude, double longitude);

	Object recommendTrendingNews(int categoryId, int languageId, long newsId, double latitude, double longitude);
	
}
