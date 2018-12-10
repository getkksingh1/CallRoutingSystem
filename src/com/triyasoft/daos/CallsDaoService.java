package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.triyasoft.model.CallModel;
import com.triyasoft.model.OutboundCallModel;
import com.triyasoft.utils.ProjectUtils;

public class CallsDaoService {
	
	public static List<CallModel> todaysCalls = new ArrayList<CallModel>();

	public static boolean isLoaded = false;

	public static void main(String[] args) {
		String todayStr = ProjectUtils.getTodayStrInClientsTimezone("US/Eastern");
		String tomorrowStr = ProjectUtils.getTomorrowStrInClientsTimezone("US/Eastern");
		System.out.println(todayStr);
		System.out.println(tomorrowStr);
	}

	public static List<CallModel> getTodaysCalls() {
		
		
		// String yesterdayStr = 	ProjectUtils.getYesterdayStrInClientsTimezone("US/Eastern");
		
		String todayStr = ProjectUtils.getTodayStrInClientsTimezone("US/Eastern");
		String tomorrowStr = ProjectUtils.getTomorrowStrInClientsTimezone("US/Eastern");

			 
		
		todaysCalls = loadCalls( todayStr,  tomorrowStr);
		
		isLoaded = true;
		
		return todaysCalls;
	}

	
	public static List<CallModel> loadCalls(String startDate, String endDate) {
		


		
		
		List<CallModel>  calls = new ArrayList<CallModel>();
		
		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       String query =  "select * from calls where calllandingtimeonsever>  ADDTIME('"+startDate+" 00:00:00', '050000')  AND calllandingtimeonsever< ADDTIME('"+endDate+" 00:00:00', '050000') ;";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {
					
					CallModel callModel = new CallModel();
					
					Integer id =  rs.getInt("id");
					String uuid = rs.getString("uuid");
					
					callModel.setId(id);
					callModel.setUuid(uuid);
					
					callModel.setRecording_url(rs.getString("recording_url"));
					callModel.setNumber_called(rs.getString("number_called"));
					callModel.setConnected_to(rs.getString("connected_to"));
					callModel.setCaller_number(rs.getString("caller_number"));
					callModel.setTraffic_source_id(rs.getInt("traffic_source_id"));
					callModel.setTraffic_source(rs.getString("traffic_source"));
					callModel.setBuyer(rs.getString("buyer"));
					callModel.setBuyer_id(rs.getInt("buyer_id"));
					
					
					//Plivo Parameters at call start
					callModel.setCallStatusAtAnswer(rs.getString("callStatusAtAnswer"));
					callModel.setAnswerEvent(rs.getString("answerEvent"));
					callModel.setBillRate(rs.getString("billRate"));
					callModel.setXmlResponse(rs.getString("xmlResponse"));
					callModel.setDirection(rs.getString("direction"));
					callModel.setCallerName(rs.getString("callerName"));

					
					//Plivo Parameters at call hangup

					callModel.setTotalCost(rs.getString("totalCost"));
					callModel.setHangupCause(rs.getString("hangupCause"));
					callModel.setStartTime(rs.getTimestamp("startTime"));
					callModel.setEndTime(rs.getTimestamp("endTime"));
					callModel.setAnswerTime(rs.getTimestamp("answer_time"));
					callModel.setCallLandingTimeOnServer(rs.getTimestamp("calllandingtimeonsever"));


					callModel.setDuration(rs.getString("duration"));
					callModel.setHangupEvent(rs.getString("hangupEvent"));
					callModel.setCallStatusAtHangup(rs.getString("callStatusAtHangup"));
					
					callModel.setIs_running(rs.getInt("is_running"));
					
					callModel.setBuyer_revenue(rs.getDouble("buyer_revenue"));
					callModel.setTraffic_source_revenue(rs.getDouble("traffic_source_revenue"));
					
					
					callModel.setState(rs.getString("state"));
					callModel.setCity(rs.getString("city"));
					callModel.setCountry(rs.getString("country"));
					callModel.setLatitue(rs.getString("latitude"));
					callModel.setLongitude(rs.getString("longitude"));
					callModel.setPhoneProvider(rs.getString("phoneprovider"));
					
			//		callModel.setCall_buyer(cachedbuyersMap.get(callModel.getBuyer_id()));
			//		callModel.setCall_from_source(cachedTrafficsourcesMap.get(callModel.getTraffic_source_id()));
					
					
					calls.add(callModel);
					
			//		runningCallsMapByUUID.put(uuid, callModel);
					
					
					
		
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
		  
			List<CallModel> callsWithOutBlankSource = new ArrayList<CallModel>() ;

			  
			  for (CallModel callModel : calls) {
				
				  if(callModel.getTraffic_source_id() ==0 && callModel.getBuyer_id() == 0)
					  continue;
				  else
					  callsWithOutBlankSource.add(callModel);
			}
			  
			return callsWithOutBlankSource;
		  
	
	}
	
	public static int saveCallDataTrackDrive(CallModel callModel) {
		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

		   int result  = 0 ;
			     
			        try
		       		{
			        	
			           	//        setTotalCost   setTraffic_source_revenue   setBuyer_revenue   setIs_running
			        	
			        	boolean callDataExists = checkIfUUIDExists(callModel.getUuid());
			        	if(callDataExists) {
			        		deleteCall(callModel.getUuid());
			        	}
			        	
			        	boolean sameBuyerCheck =  CallsDaoService.checkIfCallAlreadyGoneToSameBuyer(callModel);
						
						if(sameBuyerCheck) {
							 callModel.setBuyer_revenue(0);
							 callModel.setTraffic_source_revenue(0);

						}
			        	
			       
		            stmt = conn.prepareStatement("INSERT INTO calls ( uuid ,number_called,caller_number,"
		            		+ "connected_to,traffic_source_id,traffic_source,buyer,buyer_id ,calllandingtimeonsever,duration,billduration,"
		            		+ "totalCost,buyer_revenue,traffic_source_revenue,is_running)  "
		            		+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++, callModel.getUuid());
		            stmt.setString(counter++, callModel.getNumber_called());
		            stmt.setString(counter++, callModel.getCaller_number());
		            stmt.setString(counter++, callModel.getConnected_to());
		            stmt.setInt(counter++, callModel.getTraffic_source_id());
		            stmt.setString(counter++, callModel.getTraffic_source());
		            stmt.setString(counter++, callModel.getBuyer());
		            stmt.setInt(counter++, callModel.getBuyer_id());
		            stmt.setTimestamp(counter++, new Timestamp(callModel.getCallLandingTimeOnServer().getTime()));
		            stmt.setString(counter++, callModel.getDuration());
		            stmt.setString(counter++, callModel.getBillDuration());
		            stmt.setString(counter++, callModel.getTotalCost());
		            stmt.setDouble(counter++, callModel.getBuyer_revenue());
		            stmt.setDouble(counter++, callModel.getTraffic_source_revenue());
		            stmt.setInt(counter++, callModel.getIs_running());




		            result = stmt.executeUpdate();

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
			  
						
			        		ProjectUtils.closeConnection(null, stmt, conn);
					
			        
			        }
					return result;
				
		
	}
	
	
	private static void deleteCall(String uuid) {
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();

		
		
		String sqlQuery  = "delete from calls where uuid='"+uuid+"'";
		
		
		
		
		  try
  		{
		
		       stmt = connection.createStatement();
		       
				
				 
			   stmt.executeUpdate(sqlQuery);
				
				
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
		
	}


	private static boolean checkIfUUIDExists(String uuid) {
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();

		
		
		String sqlQuery  = "select * from calls where uuid='"+uuid+"'";
		
		
		
		
		  try
  		{
		
		       stmt = connection.createStatement();
		       
				
			   rs = stmt.executeQuery(sqlQuery);
				 
				
				while(rs.next()) {
					
					return true;
					
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
		
		return false;
	}


	
	public static void saveCallModelV2(CallModel callModel) {
	
		

		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			           	
		            stmt = conn.prepareStatement("INSERT INTO calls ( callStatusAtAnswer,answerEvent,billRate,xmlResponse,direction,uuid,callerName,recording_url,number_called,caller_number,"
		            		+ "traffic_source_id,traffic_source,"
		            		+ "error_code,error_description,exception,calllandingtimeonsever,is_running"
		            		+ ",city,state,country,phoneprovider,latitude,longitude,auditing_logic)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		           
		            
		            int counter =1;
		            stmt.setString(counter++, callModel.getCallStatusAtAnswer());
		            stmt.setString(counter++, callModel.getAnswerEvent());
		            stmt.setString(counter++, callModel.getBillRate());
		            stmt.setString(counter++, callModel.getXmlResponse());
		            stmt.setString(counter++, callModel.getDirection());
		            stmt.setString(counter++, callModel.getUuid());
		            stmt.setString(counter++, callModel.getCallerName());
		            stmt.setString(counter++, callModel.getRecording_url());
		            stmt.setString(counter++, callModel.getNumber_called());
		            stmt.setString(counter++, callModel.getCaller_number());
		            stmt.setInt(counter++, 	  callModel.getTraffic_source_id());
		            stmt.setString(counter++, callModel.getTraffic_source());
		            stmt.setString(counter++, callModel.getError_code());
		            stmt.setString(counter++, callModel.getError_description());
		            stmt.setString(counter++, callModel.getException());
		            
		            callModel.setCallLandingTimeOnServer(new java.util.Date());
		            
		            stmt.setTimestamp(counter++, new Timestamp(callModel.getCallLandingTimeOnServer().getTime()));
		            stmt.setInt(counter++, callModel.getIs_running());
		           
		            stmt.setString(counter++, callModel.getCity());
		            stmt.setString(counter++, callModel.getState());
		            stmt.setString(counter++, callModel.getCountry());
		            stmt.setString(counter++, callModel.getPhoneProvider());
		            stmt.setString(counter++, callModel.getLatitue());
		            stmt.setString(counter++, callModel.getLongitude());
		            stmt.setString(counter++, callModel.getRoutingLogic().toString());


	

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
			        
			        finally{
			        ProjectUtils.closeConnection(null, stmt, conn);
			        }
				
		
	
		
	}
	
	public static void saveCallModel(CallModel callModel) {
		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			           	
		            stmt = conn.prepareStatement("INSERT INTO calls ( callStatusAtAnswer,answerEvent,billRate,xmlResponse,direction,uuid,callerName,recording_url,number_called,caller_number,"
		            		+ "connected_to,traffic_source_id,traffic_source,buyer,buyer_id,"
		            		+ "error_code,error_description,exception,calllandingtimeonsever,is_running"
		            		+ ",city,state,country,phoneprovider,latitude,longitude)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		           
		            
		            int counter =1;
		            stmt.setString(counter++, callModel.getCallStatusAtAnswer());
		            stmt.setString(counter++, callModel.getAnswerEvent());
		            stmt.setString(counter++, callModel.getBillRate());
		            stmt.setString(counter++, callModel.getXmlResponse());
		            stmt.setString(counter++, callModel.getDirection());
		            stmt.setString(counter++, callModel.getUuid());
		            stmt.setString(counter++, callModel.getCallerName());
		            stmt.setString(counter++, callModel.getRecording_url());
		            stmt.setString(counter++, callModel.getNumber_called());
		            stmt.setString(counter++, callModel.getCaller_number());
		            stmt.setString(counter++, callModel.getConnected_to());
		            stmt.setInt(counter++, callModel.getTraffic_source_id());
		            stmt.setString(counter++, callModel.getTraffic_source());
		            stmt.setString(counter++, callModel.getBuyer());
		            stmt.setInt(counter++, callModel.getBuyer_id());
		            stmt.setString(counter++, callModel.getError_code());
		            stmt.setString(counter++, callModel.getError_description());
		            stmt.setString(counter++, callModel.getException());
		            
		            callModel.setCallLandingTimeOnServer(new java.util.Date());
		            
		            stmt.setTimestamp(counter++, new Timestamp(callModel.getCallLandingTimeOnServer().getTime()));
		            stmt.setInt(counter++, callModel.getIs_running());
		           
		            stmt.setString(counter++, callModel.getCity());
		            stmt.setString(counter++, callModel.getState());
		            stmt.setString(counter++, callModel.getCountry());
		            stmt.setString(counter++, callModel.getPhoneProvider());
		            stmt.setString(counter++, callModel.getLatitue());
		            stmt.setString(counter++, callModel.getLongitude());

	

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
			        
			        finally{
			        ProjectUtils.closeConnection(null, stmt, conn);
			        }
				
		
	}

	public static List<Integer> getAllBuyersCalledBySource(String caller_number) {
		

		List<Integer>  buyerids = new ArrayList<Integer>();
		
		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		       Date today = new Date();
			   today.setTime(today.getTime()-86400*1000);
			   String yesterdayStr = formatter.format(today);
	       
		       String query =  "select distinct buyer_id from calls where calllandingtimeonsever > '"+yesterdayStr+" 00:00:00' and caller_number = '"+caller_number+"'; ";
				
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {
					
					buyerids.add(rs.getInt("buyer_id"));
					
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
		return buyerids;
		
	
	}


	public static boolean checkIfCallAlreadyGoneToSameBuyer(CallModel callModel) {
		

		

		
		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		       Date today = new Date();
			   today.setTime(today.getTime()-86400*1000);
			   String yesterdayStr = formatter.format(today);
	       
		       String query =  "select * from calls where calllandingtimeonsever > '"+yesterdayStr+" 00:00:00' and caller_number = '"+callModel.getCaller_number()+"' and buyer_id='"+callModel.getBuyer_id()+"' ;";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {
					
					return true;
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
		return false;
		
	
	
	}


	public static CallModel getCallByUUID(String callUUID) {
		

		


		
		
	   CallModel  callModel = null;
		
		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       String query =  "select * from calls where uuid='"+callUUID+"'  ;";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {
					
					 callModel = new CallModel();
					
					Integer id =  rs.getInt("id");
					String uuid = rs.getString("uuid");
					
					callModel.setId(id);
					callModel.setUuid(uuid);
					
					callModel.setRecording_url(rs.getString("recording_url"));
					callModel.setNumber_called(rs.getString("number_called"));
					callModel.setConnected_to(rs.getString("connected_to"));
					callModel.setCaller_number(rs.getString("caller_number"));
					callModel.setTraffic_source_id(rs.getInt("traffic_source_id"));
					callModel.setTraffic_source(rs.getString("traffic_source"));
					callModel.setBuyer(rs.getString("buyer"));
					callModel.setBuyer_id(rs.getInt("buyer_id"));
					
					
					//Plivo Parameters at call start
					callModel.setCallStatusAtAnswer(rs.getString("callStatusAtAnswer"));
					callModel.setAnswerEvent(rs.getString("answerEvent"));
					callModel.setBillRate(rs.getString("billRate"));
					callModel.setXmlResponse(rs.getString("xmlResponse"));
					callModel.setDirection(rs.getString("direction"));
					callModel.setCallerName(rs.getString("callerName"));

					
					//Plivo Parameters at call hangup

					callModel.setTotalCost(rs.getString("totalCost"));
					callModel.setHangupCause(rs.getString("hangupCause"));
					callModel.setStartTime(rs.getTimestamp("startTime"));
					callModel.setEndTime(rs.getTimestamp("endTime"));
					callModel.setAnswerTime(rs.getTimestamp("answer_time"));
					callModel.setCallLandingTimeOnServer(rs.getTimestamp("calllandingtimeonsever"));


					callModel.setDuration(rs.getString("duration"));
					callModel.setHangupEvent(rs.getString("hangupEvent"));
					callModel.setCallStatusAtHangup(rs.getString("callStatusAtHangup"));
					
					callModel.setIs_running(rs.getInt("is_running"));
					
					callModel.setBuyer_revenue(rs.getDouble("buyer_revenue"));
					callModel.setTraffic_source_revenue(rs.getDouble("traffic_source_revenue"));
					
					
					
		
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
		return callModel;
		
	
	
		
	}


	public static boolean checkIfDupCall(CallModel callModel) {
		

			
			 
			
			Statement stmt = null;
			ResultSet rs =  null;
		    Connection connection = ProjectUtils.getMySQLConnection();


			  try
	    		{
			
			       stmt = connection.createStatement();
			       
			      String yesrdayStr =  ProjectUtils.getYesterdayStrInClientsTimezone(null);
			       
			       String query =  "select * from calls where caller_number='"+callModel.getCaller_number()+"' and traffic_source_id = '"+callModel.getTraffic_source_id()+"'   and duration !='0'  and calllandingtimeonsever > '"+yesrdayStr+"' ;";
					
			       System.out.println(query);
				   rs = stmt.executeQuery(query);
					 
					
					if(rs.next()) {   
						
						return true;
						
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
			
			
		
		
			
		
		
		return false;
	}


	public static void removeDuplicateZeroSecondCallsFromCurrentUserAndNoBuyer(
			CallModel callModel) {

		//		//select *  from calls  where caller_number='14802293322' and  billduration = '0' and buyer_id = '0' and is_running = 0 and uuid !='bf419ee6-5c0c-11e7-a115-21e56fd1e4ee'

		

		if("0".equals(callModel.getDuration()) ){
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();

		
		
		String sqlQuery  =  " delete from calls  where caller_number='"+callModel.getCaller_number()+"' and  billduration = '0'  and buyer_id is null and is_running = 0 and uuid !='"+callModel.getUuid()+"'";
		
		
		
		
		  try
  		{
		
		       stmt = connection.createStatement();
		       
				
				 
			   stmt.executeUpdate(sqlQuery);
				
				
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
		
	
		}
		
		
	}
	
	
	
	public static boolean checkBuyerDup(String customer_number, int buyer_id) {

		

		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		      String yesrdayStr =  ProjectUtils.getYesterdayStrInClientsTimezone(null);
		       
		       String query =  "select * from calls where caller_number='"+customer_number+"'  and buyer_id ='"+buyer_id+"' and duration !='0' and buyer_revenue> 0  and calllandingtimeonsever > '"+yesrdayStr+"' ;";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {   
					
					return true;
					
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
		
		
	
	
		
	
	
	return false;

	}


	public static boolean checkBuerDupCall(CallModel callModel) {
		

		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		      String yesrdayStr =  ProjectUtils.getYesterdayStrInClientsTimezone(null);
		       
		       String query =  "select * from calls where caller_number='"+callModel.getCaller_number()+"'  and buyer_id ='"+callModel.getBuyer_id()+"' and duration !='0' and buyer_revenue> 0  and calllandingtimeonsever > '"+yesrdayStr+"' ;";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {   
					
					return true;
					
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
		
		
	
	
		
	
	
	return false;
}


	public static void updateRecordingURL(String uuid, String recordingUrl) {

		
	}


	public static void createOutBoundCallRinging(String calluuid,
			String parentuuid, String legBId, String conferenceID, String from,
			String to, String requestuuid, Date ringtime, String callstatus) {
		
		

		   
		   PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	
			        String sql = "INSERT INTO callsoutbound "
		            		+ "( uuid,parentuuid,legBId,conferenceID,callfrom,callto,requestuuid,calluuid,ringtime,callstatus  )"
		            		+"  VALUES (?,?,?,?,?,?,?,?,?,?)";   	
		            stmt = conn.prepareStatement(sql);
		           
		            
		            int counter =1;
		            stmt.setString(counter++, calluuid);
		            stmt.setString(counter++, parentuuid);
		            stmt.setString(counter++, legBId);
		            stmt.setString(counter++, conferenceID);
		            stmt.setString(counter++, from);
		            stmt.setString(counter++, to);
		            stmt.setString(counter++, requestuuid);
		            stmt.setString(counter++, calluuid);
		            stmt.setTimestamp(counter++, new Timestamp(ringtime.getTime()));
		            stmt.setString(counter++, callstatus);

	

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
			        
			        finally{
			        ProjectUtils.closeConnection(null, stmt, conn);
			        }
				
		
	
		
	}


	public static void updateOutBoundCallAnswer(String callUUID,
			String aLegUUID, String billRate, String aLegRequestUUID,
			String callstatus,  Date answerTime ) {
		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callsoutbound set aleguuid = ?, billrate = ?, alegrequestuuid = ?, answertime = ?, callstatus = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter =1;
			            stmt.setString(counter++, aLegUUID);
			            stmt.setDouble(counter++, Double.parseDouble(billRate));
			            stmt.setString(counter++, aLegRequestUUID);
			            stmt.setTimestamp(counter++, new Timestamp(answerTime.getTime()));
			            stmt.setString(counter++, callstatus);
			            stmt.setString(counter++, callUUID);

			            
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	}


	public static void updateOutBoundCallConferenceEnter(String callUUID,
			String conferenceFirstMember, String conferenceMemberID,
			String conferenceName, String conferenceUUID,
			Date conferencestarttime) {

		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callsoutbound set conferencefirstmember = ?, conferencememberid = ?, conferencename = ?, conferenceuuid = ?, conferencestarttime = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter =1;
			            stmt.setString(counter++, conferenceFirstMember);
			            stmt.setString(counter++, conferenceMemberID);
			            stmt.setString(counter++, conferenceName);
			            stmt.setString(counter++, conferenceUUID);
			            stmt.setTimestamp(counter++, new Timestamp(conferencestarttime.getTime()));
			            stmt.setString(counter++, callUUID);

			            
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	}


	public static void updateOutBoundCallConferenceEnd(String callUUID,
			String conferenceLastMember, Date conferenceendtime) {
		


		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callsoutbound set conferencelastmember = ?,  conferenceendtime = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter =1;
			            stmt.setString(counter++, conferenceLastMember);
			            stmt.setTimestamp(counter++, new Timestamp(conferenceendtime.getTime()));
			            stmt.setString(counter++, callUUID);

			            
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	
		
		
	}


	public static void updateOutBoundCallConferenceEnd2(String callUUID,
			Date conferenceendtime) {

		


		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callsoutbound set   conferencingendedcallbacktime = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter =1;
			            stmt.setTimestamp(counter++, new Timestamp(conferenceendtime.getTime()));
			            stmt.setString(counter++, callUUID);

			            
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	
		
		
	
		
	}


	public static void updateOutBoundCallConferenceHangUp(String callUUID,
			String totalCost, String hangupCause, String callStatus,
			String billDuration, String duration, Date hangupTime) {
		


		


		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE callsoutbound set   totalcost = ?,hangupcause = ?, billduration = ?, duration = ? , callstatus = ? , outboundcallhanguptime = ? where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter = 1;
			            
			            stmt.setDouble(counter++, Double.parseDouble(totalCost));
			            stmt.setString(counter++, hangupCause);
			            stmt.setInt(counter++, Integer.parseInt(billDuration));
			            stmt.setInt(counter++, Integer.parseInt(duration));
			            stmt.setString(counter++, callStatus);
			            stmt.setTimestamp(counter++,  new Timestamp(hangupTime.getTime()));
			            stmt.setString(counter++, callUUID);
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	
		
		
	
		
	
	}
	
	
	public static List<String> getAllBuyersWithParentUUID(String parentuuid) {

		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();
	    List<String> buyersNumber  = new ArrayList<String>();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       
		       String query =  "select callto from callsoutbound where parentuuid='"+parentuuid+"' ;" ;
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {   
					
					buyersNumber.add(rs.getString("callto"));
					
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
		
		  
		  	return buyersNumber;

	
	}


	public static void updateBuyer(CallModel callModel) {
		
			
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE calls set   connected_to = ?, buyer = ?,  buyer_id = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter = 1;
			            
			            stmt.setString(counter++, callModel.getConnected_to());
			            stmt.setString(counter++, callModel.getBuyer());
			            stmt.setInt(counter++, callModel.getBuyer_id());
			            stmt.setString(counter++, callModel.getUuid());
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	
		
	}


	public static void updateRecordingUrl(String recordUrl, String parentuuid) {
		

		
		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = "UPDATE calls set   recording_url = ?  where uuid = ? ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter = 1;
			            
			            stmt.setString(counter++, recordUrl);
			            stmt.setString(counter++, parentuuid);
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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	
		
	
	}


	public static List<OutboundCallModel>  populateBuyerSideCallDetails(CallModel callModel) {
		
		


		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();
	    List<OutboundCallModel> outboundCalls  = new ArrayList<OutboundCallModel>();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       
		       String query =  "select * from callsoutbound where parentuuid='"+callModel.getUuid()+"' ;" ;
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {   

					OutboundCallModel  outboundCallModel = new OutboundCallModel();
					outboundCallModel.setTo(rs.getString("callto"));
					outboundCallModel.setRingtime(rs.getTimestamp("ringtime"));
					outboundCallModel.setAnswertime(rs.getTimestamp("answertime"));
					outboundCallModel.setConferencestarttime(rs.getTimestamp("conferencestarttime"));
					outboundCallModel.setConferenceendtime(rs.getTimestamp("conferencingendedcallbacktime"));
					outboundCallModel.setTotalcost(rs.getDouble("totalcost"));
					outboundCallModel.setHangupcause(rs.getString("hangupcause"));
					outboundCallModel.setCallstatus(rs.getString("callstatus"));
			
					outboundCalls.add(outboundCallModel);
					
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

	
	
	

	public static  Map<String, Date>  getInboundCallsUUIDsForSanitization() {
		


		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();
	    Map<String, Date> uuidsAndLandingTime  = new HashMap<String, Date>();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		 	  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			  Date today  = new Date();
			  today.setTime(today.getTime()-24*60*60*1000);
			  String dateStr  = formatter.format(today)+" 00:00:00";
		       
		       String query =  "select uuid, calllandingtimeonsever from calls where  calllandingtimeonsever > '"+dateStr+"' and is_running = '0' and  buyers_tried   is null order by id desc limit 1000 ;" ;
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {   
					uuidsAndLandingTime.put(rs.getString("uuid"), rs.getTimestamp("calllandingtimeonsever"));
					
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
		
		  
		  	return uuidsAndLandingTime;

	
	
		
	}


	public static List<OutboundCallModel>  getOutBoundCalls(String uuid) {
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();
	    List<OutboundCallModel> outboundCalls  = new ArrayList<OutboundCallModel>();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       
		       String query =  "select * from callsoutbound where parentuuid='"+uuid+"' ;" ;
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {   

					OutboundCallModel  outboundCallModel = new OutboundCallModel();
					outboundCallModel.setTo(rs.getString("callto"));
					outboundCallModel.setRingtime(rs.getTimestamp("ringtime"));
					outboundCallModel.setAnswertime(rs.getTimestamp("answertime"));
					outboundCallModel.setConferencestarttime(rs.getTimestamp("conferencestarttime"));
					outboundCallModel.setConferenceendtime(rs.getTimestamp("conferencingendedcallbacktime"));
					outboundCallModel.setOutboundcallhanguptime(rs.getTimestamp("outboundcallhanguptime"));
					outboundCallModel.setTotalcost(rs.getDouble("totalcost"));
					outboundCallModel.setHangupcause(rs.getString("hangupcause"));
					outboundCallModel.setCallstatus(rs.getString("callstatus"));
					outboundCalls.add(outboundCallModel);
					
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


	public static void updateCallData(String parentuuid,
			int buyerConnectedTime, int buyerConnectingTime, int callAttempts,
			StringBuffer buyers_tried, StringBuffer buyer_hangup_reasons) {

		
	
			
			  PreparedStatement stmt = null;
		       Connection conn = ProjectUtils.getMySQLConnection();

				     
				        try
			       		{
				        	String sql = "UPDATE calls set callattempts = ?, connected_time = ?, buyer_connecting_time = ?, buyers_tried = ?, buyer_hangup_reason = ?  "
				        			+ "where uuid = ? ";   	
				            stmt = conn.prepareStatement(sql);
				           
				            
				            int counter =1;
				            stmt.setInt(counter++, callAttempts);
				            stmt.setInt(counter++, buyerConnectedTime);
				            stmt.setInt(counter++, buyerConnectingTime);
				            stmt.setString(counter++, buyers_tried.toString());
				            stmt.setString(counter++, buyer_hangup_reasons.toString());
				            stmt.setString(counter++, parentuuid);

				            
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
					        
					        finally{
					        ProjectUtils.closeConnection(null, stmt, conn);
					        }
						
		}


	public static boolean checkIfRiskyCaller(String fromNumber) {

		return false;
	}


	public static String getRoutingLogForCall(String uuid) {
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		       
		       String query =  "select auditing_logic from calls where uuid='"+uuid+"' ;" ;
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {
					return rs.getString("auditing_logic");
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
		
		
		
		return "";
	}


	public static List<CallModel> getNobuyersCalls() {
		

		


		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();
	    List<CallModel> calls = new ArrayList<CallModel>();

		  try
    		{
		
		       stmt = connection.createStatement();
		       
		 /*	  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			  Date today  = new Date();
			  today.setTime(today.getTime()-24*60*60*1000);
			  String dateStr  = formatter.format(today)+" 00:00:00";
		       
		       */
		       
		 	  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
			  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
			
			  
			  
			
			 
			  Date date = new Date();
		      date.setTime(date.getTime()-2*86400*1000);

		       																																						

		       String query =  "select uuid,duration,buyers_tried, number_called , caller_number  from calls where  calllandingtimeonsever > '"+formatter.format(date)+" 00:00:00'  and is_running = '0' and  buyer is null and duration > 0 ;" ;
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {   
					
					CallModel callModel = new CallModel();
					callModel.setUuid(rs.getString("uuid"));
					callModel.setDuration(rs.getString("duration"));
					callModel.setBuyers_tried(rs.getString("buyers_tried"));
					callModel.setNumber_called(rs.getString("number_called"));
					callModel.setCaller_number(rs.getString("caller_number"));
					calls.add(callModel);
					
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
		
		  
		  	return calls;

	
	
		
	
		
	}


	public static String getBuyerNumber(String uuid) {
		

		

		


		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();

		  try
    		{
		
		       stmt = connection.createStatement();
		       
		
		       
		       
		       String query =  "select callto from callsoutbound where parentuuid='"+uuid+"' order by ringtime limit 1 ; " ;
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				while(rs.next()) {
					
					return rs.getString("callto");
					
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
		
		  
		  	return null;

	
	
		
	
		
	}


	public static boolean checkTraffic_SourceDup(String customer_number,String uuid) {

		

		
		 
		
		Statement stmt = null;
		ResultSet rs =  null;
	    Connection connection = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = connection.createStatement();
		       
		      String yesrdayStr =  ProjectUtils.getYesterdayStrInClientsTimezone(null);
		       
		       String query =  "select * from calls where caller_number = '"+customer_number+"' and uuid != '"+uuid+"' and traffic_source_revenue > '0.0' ;";
				
		       System.out.println(query);
			   rs = stmt.executeQuery(query);
				 
				
				if(rs.next()) {   
					
					return true;
					
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
		
		
	
		  		return false;

	}


	public static void updateCallByUUID(int buyer_id, String buyer_name,
			double buyer_revenue, double traffic_source_revenue,
			String buyer_number, String uuid) {
		

		
		
		  PreparedStatement stmt = null;
	       Connection conn = ProjectUtils.getMySQLConnection();

			     
			        try
		       		{
			        	String sql = " update calls set buyer_id = ? , buyer = ? ,  buyer_revenue = ? , traffic_source_revenue= ? , connected_to = ?  where uuid= ? ; ";   	
			            stmt = conn.prepareStatement(sql);
			           
			            
			            int counter = 1;
			            
			            stmt.setInt(counter++, buyer_id );
			            stmt.setString(counter++, buyer_name);
			            stmt.setDouble(counter++, buyer_revenue);
			            stmt.setDouble(counter++, traffic_source_revenue);
			            stmt.setString(counter++, buyer_number);
			            stmt.setString(counter++, uuid);

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
				        
				        finally{
				        ProjectUtils.closeConnection(null, stmt, conn);
				        }
					
	
		
	
		
	
		
		
	}

		
	


}
