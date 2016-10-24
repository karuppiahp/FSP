package com.fsp.blacksheep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.fsp.blacksheep.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class BS_Info extends Activity {
	TextView tv, web_text;
	// Button imgvw;
	Button back;
	WebView webvw;
	boolean san_netcheck = false;

	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;

	// GoogleAnalyticsTracker tracker;
	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(BS_Info.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("BlackSheepMobile Info Started");
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);
		// tracker = GoogleAnalyticsTracker.getInstance();
		//
		// // Start the tracker in manual dispatch mode...
		// // tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1", this);
		// tracker.trackPageView("/BS_Info");
		/*
		 * Context mCtx = this; // Get current context. myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * myInstance.setDebug(true); myNewTracker =
		 * myInstance.getTracker("UA-36318648-1");
		 * myInstance.setDefaultTracker(myNewTracker);
		 * myNewTracker.trackView("BlackSheepMobile Info Started");
		 */
	}

	@Override
	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(BS_Info.this);
		EasyTracker.getInstance().activityStop(this);
		// myNewTracker.close();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bs_info);

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bs_header);

		tv = (TextView) findViewById(R.id.header_text1);
		tv.setText("Black Sheep");
		web_text = (TextView) findViewById(R.id.web);

		web_text.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (san_netcheck == true) {
					displayAlert();
				} else {
					startActivity(new Intent(BS_Info.this, Web_Info.class));
				}
				return false;
			}
		});

		findViewById(R.id.mail).setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				sendIntent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { " mHelp@theblacksheeponline.com" });
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						"Sent From My Android Mobile");
				sendIntent.setType("vnd.android.cursor.dir/email");
				startActivity(Intent.createChooser(sendIntent, "Email:"));
			}
		});
	}

	public boolean netCheck() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			// notify user you are not online
			san_netcheck = true;
		}
		return san_netcheck;
	}

	public void displayAlert() {
		new AlertDialog.Builder(this)
				.setMessage(
						"Please Check Your Internet Connection and Try Again")
				.setTitle("Network Error")
				.setCancelable(true)
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
