package com.pennapps.spotme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

import android.util.Log;

public class idRequestListener implements RequestListener{
	protected static String songID;


	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub
		try {
			JSONObject json = new JSONObject(response);
			JSONArray son = json.getJSONArray("data");
			json = son.getJSONObject(0);
//			String stime = json.getString("start_time");
//			String etime = json.getString("end_time");
//			String [] ssplit = stime.split("T");
//			String [] esplit = etime.split("T");
//			ssplit[1] = ssplit[1].substring(0, ssplit[1].indexOf("+"));
//			esplit[1] = esplit[1].substring(0, esplit[1].indexOf("+"));
//			Calendar cal = new GregorianCalendar();
//			int date = cal.getTime().getDate();
//			int time = cal.getTime().
//			if((Integer.getInteger(ssplit[0].substring(8, 9))) <= date && (Integer.getInteger(esplit[0].substring(8, 9))) >= date)
//				if()
			json = json.getJSONObject("data");
			json = json.getJSONObject("song");
			String id = json.getString("id");
			songID = id;
			Log.e("log_tag", id);
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