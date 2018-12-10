package com.triyasoft.model;

import java.util.Date;

public class CallModel {

	private int id;

	private String uuid;
	private String recording_url;
	private String number_called;
	private String connected_to;
	private String caller_number;
	private int traffic_source_id;
	private String traffic_source;
	private String buyer;
	private int buyer_id;

	private String callerName;
	private String callStatusAtAnswer;
	private String answerEvent;
	private String billRate;
	private String xmlResponse;
	private String direction;

	private String totalCost;
	private String hangupCause;
	private String billDuration;
	private Date startTime;
	private String duration;
	private Date endTime;
	private Date answerTime;
	private Date callLandingTimeOnServer;

	private String hangupEvent;
	private String callStatusAtHangup;

	private int is_running;

	private String error_code;
	private String error_description;
	private String exception;

	private PhoneNumber source_phoneNumber;
	private Buyer call_buyer;
	private TrafficSource call_from_source;

	private double callProfit;

	private long callRoutingTime;

	private double buyer_revenue = 0;
	private double traffic_source_revenue = 0;

	private String dateInEST;

	private String endDateInEST;

	private String city;
	private String state;
	private String country;
	private String phoneProvider;
	private String latitue;
	private String longitude;

	private int callattempts;
	private int buyer_connecting_time;
	private int holdtime;
	private int connected_time;
	private String buyers_tried;
	private String buyer_hangup_reason;

	private StringBuffer routingLogic = new StringBuffer();;

	private int org_id;
	private String orgnization_name;

	private boolean isBuyerAndCostumerConnected = false;

	private String no_connect_cause;

	public String getNo_connect_cause() {
		return no_connect_cause;
	}

	public void setNo_connect_cause(String no_connect_cause) {
		this.no_connect_cause = no_connect_cause;
	}

	public boolean isBuyerAndCostumerConnected() {
		return isBuyerAndCostumerConnected;
	}

	public void setBuyerAndCostumerConnected(boolean isBuyerAndCostumerConnected) {
		this.isBuyerAndCostumerConnected = isBuyerAndCostumerConnected;
	}

