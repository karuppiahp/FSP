package com.gradapp.au.hamburger.menus;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.SurveyTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.HomeActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SurveyActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textSurveyHeader, textForQues;
	ListView listView;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon;
	float density;
	RelativeLayout.LayoutParams layout_description;
	ArrayList<HashMap<String, String>> surveyArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> schoolIdArray = new ArrayList<HashMap<String, String>>();
	float x1, x2;
	float y1, y2;
	boolean touch = false;
	Handler h;
	Runnable runnable;
	Dialog dialog;
	ProgressDialog progressDialog;

	@SuppressLint("DefaultLocale")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.survey_screen);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");

		density = getResources().getDisplayMetrics().density;
		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		listView = (ListView) findViewById(R.id.listForSurvey);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForSurveyFull);
		textSurveyHeader = (TextView) findViewById(R.id.textForSurveyHeader);
		textForHeader.setTypeface(typeFaceHeader);
		textSurveyHeader.setTypeface(typeFace);
		textSurveyHeader.setText(getResources().getString(R.string.survey)
				.toUpperCase());
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForMainScreen.setClickable(false);
		layoutForUnivIcon.setVisibility(View.GONE);

		final ViewHolderSurvey holderView = new ViewHolderSurvey();
		progressDialog = new ProgressDialog(this);

		if (Utils.isOnline()) {
			//Fetch values from service by using AsyncTask process
			new SurveyTasks(SurveyActivity.this,
					SessionStores.getUnivId(SurveyActivity.this),
					SessionStores.getSchoolId(SurveyActivity.this),
					SessionStores.getRoleType(SurveyActivity.this),
					surveyArray, typeFace, typeFaceLight, listView, holderView,
					progressDialog).execute();
		} else {
			Toast("Internet connection is not available");
		}

	}

	public class ViewHolderSurvey {
		public ImageButton btnYes, btnNo;
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			toggle();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			if (h != null) {
				h.removeCallbacks(runnable);
			}
			Intent intentToHome = new Intent(SurveyActivity.this,
					HomeActivity.class);
			startActivity(intentToHome);
			finish();
		}
	}

	public static class ViewHolder {
		ImageButton btnYes, btnNo;
	}

	public void Toast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (h != null) {
			h.removeCallbacks(runnable);
		}
		super.onBackPressed();
	}
}
