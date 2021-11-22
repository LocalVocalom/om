package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.State;

public interface StateRepo extends JpaRepository<State, Integer> {

	State findByStateName(String stateName);
	
	State findByLanguageIdAndStateId(int languageId, long stateId);
	
	List<State> findAllByLanguageId(int languageId);
	
}
