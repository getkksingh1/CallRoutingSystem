package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.triyasoft.utils.ProjectUtils;

public class AuditDao {

	public static void auditHttpRequest(String audit_type,
			HttpServletRequest request) {

		Enumeration<String> parameterNames = request.getParameterNames();

		StringBuffer stringBuffer = new StringBuffer();
		String ipAddress = request.getRemoteAddr();
		stringBuffer.append("ipaddress=" + ipAddress + "&");
		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			stringBuffer.append(paramName + "=");
			String paramValue = request.getParameter(paramName);
			stringBuffer.append(paramValue + "&");

		}

		auditRecord(audit_type, stringBuffer.toString());

	}

	public static void auditRecord(String audit_type, String audit_value) {

		PreparedStatement stmt = null;
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn
					.prepareStatement("INSERT INTO audit_log ( audit_type,audit_value)  VALUES (?,?)");
			int counter = 1;

			stmt.setString(counter++, audit_type);
			stmt.setString(counter++, audit_value);

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

	public static void auditPlivoSignature(String uuid, String uri,
			String plivoSignature, String mySignatureHash) {

		PreparedStatement stmt = null;
		Connection conn = ProjectUtils.getMySQLConnection();

		try {

			stmt = conn
					.prepareStatement("INSERT INTO plivosignature  ( uuid, uri, plivosignature, myignaturehash)  VALUES (?,?,?,?)");
			int counter = 1;

			stmt.setString(counter++, uuid);
			stmt.setString(counter++, uri);
			stmt.setString(counter++, plivoSignature);
			stmt.setString(counter++, mySignatureHash);

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
