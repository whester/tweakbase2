package com.example.tweakbase;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

// TODO: Make this run once a day, deal with overnight stuff
public class PredictVolumeReceiver extends BroadcastReceiver {
	private static String TAG = "PredictVolumeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Starting to detect new ringermode pairs");
		DatabaseHandler db = new DatabaseHandler(context);
		List<TBRingermodePair> pairList = new ArrayList<TBRingermodePair>();
		List<TBRingermode> ringermodeList = db.getAllRingermodes();
		for (int i = 0; i < ringermodeList.size() - 2; i++) {
			if (normalToSilent(ringermodeList.get(i), ringermodeList.get(i+1), ringermodeList.get(i+2))) {
				pairList.add(new TBRingermodePair(ringermodeList.get(i+1), ringermodeList.get(i+2)));		
				i += 2;	// Need to skip
			} else if (normalToVibrate(ringermodeList.get(i), ringermodeList.get(i+1), ringermodeList.get(i+2))) {
				pairList.add(new TBRingermodePair(ringermodeList.get(i), ringermodeList.get(i+1)));
				i += 2;	// Need to skip
			}
		}

		for (int i = 0; i < pairList.size() - 1; i++) {
			int numMatches = 0;
			TBRingermodePair currPair = pairList.get(i);
			for (int j = i+1; j < pairList.size() && numMatches < 3; j++) {
				if (currPair.equals(pairList.get(j))) {
					numMatches++;
				}
			}
			if (numMatches >= 3) {
				Log.d(TAG, "We found a match from ID " + currPair.getStartIntervalId() + " to " + 
						currPair.getEndIntervalId() + " on " + currPair.getDayOfWeek() + " with profile " + currPair.getType());
			}
		}

		// Remove duplicates
		for (int i = 0; i < pairList.size(); i++) {
			for (int j = i + 1; j < pairList.size(); j++) {
				if (pairList.get(i).equals(pairList.get(j))) {
					pairList.remove(j);
					j--;
				}
			}
		}

		// Add these profiles to the database
		for (TBRingermodePair pair : pairList) {
			TBRingermodePair currPair = pair;
			long id = db.addRMProfile(new TBRingermodeProfiles(0, 0, currPair.getDayOfWeek(), currPair.getDayOfWeek(), 
					currPair.getStartIntervalId(), currPair.getEndIntervalId(), false));

			// Record reminders to ask user if they want us to remember this profile
			Intent receiverIntent = new Intent(context, RMProfileReceiver.class);
			receiverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			receiverIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			receiverIntent.putExtra("tbRingermodePair", pair);
			receiverIntent.putExtra("id", id);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, receiverIntent, PendingIntent.FLAG_ONE_SHOT);
			((AlarmManager) context.getSystemService(Activity.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, pair.getMillisOfNextOccurance(), pendingIntent);
			Log.d(TAG, "Set an alert for RMProfileReceiver to be in " + (pair.getMillisOfNextOccurance() -  System.currentTimeMillis()));
		}
	}

	private boolean normalToSilent(TBRingermode t1, TBRingermode t2, TBRingermode t3) {
		return t1.getType() == AudioManager.RINGER_MODE_NORMAL && 
				t2.getType() == AudioManager.RINGER_MODE_VIBRATE && 
				t3.getType() == AudioManager.RINGER_MODE_SILENT && 
				t2.getIntervalId() == t3.getIntervalId();
	}

	private boolean normalToVibrate(TBRingermode t1, TBRingermode t2, TBRingermode t3) {
		return t1.getType() == AudioManager.RINGER_MODE_NORMAL && 
				t2.getType() == AudioManager.RINGER_MODE_VIBRATE && 
				t3.getType() == AudioManager.RINGER_MODE_NORMAL;
	}
}


