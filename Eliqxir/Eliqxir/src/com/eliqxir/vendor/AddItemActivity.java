package com.eliqxir.vendor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eliqxir.R;
import com.eliqxir.adapter.SpinnerAdapter;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.tabhostfragments.BeerTab;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.DecimalDigitsInputFilter;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

public class AddItemActivity extends Activity implements OnClickListener     {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();		
	}
//	ArrayList<String> item_Size=new ArrayList<String>();
	RelativeLayout rel1;
	private LinearLayout list_Linear;
	ImageButton backImg, cartBtn, btnSlideMenu, btnForAddItem, btnForScan,btnAddSign;
	TextView textForHeader,text_Size,text_Price,text_selectSize,text_selectPrice;
	int itemStatus,featuredStatus,btn_select = 0,removeIndex = 0;
	CheckBox availableBtn, FeaturedBtn;
	boolean available = true, featured = true;
	EditText editTxtForName, editTxtForQty, editTxtForFiOz, editTxtForPrice,edit_ValueSize,edit_ValuePrice,
			editTextForAddItemWeight, editTextForAddItemSku,
			editTextForAddItemDesc, editTextForAddItemShortDesc,
			editTextForAddItemMetaDesc,edit_Size,edit_Price;
	Spinner spinnerForCategory, spinnerForSubCategory;
	ArrayList<String> categoryList = new ArrayList<String>();
	ArrayList<String> subCategoryList = new ArrayList<String>();
	ArrayList<String> subCategoryIdList = new ArrayList<String>();
	HashMap<String, String> selectedHasmap = new HashMap<String, String>();
	String selectedCategory = "", selectedsubCategory = "",first_Name,size_Value,price_Val,total_ItemSize,total_ItemPrize,
			selectedsubCategoryId = "", selectedPrice,stordID;
//	private String current = "";
	SharedPreferences vendorSharedpreferences,customerPreference;
	Editor vendorPreferenceEditor,customerPrefEditor;
    Button btn_Add,btn_Edit;
    RelativeLayout relative_Main;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent(AddItemActivity.this,
					TabsFragmentActivity.class));
		}

		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_item);
		
		parseData(BeerTab.vendor_Response);
		
		vendorSharedpreferences = this.getSharedPreferences(
				"vendorPrefs", Context.MODE_PRIVATE);
		stordID = vendorSharedpreferences.getString("store_id", "");
		/*customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		customerPrefEditor=customerPreference.edit();
		stordID = customerPreference.getString("store_id", "");*/
		Log.e("stordID in add item page", stordID);
	//	categoryList.clear();
		list_Linear = (LinearLayout)findViewById(R.id.listingLayout);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		availableBtn = (CheckBox) findViewById(R.id.imageForAddAvailable);
		FeaturedBtn = (CheckBox) findViewById(R.id.imageForAddFeatured);
		btnForAddItem = (ImageButton) findViewById(R.id.btnForAddItem);
		btnAddSign= (ImageButton) findViewById(R.id.imageAddButton);
		
		editTxtForName = (EditText) findViewById(R.id.editTextForAddItemName);
		editTxtForQty = (EditText) findViewById(R.id.editTextForAddItemQty);
		editTxtForFiOz = (EditText) findViewById(R.id.editTextForAddItemFiOz);
