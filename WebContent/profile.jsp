
 <%@page import="com.triyasoft.daos.UsersDao"%>
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
    
    String  userid  = ((UserModel)request.getSession().getAttribute("user")).getUserid();
    UserModel user = UsersDao.getUserDetails(userid);

    %>

<html>
<head>
    <meta charset="utf-8" />
                    <script src="http://www.jtable.org/Scripts/jquery-1.9.1.min.js" type="text/javascript"></script>
    			<script src="http://www.jtable.org/Scripts/jquery-ui-1.10.0.min.js" type="text/javascript"></script>
            <link href="js/themes/metro/brown/jtable.css" rel="stylesheet" type="text/css" />
        <link href="css/jquery-ui.css" rel="stylesheet" type="text/css" />
        
              
            <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		    <link href="css/tdstyles.css" rel="stylesheet" type="text/css">
		    
		    
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
    

<form action="user?requestType=updateuser" method="post">

<div class="content-section">
	<div class=" form-section-container  has-content-section"
		data-become="Trackdrive.FormSection" data-index="1" id="account">
		<div class="form-section-header">
			<h4 class="form-section-name">Account</h4>
		</div>
		<div class="form-section-content">
			<div class="form-section-content-inner clearfix">
				<div class="form-group email required user_email">
					<label class="email required col-sm-2 control-label"
						for="user_email"><abbr title="required">*</abbr> Email</label>
					<div class="col-sm-10">
						<input aria-required="true" readonly="true"
							class="string email required form-control" id="user_email"
							name="user[email]" required="required"
							value="<%= user.getEmailid() %>" data-fv-field="user[email]"
							type="email"><small style="display: none;"
							class="help-block" data-fv-validator="emailAddress"
							data-fv-for="user[email]" data-fv-result="NOT_VALIDATED">Please
							enter a valid email address</small><small style="display: none;"
							class="help-block" data-fv-validator="notEmpty"
							data-fv-for="user[email]" data-fv-result="NOT_VALIDATED">Please
							enter a value</small>
					</div>
				</div>
				<div class="form-group password optional user_password">
					<label class="password optional col-sm-2 control-label"
						for="user_password">Password</label>
					<div class="col-sm-10">
						<input class="password optional form-control" id="user_password"
							name="user[password]" type="password">
						<p class="help-block">leave it blank if you dont want to
							change it</p>
					</div>
				</div>
				<div class="form-group password optional user_password_confirmation">
					<label class="password optional col-sm-2 control-label"
						for="user_password_confirmation">Password confirmation</label>
					<div class="col-sm-10">
						<input class="password optional form-control"
							id="user_password_confirmation"
							name="user[password_confirmation]" type="password">
					</div>
				</div>
				<div class="form-group password optional user_current_password">
					<label class="password optional col-sm-2 control-label"
						for="user_current_password">Current password</label>
					<div class="col-sm-10">
						<input class="password optional form-control"
							id="user_current_password" name="user[current_password]"
							type="password">
						<p class="help-block">we need your current password if you
							made changes above</p>
					</div>
				</div>
				<div class="form-group time_zone optional user_time_zone">
					<label class="time_zone optional col-sm-2 control-label"
						for="user_time_zone">Time zone</label>
					<div class="col-sm-10">
						<select
							class="time_zone optional form-control"
							 id="user_time_zone"
							name="user[time_zone]" style="width: 100%;" tabindex="-1"
							><option
								value="Pacific Time (US &amp; Canada)">(GMT-08:00)
								Pacific Time (US &amp; Canada)</option>
							<option value="Mountain Time (US &amp; Canada)">(GMT-07:00)
								Mountain Time (US &amp; Canada)</option>
							<option value="Central Time (US &amp; Canada)">(GMT-06:00)
								Central Time (US &amp; Canada)</option>
							<option selected="selected"
								value="Eastern Time (US &amp; Canada)">(GMT-05:00)
								Eastern Time (US &amp; Canada)</option>

							<option value="American Samoa">(GMT-11:00) American
								Samoa</option>
							<option value="International Date Line West">(GMT-11:00)
								International Date Line West</option>
							<option value="Midway Island">(GMT-11:00) Midway Island</option>
							<option value="Hawaii">(GMT-10:00) Hawaii</option>
							<option value="Alaska">(GMT-09:00) Alaska</option>
							<option value="Tijuana">(GMT-08:00) Tijuana</option>
							<option value="Arizona">(GMT-07:00) Arizona</option>
							<option value="Chihuahua">(GMT-07:00) Chihuahua</option>
							<option value="Mazatlan">(GMT-07:00) Mazatlan</option>
							<option value="Central America">(GMT-06:00) Central
								America</option>
							<option value="Guadalajara">(GMT-06:00) Guadalajara</option>
							<option value="Mexico City">(GMT-06:00) Mexico City</option>
							<option value="Monterrey">(GMT-06:00) Monterrey</option>
							<option value="Saskatchewan">(GMT-06:00) Saskatchewan</option>
							<option value="Bogota">(GMT-05:00) Bogota</option>
							<option value="EST">(GMT-05:00) EST</option>
							<option value="Indiana (East)">(GMT-05:00) Indiana
								(East)</option>
							<option value="Lima">(GMT-05:00) Lima</option>
							<option value="Quito">(GMT-05:00) Quito</option>
							<option value="Atlantic Time (Canada)">(GMT-04:00)
								Atlantic Time (Canada)</option>
							<option value="Caracas">(GMT-04:00) Caracas</option>
							<option value="Georgetown">(GMT-04:00) Georgetown</option>
							<option value="La Paz">(GMT-04:00) La Paz</option>
							<option value="Santiago">(GMT-04:00) Santiago</option>
							<option value="Newfoundland">(GMT-03:30) Newfoundland</option>
							<option value="Brasilia">(GMT-03:00) Brasilia</option>
							<option value="Buenos Aires">(GMT-03:00) Buenos Aires</option>
							<option value="Greenland">(GMT-03:00) Greenland</option>
							<option value="Montevideo">(GMT-03:00) Montevideo</option>
							<option value="Mid-Atlantic">(GMT-02:00) Mid-Atlantic</option>
							<option value="Azores">(GMT-01:00) Azores</option>
							<option value="Cape Verde Is.">(GMT-01:00) Cape Verde
								Is.</option>
							<option value="Casablanca">(GMT+00:00) Casablanca</option>
							<option value="Dublin">(GMT+00:00) Dublin</option>
							<option value="Edinburgh">(GMT+00:00) Edinburgh</option>
							<option value="Etc/UTC">(GMT+00:00) Etc/UTC</option>
							<option value="Lisbon">(GMT+00:00) Lisbon</option>
							<option value="London">(GMT+00:00) London</option>
							<option value="Monrovia">(GMT+00:00) Monrovia</option>
							<option value="UTC">(GMT+00:00) UTC</option>
							<option value="Amsterdam">(GMT+01:00) Amsterdam</option>
							<option value="Belgrade">(GMT+01:00) Belgrade</option>
							<option value="Berlin">(GMT+01:00) Berlin</option>
							<option value="Bern">(GMT+01:00) Bern</option>
							<option value="Bratislava">(GMT+01:00) Bratislava</option>
							<option value="Brussels">(GMT+01:00) Brussels</option>
							<option value="Budapest">(GMT+01:00) Budapest</option>
							<option value="Copenhagen">(GMT+01:00) Copenhagen</option>
							<option value="Ljubljana">(GMT+01:00) Ljubljana</option>
							<option value="Madrid">(GMT+01:00) Madrid</option>
							<option value="Paris">(GMT+01:00) Paris</option>
							<option value="Prague">(GMT+01:00) Prague</option>
							<option value="Rome">(GMT+01:00) Rome</option>
							<option value="Sarajevo">(GMT+01:00) Sarajevo</option>
							<option value="Skopje">(GMT+01:00) Skopje</option>
							<option value="Stockholm">(GMT+01:00) Stockholm</option>
							<option value="Vienna">(GMT+01:00) Vienna</option>
							<option value="Warsaw">(GMT+01:00) Warsaw</option>
							<option value="West Central Africa">(GMT+01:00) West
								Central Africa</option>
							<option value="Zagreb">(GMT+01:00) Zagreb</option>
							<option value="Athens">(GMT+02:00) Athens</option>
							<option value="Bucharest">(GMT+02:00) Bucharest</option>
							<option value="Cairo">(GMT+02:00) Cairo</option>
							<option value="Harare">(GMT+02:00) Harare</option>
							<option value="Helsinki">(GMT+02:00) Helsinki</option>
							<option value="Istanbul">(GMT+02:00) Istanbul</option>
							<option value="Jerusalem">(GMT+02:00) Jerusalem</option>
							<option value="Kyiv">(GMT+02:00) Kyiv</option>
							<option value="Pretoria">(GMT+02:00) Pretoria</option>
							<option value="Riga">(GMT+02:00) Riga</option>
							<option value="Sofia">(GMT+02:00) Sofia</option>
							<option value="Tallinn">(GMT+02:00) Tallinn</option>
							<option value="Vilnius">(GMT+02:00) Vilnius</option>
							<option value="Baghdad">(GMT+03:00) Baghdad</option>
							<option value="Kuwait">(GMT+03:00) Kuwait</option>
							<option value="Minsk">(GMT+03:00) Minsk</option>
							<option value="Moscow">(GMT+03:00) Moscow</option>
							<option value="Nairobi">(GMT+03:00) Nairobi</option>
							<option value="Riyadh">(GMT+03:00) Riyadh</option>
							<option value="St. Petersburg">(GMT+03:00) St.
								Petersburg</option>
							<option value="Volgograd">(GMT+03:00) Volgograd</option>
							<option value="Tehran">(GMT+03:30) Tehran</option>
							<option value="Abu Dhabi">(GMT+04:00) Abu Dhabi</option>
							<option value="Baku">(GMT+04:00) Baku</option>
							<option value="Muscat">(GMT+04:00) Muscat</option>
							<option value="Tbilisi">(GMT+04:00) Tbilisi</option>
							<option value="Yerevan">(GMT+04:00) Yerevan</option>
							<option value="Kabul">(GMT+04:30) Kabul</option>
							<option value="Ekaterinburg">(GMT+05:00) Ekaterinburg</option>
							<option value="Islamabad">(GMT+05:00) Islamabad</option>
							<option value="Karachi">(GMT+05:00) Karachi</option>
							<option value="Tashkent">(GMT+05:00) Tashkent</option>
							<option value="Chennai">(GMT+05:30) Chennai</option>
							<option value="Kolkata">(GMT+05:30) Kolkata</option>
							<option value="Mumbai">(GMT+05:30) Mumbai</option>
							<option value="New Delhi">(GMT+05:30) New Delhi</option>
							<option value="Sri Jayawardenepura">(GMT+05:30) Sri
								Jayawardenepura</option>
							<option value="Kathmandu">(GMT+05:45) Kathmandu</option>
							<option value="Almaty">(GMT+06:00) Almaty</option>
							<option value="Astana">(GMT+06:00) Astana</option>
							<option value="Dhaka">(GMT+06:00) Dhaka</option>
							<option value="Novosibirsk">(GMT+06:00) Novosibirsk</option>
							<option value="Urumqi">(GMT+06:00) Urumqi</option>
							<option value="Rangoon">(GMT+06:30) Rangoon</option>
							<option value="Bangkok">(GMT+07:00) Bangkok</option>
							<option value="Hanoi">(GMT+07:00) Hanoi</option>
							<option value="Jakarta">(GMT+07:00) Jakarta</option>
							<option value="Krasnoyarsk">(GMT+07:00) Krasnoyarsk</option>
							<option value="Beijing">(GMT+08:00) Beijing</option>
							<option value="Chongqing">(GMT+08:00) Chongqing</option>
							<option value="Hong Kong">(GMT+08:00) Hong Kong</option>
							<option value="Irkutsk">(GMT+08:00) Irkutsk</option>
							<option value="Kuala Lumpur">(GMT+08:00) Kuala Lumpur</option>
							<option value="Perth">(GMT+08:00) Perth</option>
							<option value="Singapore">(GMT+08:00) Singapore</option>
							<option value="Taipei">(GMT+08:00) Taipei</option>
							<option value="Ulaanbaatar">(GMT+08:00) Ulaanbaatar</option>
							<option value="Osaka">(GMT+09:00) Osaka</option>
							<option value="Sapporo">(GMT+09:00) Sapporo</option>
							<option value="Seoul">(GMT+09:00) Seoul</option>
							<option value="Tokyo">(GMT+09:00) Tokyo</option>
							<option value="Yakutsk">(GMT+09:00) Yakutsk</option>
							<option value="Adelaide">(GMT+09:30) Adelaide</option>
							<option value="Darwin">(GMT+09:30) Darwin</option>
							<option value="Brisbane">(GMT+10:00) Brisbane</option>
							<option value="Canberra">(GMT+10:00) Canberra</option>
							<option value="Guam">(GMT+10:00) Guam</option>
							<option value="Hobart">(GMT+10:00) Hobart</option>
							<option value="Melbourne">(GMT+10:00) Melbourne</option>
							<option value="Port Moresby">(GMT+10:00) Port Moresby</option>
							<option value="Sydney">(GMT+10:00) Sydney</option>
							<option value="Vladivostok">(GMT+10:00) Vladivostok</option>
							<option value="Magadan">(GMT+11:00) Magadan</option>
							<option value="New Caledonia">(GMT+11:00) New Caledonia</option>
							<option value="Solomon Is.">(GMT+11:00) Solomon Is.</option>
							<option value="Auckland">(GMT+12:00) Auckland</option>
							<option value="Fiji">(GMT+12:00) Fiji</option>
							<option value="Kamchatka">(GMT+12:00) Kamchatka</option>
							<option value="Marshall Is.">(GMT+12:00) Marshall Is.</option>
							<option value="Wellington">(GMT+12:00) Wellington</option>
							<option value="Chatham Is.">(GMT+12:45) Chatham Is.</option>
							<option value="Nuku'alofa">(GMT+13:00) Nuku'alofa</option>
							<option value="Samoa">(GMT+13:00) Samoa</option>
							<option value="Tokelau Is.">(GMT+13:00) Tokelau Is.</option>
							
							</select>
							
							
						<p class="help-block">Dates and times will be
							displayed with this time zone.</p>
					</div>
				</div>

			</div>
		</div>
	</div>

