package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NEWS_DETAILS")
public class NewsDetails  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "news_id")
	private long newsId;
	
	@Column(length = 500, name = "news_headline")
	private String newsHeadline;
	
	@Column(length = 5000, name = "news_discription_text")
	private String newsDiscriptionText;
	
	@Column(length = 500, name = "news_discription_audio_url")
	private String newsDiscriptionAudioUrl;
	
	@Column(length = 250, name = "news_image_url")
	private String newsImageUrl;
	
	@Column(length = 2500, name = "news_video_url")
	private String newsVideoUrl;
	
	@Column(length = 250, name = "news_creator")
	private String newsCreator;
	
	@Column(length = 3)
	private int dislikes;
	
	@Column(length = 5)
	private int share;
	
	@Column(length = 5)
	private int comment;
	
	@Column(length = 5)
	private int flage;
	
	@Column(length = 500, name = "news_url")
	private String newsUrl;
	
	private Long userid;
	
	@Column(length = 10, name = "openAction")
	private String openAction;

	private boolean aggregator;
	
	private boolean isReporter;
	
	private long createrId;
	
	public NewsDetails() {
		super();
	}

	public NewsDetails(long newsId, String newsHeadline, String newsDiscriptionText, String newsDiscriptionAudioUrl,
			String newsImageUrl, String newsVideoUrl, String newsCreator, int dislikes, int share, int comment,
			int flage, long userid) {
		super();
		this.newsId = newsId;
		this.newsHeadline = newsHeadline;
		this.newsDiscriptionText = newsDiscriptionText;
		this.newsDiscriptionAudioUrl = newsDiscriptionAudioUrl;
		this.newsImageUrl = newsImageUrl;
		this.newsVideoUrl = newsVideoUrl;
		this.newsCreator = newsCreator;
		this.dislikes = dislikes;
		this.share = share;
		this.comment = comment;
		this.flage = flage;
		this.userid = userid;
	}
	
	public long getNewsId() {
		return newsId;
	}

	public void setNewsId(long newsId) {
		this.newsId = newsId;
	}

	public String getNewsHeadline() {
		return newsHeadline;
	}

	public void setNewsHeadline(String newsHeadline) {
		this.newsHeadline = newsHeadline;
	}

	public String getNewsDiscriptionText() {
		return newsDiscriptionText;
	}

	public void setNewsDiscriptionText(String newsDiscriptionText) {
		this.newsDiscriptionText = newsDiscriptionText;
	}

	public String getNewsDiscriptionAudioUrl() {
		return newsDiscriptionAudioUrl;
	}

	public void setNewsDiscriptionAudioUrl(String newsDiscriptionAudioUrl) {
		this.newsDiscriptionAudioUrl = newsDiscriptionAudioUrl;
	}

	public String getNewsImageUrl() {
		return newsImageUrl;
	}

	public void setNewsImageUrl(String newsImageUrl) {
		this.newsImageUrl = newsImageUrl;
	}

	public String getNewsVideoUrl() {
		return newsVideoUrl;
	}

	public void setNewsVideoUrl(String newsVideoUrl) {
		this.newsVideoUrl = newsVideoUrl;
	}

	public String getNewsCreator() {
		return newsCreator;
	}

	public void setNewsCreator(String newsCreator) {
		this.newsCreator = newsCreator;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int getFlage() {
		return flage;
	}

	public void setFlage(int flage) {
		this.flage = flage;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getOpenAction() {
		return openAction;
	}

	public void setOpenAction(String openAction) {
		this.openAction = openAction;
	}

	public boolean isAggregator() {
		return aggregator;
	}

	public void setAggregator(boolean aggregator) {
		this.aggregator = aggregator;
	}

	public boolean isReporter() {
		return isReporter;
	}

	public void setReporter(boolean isReporter) {
		this.isReporter = isReporter;
	}

	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
}
