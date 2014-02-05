package com.example.planit;

import java.io.BufferedInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class ServiceClass extends IntentService
{
	public static final String MESSENGER_KEY = "messenger";
	public static final String ADDRESS_KEY = "address";

	static String mTAG = "Service Class - ";
	String apiUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=";
	String encodedString = "";
	String address = null;
	String response = null;
	URL finalURL;

	public ServiceClass() 
	{
		super("ServiceClass");
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
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			// Setting address constant to the set ADDRESS_KEY from TripInfoActivity
			 address = extras.getString(ADDRESS_KEY);
			 address = address.replace(" ", "+");
		}
		try {
			// Initializing a new url with passed in gameYear key, giant bomb api string, and custom string in between for the query
			finalURL = new URL(apiUrl + address + "&sensor=false");
			Log.i(mTAG, finalURL.toString());
			// Setting the response to return the urls json response by utilizing the getURLResponse method and passing in finalURL
			response = getURLResponse(finalURL);

		} catch (MalformedURLException e) {
			Log.e("ERROR ::::: ", "MalformedURLException");
			finalURL = null;
			e.printStackTrace();
		}
		// Obtaining messenger key
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);
		Message message = Message.obtain();
		
		// Checking whether message is null, if not then:
		if (message != null)
		{
			message.arg1 = Activity.RESULT_OK;
			message.obj = response;
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
	}

}
