package com.triyasoft.plivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.exception.PlivoException;

public class MaikeCall {
	public static String  auth_id = "XXXXXXXX";
	public static  String auth_token = "XXXXXXX";


public static void main(String[] args) {
	
/*	if(true) {
		for(int i = 0 ;  i <200 ;i++)
		System.out.println(getRandomCallerID());
		System.exit(0);
	}
	*/
	try {

        File f = new File("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/plivo/numbers.txt");

        BufferedReader b = new BufferedReader(new FileReader(f));

        String readLine = "";
  
  
    
   
    int counter = 10;
    while ((readLine = b.readLine()) != null) {
    	System.out.println("Current value of counter:"+counter);
    	if(counter > 50)
    		break;
    	  RestAPI api = new RestAPI(auth_id, auth_token, "v1");
    	    

    	LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        System.out.println("Calling Number"+readLine);
        parameters.put("to",readLine.trim()); // The phone numer to which the all has to be placed
        
        String callerId = getRandomCallerID();
        
        parameters.put("from", callerId); // The phone number to be used as the caller id

        
     //   parameters.put("from","18882117049"); // The phone number to be used as the caller id
        parameters.put("answer_url","http://52.42.152.129/RingPool/plivoinboundcall?requestType=answer"); // The URL invoked by Plivo when the outbound call is answered
        parameters.put("answer_method","POST"); // method to invoke the answer_url

        try {
           Call resp = api.makeCall(parameters);
           System.out.println(resp);
           try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        }catch (PlivoException e){  
           System.out.println(e.getLocalizedMessage());
        }

    }


    
 
    
	} catch (IOException e) {
	    e.printStackTrace();
	}
}


private static String getRandomCallerID() {

	String phoneNumber = "";
	for(int i = 0 ; i <10;i++)
		phoneNumber = phoneNumber+getRandomInt();
	
	return phoneNumber;
}

public static int getRandomInt() {
	
	
	Random rn = new Random();
	int i = rn.nextInt(7) ;
	return i+2;

}
}
