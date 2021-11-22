package com.vocal.viewmodel;


public class AddressModel {
	
	//private String addressLine;
	
	private String adminArea;

	private String country;
	
	private String state;
	
	private String city;
	
	private double latitude;
	
	private double longitude;
	
	private String postalCode;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/*
	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}
	*/

	public String getAdminArea() {
		return adminArea;
	}

	public void setAdminArea(String adminArea) {
		this.adminArea = adminArea;
	}

	@Override
	public String toString() {
		return "AddressModel [adminArea=" + adminArea + ", country=" + country + ", state=" + state + ", city=" + city
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", postalCode=" + postalCode + "]";
	}
}