	public int getOrg_id() {
		return org_id;
	}

	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}

	public String getOrgnization_name() {
		return orgnization_name;
	}

	public void setOrgnization_name(String orgnization_name) {
		this.orgnization_name = orgnization_name;
	}

	public StringBuffer getRoutingLogic() {
		return routingLogic;
	}

	public void setRoutingLogic(StringBuffer routingLogic) {
		this.routingLogic = routingLogic;
	}

	public int getBuyer_connecting_time() {
		return buyer_connecting_time;
	}

	public void setBuyer_connecting_time(int buyer_connecting_time) {
		this.buyer_connecting_time = buyer_connecting_time;
	}

	public int getCallattempts() {
		return callattempts;
	}

	public void setCallattempts(int callattempts) {
		this.callattempts = callattempts;
	}

	public int getHoldtime() {
		return holdtime;
	}

	public void setHoldtime(int holdtime) {
		this.holdtime = holdtime;
	}

	public int getConnected_time() {
		return connected_time;
	}

	public void setConnected_time(int connected_time) {
		this.connected_time = connected_time;
	}

	public String getBuyers_tried() {
		return buyers_tried;
	}

	public void setBuyers_tried(String buyers_tried) {
		this.buyers_tried = buyers_tried;
	}

	public String getBuyer_hangup_reason() {
		return buyer_hangup_reason;
	}

	public void setBuyer_hangup_reason(String buyer_hangup_reason) {
		this.buyer_hangup_reason = buyer_hangup_reason;
	}

	public double getCallProfit() {
		return callProfit;
	}

	public void setCallProfit(double callProfit) {
		this.callProfit = callProfit;
	}

	public String getEndDateInEST() {
		return endDateInEST;
	}

	public void setEndDateInEST(String endDateInEST) {
		this.endDateInEST = endDateInEST;
	}

	public String getDateInEST() {
		return dateInEST;
	}

	public void setDateInEST(String dateInEST) {
		this.dateInEST = dateInEST;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRecording_url() {
		return recording_url;
	}

	public void setRecording_url(String recording_url) {
		this.recording_url = recording_url;
	}

	public String getNumber_called() {
		return number_called;
	}

	public void setNumber_called(String number_called) {
		this.number_called = number_called;
	}

	public String getConnected_to() {
		return connected_to;
	}

	public void setConnected_to(String connected_to) {
		this.connected_to = connected_to;
	}

	public String getCaller_number() {
		return caller_number;
	}

	public void setCaller_number(String caller_number) {
		this.caller_number = caller_number;
	}

	public int getTraffic_source_id() {
		return traffic_source_id;
	}

	public void setTraffic_source_id(int traffic_source_id) {
		this.traffic_source_id = traffic_source_id;
	}

	public String getTraffic_source() {
		return traffic_source;
	}

	public void setTraffic_source(String traffic_source) {
		this.traffic_source = traffic_source;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public int getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getCallerName() {
		return callerName;
	}

	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}

	public String getAnswerEvent() {
		return answerEvent;
	}

	public void setAnswerEvent(String answerEvent) {
		this.answerEvent = answerEvent;
	}

	public String getBillRate() {
		return billRate;
	}

	public void setBillRate(String billRate) {
		this.billRate = billRate;
	}

	public String getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(String xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getHangupCause() {
		return hangupCause;
	}

	public void setHangupCause(String hangupCause) {
		this.hangupCause = hangupCause;
	}

	public String getBillDuration() {
		return billDuration;
	}

	public void setBillDuration(String billDuration) {
		this.billDuration = billDuration;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getHangupEvent() {
		return hangupEvent;
	}

	public void setHangupEvent(String hangupEvent) {
		this.hangupEvent = hangupEvent;
	}

	public String getCallStatusAtAnswer() {
		return callStatusAtAnswer;
	}

	public void setCallStatusAtAnswer(String callStatusAtAnswer) {
		this.callStatusAtAnswer = callStatusAtAnswer;
	}

	public String getCallStatusAtHangup() {
		return callStatusAtHangup;
	}

	public void setCallStatusAtHangup(String callStatusAtHangup) {
		this.callStatusAtHangup = callStatusAtHangup;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIs_running() {
		return is_running;
	}

	public void setIs_running(int is_running) {
		this.is_running = is_running;
	}

	public PhoneNumber getSource_phoneNumber() {
		return source_phoneNumber;
	}

	public void setSource_phoneNumber(PhoneNumber source_phoneNumber) {
		this.source_phoneNumber = source_phoneNumber;
	}

	public Buyer getCall_buyer() {
		return call_buyer;
	}

	public void setCall_buyer(Buyer call_buyer) {
		this.call_buyer = call_buyer;
	}

	public TrafficSource getCall_from_source() {
		return call_from_source;
	}

	public void setCall_from_source(TrafficSource call_from_source) {
		this.call_from_source = call_from_source;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public Date getCallLandingTimeOnServer() {
		return callLandingTimeOnServer;
	}

	public void setCallLandingTimeOnServer(Date callLandingTimeOnServer) {
		this.callLandingTimeOnServer = callLandingTimeOnServer;
	}

	public long getCallRoutingTime() {
		return callRoutingTime;
	}

	public void setCallRoutingTime(long callRoutingTime) {
		this.callRoutingTime = callRoutingTime;
	}

	public double getBuyer_revenue() {
		return buyer_revenue;
	}

	public void setBuyer_revenue(double buyer_revenue) {
		this.buyer_revenue = buyer_revenue;
	}

	public double getTraffic_source_revenue() {
		return traffic_source_revenue;
	}

	public void setTraffic_source_revenue(double traffic_source_revenue) {
		this.traffic_source_revenue = traffic_source_revenue;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoneProvider() {
		return phoneProvider;
	}

	public void setPhoneProvider(String phoneProvider) {
		this.phoneProvider = phoneProvider;
	}

	public String getLatitue() {
		return latitue;
	}

	public void setLatitue(String latitue) {
		this.latitue = latitue;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
