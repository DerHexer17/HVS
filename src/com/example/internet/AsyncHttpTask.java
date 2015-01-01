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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.datahandling.DBGateway;
import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.HTMLParser;
import com.example.datahandling.Liga;
import com.example.datahandling.Spiel;
import com.example.hvs.LigawahlActivity;
import com.example.hvs.R;
import com.example.hvs.StartActivity;

public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

	private ArrayList<Spiel> spiele;
	private List<Liga> ligen;
	private Map<Integer, String> neueHallen;
	private int ligaNr;
	private boolean update;
	private Activity activity;
	private DatabaseHelper dbh;

	public AsyncHttpTask(int ligaNr, boolean update, Activity activity, List<Liga> ligen) {

		this.ligaNr = ligaNr;
		this.update = update;
		this.activity = activity;
		this.ligen = ligen;
		this.dbh = DatabaseHelper.getInstance(activity);
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

	}


	@Override
	protected Integer doInBackground(String... params) {
		InputStream inputStream = null;
		HttpURLConnection urlConnection = null;

		int result = 0;
		
		String response = "Keine Daten";

		try {
			/* forming the java.net.URL object */
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

					// Hier holen wir uns nur eine Map der Hallen, die noch neu
					// hinzukommen sollen
					// Das eigentliche Parsen findet weiter unten in PostExecute
					// statt
					neueHallen = htmlparser.getUnsavedHallenLinkListe(dbh.getAlleHallen());

					long diff = System.currentTimeMillis() - startTime;
					Log.d("BENNI", "Parser Exec Time: " + Long.toString(diff) + "ms");
				} else {
					/*
					 * TODO: Update noch nicht implementiert!
					 */
				}
				result = 1; // Successful

			} else {
				result = 0; // "Failed to fetch data!";
			}

		} catch (Exception e) {
			result = 1000;
			String TAG = "Hauptmethode";
			Log.d(TAG, e.getMessage());
		}
		// StartActivity.setTestDataResult(result);

		return result; // "Failed to fetch data!";
	}

	@Override
	protected void onPostExecute(Integer result) {
		Log.d("BENNI","Gateway start");
		if (result == 1) {
			if (update == false) {
				
				DBGateway gate = new DBGateway(activity);
				gate.saveGamesIntoDB(spiele);

				for (Entry<Integer, String> e: neueHallen.entrySet()) {
					new AsyncHttpTaskHallen(activity, e.getKey()).execute(e.getValue());
					Log.d("Benni", "Iteration Async: " + e.getKey());
				}
			}

			ligen.remove(0);
			
			// Update des Ladestandes
			TextView loadingText = (TextView) activity.findViewById(R.id.textView1);
			double it = StartActivity.ITERATIONS;
			double size = ligen.size();
			double ladestatus = 1 - (size / it);
			Log.d("load", "its: "+it+" size: "+size +" status: "+ladestatus+" test: "+(size / it));
			ladestatus = ladestatus * 100;
			ladestatus = Math.round(ladestatus);
			loadingText.setText("Loading (" + ladestatus + "%)");
			
			
			if(ligen.size() != 0){
				new AsyncHttpTask(ligen.get(0).getLigaNr(), false, activity, ligen).execute(ligen.get(0).getLink());
			}else{
				Intent intent = new Intent(activity.getApplicationContext(), LigawahlActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.getApplicationContext().startActivity(intent);
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
