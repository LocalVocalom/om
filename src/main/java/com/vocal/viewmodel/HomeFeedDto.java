package com.vocal.viewmodel;

import java.util.List;
	
public class HomeFeedDto {
	
	private List<BreakingDto> breaking;
	
	private List<Object> personalized;
	
	private List<Object> local;
	
	private List<NewsCategoryFeedDto> subCats;

	public List<BreakingDto> getBreaking() {
		return breaking;
	}

	public void setBreaking(List<BreakingDto> breaking) {
		this.breaking = breaking;
	}

	public List<Object> getPersonalized() {
		return personalized;
	}

	public void setPersonalized(List<Object> personalized) {
		this.personalized = personalized;
	}

	public List<Object> getLocal() {
		return local;
	}

	public void setLocal(List<Object> local) {
		this.local = local;
	}

	public List<NewsCategoryFeedDto> getSubCats() {
		return subCats;
	}

	public void setSubCats(List<NewsCategoryFeedDto> subCats) {
		this.subCats = subCats;
	}
	
}
