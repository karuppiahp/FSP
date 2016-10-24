package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eliqxir.R;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.CustomTextOpensansRegular;
import com.eliqxir.utils.CustomTextOpensansSemiBold;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class StoreInfoActivity extends SlidingMenuActivity implements
		OnClickListener {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();
		
	}
	ImageButton backImg, cartBtn, btnSlideMenu, btnSettings;
	TextView textForHeader;
	CustomTextOpensansRegular textMonday, textTuesday, textWednesday,
			textThursday, textFriday, textSaturday, textSunday, textAddress1,
			textAddress2, textAddress3;
	CustomTextOpensansSemiBold textName;
	ImageView openClosedBtn, deliveriesBtn;
//	boolean openClosed = true, deliveries = true;
	String stordID, open_closed_Toggle, delivery_accept_Toggle,openClosed,deliveries;
	SharedPreferences vendorSharedpreferences,customerPreference;
	Editor customerPrefEditor;
	boolean isOnline;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		setContentView(R.layout.store_info);
		vendorSharedpreferences = this.getSharedPreferences(
				"vendorPrefs", Context.MODE_PRIVATE);
		stordID = vendorSharedpreferences.getString("store_id", "");
		/*customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		customerPrefEditor=customerPreference.edit();
		stordID = customerPreference.getString("store_id", "");*/
		Log.e("stordID", stordID);
		
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		btnSettings = (ImageButton) findViewById(R.id.settingsBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
	
		openClosedBtn = (ImageView) findViewById(R.id.imageForOpenClosed);
		deliveriesBtn = (ImageView) findViewById(R.id.imageForAcceptDelivers);

		textMonday = (CustomTextOpensansRegular) findViewById(R.id.textForMondayField);
		textTuesday = (CustomTextOpensansRegular) findViewById(R.id.textForTuesdayField);
		textWednesday = (CustomTextOpensansRegular) findViewById(R.id.textForWednesdayField);
		textThursday = (CustomTextOpensansRegular) findViewById(R.id.textForThursdayField);
		textFriday = (CustomTextOpensansRegular) findViewById(R.id.textForFridayField);
		textSaturday = (CustomTextOpensansRegular) findViewById(R.id.textForSaturdayField);
		textSunday = (CustomTextOpensansRegular) findViewById(R.id.textForSundayField);
		textName = (CustomTextOpensansSemiBold) findViewById(R.id.textforStoreName);
		textAddress1 = (CustomTextOpensansRegular) findViewById(R.id.textforStoreAddress1);
		textAddress2 = (CustomTextOpensansRegular) findViewById(R.id.textforStoreAddress2);
		textAddress3 = (CustomTextOpensansRegular) findViewById(R.id.textforStoreAddress3);

		btnSettings.setOnClickListener(this);
		btnSlideMenu.setOnClickListener(this);
		openClosedBtn.setOnClickListener(this);
		deliveriesBtn.setOnClickListener(this);
		btnSettings.setVisibility(View.GONE);
		btnSlideMenu.setVisibility(View.VISIBLE);
		backImg.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		textForHeader.setText("STORE INFO");
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new StoreInfoHrs().execute();
		}
		else
		{
			Utils.ShowAlert(StoreInfoActivity.this, Constant.networkDisconected);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}

		if (v.getId() == R.id.imageForOpenClosed) {
			/*if (openClosed == true) {
				openClosed = false;
				openClosedBtn.setImageResource(R.drawable.available_on);
			} else {
				openClosed = true;
				openClosedBtn.setImageResource(R.drawable.available_off);
			}*/
			
			//Added on 23rd April
			Log.e("Open closed Toggle Value from Response",open_closed_Toggle);
			if (open_closed_Toggle.equals("1")) {
				open_closed_Toggle ="0";
				openClosedBtn.setImageResource(R.drawable.available_off);
			} else {
				open_closed_Toggle = "1";
				openClosedBtn.setImageResource(R.drawable.available_on);
			}
			
			if (isOnline) {
				new StoreInfoUpdateHrs().execute();
			} else	{
				Utils.ShowAlert(StoreInfoActivity.this, Constant.networkDisconected);
			}
		}

		if (v.getId() == R.id.imageForAcceptDelivers) {
			/*if (deliveries == true) {
				deliveries = false;
				deliveriesBtn.setImageResource(R.drawable.available_on);
			} else {
				deliveries = true;
				deliveriesBtn.setImageResource(R.drawable.available_off);
			}*/
		
			//Added on 23rd April
			Log.e("Delivery Accept Toggle Value from Response",delivery_accept_Toggle);	
			if (delivery_accept_Toggle.equals("1")) {
				delivery_accept_Toggle = "0";
				deliveriesBtn.setImageResource(R.drawable.available_off);
			} else {
				delivery_accept_Toggle = "1";
				deliveriesBtn.setImageResource(R.drawable.available_on);
			}
			
			if (isOnline) {
				new StoreInfoUpdateHrs().execute();
			} else	{
				Utils.ShowAlert(StoreInfoActivity.this, Constant.networkDisconected);
			}
		}
	}

	public class StoreInfoHrs extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		JSONObject jsonObj;
		String name,phone,address,mon,tue,wed,thu,fri,sat,sun;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(StoreInfoActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0)  {
			// TODO Auto-generated method stub		
				try {
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair(
							"store_id",stordID));
					jsonObj = new ServerResponse(
							UrlGenerator.vendorStoreInfoHrs())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
					Log.e("Store Info Response",""+jsonObj);
					
					if (jsonObj.getString("status").toString().equals("1")) {
						 name = jsonObj.getString("store_name");
						 phone = jsonObj.getString("store_phone");
						 address = jsonObj.getString("store_address");
						 
						 //Add on 23rd April
						 open_closed_Toggle=jsonObj.getString("active");
						 delivery_accept_Toggle=jsonObj.getString("delivery");
						//
						 
						JSONObject timeObj = jsonObj
								.getJSONObject("store_locale_information");
						 mon = timeObj.getString("Monday");						
						 tue = timeObj.getString("Tuesday");						 
						 wed = timeObj.getString("Wednesday");
						 thu = timeObj.getString("Thursday");
						 fri = timeObj.getString("Friday");
						 sat = timeObj.getString("Saturday");
						 sun = timeObj.getString("Sunday");
						 
						 mon=mon.replace("*", "-");
						 tue=tue.replace("*", "-");
						 wed=wed.replace("*", "-");
						 thu=thu.replace("*", "-");
						 fri=fri.replace("*", "-");
						 sat=sat.replace("*", "-");
						 sun=sun.replace("*", "-");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				DefaultHttpClient httpClient = new DefaultHttpClient();
//				HttpContext localContext = new BasicHttpContext();
//				HttpPost httpPost = new HttpPost(
//						UrlGenerator.vendorStoreInfoHrs());
//
//				MultipartEntity entity = new MultipartEntity(
//						HttpMultipartMode.BROWSER_COMPATIBLE);
//				entity.addPart("store_id", new StringBody("1"));
//				httpPost.setEntity(entity);
//				Log.e("entity", entity + "");
//				HttpResponse response = httpClient.execute(httpPost,
//						localContext);
//				Log.v("response >>>>>>>>", response + "");
//				HttpEntity httpEntity = response.getEntity();
//				jsonResponse = EntityUtils.toString(httpEntity);

			
			return null;
		}

//		private String String str) {
//			String am = str.substring(0, str.indexOf("am") + 2).trim();
//			String pm = str.substring(str.indexOf("am") + 2).trim();
//
//			if (am.contains("10") || am.contains("11") || am.contains("12")) {
//
//			} else {
//				am = "  " + am;
//			}
//			if (pm.contains("10") || pm.contains("11") || pm.contains("12")) {
//
//			} else {
//				pm = "  " + pm;
//			}
//			return am + " - " + pm;
//		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			try {
				
					textName.setText(name);
		//			textAddress1.setText(phone);
					textAddress2.setText(address);
					textAddress3.setVisibility(View.GONE);
					textMonday.setText(mon);
					textTuesday.setText(tue);
					textWednesday.setText(wed);
					textThursday.setText(thu);
					textFriday.setText(fri);
					textSaturday.setText(sat);
					textSunday.setText(sun);

				 //Add on 23rd April
				if(open_closed_Toggle.equals("1")){
					openClosedBtn.setImageResource(R.drawable.available_on);
				}else{
					openClosedBtn.setImageResource(R.drawable.available_off);
				}
				
				if(delivery_accept_Toggle.equals("1")){
					deliveriesBtn.setImageResource(R.drawable.available_on);
				}else{
					deliveriesBtn.setImageResource(R.drawable.available_off);
				}

				/////
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Added on 23rd April
	public class StoreInfoUpdateHrs extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		JSONObject jsonObjUpdate;		

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(StoreInfoActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0)  {
			// TODO Auto-generated method stub		
				try {
					Log.e("Store_ID"+"="+stordID,"OpenClosedToggle"+"="+open_closed_Toggle+"  "+"DeliveryAccepttoggle"+"="+delivery_accept_Toggle);
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair("store_id",stordID));
					nameValuepair.add(new BasicNameValuePair("active",open_closed_Toggle));
					nameValuepair.add(new BasicNameValuePair("delivery",delivery_accept_Toggle));
					jsonObjUpdate = new ServerResponse(
							UrlGenerator.vendorStoreInfoUpdateHrs())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
					Log.e("Store Info Update Response",""+jsonObjUpdate);					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();			
		}
	}
	
}
