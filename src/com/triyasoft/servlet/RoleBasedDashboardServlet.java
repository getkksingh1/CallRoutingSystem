package com.triyasoft.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.exception.PlivoException;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.DashboardDataModel;
import com.triyasoft.model.DashboardDataModel.OnGoingCalls;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary.TrafficSourceSummary;
import com.triyasoft.model.HeaderSummaryModel;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.model.UserModel;
import com.triyasoft.ui.DashboardUIGenerator;

@WebServlet("/rolebaseddashboard")
public class RoleBasedDashboardServlet extends HttpServlet {

	public static List<CallModel> todaysCalls = new ArrayList<CallModel>();
	public static List<CallModel> yesterdayCalls = new ArrayList<CallModel>();
	public static List<Buyer> allBuyers = new ArrayList<Buyer>();
	public static long lasttimePlivoHit = System.currentTimeMillis();
	public boolean plivoBalanceFetched = false;
	public static double plivoBalance = 0.0;

	public static boolean isLoaded = false;
	public static long lasttimeDashBoardDataFectched = System
			.currentTimeMillis();

	private void loadData() {

		long duration = System.currentTimeMillis()
				- DashboardServlet.lasttimeDashBoardDataFectched;

		if ((!DashboardServlet.isLoaded) || (duration > 10 * 1000) || !isLoaded) {
			DashboardServlet.loadData();

			allBuyers = DashboardServlet.allBuyers;
			todaysCalls = DashboardServlet.todaysCalls;
			yesterdayCalls = DashboardServlet.yesterdayCalls;
			isLoaded = true;

		}

	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		DashboardUIGenerator dashboardUIGenerator = new DashboardUIGenerator();

		UserModel user = (UserModel) request.getSession().getAttribute("user");

		loadData();

		// "18003031147";

		if ("forceload".equals(requestType)) {
			isLoaded = false;

		}

		if ("headersummary".equals(requestType)) {

			HeaderSummaryModel hsm = new HeaderSummaryModel();

			hsm.setActiveCalls(getActiveCalls(request));

			String content = dashboardUIGenerator
					.generateHeaderStripRoleBased(hsm);

			// System.out.println(hsm);

			response.getWriter().print(content);

		}

		if ("maincontainer".equals(requestType)) {

			DashboardDataModel ddm = new DashboardDataModel();

			// Ongoing Calls Block
			OnGoingCalls ogc = ddm.getOnGoingCalls();
			ogc.setOngoingCalls(getActiveCalls(request));

			// Calls By Traffic Source

			TrafficSourcesSummary trafficSourcesSummary = ddm
					.getTrafficSourcesSummary();

			for (CallModel todayCall : todaysCalls) {

				int[] userAccountIds = user.getAccount_id();
				for (int i = 0; i < userAccountIds.length; i++) {

					if (todayCall.getTraffic_source_id() == userAccountIds[i]) {

						int trafficSourceId = todayCall.getTraffic_source_id();
						TrafficSourceSummary trafficSourceSummary = trafficSourcesSummary
								.getTrafficSourceSummary(trafficSourceId);

						if (trafficSourceSummary == null) {
							trafficSourceSummary = trafficSourcesSummary
									.getNewTrafficSourceSummaryInstance();
							trafficSourceSummary
									.setTrafficSourceId(trafficSourceId);
							trafficSourceSummary.setTrafficSourceName(todayCall
									.getTraffic_source());
						}

						trafficSourceSummary.incermentTodayCallsCount();
						String duration = todayCall.getDuration();

						if (duration == null || duration.length() == 0
								|| "null".equals(duration))
							duration = "0";

						trafficSourceSummary.addTotalCallTime(Double
								.parseDouble(duration));
						trafficSourceSummary.addToCallRevenue(todayCall
								.getTraffic_source_revenue());

						if (todayCall.getIs_running() == 1)
							trafficSourceSummary.incrementLiveCall();

						trafficSourceSummary.setAvgCall(trafficSourceSummary
								.getTotalCallTime()
								/ ((double) trafficSourceSummary
										.getTodayCalls()));

					}
				}
			}

			for (CallModel yDayCall : yesterdayCalls) {

				int[] userAccountIds = user.getAccount_id();
				for (int i = 0; i < userAccountIds.length; i++) {

					if (yDayCall.getTraffic_source_id() == userAccountIds[i]) {
						int trafficSourceId = yDayCall.getTraffic_source_id();
						TrafficSourceSummary trafficSourceSummary = trafficSourcesSummary
								.getTrafficSourceSummary(trafficSourceId);

						if (trafficSourceSummary == null) {
							trafficSourceSummary = trafficSourcesSummary
									.getNewTrafficSourceSummaryInstance();
							trafficSourceSummary
									.setTrafficSourceId(trafficSourceId);
							trafficSourceSummary.setTrafficSourceName(yDayCall
									.getTraffic_source());

						}

						trafficSourceSummary.incermentYdaytCallsCount();
					}

				}

			}

			// Setting dashboard source summary footer

			TrafficSourcesSummary sourcesSummary = ddm
					.getTrafficSourcesSummary();

			sourcesSummary.setSourceCount(sourcesSummary.getSourceSummaries()
					.size());
			sourcesSummary.setTotalCallCountToday(ddm.getBuyerConversion()
					.getTotalCallsToday());
			sourcesSummary.setTotalCallCountYday(ddm.getBuyerConversion()
					.getTotaslCallsYday());
			sourcesSummary.setTotalPayout(ddm.getBuyerRevenue()
					.getBuyerRevenueToday()
					- ddm.getPhoneCost().getPhoneCostToday()
					- ddm.getProfit().getProfitToday());
			sourcesSummary.setTotalLiveCalls(ddm.getOnGoingCalls()
					.getOngoingCalls());

			List<TrafficSourceSummary> summaries = sourcesSummary
					.getSourceSummaries();

			double totalCalltime = 0.0;
			int numberofcall = 0;

			for (TrafficSourceSummary trafficSourceSummary : summaries) {

				totalCalltime = totalCalltime
						+ trafficSourceSummary.getTotalCallTime();
				numberofcall = numberofcall
						+ trafficSourceSummary.getTodayCalls();
			}

			double callAvgTime = 0;
			if (numberofcall != 0)
				callAvgTime = totalCalltime / numberofcall;

			sourcesSummary.setOverAllavg(callAvgTime);

			Collections.sort(trafficSourcesSummary.getSourceSummaries());

			String daashboardPartialUI = dashboardUIGenerator
					.generateRoleBasedDashBoardPartialUI(ddm);

			response.getWriter().print(daashboardPartialUI);

		}

		if ("testcontainer".equals(requestType)) {

			DashboardDataModel dashboardDataModel = new DashboardDataModel();

			String daashboardPartialUI = dashboardUIGenerator
					.generateDashBoardPartialUI(dashboardDataModel);

		}

	}

