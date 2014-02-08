package com.jasonwoolard.skybrowser;


import com.jasonwoolard.skybrowser.fragments.BrowserSettingsFragment;
import com.jasonwoolard.skybrowser.fragments.BrowserSettingsFragment.searchListener;
import com.jasonwoolard.skybrowser.fragments.SearchGoogleFragment;
import com.jasonwoolard.skybrowser.network.NetworkConnectivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

public class BrowserActivity extends Activity implements OnClickListener, searchListener {
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
			skyBrowser.loadUrl(url);
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
			// TODO: DialogFragment to be implemented here
			showSettingsDialog();
			break;
		case R.id.searchDialog:
			// TODO: DialogFragment to be implemented here
			//https://www.google.com/?q=SearchHere#q=SearchHere
			showDialog();
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
		if (userConnected) {

			
			if (inputedString != null && !inputedString.equals("")) 
			{
				skyBrowser.loadUrl(inputedString);		
				inputUrl.setText(inputedString);
			}
			
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
	}
	@Override
	public Boolean checkJavaScriptStatus()
	{
		Boolean javaScriptEnabled = skyBrowser.getSettings().getJavaScriptEnabled();
		return javaScriptEnabled;
	}
}