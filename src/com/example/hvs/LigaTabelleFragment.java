package com.example.hvs;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Spiel;
import com.example.datahandling.TabellenplatzComparator;
import com.example.datahandling.Tabellenrang;
import com.example.hvs.LigaSpieleFragment.CustomOnItemSelectedListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class LigaTabelleFragment extends Fragment {

	int ligaNr;
	DatabaseHelper dbh;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_liga_tabelle, container, false);
         
        this.ligaNr = getActivity().getIntent().getIntExtra("nummer", 0);
		TextView tv = (TextView) rootView.findViewById(R.id.ligaNrTabelle);
		tv.setText("Liganummer: "+getActivity().getIntent().getIntExtra("nummer", 0));
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		
		SortedSet<Tabellenrang> tabellenPositionen = (SortedSet<Tabellenrang>) getTabellenPositionen();
		
		createTabelle(tabellenPositionen, rootView);
		return rootView;
    }
	
	public Set<Tabellenrang> getTabellenPositionen(){
		TabellenplatzComparator comp = new TabellenplatzComparator();
		SortedSet<Tabellenrang> tabellenPositionen = new TreeSet<Tabellenrang>(comp);
		for(String team : dbh.getAllLeagueTeams(ligaNr)){
			Tabellenrang tr = new Tabellenrang();
			tr.setTeam(team);
			int anzahlGespielt = 0;
			for(Spiel s : dbh.getAllTeamGames(ligaNr, team)){
				if(s.getToreHeim()>0){
					if(s.getTeamHeim().equals(team)){
						tr.setPunktePositiv(tr.getPunktePositiv()+s.getPunkteHeim());
						tr.setPunkteNegativ(tr.getPunkteNegativ()+s.getPunkteGast());
						tr.setTorePositiv(tr.getTorePositiv()+s.getToreHeim());
						tr.setToreNegativ(tr.getToreNegativ()+s.getToreGast());
						
					}else{
						tr.setPunktePositiv(tr.getPunktePositiv()+s.getPunkteGast());
						tr.setPunkteNegativ(tr.getPunkteNegativ()+s.getPunkteHeim());
						tr.setTorePositiv(tr.getTorePositiv()+s.getToreGast());
						tr.setToreNegativ(tr.getToreNegativ()+s.getToreHeim());
					}
					anzahlGespielt++;
				}
				
			}
			tr.setAnzahlGespielt(anzahlGespielt);
			tabellenPositionen.add(tr);
		}
		
		return tabellenPositionen;
	}
	
	public void createTabelle(SortedSet<Tabellenrang> positionen, View v){
		TableLayout table = (TableLayout) v.findViewById(R.id.tableTabelle);
		if(table.getChildCount()>1){
			table.removeViews(1, table.getChildCount()-1);
		}
		int rang = positionen.size()+1;
		for(Tabellenrang tr : positionen){
			TableRow row = new TableRow(getActivity().getApplicationContext());
			TextView field1 = new TextView(getActivity().getApplicationContext());
			ArrayList<TextView> formatArray = new ArrayList<TextView>();
			rang--;
			field1.setText(String.valueOf(rang));
			formatArray.add(field1);
			TextView field2 = new TextView(getActivity().getApplicationContext());
			field2.setText(tr.getTeam());
			formatArray.add(field2);
			TextView field3 = new TextView(getActivity().getApplicationContext());
			field3.setText(tr.getPunktePositiv()+":"+tr.getPunkteNegativ());
			formatArray.add(field3);
			TextView field4 = new TextView(getActivity().getApplicationContext());
			field4.setText(String.valueOf(tr.getTorePositiv()-tr.getToreNegativ()));
			formatArray.add(field4);
			
			for(TextView t : formatArray){
				t.setTextColor(Color.BLACK);
				t.setPadding(5, 5, 5, 5);
				t.setGravity(Gravity.CENTER);
				row.addView(t);
			}
			row.setPadding(0, 0, 0, 10);
			row.setBackgroundResource(R.drawable.table_back);
			table.addView(row, 1);
		}
	}
}
