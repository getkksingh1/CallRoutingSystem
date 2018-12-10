package com.triyasoft.model;

import java.util.Date;

public class TrafficSource {

	private int id;
	private Date created_at;
	private Date updated_at;
	private String first_name;
	private String last_name;
	private int is_Active;

	private int org_id;
	private String orgnization_name;

	public int getOrg_id() {
		return org_id;
	}

	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}

	public String getOrgnization_name() {
		return orgnization_name;
	}

	public void setOrgnization_name(String orgnization_name) {
		this.orgnization_name = orgnization_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getIs_Active() {
		return is_Active;
	}

	public void setIs_Active(int is_Active) {
		this.is_Active = is_Active;
	}

	@Override
	public String toString() {
		return "TrafficSource [id=" + id + ", created_at=" + created_at
				+ ", updated_at=" + updated_at + ", first_name=" + first_name
				+ ", last_name=" + last_name + ", is_Active=" + is_Active + "]";
	}

}
