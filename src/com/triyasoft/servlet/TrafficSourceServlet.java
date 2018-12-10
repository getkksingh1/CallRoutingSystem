package com.triyasoft.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.TrafficSourceDaoService;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.Option;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.JsonOptionsWrapper;
import com.triyasoft.utils.JsonWrapper;
import com.triyasoft.utils.JsonWrapper1;


@WebServlet("/sources")
public class TrafficSourceServlet extends HttpServlet{


	

	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
				 
					String requestType = request.getParameter("requestType");

							
							//"18003031147";
					
					
					
					if("loadSources".equals(requestType)) {
						
					
						
						Gson gson = new Gson();
						
						
						JsonWrapper wrapper = new JsonWrapper();
						wrapper.setResult("OK");
						
						List<TrafficSource> lst = null;
						
						String jtSorting = request.getParameter("jtSorting");
						
						if(jtSorting !=null && jtSorting.length()>0)
							lst  = TrafficSourceDaoService.loadAllSources(jtSorting);
						else		
							lst = TrafficSourceDaoService.loadAllSources();
						
						wrapper.setTotalRecordCount(lst.size());

						
						
					
						Object[] objarr = lst.toArray();
						
						wrapper.setRecords(lst.toArray());

						
						// 2. Java object to JSON, and assign to a String
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);

					}			
					
			
					
					if("deleteSource".equals(requestType)) {
						
						String id =  request.getParameter("id");
						System.out.println("Removing traffic source with id :"+id);
						TrafficSourceDaoService.deleteSource(id);
						response.getWriter().print("{\"Result\":\"OK\"}");
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					if("updateSource".equals(requestType)) {
						
						String id = request.getParameter("id");
						System.out.println("id:"+id);
						
						String first_name = request.getParameter("first_name");
						System.out.println("first_name:"+first_name);

						String last_name = request.getParameter("last_name");
						System.out.println("last_name:"+last_name);

					
						String is_active = request.getParameter("is_Active");
						
						if(is_active == null)
							is_active = "0";
						
						System.out.println("is_active:"+is_active);

						TrafficSourceDaoService.updateSource(id,first_name,last_name,is_active);



						

						response.getWriter().print("{\"Result\":\"OK\"}");
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					if("addSource".equals(requestType)) {
						
						String first_name = request.getParameter("first_name");
						System.out.println("first_name:"+first_name);

						String last_name = request.getParameter("last_name");
						System.out.println("last_name:"+last_name);

					
						String is_active = request.getParameter("is_Active");
						
						if(is_active == null)
							is_active = "0";
						
						System.out.println("is_active:"+is_active);
						
						
						TrafficSource buyer = TrafficSourceDaoService.addTrafficSource(first_name, last_name, is_active);

						Gson gson = new Gson();

						
						JsonWrapper1 wrapper = new JsonWrapper1();
						wrapper.setResult("OK");
						wrapper.setRecords(buyer);
						
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					
					if("loadSourcesOption".equals(requestType)) { 
						

						
					
						
						Gson gson = new Gson();
						
						System.out.println("Reaching here");
						
						JsonOptionsWrapper wrapper = new JsonOptionsWrapper();
						wrapper.setResult("OK");
						
						List<TrafficSource> lst = TrafficSourceDaoService.loadAllSources();
						List<Option> sourceOptions = new ArrayList<Option>();
						
						for (TrafficSource source : lst) {
							
							Option option = new Option();
							option.setDisplayText(source.getFirst_name()+":"+ source.getLast_name());
							option.setValue(source.getId()+"");
							sourceOptions.add(option);
						}
						
						

						
						
					
						Object[] objarr = sourceOptions.toArray();
						
						wrapper.setOptions(objarr);

						
						// 2. Java object to JSON, and assign to a String
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);

					
						
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
