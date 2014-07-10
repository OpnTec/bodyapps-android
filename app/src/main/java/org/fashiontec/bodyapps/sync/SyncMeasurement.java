/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.Person;

/**
 * Handles the Sync of measurements
 */
public class SyncMeasurement extends Sync {

    private static final String URL = "http://192.168.1.2:8020/users/measurements";
    private static String result;
    private static final int CON_TIMEOUT = 10000;
    private static final int SOC_TIMEOUT = 20000;

    /**
     * Converts measurement object to JSON string
     *
     * @param measurement
     * @param person
     * @return
     */
    public static String sendMeasurement(Measurement measurement, Person person) {
        String json = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("m_id", measurement.getID());
            switch (measurement.getUnit()) {
                case 0:
                    jsonObject.accumulate("m_unit", "cm");
                    break;
                case 1:
                    jsonObject.accumulate("m_unit", "inch");
            }
            jsonObject.accumulate("mid_neck_girth",
                    measurement.getMid_neck_girth());
            jsonObject.accumulate("bust_girth", measurement.getBust_girth());
            jsonObject.accumulate("waist_girth", measurement.getWaist_girth());
            jsonObject.accumulate("hip_girth", measurement.getHip_girth());
            jsonObject.accumulate("across_back_shoulder_width",
                    measurement.getAcross_back_shoulder_width());
            jsonObject.accumulate("shoulder_drop",
                    measurement.getShoulder_drop());
            jsonObject.accumulate("shoulder_slope_degrees",
                    measurement.getShoulder_slope_degrees());
            jsonObject.accumulate("arm_length", measurement.getArm_length());
            jsonObject.accumulate("wrist_girth", measurement.getWrist_girth());
            jsonObject.accumulate("upper_arm_girth",
                    measurement.getUpper_arm_girth());
            jsonObject.accumulate("armscye_girth",
                    measurement.getArmscye_girth());
            jsonObject.accumulate("height", measurement.getHeight());
            jsonObject.accumulate("hip_height", measurement.getHip_height());
            jsonObject.accumulate("user_id", measurement.getUserID());
            JSONObject personJSON=new JSONObject();
            personJSON.accumulate("name", person.getName());
            personJSON.accumulate("email", person.getEmail());
            personJSON.accumulate("dob", "12/10/1990");// just dummy data to fulfill API post

            if (person.getGender() == 1) {
                personJSON.accumulate("gender", "male");
            } else {
                personJSON.accumulate("gender", "female");
            }
            jsonObject.put("person",personJSON);

            json = jsonObject.toString();
            Log.d("syncMeasure",json);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SyncMeasurement sm = new SyncMeasurement();

        result = sm.POST(URL, json, CON_TIMEOUT, SOC_TIMEOUT);
//        System.out.println(sm.POST(URL, json, CON_TIMEOUT, SOC_TIMEOUT));
        String[] images = SyncPic.encodePics(measurement.getPic_front(), measurement.getPic_side(),
                measurement.getPic_back(), measurement.getID());
        if(images[0]!=null){
            sm.POST("http://192.168.1.2:8020/users/measurements/image",images[0],CON_TIMEOUT,SOC_TIMEOUT);
//            System.out.println("images = " + "front");
        }
        if(images[1]!=null){
            sm.POST("http://192.168.1.2:8020/users/measurements/image",images[1],CON_TIMEOUT,SOC_TIMEOUT);
//            System.out.println("images = side" );
        }
        if(images[2]!=null){
            sm.POST("http://192.168.1.2:8020/users/measurements/image",images[2],CON_TIMEOUT,SOC_TIMEOUT);

        }
        return result;
    }

}
