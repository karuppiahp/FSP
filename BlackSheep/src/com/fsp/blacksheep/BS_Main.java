package com.fsp.blacksheep;

import com.flurry.android.FlurryAgent;
import com.fsp.blacksheep.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class BS_Main extends TabActivity {

	/**
	 * Variables & Objects Declaration
	 * 
	 */
	Activity a;
	private TabHost tabHost;
	Setting1 setting;
	private String title, temp_stat, temp_stat1, temp_stat_info;
	static ImageView bs_share;
	int bar_back = 0;
	public static ProgressDialog dialog;
	String[] VAL1, VAL2, VAL3, bs_tagname, bs_id, bname, mycampus_name;
	String device_id = null;
	Button button;
	int passVal = 0, jump, i = -1, y = 0, j = -1, k = -1, pos,
			url_startcount = 1;
	Bundle b;

	HorizontalScrollView hv;
	boolean bs_check = false;
	MyCount counter = new MyCount(3000, 1000);
	MyCount testcounter = new MyCount(5000, 1000);
	boolean checkfooter = false, bs_flag = false, bs_netcheck = false,
			enablemycampus = false;
	static String link_val = " ", desc_val = " ";
	String url, flyurl, flyurl1, bstemp, oldurl;
	static String school_id, school_value;

	private Handler handler = new Handler();
	String def_val = "15";
	boolean bs_checknew = false, footer_click = false,
			navagiation_check = false, mycampustabcheck = false;
	static boolean dev_check = false;
	Configuration confi;
	Handler mHandler; // For FB
	Button bs_nextpage;
	static int screenWidth = 0;
	static int screenHeight = 0;
	String query_string = " ";
	String arrange_val = " ";

	public static final String PREFS_NAME = "MyPrefsFile";
	static SharedPreferences settings;

	private static Context CONTEXT;

	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;

	// GoogleAnalyticsTracker tracker;
	/*
	 * (non-Javadoc) Invoking Flurry on the Start()
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(BS_Main.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("BlackSheepMobile Android Application Started");

		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);
		// tracker = GoogleAnalyticsTracker.getInstance();

		// Start the tracker in manual dispatch mode...
		// tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1", 20,this);
		// tracker.trackPageView("/BS_Info");
		/*
		 * Context mCtx = this; // Get current context. GoogleAnalytics
		 * myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * myInstance.setDebug(true); Tracker myNewTracker =
		 * myInstance.getTracker("UA-36318648-1");
		 * myInstance.setDefaultTracker(myNewTracker);
		 * myNewTracker.trackView("BlackSheepMobile Android Application Started"
		 * );
		 */

	}

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		String str_ScreenSize = dm.widthPixels + " x " + dm.heightPixels;
		str_ScreenSize = "dd" + " x " + dm.heightPixels;

		if (screenWidth == 480 && screenHeight == 800) {
			Log.e("ggggg", "gggggggg");
			setContentView(R.layout.main_large);
		} else {
			Log.e("ggggg", "jjjjjjjjjj");
			setContentView(R.layout.main);
		}

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			Window window = BS_Main.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(BS_Main.this.getResources().getColor(
					R.color.background));
		}
		CONTEXT = this;
		a = BS_Main.this;
		// End of Standard Code
		settings = getSharedPreferences(PREFS_NAME, 0);
		BS_Main.school_id = settings.getString("tagId", "No Data Found");
		BS_Main.school_value = settings.getString("school", "No Data Found");
		// vm.clear(); // clearing the Singleton Object........
		hv = (HorizontalScrollView) findViewById(R.id.gv);
		TelephonyManager telemngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		device_id = telemngr.getDeviceId();
		if (screenWidth >= 480 && screenHeight >= 640) {

		}

		else {
			// header_text.setTextSize(12);
		}

		//
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int density = metrics.densityDpi;
		Log.e("Density Value", "" + density);
		//

		tabHost = getTabHost();
		b = getIntent().getExtras();
		title = b.getString("key1");
		Log.v("Tag", title);

		// New changes
		if (title.equalsIgnoreCase("HomePage")) {

			tabHost.setCurrentTab(1);
		}
		// --
		if (title.equalsIgnoreCase("bars")) {
			Log.e("Inside Bars click", "Page");
			tabHost.setCurrentTab(2);
		} else if (title.equalsIgnoreCase("pictures")) {

			tabHost.setCurrentTab(3);
		}

		tabHost.addTab(tabHost
				.newTabSpec("tab5")
				.setContent(new Intent(this, Rss_Feed_Grid.class))
				.setIndicator(
						"",
						getResources()
								.getDrawable(R.drawable.home_tab_selector)));
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setContent(new Intent(this, BS_Bars.class))
				.setIndicator("",
						getResources().getDrawable(R.drawable.bar_tab_selector)));
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setContent(new Intent(this, Articles.class))
				.setIndicator(
						"",
						getResources()
								.getDrawable(R.drawable.pics_tab_selector)));
		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setContent(new Intent(this, DrinkGames.class))
				.setIndicator(
						"",
						getResources()
								.getDrawable(R.drawable.game_tab_selector)));
		tabHost.addTab(tabHost
				.newTabSpec("tab4")
				.setContent(new Intent(this, Setting1.class))
				.setIndicator(
						"",
						getResources().getDrawable(
								R.drawable.settings_tab_selector)));

		tabHost.getTabWidget().getChildAt(0)
				.setBackgroundResource(R.drawable.tab_back);
		tabHost.getTabWidget().getChildAt(1)
				.setBackgroundResource(R.drawable.tab_back);
		tabHost.getTabWidget().getChildAt(2)
				.setBackgroundResource(R.drawable.tab_back);
		tabHost.getTabWidget().getChildAt(3)
				.setBackgroundResource(R.drawable.tab_back);
		tabHost.getTabWidget().getChildAt(4)
				.setBackgroundResource(R.drawable.tab_back);
		b = getIntent().getExtras();
		title = b.getString("key1");
		Log.e("Tag", title);

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {

			if (density == DisplayMetrics.DENSITY_HIGH) {
				Log.e("Inside HDPI", "Devices Value");
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 80;
				tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);

			} else if (density == DisplayMetrics.DENSITY_MEDIUM) {
				Log.e("Inside MDPI", "Devices Value");
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 60;
				tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);

			} else if (density == DisplayMetrics.DENSITY_LOW) {
				Log.e("Inside LDPI", "Devices Value");
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
				tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);

			} else if (density == DisplayMetrics.DENSITY_XHIGH) {
				Log.e("Inside XHDPI", "Devices Value");
				tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 110;
				tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, 0);
			} else {
				Log.e("Inside Others", "Devices Value");
				for (int j = 0; j < tabHost.getTabWidget().getChildCount(); j++) {
					tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 150;
					tabHost.getTabWidget().getChildAt(j).setPadding(0, 0, 0, 0); // .setPadding(10,
																					// 20,
																					// 10,
																					// 20);
				}
			}
		}

		if (title.equalsIgnoreCase("HomePage")) {
			tabHost.setCurrentTab(0);
			bs_flag = true;
			Log.e("Home Page", "Home");

			if (bs_netcheck == true) {
				displayAlert();
			}
		}

		if (title.equalsIgnoreCase("bars")) {
			tabHost.setCurrentTab(1);
			bs_flag = true;
			Log.e("Bsmain 55555555555", "Bars");

			if (bs_netcheck == true) {
				displayAlert();
			}
		} else if (title.equalsIgnoreCase("games")) {
			tabHost.setCurrentTab(3);
			bs_flag = true;
			if (bs_netcheck == true) {
				displayAlert();
			} else {
			}
		} else if (title.equalsIgnoreCase("pictures")) {
			tabHost.setCurrentTab(2);
			bs_flag = true;
			if (bs_netcheck == true) {
				displayAlert();
			} else {

			}
		}
		temp_stat = "lv";
		temp_stat_info = "info";

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			public void onTabChanged(String arg0) {
				Bundle b = new Bundle();

				Utils.ARTICLES_SHARE_VISIBILITY = false;
				Utils.DRINKING_GAMES_SHARE_VISIBILITY = false;
				Utils.BARS_SHARE_VISIBILITY = false;

				if (bs_netcheck == true) {
					displayAlert();
				}

				else if (arg0.equals("tab1")) {
					Log.e("Tag Value 8888888888", "TAB2");
				} else if (arg0.equals("tab2")) {
				} else if (arg0.equals("tab3")) {
					Log.e("Tag", "TAB4");

				} else if (arg0.equals("tab4")) {
					Log.e("clicked tab4 settings >>", "tab4");
					tabHost.setCurrentTab(4);
				}

				else if (arg0.equals("tab5")) {
				}

			}

		});

		tabHost.getTabWidget().getChildAt(4)
				.setOnTouchListener(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						// Log.v("Picture_ONTabchange", "Tab4");
						tabHost.setCurrentTab(4);
						temp_stat1 = "imgv";
						if (temp_stat_info.equals("info")) {

						} else if (temp_stat_info.equals("wv_site")) {

							if (tabHost.getCurrentTab() == 4) {
								temp_stat_info = "picture";
							}
						}
						return false;
					}
				});

	}

	public boolean netCheck() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			// notify user you are not online
			bs_netcheck = true;
		}
		return bs_netcheck;
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

	public void displayPopUpAlertBars() {

		final TextView message = new TextView(BS_Main.this);
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

	public void notificationAlert(String msg) {
		new AlertDialog.Builder(getApplicationContext())
				.setMessage(msg)
				.setTitle("Push Notification Message")
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	public void displayPopUpAlert() {
		final TextView message = new TextView(BS_Main.this);
		final SpannableString s = new SpannableString(
				"We currently don’t have articles for your school. Email mhelp@theblacksheeponline.com to learn how to bring them to your campus.");
		Linkify.addLinks(s, Linkify.ALL);
		message.setText(s);
		message.setLinkTextColor(Color.WHITE);
		message.setMovementMethod(LinkMovementMethod.getInstance());
		new AlertDialog.Builder(this)
				.setView(message)
				.setTitle("Articles")
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	public static void displayParsingAlert() {
		new AlertDialog.Builder(CONTEXT)
				.setMessage(
						"Please Check Your Internet Connection and Try Again")
				.setTitle("Network Error")
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								System.exit(0);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(BS_Main.this);
		EasyTracker.getInstance().activityStop(this);
	}
}
