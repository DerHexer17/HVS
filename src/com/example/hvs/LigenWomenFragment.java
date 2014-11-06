package com.example.hvs;

import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LigenWomenFragment extends Fragment {
	DatabaseHelper dbh;
	Context mContext;
	List<Liga> alleLigenWomen;
	
	public LigenWomenFragment(Context mContext){
		this.mContext = mContext;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_liga_women, container, false);

	
        
		
		ligenAnzeigen(rootView);
        return rootView;
    }
	
	public void ligenAnzeigen(View view){
		dbh = DatabaseHelper.getInstance(mContext);

		// Alle Ligen bedeutet nur Ligen, die initialisiert wurden
		alleLigenWomen = dbh.getAlleLigen();
		
		TableLayout table = (TableLayout) view.findViewById(R.id.tabelleAlleLigenWomen);
		
		for (Liga l : alleLigenWomen) {
			if(l.getGeschlecht().contains("weiblich")){
				TextView bt = new TextView(mContext);
				TableRow row = new TableRow(mContext);
				row.setPadding(20, 20, 20, 20);
				
				bt.setText(l.getName());
				bt.setHint("ligaNr:" + l.getLigaNr());
				bt.setTextColor(Color.BLACK);
				bt.setPadding(10, 10, 10, 10);
				bt.setTextSize(20);
				//bt.setBackgroundResource(R.drawable.table_back);
				bt.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Perform action on click
						TextView b = (TextView) v;
						int ligaNr = Integer.parseInt(b.getHint().toString().split(":")[1]);
						Intent intent = new Intent(mContext, LigaTabActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("liganame", (String) b.getText());
						bundle.putInt("nummer", ligaNr);
						intent.putExtras(bundle);
						dbh.getLiga(ligaNr);
						startActivity(intent);
					}
				});
				row.addView(bt);
				table.addView(row);
			}

		}
	}
}
