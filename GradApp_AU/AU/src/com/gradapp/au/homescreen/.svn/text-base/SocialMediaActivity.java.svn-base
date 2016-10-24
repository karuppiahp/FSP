package com.gradapp.au.homescreen;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.twitter.TwitShareText;
import com.gradapp.au.twitter.TwitterLoginActivity;
import com.gradapp.au.twitter.TwitterTasks;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SocialMediaActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack, btnForTweet;
	TextView textForHeader, textForMyScheduleAnim;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon, layoutForBottom;
	ListView listView;
	float density;
	RelativeLayout.LayoutParams layout_description, paramsCommencement;
	EditText editTxtForTwet;
	ArrayList<HashMap<String, String>> schoolIdArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> hashTagArray = new ArrayList<HashMap<String, String>>();
	int paging_count = 0;
	ProgressDialog progress;
	Dialog dialog;

	float x1, x2;
	float y1, y2;
	boolean touch = false;

	ProgressDialog progressDialog;

	Runnable runnable;
	boolean reload = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_media);

		initUI();
	}

	public void initUI() {
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");
		progressDialog = new ProgressDialog(this);

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForSocialMediaFull);
		listView = (ListView) findViewById(R.id.listForTweets);
		editTxtForTwet = (EditText) findViewById(R.id.editTxtForTweets);
		btnForTweet = (ImageButton) findViewById(R.id.imgBtnForTwitter);
		layoutForBottom = (RelativeLayout) findViewById(R.id.layoutforSocialMediaBottom);
		textForMyScheduleAnim = (TextView) findViewById(R.id.txtForMySchedule);
		textForHeader.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnForTweet.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForUnivIcon.setVisibility(View.GONE);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		textForMyScheduleAnim.setTypeface(typeFace);
		editTxtForTwet.setOnClickListener(this);
		editTxtForTwet.setFocusable(false);

		density = getResources().getDisplayMetrics().density;

		//EditText keyboard focaus has been checked
		editTxtForTwet.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (v == editTxtForTwet) {
					if (hasFocus) {
						// open keyboard
						((InputMethodManager) getBaseContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE))
								.showSoftInput(editTxtForTwet,
										InputMethodManager.SHOW_FORCED);
					} else { // close keyboard
						((InputMethodManager) getBaseContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(
										editTxtForTwet.getWindowToken(), 0);
					}
				}
			}
		});

		Utils.hideKeyboard(SocialMediaActivity.this);
		//Paging_count has been send as params in twitter api.
		paging_count = paging_count + 1;
		if (SessionStores.getAccessToken(getBaseContext()) != null) {
			dialog = new Dialog(SocialMediaActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.filters_options);
			dialog.setCanceledOnTouchOutside(false);

			ListView listViewDialog = (ListView) dialog
					.findViewById(R.id.listOfFiltersOption);
			if (Utils.isOnline()) {
				//TwitterTasks has been called to fetch twitter feeds from api
				new TwitterTasks(SocialMediaActivity.this, listView,
						paging_count,
						SessionStores.getSchoolId(SocialMediaActivity.this),
						progressDialog, listViewDialog, dialog, hashTagArray,
						editTxtForTwet).execute();
			} else {
				Toast("Internet connection is not available");
			}
		} else {
			//Alert shows as if user doesn't login in twitter.
			AlertWindow();
			Utils.hideKeyboard(SocialMediaActivity.this);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			((InputMethodManager) getBaseContext().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					editTxtForTwet.getWindowToken(), 0);
			showSecondaryMenu();
		}

		if (v.getId() == R.id.editTxtForTweets) {
			setEditTextFocus(true);
		}

		//To share the text which is entered in edittext box
		if (v.getId() == R.id.imgBtnForTwitter) {
			if (SessionStores.getAccessToken(getBaseContext()) != null) {
				if (Constant.hashTagValue.length() > 0) {
					if (editTxtForTwet.getText().toString().length() > 0) {
						((InputMethodManager) getBaseContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(
										editTxtForTwet.getWindowToken(), 0);
						//TwitterShareText class is called to share the text in twitter homeline
						new TwitShareText(this, editTxtForTwet.getText()
								.toString() + " " + Constant.hashTagValue,
								listView, paging_count, Constant.hashTagValue,
								editTxtForTwet, hashTagArray).execute();
						editTxtForTwet.setText("");
						reload = false;
					} else {
						Toast.makeText(getBaseContext(),
								"TextField must not be empty",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getBaseContext(),
							"HashTag is not found to share text",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getBaseContext(),
						"Please login through twitter", Toast.LENGTH_SHORT)
						.show();
			}
		}

		if (v.getId() == R.id.btnSliderMenu) {
			Intent intentToHome = new Intent(SocialMediaActivity.this,
					HomeActivity.class);
			startActivity(intentToHome);
			((InputMethodManager) getBaseContext().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					editTxtForTwet.getWindowToken(), 0);
			finish();
		}

		if (v.getId() == R.id.btnForAlertCancel) {
			dialog.dismiss();
			Utils.hideKeyboard(SocialMediaActivity.this);
		}

		//Alert ok button clicks it redirects to twitter login screen
		if (v.getId() == R.id.btnForAlertOk) {
			dialog.dismiss();
			Utils.hideKeyboard(SocialMediaActivity.this);
			Constant.twitterLogin = "socialMedia";
			SessionStores.saveConsumerKey(Constant.consumer_key,
					SocialMediaActivity.this);
			SessionStores.saveConsumerSecretKey(Constant.secret_key,
					SocialMediaActivity.this);

			Fragment login = new TwitterLoginActivity();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.content_frame, login);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();
		}
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	private void AlertWindow() {
		dialog = new Dialog(SocialMediaActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);

		TextView textForAlert = (TextView) dialog
				.findViewById(R.id.textForAlert);
		TextView textForCancel = (TextView) dialog
				.findViewById(R.id.btnForAlertCancel);
		TextView textForOk = (TextView) dialog.findViewById(R.id.btnForAlertOk);
		textForAlert.setText("Please connect to your Twitter account to post");
		textForCancel.setOnClickListener(this);
		textForOk.setOnClickListener(this);
		dialog.show();
	}

	public void setEditTextFocus(boolean isFocused) {
		editTxtForTwet.setCursorVisible(isFocused);
		editTxtForTwet.setFocusable(isFocused);
		editTxtForTwet.setFocusableInTouchMode(isFocused);

		if (isFocused) {
			editTxtForTwet.requestFocus();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intentToHome = new Intent(SocialMediaActivity.this,
				HomeActivity.class);
		startActivity(intentToHome);
		finish();
	}
}
