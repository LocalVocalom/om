package com.vocal.services.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import com.vocal.entities.News;
import com.vocal.entities.NewsDetails;
import com.vocal.entities.UserDevice;
import com.vocal.repos.jpa.NewsDetailsRepo;
import com.vocal.repos.jpa.NewsRepo;
import com.vocal.repos.jpa.UserDeviceRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.PushMangerService;
import com.vocal.utils.Constants;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.StatusDto;
import com.vocal.viewmodel.TopicStatus;

import reactor.core.publisher.Mono;

@Service
public class PushManagerServiceImpl implements PushMangerService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(PushManagerServiceImpl.class);
	
	@Autowired
	private UserDeviceRepo userDeviceRepo;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private NewsDetailsRepo newsDetailsRepo;
	
	@Autowired
	private NewsRepo newsRepo;
	
	@Resource
	private JmsTemplate jmsTemplate;
		
	@Override
	public StatusDto sendPush(long userId, String text, String imageUrl, String actionUrl, long type) {
		String tokenTemp = null;
		UserDevice userDevice = userDeviceRepo.findByUserId(userId);
		if(userDevice != null) {
			tokenTemp = userDevice.getDeviceToken();
		}
		
	    final String token = tokenTemp; // Just to satisfy inner class method's requirement of final.
	    boolean flag = false;
	    String destination = dbConfigService.getProperty(Properties.PUSH_DESTINATION.getProperty(), Properties.PUSH_DESTINATION.getDefaultValue());
	    StatusDto dto = new StatusDto();
	    
		try {
			if(token != null  && !token.equals("")) {
				jmsTemplate.setDefaultDestinationName(destination);
				jmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						MapMessage mapMessage = session.createMapMessage();
						mapMessage.setString(Constants.ID, token);
						mapMessage.setString(Constants.DISPLAY_STRING, text);
						mapMessage.setInt(Constants.BADGE_COUNT, 1);
						mapMessage.setString(Constants.DEVICE_TYPE, Constants.ANDROID);
						mapMessage.setLong(Constants.USERID, userId);
						
						Map<String, String> data = new HashMap<String,  String>();
						data.put(Constants.TEXT, text);
						data.put(Constants.TYPE, String.valueOf(type));
						data.put(Constants.IMAGE_URL, imageUrl);
						data.put(Constants.ACTION_URL, actionUrl);
						
						mapMessage.setObject(Constants.DATA_MAP, data);
						
						return mapMessage;
					}
				});
				flag = true;
			}
		} catch (Exception e) {
			LOGGER.error("error while sending push, exception={}", e.getMessage());
			// TODO : use  apache commons exception utils 
		}
		dto.setStatus( flag ? "success" : "fail");
		LOGGER.debug("status={}", dto.getStatus());
		return dto;
	}
	
	public void sendPush(String token, String text, String imageUrl, String actionUrl, long type) {
	    String destination = dbConfigService.getProperty(Properties.PUSH_DESTINATION.getProperty(), Properties.PUSH_DESTINATION.getDefaultValue());
		try {
			if(token != null  && !token.equals("")) {
				jmsTemplate.setDefaultDestinationName(destination);
				jmsTemplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						MapMessage mapMessage = session.createMapMessage();
						mapMessage.setString(Constants.ID, token);
						mapMessage.setString(Constants.DISPLAY_STRING, text);
						mapMessage.setInt(Constants.BADGE_COUNT, 1);
						mapMessage.setString(Constants.DEVICE_TYPE, Constants.ANDROID);
						//mapMessage.setLong(Constants.USERID, userId);
						
						Map<String, String> data = new HashMap<String,  String>();
						data.put(Constants.TEXT, text);
						data.put(Constants.TYPE, String.valueOf(type));
						data.put(Constants.IMAGE_URL, imageUrl);
						data.put(Constants.ACTION_URL, actionUrl);
						
						mapMessage.setObject(Constants.DATA_MAP, data);
						return mapMessage;
					}
				});
			}
		} catch (Exception ex) {
			LOGGER.error("error while sending push using token, exception={}", ex.getMessage());
		}
	}
	
	@Override
	public void sendPushBatchNotificationBySeparatingTokensOfHindiAndNonHindiUsers(List<Object[]> tokensAndUserId) {
		// SEPARATING HINDI DEVICE TOKENS AND USERIDS
		List<Long> userIdsOfHindiLangUsers = new ArrayList<>();
		List<String> registrationIdsOfHindiLangUsers = new ArrayList<>();
		
		// SEPARATING NON-HINDI DEVICE TOKENS AND USERIDS
		List<Long> userIdsOfNonHindiLangUsers = new ArrayList<>();
		List<String> registrationIdsOfNonHindiLangUsers = new ArrayList<>();

		int counter = 0;
		for (Object[] deviceTokens : tokensAndUserId) {
			String tempReg = deviceTokens[0].toString();
			Long tempUserId = Long.valueOf((deviceTokens[1]).toString());
			Integer tempLangId = Integer.valueOf(deviceTokens[2].toString());
			
			if(tempLangId != null && tempLangId == 1) {
				registrationIdsOfHindiLangUsers.add(tempReg);
				userIdsOfHindiLangUsers.add(tempUserId);
				LOGGER.debug("Added regid={} and userId={} in hindi lang users", tempReg, tempUserId);
			} else {
				registrationIdsOfNonHindiLangUsers.add(tempReg);
				userIdsOfNonHindiLangUsers.add(tempUserId);
				LOGGER.debug("Added regid={} and userId={} in non-hindi lang users", tempReg, tempUserId);
			}
			counter++;
		}
		LOGGER.info("number of objects processed for seperation={}", counter);
		
		if(registrationIdsOfHindiLangUsers.size() != 0 && registrationIdsOfHindiLangUsers.size() == userIdsOfHindiLangUsers.size() ) {
			sendPushBatchMostRecentNewsOfLanguageId(userIdsOfHindiLangUsers, registrationIdsOfHindiLangUsers, 5, 1);
		}
		
		if(registrationIdsOfNonHindiLangUsers.size() != 0 && registrationIdsOfNonHindiLangUsers.size() == userIdsOfNonHindiLangUsers.size()) {
			sendPushBatchMostRecentNewsOfLanguageId(userIdsOfNonHindiLangUsers, registrationIdsOfHindiLangUsers, 5, 2);
		}
		
		if (registrationIdsOfHindiLangUsers.size() != userIdsOfHindiLangUsers.size()
				|| registrationIdsOfNonHindiLangUsers.size() != userIdsOfNonHindiLangUsers.size()) {
			LOGGER.error(
					"mismatch in userId list and registrationId list, size of hindi lang reg={} and user ids={}, size of non-hindi reg={} and userIds={}",
					registrationIdsOfHindiLangUsers.size(), userIdsOfHindiLangUsers.size(),
					registrationIdsOfNonHindiLangUsers.size(), userIdsOfNonHindiLangUsers.size());
		}
	}
	
	private void sendPushBatchMostRecentNewsOfLanguageId(List<Long> userIds, List<String> registrationIds, int type, int languageId) {
		// Get the destination to push and the size of batch
		String destination = dbConfigService.getProperty(Properties.PUSH_DESTINATION.getProperty(),
				Properties.PUSH_DESTINATION.getDefaultValue());
		
		News lastEnglishNews = newsRepo.findTopByLanguageIdOrderByNewsIdDesc(languageId);
		NewsDetails newsDetails;
		if(lastEnglishNews != null) {
			LOGGER.info("last inserted news  with languageId={} found with newsid={}", languageId, lastEnglishNews.getNewsId());
			newsDetails = newsDetailsRepo.findByNewsId(lastEnglishNews.getNewsId());
		} else {
			LOGGER.warn("failed to find the last inserted news id with languageId={}, falling back to any last inserted news");
			newsDetails = newsDetailsRepo.findFirstByOrderByNewsIdDesc();
		}
		
		try {
			jmsTemplate.setDefaultDestinationName(destination);
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					LOGGER.debug("Prepairing the MapMessage");
					MapMessage mapMessage = session.createMapMessage();
					// mapMessage.setString(Constants.ID, token);
					mapMessage.setString(Constants.DISPLAY_STRING, newsDetails.getNewsHeadline());
					mapMessage.setInt(Constants.BADGE_COUNT, 1);
					mapMessage.setString(Constants.DEVICE_TYPE, "ANDROID_BATCH");

					Map<String, String> data = new HashMap<String, String>();
					data.put(Constants.TEXT, newsDetails.getNewsHeadline());
					data.put(Constants.TYPE, String.valueOf(type));
					data.put(Constants.IMAGE_URL, newsDetails.getNewsImageUrl());
					data.put(Constants.ACTION_URL, newsDetails.getNewsUrl());
					mapMessage.setObject(Constants.DATA_MAP, data);

					mapMessage.setObject("REGISTRATION_IDS", registrationIds);
					mapMessage.setObject("USER_IDS", userIds);
					mapMessage.setObject("NEWS_ID", newsDetails.getNewsId());

					return mapMessage;
				}
			});
		} catch (Exception ex) {
			LOGGER.error("error occured while sending push in batch, exception={}", ex.getMessage());
		}
	}
	
	@Override
	public void sendPushBatchMostRecentNewsOfLanguageId(List<Object[]> tokensAndUserId,
			 long type, int languageId) {
		// Get the destination to push and the size of batch
		String destination = dbConfigService.getProperty(Properties.PUSH_DESTINATION.getProperty(),
				Properties.PUSH_DESTINATION.getDefaultValue());
		
		News lastEnglishNews = newsRepo.findTopByLanguageIdOrderByNewsIdDesc(languageId);
		NewsDetails newsDetails;
		if(lastEnglishNews != null) {
			LOGGER.info("last inserted news  with languageId={} found with newsid={}", languageId, lastEnglishNews.getNewsId());
			newsDetails = newsDetailsRepo.findByNewsId(lastEnglishNews.getNewsId());
		} else {
			LOGGER.warn("failed to find the last inserted news id with languageId={}, falling back to any last inserted news");
			newsDetails = newsDetailsRepo.findFirstByOrderByNewsIdDesc();
		}
		
		try {
			jmsTemplate.setDefaultDestinationName(destination);
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					LOGGER.debug("Prepairing the MapMessage");
					MapMessage mapMessage = session.createMapMessage();
					// mapMessage.setString(Constants.ID, token);
					mapMessage.setString(Constants.DISPLAY_STRING, newsDetails.getNewsHeadline());
					mapMessage.setInt(Constants.BADGE_COUNT, 1);
					mapMessage.setString(Constants.DEVICE_TYPE, "ANDROID_BATCH");

					Map<String, String> data = new HashMap<String, String>();
					data.put(Constants.TEXT, newsDetails.getNewsHeadline());
					data.put(Constants.TYPE, String.valueOf(type));
					data.put(Constants.IMAGE_URL, newsDetails.getNewsImageUrl());
					data.put(Constants.ACTION_URL, newsDetails.getNewsUrl());
					mapMessage.setObject(Constants.DATA_MAP, data);
					
					List<Long> userIds = new ArrayList<>(tokensAndUserId.size());
					List<String> registrationIds = new ArrayList<>(tokensAndUserId.size());
					int counter = 0;

					for (Object[] deviceTokens : tokensAndUserId) {
						String tempReg = deviceTokens[0].toString();
						Long tempUserId = Long.valueOf((deviceTokens[1]).toString());

						registrationIds.add(tempReg);
						userIds.add(tempUserId);
						counter++;
						LOGGER.debug("Added regid={} and userId={}", tempReg, tempUserId);
					}
					LOGGER.info("number of objects added in mapMessage={}", counter);
					mapMessage.setObject("REGISTRATION_IDS", registrationIds);
					mapMessage.setObject("USER_IDS", userIds);
					mapMessage.setObject("NEWS_ID", newsDetails.getNewsId());

					return mapMessage;
				}
			});
		} catch (Exception ex) {
			LOGGER.error("error occured while sending push in batch, exception={}", ex.getMessage());
		}
	}

	@Override
	public TopicStatus sendTopic(List<Long> ids) throws UnsupportedEncodingException {
		List<Long> succeededList = new ArrayList<>();
		List<Long> failedList = new ArrayList<>();
		List<NewsDetails> listDetails = newsDetailsRepo.findAllById(ids);
		//String topicName = "BREAKING_NEWS";
		
		RequestBodySpec requestBodySpec = WebClient.create(Constants.FCM_ENDPOINT)
		 		.post()
		 		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.AUTHORIZATION, "key=" + Constants.FCM_KEY);
		
		for(NewsDetails ele : listDetails) {
			String headlines = ele.getNewsHeadline();
			String imageUrl = ele.getNewsImageUrl();
			String newsUrl = ele.getNewsUrl();
			Long newsId = ele.getNewsId();
			
			JSONObject customFieldMap = new JSONObject();
			customFieldMap.put("text", headlines);
			customFieldMap.put("type", 2);
			customFieldMap.put("image URL", imageUrl);
			customFieldMap.put("action URL", newsUrl);
			
			JSONObject payload = new JSONObject();
			payload.put("JSON", URLEncoder.encode(customFieldMap.toString(), "UTF-8")); //StandardCharsets.UTF_8
			
			LOGGER.debug("customFieldMap={}", customFieldMap);
			JSONObject body = new JSONObject();
			body.put("to", "coQEWt8sQoquDBNGv6IKKV:APA91bEOPAtLExRu0IUy5m7Pdy0S52EW2jTsng0hGEEEVQryKir5j_Pvm1Rc6SWvEdvjISHrKX10p5-b8X7fQl1B80PooUGxgJytVR-27KNVz737aBBWGggsW9cvvHID-i6_NZ8k5-ce");
			body.put("data", payload);
			
			LOGGER.debug("request body={}", body.toString());
			String response = requestBodySpec
				.body(Mono.just(body.toString()), String.class)
				.retrieve()
				.bodyToMono(String.class)
				.block();
			LOGGER.debug("response body={}", response);
			
			//TODO: if succeeded add newsId into succeededList else add into failedList
			//TODO: if topic limit exceeded then break the loop and respond with a reason 
			// or add remaining elements in failedList
			succeededList.add(newsId);
		}
		return new TopicStatus(succeededList, failedList);
	}
	
}
