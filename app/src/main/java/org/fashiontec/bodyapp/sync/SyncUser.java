/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapp.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handles the Sync of users
 */
public class SyncUser extends Sync {

	private static String json;
	private static final String URL = "http://192.168.1.2:8020/user";
	private static String result;
	private static final int CON_TIMEOUT=5000;
	private static final int SOC_TIMEOUT=5000;
	

	/**
	 * Get the user ID of the given user from web application
	 * 
	 * @param email
	 * @param name
	 * @return
	 */
	public static String getUserID(String email, String name) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.accumulate("name", name);
			jsonObject.accumulate("age", "22");
			jsonObject.accumulate("dob", "12/10/1990");
			jsonObject.accumulate("emailId", email);
			json = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		SyncUser su=new SyncUser();

		result = su.POST(URL, json, CON_TIMEOUT,SOC_TIMEOUT);
		return result;
	}
	
	@Override
	public String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		result = result.replaceAll("\"", "");
		JSONObject jObject;
		String out=null;
		try {
			jObject = new JSONObject(result);
			out= jObject.getString("user_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;

	}
	

}
