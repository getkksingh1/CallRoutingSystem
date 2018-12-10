package com.triyasoft.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.text.StrSubstitutor;

import com.triyasoft.daos.TemplatesDao;
import com.triyasoft.model.DashboardDataModel;
import com.triyasoft.model.HeaderSummaryModel;
import com.triyasoft.model.DashboardDataModel.BuyerConversion;
import com.triyasoft.model.DashboardDataModel.BuyerRevenue;
import com.triyasoft.model.DashboardDataModel.BuyersSummary;
import com.triyasoft.model.DashboardDataModel.OnGoingCalls;
import com.triyasoft.model.DashboardDataModel.PhoneCost;
import com.triyasoft.model.DashboardDataModel.Profit;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary;
import com.triyasoft.model.DashboardDataModel.BuyersSummary.BuyerSummary;
import com.triyasoft.model.DashboardDataModel.TrafficSourcesSummary.TrafficSourceSummary;
import com.triyasoft.utils.ProjectUtils;

public class DashboardUIGenerator {
	
	public static boolean areTemplatesLoaded  = false ;
	
	public static Map<String, String> templatesMap = new  HashMap<String, String>();
	
	public static void main(String[] args) {
		DashboardUIGenerator dashboardUIGenerator = new DashboardUIGenerator();
		//System.out.println(dashboardUIGenerator.generateHeader(new Date()));
		DashboardDataModel dashboardDataModel = new DashboardDataModel();
		//System.out.println(dashboardUIGenerator.generateOnGoingCallsContent(dashboardDataModel.getOnGoingCalls()));
	//	System.out.println(dashboardUIGenerator.generateBuyerConvertedCallsContent(dashboardDataModel.getBuyerConversion()));
		//System.out.println(dashboardUIGenerator.generateBuyerRevenueContent(dashboardDataModel.getBuyerRevenue()));
		
//		System.out.println(dashboardUIGenerator.generatePhoneCostContent(dashboardDataModel.getPhoneCost()));

	//	System.out.println(dashboardUIGenerator.generateProfitContent(dashboardDataModel.getProfit()));
		System.out.println(dashboardUIGenerator.convertDouble1DigitPrecison(22.2234));



	}
	

	
	public String generateRoleBasedDashBoardPartialUI(DashboardDataModel dashboardDataModel) {
		StringBuffer buffer = new StringBuffer();

		String headerContent = generateHeader(dashboardDataModel.getLastUpdatedat());
		buffer.append(headerContent);
		
		String onGoingCallsContent =  generateRoleBasedOnGoingCallsContent(dashboardDataModel.getOnGoingCalls());
		buffer.append(onGoingCallsContent);

		
		String clearfix = "<div class=\"clearfix\"></div>";
		buffer.append(clearfix);

		
		String trafficSourcesContent = generateRoleBasedTrafficSourcesContent(dashboardDataModel.getTrafficSourcesSummary());
		buffer.append(trafficSourcesContent);

		
			
		String footerTemplate	=  getfooterTemplate();
		buffer.append(footerTemplate);
		
		return buffer.toString();
		
	}
	


