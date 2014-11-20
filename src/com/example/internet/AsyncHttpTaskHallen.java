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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.HTMLParser;
import com.example.datahandling.Spiel;
import com.example.datahandling.Spieltag;
import com.example.hvs.LigawahlActivity;
import com.example.hvs.R;

public class AsyncHttpTaskHallen extends AsyncTask<String, Void, Integer> {
	
	String hallenUrl;
	int hallenNr;
	DatabaseHelper dbh;
	Activity activity;
	
	public AsyncHttpTaskHallen(Activity a, DatabaseHelper dbh, int i){
		this.activity = a;
		this.dbh = dbh;
		this.hallenNr = i;
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
				
				/*
				 * Durch diese Frame ***** müssen wir uns noch nen Link weiterhangeln
				 * Daher eine weitere Async Klasse, der wir die unten geparste URL übergeben
				 * Dort bekommen wir dann den eigentlichen Quelltext der Halle
				 * Ergo ist dies hier nur ne (leider) nötige Krücke
				 */
				String tempResponse = response.split("<frame name=\"Anzeige\" src=\"")[1].split("\"")[0];
				tempResponse = "http://hvs-handball.de/_stdSportplatz/"+tempResponse;
				
				new AsyncHttpTaskHallenFinal(activity, dbh, hallenNr).execute(tempResponse);

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
