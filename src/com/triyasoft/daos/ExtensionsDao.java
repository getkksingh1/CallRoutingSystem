package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.triyasoft.model.Buyer;
import com.triyasoft.model.PlivoEndpointExtention;
import com.triyasoft.utils.ProjectUtils;

public class ExtensionsDao {

	public static void addExtension(
			PlivoEndpointExtention plivoEndpointExtention) {

		Connection conn = ProjectUtils.getMySQLConnection();
		PreparedStatement stmt = null;

		try {

			stmt = conn
					.prepareStatement("INSERT INTO extensions_endpoint_mapping (extension_name,sip_url,plivo_endpoint_id,plivo_endpoint_user_id,"
							+ "plivo_endpoint_user_name,plivo_endpoint_user_password,plivo_endpoint_user_alias,plivo_end_point_domain,plivo_endpoint_application_id,"
							+ "plivo_endpoint_application_name, isactive)  VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			int counter = 1;

			stmt.setString(counter++,
					plivoEndpointExtention.getExtension_name());
			stmt.setString(counter++, plivoEndpointExtention.getSip_url());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_id());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_user_id());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_user_name());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_user_password());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_user_alias());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_end_point_domain());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_application_id());
			stmt.setString(counter++,
					plivoEndpointExtention.getPlivo_endpoint_application_name());
			stmt.setInt(counter++, plivoEndpointExtention.getIsactive());

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

	public static List<PlivoEndpointExtention> loadAllExtensions() {

		List<PlivoEndpointExtention> extensions = new ArrayList<PlivoEndpointExtention>();

		Connection conn = ProjectUtils.getMySQLConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {

			stmt = conn.createStatement();

			rs = stmt
					.executeQuery("select * from extensions_endpoint_mapping ; ");

			while (rs.next()) {
				PlivoEndpointExtention extension = new PlivoEndpointExtention();
				extension.setId(rs.getInt("id"));
				extension.setExtension_number((rs.getInt("id") + 200) + "");
				extension.setExtension_name(rs.getString("extension_name"));
				extension.setSip_url(rs.getString("sip_url"));
				extension.setPlivo_endpoint_id(rs
						.getString("plivo_endpoint_id"));
				extension.setPlivo_endpoint_user_id(rs
						.getString("plivo_endpoint_user_id"));
				extension.setPlivo_endpoint_user_name(rs
						.getString("plivo_endpoint_user_name"));
				extension.setPlivo_endpoint_user_password(rs
						.getString("plivo_endpoint_user_password"));
				extension.setPlivo_endpoint_user_alias(rs
						.getString("plivo_endpoint_user_alias"));
				extension.setPlivo_end_point_domain(rs
						.getString("plivo_end_point_domain"));
				extension.setPlivo_endpoint_application_id(rs
						.getString("plivo_endpoint_application_id"));
				extension.setPlivo_endpoint_application_name(rs
						.getString("plivo_endpoint_application_name"));
				extension.setIsactive(rs.getInt("isactive"));
				extensions.add(extension);
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
			ProjectUtils.closeConnection(null, stmt, conn);
		}

		return extensions;
	}

	public static String getPlivoEndpointId(String id) {

		Statement stmt = null;
		ResultSet rs = null;

		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			rs = stmt
					.executeQuery("select plivo_endpoint_id from extensions_endpoint_mapping where id = '"
							+ id + "';");

			if (rs.next()) {

				String plivo_endpoint_id = rs.getString("plivo_endpoint_id");
				return plivo_endpoint_id;

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
		return null;

	}

	public static void deleteExtension(String id) {
		Statement stmt = null;
		ResultSet rs = null;
		Connection connection = ProjectUtils.getMySQLConnection();

		try {

			stmt = connection.createStatement();
			String sql = "DELETE FROM extensions_endpoint_mapping "
					+ "WHERE id = '" + id + "';";

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
			ProjectUtils.closeConnection(rs, stmt, connection);
		}
	}

}
