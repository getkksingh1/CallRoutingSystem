package com.triyasoft.plivo;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.triyasoft.trackdrive.BuyerResponse;

public class PlivoUtils {
	
	public static void main(String[] args) {
		try {
			String webPage = "https://api.plivo.com/v1/Account/MAODJKN2MZOGE4OTNHNJ/Call/d387001e-5930-11e7-adae-5fa4a6c35b50/";
			String name = "MAODJKN2MZOGE4OTNHNJ";
			String password = "YmI0MmZkZmUwZTBmNmM5ZmFhNjllNjM4MzEzZDAz";

			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);

			URL url = new URL(webPage);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			String result = sb.toString();
			
			Gson gson = new Gson();
			
			PlivoCallResponseModel plivoCallResponseModel = gson.fromJson(result, PlivoCallResponseModel.class);


			System.out.println("*** BEGIN ***");
			System.out.println(result);
			System.out.println("*** END ***");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
