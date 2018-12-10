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


       
              <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>



<script>
 
 
 function showRoutingLogs(calluuid) {
 	
 	  $.ajax({
 		  	 
 		  	 url: "reports?requestType=showRoutingLogs&uuid="+calluuid, 
 			 success: function(result){
 	    	  $("#weightModal").modal('show');
 	    	  $("#routingtext").html(result.replace(/\n/g, "<br />"));
 		  
 		  
 	    }});
 	
 }
 
 
 </script>


<script >


function disconnectCall(uuid) {
	
	$.ajax({
	  	 
	  	 url: "plivoinboundcall?requestType=disconnectCall&callUUID="+uuid, 
		 success: function(result){
 		alert('Call Disconnected');
 		 window.location.reload(1);
	
   }});
}

setTimeout(function(){
	   window.location.reload(1);
	}, 20000);
	
$(document).ready(function() 
	    { 
	
	$('#runningcalls').text($('input[name=runningCalls]').val());
    $("#myTable").tablesorter( {sortList: [[6,0]]} ); 

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





if(!CallRoutingServiceV2.isdataLoaded) {
	CallRoutingServiceV2.loadAlldata(false);
}

String buyer_id = request.getParameter("buyer_id");
String traffic_source_id = request.getParameter("traffic_source_id");



Map<String, CallModel> runningCallsMapByUUID =   CallRoutingServiceV2.runningCallsMapByUUID;

%>


<h2 style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:33px"> &nbsp;&nbsp; <span id="runningcalls">  </span>  Calls Running Now </h2>



 <div id="weightModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="confirmationelementweight">Routing Explanation</h4>
                </div>
                <div class="modal-body">
                
                    <p id="routingtext"> </p>
                   
              
                     
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
               
                </div>
            </div>
        </div>
    </div>


<div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
       <th>Call UUID</th>
      <th>Call Source</th>
      <th>Source Number</th>
       <th>Buyer Name</th>
       <th>Buyer Number</th>
       <th>Customer Number</th>
       <th>Duration</th>
       <th>Call Disconnect</th>
       
    </tr>
  </thead> 
  
    
   <% 

  Set set = runningCallsMapByUUID.entrySet();
   
   int counter = 0;
   
   int totaltime = 0;

Iterator iterator = set.iterator();
while(iterator.hasNext()) {
     Map.Entry me = (Map.Entry)iterator.next();
     CallModel callModel =  (CallModel) me.getValue();
     String callSourceName = callModel.getTraffic_source();
     String callSourceNumber = callModel.getNumber_called();
     String buyerName =  callModel.getBuyer();
     
     if(buyerName == null)
    	 buyerName = "Ringing";
     
     
     
     String buyerNumber = callModel.getConnected_to();
     
     
     if(buyerNumber == null)
    	 buyerNumber = "Ringing";
    	 
     String customerNumber =  callModel.getCaller_number();
   
     Date callLanding = callModel.getCallLandingTimeOnServer();
     Date currentDate = new Date();
     int timeduration =  (int) ((currentDate.getTime()-callLanding.getTime())/1000);
     
      int callBuyer_id = callModel.getBuyer_id();
      
      if(buyer_id!=null && buyer_id.trim().length()>0) {
    	  if(callBuyer_id != Integer.parseInt(buyer_id))
    		  continue;
      }
     
      int calTraffficSourceid = callModel.getTraffic_source_id();
      
      
      if(traffic_source_id!=null && traffic_source_id.trim().length()>0) {
    	  if(calTraffficSourceid != Integer.parseInt(traffic_source_id))
    		  continue;
      }
      
     
   
     
     out.println("<tr>");
     out.println("<td> <a href=\"https://manage.plivo.com/logs/debug/call/"+callModel.getUuid()+"/\"  target=\"_blank\"  style=\"color: black;\" >"+callModel.getUuid().substring(15)+"</a>  </td>");
     out.println("<td>"+callSourceName+"</td>");
     out.println("<td>"+callSourceNumber+"</td>");
     out.println("<td   onclick=\"showRoutingLogs('"+callModel.getUuid()+"')\" >"+buyerName+"</td>");
     out.println("<td>"+buyerNumber+"</td>");
     out.println("<td>"+customerNumber+"</td>");
     out.println("<td>"+timeduration+"</td>");
     out.println("<td><img src=\"images/dc.jpg\" onclick=\"disconnectCall('"+callModel.getUuid()+"')\"></td>");

     out.println(" </tr>");
     counter++;
      
}


%>
  
  </table>
</div>



  <input type="hidden" name="runningCalls" value="<%= counter%>">

<h3 style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:33px"> <a href="callsmap.jsp"><span style="color:white;">Click for Map View</span></a> </h3>


</body>
</html>
