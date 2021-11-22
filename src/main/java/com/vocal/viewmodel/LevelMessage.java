package com.vocal.viewmodel;

public class LevelMessage {

	private int level;
	
	private String msg;
	
	private String headline;
	
	public LevelMessage() {
		super();
	}

	public LevelMessage(int level, String msg) {
		super();
		this.level = level;
		this.msg = msg;
	}

	public LevelMessage(int level, String msg, String headline) {
		super();
		this.level = level;
		this.msg = msg;
		this.headline = headline;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}
}
