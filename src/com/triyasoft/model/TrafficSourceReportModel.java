package com.triyasoft.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficSourceReportModel {

	Map<Integer, TrafficSourceSummary> trafficSourcesSummary = new HashMap<Integer, TrafficSourceSummary>();
	public int totalCall = 0 ;
	
	

	
	 public int getTotalCall() {
		return totalCall;
	}

	public void setTotalCall(int totalCall) {
		this.totalCall = totalCall;
	}

	public	TrafficSourceSummary getTrafficSourceSummaryInstance() {
			return new TrafficSourceSummary();
		}
	
	public Map<Integer, TrafficSourceSummary> getTrafficSourcesSummary() {
		return trafficSourcesSummary;
	}

	public void setTrafficSourcesSummary(
			Map<Integer, TrafficSourceSummary> trafficSourcesSummary) {
		this.trafficSourcesSummary = trafficSourcesSummary;
	}

	
	public class TrafficSourceSummary {
		
		public int trafficSourceId ;
		public String trafficSourceName;
		public int totalCalls = 0;
		public double percentageCalls;
		public int buyerConnected = 0;
		public double avgCallTime  = 0;
		public double totalCallTime  = 0;
		public int tsRepeat = 0;
		public int tsDuplicate = 0;
		public int tsUnique = 0;
		public int tsConverted = 0;
		public int buyerConverted = 0 ;
		public double percentageTsConveted = 0;
		public double tsPayout = 0;
		public double tsPPC = 0;
		public double providerCost = 0;
		public double providerCPC = 0;
		public double totalCost = 0;
		public double avgCPC = 0;
		public double profit =0;
		public double buyerRevenue = 0 ;
		public List<CallModel> calls = new ArrayList<CallModel>();
		
		
	 
		
		public double getBuyerRevenue() {
			return buyerRevenue;
		}
		public void setBuyerRevenue(double buyerRevenue) {
			this.buyerRevenue = buyerRevenue;
		}
		public int getBuyerConverted() {
			return buyerConverted;
		}
		public void setBuyerConverted(int buyerConverted) {
			this.buyerConverted = buyerConverted;
		}
		public List<CallModel> getCalls() {
			return calls;
		}
		public void setCalls(List<CallModel> calls) {
			this.calls = calls;
		}
		public double getTotalCallTime() {
			return totalCallTime;
		}
		public void setTotalCallTime(double totalCallTime) {
			this.totalCallTime = totalCallTime;
		}
		public int getTrafficSourceId() {
			return trafficSourceId;
		}
		public void setTrafficSourceId(int trafficSourceId) {
			this.trafficSourceId = trafficSourceId;
		}
		public String getTrafficSourceName() {
			return trafficSourceName;
		}
		public void setTrafficSourceName(String trafficSourceName) {
			this.trafficSourceName = trafficSourceName;
		}
		public int getTotalCalls() {
			return totalCalls;
		}
		public void setTotalCalls(int totalCalls) {
			this.totalCalls = totalCalls;
		}
		public double getPercentageCalls() {
			return percentageCalls;
		}
		public void setPercentageCalls(double percentageCalls) {
			this.percentageCalls = percentageCalls;
		}
		public int getBuyerConnected() {
			return buyerConnected;
		}
		public void setBuyerConnected(int buyerConnected) {
			this.buyerConnected = buyerConnected;
		}
		public double getAvgCallTime() {
			return avgCallTime;
		}
		public void setAvgCallTime(double avgCallTime) {
			this.avgCallTime = avgCallTime;
		}
		public int getTsRepeat() {
			return tsRepeat;
		}
		public void setTsRepeat(int tsRepeat) {
			this.tsRepeat = tsRepeat;
		}
		public int getTsDuplicate() {
			return tsDuplicate;
		}
		public void setTsDuplicate(int tsDuplicate) {
			this.tsDuplicate = tsDuplicate;
		}
		public int getTsUnique() {
			return tsUnique;
		}
		public void setTsUnique(int tsUnique) {
			this.tsUnique = tsUnique;
		}
		public int getTsConverted() {
			return tsConverted;
		}
		public void setTsConverted(int tsConverted) {
			this.tsConverted = tsConverted;
		}
		public double getPercentageTsConveted() {
			return percentageTsConveted;
		}
		public void setPercentageTsConveted(double percentageTsConveted) {
			this.percentageTsConveted = percentageTsConveted;
		}
		public double getTsPayout() {
			return tsPayout;
		}
		public void setTsPayout(double tsPayout) {
			this.tsPayout = tsPayout;
		}
		public double getTsPPC() {
			return tsPPC;
		}
		public void setTsPPC(double tsPPC) {
			this.tsPPC = tsPPC;
		}
		public double getProviderCost() {
			return providerCost;
		}
		public void setProviderCost(double providerCost) {
			this.providerCost = providerCost;
		}
		public double getProviderCPC() {
			return providerCPC;
		}
		public void setProviderCPC(double providerCPC) {
			this.providerCPC = providerCPC;
		}
		public double getTotalCost() {
			return totalCost;
		}
		public void setTotalCost(double totalCost) {
			this.totalCost = totalCost;
		}
		public double getAvgCPC() {
			return avgCPC;
		}
		public void setAvgCPC(double avgCPC) {
			this.avgCPC = avgCPC;
		}
		public double getProfit() {
			return profit;
		}
		public void setProfit(double profit) {
			this.profit = profit;
		}
		
		
		
	}
	
}

