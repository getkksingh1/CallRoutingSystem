package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.triyasoft.model.CallModel;
import com.triyasoft.utils.ProjectUtils;

public class TemplatesDao {

	public static Map<String, String> loadAllTemplates() {

		
		 Map<String, String> templatesMap = new  HashMap<String, String>();

		Statement stmt = null;
		ResultSet rs =  null;
	       Connection conn = ProjectUtils.getMySQLConnection();


		  try
    		{
		
		       stmt = conn.createStatement();
				String sqlQuery = "select * from user_interface ";
				 rs = stmt.executeQuery(sqlQuery+"  ;");


				while(rs.next()) {
					
					String templateName =  rs.getString("template_name");
					String template = rs.getString("template");
					    
					
					templatesMap.put(templateName, template);
					
					
		
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
		return templatesMap;
	
		
	}

}
