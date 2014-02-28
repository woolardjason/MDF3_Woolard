package com.jasonwoolard.csnip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.jasonwoolard.csnipapp.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MainActivity extends Activity {
	ListView mSnippetList;
	List<Map<String, String>> mData;
	SimpleAdapter mAdapter;
	ProgressDialog mProgress;
	ParseUser mUser;

	private class ObtainParseData extends AsyncTask<Void, Integer, Void> {
		protected Void doInBackground(Void... params) {
			for (int i = 0; i < 20; i++)
			{
				publishProgress(5);
				try {
					Thread.sleep(45);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Snippets");
			query.orderByDescending("createdAt");
			query.whereEqualTo("postedBy", mUser);
			query.findInBackground(new FindCallback<ParseObject>() 
			{
				@Override
				public void done(List<ParseObject> objects, ParseException e) 
				{
					if (e == null) 
					{
						if (objects.toArray().length > 0)
						{
							mData = new ArrayList<Map<String, String>>();
						    
							for(int i=0;i<objects.toArray().length;i++){							
								ParseObject snippets = objects.get(i);
								Map<String, String> map = new HashMap<String, String>(2);
								map.put("title", snippets.getString("title"));
								map.put("code", snippets.getString("code"));
								map.put("language", snippets.getString("language"));
								map.put("oid", snippets.getObjectId());
								mData.add(map);
								
							}
							/* Putting the List of Snippets into a List View, by first creating the adapter, obtaining each Parse Object's info 
							   and then finally setting the adapter of ListView after info is set. */
							SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), mData, android.R.layout.simple_list_item_2, 
													new String[] {"title", "language", "code", "oid"},
													new int[] {android.R.id.text1, android.R.id.text2});
							
							mSnippetList.setAdapter(adapter);
						}
					}
					else
					{
						
					}
				}
			});
			return null;
		}

		@Override
		protected void onPreExecute() {
			mProgress = new ProgressDialog(MainActivity.this);
			mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgress.setMax(100);
			mProgress.setTitle("Loading...");
			mProgress.setMessage("Please wait while we fetch your latest saved snippets!");
			mProgress.show(); 
			//	MainActivity.this.progressDialog = ProgressDialog.show(MainActivity.this, "",
			//			"Loading, Please Wait...", true);
			super.onPreExecute();

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgress.incrementProgressBy(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {

			mProgress.dismiss();


			
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Parse.initialize(this, "mOGP6ok6FPb6wXFR3T23kBZ3OZHpWHbnwC8jYUOL", "Xuey8ugcmerjtN2rBjkNeyebT7D0cva5LxnsTvsY");

		ParseAnalytics.trackAppOpened(getIntent());
		ParseUser.enableAutomaticUser();
		mUser = ParseUser.getCurrentUser();
		
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);

		mSnippetList = (ListView) findViewById(R.id.listView1);
		mSnippetList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> hm = (HashMap<String, String>)mSnippetList.getItemAtPosition(pos);
				
				Intent intent = new Intent(getApplicationContext(), SnippetDetailActivity.class);
				
				intent.putExtra("title", hm.get("title"));
				intent.putExtra("code", hm.get("code"));
				intent.putExtra("language", hm.get("language"));
				
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Executing Async Task to pull in parse db data
		new ObtainParseData().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) 
		{
		case R.id.action_add_new_snippet:
			Intent intent = new Intent(this, AddSnippetActivity.class);

			startActivity(intent);

			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
}
