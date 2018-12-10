package com.triyasoft.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.AuditDao;
import com.triyasoft.daos.CallcenterOutboundCallsDao;
import com.triyasoft.model.CallcenterOutboundCall;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.CallcenterCallAllocationService;

@WebServlet("/callcenteroutbound")
public class CallCenterOutboundServlet extends HttpServlet {

	public static String ringback_audio_location = "https://s3-ap-southeast-1.amazonaws.com/triya/ringback_tone.wav";
	public static String nonBussinessHours = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> <Response><Speak voice='WOMAN'>At this time all of our offices are closed, please call back during normal operating hours 7 days a week. Thanks for calling, goodbye.</Speak><Wait length='5'/><Hangup/></Response> ";
	public static String ampersandEscape = "&amp;";
	public static String callerId = "18447341029";

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		// Inbound Calls on 18447341029

		if ("answer".equals(requestType)) {

			String callerIDNumber = request.getParameter("callerId");

			if (callerIDNumber != null && callerIDNumber.trim().length() > 0) {
				callerId = callerIDNumber.trim();
				response.getWriter().println("Setting caller id = " + callerId);
				return;

			}

			String from = request.getParameter("From");
			String to = request.getParameter("To");
			String uuid = request.getParameter("CallUUID");
			String callerName = request.getParameter("CallerName");
			CallcenterOutboundCall callcenterOutboundCall = new CallcenterOutboundCall();
			CallcenterCallAllocationService.markExtensionBusy(from);

			callcenterOutboundCall.setFromsipendpoint(from);
			callcenterOutboundCall.setUuid(uuid);
			callcenterOutboundCall.setTonumber(to);
			callcenterOutboundCall.setCallername(callerName);
			callcenterOutboundCall.setIsrunning(1);
			callcenterOutboundCall.setCallstarttime(new Date());

			CallcenterOutboundCallsDao.createCall(callcenterOutboundCall);

			String xmlResponse = "<Response>  <Dial callerId=\"" + callerId
					+ "\"><Number>" + to + "</Number> </Dial> </Response>";

			System.out
					.println("Getting Inside CallCenterOutboundServlet answer ");
			printRequestparameters(request);

			AuditDao.auditHttpRequest("CallCenterOutboundServletAnswer",
					request);
			response.addHeader("Content-Type", "text/xml");
			response.getWriter().println(xmlResponse);

		}

		if ("hangup".equals(requestType)) {
			AuditDao.auditHttpRequest("CallCenterOnboundServletHangup", request);
			System.out
					.println("Getting Inside CallCenterOutboundServlet Hangup ");

			printRequestparameters(request);

			AuditDao.auditHttpRequest("CallCenterOutboundServletHangup",
					request);

			String sipUrl = request.getParameter("From");
			CallcenterCallAllocationService.markExtensionFree(sipUrl);

			String totalCost = request.getParameter("TotalCost");
			String hangupCause = request.getParameter("HangupCause"); // Add to
																		// DB

			String billDuration = request.getParameter("BillDuration");
			String billRate = request.getParameter("BillRate");
			String duration = request.getParameter("Duration");

			String callStatusAtHangup = request.getParameter("CallStatus");// Add
																			// to
																			// DB
			String uuid = request.getParameter("CallUUID");

			CallcenterOutboundCallsDao.updateOutboundCall(totalCost,
					billDuration, billRate, duration, hangupCause,
					callStatusAtHangup, uuid);

			response.addHeader("Content-Type", "text/xml");
			response.getWriter().println(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response/>");

		}

		if ("reloaddata".equals(requestType)) {

			long startTime = System.currentTimeMillis();

			CallRoutingServiceV2.loadAlldata(true);

			long endTime = System.currentTimeMillis();

			response.getWriter().println(
					"Loaded Buyers: "
							+ CallRoutingServiceV2.cachedBuyersList.size());
			response.getWriter()
					.println(
							"Loaded PhoneNumbers: "
									+ CallRoutingServiceV2.cachedPhoneNumbersMap
											.size());
			response.getWriter().println(
					"Loaded Sources: "
							+ CallRoutingServiceV2.cachedTrafficsourcesMap
									.size());

			response.getWriter().println(
					"Time Taken to reload the data: " + (endTime - startTime)
							+ " millliseconds");

		}

	}

	public static void printRequestparameters(HttpServletRequest request) {

		Enumeration<String> parameterNames = request.getParameterNames();

		StringBuffer stringBuffer = new StringBuffer();
		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			stringBuffer.append(paramName + "=");
			String paramValue = request.getParameter(paramName);
			stringBuffer.append(paramValue + "&");

		}

		System.out.println(stringBuffer);
		System.out.println("-------------------------");
	}

	public static void main(String[] args) {

	}

}
