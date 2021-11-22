package com.vocal.viewmodel;

public class ProfileDto {
	
	private Long profileId;
	
	private String name;
	
	private Long followersCount;
	
	private Long followingsCount;
	
	private boolean alreadyFollowing;
	
	private double rating;
	
	private Long postPublished;
	
	private String about;
	
	private String balance;
	
	private String profilePicUrl;
	
	private String profileType;
	
	private int level;
	
	private String referralCount;
	
	public ProfileDto() {
		super();
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(Long followersCount) {
		this.followersCount = followersCount;
	}

	public Long getFollowingsCount() {
		return followingsCount;
	}

	public void setFollowingsCount(Long followingsCount) {
		this.followingsCount = followingsCount;
	}

	public boolean isAlreadyFollowing() {
		return alreadyFollowing;
	}

	public void setAlreadyFollowing(boolean alreadyFollowing) {
		this.alreadyFollowing = alreadyFollowing;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Long getPostPublished() {
		return postPublished;
	}

	public void setPostPublished(Long postPublished) {
		this.postPublished = postPublished;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public String getProfileType() {
		return profileType;
	}

	public void setProfileType(String profileType) {
		this.profileType = profileType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getReferralCount() {
		return referralCount;
	}

	public void setReferralCount(String referralCount) {
		this.referralCount = referralCount;
	}
}
