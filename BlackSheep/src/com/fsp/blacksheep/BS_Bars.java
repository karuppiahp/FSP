package com.fsp.blacksheep;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import com.codecarpet.fbconnect.FBFeedActivity;
import com.codecarpet.fbconnect.FBRequest;
import com.codecarpet.fbconnect.FBSession;
import com.codecarpet.fbconnect.FBSession.FBSessionDelegate;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.flurry.android.FlurryAgent;
import com.fsp.blacksheep.R;
import com.fsp.blacksheep.Setting1.Task_Update;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

@SuppressWarnings("unused")
public class BS_Bars extends Activity {
	// static ListView lv;
	boolean[] check = { false, false };
	Activity act;
	static int myVal = 0;
	String mynote = null, mydesc = null, temp_url, tweetMsg = "";
	private int l, k, a = -1, a1 = -1, b, c, d, e, e1, e2, c1 = -1, c2, c3, c4,
			c5, c6, c7, c8;
	List<Message_bar_date_id> l_obj_bar_date_id;
	DateFormat formatter1 = new SimpleDateFormat("MMMMM d");

	private String[] VALUE_UNIVID, VALUE_BARDATE, VALUE_BARDATEID = null,
			VALUE_eventdate, VALUE_eventdesc, VALUE_eventid, VALUE_eventimg,
			VALUE_barname = null, VALUE_barphone, VALUE_baraddress1,
			VALUE_baraddress2, VALUE_baraddress3, VALUE_barsite, VALUE_bardesc,
			VALUE_bardrink;
	public static String[] VALUE_eventname;
	public static ProgressDialog dialog;
	MyCount counter = new MyCount(2000, 1000);
	private String TAG = "BS_Bars";
	Uri uri;
	View previous;
	static ListView lv_bardate;
	String temp, temp_img, mon;
	static ListView lv_barlist;
	static ScrollView sv;
	static RelativeLayout bars_wv, barsocialshare_Rel;
	static LinearLayout lv_barweb;
	static int bar_back = 0;
	static int pos = 0;
	static String detail_temp, date_temp, head_text, date_temp1;
	TextView t1, t2, t3, t4, t5, desc_label, text_Header;
	private ImageView forward_img, backward_img;
	private ImageView arrow3 = null;
	private Bitmap mIcon1;
	String url_temp = null;
	static String url2;
	private Bundle bund;
	boolean row_disapper = false;
	MultiLine_bar_details1 mbar;
	String day = null;
	public static final String APP_ID = "242878212411329";
	private static final String[] PERMISSIONS = new String[] {
			"publish_stream", "read_stream", "offline_access" };
	public static Facebook authenticatedFacebook = new Facebook(APP_ID);
	public static String name_login = null;
	String birthday, email, school, tagId;
	/* share variables */

	final private int MENU_MAIL = Menu.FIRST;
	final private int MENU_FB = Menu.FIRST + 1;
	final private int MENU_TWITTER = Menu.FIRST + 2;
	final private int MENU_SMS = Menu.FIRST + 3;
	final private int MENU_CANCEL = Menu.FIRST + 4;
	private Handler mHandler;
	private String desc_val;
	String temp_title;
	String share_desc;
	String share_desc2;
	String share_url, phone_link;
	String test_value = " ";
	static int screenWidth = 0;
	static int screenHeight = 0;
	static String school_id, school_value;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String PREFS_NAME1 = "BLACKSHEEP";
	static SharedPreferences settings;
	SharedPreferences.Editor editor;

	static SharedPreferences settings1;
	SharedPreferences.Editor editor1;

	private SharedPreferences prefs;

	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 20;

	// Starts
	URL bars_ads_url = null;

	String ads_value, fb_clicked = "", twitterShareLink = "";

	// Ends

	String url = ParserClass.drinksSpecial;

