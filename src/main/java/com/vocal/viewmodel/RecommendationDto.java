package com.vocal.viewmodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RecommendationDto {
	
	private String headline;
	
	private String image_url;
	
	private String type;
	
	private String news_url;
	
	private long newsid;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date datetime;
	
	public RecommendationDto() {
		super();
	}
	
	
	public String getNews_url() {
		return news_url;
	}


	public void setNews_url(String news_url) {
		this.news_url = news_url;
	}


	public RecommendationDto(String headline, String imageUrl, long newsId, String typeDeterminer) {
		super();
		this.headline = headline;
		this.image_url = imageUrl;
		this.newsid = newsId;
		this.type = (typeDeterminer.length() >= 10) ? "Video" : "News";
	}

	public RecommendationDto(String headline, String image_url, long newsid, String typeDeterminer, Date datetime, String news_url) {
		super();
		this.headline = headline;
		this.image_url = image_url;
		this.newsid = newsid;
		this.type = (typeDeterminer.length() >= 10) ? "Video" : "News";
		this.datetime = datetime;
		this.news_url = news_url;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getNewsid() {
		return newsid;
	}

	public void setNewsid(long newsid) {
		this.newsid = newsid;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)  
			return false;  
		if (obj == this)
			return true;  
		return this.getNewsid() == ((RecommendationDto) obj).getNewsid(); 
	}
}
