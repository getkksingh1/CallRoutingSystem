package com.triyasoft.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.daos.SourcePhoneNumberMappingDao;
import com.triyasoft.model.PhoneNumber;

@WebServlet("/getphoneNumber")
public class NumberAllocatorServlet extends HttpServlet {

	public static void main(String[] args) {
		String phoneNumber = "18438254185";
		getFormattedNumber(phoneNumber);
	}

	public static String getFormattedNumber(String phoneNumber) {

		String internationalCode = phoneNumber.substring(0, 1);
		String first3Digits = phoneNumber.substring(1, 4);
		String next3Digits = phoneNumber.substring(4, 7);
		String last4Digits = phoneNumber.substring(7, 11);
		String formattedNumber = internationalCode + "-" + first3Digits + "-"
				+ next3Digits + "-" + last4Digits;
		return formattedNumber;
	}

	public static boolean areActiveNmunbersLoaded = false;
	public static int currentAllocatedIndex = 0;

	public static List<PhoneNumber> allActiveNumbers = null;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!areActiveNmunbersLoaded) {
			allActiveNumbers = PhoneNumberDaoService.loadAllActiveNumbers();
			areActiveNmunbersLoaded = true;
		}

		String requestType = request.getParameter("requestType");

		if ("reloadNumbers".equals(requestType)) {
			allActiveNumbers = PhoneNumberDaoService.loadAllActiveNumbers();
			areActiveNmunbersLoaded = true;
			return;

		}

		if ("allocateNumber".equals(requestType)) {

			// String sourceParamName =
			// request.getParameter("sourceparametername");
			// String network = request.getParameter("network");

			String zoneidParameter = request.getParameter("zoneparam");
			if (zoneidParameter == null || zoneidParameter.trim().length() == 0)
				zoneidParameter = "zone";

			String sourceid = request.getParameter(zoneidParameter);
			String phoneNumber = "";

			if (sourceid == null || sourceid.trim().length() == 0)
				sourceid = "blanksource";

			phoneNumber = SourcePhoneNumberMappingDao
					.getPhoneNumberforSource(sourceid);

			if (phoneNumber == null) {
				if (currentAllocatedIndex >= allActiveNumbers.size()) {
					currentAllocatedIndex = 0;
				}

				phoneNumber = allActiveNumbers.get(currentAllocatedIndex)
						.getNumber();

				SourcePhoneNumberMappingDao.saveSourceIdPhoneNumberMapping(
						sourceid, phoneNumber);

				currentAllocatedIndex++;
			}

			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			String callback = request.getParameter("callback");

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("phoneNumber", getFormattedNumber(phoneNumber));
				jsonObject.put("zoneid", sourceid);
				jsonObject.put("zoneidParameter", zoneidParameter);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.getWriter().print(callback + "(");
			response.getWriter().print(jsonObject);
			response.getWriter().print(")");

		}

	}
}
