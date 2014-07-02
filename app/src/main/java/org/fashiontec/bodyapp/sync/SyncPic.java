/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */
package org.fashiontec.bodyapp.sync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Class to encode images in JSON
 */
public class SyncPic {

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
                jsonObject.accumulate("m_id",mid);
                jsonObject.accumulate("body_part","front");
                jsonObject.accumulate("m_id","jpg");
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
                jsonObject.accumulate("m_id",mid);
                jsonObject.accumulate("body_part","side");
                jsonObject.accumulate("m_id","jpg");
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
                jsonObject.accumulate("m_id",mid);
                jsonObject.accumulate("body_part","back");
                jsonObject.accumulate("m_id","jpg");
                jsonObject.accumulate("binary_data",backEnc);
                backEnc=jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


//        System.out.println(frontEnc);
        String[] encoded={frontEnc,sideEnc,backEnc};
        return encoded;
    }
}
