
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

        

    </head>
    <body>
    
        
    
    <div  id="trackdrive-summary-bar-containerid" class="trackdrive-summary-bar-container">
    
  
  
  
  
  
     </div>
    
    
    
    <script>
    
    var selectedBuyerforConcurrencyChange = "-1";
    var selectedBuyerforTierChange = "-1";
    var selectedBuyerforWeightChange = "-1";
    var selectedBuyerforStateChange = "-1";
    var selectedBuyerfordailycap = "-1";


    
    function editBuyerDailycap(buyerid) {
    	
    	  $.ajax({
    		  	 
    		  	 url: "buyers?requestType=loadbyerbyBuyerId&buyerId="+buyerid, 
    		 	 dataType: 'json',
    			 success: function(result){
    	       
    		//  alert(result.buyer_name);
    		  $("#olddailycap").val(result.buyer_daily_cap);
    		  $("#confirmationelementdailycap").text("Change Daily Cap of Buyer: "+result.buyer_name);
    		  $("#newweight").val('');
    		  $("#dailycapModal").modal('show');
    		  selectedBuyerfordailycap = buyerid;
    		  
    	    }});
    	
    }
    
    
    function editBuyerWeight(buyerid) {
    	
  	  $.ajax({
  		  	 
  		  	 url: "buyers?requestType=loadbyerbyBuyerId&buyerId="+buyerid, 
  		 	 dataType: 'json',
  			 success: function(result){
  	       
  		//  alert(result.buyer_name);
  		  $("#oldweight").val(result.weight);
  		  $("#confirmationelementweight").text("Change Weight of Buyer: "+result.buyer_name);
  		  $("#newweight").val('');
  		  $("#weightModal").modal('show');
  			selectedBuyerforWeightChange = buyerid;
  		  
  	    }});
  	
  }
    

    function editBuyerConcurrency(buyerid) {
    	
    	  $.ajax({
    		  	 
    		  	 url: "buyers?requestType=loadbyerbyBuyerId&buyerId="+buyerid, 
    		 	 dataType: 'json',
    			 success: function(result){
    	       
    		//  alert(result.buyer_name);
    		  $("#oldconcurrency").val(result.concurrency_cap_limit);
    		  $("#confirmationelementconcurrency").text("Change Concurrency of Buyer: "+result.buyer_name);
    		  $("#newconcurrency").val('');
    		  $("#concurrencyModal").modal('show');
    		  selectedBuyerforConcurrencyChange = buyerid;
    		  
    	    }});
    	
    }
    
    function editBuyerTier(buyerid) {
    	
  	  $.ajax({
  		  	 
  		  	 url: "buyers?requestType=loadbyerbyBuyerId&buyerId="+buyerid, 
  		 	 dataType: 'json',
  			 success: function(result){
  	       
  		//  alert(result.buyer_name);
  		  $("#oldtier").val(result.tier);
  		  $("#confirmationelementtier").text("Change Tier of Buyer: "+result.buyer_name);
  		  $("#newtier").val('');
  		  $("#tierModal").modal('show');
  		  selectedBuyerforTierChange = buyerid;
  		  
  	    }});
  	
  }
    
    function changeBuyerActiveState(buyerid) {
    	
    	  $.ajax({
    		  	 
    		  	 url: "buyers?requestType=loadbyerbyBuyerId&buyerId="+buyerid, 
    		 	 dataType: 'json',
    			 success: function(result){
    	    
    				 
    		  $("#state").val(result.is_active);
    		  $("#buyername").val(result.buyer_name);
    		  $("#buyerNumber").val(result.buyer_number);
    		  $("#tier").val(result.tier);
    		  $("#weight").val(result.weight);
    		  $("#concurrency").val(result.concurrency_cap_limit);
    		  $("#dailycap").val(result.buyer_daily_cap);
    		  $("#bidprice").val(result.bid_price);
    		  $("#ring_timeout").val(result.ring_timeout);
    		  $("#confirmationelementstate").text("Edit Buyer: "+result.buyer_name);	  
    		  $("#stateModal").modal('show');
    		  selectedBuyerforStateChange = buyerid;
    		  
    	    }});
    	
    } 

   
    window.setInterval(function(){
    
	  
	  $.ajax({url: "dashboard?requestType=maincontainer", success: function(result){
        $("#trackdrive-metrics-indexid").html(result);
   	
	  	}});
	  

    }, 9000);
    
    window.setInterval(function(){

    	  $.ajax({url: "dashboard?requestType=headersummary", success: function(result){
              $("#trackdrive-summary-bar-containerid").html(result);
          }})
    	  
    
    }, 11000);
    
	$(document).ready(function(){
   
        $.ajax({url: "dashboard?requestType=headersummary", success: function(result){
            $("#trackdrive-summary-bar-containerid").html(result);
        }});
        
        $.ajax({url: "dashboard?requestType=maincontainer", success: function(result){
            $("#trackdrive-metrics-indexid").html(result);
        }});
        
        
	$("#savebuyerconcurrency").click(function(){
    		
    		var concurrency  = $("#newconcurrency").val();
    		
    		if(!isInt(concurrency)) {
    			alert("Please enter a Integer Value in Concurrency Cap Field");
    			return;
    		}
    		
    		  $.ajax({
    			  	 
    			  	 url: "buyers?requestType=updateBuyerConcurrency&buyerId="+selectedBuyerforConcurrencyChange+"&concurrency="+concurrency, 
    			 	 dataType: 'json',
    				 success: function(result){
    		       
    		    		  $("#concurrencyModal").modal('hide');

    			//  alert(result.buyer_name);
    			 // $("#oldconcurrency").val(result.concurrency_cap_limit);
    			//  $("#confirmationelement").text("Change Concurrency of Buyer: "+result.buyer_name);
    			 // $("#myModal").modal('show');
    		    }});

    		
    		
    	});
	
	$("#savebuyertier").click(function(){
		
		var tier  = $("#newtier").val();
		
		if(!isInt(tier)) {
			alert("Please enter a Integer Value in Tier Field");
			return;
		}
		
		  $.ajax({
			  	 
			  	 url: "buyers?requestType=updateBuyerTier&buyerId="+selectedBuyerforTierChange+"&tier="+tier, 
			 	 dataType: 'json',
				 success: function(result){
		       
		    		  $("#tierModal").modal('hide');

			//  alert(result.buyer_name);
			 // $("#oldconcurrency").val(result.concurrency_cap_limit);
			//  $("#confirmationelement").text("Change Concurrency of Buyer: "+result.buyer_name);
			 // $("#myModal").modal('show');
		    }});

		
		
	});
	
	
	function isInt(value) {
		  return !isNaN(value) && 
		         parseInt(Number(value)) == value && 
		         !isNaN(parseInt(value, 10));
		}
	
	function isFloat(value){
	    return ((!isNaN(value) && value.toString().indexOf('.') != -1 ) || (isInt(value)));
	}
	
	
  function checkValidPhoneNumber(inputtxt) 
	
	{  
	  var phoneno1 = /^\d{11}$/;
      var phoneno2 = /^\d{12}$/;
       if((inputtxt.match(phoneno1) || (inputtxt.match(phoneno2))))
           return true;
           else  return false;
	 
	}
	
	
	$("#savebuyerstate").click(function(){
		
		var state  = $("#state").val();
		var buyername = $("#buyername").val();
		var buyerNumber = $("#buyerNumber").val();
		var tier =  $("#tier").val();
		var weight =  $("#weight").val();
		var concurrency = $("#concurrency").val();
		var dailycap = $("#dailycap").val();
		var bidprice =  $("#bidprice").val();
		var ring_timeout =  $("#ring_timeout").val();

		
		if(!checkValidPhoneNumber(buyerNumber)) {
			
			alert("Please enter a Valid Phone Number, it should be 11 digit number with 1st digit as country code.");
			return ;
			
		}
		
		if(!isInt(tier)) {
			alert("Please enter a Integer Value in Tier Field");
			return;
		}
		
		if(!isInt(weight)) {
			alert("Please enter a Integer Value in Weight Field");
			return;
		}
		
		if(!isInt(concurrency)) {
			alert("Please enter a Integer Value in Concurrency Cap Field");
			return;
		}
		
		if(!isInt(dailycap)) {
			alert("Please enter a Integer Value in Daily Cap  Field");
			return;
		}
		
		if(!isFloat(bidprice)) {
			
			alert("Please enter a Decimal Value in Bid Price Field");
			return;

			
		}
		
		
		 $.post("buyers?requestType=updateBuyer",
			    {
			         	buyer_id:	selectedBuyerforStateChange,
			        	buyer_name:	buyername,
			        	buyer_number:	buyerNumber,
			        	weight:	weight,
			        	tier:	tier,
			        	concurrency_cap_limit:	concurrency,
			        	buyer_daily_cap:	dailycap,
			        	bid_price:	bidprice,
			        	is_active:	state,
			        	ring_timeout: ring_timeout
			        
			    },
			    function(data, status){
			    	
			    	
		    		  $("#stateModal").modal('hide');
			    });
	
	
		
		
	});
	
	
	$("#savebuyerweight").click(function(){
		
		var weight  = $("#newweight").val();
		
		if(!isInt(weight)) {
			alert("Please enter a Integer Value in Weight Field");
			return;
		}
		
		  $.ajax({
			  	 
			  	 url: "buyers?requestType=updateBuyerWeight&buyerId="+selectedBuyerforWeightChange+"&weight="+weight, 
			 	 dataType: 'json',
				 success: function(result){
		       
		    		  $("#weightModal").modal('hide');

			//  alert(result.buyer_name);
			 // $("#oldconcurrency").val(result.concurrency_cap_limit);
			//  $("#confirmationelement").text("Change Concurrency of Buyer: "+result.buyer_name);
			 // $("#myModal").modal('show');
		    }});

		
		
	});
	
	$("#savebuyerdailydcap").click(function(){
			
			var newdailydcap  = $("#newdailycap").val();
			
			if(!isInt(newdailydcap)) {
				alert("Please enter a Integer Value in Daily Cap  Field");
				return;
			}
			
			  $.ajax({
				  	 
				  	 url: "buyers?requestType=updateBuyerDailyCap&buyerId="+selectedBuyerfordailycap+"&dailycap="+newdailydcap, 
				 	 dataType: 'json',
					 success: function(result){
			       
			    		  $("#dailycapModal").modal('hide');
	
				//  alert(result.buyer_name);
				 // $("#oldconcurrency").val(result.concurrency_cap_limit);
				//  $("#confirmationelement").text("Change Concurrency of Buyer: "+result.buyer_name);
				 // $("#myModal").modal('show');
			    }});
	
			
			
		});
	
	
	 
    });

