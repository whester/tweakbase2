package com.example.tweakbase;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String TAG = "MyWidgetProvider";
	private String[] topFourApps = {"com.twitter.android", "com.snapchat.android", "com.facebook.katana", "com.facebook.katana"};

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "In onUpdate");

		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				MyWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {    

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			// registration for App1 TODO: Figure out how to get the correct app icon
			Intent intent1 = new Intent(context, MyWidgetProvider.class);
			intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent1.setAction(topFourApps[0]);

			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
					0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App1, pendingIntent1);

			// registration for App2
			Intent intent2 = new Intent(context, MyWidgetProvider.class);
			intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent2.setAction(topFourApps[1]);

			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,
					0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App2, pendingIntent2);

			// registration for App3
			Intent intent3 = new Intent(context, MyWidgetProvider.class);
			intent3.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent3.setAction(topFourApps[2]);

			PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context,
					0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App3, pendingIntent3);

			// registration for App4
			Intent intent4 = new Intent(context, MyWidgetProvider.class);
			intent4.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent4.setAction(topFourApps[3]);

			PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context,
					0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App4, pendingIntent4);

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().contains("com")){
			Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(intent.getAction());
			context.startActivity(LaunchIntent);
		}
	}
	
	public void setTopFourApps(String[] arr) {
		topFourApps = arr;
	}

} 
