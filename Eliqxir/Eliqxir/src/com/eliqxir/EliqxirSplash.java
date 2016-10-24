package com.eliqxir;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.Utils;

public class EliqxirSplash extends Activity {
	Thread splashThread;
	WebView wView;
	String menuPref, cartTime, add;
	DBAdapter db;
	Date datePref, dateCurrent;
	double latitude, longitude, latitudeCurrent, longitudeCurrent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		 try {
			 PackageInfo info = getPackageManager().getPackageInfo(
			 "com.eliqxir", PackageManager.GET_SIGNATURES);
			 for (Signature signature : info.signatures) {
			 MessageDigest md = MessageDigest.getInstance("SHA");
			 md.update(signature.toByteArray());
			 Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			 }
			 } catch (NameNotFoundException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
			 } catch (NoSuchAlgorithmException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
			 }
		db = new DBAdapter(getBaseContext());
		menuPref = SessionStore.getSlidinMenu(getApplicationContext());

		Utils.timeDiff(getBaseContext(), db);

		wView = (WebView) findViewById(R.id.webView1);
		wView.loadUrl("file:///android_res/drawable/splash.gif");
		String addressSession = SessionStore.getLocation(getBaseContext());

		if (addressSession == null) {
			Log.e("addressSession is ","null");
			try {
				double[] d = getlocation();
				latitudeCurrent = d[0];
				longitudeCurrent = d[1];
				Constant.currentLattitue = latitudeCurrent;
				Constant.currentLongitude = longitudeCurrent;
				Geocoder geocoder = new Geocoder(EliqxirSplash.this,
						Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(
						latitudeCurrent, longitudeCurrent, 1);
				// List<Address> addresses =
				// geocoder.getFromLocationName("60201",
				// 1);
				if (addresses != null && !addresses.isEmpty()) {
					Address obj = addresses.get(0);
					add = obj.getAddressLine(0);
					add = add + "\n" + obj.getLocality();
					add = add + "\n" + obj.getAdminArea();
					add = add + "," + obj.getCountryCode();
					add = add + " " + obj.getPostalCode();
					 String message =
					 String.format("Latitude: %f, Longitude: %f",
					 obj.getLatitude(), obj.getLongitude());
					 Log.e("lat long",message);
					Log.e("address ", add);
					SessionStore.saveLocation(add, getBaseContext());
					Constant.zipCode = obj.getPostalCode();
				}
				else
				{
					Log.e("address is", "null");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Log.e("addressSession is ","not null");
			String[] addressSplits = addressSession.split("\n");
		
		String address3 = addressSplits[2];
		String[] addressSplit = address3.split(",");
	
		String countryCode = addressSplit[1];
		String[] addressSpace = countryCode.split(" ");
	
		String countryNamePinCode = addressSpace[1];
			Constant.zipCode=countryNamePinCode.trim();
		}
		splashThread = new Thread() {
			@Override
			public void run() {
				try {

					db.open();
					Cursor c = db.getAllCarts();
					Constant.cartArray.clear();
					if (c.moveToFirst()) {
						do {
							// String id = c.getString(0);
							String qty = c.getString(1);
							String name = c.getString(2);
							String desc = c.getString(3);
							String initialPrice = c.getString(4);
							String price = c.getString(5);
							String itemId = c.getString(6);
							String optionId = c.getString(7);
							String spinnerID = c.getString(8);
							String totalQty = c.getString(9);
							String itemName = c.getString(10);
							Log.i("name is::", "" + name);
							Log.i("desc is:::", "" + desc);
							Log.i("itemId is::", "" + itemId);
							Log.i("optionId is:::", "" + optionId);

							HashMap<String, String> hashValues = new HashMap<String, String>();
							hashValues.put("qty", qty);
							hashValues.put("size", name);
							hashValues.put("desc", desc);
							hashValues.put("initialPrice", initialPrice);
							hashValues.put("price", price);
							hashValues.put("itemId", itemId);
							hashValues.put("optionId", optionId);
							hashValues.put("sizeId", spinnerID);
							hashValues.put("totalQty", totalQty);
							hashValues.put("itemName", itemName);
							Constant.cartArray.add(hashValues);

						} while (c.moveToNext());
					}
					c.close();
					db.close();

					sleep(3000);
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					// if(menuPref == null|menuPref.equals("vendor"))
					// {

					startActivity(new Intent(EliqxirSplash.this,
							TabsFragmentActivity.class));
//					startActivity(new Intent(EliqxirSplash.this,
//							OrderPlacedActivity.class));
					finish();
					// }

					// else if(menuPref.equals("vendor")){
					// startActivity(new
					// Intent(EliqxirSplash.this,TabsFragmentActivityVendor.class));
					// finish();
					// }
					// else{
					// startActivity(new
					// Intent(EliqxirSplash.this,OrdersActivity.class));
					// finish();
					// }
				}
			}
		};
		splashThread.start();
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
		return gps;
	}
}
