package com.example.internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataHVS {

	public String getTestData() {
		// Somewhere in your code this is called
		// in a thread which is not the user interface
		// thread

		String status = "";
		try {
			URL url = new URL("http://javatechig.com/api/get_category_posts/?dev=1&slug=android");
			// http://www.hvs-handball.de/_stdVerband/liga.asp?M_lfdNr=10007
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			readStream(con.getInputStream());
			status = "Response Code" + con.getResponseCode();
			// Map headerFields = con.getHeaderFields();
			con.disconnect();
		} catch (Exception e) {
			status = "Catchblock";
			e.printStackTrace();
		}

		return status;

	}

	public void readStream(InputStream in) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
