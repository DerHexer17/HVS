package helper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;
import com.example.datahandling.Spiel;
import com.example.hvs.LigaTabActivity;
import com.example.hvs.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SpieleAdapter extends BaseAdapter{

	DatabaseHelper dbh;
	Context context;
	List<Spiel> spiele;
	//modus 1 heiﬂt alle Spiele in einer Halle, modus 2 dementsprechend alle Spiele von Schiedsrichtern
	int modus;
	
	public SpieleAdapter(Context c, List<Spiel> spiele, DatabaseHelper dbh, int modus){
		this.context = c;
		this.dbh = dbh;
		this.spiele = spiele;
		this.modus = modus;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return spiele.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return spiele.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_spiele, null);
        }

		TextView t = (TextView) convertView.findViewById(R.id.listSpieleDatum);
		Spiel s = spiele.get(position);
		Liga l = dbh.getLiga(s.getLigaNr());
		String ligaBezeichnung;
		if(l.getJugend().equals("")){
			if(l.getGeschlecht().equals("m‰nnlich")){
				ligaBezeichnung = "M‰nner "+l.getName();
			}else{
				ligaBezeichnung = "Frauen "+l.getName();
			}
		}else{
			if(l.getGeschlecht().equals("m‰nnlich")){
				ligaBezeichnung = l.getJugend()+"-Jugend m‰nnlich "+l.getName();
			}else{
				ligaBezeichnung = l.getJugend()+"-Jugend weiblich "+l.getName();
			}
		}
		switch(modus){
		case 1:
			
			SimpleDateFormat sf1 = new SimpleDateFormat("HH:mm", Locale.GERMANY);
			t.setText(sf1.format(s.getDate())+" Uhr | "+s.getTeamHeim()+" - "+s.getTeamGast()+" ("+ligaBezeichnung+")");
		case 2:
			SimpleDateFormat sf2 = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.GERMANY);
			t.setText(sf2.format(s.getDate())+" Uhr | "+s.getTeamHeim()+" - "+s.getTeamGast()+" ("+ligaBezeichnung+")");
		}
		
        

        return convertView;
	}

}