//		editTxtForPrice = (EditText) findViewById(R.id.editTextForAddItemPrice);
		spinnerForCategory = (Spinner) findViewById(R.id.editTextForAddItemCategory);
		spinnerForSubCategory = (Spinner) findViewById(R.id.editTextForAddItemSubCategory);
		
		editTextForAddItemWeight = (EditText) findViewById(R.id.editTextForAddItemWeight);
		editTextForAddItemSku = (EditText) findViewById(R.id.editTextForAddItemSku);
		editTextForAddItemDesc = (EditText) findViewById(R.id.editTextForAddItemDesc);
		editTextForAddItemShortDesc = (EditText) findViewById(R.id.editTextForAddItemShortDesc);
		editTextForAddItemMetaDesc = (EditText) findViewById(R.id.editTextForAddItemMetaDesc);
		
		btnForScan = (ImageButton) findViewById(R.id.btnForScanBarCode);
		
		editTextForAddItemWeight.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(10, 2) });
		
		backImg.setOnClickListener(this);
		// availableBtn.setOnClickListener(this);
		// FeaturedBtn.setOnClickListener(this);
		btnForAddItem.setOnClickListener(this);
		btnForScan.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		textForHeader.setText("ITEM");
	/*	categoryList.add("Beer");
//		categoryList.add("Featured");
		categoryList.add("Wine");
		categoryList.add("Liquor");
		categoryList.add("Extras");
		categoryList.add("Select a Category");*/
		SpinnerAdapter spinAdapter = new SpinnerAdapter(this,
				R.layout.simple_spinner_item, categoryList,"fromAddItem");
		spinnerForCategory.setAdapter(spinAdapter);
		spinnerForCategory.setSelection(categoryList.size() - 1);
		subCategoryList.add("Select a subCategory");
		subCategoryList.add("Select a subCategory");
		subCategoryList.add("Select a subCategory");
		subCategoryList.add("Select a subCategory");
		subCategoryList.add("Select a subCategory");
		subCategoryList.add("Select a subCategory");
		subCategoryIdList.add(" ");
		SpinnerAdapter spinAdapter1 = new SpinnerAdapter(this,
				R.layout.simple_spinner_item, subCategoryList,"fromAddItem");
		spinnerForSubCategory.setAdapter(spinAdapter1);
		spinnerForSubCategory.setSelection(subCategoryList.size() - 1);
		spinnerForSubCategory.getSelectedView();
		spinnerForSubCategory.setEnabled(false);
	
		spinnerForCategory
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position != categoryList.size() - 1) {
							selectedCategory = categoryList.get(position);

							Log.e("selectedCategory", selectedCategory);
							if (selectedCategory.equals("Beer")) {
								selectedHasmap = Constant.beerhashMapHeader;
							} else if (selectedCategory.equals("Wine")) {
								selectedHasmap = Constant.winehashMapHeader;
							} 
							else if (selectedCategory.equals("Featured")) {
								selectedHasmap = Constant.featuredhashMapHeader;

							} else if (selectedCategory.equals("Liquor")) {
								selectedHasmap = Constant.liquorhashMapHeader;

							} else if (selectedCategory.equals("Extras")) {
								selectedHasmap = Constant.mixerhashMapHeader;
							}
							
							spinnerForSubCategory.setEnabled(true);
							subCategoryList.clear();
							subCategoryIdList.clear();
							Iterator myVeryOwnIterator = selectedHasmap
									.keySet().iterator();
							while (myVeryOwnIterator.hasNext()) {
								String key = (String) myVeryOwnIterator.next();
								String value = (String) selectedHasmap.get(key);
								Log.e("subcat", "Key: " + key + " Value: "
										+ value);
								subCategoryList.add(key);
								subCategoryIdList.add(value);
							}
							
							subCategoryList.add("Select a subCategory");
							subCategoryIdList.add(" ");
							SpinnerAdapter spinAdapter = new SpinnerAdapter(
									AddItemActivity.this,
									R.layout.simple_spinner_item,
									subCategoryList,"fromAddItem");
							spinnerForSubCategory.setAdapter(spinAdapter);
							spinnerForSubCategory.setSelection(0);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}

				});

		spinnerForSubCategory
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position != subCategoryList.size() - 1) {
							selectedsubCategory = subCategoryList.get(position);
							selectedsubCategoryId = subCategoryIdList
									.get(position);
							Log.e("selectedsubCategory", selectedsubCategory);
							// Log.e("selectedsubCategoryId",
							// selectedsubCategoryId);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}

				});

		availableBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (availableBtn.isChecked()) {
					itemStatus = 1;
				} else {
					itemStatus = 0;
				}
			}
		});
		
		//Added on 14th April
		FeaturedBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (FeaturedBtn.isChecked()) {
					featuredStatus = 1;
				} else {
					featuredStatus = 0;
				}
			}
		});

		/*editTxtForPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (!s.toString().equals(current)) {

					try {
						editTxtForPrice.removeTextChangedListener(this);

						String cleanString = s.toString().replaceAll("[$,.]", "");

						double parsed = Double.parseDouble(cleanString);
						String formatted = NumberFormat.getCurrencyInstance()
								.format((parsed / 100));

						current = formatted;
						editTxtForPrice.setText(formatted);
						editTxtForPrice.setSelection(formatted.length());

						editTxtForPrice.addTextChangedListener(this);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override                                              
			public void afterTextChanged(Editable s) {
			}
		});    */                                              
		
		btnAddSign.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_select=0;
				final Dialog dialogbox = new Dialog(AddItemActivity.this);
				dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogbox.setContentView(R.layout.additem_alert_dialog);
			
				btn_Add = (Button) dialogbox.findViewById(R.id.btn_add);		
				btn_Edit = (Button) dialogbox.findViewById(R.id.btn_edit);		
				edit_Size= (EditText) dialogbox.findViewById(R.id.editsize);
				edit_Price = (EditText) dialogbox.findViewById(R.id.editprice);
				
				edit_Size.setFilters(new InputFilter[]{Constant.filter_sizeValue});
				edit_Price.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(15,2)});
				
				btn_Add.setVisibility(View.VISIBLE);
		        btn_Edit.setVisibility(View.GONE);
		        
				btn_Add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.e("Button Add","Select");
						size_Value=edit_Size.getText().toString().trim();
						price_Val	=edit_Price.getText().toString().trim();
																	
						if(price_Val.startsWith("0")||size_Value.startsWith("0")){
							Utils.ShowAlert(AddItemActivity.this,
									"Please enter valid price or size value.");
						}else if(price_Val.length()==0|| size_Value.length()==0){
							Utils.ShowAlert(AddItemActivity.this,
									"Please enter price or size value.");
						}
						else{
						
						final View listing_View = getLayoutInflater().inflate(R.layout.add_item_adapter, null);
						text_Size = (TextView) listing_View
								.findViewById(R.id.sizetext);
						text_Price = (TextView) listing_View
								.findViewById(R.id.pricetext);
						relative_Main = (RelativeLayout) listing_View
								.findViewById(R.id.mainRelative);
						Log.e("Inside Adding view",""+size_Value+" "+price_Val);
						text_Size.setText(size_Value);
						text_Price.setText(price_Val);
			//			}
						
				relative_Main.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
							    	btn_select=1;
									final Dialog dialogbox = new Dialog(AddItemActivity.this);
									dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
									dialogbox.setContentView(R.layout.additem_alert_dialog);
								
									btn_Add = (Button) dialogbox.findViewById(R.id.btn_add);		
									btn_Edit = (Button) dialogbox.findViewById(R.id.btn_edit);		
									edit_Size= (EditText) dialogbox.findViewById(R.id.editsize);
									edit_Price = (EditText) dialogbox.findViewById(R.id.editprice);
									
									edit_Size.setFilters(new InputFilter[]{Constant.filter_sizeValue});
									edit_Price.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(15,2)});
									
									dialogbox.show();
									dialogbox.setCancelable(true);									

									btn_Add.setVisibility(View.GONE);
									btn_Edit.setVisibility(View.VISIBLE);
									
									removeIndex = list_Linear.indexOfChild(listing_View);
									rel1=(RelativeLayout)list_Linear.getChildAt(removeIndex);
									/*text_selectSize = (TextView) rel1	.findViewById(R.id.sizetext);
									text_selectPrice = (TextView) rel1.findViewById(R.id.pricetext);*/
									
									text_Size = (TextView) rel1.findViewById(R.id.sizetext);
									text_Price = (TextView) rel1.findViewById(R.id.pricetext);
									
					//				Log.e("Inside Relative Click  view",""+text_selectSize.getText().toString()+" "+text_selectPrice.getText().toString());
