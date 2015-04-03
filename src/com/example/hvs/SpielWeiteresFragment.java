package com.example.hvs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
		heimteamToreImSchnitt.setText(durchschnittlicheHeimtore(spiel.getTeamHeim()));
		
		TextView gastteamToreImSchnitt = (TextView) rootView.findViewById(R.id.spielWeiteresGastteamToreImSchnitt);
		gastteamToreImSchnitt.setText(durchschnittlicheGasttore(spiel.getTeamGast()));
		
		TextView heimteamHeimsieg = (TextView) rootView.findViewById(R.id.spielWeiteresHeimteamSieg);
		heimteamHeimsieg.setText(hoechsterHeimsieg(spiel.getTeamHeim()));
		
		TextView gastteamSieg = (TextView) rootView.findViewById(R.id.spielWeiteresGastteamSieg);
		gastteamSieg.setText(hoechsterAuswaertssieg(spiel.getTeamGast()));
		
		TextView heimteamBilanz = (TextView) rootView.findViewById(R.id.spielWeiteresHeimteamBilanz);
		heimteamBilanz.setText(heimbilanz());
		
		TextView gastteamBilanz = (TextView) rootView.findViewById(R.id.spielWeiteresGastteamBilanz);
		gastteamBilanz.setText(gastbilanz());
		
		return rootView;
	}
	
	public String durchschnittlicheHeimtore(String heimteam){
		double anzahlGespielt = 0;
		double anzahlHeimtore = 0;
		for (Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamHeim())) {
			
			
			if (s.getToreHeim() > 0 && s.getTeamHeim().equals(heimteam)) {
				anzahlHeimtore += s.getToreHeim();
				anzahlGespielt++;
				Log.d("Statistik", s.getTeamHeim()+s.getTeamGast()+s.getToreHeim()+s.getToreGast());
			}

		}
		Double result = anzahlHeimtore/anzahlGespielt;

		
		return String.format("%.4g%n", result);
				
		//return Double.toString(result);
	}
	
	public String durchschnittlicheGasttore(String gastteam){
		double anzahlGespielt = 0;
		double anzahlGasttore = 0;
		for (Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamGast())) {
			
			
			if (s.getToreGast() > 0 && s.getTeamGast().equals(gastteam)) {
				anzahlGasttore += s.getToreGast();
				anzahlGespielt++;
			}

		}
		Double result = anzahlGasttore/anzahlGespielt;

		
		return String.format("%.4g%n", result);
		
		//return Double.toString(result);
	}
	
	public String hoechsterHeimsieg(String heimteam){
		int maxDifferenz = 0;
		int differenz = 0;
		Spiel heimsieg = null;
		for(Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamHeim())){
			differenz = s.getToreHeim()-s.getToreGast();
			if(differenz > maxDifferenz && s.getTeamHeim().equals(heimteam)){
				heimsieg = s;
				maxDifferenz = differenz;
			}
		}
		
		try{
			return "- gegen "+heimsieg.getTeamGast()+" ("+heimsieg.getToreHeim()+":"+heimsieg.getToreGast()+")";
		}catch (Exception e){
			return "- Noch ohne Heimsieg";
		}
	}
	
	public String hoechsterAuswaertssieg(String gastteam){
		int maxDifferenz = 0;
		int differenz = 0;
		Spiel gastsieg = null;
		for(Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamGast())){
			differenz = s.getToreGast()-s.getToreHeim();
			if(differenz > maxDifferenz && s.getTeamGast().equals(gastteam)){
				gastsieg = s;
				maxDifferenz = differenz;
			}
		}
		
		try{
			return "- bei "+gastsieg.getTeamHeim()+" ("+gastsieg.getToreHeim()+":"+gastsieg.getToreGast()+")";
		}catch (Exception e){
			return "- Noch ohne Auswärtssieg";
		}
	}
	
	public String heimbilanz(){
		String heimbilanz = null;
		int positivpunkte = 0;
		int negativpunkte = 0;
		int positivtore = 0;
		int negativtore = 0;
		for(Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamHeim())){
			if(s.getTeamHeim().equals(spiel.getTeamHeim()) && s.getToreHeim() > 0){
				positivpunkte += s.getPunkteHeim();
				negativpunkte += s.getPunkteGast();
				positivtore += s.getToreHeim();
				negativtore += s.getToreGast();
			}
		}
		heimbilanz = positivpunkte+":"+negativpunkte+" Punkte"+"\n"+positivtore+":"+negativtore+" Tore";
		return heimbilanz;
	}
	
	public String gastbilanz(){
		String gastbilanz = null;
		int positivpunkte = 0;
		int negativpunkte = 0;
		int positivtore = 0;
		int negativtore = 0;
		for(Spiel s : dbh.getAllTeamGames(ligaNr, spiel.getTeamGast())){
			if(s.getTeamGast().equals(spiel.getTeamGast()) && s.getToreGast() > 0){
				positivpunkte += s.getPunkteGast();
				negativpunkte += s.getPunkteHeim();
				positivtore += s.getToreGast();
				negativtore += s.getToreHeim();
			}
		}
		gastbilanz = positivpunkte+":"+negativpunkte+" Punkte"+"\n"+positivtore+":"+negativtore+" Tore";
		return gastbilanz;
	}
}
