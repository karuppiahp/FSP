package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

public class OrderSpecificActivity extends Activity implements OnClickListener {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();
		
	}
	
	Typeface fontSemiBold,fontRegular;
	ImageButton backImg, cartBtn, btnSlideMenu;
	TextView textForHeader, textForVendorOrderName, textForVendorOrderOrderNo,
			textForVendorOrderTimeIn, textForVendorOrderDriverClosedTime,
			textForVendorOrderAddress, textForVendorOrderDriverName,textOrderNo,textTimeIn,
			textClosed,textAddress,textPhone,textNotes,textDriver,textSubtotal,
			textForVendorOrderPhone, subtotal_value,textDelivery,textForVendorOrderNotes;
	ListView listView;
	
	Double totalPrice;
	String type, country, customerName, orderId, phoneNumber, state, storeName,customer_Notes,
			street, inTime, total, zipcode, city, closedTime,driverName;
	ImageButton btnForAssign;
	LinearLayout layoutForBottom, layoutForClosedTime, layoutForDriverName;
	List<VendorOrderViewData> orderView;
  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_assign_mark);
		orderView = new ArrayList<VendorOrderViewData>();
		orderView.clear();
		type = getIntent().getExtras().getString("type");

		country = getIntent().getExtras().getString("country");
		customerName = getIntent().getExtras().getString("customerName");
		orderId = getIntent().getExtras().getString("orderId");
		phoneNumber = getIntent().getExtras().getString("phoneNumber");
		Log.e("phoneNumber",phoneNumber);
	
		state = getIntent().getExtras().getString("state");
		storeName = getIntent().getExtras().getString("storeName");
		street = getIntent().getExtras().getString("street");
		inTime = getIntent().getExtras().getString("time");
		city = getIntent().getExtras().getString("city");
		closedTime = getIntent().getExtras().getString("closed_ime");
		customer_Notes = getIntent().getExtras().getString("notes");	
		// total = getIntent().getExtras().getString("total");
		// Log.e("Total1 >>", total);
		zipcode = getIntent().getExtras().getString("zipcode");
		textForVendorOrderName = (TextView) findViewById(R.id.textForVendorOrderName);
		textForVendorOrderOrderNo = (TextView) findViewById(R.id.textForVendorOrderOrderNo);
		textForVendorOrderTimeIn = (TextView) findViewById(R.id.textForVendorOrderTimeIn);
		textForVendorOrderDriverClosedTime = (TextView) findViewById(R.id.textForVendorOrderDriverClosedTime);
		textForVendorOrderDriverClosedTime.setText(closedTime);
		textForVendorOrderAddress = (TextView) findViewById(R.id.textForVendorOrderAddress);
		textForVendorOrderDriverName = (TextView) findViewById(R.id.textForVendorOrderDriverName);
		subtotal_value = (TextView) findViewById(R.id.subtotal_value);
//		subtotal_value.setText("$" + String.format("%.2f", totalPrice));
		textForVendorOrderPhone = (TextView) findViewById(R.id.textForVendorOrderPhone);
		textForVendorOrderNotes= (TextView) findViewById(R.id.textForVendorOrderNotes);
		
		textDelivery= (TextView) findViewById(R.id.textForDeliveryText);		
		textOrderNo= (TextView) findViewById(R.id.orderNoText);
		textTimeIn= (TextView) findViewById(R.id.timeInText);
		textClosed= (TextView) findViewById(R.id.closedText);
		textAddress= (TextView) findViewById(R.id.addressText);
		textPhone= (TextView) findViewById(R.id.phoneText);
		textNotes= (TextView) findViewById(R.id.notesText);
		textDriver= (TextView) findViewById(R.id.driverText);
		textSubtotal= (TextView) findViewById(R.id.subtotalText);
		
		fontSemiBold = Typeface.createFromAsset(getAssets(),"OpenSans-Semibold_0.ttf");
		fontRegular= Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf");
		
		textDelivery.setTypeface(fontSemiBold);
		textForVendorOrderName.setTypeface(fontSemiBold);
		textSubtotal.setTypeface(fontSemiBold);
		subtotal_value.setTypeface(fontSemiBold);
		textForVendorOrderTimeIn.setTypeface(fontSemiBold);
		textForVendorOrderOrderNo.setTypeface(fontSemiBold);
		
		textOrderNo.setTypeface(fontRegular);
		textTimeIn.setTypeface(fontRegular);
		textClosed.setTypeface(fontRegular);
		textAddress.setTypeface(fontRegular);
		textPhone.setTypeface(fontRegular);
		textNotes.setTypeface(fontRegular);
		textDriver.setTypeface(fontRegular);
		textForVendorOrderDriverName.setTypeface(fontRegular);
		textForVendorOrderNotes.setTypeface(fontRegular);
		textForVendorOrderPhone.setTypeface(fontRegular);
		textForVendorOrderAddress.setTypeface(fontRegular);
		textForVendorOrderDriverClosedTime.setTypeface(fontRegular);
		
