package com.eliqxir.driver;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.eliqxir.vendor.LoginVendorActivity;
import com.eliqxir.vendor.OverviewActivity;

public class LoginDriverActivity extends Activity implements OnClickListener {

	Button btnForLogin,btnDriverLoginCancel,btn_Ok;
	Dialog dialogbox;
	EditText editTxtForMailId, editTxtForPwd;
SharedPreferences driverPref;
SharedPreferences.Editor driverEditor;
TextView text_Message;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.driver_login);
		driverPref=this.getSharedPreferences("driverPref", 1);
		editTxtForMailId = (EditText) findViewById(R.id.edtDriverUname);
		editTxtForPwd = (EditText) findViewById(R.id.edtDriverPwd);
		btnForLogin = (Button) findViewById(R.id.btnDriverLogin);
		btnForLogin.setOnClickListener(this);
		btnDriverLoginCancel = (Button) findViewById(R.id.btnDriverLoginCancel);
		btnDriverLoginCancel.setOnClickListener(this);
		/*editTxtForMailId.setText("JeyVarshan");
		editTxtForPwd.setText("123456");*/
		/*editTxtForMailId.setText("bo_radley");
		editTxtForPwd.setText("test123");*/
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
			//	if (mailId.equals("android_user") && pwd.equals("test123")) {
				boolean isOnline = Utils.isOnline();
				Log.e("isOnline", isOnline + "");
				if (isOnline) {
					new DriverLogin(mailId, pwd).execute();
				}
				else
				{
					Utils.ShowAlert(LoginDriverActivity.this, Constant.networkDisconected);
				}
			    //	} else {
				//	Utils.ShowAlert(LoginDriverActivity.this,
					//		"Please enter a valid email id & password.");
				//}
			} else {
				Utils.ShowAlert(LoginDriverActivity.this,
						"Text fields must not be empty");
			}

			break;
		}
	}

	public class DriverLogin extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String email, password, status = "", storeId = "",
				error = "Login Unsuccessfull!", userId, fName, lName, dob;

		public DriverLogin(String mailId, String pwd) {
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
					
					dialogbox = new Dialog(LoginDriverActivity.this,R.style.custom_dialog_theme);
					dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE);		
					dialogbox.getWindow();
			  	    dialogbox.setContentView(R.layout.custom_alert);
					dialogbox.setCancelable(false);
										
					text_Message= (TextView) dialogbox.findViewById(R.id.success);
					btn_Ok= (Button) dialogbox.findViewById(R.id.habitokbtn);
					text_Message.setText("Driver Login Success");
					
					btn_Ok.setOnClickListener(new OnClickListener() {						
						@Override
						public void onClick(View arg0) {
			//				dialog.cancel();
							dialogbox.cancel();
							driverEditor=driverPref.edit();
							driverEditor.putString("driverName", email);
							driverEditor.commit();
							SessionStore.saveSlidinMenu("driver", getBaseContext());
							Intent intentToOverview = new Intent(
									LoginDriverActivity.this, OrdersActivity.class);
							startActivity(intentToOverview);
							finish();				
						}
					});
					
				/*	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginDriverActivity.this);			 
					alertDialogBuilder.setTitle("Message");
			 		alertDialogBuilder	.setMessage("Driver Login Success")
							.setCancelable(false)
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {								
									dialog.cancel();
									driverEditor=driverPref.edit();
									driverEditor.putString("driverName", email);
									driverEditor.commit();
									SessionStore.saveSlidinMenu("driver", getBaseContext());
									Intent intentToOverview = new Intent(
											LoginDriverActivity.this, OrdersActivity.class);
									startActivity(intentToOverview);
									finish();
								}
							  });			 
							AlertDialog alertDialog = alertDialogBuilder.create();
			 				alertDialog.show();*/				
			 				dialogbox.show();
				} else {
					Utils.ShowAlert(LoginDriverActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(LoginDriverActivity.this);
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
					.add(new BasicNameValuePair("driver_username", email));
			nameValuePairs.add(new BasicNameValuePair("driver_password",	password));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.driverLogin())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("vendor Login", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("1")) {
						
					} else if (status.equals("0")) {
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
