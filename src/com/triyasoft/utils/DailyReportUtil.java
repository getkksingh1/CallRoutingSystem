package com.triyasoft.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.triyasoft.daos.ReportsDao;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.TrafficSourceReportModel;
import com.triyasoft.model.TrafficSourceReportModel.TrafficSourceSummary;
import com.triyasoft.model.TrafficSourceSummaryReportModel;
import com.triyasoft.model.UserModel;

public class DailyReportUtil {

	public static int overAllTotalCallsFromSources = 0;
	public static int overAllTotalUniqueCallsFromSources = 0;
	public static int overAllTotalDuplicateCallsFromSources = 0;
	public static double overAllTtotalPayoutTosources = 0;
	public static int overAllZeroSecondsCallsFromSources = 0;
	public static double overAllTotalCallDurationFromSources = 0;

	public static int overAllTotalCallsToBuyers = 0;
	public static int overAllTotalUniqueCallsToBuyers = 0;
	public static int overAllTotalDuplicateCallsToBuyers = 0;
	public static double overAllTtotalPayoutFromBuyers = 0;
	public static int overAllZeroSecondsCallsToBuyers = 0;
	public static double overAllTotalCallDurationToBuyers = 0;
	public static int overAllcallCountForSourcePayment = 0;
	public static int overAllNoBuyerForSource = 0;
	public static int OverAllnumberOfCallsForPayout = 0;

	public static void main(String[] args) throws IOException {

		String buyerName = "sohib-6363/8154";
		System.out.println(buyerName.replaceAll("/", "-"));

	}

	// 2017-07-11 10:31:38.0

