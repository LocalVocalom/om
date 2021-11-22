package com.vocal.services.impl;

import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.ReferralLogic;
import com.vocal.repos.jpa.ReferralLogicRepo;
import com.vocal.services.ReferralLogicService;

@Service
public class ReferralLogicServiceImpl implements ReferralLogicService {

	@Autowired
	private ReferralLogicRepo referralLogicRepo;
	
	private List<ReferralLogic> logicList; // = new ArrayList<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(ReferralLogicServiceImpl.class);

	@Override
	@PostConstruct
	@Scheduled(fixedDelay = 3600000)
	public void refresh() {
		LOGGER.info("Refreshing ReferralLogic");
		List<ReferralLogic> tempLogicList = referralLogicRepo.findAll();
		tempLogicList.sort(new Comparator<ReferralLogic>() {
			@Override
			public int compare(ReferralLogic o1, ReferralLogic o2) {
				int counter1 = (int) o1.getStartCounter(); 
				int counter2 = (int) o2.getStartCounter();
				int diff = counter1 - counter2;
				return diff;
			}
		});
		if(tempLogicList.size() == 0) {
			LOGGER.error("No ReferralLogic found, please define some ReferralLogic first");
		}
		LOGGER.debug("Logic list after sorting based on startCounter={}", logicList);
		logicList = tempLogicList;
	}
	
	@Override
	public boolean randomSelectionForReferral(long refCount, boolean isWithinTimeBound) {
		boolean isEligible = false;
		for(ReferralLogic logic : logicList) {
			long startCounter = logic.getStartCounter();
			long endCounter = logic.getEndCounter();
			long tempRefCount = refCount + 1;
			if(tempRefCount >= startCounter && tempRefCount <= endCounter) {
				LOGGER.debug("The refCount={}th is within startCounter={} and endCounter={}", tempRefCount, startCounter, endCounter);
				if(isWithinTimeBound) {
					LOGGER.info("within bounded time");
					return   tempRefCount % logic.getTimeTrue()  == 0;
				}	else {
					LOGGER.info("not within time");
					return   tempRefCount % logic.getTimeFalse()  == 0;
				}
			}
		}
		return isEligible;
	}
	
	
}
