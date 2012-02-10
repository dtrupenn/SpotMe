package com.pennapps.spotme;

import java.io.InputStream;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.facebook.android.*;
import com.facebook.android.Facebook.DialogListener;

public class SpotMe_TestActivity extends Activity {
	Facebook facebook = new Facebook("198313533597169");
	private SharedPreferences mPrefs;
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/*
		 * Retrieve existing access_token
		 */
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if(access_token != null)
			facebook.setAccessExpires(expires);
		/*
		 * Only call authorize when access_token is expired
		 */
		if(!facebook.isSessionValid()){
			facebook.authorize(this, new String[] {"user_actions.music"},
					new DialogListener() {

				public void onComplete(Bundle values) {
					AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);

					// post information about the currently logged in user
					mAsyncRunner.request("me/", new meRequestListener());
					
					// get information about the currently played song
					mAsyncRunner.request("me/music.listens", new idRequestListener());

					// post song info to server
					mAsyncRunner.request("10150639755555154", new musicRequestListener());
					
				}


				public void onFacebookError(FacebookError error) {}


				public void onError(DialogError e) {}


				public void onCancel() {}
			});
		}


		if(facebook.isSessionValid()){
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
			// post information about the currently logged in user
			mAsyncRunner.request("me/", new meRequestListener());
			
			// get information about the currently played song
			mAsyncRunner.request("me/music.listens", new idRequestListener());

			// post song info to server
			mAsyncRunner.request("10150106679409734", new musicRequestListener());
			
//			// post checkin data if gps location doesn't work
//			mAsyncRunner.request("me/friends", new checkinsRequestListener());
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}