package com.triyasoft.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.triyasoft.model.UserModel;


public class ProjectUtils {
	
	public static boolean isProd = true;
	
	public static String currentAppContext = null;
	
	public static String getAppContext () {
		return currentAppContext;
	}
	
	public static boolean checkSessionValidity(HttpServletRequest request) { 
		
		if(!isProd)
		return true;
		
	     UserModel  user  = (UserModel)request.getSession().getAttribute("user");
	     if(user == null)
	    	 return false;
	     else 
	    	 return user.isIs_logged_in();

		
	}
	
	
	
	
	public static String getBaseURL(HttpServletRequest request) {
	
		String baseURL = "";
		
    	String serverName =  request.getServerName();
		
		//String serverName =  "52.36.216.25";

		
		
	     UserModel  user  = (UserModel)request.getSession().getAttribute("user");

		
		int serverPort = request.getServerPort();
		String protocol = "http";
		String appContext = request.getContextPath();
		
		System.out.println("appContext:"+appContext);
		
		serverName =  "54.69.18.111";
		
		if("/HTA".equals(appContext))
			serverName =  "54.69.18.111";
		
		if("/PHARMACY".equals(appContext))
			serverName =  "54.69.18.111";
		
		if("/MKhullar".equals(appContext))
			serverName =  "54.69.18.111";
		
		if("/Shukla".equals(appContext))
			serverName =  "54.69.18.111";
		
		if("/CallTracker".equals(appContext))
					serverName =  "localhost";
		
		if("/RingPool".equals(appContext))
			serverName =  "54.203.99.136";
		
		if("/SM".equals(appContext))
			serverName =  "54.203.99.136";

		
		if("/NN".equals(appContext))
			serverName =  "54.203.99.136";
		
	
		if("/AG".equals(appContext))
			serverName =  "54.203.99.136";
		
		if("/HTA3".equals(appContext))
			serverName =  "54.203.99.136";
	
		if("/Divyansh".equals(appContext))
			serverName =  "54.203.99.136";
		
		if("/AGT".equals(appContext))
			serverName =  "54.203.99.136";
	
		if("/HTA2".equals(appContext))
			serverName =  "54.69.18.111";
		
		
		if("/Nitish".equals(appContext))
			serverName =  "54.203.99.136";
		
		if("/can6600".equals(appContext))
			serverName =  "54.203.99.136";
		
		
		if("/HPS".equals(appContext))
			serverName =  "52.11.17.61";
		
		
		String portStr = "";
		
		if(serverPort != 80)
			portStr = ":"+serverPort;
		
		String userAppContext = "";
		if(user!=null)
			userAppContext = user.getAdditional_context();
		
		System.out.println("serverName:"+serverName);

		
		baseURL = protocol+"://"+serverName+portStr+appContext+userAppContext;
		
		return baseURL ;
		
	}
	
	public static String findCookieValue(String cookieName, HttpServletRequest request) {

			Cookie[] cookies = request.getCookies();
		
			if(cookies!=null) {
			for (Cookie cookie : cookies) {
				if(cookieName.equals(cookie.getName()))
					return cookie.getValue();
			}
			return null;
			}
			else return null;
		}
	
	
	
 	public static Map<String, String> propertyMap=new HashMap<String, String>();	
	 private static final Logger logger = Logger.getLogger(ProjectUtils.class);


	  public static String getProperty(String property){
		  
		  propertyMap = readProperties(FeedConstant.PROPERTY_FILE_PATH);
	    	return propertyMap.get(property);
	    }
	
