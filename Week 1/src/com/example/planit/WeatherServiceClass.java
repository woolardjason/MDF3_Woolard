package com.example.planit;

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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class WeatherServiceClass extends IntentService
{
	public static final String MESSENGER_KEY = "messenger";
	public static final String ADDRESS_KEY = "address";

	static String mTAG = "Weather Service Class - ";
	String apiUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	String weatherApiUrl = "http://api.wunderground.com/api/1db86998d184d234/conditions/q/";
	static ArrayList<HashMap<String, String>> locationDetails = new ArrayList<HashMap<String, String>>();

	String encodedString = "";
	String address = null;
	String latitude = null;
	String longitude = null;
	String response = null;
	String weatherResponse = null;
	String formattedAddress = null;
	JSONArray mResults;
	JSONObject wResults;
	URL finalURL;
	Bundle extras;

	public WeatherServiceClass() 
	{
		super("WeatherServiceClass");
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
			// Setting address constant to the set ADDRESS_KEY from TripInfoActivity
			 address = extras.getString(ADDRESS_KEY);
			 address = address.replace(" ", "+");
		}
		try {
			// Initializing a new url with passed in key, api string, and custom string for the query
			finalURL = new URL(apiUrl + address + "&sensor=false");
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
					JSONObject geometry = locationObject.getJSONObject("geometry");
					formattedAddress = locationObject.getString("formatted_address");

					JSONObject location = geometry.getJSONObject("location");
					latitude = location.getString("lat");
					longitude = location.getString("lng");
					Log.i(mTAG, latitude + " " + longitude);
					
					
				} catch (JSONException e) {
					Log.e("JSON ERROR", e.toString());
				}
				checkWeather();
						
		} catch (MalformedURLException e) {
			Log.e("ERROR ::::: ", "MalformedURLException");
			finalURL = null;
			e.printStackTrace();
		}
	}
	public void checkWeather () {
		try {
			URL url = new URL(weatherApiUrl + latitude +"," + longitude + ".json");
			weatherResponse = getURLResponse(url);
			Log.i(mTAG, weatherResponse);

				try {
					JSONObject json = new JSONObject(weatherResponse);
					wResults = json.getJSONObject("current_observation");
					String temperatureDetails = wResults.getString("temperature_string");
					String windDetails = wResults.getString("wind_string");
					String feelsLikeDetails = wResults.getString("feelslike_string");
					String lastUpdated = wResults.getString("observation_time");
					String iconImgUrl = wResults.getString("icon_url");
					String address = formattedAddress;
					HashMap<String, String> map = new HashMap<String, String>();

					map.put("temperature_string", temperatureDetails);
					map.put("wind_string", windDetails);
					map.put("feelslike_string", feelsLikeDetails);
					map.put("observation_time", lastUpdated);
					map.put("icon_url", iconImgUrl);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
