package com.triyasoft.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.api.response.response.GenericResponse;
import com.plivo.helper.api.response.response.Record;
import com.plivo.helper.exception.PlivoException;
import com.plivo.helper.xml.elements.Dial;
import com.plivo.helper.xml.elements.Number;
import com.plivo.helper.xml.elements.PlivoResponse;
import com.triyasoft.adapters.PlivoRequestAdapter;
import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.daos.AuditDao;
import com.triyasoft.daos.ConferenceDao;
import com.triyasoft.model.CallModel;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.ProjectUtils;

@WebServlet("/plivoinboundcall")
public class PlivoInBoundCallManagementServelet extends HttpServlet  {
	

	
	public static String ringback_audio_location = "https://s3-ap-southeast-1.amazonaws.com/triya/ringback_tone.wav";
	//public static String nonBussinessHours = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> <Response><Speak voice='WOMAN'>At this time all of our offices are closed, please call back during normal operating hours 7 days a week. Thanks for calling, goodbye.</Speak><Wait length='5'/><Hangup/></Response> ";
	
	public static String nonBussinessHours = " <?xml version=\"1.0\" encoding=\"UTF-8\"?> <Response><Speak voice='WOMAN'>At this time all of our agents are busy, please call back after 30 minutes. Thanks for calling, goodbye.</Speak><Wait length='5'/><Hangup/></Response> ";

	public static String ampersandEscape = "&amp;";
	
	public static String phoneNumber = "";
	public static String toNumber = "";
	public static int counter = 0;
	
	


	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
			
		
			
			
				
		
			
					
			
