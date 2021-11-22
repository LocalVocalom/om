package com.vocal.services;

import com.vocal.viewmodel.StatusDto;

public interface EdrService {

	StatusDto handleEdr(long userId, long newsId, int type, String reportId);
	
}
