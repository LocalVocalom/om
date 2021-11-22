package com.vocal.viewmodel;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsuranceDto {

	private String profilePic;
	
	private String nomineeName;
	
	private String insurrenceStatus;
	
	private String email;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date dob;
		
	private String name;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date insureneStartDate;
	
	private String termsLink;
	
	private List<String> termsPoints;
		
	private String policyDetailUrl;
	
	private String policyNumber;
	
	private String helplineNumber;
	
	private String helplineEmail;
	
	private long userid;
	
	private String gender;
	
	private String sumInsuredAmount;
	
	public String getSumInsuredAmount() {
		return sumInsuredAmount;
	}

	public void setSumInsuredAmount(String sumInsuredAmount) {
		this.sumInsuredAmount = sumInsuredAmount;
	}

	public InsuranceDto() {
		super();
	}

	public InsuranceDto(String profilePic, String nomineeName, String insurrenceStatus, String email, Date dob,
			String name, Date insureneStartDate) {
		super();
		this.profilePic = profilePic;
		this.nomineeName = nomineeName;
		this.insurrenceStatus = insurrenceStatus;
		this.email = email;
		this.dob = dob;
		this.name = name;
		this.insureneStartDate = insureneStartDate;
	}

	public String getProfilePic() {
		return profilePic;
	}

	@JsonProperty(value = "profile_pic")
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getNomineeName() {
		return nomineeName;
	}

	@JsonProperty(value = "nominee_name")
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	public String getInsurrenceStatus() {
		return insurrenceStatus;
	}

	@JsonProperty(value = "insurence_status")
	public void setInsurrenceStatus(String insurrenceStatus) {
		this.insurrenceStatus = insurrenceStatus;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getInsureneStartDate() {
		return insureneStartDate;
	}

	@JsonProperty(value = "insurene_start_date")
	public void setInsureneStartDate(Date insureneStartDate) {
		this.insureneStartDate = insureneStartDate;
	}

	public String getTermsLink() {
		return termsLink;
	}

	@JsonProperty(value = "terms_link")
	public void setTermsLink(String termsLink) {
		this.termsLink = termsLink;
	}

	public List<String> getTermsPoints() {
		return termsPoints;
	}

	@JsonProperty(value = "terms_points")
	public void setTermsPoints(List<String> termsPoints) {
		this.termsPoints = termsPoints;
	}

	public String getPolicyDetailUrl() {
		return policyDetailUrl;
	}

	public void setPolicyDetailUrl(String policyDetailUrl) {
		this.policyDetailUrl = policyDetailUrl;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getHelplineNumber() {
		return helplineNumber;
	}

	public void setHelplineNumber(String helplineNumber) {
		this.helplineNumber = helplineNumber;
	}

	public String getHelplineEmail() {
		return helplineEmail;
	}

	public void setHelplineEmail(String helplineEmail) {
		this.helplineEmail = helplineEmail;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "InsuranceDto [profilePic=" + profilePic + ", nomineeName=" + nomineeName + ", insurrenceStatus="
				+ insurrenceStatus + ", email=" + email + ", dob=" + dob + ", name=" + name + ", insureneStartDate="
				+ insureneStartDate + ", termsLink=" + termsLink + ", termsPoints=" + termsPoints + ", policyDetailUrl="
				+ policyDetailUrl + ", policyNumber=" + policyNumber + ", helplineNumber=" + helplineNumber
				+ ", helplineEmail=" + helplineEmail + ", userid=" + userid + ", gender=" + gender + "]";
	}
}	