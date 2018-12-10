<%@page import="java.util.ArrayList"%>
<%@page import="com.triyasoft.utils.DailyReportUtil"%>
<%@page import="com.triyasoft.ui.DashboardUIGenerator"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.triyasoft.model.CallModel"%>
<%@page import="com.triyasoft.daos.ReportsDao"%>
<%@page import="com.triyasoft.model.UserModel"%>
<%@page import="com.triyasoft.daos.BuyerDaoService"%>
<%@page import="com.triyasoft.model.Buyer"%>
<%@page import="com.triyasoft.model.TrafficSource"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.daos.TrafficSourceDaoService"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>



<html>
<head>
    <meta charset="utf-8" />
                    <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
            <link href="../js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.css" rel="stylesheet" type="text/css" />
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
    -webkit-text-size-adjust: 100%;
}

ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #468bc0; 
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
    z-index: 1;
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

div.filtering {
    border: 1px solid #999;
    margin-bottom: 5px;
    padding: 10px;
    background-color: #EEE;
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

.divTableCell {
border: 1px solid #999999;
	display: table-cell;
	padding: 3px 10px;
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




table {
    border-collapse: collapse;
    border-spacing: 0;
    width: 100%;
    border: 1px solid #ddd;
}

th, td {
    border: none;
    text-align: left;
    padding: 8px;
}

tr:nth-child(even){background-color: #f2f2f2}
</style>

        <script src="../js/jquery.jtable.min.js" type="text/javascript"></script>

        

    </head>
    <body>
    
      <%
    
   
      UserModel  user  = (UserModel)request.getSession().getAttribute("user");
      if(!ProjectUtils.checkSessionValidity(request)) 
     	 {
     	
     	   String baseURL = ProjectUtils.getBaseURL(request);
 		  response.sendRedirect(baseURL+"/login.jsp");
 		  return ;
     	 }
     
     
		String appContext = request.getContextPath();

      
    
    %>
    
      <ul>
  <li><a href="dashboard.jsp">Dashboard</a></li>
  
    <li><a href="calls.jsp">Calls</a></li>


 
  <li class="dropdown">
    <a href="numbers.jsp"" class="dropbtn">Numbers</a>
   
  </li>

    <li><a href="reports.jsp">Reports</a></li>



 <li class="dropdown" style="float:right">
    <a href="javascript:void(0)" class="dropbtn"><%out.print(user.getFirstname()+"  "+user.getLastname()); %> </a>
    <div class="dropdown-content">
      <a href="#">Profile</a>
      <a href="<%=appContext%>/LogoutServlet">Logout</a>
    </div>
  </li>
   

  
</ul>
   

  
    
    
   <div class="filtering">
      
          Start Date: <input type="text" id="datepicker1" name="startdate">
        End Date: <input type="text" id="datepicker2" name="enddate">
        
        
        <button type="submit" id="LoadRecordsButton" onclick="reloadReport();">Pull Report</button>
        <button type="submit" id="detailedcallreport" onclick="getDetailedCallReport();">Detailed Call Report(In xls)</button>
        
    
</div>
    
    
   

    <div id="StudentTableContainer">
</div>
<script type="text/javascript">


var startdate = "";
var endDate = "";


function getDetailedCallReport() {

	 startdate = $( "#datepicker1" ).val();
	 endDate = $( "#datepicker2" ).val();
    var url =   '/<%=appContext%>/reports?requestType=exportyesterdayReportForSource'+ '&startdate=' + startdate+ '&endDate=' + endDate;
    window.open(url);

	
}


function reloadReport() {
	
	startdate = $( "#datepicker1" ).val();
	 endDate = $( "#datepicker2" ).val();
	 window.location.href = "<%=appContext%>/source/reports.jsp?startdate="+startdate+"&endDate="+endDate;


}


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


 <div class="container" style="max-height: 100%; overflow: auto;"> 
<div class="divTable">
<div class="divTableBody">
<div class="divTableRow">
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">UUID</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">Call Source</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">Caller</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">Duration</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">Status</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee" >Created At</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee" >End Time</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee" >TS Converted</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">TS Revenue</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee" >Error Code</div>
<div class="divTableCell" style = "color: #626262;border: 1px solid #bfbfbf;font-weight: bold; background:#eeeeee">Error Description</div>
</div>


<%

List<CallModel> calls =   DailyReportUtil.getReportRawDataForTrafficSource(request);
int counter = 0 ;
for(int i = 0 ; i < calls.size(); i ++) {
	
	CallModel call = calls.get(i);
	counter++;
	
	String buyer  = "";
	if(call.getBuyer() == null)
		buyer = "No Buyer";
	else 
		buyer = call.getBuyer() +":" +call.getConnected_to();
	
	boolean tsConverted  = false;
	boolean buyerConverted = false;
	
	String tsConvertedStyle= "";
	String tsConvertedText = "";
	
	String buyerConvertedStyle= "";
	String buyerConvertedtext= "";

	
	if(call.getTraffic_source_revenue() > 0 ) {
		tsConverted = true;
		tsConvertedStyle= "style=\"background: #599734;border-color: #599734;color: #e5f6db;\"";
		tsConvertedText = "Converted";
	}
	
	if(call.getBuyer_revenue() > 0 ) {
		buyerConverted = true;
		buyerConvertedStyle = "style=\"background: #599734;border-color: #599734;color: #e5f6db;\"";
		buyerConvertedtext = "Converted";
	}
    
	
	String duration = "0";
	
	if(call.getDuration() !=null)
		duration = call.getDuration();
	
	String durationText = ProjectUtils.convertDurationToString(Double.parseDouble(duration));
	String durationStyle = "style=\"background: #599734; border-color: #599734;color: #e5f6db;\"";	
	
	if("0".equals(duration))
		durationStyle = "style=\"background: #ac2c2d;border-color: #ac2c2d;color: #fbe2e3;\"";

	
	
	String status = "";
	
    String callStatusAtHangup =  call.getCallStatusAtHangup();
    String statusStyle = "";
    String statusText = "";
    
    if("completed".equals(callStatusAtHangup)){
    	statusText = "Finished";
    	statusStyle = "style=\"background: #c9ecb3;border-color: #c9ecb3;color: #4f8d2a;\"";
    }
    if("no-answer".equals(callStatusAtHangup)) {
    	statusText = "No Answer";
    	statusStyle =  "style=\"background: #f9cbcb;border-color: #f9cbcb;color: #9e2727;\"";
    }
    if("cancel".equals(callStatusAtHangup)) {
    	statusText = "Caller Hung Up";
    	statusStyle = "style=\"background: #ffe28d;border-color: #ffe28d;color: #9d7d23;\"";
    }



	
	
	
	
	
	String phoneCost = "0";
	
	 if(call.getTotalCost() !=null)
		 phoneCost = call.getTotalCost();
	
		 String cssClassName = "";
	    if(counter%2 ==0)
	    	cssClassName = "divTableRowEven";
	    else 
	    	cssClassName = "divTableRowOdd";
	    
	    String errorCode = "";
	    String errorDesc = "";
	    
	    if(call.getError_code() !=null)
	    	errorCode = call.getError_code();
	    
	    if(call.getError_description() != null)
	    	errorDesc = call.getError_description();
	    
%>



<div class="<%=cssClassName %>">
<div class="divTableCell"><%=call.getUuid()%></div>
<div class="divTableCell"><%=call.getTraffic_source() +":"+ call.getNumber_called() %></div>
<div class="divTableCell"><%=call.getCaller_number() %></div>
<div class="divTableCell">
						<a href="<%=call.getRecording_url()%>" target="_blank"" > <span
							<%=durationStyle%>> <%=durationText%> mins
						</span>
						</a>
					</div>
<div class="divTableCell"  <%=statusStyle %>  ><%=statusText %></div>
<div class="divTableCell"><%=call.getDateInEST() %></div>
<div class="divTableCell"><%=call.getEndDateInEST() %></div>
<div class="divTableCell"  <%= tsConvertedStyle %> ><%= tsConvertedText %></div>
<div class="divTableCell" style="color: #599734;" >$<%=DashboardUIGenerator.convertDouble2DigitPrecison(call.getTraffic_source_revenue()) %></div>
<div class="divTableCell"><%= errorCode %></div>
<div class="divTableCell"><%= errorDesc %></div>
</div>

<% 
}
%>
</div>
</div>
</div>

    
</body>
</html>



