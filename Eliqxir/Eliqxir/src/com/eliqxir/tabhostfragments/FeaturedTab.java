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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class FeaturedTab extends Fragment implements OnClickListener {

	int i = 0;
	ArrayList<HashMap<String, String>> subCategoriesHeader = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> subCategoriesArray = new ArrayList<HashMap<String, String>>();
	// HashMap<String, String> hashMapKeys = new HashMap<String, String>();
	HashMap<String, String> hashMapsubCategory = new HashMap<String, String>();
	String subCategoryId, subCategoryName, subCategoryDesc, subCategoryPrice,feature_status,stock_status,fioz_value,
	stordID, subCategorysku, subCategoryParent, subCategoryCategory,subCategorytax,
	subCategoryproductId, subCategorystatus, menuPref,storeName,storeAddress,storeid,dayOfTheWeek,currentTime,tax="0";
	ArrayList<String> subcatList = new ArrayList<String>();
	View v;
	SharedPreferences customerPreference;
	SharedPreferences.Editor customerPrefEditor;
	SharedPreferences vendorSharedpreferences;
	ArrayList<String> catIdList = new ArrayList<String>();
	ArrayList<String> catList = new ArrayList<String>();
	JSONObject jsonObj;
	ViewPager pager;
	PagerTabStrip pagerTabStrip;
	TextView txtNoItem;
	int time_format = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Utils.trackError(getActivity());

		if (container == null) {
			return null;
		}

		v = inflater.inflate(R.layout.tab_featured, container, false);
		 pager = (ViewPager) v
					.findViewById(R.id.mypagerFeatured);

			 pagerTabStrip = (PagerTabStrip) v
					.findViewById(R.id.pager_title_stripFeatured);
			  txtNoItem=(TextView)v.findViewById(R.id.txtNoProduct);
			  txtNoItem.setVisibility(View.GONE);
			  pager.setVisibility(View.VISIBLE);
	/*	vendorSharedpreferences = getActivity().getSharedPreferences(
				"vendorPrefs", Context.MODE_PRIVATE);
		stordID = vendorSharedpreferences.getString("store_id", "");*/
		
		customerPreference=getActivity().getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);		
