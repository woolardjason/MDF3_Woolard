package com.jasonwoolard.skybrowser;


import com.jasonwoolard.skybrowser.fragments.BrowserSettingsFragment;
import com.jasonwoolard.skybrowser.fragments.BrowserSettingsFragment.settingsListener;
import com.jasonwoolard.skybrowser.fragments.SearchGoogleFragment;
import com.jasonwoolard.skybrowser.fragments.SearchGoogleFragment.searchListener;
import com.jasonwoolard.skybrowser.network.NetworkConnectivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BrowserActivity extends Activity implements OnClickListener, settingsListener, searchListener {
	WebView skyBrowser;
	EditText inputUrl;
	Context context;
	static Boolean userConnected = false;
	String mTAG = "BrowserActivity Class";

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		context = this;
		// Defining WebView to existing one in XML activity_browser
		skyBrowser = (WebView) findViewById(R.id.skyBrowser);
		// Enabling JavaScript for the WebView
		 skyBrowser.getSettings().setJavaScriptEnabled(true);
		 // Setting Web View Client in order to properly display URLs inside skyBrowser WebView
		 skyBrowser.setWebViewClient(new WebViewClient() {
			 // When an error is received, display a toast notification.
	            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	                Toast.makeText(BrowserActivity.this, description, Toast.LENGTH_LONG).show();
	            }
	        });
		skyBrowser.loadUrl("http://www.fullsail.edu");
		inputUrl = (EditText) findViewById(R.id.userInput);
		
		// Back Button
		Button backButton = (Button) findViewById(R.id.button_back);
		backButton.setOnClickListener(this);
		// Forward Button
		Button forwardButton = (Button) findViewById(R.id.button_forward);
		forwardButton.setOnClickListener(this);
		// Go To URL Button
		Button goToUrlButton = (Button) findViewById(R.id.button_goToUrl);
		goToUrlButton.setOnClickListener(this);
		// Getting Intent Data passed from external implicit intent 
		Intent intent = getIntent();
		Uri website = intent.getData();
		if (website != null) 
		{
		String fURL = website.toString();
		// Launching website based on data passed in & updating textView
		skyBrowser.loadUrl(fURL);
		inputUrl.setText(fURL);
		}
	}
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.button_back:
			if (skyBrowser.canGoBack())
			{
				skyBrowser.goBack();
			}
			break;
		case R.id.button_forward:
			if (skyBrowser.canGoForward())
			{
				skyBrowser.goForward();
			}
			break;
		case R.id.button_goToUrl:
			String url = inputUrl.getText().toString();
			if (url != null || url != "")
			{
				// If user types in http://, remove it to prevent from it being put in twice
				String newUrl = url.replace("http://", "");
				// Construct a new url with what the user typed, and adding in the http:// to ensure it's their once
				String finalUrl = "http://" + newUrl;
				// Loading the finalUrl
				skyBrowser.loadUrl(finalUrl);
				// Updating the textField
				inputUrl.setText(finalUrl);
			}
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
		case R.id.browserCancel:
			skyBrowser.stopLoading();
			break;
		case R.id.browserRefresh:
			skyBrowser.reload();
			break;
		case R.id.browserSettings:
			showSettingsDialog();
			break;
		case R.id.searchDialog:
			showDialog();
			break;
		case R.id.share_action:
			// Grabbing title of webpage and url as well as default message to share
			String fUrl = getString(R.string.share_message) + skyBrowser.getTitle().toString() + " - " + skyBrowser.getUrl().toString();
			// Declaring a new intent (Share intent)
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			// Adding extras to intent to properly share text
			intent.putExtra(Intent.EXTRA_TEXT, fUrl);
			// Utilizing createChooser for flexibility
			startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}
	public void searchGoogle(String inputedString) {
		Log.i(mTAG, "Search Button Fired!");

		userConnected = NetworkConnectivity.pullConnectionStatus(context);
		// If user is connected, perform service call
		if (userConnected) 
		{		
			if (inputedString != null && !inputedString.equals("")) 
			{
				skyBrowser.loadUrl(inputedString);		
				inputUrl.setText(inputedString);
			}
			else
			{
				  AlertDialog.Builder adb = new AlertDialog.Builder(context);
	        		// Setting adb properties
	        		adb.setTitle(context.getString(R.string.error_title));
	        		adb.setMessage(context.getString(R.string.empty_field_error_message));
	        		adb.setPositiveButton(android.R.string.ok, null);
	        		// Setting alertDialog to create initialized adb with set properties
	        		AlertDialog alertDialog = adb.create();
	        		// Showing alertDialog to user
	        		alertDialog.show();
			}
		} 
		else 
		{

			AlertDialog.Builder adb = new AlertDialog.Builder(context);
      		// Setting adb properties
      		adb.setTitle(context.getString(R.string.error_title));
      		adb.setMessage(context.getString(R.string.network_error_message));
      		adb.setPositiveButton(android.R.string.ok, null);
      		// Setting alertDialog to create initialized adb with set properties
      		AlertDialog alertDialog = adb.create();
      		// Showing alertDialog to user
      		alertDialog.show();
		}
	}
	@SuppressLint("NewApi")
	public void showDialog() {
	    // Create the fragment and show it as a dialog.
	    SearchGoogleFragment newFragment = SearchGoogleFragment.newInstance();
	    newFragment.show(getFragmentManager(), "dialog");
	}
	public void showSettingsDialog() {
	    // Create the fragment and show it as a dialog.
	    BrowserSettingsFragment newFragment = BrowserSettingsFragment.newInstance();
	    newFragment.show(getFragmentManager(), "dialog");
	}
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void toggleJavaScript(Boolean isEnabled) {
		if (isEnabled)
		{
			 skyBrowser.getSettings().setJavaScriptEnabled(true);
		}
		else if (isEnabled == false)
		{
			 skyBrowser.getSettings().setJavaScriptEnabled(false);
		}
		// Refreshing browser to account for javascript change
		 skyBrowser.reload();
	}
	@Override
	public Boolean checkJavaScriptStatus()
	{
		Boolean javaScriptEnabled = skyBrowser.getSettings().getJavaScriptEnabled();
		return javaScriptEnabled;
	}
	@Override
	// Method used to clear the users browser history
	public void clearBrowserHistory() {
		skyBrowser.clearHistory();		
	}
}