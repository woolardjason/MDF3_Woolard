package com.jasonwoolard.csnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jasonwoolard.csnipapp.R;

public class SnippetDetailActivity extends Activity {
	String mTitle;
	String mCode;
	String mLanguage;
	
	TextView mCodeTitle;
	TextView mCodeCode;
	TextView mCodeLanguage;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.snippet_detail_activity);
		
		mCodeTitle = (TextView) findViewById(R.id.codeTitle);
		mCodeCode = (TextView) findViewById(R.id.codeCode);
		mCodeLanguage = (TextView) findViewById(R.id.codeLanguage);
		
		Intent intent = getIntent();
		
		mTitle = intent.getStringExtra("title");
		mCode = intent.getStringExtra("code");
		mLanguage = intent.getStringExtra("language");
		
		mCodeTitle.setText(mTitle);
		mCodeCode.setText(mCode);
		mCodeLanguage.setText(mLanguage);
		
	}
}
