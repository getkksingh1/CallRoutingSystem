package com.triyasoft.daos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.triyasoft.model.AppSetting;
import com.triyasoft.utils.ProjectUtils;

public class AppSettingsDao {
	
	public static Map<String, AppSetting> appSettings = null;
	private static boolean    isAppSettingLoaded =  false;
	
	
	public static String getServerIP() {
		
		return appSettings.get("server_ip_for_plivo_communication").getValue();

	}
	
	public static String getPlivoAuthID () {
		
		return appSettings.get("auth_id").getValue();
	}

	public static String getPlivoAuthToken() {
		
		return appSettings.get("auth_token").getValue();
		
	}
	
	public static boolean isSkypeAllowed() {
		
		String skypeallowed = appSettings.get("skypeallowed").getValue();
		return Boolean.valueOf(skypeallowed);
	}
	
	
	public static boolean isFlatBillingRate () {
		String isFlatBillingRate =  appSettings.get("isFlatBillingRate").getValue();
		return Boolean.valueOf(isFlatBillingRate);
	}
	
	public static Double getFlatBillingRate() {
		String flatBillingRate = appSettings.get("flatBillingRate").getValue();
		return Double.valueOf(flatBillingRate);
	}
	
	
	public static boolean IscustomerHasPlivoaccount() {
		String customerHasPlivoaccount =  appSettings.get("customerHasPlivoaccount").getValue();
		return Boolean.valueOf(customerHasPlivoaccount);

	}
	
	public static Map<String, AppSetting> getAppSettings() {
		if(isAppSettingLoaded == false)
			loadAllAppSettings();
		
	    	return appSettings;
	    	
	}





	




	public static Map<String, AppSetting> loadAllAppSettings() {
		

		
		System.out.println("Loading App Settings");
		
		Statement stmt = null;
		ResultSet rs =  null;

			Connection conn = ProjectUtils.getMySQLConnection();
			 Map<String, AppSetting> appSettingsTmp = new HashMap<String, AppSetting>();


		  try
    		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select * from app_settings ; ");
				
				while(rs.next()) {
					
					AppSetting appSetting = new AppSetting();
					appSetting.setId(rs.getInt("id"));
					appSetting.setKey(rs.getString("key"));
					appSetting.setValue(rs.getString("value"));
					appSetting.setUser_configurable(rs.getInt("user_configurable"));
					appSettingsTmp.put(appSetting.getKey(), appSetting);
					
		
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
		  
		  appSettings = appSettingsTmp;
		  
		  isAppSettingLoaded = true;
		  return appSettings;
	
	}
}
