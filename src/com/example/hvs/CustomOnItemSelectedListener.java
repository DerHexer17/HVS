package com.example.hvs;

import java.util.ArrayList;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Spiel;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener{

	DatabaseHelper dbh;
	int ligaNr;
	Activity a;

	
	public CustomOnItemSelectedListener(DatabaseHelper dbh, int ligaNr, Activity a){
		this.dbh = dbh;
		this.ligaNr = ligaNr;
		this.a = a;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		// TODO Auto-generated method stub
		if(parent.getItemAtPosition(pos).toString().split("\\.").length>1){
			int spieltagsNr = Integer.parseInt(parent.getItemAtPosition(pos).toString().split("\\.")[0]);
			
			listeSpieleListener(dbh.getAllMatchdayGames(ligaNr, spieltagsNr), a);
		}else if(parent.getItemAtPosition(pos).toString().contains("Teamauswahl")){
			Toast.makeText(parent.getContext(), 
					"Die Auswahl führt zu nix",
					Toast.LENGTH_SHORT).show();
		}else{
			listeSpieleListener(dbh.getAllTeamGames(ligaNr, parent.getItemAtPosition(pos).toString()), a);
		}
		
		
		/*Toast.makeText(parent.getContext(), 
				"Größe der Abfrage : " + dbh.getAllMatchdayGames(ligaNr, spieltagsNr).size(),
				Toast.LENGTH_SHORT).show();
		*/
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void listeSpieleListener(List<Spiel> spiele, Activity a){
		TableLayout table = (TableLayout) a.findViewById(R.id.tableAlleSpiele);
		if(table.getChildCount()>1){
			table.removeViews(1, table.getChildCount()-1);
		}
		for(Spiel s : spiele){
			TableRow row = new TableRow(a.getApplicationContext());
			TextView field1 = new TextView(a.getApplicationContext());
			ArrayList<TextView> formatArray = new ArrayList<TextView>();
			field1.setText(s.getDateDay()+"."+s.getDateMonth()+"."+String.valueOf(s.getDateYear()).split("0")[1]);
			formatArray.add(field1);
			TextView field2 = new TextView(a.getApplicationContext());
			field2.setText(s.getTeamHeim());
			formatArray.add(field2);
			TextView field3 = new TextView(a.getApplicationContext());
			field3.setText(s.getTeamGast());
			formatArray.add(field3);
			TextView field4 = new TextView(a.getApplicationContext());
			field4.setText(s.getToreHeim()+":"+s.getToreGast());
			formatArray.add(field4);
			
			for(TextView t : formatArray){
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
	

}
