package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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

public class OverviewActivity extends SlidingMenuActivity implements
		OnClickListener {
	public void onStop() {
		if (Constant.isVendorAvailable.equals("notAvailable")) {
			finish();
		}
		super.onStop();

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}

		return false;
	}

	ImageButton backImg, btnSlideMenu, cartBtn;
	TextView textForHeader, textForSalesAmnt, textForAvgTime;
	ListView listView;
	ArrayList<String> itemName = new ArrayList<String>();
	ArrayList<String> itemDesc = new ArrayList<String>();
	SharedPreferences vendorSharedpreferences,customerPreference;
	Editor vendorPreferenceEditor,customerPrefEditor;
	String storeID,classFrom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		setContentView(R.layout.overview);
		vendorSharedpreferences = getSharedPreferences("vendorPrefs",
				Context.MODE_PRIVATE);
		vendorPreferenceEditor = vendorSharedpreferences.edit();
		storeID = vendorSharedpreferences.getString("store_id", "");
		/*customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		customerPrefEditor=customerPreference.edit();
		storeID = customerPreference.getString("store_id", "");*/
		Log.e("Store in inside Overview Page",storeID);	
		 
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		textForSalesAmnt = (TextView) findViewById(R.id.textForSalesAmnt);
		textForAvgTime = (TextView) findViewById(R.id.textForAvgTime);
		listView = (ListView) findViewById(R.id.listForOverviewItems);
		btnSlideMenu.setVisibility(View.VISIBLE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.GONE);
		textForHeader.setText("OVERVIEW");
		btnSlideMenu.setOnClickListener(this);
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new VendorDashbord().execute();

		} else {
			Utils.ShowAlert(OverviewActivity.this, Constant.networkDisconected);
		}

	}

	public class OrderItemsAdapter extends BaseAdapter {
		Context context;

		public OrderItemsAdapter(Context mContext) {
			// TODO Auto-generated constructor stub
			context = mContext;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemName.size();
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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			int num = arg0 + 1;
			View v = arg1;
			LayoutInflater inflator = LayoutInflater.from(context);
			v = inflator.inflate(R.layout.list_overview_items, null);

			RelativeLayout linear_List = (RelativeLayout) v
					.findViewById(R.id.overviewList);
			TextView textForOrderNum = (TextView) v
					.findViewById(R.id.textForSellingItemNum);
			TextView textForOrderName = (TextView) v
					.findViewById(R.id.textForSellingItemName);
			TextView textForOrderDesc = (TextView) v
					.findViewById(R.id.textForSellingItemDesc);
			textForOrderNum.setText("" + num);
			textForOrderName.setText(itemName.get(arg0));
			textForOrderDesc.setText(itemDesc.get(arg0));

			linear_List.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("Position For OverviewList", "" + arg0);

					// Intent intentToMenu = new
					// Intent(OverviewActivity.this,EditingMenuActivity.class);
					// intentToMenu.putExtra("id", idIntent);
					// intentToMenu.putExtra("price", priceIntent);
					// intentToMenu.putExtra("name", nameIntent);
					// intentToMenu.putExtra("desc", descIntent);
					// intentToMenu.putExtra("status", status);
					// intentToMenu.putExtra("sku", skuIntent);
					// intentToMenu.putExtra("parent", parentIntent);
					// intentToMenu.putExtra("category", categoryIntent);
					// intentToMenu.putExtra("product_id", productId);
					// startActivity(intentToMenu);

				}
			});

			return v;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}
	}

	public class VendorDashbord extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error, sales, avg_delivery_time;

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					textForSalesAmnt.setText("$" + sales);
					if (avg_delivery_time.equals("0"))
						textForAvgTime.setText("0.00 Minutes");
					else
						textForAvgTime.setText(avg_delivery_time + " Minutes");
					listView.setAdapter(new OrderItemsAdapter(
							OverviewActivity.this));
				} else {
					Utils.ShowAlert(OverviewActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OverviewActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("store_id", storeID));
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.vendorDashboard()).getJSONObjectfromURL(
					RequestType.POST, nameValuePair);
			Log.e("vendor Dashbord", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
						itemDesc.clear();
						itemName.clear();
						sales = jsonObj.getString("sales");
						avg_delivery_time = jsonObj
								.getString("average_deliverytime");

						String products = jsonObj.getString("products");
						JSONArray jarr = new JSONArray(products);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job1 = jarr.getJSONObject(i);
							String name = job1.getString("name");
							String description = job1.getString("description");
							itemName.add(name);
							itemDesc.add(description);
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
