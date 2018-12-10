package com.triyasoft.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plivo.helper.exception.PlivoException;
import com.plivo.helper.xml.elements.Dial;
import com.plivo.helper.xml.elements.Number;
import com.plivo.helper.xml.elements.PlivoResponse;
import com.triyasoft.daos.AuditDao;
import com.triyasoft.plivo.XPlivoSignature;
import com.triyasoft.utils.CallRoutingService;

@WebServlet("/plivosip")
public class PlivoSIPServlet extends HttpServlet {

	public static String nonBussinessHours = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response><Speak voice='WOMAN'>At this time all of our offices are closed, please call back during normal operating hours 7 days a week. Thanks for calling, goodbye.</Speak><Wait length='5'/><Hangup/></Response> ";

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
			AuditDao.auditHttpRequest("PlivoSipAnswerRequest", request);
			XPlivoSignature.auditPlivoSignature(request, "?requestType=answer");

			long startTime = System.currentTimeMillis();

			PlivoResponse plivoResponse = new PlivoResponse();

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

			// long endTime = System.currentTimeMillis();
			// callModel.setCallRoutingTime(endTime-startTime);

		}

		if ("hangup".equals(requestType)) {
			AuditDao.auditHttpRequest("PlivoSIPHangupRequest", request);
			XPlivoSignature.auditPlivoSignature(request, "?requestType=hangup");

		}

	}

	public static void main(String[] args) {

	}

}
