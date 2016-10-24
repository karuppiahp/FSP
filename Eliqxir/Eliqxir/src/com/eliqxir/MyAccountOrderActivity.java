package com.eliqxir;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.support.UserOrderViewClass;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

public class MyAccountOrderActivity extends SlidingMenuActivity implements
		OnClickListener {

	TextView textForHeader, textForOrderName, textForSubTotalField,
			textForTaxField, textForServiceFeeField, textForDiscountField,
			textForTipField;
	ImageButton imageCart, imageBack, btnSlideMenu;
	Button btnToOrderAgain;
	String orderName, orderNo, storeID, service_fee, tax, discount, orderTotal,store_status,delivery_status,
			quantityTot, tip;
	ListView listOfOrders;
	List<UserOrderViewClass> orderViewClass;
	DBAdapter db;
	// double totalPrice;

	SharedPreferences customerPreference, vendorSharedpreferences,storeclosedPreference;

	// int tax;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.myaccount_order);
		customerPreference = MyAccountOrderActivity.this.getSharedPreferences(
				"customerPrefs", Context.MODE_PRIVATE);
		vendorSharedpreferences = getSharedPreferences("vendorPrefs",
				Context.MODE_PRIVATE);
		storeID = vendorSharedpreferences.getString("store_id", "");
		Log.e("stordID", storeID);
		
		//Added on 24th April
		delivery_status = customerPreference.getString("delivery", "");		
		Log.e("Delivery_status in Add to cart page", delivery_status);
		//
		 storeclosedPreference=getSharedPreferences("storeclosedPrefs", Context.MODE_PRIVATE);
		 store_status=storeclosedPreference.getString("storeval", "");	
		 
		db = new DBAdapter(getBaseContext());
		orderViewClass = new ArrayList<UserOrderViewClass>();
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		textForSubTotalField = (TextView) findViewById(R.id.textForSubTotalField);
		textForTaxField = (TextView) findViewById(R.id.textForTaxField);
		textForServiceFeeField = (TextView) findViewById(R.id.textForServiceFeeField);
		textForDiscountField = (TextView) findViewById(R.id.textForDiscountField);
		textForOrderName = (TextView) findViewById(R.id.textForOrderName);
		textForTipField = (TextView) findViewById(R.id.textForTipField);
		imageBack = (ImageButton) findViewById(R.id.backBtn);
		imageCart = (ImageButton) findViewById(R.id.cartBtn);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnToOrderAgain = (Button) findViewById(R.id.btnToOrderAgain);
		listOfOrders = (ListView) findViewById(R.id.listForOrders);
		// imageBack.setOnClickListener(this);
		btnSlideMenu.setOnClickListener(this);
		imageCart.setVisibility(View.GONE);
		btnToOrderAgain.setOnClickListener(this);

		imageBack.setVisibility(View.GONE);
		btnSlideMenu.setVisibility(View.VISIBLE);
		orderName = getIntent().getExtras().getString("orderName");
		orderNo = getIntent().getExtras().getString("orderNo");
		Log.v("order no", orderNo);
		Log.v("order name", orderName);
		textForHeader.setText(orderNo);
		textForOrderName.setText(orderName);
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new OrderDetails().execute();
		} else {
			Utils.ShowAlert(MyAccountOrderActivity.this,
					Constant.networkDisconected);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
	//		toggle();
			this.finish();
		}

		if (v.getId() == R.id.btnToOrderAgain) {
			if(delivery_status.equals("0")||store_status.equals("storeclosed")){
				Utils.ShowAlert(MyAccountOrderActivity.this,"Not Possible to Order Again");
			}else{
			String size = null, qty = "", product_id = null, option_id = null, option_value = null;
			Log.v("orderViewClass.size()", orderViewClass.size() + "");
			boolean shortageFlag = true;
			for (int i = 0; i < orderViewClass.size(); i++) {
				UserOrderViewClass order_class = orderViewClass.get(i);
				Log.v("order_class.size", order_class.size);
				Log.v("order_class.qty", order_class.qty);
				Log.v("order_class.product_id", order_class.product_id);
				Log.v("order_class.option_id", order_class.option_id);
				Log.v("order_class.option_value", order_class.option_value);

				// for multiple items, send , separated values (Confirm Kavi
				// about this)

				int totalQty = Integer.parseInt(order_class.quantityNew);
				int orderedQty = Integer.parseInt(order_class.qty);

				if (totalQty > orderedQty) {
					size = size + "," + order_class.size;
					product_id = product_id + "," + order_class.product_id;
					qty = qty + "," + order_class.qty;
					option_id = option_id + "," + order_class.option_id;
					option_value = option_value + ","
							+ order_class.option_value;
				} else {
					shortageFlag = false;
				}

				// // for single item
				//
				// size=order_class.size;
				// product_id=order_class.product_id;
				// qty=order_class.qty;
				// option_id=order_class.option_id;
				// option_value=order_class.option_value;
			}
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				if (shortageFlag) {
					new AddCart(size, qty, product_id, option_id, option_value)
							.execute();
				} else {
					showAlert(size, qty, product_id, option_id, option_value);
				}
			} else {
				Utils.ShowAlert(MyAccountOrderActivity.this,
						Constant.networkDisconected);
			}
			// finish();
		}
	  }
	}

	AlertDialog dialog = null;

	private void showAlert(final String size, final String qty,
			final String product_id, final String option_id,
			final String option_value) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.custom_dialog, null);
		RelativeLayout rlClose = (RelativeLayout) v.findViewById(R.id.rl_close);
		TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
		TextView tvYes = (TextView) v.findViewById(R.id.tv_yes);

		rlClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tvYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				new AddCart(size, qty, product_id, option_id, option_value)
						.execute();
			}
		});

		builder.setView(v);
		dialog = builder.show();

	}

	public class OrderItems extends BaseAdapter {
		Context context;

		public OrderItems(Context context1,
				List<UserOrderViewClass> orderViewClass1) {
			// TODO Auto-generated constructor stub
			context = context1;
			orderViewClass = orderViewClass1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return orderViewClass.size();
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
			UserOrderViewClass order_class = orderViewClass.get(position);

			textForQuantity.setText(order_class.qty);
			textForOrderItem.setText(order_class.product_name);
			textForOrderItemPacks.setText(order_class.size);
			double price = Double.parseDouble(order_class.price);

			// textForSubTotalField.setText("$"+String.format("%.2f",totalPrice));
			textForOrderAmnt.setText("$" + String.format("%.2f", price));
			return v;
		}

	}

	public class OrderDetails extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error, order_id;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(MyAccountOrderActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("order_id", orderNo));

			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.UserOrderDetails()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("Order Specific Details", "" + jsonObj);
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else {

						// //added on 6th December

						JSONArray qtyArray = jsonObj
								.getJSONArray("product_total_quantity");
						for (int i = 0; i < qtyArray.length(); i++) {
							quantityTot = qtyArray.getJSONObject(i).getString(
									"qty");
						}
						Log.e("Total Quantity Value in myacc9999999",
								quantityTot);

						// ////////////upto this//////

						String order_info = jsonObj.getString("Order_info");
						service_fee = jsonObj.getString("service_fee");
						discount = jsonObj.getString("order_discount");
						orderTotal = jsonObj.getString("order_total");

						JSONObject job2 = new JSONObject(order_info);
						String order_ = job2.getString(orderNo.trim());
						JSONArray jarr = new JSONArray(order_);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job3 = jarr.getJSONObject(i);
							String price = job3.getString("price");
							tip = job3.getString("delivery_tip");
							price = String.format("%.2f",
									Double.parseDouble(price));
							tax = job3.getString("tax");
							tax = String
									.format("%.2f", Double.parseDouble(tax));
							String product_id = job3.getString("product_id");
							String product_name = job3
									.getString("product_name");
							String option_value = job3
									.getString("option_value");
							String qty = job3.getString("qty");
							String option_id = job3.getString("option_id");
							String sku = job3.getString("sku");
							String size = job3.getString("size");
							// totalPrice=totalPrice+Double.parseDouble(price);
							// totalPrice=Double.parseDouble(orderTotal);
							orderViewClass.add(new UserOrderViewClass(price,
									product_id, product_name, option_value,
									qty, option_id, sku, size, quantityTot));
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
					listOfOrders.setAdapter(new OrderItems(getBaseContext(),
							orderViewClass));
					float subtotal = Float.parseFloat(orderTotal)
							+ Float.parseFloat(tip);

					// textForSubTotalField.setText("$" + subtotal);
					textForSubTotalField.setText("$"
							+ String.format("%.2f", subtotal));
					textForTaxField.setText("$" + tax);
					textForServiceFeeField.setText(service_fee);
					textForTipField.setText("$" + tip);
					if (discount.contains("-")) {
						textForDiscountField.setText(discount
								.replace("-", "-$"));
					}

				} else {
					Utils.ShowAlert(MyAccountOrderActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class AddCart extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String spinnerTxt, qty, product_id, option_id, option_value,
				status = "", error = "";
		JSONObject jsonObj;

		public AddCart(String spinnerValue, String quantity,
				String product_id1, String option_id1, String option_value1) {
			// TODO Auto-generated constructor stub
			spinnerTxt = spinnerValue;
			qty = quantity;
			product_id = product_id1;
			option_id = option_id1;
			option_value = option_value1;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(MyAccountOrderActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				Log.i("product_id before", product_id);
				Log.i("quantity before", qty);
				Log.i("option_id before", option_id);
				Log.i("value_id before", option_value);
				Log.i("store_id before", storeID);

				product_id = product_id.replace("null", "").replace(",", "");
				qty = qty.replace("null,", "").replace(",", "");
				option_id = option_id.replace("null,", "");
				option_value = option_value.replace("null,", "");

				Log.i("product_id after replace", product_id);
				Log.i("quantity after replace", qty);
				Log.i("option_id after replace", option_id);
				Log.i("value_id after replace", option_value);
				Log.i("store_id after replace", storeID);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("product_id",
						product_id));
				nameValuePairs.add(new BasicNameValuePair("quantity", qty));
				nameValuePairs.add(new BasicNameValuePair("option_id",
						option_id));
				nameValuePairs.add(new BasicNameValuePair("value_id",
						option_value));
				nameValuePairs.add(new BasicNameValuePair("store_id", storeID));

				jsonObj = new ServerResponse(UrlGenerator.addCart())
						.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
				Log.e("jsonObj", jsonObj + "");
				status = jsonObj.getString("status");
				if (!status.equals("1")) {
					error = jsonObj.getString("Error");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			try {
				status = jsonObj.getString("status");
				if (status.equals("1")) {
					// orderViewClass.add(new UserOrderViewClass(price,
					// product_id, product_name, option_value,
					// qty, option_id, sku, size));
					// orderViewClass
					for (int i = 0; i < orderViewClass.size(); i++) {

						if (product_id.equals(orderViewClass.get(i).product_id)) {
							String values = orderViewClass.get(i).qty
									+ " pack of " + orderViewClass.get(i).size;

							HashMap<String, String> hasValue = new HashMap<String, String>();
							// hasValue.put("totalQty", "null");
							hasValue.put("totalQty",
									orderViewClass.get(i).quantityNew); // added
																		// on
																		// 6th
																		// december

							hasValue.put("itemId",
									orderViewClass.get(i).product_id);
							hasValue.put("itemName",
									orderViewClass.get(i).product_name);
							hasValue.put("size", orderViewClass.get(i).size);
							hasValue.put("optionId",
									orderViewClass.get(i).option_id);
							hasValue.put("sizeId",
									orderViewClass.get(i).option_value);
							hasValue.put("qty", orderViewClass.get(i).qty);
							hasValue.put("price", orderViewClass.get(i).price);
							hasValue.put("desc", values);
							hasValue.put("initialPrice",
									orderViewClass.get(i).price);
							Constant.cartArray.add(hasValue);

							db.open();
							db.insertToCart(orderViewClass.get(i).qty,
									orderViewClass.get(i).size, values,
									orderViewClass.get(i).price,
									orderViewClass.get(i).price,
									orderViewClass.get(i).product_id,
									orderViewClass.get(i).option_id, "",
									"null", orderViewClass.get(i).product_name);
							db.close();
						}
					}

					Utils.ShowAlert(MyAccountOrderActivity.this,
							"Item added to Cart");
					// editTxtForQty.setText("");
					startActivity(new Intent(MyAccountOrderActivity.this,
							CartActivity.class));
				} else {
					if (error.contains("Call")) {
						Utils.ShowAlert(MyAccountOrderActivity.this,
								"There is problem for adding to cart!");
					} else {
						Utils.ShowAlert(MyAccountOrderActivity.this, error);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
