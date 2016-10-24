package com.eliqxir;

import java.util.Arrays;

import java.util.Collection;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.tabhostfragments.TabsFragmentActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.Utils;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.Facebook;
import com.twitter.TwitterApp;
import com.twitter.TwitterApp.TwDialogListener;

public class OrderPlacedActivity extends Activity implements OnClickListener {

	Button btnForClose;
	ImageButton backImg, cartBtn, btnSlideMenu;
	ImageView facebook, twitter;
	TextView textForHeader, txtorderId, txtStoreName, txtStoreAddress,
			txtDeliveryTime, txtDeliveryName, txtDeliveryAddress;
	Bundle b;
	// FOR FACEBOOK
	SharedPreferences fb_pref;
	SharedPreferences.Editor fb_editor;
	static String accesstoken = "0";

	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private TwitterApp mTwitter;

	String twt_auth_token = "0", twt_secret_token = "0", storeName,
			storeAddress, orderId, deliveryTime, deliveryDate, deliveryAddress,
			deliveryName, deliveryLastName;
	// AccessToken accessToken_twitter;
	SharedPreferences twitter_login_pref;
	SharedPreferences.Editor twt_login_editor;
	SharedPreferences customerPreference;
	ProgressDialog fb_dialog;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			startActivity(new Intent(OrderPlacedActivity.this,
					TabsFragmentActivity.class));

