package com.fsp.blacksheep;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setting1 extends FragmentActivity {
	private static final int DATE_DIALOG_ID = 999;
	public static final String PREFS_NAME = "MyPrefsFile";
	static SharedPreferences settings;
	SharedPreferences.Editor editor;
	String response = null, device_id = null,
			spinner_selected_school_name = "", spinner_selected_school_id,
			part1, part2, part3, Email_Value, stored_email_value,
			stored_birthday_value, campus_tagid, campus_school, school_name;
	int count = 0;
	// Static String used in BS_Main Page //
	public static String myschoolid = "";
	String email_id, bday, dob;

	int spinner_selection_position, cyear, cmonth, cday;
	// UI Varaibles
	EditText sname, tagid, e_mail_editText, br_date_editText;
	Button submit_button;
	TextView school_TextView, settings_HeaderText, text_Save;
	String[] schoolid_Arr;
	// Ends
	DatePicker datePicker;
	List<String> school_name_List = new ArrayList<String>();
	List<String> schooltagid_List = new ArrayList<String>();

	// Parsing Variables

	URL url_val = null;
	DocumentBuilderFactory DBF;
	DocumentBuilder DB;
	Document dom;
	Element elt;
	public static int check = 0;
	// End

	TextView t_school, t_email, t_dob;

	View lastView;
	String s_name, temp_SchoolValue;
	SharedPreferences pushPrefs;
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;
	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;

	private Calendar cal;
	private int day, month, year;

	private boolean textFieldClicked = false, dobClicked = false,
			spinnerClicked = false;

	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(Setting1.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("Settings page Started");

		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);

		// tracker = GoogleAnalyticsTracker.getInstance();
		//
		// // Start the tracker in manual dispatch mode...
		// // tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1", this);
		// tracker.trackPageView("/Rss_Feed_Grid");
		/*
		 * Context mCtx = this; // Get current context. myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * myInstance.setDebug(true); myNewTracker =
		 * myInstance.getTracker("UA-36318648-1");
		 * myInstance.setDefaultTracker(myNewTracker);
		 * myNewTracker.trackView("Settings page Started");
		 */

	}

	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(Setting1.this);
		EasyTracker.getInstance().activityStop(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_settings1);

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		settings = getSharedPreferences(PREFS_NAME, 0);
		email_id = settings.getString("email", "");
		Log.e("email ###############", email_id);
		bday = settings.getString("birthday", "");
		Log.e("bday **********", bday);
		spinner_selected_school_name = settings.getString("school", "");
		spinner_selected_school_id = settings.getString("tagId", "");
		s_name = settings.getString("school", "");
		Log.e("School name from sharpref >>", spinner_selected_school_name);
		Log.e("School id from sharpref >>", spinner_selected_school_id);
		editor = settings.edit();
		e_mail_editText = (EditText) findViewById(R.id.email_value);
		br_date_editText = (EditText) findViewById(R.id.dob);
		school_TextView = (TextView) findViewById(R.id.school_value);
		settings_HeaderText = (TextView) findViewById(R.id.settingsheadertext);
		text_Save = (TextView) findViewById(R.id.regsubmit);
		t_school = (TextView) findViewById(R.id.txt_school);

		Typeface type1 = Typeface.createFromAsset(getAssets(),
				"Mission Gothic Regular.otf");
		e_mail_editText.setTypeface(type1);
		br_date_editText.setTypeface(type1);
		school_TextView.setTypeface(type1);
		t_school.setTypeface(type1);
		settings_HeaderText.setTypeface(type1);
		text_Save.setTypeface(type1);

		text_Save.setTextColor(Color.parseColor("#73FFFFFF"));

		e_mail_editText.setText(email_id);
		br_date_editText.setText(bday);
		school_TextView.setText(spinner_selected_school_name);
		TelephonyManager telemngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		device_id = telemngr.getDeviceId();
		Log.v("deviceid", "" + device_id);

		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH); // Current day
		month = cal.get(Calendar.MONTH);// current month
		year = cal.get(Calendar.YEAR); // current year

		school_name_List.clear();
		e_mail_editText.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				textFieldClicked = true;
				text_Save.setTextColor(Color.parseColor("#FFFFFF"));
				return false;
			}
		});

		if (haveInternet(Setting1.this) == true) {
		} else {
			Toast.makeText(Setting1.this,
					"Please Check your Intenet Connection", Toast.LENGTH_SHORT)
					.show();
		}

		school_TextView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (school_name_List.size() > 0) {
					openSpinner(school_name_List);
				} else {
					Toast.makeText(
							Setting1.this,
							"Schools list is not able to fetch, please check your network availability",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		br_date_editText.setOnTouchListener(new OnTouchListener() {
			@SuppressWarnings("deprecation")
			public boolean onTouch(View arg0, MotionEvent arg1) {
				Log.e("Inside Date Dialog", "click");
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});

		text_Save.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// count=1;
				Email_Value = String.valueOf(e_mail_editText.getText());
				if (spinner_selected_school_name != null
						&& !spinner_selected_school_name.isEmpty()
						&& e_mail_editText.getText().toString().trim()
								.equals("")
						&& br_date_editText.getText().toString().trim()
								.equals("")) {
					if (haveInternet(Setting1.this) == true) {
						if (spinnerClicked == true) {
							spinnerClicked = false;
							// text_Save.setVisibility(View.GONE);
							text_Save.setTextColor(Color
									.parseColor("#73FFFFFF"));
							new Task_Update().execute();
						}
					} else {
						Toast.makeText(Setting1.this,
								"Please Check your Intenet Connection",
								Toast.LENGTH_SHORT).show();
					}
				} else if (spinner_selected_school_name != null
						&& !spinner_selected_school_name.isEmpty()
						&& ((e_mail_editText.getText().toString().trim() != null && !e_mail_editText
								.getText().toString().trim().isEmpty()) && br_date_editText
								.getText().toString().trim().equals(""))) {
					Boolean emailvalue = true;

					// checking email
					if (Email_Value.length() > 0) {
						emailvalue = checkEmailValidation(Email_Value);
					}
					if (!emailvalue) {
						Toast.makeText(getApplicationContext(),
								"Invalid Email Id Format", Toast.LENGTH_LONG)
								.show();
					} else {
						if (haveInternet(Setting1.this) == true) {
							if (spinnerClicked == true
									|| textFieldClicked == true) {
								spinnerClicked = false;
								textFieldClicked = false;
								// text_Save.setVisibility(View.GONE);
								text_Save.setTextColor(Color
										.parseColor("#73FFFFFF"));
								new Task_Update().execute();
							}
						} else {
							Toast.makeText(Setting1.this,
									"Please Check your Intenet Connection",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else if (spinner_selected_school_name != null
						&& !spinner_selected_school_name.isEmpty()
						&& (e_mail_editText.getText().toString().trim()
								.equals(""))
						&& (br_date_editText.getText().toString().trim() != null && !br_date_editText
								.getText().toString().trim().isEmpty())) {

					// //
					try {
						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("M-d-yyyy");
						String currentDate = df.format(c.getTime());

						SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
						Date date1 = sdf.parse(br_date_editText.getText()
								.toString().trim());
						Date date2 = sdf.parse(currentDate);

						System.out.println(sdf.format(date1));
						System.out.println(sdf.format(date2));
						Log.e("Date 1", "" + sdf.format(date1));
						Log.e("Date 2", "" + sdf.format(date2));

						if (date1.compareTo(date2) < 0) {
							System.out.println("Date1 is before current date");
							String[] split = br_date_editText.getText()
									.toString().trim().split("-");
							int age = getAge(Integer.parseInt(split[2]),
									Integer.parseInt(split[1]),
									Integer.parseInt(split[0]));
							if (age >= 18) {
								if (haveInternet(Setting1.this) == true) {
									if (spinnerClicked == true
											|| dobClicked == true) {
										spinnerClicked = false;
										dobClicked = false;
										text_Save.setTextColor(Color
												.parseColor("#73FFFFFF"));
										new Task_Update().execute();
									}
								} else {
									Toast.makeText(
											Setting1.this,
											"Please Check your Intenet Connection",
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"Must be 18 or older to enjoy.",
										Toast.LENGTH_LONG).show();
							}
						} else if (date1.compareTo(date2) == 0) {
							System.out
									.println("Date1 is equal to current date");
							Toast.makeText(getApplicationContext(),
									"Must be 18 or older to enjoy.",
									Toast.LENGTH_LONG).show();
						} else {

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else {
					try {
						Log.e("Inside Try", "Statement");
						Boolean value = true;
						if (Email_Value.length() > 0) {
							Log.e("Inside email", "Validation method");
							value = checkEmailValidation(Email_Value);
						}

						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("M-d-yyyy");
						String currentDate = df.format(c.getTime());

						SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
						Date date1 = sdf.parse(br_date_editText.getText()
								.toString().trim());
						Date date2 = sdf.parse(currentDate);

						System.out.println(sdf.format(date1));
						System.out.println(sdf.format(date2));
						Log.e("Date 1", "" + sdf.format(date1));
						Log.e("Date 2", "" + sdf.format(date2));

						if (!value) {
							Toast.makeText(getApplicationContext(),
									"Invalid Email Id Format",
									Toast.LENGTH_LONG).show();
						} else if (date1.compareTo(date2) < 0) {
							System.out.println("Date1 is before current date");
							String[] split = br_date_editText.getText()
									.toString().trim().split("-");
							int age = getAge(Integer.parseInt(split[2]),
									Integer.parseInt(split[1]),
									Integer.parseInt(split[0]));
							Log.v("age >>", age + "");
							if (age >= 18) {
								if (device_id.equalsIgnoreCase("")
										|| device_id.equalsIgnoreCase(null)) {
									Toast.makeText(
											Setting1.this,
											"Device ID is Not set. Try Again Later",
											Toast.LENGTH_SHORT).show();
								} else if (spinner_selected_school_name
										.equalsIgnoreCase("")
										|| spinner_selected_school_name
												.equalsIgnoreCase(null)) {
									Log.v("school not selected >>",
											"School Name Should not be Empty");
									Toast.makeText(
											Setting1.this,
											"School Name Should not be Empty. Please select one.",
											Toast.LENGTH_SHORT).show();
								}

								else if (spinner_selected_school_id
										.equalsIgnoreCase("")
										|| spinner_selected_school_id
												.equalsIgnoreCase(null)) {
									Toast.makeText(Setting1.this,
											"School Id Should not be Empty",
											Toast.LENGTH_SHORT).show();
								}

								else {
									if (haveInternet(Setting1.this) == true) {
										if (spinnerClicked == true
												|| dobClicked == true
												|| textFieldClicked == true) {
											spinnerClicked = false;
											dobClicked = false;
											textFieldClicked = false;
											// text_Save.setVisibility(View.GONE);
											text_Save.setTextColor(Color
													.parseColor("#73FFFFFF"));
											new Task_Update().execute();
										}
									} else {
										Toast.makeText(
												Setting1.this,
												"Please Check your Intenet Connection",
												Toast.LENGTH_SHORT).show();
									}
								}
								// }
							} else {
								Toast.makeText(getApplicationContext(),
										"Must be 18 or older to enjoy.",
										Toast.LENGTH_LONG).show();
							}
						} else if (date1.compareTo(date2) == 0) {
							System.out
									.println("Date1 is equal to current date");
							Toast.makeText(getApplicationContext(),
									"Must be 18 or older to enjoy.",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Must be 18 or older to enjoy.",
									Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} // end of main else
			}

		});
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

	public void showAlert(String msg) {
		final AlertDialog alertDialog = new AlertDialog.Builder(Setting1.this)
				.create();
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	public void checkEmail(String email_value) {
		Validation val = new Validation();
		boolean emailValid = false;
		emailValid = val.validateEmail(email_value.trim());
		if (emailValid) {
			if (haveInternet(Setting1.this) == true) {
				new Task_Update().execute();
			} else {
				Toast.makeText(Setting1.this,
						"Please Check your Intenet Connection",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			showAlert("Invalid Email Id Format");
		}
	}

	public Boolean checkEmailValidation(String email_value) {
		Validation val = new Validation();
		boolean emailValid = false;
		emailValid = val.validateEmail(email_value.trim());
		if (emailValid) {
			return true;
		} else {
			return false;
		}
	}

	public class Task_Update extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(Setting1.this);
		String res = "";

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000); // Timeout
																		// Limit

			HttpResponse response;
			HttpPost post = null;

			JSONObject json = new JSONObject();
			try {
				// changed
				// post = new HttpPost(
				// "http://104.130.66.20/mobile_new/registration_edit.php?");

				post = new HttpPost(ParserClass.settingSubOne);

				json.put("u_id", device_id);
				json.put("firstName", "");
				json.put("lastName", "");

				json.put("school", spinner_selected_school_name);
				json.put("tagId", spinner_selected_school_id);
				json.put("email", e_mail_editText.getText().toString());
				json.put("birthday", br_date_editText.getText().toString());

				post.setHeader("Content-Type", "application/json");
				post.setHeader("Accept", "application/json");
				StringEntity se = new StringEntity(json.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				Log.e("Response >>>", response + "");
				String ret = EntityUtils.toString(response.getEntity());
				Log.e("My calendar response", ret);
				JSONObject res_obj = new JSONObject(ret);
				String res1 = res_obj.getString("blacksheep");
				JSONArray res_arr = new JSONArray(res1);
				for (int i = 0; i < res_arr.length(); i++) {
					JSONObject inn_obj = res_arr.getJSONObject(i);
					res = inn_obj.getString("status");
					Log.e("register response >>", res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (res.equals("Success")) {

				Log.e("Inside onPost >>", "onPost");
				Log.e("school_name", spinner_selected_school_name);
				Log.e("school_id", spinner_selected_school_id);
				editor = settings.edit();
				editor.putString("school", spinner_selected_school_name);
				editor.putString("tagId", spinner_selected_school_id);
				editor.putString("email", e_mail_editText.getText().toString());
				editor.putString("birthday", br_date_editText.getText()
						.toString());
				editor.putString("school_name", school_name);
				editor.commit();
				Setting1.myschoolid = spinner_selected_school_id;
				Toast.makeText(Setting1.this, "Updated Successfully",
						Toast.LENGTH_SHORT).show();
			} else if (response != null && response.contains("Campus Failed")) {
				Toast.makeText(Setting1.this, "Campus Failed",
						Toast.LENGTH_SHORT).show();
			} else if (response != null && response.contains("UserId Failed")) {
				Toast.makeText(Setting1.this, "UserId Failed",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Setting1.this,
						"Failed- Query execution failed. please try again",
						Toast.LENGTH_SHORT).show();
			}
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			Log.i("Date dialog", "entered");
			// set date picker as current date
			DatePickerDialog dateDialog = new DatePickerDialog(this,
					mDateSetListener, year, month, day) {
				@Override
				public void onDateChanged(DatePicker view, int mYear,
						int monthOfYear, int dayOfMonth) {
					Log.i("On Date changed", "entered");
					if (mYear > year)
						view.updateDate(year, month, day);

					if (monthOfYear > month && mYear == year)
						view.updateDate(year, month, day);

					if (dayOfMonth > day && mYear == year
							&& monthOfYear == month)
						view.updateDate(year, month, day);

				}
			};
			return dateDialog;
		}
		return null;
	}

	public void Get_Campuses() {
		Log.v("Inside Checker", "==============>>" + check);
		String res = Parsing_JSON.readFeed(ParserClass.settingSubTwo);

		JSONObject job1;
		try {
			schooltagid_List.clear();
			school_name_List.clear();
			job1 = new JSONObject(res);
			if (job1 != null) {
				check++;
				String reg = job1.getString("data");
				JSONArray jarr1 = new JSONArray(reg);
				for (int i = 0; i < jarr1.length(); i++) {

					JSONObject inner_obj = jarr1.getJSONObject(i);

					campus_tagid = inner_obj.getString("tagid");
					campus_school = inner_obj.getString("school");
					Log.v("tag id >>", campus_tagid);
					Log.v("school name >>", campus_school);
					schooltagid_List.add(campus_tagid);
					school_name_List.add(campus_school);

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openSpinner(final List<String> wordList2) {
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				Setting1.this, R.layout.school_list, wordList2);
		String title = "Select School";

		new AlertDialog.Builder(Setting1.this).setTitle(title)
				.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						spinnerClicked = true;
						text_Save.setTextColor(Color.parseColor("#FFFFFF"));
						spinner_selected_school_name = wordList2.get(which)
								.toString();
						school_name = spinner_selected_school_name;
						Log.e("Selected Item spinner select-->", school_name);
						school_TextView.setText(spinner_selected_school_name);
						schoolid_Arr = new String[schooltagid_List.size()];
						schoolid_Arr = schooltagid_List.toArray(schoolid_Arr);
						spinner_selected_school_id = schoolid_Arr[which];
						dialog.dismiss();
					}
				}).create().show();

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String date_selected = String.valueOf(monthOfYear + 1) + "-"
					+ String.valueOf(dayOfMonth) + "-" + String.valueOf(year);
			// Toast.makeText(ExampleApp.this,
			// "Selected Date is ="+date_selected, Toast.LENGTH_SHORT).show();
			dobClicked = true;
			// text_Save.setVisibility(View.VISIBLE);
			text_Save.setTextColor(Color.parseColor("#FFFFFF"));
			br_date_editText.setText(date_selected);

		}
	};

	public static boolean haveInternet(Context ctx) {
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("Inside settings OnResume", "Method");
		settings = getSharedPreferences(PREFS_NAME, 0);
		email_id = settings.getString("email", "");
		bday = settings.getString("birthday", "");
		spinner_selected_school_name = settings.getString("school", "");

		text_Save.setTextColor(Color.parseColor("#73FFFFFF"));
		textFieldClicked = false;
		dobClicked = false;
		spinnerClicked = false;

		if (haveInternet(Setting1.this) == true) {
			Get_Campuses();
		} else {
			Toast.makeText(Setting1.this,
					"Please Check your Intenet Connection", Toast.LENGTH_SHORT)
					.show();
		}

		Log.e("onResume School name from sharpref >>",
				spinner_selected_school_name);
		Log.e("onResume email value in sharedpref", email_id);
		Log.e("onResume bday value in sharedpref", bday);
		e_mail_editText.setText(email_id);
		br_date_editText.setText(bday);
		school_TextView.setText(spinner_selected_school_name);

	}

}
