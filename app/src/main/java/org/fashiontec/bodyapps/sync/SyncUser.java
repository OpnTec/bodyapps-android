/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

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
	private static final String URL = serverID+"/users";
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
			jsonObject.accumulate("email", email);
			json = jsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		SyncUser su=new SyncUser();
        InputStream inputStream = null;
        try {
            inputStream = su.POST(URL, json, CON_TIMEOUT,SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null)
                result = su.convertInputStreamToString(inputStream);
            else
                result = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
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
		//result = result.replaceAll("\"", "");
		JSONObject jObject;
		String out=null;
		try {
			jObject = new JSONObject(result);
			out= jObject.getString("data");
            jObject = new JSONObject(out);
            out=jObject.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;

	}
	

}
