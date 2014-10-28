package com.example.internet;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.HTMLParser;
import com.example.datahandling.Spiel;
import com.example.datahandling.Spieltag;
import com.example.hvs.AlleLigenActivity;
import com.example.hvs.R;
import com.example.hvs.StartActivity;
import com.example.hvs.TempResultActivity;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
	//Context wird benötigt, kann aber nur in Activity erzeugt werden. Daher Mitgabe als Paramter
	private Context mContext;
	//Liste der gezogenen Spiele, die dann in die Datenbank eingeträgen bzw. geändert werden
	ArrayList<Spiel> spiele;
	ProgressDialog mDialog;
	//Spiele gehören zu einer bestimmten Liga, die passende Nummer brauchen wir als Variable
	int ligaNr;
	//Unterschied zwischen Initialem Daten-Download oder nur Update wird hier festgehalten
	boolean update;
	//Wir brauchen die Activity, von der die Task gestartet wurde, für Fortschrittsanzeige
	Activity activity;
	//Anzahl, wie oft diese Task noch aufgerufen wird
	double numberOfIterations;
	//An welcher Stelle an Iterationen wir gerade sind
	double iteration;
	
	DatabaseHelper dbh;
	
	public AsyncHttpTask(Context context, int ligaNr, boolean update, DatabaseHelper dbh, Activity activity, double iteration, double numberOfIterations){
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
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		//mDialog = new ProgressDialog(mContext);
        //mDialog.setMessage("Please wait...");
        //mDialog.show();
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
            if (statusCode ==  200) {

                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                response = convertInputStreamToString(inputStream);
                
                //Wenn update == false, sind wir in der initalen Phase, holen also daten komplett
                //Wenn update == true führen wir nur ein Ergebnis Update durch, brauchen also einen anderen HTML Parser
                if(update==false){
                    HTMLParser htmlparser = new HTMLParser();
	                spiele = htmlparser.initialHTMLParsing(response, ligaNr);
                }else{
                	Cursor c = dbh.getAllPlayedGames(ligaNr);
                	HTMLParser htmlparser = new HTMLParser();
                	spiele = htmlparser.updateHtmlParsing(response, ligaNr, c);
                }
                result = 1; // Successful

            }else{
                result = 0; //"Failed to fetch data!";
            }

        } catch (Exception e) {
            result=1000;
        	String TAG = "Hauptmethode";
			Log.d(TAG, e.getLocalizedMessage());
        }
        //StartActivity.setTestDataResult(result);

        return result; //"Failed to fetch data!";
    }

    @Override
    protected void onPostExecute(Integer result) {
        /* Download complete. Lets update UI */

        if(result == 1){

            //arrayAdapter = new ArrayAdapter(MyActivity.this, android.R.layout.simple_list_item_1, blogTitles);

            //listView.setAdapter(arrayAdapter);
        	
        	//Jetzt schreiben wir die Daten in die interne SQLite Datenbank. Bei initial also Neuanlage
        	//Dabei kreieren wir auch gleich die Spieltage
        	if(update==false){
	        	String TAG = "db";
	        	int z = 0;
	        	int ligaNr = spiele.get(2).getLigaNr();
	        	String saison = dbh.getLiga(ligaNr).getSaison();
	        	
	        	//Hier die Suche nach den Spieltagen
	        	Spieltag spieltag = new Spieltag();
	        	int spieltagsNr = 0;
	        	GregorianCalendar aktuell = new GregorianCalendar(1970, 1, 1);
	        	GregorianCalendar prüfung = new GregorianCalendar(2014, 9, 1);
	        	for(Spiel s : this.spiele){
	        			prüfung.set(s.getDateYear(), s.getDateMonth(), s.getDateDay());
	        			long millisec = prüfung.getTimeInMillis()-aktuell.getTimeInMillis();
	        			
	        			if(millisec > 86400000){//Ist genau die Millisekundenzahl eines Tages
	        				if(spieltagsNr>0){
	        					dbh.createSpieltag(spieltag);
	        				}
	        				spieltagsNr++;
	        				spieltag.setLigaNr(ligaNr);
	        				spieltag.setSpieltags_Nr(spieltagsNr);
	        				spieltag.setSpieltags_Name(spieltagsNr+". Spieltag");
	        				spieltag.setDatumBeginn(s.getDateYear()+"-"+s.getDateMonth()+"-"+s.getDateDay());
	        				spieltag.setDatumEnde(s.getDateYear()+"-"+s.getDateMonth()+"-"+s.getDateDay());
	        				spieltag.setSaison(saison);
	        				aktuell.set(s.getDateYear(), s.getDateMonth(), s.getDateDay()); 
	        				
	        			}else if(millisec == 86400000){
	        				spieltag.setDatumEnde(s.getDateYear()+"-"+s.getDateMonth()+"-"+s.getDateDay());
	        				aktuell.set(s.getDateYear(), s.getDateMonth(), s.getDateDay()); 
	        			}	        			
	        			
	        			s.setSpieltagsNr(spieltagsNr);
	        			dbh.createSpiel(s);
	        	}
	        	dbh.createSpieltag(spieltag);
	        	
        	}
        	
        	//Update des Ladestandes
        	TextView loadingText = (TextView) activity.findViewById(R.id.textView1);
        	double ladestatus = iteration/numberOfIterations;
        	ladestatus = ladestatus*100;
        	ladestatus = Math.round(ladestatus);
        	loadingText.setText("Loading ("+ladestatus+"%)");
        	
        	//Wenn alle Daten abgeglichen wurden gehts in die Verzweigung
        	if(iteration == numberOfIterations){
	        	//Der Button um zur Übersicht der Ligen zu gelangen wird eingeblendet
        		//Button bt = (Button) activity.findViewById(R.id.button2);
	        	//bt.setVisibility(View.VISIBLE);
	        	//bt.setText("Weiter zu allen Ligen");
	        	Intent intent = new Intent(mContext, AlleLigenActivity.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	        	mContext.startActivity(intent);
        	}
        	
        }else{
            String TAG = "PostExecute";
			Log.e(TAG, "Failed to fetch data!");
        }
    }
    
    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));

        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }

        return result;
    }
    
    /* Brauche ich JSON Parsing?
    private void parseResult(String result) {

        try{
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("posts");

            blogTitles = new String[posts.length()];

            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");

                blogTitles[i] = title;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    */
    
    
}
