package com.eliqxir.tabhostfragments;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.adapter.TabHostAdapter;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
//import com.facebook.android.Util;

public class BeerTab extends Fragment {
	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	int i = 0;
	ArrayList<HashMap<String, String>> subCategoriesHeader = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> subCategoriesArray = new ArrayList<HashMap<String, String>>();
	// HashMap<String, String> hashMapKeys = new HashMap<String, String>();
	HashMap<String, String> hashMapsubCategory = new HashMap<String, String>();
	String subCategoryId, subCategoryName, subCategoryDesc, subCategoryPrice,feature_status,stock_status,fioz_status,
			stordID, subCategorysku, subCategoryParent, subCategoryCategory,subCategorytax,
			subCategoryproductId, subCategorystatus, menuPref,storeName,storeAddress,storeid,
			dayOfTheWeek,currentTime,tax="0",delivery_Status;
	ArrayList<String> subcatList = new ArrayList<String>();
	View v;
	SharedPreferences vendorSharedpreferences;
	Editor vendorPreferenceEditor;
	SharedPreferences customerPreference,zipPreference,storeclosedPreference;
	SharedPreferences.Editor customerPrefEditor,zipPrefEditor,storeclosedPreferenceEditor;
	ArrayList<String> catIdList = new ArrayList<String>();
	ArrayList<String> catList = new ArrayList<String>();
	JSONObject jsonObj;
	ViewPager pager;
	PagerTabStrip pagerTabStrip;
	TextView txtNoItem;
	int time_format = 0;
	LocationManager lm;
	
    public static String vendor_Response="";
	
