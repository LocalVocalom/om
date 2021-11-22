package com.vocal.viewmodel;

public class DbConfigDto {

	private String propertyName;
	
	private String propertyValue;
	
	public DbConfigDto() {
		super();
	}

	public DbConfigDto(String propertyName, String propertyValue) {
		super();
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
}
