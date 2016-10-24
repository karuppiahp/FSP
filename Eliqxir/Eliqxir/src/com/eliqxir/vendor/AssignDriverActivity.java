package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

public class AssignDriverActivity extends Activity implements OnClickListener {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();
		
	}
	ImageButton backImg, cartBtn, btnSlideMenu, acceptOrderBtn;
	TextView textForHeader;
	ListView listViewDrivers;
	ImageView imageForTick;
	ImageButton btnAcceptOrder;
	List<DriverNames> driverNames;
	String selectedDriver="",orderId="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.assign_drivers);
		driverNames = new ArrayList<DriverNames>();
		driverNames.clear();
		orderId=getIntent().getExtras().getString("order_id");
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		listViewDrivers = (ListView) findViewById(R.id.listViewForAssignDrivers);
		btnAcceptOrder=(ImageButton)findViewById(R.id.imageBtnForAcceptOrders);
		btnAcceptOrder.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setOnClickListener(this);
		textForHeader.setText("ASSIGN DRIVER");
		boolean isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			new ListDrivers().execute();
		}
		else
		{
			Utils.ShowAlert(AssignDriverActivity.this, Constant.networkDisconected);
		}
		
		

		listViewDrivers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				String position = SessionStore
						.getListViewPosition(AssignDriverActivity.this);
				if (position != null) {
					int indexPosition = Integer.parseInt(position);
					removeView(indexPosition);
				}
				updateView(pos);
				
				 selectedDriver=driverNames.get(pos).username;
				Log.e("selectedDriver",selectedDriver);
				
				
			}
		});
	}

	public class DriversAssignList extends BaseAdapter {

		Context mContext;

		public DriversAssignList(Context context) {
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
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View v = arg1;
			LayoutInflater inflater = LayoutInflater.from(mContext);
			v = inflater.inflate(R.layout.assign_driver_list_item, null);
			TextView textForDrivers = (TextView) v
					.findViewById(R.id.textForAssignDriverItem);
			textForDrivers.setText(driverNames.get(position).username);
			imageForTick = (ImageView) v
					.findViewById(R.id.imageForAssignDriverItem);
			return v;
		}

	}

	private void updateView(int index) {
		ViewHolder holder = new ViewHolder();
		View v = listViewDrivers.getChildAt(index
				- listViewDrivers.getFirstVisiblePosition());
		String position = Integer.toString(index);
		SessionStore.saveListViewPosition(position, AssignDriverActivity.this);
		holder.imageForDriverTicks = (ImageView) v
				.findViewById(R.id.imageForAssignDriverItem);
		holder.imageForDriverTicks.setVisibility(View.VISIBLE);
	}

	private void removeView(int index) {
		ViewHolder holder = new ViewHolder();
		View v = listViewDrivers.getChildAt(index
				- listViewDrivers.getFirstVisiblePosition());
		if (v != null) {
			holder.imageForDriverTicks = (ImageView) v
					.findViewById(R.id.imageForAssignDriverItem);
			holder.imageForDriverTicks.setVisibility(View.GONE);
		}
	}

	public class ViewHolder {
		ImageView imageForDriverTicks;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backBtn:
			SessionStore.saveListViewPosition(null, AssignDriverActivity.this);
			finish();
			break;
		case R.id.imageBtnForAcceptOrders:
			if(selectedDriver.length()>0)
			{
				if(orderId.length()>0)
				{
					boolean isOnline = Utils.isOnline();
					Log.e("isOnline", isOnline + "");
					if (isOnline) {
						new AcceptOrder().execute();
					}
					else
					{
						Utils.ShowAlert(AssignDriverActivity.this, Constant.networkDisconected);
					}
				
				}
				else
				{
					Utils.ShowAlert(AssignDriverActivity.this, "Order id is empty.");
				}
			}
			else
			{
				Utils.ShowAlert(AssignDriverActivity.this, "Please select a driver.");
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		SessionStore.saveListViewPosition(null, AssignDriverActivity.this);
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
					listViewDrivers.setAdapter(new DriversAssignList(
							getBaseContext()));
				} else {
					Utils.ShowAlert(AssignDriverActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(AssignDriverActivity.this);
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
	
	
	public class AcceptOrder extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error = "",success="";
		JSONObject jsonObj;

		

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AssignDriverActivity.this);			 
					alertDialogBuilder.setTitle("Message");
			 		alertDialogBuilder	.setMessage("Driver has Assigned for this order and Order Moved to Processing status!!")
							.setCancelable(false)
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {								
									dialog.cancel();
									startActivity(new Intent(AssignDriverActivity.this,OrderActivity.class));
								}
							  });			 
							AlertDialog alertDialog = alertDialogBuilder.create();
			 				alertDialog.show();	
				//	Utils.ShowAlert(getApplicationContext(), success);
				
				} else {
					Utils.ShowAlert(AssignDriverActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(AssignDriverActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("order_id", orderId));
			nameValuePairs.add(new BasicNameValuePair("driver_name", selectedDriver));
			Log.e("order id",orderId);
			Log.e("driver name",selectedDriver);
			jsonObj = new ServerResponse(UrlGenerator.vendorAcceptOrder())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);

			Log.e("", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else if (status.equals("1")) {
						success= jsonObj.getString("Success");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
