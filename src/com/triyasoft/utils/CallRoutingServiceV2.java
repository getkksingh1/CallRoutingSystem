package com.triyasoft.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.triyasoft.daos.AppSettingsDao;
import com.triyasoft.daos.BuyerDaoService;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.daos.ContactDao;
import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.model.Buyer;
import com.triyasoft.model.BuyerSourcePreferenceFilter;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.ContactModel;
import com.triyasoft.model.HoldBuyer;
import com.triyasoft.model.OutboundCallModel;
import com.triyasoft.model.PhoneNumber;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.ui.DashboardUIGenerator;

public class CallRoutingServiceV2 {

	public static Map<Integer, Buyer> cachedbuyersMap = null;
	public static Map<String, Buyer> cachedbuyersMapByPhoneNumber = null;
	public static Map<Integer, List<Buyer>> cachedbuyerListByTier = null;
	public static List<Buyer> cachedBuyersList = null;
	public static int LARGE_POSTIVE_NUMBER = 999999999;
	public static List<HoldBuyer> holdBuyers = new ArrayList<HoldBuyer>();
	public static int BUYER_HOLD_TIME_DUE_TO_LINE_CONGESTION = 180;

	public static Map<Integer, PhoneNumber> cachedPhoneNumbersMap = null;
	public static Map<String, PhoneNumber> cachedPhoneNumbersMapByPhoneNumber = null;

	public static Map<Integer, TrafficSource> cachedTrafficsourcesMap = null;

	public static Map<String, CallModel> runningCallsMapByUUID = null;

	public static boolean isdataLoaded = false;

	public static int loopcount = 0;

	public static void main(String[] args) {

		String callerNumber = "15595158794";
		String npa = callerNumber.substring(1, 4);
		String nxx = callerNumber.substring(4, 7);
		System.out.println(npa);
		System.out.println(nxx);

	}

	public static CallModel allocateNumberToRoute(CallModel callModel,
			List<Buyer> prohibitedBuyers) {

		// in real scenarios it wont be called. added for standalsone testing

		if (!isdataLoaded) {
			loadAlldata(false);
		}

		Buyer buyer = null;

		// Old Allocation Version

		// buyer = allocateCalltoABuyer(callModel);

		buyer = allocateCalltoABuyerV2(callModel, prohibitedBuyers);

		if (buyer == null && callModel.getError_code() == null) {

			callModel.setError_code("E004");
			callModel
					.setError_description("Could not properly Allocate buyer for this user, debug the issue");
			;
		}

		callModel.setCall_buyer(buyer);

		return callModel;

	}

	public static void loadAlldata(boolean forceLoad) {
		loadBuyers(forceLoad);
		loadTrafficSources(forceLoad);
		loadPhoneNumbers(forceLoad);
		loadRunningCalls(false);
		isdataLoaded = true;

	}

