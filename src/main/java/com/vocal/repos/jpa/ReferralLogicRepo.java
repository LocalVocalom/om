package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.ReferralLogic;

@Repository
public interface ReferralLogicRepo extends JpaRepository<ReferralLogic, Integer>  {
	
}