	 public static Map<String, String> readProperties(String propertyFilePath){
	    	
	    	Map<String, String> propertyMap=new HashMap<String, String>();
	    	
	    	Properties properties = new Properties();
	    	try {
				properties.load(ProjectUtils.class.getResourceAsStream(propertyFilePath));
				
				Set<String> keySet=properties.stringPropertyNames();
				
				for (String key : keySet) {
					propertyMap.put(key, properties.getProperty(key));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return propertyMap;
	    }
	 
	 
	 public static String convertDurationToString (double timeduration) {
		 
		 String callDuration  = "";
		 int minutes = 0;
		 int seconds = 0;
		 int hours  = 0;
		 
		 if(timeduration<60)
	    	 callDuration = "00:"+ ((int)(timeduration));
	     
	     if(timeduration>60 && timeduration <3600)
	     {
	    	 minutes =  ((int)(timeduration))/60;
	    	 seconds = ((int)(timeduration)) - minutes*60;
	    	 if(seconds <10)
		    	 callDuration = minutes +":0"+ seconds;
	    	 else	 
	    		 callDuration = minutes +":"+ seconds;
	     }
	     
	     if(timeduration>3600) {
	    	 hours = ((int)(timeduration))/3600;
	    	 minutes = (((int)(timeduration)) -(hours*3600))/60;
	    	 seconds  = ((int)(timeduration)) - (60*minutes) -(3600*hours);
	    	 
	    	 if(seconds <10)
		    	 callDuration =hours +":0"+ minutes +":"+ seconds+"";
	    	 else    		 
	    		 callDuration =hours +":"+ minutes +":"+ seconds+"";

	     } 
	     
	     return callDuration;
	 }
	
	 public static void getPctunerStatus(Connection conn,String productKey ,int statusValue){
		 String query= "update save_key_macadd set status="+statusValue+" where product_key='"+productKey+"'";
		 try {
			Statement statement = conn.createStatement();
			int results = statement.executeUpdate(query);
			System.out.println("Total No of Row affected:" + results);
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 public static String getMacAddress(Connection connection , String productKey){
		 String macAdd = null;
		 String query = "select mac_add from save_key_macadd where product_key='"+productKey+"'";;
		 try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet.next()){
				macAdd = resultSet.getString("mac_add");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return macAdd;
	 }
	 
	 
	 public static Connection getMySQLConnection () {
			Context initContext = null;
			try {
				initContext = new InitialContext();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Context envContext = null;
			try {
				envContext = (Context)initContext.lookup("java:/comp/env");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DataSource ds = null;
			try {
				ds = (DataSource)envContext.lookup("jdbc/"+currentAppContext+"DB");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return conn;
	 }
	 
	 public static List<String> getBrowserAndOS(HttpServletRequest request)
	  {
	    List<String> detailedList = new ArrayList<String>();
	    String browserDetails = request.getHeader("User-Agent");
	    String userAgent = browserDetails;
	    String user = userAgent.toLowerCase();
	    String os = null;
	    String browser = null;
	    
	    System.out.println("User Agent for the request is===>" + browserDetails);
	    if (userAgent.toLowerCase().indexOf("windows") >= 0) {
	      os = "Windows";
	    } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
	      os = "Mac";
	    } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
	      os = "Unix";
	    } else if (userAgent.toLowerCase().indexOf("android") >= 0) {
	      os = "Android";
	    } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
	      os = "IPhone";
	    } else {
	      os = "UnKnown, More-Info: " + userAgent;
	    }
	    if (user.contains("msie"))
	    {
	      String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
	      browser = "Internet Explorer";  
	    		  // substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
	    }
	    else if ((user.contains("safari")) && (user.contains("version")))
	    {
	      browser = "Safari";
	    		  
	    		  //userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0].split("/")[0] + "-" + userAgent.substring(userAgent.indexOf("Version")).split(" ")[0].split("/")[1];
	    }
	    else if ((user.contains("opr")) || (user.contains("opera")))
	    {
	      if (user.contains("opera")) {
	        browser = "Opera"; 
	        		//userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0].split("/")[0] + "-" + userAgent.substring(userAgent.indexOf("Version")).split(" ")[0].split("/")[1];
	      } else if (user.contains("opr")) {
	        browser = "Opera";
	        		//userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0].replace("/", "-").replace("OPR", "Opera");
	      }
	    }
	    else if (user.contains("chrome"))
	    {
	      browser = "Chrome" ;
	    		 // userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0].replace("/", "-");
	    }
	    else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1) || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1))
	    {
	      browser = "Netscape";
	    }
	    else if (user.contains("firefox"))
	    {
	      browser = "Firefox"; 
	    		  //userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0].replace("/", "-");
	    }
	    else
	    {
	      browser = "UnKnown, More-Info: " + userAgent;
	    }
	    detailedList.add(os);
	    detailedList.add(browser);
	    
	    return detailedList;
	  }
	  
	public static String getDateTime() {
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    Date today = Calendar.getInstance().getTime();
		    String reportDate = df.format(today);
		    return reportDate;
		// TODO Auto-generated method stub
 	}
	
	public String getIPAddress(HttpServletRequest request){
		  String ipAddress = request.getHeader("X-FORWARDED-FOR");  
	       
	        if (ipAddress == null) {  
	     	   ipAddress = request.getRemoteAddr();  
	        }
	        if(ipAddress.contains(",")){
	        	String[] ipaddlist = ipAddress.split(",");
	        	ipAddress = ipaddlist[0];
	        }
		
		return ipAddress;
	}
	
	public static void closeConnection(ResultSet rs, Statement ps)
	{
	    if (rs!=null)
	    {
	        try
	        {
	            rs.close();

	        }
	        catch(SQLException e)
	        {
	            logger.error("The result set cannot be closed.", e);
	        }
	    }
	    if (ps != null)
	    {
	        try
	        {
	            ps.close();
	        } catch (SQLException e)
	        {
	            logger.error("The statement cannot be closed.", e);
	        }
	    }

	}
	
	
	public static void closeConnection(ResultSet rs, Statement ps, Connection con)
	{
	    if (rs!=null)
	    {
	        try
	        {
	            rs.close();

	        }
	        catch(SQLException e)
	        {
	            logger.error("The result set cannot be closed.", e);
	        }
	    }
	    if (ps != null)
	    {
	        try
	        {
	            ps.close();
	        } catch (SQLException e)
	        {
	            logger.error("The statement cannot be closed.", e);
	        }
	    }
	    
	    if (con != null)
	    {
	        try
	        {
	        	con.close();
	        } catch (SQLException e)
	        {
	            logger.error("The statement cannot be closed.", e);
	        }
	    }

	}
	
	public static void main(String[] args) {
		
		  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
		
		  
		  
		
		 
		  Date date = new Date();
	      date.setTime(date.getTime()-86400*1000);

	      System.out.println(formatter.format(date));
	      
	}
	
	
	public static Date getFirstInstantOfDay(String timeZoneId, Date date) {
	    Calendar resultDate = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
	    resultDate.setTime(date);
	    resultDate.set(Calendar.HOUR, 0);
	    resultDate.set(Calendar.MINUTE, 0);
	    resultDate.set(Calendar.SECOND, 0);
	    resultDate.set(Calendar.MILLISECOND, 0);
	    return resultDate.getTime();
	}
	
	
	public static String getTodayStrInClientsTimezone(String timeZoneId) {
		
		
		  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
		  Date date = new Date();
		  return formatter.format(date)+" 05:00:00";
		
		
	}
	
	
	public static String getYesterdayStrInClientsTimezone(String timeZoneId) {
		
		  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
		
		  
		  
		
		 
		  Date date = new Date();
	      date.setTime(date.getTime()-86400*1000);

		  return formatter.format(date)+" 04:00:00";
		
		
		
	}
	
public static String getTomorrowStrInClientsTimezone(String timeZoneId) {
	
	
	  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
	  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
	  Date date = new Date();
      date.setTime(date.getTime()+86400*1000);

	  return formatter.format(date)+" 05:00:00";
	
	
		
		
	}


public static String getDateStrInClientsTimezone(String dateStr) {
	

	
	
	  DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
	  Date date = null ;
	  
	  try {
		  date =	formatter.parse(dateStr);
	} catch (ParseException e) {
		   date = new Date();
		e.printStackTrace();
	}
	  
	  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));

