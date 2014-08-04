/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.managers.PersonManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Handles the Sync of measurements
 */
public class SyncMeasurement extends Sync {

    /**
     * Converts measurement object to JSON string
     *
     * @param measurement
     * @param person
     * @return
     */
    public static String sendMeasurement(Measurement measurement, Person person, boolean syncedOnce) {
        String result=null;
        String URL=serverID+"/users/"+measurement.getUserID()+"/measurements";
        int CON_TIMEOUT = 10000;
        int SOC_TIMEOUT = 20000;
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
            e.printStackTrace();
        }

        SyncMeasurement sm = new SyncMeasurement();
        InputStream inputStream = null;
        if(syncedOnce){
            Log.d("syncMeasure","syncedOnce");
            try {
                URL+="/"+measurement.getID();
                inputStream = sm.PUT(URL, json, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
                if (inputStream != null){
                    result = sm.convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Log.d("syncMeasure","not syncedOnce");
            try {
                inputStream = sm.POST(URL, json, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
                if (inputStream != null) {
                    result = sm.convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("syncMeasure","finished");

        String imgURL=serverID+"/users/"+measurement.getUserID()+"/measurements/"+measurement.getID()+"/image/";
        String[] images = SyncPic.encodePics(measurement.getPic_front(), measurement.getPic_side(),
                measurement.getPic_back(), measurement.getID());

        SyncPic sp=new SyncPic();
        if(images[0]!=null){
            InputStream response= null;
            try {
                response = sp.POST(imgURL+"body_front",images[0],CON_TIMEOUT,SOC_TIMEOUT).getEntity().getContent();
                Log.d("syncMeasurepic",images[0]);
                sp.convertInputStreamToString(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(images[1]!=null){
            sp.POST(imgURL+"body_side",images[1],CON_TIMEOUT,SOC_TIMEOUT);
        }
        if(images[2]!=null){
            sp.POST(imgURL+"body_back",images[2],CON_TIMEOUT,SOC_TIMEOUT);
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
        Log.d("convertInputStreamToString",result);
        JSONObject jObject;
        String out=null;
        try {
            jObject = new JSONObject(result);
            out= jObject.getString("data");
            jObject = new JSONObject(out);
            out=jObject.getString("m_id");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return out;
    }

    public static String[] getSyncList (long lastSync, String userID){
        Log.d("last sync",new Long(lastSync).toString());
        String[] out=null;
        String URL=serverID+"/users/"+userID+"/measurements/?modifiedAfter="+new Long(lastSync).toString();
        SyncMeasurement sm=new SyncMeasurement();
        String json = null;
        int CON_TIMEOUT = 2000;
        int SOC_TIMEOUT = 3000;

        InputStream inputStream = null;
        try {
            inputStream = sm.GET(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null)
                out = sm.convertInputStreamToList(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private String[] convertInputStreamToList(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        String out[]=null;
        Log.d("convertInputStreamToList",result);
        try {
            JSONArray jArray = new JSONArray(result);
            int len=jArray.length();
            out=new String[len];
            for (int i = 0; i < len; i++) {
                JSONObject jObject = new JSONObject(jArray.getString(i));
                out[i]=jObject.getString("data");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return out;
    }

    public static String getMeasurement(String id, Context context, String userID){
        Log.d("getMeasurement id",id);
        String out=null;
        String URL=serverID+"/users/"+userID+"/measurements/"+id;
        int CON_TIMEOUT = 2000;
        int SOC_TIMEOUT = 3000;
        SyncMeasurement sm=new SyncMeasurement();
        InputStream inputStream = null;
        try {
            inputStream =sm.GET(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null)
                out = sm.convertToMeasurement(inputStream, userID, context);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    private String convertToMeasurement(InputStream inputStream, String userID,
                                             Context context) throws IOException {
        Measurement measurement=null;
        Person person=null;
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        Log.d("convertToMeasurement",result);
        JSONObject jMeasurement;
        JSONObject jPerson;

        String out[]=null;
        int personID=0;
        try {
            JSONObject jObject = new JSONObject(result);
            jMeasurement=new JSONObject(jObject.getString("data"));
            jPerson=new JSONObject(jMeasurement.getString("person"));
            int gender=0;
            if(jPerson.getString("gender").equals("male")){
                gender=1;
            }
            person=new Person(jPerson.getString("email"),jPerson.getString("name"),gender);
            if(PersonManager.getInstance(context).getPerson(person)==-1){
                PersonManager.getInstance(context).addPerson(person);
            }
            personID=PersonManager.getInstance(context).getPerson(person);

            String unit=jMeasurement.getString("m_unit");
            int unitInt=0;
            if (unit.equals("cm")){
                unitInt=0;
            }else if (unit.equals("inch")){
                unitInt=1;
            }
            measurement=new Measurement(jMeasurement.getString("m_id"), userID , personID, unitInt);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String dateText = "";
            try {
                Date date = new Date();
                dateText = dateformat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }

            measurement.setCreated(dateText);
            measurement.setSynced(true);
            if(!jMeasurement.getString("mid_neck_girth").equals("null")) {
                measurement.setMid_neck_girth(jMeasurement.getString("mid_neck_girth"));
            }
            if(!jMeasurement.getString("bust_girth").equals("null")) {
                measurement.setBust_girth(jMeasurement.getString("bust_girth"));
            }
            if(!jMeasurement.getString("waist_girth").equals("null")) {
                measurement.setWaist_girth(jMeasurement.getString("waist_girth"));
            }
            if(!jMeasurement.getString("hip_girth").equals("null")) {
                measurement.setHip_girth(jMeasurement.getString("hip_girth"));
            }
            if(!jMeasurement.getString("across_back_shoulder_width").equals("null")) {
                measurement.setAcross_back_shoulder_width(jMeasurement.getString("across_back_shoulder_width"));
            }
            if(!jMeasurement.getString("shoulder_drop").equals("null")) {
                measurement.setShoulder_drop(jMeasurement.getString("shoulder_drop"));
            }
//            if(!jMeasurement.getString("shoulder_slope_degrees").equals("null")) {
//                measurement.setShoulder_slope_degrees(jMeasurement.getString("shoulder_slope_degrees"));
//            }
            if(!jMeasurement.getString("arm_length").equals("null")) {
                measurement.setArm_length(jMeasurement.getString("arm_length"));
            }
            if(!jMeasurement.getString("wrist_girth").equals("null")) {
                measurement.setWrist_girth(jMeasurement.getString("wrist_girth"));
            }
            if(!jMeasurement.getString("upper_arm_girth").equals("null")) {
                measurement.setUpper_arm_girth(jMeasurement.getString("upper_arm_girth"));
            }
            if(!jMeasurement.getString("armscye_girth").equals("null")) {
                measurement.setArmscye_girth(jMeasurement.getString("armscye_girth"));
            }
            if(!jMeasurement.getString("height").equals("null")) {
                measurement.setHeight(jMeasurement.getString("height"));
            }
            if(!jMeasurement.getString("hip_height").equals("null")) {
                measurement.setHip_height(jMeasurement.getString("hip_height"));
            }

            MeasurementManager.getInstance(context).addMeasurement(measurement);
            MeasurementManager.getInstance(context).setSyncedOnce(measurement.getID());
            return measurement.getID();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
