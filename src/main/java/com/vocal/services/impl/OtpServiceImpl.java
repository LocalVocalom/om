package com.vocal.services.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.vocal.entities.OtpHashed;
import com.vocal.repos.jpa.OtpHashedRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.OtpService;
import com.vocal.utils.Properties;


@Service
public class OtpServiceImpl implements OtpService {

	@Value("${OTP_DURATION}")
	private int EXPIRE_MINUTES;
	
//	private LoadingCache<String, Integer> otpCache;
	

	//private Cache<String, Integer> otpCache;
	private Cache<String, String> otpCache;
	
	
	@Autowired
	private OtpHashedRepo otpHashedRepo;
	
	@Autowired
	private DbConfigService dbConfigService;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(OtpServiceImpl.class);
	
	public OtpServiceImpl() {
		super();
		LOGGER.info("no-arg Constructor is being executed");
		
		otpCache = Caffeine.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES)
				.maximumSize(100)
				.build(new com.github.benmanes.caffeine.cache.CacheLoader<String, String>() {
					@Override
					public @Nullable String load(@NonNull String key) throws Exception {
						return null;
					}
				});
	}

	@Override
	public String generateOtp(String key) {
		String otp = RandomStringUtils.randomAlphanumeric(6);
		LOGGER.debug("generated otp={}", otp);
		Boolean isOtpCacheEnabled = dbConfigService.getBooleanProperty(Properties.IS_OTP_CACHE_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_OTP_CACHE_ENABLED.getDefaultValue()));
		if(isOtpCacheEnabled) {
			otpCache.put(key, otp);
		} else {
			OtpHashed otpHashed = otpHashedRepo.findByMobileNum(key);
			if(otpHashed == null) {
				otpHashed = new OtpHashed();
				otpHashed.setMobileNum(key);
				otpHashed.setOtp(otp);
			} else {
				otpHashed.setOtp(otp);
			}
			otpHashedRepo.save(otpHashed);
		}
		LOGGER.debug("cache after putting a values as map={}", otpCache.asMap());
		return otp;
	}
	
	@Override
	public String getOtp(String key) {
		try {
			LOGGER.debug("cache while getting values as map={}", otpCache.asMap());
			//return otpCache.get(key);
			Boolean isOtpCacheEnabled = dbConfigService.getBooleanProperty(Properties.IS_OTP_CACHE_ENABLED.getProperty(), Boolean.valueOf(Properties.IS_OTP_CACHE_ENABLED.getDefaultValue()));
			if(isOtpCacheEnabled) {
				return otpCache.getIfPresent(key);
			} else {
				OtpHashed otpHashed = otpHashedRepo.findByMobileNum(key);
				return otpHashed.getOtp();
			}	
		} catch (Exception ex) {
			LOGGER.debug("Exception while fetching key, exception = {}", ex.getMessage());
			return null;
		}
	}
	
	@Override
	public void clearOtp(String key) {
		otpCache.invalidate(key);
	}
	
	
}
