package com.vocal.services;

import java.util.List;

import com.vocal.entities.DbConfig;

public interface DbConfigService {
    
	String getProperty(String propertyName);
	
    String getProperty(String propertyName, String defaultValue);

    Boolean getBooleanProperty(String propertyName);

    Boolean getBooleanProperty(String propertyName, Boolean defaultValue);

    Double getDoubleProperty(String propertyName);
    
    Double getDoubleProperty(String propertyName, Double defaultValue);

    Integer getIntProperty(String propertyName);

    Integer getIntProperty(String propertyName, Integer defaultValue);

    Long getLongProperty(String propertyName);

    Long getLongProperty(String propertyName, Long defaultValue);
    
    Float getFloatProperty(String propertyName);
    
    Float getFloatProperty(String propertyName, Float defaultValue);
    
    public String getPropertyWithEmptyCheck(String propertyName, String defaultValue);
   
    public void update(DbConfig config);
    
    public void refresh();
    
    List<String> getPropertyValues(String string, List<String> asList);

    // Added later for multi-language handling and list of properties handling
	String getPropertyByLanguageId(String propertyName, String defaultValue, int languageId);
	
	// Added later for multi-language handling and list of properties handling
	String getPropertyByLanguageId(String propertyName,  int languageId);
	
    // Added later for multi-language handling and list of properties handling
	List<String> getPropertyList(String propertyName, List<String> defafultProperties);

    // Added later for multi-language handling and list of properties handling
	List<String> getPropertyListByLanguageId(String propertyName, List<String> defaultProperties, int languageId);
    
}