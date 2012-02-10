package com.pennapps.spotme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class Settings extends Activity{
	protected static final String PREFS_NAME = "MyPrefsFile";
	protected static boolean showName = true;
	protected static boolean showContact = true;
	protected static boolean showRecent = true;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
				
		final CheckBox nameBox = (CheckBox) this.findViewById(R.id.showname);
		nameBox.setChecked(showName);
		nameBox.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(Settings.this, "Name Public", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(Settings.this, "Name Private", Toast.LENGTH_SHORT).show();
		        }
		        showName=nameBox.isChecked();
		        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("showName", showName);
				editor.commit();
		    }
		});
		
		final CheckBox contactBox = (CheckBox) this.findViewById(R.id.showcontact);
		contactBox.setChecked(showContact);
		contactBox.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(Settings.this, "Contact Public", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(Settings.this, "Contact Private", Toast.LENGTH_SHORT).show();
		        }
		        showContact = contactBox.isChecked();
		        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("showContact", showContact);
				editor.commit();
		    }
		});
		
		final CheckBox recentBox = (CheckBox) this.findViewById(R.id.showrecent);
		recentBox.setChecked(showRecent);
		recentBox.setOnClickListener(new OnClickListener(){
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		            Toast.makeText(Settings.this, "Recent Songs Public", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(Settings.this, "Recent Songs Private", Toast.LENGTH_SHORT).show();
		        }
		        showRecent = recentBox.isChecked();
		        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("showRecent", showRecent);
				editor.commit();
		    }
		});
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("showName", showName);
		editor.putBoolean("showContact", showContact);
		editor.putBoolean("showRecent", showRecent);
		editor.commit();
		System.out.println(settings.getAll());
	}
	}
		