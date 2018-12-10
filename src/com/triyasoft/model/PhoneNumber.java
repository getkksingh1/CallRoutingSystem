package com.triyasoft.model;

import java.util.Date;

public class PhoneNumber {

	private int id;
	private String number;
	private int traffic_source_id;
	private Date last_call_at;
	private Date created_at;
	private Date updated_at;
	private TrafficSource trafficSource;
	private int is_Active;
	private double costpercall = 0.0;

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getTraffic_source_id() {
		return traffic_source_id;
	}

	public void setTraffic_source_id(int traffic_source_id) {
		this.traffic_source_id = traffic_source_id;
	}

	public Date getLast_call_at() {
		return last_call_at;
	}

	public void setLast_call_at(Date last_call_at) {
		this.last_call_at = last_call_at;
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

	public int getIs_Active() {
		return is_Active;
	}

	public void setIs_Active(int is_Active) {
		this.is_Active = is_Active;
	}

	public TrafficSource getTrafficSource() {
		return trafficSource;
	}

	public void setTrafficSource(TrafficSource trafficSource) {
		this.trafficSource = trafficSource;
	}

	@Override
	public String toString() {
		return "PhoneNumber [id=" + id + ", number=" + number
				+ ", traffic_source_id=" + traffic_source_id
				+ ", last_call_at=" + last_call_at + ", created_at="
				+ created_at + ", updated_at=" + updated_at + ", is_Active="
				+ is_Active + "]";
	}

	public double getCostpercall() {
		return costpercall;
	}

	public void setCostpercall(double costpercall) {
		this.costpercall = costpercall;
	}

}
