package com.jasonwoolard.geomeet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
	// Constants
	final static int mData = 0;
	Bitmap bitmap;
	ImageView inputtedImage;
	EditText addressTxtField;
	EditText meetUpInfoTxtField;
	Button shareMeetUp;
	Button snapPhoto;
	Button grabUserLocation;
	
	String mTAG = "MainActivity Class - ";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeUIElements();
	}

	private void initializeUIElements() {
		inputtedImage = (ImageView) findViewById (R.id.userInputedImage);
		snapPhoto = (Button) findViewById (R.id.snapPhotoBtn);
		snapPhoto.setOnClickListener(this);
		grabUserLocation = (Button) findViewById (R.id.currentLocationBtn);
		grabUserLocation.setOnClickListener(this);
		addressTxtField = (EditText) findViewById (R.id.addressField);
		meetUpInfoTxtField = (EditText) findViewById (R.id.meetUpDescription);
		shareMeetUp = (Button) findViewById (R.id.shareMeetUpBtn);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.snapPhotoBtn:
			Log.i(mTAG, "Snap Photo Button Fired!");
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, mData);
			break;
		case R.id.currentLocationBtn:
			break;
		case R.id.shareMeetUpBtn:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// Operation succeeded? If so:
		if (resultCode == RESULT_OK)
		{
			Bundle bExtras = data.getExtras();
			bitmap = (Bitmap) bExtras.get("data");
			inputtedImage.setImageBitmap(bitmap);
			
		}
	}
	

}
