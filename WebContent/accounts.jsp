<%@page import="com.triyasoft.model.ClientSummary"%>
<%@page import="com.triyasoft.ui.DashboardUIGenerator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.triyasoft.daos.AllCustomerDao"%>
<%@page import="com.triyasoft.utils.CallRoutingServiceV2"%>
<%@page import="com.triyasoft.model.UserModel"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="com.triyasoft.model.CallModel"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
      <%
    
    boolean checkIfSessionValid = ProjectUtils.checkSessionValidity(request);
    if(!checkIfSessionValid) {
		response.getWriter().println("<font color=red>Session has Expired.</font>");
		String baseURL = ProjectUtils.getBaseURL(request);
		response.sendRedirect(baseURL+"/login.jsp");
		return;
    }
    
    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

<style>


body {
 	font-family: 'Roboto', sans-serif;   
 	 font-size: 14px;
    line-height: 1.42857143;
    color: #222;
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
</style>

<style>
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



             <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
              <script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
             <script src="http://tablesorter.com/__jquery.tablesorter.min.js" type="text/javascript"></script>
                 			
             
    			
            <link href="js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
            
        <link href="css/jquery-ui.css" rel="stylesheet" type="text/css" />

<script >

	
$(document).ready(function() 
	    { 
	
	$('#runningcalls').text($('input[name=runningCalls]').val());
    $("#myTable").tablesorter( {sortList: [[2,0]]} ); 

	        $("#myTable").tablesorter(); 
	    } 
	); 
	
</script>

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


 <li class="dropdown" style="float:right">
<%
 UserModel  user  = (UserModel)request.getSession().getAttribute("user");
if( user ==null || user.getUserroleid() != 1)
{
	response.sendRedirect(ProjectUtils.getBaseURL(request)+"/login.jsp");
	return;
	}

 %>
    <a href="javascript:void(0)" class="dropbtn"><%out.print(user.getFirstname()+"  "+user.getLastname()); %> </a>
    <div class="dropdown-content">
      <a href="#">Profile</a>
      <a href="LogoutServlet">Logout</a>
    </div>
  </li>
   

  
</ul>

<%

List<String> prohibitedBuyes =  new ArrayList<String>();
prohibitedBuyes.add("information_schema");
prohibitedBuyes.add("Users");
prohibitedBuyes.add("analytics");
prohibitedBuyes.add("calltracker");
prohibitedBuyes.add("gizmo1");
prohibitedBuyes.add("mysql");
prohibitedBuyes.add("pctuner");
prohibitedBuyes.add("pctunerbackup");
prohibitedBuyes.add("performance_schema");
prohibitedBuyes.add("proptiger");
prohibitedBuyes.add("searchrecords");
prohibitedBuyes.add("test");
prohibitedBuyes.add("wordpress");
prohibitedBuyes.add("PHARMACY");
prohibitedBuyes.add("InTech");


int totalYDayCalls = 0 ;
int totalTodayCalls = 0;
int totalLiveCalls = 0;
double totalYdayPhoneCost = 0.0;
double totalTodayPhoneCost = 0.0;
double totalPhoneCost = 0.0;





Map<String, ClientSummary> allClientSummary = AllCustomerDao.getAllClientsSummaryt(prohibitedBuyes);

%>


<h2 style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:33px"> &nbsp;&nbsp; <span id="runningcalls">  </span>  Accounts </h2>



<div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
       <th>Buyer</th>
              <th>Yesterday Calls</th>
              <th>Today Calls</th>
              <th>Running Calls</th>
              <th>Yesterday Phone Cost</th>
    		  <th>Today Phone Cost</th>
    		 <th>Total Call Cost($)</th>
    
    </tr>
  </thead> 
  
    
   <% 

  Set set = allClientSummary.entrySet();
   
  
Iterator iterator = set.iterator();
while(iterator.hasNext()) {
     Map.Entry me = (Map.Entry)iterator.next();
     ClientSummary clientSummary = (ClientSummary)  me.getValue();
     String buyer  = (String) me.getKey();
     if("gizmo".equals(buyer))
    	 buyer = "CallTracker";  
    
     totalYDayCalls = totalYDayCalls+ clientSummary.getYesterdayCalls();
     totalTodayCalls = totalTodayCalls + clientSummary.getTodayCalls();
     totalLiveCalls = totalLiveCalls + clientSummary.getRunningcalls();
     
     totalPhoneCost = totalPhoneCost+ clientSummary.getTotalCost();
     totalYdayPhoneCost = totalYdayPhoneCost + clientSummary.getYesterdayCost();
     totalTodayPhoneCost = totalTodayPhoneCost + clientSummary.getTodaysCost();
     
     
     String totalcost  = DashboardUIGenerator.convertDouble2DigitPrecison((Double) clientSummary.getTotalCost());
      out.println("<tr>");
      out.println("<td> <a href=\"credentials.jsp?client="+buyer+"\" target=\"_blank\">"+buyer+"</a></td>");
      out.println("<td>"+clientSummary.getYesterdayCalls()+"</td>");
      out.println("<td>"+clientSummary.getTodayCalls()+"</td>");
      int runningCalls = clientSummary.getRunningcalls();
      if(runningCalls>=4 && "PHARMACY".equals(buyer))
    	  runningCalls = runningCalls -2;
      out.println("<td>"+runningCalls+"</td>");
      out.println("<td>"+DashboardUIGenerator.convertDouble2DigitPrecison((Double) clientSummary.getYesterdayCost())+"</td>");
      out.println("<td>"+DashboardUIGenerator.convertDouble2DigitPrecison((Double) clientSummary.getTodaysCost())+"</td>");
      out.println("<td>"+DashboardUIGenerator.convertDouble2DigitPrecison((Double) clientSummary.getTotalCost())+"</td>");
     out.println(" </tr>");
      
}




%>


  
  </table>
  
   <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
       		<th>Total</th>
              <th><%= totalYDayCalls%></th>
              <th><%= totalTodayCalls %></th>
              <th><%= totalLiveCalls%></th>
              <th><%= DashboardUIGenerator.convertDouble2DigitPrecison((Double) totalYdayPhoneCost) %></th>
    		  <th><%= DashboardUIGenerator.convertDouble2DigitPrecison((Double) totalTodayPhoneCost)%></th>
    		 <th><%= DashboardUIGenerator.convertDouble2DigitPrecison((Double) totalPhoneCost)%></th>
    
    </tr>
  </thead> 
  </table>
  
</div>






</body>
</html>
