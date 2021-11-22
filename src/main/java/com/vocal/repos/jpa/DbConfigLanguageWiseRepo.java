package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.DbConfigLanguageWise;

@Repository
public interface DbConfigLanguageWiseRepo extends JpaRepository<DbConfigLanguageWise, Integer> {
	
}
