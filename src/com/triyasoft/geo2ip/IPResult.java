package com.triyasoft.geo2ip;

public class IPResult {
	public static final String NOT_SUPPORTED = "Not_Supported";
	String ip_address;
	String country_short;
	String country_long;
	String region;
	String city;
	String isp;
	float latitude;
	float longitude;
	String domain;
	String zipcode;
	String netspeed;
	String timezone;
	String iddcode;
	String areacode;
	String weatherstationcode;
	String weatherstationname;
	String mcc;
	String mnc;
	String mobilebrand;
	float elevation;
	String usagetype;
	String status;
	boolean delay = false;
	String version = "Version 6.1.0";

	IPResult(String paramString) {
		this.ip_address = paramString;
	}

	public String getCountryShort() {
		return this.country_short;
	}

	public String getCountryLong() {
		return this.country_long;
	}

	public String getRegion() {
		return this.region;
	}

	public String getCity() {
		return this.city;
	}

	public String getISP() {
		return this.isp;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public String getDomain() {
		return this.domain;
	}

	public String getZipCode() {
		return this.zipcode;
	}

	public String getTimeZone() {
		return this.timezone;
	}

	public String getNetSpeed() {
		return this.netspeed;
	}

	public String getIDDCode() {
		return this.iddcode;
	}

	public String getAreaCode() {
		return this.areacode;
	}

	public String getWeatherStationCode() {
		return this.weatherstationcode;
	}

	public String getWeatherStationName() {
		return this.weatherstationname;
	}

	public String getMCC() {
		return this.mcc;
	}

	public String getMNC() {
		return this.mnc;
	}

	public String getMobileBrand() {
		return this.mobilebrand;
	}

	public float getElevation() {
		return this.elevation;
	}

	public String getUsageType() {
		return this.usagetype;
	}

	public String getStatus() {
		return this.status;
	}

	public boolean getDelay() {
		return this.delay;
	}

	public String getVersion() {
		return this.version;
	}

	public String toString() {
		String str = System.getProperty("line.separator");
		StringBuffer localStringBuffer = new StringBuffer("IP2LocationRecord:"
				+ str);
		localStringBuffer.append("\tIP Address = " + this.ip_address + str);
		localStringBuffer.append("\tCountry Short = " + this.country_short
				+ str);
		localStringBuffer.append("\tCountry Long = " + this.country_long + str);
		localStringBuffer.append("\tRegion = " + this.region + str);
		localStringBuffer.append("\tCity = " + this.city + str);
		localStringBuffer.append("\tISP = " + this.isp + str);
		localStringBuffer.append("\tLatitude = " + this.latitude + str);
		localStringBuffer.append("\tLongitude = " + this.longitude + str);
		localStringBuffer.append("\tDomain = " + this.domain + str);
		localStringBuffer.append("\tZipCode = " + this.zipcode + str);
		localStringBuffer.append("\tTimeZone = " + this.timezone + str);
		localStringBuffer.append("\tNetSpeed = " + this.netspeed + str);
		localStringBuffer.append("\tIDDCode = " + this.iddcode + str);
		localStringBuffer.append("\tAreaCode = " + this.areacode + str);
		localStringBuffer.append("\tWeatherStationCode = "
				+ this.weatherstationcode + str);
		localStringBuffer.append("\tWeatherStationName = "
				+ this.weatherstationname + str);
		localStringBuffer.append("\tMCC = " + this.mcc + str);
		localStringBuffer.append("\tMNC = " + this.mnc + str);
		localStringBuffer.append("\tMobileBrand = " + this.mobilebrand + str);
		localStringBuffer.append("\tElevation = " + this.elevation + str);
		localStringBuffer.append("\tUsageType = " + this.usagetype + str);
		return localStringBuffer.toString();
	}
}
