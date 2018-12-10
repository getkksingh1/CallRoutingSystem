package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.triyasoft.model.Buyer;
import com.triyasoft.model.CallModel;
import com.triyasoft.utils.AdcashZoneTracker;
import com.triyasoft.utils.ProjectUtils;

public class ReportsDao {

	public static List<CallModel> loadAllCall(String buyerId, String sourceId,
			String startdate, String endDate) {

		Statement stmt = null;
		ResultSet rs = null;
		List<CallModel> calls = new ArrayList<CallModel>();
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();
			String sqlQuery = "select * from calls where is_running!=1 ";
			if (buyerId != null && buyerId.length() > 0
					&& !"-1".equals(buyerId))
				sqlQuery = sqlQuery + " and buyer_id ='" + buyerId + "'  ";

			if (sourceId != null && sourceId.length() > 0
					&& !"-1".equals(sourceId))
				sqlQuery = sqlQuery + " and traffic_source_id ='" + sourceId
						+ "'  ";

			sqlQuery = sqlQuery + " and calllandingtimeonsever >'" + startdate
					+ "'  ";
			sqlQuery = sqlQuery + " and calllandingtimeonsever <'" + endDate
					+ "'  ";

			sqlQuery = sqlQuery + "  order by calllandingtimeonsever desc ";

			System.out.println(sqlQuery);
			rs = stmt.executeQuery(sqlQuery + "  ;");

			while (rs.next()) {

				CallModel call = new CallModel();

				call.setUuid(rs.getString("uuid"));
				call.setNumber_called(rs.getString("number_called"));
				call.setConnected_to(rs.getString("connected_to"));
				call.setCaller_number(rs.getString("caller_number"));
				call.setTraffic_source(rs.getString("traffic_source"));
				call.setTraffic_source_id(rs.getInt("traffic_source_id"));
				call.setBuyer(rs.getString("buyer"));
				call.setBuyer_id(rs.getInt("buyer_id"));

				call.setBuyer_revenue(rs.getDouble("buyer_revenue"));
				call.setTraffic_source_revenue(rs
						.getDouble("traffic_source_revenue"));
				call.setTotalCost(rs.getString("totalcost"));

				call.setError_code(rs.getString("error_code"));
				call.setError_description(rs.getString("error_description"));
				call.setCallStatusAtHangup(rs.getString("callStatusAtHangup"));
				call.setRecording_url(rs.getString("recording_url"));

				call.setCallattempts(rs.getInt("callattempts"));
				call.setConnected_time(rs.getInt("connected_time"));
				call.setBuyer_connecting_time(rs
						.getInt("buyer_connecting_time"));
				call.setBuyers_tried(rs.getString("buyers_tried"));
				call.setBuyer_hangup_reason(rs.getString("buyer_hangup_reason"));
				call.setNo_connect_cause(rs.getString("no_connect_cause"));

				String duration = rs.getString("duration");

				if (duration == null)
					duration = "0";
				call.setDuration(duration);

				Date callLandingTime = rs
						.getTimestamp("calllandingtimeonsever");

				call.setDateInEST(ProjectUtils
						.convertToEasternTime(callLandingTime));

				call.setCallLandingTimeOnServer(callLandingTime);

				call.setEndTime(new Date(callLandingTime.getTime()
						+ Integer.parseInt(duration) * 1000));
				call.setEndDateInEST(ProjectUtils.convertToEasternTime(call
						.getEndTime()));

				calls.add(call);

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
			ProjectUtils.closeConnection(rs, stmt, conn);
		}

		List<CallModel> callsWithOutBlankSource = new ArrayList<CallModel>();

		for (CallModel callModel : calls) {

			if (callModel.getTraffic_source_id() == 0
					&& callModel.getBuyer_id() == 0)
				continue;
			else
				callsWithOutBlankSource.add(callModel);
		}

		return callsWithOutBlankSource;
	}

