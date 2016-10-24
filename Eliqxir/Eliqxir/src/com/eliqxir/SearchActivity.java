package com.eliqxir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.SearchAdapter;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SearchActivity extends SlidingMenuActivity implements OnClickListener {

	ImageButton backImg, cartBtn, btnSlideMenu;
	Button searchClose;
	TextView txtForHeader;
	EditText editTxtForSearch;
	String editTxtValue,stordID,sessionPosition;
	ArrayList<HashMap<String, String>> searchPdtArray = new ArrayList<HashMap<String,String>>();
	ListView listView;
	TextView txtNodata;
	SharedPreferences customerPreference,zipPreference;
	SharedPreferences.Editor zipPrefEditor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.search);
		zipPreference=SearchActivity.this.getSharedPreferences("zipprefs", Context.MODE_PRIVATE);
		zipPrefEditor=zipPreference.edit();
		sessionPosition=zipPreference.getString("statusValue","");
		Log.e("Session Zip Strore Value",sessionPosition);
		customerPreference = SearchActivity.this.getSharedPreferences(
				"customerPrefs", Context.MODE_PRIVATE);
		stordID = customerPreference.getString("store_id", "");
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		txtForHeader = (TextView) findViewById(R.id.textForHeader);
		editTxtForSearch = (EditText)findViewById(R.id.editTxtForSearch);
		listView = (ListView)findViewById(R.id.listForSearchPdt);
		searchClose=(Button)findViewById(R.id.searchclose);
		
		btnSlideMenu.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.VISIBLE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.GONE);
		txtForHeader.setText("SEARCH");
		txtNodata=(TextView)findViewById(R.id.txtNoitems);
		
		/*txtNodata.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);*/
		
		txtNodata.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String idIntent = searchPdtArray.get(arg2).get("pdtSearchId");
				String priceIntent = searchPdtArray.get(arg2).get("pdtSearchPrice");
				String nameIntent = searchPdtArray.get(arg2).get("pdtSearchName");
				String descIntent = searchPdtArray.get(arg2).get("pdtSearchDesc");
				Intent intentToAddCart = new Intent(SearchActivity.this, AddToCartActivity.class);
				intentToAddCart.putExtra("product_id", idIntent);
				intentToAddCart.putExtra("price", priceIntent);
				intentToAddCart.putExtra("name", nameIntent);
				intentToAddCart.putExtra("desc", descIntent);
				startActivity(intentToAddCart);
			}
		});
		
		searchClose.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				editTxtForSearch.setText("");
				txtNodata.setText("Key in to search with the product name in search bar.");
				txtNodata.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		});
		
		editTxtForSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {					
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {	
				if(arg0.length()>0)
				{
				editTxtValue = editTxtForSearch.getText().toString();
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					if(sessionPosition.equals("0")){
						Log.e("Not Possible","To Search");
						listView.setVisibility(View.GONE);
					    txtNodata.setVisibility(View.VISIBLE);
				//	    txtNodata.setText("No data found.");
					}else{		
						listView.setVisibility(View.VISIBLE);
					    txtNodata.setVisibility(View.GONE);
						new SearchProducts().execute();
					}
				}
				else
				{
					Utils.ShowAlert(SearchActivity.this, Constant.networkDisconected);
				}
				}else{
					txtNodata.setText("Key in to search with the product name in search bar.");
					txtNodata.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
				}
			}
		});
		
		
	}
	
	public class SearchProducts extends AsyncTask<Void, Void, Boolean>
	{
		ProgressDialog dialog;
		String status ="";
		@Override
		public void onPreExecute(){
			super.onPreExecute();
			dialog = new ProgressDialog(SearchActivity.this);
			dialog.setMessage("Loading........");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("search", editTxtValue));
			nameValuePairs
			.add(new BasicNameValuePair("store_id", stordID));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.searchUserPdt()).getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("Search Response values",""+jsonObj);
			try {
				searchPdtArray.clear();
				if (jsonObj != null) {
					 status = jsonObj.getString("status");
					if (status.equals("1")) {
						JSONArray pdtSearchArray = jsonObj.getJSONArray("Search_info");
						for(int i=0; i<pdtSearchArray.length(); i++)
						{
							String pdtSearchId = pdtSearchArray.getJSONObject(i).getString("id");
							String pdtSearchName = pdtSearchArray.getJSONObject(i).getString("name");
							String pdtSearchDesc = pdtSearchArray.getJSONObject(i).getString("desc");
							String pdtSearchPrice = pdtSearchArray.getJSONObject(i).getString("price");
							
							HashMap<String, String> hashValue = new HashMap<String, String>();
							hashValue.put("pdtSearchId", pdtSearchId);
							hashValue.put("pdtSearchName", pdtSearchName);
							hashValue.put("pdtSearchDesc", pdtSearchDesc);
							hashValue.put("pdtSearchPrice", pdtSearchPrice);
							searchPdtArray.add(hashValue);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
    	protected void onPostExecute(Boolean result)
    	{
    	//	dialog.dismiss();
			if(searchPdtArray.size()>0){
				listView.setVisibility(View.VISIBLE);
				txtNodata.setVisibility(View.GONE);
				
				Log.i("array size::::::::::", ""+searchPdtArray.size());
				listView.setAdapter(new SearchAdapter(SearchActivity.this, searchPdtArray));
		} 			
			else{
				listView.setVisibility(View.GONE);
				txtNodata.setVisibility(View.VISIBLE);
				txtNodata.setText("No data found.");
			//	Utils.ShowAlert(SearchActivity.this, "There is no item to view");
			}
			dialog.dismiss();
    	}
		
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editTxtForSearch.getWindowToken(), 0);
			toggle();
		}
	}
}
