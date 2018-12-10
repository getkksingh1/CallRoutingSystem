package com.triyasoft.plivo;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import com.plivo.helper.exception.PlivoException;
import com.triyasoft.daos.AuditDao;

public class XPlivoSignature {

	public static void main(String[] args) throws PlivoException {

		System.out
				.println(getSignedHash(
						"http://52.42.152.129/CallTracker/plivo?requestType=hangupBillDuration0BillRate0.021CallStatusno-answerCallUUIDa633d74c-5e6a-11e7-885a-0d39f994073eCallerNameskypeDirectioninboundDuration0EndTime2017-07-01 14:36:20EventHangupFrom16613804714HangupCauseCALL_REJECTEDStartTime2017-07-01 14:36:20To18662145075TotalCost0.00000requestTypehangup",
						"YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz"));

	}

	public static void auditPlivoSignature(HttpServletRequest request,
			String additionalParam) {

		String authToken = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";
		Enumeration<String> parameterNames = request.getParameterNames();
		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		String plivoSignature = request.getHeader("X-Plivo-Signature");
		String uri = "http://52.42.152.129/CallTracker/plivo" + additionalParam;
		String uuid = request.getParameter("CallUUID");

		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();
			String paramValue = request.getParameter(paramName);
			parameters.put(paramName, paramValue);

		}

		Map<String, String> sortedParams = new TreeMap<String, String>(
				parameters);
		for (Entry<String, String> pair : sortedParams.entrySet()) {
			uri += pair.getKey() + pair.getValue();
		}

		String mySignatureHash = getSignedHash(uri, authToken);

		AuditDao.auditPlivoSignature(uuid, uri, plivoSignature, mySignatureHash);

	}

	public static String getSignedHash(String uri, String authToken) {

		String signature = "";

		try {
			byte[] keyBytes = authToken.getBytes();
			byte[] textBytes = uri.getBytes();
			Mac hmac = Mac.getInstance("HmacSHA1");
			SecretKeySpec macKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			hmac.init(macKey);
			byte[] signBytes = hmac.doFinal(textBytes);
			signature = new String(Base64.encodeBase64(signBytes));

		} catch (Exception e) {
			// throw new PlivoException(e.getLocalizedMessage());
		}

		return signature;
	}

	public static Boolean verify(String uri,
			LinkedHashMap<String, String> parameters, String xsignature,
			String authToken) throws PlivoException {
		Boolean isMatch = false;
		Map<String, String> sortedParams = new TreeMap<String, String>(
				parameters);
		for (Entry<String, String> pair : sortedParams.entrySet()) {
			uri += pair.getKey() + pair.getValue();
		}

		try {
			byte[] keyBytes = authToken.getBytes();
			byte[] textBytes = uri.getBytes();
			Mac hmac = Mac.getInstance("HmacSHA1");
			SecretKeySpec macKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			hmac.init(macKey);
			byte[] signBytes = hmac.doFinal(textBytes);
			String signature = new String(Base64.encodeBase64(signBytes));
			if (signature.equals(xsignature))
				isMatch = true;
		} catch (Exception e) {
			throw new PlivoException(e.getLocalizedMessage());
		}

		return isMatch;
	}
}