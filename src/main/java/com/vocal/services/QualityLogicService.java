package com.vocal.services;

public interface QualityLogicService {

	void refresh();
	
	boolean getQulityBasedOnRetention(double retainPercentage, long refCount);

	int getAmountBasedOnRetention(double retainPercentage);
	
}
