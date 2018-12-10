package com.triyasoft.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.api.response.response.GenericResponse;
import com.plivo.helper.exception.PlivoException;
import com.plivo.helper.xml.elements.Conference;
import com.plivo.helper.xml.elements.PlivoResponse;
import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.daos.AuditDao;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.daos.ConferenceDao;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.CallModel;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.ProjectUtils;

@WebServlet("/plivooutboundcall")
public class PlivoOutBoundCallManagementServelet extends HttpServlet  {
	

	public static int counferenceId = 1000;
	public static String Blank_XML_Response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response/>";
	public static String ampersandEscape = PlivoInBoundCallManagementServelet.ampersandEscape;


	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
					
					String requestType = request.getParameter("requestType");
					String legid = request.getParameter("legid");
				
					
					response.addHeader("Content-Type", "text/xml");

					
			if("ring".equals(requestType))		
			{
				
				System.out.println("Getting Inside PlivoOutBoundCallManagementServelet Ring ");
				
				String parentuuid = request.getParameter("parentuuid");
				String legBId = request.getParameter("legBId");
				String conferenceID = request.getParameter("conferenceID");
				String from = request.getParameter("From");
				String to = request.getParameter("To");
				String requestuuid = request.getParameter("RequestUUID");
				String calluuid = request.getParameter("CallUUID");
				Date ringtime = new Date();
				String callstatus = request.getParameter("CallStatus");
				
				CallsDaoService.createOutBoundCallRinging(calluuid,parentuuid,legBId,conferenceID,from,to,requestuuid,ringtime,callstatus);
				
				
				printRequestparameters(request);

				AuditDao.auditHttpRequest("plivooutboundcallring", request);
			
				response.getWriter().print(Blank_XML_Response);
				
				
				
			}
					
					
			
