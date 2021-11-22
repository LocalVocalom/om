package com.vocal.utils;

public enum Properties {
	NEWS_SHARE_URL("NEWS_SHARE_URL", "https://localvocalnews.com/vokal/news?id=NEWS_ID"),
	INVITE_URL("INVITE_URL", "\nhttps://localvocalnews.com/vokal/ref?id=USER_ID"),
	INVITE_TEXT("INVITE_TEXT", "Hey, To get Local news download Vocal Local News App"),
	REPORT_TEXT("REPORT_TEXT", "What is the reason for reporting ?"),
	TERMS_LINK("TERMS_LINK", "http://google.com"),
	PUSH_DESTINATION("PUSH_DESTINATION", "VOKAL_PUSH_NOTIFICATION"),
	BATCH_PUSH_DESTINATION("BATCH_PUSH_DESTINATION", "VOKAL_BATCH_PUSH_NOTIFICATION"),
	MINIMUM("MINIMUM", "20"),
	MAXIMUM("MAXIMUM", "180"),
	CAMERA("CAMERA", "Camera"),
	GALARY("GALARY", "Galary"),
	TNC_HEADING("TNC_HEADING", "Take care of family, protect yourself"),
	TNC_POPUP_LIST("TNC_POPUP_LIST", "Only those aged 18 to 50 years will have insurance.\n" + 
			"You will not have to un-install the app until the insurance period. \n" + 
			"You have to open the app at least 1 time in 1 week. \n" + 
			"Your insurance will be effective within 9 days of verification.\n" + 
			"Fill the information correctly otherwise there may be a problem during the claim."),
	KYC_DONE_MSG("KYC_DONE_MSG", "Congratulations, You have completed insurance registration process. We are sending your data to New India Insurance Company. After 9 days you can download your policy and save."),
	
	TERMS_POINT_LIST("TERMS_POINT_LIST", "\"Incase you remove the 'be vokal news' app. your policy will be terminated.\"\n" +
			"\"You have to open the 'be vokal news' app once in a week, in case of failure your policy will be terminated.\""),
	
	POLICY_DETAIL_URL("POLICY_DETAIL_URL", "https://google.com"),
	
	POLICY_NUMBER("POLICY_NUMBER", "123456789012345"),
	
	HELPLINE_NUMBER("HELPLINE_NUMBER", "helplineNumber"),
	
	HELPLINE_EMAIL("HELPLINE_EMAIL", "randomHelplineEmail"),
	
	INSURENCE_REGISTRATION_TEXT("INSURENCE_REGISTRATION_TEXT", "Thank you for showing interest with us. Your policy is under process. Hence, do not forget to read news regularly."),

	INSURENCE_ACTIVE_AFTER_DAYS("INSURENCE_ACTIVE_AFTER_DAYS", "1"),
	
	CHOOSE_OPTIONS("CHOOSE_OPTIONS", "To create News Choose option"),
	REPORT_REASONS_LIST("REPORT_REASONS_LIST", "reason1,reason2,reason3,reason4,reason5"),
	AD_INTERVAL("AD_INTERVAL", "4"),
	TEST_AD_INTERVAL("TEST_AD_INTERVAL", "4"),
	SUM_INSURED_AMOUNT("SUM_INSURED_AMOUNT", "200000/-"),
	BANNER_ADS_ENABLED("BANNER_ADS_ENABLED", "true"),
	
	// LOGIN OPTIONS
	IS_FACEBOOK_LOGIN_ENABLED("IS_FACEBOOK_LOGIN_ENABLED", "true"),
	IS_GOOGLE_LOGIN_ENABLED("IS_GOOGLE_LOGIN_ENABLED", "true"),
	IS_TRUE_CALLER_LOGIN_ENABLED("IS_TRUE_CALLER_LOGIN_ENABLED", "true"),
	IS_MOBILE_NUMBER_LOGIN_ENABLED("IS_MOBILE_NUMBER_LOGIN_ENABLED", "true"),
	IS_QUALITY_CHECK_ENABLED("IS_QUALITY_CHECK_ENABLED", "false"),
	QUALITY_CHECK_COUNTER_LIMIT("QUALITY_CHECK_COUNTER_LIMIT", "3"),
	
	
	BETA_USERS("BETA_USERS", "121544, 121449, 105147, 123731"),
	MAINTAINERS_ID("MAINTAINERS_ID", "243361, 302970"),
	
