package com.eliqxir;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DeliveryTipsActivity extends SlidingMenuActivity implements
		OnClickListener {

	TextView label_subtotal, label_serviceFee, label_tip, label_taxes,
			label_total, value_subtotal, value_serviceFee, value_tip,
			value_taxes, value_total, textForHeader, promo_value,
			txtTipPercent;
	Button btn_addPromo, btn_confirm, btn_tip;
	ImageButton backImg, btnSlideMenu, cartBtn;
	String productId, quantities, optionId, valueId, img_name = "", totPrice,
			state, city, country, zip, customerId, address = "",
			firstName = "", lastName = "", dob = "", shippingAddress,
			billingName, billingLName, shippingLName, shippingName,
			shippingCity, shippingState, shippingCountry, shippingZip,
			yearOfBirth, monthOfBirth, dateOfBirth, address3, cardType,notes_Value,
			expYear, phone, cc_id, delivery_date, delivery_time, cc_no,
			expMonth, code = "noPromo", discount, isPromoValid = "notValid",
			stordID;
	SharedPreferences sharedpreferences;
	SharedPreferences customerPreference;
	EditText promoCode;
	byte[] byteArray;
	int monthInt;
	// double priceAfterdetection;
	String deliveryLattitude, deliveryLongitude, dayOfTheWeek, currentTime;
	ArrayList<String> listCouponName = new ArrayList<String>();
	ArrayList<String> listDiscountAmount = new ArrayList<String>();
	ArrayList<String> listCouponType = new ArrayList<String>();
	RelativeLayout tip_layout;
	int serviceTax = 5;
	double tax = 2,total_Tax;

	@Override
	public void onBackPressed() {
		// Write your code here
		Constant.tipPosition = 0;
		super.onBackPressed();
	}

	double discountAmount;

	public void onResume() {
		super.onResume();

		Log.e("Constant.tipsAmount", Constant.tipsAmount + "");
		Log.e("DiscountAmount", discountAmount + "");
		Log.e("ServiceTax", serviceTax + "");
		Log.e("tax Value", tax + "");
		if (discountAmount > Double.parseDouble(value_subtotal.getText().toString().replace("$", ""))) {		
			double totalCalculatedAmount = Constant.tipsAmount + serviceTax	+ tax;
			Log.e("@@@@@@@@@", ""+totalCalculatedAmount);
			value_total.setText("$"+ String.format("%.2f", totalCalculatedAmount));
			value_tip.setText("$" + String.format("%.2f", Constant.tipsAmount));			
		} else {
			double totalCalculatedAmount = Double.parseDouble(value_subtotal.getText().toString().replace("$", ""))
					+ Constant.tipsAmount + serviceTax + tax - discountAmount;
			Log.e("TotalCalculatedAmount Value 444", ""+ totalCalculatedAmount);
			value_total.setText("$"+ String.format("%.2f", totalCalculatedAmount));
			value_tip.setText("$" + String.format("%.2f", Constant.tipsAmount));		
			Log.e("@@@@@@@@@", "" + Constant.tipsPercent);
			double tipsValueAvilable = (Constant.tipsPercent);
	//		Log.e("Tips Total Value 5555555555555544444444", ""+ tipsValueAvilable);
		
			if (tipsValueAvilable > 0) {			
	//			Log.e("vvvvvvvv", "nnnnnnnnnnnnnn");	
			} else {			
	//			Log.e("wwwwwwwwwww", "eeeeeeeeeeee");	
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.delivery_tip);
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();
		dayOfTheWeek = sdf.format(d);
		Log.e("dayOfTheWeek", dayOfTheWeek);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

		String cur_date = dateFormat.format(new Date());
		Log.v("cur date", cur_date);
		String sp[] = cur_date.split(" ");

		currentTime = sp[1] + sp[2];
		Log.e("current time", currentTime);
		customerPreference = DeliveryTipsActivity.this.getSharedPreferences(
				"customerPrefs", Context.MODE_PRIVATE);
		stordID = customerPreference.getString("store_id", "");
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		tip_layout = (RelativeLayout) findViewById(R.id.tip_layout);
		tip_layout.setOnClickListener(this);
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		productId = getIntent().getExtras().getString("prdtId");
		quantities = getIntent().getExtras().getString("quantity");
		optionId = getIntent().getExtras().getString("optionId");
		valueId = getIntent().getExtras().getString("valueId");
		img_name = getIntent().getExtras().getString("img_name");

		totPrice = getIntent().getExtras().getString("tot_price");
		address = getIntent().getExtras().getString("address");
		state = getIntent().getExtras().getString("state");
		city = getIntent().getExtras().getString("city");
		country = getIntent().getExtras().getString("country");
		zip = getIntent().getExtras().getString("zip_code");

		billingName = getIntent().getExtras().getString("billing_fname");
		billingLName = getIntent().getExtras().getString("billing_lname");

		shippingName = getIntent().getExtras().getString("shipping_fname");
		shippingLName = getIntent().getExtras().getString("shipping_lname");

		shippingAddress = getIntent().getExtras().getString("shipping_address");
		shippingState = getIntent().getExtras().getString("shipping_state");
		shippingCity = getIntent().getExtras().getString("shipping_city");
		shippingCountry = getIntent().getExtras().getString("shipping_country");
		shippingZip = getIntent().getExtras().getString("shipping_zip_code");
		String taxString = customerPreference.getString("tax", "0");
		tax = Double.parseDouble(taxString);
		System.out.println("tax-->First" + tax);
		total_Tax=tax;
		Log.e("Tax value>>>>>>>>>>",""+total_Tax);
		phone = getIntent().getExtras().getString("phone");
		cc_id = getIntent().getExtras().getString("cc_id");
		delivery_date = getIntent().getExtras().getString("delivery_date");
		delivery_time = getIntent().getExtras().getString("delivery_time");
		cc_no = getIntent().getExtras().getString("cc_number");
		cardType = getIntent().getExtras().getString("cardType");
		expYear = getIntent().getExtras().getString("expYear");
		expMonth = getIntent().getExtras().getString("expMonth");
		byteArray = getIntent().getByteArrayExtra("image");
		customerId = sharedpreferences.getString("userId", null);
		firstName = getIntent().getExtras().getString("fname");
		lastName = getIntent().getExtras().getString("lname");
		notes_Value= getIntent().getExtras().getString("notes");
		monthInt = Integer.parseInt(expMonth);

		System.out.println("Preference Last Name---->" + lastName);
		System.out.println("Shipping Name---->" + shippingName);
		System.out.println("Billing Name ---->" + billingName);
		System.out.println("Shipping Last Name---->" + shippingLName);
		System.out.println("Billing Last Name ---->" + billingLName);

		if (shippingLName != null) {
			// shippingLName=" ";
		} else {
			shippingLName = "nolastname";
		}

		if (billingLName != null) {
			// billingLName=" ";
		} else {
			billingLName = "nolastname";
		}

		label_subtotal = (TextView) findViewById(R.id.subtotal_label);
		promo_value = (TextView) findViewById(R.id.promo_value);
		label_serviceFee = (TextView) findViewById(R.id.service_fee_label);
		label_tip = (TextView) findViewById(R.id.tip_label);
		label_taxes = (TextView) findViewById(R.id.taxes_label);
		label_total = (TextView) findViewById(R.id.total_label);
		// txtTipPercent = (TextView) findViewById(R.id.txtTipPercent);
		value_subtotal = (TextView) findViewById(R.id.subtotal_value);
		value_serviceFee = (TextView) findViewById(R.id.service_fee_value);
		value_tip = (TextView) findViewById(R.id.tip_value);
		value_taxes = (TextView) findViewById(R.id.taxes_value);
		// double taxValue = (double) tax;
		System.out.println("totPrice-->" + totPrice);
		System.out.println("tax-->" + tax);
		
		tax = calculateTax();
		System.out.println("Calculated Tax------>");
		System.out.println("tax-->" + tax);
		value_taxes.setText("$" + String.format("%.2f", tax));
		value_total = (TextView) findViewById(R.id.total_value);

		btn_addPromo = (Button) findViewById(R.id.btn_add_a_promo_code);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_tip = (Button) findViewById(R.id.btn_tip);

		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		backImg.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		textForHeader.setText("DELIVERY & TIP");
		totPrice = totPrice.replace("$", "");
		double t = (Double.parseDouble(totPrice) * 5) / 100;
		Log.e("Double.parseDouble(totPrice)*5", t + "");

		double priceAfterdetection = Double.parseDouble(totPrice)+ (serviceTax + tax);
		Log.e("priceAfterdetection", priceAfterdetection + "");
		value_subtotal.setText("$" + totPrice);
		value_total.setText("$" + String.format("%.2f", priceAfterdetection));
		btn_addPromo.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		btn_tip.setOnClickListener(this);
	}

	private double calculateBase() {
		// Subtotal-discount+service fees
		double baseValue = 0;
		try {

			String sub_total_String = totPrice;
			if (sub_total_String.contains("$")) {
				sub_total_String = sub_total_String.substring(totPrice
						.indexOf("$") + 1);
			}
			double sub_total = Double.parseDouble(sub_total_String);
			Log.e("sub_total", "" + sub_total);
			String discountString = promo_value.getText().toString().trim();
			discountString = discountString.substring(discountString
					.indexOf("$") + 1);
			double discount = Double.parseDouble(discountString);
			Log.e("discount", "" + discount);
			baseValue = 0;
			if (sub_total > discount) {
				baseValue = sub_total - discount;
			}
	//		baseValue = baseValue + 5;
			Log.e("baseValue", "" + baseValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baseValue;
	}

	private double calculateTax() {
		double taxValue = 0;
		// Subtotal-discount+service fees
		try {
			String sub_total_String = totPrice;
			if (sub_total_String.contains("$")) {
				sub_total_String = sub_total_String.substring(totPrice
						.indexOf("$") + 1);
			}
			double sub_total = Double.parseDouble(sub_total_String);
			Log.e("sub_total", "" + sub_total);
			String discountString = promo_value.getText().toString().trim();
			discountString = discountString.substring(discountString
					.indexOf("$") + 1);
			double discount = Double.parseDouble(discountString);
			Log.e("discount", "" + discount);

			double baseValue = 0;
			if (sub_total > discount) {
				baseValue = sub_total - discount;
			}
	//		baseValue = baseValue + 5;
			Log.e("baseValue", "" + baseValue);

			String taxString = customerPreference.getString("tax", "0");
			double taxpercent = Double.parseDouble(taxString);
			Log.e("taxpercent", "" + taxpercent + " %");
			taxValue = taxpercent / 100;
			taxValue = taxValue * baseValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return taxValue;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_add_a_promo_code:			
			showPromo("getPromo","");
			break;
		case R.id.btn_confirm:			
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new OrderConfirm().execute();				
			} else {
				Utils.ShowAlert(DeliveryTipsActivity.this,
						Constant.networkDisconected);
			}
			break;
		/*case R.id.tip_layout:
			Intent tipIntent = new Intent(DeliveryTipsActivity.this,
					DeliveryTipsTotalActivity.class);
			tipIntent.putExtra("total", String.valueOf(calculateBase()));
			tipIntent.putExtra("tip_position", Constant.tipPosition);
			startActivity(tipIntent);
			break;*/
		case R.id.btn_tip:
			// Toast.makeText(DeliveryTipsActivity.this, "In Progress.",
			// Toast.LENGTH_LONG);
			/*Intent tipIntent1 = new Intent(DeliveryTipsActivity.this,
					DeliveryTipsTotalActivity.class);*/
			Intent tipIntent1 = new Intent(DeliveryTipsActivity.this,
					DeliveryTotaltip.class);
			tipIntent1.putExtra("tip_position", Constant.tipPosition);
			tipIntent1.putExtra("total", String.valueOf(calculateBase()));
			startActivity(tipIntent1);
			break;
		case R.id.backBtn:			
			Constant.tipsAmount = 0;
			Constant.tipPosition = 0;
			Constant.manualTipAmount = "$0.00";
			finish();
			break;
		}
	}

	public class OrderConfirm extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error, orderId, deliveryDate, deliveryTime;
		JSONObject jsonObj;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(DeliveryTipsActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			String replaceQuantity = quantities.replace("null,", "");
			String replaceProductId = productId.replace("null,", "");
			String replaceOptionId = optionId.replace("null,", "");
			String replaceValueId = valueId.replace("null,", "");
			Log.i("quantities are//////////", "" + replaceQuantity);
			Log.i("product id's are//////////", "" + replaceProductId);
			Log.i("option id's are//////////", "" + replaceOptionId);
			Log.i("value id's are//////////", "" + replaceValueId);

			Log.i("city", "" + city);
			Log.i("state", "" + state);
			Log.i("country", "" + country);
			Log.i("zip", "" + zip);

			String jsonResponse = null;
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(UrlGenerator.createOrder());

				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				ByteArrayBody byte_arr_body = new ByteArrayBody(byteArray,
						img_name + ".png");

				entity.addPart("sign", byte_arr_body);
				entity.addPart("customer_id", new StringBody(customerId));
				entity.addPart("firstname", new StringBody(firstName));
				entity.addPart("lastname", new StringBody(lastName));
				entity.addPart("store_id", new StringBody(stordID));

//				System.out.println("Shipping Name@@@@@@" + shippingName);
//				System.out.println("Billing Name @@@@@@" + billingName);
//				System.out.println("Shipping Last Name@@@@@" + shippingLName);
//				System.out.println("Billing Last Name @@@@@" + billingLName);

				// dynamic address values
				entity.addPart("billing_firstname", new StringBody(billingName));
				entity.addPart("billing_lastname", new StringBody(billingLName));

				entity.addPart("shipping_firstname", new StringBody(
						shippingName));
				entity.addPart("shipping_lastname", new StringBody(
						shippingLName));

				entity.addPart("address", new StringBody(shippingAddress));
				entity.addPart("city", new StringBody(shippingCity));
				entity.addPart("state", new StringBody(shippingState));
				entity.addPart("country", new StringBody(shippingCountry));
				entity.addPart("zipcode", new StringBody(shippingZip));

				entity.addPart("shipping_address", new StringBody(address));
				entity.addPart("shipping_state", new StringBody(state));
				entity.addPart("shipping_city", new StringBody(city));
				entity.addPart("shipping_zipcode", new StringBody(zip));
				entity.addPart("shipping_country", new StringBody(country));
				// End of dynamic address values

				entity.addPart("phone", new StringBody(phone));
				entity.addPart("product_id", new StringBody(replaceProductId));
				entity.addPart("quantity", new StringBody(replaceQuantity));
				entity.addPart("option_id", new StringBody(replaceOptionId));

				entity.addPart("value_id", new StringBody(replaceValueId));
				entity.addPart("cc_id", new StringBody(cc_id));
				entity.addPart("delivery_date", new StringBody(delivery_date));
				entity.addPart("delivery_time", new StringBody(delivery_time));
				entity.addPart("cc_number", new StringBody(cc_no));

				String tip = String.valueOf(Constant.tipsAmount);		
				Log.e("Tip Value in delivery tip page",tip);
				entity.addPart("delivery_tip", new StringBody(tip));

				entity.addPart("cc_type", new StringBody(cardType));
				entity.addPart("cc_exp_year", new StringBody(expYear));

				entity.addPart("cc_exp_month",
						new StringBody(Integer.toString(monthInt)));

				if (!code.equals("noPromo")) {
					entity.addPart("coupon_code", new StringBody(code));
				}

				entity.addPart("current_time", new StringBody(currentTime));
				entity.addPart("current_day", new StringBody(dayOfTheWeek));
				Log.e("Notes Value from checkout page",notes_Value);
				entity.addPart("customer_notes", new StringBody(notes_Value));
				httpPost.setEntity(entity);

				Log.e("entity", entity + "");
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				Log.v("response >>>>>>>>", response + "");
				HttpEntity httpEntity = response.getEntity();
				jsonResponse = EntityUtils.toString(httpEntity);
				Log.v("checkout response", jsonResponse);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// JSONObject jsonObj = new
			// ServerResponse(UrlGenerator.createOrder()).getJSONObjectfromURL(RequestType.POST,
			// nameValuePairs);
			try {
				if (jsonResponse != null) {
					jsonObj = new JSONObject(jsonResponse);

					if (jsonObj != null) {
						status = jsonObj.getString("status");
						if (status.equals("1")) {
							orderId = jsonObj.getString("order_id");
							deliveryDate = jsonObj.getString("delivery_date");
							deliveryTime = jsonObj.getString("delivery_time");
						}
						if (status.equals("0")) {
							error = jsonObj.getString("Error");
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			try {
				if (status.equals("1")) {
					Constant.cartArray.clear();
					String addressToDeliver = address;
					addressToDeliver = addressToDeliver + "," + "\n" + city
							+ "," + state + ",";

					addressToDeliver = addressToDeliver + "\n" + country + ","
							+ zip + ".";
					// addressToDeliver = addressToDeliver + " " + zip;
				/*	Intent intentToDeliver = new Intent(
							DeliveryTipsActivity.this,
							OrderPlacedActivity.class);*/
					Intent intentToDeliver = new Intent(
							DeliveryTipsActivity.this,
							OrderActivity.class);
					Bundle b = new Bundle();
					b.putString("delivery_name", shippingName);
					b.putString("delivery_last_name", shippingLName);
					b.putString("delivery_address", addressToDeliver);
					b.putString("order_id", orderId);
					b.putString("delivery_date", deliveryDate);
					b.putString("delivery_time", deliveryTime);

					intentToDeliver.putExtras(b);
					startActivity(intentToDeliver);
					Constant.tipsAmount = 0;
					Constant.tipPosition = 0;
					Constant.manualTipAmount = "$0.00";
					finish();
				} else {
					Utils.ShowAlert(DeliveryTipsActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class CheckPromoCode extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error;
		JSONObject jsonObj;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(DeliveryTipsActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			try {
				jsonObj = new ServerResponse(
						UrlGenerator.customerPromocodeList())
						.getJSONObjectfromURL(RequestType.GET, null);
				Log.e(" Promo code Response",""+jsonObj);
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("1")) {
						listCouponType.clear();
						listCouponName.clear();
						listDiscountAmount.clear();
						String couponInfo = jsonObj.getString("Coupon_info");
						JSONArray jarr = new JSONArray(couponInfo);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job1 = jarr.getJSONObject(i);
							String discount_amount = job1
									.getString("discount_amount");
							String coupon_type = job1.getString("coupon_type");
							String coupon_code = job1.getString("coupon_code");
							listCouponType.add(coupon_type);
							listCouponName.add(coupon_code);
							listDiscountAmount.add(discount_amount);
						}
					}
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			try {
				if (status.equals("1")) {
					if (listCouponName.size() != 0) {
						for (int i = 0; i < listCouponName.size(); i++) {
							if (listCouponName.get(i)	.trim().equals(promoCode.getText().toString().trim())) {
								if (listCouponType.get(i).trim().equals("1")) {
									Log.e("inside coupon type 11111111", "1");
									discount = String.format("%.2f", Double
											.parseDouble(listDiscountAmount
													.get(i)));
									Log.e("discount 111111111", discount);
									discountAmount = Double
											.parseDouble(discount);
									if (Double.parseDouble(discount) < Double
											.parseDouble(totPrice)) {
										if (Double.parseDouble(discount) > 0) {
											promo_value
													.setText("-$" + discount);
										} else {
											promo_value.setText("$" + discount);
										}
										double discountAmountCalc = Double
												.parseDouble(discount);
										Log.e("discountAmountCalc1 11111",
												discountAmountCalc + "");
										value_subtotal.getText().toString()
												.replace("$", "");
										double detectedAmount = Double
												.parseDouble(totPrice)
												- discountAmountCalc;
										// if (detectedAmount <= 0) {
										Log.e("Constant.tipsAmount 1111111", ""
												+ Constant.tipsAmount);
										double taxesAlone = serviceTax + tax
												+ Constant.tipsAmount
												+ detectedAmount;
										Log.e("Promo If cond TaxesAlone Amount 1111111",
												"" + taxesAlone);

										// Added on 18thNov
										double discountVal = Double
												.valueOf(discount);
										double deductedTotal = (Double
												.parseDouble(totPrice.replace(
														"$", "")) - discountVal);
										String taxString = customerPreference
												.getString("tax", "0");
										tax = Double.parseDouble(taxString);
										Log.e("Tax Amount inside promo", ""
												+ tax);
										// tax = (deductedTotal * tax) / 100;
										tax = calculateTax();
										value_taxes.setText("$"
												+ String.format("%.2f", tax));
										//

										value_total.setText("$"
												+ String.format("%.2f",
														taxesAlone));
									} else {
										promo_value
												.setText("-$"
														+ String.format(
																"%.2f",
																Double.parseDouble(totPrice)));
										double taxesAlone = serviceTax + tax
												+ Constant.tipsAmount;
										Log.e("Promo Else Cond TaxesAlone Amount 11111111",
												"" + taxesAlone);
										value_total.setText("$"
												+ String.format("%.2f",
														taxesAlone));
									}
									changeTipAfterPromo();
									Log.e("Before Success Dialog","2222222");
									showPromo("success","");
								} else {
									Log.e("inside coupon type 0000000", "0");
									String discountPercent = listDiscountAmount
											.get(i).replace("%", "");
									Log.e("Discount Percent 111111111111",discountPercent);
									double disc = Double
											.parseDouble(discountPercent);
									discountPercent = String.format("%.2f",
											disc);
									Log.e("Discount Percent 22222222222",""+discountPercent);
									discountAmount = (Double
											.parseDouble(totPrice.replace("$",
													"")) * Double
											.parseDouble(discountPercent)) / 100;

									Log.e("discount 000", discountAmount + "");
									if (discountAmount < Double
											.parseDouble(totPrice)) {
										if (discountAmount > 0) {
											promo_value.setText("-$"
													+ String.format("%.2f",
															discountAmount));
										} else {
											promo_value.setText("$"
													+ String.format("%.2f",
															discountAmount));
										}
										value_total.getText().toString()
												.replace("$", "");
										double detectedAmount = Double
												.parseDouble(totPrice)
												- discountAmount;
										// if (detectedAmount <= 0) {
										// value_total.setText("$0.00");
										Log.e("Service Tax Value ---------",""+serviceTax);
										Log.e("Tax Value ---------",""+tax);
										Log.e("Constant.tipsAmount Value ---------",""+Constant.tipsAmount);
										Log.e("DetectedAmount Value ---------",""+detectedAmount);
										double taxesAlone = serviceTax + tax
												+ Constant.tipsAmount
												+ detectedAmount;
										Log.e("Grand Total Value !!!!!!!!!!!!",""+taxesAlone);
										value_total.setText("$"
												+ String.format("%.2f",
														taxesAlone));
									} else {
										promo_value
												.setText("-$"
														+ String.format(
																"%.2f",
																Double.parseDouble(totPrice)));
										value_total.getText().toString()
												.replace("$", "");

//										Log.e("Service Tax Value ---------",""+serviceTax);
//										Log.e("Tax Value ---------",""+tax);
//										Log.e("Constant.tipsAmount Value ---------",""+Constant.tipsAmount);
//										Log.e("DetectedAmount Value ---------",""+detectedAmount);
										double taxesAlone = serviceTax + tax
												+ Constant.tipsAmount;
										Log.e("Grand Total Value >>>>>>>>>",""+taxesAlone);
										value_total.setText("$"
												+ String.format("%.2f",
														taxesAlone));
									}
									changeTipAfterPromo();
									Log.e("Before Success Dialog","11111");
									showPromo("success",discountPercent);
									
								}
								isPromoValid = "valid";
								break;
							} else {
								isPromoValid = "notValid";
							}
						}
					} else {
						isPromoValid = "notValid";

					}
				} else {
					Utils.ShowAlert(DeliveryTipsActivity.this, error);
				}
				if (isPromoValid.equals("notValid")) {
					Utils.ShowAlert(DeliveryTipsActivity.this,
							"Invalid promo code.");
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showPromo(String flag,String discount_amt) {

		final Dialog dialog = new Dialog(DeliveryTipsActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.promo_dailog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		// TextView promoTitle = (TextView)
		// dialog.findViewById(R.id.promo_title);
		promoCode = (EditText) dialog.findViewById(R.id.promo_code);
		Button closeButton = (Button) dialog.findViewById(R.id.promo_close);
		Button applyButton = (Button) dialog.findViewById(R.id.promo_apply);
		TextView promo_success = (TextView) dialog
				.findViewById(R.id.promo_success);
		TextView promo_percentage = (TextView) dialog
				.findViewById(R.id.promo_percentage_off);

		if (flag.equals("getPromo")) {
			promo_success.setVisibility(View.GONE);
			promo_percentage.setVisibility(View.GONE);
		} else {
			promo_percentage.setText(discount_amt+"% off Purchase");
			promoCode.setVisibility(View.GONE);			
			applyButton.setVisibility(View.GONE);
		}

		// if button is clicked, close the custom dialog
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		applyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				code = promoCode.getText().toString().trim();
				if (code.length() > 0 && code != null) {
					boolean isOnline = Utils.isOnline();
					Log.e("isOnline", isOnline + "");
					if (isOnline) {
						dialog.dismiss();
						new CheckPromoCode().execute();
					} else {
						Utils.ShowAlert(DeliveryTipsActivity.this,
								Constant.networkDisconected);
					}
				} else {
					Utils.ShowAlert(DeliveryTipsActivity.this,
							"Promo code is empty!");
				}
			}
		});
		dialog.show();
	}
	
	public void changeTipAfterPromo() {
		
		  if (Constant.tipsPercent !=0.0 &&Constant.tipsPercent>0) {
		 
		   double tip = Double.parseDouble(value_subtotal.getText().toString().replace("$", ""))
				   				  - discountAmount;
		   Log.e("Constant.tipsPercent Value",""+Constant.tipsPercent);
		   tip = (tip * Constant.tipsPercent) / 100;		   
		   Constant.tipsAmount=tip;
		   value_tip.setText("$" + String.format("%.2f", tip));
		   
		   
		   double taxes=Double.parseDouble(value_subtotal.getText().toString().replace("$", ""))
	   				 - discountAmount;
		   Log.e("Inside Total ax value",""+total_Tax);
		   taxes= (taxes * total_Tax) / 100;
		   value_taxes.setText("$" + String.format("%.2f", taxes));
		   
		   Log.e("Final Tip value ", String.format("%.2f", tip));
		   Log.e("Final Tax value ", String.format("%.2f", taxes));
		   
			double grandtotal = Double.parseDouble(value_subtotal.getText().toString().replace("$", ""))+serviceTax + taxes
					+ Double.parseDouble(String.format("%.2f", tip))
					- discountAmount;
			Log.e("Total Value 1111111111122222",""+grandtotal);
			value_total.setText("$"
					+ String.format("%.2f",
							grandtotal));
			tax=taxes;
		  }
		 }
}
