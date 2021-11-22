package com.vocal.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.vocal.entities.DbConfig;
import com.vocal.exceptions.ParseException;
import com.vocal.repos.jpa.DbConfigRepo;
import com.vocal.services.DbConfigService;

@Service
public class DbConfigServiceImpl implements DbConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbConfigServiceImpl.class);

    @Autowired
    private DbConfigRepo dbConfigRepo;
    
    private Map<String,String> propertyMap;// = new ConcurrentHashMap<>(); // lazily
    
    private volatile boolean isLoaded=false;
    
    @Override
    @PostConstruct
    @Scheduled(fixedDelay=3600000)
    public  void refresh() {
        LOGGER.info("Refreshing DbConfig");
        List<DbConfig> propertyList = dbConfigRepo.findAll();
        Map<String, String> tempPropertymap = new HashMap<>();
        for (DbConfig dbConfig : propertyList) {
        	tempPropertymap.put(dbConfig.getPropertyName(), dbConfig.getPropertyValue());
        }
        propertyMap = new ConcurrentHashMap<>(tempPropertymap);
        LOGGER.info("db config refreshed={}", propertyMap);
    }
    
    @Override
    public String getProperty(String propertyName, String defaultValue) {
        String value = getProperty(propertyName);
        return value == null ? defaultValue: value;
    }
    
    @Override
    public String getProperty(String propertyName) {
        checkIfLoadOrRefresh();
        return propertyMap.get(propertyName);
    }
    
    @Override
    public String getPropertyWithEmptyCheck(String propertyName, String defaultValue) {
        String value = getProperty(propertyName);
        return value == null || value.isEmpty() ? defaultValue: value;
    }

    @Override
    public Boolean getBooleanProperty(String propertyName, Boolean defaultValue) {
        Boolean value = getBooleanProperty(propertyName);
        return value == null ? defaultValue: value;
    }
    
    @Override
    public Boolean getBooleanProperty(String propertyName) {
        checkIfLoadOrRefresh();
        String propertyValue = propertyMap.get(propertyName);
        if(propertyValue==null)
            return null;
		try {
			boolean value = Boolean.parseBoolean(propertyValue);
			return value;
		} catch (Exception e) {
			throw new ParseException("Unable to parse boolean property:" + propertyName, e);
		}
    }

    @Override
    public Integer getIntProperty(String propertyName, Integer defaultValue) {
        Integer value = getIntProperty(propertyName);
        return value == null ? defaultValue: value;
    }
    
    @Override
    public Integer getIntProperty(String propertyName) {
        checkIfLoadOrRefresh();
        String propertyValue = propertyMap.get(propertyName);
        if(propertyValue==null)
            return null;
        try{
            int value = Integer.parseInt(propertyValue);
            return value;
        }catch(NumberFormatException e) {
			throw new ParseException("Unable to parse integer property:" + propertyName, e);
        }
    }
    
    @Override
    public Long getLongProperty(String propertyName, Long defaultValue) {
        Long value = getLongProperty(propertyName);
        return value == null ? defaultValue: value;
    }

    @Override
    public Long getLongProperty(String propertyName) {
        checkIfLoadOrRefresh();
        String propertyValue = propertyMap.get(propertyName);
        if(propertyValue==null)
            return null;
        try{
            long value = Long.parseLong(propertyValue);
            return value;
        }catch(NumberFormatException e){
			throw new ParseException("Unable to parse long property:" + propertyName, e);
        }
    }

    @Override
    public Double getDoubleProperty(String propertyName, Double defaultValue) {
        Double value = getDoubleProperty(propertyName);
        return value == null ? defaultValue: value;
    }

    @Override
    public Double getDoubleProperty(String propertyName) {
        checkIfLoadOrRefresh();
        String propertyValue = propertyMap.get(propertyName);
        if(propertyValue==null)
            return null;
        try{
            double value = Double.parseDouble(propertyValue);
            return value;
        }catch(NumberFormatException e){
			throw new ParseException("Unable to parse double property:" + propertyName, e);
        }
    }
    
    
    @Override
    public Float getFloatProperty(String propertyName, Float defaultValue) {
        Float value = getFloatProperty(propertyName);
        return value == null ? defaultValue: value;
    }

    @Override
    public Float getFloatProperty(String propertyName) {
        checkIfLoadOrRefresh();
        String propertyValue = propertyMap.get(propertyName);
        if(propertyValue==null)
            return null;
        try{
            float value = Float.parseFloat(propertyValue);
            return value;
        } catch(NumberFormatException e){
			throw new ParseException("Unable to parse float property:" + propertyName, e);
        }
    }

    private void checkIfLoadOrRefresh() {
        if(isLoaded)
            return;

        synchronized (propertyMap){
            if (!isLoaded) {
                List<DbConfig> propertyList = dbConfigRepo.findAll();
                Map<String, String> tempPropertymap = new HashMap<>();
                for (DbConfig dbConfig : propertyList) {
                	tempPropertymap.put(dbConfig.getPropertyName(), dbConfig.getPropertyValue());
                }
                propertyMap = new ConcurrentHashMap<>(tempPropertymap);
                LOGGER.info("db config loaded: {}",propertyMap);
                isLoaded=true;
            }
        }
    }


	 @Override
	 public void update(DbConfig config) {
	  dbConfigRepo.saveAndFlush(config);
	 }
	
	
	 @Override
	 public List<String> getPropertyValues(String propertyName, List<String> asList) {
	  DbConfig config=dbConfigRepo.findByPropertyName(propertyName);
	  if(config == null)
	   return asList;
	  return Arrays.asList(config.getPropertyValue());
	 }
	 
	// Added later 
	public String getPropertyByLanguageId(String propertyName,  int languageId) {
		DbConfig config = dbConfigRepo.findByPropertyNameAndLanguageId(propertyName, languageId);
		if(config == null) {
			return null;
		}
		return config.getPropertyValue();
	}
	
	// Added later
	@Override
	public String getPropertyByLanguageId(String propertyName, String defaultValue, int languageId) {
		DbConfig config = dbConfigRepo.findByPropertyNameAndLanguageId(propertyName, languageId);
		if(config == null) {
			LOGGER.debug("fetched single property by languageId for propertyName={} is null", propertyName);
			return defaultValue;
		}
		LOGGER.debug("successfully fetched single property by languageId for propertyName={}", propertyName);
		return config.getPropertyValue();
	}
	
	// Added later
	@Override
	public List<String> getPropertyList(String propertyName, List<String> defaultProperties) {
		List<DbConfig> listProperties = dbConfigRepo.findAllByPropertyName(propertyName);
		if(listProperties == null || listProperties.size() == 0) {
			LOGGER.debug("fetched property list for propertyName={} is null", propertyName);
			return defaultProperties;
		}
		List<String> properties = new ArrayList<>(listProperties.size());
		LOGGER.debug("successfully fetched property list for propertyName={}", propertyName);
		for(DbConfig db: listProperties) {
			properties.add(db.getPropertyValue());
		}
		return properties;
	}

	// Added later
	@Override
	public List<String> getPropertyListByLanguageId(String propertyName, List<String> defaultProperties, int languageId) {
		List<DbConfig> listProperties = dbConfigRepo.findAllByPropertyNameAndLanguageId(propertyName, languageId);
		if(listProperties == null || listProperties.size() == 0) {
			LOGGER.debug("fetched property list by language for propertyName={} is null", propertyName);
			return defaultProperties;
		}
		List<String> properties = new ArrayList<>(listProperties.size());
		LOGGER.debug("successfully fetched propertiy list by languageId for propertyName={}", propertyName);
		for(DbConfig db: listProperties) {
			properties.add(db.getPropertyValue());
		}
		return properties;
	}
	
}