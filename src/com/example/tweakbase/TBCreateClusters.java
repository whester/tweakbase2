package com.example.tweakbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class TBCreateClusters extends BroadcastReceiver {
	private static String TAG = "Creating Clusters";
	private static String OTHER_LOCATION = "Location Other";
	ArrayList<String> appTracker = new ArrayList<String>(); //keeps track of index number of each app in the three dimensional array

	
	
	public void onReceive(Context context, Intent intent){
		Log.d(TAG, "Starting to cluster");
		ArrayList<LocationObject> topLocations = new ArrayList<LocationObject>();
		
		try {
			Log.d(TAG, "Inside try");
			FileInputStream fis = context.openFileInput("clusters");
			ObjectInputStream is = new ObjectInputStream(fis);
			topLocations = (ArrayList<LocationObject>) is.readObject();
			is.close();
/*			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("tracker", 0);
			editor.commit(); 
			Log.d(TAG, "It worked inside try"); */
		} catch (IOException IE) { //if the files does not exist 
			Log.d(TAG, "Inside catch");
			String filename = "clusters";
			try {
				FileOutputStream fos = context.openFileOutput(filename, context.MODE_PRIVATE);
				ObjectOutputStream os = new ObjectOutputStream(fos);
				os.writeObject(topLocations);
				os.close();
				Log.d(TAG, "Making a new one");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (ClassNotFoundException e) { //if the class type (hashMap) is not found
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Log.d(TAG, "We good");
		topLocations = findTopLocations(context, topLocations);
		Log.d(TAG, "hoy");
		createMatrix(context, topLocations);
		
	}
	//find top 5 locations plus an extra one for everything else.
	public ArrayList<LocationObject> findTopLocations(Context context, ArrayList<LocationObject> locationCount){
		
		DatabaseHandler db = new DatabaseHandler(context);
		List<TBAppOpened> locationDB = db.getAllApps(); //database puts all the rows from the table here
		Log.d(TAG,"Size of DB " + locationDB.size());
		
		int Index = 0; //temp for holding the line we ended at last time
	/*	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int tracker = sp.getInt("tracker", 0);
		Log.d(TAG, "Tracker is " + tracker); */
		
		for ( int i = 0; i < locationDB.size(); i++){
			double lat = Math.round(locationDB.get(i).getLatitude() * 1000);
			lat = lat / 1000;
			LocationObject locIndex = new LocationObject(); 
			locIndex.setLat(lat);
			//Log.d(TAG, "lat is " + lat);
			double lon = Math.round(locationDB.get(i).getLongitude() * 1000);
			lon = lon / 1000;
			locIndex.setLon(lon);
			boolean found = false;
			//Log.d(TAG,"Before " + locationDB.get(i).getLatitude() + " After " + locIndex.getLat());
			
			for (int j = 0; j < locationCount.size(); j++) { //check to see if we can find a match
				//Log.d(TAG,"no match");
				if ((locationCount.get(j).getLat() == locIndex.getLat()) &&
					(locationCount.get(j).getLon() == locIndex.getLon())){
					//Log.d(TAG,"FOUND A MATCH");
					locationCount.get(j).setCount(locationCount.get(j).getCount() + 1);
					//Log.d(TAG, "DB Lat " + locationCount.get(j).getLat() + " Other Lat " + locIndex.getLat());
					found = true;
					break;
				}
			}
	
			if (!found){      //if location does not exist, make a new one
				//Log.d(TAG,"Making New Location " + Index);
				locIndex.setCount(1);     //set the count number to 1 to begin. 
				locationCount.add(locIndex); //add it to the list of already identified locations
			}
			Index = i;
			
		}
		
/*		sp = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("tracker", Index);
		editor.commit(); */
		Log.d(TAG, "We left off at line " + Index);
		Log.d(TAG, "DONE-------------------------------------------");
		Collections.sort(locationCount);
		//The list is now ordered
		ArrayList<LocationObject> topLocs = new ArrayList<LocationObject>();
		
	//	Log.d(TAG, "Before topLoc");
		if (locationCount.size() > 0){
			topLocs.add(locationCount.get(locationCount.size() - 1));
			topLocs.get(0).setName("Location1");
		}
		if (locationCount.size() > 1){
			topLocs.add(locationCount.get(locationCount.size() - 2));
			topLocs.get(1).setName("Location2");
		}
		if (locationCount.size() > 2){
			topLocs.add(locationCount.get(locationCount.size() - 3));
			topLocs.get(2).setName("Location3");
		}
		if (locationCount.size() > 3){
			topLocs.add(locationCount.get(locationCount.size() - 4));
			topLocs.get(3).setName("Location4");
		}
		if (locationCount.size() > 4){
			topLocs.add(locationCount.get(locationCount.size() - 5));
			topLocs.get(4).setName("Location5");
		}
		if (locationCount.size() > 5){
			topLocs.add(locationCount.get(locationCount.size() - 6));
			topLocs.get(5).setName("Location6");
		}
		if (locationCount.size() > 6){
			topLocs.add(locationCount.get(locationCount.size() - 7));
			topLocs.get(6).setName("Location7"); 
		}

	//	Log.d(TAG, "After toploc");
		
		return topLocs;
		//TO BE DONE
		//1. Sort the array and find top whatever most visited locations -> DONE
		//2. Name each of those locations and put them in the Hash Map
		//Don't forget to make one location for "others"
		
		
	}
	
	public void createMatrix(Context context, ArrayList<LocationObject> topLocations){
		DatabaseHandler db = new DatabaseHandler(context);
		List<TBAppOpened> locationDB = db.getAllApps(); //database puts all the rows from the table here
		HashMap <String, int [][][]> matrix = new HashMap<String, int [][][]>();
		int [][][] locMatrix =  new int[7][288][100] ;
		if (topLocations.size() > 0){
			matrix.put(topLocations.get(0).getName(), locMatrix);
		}
		if (topLocations.size() > 1){
			matrix.put(topLocations.get(1).getName(), locMatrix);
		}
		if (topLocations.size() > 2){
			matrix.put(topLocations.get(2).getName(), locMatrix);
		}
		if (topLocations.size() > 3){
			matrix.put(topLocations.get(3).getName(), locMatrix);
		}
		if (topLocations.size() > 4){
			matrix.put(topLocations.get(4).getName(), locMatrix);
		}
		if (topLocations.size() > 5){
			matrix.put(topLocations.get(5).getName(), locMatrix);
		}
		if (topLocations.size() > 6){
			matrix.put(topLocations.get(6).getName(), locMatrix);
		}
		if (topLocations.size() > 7){
			matrix.put(OTHER_LOCATION, locMatrix);
		}

		
		int app = 0;
		for ( int i = 0; i < locationDB.size(); i++){
			double lat = Math.round(locationDB.get(i).getLatitude() * 1000);
			lat = lat / 1000;
			LocationObject locIndex = new LocationObject(); 
			locIndex.setLat(lat);
			//Log.d(TAG, "lat is " + lat);
			double lon = Math.round(locationDB.get(i).getLongitude() * 1000);
			lon = lon / 1000;
			locIndex.setLon(lon);
			boolean found = false;
			for (int j = 0; j < 7; j++){
				if (locIndex.getLat() == topLocations.get(j).getLat() &&
					locIndex.getLon() == topLocations.get(j).getLon()){
					app = whatApp(locationDB.get(i));
					locMatrix = matrix.get(topLocations.get(j).getName());
					locMatrix [locationDB.get(i).getDayOfWeek()][locationDB.get(i).getIntervalId()][app]++;
					matrix.put(topLocations.get(j).getName(), locMatrix);
					found = true;
					break;
				}
			}
			
			if (!found){
				locMatrix = matrix.get(OTHER_LOCATION);
				locMatrix [locationDB.get(i).getDayOfWeek()][locationDB.get(i).getIntervalId()][app]++;
				matrix.put(OTHER_LOCATION, locMatrix);
			}
			
			
		}
		
		locMatrix = matrix.get(topLocations.get(0).getName());
		Log.d(TAG,"Index of Facebook is " + appTracker.indexOf("Facebook"));
		Log.d(TAG,"Index of GroupMe is " + appTracker.indexOf("GroupMe"));
		Log.d(TAG,"Index of Calendar is " + appTracker.indexOf("Calendar"));


		//Log.d(TAG,"Facebook: " + locMatrix[2][170][1]);
		
	}
	
	public int whatApp(TBAppOpened apps){
		if (appTracker.contains(apps.getAppName())){
			return appTracker.indexOf(apps.getAppName());
		} else {
			appTracker.add(apps.getAppName());
			return appTracker.indexOf(apps.getAppName());
		}
	}


}
