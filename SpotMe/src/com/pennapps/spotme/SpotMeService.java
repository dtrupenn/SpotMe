package com.pennapps.spotme;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.service.*;
import android.util.Log;
import android.widget.Toast;
import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;


public class SpotMeService extends Service{
	private LocationManager mlocManager; 
	private LocationListener mlocListener;
	private NotificationManager mNM;
	HttpResponse reply;
	InputStream is = null;
	StringBuilder sb = null;
	String result = null;
	private int NOTIFICATION = R.string.app_name;
	
	public class LocalBinder extends Binder{
		 SpotMeService getService(){
			return SpotMeService.this;
		}
	}

	@Override
	public void onCreate(){
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("activated",true);
		editor.commit();
		System.out.println(settings.getAll());
		showStartNotification();
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new MyLocationListener();
		mlocManager = locationManager;
		mlocListener = locationListener;
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mlocListener);
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SpotMeService", "Received start id " + startId + ": " + intent);
		
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, "SpotMe stopped" , Toast.LENGTH_SHORT).show();
        SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("activated",false);
		editor.commit();
		System.out.println(settings.getAll());
		mlocManager.removeUpdates(mlocListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showStartNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "SpotMe started";

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_launcher, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SpotMeActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "SpotMe activity",
                       text, contentIntent);

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
	
    private void showNotification(){
    	CharSequence text = "Spotted";
    	CharSequence info = "insert info";
    	
    	Notification notification = new Notification (R.drawable.ic_launcher, info, System.currentTimeMillis());
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Information.class), 0);
    	notification.setLatestEventInfo(this, info, text, contentIntent);
    	
    }

    public class MyLocationListener implements LocationListener{
    	public void onLocationChanged(Location loc){
    		JSONObject object = new JSONObject();
    		try {
    			object.put("FID", meRequestListener.fid);
    			object.put("Lat", loc.getLatitude());
				object.put("Long", loc.getLongitude());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		//http post

			try{
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://158.130.107.37:80");
				httppost.setHeader("json", object.toString());
				httppost.getParams().setParameter("jsonpost",object);
				reply = httpclient.execute(httppost);
				if(reply != null){
					Log.d("log_tag", reply.toString());
				}
//				HttpEntity entity = reply.getEntity();
//				is = entity.getContent();
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
				Log.d("log_tag", result);
			}
			catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}
//	
//			//paring data		
//			int fd_id;		
//			String fd_name;	
//			try{	
//				JSONArray jArray = new JSONArray(result);
//				JSONObject json_data=null;
//				for(int i=0;i<jArray.length();i++){
//					json_data = jArray.getJSONObject(i);
//					fd_id=json_data.getInt("FOOD_ID");
//					fd_name=json_data.getString("FOOD_NAME");
//				}
//	
//			}
//			catch(JSONException e1){
//				Toast.makeText(getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
//			}
//			catch (ParseException e1){
//				e1.printStackTrace();
//			}

    		
    		
    		try{
    				object.put("lat", loc.getLatitude());
    				object.put("long",loc.getLongitude());
    			}catch(JSONException e){
    				e.printStackTrace();
    			}
    			System.out.println(object);
    		/*loc.getLatitude();

    		loc.getLongitude();

    		String Text = "My current location is:" +

    		"Latitude = " + loc.getLatitude() +

    		"Logitude = " + loc.getLongitude();


    		Toast.makeText( getApplicationContext(),

    		Text,

    		Toast.LENGTH_SHORT).show();
    		*/
    			SharedPreferences settings = getSharedPreferences(Settings.PREFS_NAME,0);
    			Facebook facebook = new Facebook(settings.getString("app_id","0"));
        		facebook.setAccessToken(getSharedPreferences(Settings.PREFS_NAME,0).getString("access_token","0"));
    			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
//				// post information about the currently logged in user
//				mAsyncRunner.request("me/", new meRequestListener());

				// get information about the currently played song
				mAsyncRunner.request("me/music.listens", new idRequestListener());

				// post song info to server
				mAsyncRunner.request(idRequestListener.songID, new musicRequestListener());

				//			// post checkin data if gps location doesn't work
				//			mAsyncRunner.request("me/friends", new checkinsRequestListener());
        	}
        	public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {
            	Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
            }
            public void onProviderDisabled(String provider) {
            	Toast.makeText(getApplicationContext(),"GPS Disabled", Toast.LENGTH_SHORT).show();
            }

        };
        	
        }


