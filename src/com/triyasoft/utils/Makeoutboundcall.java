package com.triyasoft.utils;

import java.util.LinkedHashMap;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.exception.PlivoException;

public class Makeoutboundcall {
	public static void main(String[] args) throws IllegalAccessException {
		String auth_id = "MAMMQ5NTC2ZMM2MTY0YZ";
		String auth_token = "YTE1N2ViYTNjNWNhOWY1NzgxMGYyNTZhMTVmYWE4";
		RestAPI api = new RestAPI(auth_id, auth_token, "v1");

		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("to", "919899016401"); // The phone numer to which the
												// all has to be placed
		parameters.put("from", "18442076807 "); // The phone number to be used
												// as the caller id

		// answer_url is the URL invoked by Plivo when the outbound call is
		// answered
		// and contains instructions telling Plivo what to do with the call
		parameters
				.put("answer_url",
						"https://s3-ap-southeast-1.amazonaws.com/aol-noida/sadhana.xml");
		parameters.put("answer_method", "GET"); // method to invoke the
												// answer_url

		// Example for asynchronous request
		// callback_url is the URL to which the API response is sent.
		// parameters.put("callback_url","http://myvoiceapp.com/callback/");
		// parameters.put("callback_method","GET"); // The method used to notify
		// the callback_url.
		try {
			// Make an outbound call and print the response
			Call resp = api.makeCall(parameters);
			System.out.println(resp);
		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}