	ArrayList<String> ads_List = new ArrayList<String>();
	Button btn_Down;
	LinearLayout ll1;
	TextView text_Cancel, text_Dateheader, text_Header1;
	Button drinks_BackBtn, baremail_Btn, barfacebook_Btn, bartwitter_Btn,
			barcancel_Btn;
	URL feedUrl;
	ArrayList<String> bar_det = new ArrayList<String>();
	RelativeLayout br_details, relative_Downarrow;
	Button click_toshare;
	Bundle b1;
	String date_url, size;
	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;
	// Twitter
	/* Shared preference keys */
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";
	private static Twitter twitter;
	private static RequestToken requestToken;
	private String callbackUrl = null;
	private String oAuthVerifier = null;
	public static final int WEBVIEW_REQUEST_CODE = 100;
	String consumerKey = "", consumerSecret = "";
	private static SharedPreferences mSharedPreferences;
	RelativeLayout rel_Alert;
	private ArrayList<String> daysArray = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> barsArray = new ArrayList<HashMap<String, String>>();
	private TextView txtForDetailHeader, txtForDetailAddress, txtForSunDesc,
			txtForMonDesc, txtForTueDesc, txtForWedDesc, txtForThuDesc,
			txtForFriDesc, txtForSatDesc;

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(BS_Bars.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("Drink Specials Started");

		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);

		// tracker = GoogleAnalyticsTracker.getInstance();
		//
		// // Start the tracker in manual dispatch mode...
		// // tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1",this);
		// tracker.trackPageView("/BS_Bars");
		/*
		 * Context mCtx = this; // Get current context. myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * myInstance.setDebug(true); myNewTracker =
		 * myInstance.getTracker("UA-36318648-1");
		 * myInstance.setDefaultTracker(myNewTracker);
		 * myNewTracker.trackView("Drink Specials Started");
		 */
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Log.e("The", "BS_BARS PAGE IS VISITED");

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		settings = getSharedPreferences(PREFS_NAME, 0);

		settings1 = getSharedPreferences(PREFS_NAME1, 0);

		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = settings.edit();

		editor1 = settings1.edit();

		BS_Bars.school_id = settings.getString("tagId", "No Data Found");
		BS_Bars.school_value = settings.getString("school", "No Data Found");
		Log.v("school id in bars >>", BS_Bars.school_id);
		Log.v("school name in bars", BS_Bars.school_value);
		act = BS_Bars.this;
		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		String str_ScreenSize = dm.widthPixels + " x " + dm.heightPixels;
		str_ScreenSize = "dd" + " x " + dm.heightPixels;
		setContentView(R.layout.bars_layout);

		if (screenWidth >= 480 && screenHeight >= 800) {

			size = "xl";
		} else if (screenWidth == 480 && screenHeight == 640) {

			size = "l";
		}

