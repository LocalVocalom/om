package com.vocal.services;

import java.util.Iterator;

import com.vocal.viewmodel.NewsDtoInnerAll;

public interface CachedSponsorService {

	NewsDtoInnerAll getSponsoredContentByLanguageIdAndCategoryIdAndIndex(int languageId, int categoryId, int adsIndex);
	
	Iterator<Integer> getAdsIndexesForLanguageIdAndCategoryId(int languageId, int categoryid);

	void reloadOrRefresh();
	
}
