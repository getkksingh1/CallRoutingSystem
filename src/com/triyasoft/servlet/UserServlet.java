package com.triyasoft.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.triyasoft.daos.LeadDao;
import com.triyasoft.daos.UsersDao;
import com.triyasoft.model.LeadModel;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.ProjectUtils;

@WebServlet("/user")
public class UserServlet extends HttpServlet  {
	

	///user?roleid=1&requestType=adduser
	 @Override
	    protected void service(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		 
		 String requestType =  request.getParameter("requestType");
		 
		 if("deleteuser".equals(requestType)) {
			 String email = request.getParameter("email");
			 UsersDao.deleteUser(email);
			 response.sendRedirect(ProjectUtils.getBaseURL(request)+"/users.jsp");
			 
			 
		 }
		 
		 if("adduser".equals(requestType)) {
			 String email = request.getParameter("email");
			 String fname = request.getParameter("fname");
			 String lname = request.getParameter("lname");
			 String password = request.getParameter("password");
			 String roleid = request.getParameter("roleid");
			 String[] account_ids = request.getParameterValues("account_id");
			 
			 System.out.println(email);
			 System.out.println(fname);
			 System.out.println(lname);
			 System.out.println(password);
			 System.out.println(roleid);
			 
			 String accountIds = "";
			 
			 if(account_ids !=null && account_ids.length > 0){
				 for (String account_id : account_ids) {
					 accountIds = account_id+","+accountIds;

				}
			 }
				 
			 
				 UsersDao.addUser(email, fname, lname, password, roleid, accountIds);
				 response.sendRedirect(ProjectUtils.getBaseURL(request)+"/users.jsp");
			 
		 }
	 
		 if("updateuser".equals(requestType)) {
			 		boolean changePassword =  false;
		            String email = request.getParameter("user[email]");
		            String password  = request.getParameter("user[password]");
		            String password_confirmation =  request.getParameter("user[password_confirmation]");
		            String current_password = request.getParameter("user[current_password]");
		            UserModel userModel  = (UserModel)request.getSession().getAttribute("user");
	                String  oldPass  = ((UserModel)request.getSession().getAttribute("user")).getPassword();

		            
		            if(password.equals(password_confirmation) && oldPass.equals(current_password)) {
		            	changePassword = true;
		            	userModel.setPassword(password_confirmation);
		            }
		            
		            String time_zone = request.getParameter("user[time_zone]");
		            String first_name = request.getParameter("user[first_name]");
		            String last_name = request.getParameter("user[last_name]");
		            String phone_1 = request.getParameter("user[phone_1]");
		            String skype_name = request.getParameter("user[skype_name]");
		            String address_line1 = request.getParameter("user[address_line1]");
		            String address_line2 = request.getParameter("user[address_line2]");
		            String address_city = request.getParameter("user[address_city]");
		            String address_state = request.getParameter("user[address_state]");
		            String address_country = request.getParameter("user[address_country]");
		            String address_zip = request.getParameter("user[address_zip]");
		            UsersDao.updateUser(changePassword, email,password,password_confirmation,current_password,time_zone,first_name,last_name,phone_1,skype_name,address_line1,address_line2,address_city, address_state,address_country,address_zip);
					 response.sendRedirect(ProjectUtils.getBaseURL(request)+"/profile.jsp");

		        
		 }
		 
		 if("userregistration".equals(requestType)) {
			 
			    LeadModel leadModel = new LeadModel();
	            String email = request.getParameter("registration[email]");
	            leadModel.setEmail(email);
	            
	            String companyName  = request.getParameter("registration[company_name]");
	            leadModel.setCompanyName(companyName);

	            String firstName =  request.getParameter("registration[first_name]");
	            leadModel.setFirstName(firstName);
	            String lastName = request.getParameter("registration[last_name]");
	            leadModel.setLastName(lastName);
     
	            String skype_name = request.getParameter("registration[skype_name]");
	            leadModel.setSkype_name(skype_name);
	            String phoneNumber = request.getParameter("registration[phone_1]");
	            leadModel.setPhoneNumber(phoneNumber);
	            String addressLine1 = request.getParameter("registration[address_line1]");
	            leadModel.setAddressLine1(addressLine1);
	            String addressLine2 = request.getParameter("registration[address_line2]");
	            leadModel.setAddressLine2(addressLine2);
	            String zip = request.getParameter("registration[address_zip]");
	            leadModel.setZip(zip);
	            String city = request.getParameter("registration[address_city]");
	            leadModel.setCity(city);
	            String state = request.getParameter("registration[address_state]");
	            leadModel.setState(state);
	            String country = request.getParameter("registration[address_country]");
	            leadModel.setCountry(country);
	            String request_demo = request.getParameter("registration[request_demo]");
	            leadModel.setRequest_demo(request_demo);
	            
	            System.out.println(leadModel);
	            
	            LeadDao.createLead(leadModel);
	          
	            
				response.sendRedirect("http://htpbx.net/sign-up.html?message=success");

	        
	 }
		 
		 
	 }

}
