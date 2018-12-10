<%@page import="com.triyasoft.ui.DashboardUIGenerator"%>
<%@page import="com.triyasoft.model.CallModel"%>
<%@page import="com.triyasoft.utils.DailyReportUtil"%>
<%@page import="com.triyasoft.model.UserModel"%>
<%@page import="com.triyasoft.daos.BuyerDaoService"%>
<%@page import="com.triyasoft.model.Buyer"%>
<%@page import="com.triyasoft.model.TrafficSource"%>
<%@page import="java.util.List"%>
<%@page import="com.triyasoft.daos.TrafficSourceDaoService"%>
<%@page import="com.triyasoft.utils.ProjectUtils"%>

<%
	boolean checkIfSessionValid = ProjectUtils.checkSessionValidity(request);

    if(!checkIfSessionValid) {
    	//RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
		response.getWriter().println("<font color=red>Session has Expired.</font>");
		String baseURL = ProjectUtils.getBaseURL(request);
		response.sendRedirect(baseURL+"/login.jsp");
		return;
    }
%>

<html>
<head>
<meta charset="utf-8" />
<script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js"
	type="text/javascript"></script>
<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js"
	type="text/javascript"></script>
<link href="js/themes/metro/brown/jtable.css" rel="stylesheet"
	type="text/css" />
<link
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/ui-lightness/jquery-ui.css"
	rel="stylesheet" type="text/css" />

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


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

	function blockCaller(phonenumber) {

		var confirmation = confirm("Are you sure you want to block "
				+ phonenumber);

		if (confirmation == true) {

			$
					.ajax({

						url : "contacts?requestType=blockcontact&number="
								+ phonenumber,
						success : function(result) {

							alert(phonenumber + ' Blocked Successfully');
						}
					});
		}
	}
</script>

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
	box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
	z-index: 1;
}

.dropdown-content a {
	color: black;
	padding: 12px 16px;
	text-decoration: none;
	display: block;
	text-align: left;
}

.dropdown-content a:hover {
	background-color: #f1f1f1
}

.dropdown:hover .dropdown-content {
	display: block;
}

.container {
	width: 100%;
	overflow-x: auto;
	white-space: nowrap;
}

.divTable {
	display: table;
	width: 2000px;
	margin-bottom: 20px;
	overflow: scroll;
}

.divTableRow {
	display: table-row;
}

.divTableRowOdd {
	display: table-row;
}

.divTableRowEven {
	display: table-row;
	background-color: #f9f9f9;
}

.divTableRow:hover {
	background-color: rgba(0, 167, 255, 0.11);
}

.divTableRowEven:hover {
	background-color: rgba(0, 167, 255, 0.11);
}

.divTableRowOdd:hover {
	background-color: rgba(0, 167, 255, 0.11);
}

.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
}

.divTableCell, .divTableHead {
	border: 1px solid #999999;
	display: table-cell;
	padding: 3px 10px;
}

.divTableHeading {
	background-color: #EEE;
	display: table-header-group;
	font-weight: bold;
}

.divTableFoot {
	background-color: #EEE;
	display: table-footer-group;
	font-weight: bold;
}

.divTableBody {
	display: table-row-group;
}

div.filtering {
	border: 1px solid #999;
	margin-bottom: 5px;
	padding: 10px;
	background-color: #EEE;
}

