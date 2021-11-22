package com.vocal.viewmodel;

public class ContentDto {
	
	private int id;
	
	private String text;
	
	private boolean status;

	public ContentDto(int id, String text, boolean status) {
		super();
		this.id = id;
		this.text = text;
		this.status = status;
	}
	
	public ContentDto() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
