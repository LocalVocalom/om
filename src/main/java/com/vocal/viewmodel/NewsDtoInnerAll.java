package com.vocal.viewmodel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsDtoInnerAll {

	private long newsid;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date datetime;

	private int views;

	private int likes;

	private String news_location;

	private String headline;

	private String discription;

	private String audiourl;

	private String image_url;

	private String video_url;

	private String reporter_name;

	private int dislikes_count;

	private int share_count;

	private int comment_count;

	private int flag_count;

	private String detailed_news_url;
	
	private String type;
	
	private String openAction;
	
	private boolean aggregator;
	
	private boolean isReporter;
	
	private long createrId;

	public NewsDtoInnerAll(long newsid, Date datetime, int views, int likes, String news_location, String headline,
			String discription, String audiourl, String image_url, String video_url, String reporter_name,
			int dislikes_count, int share_count, int comment_count, int flag_count, String detailed_news_url,
			String openAction) {
		super();
		this.newsid = newsid;
		this.datetime = datetime;
		this.views = views;
		this.likes = likes;
		this.news_location = news_location;
		this.headline = headline;
		this.discription = discription;
		this.audiourl = audiourl;
		this.image_url = image_url;
		this.video_url = video_url;
		this.reporter_name = reporter_name;
		this.dislikes_count = dislikes_count;
		this.share_count = share_count;
		this.comment_count = comment_count;
		this.flag_count = flag_count;
		this.detailed_news_url = detailed_news_url;
		this.openAction = openAction;
	}
	
	public NewsDtoInnerAll(long newsid, Date datetime, int views, int likes, String news_location, String headline,
			String discription, String audiourl, String image_url, String video_url, String reporter_name,
			int dislikes_count, int share_count, int comment_count, int flag_count, String detailed_news_url,
			String openAction, boolean aggregator) {
		super();
		this.newsid = newsid;
		this.datetime = datetime;
		this.views = views;
		this.likes = likes;
		this.news_location = news_location;
		this.headline = headline;
		this.discription = discription;
		this.audiourl = audiourl;
		this.image_url = image_url;
		this.video_url = video_url;
		this.reporter_name = reporter_name;
		this.dislikes_count = dislikes_count;
		this.share_count = share_count;
		this.comment_count = comment_count;
		this.flag_count = flag_count;
		this.detailed_news_url = detailed_news_url;
		this.openAction = openAction;
		this.aggregator = aggregator;
	}
	
	public NewsDtoInnerAll(long newsid, Date datetime, int views, int likes, String news_location, String headline,
			String discription, String audiourl, String image_url, String video_url, String reporter_name,
			int dislikes_count, int share_count, int comment_count, int flag_count, String detailed_news_url,
			String openAction, boolean aggregator, boolean isReporter, long createrId) {
		super();
		this.newsid = newsid;
		this.datetime = datetime;
		this.views = views;
		this.likes = likes;
		this.news_location = news_location;
		this.headline = headline;
		this.discription = discription;
		this.audiourl = audiourl;
		this.image_url = image_url;
		this.video_url = video_url;
		this.reporter_name = reporter_name;
		this.dislikes_count = dislikes_count;
		this.share_count = share_count;
		this.comment_count = comment_count;
		this.flag_count = flag_count;
		this.detailed_news_url = detailed_news_url;
		this.openAction = openAction;
		this.aggregator = aggregator;
		this.isReporter = isReporter;
		this.createrId = createrId;
	}
	
	public NewsDtoInnerAll() {
		super();
	}

	public long getNewsid() {
		return newsid;
	}

	public void setNewsid(long newsid) {
		this.newsid = newsid;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
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

	public String getNews_location() {
		return news_location;
	}

	public void setNews_location(String news_location) {
		this.news_location = news_location;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public String getAudiourl() {
		return audiourl;
	}

	public void setAudiourl(String audiourl) {
		this.audiourl = audiourl;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getReporter_name() {
		return reporter_name;
	}

	public void setReporter_name(String reporter_name) {
		this.reporter_name = reporter_name;
	}

	public int getDislikes_count() {
		return dislikes_count;
	}

	public void setDislikes_count(int dislikes_count) {
		this.dislikes_count = dislikes_count;
	}

	public int getShare_count() {
		return share_count;
	}

	public void setShare_count(int share_count) {
		this.share_count = share_count;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getFlag_count() {
		return flag_count;
	}

	public void setFlag_count(int flag_count) {
		this.flag_count = flag_count;
	}

	public String getDetailed_news_url() {
		return detailed_news_url;
	}

	public void setDetailed_news_url(String detailed_news_url) {
		this.detailed_news_url = detailed_news_url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	@JsonProperty("isReporter")
	public void setReporter(boolean isReporter) {
		this.isReporter = isReporter;
	}

	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	
	

	@Override
	public String toString() {
		return "NewsDtoInnerAll [newsid=" + newsid + ", datetime=" + datetime + ", views=" + views + ", likes=" + likes
				+ ", news_location=" + news_location + ", headline=" + headline + ", discription=" + discription
				+ ", audiourl=" + audiourl + ", image_url=" + image_url + ", video_url=" + video_url
				+ ", reporter_name=" + reporter_name + ", dislikes_count=" + dislikes_count + ", share_count="
				+ share_count + ", comment_count=" + comment_count + ", flag_count=" + flag_count
				+ ", detailed_news_url=" + detailed_news_url + ", type=" + type + ", openAction=" + openAction + "]";
	}
	
}
