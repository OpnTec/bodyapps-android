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

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class to encode images in JSON
 */
public class SyncPic extends Sync {

    static final String TAG = SyncPic.class.getName();

    public String encodePics(String path) {
        String enc = null;
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        enc = Base64.encodeToString(b, Base64.DEFAULT);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("data", enc);
            enc = jsonObject.toString();

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.d("encodePics", enc);
        return enc;
    }

    public String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        Log.d("syncPic", result);
        //result = result.replaceAll("\"", "");
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

    public int getPic(String id, Context context, PicTypes type, String url) {
        Log.d("getPic", url);
        String URL = url;
        SyncPic sp = new SyncPic();
        int CON_TIMEOUT = 5000;
        int SOC_TIMEOUT = 8000;
        String picID = url.substring(url.lastIndexOf("/") + 1);

        try {
            InputStream inputStream = sp.get(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null) {
                return sp.savePic(inputStream, type, id, context, picID);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

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

    private int savePic(InputStream inputStream, PicTypes type, String id, Context context, String picID)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        JSONObject jObject;
        String out;
        byte[] decodedString;
        Bitmap bitmap;
        try {
            jObject = new JSONObject(result);
            out = jObject.getString("data");
            jObject = new JSONObject(out);
            out = jObject.getString("data");
            if (out.equals("")) {
                return 0;
            }
            decodedString = Base64.decode(out, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            File file = getOutputMediaFile(type, id, picID);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (type != PicTypes.OTHER) {
                MeasurementManager.getInstance(context).setImagePath(type, file.getPath(), id, picID);
            }
            return 1;

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }

    }

}
