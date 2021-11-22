package com.vocal.viewmodel;

import java.util.List;

public class KycDto {
	
	private String popupTitle;
	
	private List<String> popup;
	
	private String kycDone;
	
	private String tnc;
	
	public KycDto() {
		super();
	}

	public String getTnc() {
		return tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
	}

	public KycDto(String popupTitle, List<String> popup, String kycDone) {
		super();
		this.popupTitle = popupTitle;
		this.popup = popup;
		this.kycDone = kycDone;
	}

	public String getPopupTitle() {
		return popupTitle;
	}

	public void setPopupTitle(String popupTitle) {
		this.popupTitle = popupTitle;
	}

	public List<String> getPopup() {
		return popup;
	}

	public void setPopup(List<String> popup) {
		this.popup = popup;
	}

	public String getKycDone() {
		return kycDone;
	}

	public void setKycDone(String kycDone) {
		this.kycDone = kycDone;
	}
}
