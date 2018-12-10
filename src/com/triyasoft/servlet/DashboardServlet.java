package com.triyasoft.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.exception.PlivoException;
import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.DashboardDataModel;
import com.triyasoft.model.HeaderSummaryModel;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.model.DashboardDataModel.BuyerConversion;
import com.triyasoft.model.DashboardDataModel.BuyerRevenue;
import com.triyasoft.model.DashboardDataModel.BuyersSummary;
import com.triyasoft.model.DashboardDataModel.OnGoingCalls;
import com.triyasoft.model.DashboardDataModel.PhoneCost;
import com.triyasoft.model.DashboardDataModel.Profit;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary;
import com.triyasoft.model.DashboardDataModel.BuyersSummary.BuyerSummary;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary.TrafficSourceSummary;
import com.triyasoft.ui.DashboardUIGenerator;
import com.triyasoft.utils.ProjectUtils;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	public static List<CallModel> todaysCalls = new ArrayList<CallModel>();
	public static List<CallModel> yesterdayCalls = new ArrayList<CallModel>();
	public static List<Buyer> allBuyers = new ArrayList<Buyer>();
	public static long lasttimePlivoHit = System.currentTimeMillis();
	public boolean plivoBalanceFetched = false;
	public static double plivoBalance = 0.0;

	public static boolean isLoaded = false;
	public static long lasttimeDashBoardDataFectched = System
			.currentTimeMillis();
	public static String cachedHeaderSummaryContent = null;
	public static String cachedPartialUIMiddlePaneContent = null;
	public static boolean executionInprogressFormainContainer = false;
	public static boolean executionInprogressForHeaderContainer = false;

	public static void loadData() {

		long duration = System.currentTimeMillis()
				- lasttimeDashBoardDataFectched;

		if ((!isLoaded) || (duration > 10 * 1000)) {
			allBuyers = BuyerDaoService.loadAllBuyers();

			Date tomorrow = new Date();
			tomorrow.setTime(tomorrow.getTime() + 86400 * 1000);

			Date today = new Date();

			Date yesterday = new Date();
			yesterday.setTime(yesterday.getTime() - 86400 * 1000);

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
			// startdateInput = formatter.format(startDate);

			String yesterdayStr = formatter.format(yesterday); // ProjectUtils.getYesterdayStrInClientsTimezone("US/Eastern");
			String todayStr = formatter.format(today); // ProjectUtils.getTodayStrInClientsTimezone("US/Eastern");
			String tomorrowStr = formatter.format(tomorrow); // ProjectUtils.getTomorrowStrInClientsTimezone("US/Eastern");

			todaysCalls = CallsDaoService.loadCalls(todayStr, tomorrowStr);
			yesterdayCalls = CallsDaoService.loadCalls(yesterdayStr, todayStr);
			isLoaded = true;

			lasttimeDashBoardDataFectched = System.currentTimeMillis();

		}

	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		loadData();

		if ("headersummary".equals(requestType)) {

			String content = getHeaderSummaryContent();
			response.getWriter().print(content);

		}

		if ("maincontainer".equals(requestType)) {

			cachedPartialUIMiddlePaneContent = getMainSummaryContent();

			response.getWriter().print(cachedPartialUIMiddlePaneContent);

		}

	}

	private String getMainSummaryContent() {

		DashboardUIGenerator dashboardUIGenerator = new DashboardUIGenerator();

		DashboardDataModel ddm = new DashboardDataModel();

		// Ongoing Calls Block
		OnGoingCalls ogc = ddm.getOnGoingCalls();
		ogc.setOngoingCalls(getActiveCalls());
		ogc.setActiveCC(getActiveCCCurrent());
		ogc.setMaxActiveCC(getActiveCCmax());

		if (ogc.getMaxActiveCC() == 0)
			ogc.setActiveCCPercentage(0.0);
		else
			ogc.setActiveCCPercentage((((double) ogc.getActiveCC()) / ((double) ogc
					.getMaxActiveCC())) * 100);

		ogc.setPausedCC(getPausedCC());
		ogc.setMaxPausedCC(getMaxPausedCC());

		if (ogc.getMaxPausedCC() == 0)
			ogc.setPausedCCPercentage(0.0);
		else
			ogc.setPausedCCPercentage(((((double) ogc.getPausedCC()) / ((double) ogc
					.getMaxPausedCC()))) * 100);

		// Buyer/Converted/Total Calls
		BuyerConversion bcs = ddm.getBuyerConversion();
		bcs.setBuyerConvertedToday(getConvertedCallsforToday());
		bcs.setTotalCallsToday(todaysCalls.size());

		if (bcs.getTotalCallsToday() == 0)
			bcs.setBuyerConvetedPercentageToday(0.0);
		else
			bcs.setBuyerConvetedPercentageToday(((((double) bcs
					.getBuyerConvertedToday()) / ((double) bcs
					.getTotalCallsToday()))) * 100);

		bcs.setBuyerConvertedYday(getBuyerConvertedYday());
		bcs.setTotaslCallsYday(yesterdayCalls.size());

		if (bcs.getTotaslCallsYday() == 0)
			bcs.setBuyerConvertedPercentageYday(0.0);
		else
			bcs.setBuyerConvertedPercentageYday(((((double) bcs
					.getBuyerConvertedYday()) / ((double) bcs
					.getTotaslCallsYday()))) * 100);

		bcs.setBuyerConverted1Hrs(getBuyerConvertedXhours(1));
		bcs.setTotaslCalls1Hrs(getTotalCallsInXHours(1));

		if (bcs.getTotaslCalls1Hrs() == 0)
			bcs.setBuyerConvertedPercentage1Hrs(0.0);
		else
			bcs.setBuyerConvertedPercentage1Hrs(((((double) bcs
					.getBuyerConverted1Hrs()) / ((double) bcs
					.getTotaslCalls1Hrs()))) * 100);

		bcs.setBuyerConverted4Hrs(getBuyerConvertedXhours(4));
		bcs.setTotaslCalls4Hrs(getTotalCallsInXHours(4));

		if (bcs.getTotaslCalls4Hrs() == 0)
			bcs.setBuyerConvertedPercentage4Hrs(0.0);
		else
			bcs.setBuyerConvertedPercentage4Hrs(((((double) bcs
					.getBuyerConverted4Hrs()) / ((double) bcs
					.getTotaslCalls4Hrs()))) * 100);

		// Buyer Revenue

		BuyerRevenue brv = ddm.getBuyerRevenue();
		brv.setBuyerRevenueToday(getBuyerRevenueToday());
		brv.setBuyerRevenueYday(getBuyerRevenueYday());
		brv.setBuyerRevenue4Hrs(getBuyerRevenueXHrs(4));
		brv.setBuyerRevenue1Hrs(getBuyerRevenueXHrs(1));

		// Phone Cost
		PhoneCost phc = ddm.getPhoneCost();
		phc.setPhoneCostToday(getPhoneCostToday());
		phc.setPhoneCostYday(getPhoneCostYday());
		phc.setPhoneCost4Hrs(getPhoneCostXHrs(4));
		phc.setPhoneCost1Hrs(getPhoneCostXHrs(1));

		// Profit
		Profit prf = ddm.getProfit();
		prf.setProfitToday(getTodayProfit());
		prf.setProfitYday(getYdayProfit());
		prf.setProfit4Hrs(getProfitOfXHrs(4));
		prf.setProfit1Hrs(getProfitOfXHrs(1));

		// Calls By Traffic Source

		TrafficSourcesSummary trafficSourcesSummary = ddm
				.getTrafficSourcesSummary();

		for (CallModel todayCall : todaysCalls) {
			int trafficSourceId = todayCall.getTraffic_source_id();
			TrafficSourceSummary trafficSourceSummary = trafficSourcesSummary
					.getTrafficSourceSummary(trafficSourceId);

			if (trafficSourceSummary == null) {
				trafficSourceSummary = trafficSourcesSummary
						.getNewTrafficSourceSummaryInstance();
				trafficSourceSummary.setTrafficSourceId(trafficSourceId);
				trafficSourceSummary.setTrafficSourceName(todayCall
						.getTraffic_source());
			}

			trafficSourceSummary.incermentTodayCallsCount();
			String duration = todayCall.getDuration();

			if (duration == null || duration.length() == 0
					|| "null".equals(duration))
				duration = "0";

			trafficSourceSummary.addTotalCallTime(Double.parseDouble(duration));
			trafficSourceSummary.addToCallRevenue(todayCall
					.getTraffic_source_revenue());

			if (todayCall.getIs_running() == 1)
				trafficSourceSummary.incrementLiveCall();

			trafficSourceSummary.setAvgCall(trafficSourceSummary
					.getTotalCallTime()
					/ ((double) trafficSourceSummary.getTodayCalls()));

		}

		for (CallModel yDayCall : yesterdayCalls) {
			int trafficSourceId = yDayCall.getTraffic_source_id();
			TrafficSourceSummary trafficSourceSummary = trafficSourcesSummary
					.getTrafficSourceSummary(trafficSourceId);

			if (trafficSourceSummary == null) {
				trafficSourceSummary = trafficSourcesSummary
						.getNewTrafficSourceSummaryInstance();
				trafficSourceSummary.setTrafficSourceId(trafficSourceId);
				trafficSourceSummary.setTrafficSourceName(yDayCall
						.getTraffic_source());

			}

			trafficSourceSummary.incermentYdaytCallsCount();

		}

		// Buyers Summary

		BuyersSummary buyersSummary = ddm.getBuyersSummary();

		for (CallModel todayCall : todaysCalls) {
			int buyerId = todayCall.getBuyer_id();

			BuyerSummary buyerSummary = buyersSummary.getBuyer(buyerId);

			if (todayCall.getBuyer_id() == 0) {
				buyerSummary = buyersSummary.getNoBuyer();
			}

			if (buyerSummary == null) {
				buyerSummary = buyersSummary.getBuyerSummaryInstance();

				buyerSummary.setBuyerId(buyerId);
				buyerSummary.setBuyerName(todayCall.getBuyer());
				buyerSummary
						.setBuyerConcurrencyCap(getBuyerConcurrencyCap(buyerId));
				buyerSummary.setBuyerTier(getBuyerTier(buyerId));
				buyerSummary.setBuyerDailyCap(getBuyerDailyCap(buyerId));
				buyerSummary.setIsBuyer_active(checkIfBuyerActive(buyerId));
				buyerSummary.setBuyer_weitage(getBuyerWeitage(buyerId));

			}

			buyerSummary.incrementTodaysCallToBuyer();
			if (todayCall.getIs_running() == 1)
				buyerSummary.incrementLiveCalls();

			String duration = todayCall.getDuration();
			if (duration == null || duration.length() == 0
					|| "null".equals(duration))
				duration = "0";

			buyerSummary.addToBuyersMinute(Double.parseDouble(duration));
			buyerSummary.addToByerRevenue(todayCall.getBuyer_revenue());

			buyerSummary.setAvgCalltime(buyerSummary.getTodaysCalltimeTobuyer()
					/ ((double) buyerSummary.getTodayCallstoBuyer()));

		}

		for (CallModel yDayCall : yesterdayCalls) {

			int buyerId = yDayCall.getBuyer_id();

			BuyerSummary buyerSummary = buyersSummary.getBuyer(buyerId);

			if (yDayCall.getBuyer_id() == 0) {
				buyerSummary = buyersSummary.getNoBuyer();
			}

			if (buyerSummary == null) {
				buyerSummary = buyersSummary.getBuyerSummaryInstance();
				buyerSummary.setBuyerId(buyerId);
				buyerSummary.setBuyerName(yDayCall.getBuyer());
				buyerSummary
						.setBuyerConcurrencyCap(getBuyerConcurrencyCap(buyerId));
				buyerSummary.setBuyerTier(getBuyerTier(buyerId));
				buyerSummary.setBuyer_weitage(getBuyerWeitage(buyerId));
				buyerSummary.setBuyerDailyCap(getBuyerDailyCap(buyerId));
				buyerSummary.setIsBuyer_active(checkIfBuyerActive(buyerId));

			}

			buyerSummary.incrementYDayCallToBuyer();

		}

		// Add Other Acticve Buyers

		List<BuyerSummary> currentBuyersInBuyerSummary = ddm.getBuyersSummary()
				.getBuyersSummary();

		for (Buyer buyer : allBuyers) {

			if (buyer.getIs_active() == 1) {
				boolean buyerPresent = false;
				for (BuyerSummary buyerSummary : currentBuyersInBuyerSummary) {
					if (buyerSummary.getBuyerId() == buyer.getBuyer_id())
						buyerPresent = true;
				}

				if (!buyerPresent) {
					BuyerSummary buyerSummary = ddm.getBuyersSummary()
							.getBuyerSummaryInstance();
					buyerSummary.setBuyerId(buyer.getBuyer_id());
					buyerSummary.setBuyerName(buyer.getBuyer_name());
					buyerSummary.setBuyer_weitage(buyer.getWeight());
					buyerSummary.setBuyerConcurrencyCap(buyer
							.getConcurrency_cap_limit());
					buyerSummary.setBuyerTier(buyer.getTier());
					buyerSummary.setBuyerDailyCap(buyer.getBuyer_daily_cap());
					buyerSummary.setIsBuyer_active(buyer.getIs_active());
					buyerSummary.setTodayCallstoBuyer(0);
					buyerSummary.setYdayCallstoBuyer(0);

				}
			}

		}

		// Setting dashboard source summary footer

		TrafficSourcesSummary sourcesSummary = ddm.getTrafficSourcesSummary();

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
			numberofcall = numberofcall + trafficSourceSummary.getTodayCalls();
		}

		double callAvgTime = 0;
		if (numberofcall != 0)
			callAvgTime = totalCalltime / numberofcall;

		sourcesSummary.setOverAllavg(callAvgTime);

		// //Setting dashboard buyers summary footer

		buyersSummary = ddm.getBuyersSummary();
		buyersSummary.setTotalBuyers(buyersSummary.getBuyersSummary().size());
		buyersSummary.setCurrentLiveCallsToBuyers(ddm.getOnGoingCalls()
				.getOngoingCalls()
				+ ddm.getBuyersSummary().getNoBuyer().getLiveCallsToBuyer());
		buyersSummary.setTotalCallsToBuyersToday(ddm.getBuyerConversion()
				.getBuyerConvertedToday()
				+ ddm.getBuyersSummary().getNoBuyer().getTodayCallstoBuyer());
		buyersSummary.setTotalCallsToBuyersYday(ddm.getBuyerConversion()
				.getBuyerConvertedYday()
				+ ddm.getBuyersSummary().getNoBuyer().getYdayCallstoBuyer());
		buyersSummary.setTotalBuyerRevenue(ddm.getBuyerRevenue()
				.getBuyerRevenueToday());

		List<BuyerSummary> buyerSummaries = buyersSummary.getBuyersSummary();

		totalCalltime = 0.0;
		numberofcall = 0;

		for (BuyerSummary buyerSummary : buyerSummaries) {

			if (buyerSummary.getBuyerId() != 0) {
				totalCalltime = totalCalltime
						+ buyerSummary.getTodaysCalltimeTobuyer();
				numberofcall = numberofcall
						+ buyerSummary.getTodayCallstoBuyer();

			}
		}

		callAvgTime = 0;

		if (numberofcall != 0)
			callAvgTime = totalCalltime / numberofcall;

		buyersSummary.setAvgHandlingTime(ProjectUtils
				.convertDurationToString(callAvgTime));

		Collections.sort(trafficSourcesSummary.getSourceSummaries());
		Collections.sort(buyersSummary.getBuyersSummary());

		String daashboardPartialUI = dashboardUIGenerator
				.generateDashBoardPartialUI(ddm);

		return daashboardPartialUI;

	}

	private String getHeaderSummaryContent() {
		HeaderSummaryModel hsm = new HeaderSummaryModel();

		DashboardUIGenerator dashboardUIGenerator = new DashboardUIGenerator();
		hsm.setActiveCalls(getActiveCalls());
		hsm.setActiveCCcurrent(getActiveCCCurrent());
		hsm.setActiveCCmax(getActiveCCmax());

		hsm.setConvertedCurrentCalls(getConvertedCallsforToday());
		hsm.setConvertedMaxCalls(todaysCalls.size());

		hsm.setRevenue(getRevenue());
		hsm.setProviderbalance(getPlivoBalance());

		String content = dashboardUIGenerator.generateHeaderStrip(hsm);

		return content;

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

		if (!AppSettingsDao.IscustomerHasPlivoaccount())
			return 0.0;

		long currentTime = System.currentTimeMillis();

		if ((currentTime - lasttimePlivoHit < 90 * 1000) && plivoBalanceFetched)
			return plivoBalance;

		String cashCredit = "0";

		String auth_id = AppSettingsDao.getPlivoAuthID();
		String auth_token = AppSettingsDao.getPlivoAuthToken();

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

	private int getActiveCalls() {

		int counter = 0;

		for (CallModel todayCall : todaysCalls) {
			if (todayCall.getIs_running() == 1)
				counter++;

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

		HeaderSummaryModel hsm = new HeaderSummaryModel();
		DashboardServlet dsbs = new DashboardServlet();
		hsm.setActiveCalls(dsbs.getActiveCalls());
		hsm.setActiveCCcurrent(dsbs.getActiveCCCurrent());
		hsm.setActiveCCmax(dsbs.getActiveCCmax());

		hsm.setConvertedCurrentCalls(dsbs.getConvertedCallsforToday());
		hsm.setConvertedMaxCalls(dsbs.todaysCalls.size());

		hsm.setRevenue(dsbs.getRevenue());
		hsm.setProviderbalance(dsbs.getPlivoBalance());

		System.out.println(hsm);

	}

}
