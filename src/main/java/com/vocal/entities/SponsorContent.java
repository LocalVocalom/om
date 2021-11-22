package com.vocal.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class SponsorContent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long newsId;
	
	private int languageId;
	
	private int categoryId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	private boolean enabled;
	
	private int adIndex;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNewsId() {
		return newsId;
	}

	public void setNewsId(long newsId) {
		this.newsId = newsId;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getAdIndex() {
		return adIndex;
	}

	public void setAdIndex(int adIndex) {
		this.adIndex = adIndex;
	}

	@Override
	public String toString() {
		return new StringBuilder("SponsorContent [id=").append(id).append(", newsId=").append(newsId)
				.append(", languageId=").append(languageId).append(", categoryId=").append(categoryId)
				.append(", createdTime=").append(createdTime).append(", updatedTime=").append(updatedTime)
				.append(", startTime=").append(startTime).append(", endTime=").append(endTime).append(", enabled=")
				.append(enabled).append(", adIndex=").append(adIndex).append("]").toString();
	}
}
