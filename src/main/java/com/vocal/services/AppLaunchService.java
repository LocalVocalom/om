package com.vocal.services;

import com.vocal.viewmodel.AppLaunchDto;

public interface AppLaunchService {

	AppLaunchDto appLaunch(long userId, int languageId, String appVersion, boolean langChange);

}
