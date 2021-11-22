package com.vocal.viewmodel;

public class Ticker {

	private String text;
	
	private String action;
	
	private String color;
	
	public Ticker() {
		super();
	}

	public Ticker(String text, String action, String color) {
		super();
		this.text = text;
		this.action = action;
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
