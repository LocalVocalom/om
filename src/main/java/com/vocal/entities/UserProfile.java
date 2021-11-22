package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_profile")
public class UserProfile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum LoginMedium{
		NONE, GOOGLE, FACEBOOK
	}

	@Id
	@Column(name = "userId")
	private long userId;
	
	@Column(name = "language_id")
	private Integer languageId;

	private String dob;

	@Column(name = "state_id")
	private Integer stateId;

	@Column(name = "city_id")
	private Integer cityId;

	@Column(name = "block_id")
	private Integer blockId;

	@Column(name = "gram_sabha_id")
	private Integer gramSabhaId;

	@Column(length = 500, name = "profile_pick")
	private String profilePick;

	private String name;

	@Column(name = "login_type")
	private String loginType;

	@Column(name = "android_version")
	private String androidVersion;

	private String city; // cityId is already there,so a bit doubt

	private String token;

	@Column(name = "astro_sign")
	private String astroSign;
	
	private String about;
	
	private long rating;
	
	@Column(name = "pers_cat")
	private String personalizedCategories;
	
	public UserProfile() {
		super();
	}

	public UserProfile(long userId, int languageId, String profilePick, String name, String loginType, String androidVersion, String token) {
		super();
		this.userId = userId;
		this.languageId = languageId;
		this.profilePick = profilePick;
		this.name = name;
		this.loginType = loginType;
		this.androidVersion = androidVersion;
		this.token = token;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getBlockId() {
		return blockId;
	}

	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	public Integer getGramSabhaId() {
		return gramSabhaId;
	}

	public void setGramSabhaId(Integer gramSabhaId) {
		this.gramSabhaId = gramSabhaId;
	}

	public String getProfilePick() {
		return profilePick;
	}

	public void setProfilePick(String profilePick) {
		this.profilePick = profilePick;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAstroSign() {
		return astroSign;
	}

	public void setAstroSign(String astroSign) {
		this.astroSign = astroSign;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public long getRating() {
		return rating;
	}

	public void setRating(long rating) {
		this.rating = rating;
	}

	public String getPersonalizedCategories() {
		return personalizedCategories;
	}

	public void setPersonalizedCategories(String personalizedCategories) {
		this.personalizedCategories = personalizedCategories;
	}
}