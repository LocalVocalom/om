package com.vocal.viewmodel;

public class UploadResponseDto {
	
	private String url;
	
	public UploadResponseDto() {
		super();
	}

	public UploadResponseDto(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