	private int getBuyerWeitage(int buyerId) {

		for (Buyer buyer : allBuyers) {
			if (buyer.getBuyer_id() == buyerId)
				return buyer.getWeight();
		}

		return 0;

	}

	private int getBuyerTier(int buyerId) {
		for (Buyer buyer : allBuyers) {
			if (buyer.getBuyer_id() == buyerId)
				return buyer.getTier();
		}

		return 0;
	}

	private int checkIfBuyerActive(int buyerId) {
		for (Buyer buyer : allBuyers) {
			if (buyer.getBuyer_id() == buyerId)
				return buyer.getIs_active();
		}

		return 0;
	}

	private int getBuyerDailyCap(int buyerId) {
		for (Buyer buyer : allBuyers) {
			if (buyer.getBuyer_id() == buyerId)
				return buyer.getBuyer_daily_cap();
		}

		return 0;
	}

	private int getBuyerConcurrencyCap(int buyerId) {

		for (Buyer buyer : allBuyers) {
			if (buyer.getBuyer_id() == buyerId)
				return buyer.getConcurrency_cap_limit();
		}

		return 0;
	}

	private List<TrafficSource> getTrafficSourcesDeliveredCallsToDayORYesterday() {

		Map<Integer, TrafficSource> map = new HashMap<Integer, TrafficSource>();

		for (CallModel todayCall : todaysCalls) {
			map.put(todayCall.getCall_from_source().getId(),
					todayCall.getCall_from_source());

		}

		for (CallModel ydayCall : yesterdayCalls) {
			map.put(ydayCall.getCall_from_source().getId(),
					ydayCall.getCall_from_source());

		}

		return null;
	}

