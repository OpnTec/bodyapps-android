/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Handles the Sync of users
 */
public class SyncUser extends Sync {

    private final String URL;

    private static String json;
    private static String result = "";
    private static final int CON_TIMEOUT = 5000;
    private static final int SOC_TIMEOUT = 5000;
    static final String TAG = SyncUser.class.getName();

    public SyncUser() {
        URL = baseURL + "/users";
    }

    /**
     * Get the user ID of the given user from web application
     *
     * @param email
     * @param name
     * @return
     */
    public String getUserID(String email, String name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("name", name);
            jsonObject.accumulate("age", "22");
            jsonObject.accumulate("dob", "12/10/1990");
            jsonObject.accumulate("email", email);
            json = jsonObject.toString();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            InputStream inputStream = this.post(URL, json, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null) {
                result = this.convertInputStreamToString(inputStream);
            } else {
                result = "";
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

    /**
     * Converts given input stream to a string and process encoded JSON and returns image ID.
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        inputStream.close();
        JSONObject jObject;
        String out = null;
        try {
            jObject = new JSONObject(result);
            out = jObject.getString("data");
            jObject = new JSONObject(out);
            out = jObject.getString("id");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return out;
    }
}
