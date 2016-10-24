package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eliqxir.R;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;

public class LoginVendorActivity extends Activity implements OnClickListener {
	public void onStop()
	{
		if(Constant.isVendorAvailable.equals("notAvailable"))
		{
			finish();
		}
		super.onStop();
		
	}
	Dialog dialogbox;
	Button btnForLogin,btnDriverLoginCancel,btn_Ok;
	EditText editTxtForMailId, editTxtForPwd;
	TextView text_Message;
	
	SharedPreferences vendorSharedpreferences;
	Editor vendorPreferenceEditor;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}

		return false;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.driver_login);
		vendorSharedpreferences = getSharedPreferences("vendorPrefs", Context.MODE_PRIVATE);
		vendorPreferenceEditor = vendorSharedpreferences.edit();
		editTxtForMailId = (EditText) findViewById(R.id.edtDriverUname);
		editTxtForPwd = (EditText) findViewById(R.id.edtDriverPwd);
		btnForLogin = (Button) findViewById(R.id.btnDriverLogin);
		btnForLogin.setOnClickListener(this);
		btnDriverLoginCancel = (Button) findViewById(R.id.btnDriverLoginCancel);
		btnDriverLoginCancel.setOnClickListener(this);
		/*editTxtForMailId.setText("android_user");
		editTxtForPwd.setText("test123");		*/
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnDriverLoginCancel:
			finish();
			break;
		case R.id.btnDriverLogin:
			String mailId = editTxtForMailId.getText().toString();
			String pwd = editTxtForPwd.getText().toString();

			if (mailId.length() > 0 && pwd.length() > 0) {
	//			if (mailId.equals("android_user") && pwd.equals("test123")) {
					boolean isOnline = Utils.isOnline();
					Log.e("isOnline", isOnline + "");
					if (isOnline) {
						new VendorLogin(mailId, pwd).execute();
					}
					else
					{
						Utils.ShowAlert(LoginVendorActivity.this, Constant.networkDisconected);
					}
				
				/*} else {
					Utils.ShowAlert(LoginVendorActivity.this,
							"Please enter a valid email id & password.");
				}*/
			} else {
				Utils.ShowAlert(LoginVendorActivity.this,
						"Text fields must not be empty");
			}
			break;
		}
	}

	public class VendorLogin extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String email, password, status = "",storeId="", error = "Login Unsuccessfull!", userId, fName, lName,
				dob;

		public VendorLogin(String mailId, String pwd) {
			// TODO Auto-generated constructor stub
			email = mailId;
			password = pwd;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					
					dialogbox = new Dialog(LoginVendorActivity.this,R.style.custom_dialog_theme);
					dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);		
					dialogbox.getWindow();
			  	    dialogbox.setContentView(R.layout.custom_alert);
					dialogbox.setCancelable(false);
										
					text_Message= (TextView) dialogbox.findViewById(R.id.success);
					btn_Ok= (Button) dialogbox.findViewById(R.id.habitokbtn);
					text_Message.setText("Vendor Login Success");
					btn_Ok.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View arg0) {
		//					dialog.cancel();
							dialogbox.cancel();
							SessionStore.saveSlidinMenu("vendor", getBaseContext());
							Constant.userAccessType="vendor";
							vendorPreferenceEditor=vendorSharedpreferences.edit();
							vendorPreferenceEditor.putString("store_id",storeId);
							vendorPreferenceEditor.commit();
							Intent intentToOverview = new Intent(
									LoginVendorActivity.this, OverviewActivity.class);		
							intentToOverview.putExtra("from","vendorlogin");
							startActivity(intentToOverview);		 				
							finish();							
						}
					});
					/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginVendorActivity.this);			 
					alertDialogBuilder.setTitle("Message");
			 		alertDialogBuilder	.setMessage("Vendor Login Success")
							.setCancelable(false)
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {								
									dialog.cancel();
									SessionStore.saveSlidinMenu("vendor", getBaseContext());
									Constant.userAccessType="vendor";
									vendorPreferenceEditor=vendorSharedpreferences.edit();
									vendorPreferenceEditor.putString("store_id",storeId);
									vendorPreferenceEditor.commit();
									Intent intentToOverview = new Intent(
											LoginVendorActivity.this, OverviewActivity.class);		
									intentToOverview.putExtra("from","vendorlogin");
									startActivity(intentToOverview);		 				
									finish();
								}
							  });			 
							AlertDialog alertDialog = alertDialogBuilder.create();						
			 				alertDialog.show();				*/
					dialogbox.show();
				} else {
					Utils.ShowAlert(LoginVendorActivity.this, error);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(LoginVendorActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.e("email", email);
			Log.e("password", password);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs
					.add(new BasicNameValuePair("vendor_username", email));
			nameValuePairs.add(new BasicNameValuePair("vendor_password", password));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.vendorLogin())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("vendor Login", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if(status.equals("1"))
					{
						storeId=jsonObj.getString("store_id");
						Log.e("storeId",storeId);
					}
					else if (status.equals("0")) {
						error = jsonObj.getString("Error");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
