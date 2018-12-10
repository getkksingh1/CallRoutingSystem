package com.triyasoft.model;

public class ClientSummary {

	private int runningcalls;
	private int todayCalls;
	private int yesterdayCalls;
	private double totalCost;
	private double todaysCost;
	private double yesterdayCost;

	public int getRunningcalls() {
		return runningcalls;
	}

	public void setRunningcalls(int runningcalls) {
		this.runningcalls = runningcalls;
	}

	public int getTodayCalls() {
		return todayCalls;
	}

	public void setTodayCalls(int todayCalls) {
		this.todayCalls = todayCalls;
	}

	public int getYesterdayCalls() {
		return yesterdayCalls;
	}

	public void setYesterdayCalls(int yesterdayCalls) {
		this.yesterdayCalls = yesterdayCalls;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getTodaysCost() {
		return todaysCost;
	}

	public void setTodaysCost(double todaysCost) {
		this.todaysCost = todaysCost;
	}

	public double getYesterdayCost() {
		return yesterdayCost;
	}

	public void setYesterdayCost(double yesterdayCost) {
		this.yesterdayCost = yesterdayCost;
	}

}
