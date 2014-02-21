/*
 * Project		ResponseAble
 * 
 * Package		com.jasonwoolard.responseable
 * 
 * @author		Jason Woolard
 * 
 * Date			Feb 19, 2014
 */
package com.jasonwoolard.responseable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	SharedPreferences sharedPref;
	EditText beersConsumed;
	EditText wineGlassesConsumed;
	EditText shotsConsumed;
	Button calculateBAC;
	EditText hoursSinceFirstConsumption;
	Context context;
	TextView results;
	TextView welcomeMsg;
	String savedName = "";
	TextView resultsText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		// Initializing UI Elements to be used from XML file
		initializeUIElements();
		// Obtaining Shared Preferences Saved Data
		sharedPref = getSharedPreferences(UserSettings.userData, 0);
		// Setting the welcome message depending if there is a name saved in user settings.
		savedName = sharedPref.getString("name", "");
		if (savedName.equals("") || savedName == null)
		{
			welcomeMsg.setText("Welcome to ResponseAble, Androids hottest Blood Alcohol Calculator!");
		}
		else
		{
			Log.i("Saved Name", savedName); 
			welcomeMsg.setText("Welcome back " + savedName + "!");
		}

	}

	public void initializeUIElements() {
		beersConsumed = (EditText) findViewById(R.id.fieldBeers);
		hoursSinceFirstConsumption = (EditText) findViewById(R.id.fieldHours);
		wineGlassesConsumed = (EditText) findViewById(R.id.fieldWineGlasses);
		shotsConsumed = (EditText) findViewById(R.id.fieldShots);
		calculateBAC = (Button) findViewById(R.id.calculateBACBtn);
		calculateBAC.setOnClickListener(this);
		welcomeMsg = (TextView) findViewById(R.id.textWelcomeMsg);
		results = (TextView) findViewById(R.id.textResults);
		resultsText = (TextView) findViewById(R.id.textResultsAsText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
		case R.id.userSettings:
			Intent intent = new Intent(this, UserSettings.class);

			startActivity(intent);

			break;

		}
		return super.onOptionsItemSelected(item);
	}

	public void calculateBAC()
	{
		String savedWeight = sharedPref.getString("weight", "");
		if (savedWeight.equals("") || savedWeight == null)
		{
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
    		// Setting adb properties
    		adb.setTitle(context.getString(R.string.error_weight_title));
    		adb.setMessage(context.getString(R.string.error_weight_msg));
    		adb.setPositiveButton(android.R.string.ok, null);
    		// Setting alertDialog to create initialized adb with set properties
    		AlertDialog alertDialog = adb.create();
    		// Showing alertDialog to user
    		alertDialog.show();
			Log.i("Saved Weight:", savedWeight);
		} 
		else 
		{
			String beerAmount = beersConsumed.getText().toString();
			String wineAmount = wineGlassesConsumed.getText().toString();
			if (wineAmount.equals("") || wineAmount == null)
			{
				wineGlassesConsumed.setText("0");
				wineAmount = wineGlassesConsumed.getText().toString();
			}
			String shotAmount = shotsConsumed.getText().toString();
			if (shotAmount.equals("") || shotAmount == null) {
				shotsConsumed.setText("0");
				shotAmount = shotsConsumed.getText().toString();
			}
			if (beerAmount.equals("") || beerAmount == null){
				beersConsumed.setText("0");
				beerAmount = beersConsumed.getText().toString();
			}
			int beerOunces = Integer.parseInt(beerAmount) * 12;
			double beerCalc = beerOunces * 5 * 0.075;
			Log.i("Beer Calc:", String.valueOf(beerCalc));

			int wineOunces = Integer.parseInt(wineAmount) * 5;
			double wineCalc = wineOunces * 12 * 0.075;
			Log.i("Wine Calc:", String.valueOf(wineCalc));

			double shotOunces = Integer.parseInt(shotAmount) * 1.25;
			double shotCalc = shotOunces * 40 * 0.075;
			Log.i("Shot Calc:", String.valueOf(shotCalc));


			double totalCalc = beerCalc + wineCalc + shotCalc;
			Log.i("Total Calc:", String.valueOf(totalCalc));

			double dividedByWeight = totalCalc / Integer.parseInt(savedWeight);
			Log.i("Divided By Weight: ", String.valueOf(dividedByWeight));


			String hoursSinceConsumption = hoursSinceFirstConsumption.getText().toString();
			if (hoursSinceConsumption.equals("") || hoursSinceConsumption == null){
				hoursSinceFirstConsumption.setText("0");
				hoursSinceConsumption = hoursSinceFirstConsumption.getText().toString();
			}

			double accountForHours = Double.valueOf(hoursSinceConsumption) * 0.015;
			Log.i("Account For Hours: ", String.valueOf(accountForHours));


			double result = dividedByWeight - accountForHours;
			convertBACPercentageToText(result);
			String resultString = String.valueOf(result);
			// Referencing string preference editor
			SharedPreferences.Editor e = sharedPref.edit();
			e.putString("bac", resultString);
			// Committing / Saving out data
			e.commit();
			Log.i("MainActivity", resultString);
			results.setText(resultString);
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.calculateBACBtn:
			calculateBAC();
			break;
		}

	}
	public void convertBACPercentageToText(double percentage)
	{
		if (percentage >= 0.08)
		{
			resultsText.setText("Dude you're drunk DON'T drive, but relax you're able to be responsible... after all think about (insert image here).");
		}
		else if (percentage  >= 0.04 && percentage <= 0.079)
		{
			resultsText.setText("You're possibly impaired, but will pass a breathilizer test in the US. We recommend you to NOT drive.");

		} 
		else if (percentage <= 0.039)
		{
			resultsText.setText("You're good to go! However if you feel weird or even slightly visually impaired, DO NOT DRIVE!");
		}
	}
}