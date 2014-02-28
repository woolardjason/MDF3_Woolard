package com.jasonwoolard.csnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonwoolard.csnipapp.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

public class SnippetDetailActivity extends Activity {
	String mTitle;
	String mCode;
	String mLanguage;
	String mObjectId;
	
	TextView mCodeTitle;
	TextView mCodeCode;
	TextView mCodeLanguage;
	
	Button deleteButton;
	
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
		mObjectId = intent.getStringExtra("oid");
		
		mCodeTitle.setText(mTitle);
		mCodeCode.setText(mCode);
		mCodeLanguage.setText(mLanguage);
		
		deleteButton = (Button) findViewById(R.id.deleteBtn);
		deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	// Deleting the object from parse
            	ParseObject.createWithoutData("Snippets", mObjectId).deleteInBackground(new DeleteCallback() {
					@Override
					public void done(ParseException e) {
						if (e == null)
						{
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							finish();
							Toast.makeText(getApplicationContext(), "You have successfully deleted the snippet.", Toast.LENGTH_LONG).show();

						}
					}
            			
            	});
    
            }
        });	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) 
		{
		case R.id.action_share_snippet:
			String msg = "I just made the coolest snippet of code called " + mTitle + " for " + mLanguage + "." + "\n" + "Come check it out by downloading cSnip on Google Play Market for Android!";
			// Declaring a new intent (Share intent)
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			// Adding extras to intent to properly share text
			intent.putExtra(Intent.EXTRA_TEXT, msg);
			// Utilizing createChooser for flexibility
			startActivity(Intent.createChooser(intent, getString(R.string.share_title)));

			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.snippet_detail, menu);

		return super.onCreateOptionsMenu(menu);
	}
}
