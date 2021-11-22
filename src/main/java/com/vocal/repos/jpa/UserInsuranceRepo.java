package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserInsurance;

@Repository
public interface UserInsuranceRepo extends JpaRepository<UserInsurance, Long>{
	
	boolean existsUserInsuranceByUserId(long userId);
	
	UserInsurance findByUserId(long userId);
}