	// POPUP_IDS
	BETA_POPUP_ID("BETA_POPUP_ID", "101"),
	INSURANCE_POPUP_ID("INSURANCE_POPUP_ID", "101"),
	UPDATE_POPUP_ID("UPDATE_POPUP_ID", "102"),
	RATE_US_POPUP_ID("RATE_US_POPUP_ID", "103"),
	RATE_US_SESSION_COUNTER("RATE_US_SESSION_COUNTER", "1"),
	RATE_POP_UP_INTERVAL("RATE_POP_UP_INTERVAL", "10"),
	POP_UP_PRIORITY("POP_UP_PRIORITY", "UPDATE INSURANCE RATE"),
	FORCED_UPDATE_VERSIONS("FORCED_UPDATE_VERSIONS", "noVersions"),
	FORCE_UPDATE_POPUP_ID("FORCE_UPDATE_POPUP_ID", "104"),
	NEW_USER_RATE_POP_UP_DAYS("NEW_USER_RATE_POP_UP_DAYS", "5"),
	
	
	LATEST_APP_VERSION("LATEST_APP_VERSION", "1.4.2"),
	BASE_APP_VERSION("BASE_APP_VERSION", "1.4.1"),
	ALTERNATE_APP_VERSION("ALTERNATE_APP_VERSION", "1.4.1"),
	ENCODED_LEVEL("ENCODED_LEVEL", "0-10-20-30"),
	
	LEVEL_ONE_MSG("LEVEL_ONE_MSG", "Coming soon to your location."),
	LEVEL_TWO_MSG("LEVEL_TWO_MSG", "Coming soon to your location."),
	LEVEL_THREE_MSG("LEVEL_THREE_MSG", "Coming soon to your location."),
	LEVEL_FOUR_MSG("LEVEL_FOUR_MSG", "Coming soon to your location."),
	
	LEVEL_ONE_HEAD("LEVEL_ONE_HEAD", "Post a news and earn money"),
	LEVEL_TWO_HEAD("LEVEL_TWO_HEAD", "Post a news and earn money"),
	LEVEL_THREE_HEAD("LEVEL_THREE_HEAD", "Post a news and earn money"),
	LEVEL_FOUR_HEAD("LEVEL_FOUR_HEAD", "Post a news and earn money"),
	
	LEVEL_ONE_SPL_MSG("LEVEL_ONE_SPL_MSG", "Post a news through app and get upto ₹10"),
	LEVEL_TWO_SPL_MSG("LEVEL_TWO_SPL_MSG", "Post a news through app and get upto ₹20"),
	LEVEL_THREE_SPL_MSG("LEVEL_THREE_SPL_MSG", "Post a news through app and get upto ₹50"),
	LEVEL_FOUR_SPL_MSG("LEVEL_FOUR_SPL_MSG", "Post a news through app and get upto ₹100"),
	
	LEVEL_ONE_SPL_HEAD("LEVEL_ONE_SPL_HEAD", "Post a news and earn money"),
	LEVEL_TWO_SPL_HEAD("LEVEL_TWO_SPL_HEAD", "Post a news and earn money"),
	LEVEL_THREE_SPL_HEAD("LEVEL_THREE_SPL_HEAD", "Post a news and earn money"),
	LEVEL_FOUR_SPL_HEAD("LEVEL_FOUR_SPL_HEAD", "Post a news and earn money"),

