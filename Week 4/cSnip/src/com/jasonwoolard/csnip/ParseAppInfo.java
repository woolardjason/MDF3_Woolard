package com.jasonwoolard.csnip;

import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseAppInfo extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Init code for connecting to Parse Back-end DB
		Parse.initialize(this, "mOGP6ok6FPb6wXFR3T23kBZ3OZHpWHbnwC8jYUOL", "Xuey8ugcmerjtN2rBjkNeyebT7D0cva5LxnsTvsY");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
