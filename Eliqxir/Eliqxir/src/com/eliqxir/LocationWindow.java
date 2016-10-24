package com.eliqxir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.GPSTracker;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

@SuppressLint("SimpleDateFormat")
public class LocationWindow extends SlidingMenuActivity implements
		OnClickListener {

	double latitude, longitude, latitudeCurrent, longitudeCurrent;
	String add, dayOfTheWeek, currentTime;
	TextView textForAddress, textForUseThis, textForEdit, textForSave;
	EditText editTxtForAddress, editTxtForCity, editTxtForCountry,
			editTxtForState, editTxtForPincode;
	RelativeLayout layoutForLocation, layoutForEditLocation;
	LinearLayout layoutForEditAndUseBtns;
	SharedPreferences sharedpreferences, zipValidatePref, zipCartValuePref,
			zipPreference;
	SharedPreferences.Editor zipPrefEditor, zipValidateEditor,
			zipCartValueEditor;
	Editor editor;
	String addressPref, zipcodeOld;
	Button btnForFindMe, btnClose, btnClose1;
	JSONObject jsonObj;

	@SuppressLint({ "SimpleDateFormat", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.location);

		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		textForAddress = (TextView) findViewById(R.id.textForLocation3);
		textForUseThis = (TextView) findViewById(R.id.btnForUseThis);
		textForEdit = (TextView) findViewById(R.id.btnForEdit);
		textForSave = (TextView) findViewById(R.id.btnForSave);
		editTxtForAddress = (EditText) findViewById(R.id.editTxtField1);
		editTxtForCity = (EditText) findViewById(R.id.editTxtField2);
		editTxtForCountry = (EditText) findViewById(R.id.editTxtField4);
		editTxtForState = (EditText) findViewById(R.id.editTxtField3);
		editTxtForPincode = (EditText) findViewById(R.id.editTxtField5);
		btnForFindMe = (Button) findViewById(R.id.btnForFindMe);
		btnClose = (Button) findViewById(R.id.btnclose1);
		btnClose1 = (Button) findViewById(R.id.btnclose2);
		layoutForLocation = (RelativeLayout) findViewById(R.id.layoutForLocationCurrent);
		layoutForEditLocation = (RelativeLayout) findViewById(R.id.layoutForLocationFields);
		layoutForEditAndUseBtns = (LinearLayout) findViewById(R.id.layoutForEditAndUseBtns);
		textForUseThis.setOnClickListener(this);
		textForEdit.setOnClickListener(this);
		textForSave.setOnClickListener(this);
		btnForFindMe.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		btnClose1.setOnClickListener(this);
		this.setFinishOnTouchOutside(false);
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");

		// Zip code to check search value directly from location window
		zipPreference = getSharedPreferences("zipprefs", Context.MODE_PRIVATE);
		zipPrefEditor = zipPreference.edit();

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date dt = new Date();
		dayOfTheWeek = sdf.format(dt);
		Log.e("dayOfTheWeek", dayOfTheWeek);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

		String cur_date = dateFormat.format(new Date());
		Log.v(" cur date", cur_date);
		String sp[] = cur_date.split(" ");

		currentTime = sp[1] + sp[2];
		Log.e("current time", currentTime);
		if (isOnline) {
			addressPref = SessionStore.getLocation(getBaseContext());
			zipcodeOld = Constant.zipCode;
			try {
				double[] d = getlocation();
				latitudeCurrent = d[0];
				longitudeCurrent = d[1];
				Log.e("latitudeCurrent",""+latitudeCurrent);
				Log.e("longitudeCurrent",""+longitudeCurrent);
				Constant.currentLattitue = latitudeCurrent;
				Constant.currentLongitude = longitudeCurrent;
				Geocoder geocoder = new Geocoder(LocationWindow.this,
						Locale.getDefault());
//				GeocoderHelper geo=new GeocoderHelper();
//				geo.fetchCityName(getApplicationContext(), latitudeCurrent, longitudeCurrent);
				List<Address> addresses = geocoder.getFromLocation(
						latitudeCurrent, longitudeCurrent, 1);
				if (addresses != null && !addresses.isEmpty()) {
					Address obj = addresses.get(0);
					add = obj.getAddressLine(0);
					add = add + "\n" + obj.getLocality();
					add = add + "\n" + obj.getAdminArea();
					add = add + "," + obj.getCountryCode();
					add = add + " " + obj.getPostalCode();

					// SessionStore.saveLocation(add, getBaseContext());

					textForAddress.setText(add);
				} else {
					Utils.ShowAlert(LocationWindow.this,
							"Couldn't find the location. Please try again later");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// }
		} else {
			Utils.ShowAlert(LocationWindow.this, Constant.networkDisconected);
		}
	}

	public double[] getlocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;
		for (int i = 0; i < providers.size(); i++) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		double[] gps = new double[2];

		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}

		else {
			boolean isLocationEnabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (isLocationEnabled) {

				Location location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				if (location != null) {
					gps[0] = location.getLatitude();
					gps[1] = location.getLongitude();
				}
			}
		}

		if (gps[0] == 0.0 || gps[0] == 0.0d) {
			GPSTracker gpstrack = new GPSTracker(this);
			gps[0] = gpstrack.getLatitude();
			gps[1] = gpstrack.getLongitude();
		}

		return gps;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnclose1) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editTxtForPincode.getWindowToken(), 0);
			finish();
		}
		if (v.getId() == R.id.btnclose2) {
			layoutForLocation.setVisibility(View.VISIBLE);
			layoutForEditAndUseBtns.setVisibility(View.VISIBLE);
			layoutForEditLocation.setVisibility(View.GONE);
			btnClose.setVisibility(View.VISIBLE);
			btnClose1.setVisibility(View.GONE);
		}
		if (v.getId() == R.id.btnForFindMe) {
			try {
				double[] d = getlocation();
				latitudeCurrent = d[0];
				longitudeCurrent = d[1];
				Constant.currentLattitue = latitudeCurrent;
				Constant.currentLongitude = longitudeCurrent;
				Geocoder geocoder = new Geocoder(LocationWindow.this,
						Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(
						latitudeCurrent, longitudeCurrent, 1);
				if (addresses != null && !addresses.isEmpty()) {
					Address obj = addresses.get(0);
					add = obj.getAddressLine(0);
					add = add + "\n" + obj.getLocality();
					add = add + "\n" + obj.getAdminArea();
					add = add + "," + obj.getCountryCode();
					add = add + " " + obj.getPostalCode();

					SessionStore.saveLocation(add, getBaseContext());

					String[] addressSplits = add.split("\n");
					String address1 = addressSplits[0];
					String address2 = addressSplits[1];
					String address3 = addressSplits[2];
					String[] addressSplit = address3.split(",");
					String state = addressSplit[0];
					String countryCode = addressSplit[1];
					String[] addressSpace = countryCode.split(" ");
					String countryNameCode = addressSpace[0];
					String countryNamePinCode = addressSpace[1];
					editTxtForAddress.setText(address1);
					editTxtForCity.setText(address2);
					editTxtForState.setText(state);
					editTxtForCountry.setText(countryNameCode);
					editTxtForPincode.setText(countryNamePinCode);
					// Constant.zipCode=countryNamePinCode.trim();
				} else {
					Utils.ShowAlert(LocationWindow.this,
							"Couldn't find the location. Please try again later");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (v.getId() == R.id.btnForUseThis) {

			try {
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					Geocoder geocoder = new Geocoder(LocationWindow.this,
							Locale.getDefault());
					List<Address> addresses = geocoder.getFromLocation(
							latitudeCurrent, longitudeCurrent, 1);
					if (addresses != null && !addresses.isEmpty()) {
						Address obj = addresses.get(0);
						add = obj.getAddressLine(0);
						add = add + "\n" + obj.getLocality();
						add = add + "\n" + obj.getAdminArea();
						add = add + "," + obj.getCountryCode();
						add = add + " " + obj.getPostalCode();

						SessionStore.saveLocation(add, getBaseContext());

						String[] addressSplits = add.split("\n");
						String address1 = addressSplits[0];
						String address2 = addressSplits[1];
						String address3 = addressSplits[2];
						String[] addressSplit = address3.split(",");
						String state = addressSplit[0];
						String countryCode = addressSplit[1];
						String[] addressSpace = countryCode.split(" ");
						String countryNameCode = addressSpace[0];
						String countryNamePinCode = addressSpace[1];
						editTxtForAddress.setText(address1);
						editTxtForCity.setText(address2);
						editTxtForState.setText(state);
						editTxtForCountry.setText(countryNameCode);
						editTxtForPincode.setText(countryNamePinCode);
						Constant.zipCode = countryNamePinCode.trim();
						clearCart(Constant.zipCode);
						new ZipCodeTask().execute();
					}
				} else {
					Utils.ShowAlert(LocationWindow.this,
							Constant.networkDisconected);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// }
			// finish();
		}

		if (v.getId() == R.id.btnForEdit) {

			try {
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					layoutForLocation.setVisibility(View.GONE);
					layoutForEditAndUseBtns.setVisibility(View.GONE);
					layoutForEditLocation.setVisibility(View.VISIBLE);
					btnClose.setVisibility(View.GONE);
					btnClose1.setVisibility(View.VISIBLE);
					// addressPref = sharedpreferences.getString("address",
					// null);
					String addressSession = SessionStore
							.getLocation(getBaseContext());

					if (addressSession != null) {
						String[] addressSplits = addressSession.split("\n");
						String address1 = addressSplits[0];
						String address2 = addressSplits[1];
						String address3 = addressSplits[2];
						String[] addressSplit = address3.split(",");
						String state = addressSplit[0];
						String countryCode = addressSplit[1];
						String[] addressSpace = countryCode.split(" ");
						String countryNameCode = addressSpace[0];
						String countryNamePinCode = addressSpace[1];
						editTxtForAddress.setText(address1);
						editTxtForCity.setText(address2);
						editTxtForState.setText(state);
						editTxtForCountry.setText(countryNameCode);
						editTxtForPincode.setText(countryNamePinCode);
						Constant.zipCode = countryNamePinCode.trim();
						SessionStore.saveLocation(addressSession,
								getBaseContext());
					} else {

						try {
							Geocoder geocoder = new Geocoder(
									LocationWindow.this, Locale.getDefault());
							List<Address> addresses = geocoder.getFromLocation(
									latitudeCurrent, longitudeCurrent, 1);
							if (addresses != null && !addresses.isEmpty()) {
								Address obj = addresses.get(0);
								add = obj.getAddressLine(0);
								add = add + "\n" + obj.getLocality();
								add = add + "\n" + obj.getAdminArea();
								add = add + "," + obj.getCountryCode();
								add = add + " " + obj.getPostalCode();
								Constant.zipCode = obj.getPostalCode().trim();
								SessionStore
										.saveLocation(add, getBaseContext());

								String[] addressSplits = add.split("\n");
								String address1 = addressSplits[0];
								String address2 = addressSplits[1];
								String address3 = addressSplits[2];
								String[] addressSplit = address3.split(",");
								String state = addressSplit[0];
								String countryCode = addressSplit[1];
								String[] addressSpace = countryCode.split(" ");
								String countryNameCode = addressSpace[0];
								String countryNamePinCode = addressSpace[1];
								editTxtForAddress.setText(address1);
								editTxtForCity.setText(address2);
								editTxtForState.setText(state);
								editTxtForCountry.setText(countryNameCode);
								editTxtForPincode.setText(countryNamePinCode);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					Utils.ShowAlert(LocationWindow.this,
							Constant.networkDisconected);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (v.getId() == R.id.btnForSave) {
			String address = editTxtForAddress.getText().toString();
			String city = editTxtForCity.getText().toString();
			String state = editTxtForState.getText().toString();
			String country = editTxtForCountry.getText().toString();
			String pincode = editTxtForPincode.getText().toString();
			Constant.zipCode = pincode.trim();
			if (address.length() > 0 && city.length() > 0 && state.length() > 0
					&& country.length() > 0 && pincode.length() > 0) {

				add = address;
				add = add + "\n" + city;
				add = add + "\n" + state;
				add = add + "," + country;
				add = add + " " + pincode;

				SessionStore.saveLocation(add, getBaseContext());
			} else {
				Toast.makeText(getBaseContext(), "Please enter all fields",
						Toast.LENGTH_SHORT).show();
			}

			clearCart(Constant.zipCode);
			new ZipCodeTask().execute();
		}
	}

	private void clearCart(String code) {
		if (zipcodeOld.equals(code.trim())) {
			Log.e("clearCart", "no");
		} else {
			Log.e("clearCart", "yes");
			Constant.cartArray.clear();
			DBAdapter db = new DBAdapter(LocationWindow.this);
			db.open();
			db.deleteRows();
			db.close();
		}
	}

	public class ZipCodeTask extends AsyncTask<Void, Void, Boolean> {

		ProgressDialog pdialog;
		String status = "notAvailable", error;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			pdialog = new ProgressDialog(LocationWindow.this);
			pdialog.setMessage("Loading..");
			pdialog.show();
			pdialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
			nameValuepair.add(new BasicNameValuePair("customer_zipcode",
					Constant.zipCode));
			nameValuepair.add(new BasicNameValuePair("current_day",
					dayOfTheWeek));
			nameValuepair.add(new BasicNameValuePair("current_time",
					currentTime));
			jsonObj = new ServerResponse(UrlGenerator.getSearchLocation())
					.getJSONObjectfromURL(RequestType.POST, nameValuepair);

			try {
				if (jsonObj != null) {
					try {
						File myFile = new File(Environment
								.getExternalStorageDirectory().getPath()
								+ "/searchclass.txt");
						myFile.createNewFile();
						FileOutputStream fOut = new FileOutputStream(myFile);
						OutputStreamWriter myOutWriter = new OutputStreamWriter(
								fOut);
						myOutWriter.append(jsonObj.toString());
						myOutWriter.close();
						fOut.close();

					} catch (Exception e) {
						e.printStackTrace();
					}
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// dialog.dismiss();
			try {
				Log.e("Status Value $$$$$$$$$$$$", status);
				if (status.equals("0")) {
					zipPrefEditor.putString("statusValue", status).commit();
				} else {
					zipPrefEditor.putString("statusValue", status).commit();
				}

				if (pdialog != null) {
					pdialog.dismiss();
					pdialog = null;
				}
				finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