//		customerPrefEditor=customerPreference.edit();
		stordID = customerPreference.getString("store_id", "");
		Log.e("stordID inside featured tab ********", stordID);
		menuPref = SessionStore.getSlidinMenu(getActivity()
				.getApplicationContext());
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
					Log.e("Featred Customer Browse Response",""+jsonObj);
				
				}
				else if (menuPref.equals("vendor")) {
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair("store_id",
							stordID));
					jsonObj = new ServerResponse(UrlGenerator.vendorBrowse())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
				} else {
					List<NameValuePair> nameValuepair = new ArrayList<NameValuePair>();
					nameValuepair.add(new BasicNameValuePair(
							"customer_zipcode", Constant.zipCode));
					jsonObj = new ServerResponse(
							UrlGenerator.getCustomerBrowse())
							.getJSONObjectfromURL(RequestType.POST,
									nameValuepair);
					Log.e("Featred Customer Browse Response",""+jsonObj);
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
						Log.e("Vendor browse api status", status);
						if (status.equals("1")) {
							
							String zipcode;
							if(menuPref==null)
							{
								storeid=jsonObj.getString("store_id");
								 storeName=jsonObj.getString("store_name");
								 storeAddress=jsonObj.getString("store_address");
								 zipcode=jsonObj.getString("zipcode");
							/*	 Log.e("zipcode",zipcode);
								Log.e("store_name",storeName);
								Log.e("store_address",storeAddress);*/
								customerPrefEditor=customerPreference.edit();
								customerPrefEditor.putString("store_name", storeName);
								customerPrefEditor.putString("zipcode", zipcode);
								customerPrefEditor.putString("store_address", storeAddress);
								customerPrefEditor.putString("store_id", storeid);
								customerPrefEditor.commit();
							}
							else if (menuPref.equals("vendor")) {
								
							}
							else
							{
								storeid=jsonObj.getString("store_id");
								 storeName=jsonObj.getString("store_name");
								 storeAddress=jsonObj.getString("store_address");
								 zipcode=jsonObj.getString("zipcode");
								/* Log.e("zipcode",zipcode);
								Log.e("store_name",storeName);
								Log.e("store_address",storeAddress);*/
								customerPrefEditor=customerPreference.edit();
								customerPrefEditor.putString("store_name", storeName);
								customerPrefEditor.putString("zipcode", zipcode);
								customerPrefEditor.putString("store_address", storeAddress);
								customerPrefEditor.putString("store_id", storeid);
								customerPrefEditor.commit();
							}
							subcatList.clear();
							catIdList.clear();
							catList.clear();
							subCategoriesHeader.clear();
							subCategoriesArray.clear();
							Constant.subCategoriesHeader.clear();
							Constant.subCategoriesArray.clear();

							JSONArray subCategoryArray = jsonObj.getJSONArray("sub_category");
							for (int k = 0; k < subCategoryArray.length(); k++) {
								JSONObject job3 = subCategoryArray.getJSONObject(k);
		//						Log.e("Inside IF",""+subCategoryArray.length());
		//						Log.e("JSONOBJECT Value",""+job3);
								@SuppressWarnings("unchecked")
								Iterator<String> keys1 = job3.keys();
		//						Log.e("Iterator keys Value",""+keys1);
								while (keys1.hasNext()) {
									String key = keys1.next();
									String value = job3.getString(key);
//									 Log.e("Subcategory key", key);
//									 Log.e("Subcategory value",value);
									if (key.equals("category_id")) {
										catIdList.add(key + "~" + value);
									} else {
		//						    Log.e("comes to else","else ");
										catList.add(key + "~" + value);
									}
									// tmp1=tmp1.concat(key).concat(value);

								}
								// Log.e("tmp1 >>",tmp1);
								 Log.e("catList size 333",catList+"");
								 Log.e("catidList size 444",catIdList+"");
								for (int l = 0; l < catList.size(); l++) {
									String split[] = catList.get(l).split("~");
			//					Log.e("Split[0] Featured Tab ",split[0]);
									if (split[0].equals("Featured")) {
				//						Log.e("Category val inside Featured condition", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
				//						Log.e("CartId Inside Featured Condition", catId);
										Constant.beerhashMapHeader.put(split[1], catId);
				//						Log.e("Constant.beerhashMapHeader Value inside Featured",""+Constant.beerhashMapHeader);
										HashMap<String, String> hashMapHeader = new HashMap<String, String>();
				//						Log.e("HashMapHeader Value",""+hashMapHeader);
										hashMapHeader.put("header", split[1]);
										if (subCategoriesHeader
												.contains(hashMapHeader)) {
										} else {
											subCategoriesHeader
													.add(hashMapHeader);
											Constant.subCategoriesHeader
													.add(hashMapHeader);
										}
									}						
									else if (split[0].equals("Featured")) {
				//					Log.e("category val", split[1]);
									String splitId[] = catIdList.get(l)
											.split("~");
									String catId = splitId[1];
				//					Log.e("catId", catId);
									Constant.featuredhashMapHeader.put(
											split[1], catId);
								} 
									else if (split[0].equals("Liquor")) {
				//						Log.e("Category Featured Liquor", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
				//						Log.e("catId", catId);
										Constant.liquorhashMapHeader.put(
												split[1], catId);

									} else if (split[0].equals("Extras")) {
				//						Log.e("Category Featured Mixers", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
				//						Log.e("catId", catId);
										Constant.mixerhashMapHeader.put(
												split[1], catId);

									} else if (split[0].equals("Wine")) {
					//					Log.e("Category Featured wine", split[1]);
										String splitId[] = catIdList.get(l)
												.split("~");
										String catId = splitId[1];
						//				Log.e("catId", catId);
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
								 Log.e("Product key !!!!!!!!!!!!!!!11", key);
								// Log.e("product value", value);
								subcatList.add(key);
							}

							for (int i = 0; i < subcatList.size(); i++) {
								String tmp = pdtJsonObj.getString(subcatList
										.get(i));
								JSONArray jsonArray1 = new JSONArray(tmp);
								// Log.e("jsonArray1", jsonArray1 + "");
								for (int j = 0; j < jsonArray1.length(); j++) {
									JSONObject job2 = jsonArray1
											.getJSONObject(j);
									String category = job2
											.getString("category");
									Log.e("Parent Category Value In Featured Tab",job2.getString("parent_category"));
									Log.e("Category Value In Featured Tab",category);
									Log.e("Sub Category List Value In Featured Tab",subcatList.get(i));
									if (!job2.getString("parent_category")
											.equals("Featured")&& !category.equals(subcatList
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

										String availableStatus = job2
												.getString("status");
										subCategorystatus = subcatList.get(i)
												+ ">" + availableStatus;
										subCategorytax=subcatList.get(i)
												+ ">" + tax;
										
										//Added on April16th, 27th and 28th
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
										fioz_value = subcatList.get(i)
												+ ">" + fiozStatus;
										//
										
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("id", subCategoryId);
										hashMap.put("name", subCategoryName);
										hashMap.put("description",
												subCategoryDesc);
										hashMap.put("price", subCategoryPrice);
										hashMap.put("product_id",
												subCategoryproductId);
										hashMap.put("status", subCategorystatus);
										hashMap.put("fl_oz", fioz_value);      //Added on April 28th
										hashMap.put("stock", stock_status);      //Added on April 27th
										hashMap.put("featured", feature_status);      //Added on April 16th
										hashMap.put("sku", subCategorysku);
										hashMap.put("parent", subCategoryParent);
										hashMap.put("category",
												subCategoryCategory);
										hashMap.put("tax", subCategorytax);
										subCategoriesArray.add(hashMap);
										Constant.subCategoriesArray
												.add(hashMap);
									}

								}
							}

						}
						else if(status.equals("0")){
							// Toast("Network speed is slow");
							 error=jsonObj.getString("Error");
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
					} else {
						TabHostAdapter pagerAdapter = new TabHostAdapter(fm,
								subCategoriesHeader, subCategoriesArray);

						/** Setting the pagerAdapter to the pager object */
						pager.setAdapter(pagerAdapter);
					}
				} else {
					
					  pager.setVisibility(View.GONE);
					txtNoItem.setVisibility(View.VISIBLE);
					txtNoItem.setText("No Products available for Featured.");
//					Utils.ShowAlert(getActivity(),
//							"No Products available for Beer.");
				}
				 if(status.equals("notAvailable"))
					{
					 Utils.ShowAlert(getActivity(), Constant.networkDisconected);
					}
				else if(status.equals("0"))
				{
					Utils.ShowAlert(getActivity(), error);
				}
				
			}
		}.execute();

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}