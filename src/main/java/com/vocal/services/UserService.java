package com.vocal.services;



import javax.servlet.http.HttpServletRequest;

import com.vocal.entities.User;
import com.vocal.viewmodel.AddressModel;
import com.vocal.viewmodel.InsuranceDto;
import com.vocal.viewmodel.LoginResponseDto;
import com.vocal.viewmodel.LoginTypeDto;
import com.vocal.viewmodel.StatusDto;

public interface UserService {
		
	StatusDto handleDeviceTokenUpdation(long userId, String deviceId, String androidId, String token);
	
	LoginResponseDto handleUserLogin(String email, String token, String ip, String deviceToken, String loginType, String utmSource,
			String utmMedium, String utmTerm, String utmCampaign, String appVersion, String deviceId, String name,
			String profilePicUrl, String androidVersion, String deviceName, String make, String model,
			String timeOfDevice);
	
	LoginResponseDto handleUserLoginV1(String email, String token, String ip, String deviceToken, String loginType,
			String utmSource, String utmMedium, String utmTerm, String utmCampaign, String appVersion, String deviceId,
			String androidId, String name, String profilePicUrl, String androidVersion, String deviceName, String make, String model,
			String timeOfDevice, long mobileNumber, String otp, String payload, String signedString, String signatureAlgo);

	boolean setAddress(long userId, User user, AddressModel address);

	InsuranceDto getInsurance(long userId, int languageId);

	StatusDto setState(long userId, int stateId, int languageId);
	//userId, stateId,cityId, langId
	StatusDto setStateWithCity(long userId, int stateId,  int cityId,int languageId) ;

	LoginTypeDto getLoginOptions();
	
	String sendOtp(long mobileNumber);
	
	boolean isUnauthorizedUser(long userId, String otp);

	boolean isUnauthorizedUser(User user, String otp);

	boolean isUnauthorizedUserV1(User user, String otp, HttpServletRequest request);
}
