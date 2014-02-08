package com.jasonwoolard.skybrowser.fragments;

import com.jasonwoolard.skybrowser.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


@SuppressLint("NewApi")
public class SearchGoogleFragment extends DialogFragment {
	String searchString = "";
    public static SearchGoogleFragment newInstance() {
        return new SearchGoogleFragment();
    }
	private searchListener listener;
	public interface searchListener
	{
		public void searchGoogle(String searchString);
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
    	// Setting window to have no title, since I made my own 'header' in XML
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // Defining layout to inflate, in this case: search_google_fragment
        final View v = inflater.inflate(R.layout.search_google_fragment, container, false);
        // Setting searchButton to the id in resources
        Button searchButton = (Button) v.findViewById(R.id.searchButton);
        
        // OnClickListener for the searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            	// Defining the searchField in the XML layout 'search_google_fragment'
                EditText searchField = (EditText) v.findViewById(R.id.searchField);
                // Setting inputedString to the current text in searchField
                String inputedString = searchField.getText().toString();

                // Calling the *searchGoogle* method with passed in text from the EditText
                // This method allows for users to Short-hand type in a phrase to search google, rather then go to google and then type it in and then hit search.
                listener.searchGoogle("https://www.google.com/?q=" + inputedString + "#q=" + inputedString);
               
                // Utilizing public method dismiss(); to dismiss the search fragment dialog.
                dismiss();
            }
        });
        return v;
    }
}
