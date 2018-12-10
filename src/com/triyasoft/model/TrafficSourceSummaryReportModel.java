package com.triyasoft.model;

import java.util.TreeMap;

import com.triyasoft.model.TrafficSourceReportModel.TrafficSourceSummary;

public class TrafficSourceSummaryReportModel {

	public int totalCalls = 0 ;
	public String traffic_sourcename = "";
	public TreeMap<String, TrafficSourceSummary> sourceSummaryMap ;
	public int getTotalCalls() {
		return totalCalls;
	}
	public void setTotalCalls(int totalCalls) {
		this.totalCalls = totalCalls;
	}
	public String getTraffic_sourcename() {
		return traffic_sourcename;
	}
	public void setTraffic_sourcename(String traffic_sourcename) {
		this.traffic_sourcename = traffic_sourcename;
	}
	public TreeMap<String, TrafficSourceSummary> getSourceSummaryMap() {
		return sourceSummaryMap;
	}
	public void setSourceSummaryMap(
			TreeMap<String, TrafficSourceSummary> sourceSummaryMap) {
		this.sourceSummaryMap = sourceSummaryMap;
	}
	
	
	

}
