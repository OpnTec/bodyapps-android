/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.fashiontec.bodyapps.db.DBContract;
import org.fashiontec.bodyapps.db.DatabaseHandler;
import org.fashiontec.bodyapps.models.User;

/**
 * Manages the DB requests to user table
 */
public class UserManager {
    private SQLiteDatabase database;
    private DatabaseHandler dbHandler;
    private static UserManager userManager;
    static final String TAG = UserManager.class.getName();

    private UserManager(Context context) {
        dbHandler = DatabaseHandler.getInstance(context);
    }

    public static UserManager getInstance(Context context) {
        if (userManager == null) {
            userManager = new UserManager(context);
        }
        return userManager;
    }

    /**
     * Adds user to DB
     *
     * @param user
     */
    public void addUser(User user) {
        Log.d(TAG, "addUser");
        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_ID, user.getId());
        values.put(DBContract.User.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(DBContract.User.COLUMN_NAME_NAME, user.getName());
        values.put(DBContract.User.COLUMN_NAME_IS_CURRENT, 1);
        values.put(DBContract.User.COLUMN_NAME_AUTO_SYNC, user.getAutoSync());
        values.put(DBContract.User.COLUMN_NAME_LAST_SYNC, 0);
        database.insert(DBContract.User.TABLE_NAME, null, values);
        database.close();
    }

    /**
     * Check if the user exists.
     *
     * @param user
     * @return
     */
    public String isUser(User user) {
        Log.d(TAG, "isUser");
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.User.TABLE_NAME,
                new String[]{DBContract.User.COLUMN_NAME_ID},
                DBContract.User.COLUMN_NAME_EMAIL + " ='" + user.getEmail()
                        + "'", null, null, null, null
        );

        if (cursor.moveToFirst()) {

            String out = cursor.getString(0);
            cursor.close();
            database.close();
            return out;
        } else {
            return null;
        }
    }

    /**
     * Sets the given user as the current user.
     *
     * @param user
     */
    public void setCurrent(User user) {
        Log.d(TAG, "setCurrent");
        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_IS_CURRENT, 1);
        database.update(DBContract.User.TABLE_NAME, values,
                DBContract.User.COLUMN_NAME_EMAIL + "='" + user.getEmail()
                        + "'", null
        );
        database.close();

    }

    /**
     * Gets the current user.
     *
     * @return
     */
    public String getCurrent() {
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.User.TABLE_NAME,
                new String[]{DBContract.User.COLUMN_NAME_ID},
                DBContract.User.COLUMN_NAME_IS_CURRENT + " =" + 1, null, null,
                null, null);

        if (cursor.moveToFirst()) {

            String out = cursor.getString(0);
            cursor.close();
            database.close();
            return out;
        } else {
            return null;
        }
    }

    /**
     * Delets given user from DB
     *
     * @param user
     */
    public void delUser(User user) {
        this.database = this.dbHandler.getWritableDatabase();
        database.delete(DBContract.User.TABLE_NAME,
                DBContract.User.COLUMN_NAME_ID + " ='" + user.getId() + "'",
                null);
        database.close();
    }

    /**
     * Set ID given to the user from API.
     *
     * @param user
     */
    public void setID(User user) {
        Log.d(TAG, "setID");
        String userID = user.getId();

        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_ID, userID);
        database.update(DBContract.User.TABLE_NAME, values,
                DBContract.User.COLUMN_NAME_EMAIL + "='" + user.getEmail()
                        + "'", null
        );
        database.close();
    }

    /**
     * Unset as the current user
     */
    public void unsetCurrent() {
        Log.d(TAG, "unsetCurrent");
        String email = getCurrentEmail();
        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_IS_CURRENT, 0);
        database.update(DBContract.User.TABLE_NAME, values,
                DBContract.User.COLUMN_NAME_EMAIL + "='" + email + "'", null);
        database.close();
    }

    /**
     * Get current user's email.
     *
     * @return
     */
    public String getCurrentEmail() {
        Log.d(TAG, "getCurrentEmail");
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.User.TABLE_NAME,
                new String[]{DBContract.User.COLUMN_NAME_EMAIL},
                DBContract.User.COLUMN_NAME_IS_CURRENT + " =" + 1, null, null,
                null, null);

        if (cursor.moveToFirst()) {

            String out = cursor.getString(0);
            cursor.close();
            database.close();
            return out;
        } else {
            return null;
        }
    }

    /**
     * Enable or disable auto sync.
     *
     * @param autoSync
     */
    public void setAutoSync(Boolean autoSync) {
        Log.d(TAG, "setAutoSync");
        String email = getCurrentEmail();
        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_AUTO_SYNC, autoSync);
        database.update(DBContract.User.TABLE_NAME, values,
                DBContract.User.COLUMN_NAME_EMAIL + "='" + email + "'", null);
        database.close();
    }

    /**
     * Check whether the current user has turned on auto sync.
     *
     * @return
     */
    public boolean getAutoSync() {
        Log.d(TAG, "getAutoSync");
        String email = getCurrentEmail();
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.User.TABLE_NAME,
                new String[]{DBContract.User.COLUMN_NAME_AUTO_SYNC},
                DBContract.User.COLUMN_NAME_EMAIL + " = '" + email + "'", null, null,
                null, null);

        if (cursor.moveToFirst()) {

            int out = cursor.getInt(0);
            cursor.close();
            database.close();
            if (out == 1) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    /**
     * Sets the time of last sync.
     *
     * @param timeStamp
     */
    public void setLastSync(long timeStamp) {
        Log.d(TAG, "setLastSync");
        String email = getCurrentEmail();
        this.database = this.dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.User.COLUMN_NAME_LAST_SYNC, timeStamp);
        database.update(DBContract.User.TABLE_NAME, values,
                DBContract.User.COLUMN_NAME_EMAIL + "='" + email + "'", null);
        database.close();
    }

    /**
     * Gets the time of last sync
     *
     * @return
     */
    public long getLastSync() {
        Log.d(TAG, "getAutoSync");
        String email = getCurrentEmail();
        this.database = this.dbHandler.getReadableDatabase();
        Cursor cursor = database.query(DBContract.User.TABLE_NAME,
                new String[]{DBContract.User.COLUMN_NAME_LAST_SYNC},
                DBContract.User.COLUMN_NAME_EMAIL + " = '" + email + "'", null, null,
                null, null);

        if (cursor.moveToFirst()) {

            long out = cursor.getLong(0);
            cursor.close();
            database.close();
            return out;

        } else {
            return -1;
        }
    }
}