	public static List<CallModel> loadYesterdayCalls(String startDateStr,
			String endDateStr) {

		Statement stmt = null;
		ResultSet rs = null;
		List<CallModel> calls = new ArrayList<CallModel>();
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();
			String sqlQuery = "select uuid,	number_called,	connected_to, recording_url, 	caller_number,	traffic_source_id,	traffic_source,	buyer,	buyer_id,	callerName,	callStatusAtAnswer,	billRate ,	hangupCause,	ADDTIME(calllandingtimeonsever, '-040000') calltime ,	"
					+ "billduration, duration, buyer_revenue, traffic_source_revenue, totalcost phonecost , (buyer_revenue-traffic_source_revenue-totalcost) callprofit from calls where  callerName!='skype' "
					+ " and calllandingtimeonsever> ADDTIME('"
					+ startDateStr
					+ " 00:00:00', '040000') and calllandingtimeonsever < ADDTIME('"
					+ endDateStr
					+ " 00:00:00', '040000') order by calllandingtimeonsever desc ;";

			System.out.println(sqlQuery);
			rs = stmt.executeQuery(sqlQuery);

			while (rs.next()) {

				CallModel call = new CallModel();

				call.setUuid(rs.getString("uuid"));
				call.setNumber_called(rs.getString("number_called"));
				call.setConnected_to(rs.getString("connected_to"));
				call.setCaller_number(rs.getString("caller_number"));
				call.setTraffic_source_id(rs.getInt("traffic_source_id"));
				call.setTraffic_source(rs.getString("traffic_source"));
				call.setBuyer(rs.getString("buyer"));
				call.setBuyer_id(rs.getInt("buyer_id"));
				String duration = rs.getString("duration");

				if (duration == null)
					duration = "0";
				call.setDuration(duration);

				Date callLandingTime = rs.getTimestamp("calltime");
				call.setCallLandingTimeOnServer(callLandingTime);
				call.setEndTime(new Date(callLandingTime.getTime()
						+ Integer.parseInt(duration) * 1000));
				call.setCallerName(rs.getString("callerName"));
				call.setCallStatusAtAnswer(rs.getString("callStatusAtAnswer"));
				call.setBillRate(rs.getString("billRate"));
				call.setHangupCause(rs.getString("hangupCause"));
				call.setBillDuration(rs.getString("billduration"));
				call.setBuyer_revenue(rs.getDouble("buyer_revenue"));
				call.setTraffic_source_revenue(rs
						.getDouble("traffic_source_revenue"));
				call.setTotalCost(rs.getString("phonecost"));
				call.setCallProfit(rs.getDouble("callprofit"));
				call.setRecording_url(rs.getString("recording_url"));

				calls.add(call);

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
			ProjectUtils.closeConnection(rs, stmt, conn);
		}

		List<CallModel> callsWithOutBlankSource = new ArrayList<CallModel>();

		for (CallModel callModel : calls) {

			if (callModel.getTraffic_source_id() == 0
					&& callModel.getBuyer_id() == 0)
				continue;
			else
				callsWithOutBlankSource.add(callModel);
		}

		return callsWithOutBlankSource;
	}

	public static List<CallModel> loadTrafficSourceCalls(String startDateStr,
			String endDateStr, String trafficSourceId) {

		Statement stmt = null;
		ResultSet rs = null;
		List<CallModel> calls = new ArrayList<CallModel>();
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			String appendTrafficSourceFilter = "";
			if (trafficSourceId != null && trafficSourceId.trim().length() > 0
					&& !"-1".equals(trafficSourceId))
				appendTrafficSourceFilter = "and traffic_source_id ='"
						+ trafficSourceId + "' ";

			stmt = conn.createStatement();
			String sqlQuery = "select uuid,	number_called,	connected_to,	caller_number,	traffic_source_id,	traffic_source,	buyer,	buyer_id,	callerName,	callStatusAtAnswer,	billRate ,	hangupCause,	ADDTIME(calllandingtimeonsever, '-040000') calltime ,	"
					+ "billduration, duration, buyer_revenue, traffic_source_revenue, totalcost phonecost , (buyer_revenue-traffic_source_revenue-totalcost) callprofit from calls where  callerName!='skype' "
					+ " and calllandingtimeonsever> ADDTIME('"
					+ startDateStr
					+ " 00:00:00', '040000') and calllandingtimeonsever < ADDTIME('"
					+ endDateStr
					+ " 00:00:00', '040000') "
					+ appendTrafficSourceFilter
					+ " order by calllandingtimeonsever desc ;";

			System.out.println(sqlQuery);
			rs = stmt.executeQuery(sqlQuery);

			while (rs.next()) {

				CallModel call = new CallModel();

				call.setUuid(rs.getString("uuid"));
				call.setNumber_called(rs.getString("number_called"));
				call.setConnected_to(rs.getString("connected_to"));
				call.setCaller_number(rs.getString("caller_number"));
				call.setTraffic_source_id(rs.getInt("traffic_source_id"));
				call.setTraffic_source(rs.getString("traffic_source"));
				call.setBuyer(rs.getString("buyer"));
				call.setBuyer_id(rs.getInt("buyer_id"));
				String duration = rs.getString("duration");

				if (duration == null)
					duration = "0";
				call.setDuration(duration);

				Date callLandingTime = rs.getTimestamp("calltime");
				call.setCallLandingTimeOnServer(callLandingTime);
				call.setEndTime(new Date(callLandingTime.getTime()
						+ Integer.parseInt(duration) * 1000));
				call.setCallerName(rs.getString("callerName"));
				call.setCallStatusAtAnswer(rs.getString("callStatusAtAnswer"));
				call.setBillRate(rs.getString("billRate"));
				call.setHangupCause(rs.getString("hangupCause"));
				call.setBillDuration(rs.getString("billduration"));
				call.setBuyer_revenue(rs.getDouble("buyer_revenue"));
				call.setTraffic_source_revenue(rs
						.getDouble("traffic_source_revenue"));
				call.setTotalCost(rs.getString("phonecost"));
				call.setCallProfit(rs.getDouble("callprofit"));

				calls.add(call);

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
			ProjectUtils.closeConnection(rs, stmt, conn);
		}

		List<CallModel> callsWithOutBlankSource = new ArrayList<CallModel>();

		for (CallModel callModel : calls) {

			if (callModel.getTraffic_source_id() == 0
					&& callModel.getBuyer_id() == 0)
				continue;
			else
				callsWithOutBlankSource.add(callModel);
		}

		return callsWithOutBlankSource;
	}

}
