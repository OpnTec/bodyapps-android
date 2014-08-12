/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.sync;

import android.content.Context;
import android.util.Log;

import org.fashiontec.bodyapps.managers.MeasurementManager;
import org.fashiontec.bodyapps.managers.PersonManager;
import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles the Sync of measurements
 */
public class SyncMeasurement extends Sync {

    static final String TAG = SyncMeasurement.class.getName();

    private final SyncPic syncPic;

    SyncMeasurement() {
        syncPic = new SyncPic();
        syncPic.setBaseURL(this.baseURL);
    }

    /**
     * Converts measurement object to JSON string
     *
     * @param measurement
     * @param person
     * @return
     */
    public String sendMeasurement(Measurement measurement, Person person,
                                         boolean syncedOnce, Context context) {
        String result = null;
        String URL = baseURL + "/users/" + measurement.getUserID() + "/measurements";
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
            jsonObject.accumulate("mid_neck_girth",measurement.getMid_neck_girth());
            jsonObject.accumulate("bust_girth", measurement.getBust_girth());
            jsonObject.accumulate("waist_girth", measurement.getWaist_girth());
            jsonObject.accumulate("hip_girth", measurement.getHip_girth());
            jsonObject.accumulate("across_back_shoulder_width",measurement.getAcross_back_shoulder_width());
            jsonObject.accumulate("shoulder_drop",measurement.getShoulder_drop());
            jsonObject.accumulate("shoulder_slope_degrees",measurement.getShoulder_slope_degrees());
            jsonObject.accumulate("arm_length", measurement.getArm_length());
            jsonObject.accumulate("wrist_girth", measurement.getWrist_girth());
            jsonObject.accumulate("upper_arm_girth",measurement.getUpper_arm_girth());
            jsonObject.accumulate("armscye_girth",measurement.getArmscye_girth());
            jsonObject.accumulate("height", measurement.getHeight());
            jsonObject.accumulate("hip_height", measurement.getHip_height());
            jsonObject.accumulate("user_id", measurement.getUserID());
            JSONObject personJSON = new JSONObject();
            personJSON.accumulate("name", person.getName());
            personJSON.accumulate("email", person.getEmail());

            Date date = new Date(person.getDob());
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String dateText = df2.format(date);
            personJSON.accumulate("dob", dateText);

            if (person.getGender() == 1) {
                personJSON.accumulate("gender", "male");
            } else {
                personJSON.accumulate("gender", "female");
            }
            jsonObject.put("person", personJSON);
            json = jsonObject.toString();

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        if (syncedOnce) {
            try {
                URL += "/" + measurement.getID();
                InputStream inputStream = this.put(URL, json, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
                if (inputStream != null) {
                    result = this.convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.d("syncMeasure", "not syncedOnce");
            try {
                InputStream inputStream = this.post(URL, json, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
                if (inputStream != null) {
                    result = this.convertInputStreamToString(inputStream);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        String imgURL = baseURL + "/users/" + measurement.getUserID() + "/measurements/" + measurement.getID() + "/image/";

        String imgFront = measurement.getPic_front();
        if (!imgFront.equals("")) {
//            String frontJSON = syncPic.encodePics(imgFront);
            InputStream response = null;
            String imgID = MeasurementManager.getInstance(context).getPicID(measurement.getID(), PicTypes.FRONT);
            try {
                if (imgID == null) {
                    Log.i(TAG, "sync pic");
                    response =syncPic.post(imgURL + "body_front",imgFront).getEntity().getContent();
                    imgID = syncPic.convertInputStreamToString(response);
                    MeasurementManager.getInstance(context).setImagePath(PicTypes.FRONT, null, measurement.getID(), imgID);
                } else {
                    response = syncPic.put(baseURL + "/images/" + imgID, imgFront).getEntity().getContent();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        String imgSide = measurement.getPic_side();
        if (!imgSide.equals("")) {
            String sideJSON = syncPic.encodePics(imgSide);
            InputStream response = null;
            String imgID = MeasurementManager.getInstance(context).getPicID(measurement.getID(), PicTypes.SIDE);
            try {
                if (imgID == null) {
                    response = syncPic.post(imgURL + "body_side", imgSide).getEntity().getContent();
                    imgID = syncPic.convertInputStreamToString(response);
                    MeasurementManager.getInstance(context).setImagePath(PicTypes.SIDE, null, measurement.getID(), imgID);
                } else {
                    syncPic.put(baseURL + "/images/" + imgID, imgSide).getEntity().getContent();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        String imgBack = measurement.getPic_back();
        if (!imgBack.equals("")) {
            String backJSON = syncPic.encodePics(imgBack);
            InputStream response = null;
            String imgID = MeasurementManager.getInstance(context).getPicID(measurement.getID(), PicTypes.BACK);
            try {
                if (imgID == null) {
                    response = syncPic.post(imgURL + "body_back", imgBack).getEntity().getContent();
                    imgID = syncPic.convertInputStreamToString(response);
                    MeasurementManager.getInstance(context).setImagePath(PicTypes.BACK, null, measurement.getID(), imgID);
                } else {
                    syncPic.put(baseURL + "/images/" + imgID, imgBack).getEntity().getContent();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        JSONObject jObject;
        String out = null;
        try {
            jObject = new JSONObject(result);
            out = jObject.getString("data");
            jObject = new JSONObject(out);
            out = jObject.getString("m_id");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return out;
    }

    public String[] getSyncList(long lastSync, String userID) {
        String[] out = null;
        String URL = baseURL + "/users/" + userID + "/measurements/?modifiedAfter=" + new Long(lastSync).toString();
        SyncMeasurement sm = new SyncMeasurement();
        String json = null;
        int CON_TIMEOUT = 2000;
        int SOC_TIMEOUT = 3000;

        InputStream inputStream = null;
        try {
            inputStream = sm.get(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null) {
                out = sm.convertInputStreamToList(inputStream);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
        String out[] = null;
        Log.d("convertInputStreamToList", result);
        try {
            JSONArray jArray = new JSONArray(result);
            int len = jArray.length();
            out = new String[len];
            for (int i = 0; i < len; i++) {
                JSONObject jObject = new JSONObject(jArray.getString(i));
                out[i] = jObject.getString("data");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return out;
    }

    public String getMeasurement(String id, Context context, String userID) {
        String out = null;
        String url = baseURL + "/users/" + userID + "/measurements/" + id;

        int connTimeout = 2000;
        int socketTimeout = 3000;

        try {
            InputStream inputStream = this.get(url, connTimeout, socketTimeout).getEntity().getContent();
            if (inputStream != null) {
                String result = this.streamReader(inputStream);
                out = this.convertToMeasurement(result, userID, context);
                this.getPics(result, context, out);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return out;
    }

    private String convertToMeasurement(String result, String userID,
                                        Context context) throws IOException {

        Measurement measurement = null;
        Person person = null;
        JSONObject jMeasurement;
        JSONObject jPerson;
        int personID = 0;
        try {
            JSONObject jObject = new JSONObject(result);
            jMeasurement = new JSONObject(jObject.getString("data"));
            jPerson = new JSONObject(jMeasurement.getString("person"));
            int gender = 0;
            if (jPerson.getString("gender").equals("male")) {
                gender = 1;
            }
            String dob = jPerson.getString("dob");
            dob = dob.substring(0, dob.lastIndexOf("-") + 3);
            Date bdate = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            person = new Person(jPerson.getString("email"), jPerson.getString("name"), gender, bdate.getTime());
            if (PersonManager.getInstance(context).getPerson(person) == -1) {
                PersonManager.getInstance(context).addPerson(person);
            }
            personID = PersonManager.getInstance(context).getPerson(person);

            String unit = jMeasurement.getString("m_unit");
            int unitInt = 0;
            if (unit.equals("cm")) {
                unitInt = 0;
            } else if (unit.equals("inch")) {
                unitInt = 1;
            }
            measurement = new Measurement(jMeasurement.getString("m_id"), userID, personID, unitInt);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String dateText = "";
            try {
                Date date = new Date();
                dateText = dateformat.format(date);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            measurement.setCreated(dateText);
            measurement.setSynced(true);
            if (!jMeasurement.getString("mid_neck_girth").equals("null")) {
                measurement.setMid_neck_girth(jMeasurement.getString("mid_neck_girth"));
            }
            if (!jMeasurement.getString("bust_girth").equals("null")) {
                measurement.setBust_girth(jMeasurement.getString("bust_girth"));
            }
            if (!jMeasurement.getString("waist_girth").equals("null")) {
                measurement.setWaist_girth(jMeasurement.getString("waist_girth"));
            }
            if (!jMeasurement.getString("hip_girth").equals("null")) {
                measurement.setHip_girth(jMeasurement.getString("hip_girth"));
            }
            if (!jMeasurement.getString("across_back_shoulder_width").equals("null")) {
                measurement.setAcross_back_shoulder_width(jMeasurement.getString("across_back_shoulder_width"));
            }
            if (!jMeasurement.getString("shoulder_drop").equals("null")) {
                measurement.setShoulder_drop(jMeasurement.getString("shoulder_drop"));
            }
//            if(!jMeasurement.getString("shoulder_slope_degrees").equals("null")) {
//                measurement.setShoulder_slope_degrees(jMeasurement.getString("shoulder_slope_degrees"));
//            }
            if (!jMeasurement.getString("arm_length").equals("null")) {
                measurement.setArm_length(jMeasurement.getString("arm_length"));
            }
            if (!jMeasurement.getString("wrist_girth").equals("null")) {
                measurement.setWrist_girth(jMeasurement.getString("wrist_girth"));
            }
            if (!jMeasurement.getString("upper_arm_girth").equals("null")) {
                measurement.setUpper_arm_girth(jMeasurement.getString("upper_arm_girth"));
            }
            if (!jMeasurement.getString("armscye_girth").equals("null")) {
                measurement.setArmscye_girth(jMeasurement.getString("armscye_girth"));
            }
            if (!jMeasurement.getString("height").equals("null")) {
                measurement.setHeight(jMeasurement.getString("height"));
            }
            if (!jMeasurement.getString("hip_height").equals("null")) {
                measurement.setHip_height(jMeasurement.getString("hip_height"));
            }

            MeasurementManager.getInstance(context).addMeasurement(measurement);
            MeasurementManager.getInstance(context).setSyncedOnce(measurement.getID());
            return measurement.getID();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private void getPics(String result, Context context, String ID) throws IOException {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jMeasurement = new JSONObject(jsonObject.getString("data"));
            String txt = jMeasurement.getString("images");
            JSONArray jArray = new JSONArray(txt);
            int len = jArray.length();
            boolean front = true;
            boolean side = true;
            boolean back = true;
            for (int i = 0; i < len; i++) {
                JSONObject jObject = new JSONObject(jArray.getString(i));
                if (jObject.getString("rel").equals("body_front") && front) {
                    syncPic.getPic(ID, context, PicTypes.FRONT, jObject.getString("href"));
                    front = false;
                } else if (jObject.getString("rel").equals("body_side") && side) {
                    syncPic.getPic(ID, context, PicTypes.SIDE, jObject.getString("href"));
                    side = false;
                } else if (jObject.getString("rel").equals("body_back") && back) {
                    syncPic.getPic(ID, context, PicTypes.BACK, jObject.getString("href"));
                    back = false;
                } else {
                    syncPic.getPic(ID, context, PicTypes.OTHER, jObject.getString("href"));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private String streamReader(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        Log.d("streamReader", result);
        return result;
    }

    public boolean delMeasurement(String id, String userID) {
        String out = null;
        String URL = baseURL + "/users/" + userID + "/measurements/" + id;
        int CON_TIMEOUT = 2000;
        int SOC_TIMEOUT = 3000;
        SyncMeasurement sm = new SyncMeasurement();
        InputStream inputStream = null;
        try {
            inputStream = sm.DELETE(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null) {
                out = sm.streamReader(inputStream);
                JSONObject jObject = new JSONObject(out);
                out = jObject.getString("data");
                if (out.equals("measurement record deleted")) {
                    return true;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    public String[] getDelList(String userID) {
        Log.e(TAG, "getDelList");
        String[] out = null;
        String URL = baseURL + "/users/" + userID + "deleted/measurements";
        SyncMeasurement sm = new SyncMeasurement();
        int CON_TIMEOUT = 2000;
        int SOC_TIMEOUT = 3000;

        InputStream inputStream = null;
        try {
            inputStream = sm.get(URL, CON_TIMEOUT, SOC_TIMEOUT).getEntity().getContent();
            if (inputStream != null) {
                out = sm.convertInputStreamToList(inputStream);
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return out;
    }
}
