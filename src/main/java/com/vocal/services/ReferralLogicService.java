package com.vocal.services;


public interface ReferralLogicService {

	void refresh();

	boolean randomSelectionForReferral(long refCount, boolean isWithinTimeBound);
	
}
