package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import com.triyasoft.model.Buyer;
import com.triyasoft.utils.AdcashZoneTracker;
import com.triyasoft.utils.ProjectUtils;

public class BuyerDaoService {
	
	

	public static List<Buyer> loadAllBuyers() {
		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<Buyer> cachedBuyersList = new ArrayList<Buyer>() ;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from Buyers ; ");
				
				while(rs.next()) {
					
					Buyer buyer = new Buyer();
					Integer buyer_id = rs.getInt("buyer_id");
					buyer.setBuyer_id(buyer_id);
					buyer.setBuyer_name(rs.getString("buyer_name"));
					String buyer_number = rs.getString("buyer_number");
					buyer.setBuyer_number(buyer_number);
					buyer.setCreated_at(rs.getTimestamp("created_at"));
					buyer.setWeight(rs.getInt("weight"));
					buyer.setTier(rs.getInt("tier"));
					buyer.setConcurrency_cap_limit(rs.getInt("concurrency_cap_limit"));
					buyer.setConcurrency_cap_used(rs.getInt("concurrency_cap_used"));
					buyer.setRunning_status(rs.getInt("running_status"));
					buyer.setBid_price(rs.getDouble("bid_price"));
					buyer.setIs_active(rs.getInt("is_active"));
					buyer.setBuyer_daily_cap(rs.getInt("buyer_daily_cap"));
					buyer.setRing_timeout(rs.getInt("ring_timeout"));
					
				
					cachedBuyersList.add(buyer);
					
					
		
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
		return cachedBuyersList;
	}
	
	
public static Buyer loadBuyerByPhoneNumber(String phoneNumber) {
		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<Buyer> cachedBuyersList = new ArrayList<Buyer>() ;
		Buyer buyer = new Buyer();

	       Connection conn = ProjectUtils.getMySQLConnection();

		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from Buyers where buyer_number = '"+phoneNumber+"';");
				
				if(rs.next()) {
					
					Integer buyer_id = rs.getInt("buyer_id");
					buyer.setBuyer_id(buyer_id);
					buyer.setBuyer_name(rs.getString("buyer_name"));
					String buyer_number = rs.getString("buyer_number");
					buyer.setBuyer_number(buyer_number);
					buyer.setCreated_at(rs.getTimestamp("created_at"));
					buyer.setWeight(rs.getInt("weight"));
					buyer.setTier(rs.getInt("tier"));
					buyer.setConcurrency_cap_limit(rs.getInt("concurrency_cap_limit"));
					buyer.setConcurrency_cap_used(rs.getInt("concurrency_cap_used"));
					buyer.setRunning_status(rs.getInt("running_status"));
					buyer.setBid_price(rs.getDouble("bid_price"));
					buyer.setIs_active(rs.getInt("is_active"));
					buyer.setBuyer_daily_cap(rs.getInt("buyer_daily_cap"));
					buyer.setRing_timeout(rs.getInt("ring_timeout"));
					
				
					cachedBuyersList.add(buyer);
					
					
		
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
//		        	
		        	
					
		        		ProjectUtils.closeConnection(rs, stmt, conn);
						
						
				
		        }
		return buyer;
	}
	

	public static void deleteBuyer(String buyer_id) {
		Statement stmt = null;
		ResultSet rs =  null;
		Connection connection = ProjectUtils.getMySQLConnection();

		try
		{
	
	       stmt = connection.createStatement();
	       String sql = "DELETE FROM Buyers " +
                   "WHERE buyer_id = '"+buyer_id+"';";
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
        	ProjectUtils.closeConnection(rs,stmt, connection);
        }
	}

	public static void updateBuyer(String buyer_id, String buyer_name,
			String buyer_number, String weight, String tier,
			String concurrency_cap_limit, String bid_price, String is_active, String buyer_daily_cap, String ring_timeout) {

		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	Buyer buyer = new Buyer();
	buyer.setBuyer_id(Integer.parseInt(buyer_id));
	buyer.setBuyer_name(buyer_name);
	buyer.setBuyer_number(buyer_number);
	buyer.setWeight(Integer.parseInt(weight));
	buyer.setTier(Integer.parseInt(tier));
	buyer.setConcurrency_cap_limit(Integer.parseInt(concurrency_cap_limit));
	buyer.setBid_price(Double.parseDouble(bid_price));
	buyer.setIs_active(Integer.parseInt(is_active));
	buyer.setBuyer_daily_cap(Integer.parseInt(buyer_daily_cap));
	buyer.setRing_timeout(Integer.parseInt(ring_timeout));
	           	
         stmt = conn.prepareStatement("UPDATE Buyers SET buyer_name = ? , buyer_number = ? , weight = ? , tier = ? , concurrency_cap_limit = ? "
         		+ "  , bid_price = ? , is_active = ? , buyer_daily_cap = ? , ring_timeout = ? WHERE buyer_id = ?");
        
         int counter =1;
         
         stmt.setString(counter++, buyer.getBuyer_name());
         stmt.setString(counter++, buyer.getBuyer_number());
         stmt.setInt(counter++, buyer.getWeight());
         stmt.setInt(counter++, buyer.getTier());
         stmt.setInt(counter++, buyer.getConcurrency_cap_limit());
         stmt.setDouble(counter++, buyer.getBid_price());
         stmt.setInt(counter++, buyer.getIs_active());
         stmt.setInt(counter++, buyer.getBuyer_daily_cap());
         stmt.setInt(counter++, buyer.getRing_timeout());
         stmt.setInt(counter++, buyer.getBuyer_id());


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

	


	public static Buyer addBuyer(String buyer_name, String buyer_number,
			String weight, String tier, String concurrency_cap_limit,
			String bid_price, String is_active, String buyer_daily_cap) {
		
    	Buyer buyer = new Buyer();


	       Connection conn = ProjectUtils.getMySQLConnection();
		   PreparedStatement stmt = null;
			     
			        try
		       		{
			        	
			        	buyer.setBuyer_name(buyer_name);
			        	buyer.setBuyer_number(buyer_number);
			        	buyer.setWeight(Integer.parseInt(weight));
			        	buyer.setTier(Integer.parseInt(tier));
			        	buyer.setConcurrency_cap_limit(Integer.parseInt(concurrency_cap_limit));
			        	buyer.setBid_price(Double.parseDouble(bid_price));
			        	buyer.setIs_active(Integer.parseInt(is_active));
			        	buyer.setBuyer_daily_cap(Integer.parseInt(buyer_daily_cap));
			           	
			 	       
		            stmt = conn.prepareStatement("INSERT INTO Buyers ( buyer_name,buyer_number,weight,tier,concurrency_cap_limit,bid_price,is_active,buyer_daily_cap)  VALUES (?,?,?,?,?,?,?,?)");
		            int counter =1;
		            
		            stmt.setString(counter++, buyer.getBuyer_name());
		            stmt.setString(counter++, buyer.getBuyer_number());
		            stmt.setInt(counter++, buyer.getWeight());
		            stmt.setInt(counter++, buyer.getTier());
		            stmt.setInt(counter++, buyer.getConcurrency_cap_limit());
		            stmt.setDouble(counter++, buyer.getBid_price());
		            stmt.setInt(counter++, buyer.getIs_active());
		            stmt.setInt(counter++, buyer.getBuyer_daily_cap());


   	            int buyer_id =    stmt.executeUpdate();
   	             buyer.setBuyer_id(buyer_id);

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
				
		
			        return buyer;
	
		
		
	}

	public static List<Buyer> loadAllBuyers(String sortingParam) {
		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
	       Connection conn = ProjectUtils.getMySQLConnection();

		List<Buyer> cachedBuyersList = new ArrayList<Buyer>() ;

		  try
    		{
		
		       stmt = conn.createStatement();
				
		       if("undefined".equals(sortingParam))
					  return loadAllBuyers();
				  
				  sortingParam = sortingParam.replace("%20", "  ");
		       
				 rs = stmt.executeQuery("select * from Buyers order by "+sortingParam+"; ");
				
				while(rs.next()) {
					
					Buyer buyer = new Buyer();
					Integer buyer_id = rs.getInt("buyer_id");
					buyer.setBuyer_id(buyer_id);
					buyer.setBuyer_name(rs.getString("buyer_name"));
					String buyer_number = rs.getString("buyer_number");
					buyer.setBuyer_number(buyer_number);
					buyer.setCreated_at(rs.getTimestamp("created_at"));
					buyer.setWeight(rs.getInt("weight"));
					buyer.setTier(rs.getInt("tier"));
					buyer.setConcurrency_cap_limit(rs.getInt("concurrency_cap_limit"));
					buyer.setConcurrency_cap_used(rs.getInt("concurrency_cap_used"));
					buyer.setRunning_status(rs.getInt("running_status"));
					buyer.setBid_price(rs.getDouble("bid_price"));
					buyer.setIs_active(rs.getInt("is_active"));
					buyer.setBuyer_daily_cap(rs.getInt("buyer_daily_cap"));
					buyer.setRing_timeout(rs.getInt("ring_timeout"));

					
				
					cachedBuyersList.add(buyer);
					
					
		
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
		return cachedBuyersList;
	
		
	}

	
	public static Map<String, String>  getAllBuyersMapByBuyerNumberAndName() {
		
		Map<String, String> buyerNameAndNumberMap = new HashMap<String, String>();
		

		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
	

	       Connection conn = ProjectUtils.getMySQLConnection();

		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select buyer_name,buyer_number from Buyers;");
				
				while(rs.next()) {
					
					
					buyerNameAndNumberMap.put(rs.getString("buyer_number"), rs.getString("buyer_name"));
					
				
					
					
		
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
//		        	
		        	
					
		        		ProjectUtils.closeConnection(rs, stmt, conn);
						
						
				
		        }
	
	
		
		
		
		return buyerNameAndNumberMap;
	} 
	

	public static Buyer getBuyerByByerId(int buyer_id) {
		

		
		
		Statement stmt = null;
		ResultSet rs =  null;
		List<Buyer> cachedBuyersList = new ArrayList<Buyer>() ;
		Buyer buyer = null;

	       Connection conn = ProjectUtils.getMySQLConnection();

		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from Buyers where buyer_id = '"+buyer_id+"';");
				
				if(rs.next()) {
					
					buyer = new Buyer();
					buyer.setBuyer_id(buyer_id);
					buyer.setBuyer_name(rs.getString("buyer_name"));
					String buyer_number = rs.getString("buyer_number");
					buyer.setBuyer_number(buyer_number);
					buyer.setCreated_at(rs.getTimestamp("created_at"));
					buyer.setWeight(rs.getInt("weight"));
					buyer.setTier(rs.getInt("tier"));
					buyer.setConcurrency_cap_limit(rs.getInt("concurrency_cap_limit"));
					buyer.setConcurrency_cap_used(rs.getInt("concurrency_cap_used"));
					buyer.setRunning_status(rs.getInt("running_status"));
					buyer.setBid_price(rs.getDouble("bid_price"));
					buyer.setIs_active(rs.getInt("is_active"));
					buyer.setBuyer_daily_cap(rs.getInt("buyer_daily_cap"));
					buyer.setBuyer_number(rs.getString("buyer_number"));
					buyer.setRing_timeout(rs.getInt("ring_timeout"));

					
				
					cachedBuyersList.add(buyer);
					
					
		
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
//		        	
		        	
					
		        		ProjectUtils.closeConnection(rs, stmt, conn);
						
						
				
		        }
		return buyer;
	
	}


	public static Buyer loadBuyerByBuyerId(String buyerId) {
		// TODO Auto-generated method stub
		return null;
	}


	public static void updateBuyerConcurrency(String buyerId, String concurrency) {

		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE Buyers SET concurrency_cap_limit = ? WHERE buyer_id = ?");
        
         int counter =1;
         
         
         stmt.setInt(counter++, Integer.parseInt(concurrency.trim()));
         stmt.setInt(counter++, Integer.parseInt(buyerId.trim()));
       


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


	public static void updateBuyerTier(String buyerId, String tier) {
		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE Buyers SET tier = ? WHERE buyer_id = ?");
        
         int counter =1;
         
         
         stmt.setInt(counter++, Integer.parseInt(tier.trim()));
         stmt.setInt(counter++, Integer.parseInt(buyerId.trim()));
       


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


	public static void updateBuyerWeight(String buyerId, String weight) {
		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE Buyers SET weight = ? WHERE buyer_id = ?");
        
         int counter =1;
         
         
         stmt.setInt(counter++, Integer.parseInt(weight.trim()));
         stmt.setInt(counter++, Integer.parseInt(buyerId.trim()));
       


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


	public static void updateBuyerState(String buyerId, String state) {
		

		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		   
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE Buyers SET is_active = ? WHERE buyer_id = ?");
        
         int counter =1;
         
         
         stmt.setInt(counter++, Integer.parseInt(state.trim()));
         stmt.setInt(counter++, Integer.parseInt(buyerId.trim()));
       


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


	public static void updateBuyerDailyCap(String buyerId, String dailycap) {

		

		


		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

		   
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE Buyers SET buyer_daily_cap = ? WHERE buyer_id = ?");
        
         int counter =1;
         
         
         stmt.setInt(counter++, Integer.parseInt(dailycap.trim()));
         stmt.setInt(counter++, Integer.parseInt(buyerId.trim()));
       


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
