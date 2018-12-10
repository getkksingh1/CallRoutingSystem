package com.triyasoft.trackdrive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.triyasoft.model.Buyer;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.model.TrafficSource;

public class TrackDriverDBUtility {

	public static Buyer addBuyerViaDirectDBConnection(Buyer buyer,
			Connection conn) {

		PreparedStatement stmt = null;

		try {

			stmt = conn
					.prepareStatement("INSERT INTO Buyers ( buyer_name,buyer_number,weight,tier,concurrency_cap_limit,bid_price,is_active,buyer_daily_cap)  VALUES (?,?,?,?,?,?,?,?)");
			int counter = 1;

			stmt.setString(counter++, buyer.getBuyer_name());
			stmt.setString(counter++, buyer.getBuyer_number());
			stmt.setInt(counter++, buyer.getWeight());
			stmt.setInt(counter++, buyer.getTier());
			stmt.setInt(counter++, buyer.getConcurrency_cap_limit());
			stmt.setDouble(counter++, buyer.getBid_price());
			stmt.setInt(counter++, buyer.getIs_active());
			stmt.setInt(counter++, buyer.getBuyer_daily_cap());

			int buyer_id = stmt.executeUpdate();
			buyer.setBuyer_id(buyer_id);

		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		return buyer;

	}

	public static PhoneNumber addPhoneNumberVidDirecDBConnection(
			PhoneNumber phoneNumber, Connection conn) {

		PreparedStatement stmt = null;

		try {

			stmt = conn
					.prepareStatement("INSERT INTO phonenumbers ( number,traffic_source_id,is_active,costpercall)  VALUES (?,?,?,?)");
			int counter = 1;

			stmt.setString(counter++, phoneNumber.getNumber());
			stmt.setInt(counter++, phoneNumber.getTraffic_source_id());
			stmt.setInt(counter++, phoneNumber.getIs_Active());
			stmt.setDouble(counter++, phoneNumber.getCostpercall());

			int sourceId = stmt.executeUpdate();
			phoneNumber.setId(sourceId);

		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		return phoneNumber;

	}

	public static TrafficSource addTrafficSourceViaDirectDBConnection(
			TrafficSource trafficSource, Connection conn) {

		PreparedStatement stmt = null;

		try {

			stmt = conn
					.prepareStatement("INSERT INTO trafficsources (id, first_name,last_name,is_active)  VALUES (?, ?,?,?)");
			int counter = 1;

			stmt.setInt(counter++, trafficSource.getId());
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
		} finally {

		}

		return trafficSource;

	}
}
