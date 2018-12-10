package com.triyasoft.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plivo.helper.api.response.endpoint.Endpoint;
import com.triyasoft.daos.CallcenterInboundCallsDao;
import com.triyasoft.daos.CallcenterOutboundCallsDao;
import com.triyasoft.daos.ExtensionsDao;
import com.triyasoft.model.CallcenterInboundCall;
import com.triyasoft.model.CallcenterOutboundCall;
import com.triyasoft.model.PlivoEndpointExtention;
import com.triyasoft.plivo.PlivoEndPointUtil;

public class CallcenterCallAllocationService {

	private static List<PlivoEndpointExtention> extensions  = new ArrayList<PlivoEndpointExtention>();
	private static Map<String, PlivoEndpointExtention> sipEndPointnameExtensionMap  = new HashMap<String, PlivoEndpointExtention>();
	
	public static List<CallcenterInboundCall> runningInboundCalls = new ArrayList<CallcenterInboundCall>();
	public static Map<String, CallcenterInboundCall> runningInboundCallsMap = new HashMap<String, CallcenterInboundCall>();
	
	public static List<CallcenterOutboundCall> runningoutboundCalls = new ArrayList<CallcenterOutboundCall>();
	public static Map<String, CallcenterOutboundCall> runningOutboundCallsMap = new HashMap<String, CallcenterOutboundCall>();


	
	public static boolean initialized  = false;
	
	
	public static void markExtensionBusy(String sipUrl) {
		
		for (PlivoEndpointExtention plivoEndpointExtention : extensions) {
			if(plivoEndpointExtention.getSip_url().equals(sipUrl))
				plivoEndpointExtention.setExtension_busy(true);
		}
		
		sipEndPointnameExtensionMap.get(sipUrl).setExtension_busy(true);
	}
	
	public static void markExtensionFree(String sipUrl) {
		
		for (PlivoEndpointExtention plivoEndpointExtention : extensions) {
			if(plivoEndpointExtention.getSip_url().equals(sipUrl))
				plivoEndpointExtention.setExtension_busy(false);
		}
		
		sipEndPointnameExtensionMap.get(sipUrl).setExtension_busy(false);
	}
	
	
	public static String getAllocatedEndPoint() {
		
		
			loadInitdata();
		
		
		for (PlivoEndpointExtention plivoEndpointExtention : extensions) {
			
			if(plivoEndpointExtention.isExtensionReadytoTakeCall())
				return plivoEndpointExtention.getSip_url();
		}
		
		return null;
	}
	
	
	public static void loadInitdata() {
		
		extensions = ExtensionsDao.loadAllExtensions();
		for (PlivoEndpointExtention plivoEndpointExtention : extensions) {
			sipEndPointnameExtensionMap.put(plivoEndpointExtention.getSip_url(), plivoEndpointExtention);
		}
		
		List<Endpoint> plivoEndPoints = PlivoEndPointUtil.getAllEndpointsStatus();
		
		if(plivoEndPoints != null && plivoEndPoints.size() > 0 )
		for (Endpoint endpoint : plivoEndPoints) {
			String sipURL  = endpoint.sipUri ;
			PlivoEndpointExtention plivoEndpointExtention = sipEndPointnameExtensionMap.get(sipURL);
			if(plivoEndpointExtention != null){
					
					plivoEndpointExtention.setSip_registered(endpoint.sipRegistered);
			}
			
		}
		
		List<CallcenterInboundCall> runningInboundCalls = CallcenterInboundCallsDao.loadRunningInboundCalls();
		for (CallcenterInboundCall callcenterInboundCall : runningInboundCalls) {
			runningInboundCallsMap.put(callcenterInboundCall.getUuid(), callcenterInboundCall);
			markExtensionBusy(callcenterInboundCall.getTosipendpoint());
		}
		
		List<CallcenterOutboundCall> runningOutboundCalls = CallcenterOutboundCallsDao.loadRunningOutboundCalls();
		for (CallcenterOutboundCall callcenterOutboundCall : runningOutboundCalls) {
			runningOutboundCallsMap.put(callcenterOutboundCall.getUuid(), callcenterOutboundCall);
			markExtensionBusy(callcenterOutboundCall.getFromsipendpoint());

		}

		
		initialized = true;
		
	}
	
	


}
