/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapps.sync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class to encode images in JSON
 */
public class SyncPic extends Sync{

    public static String[] encodePics(String front, String side, String back, String mid){
        String frontEnc=null;
        String sideEnc=null;
        String backEnc=null;

        if(!front.equals("")){
            Bitmap bm = BitmapFactory.decodeFile(front);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            frontEnc = Base64.encodeToString(b, Base64.DEFAULT);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("binary_data",frontEnc);
                frontEnc=jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(!side.equals("")){
            Bitmap bm = BitmapFactory.decodeFile(side);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            sideEnc = Base64.encodeToString(b, Base64.DEFAULT);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("binary_data",sideEnc);
                sideEnc=jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(!back.equals("")){
            Bitmap bm = BitmapFactory.decodeFile(back);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            backEnc = Base64.encodeToString(b, Base64.DEFAULT);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("binary_data",backEnc);
                backEnc=jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] encoded={frontEnc,sideEnc,backEnc};
        return encoded;
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
