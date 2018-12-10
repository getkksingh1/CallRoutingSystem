package com.triyasoft.trackdrive;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.triyasoft.model.CallModel;

public class CallsResponse {

	public static void main(String[] args) throws JsonSyntaxException,
			JsonIOException, FileNotFoundException {

		/*
		 * if(true) { String data ="2017-06-22T07:47:40.772+00:00";
		 * System.out.println(convertToDate(data)); return; }
		 */
		Gson gson = new Gson();

		CallsResponse callsResponse = gson
				.fromJson(
						new FileReader(
								"/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/trackdrive/trackercallsjson.txt"),
						CallsResponse.class);

		List<Call> trackDrivecalls = callsResponse.getCalls();

		for (Call trackDriveCall : trackDrivecalls) {

			CallModel callModel = new CallModel();
			callModel.setUuid(trackDriveCall.getUuid());
			callModel.setNumber_called(trackDriveCall.getNumber_called()
					.replace("+", ""));
			callModel.setCaller_number(trackDriveCall.getCaller_number());
			callModel.setDuration(trackDriveCall.getTotal_duration() + "");
			callModel.setBillDuration(trackDriveCall.getTotal_duration() + "");

			callModel.setBuyer(trackDriveCall.getBuyer());

			String connected_to = trackDriveCall.getConnected_to();
			if (connected_to != null || connected_to.trim().length() != 0)
				connected_to = connected_to.replace("+", "");

			callModel.setConnected_to(connected_to);

			Date callLandingTime = convertToDate(trackDriveCall.getCreated_at());
			callModel.setCallLandingTimeOnServer(callLandingTime);

			double trackdrive_cost = trackDriveCall.getTrackdrive_cost();
			double provider_cost = trackDriveCall.getProvider_cost();

			callModel.setTotalCost((provider_cost + trackdrive_cost) + "");

		}

		System.out.println(callsResponse);

	}

	private static Date convertToDate(String created_at) {
		// 2017-06-22T07:47:40.772+00:00
		created_at = created_at.replace("+00:00", "").replace("T", " ");

		Date date = null;

		SimpleDateFormat datformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S");
		try {
			date = datformat.parse(created_at);
		} catch (ParseException e) {

			System.out.println("Could not parse date");
			date = new Date();
		}

		return date;
	}

	private Integer status;
	private List<Call> calls = null;

	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Call> getCalls() {
		return calls;
	}

	public void setCalls(List<Call> calls) {
		this.calls = calls;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}