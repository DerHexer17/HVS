package com.example.hvs;

import helper.SpieleAdapter;

import com.example.datahandling.DatabaseHelper;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class SchiedsrichterActivity extends ActionBarActivity {

	private String sr;
	DatabaseHelper dbh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schiedsrichter);
		Intent intent = getIntent();
		dbh = DatabaseHelper.getInstance(getApplicationContext());
		sr = intent.getStringExtra("sr");
		this.setTitle(sr);
		TextView titel = (TextView) findViewById(R.id.titelSchiedsrichter);
		titel.setText("Alle Spiele von "+sr);
		ListView lv = (ListView) findViewById(R.id.listSR);
		lv.setAdapter(new SpieleAdapter(getApplicationContext(), dbh.getAllSpieleSchiedsrichter(sr), dbh, 2));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schiedsrichter, menu);
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
}
