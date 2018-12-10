package com.triyasoft.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.response.Record;
import com.plivo.helper.exception.PlivoException;
import com.triyasoft.model.CallModel;

public class RecordingUtil {
	public static void main(String[] args) {

		String auth_id = "MAODJKN2MZOGE4OTNHNJ";
		String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";
		RestAPI api1 = new RestAPI(auth_id, auth_token, "v1");
		LinkedHashMap<String, String> parameters1 = new LinkedHashMap<String, String>();
		String uuid = "";
		parameters1.put("call_uuid", uuid); // ID of the call
		parameters1.put("time_limit", "300"); // Max recording duration in
												// seconds

		try {
			Record resp1 = api1.record(parameters1);
			System.out.println(resp1.api_id);
			System.out.println(resp1.message);
			System.out.println(resp1.url);
			System.out.println(resp1.error);
			System.out.println(resp1.serverCode);

		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	public static List<CallModel> loadLiveCalls() {

		List<CallModel> calls = new ArrayList<CallModel>();

		Statement stmt = null;
		ResultSet rs = null;
		Connection connection = ProjectUtils.getMySQLConnection();

		try {

			stmt = connection.createStatement();

			String query = "select * from calls where is_running=1 ;";

			System.out.println(query);
			rs = stmt.executeQuery(query);

			while (rs.next()) {

				CallModel callModel = new CallModel();

				Integer id = rs.getInt("id");
				String uuid = rs.getString("uuid");

				callModel.setId(id);
				callModel.setUuid(uuid);

				callModel.setRecording_url(rs.getString("recording_url"));
				callModel.setNumber_called(rs.getString("number_called"));
				callModel.setConnected_to(rs.getString("connected_to"));
				callModel.setCaller_number(rs.getString("caller_number"));
				callModel.setTraffic_source_id(rs.getInt("traffic_source_id"));
				callModel.setTraffic_source(rs.getString("traffic_source"));
				callModel.setBuyer(rs.getString("buyer"));
				callModel.setBuyer_id(rs.getInt("buyer_id"));

				// Plivo Parameters at call start
				callModel.setCallStatusAtAnswer(rs
						.getString("callStatusAtAnswer"));
				callModel.setAnswerEvent(rs.getString("answerEvent"));
				callModel.setBillRate(rs.getString("billRate"));
				callModel.setXmlResponse(rs.getString("xmlResponse"));
				callModel.setDirection(rs.getString("direction"));
				callModel.setCallerName(rs.getString("callerName"));

				// Plivo Parameters at call hangup

				callModel.setTotalCost(rs.getString("totalCost"));
				callModel.setHangupCause(rs.getString("hangupCause"));
				callModel.setStartTime(rs.getTimestamp("startTime"));
				callModel.setEndTime(rs.getTimestamp("endTime"));
				callModel.setAnswerTime(rs.getTimestamp("answer_time"));
				callModel.setCallLandingTimeOnServer(rs
						.getTimestamp("calllandingtimeonsever"));

				callModel.setDuration(rs.getString("duration"));
				callModel.setHangupEvent(rs.getString("hangupEvent"));
				callModel.setCallStatusAtHangup(rs
						.getString("callStatusAtHangup"));

				callModel.setIs_running(rs.getInt("is_running"));

				callModel.setBuyer_revenue(rs.getDouble("buyer_revenue"));
				callModel.setTraffic_source_revenue(rs
						.getDouble("traffic_source_revenue"));

				// callModel.setCall_buyer(cachedbuyersMap.get(callModel.getBuyer_id()));
				// callModel.setCall_from_source(cachedTrafficsourcesMap.get(callModel.getTraffic_source_id()));

				calls.add(callModel);

				// runningCallsMapByUUID.put(uuid, callModel);

			}
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(rs, stmt, connection);
		}

		return calls;
	}

}
