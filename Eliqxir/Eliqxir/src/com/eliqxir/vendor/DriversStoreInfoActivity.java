package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DriversStoreInfoActivity extends SlidingMenuActivity implements
		OnClickListener {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();
		
	}
	ImageButton backImg, cartBtn, btnSlideMenu, btnForCall, btnForMail,
			btnForRemove;
	TextView textForHeader, textForDriverName, textFormailId, textForPhoneNo;
	String driverID,driverEmail,driverName,driverPhone,driverFName,driverLName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		setContentView(R.layout.driver_store_info);
		driverID = getIntent().getExtras().getString("driver_id");
		Log.e("driver id", driverID);
		driverEmail= getIntent().getExtras().getString("driver_email");
		driverPhone= getIntent().getExtras().getString("driver_phone");
		driverName=getIntent().getExtras().getString("driver_name");
		driverFName=getIntent().getExtras().getString("driver_fname");
		driverLName=getIntent().getExtras().getString("driver_lname");
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		btnForCall = (ImageButton) findViewById(R.id.imageBtnForCall);
		btnForMail = (ImageButton) findViewById(R.id.imageBtnForEmail);
		btnForRemove = (ImageButton) findViewById(R.id.imageBtnForRemoveDriver);
		textForDriverName = (TextView) findViewById(R.id.textForDriverStoreName);
	
		textFormailId = (TextView) findViewById(R.id.textForDriverStoreMail);
		textForPhoneNo = (TextView) findViewById(R.id.textForDriverStorePhone);
		btnSlideMenu.setVisibility(View.VISIBLE);
		backImg.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		btnSlideMenu.setOnClickListener(this);
		btnForCall.setOnClickListener(this);
		btnForMail.setOnClickListener(this);
		btnForRemove.setOnClickListener(this);
		textForHeader.setText("STORE INFO");
		textForDriverName.setText(driverFName+" "+driverLName);
	
		textFormailId.setText(driverEmail);
		textForPhoneNo.setText(driverPhone);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}

		if (v.getId() == R.id.imageBtnForCall) {
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:"+driverPhone));
			startActivity(callIntent);
		}

		if (v.getId() == R.id.imageBtnForEmail) {
//			Intent email = new Intent(Intent.ACTION_SEND);
//			email.putExtra(Intent.EXTRA_EMAIL,
//					new String[] { "test@gmail.com" });
//			// email.putExtra(Intent.EXTRA_CC, new String[]{ to});
//			// email.putExtra(Intent.EXTRA_BCC, new String[]{to});
//			email.putExtra(Intent.EXTRA_SUBJECT, "test");
//			email.putExtra(Intent.EXTRA_TEXT, "test for sending mail");
//
//			// need this to prompts email client only
//			email.setType("message/rfc822");
//
//			startActivity(Intent.createChooser(email, "Sending mail"));
			
			Intent shareIntent = new Intent(
					android.content.Intent.ACTION_SEND); // set
			// the
			// type
			shareIntent.setType("text/plain");
			shareIntent.putExtra(
					android.content.Intent.EXTRA_EMAIL,
					new String[] {driverEmail});
			// shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
			// "");

			// String shareMessage = "";

			shareIntent.putExtra(
					android.content.Intent.EXTRA_TEXT, "");
			// shareIntent.putExtra(android.content.Intent.EXTRA_STREAM,
			// screenshotUri);

			final PackageManager pm = getPackageManager();
			final List<ResolveInfo> matches = pm
					.queryIntentActivities(shareIntent, 0);
			ResolveInfo best = null;
			for (final ResolveInfo info : matches)
				if (info.activityInfo.packageName
						.endsWith(".gm")
						|| info.activityInfo.name.toLowerCase()
								.contains("gmail"))
					best = info;
			if (best != null)
				shareIntent.setClassName(
						best.activityInfo.packageName,
						best.activityInfo.name);

			startActivity(shareIntent);
		}

		if (v.getId() == R.id.imageBtnForRemoveDriver) {
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new RemoveDriver().execute();
				
			}
			else
			{
				Utils.ShowAlert(DriversStoreInfoActivity.this, Constant.networkDisconected);
			}
			

		}
	}

	public class RemoveDriver extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error;

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					Utils.ShowAlert(DriversStoreInfoActivity.this,
							"Driver removed.");
					startActivity(new Intent(DriversStoreInfoActivity.this,DriversActivity.class));
									} else {
					Utils.ShowAlert(DriversStoreInfoActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(DriversStoreInfoActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair
					.add(new BasicNameValuePair("driver_user_id",driverID));
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.vendorRemovDriver()).getJSONObjectfromURL(
					RequestType.POST, nameValuePair);
			Log.e("vendor Dashbord", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
