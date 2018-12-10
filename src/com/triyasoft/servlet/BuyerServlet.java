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
import com.triyasoft.daos.AuditDao;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.Option;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.JsonOptionsWrapper;
import com.triyasoft.utils.JsonWrapper;
import com.triyasoft.utils.JsonWrapper1;

@WebServlet("/buyers")
public class BuyerServlet extends HttpServlet  {
	

	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
				 
					String requestType = request.getParameter("requestType");

							
							//"18003031147";
					
					if("loadbyerbyBuyerId".equals(requestType)) { 
						
						String buyerId =  request.getParameter("buyerId");
						Buyer buyer = BuyerDaoService.getBuyerByByerId(Integer.parseInt(buyerId));
						Gson gson = new Gson();
						String jsonInString = gson.toJson(buyer);
						response.getWriter().print(jsonInString);

						
					}
					
						if("updateBuyerConcurrency".equals(requestType)) { 
						
						String buyerId =  request.getParameter("buyerId");
						String concurrency=  request.getParameter("concurrency");
						 BuyerDaoService.updateBuyerConcurrency(buyerId, concurrency);
						 AuditDao.auditHttpRequest("updateBuyerConcurrency", request);
						Gson gson = new Gson();
						String jsonInString = gson.toJson("Success");
						response.getWriter().print(jsonInString);
						CallRoutingServiceV2.loadAlldata(true);
						DashboardServlet.isLoaded = false;



						
					}
					
						if("updateBuyerTier".equals(requestType)) { 
							
							String buyerId =  request.getParameter("buyerId");
							String tier =  request.getParameter("tier");
							 BuyerDaoService.updateBuyerTier(buyerId, tier);
							 AuditDao.auditHttpRequest("updateBuyerTier", request);
							Gson gson = new Gson();
							String jsonInString = gson.toJson("Success");
							response.getWriter().print(jsonInString);
							CallRoutingServiceV2.loadAlldata(true);
							DashboardServlet.isLoaded = false;



							
						}
						
						if("updateBuyerWeight".equals(requestType)) { 
							
							String buyerId =  request.getParameter("buyerId");
							String weight =  request.getParameter("weight");
							 BuyerDaoService.updateBuyerWeight(buyerId, weight);
							 AuditDao.auditHttpRequest("updateBuyerWeight", request);
							 Gson gson = new Gson();
							String jsonInString = gson.toJson("Success");
							response.getWriter().print(jsonInString);
							CallRoutingServiceV2.loadAlldata(true);
							DashboardServlet.isLoaded = false;


							
						}
						
						if("updateBuyerState".equals(requestType)) { 
							
							 AuditDao.auditHttpRequest("updateBuyerState", request);

							String buyerId =  request.getParameter("buyerId");
							String state =  request.getParameter("state");
							 BuyerDaoService.updateBuyerState(buyerId, state);
							Gson gson = new Gson();
							String jsonInString = gson.toJson("Success");
							response.getWriter().print(jsonInString);
							CallRoutingServiceV2.loadAlldata(true);
							DashboardServlet.isLoaded = false;



							
						}
						
						
						if("updateBuyerDailyCap".equals(requestType)) { 
							
							 AuditDao.auditHttpRequest("updateBuyerDailyCap", request);

							String buyerId =  request.getParameter("buyerId");
							String dailycap=  request.getParameter("dailycap");
							 BuyerDaoService.updateBuyerDailyCap(buyerId, dailycap);
							Gson gson = new Gson();
							String jsonInString = gson.toJson("Success");
							response.getWriter().print(jsonInString);
							CallRoutingServiceV2.loadAlldata(true);
							DashboardServlet.isLoaded = false;



							
						}
						
						
					
