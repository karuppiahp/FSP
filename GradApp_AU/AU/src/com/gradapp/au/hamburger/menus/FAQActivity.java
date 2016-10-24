package com.gradapp.au.hamburger.menus;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.FaqTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.HomeActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.FAQAdapter;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class FAQActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textFaqHeader, textForFaqEmpty;
	ListView listView;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon;
	String question = "How can i purchase my regalia";
	String answer = "It is a long established fact that a reader will be distracted by the readable content of a page";
	float density;
	RelativeLayout.LayoutParams layout_description;
	ArrayList<HashMap<String, String>> faqArray = new ArrayList<HashMap<String, String>>();

	float x1, x2;
	float y1, y2;
	boolean touch = false;

	Handler h;
	Runnable runnable;
	DBAdapter db;
	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faq_screen);

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
		listView = (ListView) findViewById(R.id.listForFaq);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForFaqFull);
		textFaqHeader = (TextView) findViewById(R.id.textForFaqHeader);
		textForFaqEmpty = (TextView) findViewById(R.id.textForFaqEmpty);
		textForHeader.setTypeface(typeFaceHeader);
		textFaqHeader.setTypeface(typeFace);
		textForFaqEmpty.setTypeface(typeFace);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForMainScreen.setClickable(false);
		layoutForUnivIcon.setVisibility(View.GONE);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);

		db = new DBAdapter(this);
		density = getResources().getDisplayMetrics().density;

		ViewHolder holderView = new ViewHolder();
		if (Utils.isOnline()) {
			//Fetch questions from api using AsyncTask
			new FaqTasks(FAQActivity.this,
					SessionStores.getUnivId(FAQActivity.this),
					SessionStores.getSchoolId(FAQActivity.this),
					SessionStores.getRoleType(FAQActivity.this), listView,
					faqArray, typeFace, typeFaceLight, holderView,
					progressDialog, textForFaqEmpty).execute();
		} else {
			//Fetch questions from local database in offline condition
			db.open();
			Cursor c = db.getAllFAQ();
			int count = c.getCount();
			if (count > 0) {
				if (c.moveToFirst()) {
					do {
						String ques = c.getString(1);
						String ans = c.getString(2);
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("faqQues", ques);
						hashMap.put("faqAns", ans);
						faqArray.add(hashMap);
					} while (c.moveToNext());
				}
			}
			c.close();
			db.close();
			
			if (faqArray.size() > 0) {
				listView.setAdapter(new FAQAdapter(FAQActivity.this, faqArray, typeFace,
						typeFaceLight, holderView));
			} else {
				Toast("Page details are not saved in database, please connect to network");
			}
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String position = SessionStores
						.getListViewPosition(FAQActivity.this);
				if (position != null) {
					int indexPosition = Integer.parseInt(position);
					removeView(indexPosition);
				}
				updateView(arg2);
			}
		});
	}

	/*
	 * Listview expand the list item and display the answer using updateView method
	 */
	private void updateView(int index) {
		ViewHolder holder = new ViewHolder();
		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());
		String position = Integer.toString(index);
		SessionStores.saveListViewPosition(position, FAQActivity.this);
		holder.textForAnswer = (TextView) v.findViewById(R.id.textForFaqAnswer);
		holder.textForAnswer.setVisibility(View.VISIBLE);
	}

	/*
	 * Listview expanded list item of answer will be hide by using removeView method
	 */
	@SuppressLint("ResourceAsColor")
	private void removeView(int index) {
		ViewHolder holder = new ViewHolder();
		View v = listView
				.getChildAt(index - listView.getFirstVisiblePosition());
		if (v != null) {
			holder.textForAnswer = (TextView) v
					.findViewById(R.id.textForFaqAnswer);
			holder.textForAnswer.setVisibility(View.GONE);
		}
	}

	/*
	 * ViewHolder used to hold the list items of Listview
	 */
	public class ViewHolder {
		public TextView textForAnswer;
		public LinearLayout linearLayout;
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			if(h != null) {
				h.removeCallbacks(runnable);
			}
			Intent intentToHome = new Intent(FAQActivity.this,
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
		Intent intentToHome = new Intent(FAQActivity.this,
				HomeActivity.class);
		startActivity(intentToHome);
		finish();
		super.onBackPressed();
	}
}
