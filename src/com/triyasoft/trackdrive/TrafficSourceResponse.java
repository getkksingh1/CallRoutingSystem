package com.triyasoft.trackdrive;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.triyasoft.daos.TrafficSourceDaoService;
import com.triyasoft.utils.AdcashZoneTracker;
import com.triyasoft.utils.ProjectUtils;

public class TrafficSourceResponse {
	private Integer status;
	private List<TrafficSource> traffic_sources = null;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<TrafficSource> getTrafficSources() {
		return traffic_sources;
	}
	public void setTrafficSources(List<TrafficSource> traffic_sources) {
		this.traffic_sources = traffic_sources;
	}
	
	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();

		TrafficSourceResponse callsResponse = gson.fromJson(new FileReader("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/trackdrive/trafficsourcejson"), TrafficSourceResponse.class);

		List<TrafficSource>  trafficSources =  callsResponse.getTrafficSources();
		 Connection conn = ProjectUtils.getMySQLConnection();;

		for (TrafficSource tdTrafficSource : trafficSources) {
			
			com.triyasoft.model.TrafficSource sourceModel = new com.triyasoft.model.TrafficSource();
			sourceModel.setId(Integer.parseInt(tdTrafficSource.getId().trim()));
			sourceModel.setFirst_name(tdTrafficSource.getFirst_name());
			sourceModel.setLast_name(tdTrafficSource.getUser_traffic_source_id());
			sourceModel.setIs_Active(1);
			
			TrackDriverDBUtility.addTrafficSourceViaDirectDBConnection(sourceModel, conn);
			
		}
		
		System.out.println(callsResponse);
		

	}
	
}
