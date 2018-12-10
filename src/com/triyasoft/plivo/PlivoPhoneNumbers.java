package com.triyasoft.plivo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.number.Number;
import com.plivo.helper.api.response.number.NumberSearchFactory;
import com.plivo.helper.exception.PlivoException;

public class PlivoPhoneNumbers {
	
	public static List<Number>  loadAllPlivoNumbers() {
		
        List<Number> allNumbers  = new ArrayList<Number>();

		
		String auth_id = "MAODJKN2MZOGE4OTNHNJ";
		String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";
	  
	    RestAPI api = new RestAPI(auth_id, auth_token, "v1");

	    // Get all numbers
	    
	    LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	    parameters.put("limit","20"); // Used to display the number of results per page. 
	    parameters.put("offset","0"); // Denotes the number of value items by which the results should be offset. 
	    
	    
	    
	    try{
	        NumberSearchFactory resp = api.getNumbers(parameters);
	     //   System.out.println(resp);
	        
	        List<Number> numbers1 = resp.numberList; 
	        
	        
	        api = new RestAPI(auth_id, auth_token, "v1");
	        parameters = new LinkedHashMap<String, String>();
	        parameters.put("limit","20"); // Used to display the number of results per page. 
	        parameters.put("offset","20"); // Denotes the number of value items by which the results should be offset. 
	        
	         resp = api.getNumbers(parameters);
	      ///  System.out.println(resp);
	        
	        List<Number> numbers2 = resp.numberList; 
	        
	        allNumbers.addAll(numbers1);
	        allNumbers.addAll(numbers2);
	        System.out.println(allNumbers.size());
	        
	        
	    }catch (PlivoException e){  
	        System.out.println(e.getLocalizedMessage());            
	    }

		
	    return allNumbers;
	}
public static void main(String[] args) {
	
}
}
