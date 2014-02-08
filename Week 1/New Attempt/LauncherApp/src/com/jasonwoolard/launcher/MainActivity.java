package com.jasonwoolard.launcher;

import com.jasonwoolard.planit2.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button searchBtn = (Button) findViewById(R.id.button1);
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				EditText inputedText = (EditText) findViewById(R.id.editText1);
				String myUri =  inputedText.getText().toString();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myUri));

			    String title = (String) getResources().getText(R.string.menu_title);
		        Intent chooser = Intent.createChooser(intent, title);
		        startActivityForResult(chooser, 1);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
