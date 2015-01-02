package com.example.hvs;

import java.util.List;

import android.R.drawable;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Liga;

public class LigenWomenFragment extends Fragment {
	DatabaseHelper dbh;

	List<Liga> alleLigenWomen;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_liga_women, container, false);

		ligenAnzeigen(rootView);
		return rootView;
	}

	public void ligenAnzeigen(View view) {
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());

		// Alle Ligen bedeutet nur Ligen, die initialisiert wurden
		alleLigenWomen = dbh.getAlleLigen();

		TableLayout table = (TableLayout) view.findViewById(R.id.tabelleAlleLigenWomen);

		for (Liga l : alleLigenWomen) {
			if (l.getGeschlecht().contains("weiblich")) {
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
	}
}
