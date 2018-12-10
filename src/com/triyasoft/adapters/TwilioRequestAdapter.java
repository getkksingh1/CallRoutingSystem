package com.triyasoft.adapters;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.exceptions.CallModelNotInMapException;
import com.triyasoft.model.CallModel;
import com.triyasoft.utils.CallRoutingServiceV2;

public class TwilioRequestAdapter {

	public CallModel createCallModelAdapterAtAnswer(HttpServletRequest request) {

		CallModel callModel = new CallModel();

		String uuid = request.getParameter("CallSid");
		String callStatus = request.getParameter("CallStatus");
		String direction = request.getParameter("Direction");
		String event = "NA"; // request.getParameter("Event");
		String billRate = "NA"; // request.getParameter("BillRate");
		String toNumber = request.getParameter("To");
		String fromNumber = request.getParameter("From");
		String callerName = request.getParameter("Caller");

		callModel.setIs_running(0);
		callModel.setCallStatusAtAnswer(callStatus);
		callModel.setAnswerEvent(event);
		callModel.setBillRate(billRate);
		callModel.setXmlResponse("XMLTobeFilled");
		callModel.setDirection(direction);
		callModel.setUuid(uuid);
		callModel.setCallerName(callerName);

		toNumber = toNumber.replace("+", "").replaceAll(" ", "")
				.replaceAll("-", "");
		callModel.setNumber_called(toNumber);

		fromNumber = fromNumber.replace("+", "").replaceAll(" ", "")
				.replaceAll("-", "");
		callModel.setCaller_number(fromNumber);

		return callModel;

	}

	public CallModel createCallModelAdapterAtHangup(HttpServletRequest request)
			throws CallModelNotInMapException {

		double outboundCostPerMinute = 0.013;
		double inboundCostPerMinute = 0.022;

		String callUUID = request.getParameter("CallSid");
		String duration = request.getParameter("CallDuration");
		String hangupCause = request.getParameter("CallStatus");
		String hangupCallStatus = request.getParameter("CallStatus");
		String hangupEvent = "NA";// request.getParameter("Event");

		String totalCost = "";
		String billDuration = "";

		if (duration != null) {
			int billingMinutes = (((Integer.parseInt(duration)) / 60) + 1);
			double phoneCost = (inboundCostPerMinute + outboundCostPerMinute)
					* billingMinutes;
			totalCost = phoneCost + "";
			billDuration = (billingMinutes * 60) + "";
		}

		if (!CallRoutingServiceV2.isdataLoaded) {
			CallRoutingServiceV2.loadAlldata(false);
		}

		CallModel callModel = CallRoutingServiceV2.runningCallsMapByUUID
				.get(callUUID);

		if (callModel == null) {
			callModel = CallsDaoService.getCallByUUID(callUUID);
			if (callModel == null)
				throw new CallModelNotInMapException(
						callUUID
								+ " :::CallUUID can not be found in runningCallsMapByUUID and database both");
		}

		callModel.setTotalCost(totalCost);
		callModel.setHangupCause(hangupCause);
		callModel.setBillDuration(billDuration);
		callModel.setStartTime(callModel.getCallLandingTimeOnServer());
		callModel.setEndTime(new Date());
		callModel.setAnswerTime(callModel.getCallLandingTimeOnServer());
		callModel.setDuration(duration);
		callModel.setCallStatusAtHangup(hangupCallStatus);
		callModel.setHangupEvent(hangupEvent);

		callModel.setIs_running(0);

		return callModel;
	}

}
