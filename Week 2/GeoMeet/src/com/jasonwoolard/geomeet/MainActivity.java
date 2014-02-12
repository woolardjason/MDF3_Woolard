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
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
	// Constants
	final static int mData = 0;
	Bitmap bitmap;
	ImageView inputtedImage;
	String mTAG = "MainActivity Class - ";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializeUIElements();
	}

	private void initializeUIElements() {
		inputtedImage = (ImageView) findViewById (R.id.userInputedImage);
		Button snapPhoto = (Button) findViewById (R.id.snapPhotoBtn);
		snapPhoto.setOnClickListener(this);
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