//									edit_Size.setText(size_Value);
//									edit_Price.setText(price_Val);
									
									/*edit_Size.setText(text_selectSize.getText().toString());
									edit_Price.setText(text_selectPrice.getText().toString());*/
									
									edit_Size.setText(text_Size.getText().toString());
									edit_Price.setText(text_Price.getText().toString());
									
									btn_Edit.setOnClickListener(new OnClickListener() {									
										@Override
										public void onClick(View v) {
											size_Value=edit_Size.getText().toString().trim();
											price_Val=edit_Price.getText().toString().trim();
											Log.e("Inside btn Edit click  view",""+size_Value+" "+price_Val);
//											text_Size.setText(size_Value);
//											text_Price.setText(price_Val);
											/*text_selectSize.setText(size_Value);
											text_selectPrice.setText(price_Val);*/
											if(price_Val.startsWith("0")||size_Value.startsWith("0")){
												Utils.ShowAlert(AddItemActivity.this,
														"Please enter valid price or size value.");
											}else if(price_Val.length()==0|| size_Value.length()==0){
												Utils.ShowAlert(AddItemActivity.this,
														"Please enter price or size value.");
											}else{
											text_Size.setText(size_Value);
											text_Price.setText(price_Val);
											dialogbox.cancel();
											}
										}
									});									
							}
						});
						
						if(btn_select==0){
							Log.e("Inside If","Loop");
						list_Linear.addView(listing_View);						
						dialogbox.cancel();
						}else{
							Log.e("Inside Else","Loop");
//							text_Size.setText(size_Value);
//							text_Price.setText(price_Val);
						}										
						}
					}
				});

				dialogbox.show();
				dialogbox.setCancelable(true);
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backBtn) {
			// finish();
			startActivity(new Intent(AddItemActivity.this,
					TabsFragmentActivity.class));
		}
		// if(v.getId()==R.id.imgsubcatdrop)
		// {
		//
		// }
		// if(v.getId()==R.id.imgcatdrop)
		// {
		//
		// }
		// if(v.getId()==R.id.imageForAvailable){
		// if(available == true){
		// available = false;
		// availableBtn.setImageResource(R.drawable.available_on);
		// }
		// else{
		// available = true;
		// availableBtn.setImageResource(R.drawable.available_off);
		// }
		// }
		//
		// if(v.getId()==R.id.imageForFeatured){
		// if(featured == true){
		// featured = false;
		// FeaturedBtn.setImageResource(R.drawable.available_on);
		// }
		// else{
		// featured = true;
		// FeaturedBtn.setImageResource(R.drawable.available_off);
		// }
		// }

		if (v.getId() == R.id.btnForAddItem) {
	
			Log.e("List Linear child count",""+list_Linear.getChildCount());
			for(int i=0;i<list_Linear.getChildCount();i++){	
				rel1=(RelativeLayout)list_Linear.getChildAt(i);
				text_Size = (TextView)rel1.findViewById(R.id.sizetext);				
				Log.e("Inside for size looping",""+i);				
				if(i==0){
					total_ItemSize=text_Size.getText().toString();	
				}else{
					total_ItemSize=total_ItemSize+","+text_Size.getText().toString();
				}				
	    	}
			Log.e("Final Size Value ",""+total_ItemSize);
						
			for(int j=0;j<list_Linear.getChildCount();j++){	
				rel1=(RelativeLayout)list_Linear.getChildAt(j);
				text_Price = (TextView)rel1.findViewById(R.id.pricetext);				
				Log.e("Inside for prize looping",""+j);				
				if(j==0){
					total_ItemPrize=text_Price.getText().toString();	
				}else{
					total_ItemPrize=total_ItemPrize+","+text_Price.getText().toString();
				}				
	    	}
			Log.e("Final Prize Value ",""+total_ItemPrize);			
				
			String name = editTxtForName.getText().toString().trim();
			String qty = editTxtForQty.getText().toString().trim();
			String fioz = editTxtForFiOz.getText().toString().trim();
	//		String price = editTxtForPrice.getText().toString().trim();
			String weight = editTextForAddItemWeight.getText().toString().trim();
			String sku = editTextForAddItemSku.getText().toString().trim();
			String desc = editTextForAddItemDesc.getText().toString().trim();
			String shortdesc = editTextForAddItemShortDesc.toString().trim();
			String metadesc = editTextForAddItemMetaDesc.toString().trim();
			
			try {
				first_Name=name.substring(name.length() - 1);
			} catch (Exception e) {
				Log.e("Inside Exception Name",""+e);
			}
			
			editTxtForName.setFilters(new InputFilter[]{Constant.filter_name});
			editTextForAddItemShortDesc.setFilters(new InputFilter[]{Constant.filter_name});
			editTextForAddItemMetaDesc.setFilters(new InputFilter[]{Constant.filter_name});
			editTextForAddItemDesc.setFilters(new InputFilter[]{Constant.filter_name});
			
			if (name.length() > 0 && qty.length() > 0 && fioz.length() > 0
					&&selectedCategory.length() > 0
					&& selectedsubCategory.length() > 0 && weight.length() > 0
					&& sku.length() > 0 && desc.length() > 0
					&& shortdesc.length() > 0 && metadesc.length() > 0) {
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					if(first_Name.equals(".") || first_Name.equals("_")){
						Utils.ShowAlert(AddItemActivity.this,
								"Special Characters are not allowed at the end of the string.");
					}else if(name.contains("..")||name.contains("__")){
						Utils.ShowAlert(AddItemActivity.this,
								"Please enter a valid name.");
					}else if(name.startsWith(".")||desc.startsWith(".")||name.startsWith("_")||desc.startsWith("_")
							||shortdesc.startsWith(".")||metadesc.startsWith(".")||shortdesc.startsWith("_")||metadesc.startsWith("_")){
						Utils.ShowAlert(AddItemActivity.this,
								"Special Characters are not allowed at the beginning of the string.");
					}else if(list_Linear.getChildCount()==0){
						Utils.ShowAlert(AddItemActivity.this,
								"Please enter product size and price value.");
					}else if(first_Name.equals(" ") || name.equals(" ")){
						Utils.ShowAlert(AddItemActivity.this,
								"Name field should not be empty.");
					}
					/*else if(Constant.containAlphanumeric(sku)==false){
						Utils.ShowAlert(AddItemActivity.this,
								"Please give alpha numeric value for SKU.");		}*/
					else{					
						Log.e("Success","Value");
						Log.e("Product SKU Value",editTextForAddItemSku.getText().toString().trim());
					new AddItem().execute();
					}
				}
				else
				{
					Utils.ShowAlert(AddItemActivity.this, Constant.networkDisconected);
				}
			} else {
				Utils.ShowAlert(AddItemActivity.this, "Please fill all fields.");
			}
		}

		if (v.getId() == R.id.btnForScanBarCode) {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	//		intent.putExtra("SCAN_MODE", "CODE_128");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
			startActivityForResult(intent, 0);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				// Toast toast = Toast.makeText(this, "Content:" + contents
				// + " Format:" + format, Toast.LENGTH_LONG);
				// toast.setGravity(Gravity.TOP, 25, 400);
				// toast.show();

				editTextForAddItemSku.setText(contents);
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Toast toast = Toast.makeText(this, "Scan was Cancelled!",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			}
		}
	}

	public boolean isInstalled(Context myContext, String name) {
		PackageManager myPackageMgr = myContext.getPackageManager();
		try {
			myPackageMgr.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			return (false);
		}
		return (true);
	}

	@SuppressWarnings("unused")
	private boolean appInstalledOrNot(String packagename, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public class AddItem extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error, itemQuantity = "";

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {				
					
						AlertDialog.Builder adb = new AlertDialog.Builder(AddItemActivity.this);
						adb.setMessage("Item Added Successfully");
						adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {				
								dialog.cancel();
								startActivity(new Intent(AddItemActivity.this,
										TabsFragmentActivity.class));
							}
						});
						AlertDialog alert = adb.create();
						alert.show();
				
					/*Utils.ShowAlert(AddItemActivity.this,
							"Item Added Successfully");*/
				/*	editTxtForName.setText("");
					editTxtForQty.setText("");
					editTxtForFiOz.setText("");*/
		//			editTxtForPrice.setText("");
					
					/*spinnerForCategory.setSelection(0);
					spinnerForSubCategory.setSelection(0);
					editTextForAddItemDesc.setText("");
					editTextForAddItemMetaDesc.setText("");
					editTextForAddItemShortDesc.setText("");
					editTextForAddItemSku.setText("");
					editTextForAddItemWeight.setText("");*/
				} else {
					Utils.ShowAlert(AddItemActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(AddItemActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(15);

		    nameValuePair.add(new BasicNameValuePair("category_id",	selectedsubCategoryId));
			nameValuePair.add(new BasicNameValuePair("product_name",editTxtForName.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("product_sku",editTextForAddItemSku.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("product_description",editTextForAddItemDesc.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("product_short_description", editTextForAddItemShortDesc.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("product_weight",editTextForAddItemWeight.getText().toString().trim()));
//			String price = editTxtForPrice.getText().toString().trim()	.replace("$", "");
			nameValuePair.add(new BasicNameValuePair("product_price","0"));
			nameValuePair.add(new BasicNameValuePair("product_qty",editTxtForQty.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("product_meta_description", editTextForAddItemMetaDesc.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("product_stock", Integer.toString(itemStatus)));			
			nameValuePair.add(new BasicNameValuePair("product_featured", Integer	.toString(featuredStatus)));
			nameValuePair.add(new BasicNameValuePair("store_id",stordID));
			nameValuePair.add(new BasicNameValuePair("product_fl_oz",editTxtForFiOz.getText().toString().trim()));
			nameValuePair.add(new BasicNameValuePair("custom_option_title",total_ItemSize));
			nameValuePair.add(new BasicNameValuePair("custom_option_price",total_ItemPrize));
			/*List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(15);
			JSONArray jsArr = new JSONArray();
			JSONObject obj =new JSONObject();
			try {
				obj.put("category_id",selectedsubCategoryId);
				obj.put("product_name",editTxtForName.getText().toString().trim());
				obj.put("product_sku",editTextForAddItemSku.getText().toString().trim());
				obj.put("product_description",editTextForAddItemDesc.getText().toString().trim());
				obj.put("product_short_description", editTextForAddItemShortDesc.getText().toString().trim());		
				obj.put("product_weight",editTextForAddItemWeight.getText().toString().trim());	
				obj.put("product_price","0");
				obj.put("product_qty",editTxtForQty.getText().toString());
				obj.put("product_meta_description", editTextForAddItemMetaDesc.getText().toString().trim());
				obj.put("product_stock", Integer.toString(itemStatus));			
				obj.put("product_featured", Integer	.toString(featuredStatus));
				obj.put("store_id",stordID);
				obj.put("product_fl_oz",editTxtForFiOz.getText().toString().trim());
				obj.put("custom_option_title",total_ItemSize);
				obj.put("custom_option_price",total_ItemPrize);				
				jsArr.put(0, obj);
				Log.e("Json Format Value Add item pae",jsArr.toString());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			nameValuePair.add(new BasicNameValuePair("addnew_item",jsArr.toString()));*/
			/*try {
				URLEncoder.encode("&", "UTF8");
				Log.e("URLEncoder in AddItemPage",URLEncoder.encode("&", "UTF8"));
			} catch (UnsupportedEncodingException e1) {
				Log.e("URLEncoding Exception",""+e1);
			}*/
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.vendorAddItem()).getJSONObjectfromURL(RequestType.POST,nameValuePair);
			Log.e("Add new Item Activity response",""+jsonObj);
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

	/*public class DecimalDigitsInputFilter implements InputFilter {

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero,
				int digitsAfterZero) {
			mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1)
					+ "}+((\\.[0-9]{0," + (digitsAfterZero - 1)
					+ "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			Matcher matcher = mPattern.matcher(dest);
			if (!matcher.matches())
				return "";
			return null;
		}
	}*/

	class CurrencyTextWatcher implements TextWatcher {

		boolean mEditing;

		public CurrencyTextWatcher() {
			mEditing = false;
		}

		public synchronized void afterTextChanged(Editable s) {
			if (!mEditing) {
				mEditing = true;

				String digits = s.toString().replaceAll("\\D", "");
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				try {
					String formatted = nf
							.format(Double.parseDouble(digits) / 100);
					s.replace(0, s.length(), formatted);
				} catch (NumberFormatException nfe) {
					s.clear();
				}

				mEditing = false;
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

	}

	/*public void parseData(String data) {
		Log.e("Response Parser ADDDDDDDDDdd",data);
		LinkedHashMap<String, String> subProductsMap = new LinkedHashMap<String, String>();
		try {
			JSONObject json = new JSONObject(data);
			JSONObject jsonProducts = json.getJSONObject("products");
			JSONArray jsonArr = jsonProducts.getJSONArray("Featured");
			for (int jValue = 0; jValue < jsonArr.length(); jValue++) {
				JSONObject jsonSub = jsonArr.getJSONObject(jValue);
				String subCat = jsonSub.getString("parent_category");
				// Log.e("The","Sub category is=>"+subCat);
				if (subCat.equalsIgnoreCase("Featured")) {

				} else {
					subProductsMap.put(subCat, subCat);
				}
			}
			Log.e("The", "Sub category is=>" + subProductsMap);
		} catch (Exception e) {

		}
		categoryList = new ArrayList<String>();

		Set<String> keys = subProductsMap.keySet();
		for (String key : keys) {
			categoryList.add(key);
		}
		categoryList.add("Select a Category");
	}*/
	
	public void parseData(String data) {
		  LinkedHashMap<String, String> subProductsMap = new LinkedHashMap<String, String>();
		  try {
		   JSONObject json = new JSONObject(data);
		   JSONObject jsonProducts = json.getJSONObject("products");
		   JSONArray jsonArr = jsonProducts.getJSONArray("Featured");
		   // for (int jValue = 0; jValue < jsonArr.length(); jValue++) {
		   // JSONObject jsonSub = jsonArr.getJSONObject(jValue);
		   // String subCat = jsonSub.getString("parent_category");
		   //
		   // // Log.e("The","Sub category is=>"+subCat);
		   // if (subCat.equalsIgnoreCase("Featured")) {
		   //
		   // } else {
		   // subProductsMap.put(subCat, subCat);
		   // }
		   // }
		   // Log.e("The", "Sub category is=>" + subProductsMap);
		   JSONArray jsonSub = json.getJSONArray("sub_category");
		   for (int jValue = 0; jValue < jsonSub.length(); jValue++) {
		    JSONObject jsonSubCat = jsonSub.getJSONObject(jValue);

//		    Log.e("The", "id is=>" + jsonSubCat.getString("category_id"));
		//
//		    Log.e("The", "opt string=>" + jsonSubCat.opt("Wine"));

		     if(jsonSubCat.has("Featured"))
		     {
		    
		     }
		    
		     else if(jsonSubCat.has("Wine"))
		     {
		     subProductsMap.put("Wine", "Wine");
		     }
		     else if(jsonSubCat.has("Beer"))
		     {
		     subProductsMap.put("Beer", "Beer");
		     }
		     else if(jsonSubCat.has("Liquor"))
		     {
		     subProductsMap.put("Liquor", "Liquor");
		     }
		     else if(jsonSubCat.has("Extras"))
		     {
		     subProductsMap.put("Extras","Extras");
		     }
		     else
		     {
		     Log.e("It","doesnot contains");
		     }
		    
//		    Log.e("-------------------","---------------------");
//		    String result = getQZero(jsonSubCat, "Featured");
//		    Log.e("The","result is =>"+result);
//		    if (result.equalsIgnoreCase("error")) {
//		     result = getQZero(jsonSubCat, "Wine");
//		     Log.e("The","result is =>"+result);
//		     if (result.equalsIgnoreCase("error")) {
//		      result = getQZero(jsonSubCat, "Beer");
//		      Log.e("The","result is =>"+result);
//		      if (result.equalsIgnoreCase("error")) {
//		       result = getQZero(jsonSubCat, "Liquor");
//		       Log.e("The","result is =>"+result);
//		       if (result.equalsIgnoreCase("error")) {
//		        result = getQZero(jsonSubCat, "Extras");
//		        Log.e("The","result is =>"+result);
//		        if (result.equalsIgnoreCase("error")) {
		//
//		        } else {
//		         subProductsMap.put("Extras", "Extras");
//		        }
//		       } else {
//		        subProductsMap.put("Liquor", "Liquor");
//		       }
//		      } else {
//		       subProductsMap.put("Beer", "Beer");
//		      }
//		     } else {
//		      subProductsMap.put("Wine", "Wine");
//		     }
//		    } else {
		//
//		    }

		   }
		  } catch (Exception e) {
		   Log.e("The", "Exception is=>" + e);
		  }
		  Log.e("The", "sub category =>" + subProductsMap);
		  categoryList = new ArrayList<String>();

		  Set<String> keys = subProductsMap.keySet();
		  for (String key : keys) {
		   categoryList.add(key);
		  }
		  categoryList.add("Select a Category");
		 }
	
}


