package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.triyasoft.model.PhoneNumber;
import com.triyasoft.utils.ProjectUtils;

public class ClickPhonenumberMappingDao {

	public static void createClickRequest(String clickID, String phonenumeber) {



		
		   
		   PreparedStatement stmt = null;
 	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			        	

			           	
			 	       
		            stmt = conn.prepareStatement("INSERT INTO clickphonenumbermapping ( clickid,phonenumber)  VALUES (?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++, clickID);
		            stmt.setString(counter++, phonenumeber);
			         
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

	public static void releasePhoneNumber(String clickID,String phoneNumber) {
		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        
			 
	
	           	
         stmt = conn.prepareStatement("UPDATE clickphonenumbermapping SET pagevisible = ? , click_end_time = ?   where clickid=? and phonenumber = ? ;");
         

        
         int counter =1;
         
         stmt.setInt(counter++, 0);
         stmt.setTimestamp(counter++, new Timestamp(new Date().getTime()));
         stmt.setString(counter++, clickID);
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

	public static List<String>  loadAllInUserNumbers() {
		
	
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();

	    List<String> phoneNumbersList = new ArrayList<String>();

		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select distinct phonenumber from clickphonenumbermapping where pagevisible = '1' ;");
				
				while(rs.next()) {
					
					phoneNumbersList.add(rs.getString("phonenumber"));
					
			
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

		        	ProjectUtils.closeConnection(rs,stmt,conn);

					}
					
					
					
				
		        	
		        
		return phoneNumbersList;
	
	
	}

	public static String checkIfPhoneNumberExistForClick(String clickID) {
		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();
	    String number = null;
	    
		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select distinct phonenumber from clickphonenumbermapping where clickid = '"+clickID+"' and pagevisible=1 ;");
				
				if(rs.next()) {
					
					
					number = rs.getString("phonenumber");
			
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

		        	ProjectUtils.closeConnection(rs,stmt,conn);

					}
					
					
					
				
		        	
		        
		return number;
	
	
	
	}

}
