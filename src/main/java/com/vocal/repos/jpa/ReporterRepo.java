package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.Reporter;

@Repository
public interface ReporterRepo extends JpaRepository<Reporter, Long>{
	
	Reporter findById(long id);
	
}
