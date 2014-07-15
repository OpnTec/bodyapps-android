/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.managers;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.fashiontec.bodyapps.db.DBContract;
import org.fashiontec.bodyapps.db.DatabaseHandler;
import org.fashiontec.bodyapps.models.Measurement;
import org.fashiontec.bodyapps.models.MeasurementListModel;

/**
 * Manages the DB requests to measurements table
 */
public class MeasurementManager {
	private SQLiteDatabase database;
	private DatabaseHandler dbHandler;
	private Context context;
	private static MeasurementManager measurementManager;

	private MeasurementManager(Context context) {
		dbHandler = DatabaseHandler.getInstance(context);
		this.context = context;
	}

	public static MeasurementManager getInstance(Context context) {
		if (measurementManager == null) {
			measurementManager = new MeasurementManager(context);
		}
		return measurementManager;
	}

    /**
     * Adds given measurement to the database
     * @param measurement
     */
	public void addMeasurement(Measurement measurement) {
		Log.d("measurementmanager", "addMeasurement");
        boolean available=isMeasurement(measurement.getID());
		this.database = this.dbHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContract.Measurement.COLUMN_NAME_ID, measurement.getID());
		values.put(DBContract.Measurement.COLUMN_NAME_USER_ID,
				measurement.getUserID());
		values.put(DBContract.Measurement.COLUMN_NAME_PERSON_ID,
				measurement.getPersonID());
		values.put(DBContract.Measurement.COLUMN_NAME_CREATED,
				measurement.getCreated());
		values.put(DBContract.Measurement.COLUMN_NAME_UNIT,
				measurement.getUnit());
		values.put(DBContract.Measurement.COLUMN_NAME_MID_NECK_GIRTH,
				measurement.getMid_neck_girth());
		values.put(DBContract.Measurement.COLUMN_NAME_BUST_GIRTH,
				measurement.getBust_girth());
		values.put(DBContract.Measurement.COLUMN_NAME_WAIST_GIRTH,
				measurement.getWaist_girth());
		values.put(DBContract.Measurement.COLUMN_NAME_HIP_GIRTH,
				measurement.getHip_girth());
		values.put(
				DBContract.Measurement.COLUMN_NAME_ACROSS_BACK_SHOULDER_WIDTH,
				measurement.getAcross_back_shoulder_width());
		values.put(DBContract.Measurement.COLUMN_NAME_SHOULDER_DROP,
				measurement.getShoulder_drop());
		values.put(DBContract.Measurement.COLUMN_NAME_SHOULDER_SLOPE_DEGREES,
				measurement.getShoulder_slope_degrees());
		values.put(DBContract.Measurement.COLUMN_NAME_ARM_LENGTH,
				measurement.getArm_length());
		values.put(DBContract.Measurement.COLUMN_NAME_UPPER_ARM_GIRTH,
				measurement.getUpper_arm_girth());
		values.put(DBContract.Measurement.COLUMN_NAME_ARMSCYE_GIRTH,
				measurement.getArmscye_girth());
		values.put(DBContract.Measurement.COLUMN_NAME_HEIGHT,
				measurement.getHeight());
		values.put(DBContract.Measurement.COLUMN_NAME_HIP_HEIGHT,
				measurement.getHip_height());
		values.put(DBContract.Measurement.COLUMN_NAME_WRIST_GIRTH,
				measurement.getWrist_girth());
        values.put(DBContract.Measurement.COLUMN_NAME_HEAD_GIRTH,
                measurement.getHead_girth());
        values.put(DBContract.Measurement.COLUMN_NAME_HEAD_AND_NECK_LENGTH,
                measurement.getHead_and_neck_length());
        values.put(DBContract.Measurement.COLUMN_NAME_UPPER_CHEST_GIRTH,
                measurement.getUpper_chest_girth());
        values.put(DBContract.Measurement.COLUMN_NAME_SHOULDER_LENGTH,
                measurement.getShoulder_length());
        values.put(DBContract.Measurement.COLUMN_NAME_SHOULDER_AND_ARM_LENGTH,
                measurement.getShoulder_and_arm_length());
        values.put(DBContract.Measurement.COLUMN_NAME_PIC_FRONT,
                measurement.getPic_front());
        values.put(DBContract.Measurement.COLUMN_NAME_PIC_SIDE,
                measurement.getPic_side());
        values.put(DBContract.Measurement.COLUMN_NAME_PIC_BACK,
                measurement.getPic_back());
        values.put(DBContract.Measurement.COLUMN_NAME_NOTES,measurement.getNotes());
        values.put(DBContract.Measurement.COLUMN_NAME_IS_SYNCED,measurement.isSynced());
        values.put(DBContract.Measurement.COLUMN_NAME_SHOULDER_TYPE,measurement.getShoulder_type());
        values.put(DBContract.Measurement.COLUMN_NAME_CHEST_TYPE,measurement.getChest_type());
        values.put(DBContract.Measurement.COLUMN_NAME_ARM_TYPE,measurement.getArm_type());
        values.put(DBContract.Measurement.COLUMN_NAME_BACK_SHAPE,measurement.getBack_shape());
        values.put(DBContract.Measurement.COLUMN_NAME_STOMACH_SHAPE,measurement.getStomach_shape());

