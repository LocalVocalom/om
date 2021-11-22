package com.vocal.services.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vocal.entities.Comment;
import com.vocal.entities.HeaderContent;
import com.vocal.entities.HeaderTitle;
import com.vocal.entities.MoreNews;
import com.vocal.entities.News;
import com.vocal.entities.NewsDetails;
import com.vocal.entities.User;
import com.vocal.entities.UserInsurance;
import com.vocal.exceptions.NoContentAvailableException;
import com.vocal.exceptions.RecordNotFoundException;
import com.vocal.mapper.Mapper;
import com.vocal.repos.jpa.CommentRepo;
import com.vocal.repos.jpa.HeaderContentRepo;
import com.vocal.repos.jpa.HeaderTitleRepo;
import com.vocal.repos.jpa.MoreNewsRepo;
import com.vocal.repos.jpa.NewsDetailsRepo;
import com.vocal.repos.jpa.NewsRepo;
import com.vocal.repos.jpa.UserInsuranceRepo;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.repos.jpa.impl.CustomizedRepo;
import com.vocal.services.CachedLangWiseConfigService;
import com.vocal.services.CachedSponsorService;
import com.vocal.services.DbConfigService;
import com.vocal.services.NewsService;
import com.vocal.services.PushMangerService;
import com.vocal.utils.Constants;
import com.vocal.utils.Properties;
import com.vocal.utils.Utils;
import com.vocal.viewmodel.AddDisplay;
import com.vocal.viewmodel.ContentDto;
import com.vocal.viewmodel.MoreNewsDto;
import com.vocal.viewmodel.NewsCategoryDto;
import com.vocal.viewmodel.NewsDtoAll;
import com.vocal.viewmodel.NewsDtoUser;
import com.vocal.viewmodel.NewsDtoInnerAll;
import com.vocal.viewmodel.NewsDtoInnerUser;
import com.vocal.viewmodel.NewsTutorialsDto;
import com.vocal.viewmodel.PersonalizedDto;
import com.vocal.viewmodel.StatusDto;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private MoreNewsRepo moreNewsRepo;

	@Autowired
	private CustomizedRepo customizedRepo;

	@Autowired
	private NewsRepo newsRepo;

	@Autowired
	private NewsDetailsRepo newsDetailsRepo;

	@Autowired
	private UserInsuranceRepo userInsuranceRepo;

	@Autowired
	private CommentRepo commentRepo;

	@Autowired
	private HeaderTitleRepo headerRepo;

	@Autowired
	private HeaderContentRepo headerContentRepo;
	
	@Autowired
	private UserProfileRepo userProfileRepo;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private PushMangerService pushMangerService;

	@Autowired
	private Mapper mapper;
	
	@Autowired
	private CachedSponsorService cachedSponsorService;
	
	@Autowired
	private CachedLangWiseConfigService cachedLangWiseConfigService;

	@Value("${HOST_URL}")
	private String HOST_URL;
	
	@Value("${THUMBNAIL_SCRIPT_PATH}")
	private String thumbnailScriptPath;

	@Value("${DISTANCE_RADIUS:200}")
	private double boundedDistance;
	
	@Value("${FLOORED_BOUNDED_DISTANCE:50}")
	private double flooredBoundedDistance;
	
	@Value("${NEWS_COUNT:10}")
	private int count;
	
	@Value("${IMAGE_BUCKET}")
	private String imageBucketUrl;
	
	@Value("${VIDEO_BUCKET}")
	private String videoBucketUrl;
	
	@Value("${IMAGE_AND_VIDEO_UPLOAD_SCRIPT}")
	private String videoAndThumbUploadScript;
	
	@Value("${IMAGE_ONLY_UPLOAD_SCRIPT}")
	private String imageOnlyUploadScript;
	
	@Value("${KYC_UPLOAD_SCRIPT}")
	private String kycUploadScript;
	
	@Value("${KYC_BUCKET}")
	private String kycBucketUrl;
	
	@Value("${CDN_ENABLED}")
	private boolean cdnEnabled;
	
	private long lastNewsIdForFirstTime = 999_999_99;
	
	private Random rand = new Random();
	
	private Date dateAsOnApril13;

	private static final Logger LOGGER = LoggerFactory.getLogger(NewsServiceImpl.class);
	
	@PostConstruct
	private void initializeAprilTenDate() {
		try {
			dateAsOnApril13 = new SimpleDateFormat("yyyy-MM-dd").parse("2021-04-13");
		} catch (ParseException e) {
			LOGGER.info("exception occurred while parsing date to initialize april ten date, ex={}", e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Object> getNewsNoFallbackToHome(int category, long lastNewsId, int langId, int count, String appVersion) {
		List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getNewsForAll(lastNewsId, category, langId, /*datetime,*/ count);
		if (newsForAllResultList == null || newsForAllResultList.size() == 0) {
			LOGGER.error("No news content available for the passed parameters criteria");
		}
		
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		if(appVersion.equals("99")  || appVersion.equals("1.4.1")) {
			interval = dbConfigService.getIntProperty(Properties.TEST_AD_INTERVAL.getProperty(), Properties.TEST_AD_INTERVAL.getDefaultValueAsInt());
		}
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());
		
		Iterator<Integer> indexIterator = cachedSponsorService.getAdsIndexesForLanguageIdAndCategoryId(langId, category);
		int adIndex = -1;
		if(indexIterator != null && indexIterator.hasNext()) {
			adIndex = indexIterator.next();
		}
		
		for (NewsDtoInnerAll news : newsForAllResultList) {
			// TO DYNAMICALLY DISPATCH ADS FOR FIRST TIME IN A CATEGORY
			if (lastNewsId == lastNewsIdForFirstTime && adIndex != -1 && counter == adIndex) {
				NewsDtoInnerAll adsContentDto = cachedSponsorService
						.getSponsoredContentByLanguageIdAndCategoryIdAndIndex(langId, category, adIndex);
				if (adsContentDto != null) {
					// ADJUSTING THE VIEW, accordingly
					newsObjects.add(adsContentDto);
				}
				if (indexIterator.hasNext()) {
					adIndex = indexIterator.next();
				}
			}
			//news.setViews(news.getViews() * viewMultiplier + sign * news.getViews() % 10); // modifying views with mulitiplier
			news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}
		
		return newsObjects;
	}

	@Override
	public List<Object> getNews(int category, long lastNewsId, int langId, int count, String appVersion) {
		/*
		Optional<News> newsRecord = newsRepo.findById(lastNewsId);
		Date datetime;
		if (lastNewsId == 0 || !newsRecord.isPresent()) {
			datetime = new Date();
		} else {
			datetime = newsRecord.get().getDateTime();
		}
		*/
		
		List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getNewsForAll(lastNewsId, category, langId, /*datetime,*/ count);
		if (newsForAllResultList == null || newsForAllResultList.size() == 0) {
			LOGGER.error("No news content available for the passed parameters criteria");
			newsForAllResultList = customizedRepo.getNewsForAll(lastNewsId, 0, langId, /*datetime,*/ count); // falling back to home category
			//throw new NoContentAvailableException("No news content available for the passed criteria");
		}
		
		// Added to display ads
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		if(appVersion.equals("99")  || appVersion.equals("1.4.1")) {
			interval = dbConfigService.getIntProperty(Properties.TEST_AD_INTERVAL.getProperty(), Properties.TEST_AD_INTERVAL.getDefaultValueAsInt());
		}
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());
		
		// ADDED for manual ADS
		Iterator<Integer> indexIterator = cachedSponsorService.getAdsIndexesForLanguageIdAndCategoryId(langId, category);
		int adIndex = -1;
		if(indexIterator != null && indexIterator.hasNext()) {
			adIndex = indexIterator.next();
		}
		
		for (NewsDtoInnerAll news : newsForAllResultList) {
			// TO DYNAMICALLY DISPATCH ADS FOR FIRST TIME IN A CATEGORY
			if (lastNewsId == lastNewsIdForFirstTime && adIndex != -1 && counter == adIndex) {
				NewsDtoInnerAll adsContentDto = cachedSponsorService
						.getSponsoredContentByLanguageIdAndCategoryIdAndIndex(langId, category, adIndex);
				if (adsContentDto != null) {
					// ADJUSTING THE VIEW, accordingly
					newsObjects.add(adsContentDto);
				}
				if (indexIterator.hasNext()) {
					adIndex = indexIterator.next();
				}
			}
			//news.setViews(news.getViews() * viewMultiplier + sign * news.getViews() % 10); // modifying views with mulitiplier
			news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}
		//return newsForAllResultList;
		return newsObjects;
	}

	@Override
	public NewsDtoAll getNewsForAll(int category, long newsId, int langId, int count, String appVersion) {
		NewsDtoAll newsDtoForAll = new NewsDtoAll();
		newsDtoForAll.setNews(getNews(category, newsId, langId, count, appVersion));
		return newsDtoForAll;
	}

	@Override
	public NewsDtoUser getNewsForUser(int category, long lastNewsId, long userId, int count, String appVersion) {
		/*
		Optional<News> newsRecord = newsRepo.findById(lastNewsId);
		Date datetime;
		if (lastNewsId == 0 || !newsRecord.isPresent()) {
			datetime = new Date();
		} else {
			datetime = newsRecord.get().getDateTime();
		}
		*/
		
		List<NewsDtoInnerUser> newsForUserResultList = customizedRepo.getNewsForUser(lastNewsId, category, userId, /*datetime,*/
				count);
		if (newsForUserResultList == null || newsForUserResultList.size() == 0) {
			LOGGER.error("No news available for requested parameters for user " + userId);
			throw new NoContentAvailableException("No news content available for the passed criteria");
		}
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());
		//int sign = -1;
		for (NewsDtoInnerUser news : newsForUserResultList) {
			if(news.getDatetime().before(dateAsOnApril13)) {
				news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			}
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			//sign *= -1;
		}
		NewsDtoUser dto2User = new NewsDtoUser();
		dto2User.setNews(newsForUserResultList);
		return dto2User;
	}

	@Override
	public List<MoreNewsDto> getMoreNewsUrls(int languageId) {
		List<MoreNews> moreNews = moreNewsRepo.findByLanguageId(languageId);

		if (moreNews == null || moreNews.size() == 0) {
			LOGGER.error("no moreNews were found for languageId={}", languageId);
			throw new NoContentAvailableException("Currently no MoreNews  available for languageId=" + languageId);
		}
		return mapper.moreNewsListToMoreNewsDtoList(moreNews);
	}

	@Override
	public StatusDto handleUploadedData(String type, User user, String fileUrl, int cityId, int languageId,
			double latitude, double longitude, String headlines, String name, String nominee, String email, Date dob, String gender) {
		boolean hasSucceeded = false;
		String imageUrl;
		
		// TODO : use Apache string utils and ignore cases
		if (type.equalsIgnoreCase("NEWS")) {
			// INSERT INTO NEWS and NEWSDETAILS
			LOGGER.debug("Uploading News: userId={},fileUrl={},lat{},long{}", user.getUserId(), fileUrl, latitude, longitude);
			//String cmd = thumbnailScriptPath + " " + fileUrl + " " + imageUrl;
			//String cmd = SCRIPT_PATH + " \"" + fileUrl + "\" \"" + imageUrl + "\"";
			//LOGGER.debug("thumbnail shell command={}", cmd);
			/*
			try {
				Runtime runtime = Runtime.getRuntime();
				runtime.exec(cmd);
			} catch (IOException e) {
				LOGGER.error("Failed to execute a command process, exited with exception={}", e.getMessage());
			}
			*/
			
			// if user is not authorized to upload news
			if(user.getUploadStatus() == 0) {
				return new StatusDto("success");
			}
			
			// only mp4 format is allowed
			if(fileUrl.endsWith(".MP4")) {
				imageUrl = fileUrl.replaceAll(".MP4", ".jpeg");
			} else if(fileUrl.endsWith(".mp4")) {
				imageUrl = fileUrl.replaceAll(".mp4", ".jpeg");
			} else {
				imageUrl = "";
				return new StatusDto("success");
			}
			String videoFilename = Utils.getFileName(fileUrl);
			String imageFilename = Utils.getFileName(imageUrl);
			
			String newsImageUrl;
			String newsVideoUrl;
			if(cdnEnabled) {
				// CDN UPLOAD SCRIPT
				String cmd = videoAndThumbUploadScript + " " + videoFilename + " " + imageFilename;
				LOGGER.debug("video and image upload shell command={}", cmd);
				try {
					Runtime runtime = Runtime.getRuntime();
					runtime.exec(cmd);
				} catch (IOException e){
					LOGGER.error("Failed to execute video and image upload shell command={}, exception=", cmd, e.getMessage());
				}
				// ADDED FOR CDN
				newsImageUrl = imageBucketUrl + imageFilename;
				newsVideoUrl = videoBucketUrl + videoFilename;
			} else {
				newsImageUrl = HOST_URL + "news_files/" + imageFilename;
				newsVideoUrl = HOST_URL + "news_files/" + videoFilename;
			}
			LOGGER.info("newsImageUrl={}", newsImageUrl);
			LOGGER.info("newsVideoUrl={}", newsVideoUrl);

			// CALLING THE FUNCTION WITH CATEGORY ID 99
			long primaryKey = createNewNews(99, languageId, new Date(), "PENDING", cityId, 1, "", fileUrl, latitude,
					longitude);
			LOGGER.debug("News created with primaryKey={} and newsDetail creation in process", primaryKey);
			String creator;
			try {
				creator = userProfileRepo.findByUserId(user.getUserId()).getName().split(" ")[0];
			} catch(Exception ex) {
				LOGGER.error("Exception while getting first name from userProfile, exception = {}", ex.getMessage());
				creator = "@"+user.getUserId();
			}
			LOGGER.debug("Creator={}", creator);
			try {
				creator = new String(creator.getBytes(Constants.UTF8_ENCODING));
				LOGGER.debug("Creator after formatting to UTF-8={}", creator);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("error while processing creator, err=>", e.getMessage());
			}
			
			hasSucceeded = creaetNewNewsDetails(primaryKey, headlines, "", "", newsImageUrl, newsVideoUrl, creator,
					user.getUserId());
			boolean messagingEnabled = dbConfigService.getBooleanProperty(Properties.UPLOAD_MSG_ENABLED.getProperty(), Boolean.valueOf(Properties.UPLOAD_MSG_ENABLED.getDefaultValue()));
			if(hasSucceeded && messagingEnabled) {
				//SEND A WARNING/CAUTIONARY MESSAGE
				String message = dbConfigService.getProperty(Properties.UPLOAD_MSG.getProperty(), Properties.UPLOAD_MSG.getDefaultValue());
				pushMangerService.sendPush(user.getUserId(), message, newsImageUrl, "", 5);
			}
			
		} else {
			// INSERTING INTO USER INSURANCE
			String profilePicUrl; // = HOST_URL + "kyc_files/" + Utils.getFileName(fileUrl);
			String filename = Utils.getFileName(fileUrl);
			if (cdnEnabled) {
				profilePicUrl = kycBucketUrl + filename;
				// CDN UPLOAD
				String cmd = kycUploadScript + " " + filename;
				LOGGER.debug("KYC upload shell command={}", cmd);
				try {
					Runtime runtime = Runtime.getRuntime();
					runtime.exec(cmd);
				} catch (IOException e) {
					LOGGER.error("Failed to execute  KYC upload shell command={}, exception=", cmd, e.getMessage());
				}
			} else {
				profilePicUrl = HOST_URL + "kyc_files/" + Utils.getFileName(fileUrl);
			}

			Calendar cal = Calendar.getInstance();
			Date currentDate = cal.getTime();
			int insuranceActiveAfterDays = dbConfigService.getIntProperty(Properties.INSURENCE_ACTIVE_AFTER_DAYS.getProperty(), Properties.INSURENCE_ACTIVE_AFTER_DAYS.getDefaultValueAsInt());
			cal.add(Calendar.DAY_OF_MONTH, insuranceActiveAfterDays); // added later
			Date dateAfterAddingAfterDays = cal.getTime();
			LOGGER.debug("current date={}, insuranceActiveAfterDays={} date after adding insuranceActiveAfterDays={}", currentDate, insuranceActiveAfterDays, dateAfterAddingAfterDays);
			// hasSucceeded = createUserInsurance(userId, name, nominee, dob, email,
			// profilePicUrl, currentDate, dateAfterSevenDays, "PENDING", gender); //
			// CHANGED TO 'ACTIVE'
			hasSucceeded = createUserInsurance(user.getUserId(), name, nominee, dob, email, profilePicUrl, currentDate,
					dateAfterAddingAfterDays, "ACTIVE", gender);

			String insuranceRegMsg = dbConfigService
					.getPropertyByLanguageId(Properties.INSURENCE_REGISTRATION_TEXT.getProperty(), languageId);
			if (insuranceRegMsg == null) {
				LOGGER.debug(
						"Insurance registration success msg not found for requested languageId, hence falling back to English and default");
				insuranceRegMsg = dbConfigService.getPropertyByLanguageId(
						Properties.INSURENCE_REGISTRATION_TEXT.getProperty(),
						Properties.INSURENCE_REGISTRATION_TEXT.getDefaultValue(), 2);
			}
			pushMangerService.sendPush(user.getUserId(), insuranceRegMsg, "imageUrl", "actionUrl", 5); // sending push with type 5
		}
		return new StatusDto(hasSucceeded ? "success" : "fail");
	}

	@Override
	public StatusDto addComment(long userId, long newsId, String commentText) {
		boolean status = false;
		try {
			Comment comment = new Comment();
			comment.setCreatedTime(new Date());
			comment.setUserId(userId);
			comment.setNewsId(newsId);
			comment.setText(commentText);
			commentRepo.save(comment);
			status = true;
		} catch(Exception ex) {
			LOGGER.info("Exception while saving comment, exception={}", ex.getMessage());
		}

		StatusDto dto = new StatusDto();
		if (status) {
			NewsDetails newsDetails = newsDetailsRepo.findById(newsId).get();
			newsDetails.setComment(newsDetails.getComment() + 1);
			newsDetailsRepo.save(newsDetails);
			dto.setStatus("success");
		} else {
			dto.setStatus("fail");
		}

		return dto;
	}

	@Override
	public List<NewsTutorialsDto> getNewsTutorials(int langId) {
		List<HeaderTitle> headers = headerRepo.findAllByLanguageId(langId);
		if (headers.size() == 0) {
			LOGGER.error("No headers availables for languageid={}", langId);
			headers = headerRepo.findAllByLanguageId(2); // if not available for the passed language then find the English ones
		}
		List<NewsTutorialsDto> tutorials = new ArrayList<NewsTutorialsDto>();
		for (HeaderTitle t : headers) {
			List<HeaderContent> contentList = headerContentRepo.findAllByHeaderTitle(t.getHeaderTitle());
			List<ContentDto> contentDtoList = mapper.contentListToContentDtoList(contentList);
			if (contentList.size() == 0) {
				LOGGER.error("No header content available for header={}", t.getHeaderTitle());
			}
			tutorials.add(new NewsTutorialsDto(t.getHeaderTitle(), t.getHeaderLogo(), contentDtoList));
		}
		return tutorials;
	}

	@Override
	public NewsDtoAll getLocalizedNews(double userLatitude, double userLongitude, int category, long newsId,
			int langId, int count, String appVersion) {
		Optional<News> newsOpt = newsRepo.findById(newsId);
		double distance;
		if (newsId == 0 || !newsOpt.isPresent()) {
			distance = -1;
		} else {
			//News news = newsOpt.get();
			//distance = customizedRepo.getDistance(news.getLatitude(), news.getLongitude(), newsId); // it will always be zero, it was bug hence fixed
			distance = customizedRepo.getDistance(userLatitude, userLongitude, newsId);
		}

		LOGGER.info("distance={}, boundedRadius={}", distance, boundedDistance);
		//List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getLocalizedNewsForAll(category, langId, count, userLatitude, userLongitude, distance);
		List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getLocalizedNewsForAllV1(category, langId, count, userLatitude, userLongitude, distance, boundedDistance);
		if (newsForAllResultList.size() == 0) {
			LOGGER.error("No news content available for the passed parameters criteria");
			throw new NoContentAvailableException("No news content available for the passed criteria");
		}
		
		// Added to display ads
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		if(appVersion.equals("99") || appVersion.equals("1.4.1") ) {
			interval = dbConfigService.getIntProperty(Properties.TEST_AD_INTERVAL.getProperty(), Properties.TEST_AD_INTERVAL.getDefaultValueAsInt());
		}
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());
		
		for (NewsDtoInnerAll news : newsForAllResultList) {
			
			if(news.getDatetime().before(dateAsOnApril13)) {
				news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			}
			
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}

		NewsDtoAll newsDtoForAll = new NewsDtoAll();
		//newsDtoForAll.setNews(newsForAllResultList);
		newsDtoForAll.setNews(newsObjects);
		return newsDtoForAll;
	}
	
	@Override
	public NewsDtoAll getLocalizedNewsV2(double userLatitude, double userLongitude, int category, long lastNewsId,
			int langId, int count, String appVersion) {
		double distance;
		//Date datetime;
		
		Optional<News> newsOpt = newsRepo.findById(lastNewsId);
		if (lastNewsId == 0 || !newsOpt.isPresent()) {
			distance = -1;
			//datetime = new Date();
		} else {
			distance = customizedRepo.getFlooredDistance(userLatitude, userLongitude, lastNewsId);
			//datetime = newsOpt.get().getDateTime();
		}
		
		LOGGER.info("floored distance={} and flooredBoundedDistance={}", distance, flooredBoundedDistance);
		List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getLocalizedNewsForAllV2(lastNewsId, category, langId, count, userLatitude, userLongitude, distance, flooredBoundedDistance/*, datetime*/);
		if (newsForAllResultList.size() == 0) {
			LOGGER.error("No news content available for the passed parameters criteria");
			newsForAllResultList = customizedRepo.getNewsForAll(lastNewsId, 0, langId, /*datetime,*/ count); // if no LOCALNEWS available show home news
			LOGGER.info("Now the size={}", newsForAllResultList.size());
			//throw new NoContentAvailableException("No news content available for the passed criteria");
		}
		
		// Added to display ads
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		if(appVersion.equals("99")  || appVersion.equals("1.4.1")) {
			interval = dbConfigService.getIntProperty(Properties.TEST_AD_INTERVAL.getProperty(), Properties.TEST_AD_INTERVAL.getDefaultValueAsInt());
		}
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());
		
		for (NewsDtoInnerAll news : newsForAllResultList) {
			if(news.getDatetime().before(dateAsOnApril13)) {
				news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			}
			
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}

		NewsDtoAll newsDtoForAll = new NewsDtoAll();
		//newsDtoForAll.setNews(newsForAllResultList);
		newsDtoForAll.setNews(newsObjects);
		return newsDtoForAll;
	}
	
	@Override
	public List<Object> getLocalizedNewsUptoPreviousNDays(long lastNewsId, int languageId, double latitude, double longitude, int prevDays) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -prevDays);
		Date datetime = cal.getTime();
		
		List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getLocalizedNewsUptoPreviousNDays(languageId, 99, lastNewsId, count, latitude, longitude, -1, flooredBoundedDistance, datetime);
		if (newsForAllResultList.size() == 0) {
			LOGGER.error("No news content available for the passed parameters criteria while querying for previous N days");
		}
		
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());
		
		for (NewsDtoInnerAll news : newsForAllResultList) {
			if(news.getDatetime().before(dateAsOnApril13)) {
				news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			}
			
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}
		return newsObjects;
	}
	
	@Override
	public NewsDtoAll getNewsByReporter(long reporterId, boolean isReporter, long lastNewsId, int langId, String appVersion) {
		
		/*
		Optional<News> newsRecord = newsRepo.findById(lastNewsId);
		Date datetime = new Date();
		if (newsRecord.isPresent()) {
			datetime = newsRecord.get().getDateTime();
		}
		*/
		
		List<NewsDtoInnerAll> newsForAllResultList = customizedRepo.getNewsForAllByReporter(lastNewsId, reporterId, isReporter, langId, /*datetime,*/ count);
		LOGGER.debug("Getting news by reporter for reporterId={}, isReporter={}, datetime={}, count={}", reporterId, isReporter, /*datetime,*/ count);
		if (newsForAllResultList.size() == 0) {
			LOGGER.error("No news content available for the passed parameters criteria");
			throw new NoContentAvailableException("No news content available for the passed criteria");
		}
		
		// Added to display ads
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		if(appVersion.equals("99") || appVersion.equals("1.4.1")) {
			interval = dbConfigService.getIntProperty(Properties.TEST_AD_INTERVAL.getProperty(), Properties.TEST_AD_INTERVAL.getDefaultValueAsInt());
		}
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());

		for (NewsDtoInnerAll news : newsForAllResultList) {
			//news.setViews(news.getViews() * viewMultiplier + sign * news.getViews() % 10);
			news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}
		
		NewsDtoAll newsDtoForAll = new NewsDtoAll();
		//newsDtoForAll.setNews(newsForAllResultList);
		newsDtoForAll.setNews(newsObjects);
		return newsDtoForAll;
	}
	
	@Override
	public List<Object> latestMiscellaneousCategories(List<Integer> categories, int languageId, String appVersion) {
		
		List<PersonalizedDto> newsForAllResultList = customizedRepo.getNewsForAllByCategoreis(categories, languageId, 10);// hardcoding to 10
		
		// Added to display ads
		List<Object> newsObjects = new ArrayList<Object>();
		int counter = 1;
		int interval = dbConfigService.getIntProperty(Properties.AD_INTERVAL.getProperty(), Properties.AD_INTERVAL.getDefaultValueAsInt());
		if(appVersion.equals("99") || appVersion.equals("1.4.1")) {
			interval = dbConfigService.getIntProperty(Properties.TEST_AD_INTERVAL.getProperty(), Properties.TEST_AD_INTERVAL.getDefaultValueAsInt());
		}
		
		int viewMultiplier = dbConfigService.getIntProperty(Properties.NEWS_VIEWS_MULTIPLIER.getProperty(), Properties.NEWS_VIEWS_MULTIPLIER.getDefaultValueAsInt());

		for (PersonalizedDto news : newsForAllResultList) {
			// attach categoryName, with respect to categoryId
			int categoryId = news.getCategoryId();
			if(categoryId == 0 || categoryId == 98) {
				
				String categoryName = cachedLangWiseConfigService.getStaticCategoryName(languageId, categoryId);
				if(categoryName == null) {
					categoryName = cachedLangWiseConfigService.getStaticCategoryName(2, categoryId);
				}
				news.setCategory(categoryName);
				

			} else {
				NewsCategoryDto newsCategoryDto = cachedLangWiseConfigService.getCategoryDtosFromCategoryId(categoryId, languageId);
				news.setCategory(newsCategoryDto.getCategoryName());
			}
			
			news.setViews(news.getViews() * viewMultiplier + rand.nextInt(10));
			if (news.getVideo_url().length() >= 10) {
				news.setType("Video");
			} else {
				news.setType("News");
			}
			newsObjects.add(news);
			if ( counter % interval == 0) {
				newsObjects.add(new AddDisplay(true));
			}
			counter++;
		}
		return newsObjects;
	}
	
	@Override
	public NewsDtoInnerAll getSingleNews(long newsId) {
		Optional<News> news = newsRepo.findById(newsId);
		Optional<NewsDetails> newsDetail = newsDetailsRepo.findById(newsId);
		if(news == null || !news.isPresent() ) {
			LOGGER.debug("News with passed newsId={} doesn't exist", newsId);
		}
		if(newsDetail == null || !newsDetail.isPresent()) {
			LOGGER.debug("NewsDetails with passed newsId={} doesn't exist", newsId);
		}
	
		NewsDtoInnerAll newsDtoForAll = mapper.createSingleNewsDto(news.get(), newsDetail.get());
		
		return newsDtoForAll;
	}

	// Private helper methods
	private long createNewNews(int categoryId, int languageId, Date datetime, String status, int cityId, int stateId,
			String newsLocation, String uniqueId, double latitude, double longitude) {
		// The checks were removed
		/*
		 * if (!newsCategoryRepo.existsById(categoryId)) { throw new
		 * RecordNotFoundException("categoryId:" + categoryId +
		 * " doesn't exists, news creation failed"); } if
		 * (!languageRepo.existsById(languageId)) { throw new
		 * RecordNotFoundException("languageId:" + languageId +
		 * " doesn't exists, news creation failed"); } if
		 * (!stateRepo.existsById(stateId)) { throw new
		 * RecordNotFoundException("stateId:" + stateId +
		 * " doesn't exist, news creation failed"); }
		 */

		News newNews = new News(categoryId, languageId, datetime, status, cityId, stateId, 0, 0, newsLocation, uniqueId,
				String.valueOf(latitude), String.valueOf(longitude));
		long newsId = newsRepo.save(newNews).getNewsId();
		LOGGER.debug("news created with id = {}", newsId);
		return newsId;
	}

	private boolean creaetNewNewsDetails(long newsId, String newsHeadline, String newsDiscriptionText,
			String newsDiscriptionAudioUrl, String newsImageUrl, String newsVideoUrl, String newsCreator, long userId) {
		boolean hasSucceeded = false;
		// CHECK THAT A NEWS RECORD ASSOCIATED WITH PASSED newsId ALREADY EXIST
		if (!newsRepo.existsById(newsId)) {
			LOGGER.debug("newsId={} doesn't exists, news creation failed", newsId);
			throw new RecordNotFoundException("newsId:" + newsId + " doesn't exists, newsDetails creation failed");
		}
		// CHECK THAT THERER ISN'T ALREADY A newsDetail RECORD ASSOCIATED WITH THE PASSED NEWSID
		if (newsDetailsRepo.existsById(newsId)) {
			LOGGER.debug("newsId={} is already associated with a newsDetail record", newsId);
			throw new RecordNotFoundException("newsId:" + newsId + " is already associated with a newsRecord");
		}

		NewsDetails newNewsDetails = new NewsDetails(newsId, newsHeadline, newsDiscriptionText, newsDiscriptionAudioUrl,
				newsImageUrl, newsVideoUrl, newsCreator, 0, 0, 0, 0, userId);
		newNewsDetails.setAggregator(false);
		newNewsDetails.setOpenAction("app");
		newNewsDetails.setCreaterId(userId);
		newNewsDetails.setReporter(false);
		
		try {
			newsDetailsRepo.save(newNewsDetails);
			hasSucceeded = true;
			LOGGER.debug("successfully newsDetails created for newsId={}", newsId);
		} catch (Exception ex) {
			LOGGER.error("Failed to create newsDetails....ex={}", ex.getMessage());
		}
		return hasSucceeded;
	}

	@Override
	public boolean createUserInsurance(long userId, String name, String nominee, Date dob, String email,
			String profilePic, Date datetime, Date insuranceStartDate, String status, String gender) {
		boolean hasSucceeded = false;
		// CHECK THAT THERE IS NOT ALREADY A UserInsurance RECORD ASSOCIATED WITH THE PROVIDED userId
		UserInsurance userInsurance = userInsuranceRepo.findByUserId(userId);
		if (userInsurance != null) {
			LOGGER.debug("Already the user is associated with an UserInsurence record");
			userInsurance.setProfilePic(profilePic);
			userInsuranceRepo.save(userInsurance);
			return true;
		}

		UserInsurance newUserInsurance = new UserInsurance(userId, name, nominee, dob, email, profilePic, datetime,
				insuranceStartDate, status);
		newUserInsurance.setGender(gender);
		try {
			userInsuranceRepo.save(newUserInsurance);
			hasSucceeded = true;
			LOGGER.debug("UserInsurence successfully created for userId={}", userId);
		} catch (Exception ex) {
			LOGGER.error("Failed to save UserInsurance details.");
		}
		return hasSucceeded;
	}
}
