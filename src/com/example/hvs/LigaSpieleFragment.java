package com.example.hvs;

import java.util.ArrayList;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Spiel;
import com.example.datahandling.Spieltag;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_liga_spiele, container, false);
         
        this.ligaNr = getActivity().getIntent().getIntExtra("nummer", 0);
		Intent intent = getActivity().getIntent();
		TextView tv = (TextView) rootView.findViewById(R.id.textViewLiga);
		int ligaNr = intent.getIntExtra("nummer", 0);
		tv.setText("Liganummer: " + ligaNr);

		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());

		// Der Spinner f�r die Auswahl der einzelnen Spieltage
		addSpieltageToSpinner(dbh.getAllSpieltageForLiga(ligaNr), dbh.getAllLeagueTeams(ligaNr), rootView);
		Spinner spinnerSpieltage = (Spinner) rootView.findViewById(R.id.spinnerSpieltage);
		spinnerSpieltage.setOnItemSelectedListener(new CustomOnItemSelectedListener(dbh, ligaNr, rootView));
		this.rootView = rootView;
        return rootView;
    }
	
	// Auflistung der ausgew�hlten Spiele
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
				table.addView(row);
			}
		}

		public void addSpieltageToSpinner(List<Spieltag> spieltage, List<String> teams, View v) {
			Spinner spinnerSpieltage = (Spinner) v.findViewById(R.id.spinnerSpieltage);
			List<String> spieltageText = new ArrayList<String>();

			// Eine gut lesbare Liste aller Spieltage wird erzeugt
			for (Spieltag sp : spieltage) {
				String datumBeginn = sp.getDatumBeginn().split("-")[2] + "." + sp.getDatumBeginn().split("-")[1] + "." + sp.getDatumBeginn().split("-")[0].split("0")[1];
				String datumEnde = "";
				if (Integer.parseInt(sp.getDatumBeginn().split("-")[2]) != Integer.parseInt(sp.getDatumEnde().split("-")[2])) {
					datumEnde = " - " + sp.getDatumEnde().split("-")[2] + "." + sp.getDatumEnde().split("-")[1] + "." + sp.getDatumEnde().split("-")[0].split("0")[1];
				}
				spieltageText.add(sp.getSpieltags_Name() + " (" + datumBeginn + datumEnde + ")");
			}

			// Die Liste wird um eine Auswahl je Team erweitert
			spieltageText.add("- - - - - - Teamauswahl - - - - - -");
			for (String team : teams) {
				spieltageText.add(team);
			}

			// Jetzt wird die Liste dem Adapter �bergeben und mit dem Spinner
			// verkn�pft
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spieltageText);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerSpieltage.setAdapter(dataAdapter);
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
				if (parent.getItemAtPosition(pos).toString().split("\\.").length > 1) {
					int spieltagsNr = Integer.parseInt(parent.getItemAtPosition(pos).toString().split("\\.")[0]);

					listeSpiele(dbh.getAllMatchdayGames(ligaNr, spieltagsNr));
				} else if (parent.getItemAtPosition(pos).toString().contains("Teamauswahl")) {
					Toast.makeText(parent.getContext(),
							 "Die Auswahl f�hrt zu nix",
							  Toast.LENGTH_SHORT).show();
				} else {
					listeSpiele(dbh.getAllTeamGames(ligaNr, parent.getItemAtPosition(pos).toString()));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		}
}
