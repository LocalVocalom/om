package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.StateLangMapping;

public interface StateLangMappingRepo extends JpaRepository<StateLangMapping, Integer> {
	
	List<StateLangMapping> findAllByActive(boolean active);
	
}
