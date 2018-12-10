package com.triyasoft.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.OutboundCallModel;
import com.triyasoft.model.PhoneNumber;

public class APISanitizer {
	
	public static void main(String[] args) {
		
		
		
	
		  
		  
	}

	
	
	public static void updateNoBuyers() {
		
		List<CallModel> calls = CallsDaoService.getNobuyersCalls() ;
		for (CallModel callModel : calls) {
			String number_called =  callModel.getNumber_called();
			String customer_number = callModel.getCaller_number();
			String uuid =  callModel.getUuid();
			String buyer_number  = CallsDaoService.getBuyerNumber(uuid);
			if(buyer_number == null) 
				continue;
			
			Buyer buyer = BuyerDaoService.loadBuyerByPhoneNumber(buyer_number);
			
			int buyer_id =  buyer.getBuyer_id();
			String buyer_name =  buyer.getBuyer_name();
			double buyer_revenue = buyer.getBid_price();
			
			PhoneNumber phoneNumber = PhoneNumberDaoService.getPhoneNumberbyCallerId(number_called);
			
			double traffic_source_revenue = phoneNumber.getCostpercall();
			
			boolean isBuyerDuplicate = CallsDaoService.checkBuyerDup(customer_number,buyer_id);
			
		    boolean isSourceDuplicate = CallsDaoService.checkTraffic_SourceDup(customer_number, uuid);
			
		    
		    if(isBuyerDuplicate)
		    	buyer_revenue = 0 ;
		    
		    isSourceDuplicate = true;
		    
		    if(isSourceDuplicate)
		    	traffic_source_revenue = 0;
		    
		    
		    
		    CallsDaoService.updateCallByUUID(buyer_id, buyer_name, buyer_revenue, traffic_source_revenue, buyer_number, uuid );
			
			
			
		}
		
		
	}
	
	public static void updateNoBuyersDueToBuyerIssues() {
		
		  Map<String, Date>  inboundCallsUUIDs  = CallsDaoService.getInboundCallsUUIDsForSanitization();
		  Map<String, String> buyerNameAndNumberMap =   BuyerDaoService.getAllBuyersMapByBuyerNumberAndName();

		
	}
	
	
  public static void sanitizeCallsAPIData() {
	  
	  Map<String, Date>  inboundCallsUUIDs  = CallsDaoService.getInboundCallsUUIDsForSanitization();
	  Map<String, String> buyerNameAndNumberMap =   BuyerDaoService.getAllBuyersMapByBuyerNumberAndName();
	  
	  for (Map.Entry<String, Date> entry : inboundCallsUUIDs.entrySet())
	  {		  
		  String parentuuid = entry.getKey();
		  Date callLandingTimeOnServer = entry.getValue();
		  
		  List<OutboundCallModel>  outBoundCalls =   CallsDaoService.getOutBoundCalls(parentuuid);
		  
			int buyerConnectedTime  = 0;
			int buyerConnectingTime = 0;

			if(outBoundCalls.size() == 1) {
				Date  conferenceStartTime = outBoundCalls.get(0).getConferencestarttime();
				Date  conferenceEndTime = outBoundCalls.get(0).getConferenceendtime();
				Date  buyerCallAnswerTime = outBoundCalls.get(0).getAnswertime();
				
				if(conferenceEndTime == null)
					conferenceEndTime = outBoundCalls.get(0).getOutboundcallhanguptime();
				

				
				if(conferenceStartTime!= null && conferenceEndTime!= null)
					buyerConnectedTime = (int)(conferenceEndTime.getTime()-conferenceStartTime.getTime())/1000;
			
				
				
				if(callLandingTimeOnServer !=null && buyerCallAnswerTime != null)
					buyerConnectingTime = (int)(buyerCallAnswerTime.getTime() - callLandingTimeOnServer.getTime())/1000;
				
			}
			
			
				int callAttempts = outBoundCalls.size();
			
				StringBuffer buyer_hangup_reasons = new StringBuffer();
				StringBuffer buyers_tried = new StringBuffer();

				
				for (OutboundCallModel outboundCallModel : outBoundCalls) {
					
					String buyerName = buyerNameAndNumberMap.get(outboundCallModel.getTo());
					String hangupReason = outboundCallModel.getHangupcause();
					
					if(buyerName == null) 
						buyerName = "";
					
					if(hangupReason == null)
						hangupReason = "";
					
					buyers_tried.append(buyerName+",");
					buyer_hangup_reasons.append(hangupReason+",");
				
				}
				
			
				
				CallsDaoService.updateCallData(parentuuid,buyerConnectedTime,buyerConnectingTime,callAttempts, buyers_tried, buyer_hangup_reasons);
				
				System.out.println("uuid:"+parentuuid+" buyerConnectedTime:"+buyerConnectedTime+" buyerConnectingTime:"+buyerConnectingTime+" callAttempts:"+callAttempts+" buyers_tried:"+buyers_tried+" buyer_hangup_reasons:"+buyer_hangup_reasons);
			
			}
		  
		  
		
	}
	  
	  
	  
  

private static List<String> getInboundCallsUUIDs() {
	
	return null;
}
	
}
