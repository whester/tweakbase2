package com.example.tweakbase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.PowerManager;
import android.util.Log;

public class AppTrackerReceiver extends BroadcastReceiver {

	private static final String TAG = "AppTrackerReceiver";
	private static boolean foundHome = false;
	private static String homeApp;
	private static String lastApp = "";
	final String blacklist = "System UI,Phone,Android System,TweakBase,Aviate,ContextProvider,Nfc Service,LogsProvider,PageBuddyNotiSvc,"
			+ "Knox Notification Manager,Samsung Cloud Data Relay,Cover,com.sec.msc.nts.android.proxy,com.sec.knox.eventsmanager,TwDVFSApp,"
			+ "Samsung Push Service,TouchWiz home,FilterProvider,Update Device";
	List<String> BLACKLIST = Arrays.asList(blacklist.split(","));

	private static ActivityManager am;
	private static PowerManager powerManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!foundHome) {
			Intent i = new Intent(); 
			i.setAction(Intent.ACTION_MAIN); 
			i.addCategory(Intent.CATEGORY_HOME); 
			PackageManager pm = context.getPackageManager(); 
			ResolveInfo ri = pm.resolveActivity(i, 0); 
			ActivityInfo ai = ri.activityInfo; 
			homeApp = ai.packageName;
			Log.d(TAG, "New homeApp decided on: " + homeApp);
			foundHome = true;
			am = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
			powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		}

		if (!powerManager.isScreenOn()) {
			return;	// Don't track if the screen is off
		}

		List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
		Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
		PackageManager pm = context.getPackageManager();

		// set up to log to the database
		DatabaseHandler db = new DatabaseHandler(context);
		Calendar cal = Calendar.getInstance();
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 

		while(i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
			try {
				if (! (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
					continue;
				}
				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
				if (pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA).packageName.equals(homeApp)) {
					Log.d(TAG, "At home screen, quitting.");
					break;
				}
				if(!BLACKLIST.contains(c.toString())){
					if (!lastApp.equals(c.toString())) {	// Only record if app is diff than last time
						// TODO: Implement a GPS check when it is turned on
						Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						double longitude = location.getLongitude();
						double latitude = location.getLatitude();

						db.addAppOpened(new TBAppOpened(longitude, latitude, cal.get(Calendar.DAY_OF_WEEK), c.toString(), "place.holder.app"));
						Log.d(TAG, c.toString());
						lastApp = c.toString();
						break;
					}
				}
			} catch(Exception e) {

			}
		}
	}

}
