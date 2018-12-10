package com.triyasoft.plivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java email validation program
 * 
 * @author pankaj
 *
 */
public class EmailValidator {
	// Email Regex java
	private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";

	// static Pattern object, since pattern is fixed
	private static Pattern pattern;

	// non-static Matcher object because it's created from the input String
	private static Matcher matcher;

	public EmailValidator() {
		// initialize the Pattern object
		pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * This method validates the input email address with EMAIL_REGEX pattern
	 * 
	 * @param email
	 * @return boolean
	 */

	public static void main(String[] args) throws Exception {

		File fin = new File(
				"/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/plivo/emaillist");
		// File fin = new
		// File("/Users/abc/Documents/workspace/CallTracker/src/com/triyasoft/plivo/hp-realtime");

		FileInputStream fis = new FileInputStream(fin);

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;

		int counter = 0;

		while ((line = br.readLine()) != null) {
			boolean isValidEmail = validateEmail(line.trim());
			if (isValidEmail)
				System.out.println(line.trim());
		}
	}

	public static boolean validateEmail(String email) {
		new EmailValidator();
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
}
