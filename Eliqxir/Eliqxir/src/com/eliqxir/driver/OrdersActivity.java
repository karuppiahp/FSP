package com.eliqxir.driver;

import java.util.ArrayList;
import java.util.Iterator;
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
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class OrdersActivity extends SlidingMenuActivity implements
		OnClickListener {
	public void onStop() {
		if (Constant.isDriverAvailable.equals("notAvailable")) {
			finish();
		}

		super.onStop();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}

		return false;
	}

	Typeface fontSemiBold, fontRegular;
	TextView textForProcessing, textForHistory,text_noOrder;
	ImageButton backImg, btnSlideMenu, cartBtn;
	TextView textForHeader;
	ListView listView;
	List<DriverOrdersClass> driverOrders;
	String lineFeed = System.getProperty("line.separator");
	SharedPreferences driverPref;
	String driverName;
	String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.driver_orders_screen);
		driverPref = this.getSharedPreferences("driverPref", 1);
		driverName = driverPref.getString("driverName", "");
		driverOrders = new ArrayList<DriverOrdersClass>();
		driverOrders.clear();
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);

		textForProcessing = (TextView) findViewById(R.id.textForDriverProcessing_DriverOrderScreen);
		textForHistory = (TextView) findViewById(R.id.textForDriverHistory_DriverOrderScreen);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		text_noOrder = (TextView) findViewById(R.id.text_noorder);
		listView = (ListView) findViewById(R.id.listForDriverHistory);
		btnSlideMenu.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.VISIBLE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.GONE);
		textForHeader.setText("ORDERS");

		textForHistory.setOnClickListener(this);
		textForProcessing.setOnClickListener(this);
		type = "processing";
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new DriverOrderProcessing(type).execute();
		} else {
			Utils.ShowAlert(OrdersActivity.this, Constant.networkDisconected);
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				Intent intentToOrder = new Intent(OrdersActivity.this,
						OrderDeliveredActivity.class);
				intentToOrder.putExtra("orderId",
						driverOrders.get(position).orderId);
				intentToOrder.putExtra("time", driverOrders.get(position).time);
				String address = driverOrders.get(position).street + lineFeed
						+ driverOrders.get(position).country + ","
						+ driverOrders.get(position).zipcode;
				intentToOrder.putExtra("address", address);
				intentToOrder.putExtra("customer_phone",
						driverOrders.get(position).customer_phone);
				intentToOrder.putExtra("store_phone",
						driverOrders.get(position).store_phone);
				intentToOrder.putExtra("total",
						driverOrders.get(position).total);
				intentToOrder.putExtra("customer_name",
						driverOrders.get(position).customer_name);
				intentToOrder.putExtra("store_name",
						driverOrders.get(position).store_name);
				intentToOrder.putExtra("product_name",
						driverOrders.get(position).product_name);
				intentToOrder.putExtra("size", driverOrders.get(position).size);
				intentToOrder.putExtra("qty", driverOrders.get(position).qty);
				intentToOrder.putExtra("price",
						driverOrders.get(position).price);
				intentToOrder.putExtra("product_id",
						driverOrders.get(position).product_id);
				intentToOrder.putExtra("type",
						type);
				intentToOrder.putExtra("notes",
						driverOrders.get(position).cust_Notes);
				intentToOrder.putExtra("grand_total",
						driverOrders.get(position).grand_total);
				startActivity(intentToOrder);
			}
		});
	}

	public class ProcessingListAdapter extends BaseAdapter {

		Context context;
		String viewType;

		public ProcessingListAdapter(Context mContext,
				List<DriverOrdersClass> driverOrders, String type) {
			// TODO Auto-generated constructor stub
			context = mContext;
			viewType = type;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return driverOrders.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub

			LayoutInflater inflator = LayoutInflater.from(context);
			View v = inflator.inflate(R.layout.driver_order_item, null);
			TextView textForName = (TextView) v
					.findViewById(R.id.textForVendorHistoryName);
			TextView textForNameStatus = (TextView) v
					.findViewById(R.id.textForNameStatus);
			TextView textForOrderNo = (TextView) v
					.findViewById(R.id.textForVendorHistoryOrder);
			TextView textForTimeIn = (TextView) v
					.findViewById(R.id.textForVendorHistoryTimeIn);
			TextView textForAddress = (TextView) v
					.findViewById(R.id.textForVendorHistoryAddress);
			TextView textForDriverName = (TextView) v
					.findViewById(R.id.textForVendorHistoryDriverName);

			LinearLayout layoiutForClosed = (LinearLayout) v
					.findViewById(R.id.layoutForClosedTime);
			layoiutForClosed.setVisibility(View.GONE);

			fontSemiBold = Typeface.createFromAsset(getAssets(),
					"OpenSans-Semibold_0.ttf");
			fontRegular = Typeface.createFromAsset(getAssets(),
					"OpenSans-Regular.ttf");

			textForName.setTypeface(fontSemiBold);
			textForNameStatus.setTypeface(fontSemiBold);
			textForOrderNo.setTypeface(fontSemiBold);
			textForTimeIn.setTypeface(fontSemiBold);

			textForAddress.setTypeface(fontRegular);
			textForDriverName.setTypeface(fontRegular);

			textForName.setText(driverOrders.get(position).customer_name);
			textForNameStatus.setText("Delivery");
			textForOrderNo.setText(driverOrders.get(position).orderId);
			textForTimeIn.setText(driverOrders.get(position).time);
			String addr = driverOrders.get(position).street + lineFeed
					+ driverOrders.get(position).state+ ","+driverOrders.get(position).country + ","
					+ driverOrders.get(position).zipcode;
			textForAddress.setText(addr);
			textForDriverName.setText(driverOrders.get(position).driver_name);
			return v;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}

		if (v.getId() == R.id.textForDriverProcessing_DriverOrderScreen) {
			type = "processing";
			textForProcessing.setTextColor(Color.parseColor("#ffffff"));
			textForProcessing.setBackgroundColor(Color.parseColor("#fbb03b"));
			textForHistory.setTextColor(Color.parseColor("#fbb03b"));
			textForHistory.setBackgroundColor(Color.parseColor("#ffffff"));
			Log.v("Proccess-->", "Process Buton");

			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new DriverOrderProcessing(type).execute();
			} else {
				Utils.ShowAlert(OrdersActivity.this,
						Constant.networkDisconected);
			}
		}

		if (v.getId() == R.id.textForDriverHistory_DriverOrderScreen) {
			type = "history";

			textForProcessing.setTextColor(Color.parseColor("#fbb03b"));
			textForProcessing.setBackgroundColor(Color.parseColor("#ffffff"));
			textForHistory.setTextColor(Color.parseColor("#ffffff"));
			textForHistory.setBackgroundColor(Color.parseColor("#fbb03b"));
			Log.v("History-->", "History Buton");
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new DriverOrderProcessing(type).execute();

			} else {
				Utils.ShowAlert(OrdersActivity.this,
						Constant.networkDisconected);
			}

		}
	}

	public class DriverOrderProcessing extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", orderId = "", error = "Login Unsuccessfull!",
				userId, fName, lName, dob, orderType;

		// public DriverLogin(String mailId, String pwd) {
		// // TODO Auto-generated constructor stub
		// email = mailId;
		// password = pwd;
		// }
		public DriverOrderProcessing(String orderType1) {
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
					  text_noOrder.setVisibility(View.GONE);
					  listView.setVisibility(View.VISIBLE);
					// SessionStore.saveSlidinMenu("driver", getBaseContext());
					// Intent intentToOverview = new Intent(
					// LoginDriverActivity.this, OrdersActivity.class);
					// startActivity(intentToOverview);
					// finish();

					Log.e("orderType", orderType);
					if (orderType.equals("processing")) {
						listView.setAdapter(new ProcessingListAdapter(
								OrdersActivity.this, driverOrders, "processing"));
					} else if (orderType.equals("history")) {
						listView.setAdapter(new ProcessingListAdapter(
								OrdersActivity.this, driverOrders, "history"));
					}

				} else {
					    text_noOrder.setVisibility(View.VISIBLE);
					    listView.setVisibility(View.GONE);
				//	Utils.ShowAlert(OrdersActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OrdersActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("driver_name", driverName));

			String api = UrlGenerator.driverOrderProcessing();
			if (orderType.equals("processing")) {
				api = UrlGenerator.driverOrderProcessing();
			} else if (orderType.equals("history")) {
				api = UrlGenerator.driverOrderComplete();
			}

			JSONObject jsonObj = new ServerResponse(api).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("Driver Login Page", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("1")) {
						driverOrders.clear();
						String orderInfo = jsonObj.getString("Order_info");
						JSONArray orderArray = new JSONArray(orderInfo);
						for (int i = 0; i < orderArray.length(); i++) {
							JSONObject job1 = orderArray.getJSONObject(i);
							@SuppressWarnings("unchecked")
							Iterator<String> keys = job1.keys();
							while (keys.hasNext()) {
								orderId = keys.next();

								Log.e("order id", orderId);
								String orderDetails = job1.getString(orderId);

								JSONObject job2 = new JSONObject(orderDetails);
								String total = job2.getString("total");
								String phone_number = job2
										.getString("phone_number");
								String product_id = job2
										.getString("product_id");
								String driver_name = job2
										.getString("driver_name");
								String store_phone = job2
										.getString("store_phone");
								String customer_phone = job2
										.getString("customer_phone");
								String zipcode = job2.getString("zipcode");
								String street = job2.getString("street");
								street = street.replace("[", "")
										.replace("]", "").replace("\"", "");
								String product_name = job2
										.getString("product_name");
								String customer_name = job2
										.getString("customer_name");
								String state = job2.getString("state");
								String qty = job2.getString("qty");
								String option_id = job2.getString("option_id");
								String sku = job2.getString("sku");
								String size = job2.getString("size");
								String country = job2.getString("country");
								String time = job2.getString("time");
								String price = job2.getString("price");
								String option_value = job2
										.getString("option_value");
								String store_name = job2
										.getString("store_name");
								String cust_note = job2
										.getString("customer_notes");
								String grand_total = job2
										.getString("grand_total");
								Log.e("Customer Name",customer_name);
								driverOrders.add(new DriverOrdersClass(orderId,
										total, phone_number, product_id,
										driver_name, store_phone,
										customer_phone, zipcode, street,
										product_name, customer_name, state,
										qty, option_id, sku, size, country,
										time, price, option_value, store_name,cust_note,grand_total));
							}
						}
					} else if (status.equals("0")) {
						error = jsonObj.getString("Error");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
