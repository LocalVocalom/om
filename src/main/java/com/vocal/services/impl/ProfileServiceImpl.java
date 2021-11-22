package com.vocal.services.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vocal.entities.Follow;
import com.vocal.entities.Reporter;
import com.vocal.entities.User;
import com.vocal.entities.UserAccount;
import com.vocal.entities.UserAccountSummary;
import com.vocal.entities.UserProfile;
import com.vocal.exceptions.ConditionViolationException;
import com.vocal.exceptions.RecordNotFoundException;
import com.vocal.mapper.ProfileMapper;
import com.vocal.repos.jpa.FollowRepo;
import com.vocal.repos.jpa.ReporterRepo;
import com.vocal.repos.jpa.UserAccountRepo;
import com.vocal.repos.jpa.UserAccountSummaryRepo;
import com.vocal.repos.jpa.UserProfileRepo;
import com.vocal.repos.jpa.UserRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.ProfileService;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.AccountSummaryDto;
import com.vocal.viewmodel.FollowDto;
import com.vocal.viewmodel.ProfileDto;
import com.vocal.viewmodel.StatusDto;


@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private UserProfileRepo userProfileRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private FollowRepo followRepo;
	
	@Autowired
	private ReporterRepo reporterRepo;
	
	@Autowired
	private UserAccountRepo userAccountRepo;
	
	@Autowired
	private UserAccountSummaryRepo  userAccountSummaryRepo;
	
	@Autowired
	private ProfileMapper profileMapper;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);

	@Override
	public ProfileDto getUserProfileDetails(long userId, long profileId) {
		User user = userRepo.findByUserId(profileId);
		if(user == null) {
			LOGGER.error("user with userId={} not found", profileId);
			throw new RecordNotFoundException("user with userId=" + profileId + " not found");
		}
		UserProfile profile = userProfileRepo.findByUserId(profileId);
		if(profile == null) {
			LOGGER.error("profile with profileId={} not found", profileId);
			throw new RecordNotFoundException("profile with profileId=" + profileId + " doesn't exist");
		}
		
		double bal = 0;
		long refCount = 0;
		UserAccount userAccount = userAccountRepo.findByUserId(profileId);
		if(userAccount == null) {
			LOGGER.error("User Account for profileId={} not found", profileId);
		} else {
			bal = userAccount.getCurrentBalance();
			refCount = userAccount.getInviteCounter();
		}
		
		boolean isFollowing = followRepo.existsFollowByUserIdAndFollowerAndFollowType(profileId, userId, 0);
		ProfileDto dto = new ProfileDto();
		dto.setProfileId(profile.getUserId());
		dto.setName(profile.getName());
		dto.setProfilePicUrl(profile.getProfilePick()); 
		
		// null check
		Long followers = user.getFollowers() == null ? 0 : user.getFollowers();
		Long following = user.getFollowing() == null ? 0 : user.getFollowing();
		Integer postPublished = user.getPostPublished() == null ? 0 : user.getPostPublished();
		
		dto.setFollowersCount(followers);
		dto.setFollowingsCount(following);
		dto.setPostPublished(Long.valueOf(postPublished));
		
		double rating = 0;
		if(followers == 0 ) {
			// 0
		} else {
			rating = profile.getRating() / (double) followers;
			if(rating > 5.0) {
				rating = 5.0;
			}
			if(rating < 0.01) {
				rating = 0;
			}
			
		}
		
		dto.setRating(rating);
		dto.setAbout(profile.getAbout() == null ? "": profile.getAbout()); 

		dto.setBalance(String.valueOf(bal));
		dto.setAlreadyFollowing(isFollowing);
		
		dto.setReferralCount(String.valueOf(refCount));
		dto.setLevel(calculateLevel(user.getPostPublished()));
		
		dto.setProfileType("user");
		
		return dto;
	}
	
	private int calculateLevel(Integer postPublished) {
		LOGGER.debug("Calculating level for the number of posts={}", postPublished);
		String encodedLevelString = dbConfigService.getProperty(Properties.ENCODED_LEVEL.getProperty(), Properties.ENCODED_LEVEL.getDefaultValue());
		String[] boundies = encodedLevelString.split("-");
		LOGGER.debug("The array elements are={}", Arrays.toString(boundies));
		if(boundies.length != 4 ) {
			LOGGER.error("The array length after decoding is not 4");
			return 0; // default
		}
		int firstBoundary = Integer.valueOf(boundies[0]);
		int secondBoundary = Integer.valueOf(boundies[1]);
		int thirdBoundary = Integer.valueOf(boundies[2]);
		int fourthBoundary = Integer.valueOf(boundies[3]);
		
		// null handling for postPublished
		postPublished = postPublished == null ? 0 : postPublished;
		
		if(postPublished >= firstBoundary && postPublished < secondBoundary) {
			return 1;
		} else if(postPublished >= secondBoundary && postPublished < thirdBoundary) {
			return 2;
		} else if(postPublished >= thirdBoundary && postPublished < fourthBoundary) {
			return 3;
		} else if(postPublished >= fourthBoundary) {
			return 4;
		}
		
		LOGGER.error("Failed to decide level even, returning 0");
		return 0;
	}

	@Override
	public ProfileDto getReporterProfileDetails(long userId, long reporterId) {
		Reporter reporter = reporterRepo.findById(reporterId);
		if(reporter == null) {
			LOGGER.error("reporter with id={} not found", reporterId);
			throw new RecordNotFoundException("reporter with id=" + reporterId + " not found" );
		}
		
		boolean isFollowing = followRepo.existsFollowByUserIdAndFollowerAndFollowType(reporterId, userId, 1);
		
		ProfileDto dto = new ProfileDto();
		Long followersCount = reporter.getFollowers();
		dto.setAbout(reporter.getAbout());
		dto.setAlreadyFollowing(isFollowing);
		dto.setFollowersCount(followersCount);
		dto.setFollowingsCount(reporter.getFollowing());
		dto.setName(reporter.getName());
		dto.setPostPublished(reporter.getPostPublished()); 
		dto.setProfileId(reporter.getId());
		dto.setProfilePicUrl(reporter.getProfilePick());
		
		double rating = 0;
		if(followersCount == 0 ) {
			// 0
		} else {
			rating = reporter.getRating() / (double) followersCount;
			if(rating > 5.0) {
				rating = 5.0;
			}
			if(rating < 0.001) {
				rating = 0;
			}
			
		}
		dto.setRating(rating);
		
		dto.setLevel(4);  // always expert
		dto.setBalance("0"); // hardcoded, since not applicable, changed to O from N/A
		dto.setReferralCount("0"); // changed to 0, from N/A
		
		dto.setProfileType("reporter");
		 
		return dto;
	}
	
	@Override
	@Transactional
	public StatusDto followReporterOrUser(long userId, long profileId, boolean isReporter, long rating) {
		if(isReporter) {
			try {
				Reporter followedreporter = reporterRepo.findById(profileId);
				followedreporter.setFollowers(followedreporter.getFollowers() + 1);
				followedreporter.setRating(followedreporter.getRating() + rating);
				reporterRepo.save(followedreporter);
				LOGGER.debug("successfully incremented follower count of followedReporter={}", profileId);
			} catch (Exception ex) {
				LOGGER.error("error while incrementing follower count of followedReporter, exception={}",ex.getMessage());
			}
		} else {
			try {
				User user = userRepo.findByUserId(profileId);
				Long tempCount = user.getFollowers();
				long followCount;
				if(tempCount == null) {
					followCount = 1;
				} else {
					followCount = tempCount + 1;
				}
				user.setFollowers(followCount);
				userRepo.save(user);
				
				UserProfile userProfile = userProfileRepo.findByUserId(userId);
				userProfile.setRating(userProfile.getRating() + rating);
				userProfileRepo.save(userProfile);
				
			} catch (Exception ex) {
				LOGGER.error("Failed to increment follower count of followed user profileId={}, exited with exception={}", profileId, ex.getMessage());
			}
		}
		
		try {
			User followerUser = userRepo.findByUserId(userId);
			Long tempCount = followerUser.getFollowing();
			if(tempCount == null) {
				tempCount = 0l;
			}
			followerUser.setFollowing(tempCount + 1);
			userRepo.save(followerUser);
			LOGGER.debug("successfully incremented following count of followerUser={}", userId);
			
		} catch (Exception e) {
			LOGGER.error("error occured while incrementing following count of followerUser, exception=", e.getMessage());
		}
		
		Follow follow = new Follow();
		follow.setUserId(profileId);
		follow.setFollower(userId);
		follow.setDateTime(new Date());

		if(isReporter) {
			follow.setFollowType(1l);
		} else {
			follow.setFollowType(0l);
		}
		
		boolean isSuccess = false;
		try {
			followRepo.save(follow);
			isSuccess = true; 
		} catch(Exception e) {
			LOGGER.error("Failed to save follow record");
		}
		return new StatusDto(isSuccess ? "success" : "fail");
	}
	
	@Override
	@Transactional
	public StatusDto unfollowReporterOrUser(long userId, long profileId, boolean isReporter) {
		if(!followRepo.existsFollowByUserIdAndFollowerAndFollowType(profileId, userId, isReporter ? 1 : 0)) {
			throw new ConditionViolationException("can't unfollow if not following already");
		}
		if (isReporter) {
			try {
				Reporter followedReporter = reporterRepo.findById(profileId);
				followedReporter.setFollowers(followedReporter.getFollowers() - 1);
				reporterRepo.save(followedReporter);
			} catch (Exception ex) {
				LOGGER.error("error while decrementing follower count of followedReporter={}, exception={}", profileId, ex.getMessage());
			}
		} else {
			try {
				User user = userRepo.findByUserId(profileId);
				Long followCount = user.getFollowers();
				if(followCount == 0) {
					throw new ConditionViolationException("followCount can't be negative");
				}
				user.setFollowers(followCount - 1);
				userRepo.save(user);
			} catch (Exception ex) {
				LOGGER.error("failed to decrement follower count of followedUser={}, exception={}", userId,
						ex.getMessage());
			}
		}
		
		try {
			User followerUser = userRepo.findByUserId(userId);
			Long followingCount = followerUser.getFollowing();
			followerUser.setFollowing(followingCount - 1 );
			userRepo.save(followerUser);
		} catch (Exception ex) {
			LOGGER.error("failed to decrement following count of followerUser id={}, exception={}", userId, ex.getMessage());
		}
		
		boolean isSuccess = false;
		try {
			int nums = followRepo.deleteByUserIdAndFollower(profileId, userId);
			LOGGER.debug("{} records got deleted", nums);
			isSuccess = true;
		} catch(Exception ex) {
			LOGGER.error("failed to delete follow entry , exception={}", ex.getMessage());
		}
		
		return new StatusDto(isSuccess ? "success": "fail");
	}
	
	@Override
	public List<FollowDto> getProfileFollowers(long profileId, boolean isReporter) {
		List<FollowDto> dtoList = new ArrayList<FollowDto>();
		
		if(isReporter) {
			List<UserProfile> reportersFollowers = followRepo.findAllReporterFollowers(profileId, 1);
			LOGGER.debug("reporters followers number = {}", reportersFollowers.size() );
			for(UserProfile follower : reportersFollowers) {
				FollowDto dto = new FollowDto();
				dto.setName(follower.getName());
				dto.setProfileId(follower.getUserId());
				dto.setProfilePicUrl(follower.getProfilePick());
				
				
				dto.setType("user");
				
				dtoList.add(dto);
			}
		} else {
			List<UserProfile> usersFollowers = followRepo.findAllUserFollowers(profileId, 0);
			LOGGER.debug("user followers number = {}", usersFollowers.size() );
			for(UserProfile userProfile : usersFollowers) {
				FollowDto dto = new FollowDto();
				dto.setName(userProfile.getName());
				dto.setProfileId(userProfile.getUserId());
				dto.setProfilePicUrl(userProfile.getProfilePick());
				
				dto.setType("user");
				
				dtoList.add(dto);
			}
		}
		
		LOGGER.debug("total followers number = {}", dtoList.size());
		if(dtoList.size() == 0) {
			LOGGER.error("No followers found");
		}
		return dtoList;
	}
	
	@Override
	public List<FollowDto> getProfileFollowings(long userId) {
		List<FollowDto> dtoList = new ArrayList<FollowDto>();
		List<UserProfile> userFollowings = followRepo.findAllUserFollowing(userId, 0);
		LOGGER.debug("user followings numbers = {}", userFollowings.size());
		for(UserProfile userProfile : userFollowings) {
			FollowDto dto = new FollowDto();
			dto.setName(userProfile.getName());
			dto.setProfileId(userProfile.getUserId());
			dto.setProfilePicUrl(userProfile.getProfilePick());
			dto.setType("user");
			
			dtoList.add(dto);
		}
		
		List<Reporter> reporterFollowings = followRepo.findAllReporterFollowing(userId, 1);
		LOGGER.debug("reporter followings number = {}", reporterFollowings.size());
		for(Reporter reporter : reporterFollowings) {
			FollowDto dto = new FollowDto();
			dto.setName(reporter.getName());
			dto.setProfileId(reporter.getId());
			dto.setProfilePicUrl(reporter.getProfilePick());
			dto.setType("reporter");
			
			dtoList.add(dto);
		}
		LOGGER.debug("final followings numbers = {}", dtoList.size());
		if(dtoList.size() == 0) {
			LOGGER.error("No followings found");
		}
		return dtoList;
	}

	@Override
	public AccountSummaryDto getAccountSummary(long userId, boolean isComplete) {
		List<UserAccountSummary> summaries;
		if(isComplete) {
			summaries = userAccountSummaryRepo.findAllByUserIdOrderByCreatedTimeDesc(userId);
		} else {
			summaries = userAccountSummaryRepo.findTop10ByUserIdOrderByCreatedTimeDesc(userId);
		}
		
		if(summaries == null || summaries.size() == 0) {
			LOGGER.warn("No summaries found for userId={}", userId);
		}
		
		UserAccount userAccount = userAccountRepo.findByUserId(userId);
		AccountSummaryDto dto = profileMapper.getAccountSummaryDto(summaries);
		if(userAccount == null) {
			dto.setTotal(0);
		} else {
			LOGGER.debug("Total amount calculated={}", userAccount.getCurrentBalance());
			dto.setTotal(userAccount.getCurrentBalance());
		}
		return dto;
	}
}