	private static Buyer allocateCalltoABuyerV2(CallModel callModel,
			List<Buyer> prohibitedBuyers) {

		StringBuffer auditRotingLogic = callModel.getRoutingLogic();

		// Getting List of eligibleBuyer

		List<Buyer> eligibleBuyer = new ArrayList<Buyer>(cachedBuyersList);
		for (Buyer buyer : eligibleBuyer) {
			auditRotingLogic
					.append("<span style=\"color:green;font-weight: 600;\">"
							+ buyer.getBuyer_name() + ": "
							+ buyer.getBuyer_number()
							+ " is Eligibile  </span> <br/>");

		}

		// Check if Caller is in blocked List

		callModel.setRoutingLogic(auditRotingLogic);

		List<ContactModel> blockedContacts = ContactDao.getBlockedContacts();

		for (ContactModel blockedContact : blockedContacts) {

			if (blockedContact.getNumber().equalsIgnoreCase(
					callModel.getCaller_number().trim())
					|| blockedContact.getNumber().equalsIgnoreCase(
							callModel.getCallerName())) {
				callModel.setError_code("E009");
				callModel.setError_description(callModel.getCaller_number()
						+ "  " + callModel.getCallerName() + " is blocked");
				auditRotingLogic.append(" <span style=\"color:red\">"
						+ callModel.getCaller_number() + "  "
						+ callModel.getCallerName()
						+ " is blocked </span> <br/>");
				auditRotingLogic
						.append(" <span style=\"color:red\"> Rejecting the call </span>");
				return null;
			}

		}

		if (!AppSettingsDao.isSkypeAllowed()) {

			String[] blockedSeries = { "1661380", "1559515", "1339207",
					"17194931", "1888", "1877", "1855", "1866", "1844", "1800",
					"1881", "17029131" };
			String callerNumber = callModel.getCaller_number();
			boolean isBlockedPattern = false; // checkBlockedPattern(blockedSeries,callerNumber);

			for (String pattern : blockedSeries) {
				if (callerNumber.startsWith(pattern)) {
					isBlockedPattern = true;
					break;
				}
			}

			if (isBlockedPattern) {

				callModel.setError_code("E009");
				callModel.setError_description(callModel.getCaller_number()
						+ "  " + callModel.getCallerName() + " is blocked"
						+ " due to  blocked pattern ");
				auditRotingLogic
						.append(" <span style=\"color:red\"> "
								+ callModel.getCaller_number()
								+ "  "
								+ callModel.getCallerName()
								+ " is blocked due to prefix pattern blocking</span> <br/>");

				return null;
			}
		}

		List<Buyer> excludedBuyers = new ArrayList<Buyer>();
		List<Buyer> rejectedBuyers = new ArrayList<Buyer>();

		auditRotingLogic.append(" <span style=\"color:blue\"> Buyer Map Size +"
				+ cachedbuyersMap.size() + "</span><br/>");

		if (cachedbuyersMap.size() == 0) {
			callModel.setError_code("E005");
			callModel
					.setError_description("No Buyer is currently Active to take call");
			auditRotingLogic
					.append(" <span style=\"color:red\"> No Buyer is currently Active to take call </span><br/>");
			return null;
		}

		// Duplicacy check

		List<Integer> buyerIds = CallsDaoService
				.getAllBuyersCalledBySource(callModel.getCaller_number());

		if (buyerIds != null && buyerIds.size() > 0) {
			for (Integer buyerid : buyerIds) {
				Buyer buyer = cachedbuyersMap.get(buyerid);
				if (buyer != null) {
					auditRotingLogic
							.append(" <span style=\"color:red\"> buyer Temporariliry blocked Due to Dups Check:"
									+ buyer.getBuyer_name()
									+ ": "
									+ buyer.getBuyer_number() + " </span><br/>");
					excludedBuyers.add(buyer);
				}

			}
		}

		// Getting sorted List of Tier

		int[] sortedTiers = RoutingUtil.getSortedTier(cachedbuyerListByTier);

		// Gettting Buyers failiing Filter Criteria

		for (int tier : sortedTiers) {

			List<Buyer> buyersInCurrentTier = cachedbuyerListByTier.get(tier);

			for (Buyer buyer : buyersInCurrentTier) {

				boolean evaluateBuyer = BuyerSourcePreferenceFilter.evaluateOp(
						buyer, callModel.getCall_from_source());

				if (!evaluateBuyer) {
					rejectedBuyers.add(buyer);
					auditRotingLogic
							.append(" <span style=\"color:red\"> Buyer blocked Due to Filter Restriction:"
									+ buyer.getBuyer_name()
									+ ": "
									+ buyer.getBuyer_number()
									+ " </span> <br/>");

				}

			}

		}

		// Getting Buyers failing due to ConCurrency Limit Reached

		for (Buyer buyer : cachedBuyersList) {
			int currentlyRunningCallsWithBuyer = checkNumberofCallsforBuyer(buyer);
			if ((currentlyRunningCallsWithBuyer >= buyer
					.getConcurrency_cap_limit())
					&& (buyer.getConcurrency_cap_limit() != -1)) {
				rejectedBuyers.add(buyer);
				auditRotingLogic
						.append(" <span style=\"color:red\">Buyer blocked Due to Concurrency Cap Reached:"
								+ buyer.getBuyer_name()
								+ ": "
								+ buyer.getBuyer_number()
								+ " Buyer's Currrent Concurrency:"
								+ buyer.getConcurrency_cap_limit()
								+ " Buyer's Max Concurrency :"
								+ currentlyRunningCallsWithBuyer
								+ " </span> <br/>");

			}

		}

		// Getting Buyers failing due to Daily Call Limit Reached

		List<CallModel> todaysCall = CallsDaoService.getTodaysCalls();

		for (Buyer buyer : cachedBuyersList) {
			int todayCallsWithBuyer = getTodayscallwithBuyer(buyer, todaysCall);
			auditRotingLogic
					.append(" <span style=\"color:blue\"> Logging for daily caps:"
							+ buyer.getBuyer_name()
							+ ": "
							+ buyer.getBuyer_number()
							+ " Buyer's Daily Cap:"
							+ buyer.getBuyer_daily_cap()
							+ " Number of Calls to Buyer Today :"
							+ todayCallsWithBuyer + " </span> <br/>");

			if ((todayCallsWithBuyer >= buyer.getBuyer_daily_cap())
					&& (buyer.getBuyer_daily_cap() != -1)) {
				rejectedBuyers.add(buyer);
				auditRotingLogic
						.append(" <span style=\"color:red\"> Buyer blocked Due to Daily Cap Reached:"
								+ buyer.getBuyer_name()
								+ ": "
								+ buyer.getBuyer_number()
								+ " Buyer's Daily Cap:"
								+ buyer.getBuyer_daily_cap()
								+ " Number of Calls to Buyer Today :"
								+ todayCallsWithBuyer + " </span> <br/>");

			}
		}

		auditRotingLogic
				.append("<br/><span style=\"color:red;font-weight: 800;\">Non Eligible Buyers Rejecting from Allocation </span> <br/>");

		for (Buyer rejectedByer : rejectedBuyers) {
			boolean present = eligibleBuyer.remove(rejectedByer);
			if (present)
				auditRotingLogic
						.append("<span style=\"color:red\">  Rejecting Buyer: "
								+ rejectedByer.getBuyer_name() + ": "
								+ rejectedByer.getBuyer_number()
								+ " </span><br/>");

		}

		for (Buyer excludedBuyer : excludedBuyers) {
			boolean present = eligibleBuyer.remove(excludedBuyer);
			if (present)
				auditRotingLogic
						.append(" <span style=\"color:red\"> Excluding Buyer's: "
								+ excludedBuyer.getBuyer_name()
								+ ": "
								+ excludedBuyer.getBuyer_number()
								+ " </span><br/>");

		}

		//

		if (prohibitedBuyers != null) {

			for (Buyer prohibitedBuyer : prohibitedBuyers) {
				boolean present = eligibleBuyer.remove(prohibitedBuyer);
				if (present)
					auditRotingLogic
							.append("<span style=\"color:red\"> Buyer : "
									+ prohibitedBuyer.getBuyer_name()
									+ ": "
									+ prohibitedBuyer.getBuyer_number()
									+ " already called but picked the call, so droping it from current call Allocation  </span> <br/>");

			}

		}

		auditRotingLogic
				.append("<span style=\"color:red;font-weight: 800;\">Non Eligible Buyers Rejecting from Allocation Completed</span> <br/>");

		auditRotingLogic
				.append("<br/>  <span style=\"color:green;font-weight: 600;\">Call Allocation Begins with Eligible Buyers in Lowest Tier(Highest priority tier)</span><br/>");

		// public static List<HoldBuyer> holdBuyers = new
		// ArrayList<HoldBuyer>();
		List<HoldBuyer> holdBuyersCopy = new ArrayList<HoldBuyer>(holdBuyers);
		Date currentTime = new Date();
		for (HoldBuyer holdBuyer : holdBuyersCopy) {

			long timeDiff = currentTime.getTime()
					- holdBuyer.getHoldStartTime().getTime();
			if (timeDiff > BUYER_HOLD_TIME_DUE_TO_LINE_CONGESTION * 1000) {
				holdBuyers.remove(holdBuyer);
			} else {
				eligibleBuyer.remove(holdBuyer);
			}

		}

		// Now allocating the call with all unwanted buyers removed

		Map<Integer, List<Buyer>> eligibleBuyerMapByTier = getEligibleBuyerMapByTier(eligibleBuyer);

		int[] sortTiersWithEligibleBuyers = RoutingUtil
				.getSortedTier(eligibleBuyerMapByTier);

		for (int tier : sortTiersWithEligibleBuyers) {

			List<Buyer> eligibleBuyersInCurrentTier = eligibleBuyerMapByTier
					.get(tier);

			int tierWeight = 0;
			int callRunningInTheTier = 0;

			for (Buyer eligibleBuyerInCurrentTier : eligibleBuyersInCurrentTier) {
				tierWeight = tierWeight
						+ eligibleBuyerInCurrentTier.getWeight();
				callRunningInTheTier = callRunningInTheTier
						+ (checkNumberofCallsforBuyer(eligibleBuyerInCurrentTier) + 1);
			}

			int counter = 0;

			// Getting List for Buyers Call Weight Vs buyerTier Weight Ratio

			double[] buyerCallWeightVsbuyerTierWeightRatioArr = new double[eligibleBuyersInCurrentTier
					.size()];

			double minimumBuyerCallWeightVsbuyerTierWeightRatio = Integer.MAX_VALUE;

			for (Buyer eligibleBuyerInCurrentTier : eligibleBuyersInCurrentTier) {

				double buyerCallWeight = 0;
				double buyerTierWeight = 0;
				double buyerCallWeightVsbuyerTierWeightRatio = 0;

				if (callRunningInTheTier > 0)
					buyerCallWeight = ((double) (checkNumberofCallsforBuyer(eligibleBuyerInCurrentTier) + 1))
							/ ((double) (callRunningInTheTier));

				if (tierWeight > 0)
					buyerTierWeight = ((double) eligibleBuyerInCurrentTier
							.getWeight()) / ((double) (tierWeight));

				if (buyerCallWeight > 0)
					buyerCallWeightVsbuyerTierWeightRatio = buyerCallWeight
							/ buyerTierWeight;

				buyerCallWeightVsbuyerTierWeightRatioArr[counter] = buyerCallWeightVsbuyerTierWeightRatio;

				if (minimumBuyerCallWeightVsbuyerTierWeightRatio > buyerCallWeightVsbuyerTierWeightRatio)
					minimumBuyerCallWeightVsbuyerTierWeightRatio = buyerCallWeightVsbuyerTierWeightRatio;

				auditRotingLogic
						.append(eligibleBuyerInCurrentTier.getBuyer_name()
								+ ": "
								+ eligibleBuyerInCurrentTier.getBuyer_number()
								+ ":"
								+ " Normalized Buyer Weight Vs Total Tier Weight Ratio:"
								+ DashboardUIGenerator
										.convertDouble2DigitPrecison(buyerCallWeightVsbuyerTierWeightRatio)
								+ "<br/>");

				counter++;

			}

			auditRotingLogic
					.append("<span style=\"color:green;font-weight: 600;\"> Minimum Value of Buyer's Call Weight Vs Buyer's  Tier Weight Ratio:"
							+ DashboardUIGenerator
									.convertDouble2DigitPrecison(minimumBuyerCallWeightVsbuyerTierWeightRatio)
							+ " </span><br/>");

			List<Buyer> buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio = new ArrayList<Buyer>();

			auditRotingLogic
					.append("<br>Buyers with Minimum call Weight Ratio</br>");

			counter = 0;
			for (Buyer eligibleBuyerInCurrentTier : eligibleBuyersInCurrentTier) {
				if (buyerCallWeightVsbuyerTierWeightRatioArr[counter] == minimumBuyerCallWeightVsbuyerTierWeightRatio) {
					buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio
							.add(eligibleBuyerInCurrentTier);
					auditRotingLogic
							.append("<span style=\"color:green;font-weight: 400;\"> Min Ratio Buyer:"
									+ eligibleBuyerInCurrentTier
											.getBuyer_name() + "<br/> </span>");
				}

				counter++;

			}

			// Now Select Highest Weight Buyer amongst
			// buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio

			counter = 0;
			int maxWeight = -1;

			int selectedBuyerIndex = -1;

			for (Buyer buyer : buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio) {
				if (buyer.getWeight() > maxWeight) {
					maxWeight = buyer.getWeight();
					selectedBuyerIndex = counter;
				}

				counter++;
			}

			List<Buyer> buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatioAndEqualMaxWeight = new ArrayList<Buyer>();

			if (selectedBuyerIndex != -1) {

				Buyer selectedBuyer = buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio
						.get(selectedBuyerIndex);
				int weight = selectedBuyer.getWeight();
				for (Buyer buyer : buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio) {
					if (buyer.getWeight() == weight) {
						buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatioAndEqualMaxWeight
								.add(buyer);
					}
				}

			}

			// If more than 1 max buyer weight
			if (buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatioAndEqualMaxWeight
					.size() > 1) {
				auditRotingLogic
						.append("More than 1 buyer selected..  Doing Random Allocation <br/>");

				for (Buyer buyer : buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatioAndEqualMaxWeight) {
					auditRotingLogic.append(" Random Buyer: "
							+ buyer.getBuyer_name() + ": "
							+ buyer.getBuyer_number() + " <br/>");

				}

				int qualifiedBuyersSize = buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatioAndEqualMaxWeight
						.size();
				Random random = new Random();
				selectedBuyerIndex = random.nextInt(qualifiedBuyersSize);

			}

			if (selectedBuyerIndex != -1) {

				Buyer selectedBuyer = buyersQualifingMinimumBuyerCallWeightVsbuyerTierWeightRatio
						.get(selectedBuyerIndex);
				auditRotingLogic
						.append("<span style=\"color:green;font-weight: 600;\">  Selected byer for allocation is : "
								+ selectedBuyer.getBuyer_name()
								+ "</span><br/>");

				// System.out.println(auditRotingLogic.toString());

				return selectedBuyer;
			}

		}

		auditRotingLogic
				.append(" <span style=\"color:red\">  </span>No Buyer Found, Now trying amongts Dups Buyers and Priviously tried failed buyers again <br/> </span>");

		// Coming here mean everything is full, allocating buyer from Duplicate
		// list

		List<Buyer> allExcludedBuyers = new ArrayList<Buyer>();

		allExcludedBuyers.addAll(excludedBuyers);

		for (HoldBuyer holdBuyer : holdBuyers) {
			if (!allExcludedBuyers.contains(holdBuyer.getBuyer())) {
				allExcludedBuyers.add(holdBuyer.getBuyer());
			}
		}

		for (Buyer excludedBuyer : allExcludedBuyers) {

			if (excludedBuyer.getIs_active() == 1) {
				int currentlyRunningCallsWithBuyer = checkNumberofCallsforBuyer(excludedBuyer);
				int todayCallsWithBuyer = getTodayscallwithBuyer(excludedBuyer,
						todaysCall);

				if ((currentlyRunningCallsWithBuyer >= excludedBuyer
						.getConcurrency_cap_limit())
						&& (excludedBuyer.getConcurrency_cap_limit() != -1))
					continue;
				else if ((todayCallsWithBuyer >= excludedBuyer
						.getBuyer_daily_cap())
						&& (excludedBuyer.getBuyer_daily_cap() != -1))
					continue;
				else {
					auditRotingLogic
							.append(" <span style=\"color:green;font-weight: 600;\">Selected Buyer from previously Excluded buyer being used for routing is  "
									+ excludedBuyer.getBuyer_name()
									+ ":"
									+ excludedBuyer.getBuyer_number()
									+ "  </span> <br/>");
					return excludedBuyer;
				}

			}

		}

		auditRotingLogic
				.append(" <span style=\"color:red\">No buyer found for allocation even from excluded list, this call will now be rejected   </span> <br/> </span>");

		return null;

	}

