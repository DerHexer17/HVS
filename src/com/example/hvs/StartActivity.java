package com.example.hvs;

import java.util.ArrayList;
import java.util.List;
import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;
import com.example.internet.AsyncHttpTask;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity {

	DatabaseHelper dbh;
	int ladestatus;
	List<Liga> ligenGlobal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// Wir brauchen von Beginn an unsere Datenbank
		dbh = DatabaseHelper.getInstance(getApplicationContext());

		// Erster Logeintrag. Vorerst rudimentär, aber wichtig, um zu prüfen, ob
		// wir die Daten vom HVS einmal komplett holen müssen
		dbh.addLog("App gestartet");

		// Vorläufige Liste aller Ligen, die wir anbieten
		List<Liga> ligen = new ArrayList<Liga>();

		if (dbh.getAllLogs().size() > 1 && getIntent().getIntExtra("add", 0) == 0) {
			callAlleLigen(findViewById(R.id.button1));
		} else if (dbh.getAllLogs().size() > 1 && getIntent().getIntExtra("add", 0) == 1) {
			ligen = dbh.getAlleLigenNochNichtVorhanden();
		} else {
			Liga sac = new Liga();
			sac.setLigaNr(10000);
			sac.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10000&titel=Sachsenliga%20%20M%E4nner%20%202014/15");
			sac.setName("Sachsenliga");
			sac.setEbene("HVS");
			sac.setGeschlecht("männlich");
			sac.setSaison("2014/2015");
			sac.setPokal(0);
			sac.setInitial("Nein");
			ligen.add(sac);

			Liga sacw = new Liga();
			sacw.setLigaNr(10001);
			sacw.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10001&titel=Sachsenliga%20%20Frauen%20%202014/15");
			sacw.setName("Sachsenliga");
			sacw.setEbene("HVS");
			sacw.setGeschlecht("weiblich");
			sacw.setSaison("2014/2015");
			sacw.setPokal(0);
			sacw.setInitial("Nein");
			ligen.add(sacw);

			Liga vbo = new Liga();
			vbo.setLigaNr(10007);
			vbo.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10007&titel=Verbandsliga%20Sachsen%20M%E4nner%20Staffel%20Ost%202014/15");
			vbo.setName("Verbandsliga Sachsen-Ost");
			vbo.setEbene("HVS");
			vbo.setGeschlecht("männlich");
			vbo.setSaison("2014/2015");
			vbo.setPokal(0);
			vbo.setInitial("Nein");
			ligen.add(vbo);

			Liga vbw = new Liga();
			vbw.setLigaNr(10008);
			vbw.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10008&titel=Verbandsliga%20Sachsen%20M%E4nner%20Staffel%20West%202014/15");
			vbw.setName("Verbandsliga Sachsen-West");
			vbw.setEbene("HVS");
			vbw.setGeschlecht("männlich");
			vbw.setSaison("2014/2015");
			vbw.setPokal(0);
			vbw.setInitial("Nein");
			ligen.add(vbw);

			Liga vbow = new Liga();
			vbow.setLigaNr(10009);
			vbow.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10009&titel=Verbandsliga%20Sachsen%20Frauen%20Staffel%20Ost%202014/15");
			vbow.setName("Verbandsliga Sachsen-Ost");
			vbow.setEbene("HVS");
			vbow.setGeschlecht("weiblich");
			vbow.setSaison("2014/2015");
			vbow.setPokal(0);
			vbow.setInitial("Nein");
			ligen.add(vbow);

			Liga vbww = new Liga();
			vbww.setLigaNr(10010);
			vbww.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10010&titel=Verbandsliga%20Sachsen%20Frauen%20Staffel%20West%202014/15");
			vbww.setName("Verbandsliga Sachsen-West");
			vbww.setEbene("HVS");
			vbww.setGeschlecht("weiblich");
			vbww.setSaison("2014/2015");
			vbww.setPokal(0);
			vbww.setInitial("Nein");
			ligen.add(vbww);
		}

		// Anzeige der verfügbaren Liste in Tabelle mit CheckBox
		TableLayout ligenauswahl = (TableLayout) findViewById(R.id.tableLigaAuswahl);
		for (Liga l : ligen) {
			TableRow tr = new TableRow(getApplicationContext());
			CheckBox cb = new CheckBox(getApplicationContext());
			TextView text = new TextView(getApplicationContext());
			text.setText(l.getName() + " (" + l.getGeschlecht() + ")");
			text.setTextColor(Color.BLACK);
			tr.addView(cb);
			tr.addView(text);
			ligenauswahl.addView(tr);

		}

		ligenGlobal = ligen;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public int getLadestatus() {
		return ladestatus;
	}

	public void setLadestatus(int ladestatus) {
		this.ladestatus = ladestatus;
	}

	// Diese Methode startet die Datenabfrage und ist mit einem Button
	// verknüpft. Vielleicht später auch ohne Button automatisierbar
	public void dataTest(View view) {

		if (isNetworkAvailable() == false) {
			return;
		}
		initial();
	}

	// Einfacher Check, ob das Internet zur Verfügung steht (Hilfsmethode)
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	// Start der nächsten Activity mit Anzeige aller geladenen Ligen
	public void callAlleLigen(View view) {
		Intent intent = new Intent(getApplicationContext(), AlleLigenActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	public void initial() {
		List<Liga> initialLigen = new ArrayList<Liga>();
		TableLayout ligenwahl = (TableLayout) findViewById(R.id.tableLigaAuswahl);

		ligenwahl.setVisibility(View.INVISIBLE);

		for (int i = 0; i < ligenwahl.getChildCount(); i++) {
			TableRow temptr = (TableRow) ligenwahl.getChildAt(i);
			CheckBox tempcb = (CheckBox) temptr.getChildAt(0);
			if (tempcb.isChecked()) {
				initialLigen.add(ligenGlobal.get(i));
				ligenGlobal.get(i).setInitial("Ja");
			}
		}

		if (dbh.getAllLogs().size() == 1) {
			for (Liga l : ligenGlobal) {
				dbh.addLiga(l);
			}
		} else {
			for (Liga l : ligenGlobal) {
				dbh.updateLigaInitial(l);
			}
		}

		Toast.makeText(getApplicationContext(), "Anzahl ausgewählter Ligen: " + initialLigen.size() + "Anzahl Ligen insgesamt: " + ligenGlobal.size(), Toast.LENGTH_SHORT).show();
		int iteration = 0;

		TextView loading = (TextView) findViewById(R.id.textView1);
		loading.setText("Loading (0%)");
		loading.setTextSize(20);
		TextView hinweis = (TextView) findViewById(R.id.textView2);
		hinweis.setText("Bitte haben Sie einen Moment Geduld");
		Button datenabgleich = (Button) findViewById(R.id.button1);
		datenabgleich.setVisibility(View.INVISIBLE);
		Button keinDatenabgleich = (Button) findViewById(R.id.button2);
		keinDatenabgleich.setVisibility(View.INVISIBLE);

		for (Liga l : initialLigen) {
			iteration++;
			new AsyncHttpTask(getApplicationContext(), l.getLigaNr(), false, dbh, this, iteration, initialLigen.size()).execute(l.getLink());
		}

	}
}
