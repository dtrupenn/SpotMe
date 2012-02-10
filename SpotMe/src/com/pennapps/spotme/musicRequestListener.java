package com.pennapps.spotme;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

public class musicRequestListener implements RequestListener{
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	JSONObject j = new JSONObject();
	HttpResponse reply;

	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub
		try {
			JSONObject json = new JSONObject(response);
			String title = json.getString("title");
			json = json.getJSONObject("data");
			String artist = json.getJSONArray("musician").getJSONObject(0).getString("name");
			String album = json.getJSONArray("album").getJSONObject(0).getJSONObject("url").getString("title");
			Log.e("log_tag","Title: " + title + ", Artist: " + artist + ", Album: " + album);
			nameValuePairs.add(new BasicNameValuePair("Title", title));
			nameValuePairs.add(new BasicNameValuePair("Artist", artist));
			nameValuePairs.add(new BasicNameValuePair("Album", album));
			j.accumulate("FID", meRequestListener.fid);
			j.accumulate("Name", meRequestListener.username);
			j.accumulate("Title", title);
			j.accumulate("Artist", artist);
			j.accumulate("Album", album);
			Log.e("log_tag",j.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//http post

				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://158.130.107.37:80");
					httppost.setHeader("json", j.toString());
					httppost.getParams().setParameter("jsonpost",j);
					reply = httpclient.execute(httppost);
					if(reply != null){
						Log.d("log_tag", reply.toString());
					}
//					HttpEntity entity = reply.getEntity();
//					is = entity.getContent();
					Log.d("log_tag", "HTTP Ok!");
				}
				catch(Exception e){
					Log.e("log_tag", "Error in http connection"+e.toString());
				}
				//convert response to string

				try{
					HttpEntity entity = reply.getEntity();
					is = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
					sb = new StringBuilder();
					sb.append(reader.readLine() + "\n");
					String line="0";
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					result=sb.toString();

				}
				catch(Exception e){
					Log.e("log_tag", "Error converting result "+e.toString());
				}
//		
//				//paring data		
//				int fd_id;		
//				String fd_name;	
//				try{	
//					JSONArray jArray = new JSONArray(result);
//					JSONObject json_data=null;
//					for(int i=0;i<jArray.length();i++){
//						json_data = jArray.getJSONObject(i);
//						fd_id=json_data.getInt("FOOD_ID");
//						fd_name=json_data.getString("FOOD_NAME");
//					}
//		
//				}
//				catch(JSONException e1){
//					Toast.makeText(getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
//				}
//				catch (ParseException e1){
//					e1.printStackTrace();
//				}
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