package com.vlara.honeyhide;

import java.io.IOException;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class HoneyHideWidget extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
		Intent Hide = new Intent(context, HoneyHideWidget.class);
		Hide.setAction(ACTION_WIDGET_RECEIVER);
		Hide.putExtra("msg", "Hid");

		Intent Show = new Intent(context, HoneyHideWidget.class);
		Show.setAction(ACTION_WIDGET_RECEIVER);
		Show.putExtra("msg", "Show");

		PendingIntent HidePendingIntent = PendingIntent.getBroadcast(context, 0, Hide, 0);
		PendingIntent ShowPendingIntent = PendingIntent.getBroadcast(context, 1, Show, 1);

		remoteViews.setOnClickPendingIntent(R.id.show, ShowPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.hide, HidePendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
			String msg = "null";
			try {
				msg = intent.getStringExtra("msg");
			} catch (NullPointerException e) {
				Log.e("Error", "msg = null");
			}
			Toast.makeText(context, msg + "ing " + context.getString(R.string.SystemBar), Toast.LENGTH_SHORT).show();
			ToggleBar(msg);
		}

		super.onReceive(context, intent);
	}

	public void ToggleBar(String mode){

		if (mode.equals("Hid")){
			try {
				Log.d("OFF", "Turning OFF");
				Process proc = Runtime.getRuntime().exec(new String[]{
						"su","-c","service call activity 79 s16 com.android.systemui"});
				proc.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(mode.equals("Show")){
			try {
				Log.d("OFF", "Turning ON");
				Process proc = Runtime.getRuntime().exec(new String[]{
						"am","startservice","-n","com.android.systemui/.SystemUIService"});
				proc.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
