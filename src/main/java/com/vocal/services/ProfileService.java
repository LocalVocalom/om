package com.vocal.services;

import java.util.List;

import com.vocal.viewmodel.AccountSummaryDto;
import com.vocal.viewmodel.FollowDto;
import com.vocal.viewmodel.ProfileDto;
import com.vocal.viewmodel.StatusDto;

public interface ProfileService {
	
	ProfileDto getUserProfileDetails(long userId, long profileId);
	
	ProfileDto getReporterProfileDetails(long userId, long reporterId);

	StatusDto followReporterOrUser(long userId, long reporterId, boolean isReporter, long rating);

	StatusDto unfollowReporterOrUser(long userId, long reporterId, boolean isReporter);
	
	List<FollowDto> getProfileFollowers(long profileId, boolean isReporter);
	
	List<FollowDto> getProfileFollowings(long userId);

	AccountSummaryDto getAccountSummary(long userId, boolean isComplete);
	
	/// to be discontinued later
	
//	List<FollowDto> getFollowers(long userId, long profileId);
//	
//	List<FollowDto> getFollowings(long userId, long profileId);
//	
//	StatusDto follow(long userId, long profileId);
//	
//	StatusDto unfollow(long userId, long profileId);

}
