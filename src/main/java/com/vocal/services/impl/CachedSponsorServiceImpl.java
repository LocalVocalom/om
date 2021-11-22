package com.vocal.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.News;
import com.vocal.entities.NewsDetails;
import com.vocal.entities.SponsorContent;
import com.vocal.repos.jpa.NewsDetailsRepo;
import com.vocal.repos.jpa.NewsRepo;
import com.vocal.repos.jpa.SponsorContentRepo;
import com.vocal.services.CachedSponsorService;
import com.vocal.utils.Utils;
import com.vocal.viewmodel.NewsDtoInnerAll;

@Service
public class CachedSponsorServiceImpl implements CachedSponsorService {
	
	@Autowired
	private SponsorContentRepo sponsorContentRepo;
	
	@Autowired
	private NewsDetailsRepo newsDetailsRepo;
	
	@Autowired
	private NewsRepo newsRepo;
	
	private Map<String, NewsDtoInnerAll> langIdCategoryIdWiseIndexWiseDtos;
	
	private Map<String, List<Integer>> languageIdCategoryIdWiseIndexList;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CachedSponsorServiceImpl.class);
	
	@Override
	@PostConstruct
	public void reloadOrRefresh() {
		Map<String, NewsDtoInnerAll> tempLanguageIdCategoryIdIndexedWiseMappedDto = new ConcurrentHashMap<>();
		Map<String, List<Integer>> tempLanguageIdCategoryIdWiseIndexList = new ConcurrentHashMap<>();
		
		List<SponsorContent> enabledAndOngoingSponserContents = sponsorContentRepo.findAllByEnabledAndStartTimeLessThanAndEndTimeGreaterThan(true, new Date(), new Date());
		for(SponsorContent content: enabledAndOngoingSponserContents) {
			String mappingKey = content.getLanguageId()+""+content.getCategoryId();
			
			// inserting ads index into the indices list
			List<Integer> mappedList = tempLanguageIdCategoryIdWiseIndexList.getOrDefault(mappingKey, new ArrayList<Integer>());
			mappedList.add(content.getAdIndex());
			tempLanguageIdCategoryIdWiseIndexList.put(mappingKey,  mappedList);
			
			// inserting dto into map
			tempLanguageIdCategoryIdIndexedWiseMappedDto.put(mappingKey+content.getAdIndex(), getNewsDtoInnerAll(content.getNewsId()));
		}
		langIdCategoryIdWiseIndexWiseDtos = tempLanguageIdCategoryIdIndexedWiseMappedDto;
		LOGGER.info("cached sponser service after refresh or load=\n{}", langIdCategoryIdWiseIndexWiseDtos);
		
		// sorting the ads indices
		for(Map.Entry<String, List<Integer>> entry: tempLanguageIdCategoryIdWiseIndexList.entrySet()) {
			Collections.sort(entry.getValue());
		}
		languageIdCategoryIdWiseIndexList = tempLanguageIdCategoryIdWiseIndexList;
		LOGGER.info("cached sponser index mapping after soring the lists= \n{}", languageIdCategoryIdWiseIndexList);
	}
	
	@Scheduled(fixedDelayString = "${news.fixedDelay:300000}") // 5 minutes
	public void syncNewsDtoFields() {
		LOGGER.info("before updating the the sponsor dto map=\n{}", langIdCategoryIdWiseIndexWiseDtos);
		if(langIdCategoryIdWiseIndexWiseDtos != null) {
			Set<Entry<String,NewsDtoInnerAll>> entrySet  =  langIdCategoryIdWiseIndexWiseDtos.entrySet();
			for(Entry<String,NewsDtoInnerAll> entry : entrySet) {
				NewsDtoInnerAll value = entry.getValue();
				if(value != null) {
					entry.setValue(getNewsDtoInnerAll(value.getNewsid()));
				}
			}
		}
		LOGGER.info("after updating the the sponsor dto map=\n{}", langIdCategoryIdWiseIndexWiseDtos);
	}
	
	private NewsDtoInnerAll getNewsDtoInnerAll(long newsId) {
		NewsDetails newsDetails = newsDetailsRepo.findByNewsId(newsId);
		News news = newsRepo.findByNewsId(newsId);
		if(news == null || newsDetails == null) {
			LOGGER.error("For newsId={}, news or newsDetails records doesn't exist, or invalid newsId, return dto with null fields", newsId);
			return new NewsDtoInnerAll();
		}
		
		NewsDtoInnerAll dto = new NewsDtoInnerAll();
		dto.setAggregator(newsDetails.isAggregator());
		dto.setAudiourl(newsDetails.getNewsDiscriptionAudioUrl());
		dto.setComment_count(newsDetails.getComment());
		dto.setCreaterId(newsDetails.getCreaterId());
		dto.setDatetime(news.getDateTime());
		dto.setDetailed_news_url(newsDetails.getNewsUrl());
		dto.setDiscription(newsDetails.getNewsDiscriptionText());
		dto.setDislikes_count(newsDetails.getDislikes());
		dto.setFlag_count(newsDetails.getFlage());
		dto.setHeadline(newsDetails.getNewsHeadline());
		dto.setImage_url(newsDetails.getNewsImageUrl());
		dto.setLikes(news.getLikes());
		dto.setNews_location(news.getNewsLocation());
		dto.setNewsid(newsId);
		dto.setOpenAction(newsDetails.getOpenAction());
		dto.setReporter(newsDetails.isReporter());
		dto.setReporter_name(newsDetails.getNewsCreator());
		dto.setShare_count(newsDetails.getShare());
		dto.setType(newsDetails.getNewsVideoUrl() != null && newsDetails.getNewsVideoUrl().length() >= 10 ? "Video" : "News");
		dto.setVideo_url(newsDetails.getNewsVideoUrl());
		// ADJUSTING VIEWS
		dto.setViews(news.getViews() * 10 + Utils.nextRandomInt(10));
		
		return dto;
	}

	@Override
	public NewsDtoInnerAll getSponsoredContentByLanguageIdAndCategoryIdAndIndex(int languageId, int categoryId, int adsIndex) {
		String key = new StringBuilder().append(languageId).append(categoryId).append(adsIndex).toString();
		NewsDtoInnerAll dto = langIdCategoryIdWiseIndexWiseDtos.get(key);
		if(dto == null) {
			LOGGER.error("failed to get ads for index={} with key={}, please check", adsIndex, key);
		} else {
			LOGGER.info("got an ads for index={} with key={}", adsIndex, key);
		}
		return dto;
	}

	@Override
	public Iterator<Integer> getAdsIndexesForLanguageIdAndCategoryId(int languageId, int categoryId) {
		String key = new StringBuilder().append(languageId).append(categoryId).toString();
		List<Integer> indicesList = languageIdCategoryIdWiseIndexList.get(key);
		if(indicesList == null || indicesList.size() == 0) {
			LOGGER.warn("failed to get indices list for key={} for languageId={}, categoryId={} returning iterator as null", key, languageId, categoryId);
			return null;
		}
		return indicesList.iterator();
	}

}
