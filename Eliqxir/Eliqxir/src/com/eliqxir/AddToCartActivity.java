package com.eliqxir;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.adapter.TabHostFragmentAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class AddToCartActivity extends SlidingMenuActivity implements
		OnClickListener {

	ImageButton btnForBack, btnForCart, btnSlideMenu;
	RelativeLayout layHeaderBack, layHeaderCart, layHeaderSlider;

	Spinner sizeSpinner, quantitySpinner;
	ImageButton addToCartBtn;
	String itemId, itemPrice, itemName, itemDesc, quantityTotal, selected_tab,store_status,
			optionId, spinnerId, sku, storeID,delivery_status;
	TextView textForPrice, textForName, textForDesc, txtForHeader, textForSku,
			cartCountTxt;
	// EditText editTxtForQty;
	ArrayList<String> sizeArrayList = new ArrayList<String>();
	ArrayList<String> sizeIdList = new ArrayList<String>();
	ArrayList<String> skuArrayList = new ArrayList<String>();
	ArrayList<String> priceArrayList = new ArrayList<String>();
	SharedPreferences sharedpreferences, vendorSharedpreferences,customerPreference,storeclosedPreference;
	Editor editor,customerPrefEditor;
	ArrayList<String> countArray = new ArrayList<String>();
	DBAdapter db;
	RelativeLayout layCartCount;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			startActivity(new Intent(AddToCartActivity.this,
					TabsFragmentActivity.class));
		}
		return false;

	}

	public void onResume() {
		if (Constant.cartArray.size() > 0) {
			layCartCount.setVisibility(View.VISIBLE);
			cartCountTxt.setText(Constant.cartArray.size() + "");
		} else {
			layCartCount.setVisibility(View.GONE);
		}

		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.add_to_cart);
		vendorSharedpreferences = getSharedPreferences("vendorPrefs",
				Context.MODE_PRIVATE);
		storeID = vendorSharedpreferences.getString("store_id", "");
		customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		/*		customerPrefEditor=customerPreference.edit();
		storeID = customerPreference.getString("store_id", "");*/
		Log.e("stordID in Add to cart page", storeID);
		
		//Added on 24th April
		delivery_status = customerPreference.getString("delivery", "");		
		Log.e("Delivery_status in Add to cart page", delivery_status);
		//
		 storeclosedPreference=getSharedPreferences("storeclosedPrefs", Context.MODE_PRIVATE);
		 store_status=storeclosedPreference.getString("storeval", "");		
		
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		selected_tab = sharedpreferences.getString("Selected-Tab", " ");
		itemId = getIntent().getExtras().getString("product_id");
		itemPrice = getIntent().getExtras().getString("price");
		itemName = getIntent().getExtras().getString("name");
		itemDesc = getIntent().getExtras().getString("desc");
		sku = getIntent().getExtras().getString("sku");
		// tax=getIntent().getExtras().getString("tax");
		// Log.e("sku",sku);
		Log.i("itemId is::;;;;;", "" + itemId);
		layCartCount = (RelativeLayout) findViewById(R.id.layCartCount);
		sizeSpinner = (Spinner) findViewById(R.id.spinnerForSize);
		quantitySpinner = (Spinner) findViewById(R.id.spinnerForQuantity);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		txtForHeader = (TextView) findViewById(R.id.textForHeader);
		txtForHeader.setText(selected_tab);
		addToCartBtn = (ImageButton) findViewById(R.id.addToCartBtn);
		textForPrice = (TextView) findViewById(R.id.textForAddCartPriceValue);