			if("answer".equals(requestType)) {
				System.out.println("Getting Inside PlivoOutBoundCallManagementServelet Answer ");
				printRequestparameters(request);


				AuditDao.auditHttpRequest("plivooutboundcallanswer", request);
				String legBId = request.getParameter("legBId");
				
				String callUUID = request.getParameter("CallUUID");
				String parentuuid = request.getParameter("parentuuid");
				String aLegUUID = request.getParameter("ALegUUID");
				String billRate = request.getParameter("BillRate");
				String aLegRequestUUID = request.getParameter("ALegRequestUUID");
				String callstatus = request.getParameter("CallStatus");
				Date answerTime  = new Date();
				CallsDaoService.updateOutBoundCallAnswer(callUUID,aLegUUID,billRate,aLegRequestUUID,callstatus,answerTime);

				
				String conferenceId =  request.getParameter("conferenceID");
				
		        String appContext = ProjectUtils.getAppContext();
		        
		        boolean isParentCallStillLive = checkIfCustomerConnected(request) ;
		        
		        if(!isParentCallStillLive) {
		        	  response.getWriter().print("<Response><Hangup reason=\"rejected\"  /> </Response>");
		        	  return;
		        }


				//	String actionURL = "https://tracktel.io/provider/plivo/legs/44477771?conferencing=ended";
				String actionURL =	"http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=conferencingended"+ampersandEscape+"legBId="+legBId+ampersandEscape+"parentuuid="+parentuuid;
			//	String callbackUrl = "https://tracktel.io/provider/plivo/legs/44477771?conferencing=callback";
				String callbackUrl = "http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=conferencingcallback"+ampersandEscape+"legBId="+legBId+ampersandEscape+"parentuuid="+parentuuid;

			//	String waitSoundurl = "https://tracktel.io/provider/plivo/legs/44477770/hold_music";
				String holdMusicURL = "http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivoinboundcall?requestType=holdmusic"+ampersandEscape+"legAId"+legBId+ampersandEscape+"parentuuid="+parentuuid;

				String waitSoundurl = holdMusicURL;

			//	String redirectURL = "https://tracktel.io/provider/plivo/legs/44477771/after";
				String redirectURL = "http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=hangup"+ampersandEscape+"legBId="+legBId+ampersandEscape+"parentuuid="+parentuuid;

				
				
				response.getWriter().print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Conference action='"+actionURL+"' beep='false' callbackUrl='"+callbackUrl+"' "
						+ "endConferenceOnExit='true' maxMembers='2' record='true' startConferenceOnEnter='true' waitSound='"+waitSoundurl+"'>"+conferenceId+"</Conference>"
								+ "<Redirect>"+redirectURL+"</Redirect></Response>");
		
				
				
			}
			
			
			
			if("conferencingcallback".equals(requestType)) {
				System.out.println("Getting Inside PlivoOutBoundCallManagementServelet conferencingcallback ");
				printRequestparameters(request);

				String event  = request.getParameter("Event");
				String conferenceAction = request.getParameter("ConferenceAction");
				
				if("ConferenceEnter".equals(event) || "enter".equals(conferenceAction)) {
					
					String parentuuid = request.getParameter("parentuuid");
					String toNumber  = request.getParameter("To");
					
					

					   boolean isParentCallStillLive = checkIfCustomerConnected(request) ;
				        
				        if(!isParentCallStillLive) {
				        	//Disconnect Child call here
							String callUUID = request.getParameter("CallUUID");

				        	disconnectCall(callUUID);
				        }
				        
				      if(isParentCallStillLive) {  
				        
				        Map<String, CallModel> runningCallsMap = CallRoutingServiceV2.runningCallsMapByUUID;
						if(runningCallsMap == null) {
							CallRoutingServiceV2.loadAlldata(true);
							runningCallsMap = CallRoutingServiceV2.runningCallsMapByUUID;
						}
					
					
					if(runningCallsMap != null) { 
					
					CallModel callModel = runningCallsMap.get(parentuuid);
					if(callModel != null) {
					Buyer buyer = CallRoutingServiceV2.cachedbuyersMapByPhoneNumber.get(toNumber);
					if( buyer != null) {
					callModel.setBuyer_id(buyer.getBuyer_id());
					callModel.setBuyer(buyer.getBuyer_name());
					callModel.setConnected_to(toNumber);
					callModel.setBuyerAndCostumerConnected(true);
					CallsDaoService.updateBuyer(callModel);
						}
					}
				}
					
			}
					
					AuditDao.auditHttpRequest("plivoconferencingcallbackConferenceEnter", request);
					String callUUID = request.getParameter("CallUUID");
					String conferenceFirstMember = request.getParameter("ConferenceFirstMember");
					String conferenceMemberID = request.getParameter("ConferenceMemberID");
					String conferenceName = request.getParameter("ConferenceName");
					String conferenceUUID = request.getParameter("ConferenceUUID");
					Date conferencestarttime = new Date(); 
					CallsDaoService.updateOutBoundCallConferenceEnter(callUUID,conferenceFirstMember,conferenceMemberID,conferenceName,conferenceUUID,conferencestarttime);

				}

				if("ConferenceRecordStart".equals(event) || "record".equals(conferenceAction)) {
					
					String recordUrl = request.getParameter("RecordUrl");
					String parentuuid = request.getParameter("parentuuid");
					CallsDaoService.updateRecordingUrl(recordUrl, parentuuid);
				}
				
				if("ConferenceExit".equals(event) || "exit".equals(conferenceAction)) {
					AuditDao.auditHttpRequest("plivoconferencingcallbackConferenceExit", request);
					String callUUID = request.getParameter("CallUUID");
					String conferenceLastMember = request.getParameter("ConferenceLastMember");
					Date conferenceendtime = new Date(); 
					CallsDaoService.updateOutBoundCallConferenceEnd(callUUID,conferenceLastMember,conferenceendtime);

				}
				

				response.getWriter().print(Blank_XML_Response);

				
			}
			
			
			if("conferencingended".equals(requestType)) {
				System.out.println("Getting Inside PlivoOutBoundCallManagementServelet conferencingended ");
				AuditDao.auditHttpRequest("plivoconferencingended", request);
				printRequestparameters(request);
			
				String callUUID = request.getParameter("CallUUID");

				Date conferenceendtime = new Date(); 

				CallsDaoService.updateOutBoundCallConferenceEnd2(callUUID,conferenceendtime);


				response.getWriter().print(Blank_XML_Response);

				
				
			}
			
			
			if("hangup".equals(requestType)) {
				
				System.out.println("Getting Inside PlivoOutBoundCallManagementServelet hangup ");
				AuditDao.auditHttpRequest("plivooutboundcallhangup", request);
				printRequestparameters(request);

				
				String callUUID = request.getParameter("CallUUID");
			
				allocateAnotherCallIfNeeded(request);
				
				
				String totalCost = request.getParameter("TotalCost");
				String hangupCause = request.getParameter("HangupCause");
				String callStatus = request.getParameter("CallStatus");
				String billDuration = request.getParameter("BillDuration");
				String duration = request.getParameter("Duration");
				Date  hangupTime  =  new Date(); 
				CallsDaoService.updateOutBoundCallConferenceHangUp(callUUID,totalCost,hangupCause,callStatus,billDuration,duration,hangupTime);

				
				// Triggger the call to next Buyer in case earlier assigned buyer did not received conferencingcallback or answer  
				response.getWriter().print(Blank_XML_Response);

				
				
			}
				
			if("fallback".equals(requestType)) {
				AuditDao.auditHttpRequest("plivooutboundcallfallback", request);
				printRequestparameters(request);

				
				
			}	
					
			
			
					

	 }
	 
	 
	 private void disconnectCall(String callUUID) {

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


	private boolean checkIfCustomerConnected(HttpServletRequest request) {
			String parentuuid = request.getParameter("parentuuid");
			
			Map<String, CallModel> runningCallsMap = CallRoutingServiceV2.runningCallsMapByUUID;
			if(runningCallsMap == null) {
				CallRoutingServiceV2.loadAlldata(true);
				runningCallsMap = CallRoutingServiceV2.runningCallsMapByUUID;
			}

			if(runningCallsMap != null) { 
			
			CallModel callModel = runningCallsMap.get(parentuuid);
			if(callModel != null)
				return true;
			
			}

		return false;
	}


	private void allocateAnotherCallIfNeeded(HttpServletRequest request) {
		 
			String parentuuid = request.getParameter("parentuuid");
			String toNumber  = request.getParameter("To");
	        String fromNumber = request.getParameter("From");
	        String hangupCause =  request.getParameter("HangupCause");
	     

			
			//Todo Need to implement this logic
			
			
			
			
			List<String> buyerNumbers  = CallsDaoService.getAllBuyersWithParentUUID(parentuuid);
			
			Map<String, CallModel> runningCallsMap = CallRoutingServiceV2.runningCallsMapByUUID;
			if(runningCallsMap == null) {
				CallRoutingServiceV2.loadAlldata(true);
				runningCallsMap = CallRoutingServiceV2.runningCallsMapByUUID;
			}

			CallModel callModel = runningCallsMap.get(parentuuid);
			
			   boolean outboundCondition = "NO_ANSWER".equals(hangupCause) || "USER_BUSY".equals(hangupCause)  || "NO_USER_RESPONSE".equals(hangupCause);
		        boolean inboundCondition = (callModel != null);
		        
		        boolean checkIfAnotherCallRequired =  outboundCondition && inboundCondition;
				
				if(!checkIfAnotherCallRequired)
					return;
			
			// Going to make another call 
				
				
				System.out.println("Going to make another call ");
				
			List<Buyer> prohibitedBuyers = new ArrayList<Buyer>();

			for (String buyerNumber : buyerNumbers) {
				Buyer buyer = CallRoutingServiceV2.cachedbuyersMapByPhoneNumber.get(buyerNumber);
				prohibitedBuyers.add(buyer);

			}
			
			Buyer buyer = CallRoutingServiceV2.cachedbuyersMapByPhoneNumber.get(toNumber);
			prohibitedBuyers.add(buyer);

	
			CallRoutingServiceV2.allocateNumberToRoute(callModel,prohibitedBuyers);
			
			
			String uuid = callModel.getUuid();        
			Buyer selectedBuyer = callModel.getCall_buyer();
			
			String buyerNumber = "";
			if(selectedBuyer != null)
			 buyerNumber  = selectedBuyer.getBuyer_number();
			else return;
			
			
			String conferenceID = request.getParameter("conferenceID");
			String legBId =  request.getParameter("legBId");
		
			           
			String api_key = AppSettingsDao.getPlivoAuthID();
			String api_token = AppSettingsDao.getPlivoAuthToken();
			RestAPI api = new RestAPI(api_key, api_token, "v1");
			LinkedHashMap<String, String> params = new LinkedHashMap<String, String> ();

	        String appContext = ProjectUtils.getAppContext();

			params.put("from", fromNumber);
			params.put("to", buyerNumber);
			
			
			params.put("ring_timeout", "30");
			params.put("fallback_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=fallback&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
			params.put("hangup_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=hangup&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
			params.put("ring_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=ring&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
			params.put("answer_url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=answer&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);
			params.put("url","http://"+AppSettingsDao.getServerIP()+"/"+appContext+"/plivooutboundcall?requestType=answer&legBId="+legBId+"&conferenceID="+conferenceID+"&parentuuid="+uuid);



			try {
				Call call = api.makeCall(params);
				
				System.out.println(call.apiId);
				
			} catch (PlivoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
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
		 

		 
		 


		 	String  auth_id = "MAODJKN2MZOGE4OTNHNJ";
		 	String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";

	        
	        RestAPI api = new RestAPI(auth_id, auth_token, "v1");

	        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	        parameters.put("call_uuid", "594d18aa-8447-11e7-994f-619d2e59f3b2/"); // UUID of the call to be hung up

	        try {
	            GenericResponse resp = api.hangupCall(parameters);
	            System.out.println(resp);   
	        }catch (PlivoException e){  
	            System.out.println(e.getLocalizedMessage());
	        }  
		 
	
		 
	 
	 }
	 

}
