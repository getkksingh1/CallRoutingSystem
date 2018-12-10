package com.triyasoft.model;

import java.util.Date;

public class OutboundCallModel {

private int id;
private int conferencefirstmember;
private int conferencelastmember;
private int billduration;
private int duration;

private double billrate;
private double totalcost;


private String uuid;
private String parentuuid;
private String legBId;
private String conferenceID;
private String from;
private String to;
private String requestuuid;
private String calluuid;
private String aleguuid;
private String alegrequestuuid;
private String conferencememberid;
private String conferencename;
private String conferenceuuid;
private String hangupcause;
private String callstatus;
private String buyerName;

private Date ringtime;
private Date answertime;
private Date conferencestarttime;
private Date conferenceendtime;
private Date conferencingendedcallbacktime;
private Date outboundcallhanguptime;






public Date getOutboundcallhanguptime() {
	return outboundcallhanguptime;
}
public void setOutboundcallhanguptime(Date outboundcallhanguptime) {
	this.outboundcallhanguptime = outboundcallhanguptime;
}
public String getBuyerName() {
	return buyerName;
}
public void setBuyerName(String buyerName) {
	this.buyerName = buyerName;
}
public int getId() {
	
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getConferencefirstmember() {
	return conferencefirstmember;
}
public void setConferencefirstmember(int conferencefirstmember) {
	this.conferencefirstmember = conferencefirstmember;
}
public int getConferencelastmember() {
	return conferencelastmember;
}
public void setConferencelastmember(int conferencelastmember) {
	this.conferencelastmember = conferencelastmember;
}
public int getBillduration() {
	return billduration;
}
public void setBillduration(int billduration) {
	this.billduration = billduration;
}
public int getDuration() {
	return duration;
}
public void setDuration(int duration) {
	this.duration = duration;
}
public double getBillrate() {
	return billrate;
}
public void setBillrate(double billrate) {
	this.billrate = billrate;
}
public double getTotalcost() {
	return totalcost;
}
public void setTotalcost(double totalcost) {
	this.totalcost = totalcost;
}
public String getUuid() {
	return uuid;
}
public void setUuid(String uuid) {
	this.uuid = uuid;
}
public String getParentuuid() {
	return parentuuid;
}
public void setParentuuid(String parentuuid) {
	this.parentuuid = parentuuid;
}
public String getLegBId() {
	return legBId;
}
public void setLegBId(String legBId) {
	this.legBId = legBId;
}
public String getConferenceID() {
	return conferenceID;
}
public void setConferenceID(String conferenceID) {
	this.conferenceID = conferenceID;
}
public String getFrom() {
	return from;
}
public void setFrom(String from) {
	this.from = from;
}
public String getTo() {
	return to;
}
public void setTo(String to) {
	this.to = to;
}
public String getRequestuuid() {
	return requestuuid;
}
public void setRequestuuid(String requestuuid) {
	this.requestuuid = requestuuid;
}
public String getCalluuid() {
	return calluuid;
}
public void setCalluuid(String calluuid) {
	this.calluuid = calluuid;
}
public String getAleguuid() {
	return aleguuid;
}
public void setAleguuid(String aleguuid) {
	this.aleguuid = aleguuid;
}
public String getAlegrequestuuid() {
	return alegrequestuuid;
}
public void setAlegrequestuuid(String alegrequestuuid) {
	this.alegrequestuuid = alegrequestuuid;
}
public String getConferencememberid() {
	return conferencememberid;
}
public void setConferencememberid(String conferencememberid) {
	this.conferencememberid = conferencememberid;
}
public String getConferencename() {
	return conferencename;
}
public void setConferencename(String conferencename) {
	this.conferencename = conferencename;
}
public String getConferenceuuid() {
	return conferenceuuid;
}
public void setConferenceuuid(String conferenceuuid) {
	this.conferenceuuid = conferenceuuid;
}
public String getHangupcause() {
	return hangupcause;
}
public void setHangupcause(String hangupcause) {
	this.hangupcause = hangupcause;
}
public String getCallstatus() {
	return callstatus;
}
public void setCallstatus(String callstatus) {
	this.callstatus = callstatus;
}
public Date getRingtime() {
	return ringtime;
}
public void setRingtime(Date ringtime) {
	this.ringtime = ringtime;
}
public Date getAnswertime() {
	return answertime;
}
public void setAnswertime(Date answertime) {
	this.answertime = answertime;
}
public Date getConferencestarttime() {
	return conferencestarttime;
}
public void setConferencestarttime(Date conferencestarttime) {
	this.conferencestarttime = conferencestarttime;
}
public Date getConferenceendtime() {
	return conferenceendtime;
}
public void setConferenceendtime(Date conferenceendtime) {
	this.conferenceendtime = conferenceendtime;
}
public Date getConferencingendedcallbacktime() {
	return conferencingendedcallbacktime;
}
public void setConferencingendedcallbacktime(Date conferencingendedcallbacktime) {
	this.conferencingendedcallbacktime = conferencingendedcallbacktime;
}




}
