package com.triyasoft.model;

public class LeadModel {

	private String email = "";
	private String companyName = "";
	private String firstName = "";
	private String lastName = "";

	private String skype_name = "";
	private String phoneNumber = "";
	private String addressLine1 = "";
	private String addressLine2 = "";
	private String zip = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String request_demo = "";

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSkype_name() {
		return skype_name;
	}

	public void setSkype_name(String skype_name) {
		this.skype_name = skype_name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRequest_demo() {
		return request_demo;
	}

	public void setRequest_demo(String request_demo) {
		this.request_demo = request_demo;
	}

	@Override
	public String toString() {
		return "LeadModel [email=" + email + ", companyName=" + companyName
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", skype_name=" + skype_name + ", phoneNumber=" + phoneNumber
				+ ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", zip=" + zip + ", city=" + city + ", state="
				+ state + ", country=" + country + ", request_demo="
				+ request_demo + "]";
	}

}
