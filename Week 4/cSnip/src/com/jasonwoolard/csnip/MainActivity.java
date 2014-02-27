package com.jasonwoolard.csnip;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.jasonwoolard.csnipapp.R;
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
	private List<ParseObject> mSnippets;
	
	private class ObtainParseData extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Snippets");
			query.orderByDescending("createdAt");

			try {
				mSnippets = query.find();
			} catch (ParseException e) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
		//	MainActivity.this.progressDialog = ProgressDialog.show(MainActivity.this, "",
		//			"Loading, Please Wait...", true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			/* Putting the List of Snippets into a List View, by first creating the adapter, obtaining each Parse Object's info 
			   and then finally setting the adapter of ListView after info is set. */
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
					R.layout.list_row);
            if (mSnippets != null) {
                for (ParseObject mSnippet : mSnippets) {
                	String title = (String) mSnippet.get("title");
                    adapter.add(title);
                }
            }
			mSnippetList.setAdapter(adapter);
		//	MainActivity.this.progressDialog.dismiss();
			
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
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
		
		mSnippetList = (ListView) findViewById(R.id.listView1);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Executing Async Task to pull in parse db data
		new ObtainParseData().execute();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
