package com.gradapp.au.homescreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.MyScheduleTasks;
import com.gradapp.au.activities.EditSchedulesActivity;
import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MyScheduleActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textScheduleHeader, textForEdit;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon, layForCustomView,
			layoutForAddOtherSchedule, layForUserCustomView;
	LinearLayout layoutForSchedules, layForSchedules;
	RelativeLayout.LayoutParams layout_description;
	float density;
	ArrayList<HashMap<String, String>> dateArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventsArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> userDateArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> userEventsArray = new ArrayList<HashMap<String, String>>();
	float x1, x2;
	float y1, y2;
	boolean touch = false, multipleTouch = false;
	double latitude, longitude;
	Runnable runnable;
	String schoolId, dateString = "", dateExists = "", timezoneID;
	ProgressDialog progressDialog;
	DBAdapter db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_schedule);

		timezoneID = TimeZone.getDefault().getID();
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

		schoolId = getIntent().getExtras().getString("schoolId");
		progressDialog = new ProgressDialog(this);

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForMyScheduleFull);
		textScheduleHeader = (TextView) findViewById(R.id.textForMyScheduleHeader);
		layoutForSchedules = (LinearLayout) findViewById(R.id.layoutForMySchedule);
		layoutForAddOtherSchedule = (RelativeLayout) findViewById(R.id.layoutForOtherSchedules);
		textForEdit = (TextView) findViewById(R.id.textForMyScheduleEdit);
		textForHeader.setTypeface(typeFaceHeader);
		textScheduleHeader.setTypeface(typeFace);
		textForEdit.setTypeface(typeFace);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		layoutForAddOtherSchedule.setOnClickListener(this);
		textForEdit.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForUnivIcon.setVisibility(View.GONE);
		textScheduleHeader.setText(SessionStores.getSchoolName(this));
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		textForHeader.setText("MY SCHEDULES");

		db = new DBAdapter(this);
		density = getResources().getDisplayMetrics().density;

		/*
		 * MySchedule events are fetched from online or offline functionalities.
		 * Initial stage the data has been fetch from Online and save it in database
		 * then the offline functionality is called once the app works in offline.
		 */
		if (Utils.isOnline()) {
			//Online process
			new MyScheduleTasks(MyScheduleActivity.this,
					SessionStores.getUnivId(MyScheduleActivity.this),
					SessionStores.getSchoolId(MyScheduleActivity.this),
					SessionStores.getRoleType(MyScheduleActivity.this),
					dateArray, eventsArray, typeFace, typeFaceLight,
					layoutForSchedules, layForCustomView, progressDialog,
					userDateArray, userEventsArray, layForUserCustomView,
					timezoneID).execute();
		} else {
			//Offline process
			db.open();
			Cursor c = db.getAllMySchedules();
			int count = c.getCount();
			if (count > 0) {
				//Fetch the datas and saved it in dateArray arrayList
				if (c.moveToFirst()) {
					do {
						String date = c.getString(1);
						if (dateString.length() == 0) {
							dateString = date;
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("header", dateString);
							dateArray.add(hashMap);
						} else if (!(dateString.equals(date))) {
							dateString = date;
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("header", dateString);
							dateArray.add(hashMap);
						}
					} while (c.moveToNext());
				}
			} else {
				Toast("Page details are not saved in database, please connect to network");
			}

			//If dateArray list is greater than 0 the process handled
			if (dateArray.size() > 0) {
				for (int i = 0; i < dateArray.size(); i++) {
					String date = dateArray.get(i).get("header");
					if (c.moveToFirst()) {
						do {
							String dateDb = c.getString(1);
							if (dateDb.equals(date)) {
								if (!(dateExists.equals(dateDb))) {
									Cursor cur = db.getMySchedulesItem(dateDb);
									if (cur.moveToFirst()) {
										do {
											String id = cur.getString(2);
											String name = cur.getString(3);
											String timeStarts = cur
													.getString(4);

											//Split the > symbol from string and save the values in eventsArray as id, name and starting time of event
											String scheduleEventId = dateDb
													+ ">" + id;
											String scheduleEventName = dateDb
													+ ">" + name;
											String scheduleEventTimeStarts = dateDb
													+ ">" + timeStarts;

											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put("id", scheduleEventId);
											hashMap.put("name",
													scheduleEventName);
											hashMap.put("timeStarts",
													scheduleEventTimeStarts);

											eventsArray.add(hashMap);

											if (!cur.isAfterLast()) {
												dateExists = dateDb;
											}
										} while (cur.moveToNext());
									}
									cur.close();
								}
							}
						} while (c.moveToNext());
					}
				}
			}
			c.close();
			db.close();

			Utils.MyScheduleListOffline(dateArray, eventsArray, this, typeFace,
					typeFaceLight, layoutForSchedules, layForCustomView);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnSliderMenu) {
			Intent intentToHome = new Intent(MyScheduleActivity.this,
					HomeActivity.class);
			startActivity(intentToHome);
			finish();
		}

		//Events are displayed as in custom view the event item layout clicks process
		if (v.getId() == R.id.layForCustomView) {
			if (multipleTouch == false) {
				multipleTouch = true;
				if (Utils.isOnline()) {
					//online process
					try {
						String position = v.getTag().toString();
						String spiltid[] = eventsArray
								.get(Integer.parseInt(position)).get("id")
								.split(">");
						String eventId = spiltid[1];
						String timeSplits1[] = eventsArray
								.get(Integer.parseInt(position))
								.get("timeStarts").split(">");
						//Splits the particular event date and send in intent 
						String timeStartsAt = timeStartsAt(timeSplits1[1]);
						Intent intentToEvents = new Intent(
								MyScheduleActivity.this,
								MyScheduleEventScreen.class);
						intentToEvents.putExtra("eventId", eventId);
						intentToEvents.putExtra("schoolId", SessionStores
								.getSchoolId(MyScheduleActivity.this));
						intentToEvents.putExtra("date", timeStartsAt);
						startActivity(intentToEvents);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								multipleTouch = false;
							}
						}, 500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					//Offline process
					try {
						String position = v.getTag().toString();
						String spiltid[] = eventsArray
								.get(Integer.parseInt(position)).get("id")
								.split(">");
						String eventId = spiltid[1];
						String timeSplits1[] = eventsArray
								.get(Integer.parseInt(position))
								.get("timeStarts").split(">");
						//Splits the particular event date and send in intent 
						String timeStartsAt = timeSplits1[1];
						Intent intentToEvents = new Intent(
								MyScheduleActivity.this,
								MyScheduleEventScreen.class);
						intentToEvents.putExtra("eventId", eventId);
						intentToEvents.putExtra("schoolId", SessionStores
								.getSchoolId(MyScheduleActivity.this));
						intentToEvents.putExtra("date", timeStartsAt);
						startActivity(intentToEvents);
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								multipleTouch = false;
							}
						}, 500);
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		//this is the edit schedules view in this also the views are displayed as custom
		if (v.getId() == R.id.layForUserCustomView) {
			if (Utils.isOnline()) {
				//Online Process
				try {
					String position = v.getTag().toString();
					String spiltid[] = eventsArray
							.get(Integer.parseInt(position)).get("id")
							.split(">");
					String eventId = spiltid[1];
					String timeSplits1[] = eventsArray
							.get(Integer.parseInt(position)).get("timeStarts")
							.split(">");
					//Splits the particular event date and send in intent 
					String timeStartsAt = timeStartsAt(timeSplits1[1]);
					Intent intentToEvents = new Intent(MyScheduleActivity.this,
							MyScheduleEventScreen.class);
					intentToEvents.putExtra("eventId", eventId);
					intentToEvents.putExtra("schoolId",
							SessionStores.getSchoolId(MyScheduleActivity.this));
					intentToEvents.putExtra("date", timeStartsAt);
					startActivity(intentToEvents);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							multipleTouch = false;
						}
					}, 500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Toast("Network connection is not available");
			}
		}

		//Add other schedules button functionality
		if (v.getId() == R.id.layoutForOtherSchedules) {
			if (Utils.isOnline()) {
				Intent intentToSearch = new Intent(MyScheduleActivity.this,
						SearchScheduleActivity.class);
				startActivity(intentToSearch);
				finish();
			} else {
				Toast("Network connection is not available");
			}
		}

		//Edit text click functionality
		if (v.getId() == R.id.textForMyScheduleEdit) {
			if (Utils.isOnline()) {
				if (Constant.userEventArrayList.size() > 0) {
					Intent intentToEdit = new Intent(MyScheduleActivity.this,
							EditSchedulesActivity.class);
					startActivity(intentToEdit);
					finish();
				} else {
					Utils.ShowAlert(MyScheduleActivity.this,
							"Cannot edit/delete core events");
				}
			} else {
				Toast("Network connection is not available");
			}
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

	@SuppressLint("InflateParams")
	public static String timeStartsAt(String timeStarts) {
		String timeSplits[] = timeStarts.split(" ");
		String dateValue = timeSplits[0];
		String dateSplits[] = dateValue.split("-");
		String monthValue = dateSplits[1];
		String day = dateSplits[2];
		String month = Utils.MonthValue(monthValue);
		String year = dateSplits[0];
		String dateConverted = month + " " + day + ", " + year;
		return dateConverted;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intentToHome = new Intent(MyScheduleActivity.this,
				HomeActivity.class);
		startActivity(intentToHome);
		finish();
	}
}
