package com.vocal.utils;

/**
 * A class that wraps utm parameters
 *
 */
public class UtmParameters {
	
	private String utmSource;
	
	private String utmTerm;
	
	private String utmCampaign;
	
	private String utmMedium;
	
	public UtmParameters() {
		super();
	}
	
	public UtmParameters(String utmSource, String utmTerm, String utmCampaign, String utmMedium) {
		super();
		this.utmSource = utmSource;
		this.utmTerm = utmTerm;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
	}

	public String getUtmSource() {
		return utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public String getUtmTerm() {
		return utmTerm;
	}

	public void setUtmTerm(String utmTerm) {
		this.utmTerm = utmTerm;
	}

	public String getUtmCampaign() {
		return utmCampaign;
	}

	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}

	public String getUtmMedium() {
		return utmMedium;
	}

	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}
}
