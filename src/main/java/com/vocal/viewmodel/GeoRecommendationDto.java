package com.vocal.viewmodel;

import java.util.List;

public class GeoRecommendationDto {

	public LanguageDto byLocation;

	public List<LanguageDto> allEnabled;

	/*
	public StateDto activeStateByLocation;

	public List<StateDto> allStates;
	*/

	public LanguageDto getByLocation() {
		return byLocation;
	}

	public void setByLocation(LanguageDto byLocation) {
		this.byLocation = byLocation;
	}

	public List<LanguageDto> getAllEnabled() {
		return allEnabled;
	}

	public void setAllEnabled(List<LanguageDto> allEnabled) {
		this.allEnabled = allEnabled;
	}

	/*
	public StateDto getActiveStateByLocation() {
		return activeStateByLocation;
	}

	public void setActiveStateByLocation(StateDto activeStateByLocation) {
		this.activeStateByLocation = activeStateByLocation;
	}

	public List<StateDto> getAllStates() {
		return allStates;
	}

	public void setAllStates(List<StateDto> allStates) {
		this.allStates = allStates;
	}
	*/
}
