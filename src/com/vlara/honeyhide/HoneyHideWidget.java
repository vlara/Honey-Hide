package com.vlara.honeyhide;

import java.io.IOException;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class HoneyHideWidget extends AppWidgetProvider {

    public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
    public static final String PREFS_NAME = "HoneyHideSettings";
    public boolean hidden;


    /*
     * Reset hidden boolean Value to False whenever the widget is created(e.g on restart)
     */
    @Override
    public void onEnabled(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        hidden = settings.getBoolean("hidden", false);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hidden", hidden);
        editor.commit();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        hidden = settings.getBoolean("hidden", false);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
        Intent toggle = new Intent(context, HoneyHideWidget.class);
        toggle.setAction(ACTION_WIDGET_RECEIVER);
        toggle.putExtra("msg", "toggle");

        PendingIntent TogglePendingIntent = PendingIntent.getBroadcast(context, 0, toggle, 0);
        remoteViews.setOnClickPendingIntent(R.id.toggle,TogglePendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        hidden = settings.getBoolean("hidden", false);
        SharedPreferences.Editor editor = settings.edit();
        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
            if (hidden) {
                Toast.makeText(context, "Showing " + context.getString(R.string.SystemBar), Toast.LENGTH_SHORT).show();
                Showbar();
            }
            else {
                Toast.makeText(context, "Hiding " + context.getString(R.string.SystemBar), Toast.LENGTH_SHORT).show();
                Hidebar();
            }
            editor.putBoolean("hidden", !hidden);
            editor.commit();
        }

        super.onReceive(context, intent);
    }

    public void Showbar() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("OFF", "Turning ON");
                    Process proc = Runtime.getRuntime().exec(new String[]{
                            "am","startservice","-n","com.android.systemui/.SystemUIService"});
                    proc.waitFor();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
    public void Hidebar(){
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("OFF", "Turning OFF");
                    Process proc = Runtime.getRuntime().exec(new String[]{
                            "su","-c","service call activity 79 s16 com.android.systemui"});
                    proc.waitFor();
                    proc.destroy();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

}