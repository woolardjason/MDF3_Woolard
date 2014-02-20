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

public class MainActivity extends Activity implements OnClickListener{
	SharedPreferences sharedPref;
	EditText beersConsumed;
	EditText wineGlassesConsumed;
	EditText shotsConsumed;
	Button calculateBAC;
	EditText hoursSinceFirstConsumption;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sharedPref = getSharedPreferences(UserSettings.userData, 0);
		String savedName = sharedPref.getString("name", "");
		
		beersConsumed = (EditText) findViewById(R.id.fieldBeers);
		hoursSinceFirstConsumption = (EditText) findViewById(R.id.fieldHours);
		wineGlassesConsumed = (EditText) findViewById(R.id.fieldWineGlasses);
		shotsConsumed = (EditText) findViewById(R.id.fieldShots);
		calculateBAC = (Button) findViewById(R.id.button1);
		calculateBAC.setOnClickListener(this);

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
		Log.i("Saved Weight:", savedWeight);
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
		// TODO: Replace 1 with dynamic hour placement from EditText
		double accountForHours = Double.valueOf(hoursSinceConsumption) * 0.015;
		Log.i("Account For Hours: ", String.valueOf(accountForHours));

		
		double result = dividedByWeight - accountForHours;
				
		String resultString = String.valueOf(result);
		Log.i("MainActivity", resultString);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.button1:
			calculateBAC();
			break;
		}
		
	}
}
