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

import com.triyasoft.model.CallcenterInboundCall;
import com.triyasoft.model.CallcenterOutboundCall;
import com.triyasoft.utils.ProjectUtils;

public class CallcenterOutboundCallsDao {

	public static void createCall(CallcenterOutboundCall callcenterOutboundCall) {

		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();
	     
			        try
		       		{
			        	
			           
		            stmt = conn.prepareStatement("INSERT INTO callcenteroutboundcalls ( uuid ,fromsipendpoint,tonumber,"
		            		+ "callername, callstarttime, isrunning)  "
		            		+ "VALUES (?,?,?,?,?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++, callcenterOutboundCall.getUuid());
		            stmt.setString(counter++, callcenterOutboundCall.getFromsipendpoint());
		            stmt.setString(counter++, callcenterOutboundCall.getTonumber());
		            stmt.setString(counter++, callcenterOutboundCall.getCallername());
		            stmt.setTimestamp(counter++, new Timestamp(callcenterOutboundCall.getCallstarttime().getTime()));
		            stmt.setInt(counter++, callcenterOutboundCall.getIsrunning());



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

	public static void updateOutboundCall(String totalCost,
			String billDuration, String billRate, String duration,
			String hangupCause, String callStatusAtHangup, String uuid) {
		

		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callcenteroutboundcalls set totalbill = ?, billduration = ?, billrate = ?, callduration = ?, hangupcause = ?, "
			        			+ " callstatusathangup = ?  , callendtime = ?, isrunning = ?  where uuid = ? ";   	
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

	public static List<CallcenterOutboundCall> loadRunningOutboundCalls() {
		



		


		
		
		List<CallcenterOutboundCall>  outboundCalls = new ArrayList<CallcenterOutboundCall>();
		
		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       String query =  " select * from callcenteroutboundcalls where isrunning = '1' ; ";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {
					
					CallcenterOutboundCall callcenterOutboundCall  = new CallcenterOutboundCall();
					
					callcenterOutboundCall.setFromsipendpoint(rs.getString("fromsipendpoint"));
					callcenterOutboundCall.setTonumber(rs.getString("tonumber"));
					callcenterOutboundCall.setCallername(rs.getString("callername"));
					callcenterOutboundCall.setUuid(rs.getString("uuid"));
					callcenterOutboundCall.setIsrunning(rs.getInt("isrunning"));
					callcenterOutboundCall.setCallstarttime(rs.getTimestamp("callstarttime"));
					
					
					outboundCalls.add(callcenterOutboundCall);
					
					
					
					
		
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
		  
			
			  
			return outboundCalls;
		  
	
	
	
	
		
	}

}
