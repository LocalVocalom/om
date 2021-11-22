package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vocal.entities.UserSource;

public interface UserSourceRepo extends JpaRepository<UserSource, Long> {
	
	public UserSource findByUserId(long userId);
	
	public List<UserSource> findAllByUtmSourceAndIp(String utmSource, String ip);
	
	long countByUtmSourceAndIp(String utmSource, String ip);
	
	@Query("SELECT count(us) from  UserSource us, UserDevice ud WHERE us.userId = ud.userId AND us.utmSource = :utmSource")
	long getCountReferredUsersUtmSource(@Param("utmSource") String utmSource);
	
	@Query("SELECT count(us) from UserSource us, UserDevice ud WHERE us.userId = ud.userId AND us.utmSource = :utmSource AND ud.active = :active")
	long getCountReferredUsersUtmSourceActive(@Param("utmSource") String utmSource,  @Param("active") boolean active);
	
}
