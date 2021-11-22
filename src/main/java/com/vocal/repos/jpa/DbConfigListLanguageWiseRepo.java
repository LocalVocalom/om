package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.DbConfigListLanguageWise;

@Repository
public interface DbConfigListLanguageWiseRepo extends JpaRepository<DbConfigListLanguageWise, Integer> {

	
}
