package com.vocal.viewmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppLaunchDto {
	
	private String share_text;

	private List<NewsCategoryDto> catagory;
		
	private int min;
	
	private String InviteUrl;

	private int max;
	
	private CreateNewsPopupDto createNewsPopup;
	
	private List<ReportDto> report;
	
	private String InviteText;

	private String reportText;
	
	private String recommendationUrl;
		
	private KycDto kyc;
	
	private boolean adsEnabled;
	
	private AppPopupDto app_popup;
	
	private List<LevelMessage> reporterLevelMessage;
	
	private String currentBalance;
	
	private String referralAmount;
	
	private String referralText;
	
	private String earnMoreUrl;
	
	private String redeemUrl;
	
	private boolean otpVerified;
	
	private boolean profileOk;
	
	private String redeemThreshold;
	
	private String mobile;
	
	private Ticker ticker;
	
	private String userState;
	
	private String newsUploadAllowed;
	
	private String uploadNotAllowedMessage;
	
	public AppLaunchDto(String share_text, List<NewsCategoryDto> catagory, int min, String inviteUrl, int max,
			CreateNewsPopupDto createNewsPopup, List<ReportDto> report, String inviteText, String reportText, String recommendationUrl) {
		super();
		this.share_text = share_text;
		this.catagory = catagory;
		this.min = min;
		InviteUrl = inviteUrl;
		this.max = max;
		this.createNewsPopup = createNewsPopup;
		this.report = report;
		InviteText = inviteText;
		this.reportText = reportText;
		this.recommendationUrl = recommendationUrl;
	}
	
	public AppLaunchDto() {
		super();
	}

	public String getShare_text() {
		return share_text;
	}

	public void setShare_text(String share_text) {
		this.share_text = share_text;
	}

	public List<NewsCategoryDto> getCatagory() {
		return catagory;
	}

	public void setCatagory(List<NewsCategoryDto> catagory) {
		this.catagory = catagory;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	
	public String getInviteUrl() {
		return InviteUrl;
	}

	@JsonProperty("InviteUrl")
	public void setInviteUrl(String inviteUrl) {
		this.InviteUrl = inviteUrl;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public CreateNewsPopupDto getCreateNewsPopup() {
		return createNewsPopup;
	}

	public void setCreateNewsPopup(CreateNewsPopupDto createNewsPopup) {
		this.createNewsPopup = createNewsPopup;
	}

	public List<ReportDto> getReport() {
		return report;
	}

	public void setReport(List<ReportDto> report) {
		this.report = report;
	}

	public String getInviteText() {
		return InviteText;
	}

	@JsonProperty("InviteText")
	public void setInviteText(String inviteText) {
		this.InviteText = inviteText;
	}

	public String getReportText() {
		return reportText;
	}

	public void setReportText(String reportText) {
		this.reportText = reportText;
	}

	public String getRecommendationUrl() {
		return recommendationUrl;
	}

	public void setRecommendationUrl(String recommendationUrl) {
		this.recommendationUrl = recommendationUrl;
	}

	public KycDto getKyc() {
		return kyc;
	}

	public void setKyc(KycDto kyc) {
		this.kyc = kyc;
	}

	public boolean isAdsEnabled() {
		return adsEnabled;
	}

	public void setAdsEnabled(boolean adsEnabled) {
		this.adsEnabled = adsEnabled;
	}

	@JsonProperty("AppPopup")
	public AppPopupDto getApp_popup() {
		return app_popup;
	}

	@JsonProperty("AppPopup")
	public void setApp_popup(AppPopupDto app_popup) {
		this.app_popup = app_popup;
	}

	public List<LevelMessage> getReporterLevelMessage() {
		return reporterLevelMessage;
	}

	public void setReporterLevelMessage(List<LevelMessage> reporterLevelMessage) {
		this.reporterLevelMessage = reporterLevelMessage;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getReferralAmount() {
		return referralAmount;
	}

	public void setReferralAmount(String referralAmount) {
		this.referralAmount = referralAmount;
	}

	public String getReferralText() {
		return referralText;
	}

	public void setReferralText(String referralText) {
		this.referralText = referralText;
	}

	public String getEarnMoreUrl() {
		return earnMoreUrl;
	}

	public void setEarnMoreUrl(String earnMoreUrl) {
		this.earnMoreUrl = earnMoreUrl;
	}

	public String getRedeemUrl() {
		return redeemUrl;
	}

	public void setRedeemUrl(String redeemUrl) {
		this.redeemUrl = redeemUrl;
	}

	public boolean isOtpVerified() {
		return otpVerified;
	}

	public void setOtpVerified(boolean otpVerified) {
		this.otpVerified = otpVerified;
	}

	public boolean isProfileOk() {
		return profileOk;
	}

	public void setProfileOk(boolean profileOk) {
		this.profileOk = profileOk;
	}

	public String getRedeemThreshold() {
		return redeemThreshold;
	}

	public void setRedeemThreshold(String redeemThreshold) {
		this.redeemThreshold = redeemThreshold;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Ticker getTicker() {
		return ticker;
	}

	public void setTicker(Ticker ticker) {
		this.ticker = ticker;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getNewsUploadAllowed() {
		return newsUploadAllowed;
	}

	public void setNewsUploadAllowed(String newsUploadAllowed) {
		this.newsUploadAllowed = newsUploadAllowed;
	}

	public String getUploadNotAllowedMessage() {
		return uploadNotAllowedMessage;
	}

	public void setUploadNotAllowedMessage(String uploadNotAllowedMessage) {
		this.uploadNotAllowedMessage = uploadNotAllowedMessage;
	}
}
