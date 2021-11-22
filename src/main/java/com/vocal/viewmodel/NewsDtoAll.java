package com.vocal.viewmodel;

import java.util.ArrayList;
import java.util.List;

public class NewsDtoAll {
	
	//private List<NewsDtoInnerAll> news;
	
	private List<Object> news;

	public List<Object> getNews() {
		return news;
	}

	public void setNews(List<Object> newsForUser) {
		this.news = newsForUser;
	}
	
	public void addNewsItems(List<Object> newsToAdd) {
		if(news == null) {
			this.news = new ArrayList<>();
		}
		news.addAll(newsToAdd);
	}

}
