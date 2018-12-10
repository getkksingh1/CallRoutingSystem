package com.triyasoft.plivo;

public class PlivoCallResponseModel {

	private String answer_time;
	private String bill_duration;
	private String billed_duration;
	private String call_duration;
	private String end_time;
	private String from_number;
	private String to_number;
	private double total_amount;
	private double total_rate;

	public String getAnswer_time() {
		return answer_time;
	}

	public void setAnswer_time(String answer_time) {
		this.answer_time = answer_time;
	}

	public String getBill_duration() {
		return bill_duration;
	}

	public void setBill_duration(String bill_duration) {
		this.bill_duration = bill_duration;
	}

	public String getBilled_duration() {
		return billed_duration;
	}

	public void setBilled_duration(String billed_duration) {
		this.billed_duration = billed_duration;
	}

	public String getCall_duration() {
		return call_duration;
	}

	public void setCall_duration(String call_duration) {
		this.call_duration = call_duration;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getFrom_number() {
		return from_number;
	}

	public void setFrom_number(String from_number) {
		this.from_number = from_number;
	}

	public String getTo_number() {
		return to_number;
	}

	public void setTo_number(String to_number) {
		this.to_number = to_number;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public double getTotal_rate() {
		return total_rate;
	}

	public void setTotal_rate(double total_rate) {
		this.total_rate = total_rate;
	}

}
