package com.fsp.blacksheep;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;

public class BS_Splash extends Activity {
	private int Splash_Time_Limit = 3000;

	/** Called when the activity is first created. */
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	String status = null; // Status Checking Variable
	long milliseconds = 10;

	String device_id = null;
	String response = null;
	String email_id, bday, school_ID;
	public static final String PREFS_NAME = "MyPrefsFile";
	static SharedPreferences settings;
	SharedPreferences.Editor editor;
	// GoogleAnalyticsTracker tracker;
	int screenWidth, screenHeight;

	private Cursor cursor;

	@Override
	public void onStart() {
		super.onStart();

		Log.i("Splash onStart", " entered");

		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(BS_Splash.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("BlackSheepMobile Android Application Started");

		// GoogleAnalytics.getInstance(BS_Splash.this).reportActivityStart(this);

		EasyTracker.getInstance().setContext(BS_Splash.this);
		EasyTracker.getInstance().activityStart(this);

		// tracker = GoogleAnalyticsTracker.getInstance();
		//
		// // Start the tracker in manual dispatch mode...
		// // tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1",this);
		// tracker.trackPageView("/BS_Splash");
		/*
		 * Context mCtx = this; // Get current context. GoogleAnalytics
		 * myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * 
		 * myInstance.setDebug(true); Tracker myNewTracker =
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
		setContentView(R.layout.bs_splash);

		Log.i("Splash onCreate", " entered");

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			Window window = BS_Splash.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(BS_Splash.this.getResources().getColor(
					R.color.background));
		}
		settings = getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();

		TelephonyManager telemngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		device_id = telemngr.getDeviceId();
		settings = getSharedPreferences(PREFS_NAME, 0);
		email_id = settings.getString("email", "");
		bday = settings.getString("birthday", "");

		school_ID = settings.getString("tagId", "");
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (haveInternet(BS_Splash.this) == true) {
					check_device_status();
				} else {
					Toast.makeText(BS_Splash.this,
							"Please Check your Intenet Connection",
							Toast.LENGTH_SHORT).show();
				}
			}
		}, Splash_Time_Limit);
	}

	public static String convertDate(String dateInMilliseconds,
			String dateFormat) {
		return DateFormat
				.format(dateFormat, Long.parseLong(dateInMilliseconds))
				.toString();
	}

	protected void check_device_status() {
		// TODO Auto-generated method stub

		if (school_ID.equals("")) {
			startActivity(new Intent(BS_Splash.this, Registration.class));
			BS_Splash.this.finish();
		} else {
			Intent obj_intent;
			Bundle b = new Bundle();
			b.putString("key1", "HomePage");
			obj_intent = new Intent(BS_Splash.this, BS_Main.class);
			obj_intent.putExtras(b);
			startActivity(obj_intent);
			BS_Splash.this.finish();
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

		FlurryAgent.onEndSession(BS_Splash.this);
		EasyTracker.getInstance().activityStop(this);

		// GoogleAnalytics.getInstance(BS_Splash.this).reportActivityStop(this);

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