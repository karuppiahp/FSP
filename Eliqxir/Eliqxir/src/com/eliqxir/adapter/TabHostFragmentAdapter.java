package com.eliqxir.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.AddToCartActivity;
import com.eliqxir.R;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.EditingMenuActivity;

//import com.tabhost.tabhostfragmentactivity.AddToCartActivity;
//import com.tabhost.tabhostfragmentactivity.R;

@SuppressLint("ValidFragment")
public class TabHostFragmentAdapter extends Fragment {

	String mCurrentPage, currentHeader;
	Typeface fontSemiBold,fontRegular;
	FragmentManager adapter;
	ArrayList<HashMap<String, String>> headerArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> viewPagerArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> viewPagerItemSplits = new ArrayList<HashMap<String, String>>();
	String skuValue, parentValue, categoryValue,productIdValue,statusValue,taxValue,tax,featuredVal,stockVal,fiozVal;
	ListView list;
	TextView txtNoitem;
	public TabHostFragmentAdapter() {

	}

	public TabHostFragmentAdapter(FragmentManager fm) {
		// TODO Auto-generated constructor stub
		adapter = fm;
	}

	public TabHostFragmentAdapter(FragmentManager fm,
			ArrayList<HashMap<String, String>> arrayListHeader,
			ArrayList<HashMap<String, String>> arrayListPagerValues) {
		// TODO Auto-generated constructor stub
		adapter = fm;
		headerArray = arrayListHeader;
		viewPagerArray = arrayListPagerValues;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getActivity());
		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getString("titleNames");

	}

	class ViewHolderForList {
		public ListView list;
		public TextView txtNoitem;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = null;

		v = inflater.inflate(R.layout.listview_pager, container, false);
		list = (ListView) v.findViewById(R.id.listViewForItems);
		 txtNoitem = (TextView)v.findViewById(R.id.txtNoItems);
		 
		if (Integer.decode(Build.VERSION.SDK) > 17) {
			for (int i = 0; i < Constant.subCategoriesHeader.size(); i++) {
				/*if(Constant.currentPager.length()>0) {
					Log.e("Current pager aaaaaaaaaaaaa",""+Constant.currentPager);
					Constant.currentPager = "";
				}*/
				if (mCurrentPage.equalsIgnoreCase(Constant.subCategoriesHeader
						.get(i).get("header"))) {
					viewPagerItemSplits.clear();
					String headerValue = Constant.subCategoriesHeader.get(i)
							.get("header");
					if (Constant.subCategoriesArray.size() > 0) {
						txtNoitem.setVisibility(View.GONE);
						 list.setVisibility(View.VISIBLE);
						for (int j = 0; j < Constant.subCategoriesArray.size(); j++) {
							if (Constant.userAccessType.equals("vendor")) {

								
								parentValue = Constant.subCategoriesArray
										.get(j).get("parent");
								categoryValue = Constant.subCategoriesArray
										.get(j).get("category");
								
								statusValue = Constant.subCategoriesArray
										.get(j).get("status");
								//Added on April 16th , 27th and 28th
								featuredVal=Constant.subCategoriesArray
										.get(j).get("featured");
								
								stockVal=Constant.subCategoriesArray
										.get(j).get("stock");
								
								fiozVal=Constant.subCategoriesArray
										.get(j).get("fl_oz");
								//
								
							}
							skuValue = Constant.subCategoriesArray.get(j)
									.get("sku");
							taxValue=Constant.subCategoriesArray.get(j)
									.get("tax");
							productIdValue = Constant.subCategoriesArray
									.get(j).get("product_id");
							String nameValue = Constant.subCategoriesArray.get(
									j).get("name");
							String descValue = Constant.subCategoriesArray.get(
									j).get("description");
							String idValue = Constant.subCategoriesArray.get(j)
									.get("id");
							String priceValue = Constant.subCategoriesArray
									.get(j).get("price");
														 

							if (nameValue.contains(headerValue)) {
								String[] nameSplit = nameValue.split(">");
								String name = nameSplit[1];
								String[] descSplit = descValue.split(">");
								String desc = descSplit[1];
								String[] idSplit = idValue.split(">");
								String id = idSplit[1];
								String[] priceSplit = priceValue.split(">");
								String price = priceSplit[1];
								currentHeader = nameSplit[0];
						//		Log.e("currentHeader", currentHeader);
								HashMap<String, String> hashMap = new HashMap<String, String>();
								if (Constant.userAccessType.equals("vendor")) {
								
									String[] parentSplit = parentValue
											.split(">");
									String parent = parentSplit[1];
									String[] categorySplit = categoryValue
											.split(">");								
									String category = categorySplit[1];
																
									
									String[] statusSplit = statusValue
											.split(">");								
									String status = statusSplit[1];
									Log.e("Status Value from Main IF Response",status);
									
									//Added on April 16th, 27th and 28th
									String[] featuredSplit = featuredVal
											.split(">");								
									String feature = featuredSplit[1];
									hashMap.put("featured", feature);
									Log.e("Featured Value from Main IF Response",feature);
									
									String[] stockSplit = stockVal
											.split(">");								
									String stock = stockSplit[1];
									hashMap.put("stock", stock);
									Log.e("Stock Value from Main IF Response",stock);
									
									String[] fiozSplit = fiozVal
											.split(">");								
									String fioz = fiozSplit[1];
									hashMap.put("fl_oz", fioz);
									Log.e("Fioz Value from Main IF Response",fioz);
									
									///
									hashMap.put("status", status);
									hashMap.put("parent", parent);
									hashMap.put("category", category);
									hashMap.put("categoryPosition", ""+i);
								}
								String[] skuSplit = skuValue.split(">");
								String sku = skuSplit[1];
								String[] productIdSplit = productIdValue
										.split(">");	
								
								String productID = productIdSplit[1];
								String[] taxSplit = taxValue.split(">");
								 tax = taxSplit[1];
								hashMap.put("product_id", productID);
								hashMap.put("sku", sku);
								hashMap.put("name", name);
								hashMap.put("desc", desc);
								hashMap.put("id", id);
								hashMap.put("price", price);
								hashMap.put("tax", tax);
								viewPagerItemSplits.add(hashMap);
							}
						}
					} else {
//						Utils.ShowAlert(getActivity(), "No products available for "
//								+ headerValue);
						txtNoitem.setVisibility(View.VISIBLE);
						 list.setVisibility(View.GONE);
						 txtNoitem.setText("No products available for "+headerValue);
					}
					if(viewPagerItemSplits.isEmpty())
					{
						txtNoitem.setVisibility(View.VISIBLE);
						 list.setVisibility(View.GONE);
//						Utils.ShowAlert(getActivity(), "No products available!"
//								);
					}
					else
					{
						txtNoitem.setVisibility(View.GONE);
						 list.setVisibility(View.VISIBLE);
					list.setAdapter(new ImageAdapter(getActivity(),
							headerValue, viewPagerItemSplits));
					}
				}
			}
		} else {
			for (int i = 0; i < headerArray.size(); i++) {
				
				if (mCurrentPage.equalsIgnoreCase(headerArray.get(i).get(
						"header"))) {
					viewPagerItemSplits.clear();
					String headerValue = headerArray.get(i).get("header");
					if (viewPagerArray.size() > 0) {
						for (int j = 0; j < viewPagerArray.size(); j++) {
							String nameValue = viewPagerArray.get(j)
									.get("name");
							String descValue = viewPagerArray.get(j).get(
									"description");
							String idValue = viewPagerArray.get(j).get("id");
							String priceValue = viewPagerArray.get(j).get(
									"price");
							productIdValue = viewPagerArray.get(j).get(
									"product_id");
							skuValue = viewPagerArray.get(j).get("sku");
							taxValue = viewPagerArray.get(j).get("tax");
							if (Constant.userAccessType.equals("vendor")) {

								
								parentValue = viewPagerArray.get(j).get(
										"parent");
								categoryValue = viewPagerArray.get(j).get(
										"category");
								
								
								statusValue= viewPagerArray.get(j).get(
										"status");
								featuredVal=viewPagerArray.get(j).get(
										"featured");
								Log.e("Featured Value from Main Else Response",featuredVal);
								
								stockVal=viewPagerArray.get(j).get("stock");
								Log.e("Stock Value from Main Else Response",stockVal);
								
								fiozVal=viewPagerArray.get(j).get("fl_oz");
								Log.e("Fioz Value from Main Else Response",fiozVal);
							}
							if (nameValue.contains(headerValue)) {
								String[] nameSplit = nameValue.split(">");
								String name = nameSplit[1];
								String[] descSplit = descValue.split(">");
								String desc = descSplit[1];
								String[] idSplit = idValue.split(">");
								String id = idSplit[1];
								String[] priceSplit = priceValue.split(">");
								String price = priceSplit[1];
								currentHeader = nameSplit[0];
								String[] productiIdSplit = productIdValue
										.split(">");
								String productId = productiIdSplit[1];
								String[] skuSplit = skuValue.split(">");
								String sku = skuSplit[1];
								
								String[] taxSplit = taxValue.split(">");
								tax = taxSplit[1];
						//		Log.e("currentHeader", currentHeader);
								HashMap<String, String> hashMap = new HashMap<String, String>();
								if (Constant.userAccessType.equals("vendor")) {
									
									String[] parentSplit = parentValue
											.split(">");
									String parent = parentSplit[1];
									String[] categorySplit = categoryValue
											.split(">");
									String category = categorySplit[1];
								
									String[] statusSplit = statusValue
											.split(">");								
									String status = statusSplit[1];
									
									//Added on April16th, 27th and 28th
									String[] featuredSplit = featuredVal
											.split(">");								
									String feature = featuredSplit[1];
									hashMap.put("featured", feature);
									
									String[] stockSplit = stockVal
											.split(">");								
									String stock = stockSplit[1];
									hashMap.put("stock", stock);
									
									String[] fiozSplit = fiozVal
											.split(">");								
									String fioz = fiozSplit[1];
									hashMap.put("fl_oz", fioz);
									//
									
									hashMap.put("status", status);
									hashMap.put("parent", parent);
									hashMap.put("category", category);
									hashMap.put("categoryPosition", ""+i);
								}
								hashMap.put("sku", sku);
								hashMap.put("product_id", productId);
								hashMap.put("name", name);
								hashMap.put("desc", desc);
								hashMap.put("id", id);
								hashMap.put("price", price);
								hashMap.put("tax", tax);
								viewPagerItemSplits.add(hashMap);
							}
						}
					} else {
//						Utils.ShowAlert(getActivity(), "No products available for "
//								+ headerValue);
						txtNoitem.setVisibility(View.VISIBLE);
						 list.setVisibility(View.GONE);
						 txtNoitem.setText("No products available for "+headerValue);
					}
					if(viewPagerItemSplits.isEmpty())
					{
						txtNoitem.setVisibility(View.VISIBLE);
						 list.setVisibility(View.GONE);
//						Utils.ShowAlert(getActivity(), "No products available!"
//								);
					}
					else
					{
						txtNoitem.setVisibility(View.GONE);
						 list.setVisibility(View.VISIBLE);
					list.setAdapter(new ImageAdapter(getActivity(),
							headerValue, viewPagerItemSplits));
					}
				}
				
				
	//-------------------------- changes according to vendor tabs screen
				/*if(Constant.currentPager.length()>0) {

					viewPagerItemSplits.clear();
					String headerValue = headerArray.get(i).get("header");
					if (viewPagerArray.size() > 0) {
						for (int j = 0; j < viewPagerArray.size(); j++) {
							String nameValue = viewPagerArray.get(j)
									.get("name");
							String descValue = viewPagerArray.get(j).get(
									"description");
							String idValue = viewPagerArray.get(j).get("id");
							String priceValue = viewPagerArray.get(j).get(
									"price");
							productIdValue = viewPagerArray.get(j).get(
									"product_id");
							skuValue = viewPagerArray.get(j).get("sku");
							taxValue = viewPagerArray.get(j).get("tax");
							if (Constant.userAccessType.equals("vendor")) {

								
								parentValue = viewPagerArray.get(j).get(
										"parent");
								categoryValue = viewPagerArray.get(j).get(
										"category");
								
								
								statusValue= viewPagerArray.get(j).get(
										"status");
							}
							if (nameValue.contains(headerValue)) {
								String[] nameSplit = nameValue.split(">");
								String name = nameSplit[1];
								String[] descSplit = descValue.split(">");
								String desc = descSplit[1];
								String[] idSplit = idValue.split(">");
								String id = idSplit[1];
								String[] priceSplit = priceValue.split(">");
								String price = priceSplit[1];
								currentHeader = nameSplit[0];
								String[] productiIdSplit = productIdValue
										.split(">");
								String productId = productiIdSplit[1];
								String[] skuSplit = skuValue.split(">");
								String sku = skuSplit[1];
								
								String[] taxSplit = taxValue.split(">");
								tax = taxSplit[1];
						//		Log.e("currentHeader", currentHeader);
								HashMap<String, String> hashMap = new HashMap<String, String>();
								if (Constant.userAccessType.equals("vendor")) {
									
									String[] parentSplit = parentValue
											.split(">");
									String parent = parentSplit[1];
									String[] categorySplit = categoryValue
											.split(">");
									String category = categorySplit[1];
								
									String[] statusSplit = statusValue
											.split(">");								
									String status = statusSplit[1];
									
									
									hashMap.put("status", status);
									hashMap.put("parent", parent);
									hashMap.put("category", category);
								}
								hashMap.put("sku", sku);
								hashMap.put("product_id", productId);
								hashMap.put("name", name);
								hashMap.put("desc", desc);
								hashMap.put("id", id);
								hashMap.put("price", price);
								hashMap.put("tax", tax);
								viewPagerItemSplits.add(hashMap);
							}
						}
					} else {
//						Utils.ShowAlert(getActivity(), "No products available for "
//								+ headerValue);
						txtNoitem.setVisibility(View.VISIBLE);
						 list.setVisibility(View.GONE);
						 txtNoitem.setText("No products available for "+headerValue);
					}
					if(viewPagerItemSplits.isEmpty())
					{
						txtNoitem.setVisibility(View.VISIBLE);
						 list.setVisibility(View.GONE);
//						Utils.ShowAlert(getActivity(), "No products available!"
//								);
					}
					else
					{
						txtNoitem.setVisibility(View.GONE);
						 list.setVisibility(View.VISIBLE);
					list.setAdapter(new ImageAdapter(getActivity(),
							headerValue, viewPagerItemSplits));
					}
				
					Log.e("Current pager from",""+Constant.currentPager);
					Constant.currentPager = "";
				}*/
				
				// ---------------- end of chnaged code
			}
		}

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (Constant.userAccessType.equals("enduser")) {
					String productId=viewPagerItemSplits.get(arg2).get("product_id");
					String idIntent = viewPagerItemSplits.get(arg2).get("id");
					Log.e("productId From list adapter",productId);
					String priceIntent = viewPagerItemSplits.get(arg2).get("price");
					String nameIntent = viewPagerItemSplits.get(arg2).get("name");
					String descIntent = viewPagerItemSplits.get(arg2).get("desc");
					String skuIntent = viewPagerItemSplits.get(arg2).get("sku");
					Intent intentToAddCart = new Intent(getActivity(),AddToCartActivity.class);
					intentToAddCart.putExtra("sku", skuIntent);
					intentToAddCart.putExtra("id", idIntent);
					intentToAddCart.putExtra("tax", tax);
					intentToAddCart.putExtra("product_id", productId);
					intentToAddCart.putExtra("price", priceIntent);
					intentToAddCart.putExtra("name", nameIntent);
					intentToAddCart.putExtra("desc", descIntent);
					startActivity(intentToAddCart);
				} else if (Constant.userAccessType.equals("vendor")) {
					String idIntent = viewPagerItemSplits.get(arg2).get("id");
					String priceIntent = viewPagerItemSplits.get(arg2).get("price");
					String nameIntent = viewPagerItemSplits.get(arg2).get("name");
					String descIntent = viewPagerItemSplits.get(arg2).get("desc");					
					String skuIntent = viewPagerItemSplits.get(arg2).get("sku");
					String parentIntent = viewPagerItemSplits.get(arg2).get("parent");
					String categoryIntent = viewPagerItemSplits.get(arg2).get("category");
					String productId=viewPagerItemSplits.get(arg2).get("product_id");
					String status=viewPagerItemSplits.get(arg2).get("status");
					String feature=viewPagerItemSplits.get(arg2).get("featured");
					String stocks=viewPagerItemSplits.get(arg2).get("stock");
					String fioz=viewPagerItemSplits.get(arg2).get("fl_oz");
					Constant.currentPager = viewPagerItemSplits.get(arg2).get("categoryPosition");
					Log.e("Parent Intent >>>>>>>>>>>",viewPagerItemSplits.get(arg2).get("category"));
					Log.e("Parent Intent >>>>>>>>>>>",Constant.currentPager);
					Intent intentToMenu = new Intent(getActivity(),EditingMenuActivity.class);
					intentToMenu.putExtra("id", idIntent);
					intentToMenu.putExtra("price", priceIntent);
					intentToMenu.putExtra("name", nameIntent);
					intentToMenu.putExtra("desc", descIntent);
					intentToMenu.putExtra("status", status);
					intentToMenu.putExtra("sku", skuIntent);
					intentToMenu.putExtra("parent", parentIntent);
					intentToMenu.putExtra("category", categoryIntent);
					intentToMenu.putExtra("product_id", productId);
					intentToMenu.putExtra("featureval", feature);
					intentToMenu.putExtra("stockval", stocks);
					intentToMenu.putExtra("fiozval", fioz);
					startActivity(intentToMenu);
				}
			}
		});
		return v;
	}

	public class ImageAdapter extends BaseAdapter {

		FragmentActivity context;
		ViewHolder holder;
		String[] header;
		String[] note;

		ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
		String headerViewPager;

		class ViewHolder {
			public TextView textViewHeader;
			public TextView textViewNote;
		}

		public ImageAdapter(FragmentActivity fragmentActivity,
				String headerValue,
				ArrayList<HashMap<String, String>> viewPagerArray) {
			this.context = fragmentActivity;
			headerViewPager = headerValue;
			listItems = viewPagerArray;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v = convertView;
			final ViewHolder holder;
			if (v == null) {
				LayoutInflater li = LayoutInflater.from(context);
				v = li.inflate(R.layout.listitem, null);
				holder = new ViewHolder();
				holder.textViewHeader = (TextView) v
						.findViewById(R.id.textViewHeader);
				holder.textViewNote = (TextView) v
						.findViewById(R.id.textViewNotes);
				
				v.setTag(holder);
			} else {
				holder = (ViewHolder) v.getTag();
			}
			
			fontSemiBold = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Semibold_0.ttf");
			fontRegular= Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Regular.ttf");
			
			holder.textViewHeader.setTypeface(fontSemiBold);
			holder.textViewNote.setTypeface(fontRegular);
			
			holder.textViewHeader.setText(listItems.get(position).get("name"));
			holder.textViewNote.setText(listItems.get(position).get("desc"));
			return v;
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
	}
}
