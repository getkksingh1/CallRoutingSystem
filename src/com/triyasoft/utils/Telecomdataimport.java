package com.triyasoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class Telecomdataimport {
	public static  Map<String, LatLong> latlongClliMap = new HashMap<String, LatLong>();
	public static int globalcounter  = 0;

public static void main(String[] args) {
	
	//loadcllilatlong();
	//readTeleComTable();
	
	updateExistingCallsWithGeodata();
}

 public static void updateExistingCallsWithGeodata() {
	readCallsTable();
	
}

private static void readCallsTable() {

	 

	
	
	Statement stmt = null;
	ResultSet rs =  null;
    Connection conn = ProjectUtils.getMySQLConnection();
    
   List<String> unknowClli =  new ArrayList<String>();


  try
	{

       stmt = conn.createStatement();
		
		 rs = stmt.executeQuery("select uuid, caller_number from calls ; ");
		
		while(rs.next()) {
			
			String uuid  = rs.getString("uuid");
			String caller_number = rs.getString("caller_number");
			
			updateCallerDetails(uuid,caller_number);
			
			
			
			

			
			
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
  
  System.out.println("List of Unknow Cli");
  for (String clli : unknowClli) {
	  
	  System.out.println(clli);
	
}



}

private static void updateCallerDetails(String uuid, String caller_number) {

    Connection conn = ProjectUtils.getMySQLConnection();
	Statement stmt = null;
	ResultSet rs =  null;

	try {

		String callerNumber = caller_number;
		String npa = callerNumber.substring(1, 4);
		String nxx = callerNumber.substring(4, 7);

		stmt = conn.createStatement();

		rs = stmt.executeQuery("select * from telecom_static where npa='"
				+ npa + "' and nxx= '" + nxx + "'");

		while (rs.next()) {

			String city = rs.getString("switch_name");
			String state  = rs.getString("state");
			String country  = rs.getString("country") ;
			String phoneProvide = rs.getString("company");
			String latitude = rs.getString("latitude");
			String longitude  = rs.getString("longitude");
			
			updateCallRecord(uuid, city, state, country, phoneProvide, latitude, longitude);

		}
	} catch (SQLException se) {
		// Handle errors for JDBC
		se.printStackTrace();
	}

	catch (Exception e) {
		// Handle errors for Class.forName
		e.printStackTrace();
	}

	finally {
		ProjectUtils.closeConnection(rs, stmt, conn);
	}
	
}

private static void updateCallRecord(String uuid, String city, String state,
		String country, String phoneProvide, String latitude, String longitude) {

	Connection conn = ProjectUtils.getMySQLConnection();

	PreparedStatement stmt = null;

	try {

		stmt = conn
				.prepareStatement("UPDATE calls set city = ? , state = ?, country = ? , phoneprovider = ? , latitude = ? , longitude =?		  WHERE uuid = ?");

		int counter = 1;
	
		stmt.setString(counter++, city);
		stmt.setString(counter++, state);
		stmt.setString(counter++, country);
		stmt.setString(counter++, phoneProvide);
		stmt.setString(counter++, latitude);
		stmt.setString(counter++, longitude);
		stmt.setString(counter++, uuid);



		stmt.executeUpdate();
		
		System.out.println("Updating record "+(globalcounter++));

	}

	catch (SQLException se) {
		// Handle errors for JDBC
		se.printStackTrace();
	}

	catch (Exception e) {
		// Handle errors for Class.forName
		e.printStackTrace();
	}

	finally {
		ProjectUtils.closeConnection(null, stmt, conn);
	}
	
	
}

public static void loadcllilatlong() {
	
	 
	 try {
			File file = new File("/tmp/clli-lat-lon.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			int counter = 0;
			while ((line = bufferedReader.readLine()) != null) {
			
				counter++;
			String[]	 values = line.split("\t");
			if(values.length< 3)
				System.out.println("Something wrong at line number "+counter);
			else {
				
				String clli = values[0];
				clli = clli.substring(0,6);
				String lat = values[1];
				String longit = values[2];
				LatLong latLong = new LatLong();
				latLong.setLati(lat);
				latLong.setLongi(longit);
				
				latlongClliMap.put(clli, latLong);
				
			}
				
				
			}
			fileReader.close();
			System.out.println("Contents of file:");
			System.out.println(stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	 
	
	 
}

public static  void readTeleComTable() {
	 

		
		
			Statement stmt = null;
			ResultSet rs =  null;
		    Connection conn = ProjectUtils.getMySQLConnection();
		    
	       List<String> unknowClli =  new ArrayList<String>();


		  try
 		{
		
		       stmt = conn.createStatement();
				
				 rs = stmt.executeQuery("select id, clli from telecom_static ; ");
				
				while(rs.next()) {
					
					String clli  = rs.getString("clli");
					int id = rs.getInt("id");

					String truncatedclli = clli.substring(0, 6);
					LatLong  latlong = latlongClliMap.get(truncatedclli);
					if(latlong == null) {
						System.out.println(id+" Did not find lat long "+clli);
						
						if(!unknowClli.contains(truncatedclli)) {
							unknowClli.add(truncatedclli);
						}
					}
					else {
							updatetelecom_static(id,latlong) ;
					}
					
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
		  
		  System.out.println("List of Unknow Cli");
		  for (String clli : unknowClli) {
			  
			  System.out.println(clli);
			
		}
	
	
}

private static void updatetelecom_static(int id, LatLong latlong) {


	PreparedStatement stmt = null;
    Connection conn = ProjectUtils.getMySQLConnection();

	
	 try
		{
        	

           	
     stmt = conn.prepareStatement("UPDATE telecom_static SET latitude = ? , longitude = ?  WHERE id = ?");
    
     int counter =1;
     
     stmt.setString(counter++, latlong.getLati());
     stmt.setString(counter++, latlong.getLongi());
     stmt.setInt(counter++, id);
   


    stmt.executeUpdate();
    
    System.out.println("Updated row "+(globalcounter++));

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

class LatLong{
	
	String lati;
	String longi;
	
	public String getLati() {
		return lati;
	}
	public void setLati(String lati) {
		this.lati = lati;
	}
	public String getLongi() {
		return longi;
	}
	public void setLongi(String longi) {
		this.longi = longi;
	}
	
	
	
}
