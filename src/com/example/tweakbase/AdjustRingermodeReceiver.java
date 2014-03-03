package com.example.tweakbase;

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
			// TODO: Make new intent for this to be called with start = false, make time for the end time
		} else {
			ringermodeType = AudioManager.RINGER_MODE_NORMAL;
			// TODO: Make new intent for this to be called with start = true, make time for next occurrence
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
