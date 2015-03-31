package com.example.hvs;

import helper.LigenAdapter;

import java.util.ArrayList;
import java.util.List;

import android.R.drawable;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;

public class LigenMenFragment extends Fragment {

	DatabaseHelper dbh;
	List<String> alleEbenen;
	List<Liga> alleLigenMen;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_liga_men, container, false);
		
		alleEbenen = new ArrayList<String>();
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		
		Spinner spinnerEbenenMen = (Spinner) rootView.findViewById(R.id.spinnerEbenenMen);
		createEbenenListe(rootView);
		ArrayAdapter<String> ebenenMenAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, alleEbenen);
		ebenenMenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerEbenenMen.setAdapter(ebenenMenAdapter);
		
		
		//ListView lv = (ListView) rootView.findViewById(R.id.listViewLigenMen);
		//lv.setAdapter(new LigenAdapter(getActivity().getApplicationContext(), dbh.getAlleLigenEbene("HVS")));
		spinnerEbenenMen.setOnItemSelectedListener(new OnItemSelectedListenerSpinnerLigenMen(getActivity().getApplicationContext(), dbh));
		
		//ligenAnzeigen(rootView);
		this.rootView = rootView;
		return rootView;
	}

	/*
	public void ligenAnzeigen(View view) {
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());

		// Alle Ligen bedeutet nur Ligen, die initialisiert wurden
		alleLigenMen = dbh.getAlleLigen();

		TableLayout table = (TableLayout) view.findViewById(R.id.tabelleAlleLigenMen);

		for (Liga l : alleLigenMen) {
			if (l.getGeschlecht().contains("männlich")) {
				TextView bt = new TextView(getActivity().getApplicationContext());
				TableRow row = new TableRow(getActivity().getApplicationContext());
				LayoutParams lp = new LayoutParams(-1, -2);

				row.setPadding(20, 10, 20, 10);
				row.setLayoutParams(lp);
				row.setGravity(Gravity.CENTER);

				bt.setText(l.getName());
				bt.setHint("ligaNr:" + l.getLigaNr());
				bt.setTextColor(Color.BLACK);
				bt.setPadding(10, 10, 10, 10);
				bt.setTextSize(17);
				bt.setGravity(Gravity.CENTER);
				bt.setBackgroundResource(drawable.btn_default);

				bt.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Perform action on click
						TextView b = (TextView) v;
						int ligaNr = Integer.parseInt(b.getHint().toString().split(":")[1]);
						Intent intent = new Intent(getActivity().getApplicationContext(), LigaTabActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("liganame", (String) b.getText());
						bundle.putInt("liganummer", ligaNr);
						intent.putExtras(bundle);
						
						startActivity(intent);
					}
				});
				row.addView(bt);
				table.addView(row);
			}

		}
	}*/
	
	public void createEbenenListe(View view){
		//alleEbenen.add("Favoriten");
		alleEbenen.add("HVS");
		alleEbenen.add("- Sachsen Mitte");
		alleEbenen.add("-- Kreis Dresden");
		alleEbenen.add("-- Kreis Elbe/Röder");
		alleEbenen.add("-- Kreis Pirna");
		alleEbenen.add("- Leipzig");
		alleEbenen.add("-- Kreis Leipzig");
		alleEbenen.add("-- Kreis Leipzig-Land");
		alleEbenen.add("-- Nordsachsen");
		alleEbenen.add("- Chemnitz");
		alleEbenen.add("-- Kreis Chemnitz");
		alleEbenen.add("-- Kreis Mittelsachsen");
		alleEbenen.add("-- Kreis Erzgebirge");
		alleEbenen.add("-- Kreis Zwickau");
		alleEbenen.add("-- Kreis Vogtland");
		alleEbenen.add("- Ostsachsen");
		alleEbenen.add("-- Kreis Bautzen");
		alleEbenen.add("-- Kreis Oberlausitz");
	}
	
	public class OnItemSelectedListenerSpinnerLigenMen implements OnItemSelectedListener {

		Context context;
		DatabaseHelper dbh;

		public OnItemSelectedListenerSpinnerLigenMen(Context context, DatabaseHelper dbh) {
			this.context = context;
			this.dbh = dbh;
			}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			// TODO Auto-generated method stub
			//ListView lv = (ListView) view.findViewById(R.id.listViewLigenMen);
			ListView lvv = (ListView) rootView.findViewById(R.id.listViewLigenMen);
			String ebene = null;
			if(parent.getItemAtPosition(pos).toString().split(" ").length == 1){
				ebene = parent.getItemAtPosition(pos).toString();
			}else{
				ebene = parent.getItemAtPosition(pos).toString().split(" ")[1];
			}

			lvv.setAdapter(new LigenAdapter(context, dbh.getAlleLigenEbene(ebene, "männlich")));
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}
}
