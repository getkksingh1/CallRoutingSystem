package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.triyasoft.model.PhoneNumber;
import com.triyasoft.utils.ProjectUtils;

public class SourcePhoneNumberMappingDao {

	public static String getPhoneNumberforSource(String sourceId) {
		

		
		

		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select phonenumber from sourcephonenumbermapping where sourceid='"+sourceId+"' ;");
				
				if(rs.next()) {
					
					
					return rs.getString("phonenumber");
					
					
		
				}
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
		        
		        finally{
		        	ProjectUtils.closeConnection(rs,stmt, conn);
		        }
		return null;
	
	
		
	
	}

	public static void saveSourceIdPhoneNumberMapping(String sourceid,
			String phoneNumber) {
		
		

		   
		   PreparedStatement stmt = null;
 	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			        	
			           	
			 	       
		            stmt = conn.prepareStatement("INSERT INTO sourcephonenumbermapping ( sourceid, phonenumber)  VALUES (?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++, sourceid);
		            stmt.setString(counter++, phoneNumber);


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
