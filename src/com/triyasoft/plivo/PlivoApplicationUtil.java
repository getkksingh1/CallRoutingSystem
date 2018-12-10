package com.triyasoft.plivo;

import java.util.LinkedHashMap;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.application.Application;
import com.plivo.helper.exception.PlivoException;

public class PlivoApplicationUtil {

	public static String auth_id = "MAODJKN2MZOGE4OTNHNJ";
	public static String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";

	public static void main(String[] args) {

		RestAPI api = new RestAPI(auth_id, auth_token, "v1");
		String appName = "Opulence-Systems";

		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("answer_url", "http://52.42.152.129/" + appName
				+ "/plivoinboundcall?requestType=answer");
		parameters.put("message_url", "http://52.42.152.129/" + appName
				+ "/plivoinboundcall?requestType=message");
		parameters.put("hangup_url", "http://52.42.152.129/" + appName
				+ "/plivoinboundcall?requestType=hangup");
		parameters.put("fallback_answer_url", "http://52.42.152.129/" + appName
				+ "/plivoinboundcall?requestType=answer");

		parameters.put("app_name", "HTA-" + appName);

		try {
			Application resp = api.createApplication(parameters);
			System.out.println((resp));
		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

}
