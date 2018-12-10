package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.triyasoft.model.Buyer;
import com.triyasoft.model.BuyerSourcePreferenceFilter;
import com.triyasoft.utils.ProjectUtils;

public class BuyerSourcePreferenceFilterDao {
	
	public static boolean isLoaded = false;
	
	public static List<BuyerSourcePreferenceFilter> filters = null;


	public static List<BuyerSourcePreferenceFilter> loadAllFilters() {

		filters = new ArrayList<BuyerSourcePreferenceFilter>();

		Statement stmt = null;
		ResultSet rs =  null;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
   		{
		
		       stmt = conn.createStatement();
				String sqlQuery = "select * from buyersourcepreferencefilter where is_active='1'  ;";
				 rs = stmt.executeQuery(sqlQuery);

				while(rs.next()) {
					
					BuyerSourcePreferenceFilter bspf = new BuyerSourcePreferenceFilter();
					bspf.setId(rs.getInt("id"));
					bspf.setBuyer_id(rs.getInt("buyer_id"));
					bspf.setSource_id(rs.getInt("source_id"));
					bspf.setOperator(rs.getString("operator"));
					bspf.setCreated_date(rs.getTimestamp("created_date"));
					bspf.setIs_active(rs.getInt("is_active"));
					
					filters.add(bspf);
					
					
		
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
		  
		  isLoaded = true;
		  
		return filters;
	
		
	}
	
	
	
	
	
	public static List<BuyerSourcePreferenceFilter> loadAllFiltersIncludingInActive() {

		filters = new ArrayList<BuyerSourcePreferenceFilter>();

		Statement stmt = null;
		ResultSet rs =  null;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
   		{
		
		       stmt = conn.createStatement();
				String sqlQuery = "select * from buyersourcepreferencefilter  order by buyer_id ;";
				 rs = stmt.executeQuery(sqlQuery);

				while(rs.next()) {
					
					BuyerSourcePreferenceFilter bspf = new BuyerSourcePreferenceFilter();
					bspf.setId(rs.getInt("id"));
					bspf.setBuyer_id(rs.getInt("buyer_id"));
					bspf.setSource_id(rs.getInt("source_id"));
					bspf.setOperator(rs.getString("operator"));
					bspf.setCreated_date(rs.getTimestamp("created_date"));
					bspf.setIs_active(rs.getInt("is_active"));
					
					filters.add(bspf);
					
					
		
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
		  
		  isLoaded = true;
		  
		return filters;
	
		
	}


	public static List<BuyerSourcePreferenceFilter> getFilters() {
		
		if(!isLoaded)
			loadAllFilters();
		
		return filters;
	}


	public static void setFilters(List<BuyerSourcePreferenceFilter> filters) {
		BuyerSourcePreferenceFilterDao.filters = filters;
	}


	public static BuyerSourcePreferenceFilter addFilter(String source_id,	String operator, String buyer_id, String is_active) {
		
		BuyerSourcePreferenceFilter filter = new BuyerSourcePreferenceFilter();


	       Connection conn = ProjectUtils.getMySQLConnection();
		   PreparedStatement stmt = null;
			     
			        try
		       		{
			        	
			           
		            stmt = conn.prepareStatement("INSERT INTO buyersourcepreferencefilter ( source_id,operator,buyer_id,is_active)  VALUES (?,?,?,?)");
		            int counter =1;
		            
		            stmt.setInt(counter++, Integer.parseInt(source_id.trim()));
		            stmt.setString(counter++, operator.trim());
		            stmt.setInt(counter++, Integer.parseInt(buyer_id.trim()));
		            stmt.setInt(counter++, Integer.parseInt(is_active.trim()));
		        

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
				
		
			        return filter;
	
		
		
	}


	public static void deleteFilter(String id) {
		Statement stmt = null;
		ResultSet rs =  null;
		Connection connection = ProjectUtils.getMySQLConnection();

		try
		{
	
	       stmt = connection.createStatement();
	       String sql = "DELETE FROM buyersourcepreferencefilter " +
                   "WHERE id = '"+id.trim()+"';";
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


	public static int updateFilter(String id,
			String source_id, String operator, String buyer_id, String is_active) {

		PreparedStatement stmt = null;
	    Connection conn = ProjectUtils.getMySQLConnection();

	    int result = 0;
		 try
    		{
	        	
	
	           	
         stmt = conn.prepareStatement("UPDATE buyersourcepreferencefilter SET buyer_id = ? , source_id = ? , operator = ? , is_active = ?  WHERE id = ?");
        
         int counter =1;
         
         stmt.setInt(counter++, Integer.parseInt(buyer_id.trim()));
         stmt.setInt(counter++, Integer.parseInt(source_id.trim()));
         stmt.setString(counter++, operator.trim());
         stmt.setInt(counter++, Integer.parseInt(is_active.trim()));
         stmt.setInt(counter++, Integer.parseInt(id.trim()));

      


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
	        
	       
		finally {
			ProjectUtils.closeConnection(null, stmt, conn);
		}


		 return result;
		
	}
	
	
	
}