		else {
			setContentView(R.layout.bars_layout);
			size = "others";
		}
		// try
		try {
			if (BS_Bars.school_value.contains("Other")
					|| BS_Bars.school_value.contains("Others")) {
				displayPopUpAlert();
			} else {
				// Log.v("comes here.11111", "sfsf");
				text_Header = (TextView) findViewById(R.id.textheader);
				text_Header1 = (TextView) findViewById(R.id.drinktextheader);
				drinks_BackBtn = (Button) findViewById(R.id.drinksbackbtn);
				br_details = (RelativeLayout) findViewById(R.id.rl_br_desc);
				relative_Downarrow = (RelativeLayout) findViewById(R.id.dateListRelative);
				barsocialshare_Rel = (RelativeLayout) findViewById(R.id.socialdrinksspecial);

				baremail_Btn = (Button) findViewById(R.id.baremailBtn);
				barfacebook_Btn = (Button) findViewById(R.id.barfacebookBtn);
				bartwitter_Btn = (Button) findViewById(R.id.bartwitterBtn);
				barcancel_Btn = (Button) findViewById(R.id.barcancelBtn);
				click_toshare = (Button) findViewById(R.id.btn_share_bar);

				ll1 = (LinearLayout) findViewById(R.id.ll_sec_list);
				lv_bardate = (ListView) findViewById(R.id.bars_school_details);
				text_Cancel = (TextView) findViewById(R.id.alertcancel);
				rel_Alert = (RelativeLayout) findViewById(R.id.mainRelative);
				text_Dateheader = (TextView) findViewById(R.id.dateheader);
				btn_Down = (Button) findViewById(R.id.downarrow);
				txtForDetailHeader = (TextView) findViewById(R.id.txtForDetailHeader);
				txtForDetailAddress = (TextView) findViewById(R.id.txtForDetailAddress);
				txtForSunDesc = (TextView) findViewById(R.id.txtForSunDesc);
				txtForMonDesc = (TextView) findViewById(R.id.txtForMonDesc);
				txtForTueDesc = (TextView) findViewById(R.id.txtForTueDesc);
				txtForWedDesc = (TextView) findViewById(R.id.txtForWedDesc);
				txtForThuDesc = (TextView) findViewById(R.id.txtForThuDesc);
				txtForFriDesc = (TextView) findViewById(R.id.txtForFriDesc);
				txtForSatDesc = (TextView) findViewById(R.id.txtForSatDesc);

				lv_barlist = (ListView) findViewById(R.id.bars_list);

				Typeface type1 = Typeface.createFromAsset(getAssets(),
						"Mission Gothic Regular.otf");
				Typeface type = Typeface.createFromAsset(getAssets(),
						"Mission Gothic Bold.otf");
				text_Dateheader.setTypeface(type1);
				text_Header.setTypeface(type);
				text_Header1.setTypeface(type);
				lv_barlist.setVisibility(View.VISIBLE);
				// /----//
				settings = getSharedPreferences(PREFS_NAME, 0);

				temp = settings.getString("tagId", "No Data Found");
				Log.e("******temp in settings****************", temp);

				if (temp.contains("No Data Found")) {
					temp = Setting.myschoolid;
					Log.e("If condition temp in settings ", temp);
				}

				daysArray.add("SUNDAY");
				daysArray.add("MONDAY");
				daysArray.add("TUESDAY");
				daysArray.add("WEDNESDAY");
				daysArray.add("THURSDAY");
				daysArray.add("FRIDAY");
				daysArray.add("SATURDAY");
				lv_bardate.setVisibility(View.VISIBLE);
				Log.e("days array value>>>", "" + daysArray.get(0));

				String dayOfWeek = Utils.dayConversion();
				text_Dateheader.setText("TODAY");

				lv_bardate.setAdapter(new ArrayAdapter1(BS_Bars.this,
						daysArray, dayOfWeek, dayOfWeek));

				lv_bardate.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						String dayOfWeek = Utils.dayConversion();
						String selectedDay = daysArray.get(arg2);
						if (selectedDay.equals(dayOfWeek)) {
							text_Dateheader.setText("TODAY");
						} else {
							text_Dateheader.setText(selectedDay);
						}
						rel_Alert.setVisibility(View.GONE);
						lv_bardate.setVisibility(View.GONE);
						ArrayAdapter1 adapter = new ArrayAdapter1(BS_Bars.this,
								daysArray, dayOfWeek, selectedDay);
						adapter.notifyDataSetChanged();
						lv_bardate.setAdapter(adapter);
						lv_barlist.setVisibility(View.VISIBLE);
						lv_barlist.setAdapter(new BarsListAdapter(BS_Bars.this,
								barsArray, selectedDay));
					}
				});

				lv_barlist.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						lv_barlist.setVisibility(View.GONE);
						br_details.setVisibility(View.VISIBLE);
						txtForDetailHeader.setText(Html.fromHtml(barsArray.get(
								arg2).get("venue")));
						txtForDetailAddress.setText(Html.fromHtml(barsArray
								.get(arg2).get("address")));
						txtForSunDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("sunday")));
						txtForMonDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("monday")));
						txtForTueDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("tuesday")));
						txtForWedDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("wednesday")));
						txtForThuDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("thursday")));
						txtForFriDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("friday")));
						txtForSatDesc.setText(Html.fromHtml(barsArray.get(arg2)
								.get("saturday")));
					}
				});

				if (temp.contains("No Data Found")) {
					temp = Setting.myschoolid;
				}

				barsocialshare_Rel.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Log.e("Social Relative", "Clicked");
					}
				});

				click_toshare.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Utils.BARS_SHARE_VISIBILITY = true;
						barsocialshare_Rel.setVisibility(View.VISIBLE);
					}
				});

				baremail_Btn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent shareIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						shareIntent.setType("text/plain");
						shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								"http://www.theblacksheeponline.com/" + school
										+ "/bar-specials");

						PackageManager pm = v.getContext().getPackageManager();
						List<ResolveInfo> activityList = pm
								.queryIntentActivities(shareIntent, 0);
						for (final ResolveInfo app : activityList) {
							if ((app.activityInfo.name).contains("android.gm")) {
								final ActivityInfo activity = app.activityInfo;
								final ComponentName name = new ComponentName(
										activity.applicationInfo.packageName,
										activity.name);
								shareIntent
										.addCategory(Intent.CATEGORY_LAUNCHER);
								shareIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								shareIntent.setComponent(name);
								v.getContext().startActivity(shareIntent);
								break;
							}
						}
					}
				});

				barfacebook_Btn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent shareIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						shareIntent.setType("text/plain");
						shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								"http://www.theblacksheeponline.com/" + school
										+ "/bar-specials");

						PackageManager pm = v.getContext().getPackageManager();
						List<ResolveInfo> activityList = pm
								.queryIntentActivities(shareIntent, 0);
						for (final ResolveInfo app : activityList) {
							if ((app.activityInfo.name).contains("facebook")) {
								final ActivityInfo activity = app.activityInfo;
								final ComponentName name = new ComponentName(
										activity.applicationInfo.packageName,
										activity.name);
								shareIntent
										.addCategory(Intent.CATEGORY_LAUNCHER);
								shareIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								shareIntent.setComponent(name);
								v.getContext().startActivity(shareIntent);
								break;
							}
						}
					}
				});

				bartwitter_Btn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent shareIntent = new Intent(
								android.content.Intent.ACTION_SEND);
						shareIntent.setType("text/plain");
						shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								"http://www.theblacksheeponline.com/" + school
										+ "/bar-specials");

						PackageManager pm = v.getContext().getPackageManager();
						List<ResolveInfo> activityList = pm
								.queryIntentActivities(shareIntent, 0);
						for (final ResolveInfo app : activityList) {
							String packageName = app.activityInfo.packageName;
							if (packageName.contains("twitter")) {
								final ActivityInfo activity = app.activityInfo;
								final ComponentName name = new ComponentName(
										activity.applicationInfo.packageName,
										activity.name);
								shareIntent
										.addCategory(Intent.CATEGORY_LAUNCHER);
								shareIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								shareIntent.setComponent(name);
								v.getContext().startActivity(shareIntent);
								break;
							}
						}
					}
				});

				barcancel_Btn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Utils.BARS_SHARE_VISIBILITY = false;
						barsocialshare_Rel.setVisibility(View.GONE);
					}
				});

				text_Cancel.setOnClickListener(new OnClickListener() {
					public void onClick(View arg0) {
						rel_Alert.setVisibility(View.GONE);
						br_details.setVisibility(View.GONE);
					}
				});

				drinks_BackBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						br_details.setVisibility(View.GONE);
						lv_barlist.setVisibility(View.VISIBLE);
					}
				});

				relative_Downarrow.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						rel_Alert.setVisibility(View.VISIBLE);
						lv_bardate.setVisibility(View.VISIBLE);
					}
				});
			}
		} catch (Exception e) {
			Log.e("Inside Exception", "" + e);
		}
		// catch
	}

	private List<Message_bar_date> parse_bar_date() {
		// TODO Auto-generated method stub
		List<Message_bar_date> messages = new ArrayList<Message_bar_date>();
		String res = Parsing_JSON.readFeed(date_url);
		try {
			JSONObject job = new JSONObject(res);
			if (job != null) {
				String date_str = job.getString("BarListingDate");
				JSONArray j_arr = new JSONArray(date_str);
				for (int i = 0; i < j_arr.length(); i++) {
					Message_bar_date message = new Message_bar_date();
					JSONObject inn_obj = j_arr.getJSONObject(i);
					String b_dt = inn_obj.getString("BarDate");
					message.set_bar_date(b_dt);
					messages.add(message);
				}
			} else {
				displayAlert();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}

	private List<Message_bar_date_id> parse_bar_date_id() {
		// TODO Auto-generated method stub
		List<Message_bar_date_id> messages = new ArrayList<Message_bar_date_id>();
		String res = Parsing_JSON.readFeed(date_url);
		try {
			JSONObject job = new JSONObject(res);
			if (job != null) {
				String date_str = job.getString("BarListingDate");
				JSONArray j_arr = new JSONArray(date_str);
				for (int i = 0; i < j_arr.length(); i++) {
					Message_bar_date_id message = new Message_bar_date_id();
					JSONObject inn_obj = j_arr.getJSONObject(i);
					String b_id = inn_obj.getString("DateId");
					message.set_bar_date_id(b_id);

					messages.add(message);
				}
			} else {
				displayAlert();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}

	public class BarListTasks extends AsyncTask<Void, Void, Void> {

		private final ProgressDialog dialog;
		ArrayList<HashMap<String, String>> barsArray = new ArrayList<HashMap<String, String>>();
		SharedPreferences schoolDetails;
		String loopEnters = "";

		public BarListTasks(Context context,
				ArrayList<HashMap<String, String>> barsArrayList) {
			dialog = new ProgressDialog(context);
			barsArray = barsArrayList;
		}

		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
			barsArray.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			schoolDetails = getSharedPreferences(PREFS_NAME, 1);
			school = schoolDetails.getString("school_name", "");
			Log.e("School is?????", "" + school);
			if (school.contains(" ")) {
				school = school.replace(" ", "%20");
			}
			Log.e("School is?????", "" + school);
			String res = Parsing_JSON.readFeed(url + "" + school);
			Log.e("The", "Articles detail response is=>>" + res);
			JSONObject job1;
			try {
				job1 = new JSONObject(res);
				String reg = job1.getString("data");
				loopEnters = "array";
				JSONArray jarr1 = new JSONArray(reg);
				Log.e("Bars arry length????", "" + jarr1.length());
				for (int i = 0; i < jarr1.length(); i++) {

					JSONObject inner_obj = jarr1.getJSONObject(i);

					String school = inner_obj.getString("school");
					String venue = inner_obj.getString("venue");
					String id = inner_obj.getString("id");
					String address = inner_obj.getString("address");
					String sunday = inner_obj.getString("sunday");
					String monday = inner_obj.getString("monday");
					String tuesday = inner_obj.getString("tuesday");
					String wednesday = inner_obj.getString("wednesday");
					String thursday = inner_obj.getString("thursday");
					String friday = inner_obj.getString("friday");
					String saturday = inner_obj.getString("saturday");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("school", school);
					hashMap.put("venue", venue);
					hashMap.put("id", id);
					hashMap.put("address", address);
					hashMap.put("sunday", sunday);
					hashMap.put("monday", monday);
					hashMap.put("tuesday", tuesday);
					hashMap.put("wednesday", wednesday);
					hashMap.put("thursday", thursday);
					hashMap.put("friday", friday);
					hashMap.put("saturday", saturday);
					barsArray.add(hashMap);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				loopEnters = "exception";
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if (barsArray.size() > 0) {
				String day = Utils.dayConversion();
				lv_barlist.setAdapter(new BarsListAdapter(BS_Bars.this,
						barsArray, day));
			} else {
				lv_barlist.setAdapter(new BarsListAdapter(BS_Bars.this,
						barsArray, ""));
				if (loopEnters.equals("array")) {
					Toast.makeText(BS_Bars.this, "Bars list is empty",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(BS_Bars.this,
							"Please check your netwrok availability",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (fb_clicked.equals("clicked")) {
				// Log.v("bar details is visible >>", "visible");
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			try {
				BS_Bars.this.finish();
			} catch (Exception e) {
				// Log.v("BSMain", "Error in Bar Module Back Button");
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void displayPopUpAlert() {
		final TextView message = new TextView(BS_Bars.this);
		final SpannableString s = new SpannableString(
				"We currently don't have bar specials at your school. Email mhelp@theblacksheeponline.com to learn how to bring them to your campus.");
		Linkify.addLinks(s, Linkify.ALL);
		message.setText(s);
		message.setLinkTextColor(Color.WHITE);
		message.setMovementMethod(LinkMovementMethod.getInstance());
		new AlertDialog.Builder(this)
				.setView(message)
				.setTitle("Bars")
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	public void displayAlert() {
		new AlertDialog.Builder(this)
				.setMessage(
						"Please Check Your Internet Connection and Try Again")
				.setTitle("Network Error")
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		public void onFinish() {

			dialog.dismiss();
		}

		@Override
		public void onTick(long millisUntilFinished) {

			// TODO Auto-generated method stub
		}
	}

	// First Running Async Class [To display days inside the list]
	private class Task_Dates_Review extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(BS_Bars.this);

		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		protected void onPostExecute(Void result) {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			return null;
		}
	}

	// This class is running after redirection from web view page
	private class Task_Special extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(BS_Bars.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Log.e("Inside Do In Back", "Task_Special Class");
				b = 0;
				c = 0;
				d = 0;
				e2 = 0;
				e1 = 0;

				mbar = new MultiLine_bar_details1(act, BS_Bars.this, url2,
						lv_barlist);
			} catch (ArrayIndexOutOfBoundsException e) {
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			// Log.v("Count", ""+mbar.getCount());
			if (mbar.getCount() == 0) {
				lv_barlist.setVisibility(View.GONE);
				Toast.makeText(BS_Bars.this, "No Bars Available",
						Toast.LENGTH_SHORT).show();
			} else {
				lv_barlist.setVisibility(View.VISIBLE);
				lv_barlist.setAdapter(mbar);
			}
			int list_count = (lv_barlist.getCount()) - 1;
			lv_barlist.setVisibility(View.VISIBLE);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_MAIL, 0, "Email").setIcon(R.drawable.gmail_icon);
		menu.add(0, MENU_FB, 0, "Facebook").setIcon(R.drawable.facebook_icon);
		menu.add(0, MENU_TWITTER, 0, "Twitter").setIcon(
				R.drawable.twitter_icon_mini);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_MAIL:

			String temp_title1 = " Check out these specials for " + temp_title
					+ " on" + "  " + mynote + ". " + share_desc2
					+ ". Check out more specials here.";
			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, "The Black Sheep "
					+ temp_title);
			sendIntent.putExtra(
					Intent.EXTRA_TEXT,
					Html.fromHtml(temp_title1 + "<a href=\"" + share_url
							+ "\">" + share_url + "</a>"));
			sendIntent.setType("text/html");
			startActivity(Intent.createChooser(sendIntent, "Email:"
					+ temp_title));
			return true;

		case MENU_FB:

			Bundle parameters = new Bundle();
			parameters.putString("method", "stream.publish");
			parameters.putString("attachment", "{\"name\":\""
					+ "Check out these specials for " + temp_title + " on"
					+ "  " + mynote + ". " + share_desc2
					+ ". Check out more specials here." + "\",\"href\":\""
					+ share_url + "\",\"description\":\" \",}");

			fb_clicked = "clicked";
			authenticatedFacebook.dialog(BS_Bars.this, "stream.publish",
					parameters, new TestUiServerListener());

			return true;

		case MENU_TWITTER:
			String msg = "Check out these awesome specials for " + temp_title
					+ " Tonight! ";
			tweetMsg = msg;
			tweetMsg = tweetMsg + twitterShareLink;
			Log.e("The", "Twitter message to be shared=>" + msg);

			loginToTwitter();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}

	public void loginToTwitter() {

		initTwitterConfigs();
		try {
			mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
			// Access Token
			String access_token = mSharedPreferences.getString(
					PREF_KEY_OAUTH_TOKEN, "");
			// Access Token Secret
			String access_token_secret = mSharedPreferences.getString(
					PREF_KEY_OAUTH_SECRET, "");

			Log.e("The", "Acess Token is=>" + access_token);
			Log.e("The", "Acess Token secret is=>" + access_token_secret);
		} catch (Exception e) {

		}

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();

			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(null);

				/**
				 * Loading twitter login page on webview for authorization Once
				 * authorized, results are received at onActivityResult
				 * */
				final Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL,
						requestToken.getAuthenticationURL());
				startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {

			// Follow in twitter
			new updateTwitterStatus().execute(tweetMsg);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("Inside OnResume", "Method");
		if (rel_Alert.getVisibility() == View.VISIBLE) {
			rel_Alert.setVisibility(View.GONE);
		}
		temp = settings.getString("tagId", "No Data Found");
		Log.e("Temp value Inside OnResume", "" + temp);
		if (Utils.BARS_SHARE_VISIBILITY == false) {
			if (haveInternet(BS_Bars.this) == true) {
				lv_barlist.setVisibility(View.VISIBLE);
				br_details.setVisibility(View.GONE);
				new BarListTasks(BS_Bars.this, barsArray).execute();
			} else {
				Toast.makeText(BS_Bars.this,
						"Please Check your Intenet Connection",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(BS_Bars.this);
		EasyTracker.getInstance().activityStop(this);
	}

	// ::::::::::::::::::::::::::::::::::::::::: Facebook Codes
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			String verifier = data.getExtras().getString(oAuthVerifier);
			try {
				/* Getting oAuth authentication token */
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);

				/* Getting user id form access token */
				long userID = accessToken.getUserId();
				final User user = twitter.showUser(userID);
				final String username = user.getName();

				/* save updated token */
				saveTwitterInfo(accessToken);

				Toast.makeText(getApplicationContext(), "Twitter shared",
						Toast.LENGTH_SHORT).show();

				new updateTwitterStatus().execute(tweetMsg);

			} catch (Exception e) {
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}

		authenticatedFacebook.authorizeCallback(requestCode, resultCode, data);

	}

	class updateTwitterStatus extends AsyncTask<String, String, Void> {
		private final ProgressDialog dialog = new ProgressDialog(BS_Bars.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		protected Void doInBackground(String... args) {

			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(consumerKey);
				builder.setOAuthConsumerSecret(consumerSecret);

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);

				twitter4j.Status response = twitter.updateStatus(statusUpdate);

				Log.d("Status", response.getText());

			} catch (TwitterException e) {
				Log.d("Failed to post!", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			/* Dismiss the progress dialog after sharing */
			this.dialog.dismiss();

			Toast.makeText(BS_Bars.this, "Posted to Twitter!",
					Toast.LENGTH_SHORT).show();

		}

	}

	private void saveTwitterInfo(AccessToken accessToken) {

		long userID = accessToken.getUserId();

		User user;
		try {
			user = twitter.showUser(userID);

			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();

		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
	}

	public class TestUiServerListener implements DialogListener {
		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");
			if (postId != null) {
				new AsyncFacebookRunner(authenticatedFacebook).request(postId,
						new TestPostRequestListener());

			} else {

				BS_Bars.this.runOnUiThread(new Runnable() {
					public void run() {
					}
				});
			}
		}

		public void onCancel() {
		}

		public void onError(DialogError e) {
			e.printStackTrace();
		}

		public void onFacebookError(FacebookError e) {
			e.printStackTrace();
		}

	}

	public class TestPostRequestListener implements RequestListener {
		public void onComplete(final String response, final Object state) {
			try {
				fb_clicked = "";
				JSONObject json = Util.parseJson(response);
				String postId = json.getString("id");
				BS_Bars.this.runOnUiThread(new Runnable() {
					public void run() {
						// Log.d("Tests", "Testing wall post success");
					}
				});
			} catch (Throwable e) {
			}
		}

		public void onFacebookError(FacebookError e, final Object state) {
			e.printStackTrace();
		}

		public void onFileNotFoundException(FileNotFoundException e,
				final Object state) {
			e.printStackTrace();
		}

		public void onIOException(IOException e, final Object state) {
			e.printStackTrace();
		}

		public void onMalformedURLException(MalformedURLException e,
				final Object state) {
			e.printStackTrace();
		}
	}

	public class TestLoginListener implements DialogListener {
		public void onComplete(Bundle values) {
			if (authenticatedFacebook.isSessionValid() == true) {

			}
		}

		public void onCancel() {
		}

		public void onError(DialogError e) {
			e.printStackTrace();
		}

		public void onFacebookError(FacebookError e) {
			e.printStackTrace();
		}
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

}