					if("loadbyers".equals(requestType)) {
						
					
						String jtSorting = request.getParameter("jtSorting");
						
						List<Buyer> lst = null;
						
						if(jtSorting !=null && jtSorting.length()>0)
							lst = BuyerDaoService.loadAllBuyers(jtSorting);
						else
							lst = BuyerDaoService.loadAllBuyers();
						
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
					
			
					
					if("deleteBuyer".equals(requestType)) {
						
						String buyer_id =  request.getParameter("buyer_id");
						 AuditDao.auditHttpRequest("deleteBuyer", request);
						 System.out.println("Removing Buyer :"+buyer_id);
						BuyerDaoService.deleteBuyer(buyer_id);
						response.getWriter().print("{\"Result\":\"OK\"}");
						CallRoutingServiceV2.loadAlldata(true);
						
					}
					
					if("updateBuyer".equals(requestType)) {
						
						 AuditDao.auditHttpRequest("updateBuyer", request);

						String buyer_id =  request.getParameter("buyer_id");
						System.out.println("buyer_id:"+buyer_id);
						
						String buyer_name = request.getParameter("buyer_name");
						System.out.println("buyer_name:"+buyer_name);

						String buyer_number = request.getParameter("buyer_number");
						System.out.println("buyer_number:"+buyer_number);

						String weight = request.getParameter("weight");
						System.out.println("weight:"+weight);

						String tier = request.getParameter("tier");
						System.out.println("tier:"+tier);

						String concurrency_cap_limit = request.getParameter("concurrency_cap_limit");
						System.out.println("concurrency_cap_limit:"+concurrency_cap_limit);

						String bid_price = request.getParameter("bid_price");
						System.out.println("bid_price:"+bid_price);
						
						String buyer_daily_cap = request.getParameter("buyer_daily_cap");
						System.out.println("buyer_daily_cap:"+buyer_daily_cap);

						String ring_timeout = request.getParameter("ring_timeout");
						System.out.println("ring_timeout:"+ring_timeout);
						
						if(ring_timeout == null || ring_timeout.trim().length()==0)
							ring_timeout = "30";


						String is_active = request.getParameter("is_active");
						
						if(is_active == null)
							is_active = "0";
						
						System.out.println("is_active:"+is_active);

						System.out.println("Updating Buyer"+buyer_id);
						BuyerDaoService.updateBuyer(buyer_id,buyer_name,buyer_number,weight,tier,concurrency_cap_limit,bid_price,is_active,buyer_daily_cap,ring_timeout);



						

						response.getWriter().print("{\"Result\":\"OK\"}");
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					if("addBuyer".equals(requestType)) {
						
						 AuditDao.auditHttpRequest("addBuyer", request);

						String buyer_id =  request.getParameter("buyer_id");
						System.out.println("buyer_id:"+buyer_id);
						
						String buyer_name = request.getParameter("buyer_name");
						System.out.println("buyer_name:"+buyer_name);

						String buyer_number = request.getParameter("buyer_number");
						System.out.println("buyer_number:"+buyer_number);

						String weight = request.getParameter("weight");
						System.out.println("weight:"+weight);

						String tier = request.getParameter("tier");
						System.out.println("tier:"+tier);

						String concurrency_cap_limit = request.getParameter("concurrency_cap_limit");
						System.out.println("concurrency_cap_limit:"+concurrency_cap_limit);

						String bid_price = request.getParameter("bid_price");
						System.out.println("bid_price:"+bid_price);
						
						String buyer_daily_cap = request.getParameter("buyer_daily_cap");
						System.out.println("buyer_daily_cap:"+buyer_daily_cap);

						String is_active = request.getParameter("is_active");
						if(is_active == null)
							is_active = "0";
						
						System.out.println("is_active:"+is_active);

						
						Buyer buyer = BuyerDaoService.addBuyer(buyer_name,buyer_number,weight,tier,concurrency_cap_limit,bid_price,is_active,buyer_daily_cap);

						Gson gson = new Gson();

						
						JsonWrapper1 wrapper = new JsonWrapper1();
						wrapper.setResult("OK");
						wrapper.setRecords(buyer);
						
						String jsonInString = gson.toJson(wrapper);
						
						response.getWriter().print(jsonInString);
						CallRoutingServiceV2.loadAlldata(true);

						
					}
					
					if("loadBuyersOption".equals(requestType)) {
 
						

						
					
						
						Gson gson = new Gson();
						
						System.out.println("Reaching here");
						
						JsonOptionsWrapper wrapper = new JsonOptionsWrapper();
						wrapper.setResult("OK");
						
						List<Buyer> lst = BuyerDaoService.loadAllBuyers();
						List<Option> sourceOptions = new ArrayList<Option>();
						
						for (Buyer buyer : lst) {
							
							Option option = new Option();
							option.setDisplayText(buyer.getBuyer_name());
							option.setValue(buyer.getBuyer_id()+"");
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
