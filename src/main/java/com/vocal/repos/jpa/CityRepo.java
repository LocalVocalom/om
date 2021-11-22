package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.City;

@Repository("cityRepo")
public interface CityRepo extends JpaRepository<City, Integer>{
	
	List<City> findByStateId(int stateId);
	
	City findByCityId(int cityId);
	
	List<City> findByStateIdAndLanguageId(int stateId,int languageId);
	
	City findByCityName(String cityName);
	
	City findByCityIdAndLanguageId(int cityId, int languageId);
	
	City findByLanguageIdAndId(int languageId, long Id);

}
