package com.fsp.blacksheep;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class Rss_Feed_Grid extends Activity {

	String TAG = "Rss_Feed_Grid";
	String name, responsedata = null;
	private Bundle b;
	String title;
	private static final int MODE_WORLD_WRITABLE = 2;
	static int tab_pos = 0;
	static SharedPreferences mPref;
	static boolean first_check = false;
	public boolean san_netcheck = false;
	static SharedPreferences settings;
	static String school_id, school_name;
	public static final String PREFS_NAME = "MyPrefsFile";
	protected static boolean register = true;
	String device_id = "";
	String url = null;

	/** Called when the activity is first created. */
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;
	int screenWidth, screenHeight;
	TextView drink_special, drink_game, read_Articles, Share_Love;
	PopupWindow pw;
	Display display;
	int scr_width, scr_height;
	ArrayList<String> twitter_account = new ArrayList<String>();
	private SharedPreferences prefs;
	public static final String TWT_PREFS_NAME = "TWTBLACKSHEEP";
	SharedPreferences twt_pref;
	String twt_user;
	public static String user_to_follow;
	public String twt_foll_usr = "";
	String appid;
	Bundle b1;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	static String accesstoken;
	public static String APP_ID = "242878212411329";
	public static Facebook authenticatedFacebook = new Facebook(APP_ID);
	static SharedPreferences notify_Settings;
	public static SharedPreferences.Editor editor;
	protected static String access;
	public static final String FBPREFS_NAME = "BS_FB";
	private static final String[] PERMISSIONS = new String[] {
			"friends_birthday", "friends_about_me", "friends_photos ",
			"user_birthday", "publish_stream", "read_stream", "offline_access",
			"user_location", "user_photos", "email", "publish_checkins" };
	View parent;
	IntentFilter boundServiceFilter;
	public static ProgressDialog dialog;
	MyCount counter = new MyCount(5000, 2000);
	SharedPreferences pushPrefs;
	// GoogleAnalyticsTracker tracker;

	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;
	ArrayList<String> fri_id = new ArrayList<String>();
	ArrayList<String> fri_name = new ArrayList<String>();

	private static SharedPreferences mSharedPreferences;
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

	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(Rss_Feed_Grid.this, bs_api);
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

	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(Rss_Feed_Grid.this);
		EasyTracker.getInstance().activityStop(this);
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_home);

		twitter_account.clear();
		b = getIntent().getExtras();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		pushPrefs = this.getSharedPreferences("pushPrefs", MODE_WORLD_READABLE);
		appid = pushPrefs.getString("app_id", "nothing");
		// Log.v("app id in preference >>>",appid);
		display = getWindowManager().getDefaultDisplay();
		myPrefs = Rss_Feed_Grid.this.getSharedPreferences("myPrefs",
				MODE_WORLD_READABLE);
		settings = getSharedPreferences(PREFS_NAME, 0);
		twt_pref = getSharedPreferences(TWT_PREFS_NAME, 0);
		twt_user = twt_pref.getString("twt_uname", "no user found");
		scr_width = display.getWidth();
		scr_height = display.getHeight();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		twitter_account.add("Michigan" + "-" + "BlackSheepUM");
		twitter_account.add("West Virginia" + "-" + "BlackSheepWVU");
		twitter_account.add("Louisiana State" + "-" + "theblacksheep99");
		twitter_account.add("Kentucky" + "-" + "theblacksheep99");
		twitter_account.add("South Carolina" + "-" + "BlackSheepUSC");
		twitter_account.add("Penn State" + "-" + "theblacksheep99");
		twitter_account.add("Virginia" + "-" + "theblacksheepUVA");
		twitter_account.add("VCU" + "-" + "blacksheep_vcu");
		twitter_account.add("Tennessee" + "-" + "theblacksheep99");
		twitter_account.add("Illinois" + "-" + "blacksheep_uiuc");
		twitter_account.add("Michigan State" + "-" + "MSUBlacksheep");
		twitter_account.add("Illinois State" + "-" + "blacksheep_isu");
		twitter_account.add("Indiana University" + "-" + "Blacksheep_IU");
		twitter_account.add("Georgia" + "-" + "BlackSheep_UGA");
		twitter_account.add("Ole Miss" + "-" + "Blacksheep_OM");
		twitter_account.add("Delaware" + "-" + "TheBlackSheepUD");
		twitter_account.add("Minnesota" + "-" + "blacksheep_umn");
		twitter_account.add("Virginia Tech" + "-" + "Blacksheep_VT");
		twitter_account.add("Florida State" + "-" + "Blacksheepfsu");
		twitter_account.add("Florida" + "-" + "theblacksheep99");
		twitter_account.add("Clemson" + "-" + "Blacksheep_CLEM");
		twitter_account.add("Iowa State" + "-" + "TBS_IowaState");
		twitter_account.add("Western Michigan" + "-" + "black_sheep_wmu");

		//
		// Arkansas: Blacksheep_UofA
		// Depaul: Blacksheep_DPU
		// ECSU: Blacksheep_ECSU
		// GVSU: TheBlackSheepGV
		// Miami: Blacksheep_MU
		// Ohio state: Blacksheep_OSU
		// Pitt: Blacksheep_Pitt
		// Purdue: Blacksheep_PU
		// Berkely: Blacksheep_UCB
		// Uconn: Blacksheep_UC
		// Wisconsin: Blacksheep_UW

		// Arkansas
		// DePaul University
		// Eastern Connecticut State University
		// Grand Valley State
		// Miami University (OH)
		// Ohio State
		// Pittsburgh
		// Purdue
		// UC Berkeley
		// UConn
		// Wisconsin

		// new one 23 june 2015
		twitter_account.add("Arkansas" + "-" + "Blacksheep_UofA");
		twitter_account.add("DePaul University" + "-" + "Blacksheep_DPU");
		twitter_account.add("Eastern Connecticut State University" + "-"
				+ "Blacksheep_ECSU");
		twitter_account.add("Grand Valley State" + "-" + "TheBlackSheepGV");
		twitter_account.add("Miami University (OH)" + "-" + "Blacksheep_MU");
		twitter_account.add("Ohio State" + "-" + "Blacksheep_OSU");
		twitter_account.add("Pittsburgh" + "-" + "Blacksheep_Pitt");
		twitter_account.add("Purdue" + "-" + "Blacksheep_PU");
		twitter_account.add("UC Berkeley" + "-" + "Blacksheep_UCB");
		twitter_account.add("UConn" + "-" + "Blacksheep_UC");
		twitter_account.add("Wisconsin" + "-" + "Blacksheep_UW");

		try {
			TelephonyManager telemngr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			device_id = telemngr.getDeviceId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (device_id.equals("")) {
		} else {
			String response = getResponse();
		}

		int a = R.id.bs_info;

		drink_special = (TextView) findViewById(R.id.img_dinks_spl);
		drink_game = (TextView) findViewById(R.id.img_drink_games);
		read_Articles = (TextView) findViewById(R.id.img_read_Articles);

		Typeface type = Typeface.createFromAsset(getAssets(),
				"Mission Gothic Regular.otf");
		drink_special.setTypeface(type);
		drink_game.setTypeface(type);
		read_Articles.setTypeface(type);
		drink_special.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// startActivity(new Intent(Rss_Feed_Grid.this,BS_Bars.class));
				try {
					dialog = ProgressDialog.show(Rss_Feed_Grid.this, "",
							"Loading....", true, false);
					counter.start();
					Intent obj_intent;
					b = new Bundle();
					b.putString("key1", "Bars");
					obj_intent = new Intent(Rss_Feed_Grid.this, BS_Main.class);
					obj_intent.putExtras(b);
					startActivity(obj_intent);
				} catch (Exception e) {
					Log.e("Barrrrrrrrrrr", "" + e);
				}
			}
		});

		drink_game.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					dialog = ProgressDialog.show(Rss_Feed_Grid.this, "",
							"Loading....", true, false);
					counter.start();
					Intent obj_intent;
					b = new Bundle();
					b.putString("key1", "games");
					obj_intent = new Intent(Rss_Feed_Grid.this, BS_Main.class);
					obj_intent.putExtras(b);
					startActivity(obj_intent);
				} catch (Exception e) {
					Log.e("Gamesssssssss", "" + e);
				}

			}
		});

		read_Articles.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					dialog = ProgressDialog.show(Rss_Feed_Grid.this, "",
							"Loading....", true, false);
					counter.start();
					Intent obj_intent;
					b = new Bundle();
					b.putString("key1", "pictures");
					obj_intent = new Intent(Rss_Feed_Grid.this, BS_Main.class);
					obj_intent.putExtras(b);
					startActivity(obj_intent);
				} catch (Exception e) {
					Log.e("Articlessssssssssss", "" + e);
				}
			}
		});
		mPref = Rss_Feed_Grid.this.getPreferences(MODE_WORLD_WRITABLE);
	}

	/* Reading twitter essential configuration parameters from strings.xml */
	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}

	private void loginToTwitter() {
		initTwitterConfigs();
		try {

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
				// requestToken = twitter.getOAuthRequestToken(callbackUrl);
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
			Log.e("The", "user to follow=>" + user_to_follow);
			TwitterFollow(user_to_follow);
		}
	}

	public void TwitterFollow(String user_to_follow) {

		String access_token = mSharedPreferences.getString(
				PREF_KEY_OAUTH_TOKEN, "");
		// Access Token Secret
		String access_token_secret = mSharedPreferences.getString(
				PREF_KEY_OAUTH_SECRET, "");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(access_token)
				.setOAuthAccessTokenSecret(access_token_secret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		try {

			twitter.createFriendship(user_to_follow);
			Toast.makeText(getApplicationContext(),
					"You are now following The Black Sheep on Twitter!",
					Toast.LENGTH_SHORT).show();

		} catch (TwitterException e) {

			Log.e("The", "Exception is=>" + e);

			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

				Log.e("The", "user to follow=>" + user_to_follow);

				TwitterFollow(user_to_follow);

			} catch (Exception e) {
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
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

	public String followersTwitterFeed(String id) {

		int followers_count = fri_id.size();
		// Log.v("followers_count >>",Integer.toString(followers_count));
		HttpGet httpGet1 = null;
		int i = 0;
		StringBuilder builder1 = new StringBuilder();
		HttpClient client1 = new DefaultHttpClient();

		// changed
		// httpGet1 = new
		// HttpGet("http://api.twitter.com/1/users/show.json?user_id="+id);
		httpGet1 = new HttpGet(ParserClass.rssFeedOne + "" + id);

		try {
			HttpResponse response1 = client1.execute(httpGet1);
			StatusLine statusLine = response1.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response1.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content), 8192);
				String line;
				while ((line = reader.readLine()) != null) {
					builder1.append(line);
				}
			} else {
				Log.v("in else friend details", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "[" + builder1.toString() + "]";

	}

	public String readTwitterFeed(String url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content), 8192);
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.v("in else of friend ids", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "[" + builder.toString() + "]";
	}

	public String getResponse() {
		HttpClient client = new DefaultHttpClient();

		HttpResponse response;
		HttpPost post = null;
		String ret = null;
		JSONObject json = new JSONObject();
		try {
			// changed
			// post = new HttpPost(
			// "http://104.130.66.20/mobile_new/apid_register.php?");
			post = new HttpPost(ParserClass.rssFeedTwo);

			json.put("uid", device_id);
			// Log.v("appid in rss_feed >>>",appid);
			json.put("apid", appid);
			json.put("mob", "newandroid");

			post.setHeader("Content-Type", "application/json");
			post.setHeader("Accept", "application/json");
			StringEntity se = new StringEntity(json.toString());
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);
			ret = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();

		}
		return ret;
	}

	class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		public void onFinish() {

			dialog.dismiss();
			finish();
		}

		public void onTick(long millisUntilFinished) {

			// TODO Auto-generated method stub
		}
	}

	public class getTwitUser extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				Rss_Feed_Grid.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private class LogoutRequestListener implements RequestListener {

		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub

			// Only the original owner thread can touch its views
			Rss_Feed_Grid.this.runOnUiThread(new Runnable() {
				public void run() {

				}
			});

		}

		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub

		}

		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub

		}

		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub

		}

		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub

		}

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

	public static boolean haveInternet(Context ctx) {
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}

	public class LikeWebviewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}