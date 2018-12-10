package com.triyasoft.adapters;

import javax.servlet.http.HttpServletRequest;

import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.exceptions.CallModelNotInMapException;
import com.triyasoft.model.CallModel;
import com.triyasoft.utils.CallRoutingServiceV2;

public class PlivoRequestAdapter {

	public static boolean isFlatBillingRate = AppSettingsDao
			.isFlatBillingRate();
	public static double flatBillingRate = AppSettingsDao.getFlatBillingRate();

	public CallModel createCallModelAdapterAtAnswer(HttpServletRequest request) {

		CallModel callModel = new CallModel();

		String toNumber = request.getParameter("To");
		String fromNumber = request.getParameter("From");
		String direction = request.getParameter("direction");
		String callerName = request.getParameter("CallerName");
		String billRate = request.getParameter("BillRate");
		String uuid = request.getParameter("CallUUID");
		String callStatus = request.getParameter("CallStatus");
		String event = request.getParameter("Event");

		callModel.setIs_running(0);
		callModel.setCallStatusAtAnswer(callStatus);
		callModel.setAnswerEvent(event);
		callModel.setBillRate(billRate);
		callModel.setXmlResponse("XMLTobeFilled");
		callModel.setDirection(direction);
		callModel.setUuid(uuid);
		callModel.setCallerName(callerName);
		callModel.setCaller_number(fromNumber);

		toNumber = toNumber.replace("+", "").replaceAll(" ", "")
				.replaceAll("-", "");
		callModel.setNumber_called(toNumber);

		return callModel;

	}

	public CallModel createCallModelAdapterAtHangup(HttpServletRequest request)
			throws CallModelNotInMapException {

		double outboundCostPerMinute = 0.003;

		String callUUID = request.getParameter("CallUUID");

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

		String totalCost = request.getParameter("TotalCost");
		String hangupCause = request.getParameter("HangupCause");
		String billDuration = request.getParameter("BillDuration");
		String startTime = request.getParameter("StartTime");
		String endTime = request.getParameter("EndTime");
		String answerTime = request.getParameter("AnswerTime");
		String duration = request.getParameter("Duration");
		String hangupCallStatus = request.getParameter("CallStatus");
		String hangupEvent = request.getParameter("Event");

		if (!isFlatBillingRate) {
			if (billDuration != null && totalCost != null
					&& billDuration.trim().length() > 0) {
				double numberOfMinutes = (Double.parseDouble(billDuration)) / 60;
				double totalCostDouble = Double.parseDouble(totalCost)
						+ (numberOfMinutes * outboundCostPerMinute);
				totalCost = totalCostDouble + "";
			}

		} else {

			double numberOfMinutes = (Double.parseDouble(billDuration)) / 60;
			totalCost = flatBillingRate * numberOfMinutes + "";
		}

		callModel.setTotalCost(totalCost);
		callModel.setHangupCause(hangupCause);
		callModel.setBillDuration(billDuration);
		callModel.setStartTime(CallRoutingServiceV2
				.convertPlivoDateToJavaDate(startTime));
		callModel.setEndTime(CallRoutingServiceV2
				.convertPlivoDateToJavaDate(endTime));
		callModel.setAnswerTime(CallRoutingServiceV2
				.convertPlivoDateToJavaDate(answerTime));

		callModel.setDuration(duration);
		callModel.setCallStatusAtHangup(hangupCallStatus);
		callModel.setHangupEvent(hangupEvent);

		callModel.setIs_running(0);

		return callModel;
	}

}
