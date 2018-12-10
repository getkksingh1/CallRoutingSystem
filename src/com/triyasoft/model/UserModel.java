package com.triyasoft.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserModel implements Serializable{

/**
	 * 
	 */

private static final long serialVersionUID = 1L;
private String userid;
private String firstname;
private String lastname;
private String emailid;
private String jssessionid;
private int userroleid;
private String password;
private int[] account_id = {0};
private boolean is_logged_in = false;
private String additional_context = "";
private String timezone;
private String phonenumber;
private String skype;
private String addressline1;
private String addressline2;
private String city;
private String state;
private String country;
private String zip;

private Orgnization current_orgnization;

private int current_org_id_context;

private Map<Integer, Orgnization> orgid_OrgnizationMap = new HashMap<Integer, Orgnization>();





public Orgnization getCurrent_orgnization() {
	return current_orgnization;
}
public void setCurrent_orgnization(Orgnization current_orgnization) {
	this.current_orgnization = current_orgnization;
}
public int getCurrent_org_id_context() {
	return current_org_id_context;
}
public void setCurrent_org_id_context(int current_org_id_context) {
	this.current_org_id_context = current_org_id_context;
}
public Map<Integer, Orgnization> getOrgid_OrgnizationMap() {
	return orgid_OrgnizationMap;
}
public void setOrgid_OrgnizationMap(
		Map<Integer, Orgnization> orgid_OrgnizationMap) {
	this.orgid_OrgnizationMap = orgid_OrgnizationMap;
}
public boolean isIs_logged_in() {
	return is_logged_in;
}
public void setIs_logged_in(boolean is_logged_in) {
	this.is_logged_in = is_logged_in;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getFirstname() {
	return firstname;
}
public void setFirstname(String firstname) {
	this.firstname = firstname;
}
public String getLastname() {
	return lastname;
}
public void setLastname(String lastname) {
	this.lastname = lastname;
}
public String getEmailid() {
	return emailid;
}
public void setEmailid(String emailid) {
	this.emailid = emailid;
}
public String getJssessionid() {
	return jssessionid;
}
public void setJssessionid(String jssessionid) {
	this.jssessionid = jssessionid;
}
public int getUserroleid() {
	return userroleid;
}
public void setUserroleid(int userroleid) {
	this.userroleid = userroleid;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public int[] getAccount_id() {
	return account_id;
}
public void setAccount_id(int[] account_id) {
	this.account_id = account_id;
}
public String getAdditional_context() {
	return additional_context;
}
public void setAdditional_context(String additional_context) {
	this.additional_context = additional_context;
}
public String getTimezone() {
	return timezone;
}
public void setTimezone(String timezone) {
	this.timezone = timezone;
}
public String getPhonenumber() {
	return phonenumber;
}
public void setPhonenumber(String phonenumber) {
	this.phonenumber = phonenumber;
}
public String getSkype() {
	return skype;
}
public void setSkype(String skype) {
	this.skype = skype;
}
public String getAddressline1() {
	return addressline1;
}
public void setAddressline1(String addressline1) {
	this.addressline1 = addressline1;
}
public String getAddressline2() {
	return addressline2;
}
public void setAddressline2(String addressline2) {
	this.addressline2 = addressline2;
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
public String getZip() {
	return zip;
}
public void setZip(String zip) {
	this.zip = zip;
}
public static long getSerialversionuid() {
	return serialVersionUID;
}



}
