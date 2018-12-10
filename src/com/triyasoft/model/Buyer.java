package com.triyasoft.model;

import java.util.Date;

public class Buyer {

	private int buyer_id;
	private int org_id;
	private String orgnization_name;
	private String buyer_name;
	private String buyer_number;
	private Date created_at;
	private int tier;
	private int weight;
	private int concurrency_cap_limit;
	private int concurrency_cap_used;
	private int running_status;
	private double bid_price;
	private int buyer_daily_cap = 0;
	private int ring_timeout = 30;

	private int is_active = 1;

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

	public int getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getBuyer_number() {
		return buyer_number;
	}

	public void setBuyer_number(String buyer_number) {
		this.buyer_number = buyer_number;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getConcurrency_cap_limit() {
		return concurrency_cap_limit;
	}

	public void setConcurrency_cap_limit(int concurrency_cap_limit) {
		this.concurrency_cap_limit = concurrency_cap_limit;
	}

	public int getConcurrency_cap_used() {
		return concurrency_cap_used;
	}

	public void setConcurrency_cap_used(int concurrency_cap_used) {
		this.concurrency_cap_used = concurrency_cap_used;
	}

	public int getRunning_status() {
		return running_status;
	}

	public void setRunning_status(int running_status) {
		this.running_status = running_status;
	}

	public double getBid_price() {
		return bid_price;
	}

	public void setBid_price(double bid_price) {
		this.bid_price = bid_price;
	}

	@Override
	public String toString() {
		return "Buyer [buyer_id=" + buyer_id + ", buyer_name=" + buyer_name
				+ ", buyer_number=" + buyer_number + ", created_at="
				+ created_at + ", tier=" + tier + ", weight=" + weight
				+ ", concurrency_cap_limit=" + concurrency_cap_limit
				+ ", concurrency_cap_used=" + concurrency_cap_used
				+ ", running_status=" + running_status + ", bid_price="
				+ bid_price + "]";
	}

	public int getIs_active() {
		return is_active;
	}

	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}

	public int getBuyer_daily_cap() {
		return buyer_daily_cap;
	}

	public void setBuyer_daily_cap(int buyer_daily_cap) {
		this.buyer_daily_cap = buyer_daily_cap;
	}

	public int getRing_timeout() {
		return ring_timeout;
	}

	public void setRing_timeout(int ring_timeout) {
		this.ring_timeout = ring_timeout;
	}

}