			finish();
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.final_screen);
		txtStoreName = (TextView) findViewById(R.id.name);
		txtStoreAddress = (TextView) findViewById(R.id.address1);
		txtDeliveryTime = (TextView) findViewById(R.id.today_label1);
		txtDeliveryName = (TextView) findViewById(R.id.to_name);
		txtDeliveryAddress = (TextView) findViewById(R.id.to_address1);
		twitter_login_pref = this.getSharedPreferences("TwitterPreference", 1);
		customerPreference = this.getSharedPreferences("customerPrefs",Context.MODE_PRIVATE);
		storeName = customerPreference.getString("store_name", "");
		storeAddress = customerPreference.getString("store_address", "");
		txtStoreName.setText(storeName);
		txtStoreAddress.setText(storeAddress);
		fb_pref = this.getSharedPreferences("FacebookPreference", 1);
		accesstoken = fb_pref.getString("fb_accesstoken", "0");
		twt_auth_token = twitter_login_pref.getString("token", "0");
		twt_secret_token = twitter_login_pref.getString("secret_token", "0");
		mTwitter = new TwitterApp(this, Constant.twitter_consumer_key,Constant.twitter_secret_key);

		mTwitter.setListener(mTwLoginDialogListener);
		facebook = (ImageView) findViewById(R.id.facebook);
		twitter = (ImageView) findViewById(R.id.twitter);
		facebook.setOnClickListener(this);
		twitter.setOnClickListener(this);
		txtorderId = (TextView) findViewById(R.id.order_no_value);
		fb_dialog = new ProgressDialog(OrderPlacedActivity.this);
		fb_dialog.setMessage("Loading...");
		fb_dialog.setCancelable(false);
		b = getIntent().getExtras();
		orderId = b.getString("order_id");
		deliveryDate = b.getString("delivery_date");
		deliveryTime = b.getString("delivery_time");
		deliveryAddress = b.getString("delivery_address");
		deliveryName = b.getString("delivery_name");
		deliveryLastName = b.getString("delivery_last_name");
		txtDeliveryTime.setText(deliveryDate + " " + deliveryTime);
		txtorderId.setText(orderId);
		txtDeliveryAddress.setText(deliveryAddress);
		txtDeliveryName.setText(deliveryName + " " + deliveryLastName);
		btnSlideMenu = (ImageButton) findViewById(R.id.btnSliderMenu);
		backImg = (ImageButton) findViewById(R.id.backBtn);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		btnForClose = (Button) findViewById(R.id.btn_close);
		btnForClose.setOnClickListener(this);
		btnSlideMenu.setVisibility(View.GONE);
		cartBtn.setVisibility(View.GONE);
		backImg.setVisibility(View.GONE);
		textForHeader.setText("CHECKOUT");
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null) {
			if (session == null) {
				session = new Session(this);
			}
			Session.setActiveSession(session);
		}

		clearCart();
	}

	private void clearCart() {
		Constant.cartArray.clear();
		DBAdapter db = new DBAdapter(OrderPlacedActivity.this);
		db.open();
		db.deleteRows();
		db.close();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_close:
			startActivity(new Intent(OrderPlacedActivity.this,
					TabsFragmentActivity.class));

			finish();
			break;
		case R.id.facebook:
			fbLogin();

			break;
		case R.id.twitter:			
			if(SessionStores.getAccessToken(OrderPlacedActivity.this)!=null){
				Log.e("Inside Twitter Login","Page");
				TwitterShareAlert();
			}else{
				Log.e("Inside Twitter Login","Else PArt");
				SessionStores.saveConsumerKey(Constant.twitter_consumer_key,OrderPlacedActivity.this);
				SessionStores.saveConsumerSecretKey(Constant.twitter_secret_key,OrderPlacedActivity.this);
				
				   Fragment login = new TwitterLoginActivity();
				   FragmentTransaction ft = getFragmentManager().beginTransaction();
				   ft.replace(R.id.content_frame, login);
				   ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				   ft.addToBackStack(null);
				   ft.commit();
				/*Intent intent=new Intent(OrderPlacedActivity.this,TwitterLoginActivity.class);
				startActivity(intent);	*/
			}
//			if (!twt_auth_token.equals("0") && !twt_secret_token.equals("0")) {
//				Log.e("twitter", "if");
//				TwitterShareAlert();
//			} else {
//				Log.e("twitter", "else");
//
//				onTwitterClick();
//			}
			break;
		}
	}

	private void fbLogin() {
		Session session = Session.getActiveSession();

		if (!session.isOpened() && !session.isClosed()) {
			Session.OpenRequest request = new Session.OpenRequest(this);
			request.setCallback(statusCallback);
			session.openForRead(request);
		} else {
			Session.openActiveSession(this, true, null);
			FacebookShareAlert(Constant.textToShare);
		}

	}

	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (!fb_dialog.isShowing()) {
				fb_dialog.show();
			}
			Log.e("SessionStatusCallback", "SessionStatusCallback");

			Request request = new Request(session, "/me", null, HttpMethod.GET,
					new Request.Callback() {
						@SuppressWarnings("deprecation")
						public void onCompleted(Response response) {
							fb_dialog.dismiss();
							Log.e("onCompleted", "onCompleted");

							try {
								FacebookRequestError error = response
										.getError();
								if (error == null) {
									Log.e("response", "" + response.toString());

									Session session = Session
											.getActiveSession();
									FacebookShareAlert(Constant.textToShare);
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

			request.executeAsync();

		}
	}

	@SuppressLint("InflateParams")
	public void FacebookShareAlert(final String fbMsg) {
		LayoutInflater li = LayoutInflater.from(OrderPlacedActivity.this);
		View promptsView = li.inflate(R.layout.prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				OrderPlacedActivity.this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);
		userInput.setText(fbMsg);
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Share",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result edit text
								publishTextOnWall(fbMsg);
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private void publishTextOnWall(String text) {
		if (!fb_dialog.isShowing()) {
			fb_dialog.show();
		}
		List<String> PERMISSIONS = Arrays.asList("publish_actions");

		Session session = Session.getActiveSession();

		if (session.isOpened()) {
			if (session != null) {
				// Check for publish permissions
				List<String> permissions = session.getPermissions();
				if (!isSubsetOf(PERMISSIONS, permissions)) {
					Log.e("fffffffffffff","ggggggggggggg");
					Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
							OrderPlacedActivity.this, PERMISSIONS);
					session.requestNewPublishPermissions(newPermissionsRequest);
					return;
				}

				Bundle postParams = new Bundle();
				postParams.putString("message", text);

				Log.e("publishTextOnWall", "SessionStatusCallback");

				Request.Callback callback = new Request.Callback() {

					public void onCompleted(Response response) {
						fb_dialog.dismiss();
						Log.e("response status share", "" + response.toString());

						FacebookRequestError error = response.getError();
						if (error != null) {
							Toast.makeText(
									OrderPlacedActivity.this
											.getApplicationContext(),
									error.getErrorMessage(), Toast.LENGTH_SHORT)
									.show();
						} else {

							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();
							String postId = null;
							try {
								postId = graphResponse.getString("id");
							} catch (JSONException e) {
								Log.i("Login", "JSON error " + e.getMessage());
							}
							Toast.makeText(
									OrderPlacedActivity.this
											.getApplicationContext(),
									"Published successfully ! ",
									Toast.LENGTH_LONG).show();
							Log.e("Posted Id", "" + postId);
						}
					}
				};

				Request request = new Request(session, "me/feed", postParams,
						HttpMethod.POST, callback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			}

		}

		else {
			Toast.makeText(OrderPlacedActivity.this.getApplicationContext(),
					"Login to share Status", Toast.LENGTH_SHORT).show();
		}

	}

	private void onTwitterClick() {
		if (mTwitter.hasAccessToken()) {
			Log.e("onTwitterClick", "if");
			mTwitter.resetAccessToken();
		} else {
			Log.e("onTwitterClick", "else");
			mTwitter.authorize();
		}
	}

	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		public void onComplete(String value) {
			String username = mTwitter.getUsername();
			username = (username.equals("")) ? "No Name" : username;
			TwitterShareAlert();

		}

		public void onError(String value) {

		}
	};

	public class PostToTwitter extends AsyncTask<Void, Void, Void> {

		String message, share_content;
		ProgressDialog dialog;

		public PostToTwitter(String text) {
			// TODO Auto-generated constructor stub
			share_content = text;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(OrderPlacedActivity.this);
			this.dialog.setMessage("Sharing..");
			this.dialog.show();
			this.dialog.setCancelable(false);
		}

		@Override
		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			ShowAlert(message);
		}

		@Override
		protected Void doInBackground(Void... params) {

			ConfigurationBuilder builder = new ConfigurationBuilder();
			try {
				builder.setOAuthConsumerKey(Constant.twitter_consumer_key);
				builder.setOAuthConsumerSecret(Constant.twitter_secret_key);
				twt_auth_token = twitter_login_pref.getString("token", "0");
				twt_secret_token = twitter_login_pref.getString("secret_token","0");
				Log.e("twt_auth_token inside share", twt_auth_token);
				Log.e("twt_secret_token inside share", twt_secret_token);

				AccessToken accessToken = new AccessToken(twt_auth_token,twt_secret_token);
				Log.e("AccessToken Inside 333333", "> " + accessToken);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
				Log.e("Share_content Inside 333333", "> " + share_content);
				share_content="Sample Tweet";
				twitter4j.Status response = twitter.updateStatus(share_content);
				Log.e("Status", "> " + response.getText());
				message = "Shared Successfully";
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				Log.e("Exception in Sharing Twitter 1111",""+e);
				message = "Sharing not successful";
			}

			return null;
		}

	}

	@SuppressLint("InflateParams")
	public void TwitterShareAlert() {
		LayoutInflater li = LayoutInflater.from(OrderPlacedActivity.this);
		View promptsView = li.inflate(R.layout.prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				OrderPlacedActivity.this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		userInput.setText(Constant.textToShare);
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Share",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result
								// edit text
//								new PostToTwitter(userInput.getText()
//										.toString()).execute();
								Log.e("Text To Senddddd",userInput.getText().toString());
								new TwitShareText(userInput.getText().toString(),OrderPlacedActivity.this).execute();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void ShowAlert(String msg) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				OrderPlacedActivity.this);

		TextView msg1 = new TextView(this);
		msg1.setText(msg);
		msg1.setPadding(10, 10, 10, 10);
		msg1.setGravity(Gravity.CENTER);
		msg1.setTextSize(18);
		alertDialogBuilder.setView(msg1);
		alertDialogBuilder.setCancelable(false);

		// set dialog message
		alertDialogBuilder.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();

					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}
