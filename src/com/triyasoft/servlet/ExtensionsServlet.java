package com.triyasoft.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.ExtensionsDao;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.PlivoEndpointExtention;
import com.triyasoft.plivo.PlivoEndPointUtil;
import com.triyasoft.utils.CallRoutingServiceV2;
import com.triyasoft.utils.CallcenterCallAllocationService;
import com.triyasoft.utils.JsonWrapper;
import com.triyasoft.utils.JsonWrapper1;

@WebServlet("/extension")
public class ExtensionsServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		if ("deleteExtension".equals(requestType)) {

			String id = request.getParameter("id");
			String plivoEndPointid = ExtensionsDao.getPlivoEndpointId(id);
			PlivoEndPointUtil.deletePlivoEndPoint(plivoEndPointid);

			ExtensionsDao.deleteExtension(id);

			CallcenterCallAllocationService.loadInitdata();

			response.getWriter().print("{\"Result\":\"OK\"}");

		}

		if ("loadExtension".equals(requestType)) {

			List<PlivoEndpointExtention> lst = null;

			lst = ExtensionsDao.loadAllExtensions();

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

		if ("addExtension".equals(requestType)) {

			String extension_name = request.getParameter("extension_name");
			System.out.println("extension_name:" + extension_name);

			String plivo_endpoint_user_name = request
					.getParameter("plivo_endpoint_user_name");
			System.out.println("plivo_endpoint_user_name:"
					+ plivo_endpoint_user_name);

			String plivo_endpoint_user_password = request
					.getParameter("plivo_endpoint_user_password");
			System.out.println("plivo_endpoint_user_password:"
					+ plivo_endpoint_user_password);

			String is_active = request.getParameter("isactive");
			if (is_active == null)
				is_active = "0";

			System.out.println("is_active:" + is_active);

			PlivoEndpointExtention endpointExtention = new PlivoEndpointExtention();
			endpointExtention.setExtension_name(extension_name);
			endpointExtention.setPlivo_endpoint_user_alias(extension_name);
			endpointExtention
					.setPlivo_endpoint_user_name(plivo_endpoint_user_name);
			endpointExtention
					.setPlivo_endpoint_user_password(plivo_endpoint_user_password);
			endpointExtention.setIsactive(Integer.parseInt(is_active));

			endpointExtention = PlivoEndPointUtil
					.createPlivoEndPoint(endpointExtention);

			String sipurl = "sip:"
					+ endpointExtention.getPlivo_endpoint_user_id() + "@"
					+ endpointExtention.getPlivo_end_point_domain();
			endpointExtention.setSip_url(sipurl);

			ExtensionsDao.addExtension(endpointExtention);
			CallcenterCallAllocationService.loadInitdata();

			// Buyer buyer =
			// BuyerDaoService.addBuyer(buyer_name,buyer_number,weight,tier,concurrency_cap_limit,bid_price,is_active,buyer_daily_cap);

			Gson gson = new Gson();

			JsonWrapper1 wrapper = new JsonWrapper1();
			wrapper.setResult("OK");
			wrapper.setRecords(endpointExtention);

			String jsonInString = gson.toJson(wrapper);

			response.getWriter().print(jsonInString);

		}

	}

}
