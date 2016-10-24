package com.gradapp.au.hamburger.menus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.StaticPagesTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.HomeActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class StaticPages extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textTitleHeader, textForContent;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon;
	RelativeLayout.LayoutParams layout_description;
	WebView webView;
	float density;
	float x1, x2;
	float y1, y2;
	boolean touch = false;
	String menuPageId, title = "", content = "";

	Handler h;
	Runnable runnable;
	DBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.static_menu_pages);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");

		db = new DBAdapter(this);
		menuPageId = getIntent().getExtras().getString("staticMenuId");

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForStaticPagesFull);
		textTitleHeader = (TextView) findViewById(R.id.textForStaticPageHeader);
		textForContent = (TextView) findViewById(R.id.textForStaticPageContent);
		webView = (WebView) findViewById(R.id.webViewForStaticPages);
		textForHeader.setTypeface(typeFaceHeader);
		textTitleHeader.setTypeface(typeFace);
		textForContent.setTypeface(typeFaceLight);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForMainScreen.setClickable(false);
		layoutForUnivIcon.setVisibility(View.GONE);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);

		density = getResources().getDisplayMetrics().density;

		if (Utils.isOnline()) {
			//Fetch static pages content from online using following AsyncTask process
			new StaticPagesTasks(StaticPages.this, menuPageId, textTitleHeader,
					textForContent, webView).execute();
		} else {
			//Fetch static pages content from local database in offline
			db.open();
			Cursor c = db.getAllStaticPages();
			if (c.moveToFirst()) {
				do {
					String hambrgrId = c.getString(1);
					if (hambrgrId.equals(menuPageId)) {
						title = c.getString(2);
						content = c.getString(3);
					}
				} while (c.moveToNext());
			}
			c.close();
			db.close();

			Log.i("title is::::", "" + title);
			Log.i("content is::::", "" + content);
			if (content.length() > 0) {
				textTitleHeader.setText(title);
				if (content.contains("<a href")) {
					textForContent.setText(Html.fromHtml(content));
					textForContent.setMovementMethod(LinkMovementMethod
							.getInstance());
				} else if (content.contains(".pdf")) {
					Toast("Page needs internet connection");
				} else {
					textForContent.setText(Html.fromHtml(content));
				}
			} else {
				Toast("Page details are not saved in database, please connect to network");
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			if(h != null) {
				h.removeCallbacks(runnable);
			}
			Intent intentToHome = new Intent(StaticPages.this,
					HomeActivity.class);
			startActivity(intentToHome);
			finish();
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

	@Override
	public void onBackPressed() {
		if(h != null) {
			h.removeCallbacks(runnable);
		}
		Intent intentToHome = new Intent(StaticPages.this,
				HomeActivity.class);
		startActivity(intentToHome);
		finish();
		super.onBackPressed();
	}
}
