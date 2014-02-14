/*
 * Project		GeoMeet
 * 
 * Package		com.jasonwoolard.geomeet
 * 
 * @author		Jason Woolard
 * 
 * Date			Feb 13, 2014
 */
package com.jasonwoolard.geomeet;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class GoogleGeocodeService extends IntentService
{

	public static final String MESSENGER_KEY = "messenger";
	public static final String LONGITUDE_KEY = "longitude";
	public static final String LATITUDE_KEY = "latitude";

	static String mTAG = "Google Geocode Service Class - ";
	String apiUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
	static ArrayList<HashMap<String, String>> locationDetails = new ArrayList<HashMap<String, String>>();

	String encodedString = "";
	String latitude = null;
	String longitude = null;
	String response = null;
	JSONArray mResults;
	URL finalURL;
	Bundle extras;

	public GoogleGeocodeService() 
	{
		super("GoogleGeocodeService");
		Log.i(mTAG, "Service has started!");
	}
	// getURLResponse method
	public static String getURLResponse(URL url) {
		String response = "";
		try {
			// Initializing URLConnection
			URLConnection connection = url.openConnection();
			// Setting created BufferedInputStream (bis) to connection input stream
			BufferedInputStream bis = new BufferedInputStream(
					connection.getInputStream());
			// Setting contextByte
			byte[] contextByte = new byte[1024];
			// Setting int bytesRead
			int bytesRead = 0;
			// Setting StringBuffer entitled responseBuffer to new StringBuffer
			StringBuffer responseBuffer = new StringBuffer();
			while ((bytesRead = bis.read(contextByte)) != -1) {
				response = new String(contextByte, 0, bytesRead);
				responseBuffer.append(response);
			}
			// String buffer being set to a string (getting ready for the return of response)
			response = responseBuffer.toString();
		} catch (Exception e) {
			Log.e("Something happened, no info returned! ::: ", e.toString());
		}
		// Returning the string response defined above
		return response;
	}
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		extras = intent.getExtras();
		if (extras != null)
		{ 
			 longitude = extras.getString(LONGITUDE_KEY);
			 latitude = extras.getString(LATITUDE_KEY);
		}
		try {
			// Initializing a new url with passed in key, api string, and custom string for the query
			finalURL = new URL(apiUrl + latitude + "," + longitude + "&sensor=false");
			Log.i(mTAG, finalURL.toString());
			// Setting the response to return the urls json response by utilizing the getURLResponse method and passing in finalURL
			response = getURLResponse(finalURL);
			
			

			JSONObject locationObject;
				try {
					JSONObject json = new JSONObject(response);
					mResults = json.getJSONArray("results");

					Log.i(mTAG, mResults.toString());
					
					locationObject = mResults
							.getJSONObject(0);
					String formattedAddress = locationObject.getString("formatted_address");

					String address = formattedAddress;
					HashMap<String, String> map = new HashMap<String, String>();

					
					map.put("formatted_address", address);

					locationDetails.add(map);
					
					// Obtaining messenger key
					Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
					Message message = Message.obtain();
					
					// Checking whether message is null, if not then:
					if (message != null)
					{
						message.arg1 = Activity.RESULT_OK;
						message.obj = locationDetails;
						Log.i(mTAG, "Service has Ended!");
					}
					try
					{
						messenger.send(message);
					}
					catch (RemoteException e)
					{
						Log.e("onHandleIntent e ::: ", e.getMessage().toString());
					}
					
				} catch (JSONException e) {
					Log.e("JSON ERROR", e.toString());
				}
						
		} catch (MalformedURLException e) {
			Log.e("ERROR ::::: ", "MalformedURLException");
			finalURL = null;
			e.printStackTrace();
		}
	}
}

