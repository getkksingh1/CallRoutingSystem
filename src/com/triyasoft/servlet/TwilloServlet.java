package com.triyasoft.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plivo.helper.exception.PlivoException;
import com.plivo.helper.xml.elements.Dial;
import com.plivo.helper.xml.elements.Number;
import com.plivo.helper.xml.elements.PlivoResponse;
import com.triyasoft.adapters.TwilioRequestAdapter;
import com.triyasoft.daos.AuditDao;
import com.triyasoft.model.CallModel;
import com.triyasoft.utils.CallRoutingServiceV2;






/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/twillo")
public class TwilloServlet extends HttpServlet {
	

	public static String nonBussinessHours = "<Response><Say language=\"en-US\" voice=\"woman\">At this time all of our offices are closed, please call back during normal operating hours 7 days a week. Thanks for calling, goodbye.!</Say></Response>";
	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
				 
					String requestType = request.getParameter("requestType");

							
							
					
					if("reloaddata".equals(requestType)) {
						
						long startTime = System.currentTimeMillis();
						
						CallRoutingServiceV2.loadAlldata(true);
						
						long endTime = System.currentTimeMillis();

						
						response.getWriter().println("Loaded Buyers: "+ CallRoutingServiceV2.cachedBuyersList.size());
						response.getWriter().println("Loaded PhoneNumbers: "+ CallRoutingServiceV2.cachedPhoneNumbersMap.size());
						response.getWriter().println("Loaded Sources: "+ CallRoutingServiceV2.cachedTrafficsourcesMap.size());
						
						response.getWriter().println("Time Taken to reload the data: "+ (endTime-startTime) + " millliseconds");
						


					}			
					
			if("message".equals(requestType)) {
				
				
				AuditDao.auditHttpRequest("TwiloAnswerRequest", request);
				
			}
				
				       
				            

			
					
					
			if("answer".equals(requestType))		
			{

				boolean isSipCall = false ;
				AuditDao.auditHttpRequest("TwiloAnswerRequest", request);
			//	XPlivoSignature.auditPlivoSignature(request,"?requestType=answer");

				long startTime = System.currentTimeMillis();
				
				CallModel callModel  =  null ;

				try 
		        {
		    	
		        String fromNumber = request.getParameter("From");
		        if(fromNumber !=null && fromNumber.startsWith("sip:")) {
		        	//this is a sip call handle differently
		        	isSipCall = true;
		        }
		        else
		        {
		        	TwilioRequestAdapter twilioRequestAdapter = new TwilioRequestAdapter();
		        	callModel = twilioRequestAdapter.createCallModelAdapterAtAnswer(request);
		        	
		        	callModel = CallRoutingServiceV2.createCallModel(callModel);
		        }
		        
		        }
		        
		        catch(Exception e) {
		        	
		        	 StringWriter errors = new StringWriter();
		        	 e.printStackTrace(new PrintWriter(errors));
			         AuditDao.auditRecord("TwilloAnswerException", errors.toString());
			         response.getWriter().print("<Response><Hangup/></Response>");
			         return;
		        	
		        }
		        
		        
		        if(!isSipCall && callModel.getError_code() !=null) {
		        	
		        	if("E005".equals(callModel.getError_code()))
		        	{
		        	      AuditDao.auditRecord("TwilloAnswerNonBussinessHours", callModel.getUuid()+" :::::"+callModel.getError_code()+"::::: "+callModel.getError_description());
			        	  response.getWriter().print(nonBussinessHours);
			        	//  response.getWriter().print("<Response><Hangup/></Response>");
			        	  return;
					
		        	}
		        	else {
			          AuditDao.auditRecord("TwilloAnswerError", callModel.getUuid()+" :::::"+callModel.getError_code()+"::::: "+callModel.getError_description());
		        	  response.getWriter().print("<Response><Hangup/></Response>");
				      return;
		        	}
		        	
		        }
		        

		        if(!isSipCall) {
 		        
				String buyerNumber = callModel.getConnected_to();
       	        
    	        String From = request.getParameter("From");
    	        
    	        String xml  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Dial timeout=\"30\"    "
    	        		+ "callerId=\""+From+"\"  >"+buyerNumber+"</Dial></Response> "  ; 
       	      
    	        response.setContentType("application/xml");
    	        
       	        try {
       	            response.getWriter().print(xml);
       	        } catch (Exception e) {
       	            e.printStackTrace();
       	        }
		        
		        
		        callModel.setXmlResponse(xml);
				long endTime = System.currentTimeMillis();
				callModel.setCallRoutingTime(endTime-startTime);
		        }
		        
		        else {
		        	
		  			String buyerNumber = request.getParameter("To");
					
			      //  String fromNumber = request.getParameter("From");
			        
					Dial dial = new Dial();
			        dial.setCallerId("18883084905");
			        Number num = new Number(buyerNumber);
			        try {
			            dial.append(num);
			            PlivoResponse plivoResponse = new PlivoResponse();
			            plivoResponse.append(dial);
			            response.addHeader("Content-Type", "text/xml");
			            String xmlResponse  = plivoResponse.toXML() ;
			//            callModel.setXmlResponse(xmlResponse);
			            response.getWriter().print(xmlResponse);;
			        } catch (PlivoException e) {
			            e.printStackTrace();
			        }
			        
					long endTime = System.currentTimeMillis();
				//	callModel.setCallRoutingTime(endTime-startTime);
			        
		        	
		        	
		        }


				
			}
					
					
			
			if("hangup".equals(requestType)) {
				AuditDao.auditHttpRequest("TwiloHangupRequest", request);
		
				//	XPlivoSignature.auditPlivoSignature(request,"?requestType=hangup");

				
				try{
					
			    TwilioRequestAdapter twilioRequestAdapter = new TwilioRequestAdapter();
		        CallModel	callModel = twilioRequestAdapter.createCallModelAdapterAtHangup(request);	
				CallRoutingServiceV2.updateCallModelWithConferenceForTwillo(callModel);
				} 
				 catch(Exception e) {
			        e.printStackTrace();	
		        	 StringWriter errors = new StringWriter();
		        	 e.printStackTrace(new PrintWriter(errors));
			         AuditDao.auditRecord("TwiloAnswerException", errors.toString());
			         return;
		        	
		        }
			}
					
					
					
					

	 }
	 
	 
	 
	 public static void main(String[] args) {
		 
		 
	 }
	 

}