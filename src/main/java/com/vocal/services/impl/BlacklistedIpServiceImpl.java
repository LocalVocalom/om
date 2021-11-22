package com.vocal.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.BlacklistedIp;
import com.vocal.repos.jpa.BlacklistedIpRepo;
import com.vocal.services.BlacklistedIpService;

@Service
public class BlacklistedIpServiceImpl implements BlacklistedIpService {
	
	@Autowired
    private BlacklistedIpRepo blacklistedIpRepo;

	private Map<String, Integer> propertyMap;
		
	private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistedIpServiceImpl.class);
	
	@Override
	@PostConstruct
	@Scheduled(fixedDelay=3600000)
	public void refresh() {
		LOGGER.info("Refreshing Blacked IP Config");
		Map<String, Integer> tempPropertyMap = new HashMap<>();
        List<BlacklistedIp> blackedIps = blacklistedIpRepo.findAll();
        for (BlacklistedIp ip : blackedIps) {
        	tempPropertyMap.put(ip.getIp(), ip.getMarkedCount());
        }
        propertyMap = tempPropertyMap;
        LOGGER.info("Blacked IP Config refreshed={}", propertyMap);
	}
	
	
	@Override
    public boolean isBlackedIp(String ip) {
    	Integer markedCount = propertyMap.get(ip);
    	if(markedCount == null) {
    		return false;
    	} else {
    		LOGGER.warn("blacklisted ip found in property ip={}", ip);
    		return true;
    	}
    }
	
}
