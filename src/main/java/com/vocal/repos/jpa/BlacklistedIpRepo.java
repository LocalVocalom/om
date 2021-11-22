package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.BlacklistedIp;

@Repository
public interface BlacklistedIpRepo  extends JpaRepository<BlacklistedIp, Long> {
	
	public BlacklistedIp findByIp(String ip);
	
	boolean existsBlacklistedIpByIp(String ip);
	
}
