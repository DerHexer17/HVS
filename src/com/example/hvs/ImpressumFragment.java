package com.example.hvs;

import com.example.datahandling.DatabaseHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImpressumFragment extends Fragment{
	
	DatabaseHelper dbh;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_impressum, container, false);
		dbh = DatabaseHelper.getInstance(getActivity().getApplicationContext());
		
		return rootView;
	}

}
