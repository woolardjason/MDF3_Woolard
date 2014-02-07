/*
 * Project		Planit
 * 
 * Package		com.example.planit
 * 
 * @author		Jason Woolard
 * 
 * Date			Feb 6, 2014
 */
package com.example.planit;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TripInfoActivity extends Activity {
	static JSONArray mResults;
	EditText searchField;
	static TextView addressText;
	static String mTAG = "TripInfoActivity Class";
	static Context context;
	static String latitude;
	static String longitude;
	static SimpleAdapter adapter;
	static ListView locationInfoList;
	static Boolean isConnected = false;
	static Boolean mConnected = false;
	static ArrayList<HashMap<String, String>> locationInfo = new ArrayList<HashMap<String, String>>();

	// Defining Service Class Handler extending from Handler 
	private static class ServiceClassHandler extends Handler {
		private final WeakReference<TripInfoActivity> mActivity;

		public ServiceClassHandler(TripInfoActivity activity) {
			mActivity = new WeakReference<TripInfoActivity>(activity);
		}
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			final TripInfoActivity activity = mActivity.get();
			if (activity != null) {
				if (msg.arg1 == RESULT_OK && msg.obj != null) {
					
					locationInfo = (ArrayList<HashMap<String, String>>) msg.obj;
					addressText.setText(locationInfo.get(0).get("formatted_address"));
					adapter = new SimpleAdapter(
							context, locationInfo,
							R.layout.list_view, new String[] { "temperature_string",
									"wind_string", "feelslike_string" },
									new int[] { R.id.temperatureInfo,
									R.id.windInfo,
									R.id.feelsLikeInfo });
					locationInfoList.setAdapter(adapter);
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		context = this;
		locationInfoList = (ListView) this.findViewById(R.id.locationInfo);
		View locationInfoHeader = this.getLayoutInflater().inflate(
				R.layout.list_view_header, null);
		locationInfoList.addHeaderView(locationInfoHeader);
		// Setting Local Constants to views in XML
        searchField = (EditText) findViewById(R.id.addressField);
		addressText = (TextView) findViewById(R.id.addressText);
		
		Button searchButton = (Button) findViewById(R.id.grabLocationInfoBtn);
		// OnClickListener for the searchButton to be called upon click
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				locationInfo.clear();
				Log.i(mTAG, "Button Clicked");

				mConnected = pullConnectionStatus(context);
				// If user is connected...
				if (mConnected) 
				{
					initiateGoogleService();
				} 
				else
				{
					AlertDialog.Builder adb = new AlertDialog.Builder(context);
					// Setting adb properties
					adb.setTitle(context.getString(R.string.alert_title));
					adb.setMessage(context.getString(R.string.alert_message));
					adb.setPositiveButton(android.R.string.ok, null);
					// Setting alertDialog to create initialized adb with set properties
					AlertDialog alertDialog = adb.create();
					// Showing alertDialog to user
					alertDialog.show();
				}
			}
		});
	}
	// Method to start service by taking in search field input and passing it to intent
	public void initiateGoogleService() {
		final ServiceClassHandler mHandler = new ServiceClassHandler(this);
		Messenger serviceClassMessenger = new Messenger(mHandler);
		Intent intent = new Intent(context, WeatherServiceClass.class);
		intent.putExtra("messenger", serviceClassMessenger);
		String inputedText = searchField.getText().toString();
		Log.i(mTAG, inputedText);
		intent.putExtra("address", inputedText);
		// Starting service
		startService(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	public static Boolean pullConnectionStatus(Context context)
	{
		isConnected = false;
		verifyConnectionStatus(context);
		return isConnected;
	}
	// Method used to check user connectivity
	public static void verifyConnectionStatus(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if (networkInfo != null)
		{
			if (networkInfo.isConnected())
			{
				Log.i(mTAG, "Connection Type: " +networkInfo.getTypeName());
				
				isConnected = true;
			}
		}
	}
}
