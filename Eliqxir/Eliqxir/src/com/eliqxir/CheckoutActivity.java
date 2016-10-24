package com.eliqxir;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;



public class CheckoutActivity extends SlidingMenuActivity implements
		OnClickListener {
	static EditText editTxtForPhone;
	ArrayList<String> day = new ArrayList<String>();
	ArrayList<String> year = new ArrayList<String>();
	ArrayList<String> month = new ArrayList<String>();
	ArrayList<String> cards = new ArrayList<String>();
	ArrayList<String> ccYear = new ArrayList<String>();
	LinearLayout layoutForSign, layoutForCaptureSignature,
			layoutForScanCreditCard;
	signature mSignature;
	ScrollView scrollBar;
	RelativeLayout layHeaderBack, layHeaderCart, layHeaderSlider;
	ImageView imageView, imageViewSign,ivDeleteSignature;
//	Spinner cardSpinner, monthExpSpinner, yearExpSpinner;
	TextView cardSpinner, monthExpSpinner, yearExpSpinner;
	ImageButton backImg, btnSlideMenu, cartBtn;
	TextView textForHeader, textForTotalValue;
	Button btnForSave, btnForClear, btnForConfirm, btnForClose;
	Dialog dialog;
	String flag="1",selected_Month,selected_Year;
	
	SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
	SimpleDateFormat format11 = new SimpleDateFormat("dd, MMM, yyyy ");
	SimpleDateFormat format1 = new SimpleDateFormat("dd, MMM, yyyy hh:mm");
	SimpleDateFormat currentFormat24 = new SimpleDateFormat(
			"dd, MMM, yyyy hh:mm z");
	SimpleDateFormat formatDateTime12 = new SimpleDateFormat(
			"dd, MMM, yyyy hh:mm a");
	SimpleDateFormat formatTime12 = new SimpleDateFormat("h:mm");
	SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
	SharedPreferences sharedpreferences;
	String customerId, productId, quantities, optionId, valueId, img_name = "",
			totPrice, address = "", firstName, lastName, dob, yearOfBirth,notes,
			monthOfBirth, dateOfBirth;
	EditText editTxtForDeliveryName, editTxtForAddress1, editTxtForAddress2,
			editTxtForAddress3, editTxtForAddress4, editTxtForAddress5,
			 editTxtForCcNo, editTxtForCcId,
			editTxtForBillingName, editTxtForBillingAddress1,
			editTxtForBillingAddress2, editTxtForBillingAddress3,
			editTxtForBillingAddress4, editTxtForBillingAddress5,
			editTxtForDate, editTxtForTime, monthDobSpinner, dayDobSpinner,editTxt_Notes,
			yearDobSpinner;
	Bitmap b, returnedBitmap;
	CheckBox chkBoxForBilling;
	byte[] byteArray;
	String address1, address2, address3, address4, address5, address11,
			deliverName, billingName, address12, address13, address14,deliverFName,billingFName,deliverLName,
			address15, ccId = "", ccNo = "", cardToCheck = Constant.visa,billingLName,
			cardType = "VI", expMonth = "", expYear = "", selectedCard = "";
	int monthInt = 1;
	private int mYear, mMonth, mDay;
	DBAdapter db;
	boolean imageBitmap = false,alertShowing=false;
	private int hour;
	private int minute;
	int time_format = 0;
	String aTime;
	static final int TIME_DIALOG_ID = 1111;

	public void onResume() {
		String addressSession = SessionStore.getLocation(getBaseContext());

		if (addressSession != null) {
			String[] addressSplits = addressSession.split("\n");
			String address1 = addressSplits[0];
			String address2 = addressSplits[1];
			String address3 = addressSplits[2];
			String[] addressSplit = address3.split(",");
			String state = addressSplit[0];
			String countryCode = addressSplit[1];
			String[] addressSpace = countryCode.split(" ");
			String countryNameCode = addressSpace[0];
			String countryNamePinCode = addressSpace[1];
			/*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
			      !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				editTxtForAddress1.setText("");
				editTxtForAddress2.setText("");
				editTxtForAddress3.setText("");
				editTxtForAddress4.setText("");
				editTxtForAddress5.setText("");
				
				editTxtForAddress1.setFocusable(true);
				editTxtForAddress1.setClickable(true);
				editTxtForAddress1.setEnabled(true);
								
				editTxtForAddress2.setFocusable(true);
				editTxtForAddress2.setClickable(true);
				editTxtForAddress2.setEnabled(true);
				
				editTxtForAddress3.setFocusable(true);
				editTxtForAddress3.setClickable(true);
				editTxtForAddress3.setEnabled(true);
				
				editTxtForAddress4.setFocusable(true);
				editTxtForAddress4.setClickable(true);
				editTxtForAddress4.setEnabled(true);
				
				editTxtForAddress5.setFocusable(true);
				editTxtForAddress5.setClickable(true);
				editTxtForAddress5.setEnabled(true);
				
				Log.e("EEEEEEEEEEEEEEEEEEE","FFFFFFFFFFFFFFFF");
			}else{*/
				
			editTxtForAddress1.setText(address1);
			editTxtForAddress2.setText(address2);
			/*editTxtForAddress3.setText(state);
			editTxtForAddress4.setText(countryNameCode);*/
			editTxtForAddress3.setText("IL");
			editTxtForAddress4.setText("US");
			editTxtForAddress5.setText(countryNamePinCode);			
			
			editTxtForAddress1.setEnabled(false);			
			editTxtForAddress2.setEnabled(false);			
			editTxtForAddress3.setEnabled(false);		
			editTxtForAddress4.setEnabled(false);			
			editTxtForAddress5.setEnabled(false);
			
			if (chkBoxForBilling.isChecked()) {
				editTxtForBillingAddress1.setText(editTxtForAddress1.getText()
						.toString());
				editTxtForBillingAddress2.setText(editTxtForAddress2.getText()
						.toString());
				editTxtForBillingAddress3.setText(editTxtForAddress3.getText()
						.toString());
				editTxtForBillingAddress4.setText(editTxtForAddress4.getText()
						.toString());
				editTxtForBillingAddress5.setText(editTxtForAddress5.getText()
						.toString());								
			}
	//		}
		} else {			
			Utils.ShowAlert(CheckoutActivity.this,"location details are not present to save location, press Yes for location window");		
		}
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.checkout);
		
		month.add("JAN");
		month.add("FEB");
		month.add("MAR");
		month.add("APR");
		month.add("MAY");
		month.add("JUN");
		month.add("JUL");
		month.add("AUG");
		month.add("SEP");
		month.add("OCT");
		month.add("NOV");
		month.add("DEC");
		for (int i = 1; i < 32; i++) {
			String val = Integer.toString(i);
			day.add(val);
		}

		for (int i = 1; i < 100; i++) {
			int years = Calendar.getInstance().get(Calendar.YEAR) - 1 + i;
			String valYear = Integer.toString(years);
			ccYear.add(valYear);
		}
		expYear = ccYear.get(0);

		cards.add("VISA");
		cards.add("American Express");
		cards.add("MASTER CARD");
		cards.add("DISCOVER");

		db = new DBAdapter(getBaseContext());

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		productId = getIntent().getExtras().getString("prdtId");
		quantities = getIntent().getExtras().getString("quantity");
		optionId = getIntent().getExtras().getString("optionId");
		valueId = getIntent().getExtras().getString("valueId");
		totPrice = getIntent().getExtras().getString("tot_price");
		img_name = getIntent().getExtras().getString("img_name");
	//	tax=getIntent().getExtras().getString("tax");
		
		// firstName = getIntent().getExtras().getString("first_name");
		// lastName = getIntent().getExtras().getString("last_name");
		// dob = getIntent().getExtras().getString("dob");
		Log.i("optionId is>>>>>>>>>>>", "" + optionId);
		Log.i("valueId is????????????", "" + totPrice);
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		monthDobSpinner = (EditText) findViewById(R.id.spineerForMonth);
		chkBoxForBilling = (CheckBox) findViewById(R.id.chkBoxForBilling);
		// monthDobSpinner.setAdapter(new SpinnerAdapter(getBaseContext(),
		// R.layout.custom_spinner, month, "DOB"));
		dayDobSpinner = (EditText) findViewById(R.id.spineerForDate);
		// dayDobSpinner.setAdapter(new SpinnerAdapter(getBaseContext(),
		// R.layout.custom_spinner, day, "DOB"));
		yearDobSpinner = (EditText) findViewById(R.id.spineerForYear);
		// yearDobSpinner.setAdapter(new SpinnerAdapter(getBaseContext(),
		// R.layout.custom_spinner, year, "DOB"));

		/*cardSpinner = (Spinner) findViewById(R.id.spinnerForVisa);
		cardSpinner.setAdapter(new SpinnerAdapter(getBaseContext(),
				R.layout.custom_spinner, cards, "Expired"));
		monthExpSpinner = (Spinner) findViewById(R.id.spinnerForExpMonth);
		monthExpSpinner.setAdapter(new SpinnerAdapter(getBaseContext(),
				R.layout.custom_spinner, month, "Expired"));
		yearExpSpinner = (Spinner) findViewById(R.id.spinnerForExpYear);
		yearExpSpinner.setAdapter(new SpinnerAdapter(getBaseContext(),
				R.layout.custom_spinner, ccYear, "Expired"));*/
		
		cardSpinner = (TextView) findViewById(R.id.spinnerForVisa);		
		monthExpSpinner = (TextView) findViewById(R.id.spinnerForExpMonth);		
		yearExpSpinner = (TextView) findViewById(R.id.spinnerForExpYear);		
		
		layoutForSign = (LinearLayout) findViewById(R.id.layoutForSign);
		layoutForSign.setOnClickListener(this);
		scrollBar = (ScrollView) findViewById(R.id.scrollBar);
		btnForConfirm = (Button) findViewById(R.id.btnForConfirm);
		editTxtForAddress1 = (EditText) findViewById(R.id.editTxtForAddress1);
		editTxtForAddress2 = (EditText) findViewById(R.id.editTxtForAddress2);
		editTxtForAddress3 = (EditText) findViewById(R.id.editTxtForAddress3);
		editTxtForAddress4 = (EditText) findViewById(R.id.editTxtForAddress4);
		editTxtForAddress5 = (EditText) findViewById(R.id.editTxtForAddress5);
		editTxtForDeliveryName = (EditText) findViewById(R.id.editTxtForDeliveryName);
		editTxtForBillingAddress1 = (EditText) findViewById(R.id.editTxtForBillingAddress1);
		editTxtForBillingAddress2 = (EditText) findViewById(R.id.editTxtForBillingAddress2);
		editTxtForBillingAddress3 = (EditText) findViewById(R.id.editTxtForBillingAddress3);
		editTxtForBillingAddress4 = (EditText) findViewById(R.id.editTxtForBillingAddress4);
		editTxtForBillingAddress5 = (EditText) findViewById(R.id.editTxtForBillingAddress5);
		editTxtForBillingName = (EditText) findViewById(R.id.editTxtForBillingName);
		editTxtForPhone = (EditText) findViewById(R.id.editTxtForPhoneNo);
		editTxtForCcNo = (EditText) findViewById(R.id.editTxtForCcNo);
		editTxtForCcId = (EditText) findViewById(R.id.editTxtForExpCcid);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
//		layoutForScanCreditCard = (LinearLayout) findViewById(R.id.layoutForScanCreditCard);
//		layoutForScanCreditCard.setOnClickListener(this);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		textForTotalValue = (TextView) findViewById(R.id.textForTotalValue);
		editTxtForDate = (EditText) findViewById(R.id.editTxtForDeliveryDate);
		editTxtForTime = (EditText) findViewById(R.id.editTxtForDeliveryTime);
		ivDeleteSignature = (ImageView) findViewById(R.id.iv_delete_signature);
		ivDeleteSignature.setOnClickListener(this);
		editTxt_Notes = (EditText) findViewById(R.id.edittextNotes);
//		text_Notes = (TextView) findViewById(R.id.textForNotes);
		
		textForTotalValue.setText(totPrice);
		backImg.setOnClickListener(this);
		btnForConfirm.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);
		backImg.setBackgroundResource(R.drawable.close_btn);
		textForHeader.setText("CHECKOUT");
		customerId = sharedpreferences.getString("userId", null);
		firstName = sharedpreferences.getString("firstname", null);
		lastName = sharedpreferences.getString("lastname", null);
		dob = sharedpreferences.getString("dob", null);
		String[] sp = dob.split("-");
		yearOfBirth = sp[0];
		monthOfBirth = sp[1];
		dateOfBirth = sp[2];
		monthDobSpinner.setText(monthOfBirth);
		yearDobSpinner.setText(yearOfBirth);
		dayDobSpinner.setText(dateOfBirth);
		editTxtForDeliveryName.setText(firstName+" "+lastName);

