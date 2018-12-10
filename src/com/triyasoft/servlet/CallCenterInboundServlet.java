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
import com.triyasoft.daos.CallcenterInboundCallsDao;
import com.triyasoft.model.CallcenterInboundCall;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.CallcenterCallAllocationService;

@WebServlet("/callcenterinbound")
public class CallCenterInboundServlet extends HttpServlet {

	public static String ringback_audio_location = "https://s3-ap-southeast-1.amazonaws.com/triya/ringback_tone.wav";
	public static String nonBussinessHours = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> <Response><Speak voice='WOMAN'>At this time all of our offices are closed, please call back during normal operating hours 7 days a week. Thanks for calling, goodbye.</Speak><Wait length='5'/><Hangup/></Response> ";
	public static String ampersandEscape = "&amp;";

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		// Inbound Calls on 1 844-734-1029

		if ("answer".equals(requestType)) {
			System.out
					.println("Getting Inside CallCenterInboundServlet answer ");
			printRequestparameters(request);

			AuditDao.auditHttpRequest("CallCenterInboundServletAnswer", request);

			String fromNumber = request.getParameter("From");

			String sipEndPointtoCall = CallcenterCallAllocationService
					.getAllocatedEndPoint();

			if (sipEndPointtoCall == null) {
				response.addHeader("Content-Type", "text/xml");
				response.getWriter()
						.print("<Response><Hangup reason=\"rejected\"  /> </Response>");
				return;
			}

			CallcenterCallAllocationService
					.markExtensionBusy(sipEndPointtoCall);

			String from = request.getParameter("From");
			String to = request.getParameter("To");
			String uuid = request.getParameter("CallUUID");
			String callerName = request.getParameter("CallerName");

			CallcenterInboundCall callcenterInboundCall = new CallcenterInboundCall();

			callcenterInboundCall.setUuid(uuid);
			callcenterInboundCall.setCallerName(callerName);
			callcenterInboundCall.setTosipendpoint(sipEndPointtoCall);
			callcenterInboundCall.setIsrunning(1);
			callcenterInboundCall.setCallednumber(to);
			callcenterInboundCall.setCustomernumber(from);
			callcenterInboundCall.setCallstarttime(new Date());

			CallcenterCallAllocationService.runningInboundCalls
					.add(callcenterInboundCall);
			CallcenterCallAllocationService.runningInboundCallsMap.put(uuid,
					callcenterInboundCall);

			CallcenterInboundCallsDao.createCall(callcenterInboundCall);

			String responseToPlivo = "<Response><Dial callerId=\"" + fromNumber
					+ "\"><User>" + sipEndPointtoCall
					+ "</User></Dial></Response>";
			response.addHeader("Content-Type", "text/xml");
			response.getWriter().println(responseToPlivo);

		}

		if ("hangup".equals(requestType)) {
			System.out
					.println("Getting Inside CallCenterInboundServlet hangup ");
			printRequestparameters(request);
			AuditDao.auditHttpRequest("CallCenterInboundServletHangup", request);

			String uuid = request.getParameter("CallUUID");

			CallcenterInboundCall callcenterInboundCall = CallcenterCallAllocationService.runningInboundCallsMap
					.get(uuid);

			String sipUrl = callcenterInboundCall.getTosipendpoint();
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

			CallcenterInboundCallsDao.updateInboundCall(totalCost,
					billDuration, billRate, duration, hangupCause,
					callStatusAtHangup, uuid);

			CallcenterCallAllocationService.runningInboundCalls
					.remove(callcenterInboundCall);
			CallcenterCallAllocationService.runningInboundCallsMap.remove(uuid);

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
