package com.vocal.repos.jpa;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserDevice;

@Repository
public interface UserDeviceRepo extends JpaRepository<UserDevice, Long> {

	UserDevice findByUserId(long userId);
	
	UserDevice findByDeviceId(String deviceId);
	
	boolean existsUserDeviceByDeviceId(String deviceId);
	
	long countByDeviceId(String deviceId);
	
	@Query(value = "SELECT ud.device_token, ud.userId FROM user_device ud, user_source us WHERE ud.active = 1 AND us.userId = ud.userId  AND us.utmSource = :utmSourceUserId", nativeQuery = true)
	List<Object[]> getDeviceTokensOfUsersWhoHaveBeenReferredByUtmSourceId(@Param("utmSourceUserId") String utmSourceUserId);
	
	@Query(value = "SELECT ud.device_token, ud.userId, up.language_id FROM user_device ud, user_source us, user_profile up WHERE ud.active = 1 AND us.userId = ud.userId AND ud.userId = up.userId AND us.utmSource = :utmSourceUserId", nativeQuery = true)
	List<Object[]> getDeviceTokensOfUsersWhoHaveBeenReferredByUtmSourceIdWithLanguageId(@Param("utmSourceUserId") String utmSourceUserId);
	
	long countByDeviceToken(String deviceToken);
	
	boolean existsUserDeviceByDeviceToken(String deviceToken);
	
}
