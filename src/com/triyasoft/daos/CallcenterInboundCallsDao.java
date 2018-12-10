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

import com.triyasoft.model.CallModel;
import com.triyasoft.model.CallcenterInboundCall;
import com.triyasoft.utils.ProjectUtils;

public class CallcenterInboundCallsDao {

	

	public static void createCall(CallcenterInboundCall callcenterInboundCall) {
		


		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();
	     
			        try
		       		{
			        	
					
			           
		            stmt = conn.prepareStatement("INSERT INTO callcenterinboundcalls ( uuid ,tosipendpoint,callername,"
		            		+ "isrunning, callstarttime, callednumber,customernumber)  "
		            		+ "VALUES (?,?,?,?,?,?,?)");
		            int counter =1;
		            
		            
		            
		            
		            stmt.setString(counter++, callcenterInboundCall.getUuid());
		            stmt.setString(counter++, callcenterInboundCall.getTosipendpoint());
		            stmt.setString(counter++, callcenterInboundCall.getCallerName());
		            stmt.setInt(counter++, callcenterInboundCall.getIsrunning());
		            stmt.setTimestamp(counter++, new Timestamp(callcenterInboundCall.getCallstarttime().getTime()));
		            stmt.setString(counter++, callcenterInboundCall.getCallednumber());
		            stmt.setString(counter++, callcenterInboundCall.getCustomernumber());

		            stmt.executeUpdate();

		       		}
			        
		        catch(SQLException se)
		         {
		            
		            se.printStackTrace();
		         }

		         catch(Exception e){
		           
		        	 
		            e.printStackTrace();
		         }
			        
			        finally{
			  
						
			        		ProjectUtils.closeConnection(null, stmt, conn);
					
			        
			        }
				
		
	
	
		
	}

	public static void updateInboundCall(String totalCost, String billDuration,
			String billRate, String duration, String hangupCause,
			String callStatusAtHangup, String uuid) {


		

		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callcenterinboundcalls set totalbill = ?, billduration = ?, billrate = ?, callduration = ?, hangupcause = ?, "
			        			+ " callstatusathangup = ? ,  callendtime = ? , isrunning = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter = 1;
			            
			            if(totalCost == null || totalCost.trim().length() == 0)
			            	totalCost = "0";
			            stmt.setDouble(counter++, Double.parseDouble(totalCost));
			            
			            if(billDuration == null || billDuration.trim().length() == 0)
			            	billDuration = "0";
			            stmt.setInt(counter++, Integer.parseInt(billDuration));
			            
			            if(billRate == null || billRate.trim().length() == 0)
			            	billRate = "0";
			            stmt.setDouble(counter++, Double.parseDouble(billRate));

			            if(duration == null || duration.trim().length() == 0)
			            	duration = "0";
			            stmt.setInt(counter++, Integer.parseInt(duration));
			            
			            
			            stmt.setString(counter++, hangupCause);
			            stmt.setString(counter++, callStatusAtHangup);
			            stmt.setTimestamp(counter++, new Timestamp(new Date().getTime()));
			            stmt.setInt(counter++, 0);
			            stmt.setString(counter++, uuid);


			            
			            stmt.executeUpdate();
			            
		       		}
			        
			        catch(SQLException se)
			         {
			            se.printStackTrace();
			         }

			         catch(Exception e){

			        	 e.printStackTrace();
			         }
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
		
	
		
	}

	public static List<CallcenterInboundCall> loadRunningInboundCalls() {


		


		
		
		List<CallcenterInboundCall>  inboundCalls = new ArrayList<CallcenterInboundCall>();
		
		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       String query =  " select * from callcenterinboundcalls where isrunning = '1' ; ";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {
					
					CallcenterInboundCall callcenterInboundCall  = new CallcenterInboundCall();
					callcenterInboundCall.setUuid(rs.getString("uuid"));
					callcenterInboundCall.setTosipendpoint(rs.getString("tosipendpoint"));
					callcenterInboundCall.setCallerName(rs.getString("callername"));
					callcenterInboundCall.setIsrunning(rs.getInt("isrunning"));
					callcenterInboundCall.setCallstarttime(rs.getTimestamp("callstarttime"));
					callcenterInboundCall.setCallednumber(rs.getString("callednumber"));
					callcenterInboundCall.setCustomernumber(rs.getString("customernumber"));
					
					
					inboundCalls.add(callcenterInboundCall);
					
					
					
					
		
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
		        	ProjectUtils.closeConnection(rs,stmt, connection);
		        }
		  
			
			  
			return inboundCalls;
		  
	
	
	
	}

	

	
	
}