</div>

<div class="content-section">
	<div class=" form-section-container  has-content-section"
		data-become="Trackdrive.FormSection" data-index="2"
		id="contact-details">
		<div class="form-section-header">
			<h4 class="form-section-name">Contact Details</h4>
		</div>
		<div class="form-section-content">
			<div class="form-section-content-inner clearfix">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group string required user_first_name">
							<label class="string required col-sm-2 control-label"
								for="user_first_name"><abbr title="required">*</abbr>
								First name</label>
							<div class="col-sm-10">
								<input aria-required="true" class="string required form-control"
									id="user_first_name" name="user[first_name]"
									required="required" value="<%=user.getFirstname() %>"
									data-fv-field="user[first_name]" type="text"><small
									style="display: none;" class="help-block"
									data-fv-validator="notEmpty" data-fv-for="user[first_name]"
									data-fv-result="NOT_VALIDATED">Please enter a value</small>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group string optional user_last_name">
							<label class="string optional col-sm-2 control-label"
								for="user_last_name">Last name</label>
							<div class="col-sm-10">
								<input class="string optional form-control" id="user_last_name"
									name="user[last_name]" value="<%=user.getLastname() %>" type="text">
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group tel optional user_phone_1">
							<label class="tel optional col-sm-2 control-label"
								for="user_phone_1">Phone</label>
							<div class="col-sm-10">
								<input class="string tel optional form-control"
									id="user_phone_1" name="user[phone_1]" value="<%=  user.getPhonenumber() != null ? user.getPhonenumber() : ""  %>"  
									type="tel">
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group string optional user_skype_name">
							<label class="string optional col-sm-2 control-label"
								for="user_skype_name">Skype</label>
							<div class="col-sm-10">
								<input class="string optional form-control" id="user_skype_name"
									name="user[skype_name]" type="text"  value="<%=user.getSkype() != null ? user.getSkype() : ""   %>">
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

