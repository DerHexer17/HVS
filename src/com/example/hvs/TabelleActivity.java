package com.example.hvs;

import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Spiel;
import com.example.datahandling.Tabellenrang;
import com.example.datahandling.TabellenplatzComparator;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TabelleActivity extends ActionBarActivity {

	int ligaNr;
	DatabaseHelper dbh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabelle);
		this.ligaNr = getIntent().getIntExtra("ligaNr", 0);
		TextView tv = (TextView) findViewById(R.id.ligaNrTabelle);
		tv.setText("Liganummer: "+getIntent().getIntExtra("ligaNr", 0));
		dbh = DatabaseHelper.getInstance(getApplicationContext());
		
		SortedSet<Tabellenrang> tabellenPositionen = (SortedSet<Tabellenrang>) getTabellenPositionen();
		
		createTabelle(tabellenPositionen);
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tabelle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	
	public void createTabelle(SortedSet<Tabellenrang> positionen){
		TableLayout table = (TableLayout) findViewById(R.id.tableTabelle);
		if(table.getChildCount()>1){
			table.removeViews(1, table.getChildCount()-1);
		}
		int rang = positionen.size()+1;
		for(Tabellenrang tr : positionen){
			TableRow row = new TableRow(getApplicationContext());
			TextView field1 = new TextView(getApplicationContext());
			ArrayList<TextView> formatArray = new ArrayList<TextView>();
			rang--;
			field1.setText(String.valueOf(rang));
			formatArray.add(field1);
			TextView field2 = new TextView(getApplicationContext());
			field2.setText(tr.getTeam());
			formatArray.add(field2);
			TextView field3 = new TextView(getApplicationContext());
			field3.setText(tr.getPunktePositiv()+":"+tr.getPunkteNegativ());
			formatArray.add(field3);
			TextView field4 = new TextView(getApplicationContext());
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
