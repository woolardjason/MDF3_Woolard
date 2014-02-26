package com.jasonwoolard.csnip;

import android.app.Activity;
import android.os.Bundle;

import com.parse.ParseAnalytics;
import com.jasonwoolard.csnip.R;

public class MainActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpened(getIntent());
	}
}
