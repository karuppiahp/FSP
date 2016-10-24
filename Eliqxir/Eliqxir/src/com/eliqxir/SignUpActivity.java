package com.eliqxir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.AddDriver;

public class SignUpActivity extends SlidingMenuActivity implements
		OnClickListener {

	EditText editTxtForMailId, editTxtForFirstName, editTxtForLastName,
			editTxtForPswd, editTxtForConfirmPwd;
	Button btnForRegister, btnForCancel;
	ImageButton btnSliderMenu, backBtn, cartBtn;
	TextView textForHeader, textForTermsChkBox;
	String productId, quantities, optionId, valueId, first_Name, last_Name;
	SharedPreferences sharedpreferences;
	CheckBox chkBoxForTerms;
	Button btnDate, btnMonth, btnYear;
	Editor editor;
	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	boolean isTermsChecked;
	private static final int DATE_DIALOG_ID = 0;

	// int yearEnds, endYear = 2, monthInt;
	// String dobMonth, dobYear, dobDay = "0";
	String total, type;
	Typeface appFont;
	private int currentYYYY, minimumYYYY;
	
	AlertDialog ad ;
	Boolean running=true;
	DatePicker datePick;
	private LinkedHashMap<String,String> monthsMap=new LinkedHashMap<String, String>();
	int retainedDD=-1,retainedMM=-1,retainedYYYY=-1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.signup);
		loadMonths();

		Calendar cal = Calendar.getInstance();
		currentYYYY = cal.get(Calendar.YEAR);
		minimumYYYY = currentYYYY - 21;

		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

		Log.e("The", "current Year=>" + currentYYYY);
		Log.e("The", "minimum Year=>" + minimumYYYY);
		Log.e("The", "day of the month=>" + dayFormat.format(cal.getTime()));

		appFont = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();
		
			productId = getIntent().getExtras().getString("prdtId");
			quantities = getIntent().getExtras().getString("quantity");
			optionId = getIntent().getExtras().getString("optionId");
			valueId = getIntent().getExtras().getString("valueId");
			total = getIntent().getExtras().getString("tot_price");
	
		btnSliderMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		chkBoxForTerms = (CheckBox) findViewById(R.id.chkBoxForTerms);
		textForTermsChkBox = (TextView) findViewById(R.id.textForTermsChkBox);
		String text = SignUpActivity.this.getString(R.string.signup_terms);
		textForTermsChkBox.setText(Html.fromHtml(text));
		textForTermsChkBox.setOnClickListener(this);
		btnSliderMenu.setVisibility(View.GONE);
		backBtn.setVisibility(View.VISIBLE);
		cartBtn.setVisibility(View.GONE);
		textForHeader.setText("SIGNUP");
		//
		// SlidingMenu sm = getSlidingMenu();
		// sm.setMode(SlidingMenu.LEFT);
		// getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		editTxtForMailId = (EditText) findViewById(R.id.editTxtForEmail);
		editTxtForFirstName = (EditText) findViewById(R.id.editTxtForFirstName);
		editTxtForLastName = (EditText) findViewById(R.id.editTxtForLastName);
		editTxtForPswd = (EditText) findViewById(R.id.editTxtForPwd);
		editTxtForConfirmPwd = (EditText) findViewById(R.id.editTxtForConfirmPwd);
		// editTxtForDOB = (EditText) findViewById(R.id.editTxtForDOB);
		btnForRegister = (Button) findViewById(R.id.btnForRegister);
		btnForCancel = (Button) findViewById(R.id.btnForCancel);
		btnForCancel.setOnClickListener(this);
		btnForRegister.setOnClickListener(this);
		// editTxtForDOB.setOnClickListener(this);
		// editTxtForDOB.setFocusable(false);
		backBtn.setOnClickListener(this);

		btnDate = (Button) findViewById(R.id.spinnerForDobDate);
		btnMonth = (Button) findViewById(R.id.spinnerForDobMonth);
		btnYear = (Button) findViewById(R.id.spinnerForDobYear);
		btnMonth.setTypeface(appFont);
		btnDate.setTypeface(appFont);
		btnYear.setTypeface(appFont);
		btnDate.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnYear.setOnClickListener(this);

		editTxtForMailId
				.setFilters(new InputFilter[] { Constant.filter_email });
		// editTxtForPswd.setFilters(new
		// InputFilter[]{Constant.filter_password});
		// editTxtForConfirmPwd.setFilters(new
		// InputFilter[]{Constant.filter_password});
		editTxtForFirstName
				.setFilters(new InputFilter[] { Constant.filter_fname });
		editTxtForLastName
				.setFilters(new InputFilter[] { Constant.filter_fname });

		chkBoxForTerms
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub

						if (isChecked) {

							if (chkBoxForTerms.isChecked()) {
								isTermsChecked = true;
							} else {
								isTermsChecked = false;
							}
						} else {
							isTermsChecked = false;
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnForRegister:
			String mailId = editTxtForMailId.getText().toString();
			String firstName = editTxtForFirstName.getText().toString();
			String lastName = editTxtForLastName.getText().toString();
			String pwd = editTxtForPswd.getText().toString();
			try {
				first_Name = firstName.substring(firstName.length() - 1);
				last_Name = lastName.substring(lastName.length() - 1);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			/*
			 * editTxtForPswd.setFilters(new
			 * InputFilter[]{Constant.filter_password});
			 * editTxtForFirstName.setFilters(new
			 * InputFilter[]{Constant.filter_name});
			 * editTxtForLastName.setFilters(new
			 * InputFilter[]{Constant.filter_name});
			 */

			if (mailId.length() > 0 && firstName.length() > 0
					&& lastName.length() > 0 && pwd.length() > 0) {
				if (Utils.isEmailValid(mailId)) {

					if (pwd.length() >= 6) {
						if (editTxtForPswd
								.getText()
								.toString()
								.trim()
								.equals(editTxtForConfirmPwd.getText()
										.toString().trim())) {
							Log.e("year length", btnYear.getText().toString()
									.trim().length()
									+ "");
							if (btnYear.getText().toString().trim().length() > 0) {

								try {

									int age = getAge(
											Integer.parseInt(btnYear.getText()
													.toString().trim()),
											Integer.parseInt(btnMonth.getText()
													.toString().trim()),
											Integer.parseInt(btnDate.getText()
													.toString().trim()));
									if (age >= 21) {

										if (isTermsChecked) {
											boolean isOnline = Utils.isOnline();
											Log.e("isOnline", isOnline + "");
											if (isOnline) {
												if (first_Name.equals(".")
														|| first_Name
																.equals("_")
														|| last_Name
																.equals(".")
														|| last_Name
																.equals("_")) {
													Utils.ShowAlert(
															SignUpActivity.this,
															"Special Characters are not allowed at the end of the string.");
												} else if (firstName
														.contains("..")
														|| lastName
																.contains("..")
														|| firstName
																.contains("__")
														|| lastName
																.contains("__")) {
													Utils.ShowAlert(
															SignUpActivity.this,
															"Please enter a valid name.");
												} else if (firstName
														.startsWith(".")
														|| lastName
																.startsWith(".")
														|| firstName
																.startsWith("_")
														|| lastName
																.startsWith("_")) {
													Utils.ShowAlert(
															SignUpActivity.this,
															"Special Characters are not allowed at the beginning of the string.");
												} else {
													new CreateAccount(mailId,
															firstName,
															lastName, pwd)
															.execute();
												}
												/*
												 * if(isTermsChecked) { new
												 * CreateAccount(mailId,
												 * firstName, lastName, pwd)
												 * .execute(); } else {
												 * Utils.ShowAlert
												 * (SignUpActivity.this ,
												 * "Please check the terms & conditions."
												 * ); }
												 */
											} else {
												Utils.ShowAlert(
														SignUpActivity.this,
														Constant.networkDisconected);
											}
										} else {
											Utils.ShowAlert(
													SignUpActivity.this,
													"Please check the terms & conditions.");
										}
									} else {
										Utils.ShowAlert(SignUpActivity.this,
												"You must be 21 or older to order.");
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								Utils.ShowAlert(SignUpActivity.this,
										"Please choose Date of Birth.");
							}

						} else {
							Utils.ShowAlert(SignUpActivity.this,
									"Password doesn't match.");
						}

					} else {
						Utils.ShowAlert(SignUpActivity.this,
								"Password must contain 6 or more than 6 characters");
					}
				} else {
					Utils.ShowAlert(SignUpActivity.this, "EmailID is Invalid");
				}
			} else {
				Utils.ShowAlert(SignUpActivity.this,
						"All fields are mandatory.");
			}
			break;

		case R.id.btnForCancel:
			finish();
			break;
		case R.id.textForTermsChkBox:
			Log.e("Terms and Conditions", "Clicked");
			startActivity(new Intent(SignUpActivity.this, Terms.class));

			// showLogindialog();
			break;
		/*
		 * case R.id.editTxtForDOB: showDialog(DATE_DIALOG_ID); break;
		 */
		case R.id.backBtn:
			finish();
			break;

		case R.id.spinnerForDobDate:
			type = "date";
			showDatePickerDialog();
			break;
		case R.id.spinnerForDobMonth:
			type = "month";
			showDatePickerDialog();
			break;
		case R.id.spinnerForDobYear:
			type = "year";
			showDatePickerDialog();
			break;

		}
	}

	public int getAge(int _year, int _month, int _day) {

		GregorianCalendar cal = new GregorianCalendar();
		int y, m, d, a;

		y = cal.get(Calendar.YEAR);
		m = cal.get(Calendar.MONTH) + 1;
		d = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(_year, _month, _day);
		a = y - cal.get(Calendar.YEAR);
		if ((m < cal.get(Calendar.MONTH))
				|| ((m == cal.get(Calendar.MONTH)) && (d < cal
						.get(Calendar.DAY_OF_MONTH)))) {
			--a;
		}

		return a;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		int x = cyear - 21;
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, x, cmonth, cday);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			String date_selected = String.valueOf(monthOfYear + 1) + "/"
					+ String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
			try {
				Date date1 = df.parse(date_selected);
				@SuppressWarnings("unused")
				String actualDate = df.format(date1);

				// editTxtForDOB.setText(actualDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	public class CreateAccount extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String email, first, last, password, status = "", error, userId;

		public CreateAccount(String mailId, String firstName, String lastName,
				String pwd) {
			// TODO Auto-generated constructor stub
			email = mailId;
			first = firstName;
			last = lastName;
			password = pwd;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(SignUpActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// int monthInt = 0;
			// // TODO Auto-generated method stub
			// try {
			// Calendar cal = Calendar.getInstance();
			// cal.setTime(new SimpleDateFormat("MMM").parse(btnMonth
			// .getText().toString().trim()));
			// monthInt = cal.get(Calendar.MONTH) + 1;
			// Log.e("monthInt ", monthInt + "");
			// } catch (ParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("firstname", first));
			nameValuePairs.add(new BasicNameValuePair("lastname", last));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("dob", btnMonth.getText()
					.toString().trim()
					+ "/"
					+ btnDate.getText().toString().trim()
					+ "/"
					+ btnYear.getText().toString().trim()));
			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.createAccount()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("signup response", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					}

					// else {
					// userId = jsonObj.getString("userid");
					// }
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			try {
				if (status.equals("1")) {
					boolean isOnline = Utils.isOnline();
					Log.e("isOnline", isOnline + "");
					if (isOnline) {
						new LoginAccount(email, password).execute();
					} else {
						Utils.ShowAlert(SignUpActivity.this,
								Constant.networkDisconected);
					}

				} else {
					Utils.ShowAlert(SignUpActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class LoginAccount extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String email, password, status = "", error, userId, fName, lName, dob;

		public LoginAccount(String mailId, String pwd) {
			// TODO Auto-generated constructor stub
			email = mailId;
			password = pwd;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(SignUpActivity.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			JSONObject jsonObj = new ServerResponse(UrlGenerator.loginAccount())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("account creation", jsonObj + "");
			try {
				if (jsonObj != null) {
					status = jsonObj.getString("status");
					if (status.equals("0")) {
						error = jsonObj.getString("Error");
					} else {

						userId = jsonObj.getString("userid");
						fName = jsonObj.getString("firstname");
						lName = jsonObj.getString("lastname");
						dob = jsonObj.getString("dob");

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			try {
				if (status.equals("1")) {
					editor = sharedpreferences.edit();
					editor.putString("userId", userId);

					editor.putString("img_name", email);
					editor.putString("userId", userId);

					editor.putString("img_name", email);
					editor.putString("firstname", fName);
					editor.putString("lastname", lName);
					editor.putString("pwd", editTxtForPswd.getText().toString());
					editor.putString("dob", dob);
					editor.commit();
					Log.i("User ID Is::::::", "" + userId);

					String totalQnty = null, productId = null, optionIdComma = null, valueIdComma = null;
					for (int i = 0; i < Constant.cartArray.size(); i++) {
						String cartQty = Constant.cartArray.get(i).get("qty");
						totalQnty = totalQnty + "," + cartQty;
						Log.i("Total qty ois>>>>>>>", "" + totalQnty);
						String cartId = Constant.cartArray.get(i).get("itemId");
						productId = productId + "," + cartId;
						String optionId = Constant.cartArray.get(i).get(
								"optionId");
						String valueId = Constant.cartArray.get(i)
								.get("sizeId");
						optionIdComma = optionIdComma + "," + optionId;
						valueIdComma = valueIdComma + "," + valueId;
					}
					finish();
					Intent checkoutIntent = new Intent(SignUpActivity.this,
							CheckoutActivity.class);
					checkoutIntent.putExtra("prdtId", productId);
					checkoutIntent.putExtra("optionId", optionIdComma);
					checkoutIntent.putExtra("valueId", valueIdComma);
					checkoutIntent.putExtra("quantity", totalQnty);
					checkoutIntent.putExtra("img_name", email);
					checkoutIntent.putExtra("tot_price", total);
					checkoutIntent.putExtra("first_name", fName);
					checkoutIntent.putExtra("last_name", lName);
					checkoutIntent.putExtra("dob", dob);

					startActivity(checkoutIntent);

				} else {
					Utils.ShowAlert(SignUpActivity.this, error);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showLogindialog() {

		final Dialog dialog = new Dialog(SignUpActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.terms_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		Button closeButton = (Button) dialog.findViewById(R.id.terms_close);

		// if button is clicked, close the custom dialog
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void showDatePickerDialog1() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setCancelable(false);
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public void showDatePickerDialog() {
		
		
		
		// DialogFragment newFragment = new DatePickerFragment();
		// newFragment.setCancelable(false);
		// newFragment.show(getSupportFragmentManager(), "datePicker");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
	LayoutInflater inflater = getLayoutInflater();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog
		// layout
	

		View view = inflater.inflate(R.layout.date_picker, null, false);

		 datePick = (DatePicker) view.findViewById(R.id.pickerdate);
		 Calendar now = Calendar.getInstance();
			datePick.init(minimumYYYY,
					now.get(Calendar.MONTH),  now.get(Calendar.DAY_OF_MONTH),null);
			
			if(retainedDD==-1)
			{
				retainedYYYY=minimumYYYY;
				retainedMM= now.get(Calendar.MONTH);
				retainedDD=now.get(Calendar.DAY_OF_MONTH);
			}
			else
			{
			
			datePick.init(retainedYYYY,
					retainedMM, retainedDD,null);
			}


	
		datePick.init(retainedYYYY, retainedMM,
				retainedDD, new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Log.e("changed", "chenged");
						Calendar cal;
//						ad.setTitle("Arjun");
						if(running)
						{
							running=false;
							if(year>minimumYYYY)
							{
								datePick.updateDate(minimumYYYY,
										monthOfYear, dayOfMonth);
								cal = Calendar.getInstance();
								cal.set(minimumYYYY, monthOfYear, dayOfMonth);

							}
							else
							{
							 cal = Calendar.getInstance();
								cal.set(year, monthOfYear, dayOfMonth);

							}
							

							SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
							String tempDay=dayFormat.format(cal.getTime());
							String day=tempDay.substring(0,3);
							Log.e("The", "day of the month=>" + day);
//							Log.e("The","date is=>"+monthOfYear+"/"+dayOfMonth+"/"+year);
							String monthName="";
							monthName=day+",";
							 monthName=monthName+" "+monthsMap.get(""+monthOfYear);
							monthName=monthName+" "+dayOfMonth+" "+year;
							Log.e("The","details are=>"+monthName);
							ad.setTitle(monthName);
							running=true;
						}
					}
				});
		
		builder.setView(view);

	 ad = builder.create();
	 String tempDate=setCurrentDate();
		ad.setTitle(tempDate);
		ad.setCancelable(false);
		ad.setCanceledOnTouchOutside(false);

		ad.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		ad.setButton(AlertDialog.BUTTON_POSITIVE, "Set",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int day=datePick.getDayOfMonth();
						int month=datePick.getMonth();
						int year=datePick.getYear();

						btnDate.setText(day + "");
						btnMonth.setText(month + 1 + "");
						btnYear.setText(year + "");
						
						retainedDD=day;
						retainedMM=month;
						retainedYYYY=year;
					}
				});
		ad.show();
		
		

	}

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year - 21, month,
					day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			Log.e("selected date", month + 1 + ":" + day + ":" + year);
			// if (type.equals("date")) {
			// btnDate.setText(day + "");
			// } else if (type.equals("month")) {
			// btnMonth.setText(month + 1 + "");
			//
			// } else {
			// btnYear.setText(year + "");
			// }
			btnDate.setText(day + "");
			btnMonth.setText(month + 1 + "");
			btnYear.setText(year + "");

		}
	}
	public String setCurrentDate()
	{
	
		 Calendar now = Calendar.getInstance();
			now.set(retainedYYYY,
					retainedMM,  retainedDD);
			SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
			String tempDay=dayFormat.format(now.getTime());
			String day=tempDay.substring(0,3);
			Log.e("The", "day of the month=>" + day);
//			Log.e("The","date is=>"+monthOfYear+"/"+dayOfMonth+"/"+year);
			String monthName="";
			monthName=day+",";
			 monthName=monthName+" "+monthsMap.get(""+now.get(Calendar.MONTH));
			monthName=monthName+" "+now.get(Calendar.DAY_OF_MONTH)+" "+retainedYYYY;
		return monthName;
	}
	private void loadMonths()
	{
		monthsMap.put("0", "Jan");
		monthsMap.put("1", "Feb");
		monthsMap.put("2", "Mar");
		monthsMap.put("3", "Apr");
		monthsMap.put("4", "May");
		monthsMap.put("5", "Jun");
		monthsMap.put("6", "Jul");
		monthsMap.put("7", "Aug");
		monthsMap.put("8", "Sep");
		monthsMap.put("9", "Oct");
		monthsMap.put("10", "Nov");
		monthsMap.put("11", "Dec");
	}
}
