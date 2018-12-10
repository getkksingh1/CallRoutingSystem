package com.triyasoft.utils;

import org.asynchttpclient.*;

public class AsyncClient {

	public static void main(String[] args) {
		AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
		asyncHttpClient.prepareGet("http://www.google.com/").execute(new AsyncCompletionHandler<Response>(){
		    
		    @Override
		    public Response onCompleted(Response response) throws Exception{
		       
				System.out.println("After request completion");

		    	
		        return response;
		    }
		    
		    @Override
		    public void onThrowable(Throwable t){
		        // Something wrong happened.
		    }
		});
		
		
		System.out.println("After function call");
		
		
	}
	
}
