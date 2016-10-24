package com.eliqxir.vendor;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eliqxir.CheckoutActivity;
import com.eliqxir.R;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class AddDriver extends SlidingMenuActivity implements OnClickListener {

	ImageButton backImg, cartBtn, btnSlideMenu, btnForAddDriver;
	TextView textForHeader;
	EditText editTextForDriverFName, editTextForDriverLName,editText_UserName,
			editTextForDriverEmail,
			editTextForDriverPassword;
	static EditText editTextForDriverPhone;
	Typeface appFont;
	String fName,lName,first_Name,last_Name,userName;
public void onStop()
{
	if(Constant.isVendorAvailable.equals("notAvailable"))
	{
		finish();
	}
	
	super.onStop();
	
}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		setContentView(R.layout.add_driver);
		appFont = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		btnForAddDriver = (ImageButton) findViewById(R.id.btnForAddDriver);
		btnForAddDriver.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setOnClickListener(this);
		cartBtn.setVisibility(View.GONE);
		editTextForDriverEmail = (EditText) findViewById(R.id.editTextForDriverEmail);
		editTextForDriverFName = (EditText) findViewById(R.id.editTextForDriverFName);
		editTextForDriverLName = (EditText) findViewById(R.id.editTextForDriverLName);
		editTextForDriverPassword = (EditText) findViewById(R.id.editTextForDriverPassword);
		editTextForDriverPhone = (EditText) findViewById(R.id.editTextForDriverPhone);
		editText_UserName= (EditText) findViewById(R.id.editTextuserName);
		
		editTextForDriverEmail.setTypeface(appFont);
		editTextForDriverFName.setTypeface(appFont);
		editTextForDriverLName.setTypeface(appFont);
		editTextForDriverPassword.setTypeface(appFont);
		editTextForDriverPhone.setTypeface(appFont);
		editText_UserName.setTypeface(appFont);
			
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		textForHeader.setText("ADD DRIVER");
		
		editTextForDriverPhone.setFilters(new InputFilter[] {filter});
		editTextForDriverPassword.setFilters(new InputFilter[]{Constant.filter_password});       
        editTextForDriverEmail.setFilters(new InputFilter[]{Constant.filter_email});
        editTextForDriverFName.setFilters(new InputFilter[]{Constant.filter_fname});
        editTextForDriverLName.setFilters(new InputFilter[]{Constant.filter_fname});
        editText_UserName.setFilters(new InputFilter[]{Constant.filter_username});
        
        editTextForDriverPhone.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
             if (keyCode == KeyEvent.KEYCODE_DEL) {

              if (editTextForDriverPhone.getText().length() == 14) {
               String str = editTextForDriverPhone.getText().toString();
               str = str.replace("-", "");
               str = str.replace("+", "");
               str = "(" + str.substring(0, 3) + ")"
                 + str.substring(3, 6) + "-" + str.substring(6);
               editTextForDriverPhone.setText(str);
               editTextForDriverPhone.setSelection(editTextForDriverPhone.getText().length());
              } else if (editTextForDriverPhone.getText().length() == 13) {
               String str = editTextForDriverPhone.getText().toString();
               str = str.replace("-", "");
               str = str.replace("(", "");
               str = str.replace(")", "");
               str = str.substring(0, 3) + "-" + str.substring(3);
               editTextForDriverPhone.setText(str);
               editTextForDriverPhone.setSelection(editTextForDriverPhone.getText().length());
              }

             }
             return false;
            }
           });
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnForAddDriver) {
			fName=editTextForDriverFName.getText().toString().trim();
			lName=editTextForDriverLName.getText().toString().trim();
			userName=editText_UserName.getText().toString().trim();
			try {
				first_Name=fName.substring(fName.length() - 1);
				last_Name=lName.substring(lName.length() - 1);
			} catch (Exception e) {
			Log.e("Exception in add driver",""+e);
			}	
			
			String result = editTextForDriverPhone.getText().toString().trim().replaceAll("[^0-9]","");
			Log.e("After remove special Characters",result);
			if(result.trim().length()==0){
				Utils.ShowAlert(AddDriver.this,
						"Phone number field should not be empty.");
			}
			else if ((result.length() ==7 || result.length() ==10)||result.length() ==11) {
			if (editTextForDriverEmail.getText().toString().trim().length() > 0
					&& fName
							.length() > 0
					&& lName
							.length() > 0
					&& editTextForDriverPassword.getText().toString().trim()
							.length() > 0
					&& userName.length() > 0) {
				if (Utils.isEmailValid(editTextForDriverEmail.getText()
						.toString().trim())) {										
					boolean isOnline = Utils.isOnline();
					Log.e("isOnline", isOnline + "");
					if (isOnline) {						
						if(first_Name.equals(".") || first_Name.equals("_") || last_Name.equals(".") || last_Name.equals("_")
								||userName.equals(".")||userName.equals("_")){
							Utils.ShowAlert(AddDriver.this,
									"Please enter a valid string.");
						}else if(fName.contains("..")||lName.contains("..")||fName.contains("__")||lName.contains("__")
								||userName.equals("..")||userName.equals("__")){
							Utils.ShowAlert(AddDriver.this,
									"Please enter a valid name.");
						}else if(fName.startsWith(".")||lName.startsWith(".")||fName.startsWith("_")||lName.startsWith("_")
								||userName.startsWith(".")||userName.startsWith("_")){
							Utils.ShowAlert(AddDriver.this,
									"Special Characters are not allowed at the beginning of the string.");
						}
						/*else if(fName.indexOf('.', fName.indexOf('.') + 1) != -1){
							Utils.ShowAlert(AddDriver.this,
									"Please enter a valid FirstName.");
						}*/
						else{
					new AddDrivers().execute();
						}
					}
					else
					{
						Utils.ShowAlert(AddDriver.this, Constant.networkDisconected);
					}					
				} else {
					Utils.ShowAlert(AddDriver.this,
							"Please enter a valid email.");
				}
			} else {
				Utils.ShowAlert(AddDriver.this, "Please enter all fields.");
			}
			}else{
				Utils.ShowAlert(AddDriver.this,
						"Please enter a valid phone number.");
			}

		} else if (v.getId() == R.id.backBtn) {
			finish();
		}
	}

	public class AddDrivers extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		String status = "", error, itemQuantity = "";

		@Override
		protected void onPostExecute(Void result) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {

				if (status.equals("1")) {
					Utils.ShowAlert(AddDriver.this, "Driver added.");
					startActivity(new Intent(AddDriver.this,
							DriversActivity.class));
				} else {
					Utils.ShowAlert(AddDriver.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(AddDriver.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
			String driverName =userName;
	//		String driverName =userName fName + lName;
			String driverFName = fName;
			String driverLName = lName;
			String driverEmail = editTextForDriverEmail.getText().toString()
					.trim();
			String driverPwd = editTextForDriverPassword.getText().toString()
					.trim();
			String driverPhone = editTextForDriverPhone.getText().toString()
					.trim();

			nameValuePair.add(new BasicNameValuePair("driver_username",
					driverName));
			nameValuePair.add(new BasicNameValuePair("driver_firstname",
					driverFName));
			nameValuePair.add(new BasicNameValuePair("driver_lastname",
					driverLName));
			nameValuePair.add(new BasicNameValuePair("driver_email",
					driverEmail));
			nameValuePair.add(new BasicNameValuePair("driver_password",
					driverPwd));
			nameValuePair.add(new BasicNameValuePair("driver_phone",
					driverPhone));
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.vendorAddDriver()).getJSONObjectfromURL(
					RequestType.POST, nameValuePair);

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

	public static InputFilter filter = new InputFilter() {
		  @Override
		  public CharSequence filter(CharSequence source, int start, int end,
		    Spanned dest, int dstart, int dend) {
		   if (source.length() > 0) {

		    if (dstart == 6) {
		     String str = dest.toString() + source;
		     str = str.replace("-", "");
		     str = str.substring(0, 3) + "-" + str.substring(3);
		     editTextForDriverPhone.setText(str);
		     editTextForDriverPhone.setSelection(editTextForDriverPhone.getText().length());
		    }
		    if (dstart == 10) {
		     String str = dest.toString() + source;
		     str = str.replace("-", "");
		     str = "(" + str.substring(0, 3) + ")" + str.substring(3, 6)
		       + "-" + str.substring(6);
		     editTextForDriverPhone.setText(str);
		     editTextForDriverPhone.setSelection(editTextForDriverPhone.getText().length());
		    }
		    if (dstart == 13) {
		     String str = dest.toString() + source;
		     str = str.replace("-", "");
		     str = str.replace("(", "");
		     str = str.replace(")", "");
		     str = "+" + str.substring(0, 1) + "-" + str.substring(1, 4)
		       + "-" + str.substring(4, 7) + "-"
		       + str.substring(7);
		     editTextForDriverPhone.setText(str);
		     editTextForDriverPhone.setSelection(editTextForDriverPhone.getText().length());
		    }

		    else if (dstart > 14)
		     return "";
		   } else {
		    // 1-> 123-3454 (7number)
		    // 2-> (123)456-4356 (10number)
		    // 3-> +1-123-456-2345 (11 number)
		   }
		   return null;
		  }
		 };
}
