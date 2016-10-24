package com.fsp.blacksheep;

import java.net.URL;
import java.text.ParseException;
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
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends Activity {
	private static final int DATE_DIALOG_ID = 0;
	public static final String PREFS_NAME = "MyPrefsFile";
	static SharedPreferences settings;
	SharedPreferences.Editor editor;
	String response = null, device_id = null,
			spinner_selected_school_name = "", spinner_selected_school_id,
			Email_Value, stored_email_value, stored_birthday_value,
			campus_tagid, campus_school, school_name;

	// Static String used in BS_Main Page //
	public static String myschoolid = "";
	String email_id, bday;
	int spinner_selection_position;

	// UI Varaibles

	EditText sname, tagid, e_mail_editText, br_date_editText;
	Button submit_button;
	// Spinner campus_spinner;
	ListView lv_school;
	SchoolAdapterSetting schl_adapter;

	// Ends

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
	SharedPreferences pushPrefs;
	TextView t_school, t_email, t_dob;

	View lastView;
	String s_name;
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;

	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;

	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(Setting.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("Settings page Started");

		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);

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
		FlurryAgent.onEndSession(Setting.this);
		EasyTracker.getInstance().activityStop(this);
		// myNewTracker.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_settings);

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		lv_school = (ListView) findViewById(R.id.list_school);
		settings = getSharedPreferences(PREFS_NAME, 0);
		email_id = settings.getString("email", "");
		Log.v("email", email_id);
		bday = settings.getString("birthday", "");
		Log.v("bday", bday);
		spinner_selected_school_name = settings.getString("school", "");
		spinner_selected_school_id = settings.getString("tagId", "");
		s_name = settings.getString("school", "");
		Log.v("School name from sharpref >>", spinner_selected_school_name);
		Log.v("School id from sharpref >>", spinner_selected_school_id);
		editor = settings.edit();

		e_mail_editText = (EditText) findViewById(R.id.email_value);
		br_date_editText = (EditText) findViewById(R.id.dob);
		Typeface type1 = Typeface.createFromAsset(getAssets(), "CORBEL.TTF");
		e_mail_editText.setTypeface(type1);
		br_date_editText.setTypeface(type1);
		submit_button = (Button) findViewById(R.id.regsubmit);
		t_school = (TextView) findViewById(R.id.txt_school);
		t_email = (TextView) findViewById(R.id.txt_email);
		t_dob = (TextView) findViewById(R.id.txt_bday);
		Typeface type = Typeface.createFromAsset(getAssets(),
				"Cubano-Regular.otf");
		t_school.setTypeface(type);
		t_email.setTypeface(type);
		t_dob.setTypeface(type);
		e_mail_editText.setText(email_id);
		br_date_editText.setText(bday);

		TelephonyManager telemngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		device_id = telemngr.getDeviceId();
		Log.v("deviceid", "" + device_id);
		e_mail_editText.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null
						&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							e_mail_editText.getWindowToken(), 0);

					return true;

				}
				return false;
			}
		});
		school_name_List.clear();
		if (haveInternet(Setting.this) == true) {
			Get_Campuses();
			// new Get_User_Data().execute();

			for (int i = 0; i < school_name_List.size(); i++) {
				Log.v("Inside For", school_name_List.get(i));
				Log.v("spinner_selected_school_name",
						spinner_selected_school_name);
				if (school_name_List.get(i).toString()
						.equalsIgnoreCase(spinner_selected_school_name)) {
					spinner_selection_position = i;
				}
			}
			schl_adapter = new SchoolAdapterSetting(Setting.this,
					school_name_List, spinner_selection_position);

			lv_school.setAdapter(schl_adapter);

			schl_adapter.notifyDataSetChanged();
			lv_school.setSelection(spinner_selection_position);

			lv_school.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> pa, View v, int pos,
						long row1) {
					// e_mail_editText.setText("");
					// br_date_editText.setText("");
					lv_school.setAdapter(null);
					schl_adapter = new SchoolAdapterSetting(Setting.this,
							school_name_List, pos);
					lv_school.setAdapter(schl_adapter);
					schl_adapter.notifyDataSetChanged();
					lv_school.setSelection(pos);
					spinner_selected_school_name = school_name_List.get(pos);
					spinner_selected_school_id = schooltagid_List.get(pos);
					Log.v("spinner_selected_school_name",
							school_name_List.get(pos));
					Log.v("spinner_selected_school_id",
							schooltagid_List.get(pos));
					school_name = ((TextView) v.findViewById(R.id.schl_text))
							.getText().toString();
					Log.v("school_name >>>>", school_name);
				}
			});
		} else {
			Toast.makeText(Setting.this,
					"Please Check your Intenet Connection.", Toast.LENGTH_SHORT)
					.show();
		}

		br_date_editText.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent arg1) {
				showDialog(DATE_DIALOG_ID);
				return false;
			}
		});

		submit_button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Email_Value = "";
				Email_Value = String.valueOf(e_mail_editText.getText());

				Boolean value = true;
				// checking email
				if (Email_Value.length() > 0) {
					value = checkEmailValidation(Email_Value);
				}

				try {
					if (Email_Value.equalsIgnoreCase("")
							|| Email_Value.equalsIgnoreCase(null)) {
						Toast.makeText(getApplicationContext(),
								"Please enter email", Toast.LENGTH_LONG).show();
					} else if (!value) {
						Toast.makeText(getApplicationContext(),
								"Invalid Email Id Format", Toast.LENGTH_LONG)
								.show();

					} else if (br_date_editText.getText().toString().trim()
							.length() == 0) {
						Toast.makeText(getApplicationContext(),
								"Please enter date of birth", Toast.LENGTH_LONG)
								.show();
					} else {

						Email_Value = String.valueOf(e_mail_editText.getText());

						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("M-d-yyyy");
						String currentDate = df.format(c.getTime());

						SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
						Date date1 = sdf.parse(br_date_editText.getText()
								.toString().trim());
						Date date2 = sdf.parse(currentDate);

						System.out.println(sdf.format(date1));
						System.out.println(sdf.format(date2));

						if (date1.compareTo(date2) > 0) {
							System.out.println("Date1 is after current date");
							Toast.makeText(getApplicationContext(),
									"Must be 18 or older to enjoy.",
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
											Setting.this,
											"Device ID is Not set. Try Again Later",
											Toast.LENGTH_SHORT).show();
								} else if (spinner_selected_school_name
										.equalsIgnoreCase("")
										|| spinner_selected_school_name
												.equalsIgnoreCase(null)) {
									Log.v("school not selected >>",
											"School Name Should not be Empty");
									Toast.makeText(
											Setting.this,
											"School Name Should not be Empty. Please select one.",
											Toast.LENGTH_SHORT).show();
								} else if (spinner_selected_school_id
										.equalsIgnoreCase("")
										|| spinner_selected_school_id
												.equalsIgnoreCase(null)) {
									Toast.makeText(Setting.this,
											"School Id Should not be Empty",
											Toast.LENGTH_SHORT).show();
								} else if (e_mail_editText.getText().toString()
										.trim().length() == 0) {
									Toast.makeText(Setting.this,
											"Email should not be Empty",
											Toast.LENGTH_SHORT).show();
								} else if (br_date_editText.getText()
										.toString().trim().length() == 0) {
									Toast.makeText(
											Setting.this,
											"Date of Birth should not be Empty",
											Toast.LENGTH_SHORT).show();
								}

								else {
									if (e_mail_editText.getText().toString()
											.trim().length() != 0) {
										Email_Value = String
												.valueOf(e_mail_editText
														.getText());
										checkEmail(Email_Value);
									} else {
										new Task_Update().execute();
									}
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
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
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
		final AlertDialog alertDialog = new AlertDialog.Builder(Setting.this)
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
			if (haveInternet(Setting.this) == true) {
				new Task_Update().execute();
			} else {
				Toast.makeText(Setting.this,
						"Please Check your Intenet Connection",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			showAlert("Invalid Email Id Format");
		}
	}

	public class Task_Update extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(Setting.this);
		String res;

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

				post = new HttpPost(ParserClass.settingOne);

				json.put("u_id", device_id);
				json.put("firstName", "");
				json.put("lastName", "");
				json.put("school", spinner_selected_school_name);
				json.put("tagId", spinner_selected_school_id);
				json.put("email", e_mail_editText.getText().toString());
				json.put("birthday", br_date_editText.getText().toString());
				Log.v("json input >>", json + "");
				post.setHeader("Content-Type", "application/json");
				post.setHeader("Accept", "application/json");
				StringEntity se = new StringEntity(json.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				post.setEntity(se);
				response = client.execute(post);
				Log.v("Response >>>", response + "");
				String ret = EntityUtils.toString(response.getEntity());
				Log.v("My calendar response", ret);
				JSONObject res_obj = new JSONObject(ret);
				String res1 = res_obj.getString("blacksheep");
				JSONArray res_arr = new JSONArray(res1);
				for (int i = 0; i < res_arr.length(); i++) {
					JSONObject inn_obj = res_arr.getJSONObject(i);
					res = inn_obj.getString("status");
					Log.v("register response >>", res);
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			if (res.equals("Success")) {

				Log.v("Inside onPost >>", "onPost");
				Log.v("school_name", spinner_selected_school_name);
				Log.v("school_id", spinner_selected_school_id);
				editor = settings.edit();
				editor.putString("school", spinner_selected_school_name);
				editor.putString("tagId", spinner_selected_school_id);
				editor.putString("email", e_mail_editText.getText().toString());
				editor.putString("birthday", br_date_editText.getText()
						.toString());
				editor.putString("school_name", school_name);
				editor.commit();
				Setting.myschoolid = spinner_selected_school_id;
				Toast.makeText(Setting.this, "Updated Successfully",
						Toast.LENGTH_SHORT).show();
				finish();
			} else if (response.contains("Campus Failed")) {
				Toast.makeText(Setting.this, "Campus Failed",
						Toast.LENGTH_SHORT).show();
			} else if (response.contains("UserId Failed")) {
				Toast.makeText(Setting.this, "UserId Failed",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Setting.this,
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
		Calendar c = Calendar.getInstance();
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, cyear, cmonth,
					cday);
		}
		return null;
	}

	public void Get_Campuses() {
		Log.v("Inside Checker", "==============>>" + check);
		String res = Parsing_JSON.readFeed(ParserClass.registrationOne);

		JSONObject job1;
		try {
			job1 = new JSONObject(res);
			String reg = job1.getString("data");
			JSONArray jarr1 = new JSONArray(reg);
			check++;
			for (int i = 0; i < jarr1.length(); i++) {

				JSONObject inner_obj = jarr1.getJSONObject(i);

				campus_tagid = inner_obj.getString("tagid");
				campus_school = inner_obj.getString("school");
				Log.v("tag id >>", campus_tagid);
				Log.v("school name >>", campus_school);
				schooltagid_List.add(campus_tagid);
				school_name_List.add(campus_school);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String date_selected = String.valueOf(monthOfYear + 1) + "-"
					+ String.valueOf(dayOfMonth) + "-" + String.valueOf(year);
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

	public class SchoolAdapterSetting extends BaseAdapter {
		private Activity activity;
		public View pressedView;
		private int highlight_pos;
		List<String> data = new ArrayList<String>();
		private LayoutInflater inflater = null;

		public SchoolAdapterSetting(Activity a, List<String> school_name_List,
				int pos) {
			// TODO Auto-generated constructor stub
			activity = a;
			data = school_name_List;
			highlight_pos = pos;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;

			vi = inflater.inflate(R.layout.school_item, null);
			TextView info = (TextView) vi.findViewById(R.id.schl_text);
			Typeface type1 = Typeface
					.createFromAsset(getAssets(), "CORBEL.TTF");
			info.setTypeface(type1);
			if (position == highlight_pos) {
				info.setBackgroundResource(R.drawable.list_selected);
			}
			info.setText(data.get(position));
			return vi;
		}

	}

}