</div>

<div class="content-section">
	<div class=" form-section-container  has-content-section"
		data-become="Trackdrive.FormSection" data-index="3" id="address">
		<div class="form-section-header">
			<h4 class="form-section-name">Address</h4>
		</div>
		<div class="form-section-content">
			<div class="form-section-content-inner clearfix">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group string optional user_address_line1">
							<label class="string optional col-sm-2 control-label"
								for="user_address_line1">Line 1</label>
							<div class="col-sm-10">
								<input class="string optional form-control"
									id="user_address_line1" name="user[address_line1]"
									value="<%= user.getAddressline1() != null ? user.getAddressline1() : ""    %>" type="text">
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group string optional user_address_line2">
							<label class="string optional col-sm-2 control-label"
								for="user_address_line2">Line 2</label>
							<div class="col-sm-10">
								<input class="string optional form-control"
									id="user_address_line2" name="user[address_line2]"
									value="<%=user.getAddressline2() != null ? user.getAddressline2() : ""     %>" type="text">
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group string optional user_address_city">
							<label class="string optional col-sm-2 control-label"
								for="user_address_city">City</label>
							<div class="col-sm-10">
								<input class="string optional form-control"
									id="user_address_city" name="user[address_city]"
									value="<%=user.getCity() != null ? user.getCity() : ""     %>" type="text">
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group select optional user_address_state">
							<label class="select optional col-sm-2 control-label"
								for="user_address_state">State</label>
	
	
							<div class="col-sm-10">
								<input class="string optional form-control"
									id="user_address_state" name="user[address_state]"
									value="<%=user.getState() != null ? user.getState() : ""  %>" type="text">
							</div>
	
	
	
	
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group string optional user_address_zip">
							<label class="string optional col-sm-2 control-label"
								for="user_address_zip">Zip</label>
							<div class="col-sm-10">
								<input class="string optional form-control"
									id="user_address_zip" name="user[address_zip]" value="<%=user.getZip() != null ? user.getZip() : ""       %>"
									type="text">
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group country optional user_address_country">
							<label class="country optional col-sm-2 control-label"
								for="user_address_country">Country</label>
							<div class="col-sm-10">
								<select class="form-control"
									 id="user_address_country"
									name="user[address_country]" style="width: 100%;" tabindex="-1"
									><option value="US">United
										States</option>
									<option value="CA">Canada</option>
									<option value="GB">United Kingdom</option>
									<option value="AU">Australia</option>
									<option value="AF">Afghanistan</option>
									<option value="AX">Åland Islands</option>
									<option value="AL">Albania</option>
									<option value="DZ">Algeria</option>
									<option value="AS">American Samoa</option>
									<option value="AD">Andorra</option>
									<option value="AO">Angola</option>
									<option value="AI">Anguilla</option>
									<option value="AQ">Antarctica</option>
									<option value="AG">Antigua and Barbuda</option>
									<option value="AR">Argentina</option>
									<option value="AM">Armenia</option>
									<option value="AW">Aruba</option>
									<option value="AU">Australia</option>
									<option value="AT">Austria</option>
									<option value="AZ">Azerbaijan</option>
									<option value="BS">Bahamas</option>
									<option value="BH">Bahrain</option>
									<option value="BD">Bangladesh</option>
									<option value="BB">Barbados</option>
									<option value="BY">Belarus</option>
									<option value="BE">Belgium</option>
									<option value="BZ">Belize</option>
									<option value="BJ">Benin</option>
									<option value="BM">Bermuda</option>
									<option value="BT">Bhutan</option>
									<option value="BO">Bolivia, Plurinational State of</option>
									<option value="BQ">Bonaire, Sint Eustatius and Saba</option>
									<option value="BA">Bosnia and Herzegovina</option>
									<option value="BW">Botswana</option>
									<option value="BV">Bouvet Island</option>
									<option value="BR">Brazil</option>
									<option value="IO">British Indian Ocean Territory</option>
									<option value="BN">Brunei Darussalam</option>
									<option value="BG">Bulgaria</option>
									<option value="BF">Burkina Faso</option>
									<option value="BI">Burundi</option>
									<option value="KH">Cambodia</option>
									<option value="CM">Cameroon</option>
									<option value="CA">Canada</option>
									<option value="CV">Cape Verde</option>
									<option value="KY">Cayman Islands</option>
									<option value="CF">Central African Republic</option>
									<option value="TD">Chad</option>
									<option value="CL">Chile</option>
									<option value="CN">China</option>
									<option value="CX">Christmas Island</option>
									<option value="CC">Cocos (Keeling) Islands</option>
									<option value="CO">Colombia</option>
									<option value="KM">Comoros</option>
									<option value="CG">Congo</option>
									<option value="CD">Congo, The Democratic Republic of
										the</option>
									<option value="CK">Cook Islands</option>
									<option value="CR">Costa Rica</option>
									<option value="CI">Côte d'Ivoire</option>
									<option value="HR">Croatia</option>
									<option value="CU">Cuba</option>
									<option value="CW">Curaçao</option>
									<option value="CY">Cyprus</option>
									<option value="CZ">Czech Republic</option>
									<option value="DK">Denmark</option>
									<option value="DJ">Djibouti</option>
									<option value="DM">Dominica</option>
									<option value="DO">Dominican Republic</option>
									<option value="EC">Ecuador</option>
									<option value="EG">Egypt</option>
									<option value="SV">El Salvador</option>
									<option value="GQ">Equatorial Guinea</option>
									<option value="ER">Eritrea</option>
									<option value="EE">Estonia</option>
									<option value="ET">Ethiopia</option>
									<option value="FK">Falkland Islands (Malvinas)</option>
									<option value="FO">Faroe Islands</option>
									<option value="FJ">Fiji</option>
									<option value="FI">Finland</option>
									<option value="FR">France</option>
									<option value="GF">French Guiana</option>
									<option value="PF">French Polynesia</option>
									<option value="TF">French Southern Territories</option>
									<option value="GA">Gabon</option>
									<option value="GM">Gambia</option>
									<option value="GE">Georgia</option>
									<option value="DE">Germany</option>
									<option value="GH">Ghana</option>
									<option value="GI">Gibraltar</option>
									<option value="GR">Greece</option>
									<option value="GL">Greenland</option>
									<option value="GD">Grenada</option>
									<option value="GP">Guadeloupe</option>
									<option value="GU">Guam</option>
									<option value="GT">Guatemala</option>
									<option value="GG">Guernsey</option>
									<option value="GN">Guinea</option>
									<option value="GW">Guinea-Bissau</option>
									<option value="GY">Guyana</option>
									<option value="HT">Haiti</option>
									<option value="HM">Heard Island and McDonald Islands</option>
									<option value="VA">Holy See (Vatican City State)</option>
									<option value="HN">Honduras</option>
									<option value="HK">Hong Kong</option>
									<option value="HU">Hungary</option>
									<option value="IS">Iceland</option>
									<option selected="selected" value="IN">India</option>
									<option value="ID">Indonesia</option>
									<option value="IR">Iran, Islamic Republic of</option>
									<option value="IQ">Iraq</option>
									<option value="IE">Ireland</option>
									<option value="IM">Isle of Man</option>
									<option value="IL">Israel</option>
									<option value="IT">Italy</option>
									<option value="JM">Jamaica</option>
									<option value="JP">Japan</option>
									<option value="JE">Jersey</option>
									<option value="JO">Jordan</option>
									<option value="KZ">Kazakhstan</option>
									<option value="KE">Kenya</option>
									<option value="KI">Kiribati</option>
									<option value="KP">Korea, Democratic People's Republic
										of</option>
									<option value="KR">Korea, Republic of</option>
									<option value="KW">Kuwait</option>
									<option value="KG">Kyrgyzstan</option>
									<option value="LA">Lao People's Democratic Republic</option>
									<option value="LV">Latvia</option>
									<option value="LB">Lebanon</option>
									<option value="LS">Lesotho</option>
									<option value="LR">Liberia</option>
									<option value="LY">Libya</option>
									<option value="LI">Liechtenstein</option>
									<option value="LT">Lithuania</option>
									<option value="LU">Luxembourg</option>
									<option value="MO">Macao</option>
									<option value="MK">Macedonia, Republic of</option>
									<option value="MG">Madagascar</option>
									<option value="MW">Malawi</option>
									<option value="MY">Malaysia</option>
									<option value="MV">Maldives</option>
									<option value="ML">Mali</option>
									<option value="MT">Malta</option>
									<option value="MH">Marshall Islands</option>
									<option value="MQ">Martinique</option>
									<option value="MR">Mauritania</option>
									<option value="MU">Mauritius</option>
									<option value="YT">Mayotte</option>
									<option value="MX">Mexico</option>
									<option value="FM">Micronesia, Federated States of</option>
									<option value="MD">Moldova, Republic of</option>
									<option value="MC">Monaco</option>
									<option value="MN">Mongolia</option>
									<option value="ME">Montenegro</option>
									<option value="MS">Montserrat</option>
									<option value="MA">Morocco</option>
									<option value="MZ">Mozambique</option>
									<option value="MM">Myanmar</option>
									<option value="NA">Namibia</option>
									<option value="NR">Nauru</option>
									<option value="NP">Nepal</option>
									<option value="AN">Netherlands Antilles</option>
									<option value="NL">Netherlands</option>
									<option value="NC">New Caledonia</option>
									<option value="NZ">New Zealand</option>
									<option value="NI">Nicaragua</option>
									<option value="NE">Niger</option>
									<option value="NG">Nigeria</option>
									<option value="NU">Niue</option>
									<option value="NF">Norfolk Island</option>
									<option value="MP">Northern Mariana Islands</option>
									<option value="NO">Norway</option>
									<option value="OM">Oman</option>
									<option value="PK">Pakistan</option>
									<option value="PW">Palau</option>
									<option value="PS">Palestine, State of</option>
									<option value="PA">Panama</option>
									<option value="PG">Papua New Guinea</option>
									<option value="PY">Paraguay</option>
									<option value="PE">Peru</option>
									<option value="PH">Philippines</option>
									<option value="PN">Pitcairn</option>
									<option value="PL">Poland</option>
									<option value="PT">Portugal</option>
									<option value="PR">Puerto Rico</option>
									<option value="QA">Qatar</option>
									<option value="RE">Réunion</option>
									<option value="RO">Romania</option>
									<option value="RU">Russian Federation</option>
									<option value="RW">Rwanda</option>
									<option value="BL">Saint Barthélemy</option>
									<option value="SH">Saint Helena, Ascension and Tristan
										da Cunha</option>
									<option value="KN">Saint Kitts and Nevis</option>
									<option value="LC">Saint Lucia</option>
									<option value="MF">Saint Martin (French part)</option>
									<option value="PM">Saint Pierre and Miquelon</option>
									<option value="VC">Saint Vincent and the Grenadines</option>
									<option value="WS">Samoa</option>
									<option value="SM">San Marino</option>
									<option value="ST">Sao Tome and Principe</option>
									<option value="SA">Saudi Arabia</option>
									<option value="SN">Senegal</option>
									<option value="RS">Serbia</option>
									<option value="SC">Seychelles</option>
									<option value="SL">Sierra Leone</option>
									<option value="SG">Singapore</option>
									<option value="SX">Sint Maarten (Dutch part)</option>
									<option value="SK">Slovakia</option>
									<option value="SI">Slovenia</option>
									<option value="SB">Solomon Islands</option>
									<option value="SO">Somalia</option>
									<option value="ZA">South Africa</option>
									<option value="GS">South Georgia and the South
										Sandwich Islands</option>
									<option value="SS">South Sudan</option>
									<option value="ES">Spain</option>
									<option value="LK">Sri Lanka</option>
									<option value="SD">Sudan</option>
									<option value="SR">Suriname</option>
									<option value="SJ">Svalbard and Jan Mayen</option>
									<option value="SZ">Swaziland</option>
									<option value="SE">Sweden</option>
									<option value="CH">Switzerland</option>
									<option value="SY">Syrian Arab Republic</option>
									<option value="TW">Taiwan</option>
									<option value="TJ">Tajikistan</option>
									<option value="TZ">Tanzania, United Republic of</option>
									<option value="TH">Thailand</option>
									<option value="TL">Timor-Leste</option>
									<option value="TG">Togo</option>
									<option value="TK">Tokelau</option>
									<option value="TO">Tonga</option>
									<option value="TT">Trinidad and Tobago</option>
									<option value="TN">Tunisia</option>
									<option value="TR">Turkey</option>
									<option value="TM">Turkmenistan</option>
									<option value="TC">Turks and Caicos Islands</option>
									<option value="TV">Tuvalu</option>
									<option value="UG">Uganda</option>
									<option value="UA">Ukraine</option>
									<option value="AE">United Arab Emirates</option>
									<option value="GB">United Kingdom</option>
									<option value="UM">United States Minor Outlying
										Islands</option>
									<option value="US">United States</option>
									<option value="UY">Uruguay</option>
									<option value="UZ">Uzbekistan</option>
									<option value="VU">Vanuatu</option>
									<option value="VE">Venezuela, Bolivarian Republic of</option>
									<option value="VN">Viet Nam</option>
									<option value="VG">Virgin Islands, British</option>
									<option value="VI">Virgin Islands, U.S.</option>
									<option value="WF">Wallis and Futuna</option>
									<option value="EH">Western Sahara</option>
									<option value="YE">Yemen</option>
									<option value="ZM">Zambia</option>
									<option value="ZW">Zimbabwe</option></select>
									
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
	
	<input class="btn btn-success btn-xl" name="commit" value="Update" type="submit" style="float:right;">
</form>
    
</body>
</html>



