package com.pennapps.spotme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

public class checkinsRequestListener implements RequestListener{
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;

	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub
		try {
			JSONObject json = new JSONObject(response);
			JSONArray son = json.getJSONArray("data");
			json = son.getJSONObject(0);
			String stime = json.getString("start_time");
			String etime = json.getString("end_time");

			json = json.getJSONObject("data");
			json = json.getJSONObject("song");
			String id = json.getString("id");
			Log.e("log_tag", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		//http post
//		
//				try{
//		
//					HttpClient httpclient = new DefaultHttpClient();
//					HttpPost httppost = new HttpPost("http://158.130.107.37:80");
//					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//					HttpResponse reply = httpclient.execute(httppost);
//					HttpEntity entity = reply.getEntity();
//					is = entity.getContent();
//				}
//				catch(Exception e){
//					Log.e("log_tag", "Error in http connection"+e.toString());
//				}
//				//convert response to string
//		
//				try{
//					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
//					sb = new StringBuilder();
//					sb.append(reader.readLine() + "\n");
//					String line="0";
//					while ((line = reader.readLine()) != null) {
//						sb.append(line + "\n");
//					}
//					is.close();
//					result=sb.toString();
//		
//				}
//				catch(Exception e){
//					Log.e("log_tag", "Error converting result "+e.toString());
//				}
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