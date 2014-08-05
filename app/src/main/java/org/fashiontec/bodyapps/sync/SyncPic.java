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
import org.fashiontec.bodyapps.models.Measurement;
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
public class SyncPic extends Sync{

    public static String encodePics(String path){
        String enc=null;
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] b = baos.toByteArray();
            enc = Base64.encodeToString(b, Base64.DEFAULT);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("data",enc);
                enc=jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.d("encodePics",enc);
        return enc;
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
        Log.d("syncPic",result);
        //result = result.replaceAll("\"", "");
        JSONObject jObject;
        String out=null;
        try {
            jObject = new JSONObject(result);
            out= jObject.getString("data");
            jObject = new JSONObject(out);
            out=jObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return out;

    }

    public static int getPic(String id, Context context, int type, String url){
        Log.d("getPic",url);
        String URL=url;
        SyncPic sp=new SyncPic();
        int CON_TIMEOUT = 5000;
        int SOC_TIMEOUT = 8000;
        String picID=url.substring(url.lastIndexOf("/")+1);

        try {
            InputStream inputStream=sp.GET(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null){
                return sp.savePic(inputStream, type, id, context, picID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static File getOutputMediaFile(int type, String id, String picID) {
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
        String name;
        if (type == 1) {
            name = "front.jpg";
        } else if (type == 2) {
            name = "side.jpg";
        } else if (type == 3) {
            name = "back.jpg";
        } else {
            name = picID+".jpg";
        }
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + name);
        return mediaFile;
    }

    private int savePic(InputStream inputStream, int type, String id, Context context, String picID)
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
            out= jObject.getString("data");
            jObject = new JSONObject(out);
            out= jObject.getString("data");
            if(out.equals("")){
                return 0;
            }
            decodedString = Base64.decode(out, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            File file=getOutputMediaFile(type, id, picID);
            FileOutputStream fos=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if(type!=0) {
                MeasurementManager.getInstance(context).setImagePath(type, file.getPath(), id, picID);
            }
            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

    }

}
