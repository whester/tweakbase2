package com.example.tweakbase;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidgetProvider extends AppWidgetProvider {

	private static final String TAG = "MyWidgetProvider";
	// all initialized to tweakbase because we can guarantee that the user has that app
	private static String[] topFourApps = {"com.example.tweakbase", "com.example.tweakbase", "com.example.tweakbase", "com.example.tweakbase"};

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

			// registration for App1 
			Intent intent1 = new Intent(context, MyWidgetProvider.class);
			intent1.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent1.setAction(topFourApps[0]);

			PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
					0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App1, pendingIntent1);
			
			try {
				Drawable icon = context.getPackageManager().getApplicationIcon(topFourApps[0]);
				remoteViews.setImageViewBitmap(R.id.App1, drawableToBitmap(icon));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			

			// registration for App2
			Intent intent2 = new Intent(context, MyWidgetProvider.class);
			intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent2.setAction(topFourApps[1]);

			PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,
					0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App2, pendingIntent2);
			
			try {
				Drawable icon = context.getPackageManager().getApplicationIcon(topFourApps[1]);
				remoteViews.setImageViewBitmap(R.id.App2, drawableToBitmap(icon));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			// registration for App3
			Intent intent3 = new Intent(context, MyWidgetProvider.class);
			intent3.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent3.setAction(topFourApps[2]);

			PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context,
					0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App3, pendingIntent3);
			
			try {
				Drawable icon = context.getPackageManager().getApplicationIcon(topFourApps[2]);
				remoteViews.setImageViewBitmap(R.id.App3, drawableToBitmap(icon));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			// registration for App4
			Intent intent4 = new Intent(context, MyWidgetProvider.class);
			intent4.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent4.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
			intent4.setAction(topFourApps[3]);

			PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context,
					0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.App4, pendingIntent4);
			
			try {
				Drawable icon = context.getPackageManager().getApplicationIcon(topFourApps[3]);
				remoteViews.setImageViewBitmap(R.id.App4, drawableToBitmap(icon));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
	
	private Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().contains("com")){
			Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(intent.getAction());
			try {
				context.startActivity(LaunchIntent);
			} catch (Exception e) {
				Toast.makeText(context.getApplicationContext(), "TweakBase could not open this app", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "App does not exist");
			}
		}
	}
	
	static public void setTopFourApps(String[] arr) {
		topFourApps = arr;
	}

} 
