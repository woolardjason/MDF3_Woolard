package com.example.planit;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TripInfoActivity extends Activity {
	static JSONArray mResults;
	static String mTAG = "TripInfoActivity Class";
	static Context context;
	// Defining Service Class Handler extending from Handler 
			private static class ServiceClassHandler extends Handler {
				private final WeakReference<TripInfoActivity> mActivity;

				public ServiceClassHandler(TripInfoActivity activity) {
					mActivity = new WeakReference<TripInfoActivity>(activity);
				}
				@Override
				public void handleMessage(Message msg) {
					final TripInfoActivity activity = mActivity.get();
					if (activity != null) {
						if (msg.arg1 == RESULT_OK && msg.obj != null) {
							try {
								String apiURL = msg.obj.toString();

								JSONObject json = new JSONObject(apiURL);
								mResults = json.getJSONArray("results");

								Log.i(mTAG, mResults.toString());
								
								
							} catch (JSONException e) {
								Log.e("JSON ERROR", e.toString());
							}
						}
					}
				}
			}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		context = this;
		  Button searchButton = (Button) findViewById(R.id.grabLocationInfoBtn);
	        // OnClickListener for the searchButton to be called upon click
	        searchButton.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View view) {
	            	initiateService();
	            	Log.i(mTAG, "Button Clicked");
	            }
	        });
	}
	
	public void initiateService() {
		final ServiceClassHandler mHandler = new ServiceClassHandler(this);
		Messenger serviceClassMessenger = new Messenger(mHandler);
		Intent intent = new Intent(context, ServiceClass.class);
		intent.putExtra("messenger", serviceClassMessenger);
		intent.putExtra("address", "85+emerald+lane+marstons+mills+ma");
		// Starting service
		startService(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

}
