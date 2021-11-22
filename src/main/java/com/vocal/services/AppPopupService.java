package com.vocal.services;

import java.util.Date;

import com.vocal.viewmodel.AppPopupDto;

public interface AppPopupService {

	// AppPopupDto getAppropriatePopupDto(long userId, String appVersion, int languageId, Date createdTime);

	AppPopupDto getPrioritizedPopupDto(long userId, String appVersion, int languageId, Date createdTime);

	AppPopupDto getUpdateAppPopup(String appVersion, int languageId);

	AppPopupDto getInsuranceAppPopup(long userId, int languageId, Date createdTime);

	AppPopupDto getRateUsAppPopup(long userId, int languageId, Date createdTime);

	void refresh();
	
}
