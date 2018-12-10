<%@page import="java.util.HashMap"%>
<%@page import="com.triyasoft.model.CallModel"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.ui.DashboardUIGenerator"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.triyasoft.model.TrafficSourceReportModel.TrafficSourceSummary"%>
<%@page import="java.util.Map"%>
<%@page import="com.triyasoft.utils.DailyReportUtil"%>
<%@page import="com.triyasoft.model.TrafficSourceReportModel"%>
<html>
<head>
    <meta charset="utf-8" />
                    <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
    			<script src="js/jquery.contextMenu.js" type="text/javascript"></script>
            <link href="js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
            <link href="css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
            <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.css" rel="stylesheet" type="text/css" />
    <script>			

    function reloadReport() {
    	var startdate = "";
    	var endDate = "";
    	startdate = $( "#datepicker1" ).val();
    	endDate = $( "#datepicker2" ).val();
    	window.location = "buyerssummary.jsp?startdate="+startdate+"&endDate="+endDate;
    	
    	 

    	}
    
    $( function() {
    	
    	<%
    	String startdate =  request.getParameter("startdate");
    	String endDate =  request.getParameter("endDate");

      	
    	if(startdate!=null)
    	out.println("$( \"#datepicker1\" ).val('"+startdate+"');");
    	
    	if(endDate != null)
    	out.println("$( \"#datepicker2\" ).val('"+endDate+"');");

    	
    	%>
    	
    } );    
    
$( function() {
    $( "#datepicker1" ).datepicker({
    	dateFormat: "yy-mm-dd"
    });
  } );
  
$( function() {
    $( "#datepicker2" ).datepicker({
    	dateFormat: "yy-mm-dd"
    });
  } );
</script> 
            
<style>
body {
    font-family: 'Roboto', sans-serif;
    color: #222;
}
body {
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 14px;
    line-height: 1.42857143;
    color: #333;
}
html {
    font-size: 10px;
}
html {
    font-family: sans-serif;
}

ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #7f2021;
}

li {
    float: left;
}

li a, .dropbtn {
    display: inline-block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
}

li a:hover, .dropdown:hover .dropbtn {
      background-color: white;
      color: #7f2021;
}

li.dropdown {
    display: inline-block;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1000;
}



.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    text-align: left;
}

.dropdown-content a:hover {background-color: #f1f1f1}

.dropdown:hover .dropdown-content {
    display: block;
}

.container {
    width: 100%;
    overflow-x: auto;
    white-space: nowrap;
}

.divTable{
	display: table;
	width: 2000px;
	margin-bottom: 20px;
	overflow: scroll;
	
}
.divTableRow {
	display: table-row;
}

.divTableRowOdd{
	display: table-row;
}

.divTableRowEven {
	display: table-row;
	background-color: #f9f9f9;
}


.divTableRow:hover{ background-color: rgba(0, 167, 255, 0.11); }
.divTableRowEven:hover{ background-color: rgba(0, 167, 255, 0.11); }
.divTableRowOdd:hover{ background-color: rgba(0, 167, 255, 0.11); }


.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
}
.divTableCell, .divTableHead {
	border: 1px solid #999999;
	display: table-cell;
	padding: 3px 10px;
}
.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
	font-weight: bold;
}
.divTableFoot {
	background-color: #EEE;
	display: table-footer-group;
	font-weight: bold;
}
.divTableBody {
	display: table-row-group;
}



div.filtering {
    border: 1px solid #999;
    margin-bottom: 5px;
    padding: 10px;
    background-color: #EEE;
}
</style>


        

    </head>
    <body>
    
    
    
   
    <ul>
  <li><a href="dashboard.jsp">Dashboard</a></li>
  
    <li><a href="live_calls.jsp">Calls</a></li>


<li class="dropdown">
    <a href="traffic_sources.jsp" class="dropbtn">Traffic Sources</a>
  
  </li>
 

  <li class="dropdown">
    <a href="buyers.jsp" class="dropbtn">Buyers</a>
  </li>
  
  <li class="dropdown">
    <a href="filters.jsp" class="dropbtn">Filters</a>
   
  </li>
  
  <li class="dropdown">
    <a href="numbers.jsp"" class="dropbtn">Numbers</a>
   
  </li>

     <li><a href="users.jsp">Users</a></li>


 <li class="dropdown">
    <a href="reports.jsp">Reports</a>
    
    <div class="dropdown-content">
	 <a href="trafficsourcereport.jsp">Traffic Sources Summary</a>
      <a href="sourcesummary.jsp">Traffic Source Summary(Date-wise)</a>
  
    </div>
  </li>


