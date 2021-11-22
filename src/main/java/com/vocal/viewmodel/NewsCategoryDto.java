package com.vocal.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsCategoryDto {
	
	private String Action;
	
	private String ActionUrl;
	
	private int categoryId;
	
	private String categoryName;
	
	public NewsCategoryDto() {
		super();
	}
	
	public NewsCategoryDto(int categoryId, String categoryName, String action, String actionUrl) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.Action = action;
		this.ActionUrl = actionUrl;
	}

	public String getAction() {
		return Action;
	}

	@JsonProperty("Action")
	public void setAction(String action) {
		Action = action;
	}

	public String getActionUrl() {
		return ActionUrl;
	}

	@JsonProperty("ActionUrl")
	public void setActionUrl(String actionUrl) {
		ActionUrl = actionUrl;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