</script>
    
    
    
    
    
   
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
      <a href="trafficsourcereport.jsp">Traffic Source Report</a>

    </div>
  </li>
  
  
  


<li class="dropdown" style="float:right;">
<a href="LogoutServlet">Logout</a>
</li>

<li class="dropdown" style="float:right;">
<a href="profile.jsp">Profile</a>
</li>
   

  
</ul>    
    
    
      <div id="stateModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="confirmationelementstate"  style="font-family:Helvetica Neue, Helvetica, Arial, sans-serif;font-size: 18px;line-height: 1.42857143;color: #333;" >Confirmation</h4>
                </div>
                <div class="modal-body">
                
                <table style="font-family:Helvetica Neue, Helvetica, Arial, sans-serif;font-size: 14px;line-height: 1.42857143;color: #1e1e52;">
                <tr>
                     <td style="color:black"><p>Buyer's  Name: </p> </td>
                    
                    <td style="color:black"> <input type="text" id="buyername" value="">  </td>
                </tr>    
                  
                <tr>
                    
                   <td style="color:black"> <p>Buyer's  Number: </p> </td>
                   <td style="color:black"> <input type="text" id="buyerNumber" value=""></td>
                
                </tr>    
                
                
                   
                <tr>
                     <td style="color:black">   <p>Buyer's  Tier:</p> </td> <td style="color:black"> <input type="text" id="tier" value=""> </td> </tr>

				<tr>                   
                  <td style="color:black">   <p>Buyer's  Weight: </p></td>  <td style="color:black"> <input type="text" id="weight" value=""></td>
                 </tr> 
                    
                
                <tr>
                     <td style="color:black"> <p>Buyer's  Concurrency: </p> </td> <td style="color:black"><input type="text" id="concurrency" value=""> </td>
                   
                   
                 </tr> 
                   
                    <tr>
                     <td style="color:black">
                   <p>Buyer's  Daily Cap: </p> </td> <td style="color:black"> <input type="text" id="dailycap" value=""> </td>
                   </tr>
                   
                   
                   <tr>
                     <td style="color:black">
                    <p>Buyer's  Bid Price: </p>  </td> <td style="color:black"> <input type="text" id="bidprice" value=""> </td>
                    </tr>
                   
                   
                   <tr>
                     <td style="color:black">
                    <p>Buyer's  Ring Timeout: </p>  </td> <td style="color:black"> <input type="text" id="ring_timeout" value=""> </td>
                    </tr>
                   
                    <tr>
                     <td style="color:black">
                     <p>Buyer's  State: </p> </td>
                     <td style="color:black">
                
                    <select id="state">
						  <option value="1">Active</option>
						  <option value="0">InActive</option>
						</select>
                  </td>
                  </tr>
                   </table>              
                  
              
                     
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="savebuyerstate">Save Buyer</button>
               
                </div>
            </div>
        </div>
    </div>
    
    
    <div id="weightModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="confirmationelementweight">Confirmation</h4>
                </div>
                <div class="modal-body">
                
                    <p>Buyer's Old Weight: <input type="text" id="oldweight" value=""  readonly></p>
                    <p>Buyer's New Weight: <input type="text" id="newweight" value=""></p>
              
                     
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="savebuyerweight">Save Weight</button>
               
                </div>
            </div>
        </div>
    </div>
    

   <div id="concurrencyModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="confirmationelementconcurrency">Confirmation</h4>
                </div>
                <div class="modal-body">
                
                    <p>Buyer's Old Concurrency: <input type="text" id="oldconcurrency" value=""  readonly></p>
                    <p>Buyer's New Concurrency: <input type="text" id="newconcurrency" value=""></p>
              
                     
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="savebuyerconcurrency">Save Concurrency</button>
               
                </div>
            </div>
        </div>
    </div>
    
    
     <div id="tierModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="confirmationelementtier">Confirmation</h4>
                </div>
                <div class="modal-body">
                
                    <p>Buyer's Old Tier: <input type="text" id="oldtier" value=""  readonly></p>
                    <p>Buyer's New Tier: <input type="text" id="newtier" value=""></p>
              
                     
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="savebuyertier">Save Tier</button>
               
                </div>
            </div>
        </div>
    </div>
    
       <div id="dailycapModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="confirmationelementdailycap">Confirmation</h4>
                </div>
                <div class="modal-body">
                
                    <p>Buyer's Old Daily Cap: <input type="text" id="olddailycap" value=""  readonly></p>
                    <p>Buyer's New Daily Cap: <input type="text" id="newdailycap" value=""></p>
              
                     
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="savebuyerdailydcap">Save Daily Cap</button>
               
                </div>
            </div>
        </div>
    </div>
    

    
<div id="container">
<div class="wrapper">
<div class="container top-level">
<div  id="trackdrive-metrics-indexid" class="trackdrive-metrics-index" data-become="Trackdrive.Metrics.Index">





</div>

</div>
</div>
</div>


</body></html>
  