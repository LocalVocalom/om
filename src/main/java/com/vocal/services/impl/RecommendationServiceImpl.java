package com.vocal.services.impl;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vocal.repos.jpa.impl.RecommendationRepo;
import com.vocal.services.RecommendationService;
import com.vocal.viewmodel.RecommendationDto;

@Service
public class RecommendationServiceImpl implements RecommendationService {
	
	@Autowired
	private RecommendationRepo recommendationRepo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationServiceImpl.class);
	
	private static Calendar cal = Calendar.getInstance();
	
	@Value("${NUMBER_OF_RECOMMENDATIOS}")
	private int  recordsNum;
	
	@Value("${INTERVAL_MINUTES}")
	private int intervalMinutes;
	
	@Override
	public List<RecommendationDto> recommendNewsByMostViewed(int categoryId, int languageId, long newsId, double latitude, double longitude) {
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -intervalMinutes);
		
		List<RecommendationDto> dtos = recommendationRepo.getNewsByViewsWithinTime(recordsNum, categoryId, languageId, newsId, cal.getTime());

		if(dtos.size() == 0) {
			LOGGER.debug("No most viewed news were found, falling back by ignoring time");
			dtos = recommendationRepo.getNewsByViews(recordsNum, categoryId, languageId, newsId);
		} else {
			LOGGER.debug("Got {} recommendations", dtos.size());
		}
		return dtos;
	}
	
	@Override
	public List<RecommendationDto> recommendNewsByMostLikes(int categoryId, int languageId, long newsId, double latitude, double longitude) {
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -intervalMinutes);
		List<RecommendationDto> dtos = recommendationRepo.getNewsByLikesWithinTime(recordsNum, categoryId, languageId, newsId, cal.getTime());
		if(dtos.size() == 0) {
			LOGGER.debug("No most liked news were found, falling back by ignoring time");
			dtos = recommendationRepo.getNewsByLikes(recordsNum, categoryId, languageId, newsId);
		} else {
			LOGGER.debug("Got {} recommendations", dtos.size());
		}
		return dtos;
	}

	@Override
	public List<RecommendationDto> recommendNewsByMostCommented(int categoryId, int languageId, long newsId, double latitude, double longitude) {
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -intervalMinutes);
		List<RecommendationDto> dtos = recommendationRepo.getNewsByCommentWithinTime(recordsNum, categoryId, languageId, newsId, cal.getTime());
		if(dtos.size() == 0) {
			LOGGER.debug("No most commented news were found, falling back by ignoring time");
			dtos = recommendationRepo.getNewsByComment(recordsNum, categoryId, languageId, newsId);
		} else {
			LOGGER.debug("Got {} recommendations", dtos.size());
		}
		return dtos;
	}

	@Override
	public List<RecommendationDto> recommendNewsByMostShared(int categoryId, int languageId, long newsId, double latitude, double longitude) {
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -intervalMinutes);
		List<RecommendationDto> dtos = recommendationRepo.getNewsBySharesWithinTime(recordsNum, categoryId, languageId, newsId, cal.getTime());
		if(dtos.size() == 0) {
			LOGGER.warn("No most shared news were found, falling back by ignoring time");
			dtos = recommendationRepo.getNewsByShares(recordsNum, categoryId, languageId, newsId);
		} else {
			LOGGER.debug("Got {} recommendations", dtos.size());
		}
		return dtos;
	}
	
	public List<RecommendationDto> recommendRecentlyUploadedNews(int categoryId, int languageId, long newsId, double latitude, double longitude) {
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -intervalMinutes);
		List<RecommendationDto> dtos = recommendationRepo.getRecentlyUploadedNewsWithinTime(recordsNum, categoryId, languageId, newsId, cal.getTime());
		if(dtos.size() == 0) {
			LOGGER.warn("No recently uploaded news were found, falling back by ignoring time");
			dtos = recommendationRepo.getRecentlyUploadedNews(recordsNum, categoryId, languageId, newsId);
		} else {
			LOGGER.debug("Got {} recommendations", dtos.size());
		}
		return dtos;
	}

	@Override
	public Object recommendTrendingNews(int categoryId, int languageId, long newsId, double latitude,
			double longitude) {
		TreeSet<RecommendationDto> trends = new TreeSet<RecommendationDto>(new Comparator<RecommendationDto>() {
			@Override
			public int compare(RecommendationDto o1, RecommendationDto o2) {
				return (int) (o1.getNewsid() - o2.getNewsid());
			}
		});
		
		trends.addAll(recommendRecentlyUploadedNews(categoryId, languageId, newsId, latitude, longitude));
		trends.addAll(recommendNewsByMostViewed(categoryId, languageId, newsId, latitude, longitude));
		trends.addAll(recommendNewsByMostShared(categoryId, languageId, newsId, latitude, longitude));
		
		// TODO: A custom score calculation can be added for the criteria/definition of trend
		// as score = A * shares + B * likes  + C * views + D * sharePerSecond , where A, B, C and D are 
		// constants.
		
		return trends;
	}

}
