package com.example.tweakbase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

// Will be activated every time interval (Day)
public class RMProfileReceiver extends Activity {
	private static String TAG = "RMProfileReceiver";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		askUserAboutRingermodeProfile();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void askUserAboutRingermodeProfile() {
		Log.d(TAG, "Alerting user of a new ringermode");
		final DatabaseHandler db = new DatabaseHandler(this);
		final TBRingermodePair pair = (TBRingermodePair) getIntent().getExtras().getSerializable("tbRingermodePair");
		final long id = getIntent().getExtras().getLong("id");	// id in table
		if (pair == null) {
			Log.d(TAG, "No ringermode pair found, quitting");
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		//builder.setTitle("TweakBase");
		builder.setMessage("TweakBase has detected you typically " + pair.getRingermodeTypeString() + "  from " + pair.getSartTimeString() +  " to " 
		+ pair.getEndTimeString() + " on " + pair.getDayOfWeekString() + "s. " + "Would you like TweakBase to do this for you in the future?");

		final Activity thisActivity = this;
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.setProfileAsInUse(id);
				
				Intent receiverIntent = new Intent(thisActivity, AdjustRingermodeReceiver.class);
				receiverIntent.putExtra("tbRingermodePair", pair);
				receiverIntent.putExtra("start", true);
				receiverIntent.putExtra("id", id);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(thisActivity, 70, receiverIntent, 0);
				((AlarmManager) thisActivity.getSystemService(Activity.ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, pair.getMillisOfNextOccurance(), pendingIntent);
				Log.d(TAG, "Set an alert for AdjustRingermodeReceiver to be in " + (pair.getMillisOfNextOccurance() -  System.currentTimeMillis()));
				
				dialog.dismiss();
				finish();
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				db.deleteProfile(id);
				dialog.dismiss();
				finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		alert.show();
	}

}
