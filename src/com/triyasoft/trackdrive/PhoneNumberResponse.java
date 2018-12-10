package com.triyasoft.trackdrive;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.triyasoft.daos.PhoneNumberDaoService;
import com.triyasoft.utils.AdcashZoneTracker;
import com.triyasoft.utils.ProjectUtils;

public class PhoneNumberResponse {


	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, FileNotFoundException {

		Gson gson = new Gson();

		PhoneNumberResponse phoneNumberResponse = gson.fromJson(new FileReader("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/trackdrive/phonenumbersjson"), PhoneNumberResponse.class);
		Connection conn = ProjectUtils.getMySQLConnection();

	//	System.out.println(phoneNumberResponse);
		
		List<PhoneNumber> numbers = 	phoneNumberResponse.getNumbers();
		
		for (PhoneNumber tdphoneNumber : numbers) {
			
			com.triyasoft.model.PhoneNumber trackerPhoneModel = new com.triyasoft.model.PhoneNumber();
			String trafficsourceid = tdphoneNumber.getTraffic_source_id();
			if(trafficsourceid == null || trafficsourceid.trim().length()==0 || "null".equals(trafficsourceid.trim()))
				trafficsourceid = "0" ;
			
			trackerPhoneModel.setTraffic_source_id(Integer.parseInt(trafficsourceid));
			trackerPhoneModel.setNumber(tdphoneNumber.getNumber().replace("+", ""));
			trackerPhoneModel.setIs_Active(1);
			
			if(tdphoneNumber.getOffer_id().equals("10006658"))
				trackerPhoneModel.setCostpercall(5);
			
			if(tdphoneNumber.getOffer_id().equals("10006571"))
				trackerPhoneModel.setCostpercall(9.25);
			
			TrackDriverDBUtility.addPhoneNumberVidDirecDBConnection(trackerPhoneModel, conn);
			
			System.out.println(trackerPhoneModel);
			
		}
	
	}
	
private String status;
private List<PhoneNumber> numbers = null;

public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public List<PhoneNumber> getNumbers() {
	return numbers;
}
public void setNumbers(List<PhoneNumber> numbers) {
	this.numbers = numbers;
}



}