//		editTxtForAddress1.setFocusable(false);
//		editTxtForAddress2.setFocusable(false);
//		editTxtForAddress3.setFocusable(false);
//		editTxtForAddress4.setFocusable(false);
//		editTxtForAddress5.setFocusable(false);
		editTxtForDate.setFocusable(false);
		editTxtForTime.setFocusable(false);
		editTxtForAddress1.setOnClickListener(this);
		editTxtForAddress2.setOnClickListener(this);
		editTxtForAddress3.setOnClickListener(this);
		editTxtForAddress4.setOnClickListener(this);
		editTxtForAddress5.setOnClickListener(this);
		editTxtForDate.setOnClickListener(this);
		editTxtForTime.setOnClickListener(this);
//		editTxtForPhone.addTextChangedListener(phoneText);
		
		editTxtForPhone.setFilters(new InputFilter[] {filter});
		
		final Calendar c = Calendar.getInstance();
		// Current Hour
		hour = c.get(Calendar.HOUR_OF_DAY);
		// Current Minute
		minute = c.get(Calendar.MINUTE);

		editTxtForBillingAddress3.setText("IL");
        editTxtForBillingAddress4.setText("US");
        
        editTxtForBillingAddress3.setEnabled(false);
        editTxtForBillingAddress4.setEnabled(false);
        
		// set current time into output textview
		// updateTime(hour, minute);     

        editTxtForPhone.setOnKeyListener(new OnKeyListener() {
         @Override
         public boolean onKey(View v, int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_DEL) {

           if (editTxtForPhone.getText().length() == 14) {
            String str = editTxtForPhone.getText().toString();
            str = str.replace("-", "");
            str = str.replace("+", "");
            str = "(" + str.substring(0, 3) + ")"
              + str.substring(3, 6) + "-" + str.substring(6);
            editTxtForPhone.setText(str);
            editTxtForPhone.setSelection(editTxtForPhone.getText().length());
           } else if (editTxtForPhone.getText().length() == 13) {
            String str = editTxtForPhone.getText().toString();
            str = str.replace("-", "");
            str = str.replace("(", "");
            str = str.replace(")", "");
            str = str.substring(0, 3) + "-" + str.substring(3);
            editTxtForPhone.setText(str);
            editTxtForPhone.setSelection(editTxtForPhone.getText().length());
           }

          }
          return false;
         }
        });
        
      	chkBoxForBilling
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isCheck) {
						if (isCheck) {

							if (chkBoxForBilling.isChecked()) {															
					    	    
								editTxtForBillingName
										.setText(editTxtForDeliveryName
												.getText().toString());
								editTxtForBillingAddress1
										.setText(editTxtForAddress1.getText()
												.toString());
								editTxtForBillingAddress2
										.setText(editTxtForAddress2.getText()
												.toString());
							/*	editTxtForBillingAddress3
										.setText(editTxtForAddress3.getText()
												.toString());
								editTxtForBillingAddress4
										.setText(editTxtForAddress4.getText()
												.toString());*/
								editTxtForBillingAddress3.setText("IL");
						        editTxtForBillingAddress4.setText("US");
								editTxtForBillingAddress5
										.setText(editTxtForAddress5.getText()
												.toString());								
																
								editTxtForBillingName.setEnabled(false);								
								editTxtForBillingAddress1.setEnabled(false);								
								editTxtForBillingAddress2.setEnabled(false);								
								editTxtForBillingAddress3.setEnabled(false);								
								editTxtForBillingAddress4.setEnabled(false);								
								editTxtForBillingAddress5.setEnabled(false);								
							}

						} else {							
							
							editTxtForBillingName.setEnabled(true);
							editTxtForBillingAddress1.setEnabled(true);							
							editTxtForBillingAddress2.setEnabled(true);							
							editTxtForBillingAddress3.setEnabled(false);							
							editTxtForBillingAddress4.setEnabled(false);						
							editTxtForBillingAddress5.setEnabled(true);
							
							editTxtForBillingName.setText("");
							editTxtForBillingAddress1.setText("");
							editTxtForBillingAddress2.setText("");
							editTxtForBillingAddress3.setText("IL");
							editTxtForBillingAddress4.setText("US");
							editTxtForBillingAddress5.setText("");
						}
					}
				});
      	
      	cardSpinner.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				openSpinnerView(cards);				
			}
		});
      	
      	monthExpSpinner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openSpinnerMonthView(month);				
			}
		});
      	
      	yearExpSpinner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openSpinnerYearView(ccYear);					
			}
		});
      	
		/*cardSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				selectedCard = cards.get(position);
				if (selectedCard.equals("VISA")) {
					cardToCheck = Constant.visa;
					cardType = "VI";
				} else if (selectedCard.equals("American Express")) {
					cardToCheck = Constant.amex;
					cardType = "AE";
				} else if (selectedCard.equals("MASTER CARD")) {
					cardToCheck = Constant.master;
					cardType = "MC";
				} else if (selectedCard.equals("DISCOVER")) {
					cardToCheck = Constant.discover;
					cardType = "DI";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		monthExpSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				expMonth = month.get(position);

				try {
					Calendar cal = Calendar.getInstance();
					cal.setTime(new SimpleDateFormat("MMM").parse(expMonth));
					monthInt = cal.get(Calendar.MONTH) + 1;
					Log.e("monthInt ", monthInt + "");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		yearExpSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				expYear = ccYear.get(position);
				// try {
				// Calendar cal = Calendar.getInstance();
				// cal.setTime(new SimpleDateFormat("yyyy").parse(expYear));
				// int yearInt = cal.get(Calendar.YEAR) + 1;
				// Log.e("yearInt ",yearInt+"");
				// } catch (ParseException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});*/
	
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
		     editTxtForPhone.setText(str);
		     editTxtForPhone.setSelection(editTxtForPhone.getText().length());
		    }
		    if (dstart == 10) {
		     String str = dest.toString() + source;
		     str = str.replace("-", "");
		     str = "(" + str.substring(0, 3) + ")" + str.substring(3, 6)
		       + "-" + str.substring(6);
		     editTxtForPhone.setText(str);
		     editTxtForPhone.setSelection(editTxtForPhone.getText().length());
		    }
		    if (dstart == 13) {
		     String str = dest.toString() + source;
		     str = str.replace("-", "");
		     str = str.replace("(", "");
		     str = str.replace(")", "");
		     str = "+" + str.substring(0, 1) + "-" + str.substring(1, 4)
		       + "-" + str.substring(4, 7) + "-"
		       + str.substring(7);
		     editTxtForPhone.setText(str);
		     editTxtForPhone.setSelection(editTxtForPhone.getText().length());
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
		 
	/*TextWatcher phoneText=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.e("Given Length",""+s.length());
			if(s.length()==7){
				Log.e("Success","message");
				String sphone=editTxtForPhone.getText().toString().trim();
				String f=sphone.substring(0, 3) + "-" + sphone.substring(3, sphone.length());
				Log.e("After Inserted", f);
				editTxtForPhone.setText(f);
			}
			
			if(s.length()==10){
				Log.e("Success 10","message");
				String sphone=editTxtForPhone.getText().toString().trim();
				String f="("+sphone.substring(0, 3)+")"+sphone.substring(3,7)+ "-" + sphone.substring(7, sphone.length());
				Log.e("After Inserted", f);
				editTxtForPhone.setText(f);
			}
	//		05-21 18:13:55.239: E/After Inserted(9238): (134)-56-2336

			
			//1-> 123-3454                (7number)  
			//2-> (123)456-4356      (10number) 
			//3-> +1-123-456-2345  (11 number)
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub			
		}
	};*/
	
	
	/*public class SpinnerAdapter extends ArrayAdapter<String> {

		Context mContext;
		String dobChk;
		ArrayList<String> arrayValue;

		public SpinnerAdapter(Context context, int customSpinner,
				ArrayList<String> month, String dob) {
			// TODO Auto-generated constructor stub
			super(context, customSpinner, month);
			mContext = context;
			dobChk = dob;
			arrayValue = month;
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			View spinnerView = inflater.inflate(R.layout.custom_spinner,
					parent, false);
			TextView spinnerText = (TextView) spinnerView
					.findViewById(R.id.textForSpinnerItem);
			spinnerText.setTextColor(getResources().getColor(R.color.txt_color_gray));
			spinnerText.setText(arrayValue.get(position));

			return spinnerView;
		}
	}*/

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*if (v.getId() == R.id.layoutForScanCreditCard) {
			// new ScanCCard().execute();
			boolean checkCard = checkCCard();
			Log.e("checkCard", checkCard + "");
			if (checkCard) {
				Utils.ShowAlert(CheckoutActivity.this,
						"You have entered valid card details.");
			} else {

			}
		}*/
		if (v.getId() == R.id.layoutForSign) {
		
			showLogindialog();
		}
		if (v.getId() == R.id.layHeaderBack) {
			finish();
		}

		if (v.getId() == R.id.backBtn) {
			finish();
		}

//		if (v.getId() == R.id.editTxtForAddress1) {
//			Utils.ShowAlert(CheckoutActivity.this, "You can edit delivery address at Location menu.");
//			//ShowAlert("Are you sure you want to edit location?");
//		}
//
//		if (v.getId() == R.id.editTxtForAddress2) {
//			Utils.ShowAlert(CheckoutActivity.this, "You can edit delivery address at Location menu.");
//			//ShowAlert("Are you sure you want to edit location?");
//		}
//
//		if (v.getId() == R.id.editTxtForAddress3) {
//			Utils.ShowAlert(CheckoutActivity.this, "You can edit delivery address at Location menu.");
//			//ShowAlert("Are you sure you want to edit location?");
//		}
//
//		if (v.getId() == R.id.editTxtForAddress4) {
//			Utils.ShowAlert(CheckoutActivity.this, "You can edit delivery address at Location menu.");
//		//	ShowAlert("Are you sure you want to edit location?");
//		}
//
//		if (v.getId() == R.id.editTxtForAddress5) {
//			Utils.ShowAlert(CheckoutActivity.this, "You can edit delivery address at Location menu.");
//			//ShowAlert("Are you sure you want to edit location?");
//		}

		if (v.getId() == R.id.editTxtForDeliveryDate) {
			// InputMethodManager inputMethodManager = (InputMethodManager)
			// this.getSystemService(Activity.INPUT_METHOD_SERVICE);
			// inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
			// 0);
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dpd = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// Display Selected date in textbox
							String selectedDate = dayOfMonth + "-"
									+ (monthOfYear + 1) + "-" + year;
							try {
								Calendar cal = Calendar.getInstance();
								cal.setTime(new SimpleDateFormat("dd-MM-yyyy")
										.parse(selectedDate));

								Date date2 = format2.parse(selectedDate);

								System.out.println(format11.format(date2));
								editTxtForDate.setText(format11.format(date2));

							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}, mYear, mMonth, mDay);
		
			dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//			dpd.getDatePicker().setMinDate(c.getTimeInMillis());
			dpd.show();
		}

		if (v.getId() == R.id.editTxtForDeliveryTime) {

			showDialog(TIME_DIALOG_ID);
		}

		
		if (v.getId() == R.id.iv_delete_signature) {
  	   deleteSignature();
	   }
		
		if (v.getId() == R.id.btnForConfirm) {
			alertShowing=false;
			deliverName = editTxtForDeliveryName.getText().toString().trim();	
			String[] deliver=deliverName.split(" ");
			Log.e("Deliver Address",""+deliver.length);
			if(deliver.length==2){
			deliverFName=deliver[0];
			deliverLName=deliver[1];
			}else {
				deliverFName=deliver[0];
			}			
						
			address1 = editTxtForAddress1.getText().toString();
			address2 = editTxtForAddress2.getText().toString();
			address3 = editTxtForAddress3.getText().toString();
			address4 = editTxtForAddress4.getText().toString();
			address5 = editTxtForAddress5.getText().toString();
			notes=editTxt_Notes.getText().toString();
			
			billingName = editTxtForBillingName.getText().toString().trim();
			String[] billing=billingName.split(" ");
			if(billing.length==2){
			billingFName=billing[0];
			billingLName=billing[1];	
		    }else {
		    	billingFName=billing[0];
		    }
			
			address11 = editTxtForBillingAddress1.getText().toString();
			address12 = editTxtForBillingAddress2.getText().toString();
			address13 = editTxtForBillingAddress3.getText().toString();
			address14 = editTxtForBillingAddress4.getText().toString();
			address15 = editTxtForBillingAddress5.getText().toString();
			
			String result = editTxtForPhone.getText().toString().trim().replaceAll("[^0-9]","");
			Log.e("After remove special Characters",result);
			
			if (deliverName.length() > 0) {
				if (billingName.length() > 0) {
					if (address11.length() > 0 && address12.length() > 0
							&& address13.length() > 0 && address14.length() > 0
							&& address15.length() > 0) {
						if (address1.length() > 0 && address2.length() > 0
								&& address3.length() > 0
								&& address4.length() > 0
								&& address5.length() > 0) {

							if (layoutForSign.getChildCount() > 0) {

								Log.e("cardToCheck", cardToCheck);

						//		String result= editTxtForPhone.getText().toString().trim().replaceAll("(-+)","");
						//		int val=Integer.parseInt(result);
								Log.e("After remove special Ch",""+result.length());
								if(result.trim().length()==0){
									Utils.ShowAlert(CheckoutActivity.this,
											"Phone number field should not be empty.");
								}
								else if ((result.length() ==7 || result.length() ==10)||result.length() ==11) {
									// if(Constant.CheckPhoneNumber(editTxtForPhone.getText().toString()))
									// {

									if (editTxtForDate.getText().toString()
											.trim().length() > 0) {
										if (editTxtForTime.getText().toString()
												.length() > 0) {

											Date date11 = null;
											Date date21 = null, time = null;
											try {

												try {
													time_format = Settings.System
															.getInt(getContentResolver(),
																	Settings.System.TIME_12_24);
												} catch (SettingNotFoundException e) {
													e.printStackTrace();
												}
												Log.e("Time format: ",
														time_format + "");
												if (time_format == 12) {
													Calendar c1 = Calendar
															.getInstance();

													String currentDate1 = formatDateTime12
															.format(c1
																	.getTime());
													date21 = formatDateTime12
															.parse(currentDate1);
													System.out
															.println("Current date time 12 "
																	+ formatDateTime12
																			.format(date21));
													date11 = formatDateTime12
															.parse(editTxtForDate
																	.getText()
																	.toString()
																	.trim()
																	+ " "
																	+ editTxtForTime
																			.getText()
																			.toString()
																			.trim());

													System.out
															.println("selected date time 12 "
																	+ formatDateTime12
																			.format(date11));

												} else {
													Calendar c1 = Calendar
															.getInstance();

													String currentDate1 = currentFormat24
															.format(c1
																	.getTime());
													Log.e("currentdate1",
															currentDate1);
													date21 = currentFormat24
															.parse(currentDate1);
													System.out
															.println("Current date time 24 "
																	+ currentFormat24
																			.format(date21));
													time = formatTime12
															.parse(editTxtForTime
																	.getText()
																	.toString()
																	.trim());
													System.out
															.println(formatTime12
																	.format(time));
													date11 = format1
															.parse(editTxtForDate
																	.getText()
																	.toString()
																	.trim()
																	+ " "
																	+ formatTime12
																			.format(time));

													System.out
															.println("selected date time 24 "
																	+ format1
																			.format(date11));

												}

											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											Log.e("date11 ", date11 + "");
											Log.e("date21 ", date21 + "");
							//				if (date11.compareTo(date21) > 0) {
												boolean checkCard = checkCCard();
												Log.e("checkCard", checkCard	+ "");
												if (checkCard) {
													if(notes.length()>0){
//														if(result.length()==' '){
//															if(editTxtForCcNo.getText().toString().trim().equals(" ")){
//																if(editTxtForCcId.getText().toString().trim().equals(" ")){
													check();
//																}else{
//																	Utils.ShowAlert(CheckoutActivity.this,
//																			"Please enter CCID.");
//															    }
//															}else{
//																Utils.ShowAlert(CheckoutActivity.this,
//																		"Please enter CCNO.");
//															}
														/*}else{
															Utils.ShowAlert(CheckoutActivity.this,
																	"Please enter phone number.");
														}*/
													}else{
														Utils.ShowAlert(
																CheckoutActivity.this,
																"Notes field should not be empty.");
													}
												}
									//		} else {
												/*Utils.ShowAlert(
														CheckoutActivity.this,
														"Please select future date and time to deliver.");								}
										} */
										else {
											Log.e("121212122","3232232");
											/*Utils.ShowAlert(
													CheckoutActivity.this,
													"Please enter credit card fields correctly.");*/
											if(!alertShowing)
											{
											Utils.ShowAlert(
													CheckoutActivity.this,
													"Please enter credit card fields correctly.");
											}
											alertShowing=false;											
										}
									} else {
										Utils.ShowAlert(CheckoutActivity.this,
												"Delivery Date should not be empty.");
									}
									 }
									// else
									// {
									// Utils.ShowAlert(CheckoutActivity.this,
									// "Please enter a valid phone number.");
									// }

								} else {
									Utils.ShowAlert(CheckoutActivity.this,
											"Please enter a valid phone number.");
								}
							} else {
								Utils.ShowAlert(CheckoutActivity.this,
										"Signature must not be empty");
							}
						} else {
							Utils.ShowAlert(CheckoutActivity.this,
									"Shipping address fields must not be empty");
						}
					} else {
						Utils.ShowAlert(CheckoutActivity.this,
								"Billing address fields must not be empty");
					}
				} else {
					Utils.ShowAlert(CheckoutActivity.this,
							"Billing name must not be empty.");
				}
			} else {
				Utils.ShowAlert(CheckoutActivity.this,
						"Shipping name must not be empty.");
			}
		}
	}

