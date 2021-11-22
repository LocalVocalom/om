package com.vocal.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsCategoryDtoV1 {
	
	private String Action;
	
	private String ActionUrl;
	
	private int categoryId;
	
	private String categoryName;
	
	private String icon;
	
	private String color;
	
	public NewsCategoryDtoV1() {
		super();
	}

	public NewsCategoryDtoV1(String action, String actionUrl, int categoryId, String categoryName, String icon,
			String color) {
		super();
		this.Action = action;
		this.ActionUrl = actionUrl;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.icon = icon;
		this.color = color;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
