<%@page import="com.triyasoft.utils.ProjectUtils"%>
<%@page import="com.triyasoft.model.UserModel"%>
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
    

<html>
<head>
    <meta charset="utf-8" />
                    <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
    			<script src="../js/jquery.contextMenu.js" type="text/javascript"></script>
            <link href="../js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
            <link href="../css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
            
            
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		    <link href="../css/tdstyles.css" rel="stylesheet" type="text/css">
		   
            
        
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.css" rel="stylesheet" type="text/css" />
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




</style>

        <script src="../js/jquery.jtable.min.js" type="text/javascript"></script>

        

    </head>
    <body>
    
        
    
    <div  id="trackdrive-summary-bar-containerid" class="trackdrive-summary-bar-container">
    
  
  
  
  
  
     </div>
    
    
    
  <script>    
    window.setInterval(function(){

    	  $.ajax({url: "<%=appContext%>/rolebaseddashboard?requestType=headersummary", success: function(result){
              $("#trackdrive-summary-bar-containerid").html(result);
          }});
    	  
    	  $.ajax({url: "<%=appContext%>/rolebaseddashboard?requestType=maincontainer", success: function(result){
              $("#trackdrive-metrics-indexid").html(result);
          }});
    	  
    
    }, 10000);
    
	$(document).ready(function(){
   
        $.ajax({url: "<%=appContext%>/rolebaseddashboard?requestType=headersummary", success: function(result){
            $("#trackdrive-summary-bar-containerid").html(result);
        }});
        
        $.ajax({url: "<%=appContext%>/rolebaseddashboard?requestType=maincontainer", success: function(result){
            $("#trackdrive-metrics-indexid").html(result);
        }});
	 });

        
</script>
    
   
    <ul>
  <li><a href="dashboard.jsp">Dashboard</a></li>
  
    <li><a href="calls.jsp">Calls</a></li>


 
  <li class="dropdown">
    <a href="numbers.jsp"" class="dropbtn">Numbers</a>
   
  </li>

    <li><a href="reports.jsp">Reports</a></li>



 <li class="dropdown" style="float:right">
    <a href="javascript:void(0)" class="dropbtn">
    <% out.print(user.getFirstname()+"  "+user.getLastname()); %> 
    
    </a>
    <div class="dropdown-content">
      <a href="#">Profile</a>
      <a href="<%=appContext%>/LogoutServlet">Logout</a>
    </div>
  </li>
   

  
</ul>
   
   
   
    
    
  
    
<div id="container">
<div class="wrapper">
<div class="container top-level">
<div  id="trackdrive-metrics-indexid" class="trackdrive-metrics-index" data-become="Trackdrive.Metrics.Index">





</div>

</div>
</div>
</div>


</body></html>
  