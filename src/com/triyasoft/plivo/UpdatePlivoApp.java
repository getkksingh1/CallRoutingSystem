package com.triyasoft.plivo;

import java.util.LinkedHashMap;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.application.Application;
import com.plivo.helper.api.response.response.GenericResponse;
import com.plivo.helper.exception.PlivoException;

public class UpdatePlivoApp {
	public static void main(String[] args) throws Exception {

		String auth_id = "MAODJKN2MZOGE4OTNHNJ";
		String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";

		RestAPI api = new RestAPI(auth_id, auth_token, "v1");

		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("app_id", "91076396410618848"); // ID of the application
														// for which has to be
														// modified
		// parameters.put("answer_url", "http://exampletest.com"); // Values
		// that have to be updated
		parameters.put("answer_url",
				"http://52.42.152.129/amit/plivo?requestType=answer"); // Values
																		// that
																		// have
																		// to be
																		// updated

		//
		try {
			Application resp = api.editApplication(parameters);
			System.out.println(resp);
		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
