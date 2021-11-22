package com.vocal.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.services.LeaderboardService;
import com.vocal.utils.Constants;

@RestController
public class LeaderboardController {

	@Autowired
	private LeaderboardService leaderboardService;
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(LeaderboardController.class);
	
	@PostMapping("/rank")
	public ResponseEntity<?> getTopNReferralUsersRealtime(@RequestParam("top") int top) {
		LOGGER.info("/rank top={}", top);
		return ResponseEntity.ok(leaderboardService.getTopNUserOrderedByReferralCountDesc(top - 1));
	}
	
	@PostMapping("/rankDailyHtmlFormatted")
	public String getTopNReferralUsersRealtimeHtmlFormatted(@RequestParam("top") int top) {
		LOGGER.info("/rankDailyHtmlFormatted top={}", top);
		return leaderboardService.getTopNUserOrderedByReferralCountDescHtmlFormatted(top - 1);
	}
	
	@PostMapping("/rankGlobalGzb")
	public String getTopNReferralUsersFromCityGzbHtmlFormatted(@RequestParam("top") int top) {
		LOGGER.info("/rankGlobalGzb top={}", top);
		return leaderboardService.getTopNUserOfSpecificCityOrderedByReferralCountDescHtmlFormatted(top - 1, Constants.GZB);
	}
	
	@PostMapping("/rankGlobalGzbRaw")
	public String getTopNReferralUsersFromCityGzbWithScores(@RequestParam("top") int top) {
		LOGGER.info("/rankGlobalGzbRaw top={}", top);
		return leaderboardService.getTopNUserOfSpecificCityOrderedByReferralCountDesc(top - 1, Constants.GZB);
	}
	
	@PostMapping("/globalRank")
	public ResponseEntity<?> getTopNReferralUsersGloballyRealtime(@RequestParam("top") int top) {
		LOGGER.info("/globalRank top={}", top);
		return ResponseEntity.ok(leaderboardService.getTopNUserOfOrderedByReferralCountGlobalLeaderboard(top - 1));
	}
	
	@PostMapping("/calcRank")
	public ResponseEntity<?> calculateRankRealtime(@RequestParam("userId") long userId) {
		LOGGER.info("/calcRank userId={}", userId);
		return ResponseEntity.ok(leaderboardService.calculateRankOfUserWithUserIdOrderedByReferralCountDesc(userId));
	}
	
	@PostMapping("/calcGlobalRank")
	public ResponseEntity<?> calculateRankGloballyRealtime(@RequestParam("userId") long userId) {
		LOGGER.info("/calcGlobalRank userId={}", userId);
		return ResponseEntity.ok(leaderboardService.calculateRankOfUserWithUserIdOrderedByReferralCountDesc(userId));
	}
	
	@PostMapping("/rankDaysAgo")
	public ResponseEntity<?> getTopNReferralUsersNDaysAgo(@RequestParam("top") int top,
			@RequestParam("days") int days) {
		LOGGER.info("/rankDaysAgo top={}, days={}", top, days);
		return ResponseEntity.ok(leaderboardService.getTopNUserOrderedByReferralCountDescNDaysAgo(top - 1, days));
	}
	
	@PostMapping("/calcRankDaysAgo")
	public ResponseEntity<?> calculateRank(@RequestParam("userId") long userId,
			@RequestParam("days") int days) {
		LOGGER.info("/calcRankDaysAgo userId={}, days={}", userId, days);
		return ResponseEntity.ok(leaderboardService.calculateRankOfUserWithUserIdOrderedByReferralCountDescNDaysAgo(userId, days));
	}
}
