package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.DeviceToken;

public interface DeviceTokenRepo extends JpaRepository<DeviceToken, String> {
	
	
}
