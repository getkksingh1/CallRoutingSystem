package com.triyasoft.trackdrive;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.utils.AdcashZoneTracker;
import com.triyasoft.utils.ProjectUtils;

public class BuyerResponse {
	
	private Integer status;
	private List<Buyer> buyers = null;
	
	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		//Need to find daily max cap
		
		Gson gson = new Gson();
		
		Connection conn = ProjectUtils.getMySQLConnection();

		for(int i = 1 ; i<7; i++) {
		BuyerResponse byerResponse = gson.fromJson(new FileReader("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/trackdrive/buyersjsonpage"+i), BuyerResponse.class);

		List<Buyer> buyers = byerResponse.getBuyers();
		
		for (Buyer tdbuyer : buyers) {
			
			com.triyasoft.model.Buyer trackerBuyer = new com.triyasoft.model.Buyer();
			trackerBuyer.setBuyer_name(tdbuyer.getName());
			trackerBuyer.setBuyer_number(tdbuyer.getNumber().replace("+", ""));
		
			trackerBuyer.setWeight(Integer.parseInt(tdbuyer.getWeight()));
			trackerBuyer.setTier((int)Double.parseDouble(tdbuyer.getTier()));
			trackerBuyer.setBid_price(Double.parseDouble(tdbuyer.getBid_price()));
			if(tdbuyer.getConcurrency_cap_limit() == null || tdbuyer.getConcurrency_cap_limit().trim().length()==0 || "null".equals(tdbuyer.getConcurrency_cap_limit().trim()))
				trackerBuyer.setConcurrency_cap_limit(-1);
			else
				trackerBuyer.setConcurrency_cap_limit(Integer.parseInt(tdbuyer.getConcurrency_cap_limit()));
			
			if(tdbuyer.getPaused().equals("true"))
				trackerBuyer.setIs_active(0);
			if(tdbuyer.getPaused().equals("false"))
				trackerBuyer.setIs_active(1);
			
			if(tdbuyer.getBuyer_conversion_daily_limit() == null || tdbuyer.getBuyer_conversion_daily_limit().trim().length()==0 || "null".equals(tdbuyer.getBuyer_conversion_daily_limit().trim()))
			trackerBuyer.setBuyer_daily_cap(-1);
			else
				trackerBuyer.setBuyer_daily_cap(Integer.parseInt(tdbuyer.getBuyer_conversion_daily_limit().trim()));
			

			TrackDriverDBUtility.addBuyerViaDirectDBConnection(trackerBuyer, conn);
			
		}
		
		}

	
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<Buyer> getBuyers() {
		return buyers;
	}

	public void setBuyers(List<Buyer> buyers) {
		this.buyers = buyers;
	}
	
	
}
