package com.triyasoft.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.BuyerSourcePreferenceFilterDao;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.BuyerSourcePreferenceFilter;
import com.triyasoft.model.Option;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.JsonOptionsWrapper;
import com.triyasoft.utils.JsonWrapper;
import com.triyasoft.utils.JsonWrapper1;

@WebServlet("/filters")
public class FiltersServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		if ("loadfilters".equals(requestType)) {

			String jtSorting = request.getParameter("jtSorting");

			List<BuyerSourcePreferenceFilter> lst = BuyerSourcePreferenceFilterDao
					.loadAllFiltersIncludingInActive();

			Gson gson = new Gson();

			JsonWrapper wrapper = new JsonWrapper();
			wrapper.setResult("OK");

			wrapper.setTotalRecordCount(lst.size());

			Object[] objarr = lst.toArray();

			wrapper.setRecords(lst.toArray());

			// 2. Java object to JSON, and assign to a String
			String jsonInString = gson.toJson(wrapper);

			response.getWriter().print(jsonInString);

		}

		if ("deletefilter".equals(requestType)) {

			String id = request.getParameter("id");
			System.out.println("Removing filter with id :" + id);
			BuyerSourcePreferenceFilterDao.deleteFilter(id);
			response.getWriter().print("{\"Result\":\"OK\"}");

			BuyerSourcePreferenceFilterDao.loadAllFilters();

		}

		if ("updatefilter".equals(requestType)) {

			String id = request.getParameter("id");
			System.out.println("buyer_id:" + id);

			String source_id = request.getParameter("source_id");
			System.out.println("source_id:" + source_id);

			String operator = request.getParameter("operator");
			System.out.println("operator:" + operator);

			String buyer_id = request.getParameter("buyer_id");
			System.out.println("buyer_id:" + buyer_id);

			String is_active = request.getParameter("is_active");
			System.out.println("is_active:" + is_active);

			if (is_active == null)
				is_active = "0";

			System.out.println("is_active:" + is_active);

			BuyerSourcePreferenceFilterDao.updateFilter(id, source_id,
					operator, buyer_id, is_active);

			BuyerSourcePreferenceFilterDao.loadAllFilters();

			response.getWriter().print("{\"Result\":\"OK\"}");

		}

		if ("addfilter".equals(requestType)) {

			String source_id = request.getParameter("source_id");
			System.out.println("source_id:" + source_id);

			String operator = request.getParameter("operator");
			System.out.println("operator:" + operator);

			String buyer_id = request.getParameter("buyer_id");
			System.out.println("buyer_id:" + buyer_id);

			String is_active = request.getParameter("is_active");
			System.out.println("is_active:" + is_active);

			if (is_active == null)
				is_active = "0";

			System.out.println("is_active:" + is_active);

			BuyerSourcePreferenceFilter filter = BuyerSourcePreferenceFilterDao
					.addFilter(source_id, operator, buyer_id, is_active);

			Gson gson = new Gson();

			JsonWrapper1 wrapper = new JsonWrapper1();
			wrapper.setResult("OK");
			wrapper.setRecords(filter);

			String jsonInString = gson.toJson(wrapper);

			response.getWriter().print(jsonInString);

			BuyerSourcePreferenceFilterDao.loadAllFilters();

		}

		if ("loadBuyersOption".equals(requestType)) {

			Gson gson = new Gson();

			System.out.println("Reaching here");

			JsonOptionsWrapper wrapper = new JsonOptionsWrapper();
			wrapper.setResult("OK");

			List<Buyer> lst = BuyerDaoService.loadAllBuyers();
			List<Option> sourceOptions = new ArrayList<Option>();

			for (Buyer buyer : lst) {

				Option option = new Option();
				option.setDisplayText(buyer.getBuyer_name());
				option.setValue(buyer.getBuyer_id() + "");
				sourceOptions.add(option);
			}

			Object[] objarr = sourceOptions.toArray();

			wrapper.setOptions(objarr);

			// 2. Java object to JSON, and assign to a String
			String jsonInString = gson.toJson(wrapper);

			response.getWriter().print(jsonInString);

		}

	}

	public static void main(String[] args) {

		/*
		 * Map<Integer, Buyer> buyersMap = CallRoutingService.loadBuyers(false);
		 * Iterator it = buyersMap.entrySet().iterator(); while (it.hasNext()) {
		 * Map.Entry pair = (Map.Entry)it.next();
		 * System.out.println(pair.getKey() + " = " + pair.getValue()); Buyer
		 * buyer = (Buyer) pair.getValue() ; System.out.println(buyer);
		 * it.remove(); // avoids a ConcurrentModificationException }
		 */

		Map<Integer, PhoneNumber> phoneNumbersMap = CallRoutingServiceV2
				.loadPhoneNumbers(false);

		Iterator it = phoneNumbersMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			PhoneNumber phoneNumber = (PhoneNumber) pair.getValue();
			System.out.println(phoneNumber);
			it.remove(); // avoids a ConcurrentModificationException
		}

		/*
		 * Map<Integer, TrafficSource> trafficSourceMap =
		 * CallRoutingService.loadTrafficSources(false); Iterator it =
		 * trafficSourceMap.entrySet().iterator(); while (it.hasNext()) {
		 * Map.Entry pair = (Map.Entry)it.next();
		 * System.out.println(pair.getKey() + " = " + pair.getValue());
		 * TrafficSource trafficSource = (TrafficSource) pair.getValue() ;
		 * System.out.println(trafficSource); it.remove(); // avoids a
		 * ConcurrentModificationException }
		 */
	}

}
