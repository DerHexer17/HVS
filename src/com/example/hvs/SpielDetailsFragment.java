package com.example.hvs;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Halle;
import com.example.datahandling.Spiel;

public class SpielDetailsFragment extends Fragment {

	int ligaNr;
	int spielNr;
	DatabaseHelper dbh;
	Spiel spiel;
	Halle halle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_spiel_details, container, false);

		this.ligaNr = getActivity().getIntent().getIntExtra("liganummer", 0);
		this.spielNr = getActivity().getIntent().getIntExtra("spielnummer", 0);

		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		spiel = dbh.getGame(ligaNr, spielNr);
		halle = dbh.getHalle(spiel.getHalle());

		TextView titel = (TextView) rootView.findViewById(R.id.textTitelSpielDetail);
		TextView datum = (TextView) rootView.findViewById(R.id.spielDetailsDatum);
		TextView textHalle = (TextView) rootView.findViewById(R.id.spielDetailsHalle);
		TextView textSR = (TextView) rootView.findViewById(R.id.spielDetailsSR);

		titel.setText(spiel.getTeamHeim() + " - " + spiel.getTeamGast());
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
			datum.setText("Datum :" + formatter.format(spiel.getDate()) + " Uhr");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		textHalle.setText("Spielhalle: " + halle.getName() + " (" + halle.kompletteAdresse() + ")");
		textSR.setText("Schiedsrichter: " + spiel.getSchiedsrichter());

		if (textSR.getText().toString().split(" ")[1].equals("null")) {
			textSR.setText("Schiedsrichter: noch nicht angesetzt");
		}

		return rootView;
	}

}