/*	public boolean checkCCard() {
		boolean isCCValid = false;
		try {
			ccId = editTxtForCcId.getText().toString();
			ccNo = editTxtForCcNo.getText().toString();
			Calendar c = Calendar.getInstance();
			String currentDate = df.format(c.getTime());
            
	//		Log.e("Selected Year", selected_Year);
			//Date date1 = df.parse(monthInt + "/" + expYear);
			Date date1 = df.parse(monthInt + "/" + selected_Year);
			Date date2 = df.parse(currentDate);

			System.out.println(df.format(date1));
			System.out.println(df.format(date2));
			if (ccId.length() > 0 && ccNo.length() > 0) {
				if (Constant.CheckCard(editTxtForCcNo.getText().toString(),
						cardToCheck)) {

					if (selectedCard.equals("American Express")) {
						if (editTxtForCcId.length() == 4) {
							Log.e("date1.compareTo(date2)",
									date1.compareTo(date2) + "");
							if (date1.compareTo(date2) >= 0) {

								isCCValid = true;
							} else if (date1.compareTo(date2) < 0) {
								isCCValid = false;
								Utils.ShowAlert(CheckoutActivity.this,
										"Provide an valid card expiry month and year.");

							}
						} else {
							isCCValid = false;
							Utils.ShowAlert(CheckoutActivity.this,
									"CCV no should be 4 digit length for Amex.");
						}
					} else {
						if (editTxtForCcId.length() == 3) {
							Log.e("date1.compareTo(date2)",
									date1.compareTo(date2) + "");
							if (date1.compareTo(date2) >= 0) {
								isCCValid = true;
							} else if (date1.compareTo(date2) < 0) {
								isCCValid = false;
								Utils.ShowAlert(CheckoutActivity.this,
										"Provide an valid card expiry month and year.");
							}

						} else {
							isCCValid = false;
							Utils.ShowAlert(CheckoutActivity.this,
									"CCV no should be 3 digit length.");
						}
					}
				} else {
					isCCValid = false;
					Utils.ShowAlert(CheckoutActivity.this,
							"Enter a valid Card Number.");
				}
			} else {
				isCCValid = false;
				Utils.ShowAlert(CheckoutActivity.this,
						"Credit card fields must not be empty");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isCCValid;

	}*/
	public boolean checkCCard() {
		
		  boolean isCCValid = false;
		  try {
		   ccId = editTxtForCcId.getText().toString();
		   ccNo = editTxtForCcNo.getText().toString();
		   Calendar c = Calendar.getInstance();
		   String currentDate = df.format(c.getTime());

		   Date date1 = df.parse(monthInt + "/" + selected_Year);
		   Date date2 = df.parse(currentDate);

		   System.out.println(df.format(date1));
		   System.out.println(df.format(date2));
		   
		   if(ccNo.length()==0)
			{
				Utils.ShowAlert(CheckoutActivity.this,
						"CC Number should not be empty.");
				alertShowing=true;
			}
			else if(ccId.length()==0)
			{
				Utils.ShowAlert(CheckoutActivity.this,
						"CCId should not be empty.");
				alertShowing=true;
			} 
			else if (ccId.length() > 0 && ccNo.length() > 0) {

		    CreditCardValidator cardValidator = null;
		    if (cardType.equals("VI")) {
		     cardValidator = new CreditCardValidator(
		       CreditCardValidator.VISA);
		    } else if (cardType.equals("MC")) {
		     cardValidator = new CreditCardValidator(
		       CreditCardValidator.MASTERCARD);
		    } else if (cardType.equals("AE")) {
		     cardValidator = new CreditCardValidator(
		       CreditCardValidator.AMEX);
		    } else // Discover
		    {
		     cardValidator = new CreditCardValidator(
		       CreditCardValidator.DISCOVER);
		    }
		    if (!cardValidator.isValid(ccNo)) {
		     Utils.ShowAlert(CheckoutActivity.this,
		       "Enter a valid Card Number.");
		     alertShowing=true;
		    } else {

		     if (Constant.CheckCard(editTxtForCcNo.getText().toString(),
		       cardToCheck)) {

		      if (selectedCard.equals("American Express")) {
		       if (editTxtForCcId.length() == 4) {
		        Log.e("date1.compareTo(date2)",
		          date1.compareTo(date2) + "");
		        if (date1.compareTo(date2) >= 0) {
		         isCCValid = true;
		        } else if (date1.compareTo(date2) < 0) {
		         isCCValid = false;
		         Utils.ShowAlert(CheckoutActivity.this,
		           "Provide an valid card expiry month and year.");
		         alertShowing=true;
		        }
		       } else {
		        isCCValid = false;
		        Utils.ShowAlert(CheckoutActivity.this,
		          "CCV no should be 4 digit length for Amex.");
		        alertShowing=true;
		       }
		      } else {
		       if (editTxtForCcId.length() == 3) {
		        Log.e("date1.compareTo(date2)",
		          date1.compareTo(date2) + "");
		        if (date1.compareTo(date2) >= 0) {
		         isCCValid = true;
		        } else if (date1.compareTo(date2) < 0) {
		         isCCValid = false;
		         Utils.ShowAlert(CheckoutActivity.this,
		           "Provide an valid card expiry month and year.");
		         alertShowing=true;
		        }
		       } else {
		        isCCValid = false;
		        Utils.ShowAlert(CheckoutActivity.this,
		          "CCV no should be 3 digit length.");
		        alertShowing=true;
		       }
		      }
		     } else {
		      isCCValid = false;
		      Utils.ShowAlert(CheckoutActivity.this,
		        "Enter a valid Card Number.");
		      alertShowing=true;
		     }
		    }
		   } else {
		    isCCValid = false;
		    /*Utils.ShowAlert(CheckoutActivity.this,
		      "Credit card fields must not be empty");*/
		   }
		  } catch (ParseException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  return isCCValid;
		 }

	public void check() {

		Intent intentToDeliver = new Intent(CheckoutActivity.this,
				DeliveryTipsActivity.class);

		intentToDeliver.putExtra("fname", firstName);
		intentToDeliver.putExtra("lname", lastName);
		intentToDeliver.putExtra("prdtId", productId);
		intentToDeliver.putExtra("optionId", optionId);
		intentToDeliver.putExtra("img_name", img_name);
		intentToDeliver.putExtra("valueId", valueId);
		intentToDeliver.putExtra("quantity", quantities);
		intentToDeliver.putExtra("tot_price", totPrice);
		intentToDeliver.putExtra("address", address1);
		intentToDeliver.putExtra("city", address2);
		intentToDeliver.putExtra("state", address3);
		intentToDeliver.putExtra("country", address4);
		intentToDeliver.putExtra("zip_code", address5);
		intentToDeliver.putExtra("billing_fname",billingFName);
		intentToDeliver.putExtra("billing_lname",billingLName);
	
		intentToDeliver.putExtra("shipping_fname", deliverFName);
		intentToDeliver.putExtra("shipping_lname", deliverLName);
		
		intentToDeliver.putExtra("shipping_address", address11);
		intentToDeliver.putExtra("shipping_city", address12);
		intentToDeliver.putExtra("shipping_state", address13);
		intentToDeliver.putExtra("shipping_country", address14);
		intentToDeliver.putExtra("shipping_zip_code", address15);

		intentToDeliver.putExtra("image", byteArray);
		intentToDeliver.putExtra("phone", editTxtForPhone.getText().toString());
		intentToDeliver.putExtra("cc_id", editTxtForCcId.getText().toString());
		intentToDeliver.putExtra("delivery_date", editTxtForDate.getText()
				.toString());
		intentToDeliver.putExtra("delivery_time", editTxtForTime.getText()
				.toString());
		intentToDeliver.putExtra("cc_number", editTxtForCcNo.getText()
				.toString());
		
		Log.e("totPrice Value 333333333",totPrice);
		Log.e("Selected Card",cardType);
		Log.e("Selected Year",selected_Year);
		Log.e("Selected Month",Integer.toString(monthInt));
		
		intentToDeliver.putExtra("cardType", cardType);
		intentToDeliver.putExtra("expYear", selected_Year);
//		intentToDeliver.putExtra("expYear", expYear);
		intentToDeliver.putExtra("expMonth", Integer.toString(monthInt));
	//	intentToDeliver.putExtra("tax", Integer.parseInt(tax));
		Log.e("Notes value in checkout page",notes);
        intentToDeliver.putExtra("notes",notes);
		startActivity(intentToDeliver);
	//	finish();
		// new OrderConfirm().execute();

	}

	public class signature extends View {
		static final float STROKE_WIDTH = 10f;
		static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		Paint paint = new Paint();
		Path path = new Path();

		float lastTouchX;
		float lastTouchY;
		final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		public void clear() {
			imageBitmap = false;
			path.reset();
			invalidate();
			// save.setEnabled(false);
			flag="0";
		}

		public void save() {
			if (imageBitmap == true) {
				flag="1";
				imageBitmap = false;
				returnedBitmap = Bitmap.createBitmap(
						layoutForCaptureSignature.getWidth(),
						layoutForCaptureSignature.getHeight(),
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(returnedBitmap);
				Drawable bgDrawable = layoutForCaptureSignature.getBackground();
				if (bgDrawable != null)
					bgDrawable.draw(canvas);
				else
					canvas.drawColor(Color.WHITE);
				layoutForCaptureSignature.draw(canvas);

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				returnedBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
				/*
				 * Intent intent = new Intent(); intent.putExtra("byteArray",
				 * bs.toByteArray()); setResult(1, intent);
				 */
				layoutForSign.removeAllViews();
				ImageView imageView = new ImageView(CheckoutActivity.this);
				imageView.setLayoutParams(new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

				b = BitmapFactory.decodeByteArray(bs.toByteArray(), 0,
						bs.toByteArray().length);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				b.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byteArray = stream.toByteArray();
				Log.v("bytearry 1", byteArray + "");
				imageView.setImageBitmap(b);
				layoutForSign.addView(imageView);
				// finish();
				dialog.dismiss();
			} else {
				Utils.ShowAlert(CheckoutActivity.this,
						"Signature field must not be empty");
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float eventX = event.getX();
			float eventY = event.getY();
			// save.setEnabled(true);

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				imageBitmap = true;
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;

			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:

				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) {
					float historicalX = event.getHistoricalX(i);
					float historicalY = event.getHistoricalY(i);
					path.lineTo(historicalX, historicalY);
				}
				path.lineTo(eventX, eventY);
				break;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;
			flag="2";

			return true;
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}

	private void showLogindialog() {

		dialog = new Dialog(CheckoutActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.capturesignature);
		/*
		 * dialog.getWindow().setBackgroundDrawable( new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 */
		dialog.setCanceledOnTouchOutside(false);

		layoutForCaptureSignature = (LinearLayout) dialog
				.findViewById(R.id.layoutForCaptureSignature);
		btnForSave = (Button) dialog.findViewById(R.id.save);
		btnForClear = (Button) dialog.findViewById(R.id.clear);
		imageViewSign = (ImageView) dialog.findViewById(R.id.imageForSignature);
		btnForClose = (Button) dialog.findViewById(R.id.btnForSignatureClose);

		imageViewSign.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.ShowAlert(CheckoutActivity.this,
						"Please clear the previous saved signature");
			}
		});

		if (returnedBitmap != null) {
			imageViewSign.setVisibility(View.VISIBLE);
			layoutForCaptureSignature.setVisibility(View.GONE);
			imageViewSign.setImageBitmap(returnedBitmap);
		} else {
			imageViewSign.setVisibility(View.GONE);
			layoutForCaptureSignature.setVisibility(View.VISIBLE);
			mSignature = new signature(CheckoutActivity.this, null);
			layoutForCaptureSignature.addView(mSignature);
		}

		btnForClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (returnedBitmap != null) {
					returnedBitmap = null;
					imageViewSign.setVisibility(View.GONE);
					layoutForCaptureSignature.setVisibility(View.VISIBLE);
					mSignature = new signature(CheckoutActivity.this, null);
					layoutForCaptureSignature.addView(mSignature);
				}
				mSignature.clear();
				layoutForCaptureSignature.clearAnimation();
			}
		});

		btnForSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (imageViewSign.getVisibility() == View.VISIBLE) {
					dialog.dismiss();
				} else {
					mSignature.save();
				}
			}
		});

		btnForClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag.equals("0")){
					Utils.ShowAlert(CheckoutActivity.this, getBaseContext().getResources().getString(R.string.alert_fill_signature));
				
			   }else if(flag.equals("2")){
				   Utils.ShowAlert(CheckoutActivity.this, getBaseContext().getResources().getString(R.string.alert_save_signature));
				
				}else{					
					dialog.dismiss();	
				}
			}
		});

		
		dialog.show();
	}
	
	

	public class ScanCCard extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(CheckoutActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("card_type", cardType));
			nameValuePairs.add(new BasicNameValuePair("card_number",
					editTxtForCcNo.getText().toString().trim()));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.loginAccount())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("Card Validation", jsonObj + "");

			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			try {
				if (status.equals("1")) {

				} else {
					Utils.ShowAlert(CheckoutActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void ShowAlert(String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(CheckoutActivity.this);
		adb.setMessage(message);
		adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Intent intent = new Intent(getApplicationContext(),
						LocationWindow.class);
				startActivity(intent);
			}
		});
		adb.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog alert = adb.create();
		alert.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:

			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
				int minutes) {
			// TODO Auto-generated method stub
			hour = hourOfDay;
			minute = minutes;
			Log.e("hours", hour + "");
			Log.e("minute", minute + "");
			try {
				time_format = Settings.System.getInt(getContentResolver(),
						Settings.System.TIME_12_24);
			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}
			Log.e("Time format: ", time_format + "");
			if (time_format == 12) {
				updateTime(hour, minute);
			} else {
				String minutes1 = "";
				if (minute < 10)
					minutes1 = "0" + minute;
				else
					minutes1 = String.valueOf(minute);
				aTime = new StringBuilder().append(hour).append(':')
						.append(minutes1).append(" ").toString();

				editTxtForTime.setText(aTime);
			}
		}
	};

	// private static String utilTime(int value) {
	//
	// if (value < 10)
	// return "0" + String.valueOf(value);
	// else
	// return String.valueOf(value);
	// }
