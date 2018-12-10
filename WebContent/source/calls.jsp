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
    
   
     UserModel  user  = (UserModel)request.getSession().getAttribute("user");
      if(!ProjectUtils.checkSessionValidity(request)) 
    	 {
    	
    	   String baseURL = ProjectUtils.getBaseURL(request);
		  response.sendRedirect(baseURL+"/login.jsp");
		  return ;
    	 }
     
     
		String appContext = request.getContextPath();

      
    
    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>

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

setTimeout(function(){
	   window.location.reload(1);
	}, 10000);
	
$(document).ready(function() 
	    { 
	$('#runningcalls').text($('input[name=runningCalls]').val());

    $("#myTable").tablesorter( {sortList: [[4,0]]} ); 

	        $("#myTable").tablesorter(); 
	    } 
	); 
	
</script>

</head>
<body>


   
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

<%





if(!CallRoutingServiceV2.isdataLoaded) {
	CallRoutingServiceV2.loadAlldata(false);
}

int[] traffic_source_ids = null ;
int[] buyer_ids = null ;

if(user.getUserroleid() == 2)
	traffic_source_ids = user.getAccount_id();


if(user.getUserroleid() == 3)
	buyer_ids = user.getAccount_id();



Map<String, CallModel> runningCallsMapByUUID =   CallRoutingServiceV2.runningCallsMapByUUID;

%>

<h2 style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:33px"> &nbsp;&nbsp; <span id="runningcalls">  </span>  Calls Running Now </h2>




<div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
      <th>Call Source</th>
      <th>Source Number</th>
       <th>Customer Number</th>
       <th>Duration</th>
    </tr>
  </thead> 
  
    
   <% 

  Set set = runningCallsMapByUUID.entrySet();
   int counter = 0;


Iterator iterator = set.iterator();
while(iterator.hasNext()) {
     Map.Entry me = (Map.Entry)iterator.next();
     CallModel callModel =  (CallModel) me.getValue();
     String callSourceName = callModel.getTraffic_source();
     String callSourceNumber = callModel.getNumber_called();
     String buyerName =  callModel.getBuyer();
     String buyerNumber = callModel.getConnected_to();
     String customerNumber =  callModel.getCaller_number();
     Date callLanding = callModel.getCallLandingTimeOnServer();
     Date currentDate = new Date();
     
      int callBuyer_id = callModel.getBuyer_id();
      
      if(buyer_ids !=null) {
    	  for(int i = 0 ; i < buyer_ids.length; i++)
    	  if(callBuyer_id != buyer_ids[i])
    		  continue;
      }
     
      int calTraffficSourceid = callModel.getTraffic_source_id();
      
      boolean showthiCall = false;
      
      if(traffic_source_ids != null) {
    	  for(int i = 0 ; i < traffic_source_ids.length; i++)
    	  if(calTraffficSourceid == traffic_source_ids[i]){
    		  showthiCall = true;
    		  break;
    	  }
      }
      
      if(!showthiCall)
    	  continue;
     
     int timeduration =  (int) ((currentDate.getTime()-callLanding.getTime())/1000);
    
    	 
     out.println("<tr>");
     out.println("<td>"+callSourceName+"</td>");
     out.println("<td>"+callSourceNumber+"</td>");
     out.println("<td>"+customerNumber+"</td>");
     out.println("<td>"+timeduration+"</td>");
     out.println(" </tr>");
   
     counter++;

}


%>
  
  </table>
</div>

  <input type="hidden" name="runningCalls" value="<%= counter%>">


</body>
</html>
