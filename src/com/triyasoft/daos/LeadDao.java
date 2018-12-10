package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.triyasoft.model.LeadModel;
import com.triyasoft.utils.ProjectUtils;

public class LeadDao {

	
	public static void createLead(LeadModel leadModel) {
		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
			
	           	
         stmt = conn.prepareStatement("insert into leads (email,companyname,firstname,lastname,skype_name,phonenumber,addressline1,addressLine2,zip,city,state,country,request_demo) "
         		+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?) ");
        
         int counter =1;
         
        stmt.setString(counter++, leadModel.getEmail());
         stmt.setString(counter++, leadModel.getCompanyName());
         stmt.setString(counter++, leadModel.getFirstName());
         stmt.setString(counter++, leadModel.getLastName());
         stmt.setString(counter++, leadModel.getSkype_name());
         stmt.setString(counter++, leadModel.getPhoneNumber());
         stmt.setString(counter++, leadModel.getAddressLine1());
         stmt.setString(counter++, leadModel.getAddressLine2());
         stmt.setString(counter++, leadModel.getZip());
         stmt.setString(counter++, leadModel.getCity());
         stmt.setString(counter++, leadModel.getState());
         stmt.setString(counter++, leadModel.getCountry());
         stmt.setString(counter++, leadModel.getRequest_demo());

    

        stmt.executeUpdate();

    		}
	        
     catch(SQLException se)
      {
         //Handle errors for JDBC
         se.printStackTrace();
      }

      catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
      }
	        
	       
		
		 finally {
			 
			 ProjectUtils.closeConnection(null, stmt, conn);
		 }

		
		
	
		
	
		
	
	}
}
