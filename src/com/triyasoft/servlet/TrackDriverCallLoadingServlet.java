package com.triyasoft.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.trackdrive.Call;
import com.triyasoft.trackdrive.CallsResponse;

@WebServlet("/loadtrackdrivedata")
public class TrackDriverCallLoadingServlet extends HttpServlet{


	public static String trackdriveCallAPI = "https://hta-solutions.trackdrive.net/api/v1/calls?page=PAGENUMBER&order=created_at&auth_token=TxXsXAVnyELPQAyVhrs5&columns=uuid,number_called,connected_to,caller_number,offer,traffic_source,buyer,total_duration,hold_duration,ivr_duration,%20answered_duration,user_offer_id,user_traffic_source_id,user_buyer_id,status,%20buyer_converted,%20buyer_revenue,%20traffic_source_payout,trackdrive_cost,provider_cost,hangup_reason,provider,created_at,ended_at";
	

	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
				 
					String requestType = request.getParameter("requestType");

							
					
					if("callsdata".equals(requestType)) {
						
						Gson gson = new Gson();
						
						String pagenumber = request.getParameter("pagenumber");
						String apiURL = trackdriveCallAPI.replace("PAGENUMBER", pagenumber);
						
						String jsonStr  = readAPIResponseToString(apiURL);

						CallsResponse callsResponse = gson.fromJson(jsonStr, CallsResponse.class);

						 List<Call> trackDrivecalls =  callsResponse.getCalls();
						 
						 for (Call trackDriveCall : trackDrivecalls) {
							 
							 CallModel callModel = new CallModel();
							 callModel.setUuid(trackDriveCall.getUuid());
							 callModel.setNumber_called(trackDriveCall.getNumber_called().replace("+", ""));
							 callModel.setCaller_number(trackDriveCall.getCaller_number());
							 callModel.setDuration(trackDriveCall.getTotal_duration()+"");
							 callModel.setBillDuration(trackDriveCall.getTotal_duration()+"");
							 
							 callModel.setBuyer(trackDriveCall.getBuyer());
							 
							 String connected_to = trackDriveCall.getConnected_to();
							 if(connected_to!=null && connected_to.trim().length()!=0)
								 connected_to = connected_to.replace("+", "");
							 
							 callModel.setConnected_to(connected_to);
							 
							 Date callLandingTime = convertToDate(trackDriveCall.getCreated_at());
							 callModel.setCallLandingTimeOnServer(callLandingTime);
							 
							 double trackdrive_cost = trackDriveCall.getTrackdrive_cost();
							 double provider_cost = trackDriveCall.getProvider_cost();
							 
							 callModel.setTotalCost((provider_cost+trackdrive_cost)+"");
							 
							 //Setting up Traffic Source
							 
							 callModel.setTraffic_source(trackDriveCall.getTraffic_source());
							 PhoneNumber phoneNumber = PhoneNumberDaoService.getPhoneNumberbyCallerId(callModel.getNumber_called());
							 callModel.setTraffic_source_id(phoneNumber.getTraffic_source_id());
							 
							 //Setting up Buyer
							 
							 if(trackDriveCall.getConnected_to() !=null && trackDriveCall.getConnected_to().trim().length()>0) {
								 
								 Buyer buyer = BuyerDaoService.loadBuyerByPhoneNumber(callModel.getConnected_to());
								 callModel.setBuyer_id(buyer.getBuyer_id());
								 callModel.setBuyer_revenue(buyer.getBid_price());
								 callModel.setBuyer(buyer.getBuyer_name());
								 callModel.setTraffic_source_revenue(phoneNumber.getCostpercall());

								 
							 }
							 
							 //Setting up running status
							 
							 String staus = trackDriveCall.getStatus();
							 if("not-open".equals(staus) || "finished".equals(staus))
								 callModel.setIs_running(0);
							 if("forwarded".equals(staus))
								 callModel.setIs_running(1);
							 
						
							 
							 CallsDaoService.saveCallDataTrackDrive(callModel);
							 
							 
							
						}
						
						

						
					}			
					
			
					
					
					
					
					

	 }
	 
	 
	 private String readAPIResponseToString(String apiURL) {
		 
	        StringBuffer buffer = new StringBuffer();

		
		 try {
		 URL url = new URL(apiURL);
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(url.openStream()));

	        String inputLine;
	        while ((inputLine = in.readLine()) != null)
	        	buffer.append(inputLine);
	        in.close();
		 } catch (Exception e ) {
			 
		 }
 		 
		return buffer.toString();
	}


	private static Date convertToDate(String created_at) {
			//2017-06-22T07:47:40.772+00:00
		created_at = created_at.replace("+00:00", "").replace("T", " ");
		
		Date date = null ;
			
		SimpleDateFormat datformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		try {
			 date =  datformat.parse(created_at);
		} catch (ParseException e) {
			
			System.out.println("Could not parse date");
			date = new Date();
		}
		
		
			return date;
		}
	

}
