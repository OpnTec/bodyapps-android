/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapp.db;

/**
 *Contains all the constants used as table names and attributes in DB
 */
public abstract class DBContract {
	private static final String TEXT_TYPE = " TEXT";
	private static final String NUMBER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";

	/**
	 *Person table
	 */
	public static abstract class Person {
		public static final String TABLE_NAME = "Person";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_EMAIL = "email";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_GENDER = "gender";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+ Person.TABLE_NAME + " (" 
				+ Person.COLUMN_NAME_ID + " INTEGER PRIMARY KEY autoincrement" + COMMA_SEP 
				+ Person.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP 
				+ Person.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP 
				+ Person.COLUMN_NAME_GENDER + NUMBER_TYPE
				+ ")";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ Person.TABLE_NAME;

		public static final String[] allColumns = {  
			Person.COLUMN_NAME_EMAIL,
			Person.COLUMN_NAME_NAME,
			Person.COLUMN_NAME_GENDER };

	
	}

	/**
	 *Table user
	 */
	public static abstract class User {
		public static final String TABLE_NAME = "User";
		public static final String COLUMN_NAME_EMAIL = "email";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_IS_CURRENT = "current_user";
		public static final String COLUMN_NAME_ID = "id";
		
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+User.TABLE_NAME+"("
				+User.COLUMN_NAME_ID+TEXT_TYPE+COMMA_SEP
				+User.COLUMN_NAME_EMAIL+" TEXT PRIMARY KEY"+COMMA_SEP
				+User.COLUMN_NAME_NAME+TEXT_TYPE+COMMA_SEP
				+User.COLUMN_NAME_IS_CURRENT+NUMBER_TYPE
				+")";
		
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ User.TABLE_NAME;
		
		public static final String[] allColumns = {
			User.COLUMN_NAME_ID,
			User.COLUMN_NAME_EMAIL,
			User.COLUMN_NAME_NAME,
			User.COLUMN_NAME_IS_CURRENT
		};

	}
	
	/**
	 *Table measurement
	 */
	public static abstract class Measurement {
		public static final String TABLE_NAME = "Measurement";
		public static final String COLUMN_NAME_ID = "id";
		public static final String COLUMN_NAME_USER_ID = "user_id";
		public static final String COLUMN_NAME_PERSON_ID = "person_id";
		public static final String COLUMN_NAME_CREATED = "created";
		public static final String COLUMN_NAME_LAST_SYNC = "sync";
		public static final String COLUMN_NAME_LAST_EDIT = "edit";
		public static final String COLUMN_NAME_UNIT = "unit";
		public static final String COLUMN_NAME_MID_NECK_GIRTH = "mid_neck_girth";
		public static final String COLUMN_NAME_BUST_GIRTH = "bust_girth";
		public static final String COLUMN_NAME_WAIST_GIRTH = "waist_girth";
		public static final String COLUMN_NAME_HIP_GIRTH = "hip_girth";
		public static final String COLUMN_NAME_ACROSS_BACK_SHOULDER_WIDTH = "across_back_shoulder_width";
		public static final String COLUMN_NAME_SHOULDER_DROP = "shoulder_drop";
		public static final String COLUMN_NAME_SHOULDER_SLOPE_DEGREES = "shoulder_slope_degrees";
		public static final String COLUMN_NAME_ARM_LENGTH = "arm_length";
		public static final String COLUMN_NAME_UPPER_ARM_GIRTH = "upper_arm_girth";
		public static final String COLUMN_NAME_ARMSCYE_GIRTH = "armscye_girth";
		public static final String COLUMN_NAME_HEIGHT = "height";
		public static final String COLUMN_NAME_HIP_HEIGHT = "hip_height";
		public static final String COLUMN_NAME_WRIST_GIRTH = "wrist_girth";
        public static final String COLUMN_NAME_HEAD_GIRTH = "head_girth";
        public static final String COLUMN_NAME_HEAD_AND_NECK_LENGTH= "head_and_neck_length";
        public static final String COLUMN_NAME_UPPER_CHEST_GIRTH = "upper_chest_girth";
        public static final String COLUMN_NAME_SHOULDER_LENGTH = "shoulder_length";
        public static final String COLUMN_NAME_SHOULDER_AND_ARM_LENGTH = "shoulder_and_arm_length";
        public static final String COLUMN_NAME_PIC_FRONT = "pic_front";
        public static final String COLUMN_NAME_PIC_SIDE = "pic_side";
        public static final String COLUMN_NAME_PIC_BACK = "pic_back";
		
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+Measurement.TABLE_NAME+"("
				+Measurement.COLUMN_NAME_ID+" TEXT PRIMARY KEY"+COMMA_SEP
				+Measurement.COLUMN_NAME_USER_ID+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_PERSON_ID+NUMBER_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_CREATED+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_LAST_SYNC+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_LAST_EDIT+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_UNIT+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_MID_NECK_GIRTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_BUST_GIRTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_WAIST_GIRTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_HIP_GIRTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_ACROSS_BACK_SHOULDER_WIDTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_SHOULDER_DROP+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_SHOULDER_SLOPE_DEGREES+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_ARM_LENGTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_UPPER_ARM_GIRTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_ARMSCYE_GIRTH+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_HEIGHT+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_HIP_HEIGHT+TEXT_TYPE+COMMA_SEP
				+Measurement.COLUMN_NAME_WRIST_GIRTH+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_HEAD_GIRTH+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_HEAD_AND_NECK_LENGTH+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_UPPER_CHEST_GIRTH+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_SHOULDER_LENGTH+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_SHOULDER_AND_ARM_LENGTH+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_PIC_FRONT+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_PIC_SIDE+TEXT_TYPE+COMMA_SEP
                +Measurement.COLUMN_NAME_PIC_BACK+TEXT_TYPE
				+")";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ Measurement.TABLE_NAME;
		
