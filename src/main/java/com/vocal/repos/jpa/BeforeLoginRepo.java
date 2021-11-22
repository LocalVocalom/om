package com.vocal.repos.jpa;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vocal.entities.BeforeLogin;

@Repository
@Transactional
public interface BeforeLoginRepo extends JpaRepository<BeforeLogin, String> {
	
	BeforeLogin findByAndroidId(String androidId);
	
	BeforeLogin findByDeviceId(String deviceId);
	
	long deleteByDeviceId(String deviceId);
	
//	@Modifying
//	@Query("delete from BeforeLogin bf where bf.deviceId=:deviceId")
//	BeforeLogin deleteByDeviceIdQuery(String deviceId);
	
}
