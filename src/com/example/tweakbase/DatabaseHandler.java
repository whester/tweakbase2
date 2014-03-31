package com.example.tweakbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	final String TAG = "DatabaseHandler";
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "tweakbaseDatabase";

	// Table name
	private static final String TABLE_LOCATION = "location";
	//Ringer Mode Table
	private static final String TABLE_RINGERMODE = "ringermode";
	//Ringer Mode Profiles
	private static final String TABLE_RM_PROFILES = "ringermode_profiles";

	//Accelerometer Data
	private static final String TABLE_ACCELEROMETER = "accelerometer";
	//App Opened
	private static final String TABLE_APP_OPENED = "app_opened";

	//Table Columns names
	private static final String KEY_LOC_ID = "location_id";
	private static final String KEY_LOC_LAT = "location_lat";
	private static final String KEY_LOC_LON = "location_lon";

	private static final String KEY_LOC_INTERVAL_ID = "location_interval_id";
	private static final String KEY_LOC_DAY_OF_WEEK = "location_day_of_week";
	private static final String KEY_LOC_TIMESTAMP = "location_timestamp";


	//Ringer Mode Table Columns names
	private static final String KEY_RM_ID = "ringermode_id";
	private static final String KEY_RM_INTERVAL_ID = "ringermode_interval_id";		
	private static final String KEY_RM_DAY_OF_WEEK = "ringermode_dayofweek";
	private static final String KEY_RM_LAT = "ringermode_lat";
	private static final String KEY_RM_LON = "ringermode_lon";
	private static final String KEY_RM_TYPE = "ringermode_type";
	private static final String KEY_RM_TIMESTAMP = "ringermode_timestamp";

	//Table ringermode_profiles
	private static final String KEY_RMP_ID = "ringermode_profiles_id";
	private static final String KEY_RMP_START_INTERVAL_ID = "ringermode_profiles_start_interval_id";	
	private static final String KEY_RMP_END_INTERVAL_ID = "ringermode_profiles_end_interval_id";	
	private static final String KEY_RMP_DAY_OF_WEEK = "ringermode_profiles_dayofweek";
	private static final String KEY_RMP_LAT = "ringermode_profiles_lat";
	private static final String KEY_RMP_LON = "ringermode_profiles_lon";
	private static final String KEY_RMP_TYPE = "ringermode_profiles_type";
	private static final String KEY_RMP_ACTIVE = "ringermode_profiles_active";
	private static final String KEY_RMP_TIMESTAMP = "ringermode_profiles_timestamp";


	//Table Accelerometer
	private static final String KEY_ACC_ID = "accelerometer_id";
	private static final String KEY_ACC_TIMESTAMP = "accelerometer_timestamp"; 
	private static final String KEY_ACC_DAY_OF_WEEK = "accelerometer_dayofweek";
	private static final String KEY_ACC_LON = "accelerometer_lon";
	private static final String KEY_ACC_LAT = "accelerometer_lat";
	private static final String KEY_ACC_X = "accelerometer_x";
	private static final String KEY_ACC_Y = "accelerometer_y";
	private static final String KEY_ACC_Z = "accelerometer_z";


	//Table app_opened
	private static final String KEY_APP_ID = "app_id";
	private static final String KEY_APP_PACKAGE_NAME = "app_package_name_id";		
	private static final String KEY_APP_NAME = "app_name";
	private static final String KEY_APP_INTERVAL_ID = "app_interval_id";
	private static final String KEY_APP_DAY_OF_WEEK = "app_day_of_week";
	private static final String KEY_APP_LAT = "app_lat";
	private static final String KEY_APP_LON = "app_lon";
	private static final String KEY_APP_TIMESTAMP = "app_timestamp";


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
				+ KEY_LOC_ID + " INTEGER PRIMARY KEY," 
				+ KEY_LOC_LAT + " DOUBLE,"
				+ KEY_LOC_LON + " DOUBLE" + "," 
				+ KEY_LOC_INTERVAL_ID + " INTEGER,"
				+ KEY_LOC_DAY_OF_WEEK + " INTEGER," 
				+ KEY_LOC_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP" +")";
		db.execSQL(CREATE_CONTACTS_TABLE);

		String CREATE_RINGERMODE_TABLE = "CREATE TABLE " + TABLE_RINGERMODE + "("
				+ KEY_RM_ID + " INTEGER PRIMARY KEY,"
				+ KEY_RM_INTERVAL_ID + " INTEGER,"
				+ KEY_RM_DAY_OF_WEEK + " INTEGER," 
				+ KEY_RM_LAT + " DOUBLE," 
				+ KEY_RM_LON + " DOUBLE," 
				+ KEY_RM_TYPE + " INTEGER," 
				+ KEY_RM_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP" + ")";
		db.execSQL(CREATE_RINGERMODE_TABLE);

		String CREATE_RINGERMODE_PROFILES_TABLE = "CREATE TABLE " + TABLE_RM_PROFILES + "("
				+ KEY_RMP_ID + " INTEGER PRIMARY KEY," 
				+ KEY_RMP_START_INTERVAL_ID + " INTEGER,"
				+ KEY_RMP_END_INTERVAL_ID + " INTEGER,"
				+ KEY_RMP_DAY_OF_WEEK + " INTEGER," 
				+ KEY_RMP_LAT + " DOUBLE," 
				+ KEY_RMP_LON + " DOUBLE," 
				+ KEY_RMP_TYPE + " INTEGER,"
				+ KEY_RMP_ACTIVE + " BOOLEAN," 
				+ KEY_RMP_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP" + ")";
		db.execSQL(CREATE_RINGERMODE_PROFILES_TABLE);


		String CREATE_ACCELEROMETER_TABLE = "CREATE TABLE " + TABLE_ACCELEROMETER + "("
				+ KEY_ACC_ID + "INTEGER PRIMARY KEY,"
				+ KEY_ACC_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," 
				+ KEY_ACC_DAY_OF_WEEK + " INTEGER,"
				+ KEY_ACC_LAT + " DOUBLE," 
				+ KEY_ACC_LON + " DOUBLE," 
				+ KEY_ACC_X + " DOUBLE,"
				+ KEY_ACC_Y + " DOUBLE,"
				+ KEY_ACC_Z + " DOUBLE" + ")";
		db.execSQL(CREATE_ACCELEROMETER_TABLE);


		String CREATE_APP_OPENED_TABLE = "CREATE TABLE " + TABLE_APP_OPENED + "("
				+ KEY_APP_ID + " INTEGER PRIMARY KEY,"
				+ KEY_APP_PACKAGE_NAME + " VARCHAR,"
				+ KEY_APP_NAME + " VARCHAR,"
				+ KEY_APP_INTERVAL_ID + " INTEGER,"
				+ KEY_APP_DAY_OF_WEEK + " INTEGER," 
				+ KEY_APP_LAT + " DOUBLE," 
				+ KEY_APP_LON + " DOUBLE,"
				+ KEY_APP_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP" + ")";
		db.execSQL(CREATE_APP_OPENED_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RINGERMODE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RM_PROFILES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCELEROMETER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_OPENED);

		// Create tables again
		onCreate(db);
	}

	public void addLocation(TBLocation location) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LOC_LAT, location.getLatitude());
		values.put(KEY_LOC_LON, location.getLongitude());
		values.put(KEY_LOC_INTERVAL_ID, location.getIntervalId());
		values.put(KEY_LOC_DAY_OF_WEEK, location.getDayOfWeek());

		// Inserting Row
		db.insert(TABLE_LOCATION, null, values);
		db.close(); // Closing database connection
	}

	public void addRingermode(TBRingermode ringermode){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_RM_DAY_OF_WEEK, ringermode.getDayOfWeek());
		values.put(KEY_RM_INTERVAL_ID, ringermode.getIntervalId());
		values.put(KEY_RM_LAT, ringermode.getLatitude());
		values.put(KEY_RM_LON, ringermode.getLongitude());
		values.put(KEY_RM_TYPE, ringermode.getType());

		db.insert(TABLE_RINGERMODE, null, values);
		db.close();
	}

	public long addRMProfile(TBRingermodeProfiles rmprofile){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_RMP_DAY_OF_WEEK, rmprofile.getDayOfWeek());
		values.put(KEY_RMP_START_INTERVAL_ID, rmprofile.getIntervalStartId());
		values.put(KEY_RMP_END_INTERVAL_ID, rmprofile.getIntervalEndId());
		values.put(KEY_RMP_LAT, rmprofile.getLatitude());
		values.put(KEY_RMP_LON, rmprofile.getLongitude());
		values.put(KEY_RMP_TYPE, rmprofile.getType());
		values.put(KEY_RMP_ACTIVE, rmprofile.getActive());
		
		long ret = db.insert(TABLE_RM_PROFILES, null, values);
		db.close();
		return ret;
	}


	public void addACCProfile(TBAccelerometer accel){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ACC_DAY_OF_WEEK,accel.getDayOfWeek() );
		values.put(KEY_ACC_LAT, accel.getLatitude());
		values.put(KEY_ACC_LON, accel.getLongitude());
		values.put(KEY_ACC_X, accel.getXcoordinate());
		values.put(KEY_ACC_Y, accel.getYcoordinate());
		values.put(KEY_ACC_Z, accel.getZcoordinate());

		db.insert(TABLE_ACCELEROMETER, null, values);

	}

	public void addAppOpened(TBAppOpened appopened){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_APP_DAY_OF_WEEK, appopened.getDayOfWeek());
		values.put(KEY_APP_INTERVAL_ID, appopened.getIntervalId());
		values.put(KEY_APP_LAT, appopened.getLatitude());
		values.put(KEY_APP_LON, appopened.getLongitude());
		values.put(KEY_APP_NAME, appopened.getAppName());
		values.put(KEY_APP_PACKAGE_NAME, appopened.getAppPackageName());

		db.insert(TABLE_APP_OPENED, null, values);
		db.close();
	}

	public List<TBLocation> getAllLocations() {
		List<TBLocation> locList = new ArrayList<TBLocation>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TBLocation location = new TBLocation();
				location.setLatitude(cursor.getDouble(1));
				location.setLongitude(cursor.getDouble(2));
				location.setIntervalId(cursor.getInt(3));
				location.setDayOfWeek(cursor.getInt(4));
				// Adding location to list
				locList.add(location);
			} while (cursor.moveToNext());
		}
		db.close();
		return locList;
	}
	
	public void setProfileAsInUse(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("KEY_RMP_ACTIVE", "true");
		if (db.update(TABLE_RM_PROFILES, cv, "_id "+"="+id, null) == 0) {
			Log.d(TAG, "Error setting profile " + id + " as in use");
		} else {
			Log.d(TAG, "Successfully set profile " + id + " as in use");
		}
	}
	
	public void deleteProfile(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (db.delete(TABLE_RM_PROFILES, KEY_RMP_ID + "=" + id, null) == 0) {
			Log.d(TAG, "Error deleting profile " + id);
		} else {
			Log.d(TAG, "Successfully removed profile " + id);
		}
	}
	
	public boolean profileInUse(long id) {
		String selectQuery = "SELECT " + KEY_RMP_ACTIVE + " FROM " + TABLE_RINGERMODE + " WHERE " + KEY_RMP_ID + "=" + id;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			if (cursor.getInt(0) == 1) {
				return true;
			} else {
				return false;
			}
		}
		db.close();
		return false;	// Profile probably deleted
	}
	
	public List<TBRingermode> getAllRingermodes() {
		List<TBRingermode> locList = new ArrayList<TBRingermode>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RINGERMODE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TBRingermode ringermode = new TBRingermode();
				ringermode.setLatitude(cursor.getDouble(3));
				ringermode.setLongitude(cursor.getDouble(4));
				ringermode.setIntervalId(cursor.getInt(1));
				ringermode.setDayOfWeek(cursor.getInt(2));
				ringermode.setType(cursor.getInt(5));
				// Adding location to list
				locList.add(ringermode);
			} while (cursor.moveToNext());
		}
		db.close();
		return locList;
	}
	
	public List<TBRingermodeProfiles> getAllRMP() {
		List<TBRingermodeProfiles> locList = new ArrayList<TBRingermodeProfiles>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_RM_PROFILES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TBRingermodeProfiles prof = new TBRingermodeProfiles();
				prof.setLatitude(cursor.getDouble(4));
				prof.setLongitude(cursor.getDouble(5));
				prof.setIntervalStartId(cursor.getInt(1));
				prof.setIntervalEndId(cursor.getInt(2));
				prof.setDayOfWeek(cursor.getInt(3));
				prof.setType(cursor.getInt(6));
				// Adding location to list
				locList.add(prof);
			} while (cursor.moveToNext());
		}
		db.close();
		return locList;
	}

	/**
	 * Saves the passed database to the phone under the name "backup" then your unique
	 * android device ID, then ".db"
	 * 
	 * @param databaseName	Specifies which database you want saved to the file
	 * backupname.db. Most likely, this parameter is DatabaseHandler.DATABASE_NAME
	 */
	public static String exportDatabse(String databaseName, String androidId) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//"+"com.example.tweakbase"+"//databases//"+databaseName+"";
				String backupDBPath = "backup" + androidId + ".db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
				return backupDBPath;
			}
		} catch (Exception e) {
			Log.e("DatabaseHandler", e.getMessage());
		}
		return "";
	}
}
