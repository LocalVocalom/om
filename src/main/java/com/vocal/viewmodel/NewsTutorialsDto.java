package com.vocal.viewmodel;

import java.util.List;

public class NewsTutorialsDto {

	private String header_title;
	
	private String header_logo;
	
	private List<ContentDto> content;

	public NewsTutorialsDto(String header_title, String header_logo, List<ContentDto> content) {
		super();
		this.header_title = header_title;
		this.header_logo = header_logo;
		this.content = content;
	}
	
	public NewsTutorialsDto() {
		super();
	}

	public String getHeader_title() {
		return header_title;
	}

	public void setHeader_title(String header_title) {
		this.header_title = header_title;
	}

	public String getHeader_logo() {
		return header_logo;
	}

	public void setHeader_logo(String header_logo) {
		this.header_logo = header_logo;
	}

	public List<ContentDto> getContent() {
		return content;
	}

	public void setContent(List<ContentDto> content) {
		this.content = content;
	}
	
}
