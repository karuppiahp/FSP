package com.eliqxir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.database.SQLException;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.AddDriver;
import com.eliqxir.vendor.LoginVendorActivity;
import com.eliqxir.vendor.OverviewActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CartActivity extends SlidingMenuActivity implements
		OnClickListener {
	Dialog dialogbox;
	ArrayList<Integer> removeIdValue=new ArrayList<Integer>();
	LinearLayout cartLayout;
	TextView label_subtotal, value_subtotal, textForHeader,text_Message;
	Button check_out,btn_Ok;
	ImageButton backImg, btnSlideMenu, cartBtn;
	SharedPreferences sharedpreferences, zipCartValuePref;
	Editor editor;
	Dialog dialoglogin;
	boolean click = false, rememberUser;
	int id, idAdd, initialArraySize,status_Check=0;
	double subTotalAmnt, qtyAmnt;
	String totalQnty, cartId, totalQtyAvailable, productId, optionId, valueId,first_Name,
			optionIdComma, valueIdComma, itemName, cartValuefromLocation,image_name;
	CheckBox rememberMe;
	EditText et_username, et_password;
	RelativeLayout layHeaderBack, layHeaderCart, layHeaderSlider;
	DBAdapter db;
	LocationManager lm;
	ArrayList<HashMap<String, String>> cartArray = new ArrayList<HashMap<String, String>>();
	Typeface fontRegular;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			startActivity(new Intent(CartActivity.this,
					TabsFragmentActivity.class));
		}
		return false;
	}

	/*@Override
	protected void onStop() {
	    for(int i=0;i<removeIdValue.size();i++){
	    	Log.e("Remove Id Total Size",""+removeIdValue.size());
	    	Constant.cartArray.get(removeIdValue.remove(i));	   
	    }
		super.onStop();
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.cart);

		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		rememberUser = sharedpreferences.getBoolean("remember-user", false);
		Log.e("Remember User Status >>>>>>>>>",""+rememberUser);
		
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		label_subtotal = (TextView) findViewById(R.id.subtotal_label);
		value_subtotal = (TextView) findViewById(R.id.subtotal_value);
		check_out = (Button) findViewById(R.id.btn_checkout);
		cartLayout = (LinearLayout) findViewById(R.id.cart_items_layout);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		backImg.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);

		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setBackgroundResource(R.drawable.close_btn);
		textForHeader.setText("CART");
		layHeaderBack = (RelativeLayout) findViewById(R.id.layHeaderBack);
		layHeaderCart = (RelativeLayout) findViewById(R.id.layHeaderCart);
		layHeaderSlider = (RelativeLayout) findViewById(R.id.layHeaderslider);
		layHeaderBack.setOnClickListener(this);
		layHeaderCart.setOnClickListener(this);
		layHeaderSlider.setOnClickListener(this);
		check_out.setOnClickListener(this);
		
		db = new DBAdapter(getBaseContext());

		// Code to store cart value from location window when another store
		// value is given
		zipCartValuePref = getSharedPreferences("cartvalue",
				Context.MODE_PRIVATE);
		cartValuefromLocation = zipCartValuePref
				.getString("cartstorevalue", "");
		Log.e("Cart Value from location window page", cartValuefromLocation);

		Utils.timeDiff(getBaseContext(), db);

		addItems();

		/*
		 * if(cartValuefromLocation.equals("1")){ addItems(); }else{
		 * Utils.ShowAlert
		 * (CartActivity.this,"You have no items in your shopping cart."); }
		 */
	}

	int childIndex = -1;

	private void addItems() {
		cartLayout.removeAllViewsInLayout();
		/*
		 * int i = 0; while (i < 10) { i++;
		 */

		if (Constant.cartArray.size() > 0) {
			for (int i = 0; i < Constant.cartArray.size(); i++) {
				LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View rowData = inflater1
						.inflate(R.layout.cart_item, null);
				rowData.setTag(i);
				final LinearLayout layButtons = (LinearLayout) rowData
						.findViewById(R.id.layButtons);
				layButtons.setVisibility(View.GONE);

				initialArraySize = Constant.cartArray.size();
				itemName = Constant.cartArray.get(i).get("itemName");
				cartId = Constant.cartArray.get(i).get("itemId");
				totalQtyAvailable = Constant.cartArray.get(i).get("totalQty");
				Log.i("cartId ois>>>>>>>", "" + cartId);
				// tax=Constant.cartArray.get(i).get("tax");
				// Log.e("tax",tax);
				final String cartSize = Constant.cartArray.get(i).get("size");
				String cartQty = Constant.cartArray.get(i).get("qty");
				// final String cartPrice =
				// Constant.cartArray.get(i).get("price");
				final String initialCartPrice = Constant.cartArray.get(i).get(
						"initialPrice");

				optionId = Constant.cartArray.get(i).get("optionId");
				valueId = Constant.cartArray.get(i).get("sizeId");

				/*
				 * String cartPriceReplace = cartPrice.replace("$", ""); String
				 * cartPriceReplaceEnd = cartPriceReplace.replace("ea", "");
				 */
				final double cartAmnt = Double.parseDouble(initialCartPrice);
				int initialQty = Integer.parseInt(cartQty);

				/*
				 * if(productId != null) { if(!(productId.contains(cartId))) {
				 * productId = productId + "," + cartId; } } else{ productId =
				 * productId + "," + cartId; }
				 */

				productId = productId + "," + cartId;

				/*
				 * totalQnty = totalQnty + "," + cartQty;
				 * Log.i("Total qty ois>>>>>>>", "" + totalQnty);
				 */

				/*
				 * if(optionIdComma != null) {
				 * if(!(optionIdComma.contains(optionId))) { optionIdComma =
				 * optionIdComma + "," + optionId; } } else{ optionIdComma =
				 * optionIdComma + "," + optionId; }
				 */

				optionIdComma = optionIdComma + "," + optionId;
				valueIdComma = valueIdComma + "," + valueId;
				qtyAmnt = initialQty * cartAmnt;

				final TextView txtView_count = (TextView) rowData.findViewById(R.id.cart_item_count);
				final TextView txtView_brandName = (TextView) rowData.findViewById(R.id.cart_item_name);
				final TextView txtView_packDesc = (TextView) rowData.findViewById(R.id.cart_item_pack);
				final TextView txtView_itemAmount = (TextView) rowData.findViewById(R.id.cart_item_amount);
				TextView cart_X_mark = (TextView) rowData.findViewById(R.id.cart_into_mark);

				fontRegular= Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf");
				
				txtView_count.setTypeface(fontRegular);
				txtView_brandName.setTypeface(fontRegular);
				txtView_packDesc.setTypeface(fontRegular);
				txtView_itemAmount.setTypeface(fontRegular);
				cart_X_mark.setTypeface(fontRegular);
				
				txtView_count.setText(cartQty);
				txtView_brandName.setText(itemName);
				txtView_packDesc.setText(cartQty + " pack of " + cartSize);
				txtView_itemAmount.setText("$" + String.format("%.2f", qtyAmnt));

				subTotalAmnt = subTotalAmnt + qtyAmnt;
				Log.i("SubTaotal Amnt ::::::::::::", "" + subTotalAmnt);
				value_subtotal.setText("$"+ String.format("%.2f", subTotalAmnt));

				final Button addOne = (Button) rowData.findViewById(R.id.cart_btn_add);
				Button minusOne = (Button) rowData.findViewById(R.id.cart_btn_minus);
				Button delete = (Button) rowData.findViewById(R.id.cart_btn_delete);

				delete.setTag(i);
				addOne.setTag(i);
				minusOne.setTag(i);
				rowData.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
				//		Log.e("Inside Function","rowData on click");
				//		Log.i("Row data value clicked", "" + idAdd);
						try {
							if (layButtons.getVisibility() == View.VISIBLE) {
								Log.e("Inside RowData Onclick If Function",""+idAdd);
								layButtons.setVisibility(View.GONE);
								Animation leftSwipe = AnimationUtils.loadAnimation(
										CartActivity.this, R.anim.left);
								layButtons.startAnimation(leftSwipe);
								childIndex = -1;
							} else {
					//			Log.e("Inside RowData Onclick If-Else Function",""+idAdd);
								hideSwipeLayout();
								Log.e("Below HideLayout 44444444444444","55555555Function");
								childIndex = Integer
										.parseInt(v.getTag().toString());
								layButtons.setVisibility(View.VISIBLE);
								Animation RightSwipe = AnimationUtils
										.loadAnimation(CartActivity.this,
												R.anim.right);
						
								layButtons.startAnimation(RightSwipe);
							}
						} catch (Exception e) {
							Log.e("Inside Exception Message",""+e);
						} 
					}
				});
				
				addOne.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						try {
							int count = Integer.parseInt(txtView_count.getText()
									.toString());
							Log.e("Count Value inside swipe +++++++++++++++",""+count);
							++count;
							Log.e("Count Value inside After Icrement ++++++",""+count);
							Log.e("TotalQtyAvailable +++++++++++++++",""+totalQtyAvailable);
							if (count <= Double.parseDouble(totalQtyAvailable)) {
								/*
								 * totalQnty =
								 * totalQnty.replace(txtView_count.getText
								 * ().toString(), String.valueOf(count));
								 * Log.i("Total qty ois>>>>>>>",""+totalQnty);
								 */
								txtView_count.setText(String.valueOf(count));
								txtView_packDesc.setText(String.valueOf(count)
										+ " pack of " + cartSize);

								qtyAmnt = count * cartAmnt;
								txtView_itemAmount.setText("$"
										+ String.format("%.2f", qtyAmnt));
								subTotalAmnt = subTotalAmnt + cartAmnt;
								value_subtotal.setText("$"
										+ String.format("%.2f", subTotalAmnt));

								Log.i("SubTaotal Amnt Add:::::::::::::", ""
										+ subTotalAmnt);

							if (click == true) {
									int arraySize = Constant.cartArray.size();
									int arraySizeDiff = initialArraySize - arraySize - 1;
									int position = (Integer) v.getTag();
									Log.e("Position value for Plus click button",""+position);
									if (position == 0) {
										idAdd = (Integer) v.getTag();
									} else {
										idAdd = (Integer) v.getTag() - arraySizeDiff;
									}
								} else {
									Log.e("Plus click boolean Value",""+click);
									idAdd = (Integer) v.getTag();
								}
							
								Log.i("Add One id add value display::::::::", "" + idAdd);
								String pdtIdArray = Constant.cartArray.get(idAdd)
										.get("itemId");
								String optionIdArray = Constant.cartArray
										.get(idAdd).get("optionId");
								String valueIdArray = Constant.cartArray.get(idAdd)
										.get("sizeId");
								String values = String.valueOf(count) + " pack of "
										+ cartSize;
								Constant.cartArray.remove(idAdd);
								HashMap<String, String> hasValue = new HashMap<String, String>();
								hasValue.put("totalQty", totalQtyAvailable);
								hasValue.put("itemId", pdtIdArray);
								hasValue.put("itemName", itemName);
								hasValue.put("size", cartSize);
								hasValue.put("optionId", optionIdArray);
								hasValue.put("sizeId", valueIdArray);
								hasValue.put("initialPrice", initialCartPrice);
								hasValue.put("qty", String.valueOf(count));
								hasValue.put("price",
										String.valueOf(count * cartAmnt));
								hasValue.put("desc", values);
								Constant.cartArray.add(idAdd, hasValue);

								Log.i("Minus Amnt",
										"" + String.valueOf(count * cartAmnt));

								db.open();
								db.updateCart(pdtIdArray, optionIdArray,
										valueIdArray, String.valueOf(count),
										String.valueOf(count * cartAmnt));
								db.close();

								for (int i = 0; i < Constant.cartArray.size(); i++) {
									String position = txtView_count.getText()
											.toString();
									Log.i("Tecxt count position??????????", ""
											+ position);
								}
							} else {
								/*Utils.ShowAlert(CartActivity.this,
										"Quantity should be less than or equals to "
												+ totalQtyAvailable);*/
								Utils.ShowAlert(CartActivity.this,
										"Quantity is not available");
							}
						} catch (Exception e) {
							Log.e("Exception in AddOne Button",""+e);
						} 
					}
				});

				minusOne.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						try {
							int count = Integer.parseInt(txtView_count.getText()
									.toString());
							Log.e("Count Value inside swipe --------------",""+count);
							--count;
							if (count > 0) {
								/*
								 * totalQnty =
								 * totalQnty.replace(txtView_count.getText
								 * ().toString(), String.valueOf(count));
								 * Log.i("Total qty ois>>>>>>>",""+totalQnty);
								 */
								txtView_count.setText(String.valueOf(count));
								txtView_packDesc.setText(String.valueOf(count) + " pack of " + cartSize);

								qtyAmnt = count * cartAmnt;
								txtView_itemAmount.setText("$"+ String.format("%.2f", qtyAmnt));
								subTotalAmnt = subTotalAmnt - cartAmnt;
								subTotalAmnt = Math.abs(subTotalAmnt);
								value_subtotal.setText("$"+ String.format("%.2f", subTotalAmnt));

								Log.i("SubTaotal Amnt Minus:::::::::::::", ""+ subTotalAmnt);

								if (click == true) {
									int arraySize = Constant.cartArray.size();
									int arraySizeDiff = initialArraySize- arraySize - 1;
									int position = (Integer) v.getTag();
									Log.e("Position value for Minus click button",""+position);
									if (position == 0) {
										idAdd = (Integer) v.getTag();
									} else {
										idAdd = (Integer) v.getTag()
												- arraySizeDiff;
									}
								} else {
									Log.e("Minus click boolean Value",""+click);
									idAdd = (Integer) v.getTag();
								}
							//	idAdd = (Integer) v.getTag();
								Log.i("Minus One id add value::::::::", "" + idAdd);
								String pdtIdArray = Constant.cartArray.get(idAdd).get("itemId");
								String optionIdArray = Constant.cartArray.get(idAdd).get("optionId");
								String valueIdArray = Constant.cartArray.get(idAdd).get("sizeId");
								String values = String.valueOf(count) + " pack of "	+ cartSize;
								Constant.cartArray.remove(idAdd);
								HashMap<String, String> hasValue = new HashMap<String, String>();
								hasValue.put("totalQty", totalQtyAvailable);
								hasValue.put("itemId", pdtIdArray);
								hasValue.put("itemName", itemName);
								hasValue.put("size", cartSize);
								hasValue.put("optionId", optionIdArray);
								hasValue.put("sizeId", valueIdArray);
								hasValue.put("initialPrice", initialCartPrice);
								hasValue.put("qty", String.valueOf(count));
								hasValue.put("price",String.valueOf(count * cartAmnt));
								hasValue.put("desc",values);
								Constant.cartArray.add(idAdd, hasValue);

								Log.i("Minus Amnt","" + String.valueOf(count * cartAmnt));

								db.open();
								db.updateCart(pdtIdArray, optionIdArray,
										valueIdArray, String.valueOf(count),
										String.valueOf(count * cartAmnt));
								db.close();
							} else {
								Utils.ShowAlert(CartActivity.this,
										"Quantity must be greater than 0");
							}
						} catch (Exception e) {
							Log.e("Exception in MinusOne Value",""+e);
						}
					}
				});

				delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {

						// TODO Auto-generated method stub

						AlertDialog.Builder adb = new AlertDialog.Builder(
								CartActivity.this);
						adb.setMessage("Are you sure, want to delete the item from cart!");
						adb.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
								
									public void onClick(DialogInterface dialog,
											int which) {

										if (click == true) {
											Log.e("1", "1");
											int position = (Integer) v.getTag();
											Log.e("position", position + "");
											if (position == 0) {
												Log.e("2", "2");
												id = (Integer) v.getTag();
												Log.e("id2", id + "");
											} else {
												Log.e("3", "3");
												id = (Integer) v.getTag()
														- position;
												Log.e("id3", id + "");
											}
										} else {
											Log.e("4", "4");
											click = true;
											id = (Integer) v.getTag();
											Log.e("id4", id + "");
										}
									//	id = (Integer) v.getTag();
										/*
										 * totalQnty =
										 * totalQnty.replace(","+txtView_count
										 * .getText ().toString(), "");
										 * Log.i("Total qty ois>>>>>>>"
										 * ,""+totalQnty);
										 */

										Log.e("id5", id + "");
										String pdtIdArray = Constant.cartArray
												.get(id).get("itemId");
										String optionIdArray = Constant.cartArray
												.get(id).get("optionId");
										String valueIdArray = Constant.cartArray
												.get(id).get("sizeId");

										db.open();
										db.deleteCartItem(pdtIdArray,
												optionIdArray, valueIdArray);
										db.close();

										String amntValue = txtView_itemAmount
												.getText().toString();
										String replaceAmnt = amntValue.replace(
												"$", "");
										subTotalAmnt = subTotalAmnt
												- Double.parseDouble(replaceAmnt);
										Log.i("SubTaotal Amnt Delete:::::::::::::",
												"" + subTotalAmnt);

										subTotalAmnt = Math.abs(subTotalAmnt);
										value_subtotal.setText("$"
												+ String.format("%.2f",
														subTotalAmnt));
										cartLayout.removeView(rowData);								
										Log.i("view at position cccccc:::::", "" + id);								
										Constant.cartArray.remove(id);
										Intent intent = getIntent();
										overridePendingTransition(0, 0);
										intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
										finish();
										overridePendingTransition(0, 0);
										startActivity(intent);
									}
								});
						adb.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});
						AlertDialog alert = adb.create();
						alert.show();

					}

				});

				cartLayout.addView(rowData);
			}
		} else {
		
				AlertDialog.Builder adb = new AlertDialog.Builder(CartActivity.this);
				adb.setMessage("You have no items in your shopping cart.");
				adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {				
						dialog.cancel();
						finish();
					}
				});
				AlertDialog alert = adb.create();
				alert.show();	