	private static Map<Integer, List<Buyer>> getEligibleBuyerMapByTier(
			List<Buyer> eligibleBuyer) {

		Map<Integer, List<Buyer>> eligibleBuyerMapByTier = new HashMap<Integer, List<Buyer>>();

		for (Buyer buyer : eligibleBuyer) {

			List<Buyer> buyersInTier = eligibleBuyerMapByTier.get(buyer
					.getTier());
			if (buyersInTier == null) {
				buyersInTier = new ArrayList<Buyer>();
				eligibleBuyerMapByTier.put(buyer.getTier(), buyersInTier);
			}

			buyersInTier.add(buyer);

		}

		return eligibleBuyerMapByTier;

	}

	private static int getTodayscallwithBuyer(Buyer buyer,
			List<CallModel> todaysCall) {
		int counter = 0;

		for (CallModel callModel : todaysCall) {
			if (callModel.getBuyer_id() == buyer.getBuyer_id())
				counter++;
		}

		return counter;
	}

	private static int checkNumberofCallsforBuyer(Buyer chooseBuyer) {

		Set set = runningCallsMapByUUID.entrySet();
		int runningCallforCurrentBuyer = 0;

		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			CallModel callModel = (CallModel) me.getValue();
			if (callModel.getBuyer_id() == chooseBuyer.getBuyer_id())
				runningCallforCurrentBuyer++;

		}