	REFERRAL_AMT("REFERRAL_AMT", "-1"),
	REFERRAL_UPTO_AMT("REFERRAL_UPTO_AMT", "-1"),
	REFERRAL_AMT_ADDITIONAL("REFERRAL_AMT_ADDITIONAL", "-1"),
	REFERRAL_TEXT("REFERRAL_TEXT", "Lorem Ipsum Random Referral Text"),
	REGISTRATION_GIFT_AMT("REGISTRATION_GIFT_AMT", "1.0"),
	FULL_COVER_MINUTES("FULL_COVER_MINUTES", "60"),
	ALLOWED_CAMPAIGN_MINUTES("ALLOWED_CAMPAIGN_MINUTES", "10"),
	ALLOWED_TRUE_CALLER_COUNTER("ALLOWED_TRUE_CALLER_COUNTER", "5"),
	ALLOWED_GOOGLE_COUNTER("ALLOWED_GOOGLE_COUNTER", "3"),
	IS_REFERRAL_ENABLED("IS_REFERRAL_ENABLED", "false"),
	IS_REFERRAL_FOR_TRUE_CALLER_ENABLED("IS_REFERRAL_FOR_TRUE_CALLER_ENABLED", "false"),
	IS_REFERRAL_FOR_GOOGLE_ENABLED("IS_REFERRAL_FOR_GOOGLE_ENABLED", "false"),
	IS_IP_CHECK_ENABLED("IS_IP_CHECK_ENABLED", "true"),
	DUPLICATE_DEVICE_COUNTER("DUPLICATE_DEVICE_COUNTER", "2"),
	REDEEM_URL("REDEEM_URL", "https://localvocalnews.com/"),
	IS_REDEEM_ENABLED("IS_REDEEM_ENABLED", "true"),
	EARN_MORE_URL("EARN_MORE_URL", "https://localvocalnews.com/"),
	MAX_ALLOWED_AMT("MAX_ALLOWED_AMT", "-1"),
	REDEEM_THRESHOLD("REDEEM_THRESHOLD", "50"),
	IS_OTP_CACHE_ENABLED("IS_OTP_CACHE_ENABLED", "false"),
	VOCAL_PAYTM_PAY("VOCAL_PAYTM_PAY", "https://localvocalnews.com/"),
	VOCAL_EARN_MORE("VOCAL_EARN_MORE", "https://localvocalnews.com/"),
	MIN_REMAINING_AMT("MIN_REMAINING_AMT", "10"),
	EARN_MORE_ACTION("EARN_MORE_ACTION", "web"),
	TICKER_TEXT("TICKER_TEXT", "Get ₹5 on every install(Click here)."),
	TICKER_ACTION("TICKER_ACTION", "shareApp"),
	TICKER_COLOR("TICKER_COLOR", "#0000ff"),
	REFERRAL_MSG("REFERRAL_MSG", "Congratulations, Your acccount has been credited with Rs. %s for referral."),
	NEW_USER_WELCOME_TEXT("NEW_USER_WELCOME_TEXT", "Thank you for joining Local Vocal family. Don't forget to get free insurance of ₹ 2 Lacs. Do it now."),
	OLD_USER_WELCOME_TEXT("OLD_USER_WELCOME_TEXT", "Welcome back to Local Vocal family."),
	GOOGLE_CLIENT_ID("GOOGLE_CLIENT_ID", "fakeGoogleClientId"),
	UPLOAD_MSG_ENABLED("UPLOAD_MSG_ENABLED", "true"),
	UPLOAD_MSG("UPLOAD_MSG", "Please do not upload any news as we are working on the system"),
	UPLOAD_NOT_ALLOWED_MSG("UPLOAD_NOT_ALLOWED_MSG", "Only verified users are allowed to upload news"),
	
	
	PUSH_BATCH_SIZE("PUSH_BATCH_SIZE", "100"),
	PUSH_DISTANCE_LOCAL("PUSH_DISTANCE_LOCAL", "50"),
	NEWS_VIEWS_MULTIPLIER("NEWS_VIEWS_MULTIPLIER", "10"),
	MISC_CATEGORIES("MISC_CATEGORIES", "1, 2, 3, 4"),
	MISC_NEWS_COUNT("MISC_NEWS_COUNT", "3"),
	IS_HOME_NEWS_CACHED("IS_HOME_NEWS_CACHED", "true"),
	IS_ISSUE_IN_REDEEM("IS_ISSUE_IN_REDEEM", "true"),
	REDEEM_DISABLED_MSG("REDEEM_DISABLED_MSG", "We're facing some technical issues, try again after 48 hours."),
	EXCEPTION_CITIES("EXCEPTION_CITIES", ""),
	EXCEPTIONAL_STATE("EXCEPTIONAL_STATE", "Uttar Pradesh"),
	IS_ELECTION_CAMPAIGN_ON("IS_ELECTION_CAMPAIGN_ON", "false"),
	GZB_TOP_N("GZB_TOP_N", "100"),
	// RANDOM start and end boundaries
	REF_AMT_FIRST_COUNTER_RANGE_START("REF_AMT_FIRST_COUNTER_RANGE_START", "8"),
	REF_AMT_FIRST_COUNTER_RANGE_END("REF_AMT_FIRST_COUNTER_RANGE_END", "15"),
	REF_AMT_OTHER_COUNTER_RANGE_START("REF_AMT_OTHER_COUNTER_RANGE_START", "5"),
	REF_AMT_OTHER_COUNTER_RANGE_END("REF_AMT_OTHER_COUNTER_RANGE_END", "8"),
	REFERRAL_AMT_DEFAULT("REFERRAL_AMT_DEFAULT", "5"),
	IS_MSISDN_CHECK_ENABLED_AT_REDEEM("IS_MSISDN_CHECK_ENABLED_AT_REDEEM", "true"),
	NOTIFY_COUNTER_START_LIMIT("NOTIFY_COUNTER_START_LIMIT", "2"),
	NOTIFY_COUNTER_END_LIMIT("NOTIFY_COUNTER_END_LIMIT", "50"),
	IGNORE_COUNTER_LIMIT("IGNORE_COUNTER_LIMIT", "10"),
	TABOOLA_ENABLED("TABOOLA_ENABLED", "true"),
	TABOOLA_INTERVAL("TABOOLA_INTERVAL", "0"),
	BREAKING_HOURS("BREAKING_HOURS", "2")
	;

	private String property;
	private String defaultValue;
	
	private Properties(String property, String defaultValue) {
		this.property = property;
		this.defaultValue = defaultValue;
	}
	
	public String getProperty() {
		return property;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public int getDefaultValueAsInt() {
		return Integer.parseInt(defaultValue);
	}
}
