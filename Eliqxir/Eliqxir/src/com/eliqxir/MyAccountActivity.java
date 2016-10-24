package com.eliqxir;

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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.PendingList;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.support.UserHistoryclass;
import com.eliqxir.support.UserPendingOrderClass;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.UserFavoriteclass;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MyAccountActivity extends SlidingMenuActivity implements
		OnClickListener {

	TextView textForHistory, textForPending, textForFavorites, textForHeader;
	Typeface fontSemiBold,fontRegular;
	ListView listOfHistory, listPending, listFavorite;
	ImageButton backImg, btnSlideMenu, btnSettings, btnCart;
	SharedPreferences sharedpreferences;
	Editor editor;
	boolean rememberUser;
	String customerId = "";
	List<UserHistoryclass> userHistory;
	List<UserPendingOrderClass> userPendingOrders;
	List<UserFavoriteclass> userFavorite_items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.myaccount);
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		userHistory = new ArrayList<UserHistoryclass>();
		userPendingOrders = new ArrayList<UserPendingOrderClass>();
		userFavorite_items = new ArrayList<UserFavoriteclass>();
		rememberUser = sharedpreferences.getBoolean("remember-user", false);

		textForHistory = (TextView) findViewById(R.id.textForHistory);
		textForPending = (TextView) findViewById(R.id.textForPending);
		textForFavorites = (TextView) findViewById(R.id.textForFavorites);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		
		listOfHistory = (ListView) findViewById(R.id.listForHistory);
		listPending = (ListView) findViewById(R.id.listForPendingOrders);
		listFavorite = (ListView) findViewById(R.id.listForFovorites);
		
		listOfHistory.setVisibility(View.VISIBLE);
		listFavorite.setVisibility(View.GONE);
		listPending.setVisibility(View.GONE);
		
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnSettings = (ImageButton) findViewById(R.id.settingsBtn);
		btnCart = (ImageButton) findViewById(R.id.cartBtn);
		
		btnSettings.setVisibility(View.VISIBLE);
		btnSlideMenu.setVisibility(View.VISIBLE);
		btnCart.setVisibility(View.GONE);
		
		btnSettings.setOnClickListener(this);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		backImg.setVisibility(View.GONE);

		// listOfHistory.setAdapter(new HistoryList(MyAccountActivity.this,
		// name,
		// orderNo, total, 4));
		textForHeader.setText("MY ACCOUNT");
		textForHistory.setOnClickListener(this);
		textForPending.setOnClickListener(this);
		textForFavorites.setOnClickListener(this);
		backImg.setOnClickListener(this);
		btnSlideMenu.setOnClickListener(this);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		textForHistory.setTextColor(getResources().getColor(R.color.white));
		textForHistory.setBackgroundColor(getResources().getColor(
				R.color.orange));
		textForPending
				.setTextColor(getResources().getColor(R.color.orange));
		textForPending.setBackgroundColor(getResources().getColor(
				R.color.white));
		textForFavorites.setTextColor(getResources().getColor(
				R.color.orange));
		textForFavorites.setBackgroundColor(getResources().getColor(
				R.color.white));
		listOfHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intentToOrder = new Intent(MyAccountActivity.this,
						MyAccountOrderActivity.class);
				UserHistoryclass user_histclass = userHistory.get(position);
				intentToOrder.putExtra("orderName", user_histclass.store_Name);
				intentToOrder.putExtra("orderNo", user_histclass.order_id);
				intentToOrder.putExtra("total", user_histclass.total);
				startActivity(intentToOrder);
			}
		});

		listPending.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intentToOrder = new Intent(MyAccountActivity.this,
						MyAccountOrderActivity.class);
				UserPendingOrderClass user_pendingclass = userPendingOrders.get(position);
				intentToOrder.putExtra("orderName", user_pendingclass.vendor);
				intentToOrder.putExtra("orderNo", user_pendingclass.order_id);
				intentToOrder.putExtra("total", user_pendingclass.total);
				startActivity(intentToOrder);
			}
		});
		
		// if(rememberUser)
		// {
		customerId = sharedpreferences.getString("userId", "");
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new GetUserHistory().execute();			
		} else {
			Utils.ShowAlert(MyAccountActivity.this, Constant.networkDisconected);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.textForHistory) {
			textForHistory.setTextColor(getResources().getColor(R.color.white));
			textForHistory.setBackgroundColor(getResources().getColor(
					R.color.orange));
			textForPending
					.setTextColor(getResources().getColor(R.color.orange));
			textForPending.setBackgroundColor(getResources().getColor(
					R.color.white));
			textForFavorites.setTextColor(getResources().getColor(
					R.color.orange));
			textForFavorites.setBackgroundColor(getResources().getColor(
					R.color.white));
			listOfHistory.setVisibility(View.VISIBLE);
			listPending.setVisibility(View.GONE);
			listFavorite.setVisibility(View.GONE);
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new GetUserHistory().execute();

				// }
				// else
				// {
				// Utils.ShowAlert(MyAccountActivity.this,
				// "User not logged In");
				// }
			} else {
				Utils.ShowAlert(MyAccountActivity.this,
						Constant.networkDisconected);
			}

		}

		if (v.getId() == R.id.textForPending) {
			textForHistory
					.setTextColor(getResources().getColor(R.color.orange));
			textForHistory.setBackgroundColor(getResources().getColor(
					R.color.white));
			textForPending.setTextColor(getResources().getColor(R.color.white));
			textForPending.setBackgroundColor(getResources().getColor(
					R.color.orange));
			textForFavorites.setTextColor(getResources().getColor(
					R.color.orange));
			textForFavorites.setBackgroundColor(getResources().getColor(
					R.color.white));
			listOfHistory.setVisibility(View.GONE);
			listPending.setVisibility(View.VISIBLE);
			listFavorite.setVisibility(View.GONE);
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new GetUserPendingOrders().execute();
			} else {
				Utils.ShowAlert(MyAccountActivity.this,
						Constant.networkDisconected);
			}

			// listOfHistory.setAdapter(new HistoryList(MyAccountActivity.this,
			// name, orderNo, total, 2));
		}

		if (v.getId() == R.id.textForFavorites) {
			textForHistory
					.setTextColor(getResources().getColor(R.color.orange));
			textForHistory.setBackgroundColor(getResources().getColor(
					R.color.white));
			textForPending
					.setTextColor(getResources().getColor(R.color.orange));
			textForPending.setBackgroundColor(getResources().getColor(
					R.color.white));
			textForFavorites.setTextColor(getResources()
					.getColor(R.color.white));
			textForFavorites.setBackgroundColor(getResources().getColor(
					R.color.orange));
			listOfHistory.setVisibility(View.GONE);
			listPending.setVisibility(View.GONE);
			listFavorite.setVisibility(View.VISIBLE);
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new GetUserFavoriteOrders().execute();
			} else {
				Utils.ShowAlert(MyAccountActivity.this,
						Constant.networkDisconected);
			}			
			// listOfHistory.setAdapter(new HistoryList(MyAccountActivity.this,
			// name, orderNo, total, 3));
		}

		if (v.getId() == R.id.backBtn) {
			finish();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}
		if (v.getId() == R.id.settingsBtn) {
			// if(rememberUser)
			// {
			startActivity(new Intent(MyAccountActivity.this, UserSettings.class));
			// }

			// else
			// {
			// Utils.ShowAlert(MyAccountActivity.this, "User not logged In");
			// }
		}
	}

	public class HistoryList extends BaseAdapter {
		Context mContext;

		public HistoryList(Context context, List<UserHistoryclass> userHistory1) {
			// TODO Auto-generated constructor stub
			mContext = context;
			userHistory = userHistory1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userHistory.size();
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
			v = inflater.inflate(R.layout.history_list_items, null);

			TextView nameText = (TextView) v
					.findViewById(R.id.textForHistoryName);
			TextView orderText = (TextView) v
					.findViewById(R.id.textForHistoryOrderField);
			TextView totalText = (TextView) v
					.findViewById(R.id.textForHistoryTotalField);
			
			TextView historyOrdertext = (TextView) v
					.findViewById(R.id.textForHistoryOrder);
			TextView historyTotaltext = (TextView) v
					.findViewById(R.id.textForHistoryTotal);			
			
			fontSemiBold = Typeface.createFromAsset(getAssets(),"OpenSans-Semibold_0.ttf");
			fontRegular= Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf");
			
			historyOrdertext.setTypeface(fontRegular);
			historyTotaltext.setTypeface(fontRegular);
			
			nameText.setTypeface(fontSemiBold);
			orderText.setTypeface(fontSemiBold);
			totalText.setTypeface(fontSemiBold);
			
			UserHistoryclass user_hist = userHistory.get(position);
			nameText.setText(user_hist.store_Name);
			orderText.setText(user_hist.order_id);
			double totprice = Double.parseDouble(user_hist.total);
	//		totalText.setText("$"+ user_hist.total);
			totalText.setText("$" + String.format("%.2f", totprice));
			return v;
		}

	}

	public class GetUserHistory extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error, userId;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(MyAccountActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("customer_id", customerId));

			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.UserMyAccount()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("My Account History response", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else {
						userHistory.clear();
						String orders = jsonObj.getString("orders");
						JSONArray jarr = new JSONArray(orders);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job2 = jarr.getJSONObject(i);
							String total = job2.getString("grand_total");
							String order_id = job2.getString("order_id");
							String storename = job2.getString("store_name");
							Log.e("total, order id ", total + " " + order_id);
							/*userHistory.add(new UserHistoryclass("android",storename,
									order_id, total));*/
							userHistory.add(new UserHistoryclass(order_id, total,storename));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();

			try {
				if (status.equals("1")) {
					listOfHistory.setAdapter(new HistoryList(
							MyAccountActivity.this, userHistory));
				} else {
					Utils.ShowAlert(MyAccountActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class GetUserPendingOrders extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error, userId;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(MyAccountActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("customer_id", customerId));

			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.UserPendingOrders()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("Pending Orders Pending response", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else {
						userPendingOrders.clear();
						String orders = jsonObj.getString("orders");
						JSONArray jarr = new JSONArray(orders);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job2 = jarr.getJSONObject(i);
							String total = job2.getString("grand_total");
							String order_id = job2.getString("order_id");
							String storeName = job2.getString("store_name");
							Log.e("total, order id ", total + " " + order_id);
							userPendingOrders.add(new UserPendingOrderClass(
									storeName, order_id, total));
						}

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();

			try {
				if (status.equals("1")) {
					listPending.setAdapter(new PendingList(
							MyAccountActivity.this, userPendingOrders));
				} else {
					Utils.ShowAlert(MyAccountActivity.this, error);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class GetUserFavoriteOrders extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error, userId;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(MyAccountActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("customer_id", customerId));

			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.customeFavorites()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("Pending Orders Favorite response", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else {
						userFavorite_items.clear();

						String orders = jsonObj.getString("orders");
						JSONArray jarr = new JSONArray(orders);
						for (int i = 0; i < jarr.length(); i++) {
							userFavorite_items.add(new UserFavoriteclass(jarr
									.getString(i)));

						}

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();

			try {
				if (status.equals("1")) {
					listFavorite.setAdapter(new FavoriteList(
							MyAccountActivity.this));
					// listPending.setAdapter(new
					// PendingList(MyAccountActivity.this,
					// userPendingOrders));
				} else {
					Utils.ShowAlert(MyAccountActivity.this, error);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class FavoriteList extends BaseAdapter {
		Context mContext;

		public FavoriteList(Context context) {
			// TODO Auto-generated constructor stub
			mContext = context;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userFavorite_items.size();
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
			v = inflater.inflate(R.layout.favoritelist, null);

			TextView nameText = (TextView) v.findViewById(R.id.txtFavoriteItem);

			nameText.setText(userFavorite_items.get(position).itemName);

			return v;
		}

	}
}
