package com.vocal.viewmodel;

import java.util.List;

public class SetAddressV1Dto {

	private String status;
	
	private List<LangAndCategories> langCats;
	
	private int defaultLangId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<LangAndCategories> getLangCats() {
		return langCats;
	}

	public void setLangCats(List<LangAndCategories> langCats) {
		this.langCats = langCats;
	}

	public int getDefaultLangId() {
		return defaultLangId;
	}

	public void setDefaultLangId(int defaultLangId) {
		this.defaultLangId = defaultLangId;
	}
}
