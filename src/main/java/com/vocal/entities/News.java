package com.vocal.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NEWS")
public class News implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id")
	private long newsId;

	@Column(name = "catagory_id")
	private int catagoryId;

	@Column(name = "language_id")
	private int languageId;

	@Column(name = "date_time")
	private Date dateTime;

	@Column(length = 25, name = "status")
	private String status;

	@Column(length = 4, name = "city_id", nullable = true)
	private Integer cityId;

	@Column(length = 2, name = "state_id")
	private int stateId;

	@Column(length = 10)
	private int views;

	@Column(length = 10)
	private int likes;

	@Column(length = 150, name = "news_location")
	private String newsLocation;

	@Column(length = 50, unique = true, name = "unique_id")
	private String uniqueId;
	
	@Column(length = 20)
	private String latitude;
	
	@Column(length = 20)
	private String longitude;
	
	public News() {
		super();
	}

	public News(int catagoryId, int languageId, Date dateTime, String status, Integer cityId, int stateId,
			int views, int likes, String newsLocation, String uniqueId, String latitude, String longitude) {
		super();
		this.catagoryId = catagoryId;
		this.languageId = languageId;
		this.dateTime = dateTime;
		this.status = status;
		this.cityId = cityId;
		this.stateId = stateId;
		this.views = views;
		this.likes = likes;
		this.newsLocation = newsLocation;
		this.uniqueId = uniqueId;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public long getNewsId() {
		return newsId;
	}

	public void setNewsId(long newsId) {
		this.newsId = newsId;
	}
	
	public long getCategoryId() {
		return catagoryId;
	}

	public void setCategoryId(int categoryId) {
		this.catagoryId = categoryId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public long getLanguageId() {
		return languageId;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public String getNewsLocation() {
		return newsLocation;
	}

	public void setNewsLocation(String newsLocation) {
		this.newsLocation = newsLocation;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}
