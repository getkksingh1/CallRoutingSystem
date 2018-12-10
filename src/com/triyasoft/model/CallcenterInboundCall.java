package com.triyasoft.model;

import java.util.Date;

public class CallcenterInboundCall {

	private int id;
	private String uuid;
	private String tosipendpoint;
	private String tosipuser;
	private int sipuserid;
	private String customernumber;
	private String callednumber;
	private Date callstarttime;
	private int callduration;
	private int billduration;
	private double billrate;
	private double totalbill;
	private Date callendtime;
	private String callerName;
	private int isrunning;
	private String callstatusathangup;
	private String hangupcause;
	
	
	
	
	public String getCallerName() {
		return callerName;
	}
	public void setCallerName(String callerName) {
		this.callerName = callerName;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public int getIsrunning() {
		return isrunning;
	}
	public void setIsrunning(int isrunning) {
		this.isrunning = isrunning;
	}
	public String getTosipendpoint() {
		return tosipendpoint;
	}
	public void setTosipendpoint(String tosipendpoint) {
		this.tosipendpoint = tosipendpoint;
	}
	public String getTosipuser() {
		return tosipuser;
	}
	public void setTosipuser(String tosipuser) {
		this.tosipuser = tosipuser;
	}
	public int getSipuserid() {
		return sipuserid;
	}
	public void setSipuserid(int sipuserid) {
		this.sipuserid = sipuserid;
	}
	public String getCustomernumber() {
		return customernumber;
	}
	public void setCustomernumber(String customernumber) {
		this.customernumber = customernumber;
	}
	public String getCallednumber() {
		return callednumber;
	}
	public void setCallednumber(String callednumber) {
		this.callednumber = callednumber;
	}
	public Date getCallstarttime() {
		return callstarttime;
	}
	public void setCallstarttime(Date callstarttime) {
		this.callstarttime = callstarttime;
	}
	public int getCallduration() {
		return callduration;
	}
	public void setCallduration(int callduration) {
		this.callduration = callduration;
	}
	public int getBillduration() {
		return billduration;
	}
	public void setBillduration(int billduration) {
		this.billduration = billduration;
	}
	public double getBillrate() {
		return billrate;
	}
	public void setBillrate(double billrate) {
		this.billrate = billrate;
	}
	public double getTotalbill() {
		return totalbill;
	}
	public void setTotalbill(double totalbill) {
		this.totalbill = totalbill;
	}
	public Date getCallendtime() {
		return callendtime;
	}
	public void setCallendtime(Date callendtime) {
		this.callendtime = callendtime;
	}
	public String getCallstatusathangup() {
		return callstatusathangup;
	}
	public void setCallstatusathangup(String callstatusathangup) {
		this.callstatusathangup = callstatusathangup;
	}
	public String getHangupcause() {
		return hangupcause;
	}
	public void setHangupcause(String hangupcause) {
		this.hangupcause = hangupcause;
	}
	
	
	
}
