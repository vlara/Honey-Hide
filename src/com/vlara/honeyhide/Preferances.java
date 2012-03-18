package com.vlara.honeyhide;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import com.vlara.honeyhide.donate.R;

public class Preferances extends PreferenceActivity{
	public SharedPreferences preferences;
	public Editor edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final CheckBoxPreference reenablePref = (CheckBoxPreference) findPreference("reenableKey");
		boolean checked = preferences.getBoolean("reenable", false);
		if (checked){
			reenablePref.setChecked(true);
		}
		edit = preferences.edit();
		
		reenablePref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						// intent here
						Log.d("honey", "!ReneablePref: " + reenablePref.isChecked());
						edit.putBoolean("reenable", reenablePref.isChecked());
						edit.commit();
						return true;
					}
				});
	}
}
