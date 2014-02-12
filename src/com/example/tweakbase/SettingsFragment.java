package com.example.tweakbase;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

// TODO: Upload data, get location on silence, use Places API

/**
 * This class is the meat of SettingsActivity. It handles displaying TweakBase's preferences and 
 * dispatching the relevant actions based on user preference changes.
 * 
 * This class extends PreferenceFragment, an Android abstract class that makes displaying the typical
 * Android settings activities easy.
 * 
 * This class also implements the OnSharedPreferenceChangeListener interface. This interface requires
 * implementers to override the onSharedPreferenceChanged method, which is automatically called by the
 * Android OS whenever an element in this activity's SharedPreferences is changed. The elements that
 * will change correspond to the settings TweakBase's users will be able to change.
 * 
 * @author Will Hester
 */
public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener, SensorEventListener {

	private static final String TAG = "SettingsFragment";

	static final String KEY_PREF_TRACK_LOCATION = "pref_trackLocation";	
	static final String KEY_TRACKING = "trackingLocation";
	boolean trackMyLocation;
	boolean currentlyTracking;

	static final String KEY_PREF_TRACK_RINGERMODE = "pref_trackRingerMode";
	static final String KEY_RINGERMODE = "trackingRingerMode";
	boolean trackMyRingerMode;
	boolean currentlyTrackingRingerMode;

	static final String KEY_PREF_TRACK_APPLICATIONS = "pref_trackApplications";
	static final String KEY_APPLICATIONS = "trackingApplications";
	boolean trackMyApplications;
	boolean currentlyTrackingApplications;
	
	static final String KEY_PREF_TRACK_ACCELERATION = "pref_trackAcceleration";
	static final String KEY_ACCELERATION = "trackingAcceleration";
	boolean trackMyAcceleration;
	boolean currentlyTrackingAcceleration;
	
	double xcoor;
	double ycoor;
	double zcoor;

	LocationManager locManager;
	Activity settingsActivity;
	PendingIntent trackLocationPendingIntent;
	BroadcastReceiver volumeReceiver;
	//sensors for accelerometer
	private SensorManager mSensorManager;
	private Sensor mSensor;
	
    boolean appTrackerServiceBound = false;
	//	PredictVolume addPattern;

	// The minimum time between updates in milliseconds
	private static final long TIME_BW_UPDATES = 1000 * 10; // 10 seconds

	/**
	 * Inherited from the PreferenceFragment class. Called by the Android OS when
	 * an activity is opened or a fragment is displayed.
	 * 
	 *  @param savedInstanceState	a Bundle (Android class that contains
	 *  saved file information) that the Android OS automatically passes
	 *  when this method is called.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settingsActivity = getActivity();

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		// Load this activity's SharedPreferences and get the saved preferences
		SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
		trackMyLocation = sharedPref.getBoolean(KEY_PREF_TRACK_LOCATION, true);
		currentlyTracking = sharedPref.getBoolean(KEY_TRACKING, false);

		trackMyRingerMode = sharedPref.getBoolean(KEY_PREF_TRACK_RINGERMODE, true);
		currentlyTrackingRingerMode = sharedPref.getBoolean(KEY_RINGERMODE, false);

		trackMyApplications = sharedPref.getBoolean(KEY_PREF_TRACK_APPLICATIONS, true);
		currentlyTrackingApplications = sharedPref.getBoolean(KEY_APPLICATIONS, false);
		
		trackMyAcceleration = sharedPref.getBoolean(KEY_PREF_TRACK_ACCELERATION, true);
		currentlyTrackingAcceleration = sharedPref.getBoolean(KEY_ACCELERATION, false);
		
		mSensorManager = (SensorManager) settingsActivity.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL)){
			Log.d("Success", "THIS WORKS");
		}

		if (trackMyLocation) {
			trackLocation();
		}

		if (trackMyRingerMode){
			trackRingerMode();
		}

		if (trackMyApplications){
			trackApplications();
		}
		
		if (trackMyAcceleration){
			trackAcceleration();
		}  
		
		
	}


	/**
	 * This creates us a nice button at the bottom of the settings page that allows us to upload your
	 * TweakBase database to http://whester.com/tweakbase/uploads
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout v = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

		Button btn = new Button(getActivity().getApplicationContext());
		btn.setText("Click to upload database");

		v.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String androidId = Secure.getString(settingsActivity.getContentResolver(), Secure.ANDROID_ID); 
				final String backupDBPath = DatabaseHandler.exportDatabse(DatabaseHandler.DATABASE_NAME, androidId);

				Toast toast = Toast.makeText(settingsActivity, "Uploading now. Your Android ID is " + androidId, Toast.LENGTH_LONG);
				toast.show();

				/* You can't do data actions on the main thread, so instead we create a new thread to
				 * take care of the uploading of the database.
				 */
				new Thread(new Runnable(){
					public void run()
					{
						HttpFileUpload.UploadFile(backupDBPath);
					}
				}).start();
			}
		});

		return v;
	}


	/**
	 * Kicks off tracking location. Called when the user wants TweakBase to track his/her location.
	 * This method takes advantage of threads (if you don't know what threads are, ask Professor
	 * Badass). In Android, actions that take place in the background (like recording location every 
	 * 10 min.) are not permitted on the UI thread (the main thread an Activity runs on). 
	 * 
	 * So, we're creating our own thread that we can do whatever we want on. In this case, we're going 
	 * to give the thread a LocationManager and call its requestLocaitonUpdates() method, which notifies
	 * a LocationListener on location updates.
	 * 
	 * LocationListener is an Android-defined interface with four methods. We have our own implementation,
	 * TBLocationListener. More info in that class.
	 */
	private void trackLocation() {
		Log.d(TAG, "Starting to track location");

		locManager = (LocationManager) settingsActivity.getSystemService(Context.LOCATION_SERVICE);
		Intent i = new Intent("com.example.tweakbase.LOCATION_READY");
		trackLocationPendingIntent = PendingIntent.getBroadcast(settingsActivity.getApplicationContext(),
				0, i, 0);
		// Register for broadcast intents
		locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_BW_UPDATES, 0, trackLocationPendingIntent);		
		Toast locationmodeOn = Toast.makeText(getActivity(), "Location tracking started", Toast.LENGTH_LONG);
		locationmodeOn.show();
	}

	private void trackRingerMode() {
		if (!currentlyTrackingRingerMode) {

			Log.d(TAG, "Ringer mode tracking started");
			Toast ringermodeOn = Toast.makeText(getActivity(), "Ringer mode tracking started", Toast.LENGTH_LONG);
			ringermodeOn.show();

			volumeReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context context, Intent intent) {
					DatabaseHandler db = new DatabaseHandler(context);
					Calendar cal = Calendar.getInstance();

					LocationManager lm = (LocationManager)settingsActivity.getSystemService(Context.LOCATION_SERVICE); 
					// TODO: Implement a GPS check when it is turned on
					Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					double longitude = location.getLongitude();
					double latitude = location.getLatitude();

					AudioManager am = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

					// storing everything in the database
					db.addRingermode(new TBRingermode(latitude, longitude, cal.get(Calendar.DAY_OF_WEEK), am.getRingerMode()));
					//	addPattern.onPatternIdentified();
					switch (am.getRingerMode()) {
					case AudioManager.RINGER_MODE_SILENT:
						Log.i(TAG, "Phone is in Silent mode");
						break;
					case AudioManager.RINGER_MODE_VIBRATE:
						Log.i(TAG, "Phone is in Vibrate mode");
						break;
					case AudioManager.RINGER_MODE_NORMAL:
						Log.i(TAG, "Phone is in Normal mode");
						break;
					}

					Log.d(TAG, "Latitude: "+ location.getLatitude()+" Longitude: "+ location.getLongitude());
				}
			};

			IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
			getActivity().registerReceiver(volumeReceiver, filter);
		}
	}


	private void trackApplications() {
		if(!currentlyTrackingApplications){
			Toast applicationsOn = Toast.makeText(getActivity(), "Applications tracking started", Toast.LENGTH_LONG);
			applicationsOn.show();
			
			Log.d(TAG, "Applicaiton tracking started");
			Intent intent = new Intent(settingsActivity, AppTrackerReceiver.class);
		    PendingIntent pendingIntent = PendingIntent.getBroadcast(settingsActivity.getApplicationContext(), 69, intent, 0);
		    AlarmManager alarmManager = (AlarmManager) settingsActivity.getSystemService(Activity.ALARM_SERVICE);
		    alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 100, 2000, pendingIntent);
		}
	}
	
	private void trackAcceleration(){
			
		Toast accelerationOn = Toast.makeText(getActivity(), "Movement tracking started", Toast.LENGTH_LONG);
		accelerationOn.show();
			
		Log.d(TAG, "Acceleration tracking started");
		
		
		Calendar cal = Calendar.getInstance();
		
		LocationManager lm = (LocationManager)settingsActivity.getSystemService(Context.LOCATION_SERVICE); 
		// TODO: Implement a GPS check when it is turned on
		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		
		DatabaseHandler db = new DatabaseHandler(settingsActivity);
		db.addACCProfile(new TBAccelerometer(latitude,longitude,cal.get(Calendar.DAY_OF_WEEK),xcoor,ycoor,zcoor));
	
		
	}
	
	public void onAccuracyChanged(Sensor s, int a){
		
	}
	@Override
	public void onSensorChanged(SensorEvent event){
		
		xcoor = event.values[0];
		ycoor = event.values[1];
		zcoor = event.values[2];
		//Log.d("Farzad", "Farzad is the best:" + xcoor);
		
		

	}

	



	/** 
	 * Overridden from PreferenceFragment. Automatically called by the Android OS. Necessary to notify
	 * changes to SharedPreferences to whoever needs it.
	 */
	@Override
	public void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	/** 
	 * Overridden from PreferenceFragment. Automatically called by the Android OS. Necessary to notify
	 * changes to SharedPreferences to whoever needs it.
	 */
	@Override
	public void onPause() {
		getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
		mSensorManager.unregisterListener(this, mSensor);
	}

	/**
	 * Overridden from PreferenceFragment. Automatically called by the Android OS. Also called when screen
	 * orientation changes. This saves rather or not the app is currently tracking location.
	 */
	@Override
	public void onDestroy() {
		SharedPreferences sharedPref = getPreferenceManager().getSharedPreferences();
		sharedPref.edit().putBoolean(KEY_TRACKING, trackMyLocation).commit();
		sharedPref.edit().putBoolean(KEY_RINGERMODE, trackMyRingerMode).commit();
		sharedPref.edit().putBoolean(KEY_APPLICATIONS, trackMyApplications).commit();
		sharedPref.edit().putBoolean(KEY_ACCELERATION, trackMyAcceleration).commit();
		if (volumeReceiver != null) {
			getActivity().unregisterReceiver(volumeReceiver);
		}
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_PREF_TRACK_LOCATION)) {
			trackMyLocation = sharedPreferences.getBoolean(KEY_PREF_TRACK_LOCATION, true);

			if (!trackMyLocation) {
				if (locManager != null) {
					locManager.removeUpdates(trackLocationPendingIntent);
				}
				Log.d(TAG, "Location tracking stopped");
				Toast locationmodeOff = Toast.makeText(getActivity(), "Location tracking stopped", Toast.LENGTH_LONG);
				locationmodeOff.show();
			} else {
				trackLocation();
			}
		}

		if (key.equals(KEY_PREF_TRACK_RINGERMODE)) {	
			Log.d(TAG, "Ringer mode tracking preference changed");
			trackMyRingerMode = sharedPreferences.getBoolean(KEY_PREF_TRACK_RINGERMODE, true);
			Log.d(TAG, "In onCreate, RingerMode preference read as: " + trackMyRingerMode);

			if (!trackMyRingerMode) {
				if(volumeReceiver != null){
					getActivity().unregisterReceiver(volumeReceiver);
				}
				Log.d(TAG, "Ringer mode tracking stopped");
				Toast ringermodeOff = Toast.makeText(getActivity(), "Ringer mode tracking stopped", Toast.LENGTH_LONG);
				ringermodeOff.show();
			} else {
				trackRingerMode();
			}
		}

		if (key.equals(KEY_PREF_TRACK_APPLICATIONS)) {	
			trackMyApplications = sharedPreferences.getBoolean(KEY_PREF_TRACK_APPLICATIONS, true);

			if (!trackMyApplications) {
				Log.d(TAG, "Applications tracking stopped");
				Toast applicationsOff = Toast.makeText(getActivity(), "Applications tracking stopped", Toast.LENGTH_LONG);
				applicationsOff.show();
				Intent intentstop = new Intent(settingsActivity, AppTrackerReceiver.class);
				PendingIntent senderstop = PendingIntent.getBroadcast(settingsActivity, 69, intentstop, 0);
				AlarmManager alarmManagerstop = (AlarmManager) settingsActivity.getSystemService(Activity.ALARM_SERVICE);
				alarmManagerstop.cancel(senderstop);
			} else {
				trackApplications();
			}
		}
		
		if (key.equals(KEY_PREF_TRACK_ACCELERATION)){
			trackMyAcceleration = sharedPreferences.getBoolean(KEY_PREF_TRACK_ACCELERATION, true);
			if (!trackMyAcceleration) {
				Log.d(TAG, "Acceleration tracking stopped");
				mSensorManager.unregisterListener(this, mSensor);

			} else {
				mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
				trackAcceleration();
			}
			
	
			
		}
	}


	
}
