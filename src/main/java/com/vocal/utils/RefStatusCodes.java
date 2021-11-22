package com.vocal.utils;

/**
 * The ref status codes, to trace, investigate the reasons
 *
 */
public class RefStatusCodes {

	/**
	 * To  indicate if referral is to be counted after all the checks
	 */
	public static final int COUNTABLE_REF_STATUS = 1;
	
	/**
	 * To indicate if a referral with status 1 is successfully processed
	 */
	public static final int SUCCESSFULLY_PROCESSED_REF_STATUS = 201;
	
	/**
	 * To indicate, if the referral was disabled at that time. 
	 */
	public static final int REFERRAL_DISABLED = -701;
	
	/**
	 * To indicate, if a user is registering with multiple numbers and it exceeds the allowed duplicate registrations considered for referral.
	 */
	public static final int MORE_THAN_ALLOWED_DUPLICATE_DEVICES = -702;

	/**
	 * To indicate, if the utm parameters string was modified or tempered and decryption failed.
	 */
	public static final int UTM_PARAMS_DECRYPTION_OR_PARSING_ERROR = -703;

	/**
	 * To indicate, if the decrypted id and the undecrypted id  mismatches. 
	 */
	public static final int ID_AND_DECRYPTED_ID_MISMATCH = -704;

	/**
	 * To indicate, if the app is opened after the expiry of campaign time. 
	 */
	public static final int APP_OPEN_AFTER_CAMPAIGN_TIME = -705;

	/**
	 * To indicate, if the new user registering is not registering with the latest version of the app.
	 */
	public static final int APP_VERSION_NOT_LATEST = -706;

	/**
	 * To indicate, if the referral is not eligible because of configured randomness.
	 */
	public static final int NOT_ELIGIBLE_BY_RANDOMNESS = -707;

	/**
	 * To indicate, if the referral is not eligible because of blacklisted ip address
	 */
	public static final int NOT_ELIGIBLE_BY_BLACKLISTED_IP = -708;

	/**
	 * To indicate, if the referral is not eligible because of multiple user coming from same user_source and ip address
	 */
	public static final int NOT_ELIGIBLE_BY_MULTI_USER_SAME_IP_SAME_UTM_SOURCE = -709;

	/**
	 * To indicate, if the referred user logged in with google and the allowed google counter has been exceeded.
	 */
	public static final int NOT_ELIGIBLE_BY_EXCEEDING_ALLOWED_GOOGLE_COUNTER = -710;
	
	/**
	 * To indicate, if referral is disabled for google
	 */
	public static final int REFERRAL_DISABLED_FOR_GOOGLE = -711;
	
	
	/**
	 * To indicate, if the referral is disabled for trueCaller
	 */
	public static final int REFERRAL_DISABLED_FOR_TRUE_CALLER = -712;
	
	/**
	 * To indicate, if the utm_term is not to be considered from referral
	 */
	public static final int NOT_ELIGIBLE_BY_UTM_TERM = -713;
	
	/**
	 * To indicate, if before login records doesn't existed for a user.
	 */
	public static final int NOT_ELIGIBLE_BY_B4LOGIN = -714;
	
	/**
	 * To indicate, if the login type is the one which don't have referral
	 */
	public static final int NO_REFERRAL_FLOW_FOR_THIS_LOGIN_TYPE = -715;
	
	/**
	 * To indicate, if the logins through trueCaller and his counter exceeds upto the point of allowed trueCaller counter
	 */
	public static final int NOT_ELIGIBLE_DUE_TO_EXCEED_ALLOWED_TRUE_CALLER_COUNTER = -716;
	
	/**
	 * To indicate, if the new total in the account after adding the referral amount exceeds the allowed total.
	 * This error will remove only and only if the user redeems and decrees the amount in his account. 
	 * Otherwise, no new referral amount will be added in his account even though the referrer may be eligible and checks passed.
	 */
	public static final int NOT_ELIGIBLE_DUE_TO_NEW_TOTAL_EXCEEDING_ALLOWED_TOTAL = -717;
	
	
	/**
	 * To indicate, if not eligible because of randomness of quality check
	 */
	public static final int NOT_ELIGIBLE_DUE_TO_RAND_QUALITY_CHECK_LOGIC = -718;
	
	
	/**
	 * To indicate, if the device token was null, blank or empty or existing deviceToken
	 */
	public static final int NOT_ELIGIBEL_DUE_TO_NULL_EMPTY_OR_EXISTING_DEVICE_TOKEN = -719;
	
}
