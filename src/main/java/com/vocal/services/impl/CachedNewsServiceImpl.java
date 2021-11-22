package com.vocal.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vocal.services.CachedNewsService;
import com.vocal.services.NewsService;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.NewsDtoAll;

@Service
public class CachedNewsServiceImpl implements CachedNewsService {
	
	@Autowired
	private NewsService newsService;
	
	private long lastNewsIdForFirstTime = 999_999_99;

	private Map<Integer, Map<Integer, NewsDtoAll>> langWiseCategoryWiseCachedNews = new HashMap<Integer, Map<Integer, NewsDtoAll>>(2);

	private static final Logger LOGGER = LoggerFactory.getLogger(CachedNewsServiceImpl.class);
	
	@Value("${news.size:10}")
	private int count;

	// @Scheduled(fixedDelayString = "${news.fixedDelay:600000}")
	@Override
	public void refreshingLanguageWiseCategoryWiseCachedNews() {
		long millis = System.currentTimeMillis();
		Map<Integer, Map<Integer, NewsDtoAll>> tempLangWiseCategoryWiseCachedNews = new HashMap<Integer, Map<Integer, NewsDtoAll>>(
				2);
		Set<Integer> langIds = langWiseCategoryWiseCachedNews.keySet();
		for (Integer lang : langIds) {
			Set<Integer> categoryIds = langWiseCategoryWiseCachedNews.get(lang).keySet();
			Map<Integer, NewsDtoAll> categoryNews = new HashMap<Integer, NewsDtoAll>(5);
			for (Integer category : categoryIds) {
				categoryNews.put(category, newsService.getNewsForAll(category, lastNewsIdForFirstTime, lang, count,
						Properties.LATEST_APP_VERSION.getDefaultValue()));
			}
			tempLangWiseCategoryWiseCachedNews.put(lang, categoryNews);
		}
		langWiseCategoryWiseCachedNews = tempLangWiseCategoryWiseCachedNews;
		LOGGER.info("refreshed, took {} milli seconds", System.currentTimeMillis() - millis);
	}

}
