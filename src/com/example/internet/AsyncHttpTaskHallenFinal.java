package com.example.internet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.HTMLParser;
import com.example.datahandling.Halle;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncHttpTaskHallenFinal extends
		AsyncTask<String, Void, Integer> {
	
	DatabaseHelper dbh;
	Activity activity;
	int hallenNr;
	Halle halle;
	
	public AsyncHttpTaskHallenFinal(Activity a, DatabaseHelper dbh, int i){
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
				
				HTMLParser htmlparserHallen = new HTMLParser();
				
				halle = htmlparserHallen.hallenHTMLParsing(response, hallenNr);
				
				Log.d("Hallen", halle.getName());
				
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
			 dbh.addHalle(halle);

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
