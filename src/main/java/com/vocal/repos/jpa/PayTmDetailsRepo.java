package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.PayTmDetails;

@Repository
public interface PayTmDetailsRepo extends JpaRepository<PayTmDetails, Long> {

	
	PayTmDetails findByUserId(long userId);
	
}
