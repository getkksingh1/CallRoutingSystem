package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.triyasoft.model.ClientSummary;
import com.triyasoft.utils.ProjectUtils;

public class AllCustomerDao {

	public static List<String> getAllSchemas() {
		List<String> schemas = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery("show databases;");

			while (rs.next()) {

				schemas.add(rs.getString("Database"));
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return schemas;

	}

	public static Map<String, ClientSummary> getAllClientsSummaryt(
			List<String> prohibitedSchemas) {
		Map<String, ClientSummary> allClientSummary = new HashMap<String, ClientSummary>();

		List<String> allschemas = getAllSchemas();

		for (String db : allschemas) {
			if (!prohibitedSchemas.contains(db)) {
				ClientSummary clientSummary = new ClientSummary();
				clientSummary.setTodaysCost(getTodayPhonecost(db));
				clientSummary.setTotalCost(getTotalCallCost(db));
				clientSummary.setYesterdayCost(getYesterdayPhonecost(db));
				clientSummary.setRunningcalls(getRunningCalls(db));
				clientSummary.setTodayCalls(getTodayCallCount(db));
				clientSummary.setYesterdayCalls(getYesterdayCallCount(db));

				allClientSummary.put(db, clientSummary);
			}
		}

		return allClientSummary;
	}

	private static int getYesterdayCallCount(String db) {

		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			String query = "select count(*) cnt from  `" + db
					+ "`.calls  where calllandingtimeonsever > '"
					+ ProjectUtils.getYesterdayStrInClientsTimezone(null)
					+ "'   and calllandingtimeonsever < '"
					+ ProjectUtils.getTodayStrInClientsTimezone(null) + "' ;";
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				return rs.getInt("cnt");
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return 0;

	}

	private static int getTodayCallCount(String db) {

		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			String query = "select count(*) cnt from  `" + db
					+ "`.calls  where calllandingtimeonsever > '"
					+ ProjectUtils.getTodayStrInClientsTimezone(null) + "'  ;";
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				return rs.getInt("cnt");
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return 0;

	}

	private static int getRunningCalls(String db) {

		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			String query = "select count(*) cnt from  `" + db
					+ "`.calls  where is_running=1;";
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				return rs.getInt("cnt");
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return 0;

	}

	private static Double getYesterdayPhonecost(String db) {

		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			String query = "select sum(totalCost) ydaycost from `" + db
					+ "`.calls  where  calllandingtimeonsever > '"
					+ ProjectUtils.getYesterdayStrInClientsTimezone(null)
					+ "' and calllandingtimeonsever < '"
					+ ProjectUtils.getTodayStrInClientsTimezone(null)
					+ "'    ;";
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				return rs.getDouble("ydaycost");
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return 0.0;

	}

	private static Double getTodayPhonecost(String db) {

		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			String query = "select sum(totalCost) todaycost from `" + db
					+ "`.calls  where calllandingtimeonsever > '"
					+ ProjectUtils.getTodayStrInClientsTimezone(null) + "' ;";
			rs = stmt.executeQuery(query);

			if (rs.next()) {

				return rs.getDouble("todaycost");
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return 0.0;

	}

	private static Double getTotalCallCost(String db) {
		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			String query = "select sum(totalCost) cost from `" + db
					+ "`.calls   ;";

			// String query =
			// "select sum(totalCost) cost from `"+db+"`.calls  where calllandingtimeonsever<'2017-08-31 00:00:00' ;";
			rs = stmt.executeQuery(query);
			System.out.println(query);

			if (rs.next()) {

				return rs.getDouble("cost");
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
			//

			ProjectUtils.closeConnection(rs, stmt, conn);

		}

		return 0.0;

	}

}
