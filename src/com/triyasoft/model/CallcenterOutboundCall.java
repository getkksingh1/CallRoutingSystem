package com.triyasoft.model;

import java.util.Date;

public class CallcenterOutboundCall {

	private int id;
	private String uuid;
	private String fromsipendpoint;
	private String callername;
	private String fromsipuser;
	private int sipuserid;
	private String tonumber;
	private Date callstarttime;
	private int callduration;
	private int billduration;
	private double billrate;
	private double totalbill;
	private Date callendtime;
	private int isrunning;
	private String callstatusathangup;
	private String hangupcause;

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

	public String getCallername() {
		return callername;
	}

	public void setCallername(String callername) {
		this.callername = callername;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFromsipendpoint() {
		return fromsipendpoint;
	}

	public void setFromsipendpoint(String fromsipendpoint) {
		this.fromsipendpoint = fromsipendpoint;
	}

	public String getFromsipuser() {
		return fromsipuser;
	}

	public void setFromsipuser(String fromsipuser) {
		this.fromsipuser = fromsipuser;
	}

	public int getSipuserid() {
		return sipuserid;
	}

	public void setSipuserid(int sipuserid) {
		this.sipuserid = sipuserid;
	}

	public String getTonumber() {
		return tonumber;
	}

	public void setTonumber(String tonumber) {
		this.tonumber = tonumber;
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
