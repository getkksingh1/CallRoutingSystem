package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.triyasoft.model.Buyer;
import com.triyasoft.model.UserModel;
import com.triyasoft.utils.ProjectUtils;

public class UsersDao {

	
	public static void main(String[] args) {
		
	
		
		
	}
	
	
	public static List<UserModel> getAllUsers() {
		

		List<UserModel> users = new ArrayList<UserModel>();
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from users  ;");
				
				while(rs.next()) {
					
					UserModel userModel = new UserModel();

					userModel.setUserid(rs.getString("userid"));
					userModel.setEmailid(rs.getString("emailid"));
					userModel.setFirstname(rs.getString("firstname"));
					userModel.setLastname(rs.getString("lastname"));
					userModel.setPassword(rs.getString("password"));
					userModel.setJssessionid(rs.getString("jssessionid"));
					userModel.setUserroleid(rs.getInt("userroleid"));
					String accountIds = rs.getString("account_id");
					if(accountIds == null || accountIds.trim().length() == 0){
						int[] accounts = {0};
						userModel.setAccount_id(accounts);
					}
					else {
						String[] accounts = accountIds.split(",");
						int[] accountsArr = new int[accounts.length];
						for(int i = 0 ; i <accounts.length; i++)
							accountsArr[i] = Integer.parseInt(accounts[i].trim()); 
						
						userModel.setAccount_id(accountsArr);
					}
					
					String additional_context = rs.getString("additional_context");
					
					if(additional_context == null)
						additional_context = "";
					
					userModel.setAdditional_context(additional_context);
					
					users.add(userModel);
					
					
		
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
		return users;
	
		
	}
	
	
	
	public static List<UserModel> getAllUsersBySchema(String schema) {
		

		List<UserModel> users = new ArrayList<UserModel>();
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from      `"+schema+"`.users  ;");
				
				while(rs.next()) {
					
					UserModel userModel = new UserModel();

					userModel.setUserid(rs.getString("userid"));
					userModel.setEmailid(rs.getString("emailid"));
					userModel.setFirstname(rs.getString("firstname"));
					userModel.setLastname(rs.getString("lastname"));
					userModel.setPassword(rs.getString("password"));
					userModel.setJssessionid(rs.getString("jssessionid"));
					userModel.setUserroleid(rs.getInt("userroleid"));
					String accountIds = rs.getString("account_id");
					if(accountIds == null || accountIds.trim().length() == 0){
						int[] accounts = {0};
						userModel.setAccount_id(accounts);
					}
					else {
						String[] accounts = accountIds.split(",");
						int[] accountsArr = new int[accounts.length];
						for(int i = 0 ; i <accounts.length; i++)
							accountsArr[i] = Integer.parseInt(accounts[i].trim()); 
						
						userModel.setAccount_id(accountsArr);
					}
					
					String additional_context = rs.getString("additional_context");
					
					if(additional_context == null)
						additional_context = "";
					
					userModel.setAdditional_context(additional_context);
					
					users.add(userModel);
					
					
		
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
		return users;
	
		
	}
	
	
	public static UserModel fetchUser(String userid) {
		
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();
		List<Buyer> cachedBuyersList = new ArrayList<Buyer>() ;
		UserModel userModel = new UserModel();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from users where userid='"+userid+"';");
				
				while(rs.next()) {
					
					
					userModel.setUserid(userid);
					userModel.setEmailid(rs.getString("emailid"));
					userModel.setFirstname(rs.getString("firstname"));
					userModel.setLastname(rs.getString("lastname"));
					userModel.setPassword(rs.getString("password"));
					userModel.setJssessionid(rs.getString("jssessionid"));
					userModel.setUserroleid(rs.getInt("userroleid"));
					
					String accountIds = rs.getString("account_id");
					if(accountIds == null || accountIds.trim().length() == 0){
						int[] accounts = {0};
						userModel.setAccount_id(accounts);
					}
					else {
						String[] accounts = accountIds.split(",");
						int[] accountsArr = new int[accounts.length];
						for(int i = 0 ; i <accounts.length; i++)
							accountsArr[i] = Integer.parseInt(accounts[i].trim()); 
						
						userModel.setAccount_id(accountsArr);
					}
					
					
					String additional_context = rs.getString("additional_context");
					
					if(additional_context == null)
						additional_context = "";
					
					userModel.setAdditional_context(additional_context);
					
					
		
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
		return userModel;
	}

	public static void updateJessionCookie(String userId, String jssessionid) {

		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE users SET jssessionid = ?  WHERE userid = ?");
        
         int counter =1;
         
         stmt.setString(counter++, jssessionid);
         stmt.setString(counter++, userId);
    

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


	public static void addUser(String email, String fname, String lname,String password, String roleid, String  account_id) {

		
		String sqlQuery = "insert into users (userid,firstname,lastname,emailid,userroleid,password,account_id,additional_context)  "
				+ " VALUES (?,?,?,?,?,?,?,?)";
		
		
		

		

		


		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			        	
			           	
			 	       
		            stmt = conn.prepareStatement(sqlQuery);
		            int counter =1;
		            
		            stmt.setString(counter++,email);
		            stmt.setString(counter++, fname);
		            stmt.setString(counter++, lname);
		            stmt.setString(counter++, email);
		            stmt.setInt(counter++, Integer.parseInt(roleid.trim()));
		            stmt.setString(counter++, password);
		            stmt.setString(counter++, account_id);
		            
		            String additional_context = "";
		            
		            if("2".equals(roleid.trim()))
		            	additional_context = "/source";
		            
		            stmt.setString(counter++, additional_context);
		            




		           


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

	
	public static UserModel getUserDetails(String userid) {

		
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();
		UserModel userModel = new UserModel();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from users where userid='"+userid+"'  ;");
				
				while(rs.next()) {
					
					
					userModel.setUserid(rs.getString("userid"));
					userModel.setEmailid(rs.getString("emailid"));
					userModel.setFirstname(rs.getString("firstname"));
					userModel.setLastname(rs.getString("lastname"));
					userModel.setPassword(rs.getString("password"));
					userModel.setJssessionid(rs.getString("jssessionid"));
					userModel.setUserroleid(rs.getInt("userroleid"));
					userModel.setPassword(rs.getString("password"));
					userModel.setTimezone(rs.getString("timezone"));
					userModel.setPhonenumber(rs.getString("phonenumber"));
					userModel.setSkype(rs.getString("skype"));
					userModel.setAddressline1(rs.getString("addressline1"));
					userModel.setAddressline2(rs.getString("addressline2"));
					userModel.setCity(rs.getString("city"));
					userModel.setState(rs.getString("state"));
					userModel.setCountry(rs.getString("country"));
					userModel.setZip(rs.getString("zip"));

					
					

					String accountIds = rs.getString("account_id");
					if(accountIds == null || accountIds.trim().length() == 0){
						int[] accounts = {0};
						userModel.setAccount_id(accounts);
					}
					else {
						String[] accounts = accountIds.split(",");
						int[] accountsArr = new int[accounts.length];
						for(int i = 0 ; i <accounts.length; i++)
							accountsArr[i] = Integer.parseInt(accounts[i].trim()); 
						
						userModel.setAccount_id(accountsArr);
					}
					
					String additional_context = rs.getString("additional_context");
					
					if(additional_context == null)
						additional_context = "";
					
					userModel.setAdditional_context(additional_context);
					
					
		
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
		return userModel;
	
	}
	

	public static UserModel find(String userid, String password) {
		
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();
		UserModel userModel = new UserModel();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from users where userid='"+userid+"'  and password = '"+password+"'   ;");
				
				while(rs.next()) {
					
					
					userModel.setUserid(rs.getString("userid"));
					userModel.setEmailid(rs.getString("emailid"));
					userModel.setFirstname(rs.getString("firstname"));
					userModel.setLastname(rs.getString("lastname"));
					userModel.setPassword(rs.getString("password"));
					userModel.setJssessionid(rs.getString("jssessionid"));
					userModel.setUserroleid(rs.getInt("userroleid"));
					userModel.setPassword(password);
					userModel.setTimezone(rs.getString("timezone"));
					userModel.setPhonenumber(rs.getString("phonenumber"));
					userModel.setSkype(rs.getString("skype"));
					userModel.setAddressline1(rs.getString("addressline1"));
					userModel.setAddressline2(rs.getString("addressline2"));
					userModel.setCity(rs.getString("city"));
					userModel.setState(rs.getString("state"));
					userModel.setCountry(rs.getString("country"));
					userModel.setZip(rs.getString("zip"));

					
					

					String accountIds = rs.getString("account_id");
					if(accountIds == null || accountIds.trim().length() == 0){
						int[] accounts = {0};
						userModel.setAccount_id(accounts);
					}
					else {
						String[] accounts = accountIds.split(",");
						int[] accountsArr = new int[accounts.length];
						for(int i = 0 ; i <accounts.length; i++)
							accountsArr[i] = Integer.parseInt(accounts[i].trim()); 
						
						userModel.setAccount_id(accountsArr);
					}
					
					String additional_context = rs.getString("additional_context");
					
					if(additional_context == null)
						additional_context = "";
					
					userModel.setAdditional_context(additional_context);
					
					
		
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
		return userModel;
	}


	public static void save(String uuid, UserModel user) {

		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("insert into usersessions (uuid,userid) values (?,?) ");
        
         int counter =1;
         
         stmt.setString(counter++, uuid);
         stmt.setString(counter++, user.getUserid());
    

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


	public static void deleteSession(UserModel user) {

		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("delete from  usersessions   WHERE userid = ?");
        
         int counter =1;
         

         stmt.setString(counter++, user.getUserid());
    

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


	public static UserModel find(String uuid) {
		
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection conn = ProjectUtils.getMySQLConnection();
		String userid = "";

		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from usersessions where uuid='"+uuid+"';");
				 System.out.println("select * from usersessions where uuid='"+uuid+"';");
				
				while(rs.next()) {
					
					 userid = rs.getString("userid");
					
					
		
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
		
		  
		  
		  
		  return fetchUser(userid);
	}


	public static void updateUser(boolean changePassword, String userId, String password,
			String password_confirmation, String current_password,
			String time_zone, String first_name, String last_name,
			String phone_1, String skype_name, String address_line1,
			String address_line2, String address_city, String address_state,
			String address_country, String address_zip) {
		
		
		


		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
		 String changePasswordClause = "   password = ?  , ";	 
		 String query = "";
	     
		 if(!changePassword)
			 query = "UPDATE users SET firstname= ?, lastname= ?,  timezone = ?, phonenumber = ? , skype = ?, addressline1 = ?, addressline2 = ?, city = ?, state = ?, country = ?, zip = ?  WHERE userid = ?";  
		 else
			 query = "UPDATE users SET firstname= ?, lastname= ?, "+changePasswordClause+" timezone = ?, phonenumber = ? , skype = ?, addressline1 = ?, addressline2 = ?, city = ?, state = ?, country = ?, zip = ?  WHERE userid = ?";
 
		 
		 System.out.println(query);
		 stmt = conn.prepareStatement(query);
        
         int counter =1;
         
         stmt.setString(counter++, first_name);
         stmt.setString(counter++, last_name);
         
         if(changePassword)
             stmt.setString(counter++, password);
         
         stmt.setString(counter++, time_zone);
         stmt.setString(counter++, phone_1);
         stmt.setString(counter++, skype_name);
         stmt.setString(counter++, address_line1);
         stmt.setString(counter++, address_line2);
         stmt.setString(counter++, address_city);
         stmt.setString(counter++, address_state);
         stmt.setString(counter++, address_country);
         stmt.setString(counter++, address_zip);
         stmt.setString(counter++, userId);


       

    

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


	public static void deleteUser(String email) {


		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("delete from  users   WHERE userid = ?");
        
         int counter =1;
         

         stmt.setString(counter++, email);
    

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
