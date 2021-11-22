package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.QualityLogic;

@Repository
public interface QualityLogicRepo extends JpaRepository<QualityLogic, Long> {

	
}
