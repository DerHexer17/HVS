package com.example.hvs;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Halle;
import com.example.datahandling.Spiel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SpielOrtFragment extends Fragment {

	int ligaNr;
	int spielNr;
	DatabaseHelper dbh;
	Spiel spiel;
	Halle halle;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_spiel_ort, container, false);
         
        this.ligaNr = getActivity().getIntent().getIntExtra("liganummer", 0);
        this.spielNr = getActivity().getIntent().getIntExtra("spielnummer", 0);
        
        dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
        spiel = dbh.getGame(ligaNr, spielNr);
        halle = dbh.getHalle(spiel.getHalle());
        
        TextView titel = (TextView) rootView.findViewById(R.id.spielOrtTitel);
        
        titel.setText(halle.getName());



		return rootView;
    }
}
