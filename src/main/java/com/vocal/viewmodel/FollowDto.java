package com.vocal.viewmodel;

public class FollowDto {

	private String name;
	
	private long profileId;
	
	private String profilePicUrl;
	
	private String type;
	
	//private boolean alreadyFollowing;
	
	public FollowDto() {
		super(); 
	}

	public FollowDto(String name, long profileId, String profilePicUrl, boolean alreadyFollowing) {
		super();
		this.name = name;
		this.profileId = profileId;
		this.profilePicUrl = profilePicUrl;
		//this.alreadyFollowing = alreadyFollowing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	public boolean isAlreadyFollowing() {
//		return alreadyFollowing;
//	}
//
//	public void setAlreadyFollowing(boolean alreadyFollowing) {
//		this.alreadyFollowing = alreadyFollowing;
//	}
	
}
