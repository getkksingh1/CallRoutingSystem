package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.triyasoft.model.ContactModel;
import com.triyasoft.utils.ProjectUtils;

public class ContactDao {

	public static boolean isLoaded = false;
	
	public static List<ContactModel> blockedContacts = null;
	
	
	public static  List<ContactModel> getBlockedContacts() {
		
		if(!isLoaded) {
			blockedContacts = new ContactDao().getAllBlockedContacts();
		}
		
		isLoaded = true;
		
		return blockedContacts;
		
	}
	
	
	
	public static void addBlockedContact(String phoneNumber) {

		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			        	
			           	
			 	       
		            stmt = conn.prepareStatement("INSERT INTO contacts ( number,blocked)  VALUES (?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++,phoneNumber);
		            stmt.setInt(counter++, 1);
		           


	               stmt.executeUpdate();
	               isLoaded = false;

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
	
	public List<ContactModel> getAllBlockedContacts() {

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<ContactModel> blockedContacts = new ArrayList<ContactModel>() ;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from contacts where blocked=1;");
				
				while(rs.next()) {
					
					ContactModel contactModel = new ContactModel();
				
					contactModel.setId(rs.getInt("id"));
					contactModel.setNumber(rs.getString("number"));
					contactModel.setBloced(rs.getInt("blocked"));
					contactModel.setCreated_at(rs.getTimestamp("created_at"));
					
				
					blockedContacts.add(contactModel);
					
					
		
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
		  
		return blockedContacts;
	
	}
	
}
