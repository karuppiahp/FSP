package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.DecimalDigitsInputFilter;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

public class EditingMenuActivity extends Activity implements OnClickListener {
	public void onStop() {
		if (Constant.isVendorAvailable.equals("notAvailable")) {
			finish();
		}
		super.onStop();

	}
	int btn_select=0,removeIndex=0;
   LinearLayout edit_Linear;
   RelativeLayout edit_Relative,relEdit1;
   int editIndex=0,edit_ChildIndex=0,addCunt,jsonValue;
	ImageButton backImg, cartBtn, btnSlideMenu,edit_MinusButton,btnAddSign,img_Btn;
	TextView textForHeader,text_editSize,text_editPrize;
	EditText editTxtForName, editTxtForQty, editTxtForFiOz, editTxtForPrice,edit_SizeValue,edit_PriceValue,
			editTxtForCategory, editTxtForSubCategory, editTxtForSku,
			editTextForItemDesc;
	CheckBox availableBtn, FeaturedBtn;
	ImageButton btnForDelete, btnForSave;
	boolean featured = true;
	String categoryId, price, name, desc, sku, parent, category, productId,feature,stockValue,fiozValue,value_ID,option_Id=" ",
			itemStatus, current,stordID,size_EditValue,price_EditValue,total_editItemSize,total_editItemPrize,total_Value_ID
			,editPage_size_Value,editPage_price_Val,newOptionID,total_Opiton_Id;
	SharedPreferences vendorSharedpreferences,customerPreference;
	Editor vendorPreferenceEditor,customerPrefEditor;
	  Button btn_EditValue,btn_Add;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// Intent returnIntent = new Intent();
			// returnIntent.putExtra("position", parent);
			//
			// setResult(RESULT_OK, returnIntent);
			finish();
		}

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editing_menu);

		categoryId = getIntent().getExtras().getString("id");
		price = getIntent().getExtras().getString("price");

		name = getIntent().getExtras().getString("name");
		desc = getIntent().getExtras().getString("desc");
		sku = getIntent().getExtras().getString("sku");
		itemStatus = getIntent().getExtras().getString("status");
		category = getIntent().getExtras().getString("category");
		productId = getIntent().getExtras().getString("product_id");
		feature= getIntent().getExtras().getString("featureval");
		stockValue= getIntent().getExtras().getString("stockval");
		fiozValue= getIntent().getExtras().getString("fiozval");
		Log.e("productId", productId);
		Log.e("Feature Value", feature);
		Log.e("Stock Value", stockValue);
		Log.e("Fioz Value", fiozValue);
		Log.e("sku Value", sku);
		parent = getIntent().getExtras().getString("parent");
		Log.e("parent", parent);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		edit_Linear = (LinearLayout)findViewById(R.id.editlistingLayout);
		editTxtForSku = (EditText) findViewById(R.id.editTextForItemSku);
		editTextForItemDesc = (EditText) findViewById(R.id.editTextForItemDesc);
		editTxtForName = (EditText) findViewById(R.id.editTextForItemName);
		editTxtForQty = (EditText) findViewById(R.id.editTextForItemQty);
		editTxtForFiOz = (EditText) findViewById(R.id.editTextForItemFiOz);
//		editTxtForPrice = (EditText) findViewById(R.id.editTextForItemPrice);
		editTxtForCategory = (EditText) findViewById(R.id.editTextForItemCategory);
		editTxtForSubCategory = (EditText) findViewById(R.id.editTextForItemSubCategory);
		availableBtn = (CheckBox) findViewById(R.id.imageForAvailable);
		FeaturedBtn = (CheckBox) findViewById(R.id.imageForFeatured);
		btnForDelete = (ImageButton) findViewById(R.id.btnForDelete);
		btnAddSign= (ImageButton) findViewById(R.id.editAddButton);
		btnForSave = (ImageButton) findViewById(R.id.btnForSaveChanges);
		backImg.setOnClickListener(this);
		
		vendorSharedpreferences = this.getSharedPreferences(
				"vendorPrefs", Context.MODE_PRIVATE);
		stordID = vendorSharedpreferences.getString("store_id", "");
		/*customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		customerPrefEditor=customerPreference.edit();
		stordID = customerPreference.getString("store_id", "");*/
		Log.e("stordID", stordID);
		
		if (parent.equals("Beer")) {
			Constant.selectedTabPosition = 2;
		} else if (parent.equals("Featured")) {
			Constant.selectedTabPosition = 0;
		} else if (parent.equals("Wine")) {
			Constant.selectedTabPosition = 1;
		} else if (parent.equals("Liquor")) {
			Constant.selectedTabPosition = 3;
		} else {
			Constant.selectedTabPosition = 4;
		}
				
		// availableBtn.setOnClickListener(this);
		// FeaturedBtn.setOnClickListener(this);
		btnForDelete.setOnClickListener(this);
		btnForSave.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		textForHeader.setText(name.toUpperCase());
		editTxtForName.setText(name);
		editTextForItemDesc.setText(desc);
		editTxtForFiOz.setText(fiozValue);
