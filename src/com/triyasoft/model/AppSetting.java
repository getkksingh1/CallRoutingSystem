package com.triyasoft.model;

public class AppSetting {

	private int id;
	private String key;
	private String value;
	private int user_configurable;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getUser_configurable() {
		return user_configurable;
	}
	public void setUser_configurable(int user_configurable) {
		this.user_configurable = user_configurable;
	}

}
