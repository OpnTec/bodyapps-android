/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapp.managers;

import org.fashiontec.bodyapp.db.DBContract;
import org.fashiontec.bodyapp.db.DatabaseHandler;
import org.fashiontec.bodyapp.models.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Manages the DB requests to user table
 */
public class UserManager {
	private SQLiteDatabase database;
	private DatabaseHandler dbHandler;
	private static UserManager userManager;

	private UserManager(Context context) {
		dbHandler = DatabaseHandler.getInstance(context);
	}

	public static UserManager getInstance(Context context) {
		if (userManager == null) {
			userManager = new UserManager(context);
		}
		return userManager;
	}

	public void addUser(User user) {
		Log.d("usermanager", "addUser");
		this.database = this.dbHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContract.User.COLUMN_NAME_ID, user.getId());
		values.put(DBContract.User.COLUMN_NAME_EMAIL, user.getEmail());
		values.put(DBContract.User.COLUMN_NAME_NAME, user.getName());
		values.put(DBContract.User.COLUMN_NAME_IS_CURRENT, 1);
		database.insert(DBContract.User.TABLE_NAME, null, values);
		database.close();
	}

	public String isUser(User user) {
		Log.d("usermanager", "isUser");
		this.database = this.dbHandler.getReadableDatabase();
		Cursor cursor = database.query(DBContract.User.TABLE_NAME,
				new String[] { DBContract.User.COLUMN_NAME_ID },
				DBContract.User.COLUMN_NAME_EMAIL + " ='" + user.getEmail()
						+ "'", null, null, null, null);

		if (cursor.moveToFirst()) {

			String out = cursor.getString(0);
			cursor.close();
			database.close();
			return out;
		} else {
			return null;
		}
	}

	public void setCurrent(User user) {
		Log.d("usermanager", "setCurrent");
		this.database = this.dbHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContract.User.COLUMN_NAME_IS_CURRENT, 1);
		database.update(DBContract.User.TABLE_NAME, values,
				DBContract.User.COLUMN_NAME_EMAIL + "='" + user.getEmail()
						+ "'", null);
		database.close();

	}

	public String getCurrent() {
		this.database = this.dbHandler.getReadableDatabase();
		Cursor cursor = database.query(DBContract.User.TABLE_NAME,
				new String[] { DBContract.User.COLUMN_NAME_ID },
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

	public void delUser(User user) {
		this.database = this.dbHandler.getWritableDatabase();
		database.delete(DBContract.User.TABLE_NAME,
				DBContract.User.COLUMN_NAME_ID + " ='" + user.getId() + "'",
				null);
		database.close();
	}

	public void setID(User user) {
		Log.d("usermanager", "setID");
		String userID = user.getId();

		this.database = this.dbHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContract.User.COLUMN_NAME_ID, userID);
		database.update(DBContract.User.TABLE_NAME, values,
				DBContract.User.COLUMN_NAME_EMAIL + "='" + user.getEmail()
						+ "'", null);
		database.close();
	}

	public void unsetCurrent() {
		Log.d("usermanager", "unsetCurrent");
		String email = getCurrentEmail();
		this.database = this.dbHandler.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBContract.User.COLUMN_NAME_IS_CURRENT, 0);
		database.update(DBContract.User.TABLE_NAME, values,
				DBContract.User.COLUMN_NAME_EMAIL + "='" + email + "'", null);
		database.close();
	}

	public String getCurrentEmail() {
		Log.d("usermanager", "getCurrentEmail");
		this.database = this.dbHandler.getReadableDatabase();
		Cursor cursor = database.query(DBContract.User.TABLE_NAME,
				new String[] { DBContract.User.COLUMN_NAME_EMAIL },
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
}
