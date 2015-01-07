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
	private int update;
	private Activity activity;
	private DatabaseHelper dbh;

	// update = 0 -> initial
	// update = 1 -> nur Ergebnisse
	// update = 2 -> volles Update
	public AsyncHttpTask(int update, Activity activity, List<Liga> ligen) {
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

				HTMLParser htmlparser = new HTMLParser();

				long startTime = System.currentTimeMillis();
				if (update == 0) {
					spiele = htmlparser.startParsing(0, response, ligen.get(0).getLigaNr(), activity);
				} else if (update == 1) {
					spiele = htmlparser.startParsing(1, response, ligen.get(0).getLigaNr(), activity);
				} else if (update == 2) {
					spiele = htmlparser.startParsing(2, response, ligen.get(0).getLigaNr(), activity);
				} else {
					Log.d("Error", "unclear update mode");
				}

				long diff = System.currentTimeMillis() - startTime;
				Log.d("BENNI", "Parser Exec Time: " + Long.toString(diff) + "ms");

				neueHallen = htmlparser.getUnsavedHallenLinkListe(dbh.getAlleHallen());

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
		if (result == 1) {
			DBGateway gate = new DBGateway(activity);
			if (update == 0) {
				gate.saveGamesIntoDB(spiele);
				dbh.addUpdate(ligen.get(0).getLigaNr());
			} else if (update == 1) {
				gate.updateResults(spiele);
				dbh.updateUpdate(ligen.get(0).getLigaNr());
				activity.finish();
			} else if (update == 2) {
				gate.updateGamesInDB(spiele);
				dbh.updateUpdate(ligen.get(0).getLigaNr());
			} else {
				Log.d("Error", "unclear update mode");
			}

			for (Entry<Integer, String> e : neueHallen.entrySet()) {
				new AsyncHttpTaskHallen(activity, e.getKey()).execute(e.getValue());
			}

			ligen.remove(0);

			// Update des Ladestandes
			updateLoadingStatus();

			if (update == 0 && ligen.size() != 0) {
				new AsyncHttpTask(0, activity, ligen).execute(ligen.get(0).getLink());
			} else if (update == 1 && ligen.size() != 0) {
				new AsyncHttpTask(1, activity, ligen).execute(ligen.get(0).getLink());
			} else if (update == 2 && ligen.size() != 0) {
				new AsyncHttpTask(2, activity, ligen).execute(ligen.get(0).getLink());
			} else {
				//Intent intent = new Intent(activity.getApplicationContext(), LigawahlActivity.class);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				//activity.getApplicationContext().startActivity(intent);
			}
		} else {
			String TAG = "PostExecute";
			Log.e(TAG, "Failed to fetch data!");
		}
	}

	private void updateLoadingStatus() {
		if(update == 1){
			return;
		}
		TextView loadingText = (TextView) activity.findViewById(R.id.textView1);
		double it = StartActivity.ITERATIONS;
		double size = ligen.size();
		double ladestatus = 1 - (size / it);
		ladestatus = ladestatus * 100;
		ladestatus = Math.round(ladestatus);
		loadingText.setText("Loading (" + ladestatus + "%)");
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
