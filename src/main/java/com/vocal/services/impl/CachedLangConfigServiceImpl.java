package com.vocal.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.DbConfigLanguageWise;
import com.vocal.entities.DbConfigListLanguageWise;
import com.vocal.entities.Language;
import com.vocal.entities.NewsCategory;
import com.vocal.entities.ReportReasons;
import com.vocal.entities.State;
import com.vocal.entities.StateLangMapping;
import com.vocal.entities.StaticCategories;
import com.vocal.mapper.Mapper;
import com.vocal.repos.jpa.DbConfigLanguageWiseRepo;
import com.vocal.repos.jpa.DbConfigListLanguageWiseRepo;
import com.vocal.repos.jpa.LanguageRepo;
import com.vocal.repos.jpa.NewsCategoryRepo;
import com.vocal.repos.jpa.ReportReasonsRepo;
import com.vocal.repos.jpa.StateLangMappingRepo;
import com.vocal.repos.jpa.StateRepo;
import com.vocal.repos.jpa.StaticCategoriesRepo;
import com.vocal.services.CachedLangWiseConfigService;
import com.vocal.utils.Constants;
import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.LanguageDtoV1;
import com.vocal.viewmodel.NewsCategoryDto;
import com.vocal.viewmodel.NewsCategoryDtoV1;
import com.vocal.viewmodel.ReportDto;
import com.vocal.viewmodel.StateDto;


/**
 * This service class deals with the configurable properties which can configured based on language.
 * @author asheesh
 */
@Service
public class CachedLangConfigServiceImpl implements CachedLangWiseConfigService {
	
	@Autowired
	private DbConfigLanguageWiseRepo dbConfigLanguageWiseRepo;
	
	@Autowired
	private DbConfigListLanguageWiseRepo dbConfigListLanguageWiseRepo;
	
	@Autowired
	private NewsCategoryRepo newsCategoryRepo;
	
	@Autowired
	private LanguageRepo languageRepo;
	
	@Autowired
	private ReportReasonsRepo reportReasonsRepo;
	
	@Autowired
	private StateRepo stateRepo;
	
	@Autowired
	private StateLangMappingRepo stateLangMappingRepo;
	
	@Autowired
	private StaticCategoriesRepo staticCategoriesRepo;
	
