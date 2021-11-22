package com.vocal.viewmodel;

import java.util.List;

public class CityListDto {
	
	private List<CityDto> CityList;
	
	public CityListDto() {
		super();
	}
	
	public CityListDto(List<CityDto> cityList) {
		super();
		CityList = cityList;
	}

	public List<CityDto> getCityList() {
		return CityList;
	}

	public void setCityList(List<CityDto> cityList) {
		CityList = cityList;
	}
	
}
