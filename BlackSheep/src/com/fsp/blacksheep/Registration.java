package com.fsp.blacksheep;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class Registration extends Activity {
	private static final int DATE_DIALOG_ID = 0;
	TableRow row;
	View line;
	EditText first_ed;
	EditText last_ed;
	EditText reqschool;
	EditText dob;
	EditText emailval;
	String response = null;
	String schoolvalue = "", sch_Name;
	String schoolvalue_id = null;
	TextView submit;
	int block = 0;
	String otherschool = null;
	String schoolname[];
	String schoolid[];
	List<String> schoolList;

	String url = ParserClass.registrationOne; // json
	List<String> school = new ArrayList<String>();
	List<String> schooltagid = new ArrayList<String>();
	String device_id = null;
	String firstname = null;
	String lastname = null;
	String email = "";
	String dobvalue = "", school_name;
	AlertDialog.Builder alertbox = null;
	Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	boolean e_matchFound;
	String error;
	String schoolerror;
	String emailerror;
	int status = 0;
	Button term;
	static String first;
	boolean firstTime;
	SharedPreferences mPreferences;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	BS_Splash bs;
	public static final String PREFS_NAME = "MyPrefsFile";
	TextView text_Schoolvalue, terms_Text;

	View lastView;
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;

	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;
	
	private Calendar cal;
	private int day, month,year;

	@Override
	public void onStart() {
		super.onStart();

		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(Registration.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("BlackSheepMobile Android Application Started");

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
		 * myNewTracker.trackView("BlackSheepMobile Android Application Started"
		 * );
		 */

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Enablingstrict mode
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH); //Current day
		month = cal.get(Calendar.MONTH);// current month
		year = cal.get(Calendar.YEAR); //current year
		
		setContentView(R.layout.registration);

		if (haveInternet(Registration.this) == true) {

			Log.e("The", "Registration page is visited");
			settings = getSharedPreferences(PREFS_NAME, 1);
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				Window window = Registration.this.getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.setStatusBarColor(Registration.this.getResources()
						.getColor(R.color.background));
			}

			text_Schoolvalue = (TextView) findViewById(R.id.school_value);
			dob = (EditText) findViewById(R.id.dob);
			emailval = (EditText) findViewById(R.id.email_value);
			submit = (TextView) findViewById(R.id.regsubmit);
			terms_Text = (TextView) findViewById(R.id.text1);

			Typeface type = Typeface.createFromAsset(getAssets(),
					"Mission Gothic Regular.otf");
			Typeface type1 = Typeface.createFromAsset(getAssets(),
					"Mission Gothic Bold.otf");
			emailval.setTypeface(type);
			text_Schoolvalue.setTypeface(type);
			// t_email.setTypeface(type);
			dob.setTypeface(type);
			submit.setTypeface(type1);

			text_Schoolvalue.setText(Html
					.fromHtml("PICK YOUR SCHOOL<sup>*</sup>"));
			emailval.setOnEditorActionListener(new OnEditorActionListener() {

				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (event != null
							&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(emailval.getWindowToken(),
								0);

						return true;

					}
					return false;
				}
			});

			TelephonyManager telemngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			device_id = telemngr.getDeviceId();
			alertbox = new AlertDialog.Builder(Registration.this);

			try {

				school.addAll(parse_schoolname());

				schooltagid.addAll(parse_schooltag());

				schoolname = (String[]) school
						.toArray(new String[school.size()]);
				schoolid = (String[]) schooltagid
						.toArray(new String[schooltagid.size()]);
				schoolList = Arrays.asList(schoolname);
			} catch (Exception e) {
				Log.v("parsingschool", e.toString());
			}

			text_Schoolvalue.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					openSpinner(schoolList);
				}
			});

			terms_Text.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {

				}
			});

			dob.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View arg0, MotionEvent arg1) {

					showDialog(DATE_DIALOG_ID);
					return false;
				}

			});

			submit.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					sch_Name = text_Schoolvalue.getText().toString().trim();
					email = emailval.getText().toString();
					dobvalue = dob.getText().toString();
					if (sch_Name.equals("PICK YOUR SCHOOL*")) {
						sch_Name = "";
					}
					Log.e("SchoolName Inside submit", sch_Name);
					Log.e("email Inside submit", email);
					Log.e("dobvalue Inside submit", dobvalue);
					// //
					if (sch_Name != null && !sch_Name.isEmpty()
							&& email.equals("") && dobvalue.equals("")) {
						if (haveInternet(Registration.this) == true) {
							new Task_Update().execute();
						} else {
							Toast.makeText(Registration.this,
									"Please Check your Intenet Connection",
									Toast.LENGTH_SHORT).show();
						}
					} else if (sch_Name != null
							&& !sch_Name.isEmpty()
							&& ((email != null && !sch_Name.isEmpty()) && dobvalue
									.equals(""))) {
						Boolean emailvalue = true;
						// checking email
						if (email.length() > 0) {
							emailvalue = checkEmailValidation(email);
						}
						if (!emailvalue) {
							Toast.makeText(getApplicationContext(),
									"Invalid Email Id Format",
									Toast.LENGTH_LONG).show();
						} else {
							if (haveInternet(Registration.this) == true) {
								 new Task_Update().execute();
							} else {
								Toast.makeText(Registration.this,
										"Please Check your Intenet Connection",
										Toast.LENGTH_SHORT).show();
							}
						}
					} else if (sch_Name != null && !sch_Name.isEmpty()
							&& (email.equals(""))
							&& (dobvalue != null && !dobvalue.isEmpty())) {
                         
						////
						try{
						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat(
								"M-d-yyyy");
						String currentDate = df.format(c.getTime());

						SimpleDateFormat sdf = new SimpleDateFormat(
								"M-d-yyyy");
						Date date1 = sdf.parse(dobvalue);
						Date date2 = sdf.parse(currentDate);

						System.out.println(sdf.format(date1));
						System.out.println(sdf.format(date2));

						if (date1.compareTo(date2) < 0) {
							System.out
									.println("Date1 is before current date");
							String[] split = dobvalue.split("-");
							int age = getAge(
									Integer.parseInt(split[2]),
									Integer.parseInt(split[1]),
									Integer.parseInt(split[0]));
							if (age >= 18) {
								new Task_Update().execute();
							} else {
								Toast.makeText(
										getApplicationContext(),
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
					} else {
						// //
						Boolean value = true;
						// checking email
						if (email.length() > 0) {
							value = checkEmailValidation(email);
						}

						try {
							if (sch_Name.equals("")) {
								Toast.makeText(getApplicationContext(),
										"Please select school",
										Toast.LENGTH_LONG).show();
							}
							else if (!value) {
								Toast.makeText(getApplicationContext(),
										"Invalid Email Id Format",
										Toast.LENGTH_LONG).show();
							}
							else {

								Calendar c = Calendar.getInstance();
								SimpleDateFormat df = new SimpleDateFormat(
										"M-d-yyyy");
								String currentDate = df.format(c.getTime());

								SimpleDateFormat sdf = new SimpleDateFormat(
										"M-d-yyyy");
								Date date1 = sdf.parse(dobvalue);
								Date date2 = sdf.parse(currentDate);

								System.out.println(sdf.format(date1));
								System.out.println(sdf.format(date2));

								if (date1.compareTo(date2) < 0) {
									System.out
											.println("Date1 is before current date");
									String[] split = dobvalue.split("-");
									int age = getAge(
											Integer.parseInt(split[2]),
											Integer.parseInt(split[1]),
											Integer.parseInt(split[0]));
									// Log.v("age >>",age+"");
									if (age >= 18) {
										if (sch_Name.equals("")) {
											Toast.makeText(
													getApplicationContext(),
													"Please select school",
													Toast.LENGTH_LONG).show();
										}
										else {
											checkEmail(email);
										}
									} else {
										Toast.makeText(
												getApplicationContext(),
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} // end of main else
				}
			});

		} else {
			Toast.makeText(Registration.this,
					"Please Check your Intenet Connection", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public Boolean checkEmailValidation(String email_value) {
		Validation val = new Validation();
		boolean emailValid = false;
		emailValid = val.validateEmail(email_value.trim());
		if (emailValid) {
			return true;
		} else {
			// showAlert("Invalid Email Id Format");
			return false;
		}
	}

	private List<String> parse_schooltag() throws UnknownHostException {
		// TODO Auto-generated method stub
		String res = Parsing_JSON.readFeed(url);
		Log.e("The", "registration response is=>>" + res);
		List<String> school_nameid = new ArrayList<String>();
		JSONObject job1;
		try {
			job1 = new JSONObject(res);
			String reg = job1.getString("data");
			JSONArray jarr1 = new JSONArray(reg);
			for (int i = 0; i < jarr1.length(); i++) {

				JSONObject inner_obj = jarr1.getJSONObject(i);

				String sch_id = inner_obj.getString("tagid");
				String school1 = inner_obj.getString("school");
				school_nameid.add(sch_id);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return school_nameid;
	}

	private List<String> parse_schoolname() throws UnknownHostException {
		// TODO Auto-generated method stub
		String res = Parsing_JSON.readFeed(url);

		Log.i("The", " Regggg response is->" + res);
		List<String> school_name1 = new ArrayList<String>();

		JSONObject job1;
		try {
			job1 = new JSONObject(res);
			String reg = job1.getString("data");
			Log.i("f_tag", reg);
			JSONArray jarr1 = new JSONArray(reg);
			for (int i = 0; i < jarr1.length(); i++) {
				JSONObject inner_obj = jarr1.getJSONObject(i);
				String sch_id = inner_obj.getString("tagid");
				String school1 = inner_obj.getString("school");
				school_name1.add(school1);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return school_name1;
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
		switch (id) {
		case DATE_DIALOG_ID:
			Log.i("Date dialog","entered");
			// set date picker as current date
	    	DatePickerDialog dateDialog =   new DatePickerDialog(this, mDateSetListener, year,month,
	    			day){
	            @Override
	            public void onDateChanged(DatePicker view, int mYear, int monthOfYear, int dayOfMonth)
	            {   
	            	Log.i("On Date changed","entered");
	                if (mYear > year)
	                    view.updateDate(year, month, day);
	
	                if (monthOfYear > month && mYear == year)
	                	view.updateDate(year, month, day);
	
	                if (dayOfMonth > day && mYear == year && monthOfYear == month)
	                	view.updateDate(year, month, day);
	
	            }
	        };
	    	return dateDialog;
	    }
		return null;
	}

	public void checkEmail(String email_value) {
		Validation val = new Validation();
		boolean emailValid = false;
		emailValid = val.validateEmail(email_value.trim());
		if (emailValid) {
			if (haveInternet(Registration.this) == true) {
					 new Task_Update().execute();
			} else {
				Toast.makeText(Registration.this,
						"Please Check your Intenet Connection",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			showAlert("Invalid Email Id Format");
		}
	}

	public class Task_Update extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				Registration.this);
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
			Log.e("Inside", "Backgound");
			Log.e("The School name is=>", school_name);
			Log.e("The school ID value is=>", schoolvalue_id);
			Log.e("The Email id is=>", email);
			Log.e("The DOB  id is=>", dobvalue);
			Log.e("The device id is=>", device_id);
			JSONObject json = new JSONObject();
			try {
				// changes
				// post = new HttpPost(
				// "http://104.130.66.20/mobile_new/registration_edit.php?");
				post = new HttpPost(ParserClass.registrationTwo);

				json.put("u_id", device_id);
				json.put("firstName", "");
				json.put("lastName", "");

				json.put("school", school_name);
				json.put("tagId", schoolvalue_id);
				json.put("email", email);
				json.put("birthday", dobvalue);

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
			Log.e("Response", "Post Execute");
			if(res.length() > 0) {
				if (res.equals("Success")) {
	
					try {
						editor = settings.edit();
						editor.putInt("firstTime", 1);
						editor.putString("school_name", school_name);
						editor.putString("school", school_name);
						editor.putString("tagId", schoolvalue_id);
						editor.putString("email", email);
						editor.putString("birthday", dobvalue);
						editor.commit();
					} catch (Exception e) {
					}
					Intent obj_intent;
					Bundle b = new Bundle();
					b.putString("key1", "HomePage");
					obj_intent = new Intent(Registration.this, BS_Main.class);
					obj_intent.putExtras(b);
					startActivity(obj_intent);
					finish();
				} else if (response.contains("Campus Failed")) {
					Toast.makeText(Registration.this, "Campus Failed",
							Toast.LENGTH_SHORT).show();
				} else if (response.contains("UserId Failed")) {
					Toast.makeText(Registration.this, "UserId Failed",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Registration.this,
							"Failed- Query execution failed. please try again",
							Toast.LENGTH_SHORT).show();
				}
			}
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	public void showAlert(String msg) {
		final AlertDialog alertDialog = new AlertDialog.Builder(
				Registration.this).create();
		alertDialog.setMessage(msg);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	public static boolean haveInternet(Context ctx) {
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String date_selected = String.valueOf(monthOfYear + 1) + "-"
					+ String.valueOf(dayOfMonth) + "-" + String.valueOf(year);
			dob.setText(date_selected);
			dobvalue = dob.getText().toString();

		}
	};

	private void openSpinner(final List<String> wordList2) {
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				Registration.this, R.layout.school_list, wordList2);
		String title = "Select School";

		new AlertDialog.Builder(Registration.this).setTitle(title)
				.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						school_name = wordList2.get(which).toString();
						Log.e("Selected Item spinner select-->", school_name);
						text_Schoolvalue.setText(school_name);
						schoolvalue_id = schoolid[which];
						dialog.dismiss();
					}
				}).create().show();

	}

	@Override
	public void onStop() {
		super.onStop();

		FlurryAgent.onEndSession(Registration.this);
		EasyTracker.getInstance().activityStop(this);
	}
}
