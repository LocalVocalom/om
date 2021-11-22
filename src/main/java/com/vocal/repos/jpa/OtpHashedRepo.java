package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.OtpHashed;

@Repository
public interface OtpHashedRepo extends JpaRepository<OtpHashed, String> {

	OtpHashed findByMobileNum(String mobileNum);
	
	

}
