package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.Language;

public interface LanguageRepo extends JpaRepository<Language, Integer> {
	
	//Language findById(long languageId);
	
	List<Language> findAllByIsActive(boolean isActive);
	
}