/*		textForVendorOrderAddress.setText(street + Utils.lineFeed + city
				+ Utils.lineFeed + state + Utils.lineFeed + country
				+ Utils.lineFeed + zipcode);*/
		
		textForVendorOrderAddress.setText(street + Utils.lineFeed + city
				+ Utils.lineFeed + state +","+ country +" "+ zipcode);
		
		textForVendorOrderTimeIn.setText(inTime);
		textForVendorOrderName.setText(customerName);
		Log.e("phoneNumber vvvvvvvvvvv",phoneNumber);
		textForVendorOrderPhone.setText(phoneNumber);			
		textForVendorOrderNotes.setText(customer_Notes);
		textForVendorOrderOrderNo.setText(orderId);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		listView = (ListView) findViewById(R.id.listForOrderAssign);
		btnForAssign = (ImageButton) findViewById(R.id.btnForAssignDriver);
		layoutForBottom = (LinearLayout) findViewById(R.id.layoutForOrderBottom);
		layoutForClosedTime = (LinearLayout) findViewById(R.id.layoutForDriverClosed);
		layoutForDriverName = (LinearLayout) findViewById(R.id.layoutForDriverName);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setOnClickListener(this);
		btnForAssign.setOnClickListener(this);
		textForHeader.setText("ORDER");
		Log.e("Order type in order specific activity",type);
		
		if (type.equals("pending")) {
			btnForAssign.setBackgroundResource(R.drawable.assign_driver_btn);
			layoutForClosedTime.setVisibility(View.GONE);
			layoutForDriverName.setVisibility(View.GONE);
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline 1111", isOnline + "");
			if (isOnline) {
				new VendorOrderView(type).execute();
			}
			else
			{
				Utils.ShowAlert(OrderSpecificActivity.this, Constant.networkDisconected);
			}
		} else if (type.equals("processing")) {
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline 2222", isOnline + "");
			if (isOnline) {
				Log.e("Inside Main","processing API Loading");
				new VendorOrderView(type).execute();
			}
			else
			{
				Utils.ShowAlert(OrderSpecificActivity.this, Constant.networkDisconected);
			}
			btnForAssign
					.setBackgroundResource(R.drawable.mark_as_deleivered_btn);
			layoutForClosedTime.setVisibility(View.GONE);
			layoutForDriverName.setVisibility(View.VISIBLE);

		} else {
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline 3333", isOnline + "");
			if (isOnline) {
				Log.e("Inside Main","Complete API Loading");
				new VendorOrderView(type).execute();
			}
			else
			{
				Utils.ShowAlert(OrderSpecificActivity.this, Constant.networkDisconected);
			}
			layoutForBottom.setVisibility(View.GONE);
			layoutForClosedTime.setVisibility(View.VISIBLE);
			layoutForDriverName.setVisibility(View.VISIBLE);
		}		
	}

	public class OrderItems extends BaseAdapter {

		Context context;

		// public OrderItems(Context baseContext) {
		// // TODO Auto-generated constructor stub
		// context = baseContext;
		// }

		public OrderItems(Context baseContext, String orderType,
				List<VendorOrderViewData> orderView) {
			// TODO Auto-generated constructor stub
			context = baseContext;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderView.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v = arg1;
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.list_of_order_items, null);
			TextView textForQuantity = (TextView) v
					.findViewById(R.id.textForQuantities);
			TextView textForx = (TextView) v
					.findViewById(R.id.textForX);			
			TextView textForOrderItem = (TextView) v
					.findViewById(R.id.textForOrderItemName);
			TextView textForOrderItemPacks = (TextView) v
					.findViewById(R.id.textForOrderItemPacks);
			TextView textForOrderAmnt = (TextView) v
					.findViewById(R.id.textForAmount);
			
			textForQuantity.setTypeface(fontSemiBold);
			textForx.setTypeface(fontSemiBold);
			textForOrderItem.setTypeface(fontSemiBold);
			textForOrderAmnt.setTypeface(fontSemiBold);
			
			textForOrderItemPacks.setTypeface(fontRegular);
			
			Log.e("&&&&&&&&&&&","$$$$$$$$$$$");
			textForQuantity.setText(orderView.get(position).qty);
			textForOrderItem.setText(orderView.get(position).productName);
			if(orderView.get(position).size.equals("null"))
			{
				textForOrderItemPacks.setText("-");
			}
			else
			{
			textForOrderItemPacks.setText(orderView.get(position).size);
			}
			textForOrderAmnt.setText("$"
					+ String.format("%.2f",
							Double.parseDouble(orderView.get(position).price)));
			return v;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backBtn:
			Log.e("Back Button","clicked in order specific page");
			finish();			
			break;

		case R.id.btnForAssignDriver:
			if (type.equals("pending")) {
				Intent intentToOrder = new Intent(OrderSpecificActivity.this,
						AssignDriverActivity.class);
				intentToOrder.putExtra("order_id", orderId);
				startActivity(intentToOrder);
			} else if (type.equals("processing")) {
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					new MarkOrderDelivered().execute();
				}
				else
				{
					Utils.ShowAlert(OrderSpecificActivity.this, Constant.networkDisconected);
				}				
			 btnForAssign.setBackgroundResource(R.drawable.finishing_up);				
			}
			break;
		}
	}

	public class VendorOrderView extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error = "", orderType;
		JSONObject jsonObj;

		public VendorOrderView(String orderType1) {
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
				//	listView.removeAllViews();
					totalPrice = Double.parseDouble(total);

					Log.e("totalPrice 5555555555", totalPrice + "");
					subtotal_value.setText("$"
							+ String.format("%.2f", totalPrice));
					Log.e("phoneNumber Asyn Task postexecute",phoneNumber);
					textForVendorOrderPhone.setText(phoneNumber);
					textForVendorOrderDriverName.setText(driverName);
					listView.setAdapter(new OrderItems(getBaseContext(),
							orderType, orderView));
				} else {
					Utils.ShowAlert(OrderSpecificActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OrderSpecificActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("order_id", orderId));
			Log.e("orderId in orderspecific page", orderId);
			if (orderType.equals("pending")) {

				jsonObj = new ServerResponse(
						UrlGenerator.vendorPendingOrderView())
						.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			}

			else if (orderType.equals("processing")) {
				jsonObj = new ServerResponse(
						UrlGenerator.vendorProcessingOrderView())
						.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			} else {
				jsonObj = new ServerResponse(
						UrlGenerator.vendorCompletedOrderView())
						.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			}
			Log.e("vendor orderview", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {

						String orderInfo = jsonObj.getString("Order_info");
						JSONObject job1 = new JSONObject(orderInfo);
						String orders = job1.getString(orderId);
						JSONArray jarr = new JSONArray(orders);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job2 = jarr.getJSONObject(i);

							/*phoneNumber = job2.getString("phone_number");
							if(phoneNumber.equals("null"))
							{
								phoneNumber="-";
							}*/
							String price = job2.getString("price");
							String productId = job2.getString("product_id");
							String productName = job2.getString("product_name");
							String optionValue = job2.getString("option_value");
							String optionId = job2.getString("option_id");
							String sku = job2.getString("sku");
							String size = job2.getString("size");
							String qty = job2.getString("qty");
							total = job2.getString("grand_total");
						
							orderView.add(new VendorOrderViewData(price,
									productId, productName, optionValue,
									optionId, sku, size, qty));
							if (!orderType.equals("pending")) {
							driverName=job2.getString("driver_name");
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
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
					 btnForAssign.setBackgroundResource(R.drawable.mark_as_deleivered_btn);
					Utils.ShowAlert(OrderSpecificActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(OrderSpecificActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("order_id", orderId));
			Log.e("orderId in orderspecific page", orderId);
			
				jsonObj = new ServerResponse(
						UrlGenerator.vendorMarkDelivered())
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
		 final Dialog dialog = new Dialog(OrderSpecificActivity.this);
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 dialog.setContentView(R.layout.closed_order_popup);
		 // dialog.getWindow().setBackgroundDrawable(
		 // new ColorDrawable(android.graphics.Color.WHITE));
		 dialog.setCanceledOnTouchOutside(false);
		 TextView textOk = (TextView) dialog
		 .findViewById(R.id.txtForClosedOk);
		 textOk.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View arg0) {
		 // TODO Auto-generated method stub
		 dialog.dismiss();
		 startActivity(new Intent(OrderSpecificActivity.this,OrderActivity.class));
		 }
		 });
		 dialog.show();
	}

}
