package com.triyasoft.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.triyasoft.daos.CallsDaoService;
import com.triyasoft.daos.ReportsDao;
import com.triyasoft.model.CallModel;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.DailyReportUtil;
import com.triyasoft.utils.JsonWrapper;
import com.triyasoft.utils.ProjectUtils;

@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String requestType = request.getParameter("requestType");

		// "18003031147";

		if ("showRoutingLogs".equals(requestType)) {

			String uuid = request.getParameter("uuid");
			String routingLogs = CallsDaoService.getRoutingLogForCall(uuid);
			response.getWriter().print(routingLogs);

		}

		if ("loadCalls".equals(requestType)) {

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

			Gson gson = new Gson();

			JsonWrapper wrapper = new JsonWrapper();
			wrapper.setResult("OK");

			List<CallModel> lst = ReportsDao.loadAllCall(buyerId, sourceId,
					startdate, endDate);
			wrapper.setTotalRecordCount(lst.size());

			Object[] objarr = lst.toArray();

			wrapper.setRecords(lst.toArray());

			String jsonInString = gson.toJson(wrapper);

			response.getWriter().print(jsonInString);

		}

		if ("exportToExcel".equals(requestType)) {

			String buyerId = request.getParameter("buyerId");
			String sourceId = request.getParameter("sourceId");
			String startdate = request.getParameter("startdate");
			String endDate = request.getParameter("endDate");

			if (startdate == null || startdate.length() == 0)
				startdate = ProjectUtils.getTodayStrInClientsTimezone(null);
			else
				startdate = ProjectUtils.getDateStrInClientsTimezone(startdate);

			if (endDate == null || endDate.length() == 0)
				endDate = ProjectUtils.getTomorrowStrInClientsTimezone(null);
			else
				endDate = ProjectUtils.getDateStrInClientsTimezone(endDate);

			List<CallModel> calls = ReportsDao.loadAllCall(buyerId, sourceId,
					startdate, endDate);

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Call Report");

			int rowNum = 0;
			System.out.println("Creating excel");

			Row headerRow = sheet.createRow(rowNum++);
			int colNum = 0;

			Cell headerCell = headerRow.createCell(colNum++);
			headerCell.setCellValue("UUID");

			Cell headerCell5 = headerRow.createCell(colNum++);
			headerCell5.setCellValue("Source ID");

			Cell headerCell4 = headerRow.createCell(colNum++);
			headerCell4.setCellValue("Source Name");

			Cell headerCell1 = headerRow.createCell(colNum++);
			headerCell1.setCellValue("Source Number");

			Cell headerCell7 = headerRow.createCell(colNum++);
			headerCell7.setCellValue("Buyer ID");

			Cell headerCell6 = headerRow.createCell(colNum++);
			headerCell6.setCellValue("Buyer Name");

			Cell headerCell2 = headerRow.createCell(colNum++);
			headerCell2.setCellValue("Buyer Number");

			Cell headerCell3 = headerRow.createCell(colNum++);
			headerCell3.setCellValue("Customer Number");

			Cell headerCell9 = headerRow.createCell(colNum++);
			headerCell9.setCellValue("Duration ");

			Cell headerCell10 = headerRow.createCell(colNum++);
			headerCell10.setCellValue("Call Start Timing");

			Cell headerCell11 = headerRow.createCell(colNum++);
			headerCell11.setCellValue("Call End Timing");

			for (CallModel call : calls) {
				Row row = sheet.createRow(rowNum++);
				colNum = 0;

				Cell cell = row.createCell(colNum++);
				cell.setCellValue(call.getUuid());

				Cell cell5 = row.createCell(colNum++);
				cell5.setCellValue(call.getTraffic_source_id());

				Cell cell4 = row.createCell(colNum++);
				cell4.setCellValue(call.getTraffic_source());

				Cell cell1 = row.createCell(colNum++);
				cell1.setCellValue(call.getNumber_called());

				Cell cell7 = row.createCell(colNum++);
				cell7.setCellValue(call.getBuyer_id());

				Cell cell6 = row.createCell(colNum++);
				cell6.setCellValue(call.getBuyer());

				Cell cell2 = row.createCell(colNum++);
				cell2.setCellValue(call.getConnected_to());

				Cell cell3 = row.createCell(colNum++);
				cell3.setCellValue(call.getCaller_number());

				Cell cell9 = row.createCell(colNum++);
				cell9.setCellValue(call.getDuration());

				Cell cell10 = row.createCell(colNum++);
				cell10.setCellValue(call.getDateInEST());

				Cell cell11 = row.createCell(colNum++);
				cell11.setCellValue(call.getEndDateInEST());

			}

			try {
				response.setHeader("Content-Disposition",
						"attachment; filename=report.xlsx");
				response.setContentType("application/vnd.ms-excel");

				OutputStream outputStream = response.getOutputStream();
				workbook.write(outputStream);
				outputStream.flush();
				workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		if ("exportyesterdayReport".equals(requestType)) {

			try {

				String startdateInput = request.getParameter("startdate");
				String endDateInput = request.getParameter("endDate");

				java.util.Date startDate = null;
				java.util.Date endDate = null;

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));

				if (startdateInput != null
						&& startdateInput.trim().length() > 0) {
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

				Workbook workbook = DailyReportUtil
						.loadyesterDayReportWorkbook(startdateInput,
								endDateInput);
				String reportNamePrefix = startdateInput.replace("-", "")
						+ endDateInput.replace("-", "");
				response.setHeader("Content-Disposition",
						"attachment; filename=report" + reportNamePrefix
								+ ".xlsx");
				response.setContentType("application/vnd.ms-excel");

				OutputStream outputStream = response.getOutputStream();
				workbook.write(outputStream);
				outputStream.flush();
				workbook.close();
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ("exportyesterdayReportForSource".equals(requestType)) {

			try {

				String startdateInput = request.getParameter("startdate");
				String endDateInput = request.getParameter("endDate");

				java.util.Date startDate = null;
				java.util.Date endDate = null;

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));

				if (startdateInput != null
						&& startdateInput.trim().length() > 0) {
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

				UserModel user = (UserModel) request.getSession().getAttribute(
						"user");
				int[] accountids = user.getAccount_id();

				Workbook workbook = DailyReportUtil
						.loadyesterDayReportWorkbookForSource(startdateInput,
								endDateInput, accountids);
				String reportNamePrefix = startdateInput.replace("-", "")
						+ endDateInput.replace("-", "");
				response.setHeader("Content-Disposition",
						"attachment; filename=report" + reportNamePrefix
								+ ".xlsx");
				response.setContentType("application/vnd.ms-excel");

				OutputStream outputStream = response.getOutputStream();
				workbook.write(outputStream);
				outputStream.flush();
				workbook.close();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private String convertToDBSpecificNextDate(String endDate) {

		return endDate + " 00:00:00";
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

		return startdate + " 00:00:00";
	}

	private String getTodaysDate() {

		java.util.Date today = new java.util.Date();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(today);

		return strDate + " 00:00:00";
	}

	public static void main(String[] args) {
		// yyyy-MM-dd
		String startdate = "06/19/2017";
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

		System.out.println(strDate + " 00:00:00");
	}

}
