/*
 * Project		ResponseAble
 * 
 * Package		com.jasonwoolard.responseable.widget
 * 
 * @author		Jason Woolard
 * 
 * Date			Feb 20, 2014
 */
package com.jasonwoolard.responseable.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.jasonwoolard.responseable.R;
import com.jasonwoolard.responseable.UserSettings;

public class BACWidget extends AppWidgetProvider {

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onDeleted(android.content.Context, int[])
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Toast.makeText(context, "Stay safe, do NOT drink and drive!", Toast.LENGTH_SHORT).show();
	}

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		SharedPreferences sharedPref = context.getSharedPreferences(UserSettings.userData, 0);

		String savedBAC = sharedPref.getString("bac", "N/A");
		
		final int j = appWidgetIds.length;
		for (int i = 0; i < j; i++)
		{
			int appWidgetId = appWidgetIds[i];
			RemoteViews v = new RemoteViews(context.getPackageName(),R.layout.widget);
			if (savedBAC.equals("N/A"))
			{
				
			}
			else
			{
				if (Double.valueOf(savedBAC) >= 0.08)
				{
					v.setInt(R.id.widgetLayout, "setBackgroundColor", Color.RED);
					v.setTextViewText(R.id.textResultText, "Do NOT drive, you're drunk and past the 0.08 limit!");

				} 
				else if (Double.valueOf(savedBAC) >= 0.04 && Double.valueOf(savedBAC) <= 0.079)
				{
					v.setInt(R.id.widgetLayout, "setBackgroundColor", Color.YELLOW);
					v.setTextViewText(R.id.textResultText, "Legal to Drive, but possibly impaired, be cautious.");
				}
				else if (Double.valueOf(savedBAC) <= 0.039)
				{
					v.setInt(R.id.widgetLayout, "setBackgroundColor", Color.GREEN);
					v.setTextViewText(R.id.textResultText, "Legal to Drive, stay safe!");
				}
				v.setTextViewText(R.id.textBAC, savedBAC);
				appWidgetManager.updateAppWidget(appWidgetId, v);

			}
		}
	}

}
