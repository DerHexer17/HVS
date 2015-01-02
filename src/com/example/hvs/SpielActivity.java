package com.example.hvs;

import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Halle;
import com.example.datahandling.Spiel;

public class SpielActivity extends ActionBarActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	DatabaseHelper dbh;
	int ligaNr;
	int spielNr;
	Spiel spiel;
	Halle halle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spiel);

		this.setTitle("Spiel Nr. " + getIntent().getIntExtra("spielnummer", 0));

		this.ligaNr = getIntent().getIntExtra("liganummer", 0);
		this.spielNr = getIntent().getIntExtra("spielnummer", 0);

		dbh = DatabaseHelper.getInstance(getApplicationContext());
		spiel = dbh.getGame(ligaNr, spielNr);
		halle = dbh.getHalle(spiel.getHalle());

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.spiel, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch (position) {
			case 0:
				return new SpielDetailsFragment();
			case 1:
				return new SpielOrtFragment();
			default:
				return PlaceholderFragment.newInstance(position + 1);
			}
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.spiel_tab1).toUpperCase(l);
			case 1:
				return getString(R.string.spiel_tab2).toUpperCase(l);

			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_spiel, container, false);
			return rootView;
		}
	}

	public void insertIntoCalendar(View v) {
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, spiel.getTeamHeim() + " - " + spiel.getTeamGast());
		intent.putExtra(Events.EVENT_LOCATION, halle.kompletteAdresse());
		// intent.putExtra(Events.DESCRIPTION, "Download Examples");

		// Setting dates
		GregorianCalendar calDate = new GregorianCalendar();
		calDate.setTime(spiel.getDate());
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis() + 7200000);

		/*
		 * // make it a full day event
		 * intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
		 * 
		 * // make it a recurring Event intent.putExtra(Events.RRULE,
		 * "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");
		 * 
		 * // Making it private and shown as busy
		 * intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
		 * intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
		 */
		startActivity(intent);
	}

	public void goToMaps(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String mapsString = "";
		String[] temp = halle.getStrasse().split(" ");
		for (int i = 0; i < temp.length; i++) {
			mapsString = mapsString + "+" + temp[i];
		}
		if (halle.getHausnummer() == 0) {
			mapsString = "geo:0,0?q=" + mapsString + "+" + "+" + halle.getOrt();
		} else {
			mapsString = "geo:0,0?q=" + mapsString + "+" + halle.getHausnummer() + "+" + halle.getOrt();
		}
		// geo:0,0?q=my+street+address
		intent.setData(Uri.parse(mapsString));
		startActivity(intent);
	}

}
