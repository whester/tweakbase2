package com.example.tweakbase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;


public class AdjustRingermodeReceiver extends BroadcastReceiver {
	private static final String TAG = "AdjustRingermodeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Gonna change the ringermode");
		final TBRingermodePair pair = (TBRingermodePair) intent.getExtras().getSerializable("tbRingermodePair");
		final long id = intent.getExtras().getLong("id");
		final boolean start = intent.getExtras().getBoolean("start");
		final int ringermodeType;
		if (start) {
			ringermodeType = pair.getType();
			Intent receiverIntent = new Intent(context, AdjustRingermodeReceiver.class);
			receiverIntent.putExtra("tbRingermodePair", pair);
			receiverIntent.putExtra("start", false);
			receiverIntent.putExtra("id", id);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, receiverIntent, PendingIntent.FLAG_ONE_SHOT);
			((AlarmManager) context.getSystemService(Activity.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, pair.getMillisOfNextOccurance(), pendingIntent);
			Log.d(TAG, "Set an alert for RMProfileReceiver to be in " + (pair.getMillisOfNextOccurance() -  System.currentTimeMillis()));
		} else {
			ringermodeType = AudioManager.RINGER_MODE_NORMAL;
			Intent receiverIntent = new Intent(context, AdjustRingermodeReceiver.class);
			receiverIntent.putExtra("tbRingermodePair", pair);
			receiverIntent.putExtra("start", true);
			receiverIntent.putExtra("id", id);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, receiverIntent, PendingIntent.FLAG_ONE_SHOT);
			((AlarmManager) context.getSystemService(Activity.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, pair.getMillisOfNextOccurance(), pendingIntent);
			Log.d(TAG, "Set an alert for RMProfileReceiver to be in " + (pair.getMillisOfNextOccurance() -  System.currentTimeMillis()));
		}
		DatabaseHandler db = new DatabaseHandler(context);
		if (db.profileInUse(id)) {
			Log.d(TAG, "Gonna change the ringermode for id " + id);
			if (ringermodeType == AudioManager.RINGER_MODE_SILENT) {
				AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			} else if (ringermodeType == AudioManager.RINGER_MODE_VIBRATE) {	
				AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				audiomanage.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			} else {	// Must be back to normal
				AudioManager audiomanage = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
		}
	}

}
