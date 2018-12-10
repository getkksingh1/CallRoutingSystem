package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.triyasoft.utils.ProjectUtils;

public class ConferenceDao {

	public static int getNextConferenceID() {

		PreparedStatement stmt = null;
		Connection conn = ProjectUtils.getMySQLConnection();
		String uuid = "";

		try {

			uuid = String.valueOf(UUID.randomUUID());
			stmt = conn
					.prepareStatement("insert into conference (row_key_uuid) values (?)");
			int counter = 1;

			stmt.setString(counter++, uuid);
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

		return findIdByUUID(uuid);

	}

	public static int findIdByUUID(String uuid) {

		Statement stmt = null;
		ResultSet rs = null;
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn.createStatement();

			rs = stmt
					.executeQuery("select * from conference where row_key_uuid='"
							+ uuid + "';");

			while (rs.next()) {

				return rs.getInt("id");

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

		return -1;
	}

}
