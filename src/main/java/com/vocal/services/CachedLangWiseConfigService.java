package com.vocal.services;

import java.util.List;

import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.LanguageDtoV1;
import com.vocal.viewmodel.NewsCategoryDto;
import com.vocal.viewmodel.NewsCategoryDtoV1;
import com.vocal.viewmodel.ReportDto;
import com.vocal.viewmodel.StateDto;

public interface CachedLangWiseConfigService {
	
	/**
	 * Loading all the properties and configurations
	 */
	void load();

	List<LanguageDto> getActiveLanguages();

	List<NewsCategoryDto> getActiveNewsCategories(int languageId);

	List<ReportDto> getReportOptions(int languageId);

	List<StateDto> getStates(int languageId);

	String getPropertyByLanguageId(String propertyName, int languageId);

	String getPropertyByLanguageId(String propertyName, String defaultValue, int languageId);

	List<String> getPropertyListByLanguageId(String propertyName, int languageId);

	List<String> getPropertyListByLanguageId(String propertyName, List<String> defaultProperties, int languageId);
	
	List<LanguageDto> getConfiguredLanguageBasedOnState(String state);

	StateDto getStateDtoByStateName(String state);

	boolean isValiedActiveLanguageId(int languageId);

	List<NewsCategoryDto> getActiveNewsCategoriesWithFallbackToEnglish(int languageId);

	List<Integer> getDefaultCategoryIdsForALanguage(int languageId);

	NewsCategoryDto getCategoryDtosFromCategoryId(int categoryId, int languageId);

	List<NewsCategoryDtoV1> getActiveNewsCategoriesV1(int languageId);

	List<LanguageDtoV1> getActiveLanguagesV1();

	String getStaticCategoryName(int languageId, int categoryId);

}
