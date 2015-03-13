package com.example.hvs;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;
import com.example.datahandling.XMLParser;
import com.example.internet.AsyncHttpTask;

public class StartActivity extends ActionBarActivity {

	public static int ITERATIONS;
	DatabaseHelper dbh;
	int ladestatus;
	List<Liga> ligenGlobal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		dbh = DatabaseHelper.getInstance(getApplicationContext());

		dbh.addLog("App gestartet");

		// Vorl�ufige Liste aller Ligen, die wir anbieten
		List<Liga> ligen = new ArrayList<Liga>();

		if (dbh.checkForAnyDataLoaded() && getIntent().getIntExtra("add", 0) == 0) {
			callAlleLigen(findViewById(R.id.button1));
		} else if (getIntent().getIntExtra("add", 0) == 1) {
			ligen = dbh.getAlleLigenNochNichtVorhanden();
			if (ligen.size() == 0) {
				this.finish();
			}
		} else {
			new XMLParser(getApplicationContext()).createLigen(ligen);
			/*
			Liga sac = new Liga();
			sac.setLigaNr(10000);
			sac.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10000&titel=Sachsenliga%20%20M%E4nner%20%202014/15");
			sac.setName("Sachsenliga");
			sac.setEbene("HVS");
			sac.setGeschlecht("m�nnlich");
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
			vbo.setGeschlecht("m�nnlich");
			vbo.setSaison("2014/2015");
			vbo.setPokal(0);
			vbo.setInitial("Nein");
			ligen.add(vbo);

			Liga vbw = new Liga();
			vbw.setLigaNr(10008);
			vbw.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10008&titel=Verbandsliga%20Sachsen%20M%E4nner%20Staffel%20West%202014/15");
			vbw.setName("Verbandsliga Sachsen-West");
			vbw.setEbene("HVS");
			vbw.setGeschlecht("m�nnlich");
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

			Liga bsmm = new Liga();
			bsmm.setLigaNr(10048);
			bsmm.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10048&titel=Bezirksliga%20Sachsen-Mitte%20M%E4nner%20%202014/15");
			bsmm.setName("Bezirksliga Sachsen-Mitte");
			bsmm.setEbene("Bezirk Sachsen-Mitte");
			bsmm.setGeschlecht("m�nnlich");
			bsmm.setSaison("2014/2015");
			bsmm.setPokal(0);
			bsmm.setInitial("Nein");
			ligen.add(bsmm);

			Liga bsmf = new Liga();
			bsmf.setLigaNr(10049);
			bsmf.setLink("http://www.hvs-handball.de/_stdVerband/Liga_Spiele.asp?M_lfdNr=10049&titel=Bezirksliga%20Sachsen-Mitte%20Frauen%20%202014/15");
			bsmf.setName("Bezirksliga Sachsen-Mitte");
			bsmf.setEbene("Bezirk Sachsen-Mitte");
			bsmf.setGeschlecht("weiblich");
			bsmf.setSaison("2014/2015");
			bsmf.setPokal(0);
			bsmf.setInitial("Nein");
			ligen.add(bsmf);
			*/
		}

		/*
		// Anzeige der verf�gbaren Liste in Tabelle mit CheckBox
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

		ligenGlobal = ligen;*/
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

	// Diese Methode startet die Datenabfrage und ist mit einem Button
	// verkn�pft. Vielleicht sp�ter auch ohne Button automatisierbar
	public void dataTest(View view) {

		if (isConnectedFast()) {
			initial();
		} else {
			new AlertDialog.Builder(this).setTitle("Internetverbindung langsam").setMessage("Sind sie sicher, dass sie die Datenabfrage nicht sp�ter mittels WLAN oder 3G starten wollen?").setPositiveButton(R.string.dialog_start, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					initial();
				}
			}).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			}).setIcon(android.R.drawable.ic_dialog_alert).show();
		}

	}

	private boolean isConnectedFast() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
	}

	private static boolean isConnectionFast(int type, int subType) {
		if (type == ConnectivityManager.TYPE_WIFI) {
			return true;
		} else if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false; // ~ 14-64 kbps
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false; // ~ 50-100 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true; // ~ 400-1000 kbps
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true; // ~ 600-1400 kbps
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false; // ~ 100 kbps
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true; // ~ 2-14 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true; // ~ 700-1700 kbps
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true; // ~ 1-23 Mbps
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true; // ~ 400-7000 kbps
				/*
				 * Above API level 7, make sure to set android:targetSdkVersion
				 * to appropriate level to use these
				 */
			case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
				return true; // ~ 1-2 Mbps
			case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
				return true; // ~ 5 Mbps
			case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
				return true; // ~ 10-20 Mbps
			case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
				return false; // ~25 kbps
			case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
				return true; // ~ 10+ Mbps
				// Unknown
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			default:
				return false;
			}
		} else {
			return false;
		}
	}

	// Start der n�chsten Activity mit Anzeige aller geladenen Ligen
	public void callAlleLigen(View view) {
		Intent intent = new Intent(getApplicationContext(), LigawahlActivity.class);
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

		if (getIntent().getIntExtra("add", 0) == 0) {
			for (Liga l : ligenGlobal) {
				dbh.addLiga(l);
			}
		} else {
			for (Liga l : ligenGlobal) {
				dbh.updateLiga(l);
			}
		}

		Toast.makeText(getApplicationContext(), "Anzahl ausgew�hlter Ligen: " + initialLigen.size() + " Anzahl Ligen insgesamt: " + ligenGlobal.size(), Toast.LENGTH_SHORT).show();

		TextView loading = (TextView) findViewById(R.id.textView1);
		loading.setText("Loading (0%)");
		loading.setTextSize(20);
		TextView hinweis = (TextView) findViewById(R.id.textView2);
		hinweis.setText("Bitte haben Sie einen Moment Geduld");
		Button datenabgleich = (Button) findViewById(R.id.button1);
		datenabgleich.setVisibility(View.INVISIBLE);
		Button keinDatenabgleich = (Button) findViewById(R.id.button2);
		keinDatenabgleich.setVisibility(View.INVISIBLE);

		// Screen Rotation aus gestellt. Per default wird sonst n�mlich die
		// Activity neu gestartet, springt dann also
		// direkt zur Ligen�bersicht, ohne aber die Daten schon geholt zu haben
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		// nur erste Liga,
		if (initialLigen.size() != 0) {
			ITERATIONS = initialLigen.size();
			new AsyncHttpTask(0, this, initialLigen).execute(initialLigen.get(0).getLink());
		}

	}
}