			String requestType = request.getParameter("requestType");
			
		
					
			
			if("answer".equals(requestType)) {
				System.out.println("Getting Inside PlivoInBoundCallManagementServelet answer ");
				counter++;

				printRequestparameters(request);
						
		        String fromNumber = request.getParameter("From");
		        
		      
 		        
		        
		     
		        
		        
		        String appContext = ProjectUtils.getAppContext();
		        CallModel callModel  =  null ;
		        
				      
		        
		 		
		 		try 
				        {
				    	
		 					PlivoRequestAdapter plivoRequestAdapter = new PlivoRequestAdapter();
				    		callModel = plivoRequestAdapter.createCallModelAdapterAtAnswer(request);
				    		
				    		  if(fromNumber.equals("Restricted") || fromNumber.equals("anonymous") ||(fromNumber.trim().length()==4 )) {
				    			  
				    			  if(fromNumber.equals("Restricted")) {
				    				  response.getWriter().print("<Response><Hangup reason=\"rejected\"  /> </Response>");
						        	  return;
				    			  }
									
				    			  if(fromNumber.equals("anonymous")) {
									Random r = new Random();
									int Low = 1000;
									int High = 10000;
									int result = r.nextInt(High-Low) + Low;
									
									fromNumber = result +"";
									callModel.setCaller_number(fromNumber);
				    			  }
					 		        
								}
							
				    		
				        	callModel = CallRoutingServiceV2.createCallModel(callModel);
				    	
				    	 }
				        
				        catch(Exception e) {
				        	
				        	 StringWriter errors = new StringWriter();
				        	 e.printStackTrace(new PrintWriter(errors));
					         AuditDao.auditRecord("PlivAnswerException", errors.toString());
					         response.getWriter().print("<Response><Hangup reason=\"rejected\"  /> </Response>");
					         return;
				        	
				        }
				     
				     if(callModel.getError_code() !=null) {
				        	
				        	if("E005".equals(callModel.getError_code()))
				        	{
				        	      AuditDao.auditRecord("PlivAnswerNonBussinessHours", callModel.getUuid()+" :::::"+callModel.getError_code()+"::::: "+callModel.getError_description());
					        	  response.getWriter().print(nonBussinessHours);
					        	  return;
							
				        	}
				        	else {
					          AuditDao.auditRecord("PlivAnswerError", callModel.getUuid()+" :::::"+callModel.getError_code()+"::::: "+callModel.getError_description());
				        	  response.getWriter().print("<Response><Hangup reason=\"rejected\"  /> </Response>");
						      return;
				        	}
				        	
				        }    
				        
				     
				     
				     
		 		String uuid = callModel.getUuid();        
				String buyerNumber = callModel.getConnected_to();
				
				if(buyerNumber !=null && buyerNumber.startsWith("sip:")) {

				       String responseToPlivo = "<Response><Dial callerId=\""+fromNumber+"\"><User>"+buyerNumber+"</User></Dial></Response>";
				       response.addHeader("Content-Type", "text/xml");
						response.getWriter().println(responseToPlivo);
						return;

					
				}
				
				
				           
				int uniqueId = ConferenceDao.getNextConferenceID();
				System.out.println("Conference ID " + uniqueId);
				
				String conferenceID = appContext+ uniqueId;
				String legAId = appContext+(2*uniqueId);
				String legBId = appContext+ (2*uniqueId -1);
				
				String holdMusicURL = "http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivoinboundcall?requestType=holdmusic"+ampersandEscape+"legAId="+legAId;

				AuditDao.auditHttpRequest("plivoinboundcallanswer", request);
				
	            response.addHeader("Content-Type", "text/xml");
				response.getWriter().println(" <?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Conference beep='false' endConferenceOnExit='true' maxMembers='2' startConferenceOnEnter='true' waitSound='"+holdMusicURL+"'>"+conferenceID+"</Conference></Response>");
				
				String api_key =  AppSettingsDao.getPlivoAuthID();
				String api_token = AppSettingsDao.getPlivoAuthToken();
				RestAPI api = new RestAPI(api_key, api_token, "v1");
				
				LinkedHashMap<String, String> params = new LinkedHashMap<String, String> ();
	
				params.put("from", fromNumber);
				params.put("to", buyerNumber);
				

				
				int ring_timeout = callModel.getCall_buyer().getRing_timeout();
				
				params.put("ring_timeout", ""+ring_timeout);
				params.put("fallback_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=fallback&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
				params.put("hangup_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=hangup&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
				params.put("ring_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=ring&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
				params.put("answer_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=answer&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
				params.put("url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=answer&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
				params.put("parent_call_uuid",  uuid);
				

				try {
					Call call = api.makeCall(params);
					
					System.out.println(call.apiId);
					
				} catch (PlivoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			if("holdmusic".equals(requestType)) {
				System.out.println("Getting Inside PlivoInBoundCallManagementServelet holdmusic ");
				printRequestparameters(request);

				
				AuditDao.auditHttpRequest("plivoinboundcallholdmusic", request);
	            response.addHeader("Content-Type", "text/xml");
	            response.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Play loop='10000'>"+ringback_audio_location+"</Play></Response>");
				
				
			}
			
			
			
			if("hangup".equals(requestType)) {
					System.out.println("Getting Inside PlivoInBoundCallManagementServelet hangup ");

					printRequestparameters(request);

				    AuditDao.auditHttpRequest("plivoinboundcallanswerdone", request);
				    
				    
					try{
						
					    PlivoRequestAdapter plivoRequestAdapter = new PlivoRequestAdapter();
				        CallModel	callModel = plivoRequestAdapter.createCallModelAdapterAtHangup(request);	
						CallRoutingServiceV2.updateCallModel(callModel);
						} 
						 catch(Exception e) {
					        e.printStackTrace();	
				        	 StringWriter errors = new StringWriter();
				        	 e.printStackTrace(new PrintWriter(errors));
					         AuditDao.auditRecord("PlivAnswerException", errors.toString());
					         return;
				        	
				        }
					
					 
				    response.addHeader("Content-Type", "text/xml");
		            response.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response/>");
					
					}
			
			if("disconnectCall".equals(requestType)) {
				

				String callUUID = request.getParameter("callUUID");
				String api_key =  AppSettingsDao.getPlivoAuthID();
				String api_token = AppSettingsDao.getPlivoAuthToken();

		        
		        RestAPI api = new RestAPI(api_key, api_token, "v1");

		        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		        parameters.put("call_uuid", callUUID); // UUID of the call to be hung up

		        try {
		            GenericResponse resp = api.hangupCall(parameters);
		            System.out.println(resp);   
		        }catch (PlivoException e){  
		            System.out.println(e.getLocalizedMessage());
		        }  
			 
		
			}
			
			
			if("reloaddata".equals(requestType)) {
				
				long startTime = System.currentTimeMillis();
				
				CallRoutingServiceV2.loadAlldata(true);
				
				long endTime = System.currentTimeMillis();

				
				response.getWriter().println("Loaded Buyers: "+ CallRoutingServiceV2.cachedBuyersList.size());
				response.getWriter().println("Loaded PhoneNumbers: "+ CallRoutingServiceV2.cachedPhoneNumbersMap.size());
				response.getWriter().println("Loaded Sources: "+ CallRoutingServiceV2.cachedTrafficsourcesMap.size());
				
				response.getWriter().println("Time Taken to reload the data: "+ (endTime-startTime) + " millliseconds");
				


			}			

					
				    
				    
				   
		            
		            
		            
				
			}
	 
	 
	 private String recordCall(String uuid) {  
		 
		   String auth_id = AppSettingsDao.getPlivoAuthID();;
		   String auth_token = AppSettingsDao.getPlivoAuthToken();
		   RestAPI api1 = new RestAPI(auth_id, auth_token, "v1");
		   LinkedHashMap<String, String> parameters1 = new LinkedHashMap<String, String>();
		   parameters1.put("call_uuid", uuid); // ID of the call
		   parameters1.put("time_limit","600"); // Max recording duration in seconds

	  try {
	     Record resp1 = api1.record(parameters1);
	     System.out.println(resp1.api_id);
	     System.out.println(resp1.message);
	     System.out.println(resp1.url);
	     
	     System.out.println(resp1.error);
	     System.out.println(resp1.serverCode);
	     return resp1.url;

	  	}catch (PlivoException e){  
	     System.out.println(e.getLocalizedMessage());
	  		}
	  
	  return "";
		 }
	 
					
		public static void printRequestparameters(HttpServletRequest request) {
			 
			  Enumeration<String> parameterNames = request.getParameterNames();

			  
				StringBuffer stringBuffer = new StringBuffer();
		        while (parameterNames.hasMoreElements()) {

		            String paramName = parameterNames.nextElement();
		            stringBuffer.append(paramName+"=");
		            String paramValue  = request.getParameter(paramName);
		            stringBuffer.append(paramValue+"&");

		           
		        }
		        
		        System.out.println(stringBuffer);
		        System.out.println("-------------------------");
		 }
	 
	 
	 
	 
	 public static void main(String[] args) {
		 
		 
	 }
	 

}
