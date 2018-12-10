package com.triyasoft.plivo;

import java.util.LinkedHashMap;
import java.util.List;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.endpoint.Endpoint;
import com.plivo.helper.api.response.endpoint.EndpointFactory;
import com.plivo.helper.api.response.response.GenericResponse;
import com.plivo.helper.exception.PlivoException;
import com.triyasoft.model.PlivoEndpointExtention;

public class PlivoEndPointUtil {

	public static String  auth_id = "MAODJKN2MZOGE4OTNHNJ";
	public static  String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";
	
	
	
	public static List<Endpoint> getAllEndpointsStatus() {
        
			RestAPI api = new RestAPI(auth_id, auth_token, "v1");

		
		 LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	        parameters.put("limit","10"); // The number of results per page
	        parameters.put("offset","0"); // The number of value items by which the results should be offset

	        try {
	            EndpointFactory resp = api.getEndpoints(parameters);
	            List<Endpoint> endpoints  = resp.endpointList;
	           // endpoints.get(0)
	           System.out.println(resp);
	            
	            return endpoints;
	           
	            
	        }catch (PlivoException e){
	            System.out.println(e.getLocalizedMessage());
	        }
		
	        return null;
	}
	
	public static PlivoEndpointExtention createPlivoEndPoint(PlivoEndpointExtention endpointExtention) {
		
	    
        RestAPI api = new RestAPI(auth_id, auth_token, "v1");
        
        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("username",endpointExtention.getPlivo_endpoint_user_name()); // The username for the endpoint to be created
        parameters.put("password",endpointExtention.getPlivo_endpoint_user_password()); // The password for your endpoint username
        parameters.put("alias", endpointExtention.getPlivo_endpoint_user_alias()); // Alias for this endpoint
        
        // Create an Endpoint
        try {
            Endpoint endpoint = api.createEndpoint(parameters);
            
            endpointExtention.setPlivo_endpoint_user_id(endpoint.username);
            endpointExtention.setPlivo_endpoint_id(endpoint.endpointId);
            endpointExtention.setPlivo_end_point_domain("phone.plivo.com");
            String endPointAlias = endpoint.alias;
            System.out.println(endPointAlias);
            
            
            System.out.println(endpoint);
        }catch (PlivoException e){
            System.out.println(e.getLocalizedMessage());
        }
	
		
		
		return endpointExtention;
		
		
	}
	
	public static void main(String[] args) {
		
		getAllEndpointsStatus();
		
	}

	public static void deletePlivoEndPoint(String plivoEndPointid) {
		
        RestAPI api = new RestAPI(auth_id, auth_token, "v1");

		 LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
	        parameters.put("endpoint_id",plivoEndPointid); // ID of the endpoint that as to be deleted

	        try {
	            GenericResponse resp = api.deleteEndpoint(parameters);
	            System.out.println(resp);
	        }catch (PlivoException e){
	            System.out.println(e.getLocalizedMessage());
	        }
		
	}
}
