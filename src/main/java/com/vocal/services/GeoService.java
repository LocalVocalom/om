package com.vocal.services;

import java.util.List;

import com.vocal.viewmodel.CityDto;
import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.StateDto;

public interface GeoService {

	List<StateDto> getStates();

	List<CityDto> getCitiesByStateId(int stateId);
	
	List<LanguageDto> getLanguages();

	//List<StateDto> getStatesByLanguage(int languageId);

	List<StateDto> getStates(int langId);

	String getCountOfUsersOfSpecificCity(int days);

	List<CityDto> getCitiesByStateIdAndlanguageid(int stateId, int languageId);
}