// used to convert 24hr format to 12h
	// Used to convert 24hr format to 12hr format with AM/PM values
	private void updateTime(int hours, int mins) {

		String timeSet = "";
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes = "";
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		// Append in a StringBuilder
		aTime = new StringBuilder().append(hours).append(':').append(minutes)
				.append(" ").append(timeSet).toString();

		editTxtForTime.setText(aTime);
	}
	
	public void editEnable(){
		
	}
	
	public void editDisable(){
		
	}
	
	// Method to display Card  list
		private void openSpinnerView(final ArrayList<String> list) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckoutActivity.this,
					android.R.layout.simple_list_item_1, list);
			new AlertDialog.Builder(CheckoutActivity.this)
					.setAdapter(adapter, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							selectedCard = list.get(which).toString();
							Log.e("Selected Card Item", selectedCard);
							if (selectedCard.equals("VISA")) {
								cardToCheck = Constant.visa;
								cardType = "VI";							
								editTxtForCcNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
								editTxtForCcId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							} else if (selectedCard.equals("American Express")) {
								cardToCheck = Constant.amex;
								cardType = "AE";
								editTxtForCcNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
								editTxtForCcId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
							} else if (selectedCard.equals("MASTER CARD")) {
								cardToCheck = Constant.master;
								cardType = "MC";
								editTxtForCcNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
								editTxtForCcId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							} else if (selectedCard.equals("DISCOVER")) {
								cardToCheck = Constant.discover;
								cardType = "DI";
								editTxtForCcNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
								editTxtForCcId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
							}
							cardSpinner.setText(selectedCard);							
						}
					}).create().show();
		}
		
		// Method to display Months  list
		private void openSpinnerMonthView(final ArrayList<String> list) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckoutActivity.this,
					android.R.layout.simple_list_item_1, list);
			new AlertDialog.Builder(CheckoutActivity.this)
					.setAdapter(adapter, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							selected_Month = list.get(which).toString();
							Log.e("Selected Month Item", selected_Month);
							try {
								Calendar cal = Calendar.getInstance();
								cal.setTime(new SimpleDateFormat("MMM").parse(selected_Month));
								monthInt = cal.get(Calendar.MONTH) + 1;
								Log.e("monthInt ", monthInt + "");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							monthExpSpinner.setText(selected_Month);							
						}
					}).create().show();
		}
		
		// Method to display Year  list
		private void openSpinnerYearView(final ArrayList<String> list) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckoutActivity.this,
					android.R.layout.simple_list_item_1, list);
			new AlertDialog.Builder(CheckoutActivity.this)
					.setAdapter(adapter, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							selected_Year = list.get(which).toString();
							Log.e("Selected Year Item", selected_Year);							
							yearExpSpinner.setText(selected_Year);							
						}
					}).create().show();
		}

		private void deleteSignature() {
			  if (layoutForSign.getChildCount() > 0) {
			   AlertDialog.Builder builder = new AlertDialog.Builder(this);
			   builder.setTitle("Message");
			   builder.setMessage("Are you sure you want to clear your Signature?");
			   builder.setPositiveButton("OK",
			     new DialogInterface.OnClickListener() {
			      @Override
			      public void onClick(DialogInterface dialog, int which) {
			       layoutForSign.removeAllViews();
			      }
			     });
			   builder.setNeutralButton("Cancel", null);
			   AlertDialog dialog = builder.show();
			   TextView messageText = (TextView) dialog
			     .findViewById(android.R.id.message);
			   messageText.setPadding(10, 10, 10, 10);
			   messageText.setGravity(Gravity.CENTER);
			  }

			 }
}
