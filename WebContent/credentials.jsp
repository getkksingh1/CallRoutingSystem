<%@page import="java.util.List"%>
<%@page import="com.triyasoft.daos.UsersDao"%>
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


String schemaName =  request.getParameter("client");
  if("CallTracker".equals(schemaName) || "PHARMACY".equals(schemaName))
	  schemaName= "gizmo";

List<UserModel>    users =  UsersDao.getAllUsersBySchema(schemaName.trim());

if(users == null || users.size()==0)
	return;


 %>
    <a href="javascript:void(0)" class="dropbtn"><%out.print(user.getFirstname()+"  "+user.getLastname()); %> </a>
    <div class="dropdown-content">
      <a href="#">Profile</a>
      <a href="LogoutServlet">Logout</a>
    </div>
  </li>
   

  
</ul>




<h2 style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:33px"> &nbsp;&nbsp; <span id="runningcalls">  </span>  Calls Running Now </h2>



 

<div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
       <th>User Name</th>
      <th>Password</th>
      <th>User Role</th>
    </tr>
  </thead> 
  
    
   <% 

   for(int i = 0 ;  i < users.size(); i++) {
   
	   int roleid= users.get(i).getUserroleid();
	   String role = "";
	   if(roleid == 1)
		   role="Admin";
	   if(roleid == 2)
		   role="Traffic Source";
	   if(roleid == 3)
		   role="Buyer";


	   
	
   out.println(" <tr>");

   
     out.println("<td>"+users.get(i).getEmailid()+"</td>");
     out.println("<td>"+users.get(i).getPassword()+"</td>");
     out.println("<td>"+role+"</td>");
  

     out.println("</tr>");
   }

%>
  
  </table>
</div>






</body>
</html>
