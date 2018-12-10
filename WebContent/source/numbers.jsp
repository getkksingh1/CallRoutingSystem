 <%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.triyasoft.daos.TrafficSourceDaoService"%>
<%@page import="com.triyasoft.model.TrafficSource"%>
<%@page import="com.triyasoft.daos.PhoneNumberDaoService"%>
<%@page import="com.triyasoft.model.PhoneNumber"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.model.UserModel"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>
 <%
    
   
     UserModel  user  = (UserModel)request.getSession().getAttribute("user");
   if(user != null) {
	     if(!ProjectUtils.checkSessionValidity(request)) 
    	 {
    	
    	   String baseURL = ProjectUtils.getBaseURL(request);
		  response.sendRedirect(baseURL+"/login.jsp");
		  return ;
    	 }
   }
   else {
  	   String baseURL = ProjectUtils.getBaseURL(request);
		  response.sendRedirect(baseURL+"/login.jsp");
		  return;

     }
   
     
   
	String appContext = request.getContextPath();

     
    	 
      
    
    %>
<html>
<head>
    <meta charset="utf-8" />
                      <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
            <link href="../js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
        <link href="../css/jquery-ui.css" rel="stylesheet" type="text/css" />
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

        <script src="../js/jquery.jtable.min.js" type="text/javascript"></script>

        

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
   
   

    
  <div style="overflow-x:auto;">
  <table id="myTable" class="tablesorter">
  <thead> 
  
    <tr style="font-weight: 800;font-size: 15px; background:rgb(67, 160, 141);color:white;line-height:20px">
  
      <th>Phone Number</th>
      <th>Traffic Source</th>
       <th>Traffic Source Cost/call</th>
       <th>Status</th>
    </tr>
 
    
   <% 

  
	List<PhoneNumber>	 lst = PhoneNumberDaoService.loadAllNumbers();
	List<TrafficSource> trafficSources = TrafficSourceDaoService.loadAllSources();
	Map<Integer, String> trafficSourceIdNamemap = new HashMap<Integer, String>();
	for(int i = 0 ; i < trafficSources.size(); i++) {
		
		trafficSourceIdNamemap.put(trafficSources.get(i).getId(), trafficSources.get(i).getFirst_name()+" "+trafficSources.get(i).getLast_name());
	}


   int[] accountids = user.getAccount_id();
     
   for(int i = 0 ; i < lst.size(); i ++) {
     
	 for(int j = 0 ; j < accountids.length; j++) {  
		 if(accountids[j]== lst.get(i).getTraffic_source_id()) {
     out.println("<tr>");
     out.println("<td>"+lst.get(i).getNumber()+"</td>");
     out.println("<td>"+trafficSourceIdNamemap.get(lst.get(i).getTraffic_source_id())+"</td>");
     out.println("<td>"+lst.get(i).getCostpercall()+"</td>");
     out.println("<td>"+lst.get(i).getIs_Active()+"</td>");
		 }
	 }
      
}


%>
  
  </table>
    
</div>
</body>
</html>



