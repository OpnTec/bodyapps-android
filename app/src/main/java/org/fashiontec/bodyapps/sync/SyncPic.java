/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class to encode images in JSON
 */
public class SyncPic extends Sync {

    static final String TAG = SyncPic.class.getName();

    /**
     * Converts given input stream to a string and process encoded JSON and returns image ID.
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
        Log.d("syncPic : ", result);
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

    /**
     * Manages getting pictures from server.
     * @param id
     * @param context
     * @param type
     * @param url
     * @return
     */
    public int getPic(String id, Context context, PicTypes type, String url) {
        Log.d("getPic", url);
        String URL = url;
        SyncPic sp = new SyncPic();
        int CON_TIMEOUT = 5000;
        int SOC_TIMEOUT = 8000;
        String picID = url.substring(url.lastIndexOf("/") + 1);
        String filePath = getOutputMediaFile(type, id, picID).getPath();

        try {
            int out = download(url, filePath);
            if (out == 1 && type != PicTypes.OTHER) {
                Log.d("getPic", out + " @*");
                MeasurementManager.getInstance(context).setImagePath(type, filePath, id, picID);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    /**
     * Gets path to save the images downloaded from API.
     * @param type
     * @param id
     * @param picID
     * @return
     */
    private File getOutputMediaFile(PicTypes type, String id, String picID) {
        String IMAGE_DIRECTORY_NAME = "BodyApp" + File.separator + id;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME
        );

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        String name = null;
        switch (type) {
            case FRONT:
                name = "front.jpg";
                break;
            case SIDE:
                name = "side.jpg";
                break;
            case BACK:
                name = "back.jpg";
                break;
            case OTHER:
                name = picID + ".jpg";
                break;
        }
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + name);
        return mediaFile;
    }

    /**
     * Multipart put request for images.
     * @param url
     * @param path
     * @return
     */
    public HttpResponse put(String url, String path) {
        HttpResponse response = null;
        try {
            File file = new File(path);
            HttpClient client = new DefaultHttpClient();
            HttpPut post = new HttpPut(url);

            InputStreamEntity entity = new InputStreamEntity(new FileInputStream(file.getPath()), file.length());
            entity.setContentType("image/jpeg");
            entity.setChunked(true);
            post.setEntity(entity);


            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Multipart post for images.
     * @param url
     * @param path
     * @return
     */
    public HttpResponse post(String url, String path) {
        HttpResponse response = null;
        try {
            File file = new File(path);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            InputStreamEntity entity = new InputStreamEntity(new FileInputStream(file.getPath()), file.length());
            entity.setContentType("image/jpeg");
            entity.setChunked(true);
            post.setEntity(entity);


            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
