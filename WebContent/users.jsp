<%@page import="com.triyasoft.model.Buyer"%>
<%@page import="com.triyasoft.daos.BuyerDaoService"%>
<%@page import="com.triyasoft.model.TrafficSource"%>
<%@page import="com.triyasoft.daos.TrafficSourceDaoService"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.daos.UsersDao"%>
<%@page import="com.triyasoft.model.UserModel"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="com.triyasoft.model.CallModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.triyasoft.utils.CallRoutingService"%>
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
    color: #222;
    font-size: 14px;
    line-height: 1.42857143;
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
        
        
        <script>
// Get the modal
var modal = document.getElementById('id01');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
</script>


<style>
/* Full-width input fields */
input[type=text], input[type=password], select {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

/* Set a style for all buttons */
button {
    background-color: #8c7065;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none;
    cursor: pointer;
    width: 100%;
}

/* Extra styles for the cancel button */
.cancelbtn {
    padding: 14px 20px;
    background-color: #272424;
}

/* Float cancel and signup buttons and add an equal width */
.cancelbtn,.signupbtn {float:left;width:50%}

/* Add padding to container elements */
.container {
    padding: 16px;
}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
    background-color: #fefefe;
    margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
    position: absolute;
    right: 35px;
    top: 15px;
    color: #000;
    font-size: 40px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: red;
    cursor: pointer;
}

/* Clear floats */
.clearfix::after {
    content: "";
    clear: both;
    display: table;
}

/* Change styles for cancel button and signup button on extra small screens */
@media screen and (max-width: 300px) {
    .cancelbtn, .signupbtn {
       width: 100%;
    }
}

