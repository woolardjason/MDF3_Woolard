package com.jasonwoolard.skybrowser.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
	private searchListener listener;
	public interface searchListener
	{
		public void toggleJavaScript(Boolean isEnabled);
		public Boolean checkJavaScriptStatus();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			listener = (searchListener) activity;
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

		return v;
	}

}