	public static String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		return dateFormat.format(date) + ".0";
	}

	public static Workbook loadyesterDayReportWorkbookForSource(
			String startdate, String endDate, int[] accountids) {

		List<CallModel> calls = ReportsDao.loadYesterdayCalls(startdate,
				endDate);

		XSSFWorkbook workbook = new XSSFWorkbook();

		Map<String, List<CallModel>> trafficSourceCallMap = new HashMap<String, List<CallModel>>();

		for (CallModel call : calls) {

			Integer traffic_source_id = call.getTraffic_source_id();

			String trafffic_source_name = call.getTraffic_source();

			boolean userPermitted = false;

			for (int accountid : accountids) {
				if (accountid == traffic_source_id) {
					userPermitted = true;
					break;
				}
			}

			if (!userPermitted)
				continue;

			if (trafffic_source_name == null)
				trafffic_source_name = "No Source";

			String traffic_source_key = traffic_source_id + "";

			List<CallModel> traffic_source_callList = trafficSourceCallMap
					.get(traffic_source_key);

			if (traffic_source_callList == null) {
				traffic_source_callList = new ArrayList<CallModel>();
				trafficSourceCallMap.put(traffic_source_key,
						traffic_source_callList);
			}

			traffic_source_callList.add(call);

		}

		Iterator it2 = trafficSourceCallMap.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry pair = (Map.Entry) it2.next();

			String sourceId = (String) pair.getKey();
			List<CallModel> trafficSourceCallist = (List<CallModel>) pair
					.getValue();

			String trafficSourceName = "";
			if (trafficSourceCallist.size() > 0)
				trafficSourceName = trafficSourceCallist.get(0)
						.getTraffic_source();

			int numberOfCallstoBePaid = 0;

			for (CallModel callModel : trafficSourceCallist) {
				if (callModel.getTraffic_source_revenue() > 0)
					numberOfCallstoBePaid++;
			}

			XSSFSheet buyersheet = workbook.createSheet(numberOfCallstoBePaid
					+ "_" + trafficSourceName + "_" + sourceId);

			makeTrafficCallsReport(workbook, buyersheet, trafficSourceCallist);

			it2.remove();
		}

		return workbook;

	}

	public static Workbook loadyesterDayReportWorkbook(String startdate,
			String endDate) {

		overAllTotalCallsFromSources = 0;
		overAllTotalUniqueCallsFromSources = 0;
		overAllTotalDuplicateCallsFromSources = 0;
		overAllTtotalPayoutTosources = 0;
		overAllZeroSecondsCallsFromSources = 0;
		overAllTotalCallDurationFromSources = 0;

		overAllTotalCallsToBuyers = 0;
		overAllTotalUniqueCallsToBuyers = 0;
		overAllTotalDuplicateCallsToBuyers = 0;
		overAllTtotalPayoutFromBuyers = 0;
		overAllZeroSecondsCallsToBuyers = 0;
		overAllTotalCallDurationToBuyers = 0;
		overAllcallCountForSourcePayment = 0;
		OverAllnumberOfCallsForPayout = 0;

		List<CallModel> calls = ReportsDao.loadYesterdayCalls(startdate,
				endDate);

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet sheet = workbook.createSheet("Master Summary Report");
		makeMasterSummaryReport(workbook, sheet, calls);

		sheet = workbook.createSheet("All Call Report");
		makeAllCallsReport(workbook, sheet, calls);

		Map<String, List<CallModel>> buyerCallMap = new HashMap<String, List<CallModel>>();
		Map<String, List<CallModel>> trafficSourceCallMap = new HashMap<String, List<CallModel>>();

		for (CallModel call : calls) {

			Integer buyer_id = call.getBuyer_id();
			Integer traffic_source_id = call.getTraffic_source_id();

			String buyer_Name = call.getBuyer();
			String trafffic_source_name = call.getTraffic_source();

			if (buyer_Name == null)
				buyer_Name = "No Buyer";

			if (trafffic_source_name == null)
				trafffic_source_name = "No Source";

			String buyerKey = "" + buyer_id;
			String traffic_source_key = traffic_source_id + "";

			List<CallModel> buyerList = buyerCallMap.get(buyerKey);
			if (buyerList == null) {
				buyerList = new ArrayList<CallModel>();
				buyerCallMap.put(buyerKey, buyerList);

			}
			buyerList.add(call);

			List<CallModel> traffic_source_callList = trafficSourceCallMap
					.get(traffic_source_key);

			if (traffic_source_callList == null) {
				traffic_source_callList = new ArrayList<CallModel>();
				trafficSourceCallMap.put(traffic_source_key,
						traffic_source_callList);
			}

			traffic_source_callList.add(call);

		}

		Iterator it = buyerCallMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			String buyer_id = (String) pair.getKey();
			List<CallModel> buyerCallist = (List<CallModel>) pair.getValue();

			String buyerName = "";
			if (buyerCallist.size() > 0)
				buyerName = buyerCallist.get(0).getBuyer();
			if (buyerName == null) {
				buyerName = "No Buyer";
			}

			int numberOfCallstoBePaid = 0;
			Map<String, String> customerNumbers = new HashMap<String, String>();

			for (CallModel callModel : buyerCallist) {

				customerNumbers.put(callModel.getCaller_number(),
						callModel.getCaller_number());

			}

			numberOfCallstoBePaid = customerNumbers.size();
			XSSFSheet buyersheet = workbook.createSheet(numberOfCallstoBePaid
					+ "_" + buyerName.replaceAll("/", "-") + "_" + buyer_id);

			makeBuyerCallsReport(workbook, buyersheet, buyerCallist);

			it.remove();
		}

		Iterator it2 = trafficSourceCallMap.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry pair = (Map.Entry) it2.next();

			String sourceId = (String) pair.getKey();
			List<CallModel> trafficSourceCallist = (List<CallModel>) pair
					.getValue();

			String trafficSourceName = "";
			if (trafficSourceCallist.size() > 0)
				trafficSourceName = trafficSourceCallist.get(0)
						.getTraffic_source();

			int numberOfCallstoBePaid = 0;

			for (CallModel callModel : trafficSourceCallist) {
				if (callModel.getTraffic_source_revenue() > 0)
					numberOfCallstoBePaid++;
			}

			XSSFSheet buyersheet = workbook.createSheet(numberOfCallstoBePaid
					+ "_" + trafficSourceName + "_" + sourceId);

			makeTrafficCallsReport(workbook, buyersheet, trafficSourceCallist);

			it2.remove();
		}

		return workbook;

	}

	private static void makeMasterSummaryReport(XSSFWorkbook workbook,
			XSSFSheet sheet, List<CallModel> calls) {

		Map<String, List<CallModel>> buyerCallMap = new HashMap<String, List<CallModel>>();
		Map<String, List<CallModel>> trafficSourceCallMap = new HashMap<String, List<CallModel>>();

		for (CallModel call : calls) {

			Integer buyer_id = call.getBuyer_id();
			Integer traffic_source_id = call.getTraffic_source_id();

			String buyer_Name = call.getBuyer();
			String trafffic_source_name = call.getTraffic_source();

			if (buyer_Name == null)
				buyer_Name = "No Buyer";

			if (trafffic_source_name == null)
				trafffic_source_name = "No Source";

			String buyerKey = "" + buyer_id;
			String traffic_source_key = "" + traffic_source_id;

			List<CallModel> buyerList = buyerCallMap.get(buyerKey);
			if (buyerList == null) {
				buyerList = new ArrayList<CallModel>();
				buyerCallMap.put(buyerKey, buyerList);

			}
			buyerList.add(call);

			List<CallModel> traffic_source_callList = trafficSourceCallMap
					.get(traffic_source_key);

			if (traffic_source_callList == null) {
				traffic_source_callList = new ArrayList<CallModel>();
				trafficSourceCallMap.put(traffic_source_key,
						traffic_source_callList);
			}

			traffic_source_callList.add(call);

		}

		int rowNum = 0;
		Row buyerSummaryHeader = sheet.createRow(rowNum++);

		Iterator it = buyerCallMap.entrySet().iterator();

		int colNum = 0;

		buyerSummaryHeader.createCell(colNum++).setCellValue("Buyer Name");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Over All Traffic Source Summary".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++).setCellValue(
				"Total Calls to Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Total Calls to Buyer".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++).setCellValue(
				"Total Unique Calls to Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Total Unique Calls to Buyer".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++).setCellValue(
				"Total Duplicate Calls To Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Total Duplicate Calls To Buyer".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++).setCellValue(
				"Zero Second Calls to Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Zero Second Calls to Buyer".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++)
				.setCellValue("Calls for Payout");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Calls for Payout".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++).setCellValue(
				"Average Call Handling Time");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Average Call Handling Time".length(), colNum - 1);

		buyerSummaryHeader.createCell(colNum++).setCellValue(
				"Total Payout from Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				buyerSummaryHeader.getCell(colNum - 1),
				"Total Payout from Buyer".length(), colNum - 1);

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();

			List<CallModel> buyerCallist = (List<CallModel>) pair.getValue();

			String buyerName = "";
			if (buyerCallist.size() > 0)
				buyerName = buyerCallist.get(0).getBuyer();

			if (buyerName == null || "".equals(buyerName))
				buyerName = "No Buyer";

			makeBuyerMasterReportRow(workbook, sheet, buyerCallist, buyerName,
					rowNum++);

			it.remove();
		}

		Row buyerSummaryFooter = sheet.createRow(rowNum++);

		colNum = 0;

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				"Over All Buyers Summary");

		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, false);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				overAllTotalCallsToBuyers);
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				overAllTotalUniqueCallsToBuyers);
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				overAllTotalDuplicateCallsToBuyers);
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				overAllZeroSecondsCallsToBuyers);
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				OverAllnumberOfCallsForPayout);
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		double avgAHT = overAllTotalCallDurationToBuyers
				/ ((double) overAllTotalUniqueCallsToBuyers);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				ProjectUtils.convertDurationToString(avgAHT));
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		buyerSummaryFooter.createCell(colNum++).setCellValue(
				overAllTtotalPayoutFromBuyers);
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				buyerSummaryFooter.getCell(colNum - 1), 0, colNum - 1, true);

		// Blank Rows

		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);

		Row SourceSummaryHeader = sheet.createRow(rowNum++);
		colNum = 0;

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Traffic Source Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Total Calls From Source");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Total Unique Calls From Source");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"No Buyers Call Count");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Zero Second  Calls From Source");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Call Count for Payment");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Average Call Handling Time");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1), 0, colNum - 1);

		SourceSummaryHeader.createCell(colNum++).setCellValue(
				"Total Payout To Source");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				SourceSummaryHeader.getCell(colNum - 1),
				"Total Payout To Source".length() + 1, colNum - 1);

		Iterator it2 = trafficSourceCallMap.entrySet().iterator();
		while (it2.hasNext()) {
			Map.Entry pair = (Map.Entry) it2.next();

			String sheetName = (String) pair.getKey();
			List<CallModel> trafficSourceCallist = (List<CallModel>) pair
					.getValue();

			String trafficSourceName = "";
			if (trafficSourceCallist.size() > 0)
				trafficSourceName = trafficSourceCallist.get(0)
						.getTraffic_source();

			makeTrafficSourceMasterReportRow(workbook, sheet,
					trafficSourceCallist, trafficSourceName, rowNum++);

			it2.remove();
		}

		Row trafficSourceSummaryFooter = sheet.createRow(rowNum++);

		colNum = 0;

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				"Over All Traffic Source Summary");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				overAllTotalCallsFromSources);
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				overAllTotalUniqueCallsFromSources);
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				overAllNoBuyerForSource);
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				overAllZeroSecondsCallsFromSources);
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				overAllcallCountForSourcePayment);
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		double sourceavgAHT = overAllTotalCallDurationFromSources
				/ ((double) overAllTotalUniqueCallsFromSources);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				ProjectUtils.convertDurationToString(sourceavgAHT));
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

		trafficSourceSummaryFooter.createCell(colNum++).setCellValue(
				overAllTtotalPayoutTosources);
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				trafficSourceSummaryFooter.getCell(colNum - 1), 0, colNum - 1);

	}

	private static void makeTrafficSourceMasterReportRow(XSSFWorkbook workbook,
			XSSFSheet sheet, List<CallModel> trafficSourceCallist,
			String trafficSourceName, int rowNum) {

		Map<String, String> customerNumbers = new HashMap<String, String>();
		Map<String, String> zeroSecondsCustomerNumber = new HashMap<String, String>();
		Map<String, String> noBuyerCallCustomerCustomerNumber = new HashMap<String, String>();
		Map<String, String> callforSourcePayout = new HashMap<String, String>();

		int totalCallsFromSource = trafficSourceCallist.size();
		int totalUniqueCallsFromSource = 0;
		double avgAHTOffSource = 0;
		double totalPayoutTosource = 0;
		int zeroSecondsUniqueCallsFromSource = 0;

		double sourceTotalCallDuration = 0;

		for (CallModel call : trafficSourceCallist) {

			if ("0".equals(call.getDuration()) && (call.getBuyer_id() != 0)) {

				zeroSecondsCustomerNumber.put(call.getCaller_number(),
						call.getCaller_number());
			}

			if (call.getTraffic_source_revenue() != 0) {
				callforSourcePayout.put(call.getCaller_number(),
						call.getCaller_number());
			}

			if (call.getBuyer_id() == 0) {
				noBuyerCallCustomerCustomerNumber.put(call.getCaller_number(),
						call.getCaller_number());
			}

			totalPayoutTosource = totalPayoutTosource
					+ call.getTraffic_source_revenue();
			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";

			sourceTotalCallDuration = sourceTotalCallDuration
					+ Double.parseDouble(callDuration);
			customerNumbers.put(call.getCaller_number(),
					call.getCaller_number());

		}

		zeroSecondsUniqueCallsFromSource = zeroSecondsCustomerNumber.size();
		totalUniqueCallsFromSource = customerNumbers.size();

		avgAHTOffSource = sourceTotalCallDuration
				/ ((double) totalUniqueCallsFromSource);

		overAllTotalCallsFromSources = overAllTotalCallsFromSources
				+ totalCallsFromSource;
		overAllTotalUniqueCallsFromSources = overAllTotalUniqueCallsFromSources
				+ totalUniqueCallsFromSource;
		overAllTtotalPayoutTosources = overAllTtotalPayoutTosources
				+ totalPayoutTosource;
		overAllZeroSecondsCallsFromSources = overAllZeroSecondsCallsFromSources
				+ zeroSecondsUniqueCallsFromSource;
		overAllTotalCallDurationFromSources = overAllTotalCallDurationFromSources
				+ sourceTotalCallDuration;
		overAllcallCountForSourcePayment = overAllcallCountForSourcePayment
				+ callforSourcePayout.size();
		overAllNoBuyerForSource = overAllNoBuyerForSource
				+ noBuyerCallCustomerCustomerNumber.size();

		int colNum = 0;

		Row buyerSummary = sheet.createRow(rowNum++);

		Cell buyerNameCell = buyerSummary.createCell(colNum++);
		buyerNameCell.setCellValue(trafficSourceName);

		Cell totalCallsFromSourceKeyCell = buyerSummary.createCell(colNum++);
		totalCallsFromSourceKeyCell.setCellValue(totalCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell totalUniqueCallsFromSourceRowValueCell = buyerSummary
				.createCell(colNum++);
		totalUniqueCallsFromSourceRowValueCell
				.setCellValue(totalUniqueCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell noBuyerCallsFromSourceCell = buyerSummary.createCell(colNum++);
		noBuyerCallsFromSourceCell
				.setCellValue(noBuyerCallCustomerCustomerNumber.size());
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell zeroSecondsCallsFromSourceCell = buyerSummary.createCell(colNum++);
		zeroSecondsCallsFromSourceCell
				.setCellValue(zeroSecondsUniqueCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell callCountFromSourceForPaymentCell = buyerSummary
				.createCell(colNum++);
		callCountFromSourceForPaymentCell.setCellValue(callforSourcePayout
				.size());
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell avgAHTOffSourceValueCell = buyerSummary.createCell(colNum++);
		avgAHTOffSourceValueCell.setCellValue(ProjectUtils
				.convertDurationToString(avgAHTOffSource));
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell totalPayoutTosourceValueCell = buyerSummary.createCell(colNum++);
		totalPayoutTosourceValueCell.setCellValue(totalPayoutTosource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

	}

	private static void makeBuyerMasterReportRow(XSSFWorkbook workbook,
			XSSFSheet sheet, List<CallModel> buyerCallist, String buyerName,
			int rowNum) {

		Map<String, String> customerNumbers = new HashMap<String, String>();

		int totalCallsFromSource = buyerCallist.size();
		int totalUniqueCallsFromSource = 0;
		int totalDuplicateCallsFromSource = 0;
		double avgAHTOffSource = 0;
		double totalPayoutTosource = 0;
		int zeroSecondsCallsFromSource = 0;
		int numberOfCallsForPayout = 0;

		double sourceTotalCallDuration = 0;

		for (CallModel call : buyerCallist) {

			if ("0".equals(call.getDuration()))
				zeroSecondsCallsFromSource++;

			totalPayoutTosource = totalPayoutTosource + call.getBuyer_revenue();

			if (call.getBuyer_revenue() > 0)
				numberOfCallsForPayout++;

			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";

			sourceTotalCallDuration = sourceTotalCallDuration
					+ Double.parseDouble(callDuration);
			customerNumbers.put(call.getCaller_number(),
					call.getCaller_number());

		}

		totalUniqueCallsFromSource = customerNumbers.size();
		totalDuplicateCallsFromSource = totalCallsFromSource
				- totalUniqueCallsFromSource;

		avgAHTOffSource = sourceTotalCallDuration
				/ ((double) totalUniqueCallsFromSource);

		overAllTotalCallsToBuyers = overAllTotalCallsToBuyers
				+ totalCallsFromSource;
		overAllTotalUniqueCallsToBuyers = overAllTotalUniqueCallsToBuyers
				+ totalUniqueCallsFromSource;
		overAllTotalDuplicateCallsToBuyers = overAllTotalDuplicateCallsToBuyers
				+ totalDuplicateCallsFromSource;
		overAllTtotalPayoutFromBuyers = overAllTtotalPayoutFromBuyers
				+ totalPayoutTosource;
		overAllZeroSecondsCallsToBuyers = overAllZeroSecondsCallsToBuyers
				+ zeroSecondsCallsFromSource;
		overAllTotalCallDurationToBuyers = overAllTotalCallDurationToBuyers
				+ sourceTotalCallDuration;
		OverAllnumberOfCallsForPayout = OverAllnumberOfCallsForPayout
				+ numberOfCallsForPayout;

		int colNum = 0;

		Row buyerSummary = sheet.createRow(rowNum++);

		Cell buyerNameCell = buyerSummary.createCell(colNum++);
		buyerNameCell.setCellValue(buyerName);

		Cell totalCallsFromSourceKeyCell = buyerSummary.createCell(colNum++);
		totalCallsFromSourceKeyCell.setCellValue(totalCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell totalUniqueCallsFromSourceRowValueCell = buyerSummary
				.createCell(colNum++);
		totalUniqueCallsFromSourceRowValueCell
				.setCellValue(totalUniqueCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell totalDuplicateCallsFromSourceValueCell = buyerSummary
				.createCell(colNum++);
		totalDuplicateCallsFromSourceValueCell
				.setCellValue(totalDuplicateCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell zeroSecondsCallsFromSourceCell = buyerSummary.createCell(colNum++);
		zeroSecondsCallsFromSourceCell.setCellValue(zeroSecondsCallsFromSource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell numberOfCallsForPayoutCell = buyerSummary.createCell(colNum++);
		numberOfCallsForPayoutCell.setCellValue(numberOfCallsForPayout);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell avgAHTOffSourceValueCell = buyerSummary.createCell(colNum++);
		avgAHTOffSourceValueCell.setCellValue(ProjectUtils
				.convertDurationToString(avgAHTOffSource));
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

		Cell totalPayoutTosourceValueCell = buyerSummary.createCell(colNum++);
		totalPayoutTosourceValueCell.setCellValue(totalPayoutTosource);
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				buyerSummary.getCell(colNum - 1));

	}

	private static void makeTrafficCallsReport(XSSFWorkbook workbook,
			XSSFSheet sheet, List<CallModel> trafficSourceCallist) {

		Comparator<CallModel> comparator = new Comparator<CallModel>() {
			@Override
			public int compare(CallModel left, CallModel right) {
				return left.getCallLandingTimeOnServer().compareTo(
						right.getCallLandingTimeOnServer());
			}
		};

		Collections.sort(trafficSourceCallist, comparator);

		int rowNum = 0;

		Row headerRow = sheet.createRow(rowNum++);
		int colNum = 0;

		Cell headerCellUuid = headerRow.createCell(colNum++);
		headerCellUuid.setCellValue("UUID");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"f3320878-60d3-11e7-ab17-97f40a9cda53 ".length(), colNum - 1);

		Cell headerNumber_called = headerRow.createCell(colNum++);
		headerNumber_called.setCellValue("Traffic Source Number");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Traffic Source Number".length(), colNum - 1);

		// Cell headerConnected_to= headerRow.createCell(colNum++);
		// headerConnected_to.setCellValue("Buyer Number");

		Cell headerCaller_number = headerRow.createCell(colNum++);
		headerCaller_number.setCellValue("Customer Number ");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Customer Number ".length(),
				colNum - 1);

		Cell headerCallerName = headerRow.createCell(colNum++);
		headerCallerName.setCellValue("Customer Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Customer Name   ".length(),
				colNum - 1);

		Cell headerTraffic_source_id = headerRow.createCell(colNum++);
		headerTraffic_source_id.setCellValue("Traffic Source ID");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Traffic Source ID".length(),
				colNum - 1);

		Cell headerTraffic_source = headerRow.createCell(colNum++);
		headerTraffic_source.setCellValue("Traffic Source Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Traffic Source Name".length(),
				colNum - 1);

		Cell headerBuyer = headerRow.createCell(colNum++);
		headerBuyer.setCellValue("Buyer Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Name".length(),
				colNum - 1);

		// Cell headerBuyer_id = headerRow.createCell(colNum++);
		// headerBuyer_id.setCellValue("Buyer ID");

		Cell headerCallStatusAtAnswer = headerRow.createCell(colNum++);
		headerCallStatusAtAnswer.setCellValue("Call Status At Answer");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Call Status At Answer".length(), colNum - 1);

		// Cell headerBillRate = headerRow.createCell(colNum++);
		// headerBillRate.setCellValue("Telephone Billing Rate");

		Cell headerHangupCause = headerRow.createCell(colNum++);
		headerHangupCause.setCellValue("Hangup Cause");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "ORIGINATOR_CANCEL ".length(),
				colNum - 1);

		Cell headerCalltime = headerRow.createCell(colNum++);
		headerCalltime.setCellValue("Call Start Time");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"2017-07-04 12:15:17.0  ".length(), colNum - 1);

		Cell headerCallendtime = headerRow.createCell(colNum++);
		headerCallendtime.setCellValue("Call End Time");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"2017-07-04 12:15:17.0  ".length(), colNum - 1);

		Cell headerBillduration = headerRow.createCell(colNum++);
		headerBillduration.setCellValue("Billing Duration");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Billing Duration".length(),
				colNum - 1);

		Cell headerDuration = headerRow.createCell(colNum++);
		headerDuration.setCellValue("Call Duration");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Call Duration".length(),
				colNum - 1);

		// Cell headerBuyer_revenue = headerRow.createCell(colNum++);
		// headerBuyer_revenue.setCellValue("Buyer Revenue");

		Cell headerTraffic_source_revenue = headerRow.createCell(colNum++);
		headerTraffic_source_revenue.setCellValue("Traffic Source Payout");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Traffic Source Payout".length(), colNum - 1);

		// Cell headerPhonecost = headerRow.createCell(colNum++);
		// headerPhonecost.setCellValue("Phone Cost");

		// Cell headerCallprofit = headerRow.createCell(colNum++);
		// headerCallprofit.setCellValue("Call Profit");

		for (CallModel call : trafficSourceCallist) {
			Row row = sheet.createRow(rowNum++);
			colNum = 0;

			Cell cellUuid = row.createCell(colNum++);
			cellUuid.setCellValue(call.getUuid());

			Cell number_called = row.createCell(colNum++);
			number_called.setCellType(CellType.NUMERIC);
			number_called.setCellValue(call.getNumber_called());

			// Cell connected_to= row.createCell(colNum++);
			// connected_to.setCellType(CellType.NUMERIC);
			// connected_to.setCellValue(call.getConnected_to());

			Cell caller_number = row.createCell(colNum++);
			caller_number.setCellType(CellType.NUMERIC);
			caller_number.setCellValue(call.getCaller_number());

			Cell callerName = row.createCell(colNum++);
			callerName.setCellType(CellType.NUMERIC);
			callerName.setCellValue(call.getCallerName());

			Cell traffic_source_id = row.createCell(colNum++);
			traffic_source_id.setCellValue(call.getTraffic_source_id());

			Cell traffic_source = row.createCell(colNum++);
			traffic_source.setCellValue(call.getTraffic_source());

			String buyerName = call.getBuyer();
			if (buyerName == null || buyerName.trim().length() == 0)
				buyerName = "No Buyer";

			Cell buyer = row.createCell(colNum++);
			buyer.setCellValue(buyerName);

			// Cell buyer_id = row.createCell(colNum++);
			// buyer_id.setCellValue(call.getBuyer_id());

			Cell callStatusAtAnswer = row.createCell(colNum++);
			callStatusAtAnswer.setCellValue(call.getCallStatusAtAnswer());

			// Cell billRate = row.createCell(colNum++);
			// String callBillRate = call.getBillRate();
			// if(callBillRate == null)
			// callBillRate ="0.0";

			// billRate.setCellValue(Double.parseDouble(call.getBillRate()));

			Cell hangupCause = row.createCell(colNum++);
			hangupCause.setCellValue(call.getHangupCause());

			Cell calltime = row.createCell(colNum++);
			calltime.setCellValue(call.getCallLandingTimeOnServer().toString());

			Cell callEndtime = row.createCell(colNum++);
			callEndtime.setCellValue(formatDate(call.getEndTime()));

			Cell billduration = row.createCell(colNum++);
			String callBillDuration = call.getBillDuration();
			if (callBillDuration == null)
				callBillDuration = "0";
			billduration.setCellValue(Integer.parseInt(callBillDuration));

			Cell duration = row.createCell(colNum++);
			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";
			duration.setCellValue(Integer.parseInt(callDuration));

			// Cell buyer_revenue = row.createCell(colNum++);
			// buyer_revenue.setCellValue(call.getBuyer_revenue());

			Cell traffic_source_revenue = row.createCell(colNum++);
			traffic_source_revenue.setCellValue(call
					.getTraffic_source_revenue());

			// Cell phonecost = row.createCell(colNum++);
			// String callPhoneCost =call.getTotalCost();
			// if(callPhoneCost == null)
			// callPhoneCost = "0.0";
			//

			// phonecost.setCellValue(Double.parseDouble(callPhoneCost));

			// Cell callprofit = row.createCell(colNum++);
			// callprofit.setCellValue(call.getCallProfit());

		}

		Map<String, String> customerNumbers = new HashMap<String, String>();
		Map<String, String> zeroSecondsCustomerNumber = new HashMap<String, String>();
		Map<String, String> noBuyerCallCustomerCustomerNumber = new HashMap<String, String>();
		Map<String, String> callforSourcePayout = new HashMap<String, String>();

		int totalCallsFromSource = trafficSourceCallist.size();
		int totalUniqueCallsFromSource = 0;
		double avgAHTOffSource = 0;
		double totalPayoutTosource = 0;
		int zeroSecondsUniqueCallsFromSource = 0;

		double sourceTotalCallDuration = 0;

		for (CallModel call : trafficSourceCallist) {

			if ("0".equals(call.getDuration()) && (call.getBuyer_id() != 0)) {

				zeroSecondsCustomerNumber.put(call.getCaller_number(),
						call.getCaller_number());
			}

			if (call.getTraffic_source_revenue() != 0) {
				callforSourcePayout.put(call.getCaller_number(),
						call.getCaller_number());
			}

			if (call.getBuyer_id() == 0) {
				noBuyerCallCustomerCustomerNumber.put(call.getCaller_number(),
						call.getCaller_number());
			}

			totalPayoutTosource = totalPayoutTosource
					+ call.getTraffic_source_revenue();
			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";

			sourceTotalCallDuration = sourceTotalCallDuration
					+ Double.parseDouble(callDuration);
			customerNumbers.put(call.getCaller_number(),
					call.getCaller_number());

		}

		zeroSecondsUniqueCallsFromSource = zeroSecondsCustomerNumber.size();
		totalUniqueCallsFromSource = customerNumbers.size();

		avgAHTOffSource = sourceTotalCallDuration
				/ ((double) totalUniqueCallsFromSource);

		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);

		Row Summary = sheet.createRow(rowNum++);
		colNum = 0;

		Cell sourceSummary = Summary.createCell(colNum++);
		sourceSummary.setCellValue("Traffic Source Summary");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Row totalCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalCallsFromSourceKeyCell = totalCallsFromSourceRow
				.createCell(colNum++);
		totalCallsFromSourceKeyCell.setCellValue("Total Calls From Source");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell totalCallsFromSourceValueCell = totalCallsFromSourceRow
				.createCell(colNum++);
		totalCallsFromSourceValueCell.setCellValue(totalCallsFromSource);

		Row totalUniqueCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalUniqueCallsFromSourceRowKeyCell = totalUniqueCallsFromSourceRow
				.createCell(colNum++);
		totalUniqueCallsFromSourceRowKeyCell
				.setCellValue("Total Unique Calls From Source");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell totalUniqueCallsFromSourceRowValueCell = totalUniqueCallsFromSourceRow
				.createCell(colNum++);
		totalUniqueCallsFromSourceRowValueCell
				.setCellValue(totalUniqueCallsFromSource);

		Row totalNoBuyerCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalNoBuyersFromSourceKeyCell = totalNoBuyerCallsFromSourceRow
				.createCell(colNum++);
		totalNoBuyersFromSourceKeyCell.setCellValue("No Buyers Call Count");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell totalNoBuyersFromSourceValueCell = totalNoBuyerCallsFromSourceRow
				.createCell(colNum++);
		totalNoBuyersFromSourceValueCell
				.setCellValue(noBuyerCallCustomerCustomerNumber.size());

		Row zeroSecondsCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell zeroSecondsCallsFromSourceKeyCell = zeroSecondsCallsFromSourceRow
				.createCell(colNum++);
		zeroSecondsCallsFromSourceKeyCell
				.setCellValue("Zero Second Unique Calls From Source");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell zeroSecondsCallsFromSourceCell = zeroSecondsCallsFromSourceRow
				.createCell(colNum++);
		zeroSecondsCallsFromSourceCell
				.setCellValue(zeroSecondsUniqueCallsFromSource);

		Row callCountForPaymentSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell callCountForSourcePaymentKeyCell = callCountForPaymentSourceRow
				.createCell(colNum++);
		callCountForSourcePaymentKeyCell.setCellValue("Call Count for Payment");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell callCountForSourcePaymentCell = callCountForPaymentSourceRow
				.createCell(colNum++);
		callCountForSourcePaymentCell.setCellValue(callforSourcePayout.size());

		Row avgAHTOffSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell avgAHTOffSourceKeyCell = avgAHTOffSourceRow.createCell(colNum++);
		avgAHTOffSourceKeyCell.setCellValue("Average Call Handling Time");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell avgAHTOffSourceValueCell = avgAHTOffSourceRow.createCell(colNum++);
		avgAHTOffSourceValueCell.setCellValue(ProjectUtils
				.convertDurationToString(avgAHTOffSource));
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				avgAHTOffSourceValueCell);

		Row totalPayoutTosourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalPayoutTosourceKeyCell = totalPayoutTosourceRow
				.createCell(colNum++);
		totalPayoutTosourceKeyCell.setCellValue("Total Payout To Source");
		WorkbookStyleUtil.decorateSourceSummaryFooterCell(workbook, sheet,
				sheet.getRow(rowNum - 1).getCell(colNum - 1), 0, colNum - 1);

		Cell totalPayoutTosourceValueCell = totalPayoutTosourceRow
				.createCell(colNum++);
		totalPayoutTosourceValueCell.setCellValue(totalPayoutTosource);

	}

	private static void makeAllCallsReport(XSSFWorkbook workbook,
			XSSFSheet sheet, List<CallModel> calls) {
		int rowNum = 0;

		Comparator<CallModel> comparator = new Comparator<CallModel>() {
			@Override
			public int compare(CallModel left, CallModel right) {
				return left.getCallLandingTimeOnServer().compareTo(
						right.getCallLandingTimeOnServer());
			}
		};

		Collections.sort(calls, comparator);

		Row headerRow = sheet.createRow(rowNum++);
		int colNum = 0;

		Cell headerCellUuid = headerRow.createCell(colNum++);
		headerCellUuid.setCellValue("UUID");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"14149424-60bc-11e7-972a".length(), colNum - 1);

		Cell headerNumber_called = headerRow.createCell(colNum++);
		headerNumber_called.setCellValue("Traffic Source Number");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Traffic Source Number".length(), colNum - 1);

		Cell headerConnected_to = headerRow.createCell(colNum++);
		headerConnected_to.setCellValue("Buyer Number");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Number".length(),
				colNum - 1);

		Cell headerCaller_number = headerRow.createCell(colNum++);
		headerCaller_number.setCellValue("Customer Number ");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Customer Number ".length(),
				colNum - 1);

		Cell headerCallerName = headerRow.createCell(colNum++);
		headerCallerName.setCellValue("Customer Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Customer Name".length(),
				colNum - 1);

		Cell headerTraffic_source_id = headerRow.createCell(colNum++);
		headerTraffic_source_id.setCellValue("Traffic Source ID");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Traffic Source ID".length(),
				colNum - 1);

		Cell headerTraffic_source = headerRow.createCell(colNum++);
		headerTraffic_source.setCellValue("Traffic Source Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Traffic Source Name".length(),
				colNum - 1);

		Cell headerBuyer = headerRow.createCell(colNum++);
		headerBuyer.setCellValue("Buyer Name");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Name".length(),
				colNum - 1);

		Cell headerBuyer_id = headerRow.createCell(colNum++);
		headerBuyer_id.setCellValue("Buyer ID");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer ID".length(), colNum - 1);

		Cell headerCallStatusAtAnswer = headerRow.createCell(colNum++);
		headerCallStatusAtAnswer.setCellValue("Call Status At Answer");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Call Status At Answer".length(), colNum - 1);

		/*
		 * 
		 * Cell headerBillRate = headerRow.createCell(colNum++);
		 * headerBillRate.setCellValue("Phone Billing Rate");
		 * WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
		 * headerRow.getCell(colNum-1), "Phone Billing Rate".length(),
		 * colNum-1);
		 */

		Cell headerHangupCause = headerRow.createCell(colNum++);
		headerHangupCause.setCellValue("Hangup Cause");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Hangup Cause".length(),
				colNum - 1);

		Cell headerCalltime = headerRow.createCell(colNum++);
		headerCalltime.setCellValue("Call Start Time");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Call Start Time".length(),
				colNum - 1);

		Cell headerCallendtime = headerRow.createCell(colNum++);
		headerCallendtime.setCellValue("Call End Time");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Call End Time".length(),
				colNum - 1);

		Cell headerBillduration = headerRow.createCell(colNum++);
		headerBillduration.setCellValue("Billing Duration");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Billing Duration".length(),
				colNum - 1);

		Cell headerDuration = headerRow.createCell(colNum++);
		headerDuration.setCellValue("Call Duration");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Call Duration".length(),
				colNum - 1);

		Cell headerBuyer_revenue = headerRow.createCell(colNum++);
		headerBuyer_revenue.setCellValue("Buyer Revenue");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Revenue".length(),
				colNum - 1);

		Cell headerTraffic_source_revenue = headerRow.createCell(colNum++);
		headerTraffic_source_revenue.setCellValue("Traffic Source Payout");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Traffic Source Payout".length(), colNum - 1);

		Cell headerPhonecost = headerRow.createCell(colNum++);
		headerPhonecost.setCellValue("Phone Cost");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Phone Cost".length(),
				colNum - 1);

		Cell headerCallprofit = headerRow.createCell(colNum++);
		headerCallprofit.setCellValue("Call Profit");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Call Profit".length(),
				colNum - 1);

		for (CallModel call : calls) {
			Row row = sheet.createRow(rowNum++);
			colNum = 0;

			Cell cellUuid = row.createCell(colNum++);
			cellUuid.setCellValue(call.getUuid());

			Cell number_called = row.createCell(colNum++);
			number_called.setCellType(CellType.NUMERIC);
			number_called.setCellValue(call.getNumber_called());

			Cell connected_to = row.createCell(colNum++);
			connected_to.setCellType(CellType.NUMERIC);
			connected_to.setCellValue(call.getConnected_to());

			Cell caller_number = row.createCell(colNum++);
			caller_number.setCellType(CellType.NUMERIC);
			caller_number.setCellValue(call.getCaller_number());

			Cell callerName = row.createCell(colNum++);
			callerName.setCellType(CellType.NUMERIC);
			callerName.setCellValue(call.getCallerName());

			Cell traffic_source_id = row.createCell(colNum++);
			traffic_source_id.setCellValue(call.getTraffic_source_id());

			Cell traffic_source = row.createCell(colNum++);
			traffic_source.setCellValue(call.getTraffic_source());

			String buyerName = call.getBuyer();
			if (buyerName == null || buyerName.trim().length() == 0)
				buyerName = "No Buyer";

			Cell buyer = row.createCell(colNum++);
			buyer.setCellValue(buyerName);

			Cell buyer_id = row.createCell(colNum++);
			buyer_id.setCellValue(call.getBuyer_id());

			Cell callStatusAtAnswer = row.createCell(colNum++);
			callStatusAtAnswer.setCellValue(call.getCallStatusAtAnswer());

			// Cell billRate = row.createCell(colNum++);
			// String callBillRate = call.getBillRate();
			// if(callBillRate == null)
			// callBillRate ="0.0";

			// billRate.setCellValue(Double.parseDouble(call.getBillRate()));

			Cell hangupCause = row.createCell(colNum++);
			hangupCause.setCellValue(call.getHangupCause());

			Cell calltime = row.createCell(colNum++);
			calltime.setCellValue(call.getCallLandingTimeOnServer().toString());

			Cell callEndtime = row.createCell(colNum++);
			callEndtime.setCellValue(formatDate(call.getEndTime()));

			Cell billduration = row.createCell(colNum++);
			String callBillDuration = call.getBillDuration();
			if (callBillDuration == null)
				callBillDuration = "0";
			billduration.setCellValue(Integer.parseInt(callBillDuration));

			Cell duration = row.createCell(colNum++);
			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";
			duration.setCellValue(Integer.parseInt(callDuration));

			Cell buyer_revenue = row.createCell(colNum++);
			buyer_revenue.setCellValue(call.getBuyer_revenue());

			Cell traffic_source_revenue = row.createCell(colNum++);
			traffic_source_revenue.setCellValue(call
					.getTraffic_source_revenue());

			Cell phonecost = row.createCell(colNum++);
			String callPhoneCost = call.getTotalCost();
			if (callPhoneCost == null)
				callPhoneCost = "0.0";

			phonecost.setCellValue(Double.parseDouble(callPhoneCost));

			Cell callprofit = row.createCell(colNum++);
			callprofit.setCellValue(call.getCallProfit());

		}

	}

	private static void makeBuyerCallsReport(XSSFWorkbook workbook,
			XSSFSheet sheet, List<CallModel> calls) {

		Comparator<CallModel> comparator = new Comparator<CallModel>() {
			@Override
			public int compare(CallModel left, CallModel right) {
				return left.getCallLandingTimeOnServer().compareTo(
						right.getCallLandingTimeOnServer());
			}
		};

		Collections.sort(calls, comparator);

		int rowNum = 0;

		Row headerRow = sheet.createRow(rowNum++);
		int colNum = 0;

		Cell headerCellUuid = headerRow.createCell(colNum++);
		headerCellUuid.setCellValue("UUID");
		WorkbookStyleUtil
				.decorateBuyerSummaryHeaderCell(workbook, sheet,
						headerRow.getCell(colNum - 1),
						"84147664-60cf-11e7-bd62-43e803304d81".length() + 3,
						colNum - 1);

		// Cell headerNumber_called = headerRow.createCell(colNum++);
		// headerNumber_called.setCellValue("Traffic Source Number");

		Cell headerConnected_to = headerRow.createCell(colNum++);
		headerConnected_to.setCellValue("Buyer Number");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Number".length() + 1,
				colNum - 1);

		Cell headerCaller_number = headerRow.createCell(colNum++);
		headerCaller_number.setCellValue("Customer Number ");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Customer Number".length() + 1,
				colNum - 1);

		Cell headerCallerName = headerRow.createCell(colNum++);
		headerCallerName.setCellValue("Customer Name");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Customer Name".length() + 3,
				colNum - 1);

		// Cell headerTraffic_source_id = headerRow.createCell(colNum++);
		// headerTraffic_source_id.setCellValue("Traffic Source ID");

		// Cell headerTraffic_source = headerRow.createCell(colNum++);
		// headerTraffic_source.setCellValue("Traffic Source Name");

		Cell headerBuyer = headerRow.createCell(colNum++);
		headerBuyer.setCellValue("Buyer Name");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Name".length() + 2,
				colNum - 1);

		Cell headerBuyer_id = headerRow.createCell(colNum++);
		headerBuyer_id.setCellValue("Buyer ID");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer ID".length() + 2,
				colNum - 1);

		Cell headerCallStatusAtAnswer = headerRow.createCell(colNum++);
		headerCallStatusAtAnswer.setCellValue("Call Status At Answer");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"Call Status At Answer".length() + 1, colNum - 1);

		// Cell headerBillRate = headerRow.createCell(colNum++);
		// headerBillRate.setCellValue("Telephone Billing Rate");

		Cell headerHangupCause = headerRow.createCell(colNum++);
		headerHangupCause.setCellValue("Hangup Cause");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "NORMAL_CLEARING".length() + 1,
				colNum - 1);

		Cell headerCalltime = headerRow.createCell(colNum++);
		headerCalltime.setCellValue("Call Start Time");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"2017-07-04 11:45:31.0  ".length() + 1, colNum - 1);

		Cell headerCallendtime = headerRow.createCell(colNum++);
		headerCallendtime.setCellValue("Call End Time");
		WorkbookStyleUtil.decorateSourceSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1),
				"2017-07-04 11:45:31.0".length(), colNum - 1);

		Cell headerBillduration = headerRow.createCell(colNum++);
		headerBillduration.setCellValue("Billing Duration");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Billing Duration".length() + 1,
				colNum - 1);

		Cell headerDuration = headerRow.createCell(colNum++);
		headerDuration.setCellValue("Call Duration");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Call Duration".length() + 1,
				colNum - 1);

		Cell headerBuyer_revenue = headerRow.createCell(colNum++);
		headerBuyer_revenue.setCellValue("Buyer Revenue");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Buyer Revenue".length() + 1,
				colNum - 1);

		Cell headerRecordingUrl = headerRow.createCell(colNum++);
		headerRecordingUrl.setCellValue("Recording URL");
		WorkbookStyleUtil.decorateBuyerSummaryHeaderCell(workbook, sheet,
				headerRow.getCell(colNum - 1), "Recording URL".length() + 1,
				colNum - 1);

		// Cell headerTraffic_source_revenue = headerRow.createCell(colNum++);
		// headerTraffic_source_revenue.setCellValue("Traffic Source Payout");

		// Cell headerPhonecost = headerRow.createCell(colNum++);
		// headerPhonecost.setCellValue("Phone Cost");

		// Cell headerCallprofit = headerRow.createCell(colNum++);
		// headerCallprofit.setCellValue("Call Profit");

		for (CallModel call : calls) {
			Row row = sheet.createRow(rowNum++);
			colNum = 0;

			Cell cellUuid = row.createCell(colNum++);
			cellUuid.setCellValue(call.getUuid());

			// Cell number_called = row.createCell(colNum++);
			// number_called.setCellType(CellType.NUMERIC);
			// number_called.setCellValue(call.getNumber_called());

			Cell connected_to = row.createCell(colNum++);
			connected_to.setCellType(CellType.NUMERIC);
			connected_to.setCellValue(call.getConnected_to());

			Cell caller_number = row.createCell(colNum++);
			caller_number.setCellType(CellType.NUMERIC);
			caller_number.setCellValue(call.getCaller_number());

			Cell callerName = row.createCell(colNum++);
			callerName.setCellType(CellType.NUMERIC);
			callerName.setCellValue(call.getCallerName());

			// Cell traffic_source_id = row.createCell(colNum++);
			// traffic_source_id.setCellValue(call.getTraffic_source_id());

			// Cell traffic_source = row.createCell(colNum++);
			// traffic_source.setCellValue(call.getTraffic_source());

			Cell buyer = row.createCell(colNum++);
			buyer.setCellValue(call.getBuyer());

			Cell buyer_id = row.createCell(colNum++);
			buyer_id.setCellValue(call.getBuyer_id());

			Cell callStatusAtAnswer = row.createCell(colNum++);
			callStatusAtAnswer.setCellValue(call.getCallStatusAtAnswer());

			// Cell billRate = row.createCell(colNum++);
			// String callBillRate = call.getBillRate();
			// if(callBillRate == null)
			// callBillRate ="0.0";

			// billRate.setCellValue(Double.parseDouble(call.getBillRate()));

			Cell hangupCause = row.createCell(colNum++);
			hangupCause.setCellValue(call.getHangupCause());

			Cell calltime = row.createCell(colNum++);
			calltime.setCellValue(call.getCallLandingTimeOnServer().toString());

			Cell callEndtime = row.createCell(colNum++);
			callEndtime.setCellValue(formatDate(call.getEndTime()));

			Cell billduration = row.createCell(colNum++);
			String callBillDuration = call.getBillDuration();
			if (callBillDuration == null)
				callBillDuration = "0";
			billduration.setCellValue(Integer.parseInt(callBillDuration));

			Cell duration = row.createCell(colNum++);
			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";
			duration.setCellValue(Integer.parseInt(callDuration));

			Cell buyer_revenue = row.createCell(colNum++);
			buyer_revenue.setCellValue(call.getBuyer_revenue());

			Cell recording_url = row.createCell(colNum++);
			recording_url.setCellValue(call.getRecording_url());

			// Cell traffic_source_revenue = row.createCell(colNum++);
			// traffic_source_revenue.setCellValue(call.getTraffic_source_revenue());

			// Cell phonecost = row.createCell(colNum++);
			// String callPhoneCost =call.getTotalCost();
			// if(callPhoneCost == null)
			// callPhoneCost = "0.0";
			//

			// phonecost.setCellValue(Double.parseDouble(callPhoneCost));

			// Cell callprofit = row.createCell(colNum++);
			// callprofit.setCellValue(call.getCallProfit());

		}

		Map<String, String> customerNumbers = new HashMap<String, String>();

		int totalCallsFromSource = calls.size();
		int totalUniqueCallsFromSource = 0;
		int totalDuplicateCallsFromSource = 0;
		double avgAHTOffSource = 0;
		double totalPayoutTosource = 0;
		int zeroSecondsCallsFromSource = 0;

		double sourceTotalCallDuration = 0;

		for (CallModel call : calls) {

			if ("0".equals(call.getDuration()))
				zeroSecondsCallsFromSource++;

			totalPayoutTosource = totalPayoutTosource + call.getBuyer_revenue();
			String callDuration = call.getDuration();
			if (callDuration == null)
				callDuration = "0";

			sourceTotalCallDuration = sourceTotalCallDuration
					+ Double.parseDouble(callDuration);
			customerNumbers.put(call.getCaller_number(),
					call.getCaller_number());

		}

		totalUniqueCallsFromSource = customerNumbers.size();
		totalDuplicateCallsFromSource = totalCallsFromSource
				- totalUniqueCallsFromSource;

		avgAHTOffSource = sourceTotalCallDuration
				/ ((double) totalUniqueCallsFromSource);

		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);

		Row Summary = sheet.createRow(rowNum++);
		colNum = 0;

		Cell sourceSummary = Summary.createCell(colNum++);
		sourceSummary.setCellValue("Buyer Summary");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				sourceSummary, "NORMAL_CLEARING".length() + 1, colNum - 1,
				false);

		Row totalCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalCallsFromSourceKeyCell = totalCallsFromSourceRow
				.createCell(colNum++);
		totalCallsFromSourceKeyCell.setCellValue("Total Calls to Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				totalCallsFromSourceKeyCell, "NORMAL_CLEARING".length() + 1,
				colNum - 1, false);

		Cell totalCallsFromSourceValueCell = totalCallsFromSourceRow
				.createCell(colNum++);
		totalCallsFromSourceValueCell.setCellValue(totalCallsFromSource);

		Row totalUniqueCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalUniqueCallsFromSourceRowKeyCell = totalUniqueCallsFromSourceRow
				.createCell(colNum++);
		totalUniqueCallsFromSourceRowKeyCell
				.setCellValue("Total Unique Calls to Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				totalUniqueCallsFromSourceRowKeyCell,
				"NORMAL_CLEARING".length() + 1, colNum - 1, false);

		Cell totalUniqueCallsFromSourceRowValueCell = totalUniqueCallsFromSourceRow
				.createCell(colNum++);
		totalUniqueCallsFromSourceRowValueCell
				.setCellValue(totalUniqueCallsFromSource);

		Row totalDuplicateCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalDuplicateCallsFromSourceKeyCell = totalDuplicateCallsFromSourceRow
				.createCell(colNum++);
		totalDuplicateCallsFromSourceKeyCell
				.setCellValue("Total Duplicate Calls To Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				totalDuplicateCallsFromSourceKeyCell,
				"NORMAL_CLEARING".length() + 1, colNum - 1, false);

		Cell totalDuplicateCallsFromSourceValueCell = totalDuplicateCallsFromSourceRow
				.createCell(colNum++);
		totalDuplicateCallsFromSourceValueCell
				.setCellValue(totalDuplicateCallsFromSource);

		Row zeroSecondsCallsFromSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell zeroSecondsCallsFromSourceKeyCell = zeroSecondsCallsFromSourceRow
				.createCell(colNum++);
		zeroSecondsCallsFromSourceKeyCell
				.setCellValue("Zero Second Calls to Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				zeroSecondsCallsFromSourceKeyCell,
				"NORMAL_CLEARING".length() + 1, colNum - 1, false);

		Cell zeroSecondsCallsFromSourceCell = zeroSecondsCallsFromSourceRow
				.createCell(colNum++);
		zeroSecondsCallsFromSourceCell.setCellValue(zeroSecondsCallsFromSource);

		Row avgAHTOffSourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell avgAHTOffSourceKeyCell = avgAHTOffSourceRow.createCell(colNum++);
		avgAHTOffSourceKeyCell.setCellValue("Average Call Handling Time ");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				avgAHTOffSourceKeyCell, "NORMAL_CLEARING".length() + 1,
				colNum - 1, false);

		Cell avgAHTOffSourceValueCell = avgAHTOffSourceRow.createCell(colNum++);
		avgAHTOffSourceValueCell.setCellValue(ProjectUtils
				.convertDurationToString(avgAHTOffSource));
		WorkbookStyleUtil.decorateGeneralCell(workbook,
				avgAHTOffSourceValueCell);

		Row totalPayoutTosourceRow = sheet.createRow(rowNum++);
		colNum = 0;

		Cell totalPayoutTosourceKeyCell = totalPayoutTosourceRow
				.createCell(colNum++);
		totalPayoutTosourceKeyCell.setCellValue("Total Payout from Buyer");
		WorkbookStyleUtil.decorateBuyerSummaryFooterCell(workbook, sheet,
				totalPayoutTosourceKeyCell, "NORMAL_CLEARING".length() + 1,
				colNum - 1, false);

		Cell totalPayoutTosourceValueCell = totalPayoutTosourceRow
				.createCell(colNum++);
		totalPayoutTosourceValueCell.setCellValue(totalPayoutTosource);

	}

	public static List<CallModel> getReportRawDataForTrafficSource(
			HttpServletRequest request) {

		// window.location =
		// "reports1.jsp?startdate="+startdate+"&endDate="+endDate+"&buyerId="+buyerId+"&sourceId="+sourceId;

		String buyerId = request.getParameter("buyerId");
		String sourceId = request.getParameter("sourceId");
		String startdate = request.getParameter("startdate");
		String endDate = request.getParameter("endDate");

		System.out.println(startdate);

		if (startdate == null || startdate.length() == 0)
			startdate = ProjectUtils.getTodayStrInClientsTimezone(null);
		else
			startdate = ProjectUtils.getDateStrInClientsTimezone(startdate);

		if (endDate == null || endDate.length() == 0)
			endDate = ProjectUtils.getTomorrowStrInClientsTimezone(null);
		else
			endDate = ProjectUtils.getDateStrInClientsTimezone(endDate);

		List<CallModel> lst = ReportsDao.loadAllCall(buyerId, sourceId,
				startdate, endDate);

		List<CallModel> filteredList = new ArrayList<CallModel>();

		UserModel user = (UserModel) request.getSession().getAttribute("user");
		int[] accountids = user.getAccount_id();
		for (int i = 0; i < lst.size(); i++) {
			for (int accontid : accountids) {
				if (lst.get(i).getTraffic_source_id() == accontid) {
					filteredList.add(lst.get(i));
				}
			}

		}

		return filteredList;

	}

	public static List<CallModel> getReportRawData(HttpServletRequest request) {
		// window.location =
		// "reports1.jsp?startdate="+startdate+"&endDate="+endDate+"&buyerId="+buyerId+"&sourceId="+sourceId;

		String buyerId = request.getParameter("buyerId");
		String sourceId = request.getParameter("sourceId");
		String startdate = request.getParameter("startdate");
		String endDate = request.getParameter("endDate");

		System.out.println(startdate);

		if (startdate == null || startdate.length() == 0)
			startdate = ProjectUtils.getTodayStrInClientsTimezone(null);
		else
			startdate = ProjectUtils.getDateStrInClientsTimezone(startdate);

		if (endDate == null || endDate.length() == 0)
			endDate = ProjectUtils.getTomorrowStrInClientsTimezone(null);
		else
			endDate = ProjectUtils.getDateStrInClientsTimezone(endDate);

		List<CallModel> lst = ReportsDao.loadAllCall(buyerId, sourceId,
				startdate, endDate);

		return lst;

	}

	public static TrafficSourceReportModel getTrafficSourceReport(
			HttpServletRequest request) {

		String startdateInput = request.getParameter("startdate");
		String endDateInput = request.getParameter("endDate");

		java.util.Date startDate = null;
		java.util.Date endDate = null;

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));

		if (startdateInput != null && startdateInput.trim().length() > 0) {
			try {
				startDate = formatter.parse(startdateInput);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			startDate = new java.util.Date();
		}

		if (endDateInput != null && endDateInput.trim().length() > 0) {
			try {
				endDate = formatter.parse(endDateInput);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			endDate.setTime(endDate.getTime() + (86400 + 1) * 1000);
		}

		else {

			endDate = new java.util.Date();
			endDate.setTime(startDate.getTime() + (86400 + 1) * 1000);

		}

		startdateInput = formatter.format(startDate);
		endDateInput = formatter.format(endDate);

		List<CallModel> calls = ReportsDao.loadYesterdayCalls(startdateInput,
				endDateInput);

		TrafficSourceReportModel sourceReportModel = new TrafficSourceReportModel();

		for (CallModel callModel : calls) {

			if (callModel.getTraffic_source_id() == 0)
				continue;

			sourceReportModel
					.setTotalCall(sourceReportModel.getTotalCall() + 1);

			TrafficSourceSummary trafficSourceSummary = sourceReportModel
					.getTrafficSourcesSummary().get(
							callModel.getTraffic_source_id());

			if (trafficSourceSummary == null) {
				trafficSourceSummary = sourceReportModel
						.getTrafficSourceSummaryInstance();
				trafficSourceSummary.setTrafficSourceId(callModel
						.getTraffic_source_id());
				trafficSourceSummary.setTrafficSourceName(callModel
						.getTraffic_source());
				sourceReportModel.getTrafficSourcesSummary().put(
						callModel.getTraffic_source_id(), trafficSourceSummary);

			}

			trafficSourceSummary.getCalls().add(callModel);

			if (callModel.getBuyer_id() != 0) {
				trafficSourceSummary.setBuyerConnected(trafficSourceSummary
						.getBuyerConnected() + 1);
				trafficSourceSummary.setTotalCalls(trafficSourceSummary
						.getTotalCalls() + 1);

				String duration = callModel.getDuration();
				if (duration == null || duration.trim().length() == 0)
					duration = "0";
				trafficSourceSummary.setTotalCallTime(trafficSourceSummary
						.getTotalCallTime() + Double.parseDouble(duration));

				if (callModel.getBuyer_revenue() > 0) {
					// trafficSourceSummary.setBuyerConnected(trafficSourceSummary.getBuyerConnected()+1);
					trafficSourceSummary
							.setProfit(trafficSourceSummary.getProfit()
									+ (callModel.getBuyer_revenue()
											- callModel
													.getTraffic_source_revenue() - Double
												.parseDouble(callModel
														.getTotalCost())));
					trafficSourceSummary.setBuyerRevenue(trafficSourceSummary
							.getBuyerRevenue() + callModel.getBuyer_revenue());
					trafficSourceSummary.setBuyerConverted(trafficSourceSummary
							.getBuyerConverted() + 1);
				}

				if (callModel.getTraffic_source_revenue() > 0) {
					trafficSourceSummary.setTsUnique(trafficSourceSummary
							.getTsUnique() + 1);
					trafficSourceSummary.setTsConverted(trafficSourceSummary
							.getTsConverted() + 1);
					trafficSourceSummary.setTsPayout(trafficSourceSummary
							.getTsPayout()
							+ callModel.getTraffic_source_revenue());

					trafficSourceSummary.setTotalCost(trafficSourceSummary
							.getTotalCost()
							+ callModel.getTraffic_source_revenue());

				}

				String totalCost = callModel.getTotalCost();

				if (totalCost == null || totalCost.trim().length() == 0)
					totalCost = "0";
				trafficSourceSummary.setProviderCost(trafficSourceSummary
						.getProviderCost() + Double.parseDouble(totalCost));

			}

			else {

				trafficSourceSummary.setTotalCalls(trafficSourceSummary
						.getTotalCalls() + 1);

			}

		}

		return sourceReportModel;
	}

	public static TrafficSourceSummaryReportModel getTrafficSourceSummary(
			HttpServletRequest request) {

		String startdateInput = request.getParameter("startdate");
		String endDateInput = request.getParameter("endDate");

		java.util.Date startDate = null;
		java.util.Date endDate = null;

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));

		if (startdateInput != null && startdateInput.trim().length() > 0) {
			try {
				startDate = formatter.parse(startdateInput);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			startDate = new java.util.Date();
		}

		if (endDateInput != null && endDateInput.trim().length() > 0) {
			try {
				endDate = formatter.parse(endDateInput);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			endDate.setTime(endDate.getTime() + (86400 + 1) * 1000);
		}

		else {

			endDate = new java.util.Date();
			endDate.setTime(startDate.getTime() + (86400 + 1) * 1000);

		}

		startdateInput = formatter.format(startDate);
		endDateInput = formatter.format(endDate);

		String trafficSourceId = request.getParameter("sourceId");

		TrafficSourceSummaryReportModel tssrm = new TrafficSourceSummaryReportModel();
		TreeMap<String, TrafficSourceSummary> datewiseSummary = new TreeMap<String, TrafficSourceSummary>();
		tssrm.setSourceSummaryMap(datewiseSummary);

		List<CallModel> calls = ReportsDao.loadTrafficSourceCalls(
				startdateInput, endDateInput, trafficSourceId);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		TrafficSourceReportModel tsrm = new TrafficSourceReportModel();

		int totalCalls = 0;

		double provCost = 0;

		for (CallModel callModel : calls) {

			totalCalls++;

			tssrm.setTraffic_sourcename(callModel.getTraffic_source());
			Date callLandingtime = callModel.getCallLandingTimeOnServer();
			String date = dateFormat.format(callLandingtime);

			TrafficSourceSummary trafficSourceSummary = datewiseSummary
					.get(date);

			if (trafficSourceSummary == null) {
				trafficSourceSummary = tsrm.getTrafficSourceSummaryInstance();
				trafficSourceSummary.setTrafficSourceId(callModel
						.getTraffic_source_id());
				trafficSourceSummary.setTrafficSourceName(callModel
						.getTraffic_source());
				datewiseSummary.put(date, trafficSourceSummary);

			}

			trafficSourceSummary.getCalls().add(callModel);

			if (callModel.getBuyer_id() != 0) {
				trafficSourceSummary.setBuyerConnected(trafficSourceSummary
						.getBuyerConnected() + 1);
				trafficSourceSummary.setTotalCalls(trafficSourceSummary
						.getTotalCalls() + 1);

				String duration = callModel.getDuration();
				if (duration == null || duration.trim().length() == 0)
					duration = "0";
				trafficSourceSummary.setTotalCallTime(trafficSourceSummary
						.getTotalCallTime() + Double.parseDouble(duration));

				if (callModel.getBuyer_revenue() > 0) {
					// trafficSourceSummary.setBuyerConnected(trafficSourceSummary.getBuyerConnected()+1);
					trafficSourceSummary
							.setProfit(trafficSourceSummary.getProfit()
									+ (callModel.getBuyer_revenue()
											- callModel
													.getTraffic_source_revenue() - Double
												.parseDouble(callModel
														.getTotalCost())));
					trafficSourceSummary.setBuyerRevenue(trafficSourceSummary
							.getBuyerRevenue() + callModel.getBuyer_revenue());
					trafficSourceSummary.setBuyerConverted(trafficSourceSummary
							.getBuyerConverted() + 1);
				}

				if (callModel.getTraffic_source_revenue() > 0) {
					trafficSourceSummary.setTsUnique(trafficSourceSummary
							.getTsUnique() + 1);
					trafficSourceSummary.setTsConverted(trafficSourceSummary
							.getTsConverted() + 1);
					trafficSourceSummary.setTsPayout(trafficSourceSummary
							.getTsPayout()
							+ callModel.getTraffic_source_revenue());

					trafficSourceSummary.setTotalCost(trafficSourceSummary
							.getTotalCost()
							+ callModel.getTraffic_source_revenue());

				}

				String totalCost = callModel.getTotalCost();
				if (totalCost == null || totalCost.trim().length() == 0)
					totalCost = "0";

				trafficSourceSummary.setProviderCost(trafficSourceSummary
						.getProviderCost() + Double.parseDouble(totalCost));

			}

			else {

				trafficSourceSummary.setTotalCalls(trafficSourceSummary
						.getTotalCalls() + 1);

			}

		}

		tssrm.setTotalCalls(totalCalls);

		return tssrm;

	}
}
