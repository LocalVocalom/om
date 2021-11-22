package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UnauthorizedAccess;

@Repository
public interface UnauthorizedAccessRepo extends JpaRepository<UnauthorizedAccess, Long> {

	UnauthorizedAccess findByUserIdAndIp(long userId, String ip);
	
}
