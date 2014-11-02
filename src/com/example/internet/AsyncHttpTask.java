package com.example.internet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.HTMLParser;
import com.example.datahandling.Spiel;
import com.example.datahandling.Spieltag;
import com.example.hvs.AlleLigenActivity;
import com.example.hvs.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
	private Context mContext;
	ArrayList<Spiel> spiele;
	ProgressDialog mDialog;
	int ligaNr;
	boolean update;
	Activity activity;
	double numberOfIterations;
	double iteration;

	DatabaseHelper dbh;

	public AsyncHttpTask(Context context, int ligaNr, boolean update, DatabaseHelper dbh, Activity activity, double iteration, double numberOfIterations) {
		mContext = context;
		this.ligaNr = ligaNr;
		this.update = update;
		this.dbh = dbh;
		this.activity = activity;
		this.iteration = iteration;
		this.numberOfIterations = numberOfIterations;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// mDialog = new ProgressDialog(mContext);
		// mDialog.setMessage("Please wait...");
		// mDialog.show();
	}

	@Override
	protected Integer doInBackground(String... params) {
		InputStream inputStream = null;

		HttpURLConnection urlConnection = null;

		int result = 0;
		String response = "Keine Daten";

		try {
			/* forming th java.net.URL object */
			URL url = new URL(params[0]);

			urlConnection = (HttpURLConnection) url.openConnection();

			/* optional request header */
			urlConnection.setRequestProperty("Content-Type", "application/json");

			/* optional request header */
			urlConnection.setRequestProperty("Accept", "application/json");

			urlConnection.setRequestProperty("Accept-Charset", "iso-8859-1");

			/* for Get request */
			urlConnection.setRequestMethod("GET");

			int statusCode = urlConnection.getResponseCode();

			/* 200 represents HTTP OK */
			if (statusCode == 200) {

				inputStream = new BufferedInputStream(urlConnection.getInputStream());

				response = convertInputStreamToString(inputStream);

				// Wenn update == false, sind wir in der initalen Phase, holen
				// also daten komplett
				// Wenn update == true führen wir nur ein Ergebnis Update durch,
				// brauchen also einen anderen HTML Parser
				if (update == false) {
					HTMLParser htmlparser = new HTMLParser();
					long startTime = System.currentTimeMillis();
					spiele = htmlparser.initialHTMLParsing(response, ligaNr);
					long diff = System.currentTimeMillis() - startTime;
					Log.d("BENNI", "Parser Exec Time: " + Long.toString(diff) + "ms");
				} else {
					/*
					 * TODO: Update noch nicht implementiert!
					 
					Cursor c = dbh.getAllPlayedGames(ligaNr);
					HTMLParser htmlparser = new HTMLParser();
					spiele = htmlparser.updateHtmlParsing(response, ligaNr, c);*/
				}
				result = 1; // Successful

			} else {
				result = 0; // "Failed to fetch data!";
			}

		} catch (Exception e) {
			result = 1000;
			String TAG = "Hauptmethode";
			Log.d(TAG, e.getLocalizedMessage());
		}
		// StartActivity.setTestDataResult(result);

		return result; // "Failed to fetch data!";
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (result == 1) {
			if (update == false) {
				int ligaNr = spiele.get(2).getLigaNr();
				String saison = dbh.getLiga(ligaNr).getSaison();
				List<Spiel> spieleOfSpieltag = new ArrayList<Spiel>();

				Spieltag spieltag = new Spieltag();
				int spieltagsNr = 0;
				GregorianCalendar aktuell = new GregorianCalendar(1970, 1, 1);
				GregorianCalendar prüfung = new GregorianCalendar(2014, 9, 1);
				long startTime = System.currentTimeMillis();
				for (Spiel s : this.spiele) {
					prüfung.set(s.getDateYear(), s.getDateMonth(), s.getDateDay());
					long millisec = prüfung.getTimeInMillis() - aktuell.getTimeInMillis();

					if (millisec > 86400000) {// Ist genau die Millisekundenzahl
												// eines Tages
						if (spieltagsNr > 0) {
							dbh.addSpieltag(spieltag);
							dbh.addSpieleForSpieltag(spieleOfSpieltag);
							spieleOfSpieltag.clear();
						}
						spieltagsNr++;
						spieltag.setLigaNr(ligaNr);
						spieltag.setSpieltags_Nr(spieltagsNr);
						spieltag.setSpieltags_Name(spieltagsNr + ". Spieltag");
						spieltag.setDatumBeginn(s.getDateYear() + "-" + s.getDateMonth() + "-" + s.getDateDay());
						spieltag.setDatumEnde(s.getDateYear() + "-" + s.getDateMonth() + "-" + s.getDateDay());
						spieltag.setSaison(saison);
						aktuell.set(s.getDateYear(), s.getDateMonth(), s.getDateDay());

					} else if (millisec == 86400000) {
						spieltag.setDatumEnde(s.getDateYear() + "-" + s.getDateMonth() + "-" + s.getDateDay());
						aktuell.set(s.getDateYear(), s.getDateMonth(), s.getDateDay());
					}

					s.setSpieltagsNr(spieltagsNr);
					spieleOfSpieltag.add(s);
				}
				dbh.addSpieltag(spieltag);
				dbh.addSpieleForSpieltag(spieleOfSpieltag);
				long diff = System.currentTimeMillis() - startTime;
				Log.d("BENNI", "DB Exec Time: " + Long.toString(diff) + "ms");
			}

			// Update des Ladestandes
			TextView loadingText = (TextView) activity.findViewById(R.id.textView1);
			double ladestatus = iteration / numberOfIterations;
			ladestatus = ladestatus * 100;
			ladestatus = Math.round(ladestatus);
			loadingText.setText("Loading (" + ladestatus + "%)");

			// Wenn alle Daten abgeglichen wurden gehts in die Verzweigung
			if (iteration == numberOfIterations) {
				// Der Button um zur Übersicht der Ligen zu gelangen wird
				// eingeblendet
				// Button bt = (Button) activity.findViewById(R.id.button2);
				// bt.setVisibility(View.VISIBLE);
				// bt.setText("Weiter zu allen Ligen");
				Intent intent = new Intent(mContext, AlleLigenActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}

		} else {
			String TAG = "PostExecute";
			Log.e(TAG, "Failed to fetch data!");
		}
	}

	private String convertInputStreamToString(InputStream inputStream) throws IOException {
		Charset charset = Charset.forName("iso-8859-1");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

		String line = "";
		String result = "";

		while ((line = bufferedReader.readLine()) != null) {
			result += line;
		}

		if (null != inputStream) {
			inputStream.close();
		}

		return result;
	}

}