	//Static Items
	static
	{
		vendor_Response="";
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.trackError(getActivity());

		if (container == null) {
			return null;
		}

		v = inflater.inflate(R.layout.tab_beer, container, false);
		
		 pager = (ViewPager) v
				.findViewById(R.id.mypagerFeatured);

		 pagerTabStrip = (PagerTabStrip) v
				.findViewById(R.id.pager_title_stripFeatured);
		  txtNoItem=(TextView)v.findViewById(R.id.txtNoProduct);
		  txtNoItem.setVisibility(View.GONE);
		  pager.setVisibility(View.VISIBLE);
		
	    storeclosedPreference=getActivity().getSharedPreferences("storeclosedPrefs", Context.MODE_PRIVATE);
	    storeclosedPreferenceEditor=storeclosedPreference.edit();		
	    
		vendorSharedpreferences = getActivity().getSharedPreferences(	"vendorPrefs", Context.MODE_PRIVATE);
		stordID = vendorSharedpreferences.getString("store_id", "");
 		customerPreference=getActivity().getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
//		customerPrefEditor=customerPreference.edit();
//		stordID = customerPreference.getString("store_id", "");
		Log.e("stordID in Beer Tab ###############", stordID);
		zipPreference=getActivity().getSharedPreferences("zipprefs", Context.MODE_PRIVATE);
		zipPrefEditor=zipPreference.edit();		
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();
		 dayOfTheWeek = sdf.format(d);
		 Log.e("dayOfTheWeek",dayOfTheWeek);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

		String cur_date = dateFormat.format(new Date());
		Log.v(" cur date", cur_date);
		String sp[]=cur_date.split(" ");
		
		currentTime=sp[1]+sp[2];
		Log.e("current time",currentTime);

		menuPref = SessionStore.getSlidinMenu(getActivity()
				.getApplicationContext());
		
		//		
		lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
		      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			alertdialog();
		}else{		
		//		
		new AsyncTask<Void, Void, Boolean>() {
			ProgressDialog dialog;
			String status="notAvailable",error;
			@Override
			public void onPreExecute() {
				super.onPreExecute();
				this.dialog = new ProgressDialog(getActivity());
				this.dialog.setMessage("Loading..");
				this.dialog.show();
				this.dialog.setCancelable(false);
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				if(menuPref==null)
				{
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair(
							"customer_zipcode", Constant.zipCode));
					nameValuepair.add(new BasicNameValuePair(
							"current_day", dayOfTheWeek));
					nameValuepair.add(new BasicNameValuePair(
							"current_time", currentTime));
					jsonObj = new ServerResponse(
							UrlGenerator.getCustomerBrowse())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
					Log.e("Customer Browse Response",""+jsonObj);
				}
				else if (menuPref.equals("vendor")) {
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair("store_id",
							stordID));
					jsonObj = new ServerResponse(UrlGenerator.vendorBrowse())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
					Log.e("Vendor Side Response",""+jsonObj);
					vendor_Response=jsonObj.toString();
				} else {
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair(
							"customer_zipcode", Constant.zipCode));
					nameValuepair.add(new BasicNameValuePair(
							"current_day", dayOfTheWeek));
					nameValuepair.add(new BasicNameValuePair(
							"current_time", currentTime));
					jsonObj = new ServerResponse(
							UrlGenerator.getCustomerBrowse())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
					Log.e("Customer Browse Response",""+jsonObj);
				}
				try {
					if (jsonObj != null) {
						
						 try{
							    File myFile = new File("/sdcard/tsxt.txt");
							    myFile.createNewFile();
							    FileOutputStream fOut = new FileOutputStream(myFile);
							    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
							    myOutWriter.append(jsonObj.toString());
							    myOutWriter.close();
							    fOut.close();
							}catch(Exception e){}
						 status = jsonObj.getString("status");
						Log.e("vendor browse api status", status);
						if (status.equals("1")) {
							storeclosedPreferenceEditor.clear().commit();
							zipPrefEditor.putString("statusValue","1").commit();
							String zipcode;
							if(menuPref==null)
							{
								 storeid=jsonObj.getString("store_id");
								 delivery_Status=jsonObj.getString("delivery");
								 storeName=jsonObj.getString("store_name");
								 storeAddress=jsonObj.getString("store_address");
								 zipcode=jsonObj.getString("zipcode");
								 Log.e("zipcode",zipcode);
								Log.e("store_name",storeName);
								Log.e("store_address",storeAddress);
								Log.e("delivery_Status",delivery_Status);
								customerPrefEditor=customerPreference.edit();
								customerPrefEditor.putString("store_name", storeName);
								customerPrefEditor.putString("zipcode", zipcode);
								customerPrefEditor.putString("store_address", storeAddress);
								customerPrefEditor.putString("store_id", storeid);
								customerPrefEditor.putString("delivery", delivery_Status);
								customerPrefEditor.commit();
							}
							else if (menuPref.equals("vendor")) {
								
							}
							else
							{
								delivery_Status=jsonObj.getString("delivery");
								storeid=jsonObj.getString("store_id");
								 storeName=jsonObj.getString("store_name");
								 storeAddress=jsonObj.getString("store_address");
								 zipcode=jsonObj.getString("zipcode");
								 Log.e("zipcode",zipcode);
								Log.e("store_name",storeName);
								Log.e("store_address",storeAddress);
								customerPrefEditor=customerPreference.edit();
								customerPrefEditor.putString("store_name", storeName);
								customerPrefEditor.putString("zipcode", zipcode);
								customerPrefEditor.putString("store_address", storeAddress);
								customerPrefEditor.putString("store_id", storeid);
								customerPrefEditor.putString("delivery", delivery_Status);
								customerPrefEditor.commit();
							}
							subcatList.clear();
							catIdList.clear();
							catList.clear();
							subCategoriesHeader.clear();
							subCategoriesArray.clear();
							Constant.subCategoriesHeader.clear();
							Constant.subCategoriesArray.clear();

							JSONArray subCategoryArray = jsonObj
									.getJSONArray("sub_category");
							for (int k = 0; k < subCategoryArray.length(); k++) {
								JSONObject job3 = subCategoryArray
										.getJSONObject(k);
								@SuppressWarnings("unchecked")
								Iterator<String> keys1 = job3.keys();
								while (keys1.hasNext()) {
									String key = keys1.next();
									String value = job3.getString(key);
									// Log.e("subcategory key",
									// key);
									// Log.e("subcategory value",
									// value);
									if (key.equals("category_id")) {
										catIdList.add(key + "~" + value);

									} else {
										// Log.e("comes to else","else ");
										catList.add(key + "~" + value);
									}
									// tmp1=tmp1.concat(key).concat(value);

								}
								// Log.e("tmp1 >>",tmp1);
								// Log.e("catList size",catList.size()+"");
								// Log.e("catidList size",catIdList.size()+"");
								for (int l = 0; l < catList.size(); l++) {
									String split[] = catList.get(l).split("~");
							 Log.e("Split[0] 2222222222222",split[0]);
									if (split[0].equals("Beer")) {
										Log.e("Category Beer Condition", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
						//				Log.e("catId", catId);
										Constant.beerhashMapHeader.put(
												split[1], catId);
										HashMap<String, String> hashMapHeader = new HashMap<String, String>();
										hashMapHeader.put("header", split[1]);
										if (subCategoriesHeader
												.contains(hashMapHeader)) {

										} else {
											subCategoriesHeader
													.add(hashMapHeader);
											Constant.subCategoriesHeader
													.add(hashMapHeader);
										}
									} else if (split[0].equals("Featured")) {
										Log.e("Category Beer Featured Condition", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
							//			Log.e("catId", catId);
										Constant.featuredhashMapHeader.put(
												split[1], catId);

									} else if (split[0].equals("Liquor")) {
										Log.e("Category Beer Liquor Condition", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
					//					Log.e("catId", catId);
										Constant.liquorhashMapHeader.put(
												split[1], catId);

									} else if (split[0].equals("Extras")) {
										Log.e("Category Beer Mixors condition", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
						//				Log.e("catId", catId);
										Constant.mixerhashMapHeader.put(
												split[1], catId);

									} else if (split[0].equals("Wine")) {
										Log.e("Category Beer Wine condition", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
							//			Log.e("catId", catId);
										Constant.winehashMapHeader.put(
												split[1], catId);

									}
								}
							}
							JSONObject pdtJsonObj = jsonObj
									.getJSONObject("products");

							@SuppressWarnings("unchecked")
							Iterator<String> keys = pdtJsonObj.keys();
							while (keys.hasNext()) {
								String key = keys.next();
							//	String value = pdtJsonObj.getString(key);
								// Log.e("product key", key);
								// Log.e("product value", value);

								subcatList.add(key);

							}

							for (int i = 0; i < subcatList.size(); i++) {
								String tmp = pdtJsonObj.getString(subcatList
										.get(i));
			//					Log.e("tmp !!!!",tmp);
								JSONArray jsonArray1 = new JSONArray(tmp);
								// Log.e("jsonArray1", jsonArray1 + "");
								for (int j = 0; j < jsonArray1.length(); j++) {
									JSONObject job2 = jsonArray1
											.getJSONObject(j);
									String category = job2
											.getString("category");
									Log.e("Parent Category Value In Beer Tab",job2.getString("parent_category"));
									Log.e("Category Value In Beer Tab",category);
									Log.e("Sub Category List Value In Beer Tab",subcatList.get(i));
									if (job2.getString("parent_category")
											.equals("Beer")&& category.equals(subcatList
													.get(i))) {

										// Log.e("beer subcaste ",
										// subcatList.get(i));
										// if (j == 0) {
										// HashMap<String, String> hashMapHeader
										// = new HashMap<String, String>();
										//
										// // Log.e("subcatList.get(i)",
										// // subcatList.get(i));
										// hashMapHeader.put("header",
										// subcatList.get(i));
										// subCategoriesHeader
										// .add(hashMapHeader);
										// Constant.subCategoriesHeader
										// .add(hashMapHeader);
										// }
									//	String parentId=job2.getString("id");
										if(menuPref==null)
										{
											tax=job2
													.getString("tax");
											tax=tax.replace("%", "");
											customerPrefEditor=customerPreference.edit();
											customerPrefEditor.putString("tax",tax.trim());
											customerPrefEditor.commit();
										}
										else if (menuPref.equals("vendor")) {
											
										}
										else
										{
											tax=job2
													.getString("tax");
											tax=tax.replace("%", "");
											customerPrefEditor=customerPreference.edit();
											
											customerPrefEditor.putString("tax",tax.trim());
											customerPrefEditor.commit();
										}
										
										String id = job2
												.getString("category_id");
										String name = job2.getString("name");
										String description = job2
												.getString("desc");
										String price = job2.getString("price");
										String sku = job2.getString("sku");
										String parent_category = job2
												.getString("parent_category");
										category = job2
												.getString("category");

										subCategoryId = subcatList.get(i) + ">"
												+ id;
										subCategoryName = subcatList.get(i)
												+ ">" + name;
										subCategoryDesc = subcatList.get(i)
												+ ">" + description;
										subCategoryPrice = subcatList.get(i)
												+ ">" + price;

										subCategorysku = subcatList.get(i)
												+ ">" + sku;
										subCategoryParent = subcatList.get(i)
												+ ">" + parent_category;
										subCategoryCategory = subcatList.get(i)
												+ ">" + category;
										String productId = job2.getString("id");
										subCategoryproductId = subcatList
												.get(i) + ">" + productId;

										String availableStatus = job2.getString("status");
										subCategorystatus = subcatList.get(i) + ">" + availableStatus;
										subCategorytax=subcatList.get(i) + ">" + tax;
										
										//Added on April22nd , April27th and 28th
										String featuredStatus = job2
												.getString("featured");
										feature_status = subcatList.get(i)
												+ ">" + featuredStatus;											
									
										String stockStatus = job2
												.getString("stock");
										stock_status = subcatList.get(i)
												+ ">" + stockStatus;
										
										String fiozStatus = job2
												.getString("fl_oz");
										fioz_status = subcatList.get(i)
												+ ">" + fiozStatus;
										//
										
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("id", subCategoryId);
										hashMap.put("name", subCategoryName);
										hashMap.put("description",subCategoryDesc);
										hashMap.put("price", subCategoryPrice);
										hashMap.put("product_id",subCategoryproductId);
										hashMap.put("status", subCategorystatus);
										hashMap.put("fl_oz", fioz_status);      			//Added on April 28th
										hashMap.put("stock", stock_status);      			//Added on April 27th
										hashMap.put("featured", feature_status);      //Added on April 22nd
										hashMap.put("sku", subCategorysku);
										hashMap.put("parent", subCategoryParent);
										hashMap.put("category",subCategoryCategory);
										hashMap.put("tax", subCategorytax);
										subCategoriesArray.add(hashMap);
										Constant.subCategoriesArray.add(hashMap);
									}
								}
							}
						}
						else if(status.equals("0")){
							 error=jsonObj.getString("Error");
							 Log.e("Error Value 111111111111",error);
							 if(error.equals("this store is closed.")){
								 String s="storeclosed";
								 Log.e("Error Value if stmt",s);
								 storeclosedPreferenceEditor.putString("storeval",s).commit();
							 }
							/* else{
								 Log.e("Error Value 2222",error);
							 }*/
						}
					} 
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}

			@SuppressWarnings("deprecation")
			@Override
			protected void onPostExecute(Boolean result) {
				dialog.dismiss();
				if(menuPref==null)
				{
					Constant.userAccessType = "enduser";
				}
				else if (menuPref.equals("vendor")) {
					Constant.userAccessType = "vendor";
				}
				else
				{
					Constant.userAccessType = "enduser";
				}
				
				if (subCategoriesHeader.size() > 0) {
				
					 txtNoItem.setVisibility(View.GONE);
					  pager.setVisibility(View.VISIBLE);
					pagerTabStrip.setDrawFullUnderline(false);
					pagerTabStrip.setTabIndicatorColor(Color
							.parseColor("#fbb03b"));
					for (int i = 0; i < pagerTabStrip.getChildCount(); ++i) {
						View nextChild = pagerTabStrip.getChildAt(i);
						if (nextChild instanceof TextView) {
							TextView textViewToConvert = (TextView) nextChild;
							Typeface fontStyle = Typeface.createFromAsset(
									getActivity().getAssets(),
									"OpenSans-Semibold_0.ttf");
							textViewToConvert.setTypeface(fontStyle);
						}
					}

					/** Getting fragment manager */
					FragmentManager fm = getChildFragmentManager();
					if (Integer.decode(Build.VERSION.SDK) > 17) {
						/** Instantiating FragmentPagerAdapter */
						TabHostAdapter pagerAdapter = new TabHostAdapter(fm);

						/** Setting the pagerAdapter to the pager object */
						pager.setAdapter(pagerAdapter);
						
						if(Constant.currentPager.length() > 0) {
							Log.i("Constant.currentPager is", ""+Constant.currentPager);
							pager.setCurrentItem(Integer.parseInt(Constant.currentPager));
							Constant.currentPager = "";
						}
					} else {
						TabHostAdapter pagerAdapter = new TabHostAdapter(fm,
								subCategoriesHeader, subCategoriesArray);

						/** Setting the pagerAdapter to the pager object */
						pager.setAdapter(pagerAdapter);
						
						if(Constant.currentPager.length() > 0) {
							pager.setCurrentItem(Integer.parseInt(Constant.currentPager));
							Constant.currentPager = "";
						}
					}
				} else {
					
					pager.setVisibility(View.GONE);
					txtNoItem.setVisibility(View.VISIBLE);
					txtNoItem.setText("No Products available for Beer.");
//					Utils.ShowAlert(getActivity(),
//							"No Products available for Beer.");
				}
				 if(status.equals("notAvailable"))
					{
					 Utils.ShowAlert(getActivity(), Constant.networkDisconected);
					}
				else if(status.equals("0"))
				{
				 Log.e("Error Value 44444444",error);
					Utils.ShowAlert(getActivity(), error);
					zipPrefEditor.putString("statusValue",status).commit();
				}
				
			}
		}.execute();
	}	

		return v;
	}
	
	public void alertdialog(){
		 // Build the alert dialog
		  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		  builder.setTitle("Location Services Not Active");
		  builder.setMessage("Hey! Turn on your GPS and Location Services");
		  builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialogInterface, int i) {
		    // Show location settings when the user acknowledges the alert dialog
		    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		    startActivity(intent);
		    }
		  });
		  Dialog alertDialog = builder.create();
		  alertDialog.setCanceledOnTouchOutside(false);
		  alertDialog.show();
	}
	
}