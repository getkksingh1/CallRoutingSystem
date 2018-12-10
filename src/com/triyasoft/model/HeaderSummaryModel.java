package com.triyasoft.model;

public class HeaderSummaryModel {

	private int activeCalls = 0;
	private int activeCCcurrent = 0;
	private int activeCCmax = 0;
	private int convertedCurrentCalls = 0;
	private int convertedMaxCalls = 0;
	private double revenue = 0.0;
	private double providerbalance = 0.0;
	private double trackerBalance = 0.0;
	private int noticeCount = 0;

	public int getActiveCalls() {
		return activeCalls;
	}

	public void setActiveCalls(int activeCalls) {
		this.activeCalls = activeCalls;
	}

	public int getActiveCCcurrent() {
		return activeCCcurrent;
	}

	public void setActiveCCcurrent(int activeCCcurrent) {
		this.activeCCcurrent = activeCCcurrent;
	}

	public int getActiveCCmax() {
		return activeCCmax;
	}

	public void setActiveCCmax(int activeCCmax) {
		this.activeCCmax = activeCCmax;
	}

	public int getConvertedCurrentCalls() {
		return convertedCurrentCalls;
	}

	public void setConvertedCurrentCalls(int convertedCurrentCalls) {
		this.convertedCurrentCalls = convertedCurrentCalls;
	}

	public int getConvertedMaxCalls() {
		return convertedMaxCalls;
	}

	public void setConvertedMaxCalls(int convertedMaxCalls) {
		this.convertedMaxCalls = convertedMaxCalls;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public double getProviderbalance() {
		return providerbalance;
	}

	public void setProviderbalance(double providerbalance) {
		this.providerbalance = providerbalance;
	}

	public double getTrackerBalance() {
		return trackerBalance;
	}

	public void setTrackerBalance(double trackerBalance) {
		this.trackerBalance = trackerBalance;
	}

	public int getNoticeCount() {
		return noticeCount;
	}

	public void setNoticeCount(int noticeCount) {
		this.noticeCount = noticeCount;
	}

	@Override
	public String toString() {
		return "HeaderSummaryModel [activeCalls=" + activeCalls
				+ ", activeCCcurrent=" + activeCCcurrent + ", activeCCmax="
				+ activeCCmax + ", convertedCurrentCalls="
				+ convertedCurrentCalls + ", convertedMaxCalls="
				+ convertedMaxCalls + ", revenue=" + revenue
				+ ", providerbalance=" + providerbalance + ", trackerBalance="
				+ trackerBalance + ", noticeCount=" + noticeCount + "]";
	}

}
