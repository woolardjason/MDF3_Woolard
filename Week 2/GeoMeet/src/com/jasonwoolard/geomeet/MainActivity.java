package com.jasonwoolard.geomeet;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnClickListener, LocationListener{
	// Constants
	final static int mData = 1;
	Bitmap bitmap;
	ImageView inputtedImage;
	static EditText addressTxtField;
	static EditText meetUpInfoTxtField;
	Button shareMeetUp;
	Button snapPhoto;
	Button grabUserLocation;
	LocationManager locationManager;
	Context context;

	public Uri inputedImageUri;
	Uri sImage;

	List<Address> addresses;
	double longitude;
	double latitude;
	Geocoder geoCoder;
	String providers;
	static ProgressBar progressBar;

	static ArrayList<HashMap<String, String>> locationInfo = new ArrayList<HashMap<String, String>>();

	String mTAG = "MainActivity Class - ";
	private static class ServiceClassHandler extends Handler {
		private final WeakReference<MainActivity> mActivity;

		public ServiceClassHandler(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			final MainActivity activity = mActivity.get();
			if (activity != null) {
				if (msg.arg1 == RESULT_OK && msg.obj != null) {

					locationInfo = (ArrayList<HashMap<String, String>>) msg.obj;


					addressTxtField.setText(locationInfo.get(0).get("formatted_address"));
					hideProgressBar();

				}
			}
		}
	}
	public static void showProgressBar()
	{
		progressBar.setVisibility(View.VISIBLE);
	}

	public static void hideProgressBar()
	{
		progressBar.setVisibility(View.INVISIBLE);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
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
		shareMeetUp.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.INVISIBLE);

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
			File photoTaken = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoTaken));
			inputedImageUri = Uri.fromFile(photoTaken);
			startActivityForResult(intent, mData);
			break;
		case R.id.currentLocationBtn:
			showProgressBar();
			Log.i(mTAG, "Current Location Button Hit");
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			providers = locationManager.getBestProvider(criteria, false);
			locationManager.requestLocationUpdates(providers, 3000, 0, this);

			break;
		case R.id.shareMeetUpBtn: 
			String addy = addressTxtField.getText().toString();
			String desc  = meetUpInfoTxtField.getText().toString();
			if (sImage != null)
			{
				Uri photoUri = Uri.parse(sImage.toString());
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setData(photoUri);
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, desc + "@" + addy + "\n\n" + "Posted via GeoMeet!");
				shareIntent.setType("image/png");
				shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
				startActivity(Intent.createChooser(shareIntent, "Share via..."));
			}
			else
			{
				AlertDialog.Builder adb = new AlertDialog.Builder(context);
				// Setting adb properties
				adb.setTitle(context.getString(R.string.dialog_title));
				adb.setMessage(context.getString(R.string.dialog_msg));
				adb.setPositiveButton(android.R.string.ok, null);
				// Setting alertDialog to create initialized adb with set properties
				AlertDialog alertDialog = adb.create();
				// Showing alertDialog to user
				alertDialog.show();
			}



			break;
		}
	}

	public void initializeService(double lat, double lng) {
		final ServiceClassHandler mHandler = new ServiceClassHandler(this);
		Messenger serviceClassMessenger = new Messenger(mHandler);

		Intent i = new Intent(context, GoogleGeocodeService.class);
		i.putExtra("messenger", serviceClassMessenger);

		i.putExtra("latitude", Double.toString(lat));
		i.putExtra("longitude", Double.toString(lng));
		// Starting service
		startService(i);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// Operation succeeded? If so:
		switch (requestCode) {
		case mData:
			if (resultCode == RESULT_OK)
			{
				sImage = inputedImageUri;
				getContentResolver().notifyChange(sImage, null);

				ContentResolver contentResolver = getContentResolver();

				Bitmap bmp = null;

				try {
					bmp = android.provider.MediaStore.Images.Media
							.getBitmap(contentResolver, sImage);

					inputtedImage.setImageBitmap(bmp);
					Toast.makeText(this, sImage.toString(),
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
					.show();
					Log.e("Camera", e.toString());
				}

			}

			break;
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null)
		{
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			Log.i(mTAG, Double.toString(latitude) + Double.toString(longitude));
			initializeService(latitude, longitude);


		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}


}
