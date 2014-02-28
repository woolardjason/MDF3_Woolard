package com.jasonwoolard.csnip;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jasonwoolard.csnipapp.R;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AddSnippetActivity extends Activity {
	WebView webView;
	ParseUser user;
	LocationManager lm;
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_snippet);
		user = ParseUser.getCurrentUser();

		webView = (WebView) findViewById(R.id.webView1); 
		webView.setWebViewClient(new WebViewClient()); 
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		if (savedInstanceState != null)
		{
			webView.restoreState(savedInstanceState);
		}
		else
		{
			webView.loadUrl("file:///android_asset/index.html");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_snippet, menu);
		return true;
	}
	public class WebAppInterface {
		Context context;
		WebAppInterface(Context c) {
			context = c;
		}
		@JavascriptInterface
		public void displayData(String[] info) 
		{	
			String title = info[0];
			String code = info[1];
			String language = info[2];
			
			Log.i("TESTING: ", title + " -  " + code + " - " + language);

			// Validation Check to see if each string (value) is set, if not display a toast to the user
			if (title.equals("") || title.length() == 0  || title == null) 
			{
				Toast.makeText(getApplicationContext(), "Please check code snippet title", Toast.LENGTH_LONG).show();
			}  
			else if (code.equals("") || code.length() == 0 || code == null) 
			{
				Toast.makeText(getApplicationContext(), "Please check code snippet length", Toast.LENGTH_LONG).show();
			}
			else if (language.equals("") || language.length() == 0 || language == null) 
			{
				Toast.makeText(getApplicationContext(), "Please check code snippet language", Toast.LENGTH_LONG).show();
			} 
			else 
			{
				// Creating new snippit to be stored to the backend
				ParseObject snippet = new ParseObject("Snippets");
				snippet.put("title", title);
				snippet.put("code", code);
				snippet.put("language", language);
				snippet.put("postedBy", user);
				// Saving snippit to the backend
				snippet.saveInBackground(new SaveCallback()
				{

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) 
						{
							finish();
							Toast.makeText(getApplicationContext(), "Code Snippet Saved Successfully!", Toast.LENGTH_LONG).show();
							// Recording users last location to be used in local connections feature!
							lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
							LocationListener ll = new myLocationListener();
							lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
						} 
						else 
						{
							Toast.makeText(getApplicationContext(), "An error has occured: " + e +  "\n" + "Please try posting again shortly.", Toast.LENGTH_LONG).show();
						}
					}
				});
				// Closing (finishing) activity
			}
		}
		@JavascriptInterface
		public void goBack()
		{
			finish();
		}
			
	}
	public class myLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (location != null)
			{
				double mLat = location.getLatitude();
				double mLng = location.getLongitude();
				Log.i("Geo Test: ", Double.toString(mLng) + Double.toString(mLat));
				ParseGeoPoint point = new ParseGeoPoint(mLat, mLng);
				user.put("lastKnownLocation", point);
				user.saveInBackground();
				
	            lm.removeUpdates(this);
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status,
				Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	      webView.saveState(outState);
	}
}
