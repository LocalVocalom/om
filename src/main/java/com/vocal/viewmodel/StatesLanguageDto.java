package com.vocal.viewmodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatesLanguageDto {

	private List<StateDto> States;

	private List<LanguageDto> Language;

	public StatesLanguageDto(List<StateDto> states, List<LanguageDto> languages) {
		super();
		this.States = states;
		this.Language = languages;
	}
	
	public StatesLanguageDto() {
		super();
	}

	public List<StateDto> getStates() {
		return States;
	}

	@JsonProperty("States")
	public void setStates(List<StateDto> states) {
		this.States = states;
	}

	public List<LanguageDto> getLanguages() {
		return Language;
	}

	@JsonProperty("Language")
	public void setLanguages(List<LanguageDto> languages) {
		this.Language = languages;
	}

}
