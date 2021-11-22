package com.vocal.services;

import com.vocal.entities.UserProfile;
import com.vocal.viewmodel.SocialResponse;

public interface ValidatorService {

	SocialResponse getSocialLoginDetails(String loginMedium, String accessToken);

	void userVerifierHandler(Long userId, UserProfile userProfile, String loginMedium, String accessToken);
	
	// Added for true caller validation
	boolean trueCallerValidation(String payload, String signedString, String signatureAlgorithm);

}
