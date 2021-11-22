package com.vocal.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.User;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.CachedNewsService;
import com.vocal.services.DbConfigService;
import com.vocal.services.NewsService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.NewsDtoAll;
import com.vocal.viewmodel.NewsDtoUser;

@CrossOrigin
@RestController
public class NewsController {
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private CachedNewsService cachedNewsService;
	
	@Autowired
	private UserRepo userRepo;
	
	private long lastNewsIdForFirstTime = 999_999_99;
		
	@Value("${news.size:10}")
	private int count;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);
		
	
	private Map<Integer, NewsDtoAll> homeNewsLangWise = new HashMap<Integer, NewsDtoAll>(2);
	
	private Map<Integer, NewsDtoAll> miscCachedNews  = new HashMap<Integer, NewsDtoAll>(2);
	
	private boolean isCachingEnabled = true;
	
	@PostMapping(value = "/getNews")
	public ResponseEntity<?> getNews(
			@RequestParam(name = CommonParams.USERID) long userId, 
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.VERSION) String appVersion,
			@RequestParam(name = CommonParams.LAST_NEWS_ID, defaultValue = "0") long newsId,
			HttpServletRequest request
			) {
		LOGGER.info("/getNews userid={}, otp={}, Lnewsid={}, category={}, langId={}, count={}, appVersion={}", 
				userId, otp, newsId, category, langId, count, appVersion);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return new ResponseEntity<>(newsService.getNewsForAll(category, newsId, langId, count, appVersion), HttpStatus.OK);
	}
	
	@PostMapping(value = "/getNewsCreateHistory")
	public ResponseEntity<?> getNewsCreateHistory(
			@RequestParam(name = CommonParams.USERID) long userId, 
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = CommonParams.LAST_NEWS_ID, defaultValue = "0") long lastNewsId,
			@RequestParam(name = CommonParams.CATEGORY,  defaultValue = "99") int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID, defaultValue = "0") int langId,
			@RequestParam(name = CommonParams.VERSION) String appVersion,
			@RequestParam(name = CommonParams.IS_REPORTER, defaultValue = "false") boolean isReporter,
			@RequestParam(name = "reporterId", defaultValue = "0") long reporterId,
			HttpServletRequest request) {
		LOGGER.info("/getNewsCreateHistory userid={}, otp={}, Lnewsid={}, category={}, langId={}, count={}, isReporter={}, reporterId={}", 
				userId, otp, lastNewsId, category, langId, count, isReporter, reporterId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		// If user is not allowed to upload and querying for local uploaded news, then don't return anything.
		if(user.getUploadStatus() == 0 && category == 99) {
			LOGGER.info("not allowed to query for uploaded news history");
			return ResponseEntity.ok(new NewsDtoUser());
		}
		
		if(lastNewsId == 0) {
			lastNewsId = Long.MAX_VALUE;
		}
		
		if(reporterId != 0 ) {
			LOGGER.info("Getting news by reporter including language parameter also");
			return new ResponseEntity<>(newsService.getNewsByReporter(reporterId, isReporter, lastNewsId, langId, appVersion), HttpStatus.OK);
		} else if(isReporter && reporterId == 0) {
			return new ResponseEntity<>(new NewsDtoAll(), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(newsService.getNewsForUser(category, lastNewsId, userId, count, appVersion), HttpStatus.OK);
	}

	@PostMapping(value = "/getMore")
	public ResponseEntity<?> getMore(
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int languageId,
			HttpServletRequest request
			) {
		LOGGER.info("/getMore userid = {}, otp = {}, langId = {}", userId, otp, languageId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(newsService.getMoreNewsUrls(languageId), HttpStatus.OK);
	}
	
	@GetMapping(value = "/getNewsTutorials")
	public ResponseEntity<?> getNewsTutorials(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.LANGUAGE_ID, defaultValue = "2") int languageId,
			HttpServletRequest request
			) {
		LOGGER.info("/getNewsTutorials userid = {}, otp = {}, langId = {}", userId, otp, languageId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return  ResponseEntity.ok(newsService.getNewsTutorials(languageId));
	}
	
	@PostMapping(value = "/Comment")
	public ResponseEntity<?> comment(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = "newsid") long newsId,
			@RequestParam(name = CommonParams.TEXT) String commentText,
			HttpServletRequest request
			) {
		LOGGER.info("/Comment userId={},otp={},newsId={},text={}", userId, otp, newsId, commentText);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		return new ResponseEntity<>(newsService.addComment(userId, newsId, commentText), HttpStatus.OK);
	}
	
	
	@PostMapping(value="/getNewsV2")
	public ResponseEntity<?> getNewsV2(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp, 
			@RequestParam(name = CommonParams.CATEGORY) int category,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int langId,
			@RequestParam(name = CommonParams.LATITUDE, defaultValue = "0") double latitude,
			@RequestParam(name = CommonParams.LONGITUDE, defaultValue = "0") double longitude,
			@RequestParam(name = CommonParams.LAST_NEWS_ID, defaultValue = "0") long lastNewsId,
			@RequestParam(name = CommonParams.VERSION) String appVersion,
			HttpServletRequest request) {
		LOGGER.info(
				"/getNewsV2 userid={}, otp={}, latitude={}, longitude={}, langId={}, category={}, Lnewsid={},  count={}, appVersion={}",
				userId, otp, latitude, longitude, langId, category, lastNewsId, count, appVersion);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}

		NewsDtoAll news;
		if (category == 99 && latitude != 0 && longitude != 0) {
			try {
				news  = newsService.getLocalizedNewsV2(latitude, longitude, category, lastNewsId, langId, count, appVersion);
			} catch(Exception ex) {
				news = new NewsDtoAll();
			}
			return ResponseEntity.ok(news);
		
		} else if(category == 0 && isCachingEnabled && lastNewsId == lastNewsIdForFirstTime && (langId == 1 || langId == 2)) {
			news = homeNewsLangWise.get(langId);
			if(news == null) {
				news = newsService.getNewsForAll(category, lastNewsId, langId, count, appVersion);
				LOGGER.info("home news for queried langId={} doesn't exist, getting and putting in cache", langId);
				homeNewsLangWise.put(langId, news);
				return ResponseEntity.ok(news);
			} else {
				LOGGER.info("Cache hit for langId={}, category={} from cache", langId, category);
				return ResponseEntity.ok(news);
			}
			
			/*
			// Added for multi lingual, multi category news cache.
			Map<Integer, NewsDtoAll> newsForLang = langWiseCategoryWiseCachedNews.get(langId);
			if(newsForLang == null) {
				newsForLang = new HashMap<Integer, NewsDtoAll>(5);
				NewsDtoAll news = newsService.getNewsForAll(category, lastNewsId, langId, count, appVersion);
				newsForLang.put(category, news);
				langWiseCategoryWiseCachedNews.put(langId, newsForLang);
				return ResponseEntity.ok(news);
			} else {
				NewsDtoAll news = newsForLang.get(category);
				if(news == null) {
					news = newsService.getNewsForAll(category, lastNewsId, langId, count, appVersion);
					newsForLang.put(category, news);
					return ResponseEntity.ok(news);
				} else {
					LOGGER.info("Cache hit for langId={}, cateoryId={}", langId, category);
					return ResponseEntity.ok(news);
				}
			}
			*/
		}
		else {
			try {
				news = newsService.getNewsForAll(category, lastNewsId, langId, count, appVersion);
			} catch (Exception e) {
				LOGGER.error("exception while getting news for all, exception={}", e.getMessage());
				news = new NewsDtoAll();
			}
			return ResponseEntity.ok(news);
		}
	}
	
	// NOT USED
	public ResponseEntity<?> getSingleNews(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.NEWS_ID) long newsId
			) {
		return ResponseEntity.ok(newsService.getSingleNews(newsId));
	}
	
	@PostMapping("/refreshNewsCache")
	public ResponseEntity<?> manuallyRefreshNewsCache() {
		try {
			cachedNewsService.refreshingLanguageWiseCategoryWiseCachedNews();
			return ResponseEntity.ok("successfull refreshed");
		} catch (Exception ex){
			LOGGER.error("failed to refresh, error occured={}", ex.getMessage());
			return ResponseEntity.ok("failed to refresh cached news");
		}
	}
	
	@Scheduled(fixedDelayString = "${news.fixedDelay:60000}")
	private void refreshingHomeCachedNews() {
		Set<Integer> langIds = homeNewsLangWise.keySet();
		LOGGER.info("updating cached homeNews for langIds={}", langIds);
		long milliSeconds = System.currentTimeMillis();
		Map<Integer, NewsDtoAll> tempHomeNews = new HashMap<Integer, NewsDtoAll>(4);
		for(Integer lang : langIds) {
			tempHomeNews.put(lang, newsService.getNewsForAll(0, lastNewsIdForFirstTime, lang, count, Properties.LATEST_APP_VERSION.getDefaultValue()));
		}
		homeNewsLangWise = tempHomeNews;
		//isCachingEnabled = dbConfigService.getBooleanProperty(Properties.IS_HOME_NEWS_CACHED.getProperty(), Boolean.valueOf(Properties.IS_HOME_NEWS_CACHED.getDefaultValue()));
		LOGGER.info("updated cached homeNews for langIds={} and took {} milli seconds", langIds, System.currentTimeMillis() - milliSeconds);
	}
	
	@PostMapping("/toggleCache")
	public ResponseEntity<?> toggleChacheEnabledStatus() {
		isCachingEnabled = !isCachingEnabled;
		return ResponseEntity.ok(isCachingEnabled);
	}
	
	@PostMapping("/getMiscNews")
	public ResponseEntity<?> latestMiscellaneousNews(
			@RequestParam(name = CommonParams.USERID) long userId,
			@RequestParam(name = CommonParams.OTP) String otp,
			@RequestParam(name = CommonParams.LANGUAGE_ID) int languageId,
			@RequestParam(name = CommonParams.APP_VERSION) String appVersion,
			HttpServletRequest request) {
		LOGGER.info("/getMiscNews userId={}, otp={}, languageId={}, appVersion={}", userId, otp, languageId, appVersion);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access");
		}
		
		NewsDtoAll miscNews = miscCachedNews.get(languageId);
		if(miscNews == null) {
			miscNews = getherMiscNews(languageId);
			miscCachedNews.put(languageId, miscNews);
		}
		return ResponseEntity.ok(miscNews);
	}
	
	private NewsDtoAll getherMiscNews(@RequestParam(name = CommonParams.LANGUAGE_ID) int languageId) {
		LOGGER.info("/getMiscNews langId={}", languageId);
		int miscNewsCount = dbConfigService.getIntProperty(Properties.MISC_NEWS_COUNT.getProperty(),
				Properties.MISC_NEWS_COUNT.getDefaultValueAsInt());
		String[] miscCategories = dbConfigService
				.getProperty(Properties.MISC_CATEGORIES.getProperty(), Properties.MISC_CATEGORIES.getDefaultValue())
				.split(",");
		int[] categories = new int[miscCategories.length];
		for (int i = 0; i < miscCategories.length; i++) {
			categories[i] = Integer.parseInt(miscCategories[i]);
		}
		NewsDtoAll dto = new NewsDtoAll();
		for (int category : categories) {
			List<Object> news = newsService.getNews(category, lastNewsIdForFirstTime, 1, miscNewsCount,
					Properties.LATEST_APP_VERSION.getDefaultValue());
			dto.addNewsItems(news);
		}
		return dto;
	}
}