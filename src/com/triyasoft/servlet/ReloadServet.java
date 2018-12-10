package com.triyasoft.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.daos.BuyerSourcePreferenceFilterDao;
import com.triyasoft.daos.ContactDao;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.BuyerSourcePreferenceFilter;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.ui.DashboardUIGenerator;
import com.triyasoft.utils.APISanitizer;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.Telecomdataimport;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/Reload")
public class ReloadServet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String requestType = request.getParameter("requestType");
    	
    	if("reloadFilters".equals(requestType)) {
    		BuyerSourcePreferenceFilterDao.loadAllFilters();
    	}
    	
    	if("reloadTemplates".equals(requestType)) {

    		
    		DashboardUIGenerator.areTemplatesLoaded = false;
    		
    		
			
    	}
    	
    	if("reloadtelecome".equals(requestType)) {

    	
    	{
    		
    	}
    	Telecomdataimport.loadcllilatlong();
    	Telecomdataimport.readTeleComTable();
    	}
    	
    	if("reloadtelecomeexitingcall".equals(requestType)) 
    	{
    		Telecomdataimport.updateExistingCallsWithGeodata();

    		
    	}
    	
    	if("reloadblockedcontact".equals(requestType)) {
    		ContactDao.isLoaded = false;
    		
    	}
    	
    	
    	
    	if("updateCallsData".equals(requestType)) {
    		
    		APISanitizer.sanitizeCallsAPIData();
    		APISanitizer.updateNoBuyers();

    	}
    	
    	if("updateNoBuyer".equals(requestType)) {
    		APISanitizer.updateNoBuyers();
    	}
    	
    	if("reloadAppSetting".equals(requestType)) {
    		
    		AppSettingsDao.loadAllAppSettings();
    	}
    	
    	if("testFilter".equals(requestType)) {
    		String buyerId = request.getParameter("buyerId");
    		String trafficSourceId= request.getParameter("traffic_source_id");
    		Buyer buyer = new Buyer();
    		buyer.setBuyer_id(Integer.parseInt(buyerId.trim()));
    		TrafficSource trafficSource = new TrafficSource();
    		trafficSource.setId(Integer.parseInt(trafficSourceId.trim()));
    		boolean evaluateFilter = BuyerSourcePreferenceFilter.evaluateOp(buyer, trafficSource);
			response.getWriter().println("buyerId:"+ buyerId+"   trafficSourceId:"+trafficSourceId+"  evaluatedFilter:"+evaluateFilter);
			return;
    		
    	}
    	
    	
    	if("reloadRoutingServiceData".equals(requestType)) {
    		

			
			long startTime = System.currentTimeMillis();
			
			CallRoutingServiceV2.loadAlldata(true);
			
			long endTime = System.currentTimeMillis();

			
			response.getWriter().println("Loaded Buyers: "+ CallRoutingServiceV2.cachedBuyersList.size());
			response.getWriter().println("Loaded PhoneNumbers: "+ CallRoutingServiceV2.cachedPhoneNumbersMap.size());
			response.getWriter().println("Loaded Sources: "+ CallRoutingServiceV2.cachedTrafficsourcesMap.size());
			
			response.getWriter().println("Time Taken to reload the data: "+ (endTime-startTime) + " millliseconds");
			


		
    	}
    	
    }

}