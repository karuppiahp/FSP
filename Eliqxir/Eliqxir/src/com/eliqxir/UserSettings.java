package com.eliqxir;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.DatePicker.OnDateChangedListener;

import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.support.ServerResponse;
import com.eliqxir.support.ServerResponse.RequestType;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.UrlGenerator;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class UserSettings extends SlidingMenuActivity implements
		OnClickListener {
	Dialog pwddialog;
	TextView textForHeader;
	EditText editTxtForFName, editTxtForLName, editTxtForEmail, edtNewPwd,
			edtConfirmPwd;
	// CheckBox chkboxPwdViaEmail;
	Button btnLogout, btnChangePwd, btnSave;
	ImageButton backImg, btnSlideMenu, cartBtn;
	SharedPreferences sharedpreferences;
	Editor editor;
	String userId, email, fName, lName, dob, dobYear, dobDay, type, password,
			confirmpwd = "";
	Button btnMonth, btnDate, btnYear;
	ArrayList<String> day = new ArrayList<String>();
	ArrayList<String> year = new ArrayList<String>();
	ArrayList<String> month = new ArrayList<String>();
	RelativeLayout layHeaderBack, layHeaderCart, layHeaderSlider;
	private int yearEnds, endYear = 2;
	String dobspilt[];
	Typeface appFont;
	int monthInt;

	// alert dialog code
	AlertDialog ad;
	Boolean running = true;
	DatePicker datePick;
	private LinkedHashMap<String, String> monthsMap = new LinkedHashMap<String, String>();
	int retainedDD = -1, retainedMM = -1, retainedYYYY = -1;
	private int currentYYYY, minimumYYYY;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.user_settings);

		loadMonths();

		Calendar cal = Calendar.getInstance();
		currentYYYY = cal.get(Calendar.YEAR);
		minimumYYYY = currentYYYY - 21;

		appFont = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
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

		cal = Calendar.getInstance();
		yearEnds = cal.get(Calendar.YEAR);

		for (int i = 1; i < endYear; i++) {
			int years = 1900 + i;
			if (years < yearEnds)
				endYear++;
			String valYear = Integer.toString(years);
			year.add(valYear);
		}

		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

		userId = sharedpreferences.getString("userId", null);
		email = sharedpreferences.getString("img_name", null);
		fName = sharedpreferences.getString("firstname", null);
		lName = sharedpreferences.getString("lastname", null);
		dob = sharedpreferences.getString("dob", null);
		Log.e("dob", "The dob is=>" + dob);
		dobspilt = dob.split("-");

		editTxtForFName = (EditText) findViewById(R.id.editTxtForFName);
		editTxtForLName = (EditText) findViewById(R.id.editTxtForLName);
		editTxtForEmail = (EditText) findViewById(R.id.editTxtForEmail);
		editTxtForEmail.setTypeface(appFont);
		editTxtForFName.setTypeface(appFont);
		editTxtForLName.setTypeface(appFont);
		editTxtForFName.setText(fName);
		editTxtForLName.setText(lName);
		editTxtForEmail.setText(email);

		btnChangePwd = (Button) findViewById(R.id.btnChangePwd);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnSave = (Button) findViewById(R.id.btnSaveChanges);
		btnSave.setOnClickListener(this);
		btnSave.setTypeface(appFont);
		btnChangePwd.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		btnMonth = (Button) findViewById(R.id.spineerForMonth);

		// btnMonth.setAdapter(new SpinnerAdapter(getBaseContext(),
		// R.layout.custom_spinner, month, "DOB"));
		btnDate = (Button) findViewById(R.id.spineerForDate);
		// btnDate.setAdapter(new SpinnerAdapter(getBaseContext(),
		// R.layout.custom_spinner, day, "DOB"));
		btnYear = (Button) findViewById(R.id.spineerForYear);

		btnMonth.setTypeface(appFont);
		btnDate.setTypeface(appFont);
		btnYear.setTypeface(appFont);
		btnDate.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnYear.setOnClickListener(this);

		btnDate.setText(dobspilt[2]);
		btnMonth.setText(dobspilt[1]);
		btnYear.setText(dobspilt[0]);

		retainedYYYY = Integer.parseInt(dobspilt[0]);
		retainedDD = Integer.parseInt(dobspilt[2]);
		retainedMM = Integer.parseInt(dobspilt[1]);

		retainedMM--;

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		backImg.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.VISIBLE);

		textForHeader.setText("SETTINGS");
		layHeaderBack = (RelativeLayout) findViewById(R.id.layHeaderBack);
		layHeaderCart = (RelativeLayout) findViewById(R.id.layHeaderCart);
		layHeaderSlider = (RelativeLayout) findViewById(R.id.layHeaderslider);
		layHeaderBack.setOnClickListener(this);
		layHeaderCart.setOnClickListener(this);
		layHeaderSlider.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layHeaderBack:
			finish();
			break;
		case R.id.btnSaveChanges:
			boolean isOnline = Utils.isOnline();
			Log.e("isOnline", isOnline + "");
			if (isOnline) {
				new UpdateUser().execute();
			} else {
				Utils.ShowAlert(UserSettings.this, Constant.networkDisconected);
			}
			break;
		case R.id.backBtn:
			finish();
			break;
		case R.id.btnChangePwd:
			showChangePwddialog();
			break;
		case R.id.btnLogout:
			Constant.cartArray.clear();

			editor = sharedpreferences.edit();
			editor.putString("userId", null);
			editor.putString("firstname", null);
			editor.putString("lastname", null);
			editor.putString("dob", null);
			editor.putString("img_name", null);
			editor.putBoolean("remember-user", false);
			editor.commit();

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					UserSettings.this);
			alertDialogBuilder.setTitle("Message");
			alertDialogBuilder
					.setMessage("Are you sure want to Logout?")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									startActivity(new Intent(UserSettings.this,
											TabsFragmentActivity.class));
									finish();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			break;

		case R.id.spineerForDate:
			type = "date";
			showDatePickerDialog();
			break;
		case R.id.spineerForMonth:
			type = "month";
			showDatePickerDialog();
			break;
		case R.id.spineerForYear:
			type = "year";
			showDatePickerDialog();
			break;
		}
	}

	private void showChangePwddialog() {

		pwddialog = new Dialog(UserSettings.this);
		pwddialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pwddialog.setContentView(R.layout.change_password);
		pwddialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		pwddialog.setCanceledOnTouchOutside(false);

		// TextView tv_loginTitle = (TextView) dialog
		// .findViewById(R.id.changePwd_title);

		edtNewPwd = (EditText) pwddialog.findViewById(R.id.newPwd);
		edtConfirmPwd = (EditText) pwddialog.findViewById(R.id.confirmPwd);
		Button closeButton = (Button) pwddialog
				.findViewById(R.id.setttings_close);
		Button btn_changePwdInDialog = (Button) pwddialog
				.findViewById(R.id.btn_changePwdInDialog);

		// chkboxPwdViaEmail = (CheckBox) dialog
		// .findViewById(R.id.checkbox_email_pwd);
		// chkboxPwdViaEmail
		// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton arg0,
		// boolean isCheck) {
		// if (isCheck) {
		//
		// if (chkboxPwdViaEmail.isChecked()) {
		// } else {
		// }
		//
		// } else {
		// }
		// }
		// });

		// if button is clicked, close the custom dialog
		closeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwddialog.dismiss();
			}
		});

		btn_changePwdInDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				password = edtNewPwd.getText().toString().trim();
				confirmpwd = edtConfirmPwd.getText().toString().trim();
				Log.e("confirmpwd", confirmpwd);
				// if (chkboxPwdViaEmail.isChecked()) {
				// new PasswordUpdate("from_email").execute();
				// } else {

				if (password.length() > 0 && confirmpwd.length() > 0) {
					// login process
					if (password.equals(confirmpwd)) {
						boolean isOnline = Utils.isOnline();
						Log.e("isOnline", isOnline + "");
						if (isOnline) {
							new PasswordUpdate().execute();
						} else {
							Utils.ShowAlert(UserSettings.this,
									Constant.networkDisconected);
						}
					} else {
						Utils.ShowAlert(UserSettings.this,
								"Password fields doesn't match");
					}
				} else {
					if (password.length() <= 0 || password.isEmpty()
							|| password == null) {
						Utils.ShowAlert(UserSettings.this, "Password is empty!");

					} else {
						Utils.ShowAlert(UserSettings.this,
								"Confirm password is empty!");

					}
				}
			}

			// }
		});
		pwddialog.show();
	}

	public class UpdateUser extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error;
		JSONObject jsonObj;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(UserSettings.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			// TODO Auto-generated method stub
			// if (from_where.equals("from_email")) {
			// List<NameValuePair> nameValuePairs = new
			// ArrayList<NameValuePair>(
			// 1);
			// nameValuePairs.add(new BasicNameValuePair("email", email));
			//
			// JSONObject jsonObj = new ServerResponse(
			// UrlGenerator.changeUserPwd()).getJSONObjectfromURL(
			// RequestType.POST, nameValuePairs);
			// Log.e("change pwd via email ", jsonObj + "");
			// } else {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("customer_id", userId));
			nameValuePairs.add(new BasicNameValuePair("firstname",
					editTxtForFName.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("lastname",
					editTxtForLName.getText().toString()));
			if (confirmpwd.length() > 0) {
				nameValuePairs.add(new BasicNameValuePair("password",
						edtConfirmPwd.getText().toString()));
				Log.e("password ", edtConfirmPwd.getText().toString());
			} else {
				String pwd = sharedpreferences.getString("pwd", "");
				nameValuePairs.add(new BasicNameValuePair("password", pwd));
				Log.e("password ", pwd);
			}
			nameValuePairs.add(new BasicNameValuePair("dob", btnMonth.getText()
					.toString().trim()
					+ "/"
					+ btnDate.getText().toString().trim()
					+ "/"
					+ btnYear.getText().toString().trim()));
			Log.e("userId", userId);
			Log.e("fname", editTxtForFName.getText().toString());
			Log.e("lname", editTxtForLName.getText().toString());
			Log.e("dob to send", btnMonth.getText().toString().trim() + "-"
					+ btnDate.getText().toString().trim() + "-"
					+ btnYear.getText().toString().trim());

			jsonObj = new ServerResponse(UrlGenerator.updateUser())
					.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
			Log.e("change pwd  ", jsonObj + "");
			if (jsonObj != null) {
				try {
					status = jsonObj.getString("status");
					if (status.equals("1")) {
						editor = sharedpreferences.edit();

						editor.putString("firstname", editTxtForFName.getText()
								.toString().trim());
						editor.putString("lastname", editTxtForLName.getText()
								.toString().trim());
						editor.putString("dob", btnMonth.getText().toString()
								.trim()
								+ "-"
								+ btnDate.getText().toString().trim()
								+ "-" + btnYear.getText().toString().trim());
						editor.commit();
					} else {
						error = jsonObj.getString("Error");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// }
			return null;

		}

		@Override
		public void onPostExecute(Boolean result) {
			dialog.dismiss();
			if (status.equals("1")) {
				Utils.ShowAlert(UserSettings.this, "Customer updated!");
			} else {
				Utils.ShowAlert(UserSettings.this, error);
			}
		}
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
		datePick.init(minimumYYYY, now.get(Calendar.MONTH),
				now.get(Calendar.DAY_OF_MONTH), null);

		if (retainedDD == -1) {
			retainedYYYY = minimumYYYY;
			retainedMM = now.get(Calendar.MONTH);
			retainedDD = now.get(Calendar.DAY_OF_MONTH);
		} else {

			datePick.init(retainedYYYY, retainedMM, retainedDD, null);
		}

		datePick.init(retainedYYYY, retainedMM, retainedDD,
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						Log.e("changed", "chenged");
						Calendar cal;
						// ad.setTitle("Arjun");
						if (running) {
							running = false;
							if (year > minimumYYYY) {
								datePick.updateDate(minimumYYYY, monthOfYear,
										dayOfMonth);
								cal = Calendar.getInstance();
								cal.set(minimumYYYY, monthOfYear, dayOfMonth);

							} else {
								cal = Calendar.getInstance();
								cal.set(year, monthOfYear, dayOfMonth);

							}

							SimpleDateFormat dayFormat = new SimpleDateFormat(
									"EEEE", Locale.US);
							String tempDay = dayFormat.format(cal.getTime());
							String day = tempDay.substring(0, 3);
							Log.e("The", "day of the month=>" + day);
							// Log.e("The","date is=>"+monthOfYear+"/"+dayOfMonth+"/"+year);
							String monthName = "";
							monthName = day + ",";
							monthName = monthName + " "
									+ monthsMap.get("" + monthOfYear);
							monthName = monthName + " " + dayOfMonth + " "
									+ year;
							Log.e("The", "details are=>" + monthName);
							ad.setTitle(monthName);
							running = true;
						}
					}
				});

		builder.setView(view);

		ad = builder.create();
		String tempDate = setCurrentDate();
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
						int day = datePick.getDayOfMonth();
						int month = datePick.getMonth();
						int year = datePick.getYear();

						btnDate.setText(day + "");
						btnMonth.setText(month + 1 + "");
						btnYear.setText(year + "");

						retainedDD = day;
						retainedMM = month;
						retainedYYYY = year;
					}
				});
		ad.show();

	}

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			// Use the current date as the default date in the picker
			// final Calendar c = Calendar.getInstance();
			// int year = c.get(Calendar.YEAR);
			// int month = c.get(Calendar.MONTH);
			// int day = c.get(Calendar.DAY_OF_MONTH);
			//
			final Calendar c = Calendar.getInstance();
			// setting jun-20-2011
			c.set(Integer.parseInt(btnYear.getText().toString()),
					Integer.parseInt(btnMonth.getText().toString()) - 1,
					Integer.parseInt(btnDate.getText().toString()));
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
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

	public class PasswordUpdate extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog dialog;
		String status = "", error;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			this.dialog = new ProgressDialog(UserSettings.this);
			this.dialog.setMessage("Loading..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// if (from_where.equals("from_email")) {
			// List<NameValuePair> nameValuePairs = new
			// ArrayList<NameValuePair>(
			// 1);
			// nameValuePairs.add(new BasicNameValuePair("email", email));
			//
			// JSONObject jsonObj = new ServerResponse(
			// UrlGenerator.changeUserPwd()).getJSONObjectfromURL(
			// RequestType.POST, nameValuePairs);
			// Log.e("change pwd via email ", jsonObj + "");
			// } else {
			Log.e("customer_id", userId);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("customer_id", userId));

			nameValuePairs.add(new BasicNameValuePair("password", edtConfirmPwd
					.getText().toString()));

			JSONObject jsonObj = new ServerResponse(
					UrlGenerator.changeUserPwd()).getJSONObjectfromURL(
					RequestType.POST, nameValuePairs);
			Log.e("change pwd  ", jsonObj + "");
			if (jsonObj != null) {
				try {
					status = jsonObj.getString("status");
					if (status.equals("1")) {
						editor = sharedpreferences.edit();
						editor.putString("userId", userId);
						editor.putString("firstname", fName);
						editor.putString("lastname", lName);
						editor.putString("dob", dob);
						editor.putString("img_name", email);

						editor.putString("pwd", edtConfirmPwd.getText()
								.toString().trim());

						editor.commit();
					} else {
						error = jsonObj.getString("Error");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// }
			return null;

		}

		@Override
		public void onPostExecute(Boolean result) {
			dialog.dismiss();

			if (status.equals("1")) {
				Utils.ShowAlert(UserSettings.this,
						"Your password has been updated!");
				pwddialog.dismiss();
			} else {
				Utils.ShowAlert(UserSettings.this, error);
			}
		}
	}

	public String setCurrentDate() {

		Calendar now = Calendar.getInstance();
		now.set(retainedYYYY, retainedMM, retainedDD);
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		String tempDay = dayFormat.format(now.getTime());
		String day = tempDay.substring(0, 3);
		Log.e("The", "day of the month=>" + day);
		// Log.e("The","date is=>"+monthOfYear+"/"+dayOfMonth+"/"+year);
		String monthName = "";
		monthName = day + ",";
		monthName = monthName + " "
				+ monthsMap.get("" + now.get(Calendar.MONTH));
		monthName = monthName + " " + now.get(Calendar.DAY_OF_MONTH) + " "
				+ retainedYYYY;
		return monthName;
	}

	private void loadMonths() {
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