		return runningCallforCurrentBuyer;

	}

	private static Buyer selectByerInTierXAndIndexJ(int tierX, int j,
			int[] weitage) {

		List<Buyer> buyerInCurrentTier = cachedbuyerListByTier.get(tierX);
		int counter = 0;
		for (Buyer buyer : buyerInCurrentTier) {
			if (counter == j)
				return buyer;

		}

		return null;
	}

	private static int[] getTierXConucurrency(int tierX) {

		List<Buyer> buyerInCurrentTier = cachedbuyerListByTier.get(tierX);

		int[] concurrency = new int[buyerInCurrentTier.size()];
		int counter = 0;
		for (Buyer buyer : buyerInCurrentTier) {

			concurrency[counter++] = buyer.getConcurrency_cap_limit();
		}

		return concurrency;
	}

	private static int[] getTierXWeitages(int tierX) {

		List<Buyer> buyerInCurrentTier = cachedbuyerListByTier.get(tierX);

		int[] tierXWeitages = new int[buyerInCurrentTier.size()];
		int counter = 0;
		for (Buyer buyer : buyerInCurrentTier) {

			tierXWeitages[counter++] = buyer.getWeight();
		}

		return tierXWeitages;

	}

	private static int getTierXCallCapacity(int tierX) {
		List<Buyer> buyerInCurrentTier = cachedbuyerListByTier.get(tierX);
		int capacity = 0;
		for (Buyer buyer : buyerInCurrentTier) {

			capacity += buyer.getConcurrency_cap_limit();
		}

		return capacity;
	}

	private static int getCurrentlyRunningCallsinTierX(int tierX) {

		int callsRunningInTierX = 0;

		List<Buyer> buyerInCurrentTier = cachedbuyerListByTier.get(tierX);
		for (Buyer buyer : buyerInCurrentTier) {

			callsRunningInTierX += checkNumberofCallsforBuyer(buyer);
		}

		return callsRunningInTierX;
	}

	private static int findtheTierToAllocateCall(int size) {

		int[] tiers = new int[cachedbuyerListByTier.size()];
		int counter = 0;

		Set set = cachedbuyerListByTier.entrySet();

		Iterator iterator = set.iterator();
		counter = 0;
		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			Integer tier = (Integer) me.getKey();
			tiers[counter++] = tier;

		}

		Arrays.sort(tiers);
		int sum = 0;
		for (Integer tier : tiers) {
			int runningCallintheTier = getCurrentlyRunningCallsinTierX(tier);
			int tierXcallCapacity = getTierXCallCapacity(tier);
			if (runningCallintheTier < tierXcallCapacity)
				return tier;
		}

		return LARGE_POSTIVE_NUMBER;
	}

	private static int findbucket(int[] concurrency, int generateNumber) {

		int sum = 0;
		for (int i = 0; i < concurrency.length; i++) {
			sum += concurrency[i];
			if (sum > generateNumber)
				return i;
		}

		return 0;

	}

	public static CallModel updateCallModel(CallModel callModel)
			throws Exception {

		int buyer_id = callModel.getBuyer_id();

		String phoneNumber = callModel.getNumber_called();

		Buyer buyer = cachedbuyersMap.get(buyer_id);

		if (buyer == null) {
			buyer = BuyerDaoService.getBuyerByByerId(buyer_id);
			if (buyer != null)
				cachedbuyersMap.put(buyer_id, buyer);
		}

		if (buyer != null)
			callModel.setBuyer_revenue(buyer.getBid_price());

		PhoneNumber number = cachedPhoneNumbersMapByPhoneNumber
				.get(phoneNumber);

		if (number == null) {

			number = PhoneNumberDaoService
					.getPhoneNumberbyCallerId(phoneNumber);
			cachedPhoneNumbersMapByPhoneNumber.put(phoneNumber, number);
		}

		if (number != null)
			callModel.setTraffic_source_revenue(number.getCostpercall());

		boolean checkDuplicate = CallsDaoService.checkIfDupCall(callModel);
		if (checkDuplicate || (buyer == null))
			callModel.setTraffic_source_revenue(0.0);

		boolean checkBuyerDuplicate = CallsDaoService
				.checkBuerDupCall(callModel);
		if (checkBuyerDuplicate)
			callModel.setBuyer_revenue(0);

		CallsDaoService
				.removeDuplicateZeroSecondCallsFromCurrentUserAndNoBuyer(callModel);

		if ("0".equals(callModel.getBillDuration())
				|| callModel.getError_code() != null || buyer == null
				|| (!callModel.isBuyerAndCostumerConnected())) {
			callModel.setTraffic_source_revenue(0.0);
			callModel.setBuyer_revenue(0.0);

		}

		String no_connect_cause = "";

		if (!callModel.isBuyerAndCostumerConnected()
				&& "NORMAL_CLEARING".equals(callModel.getHangupCause())) {
			// This means buyer was not allocated and customer disconnected
			no_connect_cause = "Customer Disconnected Call";
		}

		/*
		 * if (buyer == null &&
		 * "no-answer".equals(callModel.getCallStatusAtHangup())) { // this mean
		 * route was busy, keep it in hold buyers for sometime HoldBuyer
		 * holdBuyer = new HoldBuyer(); holdBuyer.setBuyer(buyer);
		 * holdBuyers.add(holdBuyer);
		 * 
		 * }
		 */

		callModel = populateBuyerSideCallDetails(callModel);

		Connection conn = ProjectUtils.getMySQLConnection();

		PreparedStatement stmt = null;

		try {

			stmt = conn
					.prepareStatement("UPDATE calls SET totalCost = ? , hangupCause = ? , billduration = ? , startTime = ? , endTime = ? "
							+ "  , duration = ? , callStatusAtHangup = ? , hangupEvent = ? ,"
							+ " is_running = ? , xmlResponse =? , answer_time=?,  callroutingtime = ? , buyer_revenue = ? , traffic_source_revenue = ?, "
							+ "recording_url = ?, callattempts = ?, connected_time = ? , buyer_connecting_time = ?, buyers_tried = ?, buyer_hangup_reason  = ? , no_connect_cause = ? "
							+ " WHERE uuid = ?");

			int counter = 1;
			stmt.setString(counter++, callModel.getTotalCost());
			stmt.setString(counter++, callModel.getHangupCause());
			stmt.setString(counter++, callModel.getBillDuration());
			stmt.setTimestamp(counter++, new Timestamp(callModel.getStartTime()
					.getTime()));
			stmt.setTimestamp(counter++, new Timestamp(callModel.getEndTime()
					.getTime()));
			stmt.setString(counter++, callModel.getDuration());
			stmt.setString(counter++, callModel.getCallStatusAtHangup());
			stmt.setString(counter++, callModel.getHangupEvent());
			stmt.setInt(counter++, callModel.getIs_running());
			stmt.setString(counter++, callModel.getXmlResponse());
			stmt.setTimestamp(counter++, new Timestamp(callModel
					.getAnswerTime().getTime()));
			stmt.setLong(counter++, callModel.getCallRoutingTime());
			stmt.setDouble(counter++, callModel.getBuyer_revenue());
			stmt.setDouble(counter++, callModel.getTraffic_source_revenue());
			stmt.setString(counter++, callModel.getRecording_url());
			stmt.setInt(counter++, callModel.getCallattempts());
			stmt.setInt(counter++, callModel.getConnected_time());
			stmt.setInt(counter++, callModel.getBuyer_connecting_time());
			stmt.setString(counter++, callModel.getBuyers_tried());
			stmt.setString(counter++, callModel.getBuyer_hangup_reason());
			stmt.setString(counter++, no_connect_cause);

			stmt.setString(counter++, callModel.getUuid());

			stmt.executeUpdate();

		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(null, stmt, conn);
		}

		runningCallsMapByUUID.remove(callModel.getUuid());

		return callModel;
	}

	public static CallModel updateCallModelWithConferenceForTwillo(
			CallModel callModel) throws Exception {

		int buyer_id = callModel.getBuyer_id();

		String phoneNumber = callModel.getNumber_called();

		Buyer buyer = cachedbuyersMap.get(buyer_id);

		if (buyer == null) {
			buyer = BuyerDaoService.getBuyerByByerId(buyer_id);
			if (buyer != null)
				cachedbuyersMap.put(buyer_id, buyer);
		}

		if (buyer != null)
			callModel.setBuyer_revenue(buyer.getBid_price());

		PhoneNumber number = cachedPhoneNumbersMapByPhoneNumber
				.get(phoneNumber);

		if (number == null) {

			number = PhoneNumberDaoService
					.getPhoneNumberbyCallerId(phoneNumber);
			cachedPhoneNumbersMapByPhoneNumber.put(phoneNumber, number);
		}

		if (number != null)
			callModel.setTraffic_source_revenue(number.getCostpercall());

		boolean checkDuplicate = CallsDaoService.checkIfDupCall(callModel);
		if (checkDuplicate || (buyer == null))
			callModel.setTraffic_source_revenue(0.0);

		boolean checkBuyerDuplicate = CallsDaoService
				.checkBuerDupCall(callModel);
		if (checkBuyerDuplicate)
			callModel.setBuyer_revenue(0);

		CallsDaoService
				.removeDuplicateZeroSecondCallsFromCurrentUserAndNoBuyer(callModel);

		if ("0".equals(callModel.getBillDuration())
				|| callModel.getError_code() != null || buyer == null) {
			callModel.setTraffic_source_revenue(0.0);
			callModel.setBuyer_revenue(0.0);

		}

		/*
		 * if (buyer == null &&
		 * "no-answer".equals(callModel.getCallStatusAtHangup())) { // this mean
		 * route was busy, keep it in hold buyers for sometime HoldBuyer
		 * holdBuyer = new HoldBuyer(); holdBuyer.setBuyer(buyer);
		 * holdBuyers.add(holdBuyer);
		 * 
		 * }
		 */

		callModel = populateBuyerSideCallDetails(callModel);

		Connection conn = ProjectUtils.getMySQLConnection();

		PreparedStatement stmt = null;

		try {

			stmt = conn
					.prepareStatement("UPDATE calls SET totalCost = ? , hangupCause = ? , billduration = ? , startTime = ? , endTime = ? "
							+ "  , duration = ? , callStatusAtHangup = ? , hangupEvent = ? ,"
							+ " is_running = ? , xmlResponse =? , answer_time=?,  callroutingtime = ? , buyer_revenue = ? , traffic_source_revenue = ?, "
							+ "recording_url = ?, callattempts = ?, connected_time = ? , buyer_connecting_time = ?, buyers_tried = ?, buyer_hangup_reason  = ? , "
							+ "buyer = ? , buyer_id = ?, connected_to = ? "
							+ " WHERE uuid = ?");

			int counter = 1;
			stmt.setString(counter++, callModel.getTotalCost());
			stmt.setString(counter++, callModel.getHangupCause());
			stmt.setString(counter++, callModel.getBillDuration());
			stmt.setTimestamp(counter++, new Timestamp(callModel.getStartTime()
					.getTime()));
			stmt.setTimestamp(counter++, new Timestamp(callModel.getEndTime()
					.getTime()));
			stmt.setString(counter++, callModel.getDuration());
			stmt.setString(counter++, callModel.getCallStatusAtHangup());
			stmt.setString(counter++, callModel.getHangupEvent());
			stmt.setInt(counter++, callModel.getIs_running());
			stmt.setString(counter++, callModel.getXmlResponse());
			stmt.setTimestamp(counter++, new Timestamp(callModel
					.getAnswerTime().getTime()));
			stmt.setLong(counter++, callModel.getCallRoutingTime());
			stmt.setDouble(counter++, callModel.getBuyer_revenue());
			stmt.setDouble(counter++, callModel.getTraffic_source_revenue());
			stmt.setString(counter++, callModel.getRecording_url());
			stmt.setInt(counter++, callModel.getCallattempts());
			stmt.setInt(counter++, callModel.getConnected_time());
			stmt.setInt(counter++, callModel.getBuyer_connecting_time());
			stmt.setString(counter++, callModel.getBuyers_tried());
			stmt.setString(counter++, callModel.getBuyer_hangup_reason());
			stmt.setString(counter++, buyer.getBuyer_name());
			stmt.setInt(counter++, buyer.getBuyer_id());
			stmt.setString(counter++, buyer.getBuyer_number());

			stmt.setString(counter++, callModel.getUuid());

			stmt.executeUpdate();

		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(null, stmt, conn);
		}

		runningCallsMapByUUID.remove(callModel.getUuid());

		return callModel;
	}

	private static CallModel populateBuyerSideCallDetails(CallModel callModel) {

		try {

			List<OutboundCallModel> outBoundCalls = CallsDaoService
					.populateBuyerSideCallDetails(callModel);

			if (outBoundCalls.size() == 0)
				return callModel;

			if (outBoundCalls.size() == 1) {
				long conferenceStartTime = outBoundCalls.get(0)
						.getConferencestarttime().getTime();
				long conferenceEndTime = outBoundCalls.get(0)
						.getConferenceendtime().getTime();
				long buyer_connecting_time = outBoundCalls.get(0)
						.getAnswertime().getTime()
						- callModel.getCallLandingTimeOnServer().getTime();

				callModel
						.setConnected_time((int) ((conferenceEndTime - conferenceStartTime) / 1000));
				callModel
						.setBuyer_connecting_time((int) (buyer_connecting_time / 1000));

			}

			callModel.setCallattempts(outBoundCalls.size());

			StringBuffer buyer_hangup_reasons = new StringBuffer();
			StringBuffer buyers_tried = new StringBuffer();

			for (OutboundCallModel outboundCallModel : outBoundCalls) {

				String buyerName = cachedbuyersMapByPhoneNumber.get(
						outboundCallModel.getTo()).getBuyer_name();
				String hangupReason = outboundCallModel.getHangupcause();

				if (buyerName == null)
					buyerName = "";

				if (hangupReason == null)
					hangupReason = "";

				buyers_tried.append(buyerName + ",");
				buyer_hangup_reasons.append(hangupReason + ",");

			}

			callModel.setBuyers_tried(buyers_tried.toString());
			callModel.setBuyer_hangup_reason(buyer_hangup_reasons.toString());
		} catch (Exception e) {
			System.out.println("Exception in populateBuyerSideCallDetails");
			e.printStackTrace();
		}

		return callModel;

	}

	public static java.util.Date convertPlivoDateToJavaDate(String dateStr) {
		// 2017-05-31+17%3A02%3A51
		java.util.Date convertedDate = null;

		try {
			dateStr = dateStr.replaceAll("%3A", ":");
			System.out.println(dateStr);
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			convertedDate = formatter.parse(dateStr);
		} catch (Exception e) {
			System.out
					.println(" Exception in  convertPlivoDateToJavaDate CallRouting Service");
			convertedDate = new java.util.Date();
		}

		return convertedDate;
	}

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static String getHAMCSHA1Base64forPlivo(String data, String key) {

		data = "https://tracktel.io/provider/plivo/legs/44035442/hold_music/BillRate0.021CallStatusin-progressCallUUIDfd2eb482-4fd2-11e7-ac71-33a3522a1debCallerName+12197945043ConferenceActionwaitSoundConferenceMemberIDConferenceName32387753ConferenceUUIDDirectioninboundEventConferenceRemoteSoundsFrom12197945043To18882142436";
		key = "MAZMVLZGE0YTLKZWRMOT";

		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
				HMAC_SHA1_ALGORITHM);
		Mac mac = null;
		try {
			mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mac.init(signingKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] hashedRawData = mac.doFinal(data.getBytes());

		System.out.println(hashedRawData);

		System.out.println(Base64.encodeBase64(hashedRawData));

		System.out.println(new String(Base64.encodeBase64(hashedRawData)));

		String hash = new String(mac.doFinal(data.getBytes()));
		return hash;

		// toHexString(mac.doFinal(data.getBytes()));

	}

	public static CallModel createCallModel(CallModel callModel) {

		if (!isdataLoaded) {
			loadAlldata(false);
		}

		String toNumber = callModel.getNumber_called();
		String uuid = callModel.getUuid();

		PhoneNumber phoneNumber = cachedPhoneNumbersMapByPhoneNumber
				.get(toNumber);

		if (phoneNumber == null) {
			callModel.setError_code("E002");
			callModel
					.setError_description("Unknown Source Called. There are high chances that phone number "
							+ toNumber
							+ " has typo or the it been made inactive...Check phonenumbers table");
			CallsDaoService.saveCallModelV2(callModel);
			runningCallsMapByUUID.put(uuid, callModel);
			return callModel;
		}

		callModel.setSource_phoneNumber(phoneNumber);

		TrafficSource trafficSource = phoneNumber.getTrafficSource();

		callModel = setCallerDetails(callModel);

		if (trafficSource == null) {

			callModel.setError_code("E003");
			callModel.setError_description(toNumber
					+ " has not correctly attached to a traffic source");
			CallsDaoService.saveCallModelV2(callModel);
			runningCallsMapByUUID.put(uuid, callModel);
			return callModel;

		}

		callModel.setCall_from_source(trafficSource);

		callModel.setTraffic_source_id(trafficSource.getId());

		String fname = trafficSource.getFirst_name();
		if (fname == null)
			fname = "";

		String lname = trafficSource.getLast_name();
		if (lname == null)
			lname = "";

		callModel.setTraffic_source(trafficSource.getLast_name() + " "
				+ trafficSource.getFirst_name());

		callModel = allocateNumberToRoute(callModel, null);

		if (callModel.getError_code() != null) {
			CallsDaoService.saveCallModelV2(callModel);
			runningCallsMapByUUID.put(uuid, callModel);
			return callModel;

		}

		String allocatedNumber = callModel.getCall_buyer().getBuyer_number();
		callModel.setConnected_to(allocatedNumber);

		callModel.setBuyer(cachedbuyersMapByPhoneNumber.get(allocatedNumber)
				.getBuyer_name());
		callModel.setBuyer_id(cachedbuyersMapByPhoneNumber.get(allocatedNumber)
				.getBuyer_id());

		callModel.setIs_running(1);
		CallsDaoService.saveCallModelV2(callModel);

		runningCallsMapByUUID.put(uuid, callModel);

		return callModel;
	}

	private static CallModel setCallerDetails(CallModel callModel) {

		Connection connection = ProjectUtils.getMySQLConnection();

		Statement stmt = null;
		ResultSet rs = null;

		try {

			String callerNumber = callModel.getCaller_number();
			String npa = callerNumber.substring(1, 4);
			String nxx = callerNumber.substring(4, 7);

			stmt = connection.createStatement();

			rs = stmt.executeQuery("select * from telecom_static where npa='"
					+ npa + "' and nxx= '" + nxx + "'");

			while (rs.next()) {

				callModel.setCity(rs.getString("switch_name"));
				callModel.setState(rs.getString("state"));
				callModel.setCountry(rs.getString("country"));
				callModel.setPhoneProvider(rs.getString("company"));
				callModel.setLatitue(rs.getString("latitude"));
				callModel.setLongitude(rs.getString("longitude"));

			}
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(rs, stmt, connection);
		}

		return callModel;
	}

	private static Map<String, CallModel> loadRunningCalls(boolean forceLoad) {

		if (runningCallsMapByUUID != null && !forceLoad)
			return runningCallsMapByUUID;

		Map<String, CallModel> runningCallsMapByUUID1 = new HashMap<String, CallModel>();
		Connection connection = ProjectUtils.getMySQLConnection();

		Statement stmt = null;
		ResultSet rs = null;

		try {

			stmt = connection.createStatement();

			rs = stmt.executeQuery("select * from calls where is_running=1 ; ");

			while (rs.next()) {

				CallModel callModel = new CallModel();

				Integer id = rs.getInt("id");
				String uuid = rs.getString("uuid");

				callModel.setId(id);
				callModel.setUuid(uuid);

				callModel.setRecording_url(rs.getString("recording_url"));
				callModel.setNumber_called(rs.getString("number_called"));
				callModel.setConnected_to(rs.getString("connected_to"));
				callModel.setCaller_number(rs.getString("caller_number"));
				callModel.setTraffic_source_id(rs.getInt("traffic_source_id"));
				callModel.setTraffic_source(rs.getString("traffic_source"));
				callModel.setBuyer(rs.getString("buyer"));
				callModel.setBuyer_id(rs.getInt("buyer_id"));

				// Plivo Parameters at call start
				callModel.setCallStatusAtAnswer(rs
						.getString("callStatusAtAnswer"));
				callModel.setAnswerEvent(rs.getString("answerEvent"));
				callModel.setBillRate(rs.getString("billRate"));
				callModel.setXmlResponse(rs.getString("xmlResponse"));
				callModel.setDirection(rs.getString("direction"));
				callModel.setCallerName(rs.getString("callerName"));

				// Plivo Parameters at call hangup

				callModel.setTotalCost(rs.getString("totalCost"));
				callModel.setHangupCause(rs.getString("hangupCause"));
				callModel.setStartTime(rs.getTimestamp("startTime"));
				callModel.setEndTime(rs.getTimestamp("endTime"));
				callModel.setAnswerTime(rs.getTimestamp("answer_time"));
				callModel.setCallLandingTimeOnServer(rs
						.getTimestamp("calllandingtimeonsever"));

				callModel.setDuration(rs.getString("duration"));
				callModel.setHangupEvent(rs.getString("hangupEvent"));

				callModel.setCity(rs.getString("city"));
				callModel.setState(rs.getString("state"));
				callModel.setCountry(rs.getString("country"));
				callModel.setLatitue(rs.getString("latitude"));
				callModel.setLongitude(rs.getString("longitude"));
				callModel.setPhoneProvider(rs.getString("phoneprovider"));

				callModel.setCallStatusAtHangup(rs
						.getString("callStatusAtHangup"));
				callModel.setCall_buyer(cachedbuyersMap.get(callModel
						.getBuyer_id()));
				callModel.setCall_from_source(cachedTrafficsourcesMap
						.get(callModel.getTraffic_source_id()));

				runningCallsMapByUUID1.put(uuid, callModel);

			}
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(rs, stmt, connection);
		}

		runningCallsMapByUUID = runningCallsMapByUUID1;

		return runningCallsMapByUUID;

	}

	public static Map<Integer, Buyer> loadBuyers(boolean forceLoad) {

		if (cachedbuyersMap != null && !forceLoad)
			return cachedbuyersMap;

		Map<Integer, Buyer> cachedbuyersMap1 = new HashMap<Integer, Buyer>();
		Map<String, Buyer> cachedbuyersMapByPhoneNumber1 = new HashMap<String, Buyer>();
		List<Buyer> cachedBuyersList1 = new ArrayList<Buyer>();
		Map<Integer, List<Buyer>> cachedbuyerListByTier1 = new HashMap<Integer, List<Buyer>>();

		Statement stmt = null;
		ResultSet rs = null;
		Connection connection = ProjectUtils.getMySQLConnection();

		try {

			stmt = connection.createStatement();

			rs = stmt.executeQuery("select * from Buyers where is_active=1 ; ");

			while (rs.next()) {

				Buyer buyer = new Buyer();
				Integer buyer_id = rs.getInt("buyer_id");
				buyer.setBuyer_id(buyer_id);
				buyer.setBuyer_name(rs.getString("buyer_name"));
				String buyer_number = rs.getString("buyer_number");
				buyer.setBuyer_number(buyer_number);
				buyer.setCreated_at(rs.getTimestamp("created_at"));
				buyer.setWeight(rs.getInt("weight"));
				buyer.setTier(rs.getInt("tier"));
				buyer.setConcurrency_cap_limit(rs
						.getInt("concurrency_cap_limit"));
				buyer.setConcurrency_cap_used(rs.getInt("concurrency_cap_used"));
				buyer.setRunning_status(rs.getInt("running_status"));
				buyer.setBid_price(rs.getDouble("bid_price"));
				buyer.setBuyer_daily_cap(rs.getInt("buyer_daily_cap"));
				buyer.setRing_timeout(rs.getInt("ring_timeout"));

				cachedbuyersMap1.put(buyer_id, buyer);
				cachedbuyersMapByPhoneNumber1.put(buyer_number, buyer);
				cachedBuyersList1.add(buyer);

				List<Buyer> buyersInThisTier = cachedbuyerListByTier1.get(buyer
						.getTier());

				if (buyersInThisTier == null)
					buyersInThisTier = new ArrayList<Buyer>();

				buyersInThisTier.add(buyer);
				cachedbuyerListByTier1.put(buyer.getTier(), buyersInThisTier);

			}
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(rs, stmt, connection);
		}

		cachedbuyersMap = cachedbuyersMap1;
		cachedbuyersMapByPhoneNumber = cachedbuyersMapByPhoneNumber1;
		cachedBuyersList = cachedBuyersList1;
		cachedbuyerListByTier = cachedbuyerListByTier1;

		return cachedbuyersMap;

	}

	public static Map<Integer, PhoneNumber> loadPhoneNumbers(boolean forceLoad) {

		if (cachedPhoneNumbersMap != null && !forceLoad)
			return cachedPhoneNumbersMap;

		Map<Integer, PhoneNumber> cachedPhoneNumbersMap1 = new HashMap<Integer, PhoneNumber>();
		Map<String, PhoneNumber> cachedPhoneNumbersMapByPhoneNumber1 = new HashMap<String, PhoneNumber>();
		Connection connection = ProjectUtils.getMySQLConnection();

		Statement stmt = null;
		ResultSet rs = null;

		try {

			stmt = connection.createStatement();

			rs = stmt
					.executeQuery("select * from phonenumbers where is_active=1 ; ");

			while (rs.next()) {

				PhoneNumber phoneNumber = new PhoneNumber();
				Integer id = rs.getInt("id");

				phoneNumber.setId(id);
				String number = rs.getString("number");
				phoneNumber.setNumber(number);
				phoneNumber.setCreated_at(rs.getTimestamp("created_at"));
				phoneNumber.setUpdated_at(rs.getTimestamp("updated_at"));
				phoneNumber.setLast_call_at(rs.getTimestamp("last_call_at"));

				int traffic_source_id = rs.getInt("traffic_source_id");
				TrafficSource trafficSource = loadTrafficSources(false).get(
						traffic_source_id);
				phoneNumber.setTrafficSource(trafficSource);

				System.out.println(trafficSource);

				phoneNumber.setTraffic_source_id(traffic_source_id);

				phoneNumber.setIs_Active(rs.getInt("is_active"));
				phoneNumber.setCostpercall(rs.getDouble("costpercall"));

				cachedPhoneNumbersMap1.put(id, phoneNumber);
				cachedPhoneNumbersMapByPhoneNumber1.put(number, phoneNumber);

			}
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(rs, stmt, connection);
		}

		cachedPhoneNumbersMap = cachedPhoneNumbersMap1;
		cachedPhoneNumbersMapByPhoneNumber = cachedPhoneNumbersMapByPhoneNumber1;

		return cachedPhoneNumbersMap;

	}

	public static Map<Integer, TrafficSource> loadTrafficSources(
			boolean forceLoad) {

		if (cachedTrafficsourcesMap != null && !forceLoad)
			return cachedTrafficsourcesMap;

		Map<Integer, TrafficSource> cachedTrafficsourcesMap1 = new HashMap<Integer, TrafficSource>();
		Connection connection = ProjectUtils.getMySQLConnection();

		Statement stmt = null;
		ResultSet rs = null;

		try {

			stmt = connection.createStatement();

			rs = stmt
					.executeQuery("select * from trafficsources  where is_active=1;");

			while (rs.next()) {

				TrafficSource trafficSource = new TrafficSource();
				Integer id = rs.getInt("id");

				trafficSource.setId(id);
				trafficSource.setFirst_name(rs.getString("first_name"));
				trafficSource.setLast_name(rs.getString("last_name"));
				trafficSource.setCreated_at(rs.getTimestamp("created_at"));
				trafficSource.setUpdated_at(rs.getTimestamp("updated_at"));
				trafficSource.setIs_Active(rs.getInt("is_active"));

				cachedTrafficsourcesMap1.put(id, trafficSource);

			}
		}

		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}

		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}

		finally {
			ProjectUtils.closeConnection(rs, stmt, connection);
		}

		cachedTrafficsourcesMap = cachedTrafficsourcesMap1;

		return cachedTrafficsourcesMap;

	}

}