<li class="dropdown" style="float:right;">
<a href="LogoutServlet">Logout</a>
</li>

<li class="dropdown" style="float:right;">
<a href="profile.jsp">Profile</a>
</li>
   

  
</ul>    

<div  class="filtering">

 Start Date: <input type="text" id="datepicker1" name="startdate">
        End Date: <input type="text" id="datepicker2" name="enddate">
        
       <button type="submit" id="LoadRecordsButton" onclick="reloadReport();">Pull Report</button>
 
 </div>  
    
    <div class="container"> 
    
    <div class="divTable">
<div class="divTableBody">
<div class="divTableRow">
<div class="divTableCell" style="background-color:#eee; font-weight:700">Calls By Traffic Source</div>
<div class="divTableCell" style="background-color:#eee;font-weight:700">Total Calls</div>
<div class="divTableCell" style="background-color:#eee;font-weight:700">Calls %</div>
<div class="divTableCell" style="background-color:#edc245;color: white;font-weight:700">B Connected</div>
<div class="divTableCell" style="background-color:#4f8d2a ;border-color:#357310; color: white;font-weight:700 ">Avg Call Time</div>
<!-- 
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a;" >TS Repeats</div>

 -->
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700" >TS Duplicates</div>
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700"   >TS Unique</div>
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700" >TS Converted</div>
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700" >TS Converted %</div>
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700" >Buyer Converted</div>

<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700" >TS Payout</div>
<div class="divTableCell" style="background: #34949d;color: #d5f8fb;border-color: #29747a; font-weight:700" >TS PPC</div>
<div class="divTableCell" style="background-color:#4f8d2a ;border-color:#357310; color: white; font-weight:700">Provider Cost</div>
<div class="divTableCell" style="background-color:#4f8d2a ;border-color:#357310; color: white; font-weight:700">Provider CPC</div>
<div class="divTableCell" style="background: #366b98;color: #e4f2ff;border-color: #8bc0ed; font-weight:700" >Total Cost</div>
<div class="divTableCell" style="background: #366b98;color: #e4f2ff;border-color: #8bc0ed; font-weight:700">Avg Total CPC</div>
<div class="divTableCell" style="background: #366b98;color: #e4f2ff;border-color: #8bc0ed; font-weight:700">Profit</div>
</div>
<%

TrafficSourceReportModel trafficSourceReportModel = DailyReportUtil.getTrafficSourceReport(request);
int totalCalls = trafficSourceReportModel.getTotalCall();
Map<Integer, TrafficSourceSummary>  summary =  trafficSourceReportModel.getTrafficSourcesSummary();

Iterator it = summary.entrySet().iterator();
int counter= 0;
int totalCallsCount = 0;
int totalByerConnected = 0 ;
double totalCallDuration  = 0;
int totalTSDuplicate = 0;
int totalTSUnique = 0 ;
int totalTSConverted = 0 ;
int totalBuyerConverted = 0 ;
double totalTSPayout  = 0 ;
double totalProviderCost = 0 ;
double totalCost  = 0 ;
double totalBuyerRevenue  = 0 ;
double totalProfit  =  0 ;

