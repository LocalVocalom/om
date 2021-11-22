package com.vocal.services;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.vocal.viewmodel.StatusDto;
import com.vocal.viewmodel.TopicStatus;

public interface PushMangerService {
	
	StatusDto sendPush(long userId, String text, String imageUrl, String actionUrl, long type);
	
	void sendPush(String token, String text, String imageUrl, String actionUrl, long type);
	
	TopicStatus sendTopic(List<Long> ids) throws UnsupportedEncodingException;

	void sendPushBatchMostRecentNewsOfLanguageId(List<Object[]> tokensAndUserId, long type, int languageId);

	void sendPushBatchNotificationBySeparatingTokensOfHindiAndNonHindiUsers(List<Object[]> tokensAndUserId);
	
}