//		textForSku = (TextView) findViewById(R.id.textForAddCartskuValue);
		textForName = (TextView) findViewById(R.id.textForAddCartItemName);
		textForDesc = (TextView) findViewById(R.id.textForAddCartItemNote);
		layHeaderBack = (RelativeLayout) findViewById(R.id.layHeaderBack);
		layHeaderCart = (RelativeLayout) findViewById(R.id.layHeaderCart);
		layHeaderSlider = (RelativeLayout) findViewById(R.id.layHeaderslider);
		cartCountTxt = (TextView) findViewById(R.id.cartCountTxt);
		layHeaderBack.setOnClickListener(this);
		layHeaderCart.setOnClickListener(this);
		layHeaderSlider.setOnClickListener(this);
		// editTxtForQty = (EditText) findViewById(R.id.editTxtForQuantity);
		btnForCart = (ImageButton) findViewById(R.id.cartBtn);
		btnForBack.setOnClickListener(this);
		addToCartBtn.setOnClickListener(this);
		btnForCart.setOnClickListener(this);

		btnSlideMenu.setVisibility(View.GONE);
		btnForBack.setVisibility(View.VISIBLE);

		db = new DBAdapter(getBaseContext());

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		// textForPrice.setText("$" + itemPrice);
		textForName.setText(itemName);
		textForDesc.setText(itemDesc);
		
		//Added on 24th April
		if(delivery_status.equals("1")){
			addToCartBtn.setVisibility(View.VISIBLE);
		}else{
			addToCartBtn.setVisibility(View.GONE);
		}
		//
		
		// textForSku.setText(sku);
		sizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				// your code here
				Log.e("skuArrayList.get(position)", skuArrayList.get(position));
				/*if (skuArrayList.get(position).equals("null")) {
				textForSku.setText(sku);
				} else {
					textForSku.setText(skuArrayList.get(position));
				}*/
				textForPrice.setText("$" + priceArrayList.get(position));
				itemPrice = priceArrayList.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
		for (int i = 1; i < 51; i++) {
			int count = i;
			String countVal = Integer.toString(count);
			countArray.add(countVal);
		}

		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new AsyncTask<Void, Void, Boolean>() {
				ProgressDialog dialog;

				@Override
				public void onPreExecute() {
					super.onPreExecute();
					this.dialog = new ProgressDialog(AddToCartActivity.this);
					this.dialog.setMessage("Loading..");
					this.dialog.show();
					this.dialog.setCancelable(false);
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);
					Log.e("itemid", itemId);
					nameValuePairs.add(new BasicNameValuePair("product_id",
							itemId));
					JSONObject jsonObj = new ServerResponse(
							UrlGenerator.getItemView()).getJSONObjectfromURL(
							RequestType.POST, nameValuePairs);
					Log.e("item view response", jsonObj + "");
					try {
						if (jsonObj != null) {
							String status = jsonObj.getString("status");
							if (status.equals("1")) {
								String basePrice = jsonObj.getString("price");
								Log.e("base price", basePrice);
								if(basePrice.contains(",")){
									basePrice=basePrice.replaceAll(",","");
								}
								Log.e("base price 11", basePrice);
								sizeArrayList.clear();
								sizeIdList.clear();
								skuArrayList.clear();
								priceArrayList.clear();

								JSONArray qtyArray = jsonObj
										.getJSONArray("quantity");
								for (int i = 0; i < qtyArray.length(); i++) {
									quantityTotal = qtyArray.getJSONObject(i)
											.getString("qty");
								}
								JSONArray optIdArray = jsonObj
										.getJSONArray("option_id");
								for (int i = 0; i < optIdArray.length(); i++) {
									optionId = optIdArray.getJSONObject(i)
											.getString("option_id");
								}
								JSONArray sizeArray = jsonObj
										.getJSONArray("Size");
								for (int i = 0; i < sizeArray.length(); i++) {
									String sizeValues = sizeArray
											.getJSONObject(i)
											.getString("title");
									String sizeId = sizeArray.getJSONObject(i)
											.getString("value_id");
									String sku = sizeArray.getJSONObject(i)
											.getString("sku");
									skuArrayList.add(sku);
									String priceVal = sizeArray
											.getJSONObject(i)
											.getString("price");
									double priceDouble = Double
											.parseDouble(priceVal)
											+ Double.parseDouble(basePrice);
									String priceConvert = String.format("%.2f",
											priceDouble);

									priceArrayList.add(priceConvert);
									sizeArrayList.add(sizeValues);
									sizeIdList.add(sizeId);
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
					// textForSku.setText(skuArrayList.get(0));
					if (sizeArrayList.size() > 0) {
						Log.e("skuArrayList.get(0)", skuArrayList.get(0));
						/*if (skuArrayList.get(0).equals("null")) {
							textForSku.setText(sku);
						} else {
							textForSku.setText(skuArrayList.get(0));
						}*/
						sizeSpinner.setAdapter(new SpinnerAdapter(
								AddToCartActivity.this,
								R.layout.custom_spinner, sizeArrayList));
						quantitySpinner.setAdapter(new SpinnerAdapter(
								AddToCartActivity.this,
								R.layout.custom_spinner, countArray));
					} else {
						/*Utils.ShowAlert(AddToCartActivity.this,
								"Size is not available");*/
						AlertDialog.Builder adb = new AlertDialog.Builder(AddToCartActivity.this);
						adb.setMessage("Size is not available.");
						adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {				
								dialog.cancel();
							//	finish();
								Intent my =new Intent(AddToCartActivity.this,TabsFragmentActivity.class);
								startActivity(my);
							}
						});
						AlertDialog alert = adb.create();
						alert.show();	
					}
				}
			}.execute();

		} else {
			Utils.ShowAlert(AddToCartActivity.this, Constant.networkDisconected);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.layHeaderBack) {
			finish();
			startActivity(new Intent(AddToCartActivity.this,
					TabsFragmentActivity.class));
		}
		if (v.getId() == R.id.layHeaderCart) {
			Log.e("Delivery_status in Add to cart activity 111", delivery_status);
			Log.e("Store_status in Add to cart activity 111", store_status);
			if(delivery_status.equals("0")||store_status.equals("storeclosed")){
				layCartCount.setVisibility(View.GONE);
				db.open();			   
				db.deleteRows();
				db.close();
				Utils.ShowAlert(AddToCartActivity.this,
						"You have no items in your shopping cart.");
			}else{
			if (Constant.cartArray.size() > 0) {
				startActivity(new Intent(AddToCartActivity.this,
						CartActivity.class));
			} else {
				Utils.ShowAlert(AddToCartActivity.this,
						"You have no items in your shopping cart.");
			}
			}
		}
		if (v.getId() == R.id.backBtn) {
			finish();
		}
		if (v.getId() == R.id.cartBtn) {
			Log.e("Delivery_status in Add to cart activity 3344", delivery_status);
			Log.e("Store_status in Add to cart activity 3344", store_status);
			Utils.timeDiff(getBaseContext(), db);
			if(delivery_status.equals("0")||store_status.equals("storeclosed")){
				layCartCount.setVisibility(View.GONE);
			   db.open();			   
			   db.deleteRows();
			   db.close();
				Utils.ShowAlert(AddToCartActivity.this,
						"You have no items in your shopping cart.");
			}else{			
			if (Constant.cartArray.size() > 0) {
				startActivity(new Intent(AddToCartActivity.this,
						CartActivity.class));
			} else {
				Utils.ShowAlert(AddToCartActivity.this,
						"You have no items in your shopping cart.");
			}
		  }
		}
		if (v.getId() == R.id.addToCartBtn) {
			if (sizeArrayList.size() > 0) {
				String spinnerValue = sizeSpinner.getSelectedItem().toString();
				int position = sizeSpinner.getSelectedItemPosition();
				spinnerId = sizeIdList.get(position).toString();
				Log.i("spoinner value:::::", "" + spinnerValue);
				Log.i("spinnerId value:::::", "" + spinnerId);
				String qty = quantitySpinner.getSelectedItem().toString();
				if (qty.length() > 0) {
					try {
						double quantity = Double.parseDouble(qty);
						double totalQty = Double.parseDouble(quantityTotal);
						if (quantity <= totalQty) {

							new AddCart(spinnerValue, quantity).execute();

						} else {
							/*Utils.ShowAlert(AddToCartActivity.this,
									"Quantity is not available");*/
							Utils.ShowAlert(AddToCartActivity.this,
									"Out of stock");
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					Utils.ShowAlert(AddToCartActivity.this,
							"Quantity field must not be empty");
				}
			} else {
				Utils.ShowAlert(AddToCartActivity.this, "Size is not available");
			}
		}

		/*
		 * if (v.getId() == R.id.cartBtn) { Intent intentToLocation = new
		 * Intent(AddToCartActivity.this, LocationWindow.class);
		 * startActivity(intentToLocation); }
		 */
	}

	private void addToCart() {

		String spinnerValue = sizeSpinner.getSelectedItem().toString();
		int position = sizeSpinner.getSelectedItemPosition();
		spinnerId = sizeIdList.get(position).toString();
		Log.i("spoinner value:::::", "" + spinnerValue);
		Log.i("spinnerId value:::::", "" + spinnerId);
		String qty = quantitySpinner.getSelectedItem().toString();

		double quantity = Double.parseDouble(qty);
		double totalQty = Double.parseDouble(quantityTotal);

		Utils.timeDiff(getBaseContext(), db);
		if (Constant.cartArray.size() > 0) {
			db.open();
			boolean dbChecks = db.getCartItem(itemId, optionId, spinnerId);
			db.close();
			if (dbChecks == true) {
				Log.i("boolean", "true");
				for (int i = 0; i < Constant.cartArray.size(); i++) {
					String cartItemId = Constant.cartArray.get(i).get("itemId");
					String cartOptionId = Constant.cartArray.get(i).get(
							"optionId");
					String cartSpinnerId = Constant.cartArray.get(i).get(
							"sizeId");
					String cartQty = Constant.cartArray.get(i).get("qty");

					if ((cartItemId.equals(itemId))
							&& (cartOptionId.equals(optionId))
							&& (cartSpinnerId.equals(spinnerId))) {
						int cartQtyAdd = Integer.parseInt(qty)
								+ Integer.parseInt(cartQty);
						Log.i("cart qty added>>>>>>>>>>>", "" + cartQtyAdd);
						Constant.cartArray.remove(i);
						HashMap<String, String> hasValue = new HashMap<String, String>();
						hasValue.put("totalQty", String.valueOf(totalQty));
						hasValue.put("itemId", cartItemId);
						hasValue.put("itemName", itemName);
						hasValue.put("size", spinnerValue);
						hasValue.put("optionId", cartOptionId);
						hasValue.put("sizeId", cartSpinnerId);
						hasValue.put("initialPrice", itemPrice);
						hasValue.put("qty", String.valueOf(cartQtyAdd));
						hasValue.put(
								"price",
								String.valueOf(cartQtyAdd
										* Double.parseDouble(itemPrice)));
						// hasValue.put("tax",
						// tax);
						Constant.cartArray.add(i, hasValue);

						db.open();
						db.updateCart(
								cartItemId,
								cartOptionId,
								cartSpinnerId,
								String.valueOf(cartQtyAdd),
								String.valueOf(cartQtyAdd
										* Double.parseDouble(itemPrice)));
						db.close();

						// new AddCart(spinnerValue, quantity)
						// .execute();
					}
				}
			} else {
				Log.i("boolean", "false");

				String values = qty + " pack of " + spinnerValue;

				HashMap<String, String> hasValue = new HashMap<String, String>();
				hasValue.put("totalQty", String.valueOf(totalQty));
				hasValue.put("itemId", itemId);
				hasValue.put("itemName", itemName);
				hasValue.put("size", spinnerValue);
				hasValue.put("optionId", optionId);
				hasValue.put("sizeId", spinnerId);
				hasValue.put("qty", qty);
				hasValue.put("price", itemPrice);
				hasValue.put("desc", values);
				hasValue.put("initialPrice", itemPrice);
				// hasValue.put("tax", tax);
				Constant.cartArray.add(hasValue);

				db.open();
				db.insertToCart(qty, spinnerValue, values, itemPrice,
						itemPrice, itemId, optionId, spinnerId,
						String.valueOf(totalQty), itemName);
				db.close();

				// new AddCart(spinnerValue, quantity)
				// .execute();
			}
		} else {
			String values = qty + " pack of " + spinnerValue;

			HashMap<String, String> hasValue = new HashMap<String, String>();
			hasValue.put("totalQty", String.valueOf(totalQty));
			hasValue.put("itemId", itemId);
			hasValue.put("itemName", itemName);
			hasValue.put("size", spinnerValue);
			hasValue.put("optionId", optionId);
			hasValue.put("sizeId", spinnerId);
			hasValue.put("qty", qty);
			hasValue.put("price", itemPrice);
			hasValue.put("desc", values);
			hasValue.put("initialPrice", itemPrice);
			// hasValue.put("tax", tax);
			Constant.cartArray.add(hasValue);

			db.open();
			db.insertToCart(qty, spinnerValue, values, itemPrice, itemPrice,
					itemId, optionId, spinnerId, String.valueOf(totalQty),
					itemName);
			db.close();

			// new AddCart(spinnerValue, quantity).execute();
		}

		quantitySpinner.setAdapter(new SpinnerAdapter(AddToCartActivity.this,
				R.layout.custom_spinner, countArray));
//		Utils.ShowAlert(AddToCartActivity.this, "Item added to Cart");
		Utils.ShowAlert(AddToCartActivity.this, "Successfully added an item to the cart");
		if (Constant.cartArray.size() > 0) {
			layCartCount.setVisibility(View.VISIBLE);
			cartCountTxt.setText(Constant.cartArray.size() + "");
		} else {
			layCartCount.setVisibility(View.GONE);
		}
	}

	public class SpinnerAdapter extends ArrayAdapter<String> {

		Context mContext;
		String dobChk;
		ArrayList<String> arrayValue;

		public SpinnerAdapter(Context context, int customSpinner,
				ArrayList<String> month) {
			// TODO Auto-generated constructor stub
			super(context, customSpinner, month);
			mContext = context;
			arrayValue = month;
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View spinnerView = inflater.inflate(R.layout.custom_spinner,
					parent, false);
			TextView spinnerText = (TextView) spinnerView
					.findViewById(R.id.textForSpinnerItem);
			spinnerText.setText(arrayValue.get(position));

			return spinnerView;
		}
	}

	public class AddCart extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String spinnerTxt, qty;
		JSONObject jsonObj;
		String status = "0", error;

		public AddCart(String spinnerValue, double quantity) {
			// TODO Auto-generated constructor stub
			spinnerTxt = spinnerValue;
			qty = Double.toString(quantity);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(AddToCartActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
			String dateFrmt = format.format(c.getTime());
			SessionStore.saveCartTiming(dateFrmt, AddToCartActivity.this);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.i("Option and value id>>>>>>>>>>>>>>>>", "" + optionId
					+ " and " + spinnerId);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("product_id", itemId));
			nameValuePairs.add(new BasicNameValuePair("quantity", qty));
			nameValuePairs.add(new BasicNameValuePair("option_id", optionId));
			nameValuePairs.add(new BasicNameValuePair("value_id", spinnerId));
			nameValuePairs.add(new BasicNameValuePair("store_id", storeID));

			jsonObj = new ServerResponse(UrlGenerator.addCart())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			try {
				status = jsonObj.getString("status");
				if (status.equals("1")) {
					addToCart();

					// editTxtForQty.setText("");
					// startActivity(new
					// Intent(AddToCartActivity.this,CartActivity.class));
				} else

				{
					error = jsonObj.getString("Error");
					Utils.ShowAlert(AddToCartActivity.this, error);

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	private void clearCart() {
//		Constant.cartArray.clear();
//		DBAdapter db = new DBAdapter(AddToCartActivity.this);
//		db.open();
//		db.deleteRows();
//		db.close();
//	}
}
