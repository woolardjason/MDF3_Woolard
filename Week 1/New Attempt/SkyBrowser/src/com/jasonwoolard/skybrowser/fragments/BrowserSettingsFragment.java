package com.jasonwoolard.skybrowser.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.jasonwoolard.skybrowser.R;


@SuppressLint("NewApi")
public class BrowserSettingsFragment extends DialogFragment  {
	String searchString = "";
	Switch javaScriptSwitch;
	Boolean checkIfJavaScriptEnabled;

	public static BrowserSettingsFragment newInstance() {
		return new BrowserSettingsFragment();
	}
	private settingsListener listener;
	public interface settingsListener
	{
		public void toggleJavaScript(Boolean isEnabled);
		public Boolean checkJavaScriptStatus();
		public void clearBrowserHistory();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (settingsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "Attaching google search dialog fragment failed!");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Disabling the title, using custom 'header' in XML
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// Inflating layout with browser_settings_fragment one
		final View v = inflater.inflate(R.layout.browser_settings_fragment, container, false);
		// Setting constant javaScriptSwitch to the one created in XML
		javaScriptSwitch = (Switch)  v.findViewById(R.id.javaScriptSwitch); 
		// Setting checkIfJavaScriptEnabled boolean constant to run method in BrowserActivity to check and see if java scripts enabled
		checkIfJavaScriptEnabled = listener.checkJavaScriptStatus();
		if (checkIfJavaScriptEnabled == true)
		{
			//	if so, update the UI elements to the proper enabled/disabled state.

			javaScriptSwitch.setChecked(true);
		}
		else
		{
			// if not, update the UI elements to the proper enabled, in this case disabled state.
			javaScriptSwitch.setChecked(false);

		}
		javaScriptSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
				{
					// if the user has the switch set to enabled, toggle javascript method called with true passed as parameter
					listener.toggleJavaScript(true);
				} 
				else if (!isChecked)
				{
					// if the user has the switch set to disabled, toggle javascript method called with false passed as parameter
					listener.toggleJavaScript(false);
				}

			}     

		});
		// Clear History Button
		Button clearHistoryBtn = (Button) v.findViewById(R.id.clearHistoryBtn);
		clearHistoryBtn.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View view) {

	                // This method allows for users to clear the browser history (ie. clear any pages so they are unable to go back etc).
	                listener.clearBrowserHistory();
	                
	                AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
	        		// Setting adb properties
	        		adb.setTitle(v.getContext().getString(R.string.success_title));
	        		adb.setMessage(v.getContext().getString(R.string.history_success_message));
	        		adb.setPositiveButton(android.R.string.ok, null);
	        		// Setting alertDialog to create initialized adb with set properties
	        		AlertDialog alertDialog = adb.create();
	        		// Showing alertDialog to user
	        		alertDialog.show();

	            }
	        });

		return v;
	}

}