package com.triyasoft.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.ClickPhonenumberMappingDao;
import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.model.PhoneNumber;

@WebServlet("/getphoneNumberOnClick")
public class ClickToCallPhoneS2SAllocator extends HttpServlet{

	public static boolean areActiveNmunbersLoaded = false;
	
	public static List<PhoneNumber>  allActiveNumbersList = null;
	public static Map<String, PhoneNumber>  allActiveNumbersMap = null;
	
	public static List<PhoneNumber>  availableNumbersForAllocationList = null;
	public static Map<String, PhoneNumber>  availableNumbersForAllocationMap = null;
	
	public static List<String>  numbersInUse = null;
	

	

	
	
	
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
				 
		 	if(!areActiveNmunbersLoaded) {
		 		allActiveNumbersList =  PhoneNumberDaoService.loadAllActiveNumbers();
			  numbersInUse =  ClickPhonenumberMappingDao.loadAllInUserNumbers();
			  availableNumbersForAllocationMap = new HashMap<String, PhoneNumber> ();
			  allActiveNumbersMap = new HashMap<String, PhoneNumber> ();
			  availableNumbersForAllocationList = new ArrayList<PhoneNumber>();
			  for (PhoneNumber activeNumber : allActiveNumbersList) {
				  availableNumbersForAllocationMap.put(activeNumber.getNumber(), activeNumber);
				  availableNumbersForAllocationList.add(activeNumber);
				  allActiveNumbersMap.put(activeNumber.getNumber(), activeNumber);
			}
			  
			  if(numbersInUse !=null && numbersInUse.size()>0){
			 for (String numberInuse : numbersInUse) {
				if(availableNumbersForAllocationMap.get(numberInuse) !=null) {
					availableNumbersForAllocationList.remove(availableNumbersForAllocationMap.get(numberInuse));
					availableNumbersForAllocationMap.remove(numberInuse);
				}
			} 
		}
			
			  areActiveNmunbersLoaded = true;
		}
		 
		  
					String requestType = request.getParameter("requestType");
			
					
					if("allocateNumberonclick".equals(requestType)) {
						String defaultNumber =  "18884440325";
						String phoneNumber="";

						String clickID = request.getParameter("clickID");
						if(clickID == null || clickID.trim().length() ==0 || availableNumbersForAllocationList.size()==0)
							phoneNumber = defaultNumber;
						else{
							
							String number = ClickPhonenumberMappingDao.checkIfPhoneNumberExistForClick(clickID);
							
							if(number == null) {
							phoneNumber = availableNumbersForAllocationList.get(0).getNumber();
							ClickPhonenumberMappingDao.createClickRequest(clickID,phoneNumber);
							PhoneNumber phoneNumberObj = availableNumbersForAllocationMap.remove(phoneNumber);
							availableNumbersForAllocationList.remove(phoneNumberObj);
							numbersInUse.add(phoneNumberObj.getNumber());
							}
							else{
								phoneNumber =  number;
							}
						
						}
						
						response.getWriter().print(getFormattedNumber(phoneNumber));

					}
					
					
					
					if("releaseNumberonpageunload".equals(requestType)) {
						
						String clickID = request.getParameter("clickID");
						String phoneNumber = request.getParameter("phoneNumber");
						ClickPhonenumberMappingDao.releasePhoneNumber(clickID,phoneNumber);
						PhoneNumber phoneNumberObj = allActiveNumbersMap.get(phoneNumber);
						
						availableNumbersForAllocationList.add(phoneNumberObj);
						availableNumbersForAllocationMap.put(phoneNumber, phoneNumberObj);
						numbersInUse.remove(phoneNumberObj.getNumber());
						
						
					}
					
					if("releaseClicksNotConvertedForLong".equals(requestType)) {
						
					}
					
					
					
					if("reloadNumbers".equals(requestType)) {
						allActiveNumbersList =  PhoneNumberDaoService.loadAllActiveNumbers();
						  areActiveNmunbersLoaded = true;
						  return;

					}
					
					
	 }
	 
		public static String getFormattedNumber(String phoneNumber) {
			
			String internationalCode =  phoneNumber.substring(0,1);
			String first3Digits= phoneNumber.substring(1,4);
			String next3Digits =  phoneNumber.substring(4,7);
			String last4Digits = phoneNumber.substring(7,11);
			String formattedNumber= internationalCode+"-"+first3Digits+"-"+next3Digits+"-"+last4Digits;
			return formattedNumber;
		}

}
