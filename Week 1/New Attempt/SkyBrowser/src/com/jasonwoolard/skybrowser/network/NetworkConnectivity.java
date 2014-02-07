package com.jasonwoolard.skybrowser.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkConnectivity {
	static Boolean isConnected = false;
	static String mTAG = "NetworkConnectivity Class";
	
	public static Boolean pullConnectionStatus(Context context)
	{
		isConnected = false;
		verifyConnectionStatus(context);
		return isConnected;
	}
	public static void verifyConnectionStatus(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if (networkInfo != null)
		{
			if (networkInfo.isConnected())
			{
				Log.i(mTAG, "Connection Type: " +networkInfo.getTypeName());
				
				isConnected = true;
			}
		}
	}
}
