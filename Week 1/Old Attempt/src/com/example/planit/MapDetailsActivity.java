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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

public class MapDetailsActivity extends Activity {
	
	protected String sUrl;
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_details_activity);
		Intent intent = getIntent();
		Uri videoGameUri = intent.getData();
		sUrl = videoGameUri.toString();

		WebView webView = (WebView) findViewById(R.id.webView1);
		webView.loadUrl(sUrl);
	}

}
