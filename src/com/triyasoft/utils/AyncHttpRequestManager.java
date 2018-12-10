package com.triyasoft.utils;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.exception.PlivoException;

public class AyncHttpRequestManager {

	public static int callCount = 0;
	public static long startTime = System.currentTimeMillis();

	public static void main(String[] args) {

		for (int i = 0; i < 10; i++) {
			initiateCallRecording("kjfkjf");
		}

	}

	static ExecutorService myExecutor = Executors.newCachedThreadPool(); // or
																			// whatever

	static void initiateCallRecording(final String calluuid) {
		myExecutor.execute(new Runnable() {
			public void run() {
				fireCall(calluuid);
			}

			private void fireCall(String uuid1) {

				String api_key = "MAODJKN2MZOGE4OTNHNJ";
				String api_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";
				RestAPI api = new RestAPI(api_key, api_token, "v1");

				LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

				params.put("from", "18006789143");
				params.put("to", "18557669909");

				String appContext = "CallTracker";
				String legBId = "123";
				String conferenceID = "456";
				String uuid = "abcd" + callCount;

				params.put("ring_timeout", "30");
				params.put("fallback_url", "http://52.42.152.129/" + appContext
						+ "/plivooutboundcall?requestType=fallback&legBId="
						+ legBId + "&conferenceID=" + conferenceID
						+ "&parentuuid=" + uuid);
				params.put("hangup_url", "http://52.42.152.129/" + appContext
						+ "/plivooutboundcall?requestType=hangup&legBId="
						+ legBId + "&conferenceID=" + conferenceID
						+ "&parentuuid=" + uuid);
				params.put("ring_url", "http://52.42.152.129/" + appContext
						+ "/plivooutboundcall?requestType=ring&legBId="
						+ legBId + "&conferenceID=" + conferenceID
						+ "&parentuuid=" + uuid);
				params.put("answer_url", "http://52.42.152.129/" + appContext
						+ "/plivooutboundcall?requestType=answer&legBId="
						+ legBId + "&conferenceID=" + conferenceID
						+ "&parentuuid=" + uuid);
				params.put("url", "http://52.42.152.129/" + appContext
						+ "/plivooutboundcall?requestType=answer&legBId="
						+ legBId + "&conferenceID=" + conferenceID
						+ "&parentuuid=" + uuid);

				try {
					Call call = api.makeCall(params);

					System.out.println(call.apiId);
					System.out.println(call.message);

				} catch (PlivoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Current Time: "
						+ (System.currentTimeMillis() - startTime));

				try {
					// Record resp1 = api1.record(parameters1);

					// System.out.println(resp1.url);

					// CallsDaoService.updateRecordingURL(uuid, resp1.url);

					System.out.println("Firing Call " + (callCount++));

				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());

				}

			}
		});
	}

}
