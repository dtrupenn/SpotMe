package com.pennapps.spotme;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

public class meRequestListener implements RequestListener{
	protected static String fid;
	protected static String username;

	InputStream is = null;
	StringBuilder sb = null;
	String result = null;
	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub

		try {
			JSONObject json = new JSONObject(response);
			String id = json.getString("id");
			String name = json.getString("name");
			fid = id;
			username = name;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub
		Log.e("log_tag", "We f***ed up!\n" + e.toString());
	}

	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub
		Log.e("log_tag", "They F***ED up!!!");
	}

	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub
		Log.e("log_tag", "Can't spell for shit");
	}

	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub
		Log.e("log_tag","F*** this");
	}

}