.styled-select {
	background: url(http://i62.tinypic.com/15xvbd5.png) no-repeat 96% 0;
	height: 29px;
	overflow: hidden;
	width: 240px;
}

.styled-select select {
	background: transparent;
	border: none;
	font-size: 14px;
	height: 29px;
	padding: 5px;
	/* If you add too much padding here, the options won't show in IE */
	width: 268px;
}

.rounded {
	-webkit-border-radius: 20px;
	-moz-border-radius: 20px;
	border-radius: 20px;
}

.green {
	background-color: #779126;
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

.columnSelector {
	left: auto;
	min-width: 250px;
	position: absolute;
	top: 70px;
	z-index: 9999;
	padding: 10px;
	outline: none !important;
	box-sizing: border-box;
	font-family: 'Roboto', sans-serif;
	color: #222;
}

.labelcls {
	padding: 3px 0;
	padding-bottom: 3px;
	margin-bottom: 5px;
	padding-bottom: 5px;
	display: block;
	padding: 2px 0;
	margin: 0;
	max-width: 100%;
	font-weight: 700;
	outline: none !important;
	font-size: 14px;
	text-align: left;
	list-style: none;
	font-family: 'Roboto', sans-serif;
	color: #222;
	line-height: 1.42857143;
}

.inputclass {
	margin-right: 5px;
	line-height: normal;
	box-sizing: border-box;
	padding: 0;
	outline: none !important;
	text-align: left;
	list-style: none;
}
</style>

<script src="js/jquery.jtable.min.js" type="text/javascript"></script>



</head>
<body>

	<ul>
		<li><a href="dashboard.jsp">Dashboard</a></li>

		<li><a href="live_calls.jsp">Calls</a></li>


		<li class="dropdown"><a href="traffic_sources.jsp"
			class="dropbtn">Traffic Sources</a></li>


		<li class="dropdown"><a href="buyers.jsp" class="dropbtn">Buyers</a>
		</li>

		<li class="dropdown"><a href="filters.jsp" class="dropbtn">Filters</a>

		</li>

		<li class="dropdown"><a href="numbers.jsp" class="dropbtn">Numbers</a>

		</li>

		<li><a href="users.jsp">Users</a></li>


		<li class="dropdown"><a href="reports.jsp">Reports</a>

			<div class="dropdown-content">
				<a href="trafficsourcereport.jsp">Traffic Sources Summary</a> <a
					href="sourcesummary.jsp">Traffic Source Summary(Date-wise)</a>

			</div></li>




		<li class="dropdown" style="float: right">
			<%
				UserModel  user  = (UserModel)request.getSession().getAttribute("user");
			if( user ==null || user.getUserroleid() != 1)
			{
				response.sendRedirect(ProjectUtils.getBaseURL(request)+"/login.jsp");
				return;
				}
			%> <a href="javascript:void(0)" class="dropbtn">
				<%
					out.print(user.getFirstname()+"  "+user.getLastname());
				%>
		</a>
			<div class="dropdown-content">
				<a href="#">Profile</a> <a href="LogoutServlet">Logout</a>
			</div>
		</li>



	</ul>

	<div id="weightModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="confirmationelementweight">Routing
						Explanation</h4>
				</div>
				<div class="modal-body">

					<p id="routingtext"></p>



				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

				</div>
			</div>
		</div>
	</div>


	<div class="filtering">

		Source: <select id="sourceId" name="sourceId">


			<option selected="selected" value="-1">All Sources</option>

			<%
				List<TrafficSource> trafficSources = TrafficSourceDaoService.loadAllSources();
			            for(int i = 0 ; i< trafficSources.size();i++) {
			            out.println("<option value=\""+trafficSources.get(i).getId()+"\">"+trafficSources.get(i).getFirst_name()+" "+trafficSources.get(i).getLast_name()+"</option>");
			            }
			%>
		</select> Buyer: <select id="buyerId" name="buyerId">


			<option selected="selected" value="-1">All Buyers</option>

			<%
				List<Buyer> buyers = BuyerDaoService.loadAllBuyers();
			            
			            System.out.println("Server Base Path= "+ProjectUtils.getBaseURL(request));
			            for(int i = 0 ; i< buyers.size();i++) {
			            
			            	out.println("<option value=\""+buyers.get(i).getBuyer_id()+"\">"+buyers.get(i).getBuyer_name()+"</option>");
			            }
			%>


		</select> Start Date: <input type="text" id="datepicker1" name="startdate">
		End Date: <input type="text" id="datepicker2" name="enddate">

		<button type="submit" id="detailedcallreport"
			onclick="getDetailedCallReport();">Detailed Call Report(In
			xls)</button>
		<button type="submit" id="LoadRecordsButton" onclick="reloadReport();">Pull
			Report</button>




	</div>
	<!--  
       <button type="submit" id="LoadRecordsButton" onclick="configureColumn();">Configure Report Columns</button>
-->
	<div class="columnSelector" id="columnSelector" style="display: none">


		<label class="labelcls"><input data-column="0"
			class="inputclass, checked" type="checkbox">Calls by Buyer</label> <label
			class="labelcls"><input data-column="1"
			class="inputclass,checked" type="checkbox">Total Calls</label> <label
			class="labelcls"><input data-column="2"
			class="inputclass,checked" type="checkbox">Calls %</label> <label
			class="labelcls"><input data-column="3"
			class="inputclass,checked" type="checkbox">B Attempts</label> <label
			class="labelcls"><input data-column="4"
			class="inputclass,checked" type="checkbox">B Connected</label> <label
			class="labelcls"><input data-column="5"
			class="inputclass,checked" type="checkbox">B Answered %</label> <label
			class="labelcls"><input data-column="6" type="checkbox">B
			Repeats</label> <label class="labelcls"><input data-column="7"
			class="inputclass,checked" type="checkbox">B Duplicates</label> <label
			class="labelcls"><input data-column="8"
			class="inputclass,checked" type="checkbox">B Uniques</label> <label
			class="labelcls"><input data-column="9"
			class="inputclass,checked" type="checkbox">B Converted</label> <label
			class="labelcls"><input data-column="10" type="checkbox">B
			Converted %</label> <label class="labelcls"><input data-column="11"
			class="inputclass,checked" type="checkbox">B Revenue</label> <label
			class="labelcls"><input data-column="12" type="checkbox">B
			RPC</label> <label class="labelcls"><input data-column="13"
			class="inputclass,checked" type="checkbox">Avg Call Time</label> <label
			class="labelcls"><input data-column="14" type="checkbox">Avg
			Forwarded Time</label> <label class="labelcls"><input
			data-column="15" type="checkbox">Impressions Requested</label> <label
			class="labelcls"><input data-column="16" class="checked"
			type="checkbox">Impressions Displayed</label> <label class="labelcls"><input
			data-column="17" class="checked" type="checkbox">Displayed %</label>
		<label class="labelcls"><input data-column="18"
			class="checked" type="checkbox">Impressions Per Call</label> <label
			class="labelcls"><input data-column="19" class="checked"
			type="checkbox">TS Repeats</label> <label class="labelcls"><input
			data-column="20" class="checked" type="checkbox">TS
			Duplicates</label> <label class="labelcls"><input data-column="21"
			class="checked" type="checkbox">TS Uniques</label> <label
			class="labelcls"><input data-column="22" class="checked"
			type="checkbox">TS Converted</label> <label class="labelcls"><input
			data-column="23" class="checked" type="checkbox">TS Converted
			%</label> <label class="labelcls"><input data-column="24"
			class="checked" type="checkbox">TS Payout</label> <label
			class="labelcls"><input data-column="25" class="checked"
			type="checkbox">TS PPC</label> <label class="labelcls"><input
			data-column="26" class="checked" type="checkbox">Trackdrive
			Cost</label> <label class="labelcls"><input data-column="27"
			class="checked" type="checkbox">Provider Cost</label> <label
			class="labelcls"><input data-column="28" class="checked"
			type="checkbox">Provider CPC</label> <label class="labelcls"><input
			data-column="29" class="checked" type="checkbox">Total Cost</label> <label
			class="labelcls"><input data-column="30" class="checked"
			type="checkbox">Avg Total CPC</label> <label class="labelcls"><input
			data-column="31" class="checked" type="checkbox">Profit</label>
		<button type="submit" id="LoadRecordsButton" onclick="saveColumns();">Save</button>


	</div>

	<div class="container" style="max-height: 100%; overflow: auto;">

		<div class="divTable">
			<div class="divTableBody">
				<div class="divTableRow">
					<div class="divTableCell" colindex="1"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">UUID</div>
					<div class="divTableCell" colindex="2"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Call
						Source</div>
					<div class="divTableCell" colindex="3"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Caller</div>
					<div class="divTableCell" colindex="4"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Buyer</div>
					<div class="divTableCell" colindex="5"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">
						Total Duration</div>
					<div class="divTableCell" colindex="7"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Created
						At</div>
					<div class="divTableCell" colindex="8"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">End
						Time</div>

					<div class="divTableCell" colindex="9"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">TS
						Converted</div>
					<div class="divTableCell" colindex="10"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Buyer
						Converted</div>
					<div class="divTableCell" colindex="6"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Status</div>


					<div class="divTableCell" colindex="15"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Call
						Attempts</div>
					<div class="divTableCell" colindex="16"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Connected
						Time</div>
					<div class="divTableCell" colindex="17"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Buyer
						Connecting Time</div>
					<div class="divTableCell" colindex="18"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Buyers
						Tried</div>
					<div class="divTableCell" colindex="19"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Buyer
						Hangup Reason</div>


					<div class="divTableCell" colindex="11"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Provider
						Cost</div>
					<div class="divTableCell" colindex="12"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">TS
						Revenue</div>
					<div class="divTableCell" colindex="13"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Buyer
						Revenue</div>
					<div class="divTableCell" colindex="14"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Profit</div>




					<div class="divTableCell" colindex="20"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Error
						Code</div>
					<div class="divTableCell" colindex="21"
						style="color: #626262; border: 1px solid #bfbfbf; font-weight: bold; background: #eeeeee">Error
						Description</div>
				</div>

				<%
					List<CallModel> calls =   DailyReportUtil.getReportRawData(request);
				int counter = 0 ;
				for(int i = 0 ; i < calls.size(); i ++) {
					int colindex = 0;
					CallModel call = calls.get(i);
					counter++;
					
					String buyer  = "";
					if(call.getBuyer() == null && !"Customer Disconnected Call".equals(call.getNo_connect_cause()))
						buyer = "No Buyer";
					
					else if(call.getBuyer() == null && "Customer Disconnected Call".equals(call.getNo_connect_cause())) 
					{
						buyer = "Customer Disconnected";
					}
					
					else 
						buyer = call.getBuyer() +":" +call.getConnected_to();
					
					boolean tsConverted  = false;
					boolean buyerConverted = false;
					
					String tsConvertedStyle= "";
					String tsConvertedText = "";
					
					String buyerConvertedStyle= "";
					String buyerConvertedtext= "";

					
					if(call.getTraffic_source_revenue() > 0 ) {
						tsConverted = true;
						tsConvertedStyle= "style=\"background: #599734;border-color: #599734;color: #e5f6db;\"";
						tsConvertedText = "Converted";
					}
					
					if(call.getBuyer_revenue() > 0 ) {
						buyerConverted = true;
						buyerConvertedStyle = "style=\"background: #599734;border-color: #599734;color: #e5f6db;\"";
						buyerConvertedtext = "Converted";
					}
				    
					if(!"get.kksingh@gmail.com".equals(user.getEmailid()) && ( "9841 Ak".equals(call.getTraffic_source()) ||  "ak-5".equals(call.getTraffic_source()) || "18445458535".equals(call.getNumber_called()) || "18445458537".equals(call.getNumber_called())|| "18445458531".equals(call.getNumber_called())))
					call.setRecording_url("null");
					
					String duration = "0";
					
					if(call.getDuration() !=null)
						duration = call.getDuration();
					
					String durationText = ProjectUtils.convertDurationToString(Double.parseDouble(duration));
					String durationStyle = "style=\"background: #599734; border-color: #599734;color: #e5f6db;\"";	
					
					if("0".equals(duration))
						durationStyle = "style=\"background: #ac2c2d;border-color: #ac2c2d;color: #fbe2e3;\"";

					
					
					String status = "";
					
				    String callStatusAtHangup =  call.getCallStatusAtHangup();
				    String statusStyle = "";
				    String statusText = "";
				    
				    if("completed".equals(callStatusAtHangup)){
				    	statusText = "Finished";
				    	statusStyle = "style=\"background: #c9ecb3;border-color: #c9ecb3;color: #4f8d2a;\"";
				    }
				    if("no-answer".equals(callStatusAtHangup)) {
				    	statusText = "No Answer";
				    	statusStyle =  "style=\"background: #f9cbcb;border-color: #f9cbcb;color: #9e2727;\"";
				    }
				    if("cancel".equals(callStatusAtHangup)) {
				    	statusText = "Caller Hung Up";
				    	statusStyle = "style=\"background: #ffe28d;border-color: #ffe28d;color: #9d7d23;\"";
				    }



					
					
					
					
					
					String phoneCost = "0";
					
					 if(call.getTotalCost() !=null)
						 phoneCost = call.getTotalCost();
					
						 String cssClassName = "";
					    if(counter%2 ==0)
					    	cssClassName = "divTableRowEven";
					    else 
					    	cssClassName = "divTableRowOdd";
					    
					    String errorCode = "";
					    String errorDesc = "";
					    
					    
					    if(call.getError_code() !=null)
					    	errorCode = call.getError_code();
					    
					    String buyerStyle = "";
					    
					    if("E009".equals(errorCode)) {
					    	buyer = "BLOCKED";
					    	buyerStyle = "style=\"background: #f9cbcb;border-color: #f9cbcb;color: #9e2727;\"";
					    	statusText = "DISCONNECTED";
					    	statusStyle =  "style=\"background: #f9cbcb;border-color: #f9cbcb;color: #9e2727;\"";

					    }

					    
					    if("No Buyer".equals(buyer)) {
					    	buyerStyle = "style=\"background: #ac2c2d;border-color: #ac2c2d;color: #fbe2e3;;\"";

					    }
					    
					    if("Customer Disconnected".equals(buyer)) {
					    	buyerStyle = "style=\"background: #423b3b; border-color: #ac2c2d;color: #ffffff;;\"";

					    }
					    
					    if(call.getError_description() != null)
					    	errorDesc = call.getError_description();
				%>
				<div class="<%=cssClassName%>">
					<div colindex="<%=++colindex%>" class="divTableCell">
						<a
							href="https://manage.plivo.com/logs/debug/call/<%=call.getUuid()%>/"
							target="_blank" style="color: black;"><%=call.getUuid().substring(15)%></a>
					</div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=call.getTraffic_source() +":"+ call.getNumber_called()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						onclick="blockCaller('<%=call.getCaller_number()%>')"><%=call.getCaller_number()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						onclick="showRoutingLogs('<%=call.getUuid()%>')"
						<%=buyerStyle%>><%=buyer%></div>
					<div colindex="<%=++colindex%>" class="divTableCell">
						<a href="<%=call.getRecording_url()%>" target="_blank"" > <span
							<%=durationStyle%>> <%=durationText%> mins
						</span>
						</a>
					</div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=call.getDateInEST()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=call.getEndDateInEST()%></div>

					<div colindex="<%=++colindex%>" class="divTableCell"
						<%=tsConvertedStyle%>><%=tsConvertedText%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						<%=buyerConvertedStyle%>><%=buyerConvertedtext%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						<%=statusStyle%>><%=statusText%></div>


					<div colindex="<%=++colindex%>" class="divTableCell"><%=call.getCallattempts()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=call.getConnected_time()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=call.getBuyer_connecting_time()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=(call.getBuyers_tried() == null) ? "" : call.getBuyers_tried()%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=(call.getBuyer_hangup_reason() == null) ? "" : call.getBuyer_hangup_reason()%></div>


					<div colindex="<%=++colindex%>" class="divTableCell"
						style="color: #599734;">
						$<%=DashboardUIGenerator.convertDouble2DigitPrecison(Double.parseDouble(phoneCost))%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						style="color: #599734;">
						$<%=DashboardUIGenerator.convertDouble2DigitPrecison(call.getTraffic_source_revenue())%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						style="color: #599734;">
						$<%=DashboardUIGenerator.convertDouble2DigitPrecison(call.getBuyer_revenue())%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"
						style="color: #599734;">
						$<%=DashboardUIGenerator.convertDouble2DigitPrecison(call.getBuyer_revenue()-call.getTraffic_source_revenue()-Double.parseDouble(phoneCost))%></div>



					<div colindex="<%=++colindex%>" class="divTableCell"><%=errorCode%></div>
					<div colindex="<%=++colindex%>" class="divTableCell"><%=errorDesc%></div>
				</div>

				<%
					}
				%>
			</div>
		</div>
	</div>



	<script type="text/javascript">
		var buyerId = -1;
		var sourceId = -1;
		var startdate = "";
		var endDate = "";
	<%String buyerId = request.getParameter("buyerId");
String sourceId = request.getParameter("sourceId");
String startdate = request.getParameter("startdate");
String endDate = request.getParameter("endDate");

if(buyerId!=null && buyerId.trim().length()>0)
out.println("buyerId = "+buyerId+";");

if(sourceId!=null && sourceId.trim().length()>0)
out.println("sourceId = "+sourceId+";");

if(startdate!=null && startdate.trim().length()>0)
out.println("startdate = "+startdate+";");

if(endDate!=null && endDate.trim().length()>0)
out.println("endDate = "+endDate+";");%>
		function getDetailedCallReport() {

			startdate = $("#datepicker1").val();
			endDate = $("#datepicker2").val();
			var url = 'reports?requestType=exportyesterdayReport'
					+ '&startdate=' + startdate + '&endDate=' + endDate;
			window.open(url);

		}

		function saveColumns() {

			$(".filtering").show();
			$(".container").show();
			$(".columnSelector").hide();
		}

		function configureColumn() {

			$(".filtering").hide();
			$(".container").hide();
			$(".columnSelector").show();

		}

		function reloadReport() {
			buyerId = $("#buyerId").val();
			sourceId = $("#sourceId").val();
			startdate = $("#datepicker1").val();
			endDate = $("#datepicker2").val();
			window.location = "reports.jsp?startdate=" + startdate
					+ "&endDate=" + endDate + "&buyerId=" + buyerId
					+ "&sourceId=" + sourceId;
		}

		$(function() {
			$("#datepicker1").datepicker({
				dateFormat : "yy-mm-dd"
			});
		});

		$(function() {
			$("#datepicker2").datepicker({
				dateFormat : "yy-mm-dd"
			});
		});
	</script>


</body>
</html>



