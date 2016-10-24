package com.eliqxir.driver;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.OrderSpecificActivity;
import com.eliqxir.vendor.VendorOrderViewData;
import com.eliqxir.vendor.OrderSpecificActivity.OrderItems;
import com.eliqxir.vendor.OrderSpecificActivity.VendorOrderView;

public class OrderDeliveredActivity extends Activity implements OnClickListener {

	public void onStop() {
		if (Constant.isDriverAvailable.equals("notAvailable")) {
			finish();
		}
		super.onStop();
	}
	List<DriverOrdersClass> driverOrders;
	/*ArrayList<String> quantity = new ArrayList<String>();
	ArrayList<String> orderItmName = new ArrayList<String>();
	ArrayList<String> orderItmSize = new ArrayList<String>();
	ArrayList<String> orderItmValue = new ArrayList<String>();*/
	ImageView imageForDriverOrderCallStore, imageForDriverOrderCallCustomer;
	ImageButton backImg, cartBtn, btnSlideMenu, btnForAssignDriver;
	TextView textForHeader, textForDriverOrderTimeIn,textForDriverOrderNotes,
			textForDriverOrderOrderNo, textForDriverOrderAddress,textDriverOrderDriverName,
			textForDriverOrderPhone, textForDriverOrderName,textSubTotalValue;
	ListView listOfOrders;
	String orderId, time, address, customer_phone, store_phone, total,grand_tot,
			customer_name, store_name, product_name, size, qty, price,cust_Notes,
			product_id, type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.driver_order_delivered);

		driverOrders = new ArrayList<DriverOrdersClass>();
		driverOrders.clear();
		/*quantity.clear();
		orderItmName.clear();
		orderItmSize.clear();
		orderItmValue.clear();*/
		orderId = getIntent().getExtras().getString("orderId");
		time = getIntent().getExtras().getString("time");
		address = getIntent().getExtras().getString("address");
		customer_phone = getIntent().getExtras().getString("customer_phone");
		store_phone = getIntent().getExtras().getString("store_phone");
		total = getIntent().getExtras().getString("total");
		customer_name = getIntent().getExtras().getString("customer_name");
		store_name = getIntent().getExtras().getString("store_name");
		product_name = getIntent().getExtras().getString("product_name");
		size = getIntent().getExtras().getString("size");
		qty = getIntent().getExtras().getString("qty");
		price = getIntent().getExtras().getString("price");
		product_id = getIntent().getExtras().getString("product_id");
		type = getIntent().getExtras().getString("type");
		cust_Notes = getIntent().getExtras().getString("notes");
		grand_tot = getIntent().getExtras().getString("grand_total");
		/*quantity.add(qty);
		orderItmName.add(product_name);
		orderItmValue.add(price);
		orderItmSize.add(size);*/
		textForDriverOrderTimeIn = (TextView) findViewById(R.id.textForDriverOrderTimeIn);
		textForDriverOrderTimeIn.setText(time);
		textForDriverOrderOrderNo = (TextView) findViewById(R.id.textForDriverOrderOrderNo);
		textForDriverOrderOrderNo.setText(orderId);
		textForDriverOrderAddress = (TextView) findViewById(R.id.textForDriverOrderAddress);
		textForDriverOrderAddress.setText(address);
		textForDriverOrderPhone = (TextView) findViewById(R.id.textForDriverOrderPhone);
		textForDriverOrderPhone.setText(customer_phone);
		textForDriverOrderName = (TextView) findViewById(R.id.textForDriverOrderName);
		textForDriverOrderNotes = (TextView) findViewById(R.id.textForDriverOrderNotes);
		textForDriverOrderNotes.setText(cust_Notes);
		textSubTotalValue= (TextView) findViewById(R.id.subtotal_value);
		textSubTotalValue.setText("$"+grand_tot);
		textDriverOrderDriverName=(TextView) findViewById(R.id.textForDriverOrderDriverName);
		textDriverOrderDriverName.setText(store_name);
		imageForDriverOrderCallCustomer = (ImageView) findViewById(R.id.imageForDriverOrderCallCustomer);
		imageForDriverOrderCallStore = (ImageView) findViewById(R.id.imageForDriverOrderCallStore);
		imageForDriverOrderCallStore.setOnClickListener(this);
		imageForDriverOrderCallCustomer.setOnClickListener(this);
		textForDriverOrderName.setText(customer_name);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		btnForAssignDriver = (ImageButton) findViewById(R.id.btnForAssignDriver);
		btnForAssignDriver.setOnClickListener(this);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		listOfOrders = (ListView) findViewById(R.id.listForOrderAssign);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setOnClickListener(this);
		textForHeader.setText("ORDER");
		boolean isOnline = Utils.isOnline();
		Log.e("Order Type",type);
		Log.e("isOnline 1111", isOnline + "");
		if (isOnline) {
			new DriverSpecficView(type).execute();
		} else	{
				Utils.ShowAlert(OrderDeliveredActivity.this, Constant.networkDisconected);
			}
		
		if (type.equals("history")) {
			btnForAssignDriver.setVisibility(View.GONE);	
		}
		//	listOfOrders.setAdapter(new OrderItems(getBaseContext()));
	}

	public class DriverSpecficView extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error = "", orderType;
		JSONObject jsonObj;

		public DriverSpecficView(String orderType1) {
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
					listOfOrders.setAdapter(new OrderItems(getBaseContext(),
							driverOrders));
					Log.e("ListOfOrders value", listOfOrders + "");
				} else {
					Utils.ShowAlert(OrderDeliveredActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OrderDeliveredActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("order_id", orderId));
			Log.e("orderId in orderspecific page",orderId);			
		/*	if (orderType.equals("history")) {
				jsonObj = new ServerResponse(
						UrlGenerator.vendorPendingOrderView())
						.getJSONObjectfromURL(RequestType.POST, nameValuePairs);			}
			else {*/
				jsonObj = new ServerResponse(
						UrlGenerator.driverOrderView())
						.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
	//		} 
			Log.e("Driver Specific View", jsonObj + "");
			
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
						String orderInfo = jsonObj.getString("Order_info");
						JSONObject job1 = new JSONObject(orderInfo);
						Log.e("orderId inside status value", orderId + "");
						String orders = job1.getString(orderId);
						JSONArray jarr = new JSONArray(orders);
						Log.e("Jarray length() inside status value", jarr.length() + "");
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job2 = jarr.getJSONObject(i);						
							
							String productName = job2.getString("product_name");
							String price = job2.getString("price");
							String qty = job2.getString("qty");
							String size = job2.getString("size");
							
							String productId = job2.getString("product_id");						
							String optionValue = job2.getString("option_value");
							String optionId = job2.getString("option_id");
							String sku = job2.getString("sku");							
							total = job2.getString("total");
							Log.e("ProductName value", productName + "");
							driverOrders.add(new DriverOrdersClass(productName,price,qty,size,productId,optionValue,
									optionId, sku));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
			return null;
		}

	}
	
	public class OrderItems extends BaseAdapter {
		Context context;

		public OrderItems(Context context1, List<DriverOrdersClass> driverOrders) {
			// TODO Auto-generated constructor stub
			context = context1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return driverOrders.size();
		}

		@Override
		public Object getItem(int position) {
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
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.list_of_order_items, null);
			TextView textForQuantity = (TextView) v
					.findViewById(R.id.textForQuantities);
			TextView textForOrderItem = (TextView) v
					.findViewById(R.id.textForOrderItemName);
			TextView textForOrderItemPacks = (TextView) v
					.findViewById(R.id.textForOrderItemPacks);
			TextView textForOrderAmnt = (TextView) v
					.findViewById(R.id.textForAmount);

			textForQuantity.setText(driverOrders.get(position).specificqty2);
			textForOrderItem.setText(driverOrders.get(position).specificproductName);
			textForOrderItemPacks.setText(driverOrders.get(position).specificsize2);
			textForOrderAmnt.setText(driverOrders.get(position).specificprice2);
			
			/*textForQuantity.setText(quantity.get(position));
			textForOrderItem.setText(orderItmName.get(position));
			textForOrderItemPacks.setText(orderItmSize.get(position));
			textForOrderAmnt.setText(orderItmValue.get(position));*/
			return v;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backBtn:
			this.finish();
		/*	Intent intentToOverview = new Intent(
					OrderDeliveredActivity.this, OrdersActivity.class);
			startActivity(intentToOverview);*/
			break;
		case R.id.imageForDriverOrderCallCustomer:
			Intent callIntent = new Intent(Intent.ACTION_DIAL);
			callIntent.setData(Uri.parse("tel:" + customer_phone));
			startActivity(callIntent);
			break;
		case R.id.imageForDriverOrderCallStore:
			Intent callIntent1 = new Intent(Intent.ACTION_DIAL);
			callIntent1.setData(Uri.parse("tel:" + store_phone));
			startActivity(callIntent1);
			break;
		case R.id.btnForAssignDriver:
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new MarkOrderDelivered().execute();
			} else {
				Utils.ShowAlert(OrderDeliveredActivity.this,
						Constant.networkDisconected);
			}
			break;
		}
	}

	public class MarkOrderDelivered extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error = "", orderType;
		JSONObject jsonObj;

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					showDeliverAlert();
				} else {
					btnForAssignDriver
							.setBackgroundResource(R.drawable.mark_as_deleivered_btn);
					Utils.ShowAlert(OrderDeliveredActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OrderDeliveredActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("order_id", orderId));
			Log.e("orderId in orderspecific page", orderId);

			jsonObj = new ServerResponse(UrlGenerator.vendorMarkDelivered())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);

			Log.e("order Delivered", jsonObj + "");
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

	private void showDeliverAlert() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(OrderDeliveredActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.closed_order_popup);
		// dialog.getWindow().setBackgroundDrawable(
		// new ColorDrawable(android.graphics.Color.WHITE));
		dialog.setCanceledOnTouchOutside(false);
		TextView textOk = (TextView) dialog.findViewById(R.id.txtForClosedOk);
		textOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				startActivity(new Intent(OrderDeliveredActivity.this,
						OrdersActivity.class));
			}
		});
		dialog.show();
	}
}
