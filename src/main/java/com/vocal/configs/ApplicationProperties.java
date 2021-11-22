package com.vocal.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProperties {
	
	// TODO: to organize properties in a single class, so that they are not repeatedly used with @Value and are not scattered.
	
	/**
	 * First group of properteis
	 */
	@Value("${HOST_URL}")
	private String HOST_URL;
	
	@Value("${HOST_DIR}")
	private String HOST_DIR;
	
	@Value("${DISTANCE_RADIUS:200}")
	private double boundedDistance;
	
	@Value("${FLOORED_BOUNDED_DISTANCE:50}")
	private double flooredBoundedDistance;
	
	
	/**
	 * Second group of properties
	 */
	@Value("${THUMBNAIL_SCRIPT_PATH}")
	private String thumbnailScriptPath;
	
	@Value("${STATE_AS_CATEGORY_INDEX}")
	private int stateAsCategoryIndex;
	
	@Value("${NUMBER_OF_RECOMMENDATIOS}")
	private int numberOfRecommendations;
	
	@Value("INTERVAL_MINUTES")
	private int intervalMinutes;
	
	
	/**
	 * Third group of properties
	 */
	@Value("${NEWS_COUNT:10}")
	private int count;
	
	
	/**
	 * Fourth group of properties
	 */
	@Value("${CDN_ENABLED}")
	private boolean cdnEnabled;
	
	@Value("${IMAGE_BUCKET}")
	private String imageBucketUrl;
	
	@Value("${VIDEO_BUCKET}")
	private String videoBucketUrl;
	
	@Value("${KYC_BUCKET}")
	private String kycBucketUrl;
	
	@Value("${PROFILE_BUCKET}")
	private String profileBucketUrl;
	
	
	/**
	 * Fifth group of properties
	 */
	@Value("${IMAGE_AND_VIDEO_UPLOAD_SCRIPT}")
	private String videoAndThumbUploadScript;
	
	@Value("${IMAGE_ONLY_UPLOAD_SCRIPT}")
	private String imageOnlyUploadScript;
	
	@Value("${KYC_UPLOAD_SCRIPT}")
	private String kycUploadScript;
	
	@Value("${PROFILE_UPLOAD_SCRIPT}")
	private String profileUploadScript;
	
	
	/**
	 * Sixth group of properties
	 */
	@Value("${SMS_PROCESS_IP}")
	private String ip;
	
	@Value("${SMS_PROCESS_PORT}")
	private String port;
	
	@Value("${SMS_FORMAT}")
	private String msgTemplate;
	
	@Value("${SECRET_KEY}")
	private String encryptionKey;
	
	@Value("${OTP_DURATION}")
	private int EXPIRE_MINUTES;
	
	/**
	 *  Seventh set of values
	 */
	@Value("${maxSupportedRange}")
	private int maxSupportedRange;
	
	@Value("${maxNumberOfPreviousDays}")
	private int maxNumberOfPreviousDays;
	
	@Value("${isScoreIncluded}")
	private boolean isScoreIncluded;
	
}
