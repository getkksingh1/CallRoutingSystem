
 <%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="com.triyasoft.utils.CallRoutingService"%>
<%@page import="java.util.Map"%>
<%@page import="com.triyasoft.model.CallModel"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.daos.CallsDaoService"%>
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
    			<script src="js/jquery.contextMenu.js" type="text/javascript"></script>
            <link href="js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
            <link href="css/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
            
            
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		    <link href="css/tdstyles.css" rel="stylesheet" type="text/css">
		   
            
        
        <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.css" rel="stylesheet" type="text/css" />
<style>

  #map {
        height: 100%;
      }

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

        <script src="js/jquery.jtable.min.js" type="text/javascript"></script>

        <script >

	setTimeout(function(){
	   window.location.reload(1);
	}, 20000);
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


    <li><a href="reports.jsp">Reports</a></li>



<li class="dropdown" style="float:right;">
<a href="LogoutServlet">Logout</a>
</li>

<li class="dropdown" style="float:right;">
<a href="profile.jsp">Profile</a>
</li>
   

  
</ul>    


    
    
   
   
      <div id="map"></div>
    <script>
      var map;
      function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 4,
          center: new google.maps.LatLng(41.850033, -87.6500523),
          mapTypeId: 'roadmap'
        });
        
        
        <%
        
       

      
        Map<String, CallModel> runningCallsMapByUUID =   CallRoutingService.runningCallsMapByUUID;

        if(runningCallsMapByUUID != null) {
        Set set = runningCallsMapByUUID.entrySet();





        Iterator iterator = set.iterator();
     
        int counter = 0;
        String buyerId = request.getParameter("buyerid");
        String sourceId = request.getParameter("sourceid");
     //   boolean buyerCheck = (buyerId != null && buyerId.length()>0 && buyerId.equals(callModel.getBuyer_id()+"") );
     //   boolean sourceCheck =  (sourceId !=null && sourceId.length()>0 && sourceId.equals(callModel.getTraffic_source_id()+""));
		
        List<CallModel> calls = new ArrayList();

      
        while(iterator.hasNext()) {
        	
        	   Map.Entry me = (Map.Entry)iterator.next();
               CallModel callModel =  (CallModel) me.getValue();
        	
            if((buyerId != null && buyerId.length()>0 && buyerId.equals(callModel.getBuyer_id()+""))&&(sourceId !=null && sourceId.length()>0 && sourceId.equals(callModel.getTraffic_source_id()+"")))
           		calls.add(callModel);
            else if(buyerId != null && buyerId.length()>0 && buyerId.equals(callModel.getBuyer_id()+""))
        		calls.add(callModel);
            else if(sourceId !=null && sourceId.length()>0 && sourceId.equals(callModel.getTraffic_source_id()+""))
        		calls.add(callModel);
            else
        		calls.add(callModel);
  	
        }
        
        for(int i = 0 ; i < calls.size(); i++) {
        
          counter++;	
          CallModel callModel =  calls.get(i);
          
          if(callModel.getLatitue() !=null && callModel.getLongitude() != null )  {
        	  	Date callLanding = callModel.getCallLandingTimeOnServer();
        	     Date currentDate = new Date();
        	     int timeduration =  (int) ((currentDate.getTime()-callLanding.getTime())/1000);
        
        %>
        
        
        var marker<%=counter %> = new google.maps.Marker({
            map: map,
            position: new google.maps.LatLng(<%=callModel.getLatitue() %>, <%=callModel.getLongitude() %>),
            customInfo: "Marker <%=counter %>",
            icon:  'images/phone1.png'
        });
        
        
        var contentString<%=counter %> = '<div id="content">'+
        '<div id="siteNotice">'+
        '</div>'+
        '<h3 id="firstHeading" class="firstHeading"><b>Buyer: </b><%=callModel.getBuyer() %></h3>'+
        '<div id="bodyContent">'+
        '<p><b>Traffic Source: </b><%=callModel.getTraffic_source()%> <br/> ' +
        '<p><b>Custmer Number: </b><%=callModel.getCaller_number()%> <br/> ' +
        '<p><b>Customer City: </b><%=callModel.getCity()%> <br/> ' +
        '<p><b>Customer State: </b><%=callModel.getState()%> <br/> ' +
        '<p><b>Customer Country: </b><%=callModel.getCountry()%> <br/> ' +
        '<p><b>Customer Provider: </b><%=callModel.getPhoneProvider()%> <br/> ' +
        '<p><b>Call Duration: </b><%=timeduration%> (s)<br/> ' +


       
        '</div>'+
        '</div>';

    var infowindow<%=counter %> = new google.maps.InfoWindow({
      content: contentString<%=counter %>
    });
       
    
    marker<%=counter %>.addListener('click', function() {
        infowindow<%= counter %>.open(map, marker<%=counter %>);
    });
    
      <%
         
     
      
          }
          
        	}
 
        	
        }
                

        %>
        
      }
    </script>
   
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDP-wVGAlw4V7wdsJTNDqMTV20JqOiWWm8&callback=initMap">
    </script> 
    


</body></html>
  