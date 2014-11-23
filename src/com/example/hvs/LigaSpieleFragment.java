package com.example.hvs;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Spiel;
import com.example.datahandling.Spieltag;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LigaSpieleFragment extends Fragment {
	
	View rootView;
	int ligaNr;
	DatabaseHelper dbh;
	int indexLetzterSpieltag;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_liga_spiele, container, false);
         
        this.ligaNr = getActivity().getIntent().getIntExtra("nummer", 0);

		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());

		// Der Spinner für die Auswahl der einzelnen Spieltage
		int positionAktuellerSpieltag = addSpieltageToSpinner(dbh.getAllSpieltageForLiga(ligaNr), dbh.getAllLeagueTeams(ligaNr), rootView);
		Spinner spinnerSpieltage = (Spinner) rootView.findViewById(R.id.spinnerSpieltage);
		spinnerSpieltage.setOnItemSelectedListener(new CustomOnItemSelectedListener(dbh, ligaNr, rootView));
		spinnerSpieltage.setSelection(positionAktuellerSpieltag+2);
		this.rootView = rootView;
        return rootView;
    }
	
	// Auflistung der ausgewählten Spiele
		public void listeSpiele(List<Spiel> spiele) {
			TableLayout table = (TableLayout) rootView.findViewById(R.id.tableAlleSpiele);
			if (table.getChildCount() > 1) {
				table.removeViews(1, table.getChildCount() - 1);
			}
			for (Spiel s : spiele) {
				TableRow row = new TableRow(getActivity().getApplicationContext());
				TextView field1 = new TextView(getActivity().getApplicationContext());
				ArrayList<TextView> formatArray = new ArrayList<TextView>();
				field1.setText(s.getDateDay() + "." + s.getDateMonth() + "." + String.valueOf(s.getDateYear()).split("0")[1]
						+ "\n(" + s.getTime() + ")");
				formatArray.add(field1);
				TextView field2 = new TextView(getActivity().getApplicationContext());
				field2.setText(s.getTeamHeim());
				formatArray.add(field2);
				TextView field3 = new TextView(getActivity().getApplicationContext());
				field3.setText(s.getTeamGast());
				formatArray.add(field3);
				TextView field4 = new TextView(getActivity().getApplicationContext());
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
				row.setContentDescription(ligaNr+";"+s.getSpielNr());
				row.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Perform action on click

						int ligaNr = Integer.parseInt(v.getContentDescription().toString().split(";")[0]);
						int spielNr = Integer.parseInt(v.getContentDescription().toString().split(";")[1]);
						Intent intent = new Intent(getActivity().getApplicationContext(), SpielActivity.class);
						Bundle bundle = new Bundle();
						//bundle.putString("liganame", (String) b.getText());
						bundle.putInt("liganummer", ligaNr);
						bundle.putInt("spielnummer", spielNr);
						intent.putExtras(bundle);

						startActivity(intent);
					}
				});
				table.addView(row);
			}
		}

		public int addSpieltageToSpinner(List<Spieltag> spieltage, List<String> teams, View v) {
			Spinner spinnerSpieltage = (Spinner) v.findViewById(R.id.spinnerSpieltage);
			List<String> spieltageText = new ArrayList<String>();
			int i=0;
			long jetzt = System.currentTimeMillis();
			long spieltag;
			long diff;

			// Eine gut lesbare Liste aller Spieltage wird erzeugt
			for (Spieltag sp : spieltage) {
				String datumBeginn = sp.getDatumBeginn().split("-")[2] + "." + sp.getDatumBeginn().split("-")[1] + "." + sp.getDatumBeginn().split("-")[0].split("0")[1];
				String datumEnde = "";
				if (Integer.parseInt(sp.getDatumBeginn().split("-")[2]) != Integer.parseInt(sp.getDatumEnde().split("-")[2])) {
					datumEnde = " - " + sp.getDatumEnde().split("-")[2] + "." + sp.getDatumEnde().split("-")[1] + "." + sp.getDatumEnde().split("-")[0].split("0")[1];
				}
				spieltageText.add(sp.getSpieltags_Name() + " (" + datumBeginn + datumEnde + ")");
				
				GregorianCalendar spieltagCalendar = new GregorianCalendar(Integer.parseInt(sp.getDatumBeginn().split("-")[0]), Integer.parseInt(sp.getDatumBeginn().split("-")[1]), Integer.parseInt(sp.getDatumBeginn().split("-")[2]));
				spieltag = spieltagCalendar.getTimeInMillis();
				diff = jetzt-spieltag;
				if(diff > 86400000){//86400000*2){
					i++;
				}
			}
			
			indexLetzterSpieltag = spieltageText.size()-1;

			// Die Liste wird um eine Auswahl je Team erweitert
			spieltageText.add("- - - - - - Teamauswahl - - - - - -");
			for (String team : teams) {
				spieltageText.add(team);
			}

			// Jetzt wird die Liste dem Adapter übergeben und mit dem Spinner
			// verknüpft
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spieltageText);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSpieltage.setAdapter(dataAdapter);
			
			return i;
		}

		public class CustomOnItemSelectedListener implements OnItemSelectedListener {

			DatabaseHelper dbh;
			int ligaNr;
			View v;


			public CustomOnItemSelectedListener(DatabaseHelper dbh, int ligaNr, View v) {
				this.dbh = dbh;
				this.ligaNr = ligaNr;
				this.v = v;
			}

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// TODO Auto-generated method stub
				LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.ligaLayoutSpieltageButtons);
				Button bt1 = (Button) rootView.findViewById(R.id.buttonVorherigerSpieltag);
				Button bt2 = (Button) rootView.findViewById(R.id.buttonNaechsterSpieltag);
				
				if (parent.getItemAtPosition(pos).toString().split("\\.").length > 1) {
					int spieltagsNr = Integer.parseInt(parent.getItemAtPosition(pos).toString().split("\\.")[0]);

					listeSpiele(dbh.getAllMatchdayGames(ligaNr, spieltagsNr));
					
					ll.setVisibility(View.VISIBLE);
				} else if (parent.getItemAtPosition(pos).toString().contains("Teamauswahl")) {
					Toast.makeText(parent.getContext(),
							 "Die Auswahl führt zu nix",
							  Toast.LENGTH_SHORT).show();
					ll.setVisibility(View.INVISIBLE);
					
				} else {
					listeSpiele(dbh.getAllTeamGames(ligaNr, parent.getItemAtPosition(pos).toString()));
					
					ll.setVisibility(View.INVISIBLE);

				}
				
				if(pos ==0){					
					bt1.setVisibility(View.INVISIBLE);
				}else if(pos == indexLetzterSpieltag){
					bt2.setVisibility(View.INVISIBLE);
				}else{	
					bt1.setVisibility(View.VISIBLE);
					bt2.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		}
}
