package com.vocal.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.vocal.entities.UserAccountSummary;
import com.vocal.viewmodel.AccountSummaryDto;
import com.vocal.viewmodel.SummaryDto;

@Component
public class ProfileMapper {
	
	public AccountSummaryDto getAccountSummaryDto(List<UserAccountSummary> summaries) {
		AccountSummaryDto accountSummaryDto = new AccountSummaryDto();
		List<SummaryDto> summaryDtoList = new ArrayList<>();
		for (UserAccountSummary summary : summaries) {
			summaryDtoList.add(new SummaryDto(summary.getCreatedTime(), summary.getRemarks(), summary.getAmount()));
		}
		
		accountSummaryDto.setSummaries(summaryDtoList);
		
		return accountSummaryDto;
	}
	
//	public List<FollowDto> getFollowDtoListFromFollowers(long userId, List<Follow> followers) {
//		List<FollowDto> dtoList = new ArrayList<>(followers.size());
//		for(Follow f: followers) {
//			long followerId = f.getFollower();
//			UserProfile profile = userProfileRepo.findByUserId(followerId);
//			
//			LOGGER.debug("userId={}, followingId={}", userId, followerId);
//			
//			//boolean alreadyFollowing = followRepo.existsFollowByUserIdAndFollower(followerId, userId);
//
//			FollowDto dto = new FollowDto();
//			dto.setProfileId(followerId);		
//			dto.setName(profile.getName());
//			dto.setProfilePicUrl(profile.getProfilePick());
//			//dto.setAlreadyFollowing(alreadyFollowing);
//			
//			dtoList.add(dto);
//		}
//		return dtoList;
//	}
	
	
//	public List<FollowDto> getFollowDtoListFromFollowings(long userId, List<Follow> followings) {
//		List<FollowDto> dtoList = new ArrayList<>(followings.size());
//		for(Follow f: followings) {
//			long followingId = f.getUserId();
//			UserProfile profile = userProfileRepo.findByUserId(followingId);
//			
//			LOGGER.debug("userId={}, followingId={}", userId, followingId);
//			
//			//boolean alreadyFollowing = followRepo.existsFollowByUserIdAndFollower(followingId, userId);
//
//			FollowDto dto = new FollowDto();
//			dto.setProfileId(followingId);		
//			dto.setName(profile.getName());
//			dto.setProfilePicUrl(profile.getProfilePick());
//			//dto.setAlreadyFollowing(alreadyFollowing);
//			
//			dtoList.add(dto);
//		}
//		return dtoList;
//	}
	
}
