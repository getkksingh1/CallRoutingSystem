package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.triyasoft.model.Buyer;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.utils.AdcashZoneTracker;
import com.triyasoft.utils.ProjectUtils;

public class TrafficSourceDaoService {

	public static List<TrafficSource> loadAllSources() {

		Statement stmt = null;
		ResultSet rs = null;
		List<TrafficSource> trafficSources = new ArrayList<TrafficSource>();
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			rs = stmt.executeQuery("select * from trafficsources ; ");

			while (rs.next()) {

				TrafficSource trafficSource = new TrafficSource();
				Integer id = rs.getInt("id");
				trafficSource.setId(id);
				trafficSource.setFirst_name(rs.getString("first_name"));
				trafficSource.setLast_name(rs.getString("last_name"));
				trafficSource.setIs_Active(rs.getInt("is_active"));

				trafficSources.add(trafficSource);

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
		return trafficSources;
	}

	public static void deleteSource(String id) {

		Statement stmt = null;
		Connection connection = ProjectUtils.getMySQLConnection();

		try {

			stmt = connection.createStatement();
			String sql = "DELETE FROM trafficsources " + "WHERE id = '" + id
					+ "';";
			stmt.executeUpdate(sql);

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
			ProjectUtils.closeConnection(null, stmt, connection);
		}

	}

	public static void updateSource(String id, String first_name,
			String last_name, String is_active) {

		PreparedStatement stmt = null;
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			TrafficSource trafficSource = new TrafficSource();
			trafficSource.setId(Integer.parseInt(id));
			trafficSource.setFirst_name(first_name);
			trafficSource.setLast_name(last_name);
			trafficSource.setIs_Active(Integer.parseInt(is_active));

			stmt = conn
					.prepareStatement("UPDATE trafficsources SET first_name = ? , last_name = ? , is_active = ?  WHERE id = ?");

			int counter = 1;

			stmt.setString(counter++, trafficSource.getFirst_name());
			stmt.setString(counter++, trafficSource.getLast_name());
			stmt.setInt(counter++, trafficSource.getIs_Active());
			stmt.setInt(counter++, trafficSource.getId());

			stmt.executeUpdate();

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

			ProjectUtils.closeConnection(null, stmt, conn);
		}

	}

	public static TrafficSource addTrafficSource(String first_name,
			String last_name, String is_active) {

		TrafficSource trafficSource = new TrafficSource();

		Connection conn = ProjectUtils.getMySQLConnection();

		PreparedStatement stmt = null;

		try {

			trafficSource.setFirst_name(first_name);
			trafficSource.setLast_name(last_name);
			trafficSource.setIs_Active(Integer.parseInt(is_active));

			stmt = conn
					.prepareStatement("INSERT INTO trafficsources ( first_name,last_name,is_active)  VALUES (?,?,?)");
			int counter = 1;

			stmt.setString(counter++, trafficSource.getFirst_name());
			stmt.setString(counter++, trafficSource.getLast_name());
			stmt.setInt(counter++, trafficSource.getIs_Active());

			int sourceId = stmt.executeUpdate();
			trafficSource.setId(sourceId);

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
			ProjectUtils.closeConnection(null, stmt, conn);
		}

		return trafficSource;

	}

	public static List<TrafficSource> loadAllSources(String sortingParam) {

		Statement stmt = null;
		ResultSet rs = null;
		List<TrafficSource> trafficSources = new ArrayList<TrafficSource>();
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			if ("undefined".equals(sortingParam))
				return loadAllSources();

			sortingParam = sortingParam.replace("%20", "  ");

			stmt = conn.createStatement();

			rs = stmt.executeQuery("select * from trafficsources order by "
					+ sortingParam + " ; ");

			while (rs.next()) {

				TrafficSource trafficSource = new TrafficSource();
				Integer id = rs.getInt("id");
				trafficSource.setId(id);
				trafficSource.setFirst_name(rs.getString("first_name"));
				trafficSource.setLast_name(rs.getString("last_name"));
				trafficSource.setIs_Active(rs.getInt("is_active"));

				trafficSources.add(trafficSource);

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
		return trafficSources;

	}

}
