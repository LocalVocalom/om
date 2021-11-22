package com.vocal.services.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vocal.entities.City;
import com.vocal.entities.Language;
import com.vocal.entities.State;
import com.vocal.exceptions.RecordNotFoundException;
import com.vocal.mapper.Mapper;
import com.vocal.repos.jpa.CityRepo;
import com.vocal.repos.jpa.LanguageRepo;
import com.vocal.repos.jpa.StateRepo;
import com.vocal.repos.jpa.UserLocationRepo;
import com.vocal.services.DbConfigService;
import com.vocal.services.GeoService;
import com.vocal.utils.Properties;
import com.vocal.viewmodel.CityDto;
import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.StateDto;

@Service
public class GeoServiceImpl implements GeoService {

	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private CityRepo cityRepo;
	
	@Autowired
	private LanguageRepo languageRepo;
	
	@Autowired
	private UserLocationRepo userLocationRepo;
	
	@Autowired
	private DbConfigService dbConfigService;
	
	@Autowired
	private Mapper mapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoServiceImpl.class);
	
	private int defaultLanguageId = 2;
	
	@Override
	public List<StateDto> getStates() {
		List<State> states = stateRepo.findAll();
		// TODO : apache collection utils
		if(states == null || states.size() == 0) {
			LOGGER.error("No states available");
			throw new RecordNotFoundException("No states available");
		}
		return mapper.stateListToStateDtoList(states);
	}
	
	@Override
	public List<StateDto> getStates(int langId) {
		List<State> states = stateRepo.findAllByLanguageId(langId);
		// TODO : apache collection utils
		if(states == null || states.size() == 0) {
			LOGGER.warn("No states available for languageid={} falling back to default language", langId);
			states = stateRepo.findAllByLanguageId(defaultLanguageId);
		}
		return mapper.stateListToStateDtoList(states);
	}
	
	
	@Override
	public List<CityDto> getCitiesByStateId(int stateId) {
		List<City> cities = cityRepo.findByStateId(stateId);
		// TODO : apache collection utils
		if(cities == null || cities.size() == 0) {
			LOGGER.error("No cities available for the stateId={}", stateId);
			throw new RecordNotFoundException("No cities found for the stateid=" +  stateId);
		}
		return mapper.cityListToCityDtoList(cities);
	}
	
	@Override
	public List<CityDto> getCitiesByStateIdAndlanguageid(int stateId,int languageId) {
		List<City> cities = cityRepo.findByStateIdAndLanguageId(stateId,languageId);
		// TODO : apache collection utils
		if(cities == null || cities.size() == 0) {
			LOGGER.error("No cities available for the stateId={}", stateId);
			throw new RecordNotFoundException("No cities found for the stateid=" +  stateId);
		}
		return mapper.cityListToCityDtoList(cities);
	}

	@Override
	public List<LanguageDto> getLanguages() {
		//List<Language> langs = languageRepo.findAll();
		List<Language> langs = languageRepo.findAllByIsActive(true);
		// TODO : apache collection utils
		if(langs == null || langs.size() == 0) {
			LOGGER.error("No languages available");
			throw new RecordNotFoundException("No languages available");
		}
		return mapper.languageListToLanguageDtoList(langs);
	}
	
	@Override
	public String getCountOfUsersOfSpecificCity(int days) {
		if(days > 10 || days < 0) {
			return "NO_ALLOWED_DAYS_RANGE";
		}
		String exceptionalCities = dbConfigService.getProperty(Properties.EXCEPTION_CITIES.getProperty(), Properties.EXCEPTION_CITIES.getDefaultValue());
		String[] split = exceptionalCities.split(",");
		LOGGER.info("exceptionCities={}, splittedByComma={}, toList={}", exceptionalCities, split, Arrays.asList(split));
		List<Object[]> userLocationsOfGhz = userLocationRepo.countUserLocationByDateTimeAndCity(days, Arrays.asList(split));
		StringBuilder sb = new StringBuilder();
		for(Object[] objects : userLocationsOfGhz) {
			sb.append("<tr><td>").append(objects[0]).append("</td><td>").append(objects[1]).append("</td></tr>");
		}
		return sb.toString();
	}
}
