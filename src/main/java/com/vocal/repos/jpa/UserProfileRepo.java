package com.vocal.repos.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.UserProfile;

public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
	
	UserProfile findByUserId(Long userId);
	
}
