package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.CityMapping;

@Repository
public interface CityMappingRepo extends JpaRepository<CityMapping, Long>{
	
	CityMapping findByAlternateNamesContaining(String alternateNames);

}
