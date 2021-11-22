package com.vocal.viewmodel;

import java.util.List;

public class NewsCategoryFeedDto {

	private long categoryId;
	
	private String categoryName;
	
	private List<Object> catNews;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Object> getCatNews() {
		return catNews;
	}

	public void setCatNews(List<Object> catNews) {
		this.catNews = catNews;
	}
}