		public static final String[] allColumns = {
			Measurement.COLUMN_NAME_ID,
			Measurement.COLUMN_NAME_USER_ID,
			Measurement.COLUMN_NAME_PERSON_ID,
			Measurement.COLUMN_NAME_CREATED,
			Measurement.COLUMN_NAME_LAST_SYNC,
			Measurement.COLUMN_NAME_LAST_EDIT,
			Measurement.COLUMN_NAME_UNIT,
			Measurement.COLUMN_NAME_MID_NECK_GIRTH,
			Measurement.COLUMN_NAME_BUST_GIRTH,
			Measurement.COLUMN_NAME_WAIST_GIRTH,
			Measurement.COLUMN_NAME_HIP_GIRTH,
			Measurement.COLUMN_NAME_ACROSS_BACK_SHOULDER_WIDTH,
			Measurement.COLUMN_NAME_SHOULDER_DROP,
			Measurement.COLUMN_NAME_SHOULDER_SLOPE_DEGREES,
			Measurement.COLUMN_NAME_ARM_LENGTH,
			Measurement.COLUMN_NAME_UPPER_ARM_GIRTH,
			Measurement.COLUMN_NAME_ARMSCYE_GIRTH,
			Measurement.COLUMN_NAME_HEIGHT,
			Measurement.COLUMN_NAME_HIP_HEIGHT,
			Measurement.COLUMN_NAME_WRIST_GIRTH,
            Measurement.COLUMN_NAME_HEAD_GIRTH,
            Measurement.COLUMN_NAME_HEAD_AND_NECK_LENGTH,
            Measurement.COLUMN_NAME_UPPER_CHEST_GIRTH,
            Measurement.COLUMN_NAME_SHOULDER_LENGTH,
            Measurement.COLUMN_NAME_SHOULDER_AND_ARM_LENGTH,
            Measurement.COLUMN_NAME_PIC_FRONT,
            Measurement.COLUMN_NAME_PIC_SIDE,
            Measurement.COLUMN_NAME_PIC_BACK

		};
	}
	
	/**
	 *Table delete
	 */
	public static abstract class Delete {
		public static final String TABLE_NAME = "Deletes";
		public static final String COLUMN_NAME_FILE_ID = "file_id";
		public static final String COLUMN_NAME_DEL_DATE = "date";
		
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
				+Delete.TABLE_NAME+"("
				+Delete.COLUMN_NAME_FILE_ID+" INTEGER PRIMARY KEY"+COMMA_SEP
				+Delete.COLUMN_NAME_DEL_DATE+TEXT_TYPE
				+")";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ Delete.TABLE_NAME;
		public static final String[] allColumns = {
			Delete.COLUMN_NAME_FILE_ID,
			Delete.COLUMN_NAME_DEL_DATE
		};
	}

}
