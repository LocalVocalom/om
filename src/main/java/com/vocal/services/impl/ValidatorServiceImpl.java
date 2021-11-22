package com.vocal.services.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.vocal.entities.UserProfile;
import com.vocal.entities.UserProfile.LoginMedium;
import com.vocal.services.DbConfigService;
import com.vocal.services.ValidatorService;
import com.vocal.utils.Constants;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.SocialResponse;
import com.vocal.viewmodel.TrueCallerResponse;

@Service
public class ValidatorServiceImpl implements ValidatorService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorServiceImpl.class);
	
	@Autowired
	private DbConfigService dbConfigService;
	
	private static GoogleIdTokenVerifier verifier;
	
	private String[] fields = {"id", "email", "name"};
	
	
	@PostConstruct
	public void init() throws GeneralSecurityException, IOException {
		String googleClientId = dbConfigService.getProperty(Properties.GOOGLE_CLIENT_ID.getProperty(), Properties.GOOGLE_CLIENT_ID.getDefaultValue());
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
				.setAudience(Collections.singletonList(googleClientId))
				.build();		
	}

	@Override
	public SocialResponse getSocialLoginDetails(String loginMedium, String accessToken) {
		SocialResponse socialResponse = null;
		try {
			if(loginMedium != null && accessToken != null && !loginMedium.equals("") && !accessToken.equals("")) {
				if(loginMedium.equals("google")) {
					socialResponse = isGoogleVerified(accessToken);
				} else if(loginMedium.equals("facebook")) {
					socialResponse = isFacebookVerified(accessToken);
				} else {
					LOGGER.debug("loginMedium={}, accessToken={} not defined", loginMedium, accessToken);
				}
				//return socialResponse;
			}	
		} catch(Exception e) {
			LOGGER.error("error raised during social details fetch loginMedium={}, accessToken={}", loginMedium, accessToken);
		}
		return socialResponse;
	}

	private SocialResponse isFacebookVerified(String accessToken) {
		SocialResponse socialResponse = null;
		org.springframework.social.facebook.api.User fbProfile = null;
		Facebook facebook = new FacebookTemplate(accessToken);
		fbProfile = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
		LOGGER.info("data fetched from fb accessToken={}", accessToken);
		if(fbProfile != null) {
			socialResponse = new SocialResponse();
			socialResponse.setSocialId(fbProfile.getId());
			socialResponse.setSocialName(fbProfile.getName());
			socialResponse.setLoginMedium(LoginMedium.FACEBOOK);
			socialResponse.setSocialPicUrl("https://graph.facebook.com/" + fbProfile.getId() + "/picture?type=normal");
			boolean emailVerified = true;
			socialResponse.setEmailVerified(emailVerified);
			socialResponse.setEmail(fbProfile.getEmail());
			LOGGER.debug("facebook data accessToken={}, fbProfile={}", accessToken, fbProfile);
		}
		return socialResponse;
	}

	private SocialResponse isGoogleVerified(String accessToken) {
		GoogleIdToken idToken = null;
		SocialResponse socialResponse = null;
		try {
			idToken = verifier.verify(accessToken);
			if(idToken != null) {
				Payload payload = idToken.getPayload();
				socialResponse = new SocialResponse();
				socialResponse.setSocialId(payload.getSubject());
				socialResponse.setSocialName( (String) payload.get("name"));
				socialResponse.setLoginMedium(LoginMedium.GOOGLE);
				socialResponse.setSocialPicUrl((String) payload.get("picture"));
				Boolean isEmailVerified = Boolean.valueOf(payload.getEmailVerified());
				String email = payload.getEmail();
				socialResponse.setEmailVerified(isEmailVerified);
				socialResponse.setEmail(email);
			}
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.error("accessToken={}, google verification failed exp={}", accessToken, e.getMessage());
		}
		return socialResponse;
	}

	@Override
	public void userVerifierHandler(Long userId, UserProfile userProfile, String loginMedium, String accessToken) {
		// TODO Auto-generated method stub
		LOGGER.debug("Inimplemented method");
	}

	@Override
	public boolean trueCallerValidation(String payload, String signedString, String signatureAlgorithm) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<TrueCallerResponse[]> respEntity = restTemplate.getForEntity(Constants.TRUE_CALLER_ENDPOINT,
				TrueCallerResponse[].class);
		TrueCallerResponse[] realResp = respEntity.getBody();
		String keyType = realResp[0].getKeyType();
		String key = realResp[0].getKey();

		LOGGER.debug("keyType={}, key={}", keyType, key);
		boolean verifyStatus = false;
		try {
			verifyStatus = verifyTrueCaller(keyType, key, payload, signedString, signatureAlgorithm);
			LOGGER.debug("verification returned=" + verifyStatus);
		} catch (Exception e) {
			LOGGER.error("Exception orccrued while verifying the true caller payload");
		}
		return verifyStatus;
	}
	
	private boolean verifyTrueCaller(final String keyType, final String publicKeyString, final String payload,
			final String signedString, final String signatureAlgorithm) throws Exception {
		final byte[] publicKeyBytes = DatatypeConverter.parseBase64Binary(publicKeyString);
		final PublicKey publicKey = KeyFactory.getInstance(keyType)
				.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

		final byte[] signatureByteArray = Base64.decodeBase64(signedString.getBytes(StandardCharsets.UTF_8));
		final byte[] payloadArray = payload.getBytes(StandardCharsets.UTF_8);

		Signature vSignature = Signature.getInstance(signatureAlgorithm);
		vSignature.initVerify(publicKey);
		vSignature.update(payloadArray);

		return vSignature.verify(signatureByteArray);
	}

}