while (it.hasNext()) {
    counter++;
	Map.Entry pair = (Map.Entry)it.next();
    TrafficSourceSummary sourceSummary = (TrafficSourceSummary)pair.getValue();
    System.out.println(sourceSummary.getBuyerConnected()+" sourceSummary.getBuyerConnected()");
    List<CallModel>  calls = sourceSummary.getCalls();
    HashMap<String, String> customersNumberMap =  new HashMap<String, String>();
    
    for(int i = 0 ; i < calls.size(); i++) {
    	customersNumberMap.put(calls.get(i).getCaller_number(), calls.get(i).getCaller_number());
    }
    
    totalCallsCount= totalCallsCount + sourceSummary.getTotalCalls();
    totalByerConnected = totalByerConnected + sourceSummary.getBuyerConnected();
    totalCallDuration = totalCallDuration+ sourceSummary.getTotalCallTime();
    totalTSDuplicate = totalTSDuplicate+  sourceSummary.getTotalCalls()-customersNumberMap.size();
    totalTSUnique = totalTSUnique + customersNumberMap.size() ;
    totalTSConverted =  totalTSConverted + sourceSummary.getTsConverted();
    totalBuyerConverted  = totalBuyerConverted + sourceSummary.getBuyerConverted();
    totalTSPayout = totalTSPayout + sourceSummary.getTsPayout();
    totalProviderCost = totalProviderCost + sourceSummary.getProviderCost();
    totalCost =  totalCost + sourceSummary.getTotalCost() ;
    totalBuyerRevenue = totalBuyerRevenue + sourceSummary.getBuyerRevenue();
    totalProfit  = totalProfit + sourceSummary.getProfit();
    
    //divTableRowOdd divTableRowEven
    String cssClassName = "";
    if(counter%2 ==0)
    	cssClassName = "divTableRowEven";
    else 
    	cssClassName = "divTableRowOdd";

%>

<div class="<%=cssClassName %>">
<div class="divTableCell"><%=sourceSummary.getTrafficSourceName() %></div>
<div class="divTableCell"><%=sourceSummary.getTotalCalls()%></div>
<div class="divTableCell"><%=DashboardUIGenerator.convertDouble2DigitPrecison((double)(sourceSummary.getTotalCalls()*100)/(double)totalCalls)%>%</div>
<div class="divTableCell"><%=sourceSummary.getBuyerConnected()%></div>
<div class="divTableCell"><%=ProjectUtils.convertDurationToString(sourceSummary.getTotalCallTime()/sourceSummary.getBuyerConnected())%> minutes</div>
<!-- 
<div class="divTableCell">&nbsp;</div>
 -->
<div class="divTableCell"><%=sourceSummary.getTotalCalls()-customersNumberMap.size()%></div>
<div class="divTableCell"><%=customersNumberMap.size()%></div>
<div class="divTableCell"><%=sourceSummary.getTsConverted()%></div>
<div class="divTableCell"><%=DashboardUIGenerator.convertDouble2DigitPrecison((double)(sourceSummary.getTsConverted()*100)/(double)customersNumberMap.size())%>%</div>
<div class="divTableCell"><%=sourceSummary.getBuyerConverted()%></div>
<div class="divTableCell">$<%=sourceSummary.getTsPayout()%></div>
<div class="divTableCell">$<%= DashboardUIGenerator.convertDouble2DigitPrecison(sourceSummary.getTsPayout()/sourceSummary.getBuyerConverted()) %></div>
<div class="divTableCell">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(sourceSummary.getProviderCost())%></div>
<div class="divTableCell">$<%= DashboardUIGenerator.convertDouble2DigitPrecison(sourceSummary.getProviderCost()/sourceSummary.getTotalCalls()) %></div>
<div class="divTableCell">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(sourceSummary.getTotalCost())%></div>
<div class="divTableCell">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(sourceSummary.getBuyerRevenue()/sourceSummary.getBuyerConverted())%></div>
<div class="divTableCell">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(sourceSummary.getProfit())%></div>
</div>


<% 
it.remove(); // avoids a ConcurrentModificationException
}

%>


<div class="divTableRow">

<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%= counter %>  Items</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%= totalCallsCount %></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee" ><%= totalByerConnected%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%= ProjectUtils.convertDurationToString(totalCallDuration/totalByerConnected) %> minutes</div>
<!-- totalTSDuplicate
<div class="divTableCell">&nbsp;</div>
 -->
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%= totalTSDuplicate%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%=totalTSUnique %></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%=totalTSConverted %></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%=DashboardUIGenerator.convertDouble2DigitPrecison(((double)totalTSConverted*100/(double)totalTSUnique)) %>%</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee"><%= totalBuyerConverted%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%=totalTSPayout%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%= DashboardUIGenerator.convertDouble2DigitPrecison(totalTSPayout/totalBuyerConverted) %></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(totalProviderCost)%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%= DashboardUIGenerator.convertDouble2DigitPrecison(totalProviderCost/totalCallsCount) %></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(totalCost)%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(totalBuyerRevenue/totalBuyerConverted)%></div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">$<%=DashboardUIGenerator.convertDouble2DigitPrecison(totalProfit)%></div>


</div>

</div>
</div>
    
</div>

</body></html>
  