//		editTxtForPrice.setText("$" + price);
		
		/*editTxtForPrice.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				editTxtForPrice.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
						if (!s.toString().equals(current)) {

							editTxtForPrice.removeTextChangedListener(this);

							String cleanString = s.toString().replaceAll(
									"[$,.]", "");

							double parsed = Double.parseDouble(cleanString);
							String formatted = NumberFormat
									.getCurrencyInstance().format(
											(parsed / 100));

							current = formatted;
							editTxtForPrice.setText(formatted);
							editTxtForPrice.setSelection(formatted.length());

							editTxtForPrice.addTextChangedListener(this);
						}

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
					}

				});
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});*/
		
		editTxtForCategory.setText(parent);
		editTxtForSubCategory.setText(category);
		editTxtForSku.setText(sku);
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new GetItemQuantity().execute();
		} else {
			Utils.ShowAlert(EditingMenuActivity.this,
					Constant.networkDisconected);
		}

		if (stockValue.equals("1")) {
			availableBtn.setChecked(true);
		} else {
			availableBtn.setChecked(false);
		}
		  
		if(feature.equals("1")){
			FeaturedBtn.setChecked(true);
		}else{
			FeaturedBtn.setChecked(false);
		}
		
		availableBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (availableBtn.isChecked()) {
					itemStatus = "1";
				} else {
					itemStatus = "0";
				}
			}
		});
		Log.e("status", itemStatus);
		
		btnAddSign.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_select=0;
				final Dialog dialogbox = new Dialog(EditingMenuActivity.this);
				dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogbox.setContentView(R.layout.additem_alert_dialog);
			
				btn_Add = (Button) dialogbox.findViewById(R.id.btn_add);		
				btn_EditValue = (Button) dialogbox.findViewById(R.id.btn_edit);		
				edit_SizeValue= (EditText) dialogbox.findViewById(R.id.editsize);
				edit_PriceValue = (EditText) dialogbox.findViewById(R.id.editprice);
				
				edit_SizeValue.setFilters(new InputFilter[]{Constant.filter_sizeValue});
				edit_PriceValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(15,2)});								
				
				dialogbox.show();
				dialogbox.setCancelable(true);				
				
				btn_Add.setVisibility(View.VISIBLE);
				btn_EditValue.setVisibility(View.GONE);
		        
				btn_Add.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {			
						editPage_size_Value=edit_SizeValue.getText().toString().trim();
						editPage_price_Val	=edit_PriceValue.getText().toString().trim();
												
						if(editPage_size_Value.startsWith("0")||editPage_price_Val.startsWith("0")){
							Utils.ShowAlert(EditingMenuActivity.this,
									"Please enter valid price or size value.");
						}else if(editPage_size_Value.length()==0|| editPage_price_Val.length()==0){
							Utils.ShowAlert(EditingMenuActivity.this,
									"Please enter price or size value.");
						}
						else{
						
						final View edit_View = getLayoutInflater().inflate(R.layout.edit_item_adapter, null);
						text_editSize = (TextView) edit_View
								.findViewById(R.id.editsizetext);
						text_editPrize = (TextView) edit_View
								.findViewById(R.id.editpricetext);
						edit_MinusButton = (ImageButton) edit_View
								.findViewById(R.id.minusBtn);
						edit_Relative=(RelativeLayout)edit_View
								.findViewById(R.id.editmainRelative);
						Log.e("Inside Adding view",""+editPage_size_Value+" "+editPage_price_Val);
						text_editSize.setText(editPage_size_Value);
						text_editPrize.setText(editPage_price_Val);					
						
						edit_MinusButton
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
						     
								removeIndex = edit_Linear.indexOfChild(edit_View);
					//			img_Btn=(ImageButton)edit_Linear.getChildAt(removeIndex);	
							    Log.e("Selected Minus button position",""+removeIndex);
							   
									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditingMenuActivity.this);
									alertDialogBuilder.setTitle("Message");
									 alertDialogBuilder.setMessage("Are you sure you want to delete price and size?")
									 		.setCancelable(false)						
											.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();				
													edit_Linear.removeViewAt(removeIndex);
												}
											})
											.setNegativeButton("No",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {					
												dialog.cancel();
										  	    }
										     });
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();										
							}
						});
												
						edit_Relative.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
							    	btn_select=1;
									final Dialog dialogbox = new Dialog(EditingMenuActivity.this);
									dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
									dialogbox.setContentView(R.layout.additem_alert_dialog);
								
									btn_Add = (Button) dialogbox.findViewById(R.id.btn_add);		
									btn_EditValue = (Button) dialogbox.findViewById(R.id.btn_edit);		
									edit_SizeValue= (EditText) dialogbox.findViewById(R.id.editsize);
									edit_PriceValue = (EditText) dialogbox.findViewById(R.id.editprice);
									
									edit_SizeValue.setFilters(new InputFilter[]{Constant.filter_sizeValue});
									edit_PriceValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(15,2)});
									
									dialogbox.show();
									dialogbox.setCancelable(true);									

									btn_Add.setVisibility(View.GONE);
									btn_EditValue.setVisibility(View.VISIBLE);
									
									removeIndex = edit_Linear.indexOfChild(edit_View);
									relEdit1=(RelativeLayout)edit_Linear.getChildAt(removeIndex);									
									text_editSize = (TextView) relEdit1.findViewById(R.id.editsizetext);
									text_editPrize = (TextView) relEdit1.findViewById(R.id.editpricetext);
									
									edit_SizeValue.setText(text_editSize.getText().toString());
									edit_PriceValue.setText(text_editPrize.getText().toString());
									
									btn_EditValue.setOnClickListener(new OnClickListener() {									
										@Override
										public void onClick(View v) {
											editPage_size_Value=edit_SizeValue.getText().toString().trim();
											editPage_price_Val=edit_PriceValue.getText().toString().trim();
											Log.e("Inside btn Edit click  view",""+editPage_size_Value+" "+editPage_price_Val);
									
											if(editPage_price_Val.startsWith("0")||editPage_size_Value.startsWith("0")){
												Utils.ShowAlert(EditingMenuActivity.this,
														"Please enter valid price or size value.");
											}else if(editPage_price_Val.length()==0||editPage_size_Value.length()==0){
												Utils.ShowAlert(EditingMenuActivity.this,
														"Please enter price or size value.");
											}else{
												text_editSize.setText(editPage_size_Value);
												text_editPrize.setText(editPage_price_Val);
											dialogbox.cancel();
											}
										}
									});									
							}
						});
						
						if(btn_select==0){
							Log.e("Inside If","Loop");
							edit_Linear.addView(edit_View);					
						  
						dialogbox.cancel();
						}else{
							Log.e("Inside Else","Loop");
						//	text_Size.setText(size_Value);
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
			finish();
		}

		// if (v.getId() == R.id.imageForAvailable) {
		// if (status.equals(1)) {
		// // available = false;
		// availableBtn.setImageResource(R.drawable.available_on);
		// } else {
		// // available = true;
		// availableBtn.setImageResource(R.drawable.available_off);
		// }
		// }

		// if (v.getId() == R.id.imageForFeatured) {
		// if (featured == true) {
		// featured = false;
		// FeaturedBtn.setImageResource(R.drawable.available_on);
		// } else {
		// featured = true;
		// FeaturedBtn.setImageResource(R.drawable.available_off);
		// }
		// }

		if (v.getId() == R.id.btnForDelete) {
			ShowDeleteAlert("Are you sure, you want to delete this item.");
		}

		if (v.getId() == R.id.btnForSaveChanges) {
			newOptionID="";
			addCunt=edit_Linear.getChildCount();
			Log.e("Newly Added Count",""+addCunt);
			Log.e("jsonValue Array Count ",""+jsonValue);
		
			int xx=addCunt-jsonValue;
			Log.e("After Subtraction final Count",""+xx);
			for(int z=0;z<xx;z++){
				if(z==0){
					newOptionID="x";
					Log.e("New IF Stmt option ID",newOptionID);
				}else{
					newOptionID=newOptionID+","+"x";
					Log.e("New Else IF Stmt option ID",newOptionID);
				}				
			}
			
			if(xx==0){
				total_Opiton_Id=total_Value_ID;
			}else{
			   total_Opiton_Id=total_Value_ID+","+newOptionID;
			}
			
			Log.e("List Linear child count",""+edit_Linear.getChildCount());
			for(int l=0;l<edit_Linear.getChildCount();l++){	
				relEdit1=(RelativeLayout)edit_Linear.getChildAt(l);
				text_editSize = (TextView)relEdit1.findViewById(R.id.editsizetext);				
				Log.e("Inside for size looping",""+l);				
				if(l==0){
					total_editItemSize=text_editSize.getText().toString();	
				}else{
					total_editItemSize=total_editItemSize+","+text_editSize.getText().toString();
				}				
	    	}
									
			for(int m=0;m<edit_Linear.getChildCount();m++){	
				relEdit1=(RelativeLayout)edit_Linear.getChildAt(m);
				text_editPrize = (TextView)relEdit1.findViewById(R.id.editpricetext);				
				Log.e("Inside for prize looping",""+m);				
				if(m==0){
					total_editItemPrize=text_editPrize.getText().toString();	
				}else{
					total_editItemPrize=total_editItemPrize+","+text_editPrize.getText().toString();
				}				
	    	}		
			
			if (editTxtForCategory.getText().toString().length() > 0
					&& editTxtForFiOz.getText().toString().length() > 0
					&& editTxtForName.getText().toString().length() > 0
		//			&& editTxtForPrice.getText().toString().length() > 0
					&& editTxtForQty.getText().toString().length() > 0
					&& editTxtForSku.getText().toString().length() > 0
					&& editTxtForSubCategory.getText().toString().length() > 0
					&& editTextForItemDesc.getText().toString().length() > 0) {
				
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					 if(edit_Linear.getChildCount()==0){
						 Utils.ShowAlert(EditingMenuActivity.this,"One Item should be available in the list."); 
					 }else{
						Log.e("Success", "Edit Menu Activity");
						Log.e("Final Size Value ",""+total_editItemSize);
						Log.e("Final Prize Value ",""+total_editItemPrize);			
						Log.e(" Total_Value_ID when button save is selected",total_Opiton_Id);
						Log.e("Option_Id Value ",""+option_Id);						
					new UpdateItem().execute();
					 }
				} else {
					Utils.ShowAlert(EditingMenuActivity.this,
							Constant.networkDisconected);
				}
			} else {
				Utils.ShowAlert(EditingMenuActivity.this,
						"Please enter data for all fields.");
			}
		}
	}

	public class DeleteItem extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error;

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {

					if (parent.equals("Beer")) {
						Constant.selectedTabPosition = 2;
					} else if (parent.equals("Featured")) {
						Constant.selectedTabPosition = 0;
					} else if (parent.equals("Wine")) {
						Constant.selectedTabPosition = 1;
					} else if (parent.equals("Liquor")) {
						Constant.selectedTabPosition = 3;
					} else {
						Constant.selectedTabPosition = 4;
					}
					// finish();
					startActivity(new Intent(EditingMenuActivity.this,
							TabsFragmentActivity.class));
				} else {
					Utils.ShowAlert(EditingMenuActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(EditingMenuActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
			nameValuePair.add(new BasicNameValuePair("product_id", productId));
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.vendorDeleteItem()).getJSONObjectfromURL(
					RequestType.POST, nameValuePair);
			Log.e("vendor delete item", jsonObj + "");
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

	public class GetItemQuantity extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error, itemQuantity = "";
		JSONObject jsonObj;
		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {

				if (status.equals("1")) {
					String quantity = jsonObj.getString("quantity");
					JSONArray jarr = new JSONArray(quantity);
					itemQuantity = jarr.getJSONObject(0).getString("qty");
					editTxtForQty.setText(itemQuantity);
					
					String optionID = jsonObj.getString("option_id");
					JSONArray jarr1 = new JSONArray(optionID);
					option_Id = jarr1.getJSONObject(0).getString("option_id");
					
					String sizeprice = jsonObj.getString("Size");
					JSONArray jarrPrice = new JSONArray(sizeprice);
					Log.v("Total Length from response",""+jarrPrice.length());
					jsonValue=jarrPrice.length();
					for (int k = 0; k < jarrPrice.length(); k++) {
						JSONObject myPriceObj = jarrPrice.getJSONObject(k);
						final View edit_View =getLayoutInflater()
								.inflate(R.layout.edit_item_adapter, null);
						text_editSize = (TextView) edit_View
								.findViewById(R.id.editsizetext);
						text_editPrize = (TextView) edit_View
								.findViewById(R.id.editpricetext);
						edit_MinusButton = (ImageButton) edit_View
								.findViewById(R.id.minusBtn);
						edit_Relative=(RelativeLayout)edit_View
								.findViewById(R.id.editmainRelative);
						
						text_editSize.setText(myPriceObj.getString("title"));
						text_editPrize.setText(myPriceObj.getString("price"));			    		
				    	value_ID=myPriceObj.getString("value_id");
						Log.e(" Value id in edit menu activity Are",value_ID);						
						
						if(k==0){
							total_Value_ID=value_ID;
						}else{
							total_Value_ID=total_Value_ID+","+value_ID;
						}
						
						Log.e(" Total_Value_ID in edit menu activity Are",total_Value_ID);
						
						edit_MinusButton.setTag(myPriceObj.getString("value_id")
								+ ":" + k);
	
						edit_Relative.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								
								final Dialog dialogbox = new Dialog(EditingMenuActivity.this);
								dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialogbox.setContentView(R.layout.additem_alert_dialog);
															
								btn_EditValue = (Button) dialogbox.findViewById(R.id.btn_edit);		
								edit_SizeValue= (EditText) dialogbox.findViewById(R.id.editsize);
								edit_PriceValue = (EditText) dialogbox.findViewById(R.id.editprice);
								
								edit_SizeValue.setFilters(new InputFilter[]{Constant.filter_sizeValue});
								edit_PriceValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(15,2)});								
								
								dialogbox.show();
								dialogbox.setCancelable(true);				
								
								btn_EditValue.setVisibility(View.VISIBLE);
								
								edit_ChildIndex = edit_Linear.indexOfChild(edit_View);
								relEdit1=(RelativeLayout)edit_Linear.getChildAt(edit_ChildIndex);
								
								text_editSize = (TextView) relEdit1.findViewById(R.id.editsizetext);
								text_editPrize = (TextView) relEdit1.findViewById(R.id.editpricetext);		
								
								edit_SizeValue.setText(text_editSize.getText().toString());
								edit_PriceValue.setText(text_editPrize.getText().toString());
								
								btn_EditValue.setOnClickListener(new OnClickListener() {									
									@Override
									public void onClick(View v) {
										size_EditValue=edit_SizeValue.getText().toString().trim();
										price_EditValue=edit_PriceValue.getText().toString().trim();
										Log.e("Inside btn Edit click  view",""+size_EditValue+" "+price_EditValue);		
										if(price_EditValue.startsWith("0")||price_EditValue.startsWith(".")||price_EditValue.equals(" ")
												||size_EditValue.startsWith("0") || size_EditValue.equals(" ")||size_EditValue.startsWith(".")){
											Utils.ShowAlert(EditingMenuActivity.this,
													"Please enter valid price or size value.");
										}else{
										text_editSize.setText(size_EditValue);
										text_editPrize.setText(price_EditValue);
										dialogbox.cancel();
										}
									}
								});									
							}
						});
						
						edit_MinusButton
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								boolean isOnline = Utils.isOnline();
								if (isOnline) {
									String tag = v.getTag().toString();
									final String IdValue = tag.split(":")[0];
									
									editIndex = edit_Linear.indexOfChild(edit_View);								
									Log.e("Edit Value id & position",
											IdValue + " & "
													+ editIndex);

									AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditingMenuActivity.this);
									alertDialogBuilder.setTitle("Message");
									 alertDialogBuilder.setMessage("Are you sure you want to delete price and size?")
									 		.setCancelable(false)						
											.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													dialog.cancel();
													new DeleteItemList().execute(IdValue);
												}
											})
											.setNegativeButton("No",new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,int id) {					
												dialog.cancel();
										  	    }
										     });
											AlertDialog alertDialog = alertDialogBuilder.create();
											alertDialog.show();											
								}else {
									Utils.ShowAlert(EditingMenuActivity.this,
											Constant.networkDisconected);
								}
							}
						});
						edit_Linear.addView(edit_View);
					}				
					
				} else {
					Utils.ShowAlert(EditingMenuActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(EditingMenuActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
	//		nameValuePair.add(new BasicNameValuePair("product_sku", sku));
			nameValuePair.add(new BasicNameValuePair("product_id", productId));
			jsonObj = new ServerResponse(
					UrlGenerator.vendorGetQuantity()).getJSONObjectfromURL(
					RequestType.POST, nameValuePair);
			Log.e("Edit Menu Activity response",""+jsonObj);
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
//						String quantity = jsonObj.getString("quantity");
//						JSONArray jarr = new JSONArray(quantity);
//						itemQuantity = jarr.getJSONObject(0).getString("qty");
						
						/*	String optionID = jsonObj.getString("option_id");
						JSONArray jarr1 = new JSONArray(optionID);
						option_Id = jarr1.getJSONObject(0).getString("option_id");
						
						String sizeprice = jsonObj.getString("Size");
						JSONArray jarrPrice = new JSONArray(sizeprice);
						for (int k = 0; k < jarrPrice.length(); k++) {
							JSONObject myPriceObj = jarrPrice.getJSONObject(k);
							final View listing_View =getLayoutInflater()
									.inflate(R.layout.edit_item_adapter, null);
						}*/
						
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
	
	//Delete Selected Procuct List	
		public class DeleteItemList extends AsyncTask<String, String, String> {
			ProgressDialog dialog;
			JSONObject jsondelete;
			String deletestatus = "";
			
			protected void onPreExecute() {
				super.onPreExecute();
				this.dialog = new ProgressDialog(EditingMenuActivity.this);
				this.dialog.setMessage("Loading..");
				this.dialog.show();
				this.dialog.setCancelable(false);
			}

			
			@Override
			protected String doInBackground(String... arg0) {
				Log.e("Value Id before do in background", arg0[0]);
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);				
				nameValuePair.add(new BasicNameValuePair("value_id",arg0[0]));
				jsondelete = new ServerResponse(UrlGenerator.vendorDeleteList()).getJSONObjectfromURL(
						RequestType.POST, nameValuePair);
				Log.e("Delete List response",""+jsondelete);
				return null;
			}

			protected void onPostExecute(String result) {
				
				try {
					deletestatus=jsondelete.getString("status");					 
					if (deletestatus.equals("1")) {	
						edit_Linear.removeViewAt(editIndex);						
						/*Utils.ShowAlert(EditingMenuActivity.this,
								"Product List Deleted.");*/
						AlertDialog.Builder alertPop = new AlertDialog.Builder(EditingMenuActivity.this);
						alertPop.setMessage("Product List Deleted.");
						alertPop.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {				
								dialog.cancel();
								boolean isOnlines = Utils.isOnline();
								if (isOnlines) {
									edit_Linear.removeAllViews();
									new GetItemQuantity().execute();
								} else {
									Utils.ShowAlert(EditingMenuActivity.this,
											Constant.networkDisconected);
								}
							}
						});
						AlertDialog alert = alertPop.create();
						alert.show();						
						/*jsonValue=edit_Linear.getChildCount();
						Log.e("After Deleting a selected list",""+jsonValue);*/
					} else {
						Utils.ShowAlert(EditingMenuActivity.this,
								"Option should have at least one value. Can not delete last value.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}

	public class UpdateItem extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error;

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					if (parent.equals("Beer")) {
						Constant.selectedTabPosition = 2;

					} else if (parent.equals("Featured")) {
						Constant.selectedTabPosition = 0;
					} else if (parent.equals("Wine")) {
						Constant.selectedTabPosition = 1;
					} else if (parent.equals("Liquor")) {
						Constant.selectedTabPosition = 3;
					} else {
						Constant.selectedTabPosition = 4;
					}
					finish();
					startActivity(new Intent(EditingMenuActivity.this,
							TabsFragmentActivity.class));
				} else {
					Utils.ShowAlert(EditingMenuActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(EditingMenuActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
			String product_name = editTxtForName.getText().toString().trim();
			String product_stock = itemStatus;
			/*String product_price = editTxtForPrice.getText().toString().trim()
					.replace("$", "");*/
			String product_qty = editTxtForQty.getText().toString().trim();
			String product_sku = editTxtForSku.getText().toString().trim();
			String desc = editTextForItemDesc.getText().toString().trim();
			String fioz = editTxtForFiOz.getText().toString().trim();
			Log.e("product_name", product_name);
			Log.e("product_stock", product_stock);
//			Log.e("product_price", product_price);
			Log.e("product_qty", product_qty);
			Log.e("product_sku **************", product_sku);
			Log.e("categoryId", categoryId);
			Log.e("Product Id", productId);
			Log.e("desc", desc);
			Log.e("fioz", fioz);
			nameValuePair.add(new BasicNameValuePair("product_name",
					product_name));
			nameValuePair.add(new BasicNameValuePair("product_sku", productId));					
			nameValuePair
					.add(new BasicNameValuePair("category_id", categoryId));
			nameValuePair.add(new BasicNameValuePair("product_stock",
					product_stock));
			/*nameValuePair.add(new BasicNameValuePair("product_price",
					product_price));*/
			nameValuePair.add(new BasicNameValuePair("product_price","0"));
			nameValuePair.add(new BasicNameValuePair("product_featured", "0"));
			nameValuePair
					.add(new BasicNameValuePair("product_qty", product_qty));

			nameValuePair.add(new BasicNameValuePair("product_description",
					desc));
			nameValuePair.add(new BasicNameValuePair("store_id",stordID));
			nameValuePair.add(new BasicNameValuePair("product_fl_oz",fioz));
			
			Log.e("Final Size Value ",""+total_editItemSize);
			Log.e("Final Prize Value ",""+total_editItemPrize);			
			Log.e(" Total_Value_ID when button save is selected",total_Opiton_Id);
			Log.e("Option_Id Value ",""+option_Id);
			
			nameValuePair.add(new BasicNameValuePair("custom_option_title",total_editItemSize));
			nameValuePair.add(new BasicNameValuePair("custom_option_price",total_editItemPrize));
	        nameValuePair.add(new BasicNameValuePair("custom_opiton_value_id",total_Opiton_Id));
	        nameValuePair.add(new BasicNameValuePair("custom_option_id",option_Id));
	        nameValuePair.add(new BasicNameValuePair("product_id", productId));			
	        
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.vendorUpdateItem()).getJSONObjectfromURL(
					RequestType.POST, nameValuePair);
			Log.e("Edit Menu Update Response",""+jsonObj);
			
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
				//		Utils.ShowAlert(EditingMenuActivity.this,"Item Updated Successfully");
						// Toast.makeText(getApplicationContext(),
						// "Updated successfully!", Toast.LENGTH_LONG).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public void ShowDeleteAlert(String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(
				EditingMenuActivity.this);
		adb.setMessage(message);
		adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					new DeleteItem().execute();

				} else {
					Utils.ShowAlert(EditingMenuActivity.this,
							Constant.networkDisconected);
				}

			}
		});
		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});
		AlertDialog alert = adb.create();
		alert.show();
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
	
}
