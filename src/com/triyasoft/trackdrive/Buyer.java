package com.triyasoft.trackdrive;

public class Buyer {

	private String id;
	private String created_at;
	private String updated_at;
	private String user_updated_at;
	private String name;
	private String number;
	private String paused;
	private String time_zone;
	private String user_buyer_id;
	private String bid_price;
	private String weight;
	private String tier;
	private String concurrency_cap_limit;
	private String buyer_conversion_daily_limit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getUser_updated_at() {
		return user_updated_at;
	}

	public void setUser_updated_at(String user_updated_at) {
		this.user_updated_at = user_updated_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPaused() {
		return paused;
	}

	public void setPaused(String paused) {
		this.paused = paused;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public void setTime_zone(String time_zone) {
		this.time_zone = time_zone;
	}

	public String getUser_buyer_id() {
		return user_buyer_id;
	}

	public void setUser_buyer_id(String user_buyer_id) {
		this.user_buyer_id = user_buyer_id;
	}

	public String getBid_price() {
		return bid_price;
	}

	public void setBid_price(String bid_price) {
		this.bid_price = bid_price;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public String getConcurrency_cap_limit() {
		return concurrency_cap_limit;
	}

	public void setConcurrency_cap_limit(String concurrency_cap_limit) {
		this.concurrency_cap_limit = concurrency_cap_limit;
	}

	public String getBuyer_conversion_daily_limit() {
		return buyer_conversion_daily_limit;
	}

	public void setBuyer_conversion_daily_limit(
			String buyer_conversion_daily_limit) {
		this.buyer_conversion_daily_limit = buyer_conversion_daily_limit;
	}

}