	  return formatter.format(date)+" 05:00:00";
	
	
	
}
	
	
	
	
	public static String getDateInOtherTimeZone() {
		
		/*Date date = new Date();
		date =	getFirstInstantOfDay("US/Eastern", date);
	    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    
	    System.out.println(formatter.format(date));

		
		//2017-06-22 21:14:36
		System.out.println(getFirstInstantOfDay("US/Eastern", new Date()).getTime());
	    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		  date = null;
		try {
			date = formatter.parse("2013-04-13 00:00:00.000");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("London: " + formatter.format(getFirstInstantOfDay("Europe/London",  date)));
		 System.out.println("Helsinki: " + formatter.format(getFirstInstantOfDay("Europe/Helsinki", date)));
		
		
		*/
		
		/*
		
		  Date date  = new Date();
		  DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
		 
		  
		  System.out.println(formatter.format(date));
		  
		  formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		  
		  String formattedDate = formatter.format(date);
		 
		  String deviationFromGMT = formattedDate.substring(20);
		  String signOfDeveation = deviationFromGMT.substring(0,1);
		  String hourDeviation = deviationFromGMT.substring(1,3);
		  String minutesDevation = deviationFromGMT.substring(3,5);
		 
		  System.out.println(signOfDeveation);
		  System.out.println(hourDeviation);
		  System.out.println(minutesDevation);


		  
		  

		  System.out.println(formattedDate);

		  date.setTime(date.getTime()+86400*1000);
		  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
		  System.out.println(formatter.format(date));
		  
		  //2017-06-23 15:07:41
		  
	/*	  String[] zoneids = TimeZone.getAvailableIDs();
		  for (String zoneid : zoneids) {
			  System.out.println(zoneid);
			
		}
		*/
		 
		 
		  
		  return null;
		  
		
		
	}

	public static String convertToEasternTime(Date callLandingTime) {
	
	  DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	  formatter.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
	  
	  return formatter.format(callLandingTime);
	  
	 
	  
	}

	
	
	public static String getsubDomain(String domain) {
		
		int indexofFirstdot = domain.indexOf(".");
		if(indexofFirstdot>=0)
			return domain.substring(0,indexofFirstdot);
		
		return null;
	} 
	
}
