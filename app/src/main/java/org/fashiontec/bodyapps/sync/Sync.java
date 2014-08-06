/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to handle main sync methods
 */
public class Sync {

    static final String TAG = Sync.class.getName();
    public static String baseURL = "http://freelayers.org";

    public static void setBaseURL(String baseURL) {
        Sync.baseURL = baseURL;
    }

    /**
     * Method which makes all the POST calls
     *
     * @param url
     * @param json
     * @return
     */
    public HttpResponse POST(String url, String json, int conTimeOut, int socTimeOut) {
        HttpResponse response = null;
        try {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, conTimeOut);
            HttpConnectionParams.setSoTimeout(httpParameters, socTimeOut);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(url);
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            response = httpclient.execute(httpPost);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return response;
    }

    /**
     * Converts the given input stream to a string.
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
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
        return result;

    }

    public int download(String url, String file) {

        try {
            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setConnectTimeout(2000);
            c.setRequestProperty("Accept", "application/vnd.valentina.hdf");
            c.connect();

            FileOutputStream f = new FileOutputStream(new File(file));
            InputStream in = c.getInputStream();

            //download code
            byte[] buffer = new byte[1024];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
        return 1;
    }

    public HttpResponse GET(String url, int conTimeOut, int socTimeOut) {
        HttpResponse response = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, conTimeOut);
            HttpConnectionParams.setSoTimeout(httpParameters, socTimeOut);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/json");
            response = client.execute(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return response;

    }

    public HttpResponse PUT(String url, String json, int conTimeOut, int socTimeOut) {
        HttpResponse response = null;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, conTimeOut);
            HttpConnectionParams.setSoTimeout(httpParameters, socTimeOut);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPut request = new HttpPut(url);
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            response = client.execute(request);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }
}
