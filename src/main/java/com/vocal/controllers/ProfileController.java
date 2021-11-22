package com.vocal.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vocal.entities.User;
import com.vocal.entities.UserProfile;
import com.vocal.exceptions.AuthorizationException;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.ProfileService;
import com.vocal.services.UserService;
import com.vocal.utils.CommonParams;
import com.vocal.viewmodel.AccountSummaryDto;
import com.vocal.viewmodel.ProfileDto;
import com.vocal.viewmodel.SummaryDto;

@RestController
public class ProfileController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired 
	private UserProfileRepo userProfileRepo;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
	
	private Random rand = new Random();
	
	private Calendar calender = Calendar.getInstance();
	
	
	@PostMapping("/profilePicUpload")
	public ResponseEntity<?> profilePictureUplaod(
			@RequestParam(value = CommonParams.USERID) long userId, 
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = "profilePicUrl") String profilePicUrl,
			@RequestParam(value = CommonParams.NAME) String name,
			HttpServletRequest request
			) {
		LOGGER.info("/profilePicUpload userId={}, otp={}, profilePicUrl={}, name={}");
		
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		// ProfilePic and name updation
		UserProfile userProfile = userProfileRepo.findByUserId(userId);
		if(userProfile == null) {
			LOGGER.error("Profile doesn't exist with userId={}", userId);
		}
		userProfile.setProfilePick(profilePicUrl);
		userProfile.setName(name);
		userProfileRepo.save(userProfile);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/getProfileInfo")
	public ResponseEntity<?> profileInfo(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.PROFILE_ID) long profileId,
			@RequestParam(value = CommonParams.IS_REPORTER) boolean isReporter,
			HttpServletRequest request
			) {
		LOGGER.info("/getProfileInfo userId={}, otp={}, profileId={}", userId, otp, profileId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		
		ProfileDto dto;
		if(isReporter) {
			dto = profileService.getReporterProfileDetails(userId, profileId);
		} else {
			dto = profileService.getUserProfileDetails(userId, profileId);
		}
		return ResponseEntity.ok(dto);
	}

	@PostMapping("/getFollowers")
	public ResponseEntity<?> getFollowers(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.PROFILE_ID) long profileId,
			@RequestParam(value = CommonParams.IS_REPORTER) boolean isReporter,
			HttpServletRequest request
			) {
		LOGGER.info("/getFollowers userId={}, otp={}, profileId={}, isReporter={}", userId, otp, profileId, isReporter);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return ResponseEntity.ok(profileService.getProfileFollowers(profileId, isReporter));
	}
	
	@PostMapping("/getFollowings")
	public ResponseEntity<?> getFollowings(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.PROFILE_ID) long profileId,
			HttpServletRequest request
			) {
		LOGGER.info("/getFollowings userId={}, otp={}, profileId={}", userId, otp, profileId);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return ResponseEntity.ok(profileService.getProfileFollowings(profileId));
	}
	
	@PostMapping("/follow")
	public ResponseEntity<?> follow(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.PROFILE_ID) long profileId,
			@RequestParam(value = CommonParams.IS_REPORTER) boolean isReporter,
			@RequestParam(value = CommonParams.RATING, defaultValue = "0") long rating,
			HttpServletRequest request
			) {
		LOGGER.info("/follow userId={}, otp={}, profileId={}, isReporter={}, rating={}", userId, otp, profileId, isReporter, rating);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return ResponseEntity.ok(profileService.followReporterOrUser(userId, profileId, isReporter, rating));
	}
	
	@PostMapping("/unfollow")
	public ResponseEntity<?> unfollow(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = CommonParams.PROFILE_ID) long profileId,
			@RequestParam(value = CommonParams.IS_REPORTER) boolean isReporter,
			HttpServletRequest request
			) {
		LOGGER.info("/unfollow userId={}, otp={}, profileId={}, isReporter={}", userId, otp, profileId, isReporter);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			throw new AuthorizationException("Unauthorized Access with");
		}
		return ResponseEntity.ok(profileService.unfollowReporterOrUser(userId, profileId, isReporter));
	}
	
	@PostMapping("/getAccountSummary")
	public ResponseEntity<?> getAccountSummary(
			@RequestParam(value = CommonParams.USERID) long userId,
			@RequestParam(value = CommonParams.OTP) String otp,
			@RequestParam(value = "all", defaultValue = "true") boolean isAll,
			HttpServletRequest request
			) {
		LOGGER.info("/getAccountSummary userId={}, otp={}", userId, otp);
		User user = userRepo.findByUserId(userId);
		if(userService.isUnauthorizedUserV1(user, otp, request)) {
			LOGGER.error("Unauthorized Access with userId={}, otp={}", userId, otp);
			// SENDING A FAKE AccountSummaryDto SUCH THAT IT IS THE RANDOM FOR EVERY TIME IT IS REQUESTED
			List<SummaryDto> dtoSumarries = new ArrayList<>();
			Date createdTime = user.getCreatedTime();
			calender.setTime(createdTime);
			
			dtoSumarries.add(new SummaryDto(calender.getTime(), "Registration bonus", 1));
			int tempAmount = 1;
			// ADD redeem 
			int numOfSummmaryEntries = rand.nextInt(101);
			for (int i = 0; i < numOfSummmaryEntries; i++) {
				calender.add(Calendar.DAY_OF_MONTH, rand.nextInt(2));
				calender.add(Calendar.HOUR_OF_DAY, rand.nextInt(12));
				calender.add(Calendar.MINUTE, rand.nextInt(60));
				dtoSumarries.add(new SummaryDto(calender.getTime(), "Credited for referral", 5));
				tempAmount += 5;
				if(tempAmount > 30 && rand.nextBoolean()) {
					calender.add(Calendar.HOUR_OF_DAY, rand.nextInt(2));
					calender.add(Calendar.MINUTE, rand.nextInt(60));
					dtoSumarries.add(new SummaryDto(calender.getTime(), "PAYTM", -20));
					tempAmount -= 20;
				}
				
				if (rand.nextInt() % 5 == 0) {
					break;
				}
			}
			return ResponseEntity.ok(new AccountSummaryDto(dtoSumarries, tempAmount));
		}
		return ResponseEntity.ok(profileService.getAccountSummary(userId, isAll));
	}
	
}
