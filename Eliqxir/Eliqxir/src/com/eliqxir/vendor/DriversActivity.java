package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DriversActivity extends SlidingMenuActivity implements OnClickListener {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();
		
	}
	Typeface fontRegular;
	ImageButton backImg, cartBtn, btnSlideMenu;
	TextView textForHeader;
	ListView listOfDrivers;
	List<DriverNames> driverNames;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		setContentView(R.layout.drivers_list);
		driverNames = new ArrayList<DriverNames>();
		driverNames.clear();
		btnSlideMenu = (ImageButton)findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton)findViewById(R.id.backBtn);
		cartBtn = (ImageButton)findViewById(R.id.cartBtn);
		textForHeader = (TextView)findViewById(R.id.textForHeader);
		listOfDrivers = (ListView)findViewById(R.id.listViewForDrivers);
		btnSlideMenu.setVisibility(View.VISIBLE);
		backImg.setVisibility(View.GONE);
		btnSlideMenu.setOnClickListener(this);
		cartBtn.setOnClickListener(this);
		cartBtn.setBackgroundResource(R.drawable.plus_img);
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		textForHeader.setText("DRIVERS");
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new ListDrivers().execute();			
		}
		else
		{
			Utils.ShowAlert(DriversActivity.this, Constant.networkDisconected);
		}
	
		listOfDrivers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intentToStoreInfo = new Intent(DriversActivity.this, DriversStoreInfoActivity.class);
				intentToStoreInfo.putExtra("driver_id", driverNames.get(arg2).driverId);
				intentToStoreInfo.putExtra("driver_email", driverNames.get(arg2).email);
				intentToStoreInfo.putExtra("driver_name",driverNames.get(arg2).username);
				intentToStoreInfo.putExtra("driver_fname",driverNames.get(arg2).firstName);
				intentToStoreInfo.putExtra("driver_lname",driverNames.get(arg2).lastName);
				intentToStoreInfo.putExtra("driver_phone",driverNames.get(arg2).driverPhone);
				startActivity(intentToStoreInfo);
			}			
		});			
	}
	
	public class DriversAdapter extends BaseAdapter{

		Context mContext;
		public DriversAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return driverNames.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v = arg1;
			LayoutInflater inflater = LayoutInflater.from(mContext);
			v = inflater.inflate(R.layout.drivers_list_item, null);
			TextView textForName = (TextView)v.findViewById(R.id.textForDriverNames);
			TextView textForLName = (TextView)v.findViewById(R.id.textForDriverLNames);
			
			fontRegular= Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf");
			
			textForName.setTypeface(fontRegular);
			textForLName.setTypeface(fontRegular);
			
			textForName.setText(driverNames.get(arg0).username);
	//		textForLName.setText(driverNames.get(arg0).lastName);
			return v;
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSliderMenu) {
			toggle();
		}
		if(v.getId()==R.id.cartBtn)
		{
			startActivity(new Intent(DriversActivity.this,AddDriver.class));
		}
	}
	
	
	public class ListDrivers extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error = "";
		JSONObject jsonObj;

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
				listOfDrivers.setAdapter(new DriversAdapter(DriversActivity.this));
				} else {
					Utils.ShowAlert(DriversActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(DriversActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonObj = new ServerResponse(UrlGenerator.vendorListDrivers())
					.getJSONObjectfromURL(RequestType.GET, null);

			Log.e("Drivers List", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
						String driverRoles = jsonObj.getString("driver_roles");
						JSONArray jarr = new JSONArray(driverRoles);
						for (int i = 0; i < jarr.length(); i++) {
							JSONObject job1 = jarr.getJSONObject(i);
							String username = job1.getString("username");
							String firstName = job1.getString("firstname");
							String lastName = job1.getString("lastname");
							String email = job1.getString("email");
							String driverId=job1.getString("user_id");
							String driverPhone=job1.getString("phone_no");
							driverNames.add(new DriverNames(username,
									firstName, lastName, email,driverId,driverPhone));
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
	
}
