package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.triyasoft.model.PhoneNumber;
import com.triyasoft.model.TrafficSource;
import com.triyasoft.utils.ProjectUtils;

public class PhoneNumberDaoService {

	
	public static List<PhoneNumber> loadAllActiveNumbers() {
		
		

		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>() ;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from phonenumbers where is_active=1; ");
				
				while(rs.next()) {
					
					PhoneNumber phoneNumber = new PhoneNumber();
					Integer id = rs.getInt("id");
					phoneNumber.setId(id);
					phoneNumber.setNumber(rs.getString("number"));
					phoneNumber.setTraffic_source_id(rs.getInt("traffic_source_id"));
					phoneNumber.setIs_Active(rs.getInt("is_active"));
					phoneNumber.setCostpercall(rs.getDouble("costpercall"));
					phoneNumbers.add(phoneNumber);
					
					
		
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
		return phoneNumbers;
	
	
		
	}
	
	public static List<PhoneNumber> loadAllNumbers() {
		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>() ;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from phonenumbers order by number; ");
				
				while(rs.next()) {
					
					PhoneNumber phoneNumber = new PhoneNumber();
					Integer id = rs.getInt("id");
					phoneNumber.setId(id);
					phoneNumber.setNumber(rs.getString("number"));
					phoneNumber.setTraffic_source_id(rs.getInt("traffic_source_id"));
					phoneNumber.setIs_Active(rs.getInt("is_active"));
					phoneNumber.setCostpercall(rs.getDouble("costpercall"));
					phoneNumbers.add(phoneNumber);
					
					
		
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
		return phoneNumbers;
	
	}

	
	
	public static PhoneNumber getPhoneNumberbyCallerId(String number) {
		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		PhoneNumber phoneNumber = new PhoneNumber();
	    Connection conn = ProjectUtils.getMySQLConnection();



		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from phonenumbers where number = '"+number+"' ;");
				
				if(rs.next()) {
					
					Integer id = rs.getInt("id");
					phoneNumber.setId(id);
					phoneNumber.setNumber(rs.getString("number"));
					phoneNumber.setTraffic_source_id(rs.getInt("traffic_source_id"));
					phoneNumber.setIs_Active(rs.getInt("is_active"));
					phoneNumber.setCostpercall(rs.getDouble("costpercall"));
					
					
		
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
					
					
					
				
		        	
		        
		return phoneNumber;
	
	}
	
	
	public static void deleteNummber(String id) {


		Statement stmt = null;
		ResultSet rs =  null;
	       Connection connection = ProjectUtils.getMySQLConnection();

		try
		{
	
	       stmt = connection.createStatement();
	       String sql = "DELETE FROM phonenumbers  " +
                   "WHERE id = '"+id+"';";
      stmt.executeUpdate(sql);

		
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
        	ProjectUtils.closeConnection(rs,stmt,connection);
        }
	
		
	
		
	}
	
	

	public static PhoneNumber addPhoneNumber(String number,
			String traffic_source_id, String is_active,  String costpercall) {

		

		
    	PhoneNumber phoneNumber = new PhoneNumber();


		   
		   PreparedStatement stmt = null;
 	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			        	phoneNumber.setNumber(number.trim().replace("+", "").replaceAll("-", "").replaceAll(" ", ""));
			        	phoneNumber.setTraffic_source_id(Integer.parseInt(traffic_source_id));
			        	phoneNumber.setIs_Active(Integer.parseInt(is_active));
			        	phoneNumber.setCostpercall(Double.parseDouble(costpercall));

			           	
			 	       
		            stmt = conn.prepareStatement("INSERT INTO phonenumbers ( number,traffic_source_id,is_active,costpercall)  VALUES (?,?,?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++, phoneNumber.getNumber());
		            stmt.setInt(counter++, phoneNumber.getTraffic_source_id());
			        stmt.setInt(counter++, phoneNumber.getIs_Active());
			        stmt.setDouble(counter++, phoneNumber.getCostpercall());
			        
			        //					phoneNumber.setCostpercall(rs.getDouble("costpercall"));



   	            int sourceId =    stmt.executeUpdate();
   	         phoneNumber.setId(sourceId);

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
			        
			       
				
		
			        return phoneNumber;
	
		
		
	
	
	}

	public static void updateNumber(String id, String number,
			String traffic_source_id, String is_active, String costpercall) {
		

		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        
			 PhoneNumber phoneNumber = new PhoneNumber();
			 
			    phoneNumber.setId(Integer.parseInt(id));
				phoneNumber.setNumber(number);
	        	phoneNumber.setTraffic_source_id(Integer.parseInt(traffic_source_id));
	        	phoneNumber.setIs_Active(Integer.parseInt(is_active));
	        	phoneNumber.setCostpercall(Double.parseDouble(costpercall));
	           	
			 

	           	
         stmt = conn.prepareStatement("UPDATE phonenumbers SET number = ? , traffic_source_id = ? , is_active = ? , costpercall = ? WHERE id = ?");
         

        
         int counter =1;
         
         stmt.setString(counter++, phoneNumber.getNumber());
         stmt.setInt(counter++, phoneNumber.getTraffic_source_id());
         stmt.setInt(counter++, phoneNumber.getIs_Active());
         stmt.setDouble(counter++, phoneNumber.getCostpercall());
         stmt.setInt(counter++, phoneNumber.getId());


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

	public static List<PhoneNumber> loadAllNumbers(String sortingParam) {
		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>() ;
	    Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
			  if("undefined".equals(sortingParam))
				  return loadAllNumbers();
			  
			  sortingParam = sortingParam.replace("%20", "  ");
			  
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from phonenumbers  order by "+sortingParam+" ; ");
				
				while(rs.next()) {
					
					PhoneNumber phoneNumber = new PhoneNumber();
					Integer id = rs.getInt("id");
					phoneNumber.setId(id);
					phoneNumber.setNumber(rs.getString("number"));
					phoneNumber.setTraffic_source_id(rs.getInt("traffic_source_id"));
					phoneNumber.setIs_Active(rs.getInt("is_active"));
					phoneNumber.setCostpercall(rs.getDouble("costpercall"));
					phoneNumbers.add(phoneNumber);
					
					
		
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
		return phoneNumbers;
	
	}



	public static List<PhoneNumber> loadAllNumbersByTrafficSourceId(
			String trafficsource) {

		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>() ;
	    Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
			
			  
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from phonenumbers  where  traffic_source_id='"+trafficsource+"' ; ");
				
				while(rs.next()) {
					
					PhoneNumber phoneNumber = new PhoneNumber();
					Integer id = rs.getInt("id");
					phoneNumber.setId(id);
					phoneNumber.setNumber(rs.getString("number"));
					phoneNumber.setTraffic_source_id(rs.getInt("traffic_source_id"));
					phoneNumber.setIs_Active(rs.getInt("is_active"));
					phoneNumber.setCostpercall(rs.getDouble("costpercall"));
					phoneNumbers.add(phoneNumber);
					
					
		
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
		return phoneNumbers;
	
	
	}

}
