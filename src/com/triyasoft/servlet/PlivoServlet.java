package com.triyasoft.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.response.Record;
import com.plivo.helper.exception.PlivoException;
import com.plivo.helper.xml.elements.Dial;
import com.plivo.helper.xml.elements.Number;
import com.plivo.helper.xml.elements.PlivoResponse;
import com.triyasoft.adapters.PlivoRequestAdapter;
import com.triyasoft.daos.AuditDao;
import com.triyasoft.model.CallModel;
import com.triyasoft.plivo.XPlivoSignature;
import com.triyasoft.utils.CallRoutingService;

@WebServlet("/plivo")
public class PlivoServlet extends HttpServlet {

	// public static String nonBussinessHours =
	// "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response><Speak voice='WOMAN'>At this time all of our offices are closed, please call back during normal operating hours 7 days a week. Thanks for calling, goodbye.</Speak><Wait length='5'/><Hangup/></Response> ";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		if ("reloaddata".equals(requestType)) {

			long startTime = System.currentTimeMillis();

			CallRoutingService.loadAlldata(true);

			long endTime = System.currentTimeMillis();

			response.getWriter().println(
					"Loaded Buyers: "
							+ CallRoutingService.cachedBuyersList.size());
			response.getWriter().println(
					"Loaded PhoneNumbers: "
							+ CallRoutingService.cachedPhoneNumbersMap.size());
			response.getWriter()
					.println(
							"Loaded Sources: "
									+ CallRoutingService.cachedTrafficsourcesMap
											.size());

			response.getWriter().println(
					"Time Taken to reload the data: " + (endTime - startTime)
							+ " millliseconds");

		}

		if ("message".equals(requestType)) {

			AuditDao.auditHttpRequest("PlivoMessageRequest", request);

		}

		if ("answer".equals(requestType)) {

			boolean isSipCall = false;
			AuditDao.auditHttpRequest("PlivoAnswerRequest", request);
			XPlivoSignature.auditPlivoSignature(request, "?requestType=answer");

			long startTime = System.currentTimeMillis();

			CallModel callModel = null;
			PlivoResponse plivoResponse = new PlivoResponse();
			try {

				String fromNumber = request.getParameter("From");
				if (fromNumber != null && fromNumber.startsWith("sip:")) {
					// this is a sip call handle differently
					isSipCall = true;
				} else {
					PlivoRequestAdapter plivoRequestAdapter = new PlivoRequestAdapter();
					callModel = plivoRequestAdapter
							.createCallModelAdapterAtAnswer(request);

					callModel = CallRoutingService.createCallModel(callModel);
				}

			}

			catch (Exception e) {

				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				AuditDao.auditRecord("PlivAnswerException", errors.toString());
				response.getWriter()
						.print("<Response><Hangup reason=\"rejected\"  /> </Response>");
				return;

			}

			if (!isSipCall && callModel.getError_code() != null) {

				if ("E005".equals(callModel.getError_code())) {
					AuditDao.auditRecord("PlivAnswerError", callModel.getUuid()
							+ " :::::" + callModel.getError_code() + "::::: "
							+ callModel.getError_description());

					// AuditDao.auditRecord("PlivAnswerNonBussinessHours",
					// callModel.getUuid()+" :::::"+callModel.getError_code()+"::::: "+callModel.getError_description());
					response.getWriter()
							.print("<Response><Hangup reason=\"rejected\"  /> </Response>");
					return;

				} else {
					AuditDao.auditRecord("PlivAnswerError", callModel.getUuid()
							+ " :::::" + callModel.getError_code() + "::::: "
							+ callModel.getError_description());
					response.getWriter()
							.print("<Response><Hangup reason=\"rejected\"  /> </Response>");
					return;
				}

			}

			if (!isSipCall) {

				String buyerNumber = callModel.getConnected_to();

				String fromNumber = request.getParameter("From");
				Dial dial = new Dial();
				dial.setCallerId(fromNumber);
				Number num = new Number(buyerNumber);
				try {
					dial.append(num);
					plivoResponse.append(dial);
					response.addHeader("Content-Type", "text/xml");
					String xmlResponse = plivoResponse.toXML();
					callModel.setXmlResponse(xmlResponse);
					response.getWriter().print(xmlResponse);
					callModel.setRecording_url(recordCall(callModel.getUuid()));

				} catch (PlivoException e) {
					e.printStackTrace();
				}

				long endTime = System.currentTimeMillis();
				callModel.setCallRoutingTime(endTime - startTime);
			}

			else {

				String buyerNumber = request.getParameter("To");

				// String fromNumber = request.getParameter("From");

				Dial dial = new Dial();
				dial.setCallerId("18883084905");
				Number num = new Number(buyerNumber);
				try {
					dial.append(num);
					plivoResponse.append(dial);
					response.addHeader("Content-Type", "text/xml");
					String xmlResponse = plivoResponse.toXML();
					// callModel.setXmlResponse(xmlResponse);
					response.getWriter().print(xmlResponse);
					;
				} catch (PlivoException e) {
					e.printStackTrace();
				}

				long endTime = System.currentTimeMillis();
				// callModel.setCallRoutingTime(endTime-startTime);

			}

		}

		if ("hangup".equals(requestType)) {
			AuditDao.auditHttpRequest("PlivoHangupRequest", request);
			XPlivoSignature.auditPlivoSignature(request, "?requestType=hangup");

			try {

				PlivoRequestAdapter plivoRequestAdapter = new PlivoRequestAdapter();
				CallModel callModel = plivoRequestAdapter
						.createCallModelAdapterAtHangup(request);
				CallRoutingService.updateCallModel(callModel);
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				AuditDao.auditRecord("PlivAnswerException", errors.toString());
				return;

			}
		}

	}

	private String recordCall(String uuid) {

		String auth_id = "MAODJKN2MZOGE4OTNHNJ";
		String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";
		RestAPI api1 = new RestAPI(auth_id, auth_token, "v1");
		LinkedHashMap<String, String> parameters1 = new LinkedHashMap<String, String>();
		parameters1.put("call_uuid", uuid); // ID of the call
		parameters1.put("time_limit", "7200"); // Max recording duration in
												// seconds

		try {
			Record resp1 = api1.record(parameters1);
			System.out.println(resp1.api_id);
			System.out.println(resp1.message);
			System.out.println(resp1.url);

			System.out.println(resp1.error);
			System.out.println(resp1.serverCode);
			return resp1.url;

		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return "";
	}

	public static void main(String[] args) {

	}

}
