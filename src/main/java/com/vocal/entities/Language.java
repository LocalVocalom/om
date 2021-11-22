package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "language")
public class Language implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "language_id")
	private int languageId;

	@Column(length = 500, name = "language_name")
	private String languageName;
	
	
	@Column(length = 5)
	private String code;
	
	@Column(name = "isActive")
	private boolean isActive;
	
	@Column(name = "multi_cat")
	private String defaultCategories;
	
	private String color;

	public String getLanguageCode() {
		return code;
	}

	public void setLanguageCode(String languageCode) {
		this.code = languageCode;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDefaultCategories() {
		return defaultCategories;
	}

	public void setDefaultCategories(String defaultCategories) {
		this.defaultCategories = defaultCategories;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
