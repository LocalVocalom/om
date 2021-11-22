package com.vocal.services.impl;

import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.QualityLogic;
import com.vocal.repos.jpa.QualityLogicRepo;
import com.vocal.services.QualityLogicService;

@Service
public class QualityLogicServiceImpl implements QualityLogicService {
	
	@Autowired
	private QualityLogicRepo qualityLogicRepo;
	
	private List<QualityLogic> qualityLogicList;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QualityLogicServiceImpl.class);

	@Override
	@PostConstruct
	@Scheduled(fixedDelay = 3600000)
	public void refresh() {
		LOGGER.info("Refreshing QualityLogic");
		List<QualityLogic> qualities = qualityLogicRepo.findAll();
		qualities.sort(new Comparator<QualityLogic>() {
			@Override
			public int compare(QualityLogic o1, QualityLogic o2) {
				double percentStart1 =  (double) o1.getPercentStart(); 
				double percentStart2 =  (double) o2.getPercentStart();
				int diff = (int) ( percentStart1 - percentStart2);
				return  diff;
			}
		});
		if(qualities.size() == 0) {
			LOGGER.error("No QualityLogic found, please define some QualityLogic first");
		}
		LOGGER.info("QualityLogic list after sorting based on percentStart={}", qualities);
		qualityLogicList = qualities;
	}

	@Override
	public boolean getQulityBasedOnRetention(double retainPercentage, long refCount) {
		boolean isEligible = false;
		for(QualityLogic logic : qualityLogicList) {
			double percentStart =  logic.getPercentStart();
			double percentEnd =  logic.getPercentEnd();
			long tempRefCount = refCount + 1;
			if(retainPercentage >= percentStart && retainPercentage <= percentEnd) {
				LOGGER.info("The retainPercentage={} is within percentStart={} and percentEnd={}", retainPercentage, percentStart, percentEnd);
				return tempRefCount % logic.getQuality() == 0;
			}
		}
		return isEligible;
	}
	
	@Override
	public int getAmountBasedOnRetention(double retainPercentage) {
		int amount = 0;
		for(QualityLogic logic : qualityLogicList) {
			double percentStart = logic.getPercentStart();
			double percentEnd = logic.getPercentEnd();
			if(retainPercentage >= percentStart && retainPercentage <= percentEnd) {
				LOGGER.info("The retainPercentage={} is within percentStart={} and percentEnd={}, respective prize amount={}", retainPercentage, percentStart, percentEnd, logic.getAmount());
				return logic.getAmount();
			}
		}
		return amount;
	}
	
}