        if(available){
            database.update(DBContract.Measurement.TABLE_NAME, values,
                    DBContract.Measurement.COLUMN_NAME_ID + "='" + measurement.getID()
                    + "'", null);
        }else{
            database.insert(DBContract.Measurement.TABLE_NAME, null, values);
        }

		database.close();
	}

	public void getch(String id) {
		Log.d("measurementmanager", "checkMeasurement");
		this.database = this.dbHandler.getReadableDatabase();
		Cursor cursor = database
				.query(DBContract.Measurement.TABLE_NAME,
						new String[] { DBContract.Measurement.COLUMN_NAME_MID_NECK_GIRTH },
						DBContract.Measurement.COLUMN_NAME_ID + " ='" + id
								+ "'", null, null, null, null);

		if (cursor.moveToFirst()) {

			String out = cursor.getString(0);
			System.out.println(out);
			cursor.close();
			database.close();
		}
	}

    /**
     * Returns a list to populate saved measurements list.
     * @return
     */
	public List<MeasurementListModel> getList() {

		List<MeasurementListModel> measurementList = new ArrayList<MeasurementListModel>();

		String user = UserManager.getInstance(this.context).getCurrent();
		String selectQuery;
		if (user == null) {
			Log.d("measurementmanager", "user null");
			selectQuery = "SELECT  C." + DBContract.Measurement.COLUMN_NAME_ID
					+ ", " + DBContract.Measurement.COLUMN_NAME_PERSON_ID
					+ " AS pid" + ", "
					+ DBContract.Measurement.COLUMN_NAME_CREATED + ", "
                    + DBContract.Measurement.COLUMN_NAME_IS_SYNCED + ", "
					+ DBContract.Person.COLUMN_NAME_NAME + ", "
					+ DBContract.Person.COLUMN_NAME_EMAIL + " FROM "
					+ DBContract.Measurement.TABLE_NAME + " AS C JOIN "
					+ DBContract.Person.TABLE_NAME + " AS R ON C."
					+ DBContract.Measurement.COLUMN_NAME_PERSON_ID + " = R."
					+ DBContract.Person.COLUMN_NAME_ID
					+ " WHERE C."+DBContract.Measurement.COLUMN_NAME_USER_ID+" = 'NoUser'";
			System.out.println(selectQuery);
		} else {
			Log.d("measurementmanager", "user exists");
			selectQuery = "SELECT  C." + DBContract.Measurement.COLUMN_NAME_ID
					+ ", " + DBContract.Measurement.COLUMN_NAME_PERSON_ID
					+ " AS pid" + ", "
					+ DBContract.Measurement.COLUMN_NAME_CREATED + ", "
                    + DBContract.Measurement.COLUMN_NAME_IS_SYNCED + ", "
					+ DBContract.Person.COLUMN_NAME_NAME + ", "
					+ DBContract.Person.COLUMN_NAME_EMAIL + " FROM "
					+ DBContract.Measurement.TABLE_NAME + " AS C JOIN "
					+ DBContract.Person.TABLE_NAME + " AS R ON C."
					+ DBContract.Measurement.COLUMN_NAME_PERSON_ID + " = R."
					+ DBContract.Person.COLUMN_NAME_ID
					+ " WHERE C."+DBContract.Measurement.COLUMN_NAME_USER_ID+" = '"+user+"'";
			System.out.println(selectQuery);
		}

		SQLiteDatabase db = this.dbHandler.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				MeasurementListModel msmnt = new MeasurementListModel();
				msmnt.setID(cursor.getString(0));
				msmnt.setPersonID(Integer.parseInt(cursor.getString(1)));
                if(cursor.getInt(3)==1){
                    msmnt.setSynced(true);
                }
				msmnt.setCreated(cursor.getString(2));
				msmnt.setPersonName(cursor.getString(4));
				msmnt.setPersonEmail(cursor.getString(5));
				// Adding msmnt to list
				measurementList.add(msmnt);
			} while (cursor.moveToNext());
		}
		return measurementList;
	}

    /**
     * Set the given user ID to all the measurements which have no user ID.
     * @param ID
     */
	public void setUserID(String ID) {
		Log.d("measurementManager", "setUserID");

		this.database = this.dbHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContract.Measurement.COLUMN_NAME_USER_ID, ID);
		database.update(DBContract.Measurement.TABLE_NAME, values,
				DBContract.Measurement.COLUMN_NAME_USER_ID + "='" + "NoID"
						+ "'", null);
		database.update(DBContract.Measurement.TABLE_NAME, values,
				DBContract.Measurement.COLUMN_NAME_USER_ID + "='" + "NoUser"
						+ "'", null);
		database.close();
	}

    /**
     * Get measurement by measurement ID
     * @param ID
     * @return
     */
	public Measurement getMeasurement(String ID){
		Log.d("measurementmanager", "getMeasurement");
		this.database = this.dbHandler.getReadableDatabase();
		Cursor cursor = database
				.query(DBContract.Measurement.TABLE_NAME,DBContract.Measurement.allColumns,
						DBContract.Measurement.COLUMN_NAME_ID + " ='" + ID
								+ "'", null, null, null, null);

		Measurement out=null;
		if (cursor.moveToFirst()) {
            out=createMeasurement(cursor);
			cursor.close();
			database.close();
		}
		return out;
	}

    /**
     * Check whether the given measurement exists
     * @param ID
     * @return
     */
    public boolean isMeasurement(String ID){
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database
                .query(DBContract.Measurement.TABLE_NAME,DBContract.Measurement.allColumns,
                        DBContract.Measurement.COLUMN_NAME_ID + " ='" + ID
                                + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            database.close();
            return true;
        }else {
            cursor.close();
            database.close();
            return false;
        }
    }

    /**
     * Checks for un synced measurements and gets them
     * @return
     */
    public Measurement getMeasurementSync(){
        Measurement out=null;
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database
                .query(DBContract.Measurement.TABLE_NAME,DBContract.Measurement.allColumns,
                        DBContract.Measurement.COLUMN_NAME_IS_SYNCED + " = " +0
                        , null, null, null, null);
        if (cursor.moveToFirst()) {
            out=createMeasurement(cursor);
            System.out.println("MeasurementManager.getMeasurementSync"+out.getID());
            cursor.close();
            database.close();
        }
        return out;
    }

    /**
     * Creates the measurement object using the fields in the given Cursor
     * @param cursor
     * @return
     */
    private Measurement createMeasurement(Cursor cursor){
        Measurement ms=null;
        ms=new Measurement(cursor.getString(0),cursor.getString(1),cursor.getInt(2),Integer.parseInt(cursor.getString(6)));
        ms.setCreated(cursor.getString(3));
        ms.setMid_neck_girth(cursor.getString(7));
        ms.setBust_girth(cursor.getString(8));
        ms.setWaist_girth(cursor.getString(9));
        ms.setHip_girth(cursor.getString(10));
        ms.setAcross_back_shoulder_width(cursor.getString(11));
        ms.setShoulder_drop(cursor.getString(12));
        ms.setShoulder_slope_degrees(cursor.getString(13));
        ms.setArm_length(cursor.getString(14));
        ms.setUpper_arm_girth(cursor.getString(15));
        ms.setArmscye_girth(cursor.getString(16));
        ms.setHeight(cursor.getString(17));
        ms.setHip_height(cursor.getString(18));
        ms.setWrist_girth(cursor.getString(19));
        ms.setHead_girth(cursor.getString(20));
        ms.setHead_and_neck_length(cursor.getString(21));
        ms.setUpper_chest_girth(cursor.getString(22));
        ms.setShoulder_length(cursor.getString(23));
        ms.setShoulder_and_arm_length(cursor.getString(24));
        ms.setPic_front(cursor.getString(25));
        ms.setPic_side(cursor.getString(26));
        ms.setPic_back(cursor.getString(27));
        ms.setNotes(cursor.getString(28));
        ms.setShoulder_type(cursor.getInt(30));
        ms.setArm_type(cursor.getInt(31));
        ms.setChest_type(cursor.getInt(32));
        ms.setBack_shape(cursor.getInt(33));
        ms.setStomach_shape(cursor.getInt(34));

        return ms;
    }
}
