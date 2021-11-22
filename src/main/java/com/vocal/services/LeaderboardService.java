package com.vocal.services;


/**
 * Defines services related to leaderboard
 * @author asheesh
 *
 */
public interface LeaderboardService {
	
	/**
	 * This method increments referral counter of passed userId in leaderboard.
	 * @param userId, the userId of the user of which referral counter need to be incremented
	 * @return true iff, incremented successfully, false if encounters any problem. 
	 */
	boolean incrementReferralCounterOfUser(long userId);

	/**
	 * Returns top n users who have most referral counts. By default it returns the data for current date.
	 * @param top the number of records to be queried
	 * @return string representation of array of userIds, separated by commas.
	 */
	String getTopNUserOrderedByReferralCountDesc(int top);
	
	
	/**
	 * This method returns top n users who have most referral counts in the passed date.
	 * @param top number of records requested
	 * @param date the date for which the records are to be fetched
	 * @return String representation of array of userIds, separated by commas.
	 */
	// String getTopNUserOrderedByReferralCountDescInTheDate(int top, Date date);
	
	/**
	 * Returns top n users who have most referral counts m days ago from today. 
	 *  It uses the current date to get the date m days ago and queries for that date.
	 * @param top number of records requested
	 * @param days number of previous days
	 * @return String representation of array of userId, separated by commas
	 */
	String getTopNUserOrderedByReferralCountDescNDaysAgo(int top, int days);
	
	
	/**
	 * Returns top n users who have most referral counts overall ignoring date
	 * @param top the number of records requested
	 * @return String representation of array of userId, separated by commas
	 */
	String getTopNUserOfOrderedByReferralCountGlobalLeaderboard(int top);
	
	/**
	 * Returns the rank of a user with passed userId.
	 * @param userId the userId of the user to be ranked 
	 * @return the rank/index of the user
	 */
	long calculateRankOfUserWithUserIdOrderedByReferralCountDesc(long userId);
	
	/**
	 * Returns the rank of user with userId n days ago.
	 * @param userId the userId of user of which rank is to be calculated
	 * @param days the number of previous days
	 * @return
	 */
	long calculateRankOfUserWithUserIdOrderedByReferralCountDescNDaysAgo(long userId, int days);
	
	/**
	 * Returns the rank of a user with passed userId in global leaderboard
	 * @param userId the userId of the user to be ranked globally
	 * @return the rank/index of the user
	 */
	long calculateRankOfUserWithUserIdOrderedByReferralCountDescGlobalLeaderboard(long userId);

	String getTopNUserOrderedByReferralCountDescHtmlFormatted(int top);

	String getTopNUserOfSpecificCityOrderedByReferralCountDesc(int top, String cityName);

	boolean incrementReferralCounterOfUserOfSpecificCity(long userId, String cityName);

	String getTopNUserOfSpecificCityOrderedByReferralCountDescHtmlFormatted(int top, String cityName);
	
}
