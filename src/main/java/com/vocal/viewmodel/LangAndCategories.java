package com.vocal.viewmodel;

import java.util.List;

public class LangAndCategories {

	private int langId;
	
	private String langName;
	
	private String code;
	
	private String color;
	
	private String init;
	
	private List<Integer> defaultCatIds;
	
	private List<NewsCategoryDtoV1> cats;
	
	public LangAndCategories() {
		super();
	}

	public LangAndCategories(int langId, String langName, String code, List<Integer> defaultCatIds, List<NewsCategoryDtoV1> cats) {
		super();
		this.langId = langId;
		this.langName = langName;
		this.code = code;
		this.defaultCatIds = defaultCatIds;
		this.cats = cats;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}
	
	public List<Integer> getDefaultCatIds() {
		return defaultCatIds;
	}

	public void setDefaultCatIds(List<Integer> defaultCatIds) {
		this.defaultCatIds = defaultCatIds;
	}

	public List<NewsCategoryDtoV1> getCats() {
		return cats;
	}

	public void setCats(List<NewsCategoryDtoV1> cats) {
		this.cats = cats;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
