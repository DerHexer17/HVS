package com.example.hvs;

import java.util.ArrayList;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Spiel;
import com.example.datahandling.Spieltag;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LigaActivity extends ActionBarActivity {

	DatabaseHelper dbh;
	String TAG = "liga";
	public static Activity ligaActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liga);
		Intent intent = getIntent();
		TextView tv = (TextView) findViewById(R.id.textViewLiga);
		int ligaNr = intent.getIntExtra("nummer", 0);
		tv.setText("Liganummer: " + ligaNr);

		dbh = DatabaseHelper.getInstance(getApplicationContext());

		// Der Spinner für die Auswahl der einzelnen Spieltage
		addSpieltageToSpinner(dbh.getAllSpieltageForLiga(ligaNr), dbh.getAllLeagueTeams(ligaNr));
		Spinner spinnerSpieltage = (Spinner) findViewById(R.id.spinnerSpieltage);
		spinnerSpieltage.setOnItemSelectedListener(new CustomOnItemSelectedListener(dbh, ligaNr, this));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liga, menu);
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

	// Kann die weg?? Aktuell wird diese Methode im Spinner onClickListener
	// ausgeführt...
	public void listeSpiele(List<Spiel> spiele) {
		TableLayout table = (TableLayout) findViewById(R.id.tableAlleSpiele);
		for (Spiel s : spiele) {
			TableRow row = new TableRow(getApplicationContext());
			TextView field1 = new TextView(getApplicationContext());
			ArrayList<TextView> formatArray = new ArrayList<TextView>();
			field1.setText(s.getDateDay() + "." + s.getDateMonth() + "." + String.valueOf(s.getDateYear()).split("0")[1]);
			formatArray.add(field1);
			TextView field2 = new TextView(getApplicationContext());
			field2.setText(s.getTeamHeim());
			formatArray.add(field2);
			TextView field3 = new TextView(getApplicationContext());
			field3.setText(s.getTeamGast());
			formatArray.add(field3);
			TextView field4 = new TextView(getApplicationContext());
			field4.setText(s.getToreHeim() + ":" + s.getToreGast());
			formatArray.add(field4);

			for (TextView t : formatArray) {
				t.setTextColor(Color.BLACK);
				t.setPadding(5, 5, 5, 5);
				t.setGravity(Gravity.CENTER);
				row.addView(t);
			}
			row.setPadding(0, 0, 0, 10);
			row.setBackgroundResource(R.drawable.table_back);
			table.addView(row);
		}
	}

	public void addSpieltageToSpinner(List<Spieltag> spieltage, List<String> teams) {
		Spinner spinnerSpieltage = (Spinner) findViewById(R.id.spinnerSpieltage);
		List<String> spieltageText = new ArrayList<String>();

		// Eine gut lesbare Liste aller Spieltage wird erzeugt
		for (Spieltag sp : spieltage) {
			String datumBeginn = sp.getDatumBeginn().split("-")[2] + "." + sp.getDatumBeginn().split("-")[1] + "." + sp.getDatumBeginn().split("-")[0].split("0")[1];
			String datumEnde = "";
			if (Integer.parseInt(sp.getDatumBeginn().split("-")[2]) != Integer.parseInt(sp.getDatumEnde().split("-")[2])) {
				datumEnde = " - " + sp.getDatumEnde().split("-")[2] + "." + sp.getDatumEnde().split("-")[1] + "." + sp.getDatumEnde().split("-")[0].split("0")[1];
			}
			spieltageText.add(sp.getSpieltags_Name() + " (" + datumBeginn + datumEnde + ")");
		}

		// Die Liste wird um eine Auswahl je Team erweitert
		spieltageText.add("- - - - - - Teamauswahl - - - - - -");
		for (String team : teams) {
			spieltageText.add(team);
		}

		// Jetzt wird die Liste dem Adapter übergeben und mit dem Spinner
		// verknüpft
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spieltageText);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSpieltage.setAdapter(dataAdapter);
	}

	class CustomOnItemSelectedListener implements OnItemSelectedListener {

		DatabaseHelper dbh;
		int ligaNr;
		Activity a;

		public CustomOnItemSelectedListener(DatabaseHelper dbh, int ligaNr, Activity a) {
			this.dbh = dbh;
			this.ligaNr = ligaNr;
			this.a = a;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			// TODO Auto-generated method stub
			if (parent.getItemAtPosition(pos).toString().split("\\.").length > 1) {
				int spieltagsNr = Integer.parseInt(parent.getItemAtPosition(pos).toString().split("\\.")[0]);

				listeSpiele(dbh.getAllMatchdayGames(ligaNr, spieltagsNr));
			} else if (parent.getItemAtPosition(pos).toString().contains("Teamauswahl")) {
				Toast.makeText(parent.getContext(), "Die Auswahl führt zu nix", Toast.LENGTH_SHORT).show();
			} else {
				listeSpiele(dbh.getAllTeamGames(ligaNr, parent.getItemAtPosition(pos).toString()));
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}
}