.btn {
  background: #779126;
  background-image: -webkit-linear-gradient(top, #779126, #779126);
  background-image: -moz-linear-gradient(top, #779126, #779126);
  background-image: -ms-linear-gradient(top, #779126, #779126);
  background-image: -o-linear-gradient(top, #779126, #779126);
  background-image: linear-gradient(to bottom, #779126, #779126);
  -webkit-border-radius: 28;
  -moz-border-radius: 28;
  border-radius: 28px;
  font-family: Arial;
  color: #ffffff;
  font-size: 14px;
  padding: 6px 14px 6px 14px;
  text-decoration: none;
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
    



 <li class="dropdown" style="float:right">
<%
     UserModel  user  = (UserModel)request.getSession().getAttribute("user");

if( user ==null || user.getUserroleid() != 1)
   {
	response.sendRedirect(ProjectUtils.getBaseURL(request)+"/login.jsp");
	return;
	}

      List<UserModel>   users =  UsersDao.getAllUsers();
      List<TrafficSource> trafficSources =   TrafficSourceDaoService.loadAllSources();
       List<Buyer> buyers =  BuyerDaoService.loadAllBuyers();


 %>
    <a href="javascript:void(0)" class="dropbtn"><%out.print(user.getFirstname()+"  "+user.getLastname()); %> </a>
    <div class="dropdown-content">
      <a href="#">Profile</a>
      <a href="LogoutServlet">Logout</a>
    </div>
  </li>
   

  
</ul>



<h2 style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:33px"> <span id="runningcalls">  </span> &nbsp;&nbsp; System Current Users </h2>


<div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr>
      <th>User ID</th>
      <th>First Name</th>
       <th>Last Name</th>
      	<th>Password</th>
       <th>User Role</th>
       <th>Source/Buyer Name</th>
       <th>Edit</th>
       <th>Delete</th>
       
       
    </tr>
  </thead> 
  
    
<%

   
   int counter = 0;

   int numberOfusers  = users.size();
   
   for(int i = 0 ;  i < numberOfusers; i++)
      {
     
      UserModel userModel = users.get(i);
      
     
     int roleid =  userModel.getUserroleid();
     String role = "";
     String accountName = "";
     if(roleid == 1)
    	 role = "Super User";
     
     if(roleid == 2) {
    	 role = "Traffic Source";
    	 int sepratorCounter = 0;
    	   int numberOfSources  = trafficSources.size();
    	   for(int j = 0; j < numberOfSources; j++) {
    		  int[] accountids =  userModel.getAccount_id();
    		   for(int k = 0 ; k < accountids.length; k++) {
    		   if(trafficSources.get(j).getId() == accountids[k]) {
    			   if(sepratorCounter !=0)
    	   				accountName = accountName+", ";
    			   accountName = accountName +   trafficSources.get(j).getFirst_name() + " " + trafficSources.get(j).getLast_name()+"    ";
    			   sepratorCounter++;
    		   }
    		  }
    	   }

     }
    
     if(roleid == 3) {
    	 role = "Buyer";
    	 int sepratorCounter = 0;
    	  int numberOfBuyers  = buyers.size();
   	      for(int j = 0; j < numberOfBuyers; j++) {
   		  int[] accountids =  userModel.getAccount_id();
		   for(int k = 0 ; k < accountids.length; k++) {
   		   if(buyers.get(j).getBuyer_id() == accountids[k]) {
   			   if(sepratorCounter !=0)
   				accountName = accountName+", ";
   			   
   			   accountName =  accountName+ buyers.get(j).getBuyer_name(); 
   			   sepratorCounter++;
   		   }
		 }
     }
   }
    
     
     
    	 
     out.println("<tr>");
     out.println("<td>"+userModel.getUserid()+"</td>");
     out.println("<td>"+userModel.getFirstname()+"</td>");
     out.println("<td>"+userModel.getLastname()+"</td>");
     out.println("<td>"+userModel.getPassword()+"</td>");
     out.println("<td>"+role+"</td>");
     out.println("<td>"+accountName+"</td>");
     out.println("<td><img src=\"images/edit.png\" onclick=\"edituser('"+userModel.getUserid()+"')\"></td>");
     out.println("<td><img src=\"images/delete.png\" onclick=\"deleteuser('"+userModel.getUserid()+"')\"></td>");

     out.println(" </tr>");
   
     counter++;
      
}


%>
  
  </table>
</div>

<script>
	function showRoutingLogs(calluuid) {

		$.ajax({

			url : "reports?requestType=showRoutingLogs&uuid=" + calluuid,
			success : function(result) {
				$("#weightModal").modal('show');
				$("#routingtext").html(result.replace(/\n/g, "<br />"));

			}
		});

	}

	function deleteuser(userid) {

		var confirmation = confirm("Are you sure you want to delete "
				+ userid);

		if (confirmation == true) {

			$
					.ajax({

						url : "user?requestType=deleteuser&email="
								+ userid,
						success : function(result) {

							alert(phonenumber + ' User '+email+' Deleted Successfully');

						}
					});
			location.reload();

		}
	}
</script>

<div id="addbuyeruser" style="float: left ">

				
   <button onclick="document.getElementById('id01').style.display='block'" style="width:auto;" class="btn" >Add Traffic Source User</button>  
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<div id="id01" class="modal">
  <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">×</span>
  <form class="modal-content animate" action="user?roleid=2&requestType=adduser" method="post">
    <div class="container">
      <label><b>Email(User Id)</b></label>
      <input type="text" placeholder="Enter Email" name="email" required>
      
       <label><b>First Name</b></label>
      <input type="text" placeholder="Enter First Name" name="fname" required>
      
       <label><b>Last Name</b></label>
      <input type="text" placeholder="Enter Last Name" name="lname" required>
      
    
      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="password" required>

  	  <label><b>Choose Traffic Source</b></label>
  	  
  	  
  	  <select multiple style="height: <%=trafficSources.size()*10 %>px;font-size: 15px;" name="account_id">
  
  	  

<%

int numberOfSources  = trafficSources.size();
for(int j = 0; j < numberOfSources; j++) {
		   String trafficSourceName =  trafficSources.get(j).getFirst_name() + " " + trafficSources.get(j).getLast_name();
		   out.println(" <option value=\""+trafficSources.get(j).getId()+"\">"+trafficSourceName+"</option>");

}
%>
 
</select>
    
      <div class="clearfix">
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
        <button type="submit" class="signupbtn">Add Traffic Source User</button>
      </div>
    </div>
  </form>
</div>

</div>

<div id="addbuyersourceuser" style="float: left ">

				
   <button onclick="document.getElementById('id02').style.display='block'" style="width:auto;" class="btn" >Add Buyer User</button>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<div id="id02" class="modal" >
  <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">×</span>
  <form class="modal-content animate" action="user?roleid=3&requestType=adduser"  method="post">
    <div class="container">
      <label><b>Email(User Id)</b></label>
      <input type="text" placeholder="Enter Email" name="email" required>
      
       <label><b>First Name</b></label>
      <input type="text" placeholder="Enter First Name" name="fname" required>
      
       <label><b>Last Name</b></label>
      <input type="text" placeholder="Enter Last Name" name="lname" required>
      
    
      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="password" required>

  	  <label><b>Choose Buyer</b></label>
<select style="height: 35px;font-size: 15px;" name="account_id">

<%

int numberOfBuyers  = buyers.size();
for(int j = 0; j < numberOfBuyers; j++) {
		   String buyerName =  buyers.get(j).getBuyer_name();
		   out.println(" <option value=\""+buyers.get(j).getBuyer_id()+"\">"+buyerName+"</option>");

}
%>
 
</select>
    
      <div class="clearfix">
        <button type="button" onclick="document.getElementById('id02').style.display='none'" class="cancelbtn">Cancel</button>
        <button type="submit" class="signupbtn">Add Buyer User</button>
      </div>
    </div>
  </form>
</div>

</div>


<div id="addsuperuser" style="float: left ">

  <button onclick="document.getElementById('id03').style.display='block'" style="width:auto;" class="btn">Add Super User</button>

<div id="id03" class="modal">
  <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">×</span>
  <form class="modal-content animate" action="user?roleid=1&requestType=adduser"  method="post">
    <div class="container">
      <label><b>Email(User Id)</b></label>
      <input type="text" placeholder="Enter Email" name="email" required>
      
       <label><b>First Name</b></label>
      <input type="text" placeholder="Enter First Name" name="fname" required>
      
       <label><b>Last Name</b></label>
      <input type="text" placeholder="Enter Last Name" name="lname" required>
      
    
      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="password" required>

    
      <div class="clearfix">
        <button type="button" onclick="document.getElementById('id03').style.display='none'" class="cancelbtn">Cancel</button>
        <button type="submit" class="signupbtn">Add Super User</button>
      </div>
    </div>
  </form>
</div>

</div>



</body>
</html>
