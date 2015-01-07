package com.example.hvs;

import java.util.ArrayList;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;
import com.example.internet.AsyncHttpTask;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class UpdateActivity extends ActionBarActivity {
	private DatabaseHelper dbh;
	private int ligaNr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		dbh = DatabaseHelper.getInstance(this);
		ligaNr = getIntent().getIntExtra("liga", 0);
		List<Liga> ligen = new ArrayList<Liga>();
		ligen.add(dbh.getLiga(ligaNr));
		if(getIntent().getIntExtra("update", 0) == 1){
			startSmallUpdate(null, ligen);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void startSmallUpdate(View view, List<Liga> ligen) {
		//List<Liga> ligen = dbh.getAlleLigen();

		new AsyncHttpTask(1, this, ligen).execute(ligen.get(0).getLink());

		Log.d("Benni", "einfaches Update gestartet");
	}

	public void startFullUpdate(View view) {
		List<Liga> ligen = dbh.getAlleLigen();

		new AsyncHttpTask(2, this, ligen).execute(ligen.get(0).getLink());

		Log.d("Benni", "volles Update gestartet");
	}
}
