package com.triyasoft.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdcashZoneTracker {

	public static Connection conn = null;

	public static void trackRequest(String ipAddress, String zoneid,
			String campaignid, String url, String referer, String os,
			String browser, String country, String region, String city,
			String isp) {

		PreparedStatement stmt = null;
		Connection conn = null;
		try {

			conn = ProjectUtils.getMySQLConnection();
			stmt = conn
					.prepareStatement("INSERT INTO adcash_tracking (zoneid,url,campaign_id,ipaddress,referer_url,browser,os,country,region,city,isp)  VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			int counter = 1;
			stmt.setString(counter++, zoneid);
			stmt.setString(counter++, url);
			stmt.setString(counter++, campaignid);
			stmt.setString(counter++, ipAddress);
			stmt.setString(counter++, referer);
			stmt.setString(counter++, browser);
			stmt.setString(counter++, os);
			stmt.setString(counter++, country);
			stmt.setString(counter++, region);
			stmt.setString(counter++, city);
			stmt.setString(counter++, isp);

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

}