	private String generateRoleBasedTrafficSourcesContent(
			TrafficSourcesSummary trafficSourcesSummary) {

		StringBuffer stringBuffer =  new StringBuffer();

		String trafficSourceHeader = getContainerTrafficSourceHeader() ;
		stringBuffer.append(trafficSourceHeader);
		
		String trafficSourceTableHeader = getContainerTrafficSourceTableHeader() ;
		stringBuffer.append(trafficSourceTableHeader);

		List<TrafficSourceSummary>  trafficsourceSummaries =   trafficSourcesSummary.getSourceSummaries();
		
		try {
			String trafficSourceSummaryTemplate  = templatesMap.get("rolebasedtrafficsourcesinglerowtemplate"); //  new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/trafficsourcesinglerowtemplate")));
		
			
			int counter = 0 ;
			for (TrafficSourceSummary trafficSourceSummary : trafficsourceSummaries) {
				
				Map<String, String> valuesMap = new HashMap<String, String>();
				valuesMap.put("TrafficSourceName",trafficSourceSummary.getTrafficSourceName());
				valuesMap.put("YdayCalls",trafficSourceSummary.getYdayCalls()+"" );
				valuesMap.put("TodayCalls",trafficSourceSummary.getTodayCalls()+"" );
				valuesMap.put("LiveCalls", trafficSourceSummary.getLiveCalls()+"");
				valuesMap.put("AvgCall", ProjectUtils.convertDurationToString(trafficSourceSummary.getAvgCall()));
				valuesMap.put("SourcePayout", "$"+convertDouble1DigitPrecison(trafficSourceSummary.getSourcePayout())+"");
				
				valuesMap.put("TrafficSourceId", trafficSourceSummary.getTrafficSourceId()+"");

				
				String cssClassName = "";
				
				if(counter%2 == 0)
					cssClassName = "even object-row";
				else
					cssClassName = "object-row odd";
				
				valuesMap.put("cssclass", cssClassName);
				
				counter++;
				
				java.util.Date today = new java.util.Date();

			 	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				 String todayStr= formatter.format(today);  
				 
				 

					
					long time = today.getTime();
					long yesterdayTime = time - (86400*1000);
					today.setTime(yesterdayTime);
					
					 String yesterdayStr= formatter.format(today);  
					
				 
				
				valuesMap.put("YdayReportURL","reports.jsp?startdate="+yesterdayStr+"&sourceId="+trafficSourceSummary.getTrafficSourceId()+"&endDate="+todayStr+"");
				
				valuesMap.put("TodayReportURL","reports.jsp?startdate="+todayStr+"&sourceId="+trafficSourceSummary.getTrafficSourceId());

				
				stringBuffer.append(replaceTemplate(trafficSourceSummaryTemplate,valuesMap));
				



				
				
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String trafficSourceTableFooter = getContainerTrafficSourceTableFooter( trafficSourcesSummary) ;
		stringBuffer.append(trafficSourceTableFooter);

		
		String trafficSourceFooter = getContainerTrafficSourceFooter() ;

		stringBuffer.append(trafficSourceFooter);

		
		return stringBuffer.toString();
		
	}



	public String generateDashBoardPartialUI(DashboardDataModel dashboardDataModel) {
		StringBuffer buffer = new StringBuffer();

		String headerContent = generateHeader(dashboardDataModel.getLastUpdatedat());
		buffer.append(headerContent);
		
		String onGoingCallsContent =  generateOnGoingCallsContent(dashboardDataModel.getOnGoingCalls());
		buffer.append(onGoingCallsContent);

		
		String buyerConvertedCallsContent = generateBuyerConvertedCallsContent(dashboardDataModel.getBuyerConversion());
		buffer.append(buyerConvertedCallsContent);

		
		String buyerRevenueContent = generateBuyerRevenueContent(dashboardDataModel.getBuyerRevenue());
		buffer.append(buyerRevenueContent);

		
		String phoneCostContent =  generatePhoneCostContent(dashboardDataModel.getPhoneCost());
		buffer.append(phoneCostContent);

		
		String profitContent = generateProfitContent(dashboardDataModel.getProfit());
		buffer.append(profitContent);

		
		String clearfix = "<div class=\"clearfix\"></div>";
		buffer.append(clearfix);

		
		String trafficSourcesContent = generateTrafficSourcesContent(dashboardDataModel.getTrafficSourcesSummary());
		buffer.append(trafficSourcesContent);

		
		String buyersSummaryContent  = generateBuyersSummaryContent(dashboardDataModel.getBuyersSummary());
		buffer.append(buyersSummaryContent);

		
		String footerTemplate	=  getfooterTemplate();
		buffer.append(footerTemplate);
		
		return buffer.toString();
		
	}

	
	

	public DashboardUIGenerator() {
	
		loadAllTemplates();
		
	}




	public String generateHeader(Date lastUpdatedate) {
		
		
		String header  = "" ;
		
		try {
			String template = templatesMap.get("headertemplate");  // new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/headertemplate")));
			
			
			 Map<String, String> valuesMap = new HashMap<String, String>();
			 
			  DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
			  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
			 
			  
			  String updatedDate  = formatter.format(lastUpdatedate);
			 
			 valuesMap.put("updatedDate", updatedDate+" EDT");

			  header =  replaceTemplate(template,valuesMap);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		
		return header;
	}

	private void loadAllTemplates() {
		
		if(!areTemplatesLoaded) {
		templatesMap = TemplatesDao.loadAllTemplates();
		areTemplatesLoaded = true;
		}
		
		
	}




	private String replaceTemplate(String templateString, Map<String, String> valuesMap) {
		
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
	
		return sub.replace(templateString);
	}


	public static String convertDouble2DigitPrecison(double number) {
		String numberStr = ""+number;
		int indexofDot =  numberStr.indexOf(".");
		
		if((indexofDot+3) >= numberStr.length())
			return numberStr;
		else
			return numberStr.substring(0, indexofDot+3);
		
	}
	
	
	public String convertDouble1DigitPrecison(double number) {
		String numberStr = ""+number;
		int indexofDot =  numberStr.indexOf(".");
		
		if((indexofDot+2) >= numberStr.length())
			return numberStr;
		else
			return numberStr.substring(0, indexofDot+2);
		
	}
	
	
	
	public String generateRoleBasedOnGoingCallsContent(OnGoingCalls onGoingCalls) {
		

		
		String onGoingCallsContent = "";
		
		try {
			
			String template  = templatesMap.get("rolebasedOnGoingCallTemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/OnGoingCallTemplate")));
			
			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("OngoingCalls", onGoingCalls.getOngoingCalls()+"");
					
			 onGoingCallsContent = replaceTemplate(template,valuesMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return onGoingCallsContent;
	
	}
	
	public String generateOnGoingCallsContent(OnGoingCalls onGoingCalls) {
		
		String onGoingCallsContent = "";
		
		try {
			
			String template  = templatesMap.get("OnGoingCallTemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/OnGoingCallTemplate")));
			
			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("OngoingCalls", onGoingCalls.getOngoingCalls()+"");
			valuesMap.put("ActiveCC", onGoingCalls.getActiveCC()+"");
			valuesMap.put("MaxActiveCC", onGoingCalls.getMaxActiveCC()+"");
			valuesMap.put("ActiveCCPercentage", Math.floor(onGoingCalls.getActiveCCPercentage())+"");
			valuesMap.put("PausedCC", onGoingCalls.getPausedCC()+"");
			valuesMap.put("MaxPausedCC", onGoingCalls.getMaxPausedCC()+"");
			valuesMap.put("PausedCCPercentage", Math.floor(onGoingCalls.getPausedCCPercentage())+"");

			

			
			 onGoingCallsContent = replaceTemplate(template,valuesMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return onGoingCallsContent;
	}




	public String generateBuyerConvertedCallsContent(BuyerConversion buyerConversion) {
		
		String buyerConvertedCallsContent = "";
		
		try {

			String template = templatesMap.get("BuyerConvertedToTalCallsTemplate"); // new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/BuyerConvertedToTalCallsTemplate")));
			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("BuyerConvertedToday", buyerConversion.getBuyerConvertedToday()+"");
			valuesMap.put("TotalCallsToday", buyerConversion.getTotalCallsToday()+"");
			valuesMap.put("BuyerConvetedPercentageToday", Math.floor(buyerConversion.getBuyerConvetedPercentageToday())+"");
			
			valuesMap.put("BuyerConvertedYday", buyerConversion.getBuyerConvertedYday()+"");
			valuesMap.put("TotaslCallsYday", buyerConversion.getTotaslCallsYday()+"");
			valuesMap.put("BuyerConvertedPercentageYday", Math.floor(buyerConversion.getBuyerConvertedPercentageYday())+"");
			
			valuesMap.put("BuyerConverted4Hrs", buyerConversion.getBuyerConverted4Hrs()+"");
			valuesMap.put("TotaslCalls4Hrs", buyerConversion.getTotaslCalls4Hrs()+"");
			valuesMap.put("BuyerConvertedPercentage4Hrs", Math.floor(buyerConversion.getBuyerConvertedPercentage4Hrs())+"");
			
			valuesMap.put("BuyerConverted1Hrs", buyerConversion.getBuyerConverted1Hrs()+"");
			valuesMap.put("TotaslCalls1Hrs", buyerConversion.getTotaslCalls1Hrs()+"");
			valuesMap.put("BuyerConvertedPercentage1Hrs", Math.floor(buyerConversion.getBuyerConvertedPercentage1Hrs())+"");
			
			if(buyerConversion.getBuyerConvetedPercentageToday() > 50)
			valuesMap.put("content_status_class", "content-success");
			else
			valuesMap.put("content_status_class", "content-danger");

			
			
			
			buyerConvertedCallsContent = replaceTemplate(template,valuesMap);


			
			
			
			

		} catch (Exception e) {

			e.printStackTrace();
		}


		return buyerConvertedCallsContent;
	}




	public String generateBuyerRevenueContent(BuyerRevenue buyerRevenue) {
		String buyerRevenueContent  = "";
		
		try {
			String template =  templatesMap.get("BuyerRevenueTemplate"); // new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/BuyerRevenueTemplate")));
			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("BuyerRevenueToday", "$"+convertDouble2DigitPrecison(buyerRevenue.getBuyerRevenueToday())+"");
			valuesMap.put("BuyerRevenueYday", "$"+convertDouble2DigitPrecison(buyerRevenue.getBuyerRevenueYday())+"");
			valuesMap.put("BuyerRevenue4Hrs", "$"+convertDouble2DigitPrecison(buyerRevenue.getBuyerRevenue4Hrs())+"");
			valuesMap.put("BuyerRevenue1Hrs", "$"+convertDouble2DigitPrecison(buyerRevenue.getBuyerRevenue1Hrs())+"");
			
			
			
			buyerRevenueContent = replaceTemplate(template,valuesMap);

		} catch (Exception e) {

			e.printStackTrace();
		}

		
		return buyerRevenueContent;
	}




	public String generatePhoneCostContent(PhoneCost phoneCost) {

		String phoneCostContent  = "";

		
		try {
			String template =  templatesMap.get("PhoneCostTemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/PhoneCostTemplate")));
			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("PhoneCostToday", "$"+convertDouble2DigitPrecison(phoneCost.getPhoneCostToday())+"");
			valuesMap.put("PhoneCostYday", "$"+convertDouble2DigitPrecison(phoneCost.getPhoneCostYday())+"");
			valuesMap.put("PhoneCost4Hrs", "$"+convertDouble2DigitPrecison(phoneCost.getPhoneCost4Hrs())+"");
			valuesMap.put("PhoneCost1Hrs", "$"+convertDouble2DigitPrecison(phoneCost.getPhoneCost1Hrs())+"");

		
			
			phoneCostContent = replaceTemplate(template,valuesMap);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return phoneCostContent;
	}




	public String generateProfitContent(Profit profit) {
		
		String profitContent  = "";
		
		try {
		String template  = templatesMap.get("ProfitTemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/ProfitTemplate")));
		Map<String, String> valuesMap = new HashMap<String, String>();
		
		valuesMap.put("ProfitToday", "$"+convertDouble2DigitPrecison(profit.getProfitToday())+"");
		valuesMap.put("ProfitYday", "$"+convertDouble2DigitPrecison(profit.getProfitYday())+"");
		valuesMap.put("Profit4Hrs", "$"+convertDouble2DigitPrecison(profit.getProfit4Hrs())+"");
		valuesMap.put("Profit1Hrs", "$"+convertDouble2DigitPrecison(profit.getProfit1Hrs())+"");
		
			if(profit.getProfitToday() > 0)
			valuesMap.put("content_status_class", "content-success");
			else
			valuesMap.put("content_status_class", "content-danger");



		
		profitContent = replaceTemplate(template,valuesMap);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return profitContent;
	}




	public String generateTrafficSourcesContent(TrafficSourcesSummary trafficSourcesSummary) {

		StringBuffer stringBuffer =  new StringBuffer();

		String trafficSourceHeader = getContainerTrafficSourceHeader() ;
		stringBuffer.append(trafficSourceHeader);
		
		String trafficSourceTableHeader = getContainerTrafficSourceTableHeader() ;
		stringBuffer.append(trafficSourceTableHeader);

		List<TrafficSourceSummary>  trafficsourceSummaries =   trafficSourcesSummary.getSourceSummaries();
		
		try {
			String trafficSourceSummaryTemplate  = templatesMap.get("trafficsourcesinglerowtemplate"); //  new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/trafficsourcesinglerowtemplate")));
		
			
			int counter = 0 ;
			for (TrafficSourceSummary trafficSourceSummary : trafficsourceSummaries) {
				
				Map<String, String> valuesMap = new HashMap<String, String>();
				valuesMap.put("TrafficSourceName",trafficSourceSummary.getTrafficSourceName());
				valuesMap.put("YdayCalls",trafficSourceSummary.getYdayCalls()+"" );
				valuesMap.put("TodayCalls",trafficSourceSummary.getTodayCalls()+"" );
				valuesMap.put("LiveCalls", trafficSourceSummary.getLiveCalls()+"");
				valuesMap.put("AvgCall", ProjectUtils.convertDurationToString(trafficSourceSummary.getAvgCall()));
				valuesMap.put("SourcePayout", "$"+convertDouble1DigitPrecison(trafficSourceSummary.getSourcePayout())+"");
				
				valuesMap.put("TrafficSourceId", trafficSourceSummary.getTrafficSourceId()+"");

				
				String cssClassName = "";
				
				if(counter%2 == 0)
					cssClassName = "even object-row";
				else
					cssClassName = "object-row odd";
				
				valuesMap.put("cssclass", cssClassName);
				
				counter++;
				
				java.util.Date today = new java.util.Date();

			 	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				 String todayStr= formatter.format(today);  
				 
				 

					
					long time = today.getTime();
					long yesterdayTime = time - (86400*1000);
					today.setTime(yesterdayTime);
					
					 String yesterdayStr= formatter.format(today);  
					
				 
				
				valuesMap.put("YdayReportURL","reports.jsp?startdate=%27"+yesterdayStr+"%27&sourceId="+trafficSourceSummary.getTrafficSourceId()+"&endDate=%27"+todayStr+"%27");
				
				valuesMap.put("TodayReportURL","reports.jsp?startdate=%27"+todayStr+"%27&sourceId="+trafficSourceSummary.getTrafficSourceId());

				
				stringBuffer.append(replaceTemplate(trafficSourceSummaryTemplate,valuesMap));
				



				
				
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String trafficSourceTableFooter = getContainerTrafficSourceTableFooter( trafficSourcesSummary) ;
		stringBuffer.append(trafficSourceTableFooter);

		
		String trafficSourceFooter = getContainerTrafficSourceFooter() ;

		stringBuffer.append(trafficSourceFooter);

		
		return stringBuffer.toString();
		
	}




	private String getContainerTrafficSourceTableFooter(TrafficSourcesSummary trafficSourcesSummary) {
		
		

		
		String trafficSourceSummaryFooterContent  = "";
		
		try {
		String template  = templatesMap.get("trafficsourcetablefooter"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/ProfitTemplate")));
		Map<String, String> valuesMap = new HashMap<String, String>();
		
		valuesMap.put("SourceCount", trafficSourcesSummary.getSourceCount()+"");
		valuesMap.put("TotalCallCountToday", trafficSourcesSummary.getTotalCallCountToday()+"");
		valuesMap.put("TotalCallCountYday", trafficSourcesSummary.getTotalCallCountYday()+"");
		valuesMap.put("TotalPayout", "$"+convertDouble1DigitPrecison(trafficSourcesSummary.getTotalPayout()));
		valuesMap.put("TotalLiveCalls", trafficSourcesSummary.getTotalLiveCalls()+"");
		valuesMap.put("OverAllavg", ProjectUtils.convertDurationToString(trafficSourcesSummary.getOverAllavg()));

 

		
		

		
		trafficSourceSummaryFooterContent = replaceTemplate(template,valuesMap);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return trafficSourceSummaryFooterContent;
	
		

	
	
	}




	private String getContainerTrafficSourceTableHeader() {
		// /Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/headertrafficsource
		String trafficSourceTableHeader  = "";
		try {
			trafficSourceTableHeader  =  templatesMap.get("trafficsourcetableheader");  // new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/trafficsourcetableheader")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trafficSourceTableHeader;
	}




	private String getContainerTrafficSourceFooter() {
		String trafficSourceFooter  = "";
		
		try {
			trafficSourceFooter  = templatesMap.get("footertrafficsource"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/footertrafficsource")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trafficSourceFooter;
	}




	private String getContainerTrafficSourceHeader() {
		// /Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/headertrafficsource
		String trafficSourceHeader  = "";
		try {
			trafficSourceHeader  = templatesMap.get("headertrafficsource");  //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/trafficsources/headertrafficsource")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trafficSourceHeader;
	}




	public String generateBuyersSummaryContent(BuyersSummary buyersSummary) {
		


		StringBuffer stringBuffer =  new StringBuffer();

		String buyerSummaryHeader = getBuyersSummaryHeader() ;
		stringBuffer.append(buyerSummaryHeader);
		
		String callingBuyerTemplatee = templatesMap.get("callingbuyertemplate");
		stringBuffer.append(callingBuyerTemplatee);
		
		String nobuyerContent = getNobyerContent(buyersSummary);
		
		stringBuffer.append(nobuyerContent);
		
		 List<BuyerSummary>  buyerSummaries =   buyersSummary.getBuyersSummary();
		 
		
		try {
			String buyerSummarySingleRowTemplate  =  templatesMap.get("BuyerSummarySingleRow"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/buyerssummary/BuyerSummarySingleRow")));
		
			
			int counter = 0 ;
			for (BuyerSummary buyerSummary : buyerSummaries) {
				
				Map<String, String> valuesMap = new HashMap<String, String>();
				valuesMap.put("BuyerName", buyerSummary.getBuyerName());
				valuesMap.put("YdayCallstoBuyer", buyerSummary.getYdayCallstoBuyer()+"");
				valuesMap.put("TodayCallstoBuyer", buyerSummary.getTodayCallstoBuyer()+"");
				valuesMap.put("LiveCallsToBuyer", buyerSummary.getLiveCallsToBuyer()+"");
				
				int concurrencyCap =  buyerSummary.getBuyerConcurrencyCap();
				
				if(concurrencyCap == -1)
					valuesMap.put("BuyerConcurrencyCap", "&#x221e;");
				else	
				valuesMap.put("BuyerConcurrencyCap", buyerSummary.getBuyerConcurrencyCap()+"");
				
				
				java.util.Date today = new java.util.Date();
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				 String todayStr= formatter.format(today);  
				 
				 

					
					long time = today.getTime();
					long yesterdayTime = time - (86400*1000);
					today.setTime(yesterdayTime);
					
					 String yesterdayStr= formatter.format(today);  
					
					
					
				
				
				
				
				 
				
				valuesMap.put("YdayReportURL","reports.jsp?startdate=%27"+yesterdayStr+"%27&buyerId="+buyerSummary.getBuyerId()+"&endDate=%27"+todayStr+"%27");
				
				valuesMap.put("TodayReportURL","reports.jsp?startdate=%27"+todayStr+"%27&buyerId="+buyerSummary.getBuyerId());

				
				
				valuesMap.put("BuyerTier", buyerSummary.getBuyerTier()+"");
				
				
				valuesMap.put("BuyerWeitage", buyerSummary.getBuyer_weitage()+"");

				
				valuesMap.put("BuyerId", buyerSummary.getBuyerId()+"");
				
				valuesMap.put("AvgCalltime", ProjectUtils.convertDurationToString(buyerSummary.getAvgCalltime()));
				valuesMap.put("BuyerRevenue", "$"+convertDouble1DigitPrecison(buyerSummary.getBuyerRevenue())+"");
				String pausedtemplateclass = "";
				
				if(buyerSummary.getIsBuyer_active() == 0)
					pausedtemplateclass = "fa fa-pause ";
				
				valuesMap.put("pausedtemplateclass", pausedtemplateclass);
				
				
				String concurrencyFullClass = "";
				if((buyerSummary.getLiveCallsToBuyer() >= buyerSummary.getBuyerConcurrencyCap()) && (buyerSummary.getBuyerConcurrencyCap() != -1))
					concurrencyFullClass = "text-danger";
				
				String buyer_ban_notification = "";
				if((buyerSummary.getTodayCallstoBuyer() >= buyerSummary.getBuyerDailyCap()) && (buyerSummary.getBuyerDailyCap() !=-1))
					buyer_ban_notification = "<i class=\"fa fa-ban \"></i>";
				
				valuesMap.put("concurrency_full_class", concurrencyFullClass);
				valuesMap.put("buyer_ban_notification", buyer_ban_notification);
				
				


				
				valuesMap.put("BuyerCurrentCap", buyerSummary.getBuyerCurrentCap()+"");
				
				int buyerDailyCap = buyerSummary.getBuyerDailyCap();
				if(buyerDailyCap == -1)
					valuesMap.put("BuyerDailyCap", "&#x221e;");
				else
					valuesMap.put("BuyerDailyCap", buyerSummary.getBuyerDailyCap()+"");
				
				
				
				String cssClassName = "";
				
				if(counter%2 == 0)
					cssClassName = "even object-row";
				else
					cssClassName = "object-row odd";
				
				valuesMap.put("cssclass", cssClassName);
				
				counter++;
				
				stringBuffer.append(replaceTemplate(buyerSummarySingleRowTemplate,valuesMap));



				
				
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String buyerSummaryFooter = getBuyersSummaryFooter(buyersSummary) ;
		stringBuffer.append(buyerSummaryFooter);

		
		

		
		return stringBuffer.toString();
		
	
		
	}




	private String getNobyerContent(BuyersSummary buyersSummary) {
		



		String noBuyerContent  = "";
		try {

			String noBuyerTemplate =  templatesMap.get("nobuyertemplate");
			
			BuyerSummary noBuyerSummary = buyersSummary.getNoBuyer();

			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("TodayCallstoBuyer", noBuyerSummary.getTodayCallstoBuyer()+"");
			valuesMap.put("YdayCallstoBuyer", noBuyerSummary.getYdayCallstoBuyer()+"");
			valuesMap.put("LiveCallsToBuyer", noBuyerSummary.getLiveCallsToBuyer()+"");
			
			
			java.util.Date today = new java.util.Date();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			 String todayStr= formatter.format(today);  
			 
			 

				
				long time = today.getTime();
				long yesterdayTime = time - (86400*1000);
				today.setTime(yesterdayTime);
				
				 String yesterdayStr= formatter.format(today);  
				
				
				
			int buyer_id = 0;
			
			
			
			 
			
			valuesMap.put("YdayReportURL","reports.jsp?startdate=%27"+yesterdayStr+"%27&buyerId="+buyer_id+"&endDate=%27"+todayStr+"%27");
			
			valuesMap.put("TodayReportURL","reports.jsp?startdate=%27"+todayStr+"%27&buyerId="+buyer_id);

			
	
			noBuyerContent = replaceTemplate(noBuyerTemplate,valuesMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return noBuyerContent;
	
	}




	private String getBuyersSummaryFooter(BuyersSummary buyersSummary) {

		String buyersSummaryFooter  = "";
		try {
			String buyersSummaryFooterTemplate  =  templatesMap.get("buyersummaryfootertemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/buyerssummary/buyersummaryfootertemplate")));
			Map<String, String> valuesMap = new HashMap<String, String>();
			
			valuesMap.put("TotalBuyers", buyersSummary.getTotalBuyers()+"");
			valuesMap.put("TotalCallsToBuyersToday", buyersSummary.getTotalCallsToBuyersToday()+"");
			valuesMap.put("CurrentLiveCallsToBuyers", buyersSummary.getCurrentLiveCallsToBuyers()+"");
			valuesMap.put("TotalCallsToBuyersYday", buyersSummary.getTotalCallsToBuyersYday()+"");
			valuesMap.put("TotalBuyerRevenue", "$"+convertDouble1DigitPrecison(buyersSummary.getTotalBuyerRevenue()));
			valuesMap.put("AvgHandlingTime", buyersSummary.getAvgHandlingTime());




			  
			

			
			buyersSummaryFooter = replaceTemplate(buyersSummaryFooterTemplate,valuesMap);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return buyersSummaryFooter;
	}




	private String getBuyersSummaryHeader() {

		String buyersSummaryHeader  = "";
		try {
			buyersSummaryHeader  = templatesMap.get("buyersummaryheadertemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/buyerssummary/buyersummaryheadertemplate")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return buyersSummaryHeader;
	}




	public String getfooterTemplate() {
		


		String footer  = "";
		try {
			footer  =  templatesMap.get("FooterTemplate"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/FooterTemplate")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return footer;
	
		

		
	}




	public String generateHeaderStripRoleBased(HeaderSummaryModel hsm) {
		

		
		
		

		
		String headerStripContent  = "";
		
		try {
		String template  = templatesMap.get("rolebaseddashboardpageheaderblackstrip"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/ProfitTemplate")));
		Map<String, String> valuesMap = new HashMap<String, String>();
		
		valuesMap.put("ActiveCalls", hsm.getActiveCalls()+"");
		headerStripContent = replaceTemplate(template,valuesMap);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return headerStripContent;
	

		
	
		
	}
	

	public String generateHeaderStrip(HeaderSummaryModel hsm) {
		
		
		

		
		String headerStripContent  = "";
		
		try {
		String template  = templatesMap.get("dashboardpageheaderblackstrip"); //new String(Files.readAllBytes(Paths.get("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/templates/ProfitTemplate")));
		Map<String, String> valuesMap = new HashMap<String, String>();
		
		valuesMap.put("ActiveCalls", hsm.getActiveCalls()+"");
		valuesMap.put("ActiveCCcurrent", hsm.getActiveCCcurrent()+"");
		valuesMap.put("ActiveCCmax", hsm.getActiveCCmax()+"");
		valuesMap.put("ConvertedCurrentCalls", hsm.getConvertedCurrentCalls()+"");
		valuesMap.put("ConvertedMaxCalls", hsm.getConvertedMaxCalls()+"");
		valuesMap.put("Revenue", "$"+convertDouble1DigitPrecison(hsm.getRevenue())+"");
		valuesMap.put("Providerbalance", "$"+hsm.getProviderbalance()+"");
		
		
		if(hsm.getProviderbalance()< 250 && hsm.getProviderbalance() > 100)
			valuesMap.put("summary-additional-class", "summary-warning");
		else if(hsm.getProviderbalance()< 20)
			valuesMap.put("summary-additional-class", "summary-danger");
		else
			valuesMap.put("summary-additional-class", "");
	

		double conversionPercent = ((double)hsm.getConvertedCurrentCalls())/((double)hsm.getConvertedMaxCalls())*100 ;
		
		if(conversionPercent > 50)
			valuesMap.put("conversion-success-class", "summary-success");
		else
			valuesMap.put("conversion-success-class", "");



		
		headerStripContent = replaceTemplate(template,valuesMap);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return headerStripContent;
	

		
	}





	
	

}