	private double getProfitOfXHrs(int hours) {

		double profit = 0;

		for (CallModel todayCall : todaysCalls) {
			long timeDiff = System.currentTimeMillis()
					- todayCall.getCallLandingTimeOnServer().getTime();
			if (timeDiff < hours * 3600 * 1000) {

				String phoneCost = todayCall.getTotalCost();

				if (phoneCost == null || phoneCost.length() == 0)
					phoneCost = "0.0";

				double currentCallProfit = todayCall.getBuyer_revenue()
						- todayCall.getTraffic_source_revenue()
						- Double.parseDouble(phoneCost);

				profit = profit + currentCallProfit;

			}

		}

		return profit;

	}

	private double getYdayProfit() {

		double profit = 0;

		for (CallModel yDayCall : yesterdayCalls) {

			String phoneCost = yDayCall.getTotalCost();

			if (phoneCost == null || phoneCost.length() == 0)
				phoneCost = "0.0";

			double currentCallProfit = yDayCall.getBuyer_revenue()
					- yDayCall.getTraffic_source_revenue()
					- Double.parseDouble(phoneCost);

			profit = profit + currentCallProfit;

		}

		return profit;

	}

	private double getTodayProfit() {

		double profit = 0;

		for (CallModel todayCall : todaysCalls) {

			String phoneCost = todayCall.getTotalCost();

			if (phoneCost == null || phoneCost.length() == 0)
				phoneCost = "0.0";

			double currentCallProfit = todayCall.getBuyer_revenue()
					- todayCall.getTraffic_source_revenue()
					- Double.parseDouble(phoneCost);

			profit = profit + currentCallProfit;

		}

		return profit;
	}

	private double getPhoneCostXHrs(int hours) {

		double phoneCost = 0;

		for (CallModel todayCall : todaysCalls) {
			long timeDiff = System.currentTimeMillis()
					- todayCall.getCallLandingTimeOnServer().getTime();
			if (timeDiff < hours * 3600 * 1000) {
				String callCost = todayCall.getTotalCost();

				if (callCost == null || callCost.length() == 0)
					callCost = "0.0";

				phoneCost = phoneCost + Double.parseDouble(callCost);
			}
		}

		return phoneCost;
	}

	private double getPhoneCostYday() {

		double phoneCost = 0;

		for (CallModel ydayCall : yesterdayCalls) {

			String callCost = ydayCall.getTotalCost();

			if (callCost == null || callCost.length() == 0)
				callCost = "0.0";

			phoneCost = phoneCost + Double.parseDouble(callCost);
		}

		return phoneCost;
	}

	private double getPhoneCostToday() {

		double phoneCost = 0;

		for (CallModel todayCall : todaysCalls) {

			String callCost = todayCall.getTotalCost();

			if (callCost == null || callCost.length() == 0)
				callCost = "0.0";

			phoneCost = phoneCost + Double.parseDouble(callCost);
		}

		return phoneCost;
	}

	private double getBuyerRevenueXHrs(int hours) {

		double revenue = 0;

		for (CallModel todayCall : todaysCalls) {
			long timeDiff = System.currentTimeMillis()
					- todayCall.getCallLandingTimeOnServer().getTime();
			if (timeDiff < hours * 3600 * 1000)
				revenue = revenue + todayCall.getBuyer_revenue();
		}

		return revenue;

	}

	private double getBuyerRevenueYday() {
		double totalRevenue = 0;
		for (CallModel yDayCall : yesterdayCalls) {

			if (yDayCall.getBuyer_id() != 0) {
				totalRevenue = totalRevenue + yDayCall.getBuyer_revenue();
			}
		}

		return totalRevenue;
	}

	private double getBuyerRevenueToday() {
		double totalRevenue = 0;
		for (CallModel todayCall : todaysCalls) {

			if (todayCall.getBuyer_id() != 0) {
				totalRevenue = totalRevenue + todayCall.getBuyer_revenue();
			}
		}

		return totalRevenue;
	}

	private int getTotalCallsInXHours(int hours) {

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			long timeDiff = System.currentTimeMillis()
					- todayCall.getCallLandingTimeOnServer().getTime();
			if (timeDiff < hours * 3600 * 1000)
				counter++;

		}

