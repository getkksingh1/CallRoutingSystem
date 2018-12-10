 <%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.plivo.helper.api.response.number.Number"%>
<%@page import="com.triyasoft.plivo.PlivoPhoneNumbers"%>
<%@page import="com.triyasoft.model.PhoneNumber"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.daos.PhoneNumberDaoService"%>
<%@page import="com.triyasoft.model.UserModel"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>
<%
    
    boolean checkIfSessionValid = ProjectUtils.checkSessionValidity(request);
    if(!checkIfSessionValid) {
		response.getWriter().println("<font color=red>Session has Expired.</font>");
		String baseURL = ProjectUtils.getBaseURL(request);
		
		response.sendRedirect(baseURL+"/login.jsp");
		return;
    }
    
    %>
<html>
<head>
    <meta charset="utf-8" />
                      <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
            <link href="js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
        <link href="css/jquery-ui.css" rel="stylesheet" type="text/css" />
             <script src="http://tablesorter.com/__jquery.tablesorter.min.js" type="text/javascript"></script>
                 			
        
        
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

        <script src="js/jquery.jtable.min.js" type="text/javascript"></script>
        
        <script >
	
$(document).ready(function() 
	    { 
	
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

    
    <div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
       <th>Number & Alias</th>
       <th>Area</th>
       <th>Type</th>
       <th>Voice Rate/min</th>
       <th>SMS Rate/msg</th>
       <th>Monthly Fee</th>
       <th>In Use</th>
       
    </tr>
  </thead> 
  
    
    <% 
    List<PhoneNumber> phoneNumbersInUse =   PhoneNumberDaoService.loadAllNumbers();
    Map<String, PhoneNumber> phoneNumbersInUseMap =  new HashMap <String, PhoneNumber> ();
    
    for(int i = 0 ; i < phoneNumbersInUse.size(); i ++) {
    	
    	phoneNumbersInUseMap.put(phoneNumbersInUse.get(i).getNumber(), phoneNumbersInUse.get(i));
    }
    
    List<Number>  plivoNumbers =   PlivoPhoneNumbers.loadAllPlivoNumbers();
    
    for(int i = 0 ; i < plivoNumbers.size(); i ++) {
    	
    	Number plivoNumber  = plivoNumbers.get(i);
    	String number = plivoNumber.number;
    	PhoneNumber systemNumber = phoneNumbersInUseMap.get(number);
    	String inUse  = "NO" ;
    	if(systemNumber != null)
    		inUse = "YES";
   
     
    out.println("<tr>");
    out.println("<td>"+plivoNumber.number+"</td>");
    out.println("<td>"+plivoNumber.region+"</td>");
    out.println("<td>"+plivoNumber.numberType+"</td>");
    out.println("<td>"+plivoNumber.voiceRate+"</td>");
    out.println("<td>"+plivoNumber.smsRate+"</td>");
    out.println("<td>"+plivoNumber.monthlyRentalRrate+"</td>");
    out.println("<td>"+inUse+"</td>");

    out.println(" </tr>");
     
    }


%>
 
 </table>
</div>
	
	</div>


    
</body>
</html>



