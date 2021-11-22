package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserQuality;

@Repository
public interface UserQualityRepo extends JpaRepository<UserQuality, Long> {
	
	UserQuality findByUserId(long userId);
	
}
