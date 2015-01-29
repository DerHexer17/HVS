package com.example.hvs;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.datahandling.DatabaseHelper;
import com.example.datahandling.Halle;
import com.example.datahandling.Spiel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

	private GoogleMap googleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		setTitle("Spielorte");
		
		// Getting LocationManager object from System Service
		// LOCATION_SERVICE
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Creating a criteria object to retrieve provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);

		List<String> providers = locationManager.getProviders(criteria, true);

		if (providers.isEmpty()) {
			new AlertDialog.Builder(this).setTitle("Standort ungenau").setMessage("Ihr Standort ist ungenau. Eine Navigation könnte unmöglich sein").setPositiveButton(R.string.dialog_start, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showMap();
				}
			}).setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//MapActivity.this.finish();
				}
			}).setIcon(android.R.drawable.ic_dialog_alert).show();
		} else {
			showMap();
		}

		//
		// // Getting Current Location
		// Location location =
		// locationManager.getLastKnownLocation(provider);
		//
		// LocationListener locationListener = new LocationListener() {
		// public void onLocationChanged(Location location) {
		// // redraw the marker when get location update.
		// Log.d("Benni", "changed");
		// drawMarker(new LatLng(location.getLatitude(),
		// location.getLongitude()), "Du", "hier befindest du dich",
		// BitmapDescriptorFactory.HUE_GREEN);
		// }
		//
		// @Override
		// public void onProviderDisabled(String arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onProviderEnabled(String arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// // TODO Auto-generated method stub
		//
		// }
		// };
		//
		// if (location != null) {
		// // PLACE THE INITIAL MARKER
		// drawMarker(new LatLng(location.getLatitude(),
		// location.getLongitude()), "Du", "hier befindest du dich",
		// BitmapDescriptorFactory.HUE_GREEN);
		// }
		// locationManager.requestLocationUpdates(provider, 20000, 0,
		// locationListener);

	}

	private void drawMarker(LatLng location, String title, String snippet, float color) {
		googleMap.addMarker(new MarkerOptions().position(location).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(color)).title(title));
	}

	public LatLng getLocationFromAddress(String strAddress) {

		Geocoder coder = new Geocoder(this);
		List<Address> address;
		LatLng p1 = null;
		try {
			address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);

			p1 = new LatLng(location.getLatitude(), location.getLongitude());

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return p1;
	}

	public void showMap() {
		int ligaNr = this.getIntent().getIntExtra("liga", 0);
		int spieltagsNr = this.getIntent().getIntExtra("spieltag", 0);

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

		// Showing status
		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();

		} else { // Google Play Services are available
			MapFragment fm = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfrag);

			googleMap = fm.getMap();

			DatabaseHelper dbh = DatabaseHelper.getInstance(this);
			List<Spiel> games = dbh.getAllMatchdayGames(ligaNr, spieltagsNr);
			LatLng startLoc = new LatLng(50.9280361, 13.456666);

			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.GERMANY);
			for (Spiel g : games) {
				Log.d("Benni", String.valueOf(g.getSpielNr()));
				Halle halle = dbh.getHalle(g.getHalle());
				LatLng location = getLocationFromAddress(halle.getStrasse() + ", " + halle.getHausnummer() + ", " + halle.getPlz() + ", " + halle.getOrt());
				if (location != null) {
					drawMarker(location, halle.getName(), formatter.format(g.getDate()) + " - " + g.getTeamHeim() + " vs " + g.getTeamGast()  , BitmapDescriptorFactory.HUE_AZURE);
				}
			}
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 7));
			googleMap.setMyLocationEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
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
}
