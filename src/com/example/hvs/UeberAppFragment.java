package com.example.hvs;

import java.util.ArrayList;
import java.util.List;

import com.example.datahandling.DatabaseHelper;
import com.example.hvs.LigenMenFragment.OnItemSelectedListenerSpinnerLigenMen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UeberAppFragment extends Fragment{
	
	DatabaseHelper dbh;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_ueber_app, container, false);
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		
		return rootView;
	}

}
