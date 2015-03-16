package com.example.hvs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Halle;
import com.example.datahandling.Spiel;

public class SpielWeiteresFragment extends Fragment {

	int ligaNr;
	int spielNr;
	DatabaseHelper dbh;
	Spiel spiel;
	Halle halle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_spiel_weiteres, container, false);

		this.ligaNr = getActivity().getIntent().getIntExtra("liganummer", 0);
		this.spielNr = getActivity().getIntent().getIntExtra("spielnummer", 0);

		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		spiel = dbh.getGame(ligaNr, spielNr);
		halle = dbh.getHalle(spiel.getHalle());

		TextView heimteamName = (TextView) rootView.findViewById(R.id.spielWeiteresHeimteamName);
		heimteamName.setText(spiel.getTeamHeim());
		TextView gastteamName = (TextView) rootView.findViewById(R.id.spielWeiteresGastteamName);
		gastteamName.setText(spiel.getTeamGast());
		
		TextView heimteamToreImSchnitt = (TextView) rootView.findViewById(R.id.spielWeiteresHeimteamToreImSchnitt);
		heimteamToreImSchnitt.setText(durchschnittlicheHeimtore());
		
		TextView gastteamToreImSchnitt = (TextView) rootView.findViewById(R.id.spielWeiteresGastteamToreImSchnitt);
		gastteamToreImSchnitt.setText(durchschnittlicheGasttore());
		
		

		return rootView;
	}
	
	public String durchschnittlicheHeimtore(){
		double anzahlGespielt = 0;
		double anzahlHeimtore = 0;
		for (Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamHeim())) {
			
			
			if (s.getToreHeim() > 0) {
				anzahlHeimtore += s.getToreHeim();
				anzahlGespielt++;
			}

		}
		Double result = anzahlHeimtore/anzahlGespielt;

		
		return String.format("%.4g%n", result);
		
		//return Double.toString(result);
	}
	
	public String durchschnittlicheGasttore(){
		double anzahlGespielt = 0;
		double anzahlGasttore = 0;
		for (Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamGast())) {
			
			
			if (s.getToreGast() > 0) {
				anzahlGasttore += s.getToreGast();
				anzahlGespielt++;
			}

		}
		Double result = anzahlGasttore/anzahlGespielt;

		
		return String.format("%.4g%n", result);
		
		//return Double.toString(result);
	}
}
