package com.example.hvs;

import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class AlleLigenActivity extends ActionBarActivity {

	DatabaseHelper dbh;
	List<Liga> alleLigen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alle_ligen);
		getIntent();
		dbh = DatabaseHelper.getInstance(getApplicationContext());

		// Alle Ligen bedeutet nur Ligen, die initialisiert wurden
		alleLigen = dbh.getAlleLigen();
		TableLayout table = (TableLayout) findViewById(R.id.tabelleAlleLigen);

		/*
		 * Pro Liga ein Button, der in einer Tabelle angeordnet wird Außerdem
		 * wird ein passender onClickListener hinzugefügt
		 */

		for (Liga l : alleLigen) {
			Button bt = new Button(getApplicationContext());
			TableRow row = new TableRow(getApplicationContext());
			bt.setText(l.getName() + " (" + l.getGeschlecht() + ")");
			bt.setHint("ligaNr:" + l.getLigaNr());
			bt.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Perform action on click
					Button b = (Button) v;
					int ligaNr = Integer.parseInt(b.getHint().toString().split(":")[1]);
					Intent intent = new Intent(getApplicationContext(), LigaActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("nummer", ligaNr);
					intent.putExtras(bundle);
					dbh.getLiga(ligaNr);
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
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.addLiga:
			Intent intent = new Intent(getApplicationContext(), StartActivity.class);
			intent.putExtra("add", 1);
			Toast.makeText(getApplicationContext(), "Intent intExtra: " + intent.getIntExtra("add", 1), Toast.LENGTH_SHORT).show();
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
