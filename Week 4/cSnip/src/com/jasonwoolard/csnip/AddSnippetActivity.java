package com.jasonwoolard.csnip;

import com.jasonwoolard.csnipapp.R;
import com.parse.ParseObject;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class AddSnippetActivity extends Activity {

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_snippet);
		
		WebView webView = (WebView) findViewById(R.id.webView1); 
		webView.loadUrl("http://www.jasonwoolard.com/csnip/index.html");
		webView.setWebViewClient(new WebViewClient());
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
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
			
			Log.i("TESTING DUDE", title + code + language);

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
				// Saving snippit to the backend
				snippet.saveInBackground();
				// Closing (finishing) activity
			}
		}
	}
}
