package com.pennapps.spotme;

import java.io.InputStream;

import android.app.Activity;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.location.*;
import android.app.*;

import com.facebook.android.*;
import com.facebook.android.Facebook.*;

public class SpotMeActivity extends Activity {


	Facebook facebook = new Facebook("198313533597169");
	SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME,0);
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String access_token = settings.getString("access_token", null);
		long expires = settings.getLong("access_expires", 0);
		if(access_token != null){
			facebook.setAccessToken(access_token);
		}
		if (expires !=0){
			facebook.setAccessExpires(expires);
		}
		
		if(!facebook.isSessionValid()){
			 facebook.authorize(this, new String[] {}, new DialogListener() {
	         
	                public void onComplete(Bundle values) {
	                    SharedPreferences.Editor editor = settings.edit();
	                    editor.putString("access_token", facebook.getAccessToken());
	                    editor.putLong("access_expires", facebook.getAccessExpires());
	                    editor.commit();
	                    AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	    				// post information about the currently logged in user
	    				mAsyncRunner.request("me/", new meRequestListener());
	                }
	    
	              
	                public void onFacebookError(FacebookError error) {}
	    
	                
	                public void onError(DialogError e) {}
	    
	              
	                public void onCancel() {}
	            });
			 	SharedPreferences.Editor editor = settings.edit();
				editor.putString("app_id", "198313533597169");
				editor.commit();
		}
		
		
		final ToggleButton activateButton = (ToggleButton) this.findViewById(R.id.activateButton);
		activateButton.setTextOn("SpotMe ON");
		activateButton.setTextOff("Activate SpotMe!");
		SharedPreferences pref = getSharedPreferences(Settings.PREFS_NAME,0);
		activateButton.setChecked(pref.getBoolean("activated", false));
		activateButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				Intent activityIntent = new Intent(SpotMeActivity.this, SpotMeService.class);
				if (activateButton.isChecked()){
					startService(activityIntent);
				}else{
					stopService(activityIntent);
				}
			}
		});

		Button settingsButton = (Button) this.findViewById(R.id.settings);
		settingsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0){
				System.out.println("Settings");
				Intent intent = new Intent(SpotMeActivity.this, Settings.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}

}


