package com.vocal.viewmodel;

public class CreateNewsPopupDto {

	private String title;

	private String opt1;
	
	private String opt2;
		
	public CreateNewsPopupDto() {
		super();
	}

	public CreateNewsPopupDto(String opt1, String opt2, String title) {
		super();
		this.opt1 = opt1;
		this.opt2 = opt2;
		this.title = title;
	}

	public String getOpt1() {
		return opt1;
	}

	public void setOpt1(String opt1) {
		this.opt1 = opt1;
	}

	public String getOpt2() {
		return opt2;
	}

	public void setOpt2(String opt2) {
		this.opt2 = opt2;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
