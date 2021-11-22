package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.DbConfig;

@Repository
public interface DbConfigRepo extends JpaRepository<DbConfig, Integer> {

	DbConfig findByPropertyName(String propertyname);
	
	// Added later
	DbConfig findByPropertyNameAndLanguageId(String propertyName, int languageId);
	
	// Added later
	List<DbConfig> findAllByPropertyName(String propertyName);
	
	// Added later
	List<DbConfig> findAllByPropertyNameAndLanguageId(String propertyName, int languageId);
	
}
