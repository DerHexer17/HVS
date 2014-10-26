package com.example.hvs;

import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;
import com.example.internet.AsyncHttpTask;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.ScrollingTabContainerView.TabView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AlleLigenActivity extends ActionBarActivity {

	DatabaseHelper dbh;
	List<Liga> alleLigen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alle_ligen);
		dbh = new DatabaseHelper(getApplicationContext());
		alleLigen = dbh.getAlleLigen();
		TableLayout table = (TableLayout) findViewById(R.id.tabelleAlleLigen);
		for(Liga l : alleLigen){
			Button bt = new Button(getApplicationContext());
			TableRow row = new TableRow(getApplicationContext());
			bt.setText(l.getName()+" ("+l.getGeschlecht()+")");
			bt.setHint("ligaNr:"+l.getLigaNr());
			bt.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 Button b = (Button) v;
	            	 int ligaNr = Integer.parseInt(b.getHint().toString().split(":")[1]);
	            	 Intent intent = new Intent(getApplicationContext(), LigaActivity.class);
	            	 Bundle bundle = new Bundle();
	            	 bundle.putInt("nummer", ligaNr);
	            	 intent.putExtras(bundle);
	            	 Liga l = dbh.getLiga(ligaNr);
	            	 //new AsyncHttpTask(getApplicationContext(), ligaNr, true, dbh).execute(l.getLink());
	            	 startActivity(intent);
	             }
	         });
			row.addView(bt);
			table.addView(row);
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alle_ligen, menu);
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
	
	public void callLiga(View view){
		Intent intent = new Intent(getApplicationContext(), LigaActivity.class);
		intent.putExtra("ligaID", 10007);
		startActivity(intent);
	}
}
