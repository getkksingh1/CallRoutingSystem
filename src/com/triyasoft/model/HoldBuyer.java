package com.triyasoft.model;

import java.util.Date;

public class HoldBuyer {

	Date holdStartTime = new Date();
	Buyer buyer  ;
	public Date getHoldStartTime() {
		return holdStartTime;
	}
	public void setHoldStartTime(Date holdStartTime) {
		this.holdStartTime = holdStartTime;
	}
	public Buyer getBuyer() {
		return buyer;
	}
	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}
	
	
	
}