	@Autowired
	private Mapper mapper;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CachedLangConfigServiceImpl.class);
	
	private Map<Integer, List<NewsCategoryDto>> languageWiseNewsCategories;
	
	private Map<Integer, List<NewsCategoryDtoV1>> languageWiseNewsCategoriesV1;
	
	// to map languageId wise, so that when there is a query for a default language with an id, it can be queried intantly
	private Map<Integer, NewsCategoryDto> languageIdWiseNewsCategories;
	
	private Map<Integer, List<ReportDto>> reportOptionsLanguageWise;
	
	private List<LanguageDto> availableActiveLanguages;
	
	// it also has an additional field of color
	private List<LanguageDtoV1> availableActiveLanguagesDtoV1;
	
	private Map<Integer, List<StateDto>> availableStatesLanguageWise;
	
	private Map<String, Map<Integer, String>> dbConfigLanguageWiseMapped;
	
	private Map<String, Map<Integer, List<String>>> dbConfigListLanguageWiseMapped;
	
	// to put stateWise langugage list
	private Map<String, List<LanguageDto>> stateWiseMappedLang;
	
	private Map<String, StateDto> statesMappedByStateName;
	
	private Map<Integer, List<Integer>> defaultCategoreisLangWise;
	
	// to put the name of static category names with key as languageId+""+categoryId
	private Map<String, String> homeAndHeadlineNamesLanguageIdPlusCategoryAsKey;
	
	
	/**
	 * Loading all the properties and configurations
	 */
	@PostConstruct
	@Scheduled(fixedDelay = 3600000)
	@Override
	public void load() {
		LOGGER.info("loading language configs");
		try {
			loadActiveNewsCategories();
			loadActiveLanguages();
			loadReportOptions();
			loadStates();
			loadPropertyByLanguage();
			loadProperyList();
			loadStateWiseMappedLanguage();
			loadStaticCategoryNames();
			LOGGER.info("loaded language configs");
		} catch(Exception ex) {
			LOGGER.error("failed to load configuration, exception={}", ex.getMessage());
		}
	}
	
	private void loadActiveNewsCategories() {
		List<NewsCategory> allActiveCategories = newsCategoryRepo.findAllByStatusOrderByPriorityAsc(1);
		
		// active language dtos languageId wise.
		Map<Integer, List<NewsCategoryDto>> tempLangWiseNewsCategories = new HashMap<Integer, List<NewsCategoryDto>>();	
		
		// to map languageId wise, so that when there is a query for a default language with an id, it can be queried intantly
		Map<Integer, NewsCategoryDto> tempLanguageIdWiseNewsCategories = new HashMap<>();
		
		// active language new dtos languageId wise, it contains additional color and icon
		Map<Integer, List<NewsCategoryDtoV1>> tempLanguageWiseNewsCategoriesV1 = new HashMap<>();
		
		for(NewsCategory newsCategory: allActiveCategories) {
			// for old dto v1
			List<NewsCategoryDto> dtoList = tempLangWiseNewsCategories.get(newsCategory.getLanguageId());
			if(dtoList == null || dtoList.size() == 0) {
				dtoList = new ArrayList<>();
				dtoList.add(mapper.newsCategoryToNewCategoryDto(newsCategory));
				tempLangWiseNewsCategories.put(newsCategory.getLanguageId(), dtoList);
				
			} else {
				dtoList.add(mapper.newsCategoryToNewCategoryDto(newsCategory));
				tempLangWiseNewsCategories.put(newsCategory.getLanguageId(), dtoList);
			}
			
			// same for new dto v1
			List<NewsCategoryDtoV1> dtoListV1 = tempLanguageWiseNewsCategoriesV1.get(newsCategory.getLanguageId());
			if(dtoListV1 == null || dtoListV1.size() == 0) {
				dtoListV1 = new ArrayList<>();
				dtoListV1.add(mapper.newsCategoryToNewCategoryDtoV1(newsCategory));
				tempLanguageWiseNewsCategoriesV1.put(newsCategory.getLanguageId(), dtoListV1);
				
			} else {
				dtoListV1.add(mapper.newsCategoryToNewCategoryDtoV1(newsCategory));
				tempLanguageWiseNewsCategoriesV1.put(newsCategory.getLanguageId(), dtoListV1);
			}
			
			tempLanguageIdWiseNewsCategories.put(newsCategory.getCategoryId(), mapper.newsCategoryToNewCategoryDto(newsCategory));
		}
		languageWiseNewsCategories = tempLangWiseNewsCategories;
		languageIdWiseNewsCategories = tempLanguageIdWiseNewsCategories;
		languageWiseNewsCategoriesV1 = tempLanguageWiseNewsCategoriesV1;
	}
	
	@Override
	public List<NewsCategoryDto> getActiveNewsCategoriesWithFallbackToEnglish(int languageId) {
		List<NewsCategoryDto> newsCategoryDtos = languageWiseNewsCategories.get(languageId);
		if(newsCategoryDtos == null || newsCategoryDtos.size() == 0) {
			LOGGER.error("no news categories available for the languageId={}, falling back to english", languageId);
			newsCategoryDtos = languageWiseNewsCategories.get(Constants.FALLBACK_LANGUAGE_ID);
		}
		return newsCategoryDtos;
	}
	
	@Override
	public List<NewsCategoryDto> getActiveNewsCategories(int languageId) {
		return languageWiseNewsCategories.get(languageId);
	}
	
	@Override
	public List<NewsCategoryDtoV1> getActiveNewsCategoriesV1(int languageId) {
		return languageWiseNewsCategoriesV1.get(languageId);
	}
	
	private void loadActiveLanguages() {
		
		List<Language> activeLanguages = languageRepo.findAllByIsActive(true);
		List<LanguageDto> tempLanguageDtoList = new ArrayList<>();
		
		// it also has an additional field of color
		List<LanguageDtoV1> tempAvailableActiveLanguagesDtoV1 = new ArrayList<>();
		
		Map<Integer, List<Integer>> tempDefaultCategoreisLangWise = new HashMap<>();
		for(Language language : activeLanguages) {
			// for old dto
			tempLanguageDtoList.add(mapper.languageToLanguageDto(language));
			// for new dto
			tempAvailableActiveLanguagesDtoV1.add(mapper.languageToLanguageDtoV1(language));
			String defCats = language.getDefaultCategories();
			
			// load the default category ids of a language
			List<Integer> defaultCategories = new ArrayList<>();
			String[] split = defCats.trim().split(",");
			for(String ele : split) {
				defaultCategories.add(Integer.parseInt(ele.trim()));
			}
			if(defaultCategories.size() != 0) {
				tempDefaultCategoreisLangWise.put(language.getLanguageId(), defaultCategories);
			}
		}
		availableActiveLanguages = tempLanguageDtoList;
		defaultCategoreisLangWise = tempDefaultCategoreisLangWise;
		availableActiveLanguagesDtoV1 = tempAvailableActiveLanguagesDtoV1;
	}

	@Override
	public List<LanguageDto> getActiveLanguages() {
		return availableActiveLanguages;
	}
	
	@Override
	public List<LanguageDtoV1> getActiveLanguagesV1() {
		return availableActiveLanguagesDtoV1;
	}

	private void loadReportOptions() {
		List<ReportReasons> reportReasons = reportReasonsRepo.findAll();
		Map<Integer, List<ReportDto>> tempReportOptionsLanguageWise = new HashMap<Integer, List<ReportDto>>();
		for(ReportReasons reason : reportReasons) {
			if(!tempReportOptionsLanguageWise.containsKey(reason.getLanguageId())) {
				List<ReportDto> dtos = new ArrayList<ReportDto>(6);
				dtos.add(mapper.reportReasonToReportDto(reason));
				tempReportOptionsLanguageWise.put(reason.getLanguageId(), dtos);
			} else {
				tempReportOptionsLanguageWise.get(reason.getLanguageId()).add(mapper.reportReasonToReportDto(reason));
			}
		}
		reportOptionsLanguageWise = tempReportOptionsLanguageWise;
	}
	
	@Override
	public List<ReportDto> getReportOptions(int languageId) {
		List<ReportDto> reportReasons = reportOptionsLanguageWise.get(languageId);
		if(reportReasons == null || reportReasons.size() == 0) {
			LOGGER.warn("report reasons for languageId = {} doesn't exist, falling back to english", languageId);
			reportReasons = reportOptionsLanguageWise.get(Constants.FALLBACK_LANGUAGE_ID);
		}
		return reportReasons;
	}
	
	private void loadStates() {
		List<State> availableStates = stateRepo.findAll();
		Map<Integer, List<StateDto>> tempAvailableStatesLanguageWise = new HashMap<Integer, List<StateDto>>();
		Map<String, StateDto> tempStatesMappedByStateName = new HashMap<>();
		for(State state: availableStates) {
			List<StateDto> stateLists = tempAvailableStatesLanguageWise.get(state.getLanguageId());
			if(stateLists == null) {
				stateLists = new ArrayList<>(1);
				stateLists.add(mapper.stateToStateDto(state));
				tempAvailableStatesLanguageWise.put(state.getLanguageId(), stateLists);
			} else {
				stateLists.add(mapper.stateToStateDto(state));
			}
			tempStatesMappedByStateName.put(state.getStateName(), mapper.stateToStateDto(state));
		}
		availableStatesLanguageWise = tempAvailableStatesLanguageWise;
		statesMappedByStateName = tempStatesMappedByStateName;
		LOGGER.info("states loaded...");
	}
	
	@Override
	public List<StateDto> getStates(int languageId) {
		List<StateDto> states = availableStatesLanguageWise.get(languageId);
		if(states == null || states.size() == 0) {
			LOGGER.warn("no states available for languageId={}, falling back to english", languageId);
			states = availableStatesLanguageWise.get(Constants.FALLBACK_LANGUAGE_ID);
		}
		return states;
	}
	
	private void loadPropertyByLanguage() {
		List<DbConfigLanguageWise> allLanguageWiseProperties = dbConfigLanguageWiseRepo.findAll();
		Map<String, Map<Integer, String>> tempLangwisePropertyMap = new HashMap<>();
		LOGGER.info("loading langWiseDbConfig properties repo");
		for(DbConfigLanguageWise config : allLanguageWiseProperties) {
			Map<Integer, String> mappedProperties = tempLangwisePropertyMap.get(config.getPropertyName());
			if(mappedProperties == null) {
				mappedProperties = new HashMap<>(2);
			}
			mappedProperties.put(config.getLanguageId(), config.getPropertyValue());
		}
		dbConfigLanguageWiseMapped = tempLangwisePropertyMap;
		LOGGER.info("successfully loaded langWiseDbConfig properties:{}", dbConfigLanguageWiseMapped.toString());
	}
	
	/**
	 * Returns the configured property for queried languageId, if not available then
	 * queries for english, if again fails to get for english then returns null.
	 */
	public String getPropertyByLanguageId(String propertyName, int languageId) {
		Map<Integer, String> mappedProperties = dbConfigLanguageWiseMapped.get(propertyName);
		String property = null;
		if(mappedProperties == null) {
			LOGGER.error("The propertyName={} is not mapped", propertyName);
		} else {
			property = mappedProperties.get(languageId);
			if(property == null) {
				LOGGER.warn("property not mapped for languageId={}, falling back to english", languageId);
				property = mappedProperties.get(Constants.FALLBACK_LANGUAGE_ID);
			}
		}
		return property;
	}
	
	/**
	 * Returns the passed default value iff the property is not found for passed languageId and english language.
	 */
	public String getPropertyByLanguageId(String propertyName, String defaultValue, int languageId) {
		String property = getPropertyByLanguageId(propertyName, languageId);
		return property == null ? defaultValue : property;
	}
	
	private void loadProperyList() {
		List<DbConfigListLanguageWise> languageWiseLists = dbConfigListLanguageWiseRepo.findAll();
		Map<String, Map<Integer, List<String>> > tempDbConfigListLanguageWiseMapped = new HashMap<>();
		for(DbConfigListLanguageWise config : languageWiseLists) {
			Map<Integer, List<String>> mapped = tempDbConfigListLanguageWiseMapped.get(config.getPropertyName());
			if(mapped == null) {
				mapped = new HashMap<>();
				List<String> mappedList = new ArrayList<>();
				mappedList.add(config.getPropertyValue());
				mapped.put(config.getLanguageId(), mappedList);
				tempDbConfigListLanguageWiseMapped.put(config.getPropertyName(), mapped);
			} else {
				List<String> mappedList = mapped.get(config.getLanguageId());
				
				if(mappedList == null) {
					mappedList = new ArrayList<>();
				}
				mappedList.add(config.getPropertyValue());
			}
		}
		dbConfigListLanguageWiseMapped = tempDbConfigListLanguageWiseMapped;
		LOGGER.info("successfully loaded dbConfigListLanguageWise={}", dbConfigListLanguageWiseMapped);
	}
	
	/**
	 * Queries property list for passed languageId, fallbacks to english language,
	 * even if not found for english then returns null
	 */
	@Override
	public List<String> getPropertyListByLanguageId(String propertyName, int languageId) {
		Map<Integer, List<String>> mapped = dbConfigListLanguageWiseMapped.get(propertyName);
		List<String> mappedList = null;
		if(mapped == null) {
			LOGGER.error("no mapping found for propertyName={}", propertyName);
		} else {
			mappedList = mapped.get(languageId);
			if(mappedList == null) {
				LOGGER.warn("propertyName={} not available for languageId={}, falling back to english", propertyName, languageId);
				mappedList = mapped.get(Constants.FALLBACK_LANGUAGE_ID);
			}
		}
		return mappedList;
	}
	
	
	/**
	 * Queries and returns language property list for queried language, if not
	 * found(even for english) then returns the passed default properties
	 */
	@Override
	public List<String> getPropertyListByLanguageId(String propertyName, List<String> defaultProperties, int languageId) {
		List<String> mappedList = getPropertyListByLanguageId(propertyName, languageId);
		return mappedList == null ? defaultProperties : mappedList;
	}
	
	private void loadStateWiseMappedLanguage()  {
		Map<String, List<LanguageDto>> tempStateWiseMappedLang = new HashMap<>();
		List<StateLangMapping> allStateLanguageMapping = stateLangMappingRepo.findAllByActive(true);
		for(StateLangMapping mapping : allStateLanguageMapping) {
			String commaSeparatedLanguageMappings = mapping.getLanguages();
			String[] languageIds = commaSeparatedLanguageMappings.trim().split(",");
			List<LanguageDto> dtoList = new ArrayList<>();
			for(String languageId : languageIds) {
				try {
					int parsedId = Integer.parseInt(languageId.trim());
					Optional<Language> findById = languageRepo.findById(parsedId);
					if(findById.isPresent()) {
						Language lang = findById.get();
						LanguageDto dto = new LanguageDto();
						dto.setCode(lang.getLanguageCode());
						dto.setId(lang.getLanguageId());
						dto.setLanguage(lang.getLanguageName());
						dtoList.add(dto);
					}
				} catch (Exception ex) {
					LOGGER.error("exception while parsing and querying for language with id={}, exception={}", languageId, ex.getMessage());
				}
			}
			
			if(dtoList.size() != 0) {
				tempStateWiseMappedLang.put(mapping.getState(), dtoList);
			}
		}
		stateWiseMappedLang= tempStateWiseMappedLang;
	}
	
	@Override
	public List<LanguageDto> getConfiguredLanguageBasedOnState(String state) {
		List<LanguageDto> configuredLangsForState = stateWiseMappedLang.get(state);
		if(configuredLangsForState == null || configuredLangsForState.size() == 0) {
			LOGGER.warn("no configured languages found for state={}", state);
		}
		return configuredLangsForState;
	}
	
	@Override
	public StateDto getStateDtoByStateName(String stateName) {
		StateDto dto = statesMappedByStateName.get(stateName);
		return dto;
	}
	
	@Override
	public boolean isValiedActiveLanguageId(int languageId) {
		for(LanguageDto dto : availableActiveLanguages) {
			if(dto.getId() == languageId) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public NewsCategoryDto getCategoryDtosFromCategoryId(int categoryId, int languageId) {
		NewsCategoryDto newsCategoryDto = languageIdWiseNewsCategories.get(categoryId);
		if(newsCategoryDto == null) {
			LOGGER.error("failed to get news category for passed news category={}", categoryId);
		}
		return newsCategoryDto;
	}
	
	@Override
	public List<Integer> getDefaultCategoryIdsForALanguage(int languageId) {
		List<Integer> defaultCategores = defaultCategoreisLangWise.get(languageId);
		if(defaultCategores == null || defaultCategores.size() == 0) {
			LOGGER.error("No default categores defined for languageId={}", languageId);
		}
		return defaultCategores;
	}
	
	public void loadStaticCategoryNames() {
		List<StaticCategories> staticCategories = staticCategoriesRepo.findAll();
		homeAndHeadlineNamesLanguageIdPlusCategoryAsKey = new HashMap<>();
		for(StaticCategories statCat : staticCategories) {
			homeAndHeadlineNamesLanguageIdPlusCategoryAsKey.put(statCat.getLanguageId()+"" + statCat.getCategoryId(), statCat.getCategoryName());
		}
	}
	
	@Override
	public String getStaticCategoryName(int languageId, int categoryId) {
		return homeAndHeadlineNamesLanguageIdPlusCategoryAsKey.get(languageId+"" + categoryId);
	}
}
