package com.gradapp.au.homescreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gradapp.au.AsyncTasks.MyScheduleEventTasks;
import com.gradapp.au.AsyncTasks.RSVPTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.HttpConnection;
import com.gradapp.au.support.LocationMethods;
import com.gradapp.au.support.PathJSONParser;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MyScheduleEventScreen extends SlidingMenuActivity implements
		OnClickListener, LocationListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight, typefaceAlertTxt,
			typefaceAlertCancel, typefaceAlertOk;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack, btnForRsvp,
			btnForDirections, btnForYes, btnForNo, btnForGetDirections;
	TextView textForHeader, txtForeventName, txtForEventHeld, textForEventTime,
			textForEventTimeMins, textForContent, textForCallNo,
			textForQueries, textForDate;
	ImageView imageForClock;
	LinearLayout layoutForYesNoBtns;
	private GoogleMap googleMap;
	boolean statement;
	double latitudeCurrent, longitudeCurrent, eventLat, eventLong;
	RelativeLayout layForMap;
	String add;
	Dialog dialog;
	float density;
	String eventId, schoolId, date, timezoneID;
	LocationManager lm;
	DBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_event_screen);

		timezoneID = TimeZone.getDefault().getID();
		initUI();
	} 
	
	public void initUI() {
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");

		//Initialize SupportmapFragment to get the view of google map 
		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		density = getResources().getDisplayMetrics().density;
		eventId = getIntent().getExtras().getString("eventId");
		schoolId = getIntent().getExtras().getString("schoolId");
		date = getIntent().getExtras().getString("date");

		db = new DBAdapter(this);
		dialog = new Dialog(MyScheduleEventScreen.this);
		Log.i("event id is::::", "" + eventId);

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		txtForeventName = (TextView) findViewById(R.id.txtForEventName);
		txtForEventHeld = (TextView) findViewById(R.id.txtForEventHall);
		textForEventTime = (TextView) findViewById(R.id.textForEventStartTime);
		textForEventTimeMins = (TextView) findViewById(R.id.textForEventTimeMins);
		textForContent = (TextView) findViewById(R.id.textForEventContent);
		textForQueries = (TextView) findViewById(R.id.textForAnyQueries);
		textForCallNo = (TextView) findViewById(R.id.txtForEventCallNo);
		layoutForYesNoBtns = (LinearLayout) findViewById(R.id.layForYesOrNoBtns);
		btnForRsvp = (ImageButton) findViewById(R.id.btnForRsvp);
		btnForDirections = (ImageButton) findViewById(R.id.btnForDirections);
		btnForYes = (ImageButton) findViewById(R.id.btnForRsvpYes);
		btnForNo = (ImageButton) findViewById(R.id.btnForRsvpNo);
		layForMap = (RelativeLayout) findViewById(R.id.layForMap);
		btnForGetDirections = (ImageButton) findViewById(R.id.btnForGetDirections);
		imageForClock = (ImageView) findViewById(R.id.imageForEventClock);
		textForDate = (TextView) findViewById(R.id.textForDate);
		textForHeader.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForRsvp.setOnClickListener(this);
		btnForDirections.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		btnForYes.setOnClickListener(this);
		btnForNo.setOnClickListener(this);
		txtForEventHeld.setOnClickListener(this);
		btnForGetDirections.setOnClickListener(this);
		txtForeventName.setTypeface(typeFace);
		txtForEventHeld.setTypeface(typeFaceHeader);
		textForEventTime.setTypeface(typeFaceLight);
		textForEventTimeMins.setTypeface(typeFaceLight);
		textForContent.setTypeface(typeFaceLight);
		textForQueries.setTypeface(typeFaceLight);
		textForCallNo.setTypeface(typeFaceLight);
		textForHeader.setTextColor(Color.parseColor("#111111"));
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		btnForBack.setBackgroundResource(R.drawable.back_arrow_black);
		
		textForDate.setText(date);
		textForDate.setTypeface(typeFaceLight);
		/*
		 * MySchedule events are fetched from online or offline functionalities.
		 * Initial stage the data has been fetch from Online and save it in database
		 * then the offline functionality is called once the app works in offline.
		 */
		if (Utils.isOnline()) {
			//Online process
			if(dialog != null) {
				dialog.dismiss();
			}
			//Calls api
			new MyScheduleEventTasks(MyScheduleEventScreen.this,
					SessionStores.getUnivId(this), schoolId,
					SessionStores.getRoleType(this), eventId, txtForeventName,
					txtForEventHeld, textForEventTime, textForEventTimeMins,
					textForContent, eventLat, eventLong, btnForYes, btnForNo,
					btnForDirections, btnForRsvp, timezoneID).execute();
		} else {
			//Offline process
			db.open();
			Cursor c = db.getMySchedulesDesc(eventId);
			int count = c.getCount();
			if (count > 0) {
				if (c.moveToFirst()) {
					do {
						txtForeventName.setText(c.getString(2));
						txtForEventHeld.setText(c.getString(3));
						textForContent.setText(c.getString(4));
						textForEventTime.setText("Starts at " + c.getString(5));
						textForEventTimeMins.setText(c.getString(6));
						Log.i("Time starts at>>>>>>>>>",""+c.getString(5));
						if(c.getString(6).length() > 0) {
							imageForClock.setVisibility(View.VISIBLE);
						} else {
							imageForClock.setVisibility(View.GONE);
						}
						btnForDirections.setVisibility(View.GONE);
						btnForRsvp.setVisibility(View.GONE);
						txtForEventHeld.setVisibility(View.GONE);
					} while (c.moveToNext());
				}
			} else {
				//If the data is not saved in offline
				Toast("Page details are not saved in database, please connect to network");
				btnForDirections.setVisibility(View.GONE);
				btnForRsvp.setVisibility(View.GONE);
				txtForEventHeld.setVisibility(View.GONE);
				imageForClock.setVisibility(View.GONE);
			}
			c.close();
			db.close();
		}
	}

	private void initMap() {
		//Get the current location using LocationManager
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//Check GPRS or network options are available or not
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			btnForDirections.setEnabled(true);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MyScheduleEventScreen.this);
			builder.setTitle("Location Services Not Active");
			builder.setMessage("Please enable Location Services and GPS");
			//if not active it redirects to settings screen to enabled
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface,
								int i) {
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});
			Dialog alertDialog = builder.create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();
		} else {
			//get location using network options availability
			Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			// this with time travel limit
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30 * 1000,
					10, MyScheduleEventScreen.this);
			if (location != null)
				onLocationChanged(location);
			else
				Toast.makeText(getBaseContext(), "Location can't be retrieved",
						Toast.LENGTH_SHORT).show();

			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}

			if(SessionStores.getAlertOption(MyScheduleEventScreen.this) == null) {
				//Alert window shows as whether the location can be accessed from app with ok and cancel buttons
				AlertWindow();
			}
		}
	}

	//To get the routes to draw the route from start and end address.
	private class ReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				//get the URL connection to get routes
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}

	//JSON response has been fetched and set in list
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;

			try {
				if (routes.size() > 0) {
					for (int i = 0; i < routes.size(); i++) {
						//points are fetched accrding to the lat lng positions
						points = new ArrayList<LatLng>();
						polyLineOptions = new PolylineOptions();//Initialize
						List<HashMap<String, String>> path = routes.get(i);
	
						for (int j = 0; j < path.size(); j++) {
							HashMap<String, String> point = path.get(j);
	
							double lat = Double.parseDouble(point.get("lat"));
							double lng = Double.parseDouble(point.get("lng"));
							LatLng position = new LatLng(lat, lng);
	
							points.add(position);
						}
	
						//Points added in polyline accrding to the density the size has been set
						polyLineOptions.addAll(points);
						if (density == 0.75) {
							polyLineOptions.width(6);
						} else if (density == 1.0) {
							polyLineOptions.width(6);
						} else if (density == 1.5) {
							polyLineOptions.width(8);
						} else if (density == 2.0) {
							polyLineOptions.width(10);
						} else if (density == 2.5) {
							polyLineOptions.width(12);
						}
						polyLineOptions.color(Color.BLUE);
	
					}
					//PolyLineOptins are added in GoogleMap
					googleMap.addPolyline(polyLineOptions);
				} else {
					Toast.makeText(MyScheduleEventScreen.this,
							"Routes are not fetch to draw line", Toast.LENGTH_SHORT)
							.show();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnForRsvp) {
			layoutForYesNoBtns.setVisibility(View.VISIBLE);
		}

		if (v.getId() == R.id.btnForRsvpYes) {
			layoutForYesNoBtns.setVisibility(View.GONE);
			btnForYes.setBackgroundResource(R.drawable.yes_btn);
			btnForNo.setBackgroundResource(R.drawable.no_blue_btn);
			new RSVPTasks(MyScheduleEventScreen.this, eventId, "1").execute();
		}

		if (v.getId() == R.id.btnForRsvpNo) {
			layoutForYesNoBtns.setVisibility(View.GONE);
			btnForNo.setBackgroundResource(R.drawable.no_btn);
			btnForYes.setBackgroundResource(R.drawable.yes_blue_btn);
			new RSVPTasks(MyScheduleEventScreen.this, eventId, "0").execute();
		}

		//this button show the map in app
		if (v.getId() == R.id.btnForDirections) {
			btnForDirections.setEnabled(false);
			txtForEventHeld.setEnabled(false);
			if(Utils.isOnline()) {
				initMap();
			} else {
				Toast("Network connection is not available");
			}
		}
		
		//EventHall is clicked it shows the view of map location in mapview it helds
		if (v.getId() == R.id.txtForEventHall) {
			btnForDirections.setEnabled(false);
			txtForEventHeld.setEnabled(false);
			if(Utils.isOnline()) {
				initMap();
			} else {
				Toast("Network connection is not available");
			}
		}
		
		//this button show the navigation map app in device 
		if(v.getId() == R.id.btnForGetDirections) {
			if (lm != null) {
				lm.removeUpdates(MyScheduleEventScreen.this);
			}
			googleMap.clear();
			layForMap.setVisibility(View.GONE);
			btnForDirections.setEnabled(true);
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri
					.parse("google.navigation:q=" + Constant.eventLatitude
							+ "," + Constant.eventLongitude));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

		if (v.getId() == R.id.backBtn) {
			if (lm != null) {
				lm.removeUpdates(this);
			}
			if(dialog != null) {
				dialog.dismiss();
			}
			finish();
		}

		if (v.getId() == R.id.btnForAlertCancel) {
			dialog.dismiss();
			lm.removeUpdates(this);
			btnForDirections.setEnabled(true);
			layForMap.setVisibility(View.GONE);
			btnForDirections.setVisibility(View.VISIBLE);
		}

		if (v.getId() == R.id.btnForAlertOk) {
			SessionStores.saveAlertOption("Option_clicked", MyScheduleEventScreen.this);
			dialog.dismiss();
		}
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private void AlertWindow() {
		dialog = new Dialog(MyScheduleEventScreen.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		TextView textForCancel = (TextView) dialog
				.findViewById(R.id.btnForAlertCancel);
		TextView textForOk = (TextView) dialog.findViewById(R.id.btnForAlertOk);
		textForCancel.setOnClickListener(this);
		textForOk.setOnClickListener(this);
		dialog.show();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		btnForDirections.setVisibility(View.VISIBLE);
		layForMap.setVisibility(View.VISIBLE);

		latitudeCurrent = arg0.getLatitude();
		longitudeCurrent = arg0.getLongitude();

		googleMap.clear();
		Geocoder geocoder = new Geocoder(MyScheduleEventScreen.this,
				Locale.getDefault());
		try {
			//for time limit the location has been refreshed and fetches the location address
			List<Address> addresses = geocoder.getFromLocation(latitudeCurrent,
					longitudeCurrent, 1);
			if (addresses != null && !addresses.isEmpty()) {
				Address obj = addresses.get(0);
				add = obj.getAddressLine(0);
				add = add + "\n" + obj.getAdminArea();
				add = add + "\n" + obj.getCountryName();
			} else {
				Toast("Location address is not fetched");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//this shows the user current location in green color
		googleMap.addMarker(new MarkerOptions()
				.position(new LatLng(latitudeCurrent, longitudeCurrent))
				.title("Current Location")
				.snippet(add)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.marker_green_4)));
		//this shows the destination point in red color
		googleMap.addMarker(new MarkerOptions()
				.position(
						new LatLng(Constant.eventLatitude,
								Constant.eventLongitude))
				.title("Destination Location")
				.snippet("Chennai")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.marker_red_4)));
		String url = LocationMethods.getMapsApiDirectionsUrl(latitudeCurrent,
				longitudeCurrent, Constant.eventLatitude,
				Constant.eventLongitude);
		ReadTask downloadTask = new ReadTask();
		downloadTask.execute(url);

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				latitudeCurrent, longitudeCurrent), 15));

		googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (lm != null) {
			lm.removeUpdates(this);
		}
		if(dialog != null) {
			dialog.dismiss();
		}
		finish();
	}
}
