package com.triyasoft.model;

import java.util.Date;

public class PlivoEndpointExtention {

	private int id;
	private String extension_number;
	private String extension_name;
	private String sip_url;
	private String plivo_endpoint_id;
	private String plivo_endpoint_user_id;
	private String plivo_endpoint_user_name;
	private String plivo_endpoint_user_password;
	private String plivo_endpoint_user_alias;
	private String plivo_endpoint_application_name;
	private String plivo_end_point_domain;
	private String plivo_endpoint_application_id;
	private int current_status;
	private int isactive;
	private Date created_date;
	
	private boolean sip_registered = false;
	private boolean extension_busy = false;
	
	
	
	public boolean isExtensionReadytoTakeCall() {
		
		return sip_registered && (!extension_busy);
	}
	
	
	
	
	public boolean isSip_registered() {
		return sip_registered;
	}
	public void setSip_registered(boolean sip_registered) {
		this.sip_registered = sip_registered;
	}
	public boolean isExtension_busy() {
		return extension_busy;
	}
	public void setExtension_busy(boolean extension_busy) {
		this.extension_busy = extension_busy;
	}
	public int getCurrent_status() {
		return current_status;
	}
	public void setCurrent_status(int current_status) {
		this.current_status = current_status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExtension_number() {
		return extension_number;
	}
	public void setExtension_number(String extension_number) {
		this.extension_number = extension_number;
	}
	public String getExtension_name() {
		return extension_name;
	}
	public void setExtension_name(String extension_name) {
		this.extension_name = extension_name;
	}
	public String getSip_url() {
		return sip_url;
	}
	public void setSip_url(String sip_url) {
		this.sip_url = sip_url;
	}
	public String getPlivo_endpoint_id() {
		return plivo_endpoint_id;
	}
	public void setPlivo_endpoint_id(String plivo_endpoint_id) {
		this.plivo_endpoint_id = plivo_endpoint_id;
	}
	public String getPlivo_endpoint_user_id() {
		return plivo_endpoint_user_id;
	}
	public void setPlivo_endpoint_user_id(String plivo_endpoint_user_id) {
		this.plivo_endpoint_user_id = plivo_endpoint_user_id;
	}
	public String getPlivo_endpoint_user_name() {
		return plivo_endpoint_user_name;
	}
	public void setPlivo_endpoint_user_name(String plivo_endpoint_user_name) {
		this.plivo_endpoint_user_name = plivo_endpoint_user_name;
	}
	public String getPlivo_endpoint_user_password() {
		return plivo_endpoint_user_password;
	}
	public void setPlivo_endpoint_user_password(String plivo_endpoint_user_password) {
		this.plivo_endpoint_user_password = plivo_endpoint_user_password;
	}
	public String getPlivo_endpoint_user_alias() {
		return plivo_endpoint_user_alias;
	}
	public void setPlivo_endpoint_user_alias(String plivo_endpoint_user_alias) {
		this.plivo_endpoint_user_alias = plivo_endpoint_user_alias;
	}
	public String getPlivo_endpoint_application_name() {
		return plivo_endpoint_application_name;
	}
	public void setPlivo_endpoint_application_name(
			String plivo_endpoint_application_name) {
		this.plivo_endpoint_application_name = plivo_endpoint_application_name;
	}
	public String getPlivo_end_point_domain() {
		return plivo_end_point_domain;
	}
	public void setPlivo_end_point_domain(String plivo_end_point_domain) {
		this.plivo_end_point_domain = plivo_end_point_domain;
	}
	public String getPlivo_endpoint_application_id() {
		return plivo_endpoint_application_id;
	}
	public void setPlivo_endpoint_application_id(
			String plivo_endpoint_application_id) {
		this.plivo_endpoint_application_id = plivo_endpoint_application_id;
	}
	public int getIsactive() {
		return isactive;
	}
	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	
	
	
	
}
