package com.vocal.viewmodel;

import java.util.List;

public class NewsDtoUser {
	
	private List<NewsDtoInnerUser> news;

	public List<NewsDtoInnerUser> getNews() {
		return news;
	}

	public void setNews(List<NewsDtoInnerUser> newsForUser) {
		this.news = newsForUser;
	}

}
