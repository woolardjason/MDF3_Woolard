package com.jasonwoolard.responseable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class UserSettings extends Activity implements OnClickListener {

	EditText userWeight;
	EditText userName;
	Switch userGender;
	Button saveSettings;
	Context context;
	
	public static String userData = "UserSavedData";
	SharedPreferences sharedPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_settings);
		context = this;
		// Initializing all User Interface Elements to be used throughout the Activity
		initializeUIElements();
		// Obtaining Shared Preferences Data
		sharedPref = getSharedPreferences(userData, 0);
		String savedWeight = sharedPref.getString("weight", "");
		userWeight.setText(savedWeight);
		String savedName = sharedPref.getString("name", "");
		userName.setText(savedName);
		
		Boolean savedGender = sharedPref.getBoolean("gender", true);
		if (savedGender == true)
		{
			userGender.setChecked(true);
		}
		else
		{
			userGender.setChecked(false);
		}
		
		
	}
	// User Interface Element method to initialize all UI elements from XML
	private void initializeUIElements() 
	{
		saveSettings = (Button) findViewById(R.id.saveBtn);
		userWeight = (EditText) findViewById(R.id.fieldUserWeight);
		userName = (EditText) findViewById(R.id.fieldUserName);
		userGender = (Switch) findViewById(R.id.switchUserGender);
		saveSettings.setOnClickListener(this);
	}
	
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.saveBtn:
			// Referencing string preference editor
			SharedPreferences.Editor e = sharedPref.edit();
						
			// Obtaining user's weight from EditText
			String usersWeight = userWeight.getText().toString();
			// Putting string in weight key
			e.putString("weight", usersWeight);
			
			String usersName = userName.getText().toString();
			e.putString("name", usersName);
			
			Boolean isMale = userGender.isChecked();
			e.putBoolean("gender", isMale);
			
			// Committing / Saving out data
			e.commit();
		
    		// Dismiss the current activity
			finish();
			
			// Alert the user via Toast that the settings have been successfully saved
			Toast.makeText(getApplicationContext(), "Settings Successfully Saved!",
					   Toast.LENGTH_LONG).show();
			break;
		}
	}
}
