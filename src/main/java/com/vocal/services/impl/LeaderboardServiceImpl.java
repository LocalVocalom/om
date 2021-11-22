package com.vocal.services.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.UserInsurance;
import com.vocal.repos.jpa.UserInsuranceRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.LeaderboardService;
import com.vocal.utils.Constants;
import com.vocal.utils.Properties;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
	
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardServiceImpl.class);
	
	private static final String GLOBAL_LEADERBOARD_KEY = "global";
	
	private static final String keyPrefix = "referral";
	
	private String[] starredArray = new String[] {"", "X", "XX", "XXX", "XXXX", "XXXXX", "XXXXXX"};
	
	private Map<Long, String> userIdAndNameMap = new HashMap<>();
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private UserInsuranceRepo userInsuranceRepo;
	
	@Value("${maxSupportedRange:500}")
	private int maxSupportedRange;
	
	@Value("${maxNumberOfPreviousDays:31}")
	private int maxNumberOfPreviousDays;
	
	@Value("${isScoreIncluded:false}")
	private boolean isScoreIncluded;

	@Override
	public boolean incrementReferralCounterOfUser(long userId) {
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		boolean hasSucceeded = false;
		try {
			Calendar calendar = Calendar.getInstance();
			String key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("_").append(calendar.get(Calendar.MONTH)).append("_").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
			key = keyPrefix + "_" + key.toString();
			LOGGER.info("inserting into  key={}, globalKey={}", key , GLOBAL_LEADERBOARD_KEY);
			zSetOperations.incrementScore(key, userId, 1);
			zSetOperations.incrementScore(GLOBAL_LEADERBOARD_KEY, userId, 1);
			hasSucceeded = true;
		} catch (Exception ex) {
			LOGGER.error("Failed to increment referralCount in leaderboard, exception={}", ex.getMessage());
		}
		return hasSucceeded;
	}
	
	@Override
	public boolean incrementReferralCounterOfUserOfSpecificCity(long userId, String cityName) {
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		boolean hasSucceeded = false;
		try {
			zSetOperations.incrementScore(cityName, userId, 1);
			hasSucceeded = true;
		} catch (Exception ex){
			LOGGER.error("failed to increment referralCounter in leaderboard for city={}, userId={}, exception={}", cityName, userId, ex.getMessage());
		}
		return hasSucceeded;
	}

	@Override
	public String getTopNUserOrderedByReferralCountDesc(int top) {		
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		if(isInvalidRangeRequest(top)) {
			LOGGER.error("Range too big, range={}, maxSupported={}", top, maxSupportedRange);
			return "RANK_NOT_SUPPORTED";
		}
				
		Calendar calendar = Calendar.getInstance();
		String key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("_").append(calendar.get(Calendar.MONTH)).append("_").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
		key = keyPrefix + "_" + key.toString();
		LOGGER.info("getting from key={}", key);
		
		if(isScoreIncluded) {
			Set<TypedTuple<Long>> reverseRangeWithScores = zSetOperations.reverseRangeWithScores(key, 0, top);
			return processTuples(reverseRangeWithScores);
		} else {
			return zSetOperations.reverseRange(key, 0, top).toString();
		}
	}
	
	@Override
	public String getTopNUserOrderedByReferralCountDescHtmlFormatted(int top) {
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		if(isInvalidRangeRequest(top)) {
			LOGGER.error("Range too big, range={}, maxSupported={}", top, maxSupportedRange);
			return "RANK_NOT_SUPPORTED";
		}
				
		Calendar calendar = Calendar.getInstance();
		String key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("_").append(calendar.get(Calendar.MONTH)).append("_").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
		key = keyPrefix + "_" + key.toString();
		LOGGER.info("getting from key={}", key);
		
		if(isScoreIncluded) {
			Set<TypedTuple<Long>> reverseRangeWithScores = zSetOperations.reverseRangeWithScores(key, 0, top);
			return processTuplesHtmlEncoded(reverseRangeWithScores);
		} else {
			return zSetOperations.reverseRange(key, 0, top).toString();
		}
	}
	

	@Override
	public String getTopNUserOrderedByReferralCountDescNDaysAgo(int top, int days) {
		if(isInvalidDaysAgo(days)) {
			return "DAYS_NOT_SUPPORTED";
		}
		
		if(isInvalidRangeRequest(top)) {
			return "RANK_NOT_SUPPORTED";
		}
		
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		
		String key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("_").append(calendar.get(Calendar.MONTH)).append("_").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
		key = keyPrefix + "_" + key.toString();
		LOGGER.info("getting from key={}", key);
		if(isScoreIncluded) {
			Set<TypedTuple<Long>> reverseRangeWithScores = zSetOperations.reverseRangeWithScores(key, 0, top);
			return processTuples(reverseRangeWithScores);
		} else {
			return zSetOperations.reverseRange(key , 0, top).toString();
		}
	}

	@Override
	public String getTopNUserOfOrderedByReferralCountGlobalLeaderboard(int top) {
		if(isInvalidRangeRequest(top)) {
			return "RANK_NOT_SUPPORTED";
		}
		
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		if(isScoreIncluded) {
			Set<TypedTuple<Long>> reverseRangeWithScores = zSetOperations.reverseRangeWithScores(GLOBAL_LEADERBOARD_KEY, 0, top);
			return processTuples(reverseRangeWithScores);
		} else {
			return zSetOperations.reverseRange(GLOBAL_LEADERBOARD_KEY, 0, top).toString();
		}
	}
	
	@Override
	public String getTopNUserOfSpecificCityOrderedByReferralCountDesc(int top, String cityName) {
		if(isInvalidRangeRequest(top)) {
			return "RANK_NOT_SUPPORTED";
		}
		
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		if(isScoreIncluded) {
			Set<TypedTuple<Long>> reverseRangeWithScores = zSetOperations.reverseRangeWithScores(cityName, 0, top);
			return processTuples(reverseRangeWithScores);
		} else {
			return zSetOperations.reverseRange(cityName, 0, top).toString();
		}
	}
	
	@Override
	public String getTopNUserOfSpecificCityOrderedByReferralCountDescHtmlFormatted(int top, String cityName) {
		if(isInvalidRangeRequest(top)) {
			return "RANK_NOT_SUPPORTED";
		}
		
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		if(isScoreIncluded) {
			Set<TypedTuple<Long>> reverseRangeWithScores = zSetOperations.reverseRangeWithScores(cityName, 0, top);
			return processTuplesHtmlEncodedForGzb(reverseRangeWithScores);
		} else {
			return zSetOperations.reverseRange(cityName, 0, top).toString();
		}
	}

	@Override
	public long calculateRankOfUserWithUserIdOrderedByReferralCountDesc(long userId) {
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		
		Calendar calendar = Calendar.getInstance();		
		String key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("_").append(calendar.get(Calendar.MONTH)).append("_").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
		key = keyPrefix + "_" + key.toString();
		
		Long rank = zSetOperations.reverseRank(key, userId); //zSetOperations.rank(key, userId);
		if(rank == null) {
			return -1;
		}
		return rank;
	}
	
	@Override
	public long calculateRankOfUserWithUserIdOrderedByReferralCountDescNDaysAgo(long userId, int days) {
		if(isInvalidDaysAgo(days)) {
			LOGGER.info("invalid days={}", days);
			return 0;
		}
		
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		
		String key = new StringBuilder().append(calendar.get(Calendar.YEAR)).append("_").append(calendar.get(Calendar.MONTH)).append("_").append(calendar.get(Calendar.DAY_OF_MONTH)).toString();
		key = keyPrefix + "_" + key.toString();
		
		Long rank = zSetOperations.reverseRank(key, userId);
		if(rank == null) {
			return -1;
		}
		return rank;
	}

	@Override
	public long calculateRankOfUserWithUserIdOrderedByReferralCountDescGlobalLeaderboard(long userId) {
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		Long rank = zSetOperations.reverseRank(GLOBAL_LEADERBOARD_KEY, userId);
		if(rank == null) {
			return -1;
		}
		return rank;
	}
	
	/**
	 * Process and returns the String representation of List of tuples with their scores
	 * @param tuples the set of typed tuples received from rankWithScore() etc. methods
	 * @return String representation of Lists of List of values and their scores. Example [ [userId1, score1], [userId2, score2], ... ]
	 */
	private String processTuples(Set<TypedTuple<Long>> tuples) {
		List<List<Serializable>> rows = new ArrayList<>();
		for (TypedTuple<Long> tuple : tuples) {
		      rows.add(Arrays.asList(String.valueOf(tuple.getValue()), tuple.getScore()));
		}
		return rows.toString();
	}
	
	/**
	 * Process tuples and encode in html table 
	 */
	private String processTuplesHtmlEncodedForGzb(Set<TypedTuple<Long>> tuples) {
		StringBuilder sb = new StringBuilder("");
		int[] prizes = new int[] {51000, 25000, 11000, 1000};
		int prizeLength = prizes.length;
		int index = 1;
		for (TypedTuple<Long> tuple : tuples) {
			String finalUserIdOrName = null;
			long userId = tuple.getValue();
			if(userIdAndNameMap.get(userId) == null) {
				String userIdStr =String.valueOf(userId);
				int userIdLength = userIdStr.length();
				int lastTwoDigits = (int) ( tuple.getValue()  / Math.pow(10, userIdLength - 2) );
				int unitDigit = (int) (tuple.getValue() % 10);
				LOGGER.info("userId={}, lastTwoDigits={}, unitDigit={}", userId, lastTwoDigits, unitDigit);
				int numberOfStarred = userIdLength - 3;
				String starredString = starredArray[numberOfStarred];
				LOGGER.info("starredString={}", starredString);
			
				finalUserIdOrName = lastTwoDigits + starredString + unitDigit;
			} else {
				finalUserIdOrName = userIdAndNameMap.get(userId);
			}
			
			LOGGER.info("finalUserId={}", finalUserIdOrName);
			sb.append("<tr><td>").append(index).append("</td><td>").append(finalUserIdOrName).append("</td><td>");
			// The third column which is for price
			sb.append("₹");
			sb.append(index > prizeLength ? prizes[prizeLength-1] : prizes[index - 1]);
			sb.append("</td></tr>");
			index++;
		}
		return sb.toString();
	}
	
	/**
	 * Process tuples and encode in html table 
	 */
	private String processTuplesHtmlEncoded(Set<TypedTuple<Long>> tuples) {
		StringBuilder sb = new StringBuilder("");
		int index = 1;
		for (TypedTuple<Long> tuple : tuples) {
			sb.append("<tr><td>").append(index).append("</td><td>").append(tuple.getValue()).append("</td><td>").append(tuple.getScore()).append("</td></tr>");
			index++;
		}
		return sb.toString();
	}
	
	@Scheduled(fixedDelay = 300000)
	private void fillTheUserIdNameMapPeriodically() {
		ZSetOperations<String, Long> zSetOperations = redisTemplate.opsForZSet();
		Integer gzbTopN = dbConfigService.getIntProperty(Properties.GZB_TOP_N.getDefaultValue(), Properties.GZB_TOP_N.getDefaultValueAsInt());
		if(userIdAndNameMap.size() >= (2 * gzbTopN) ) {
			LOGGER.warn("userIdAndName map size={} re-initializing to emapty", userIdAndNameMap.size());
			userIdAndNameMap = new HashMap<>(); // re-initializing
		}
		
		Set<Long> reverseRange = zSetOperations.reverseRange(Constants.GZB, 0, gzbTopN);
		LOGGER.info("refreshing userIdAndName for for city={} for top={} records", Constants.GZB, gzbTopN);
		Iterator<Long> iterator = reverseRange.iterator();
		while (iterator.hasNext()) {
			Long nextId = iterator.next();
			UserInsurance userInsurance = userInsuranceRepo.findByUserId(nextId);
			if(userInsurance != null && userInsurance.getName() != null && !userIdAndNameMap.containsKey(nextId)) {
				LOGGER.info("putting userId = {} with name = {} in userIdAndNameMap", nextId, userInsurance.getName());
				userIdAndNameMap.put(nextId, userInsurance.getName());
			}
		}
	}
	
	/**
	 * Checks whether the passed range is valid
	 * @param range the range
	 * @return true if the passed range is invalid otherwise false
	 */
	private boolean isInvalidRangeRequest(int range) {
		if(range < 0 || range > maxSupportedRange) {
			LOGGER.info("maxSupportedRange={}", maxSupportedRange);
			return true;
		}
		return false;
	}
	
	/**
	 * To check whether the passed number of days ago is valid or not
	 * @param days the number of days ago
	 * @return true if invalid days ago, otherwise false
	 */
	private boolean isInvalidDaysAgo(int days) {
		LOGGER.info("maxNumberOfPreviousDays={} passed days={}", maxNumberOfPreviousDays, days);
		if(days < 0 || days > maxNumberOfPreviousDays ) {
			return true;
		}
		return false;
	}
}
