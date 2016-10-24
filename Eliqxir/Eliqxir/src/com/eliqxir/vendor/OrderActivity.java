package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class OrderActivity extends SlidingMenuActivity implements
		OnClickListener {
	public void onStop()
	{
//		if(Constant.isVendorAvailable.equals("notAvailable"))
//		{
//			finish();
//		}
		super.onStop();		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}

		return false;
	}

	
	TextView textForHistory, textForPending, textForProcessing, textForHeader;
	Typeface fontSemiBold,fontRegular;
	ListView listOfOrders;
	ImageButton backImg, btnSlideMenu, btnSettings, btnCart;
	String type,storeID;
	List<VendorOrdersData> vendorOrders;
	RelativeLayout layout;
	VendorOrdersData venList;
	SharedPreferences vendorSharedpreferences,customerPreference;
	Editor customerPrefEditor;
//	int list,preCount=0,listscrollposition=10;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Utils.trackError(getApplicationContext());

		setContentView(R.layout.driver_orders);
		vendorSharedpreferences = getSharedPreferences("vendorPrefs", Context.MODE_PRIVATE);		
		storeID=vendorSharedpreferences.getString("store_id","" );
		/*customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		customerPrefEditor=customerPreference.edit();
		storeID = customerPreference.getString("store_id", "");*/
		Log.e("comes to Order Activity---StoreID", storeID);
		
		vendorOrders = new ArrayList<VendorOrdersData>();	
		vendorOrders.clear();		
		
		textForPending = (TextView) findViewById(R.id.textForDriverPending);
		textForProcessing = (TextView) findViewById(R.id.textForDriverProcessing);
		textForHistory = (TextView) findViewById(R.id.textForDriverHistory);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		listOfOrders = (ListView) findViewById(R.id.listForDriverHistory);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnSettings = (ImageButton) findViewById(R.id.settingsBtn);
		btnCart = (ImageButton) findViewById(R.id.cartBtn);
		btnSettings.setVisibility(View.GONE);
		btnSlideMenu.setVisibility(View.VISIBLE);
		btnCart.setVisibility(View.GONE);
		btnSettings.setOnClickListener(this);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		backImg.setVisibility(View.GONE);
		textForHeader.setText("ORDERS");
		textForHistory.setOnClickListener(this);
		textForPending.setOnClickListener(this);
		textForProcessing.setOnClickListener(this);
		backImg.setOnClickListener(this);
		btnSlideMenu.setOnClickListener(this);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		
		type = "pending";		
		textForPending.setTextColor(Color.parseColor("#ffffff"));
		textForPending.setBackgroundColor(Color.parseColor("#fbb03b"));
		textForHistory.setTextColor(Color.parseColor("#fbb03b"));
		textForHistory.setBackgroundColor(Color.parseColor("#ffffff"));
		textForProcessing.setTextColor(Color.parseColor("#fbb03b"));
		textForProcessing.setBackgroundColor(Color.parseColor("#ffffff"));
		
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			Log.e("Type in order activity",type);
			new VendorOrderPending(type).execute();
		}
		else
		{
			Utils.ShowAlert(OrderActivity.this, Constant.networkDisconected);
		}	

		listOfOrders.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Intent intentToSpecificOrder = new Intent(OrderActivity.this,
						OrderSpecificActivity.class);

				intentToSpecificOrder.putExtra("type", type);

				intentToSpecificOrder.putExtra("country",
						vendorOrders.get(position).country);
				intentToSpecificOrder.putExtra("customerName",
						vendorOrders.get(position).customerName);
				intentToSpecificOrder.putExtra("orderId",
						vendorOrders.get(position).orderId);
				intentToSpecificOrder.putExtra("phoneNumber",
						vendorOrders.get(position).phoneNumber);
				intentToSpecificOrder.putExtra("state",
						vendorOrders.get(position).state);
				intentToSpecificOrder.putExtra("storeName",
						vendorOrders.get(position).storeName);
				intentToSpecificOrder.putExtra("street",
						vendorOrders.get(position).street);
				intentToSpecificOrder.putExtra("time",
						vendorOrders.get(position).time);
				intentToSpecificOrder.putExtra("total",
						vendorOrders.get(position).total);
				Log.e("Vendor Item Selected total value",vendorOrders.get(position).total);			
				intentToSpecificOrder.putExtra("zipcode",
						vendorOrders.get(position).zipcode);
				intentToSpecificOrder.putExtra("city",
						vendorOrders.get(position).city);
				intentToSpecificOrder.putExtra("closed_ime",
						vendorOrders.get(position).closedTime);
				intentToSpecificOrder.putExtra("notes",
						vendorOrders.get(position).cust_Notes);
				Log.e("Vendor Item Selected",vendorOrders.get(position).cust_Notes);
				startActivity(intentToSpecificOrder);
				Log.e("Move to order","specific page");
			}
		});

			
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}

		if (v.getId() == R.id.textForDriverPending) {
			
			type = "pending";
			textForPending.setTextColor(Color.parseColor("#ffffff"));
			textForPending.setBackgroundColor(Color.parseColor("#fbb03b"));
			textForHistory.setTextColor(Color.parseColor("#fbb03b"));
			textForHistory.setBackgroundColor(Color.parseColor("#ffffff"));
			textForProcessing.setTextColor(Color.parseColor("#fbb03b"));
			textForProcessing.setBackgroundColor(Color.parseColor("#ffffff"));			
			
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new VendorOrderPending(type).execute();
			}
			else
			{
				Utils.ShowAlert(OrderActivity.this, Constant.networkDisconected);
			}
		}

		if (v.getId() == R.id.textForDriverProcessing) {
			
			type = "processing";
			textForPending.setTextColor(Color.parseColor("#fbb03b"));
			textForPending.setBackgroundColor(Color.parseColor("#ffffff"));
			textForProcessing.setTextColor(Color.parseColor("#ffffff"));
			textForProcessing.setBackgroundColor(Color.parseColor("#fbb03b"));
			textForHistory.setTextColor(Color.parseColor("#fbb03b"));
			textForHistory.setBackgroundColor(Color.parseColor("#ffffff"));

			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new VendorOrderPending(type).execute();
			}
			else
			{
				Utils.ShowAlert(OrderActivity.this, Constant.networkDisconected);
			}
		}

		if (v.getId() == R.id.textForDriverHistory) {
			
			type = "history";
			textForPending.setTextColor(Color.parseColor("#fbb03b"));
			textForPending.setBackgroundColor(Color.parseColor("#ffffff"));
			textForProcessing.setTextColor(Color.parseColor("#fbb03b"));
			textForProcessing.setBackgroundColor(Color.parseColor("#ffffff"));
			textForHistory.setTextColor(Color.parseColor("#ffffff"));
			textForHistory.setBackgroundColor(Color.parseColor("#fbb03b"));
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new VendorOrderPending(type).execute();

			}
			else
			{
				Utils.ShowAlert(OrderActivity.this, Constant.networkDisconected);
			}
			
		}
	}

	public class HistoryList extends BaseAdapter {
		Context mContext;

		String viewType;

		int list_Val;

		public HistoryList(OrderActivity orderActivity,
				List<VendorOrdersData> vendorOrders, String type) {
			// TODO Auto-generated constructor stub
			mContext = orderActivity;
			viewType = type;			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vendorOrders.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			LayoutInflater inflater = LayoutInflater.from(mContext);
			v = inflater.inflate(R.layout.driver_order_item, null);
			TextView nameText = (TextView) v
					.findViewById(R.id.textForVendorHistoryName);
			TextView orderText = (TextView) v
					.findViewById(R.id.textForVendorHistoryOrder);
			TextView timeText = (TextView) v
					.findViewById(R.id.textForVendorHistoryTimeIn);
			TextView addressTxt = (TextView) v
					.findViewById(R.id.textForVendorHistoryAddress);
			TextView phoneTxt = (TextView) v
					.findViewById(R.id.textForPhoneNumber);
			
			TextView driverNameTxt = (TextView) v
					.findViewById(R.id.textForVendorHistoryDriverName);
			TextView closedTime = (TextView) v
					.findViewById(R.id.textForVendorHistoryClosedTime);
			
			LinearLayout layoutForDriverName = (LinearLayout) v
					.findViewById(R.id.layoutForDriverName);
			LinearLayout layoutForClosedTime = (LinearLayout) v
					.findViewById(R.id.layoutForClosedTime);
			
			TextView textNameStatus = (TextView) v
					.findViewById(R.id.textForNameStatus);
			TextView textOrder = (TextView) v
					.findViewById(R.id.ordernotext);
			TextView textTime = (TextView) v
					.findViewById(R.id.timeintext);
			TextView textAddress = (TextView) v
					.findViewById(R.id.addresstext);
			TextView textPhone = (TextView) v
					.findViewById(R.id.phonetext);
			
			TextView textDriver = (TextView) v
					.findViewById(R.id.drivertext);
			TextView textClosed = (TextView) v
					.findViewById(R.id.closedtext);			
			
			fontSemiBold = Typeface.createFromAsset(getAssets(),"OpenSans-Semibold_0.ttf");
			fontRegular= Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf");
			
			nameText.setTypeface(fontSemiBold);
			textNameStatus.setTypeface(fontSemiBold);
			timeText.setTypeface(fontSemiBold);
			orderText.setTypeface(fontSemiBold);
			
			textOrder.setTypeface(fontRegular);
			textTime.setTypeface(fontRegular);
			textAddress.setTypeface(fontRegular);
			textDriver.setTypeface(fontRegular);
			textClosed.setTypeface(fontRegular);
			driverNameTxt.setTypeface(fontRegular);
			closedTime.setTypeface(fontRegular);
			addressTxt.setTypeface(fontRegular);
			textPhone.setTypeface(fontRegular);
			phoneTxt.setTypeface(fontRegular);
			
			if (viewType.equals("pending")) {
				layoutForDriverName.setVisibility(View.GONE);
				layoutForClosedTime.setVisibility(View.GONE);
			} else if (viewType.equals("processing")) {
				layoutForClosedTime.setVisibility(View.GONE);
				layoutForDriverName.setVisibility(View.VISIBLE);
				driverNameTxt.setText(vendorOrders.get(position).driverName);

			} else {
				layoutForClosedTime.setVisibility(View.VISIBLE);
				layoutForDriverName.setVisibility(View.VISIBLE);
				driverNameTxt.setText(vendorOrders.get(position).driverName);
				closedTime.setText(vendorOrders.get(position).closedTime);
			}
			
			nameText.setText(vendorOrders.get(position).customerName);
			orderText.setText(vendorOrders.get(position).orderId);
			timeText.setText(vendorOrders.get(position).time);
			/*String address = vendorOrders.get(position).street + Utils.lineFeed
					+ vendorOrders.get(position).city + Utils.lineFeed
					+ vendorOrders.get(position).state + Utils.lineFeed
					+ vendorOrders.get(position).country + Utils.lineFeed
					+ vendorOrders.get(position).zipcode + Utils.lineFeed
					+ vendorOrders.get(position).phoneNumber;*/
			String address = vendorOrders.get(position).street + Utils.lineFeed
					+ vendorOrders.get(position).city + Utils.lineFeed
					+ vendorOrders.get(position).state +" , "+ vendorOrders.get(position).country +" "+ vendorOrders.get(position).zipcode; 
					
			addressTxt.setText(address);
			phoneTxt.setText(vendorOrders.get(position).phoneNumber);
			return v;
		}

	}

	public class VendorOrderPending extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error = "", orderType, storeName = "",driverName="",closedTime="";
		JSONObject jsonObj;

		public VendorOrderPending(String orderType1) {
			// TODO Auto-generated constructor stub
			orderType = orderType1;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			try {

				if (status.equals("1")) {
					Log.e("orderType", orderType);
					if (orderType.equals("pending")) {

						listOfOrders.setAdapter(new HistoryList(
								OrderActivity.this, vendorOrders, "pending"));
					} else if (orderType.equals("processing")) {
						listOfOrders
								.setAdapter(new HistoryList(OrderActivity.this,
										vendorOrders, "processing"));
					} else {
						listOfOrders.setAdapter(new HistoryList(
								OrderActivity.this, vendorOrders, "history"));
					}

				} else {
					Utils.ShowAlert(OrderActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OrderActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
			vendorOrders.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePair=new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("store_id", storeID));

			if (orderType.equals("pending")) {
				jsonObj = new ServerResponse(UrlGenerator.vendorPendingOrders())
						.getJSONObjectfromURL(RequestType.POST, nameValuePair);
				Utils.writefile(jsonObj.toString(), "pending.txt");
			} else if (orderType.equals("processing")) {
				jsonObj = new ServerResponse(
						UrlGenerator.vendorProcessingOrders())
						.getJSONObjectfromURL(RequestType.POST, nameValuePair);
				Utils.writefile(jsonObj.toString(), "processing.txt");
			} else {
				jsonObj = new ServerResponse(
						UrlGenerator.vendorCompletedOrder())
						.getJSONObjectfromURL(RequestType.POST, nameValuePair);
				Utils.writefile(jsonObj.toString(), "history.txt");
			}
			Log.e("vendor Dashbord", jsonObj + "");
			
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
						vendorOrders.clear();
						String orders = jsonObj.getString("orders");
						JSONArray jarrOrders = new JSONArray(orders);
						Log.e("OrderActivity Total Length 11111111111111",""+jarrOrders.length());
						for (int i = 0; i < jarrOrders.length(); i++) {
							JSONObject job1 = jarrOrders.getJSONObject(i);
							if (!orderType.equals("pending")) {
								storeName = job1.getString("store_name");
								 driverName= job1.getString("driver_name");
								 if (orderType.equals("history")) {
									 closedTime=job1.getString("closed_time");
								}
							}
						
							String customerName = job1
									.getString("customer_name");
							String orderId = job1.getString("order_id");
							String time = job1.getString("time");
							String street = job1.getString("street");
							String country = job1.getString("country");
							String zipcode = job1.getString("zipcode");
							String state = job1.getString("state");
							String phoneNumber = job1.getString("phone_number");
							String total = job1.getString("total");
							String city = job1.getString("city");
							String customerNotes = job1.getString("customer_notes");
							Log.e("total Notes Values",customerNotes);
							street = street.replace("[", "").replace("]", "")
									.replace("\"", "");
							Log.e("street", street);
							if(country.equals("null"))
							{
								country="-";
							}
							if(state.equals("null"))
							{
								state="-";
							}
							if(street.equals("null"))
							{
								street="-";
							}
							if(phoneNumber.equals("null"))
							{
								phoneNumber="-";
							}
							if(city.equals("null"))
							{
								city="-";
							}								
							
							vendorOrders.add(new VendorOrdersData(storeName,
									customerName, orderId, time, street,
									country, zipcode, state, phoneNumber,
									total, city,driverName,closedTime,customerNotes));						
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
