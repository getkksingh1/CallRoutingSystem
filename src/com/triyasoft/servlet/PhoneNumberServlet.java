package com.triyasoft.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.daos.TrafficSourceDaoService;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.JsonWrapper;
import com.triyasoft.utils.JsonWrapper1;

@WebServlet("/numbers")
public class PhoneNumberServlet extends HttpServlet{


	

	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
				 
					String requestType = request.getParameter("requestType");

							
							//"18003031147";
					
					if("loadNumbersByTrafficSource".equals(requestType)) {
						
						String jtSorting = request.getParameter("jtSorting");
						List<PhoneNumber> lst = null ;
						
						String trafficsource =  request.getParameter("traffic_source_id");
						lst = PhoneNumberDaoService.loadAllNumbersByTrafficSourceId(trafficsource);

					
						
						Gson gson = new Gson();
						
						
						JsonWrapper wrapper = new JsonWrapper();
						wrapper.setResult("OK");
						
						wrapper.setTotalRecordCount(lst.size());

						
						
					
						Object[] objarr = lst.toArray();
						
						wrapper.setRecords(lst.toArray());

						
						// 2. Java object to JSON, and assign to a String
						
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);

					
						
					}
					
					if("loadNumbers".equals(requestType)) {
						
						String jtSorting = request.getParameter("jtSorting");
						List<PhoneNumber> lst = null ;
						
						if(jtSorting !=null && jtSorting.length()>0)
							 lst = PhoneNumberDaoService.loadAllNumbers(jtSorting);
						else
							 lst = PhoneNumberDaoService.loadAllNumbers();

					
						
						Gson gson = new Gson();
						
						
						JsonWrapper wrapper = new JsonWrapper();
						wrapper.setResult("OK");
						
						wrapper.setTotalRecordCount(lst.size());

						
						
					
						Object[] objarr = lst.toArray();
						
						wrapper.setRecords(lst.toArray());

						
						// 2. Java object to JSON, and assign to a String
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);

					}			
					
			
					
					if("deleteNumber".equals(requestType)) {
						
						String id =  request.getParameter("id");
						System.out.println("Removing phone number with id :"+id);
						PhoneNumberDaoService.deleteNummber(id);
						response.getWriter().print("{\"Result\":\"OK\"}");
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					if("updateNumber".equals(requestType)) {
						
						String id = request.getParameter("id");
						System.out.println("id:"+id);
						
						String number = request.getParameter("number");
						System.out.println("number:"+number);

						String traffic_source_id = request.getParameter("traffic_source_id");
						System.out.println("traffic_source_id:"+traffic_source_id);
						
						String costpercall = request.getParameter("costpercall");
						System.out.println("costpercall:"+costpercall);

					
						String is_active = request.getParameter("is_Active");
						
						if(is_active == null)
							is_active = "0";
						
						System.out.println("is_active:"+is_active);
						

						PhoneNumberDaoService.updateNumber(id,number,traffic_source_id,is_active,costpercall);



						

						response.getWriter().print("{\"Result\":\"OK\"}");
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					if("addNumber".equals(requestType)) {
						
						String number = request.getParameter("number");
						System.out.println("number:"+number);

						String traffic_source_id = request.getParameter("traffic_source_id");
						System.out.println("traffic_source_id:"+traffic_source_id);

					
						String costpercall = request.getParameter("costpercall");
						System.out.println("costpercall:"+costpercall);

						
						String is_active = request.getParameter("is_Active");
						
						
						if(is_active == null)
							is_active = "0";
						
						System.out.println("is_active:"+is_active);
						
						
						PhoneNumber phoneNumber = PhoneNumberDaoService.addPhoneNumber(number, traffic_source_id, is_active ,costpercall);

						Gson gson = new Gson();

						
						JsonWrapper1 wrapper = new JsonWrapper1();
						wrapper.setResult("OK");
						wrapper.setRecords(phoneNumber);
						
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					

	 }
	 
	 
	 
	 public static void main(String[] args) {

		 /*
		 Map<Integer, Buyer> buyersMap = CallRoutingServiceV2.loadBuyers(false);
		 Iterator it = buyersMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        Buyer buyer = (Buyer) pair.getValue() ;
		        System.out.println(buyer);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		 
		 */
		 
		 
		 
		 Map<Integer, PhoneNumber> phoneNumbersMap = CallRoutingServiceV2.loadPhoneNumbers(false);
		

		 Iterator it = phoneNumbersMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        PhoneNumber phoneNumber = (PhoneNumber) pair.getValue() ;
		        System.out.println(phoneNumber);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    
		    
		 
		 /*
		 Map<Integer, TrafficSource> trafficSourceMap = CallRoutingServiceV2.loadTrafficSources(false);
		 Iterator it = trafficSourceMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        TrafficSource trafficSource = (TrafficSource) pair.getValue() ;
		        System.out.println(trafficSource);
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    */
	}
	 
}
