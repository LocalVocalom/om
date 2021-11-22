package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.RatePopupEdr;

@Repository
public interface RatePopupEdrRepo extends JpaRepository<RatePopupEdr, Long> {

	RatePopupEdr findByUserId(long userId);
	
}
