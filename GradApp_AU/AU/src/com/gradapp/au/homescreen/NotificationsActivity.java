package com.gradapp.au.homescreen;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.NotificationTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.NotifyAdapter;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NotificationsActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textNotificationHeader;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon;
	ListView listView;
	float density;
	RelativeLayout.LayoutParams layout_description;
	ArrayList<HashMap<String, String>> notifyArray = new ArrayList<HashMap<String, String>>();
	DBAdapter db;
	float x1, x2;
	float y1, y2;
	boolean touch = false;
	Runnable runnable;
	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);

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

		db = new DBAdapter(this);

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForNotificationsFull);
		textNotificationHeader = (TextView) findViewById(R.id.textForNotificationsHeader);
		listView = (ListView) findViewById(R.id.listViewForNotify);
		textForHeader.setTypeface(typeFaceHeader);
		textNotificationHeader.setTypeface(typeFace);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForUnivIcon.setVisibility(View.GONE);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);

		progressDialog = new ProgressDialog(this);
		density = getResources().getDisplayMetrics().density;

		/*
		 * Notifications are fetched from online or offline functionalities.
		 * Initial stage the data has been fetch from Online and save it in database
		 * then the offline functionality is called once the app works in offline.
		 */
		if (Utils.isOnline()) {
			//Online process
			new NotificationTasks(NotificationsActivity.this,
					SessionStores.getUnivId(NotificationsActivity.this),
					SessionStores.getSchoolId(NotificationsActivity.this),
					listView, notifyArray, typeFaceLight, typeFaceHeader,
					progressDialog).execute();
		} else {
			//Offline process
			db.open();
			Cursor c = db.getAllNotifications();
			int count = c.getCount();
			if (count > 0) {
				if (c.moveToFirst()) {
					do {
						String id = c.getString(1);
						String title = c.getString(2);
						String content = c.getString(3);
						String color = c.getString(4);
						String day = c.getString(5);
						String month = c.getString(6);
						String timing = c.getString(7);

						HashMap<String, String> hashValue = new HashMap<String, String>();
						hashValue.put("id", id);
						hashValue.put("title", title);
						hashValue.put("content", content);
						hashValue.put("color", color);
						hashValue.put("day", day);
						hashValue.put("month", month);
						hashValue.put("timing", timing);

						notifyArray.add(hashValue);//Store the values in arraylist
					} while (c.moveToNext());
				}
			}
			c.close();
			db.close();

			if (notifyArray.size() > 0) {
				//Arraylist values are passed in listview adapter class
				listView.setAdapter(new NotifyAdapter(this, typeFaceLight,
						typeFaceHeader, notifyArray));
			} else {
				Toast("Page details are not saved in database, please connect to network");
			}
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intentToDetail = new Intent(NotificationsActivity.this,
						NotificationDetailedActivity.class);
				intentToDetail.putExtra("notifyId",
						notifyArray.get(arg2).get("id"));
				intentToDetail.putExtra("title",
						notifyArray.get(arg2).get("title"));
				intentToDetail.putExtra("timing",
						notifyArray.get(arg2).get("timing"));
				intentToDetail.putExtra("content",
						notifyArray.get(arg2).get("content"));
				startActivity(intentToDetail);
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			Intent intentToHome = new Intent(NotificationsActivity.this,
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
		super.onBackPressed();
		Intent intentToHome = new Intent(NotificationsActivity.this,
				HomeActivity.class);
		startActivity(intentToHome);
		finish();
	}
}
