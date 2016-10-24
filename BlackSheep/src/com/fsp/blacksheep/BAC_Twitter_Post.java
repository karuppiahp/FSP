package com.fsp.blacksheep;


import com.fsp.blacksheep.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BAC_Twitter_Post extends Activity implements TextWatcher {
	private EditText post_et;
	static String log = "1";
	TextView twt_user;
	TextView twt_count, headerText;
	SharedPreferences settings1;
	public static final String PREFS_NAME1 = "BLACKSHEEP";
	public static SharedPreferences.Editor editor1;
	String twitter_post_link;
	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	Editor edit;

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG)
					.show();
		}
	};

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		setContentView(R.layout.bs_twitter_post);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.bs_header);

		headerText = (TextView) findViewById(R.id.header_text1);
		headerText.setText("POST ON TWITTER");

		settings1 = getSharedPreferences(PREFS_NAME1, 0);

		twitter_post_link = settings1.getString("tiny_url_twitter", "empty");

		prefs = PreferenceManager
				.getDefaultSharedPreferences(BAC_Twitter_Post.this);
		edit = prefs.edit();
		if (twitter_post_link.length() > 140) {
			twitter_post_link = twitter_post_link.substring(0, 137);
			twitter_post_link = twitter_post_link + "...";
		}
		twt_user = (TextView) findViewById(R.id.twitter_user_name);
		twt_count = (TextView) findViewById(R.id.twt_content_count);
		post_et = (EditText) findViewById(R.id.twt_post_content);
		post_et.setText(twitter_post_link);
		try {
//			twt_user.setText(TwitterUtils.user_Name);
		} catch (Exception e) {
			twt_user.setVisibility(View.GONE);
		}

		int length = twitter_post_link.length();
		int rem = 140 - length;
		twt_count.setText(Integer.toString(rem));
		post_et.addTextChangedListener(this);
		post_et.setSelection(length);
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				edit.commit();
				finish();
			}
		});
		findViewById(R.id.bac_twitter_post_post).setOnClickListener(
				new OnClickListener() {
					@SuppressWarnings("deprecation")
					public void onClick(View arg0) {
						sendTweet();
						finish();
					}
				});

	}

	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		int count = arg0.toString().length();
		int rem = 140 - count;
		twt_count.setText(Integer.toString(rem));

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		int count1 = s.toString().length();
		int rem1 = 140 - count1;
		twt_count.setText(Integer.toString(rem1));
		int limit = s.toString().length();
		if (limit > 140) {
			post_et.setFocusable(false);
		} else {
			post_et.setFocusable(true);
		}
	}

	public void sendTweet() {
		//Log.v("Inside Tweet sendTweet", "Inside Tweet sendTweet");
		Thread t = new Thread() {
			public void run() {

				try {
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	private String getTweetMsg() {
		// return "Tweeting from Android App at " + new Date().toLocaleString();
		return twitter_post_link;
	}

}
