package com.vlara.honeyhide;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.vlara.honeyhide.donate.R;

public class ScreenReceiver extends BroadcastReceiver {
	public final static String TAG = "Honey";
	public SharedPreferences preferences;
	public static final String PREFS_NAME = "HoneyHideSettings";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			boolean checked = preferences.getBoolean("reenable", false);
			if (checked) {
				Showbar();
				SharedPreferences settings = context.getSharedPreferences(
						PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("hidden", false);
				editor.commit();
			}
		}
	}

	public void Showbar() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Process proc = Runtime.getRuntime().exec(
							new String[] { "am", "startservice", "-n",
									"com.android.systemui/.SystemUIService" });
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
}