//			Utils.ShowAlert(CartActivity.this,
//					"You have no items in your shopping cart.All is clear");
		}
	}

	protected void hideSwipeLayout() {
		Log.e("Inside row data","hide swipe layout");
		try {
			if (childIndex != -1) {
				Log.e("Inside If condition","hide swipe layout");
				RelativeLayout parent = (RelativeLayout) cartLayout
						.getChildAt(childIndex);
				RelativeLayout parentOne = (RelativeLayout) parent.getChildAt(0);

				LinearLayout lay = (LinearLayout) parentOne.getChildAt(3);
				lay.setVisibility(View.GONE);
				Animation leftSwipe = AnimationUtils.loadAnimation(
						CartActivity.this, R.anim.left);
				lay.startAnimation(leftSwipe);
			}
		} catch (Exception e) {
			Log.e("Exception Hide Layout",""+e);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_checkout:
			
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
					|| !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				alertdialog();
			} else {
				loopRun();
				if (Constant.cartArray.size() > 0) {
					String totalQnty = null, productId = null, optionIdComma = null, valueIdComma = null;
					for (int i = 0; i < Constant.cartArray.size(); i++) {
						String cartQty = Constant.cartArray.get(i).get("qty");
						totalQnty = totalQnty + "," + cartQty;
						Log.i("Total qty ois>>>>>>>", "" + totalQnty);
						String cartId = Constant.cartArray.get(i).get("itemId");
						productId = productId + "," + cartId;
						String optionId = Constant.cartArray.get(i).get(
								"optionId");
						String valueId = Constant.cartArray.get(i)
								.get("sizeId");
						optionIdComma = optionIdComma + "," + optionId;
						valueIdComma = valueIdComma + "," + valueId;
					}
					if (rememberUser) {

						Log.e("inside remember user", "inside remember user");
						image_name = sharedpreferences.getString(
								"img_name", "");
						Log.e("image_name", image_name);
						String pwd = sharedpreferences.getString("pwd", "");
						showLogindialog(image_name, pwd);
						// Intent checkoutIntent = new Intent(CartActivity.this,
						// CheckoutActivity.class);
						// checkoutIntent.putExtra("prdtId", productId);
						// checkoutIntent.putExtra("optionId", optionIdComma);
						// checkoutIntent.putExtra("img_name", image_name);
						// checkoutIntent.putExtra("valueId", valueIdComma);
						// checkoutIntent.putExtra("quantity", totalQnty);
						//
						// checkoutIntent.putExtra("tot_price",
						// value_subtotal.getText().toString());
						// startActivity(checkoutIntent);
					} else {
						image_name = sharedpreferences.getString("img_name", "");
						Log.e("Login Dialog Else Part Name",image_name);
						showLogindialog(image_name, "");						
					}
				} else {
					Utils.ShowAlert(CartActivity.this,
							"To place order, cart must not be empty");
				}
			}
			break;

		case R.id.layHeaderBack:
			loopRun();
			finish();
			startActivity(new Intent(CartActivity.this,
					TabsFragmentActivity.class));
			break;
		case R.id.backBtn:
			loopRun();
			finish();
			startActivity(new Intent(CartActivity.this,
					TabsFragmentActivity.class));
			break;
		}
	}

	private void showLogindialog(String userName, String pwd) {
		Log.e("userName", userName);
		Log.e("pwd", pwd);
		dialoglogin = new Dialog(CartActivity.this);
		dialoglogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialoglogin.setContentView(R.layout.login_dailog);
		dialoglogin.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialoglogin.setCanceledOnTouchOutside(false);

		// TextView tv_loginTitle = (TextView) dialog
		// .findViewById(R.id.login_title);
		TextView txtForgotPwd = (TextView) dialoglogin
				.findViewById(R.id.txtForgotPwd);
		String text = CartActivity.this.getString(R.string.forgot_pwd);
		txtForgotPwd.setText(Html.fromHtml(text));
		et_username = (EditText) dialoglogin.findViewById(R.id.username);
		et_username.setText(userName);
		et_password = (EditText) dialoglogin.findViewById(R.id.password);
		et_password.setText(pwd);
		Button closeButton = (Button) dialoglogin.findViewById(R.id.promo_close);
		Button loginButton = (Button) dialoglogin.findViewById(R.id.btn_login);
		TextView signupButton = (TextView) dialoglogin.findViewById(R.id.sign_up);
		rememberMe = (CheckBox) dialoglogin.findViewById(R.id.checkbox_remember_me);
		
//		et_password.setFilters(new InputFilter[]{Constant.filter_password});
		et_username.setFilters(new InputFilter[]{Constant.filter_itemname});
		
		Log.e("RememberUser status inside login dialog box",""+rememberUser);	
		if(userName.equals(" ") || userName==" " || userName.isEmpty())
		{
			rememberMe.setChecked(true);
		} else{
		if (rememberUser) {
			Log.e("Remember Me","If Condition");
			rememberMe.setChecked(true);
		} else {			
			rememberMe.setChecked(false);		
		  }
		}		
		
		rememberMe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isCheck) {
				
				if (isCheck) {
					Log.e("Remember Me Check Status",""+isCheck);
					if (rememberMe.isChecked()) {
						
						Log.e("Remember Me","If Check box is clicked");
						String username = et_username.getText().toString().trim();
						String password = et_password.getText().toString().trim();

						if (username.length() > 0 && password.length() > 0) {
							// login process
							editor = sharedpreferences.edit();
							editor.putBoolean("remember-user", true);
							editor.commit();
							Log.e("remember", "true");
						}

					} else {
						editor = sharedpreferences.edit();
						editor.putBoolean("remember-user", false);
						editor.commit();
						Log.e("remember", "false");
						
					}

				} else {
					Log.e("remember", "false");
					editor = sharedpreferences.edit();
					editor.putBoolean("remember-user", false);
					editor.commit();
				}
			}
		});
		txtForgotPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialoglogin.dismiss();
				showPwddialog();
			}
		});

		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialoglogin.dismiss();
			}
		});

		signupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// sign up process
				dialoglogin.dismiss();
				// Intent signUpIntent = new Intent(CartActivity.this,
				// SignUpActivity.class);
				// startActivity(signUpIntent);

				String totalQnty = null, productId = null, optionIdComma = null, valueIdComma = null;
				for (int i = 0; i < Constant.cartArray.size(); i++) {
					String cartQty = Constant.cartArray.get(i).get("qty");
					totalQnty = totalQnty + "," + cartQty;
					Log.i("Total qty ois>>>>>>>", "" + totalQnty);
					String cartId = Constant.cartArray.get(i).get("itemId");
					productId = productId + "," + cartId;
					String optionId = Constant.cartArray.get(i).get("optionId");
					String valueId = Constant.cartArray.get(i).get("sizeId");
					optionIdComma = optionIdComma + "," + optionId;
					valueIdComma = valueIdComma + "," + valueId;
				}
				Intent signUpIntent = new Intent(CartActivity.this,
						SignUpActivity.class);
				signUpIntent.putExtra("prdtId", productId);
				signUpIntent.putExtra("optionId", optionIdComma);
				Log.e("valueIdComma", "" + valueIdComma);
				signUpIntent.putExtra("valueId", valueIdComma);
				signUpIntent.putExtra("quantity", totalQnty);
				signUpIntent.putExtra("tot_price", value_subtotal.getText()
						.toString());
				startActivity(signUpIntent);

			}
		});

		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String username = et_username.getText().toString().trim();
				String password = et_password.getText().toString().trim();
				try {
					first_Name=username.substring(username.length() - 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (username.length() > 0 && password.length() > 0) {
					
					///Newly Added on 11 Dec
						if (rememberMe.isChecked()) {						
						Log.e("Remember Me","If Check box is clicked");
						/*String username = et_username.getText().toString().trim();
						String password = et_password.getText().toString().trim();
*/
						if (username.length() > 0 && password.length() > 0) {
							// login process
							editor = sharedpreferences.edit();
							editor.putBoolean("remember-user", true);
							editor.commit();
							Log.e("remember", "true");
						}

					} else {
						editor = sharedpreferences.edit();
						editor.putBoolean("remember-user", false);
						editor.commit();
						Log.e("remember", "false");
						
					}
					///////Upto this	
						
					
					// login process
			//		dialoglogin.dismiss();
					boolean isOnline = Utils.isOnline();
					Log.e("isOnline", isOnline + "");
					if (isOnline) {
						if(first_Name.equals(".") || first_Name.equals("_")){
							Utils.ShowAlert(CartActivity.this,
									"Special Characters are not allowed at the end of the string.");
						}else if(username.contains("..")||username.contains("__")){
							Utils.ShowAlert(CartActivity.this,
									"Please enter a valid name.");
						}else if(username.startsWith(".")||username.startsWith("_")){
							Utils.ShowAlert(CartActivity.this,
									"Special Characters are not allowed at the beginning of the string.");
						}else{						
						new CreateAccount(username, password).execute();
						}
					} else {
						Utils.ShowAlert(CartActivity.this,
								Constant.networkDisconected);
					}

				} else {
					if (username.length() <= 0 || username.isEmpty()
							|| username == null) {
						Toast.makeText(getApplicationContext(),
								"User name is empty!", Toast.LENGTH_LONG)
								.show();
					} else {
						Toast.makeText(getApplicationContext(),
								"password is empty!", Toast.LENGTH_LONG).show();
					}
				}

			}
		});
		dialoglogin.show();
	}

	public class CreateAccount extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String email, password, status = "", error, userId, fName, lName, dob;

		public CreateAccount(String mailId, String pwd) {
			// TODO Auto-generated constructor stub
			email = mailId;
			password = pwd;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(CartActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.loginAccount())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("account creation", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
						Log.e("Error",error);
					} else {
						userId = jsonObj.getString("userid");
						fName = jsonObj.getString("firstname");
						lName = jsonObj.getString("lastname");
						dob = jsonObj.getString("dob");
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
					
					dialogbox = new Dialog(CartActivity.this,R.style.custom_dialog_theme);
					dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);		
					dialogbox.getWindow();
			  	    dialogbox.setContentView(R.layout.custom_alert);
					dialogbox.setCancelable(false);
										
					text_Message= (TextView) dialogbox.findViewById(R.id.success);
					btn_Ok= (Button) dialogbox.findViewById(R.id.habitokbtn);
					text_Message.setText("Login Success!!!!");
					btn_Ok.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View arg0) {
							dialogbox.cancel();
							dialoglogin.dismiss();
							editor = sharedpreferences.edit();
							editor.putString("userId", userId);
							editor.putString("firstname", fName);
							editor.putString("lastname", lName);
							editor.putString("dob", dob);
							editor.putString("img_name", email);
							if (rememberMe.isChecked()) {
								editor.putString("pwd", et_password.getText()
										.toString());

							}

							editor.commit();
							Log.i("User ID Is::::::", "" + userId);

							String totalQnty = null, productId = null, optionIdComma = null, valueIdComma = null, taxComma = null;
							for (int i = 0; i < Constant.cartArray.size(); i++) {
								String cartQty = Constant.cartArray.get(i).get("qty");
								totalQnty = totalQnty + "," + cartQty;
								Log.i("Total qty ois>>>>>>>", "" + totalQnty);
								String cartId = Constant.cartArray.get(i).get("itemId");
								productId = productId + "," + cartId;
								String optionId = Constant.cartArray.get(i).get(
										"optionId");
								String valueId = Constant.cartArray.get(i)
										.get("sizeId");
								optionIdComma = optionIdComma + "," + optionId;
								valueIdComma = valueIdComma + "," + valueId;
								String tax = Constant.cartArray.get(i).get("itemId");
								taxComma = taxComma + "," + tax;
							}
							finish();
							Intent checkoutIntent = new Intent(CartActivity.this,
									CheckoutActivity.class);
							checkoutIntent.putExtra("prdtId", productId);
							checkoutIntent.putExtra("optionId", optionIdComma);
							checkoutIntent.putExtra("valueId", valueIdComma);
							checkoutIntent.putExtra("quantity", totalQnty);
							checkoutIntent.putExtra("img_name", email);
							checkoutIntent.putExtra("tot_price", value_subtotal
									.getText().toString());
							checkoutIntent.putExtra("first_name", fName);
							checkoutIntent.putExtra("last_name", lName);
							checkoutIntent.putExtra("dob", dob);
							// checkoutIntent.putExtra("tax", tax);
							startActivity(checkoutIntent);						
						}
					});
					
				/*	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);			 
					alertDialogBuilder.setTitle("Message");
			 		alertDialogBuilder	.setMessage("Login Success!!!!")
							.setCancelable(false)
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {								
									dialog.cancel();
									dialoglogin.dismiss();
									editor = sharedpreferences.edit();
									editor.putString("userId", userId);
									editor.putString("firstname", fName);
									editor.putString("lastname", lName);
									editor.putString("dob", dob);
									editor.putString("img_name", email);
									if (rememberMe.isChecked()) {
										editor.putString("pwd", et_password.getText()
												.toString());

									}

									editor.commit();
									Log.i("User ID Is::::::", "" + userId);

									String totalQnty = null, productId = null, optionIdComma = null, valueIdComma = null, taxComma = null;
									for (int i = 0; i < Constant.cartArray.size(); i++) {
										String cartQty = Constant.cartArray.get(i).get("qty");
										totalQnty = totalQnty + "," + cartQty;
										Log.i("Total qty ois>>>>>>>", "" + totalQnty);
										String cartId = Constant.cartArray.get(i).get("itemId");
										productId = productId + "," + cartId;
										String optionId = Constant.cartArray.get(i).get(
												"optionId");
										String valueId = Constant.cartArray.get(i)
												.get("sizeId");
										optionIdComma = optionIdComma + "," + optionId;
										valueIdComma = valueIdComma + "," + valueId;
										String tax = Constant.cartArray.get(i).get("itemId");
										taxComma = taxComma + "," + tax;
									}
									finish();
									Intent checkoutIntent = new Intent(CartActivity.this,
											CheckoutActivity.class);
									checkoutIntent.putExtra("prdtId", productId);
									checkoutIntent.putExtra("optionId", optionIdComma);
									checkoutIntent.putExtra("valueId", valueIdComma);
									checkoutIntent.putExtra("quantity", totalQnty);
									checkoutIntent.putExtra("img_name", email);
									checkoutIntent.putExtra("tot_price", value_subtotal
											.getText().toString());
									checkoutIntent.putExtra("first_name", fName);
									checkoutIntent.putExtra("last_name", lName);
									checkoutIntent.putExtra("dob", dob);
									// checkoutIntent.putExtra("tax", tax);
									startActivity(checkoutIntent);
								}
							  });			 
							AlertDialog alertDialog = alertDialogBuilder.create();
			 				alertDialog.show();		*/		
					
					
					dialogbox.show();
				} else {
					
					Utils.ShowAlert(CartActivity.this, error);
					Log.e("Alert Dialog","Showing");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class ForgotPwd extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String email, status = "", error;

		public ForgotPwd(String mailId) {
			// TODO Auto-generated constructor stub
			email = mailId;

		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(CartActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("customer_email", email));

			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.forgotPwdUser()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("forgot pwd", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else {

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			try {
				dialog.dismiss();
				if (status.equals("1")) {

					Utils.ShowAlert(
							CartActivity.this,
							"If there is an account associated with "
									+ email
									+ "  you will receive an email with new password.");

				} else {
					Utils.ShowAlert(CartActivity.this,
							"Please enter a valid email id.");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showPwddialog() {

		final Dialog pwdDialog = new Dialog(CartActivity.this);
		pwdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pwdDialog.setContentView(R.layout.forgot_pwd);
		pwdDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		pwdDialog.setCanceledOnTouchOutside(false);

		final EditText edtEmail = (EditText) pwdDialog
				.findViewById(R.id.forgot_username);

		Button closeButton = (Button) pwdDialog.findViewById(R.id.pwd_close);
		Button confirmButton = (Button) pwdDialog
				.findViewById(R.id.btnForgotPwd);

		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwdDialog.dismiss();
			}
		});

		confirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edtEmail.getText().toString().length() > 0) {
					if (Utils
							.isEmailValid(edtEmail.getText().toString().trim())) {
						boolean isOnline = Utils.isOnline();
						Log.e("isOnline", isOnline + "");
						if (isOnline) {
							// pwdDialog.dismiss();
							new ForgotPwd(edtEmail.getText().toString().trim())
									.execute();
						} else {
							Utils.ShowAlert(CartActivity.this,
									Constant.networkDisconected);
						}
					} else {
						Utils.ShowAlert(CartActivity.this,
								"Please enter a valid email id.");
					}
				} else {
					Utils.ShowAlert(CartActivity.this,
							"Please enter a valid email id.");
				}
			}
		});
		pwdDialog.show();
	}

	public void alertdialog() {
		// Build the alert dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
		builder.setTitle("Location Services Not Active");
		builder.setMessage("Hey! Turn on your GPS and Location Services");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				// Show location settings when the user acknowledges the alert
				// dialog
				Intent intent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		Dialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}
	
	public void loopRun(){
		 for(int i=0;i<removeIdValue.size();i++){
		    	Log.e("Remove Id Total Size",""+removeIdValue.size());
		    	Constant.cartArray.get(removeIdValue.remove(i));	   
		    }
}


}