		return counter;
	}

	private int getBuyerConvertedXhours(int hours) {

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			if (todayCall.getBuyer_id() != 0) {
				long timeDiff = System.currentTimeMillis()
						- todayCall.getCallLandingTimeOnServer().getTime();
				if (timeDiff < hours * 3600 * 1000)
					counter++;
			}

		}

		return counter;
	}

	private int getBuyerConvertedYday() {

		int counter = 0;

		for (CallModel yDayCall : yesterdayCalls) {
			if (yDayCall.getBuyer_id() != 0) {
				counter++;
			}

		}

		return counter;
	}

	private int getMaxPausedCC() {

		int sum = 0;
		for (Buyer buyer : allBuyers) {
			if (buyer.getIs_active() == 0)
				sum = sum + buyer.getConcurrency_cap_limit();

		}
		return sum;
	}

	private int getPausedCC() {

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			if (todayCall.getIs_running() == 1) {
				Buyer buyer = getbuyer(todayCall.getBuyer_id());
				if (buyer != null) {
					if (buyer.getIs_active() == 0)
						counter++;
				}
			}

		}

		return counter;

	}

	private double getPlivoBalance() {

		// To do output to be cached for 1 minute

		long currentTime = System.currentTimeMillis();

		if ((currentTime - lasttimePlivoHit < 90 * 1000) && plivoBalanceFetched)
			return plivoBalance;

		String cashCredit = "0";

		String auth_id = "MAODJKN2MZOGE4OTNHNJ";
		String auth_token = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";

		RestAPI api = new RestAPI(auth_id, auth_token, "v1");

		// Get Details of account
		try {
			cashCredit = api.getAccount().cashCredits;
			plivoBalance = Double.parseDouble(cashCredit);
			;

			cashCredit = convertDouble2DigitPrecison(plivoBalance);
			plivoBalance = Double.parseDouble(cashCredit);
			;

			plivoBalanceFetched = true;

		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return plivoBalance;
	}

	public String convertDouble2DigitPrecison(double number) {
		String numberStr = "" + number;
		int indexofDot = numberStr.indexOf(".");

		if ((indexofDot + 3) >= numberStr.length())
			return numberStr;
		else
			return numberStr.substring(0, indexofDot + 3);

	}

	private double getRevenue() {

		double revenue = 0;

		for (CallModel todayCall : todaysCalls) {

			revenue = revenue + todayCall.getBuyer_revenue();
		}

		return revenue;
	}

	private int getConvertedCallsforToday() {

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			if (todayCall.getBuyer_id() != 0) {
				counter++;
			}

		}

		return counter;
	}

	private int getActiveCCCurrent() {

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			if (todayCall.getIs_running() == 1) {
				Buyer buyer = getbuyer(todayCall.getBuyer_id());
				if (buyer != null) {
					if (buyer.getIs_active() == 1)
						counter++;
				}
			}

		}

		return counter;

	}

	private Buyer getbuyer(int buyer_id) {
		for (Buyer buyer : allBuyers) {
			if (buyer.getBuyer_id() == buyer_id)
				return buyer;
		}

		return null;
	}

	private int getActiveCCmax() {

		int sum = 0;
		for (Buyer buyer : allBuyers) {
			if (buyer.getIs_active() == 1)
				sum = sum + buyer.getConcurrency_cap_limit();

		}
		return sum;
	}

	private int getActiveCalls(HttpServletRequest request) {

		UserModel user = (UserModel) request.getSession().getAttribute("user");

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			int[] userAccountIds = user.getAccount_id();
			for (int i = 0; i < userAccountIds.length; i++) {
				if (todayCall.getIs_running() == 1
						&& (userAccountIds[i] == todayCall
								.getTraffic_source_id()))
					counter++;
			}
		}

		return counter;
	}

	private String convertToDBSpecificNextDate(String endDate) {

		String strDate = null;
		java.util.Date result = null;

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			result = df.parse(endDate);
			long nextDateTime = result.getTime() + (86400 * 1000);
			result.setTime(nextDateTime);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			strDate = formatter.format(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strDate + " 00:00:00";
	}

	private String getTomorrowsDate() {

		java.util.Date today = new java.util.Date();
		long time = today.getTime();
		long tomorrowTime = time + (86400 * 1000);
		today.setTime(tomorrowTime);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(today);

		return strDate + " 00:00:00";
	}

	private String convertToDBSpecificDate(String startdate) {

		String strDate = null;
		java.util.Date result = null;

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			result = df.parse(startdate);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			strDate = formatter.format(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strDate + " 00:00:00";
	}

	private String getTodaysDate() {

		java.util.Date today = new java.util.Date();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(today);

		return strDate + " 00:00:00";
	}

	public static void main(String[] args) {

	}

}
