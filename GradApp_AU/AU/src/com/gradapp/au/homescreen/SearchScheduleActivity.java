package com.gradapp.au.homescreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.AddOtherSchedulesTask;
import com.gradapp.au.AsyncTasks.SearchSchedulesTask;
import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SearchScheduleActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	RelativeLayout layoutForSearchCenter, layoutForSearchField, layForCustomView;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader;
	LinearLayout layoutForSchedules, layoutForSchedulesSearch;
	EditText edittextSearch;
	Handler mHandler = new Handler();
	String timezoneID;
	ArrayList<HashMap<String, String>> nameArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventsArray = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_schedules);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForSearchCenter = (RelativeLayout) findViewById(R.id.layoutForSearchCenter);
		layoutForSearchField = (RelativeLayout) findViewById(R.id.layoutForSearch);
		layoutForSchedules = (LinearLayout) findViewById(R.id.layoutForMySchedule);
		layoutForSchedulesSearch = (LinearLayout) findViewById(R.id.layoutForMyScheduleSearch);
		edittextSearch = (EditText) findViewById(R.id.editTxtForSearchSchedules);
		textForHeader.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		layoutForSearchCenter.setOnClickListener(this);
		edittextSearch.addTextChangedListener(watch);
		textForHeader.setText("OTHER SCHEDULES");
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		btnForBack.setBackgroundResource(R.drawable.back_arrow_black);
		
		timezoneID = TimeZone.getDefault().getID();
		
		if(Utils.isOnline()) {
			//Search api calls
			new SearchSchedulesTask(this, nameArray, eventsArray, typeFace, typeFaceLight,
					layoutForSchedules, layForCustomView, timezoneID).execute();
		} else {
			Toast.makeText(SearchScheduleActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	//TextWatcher used to fetch the text matches list of events
	TextWatcher watch = new TextWatcher(){
		  @Override
		  public void afterTextChanged(Editable arg0) {
		    // TODO Auto-generated method stub
		  }
		  @Override
		  public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		      int arg3) {
		    // TODO Auto-generated method stub
		  }
		  @Override
		  public void onTextChanged(final CharSequence s, int a, int b, int c) {
		    // TODO Auto-generated method stub
			  if(s.length() > 0) {
				  layoutForSchedules.setVisibility(View.GONE);
				  layoutForSchedulesSearch.setVisibility(View.VISIBLE);
				  Timer t = new Timer();
				  TimerTask timerTask = new TimerTask() {
						@Override
						public void run() {
							mHandler.postDelayed(new Runnable() {
					            public void run() {
					            	//According to the matches list the custom view is created it is held Utils class
					            	Utils.SearchScheduleEditTxtList(nameArray, eventsArray, SearchScheduleActivity.this, typeFace, typeFaceLight, layoutForSchedulesSearch, layForCustomView, s.toString());
					            }
					        },1000);
						}
					};
					t.schedule(timerTask, 1000);
					
			  } else {
				  layoutForSchedules.setVisibility(View.VISIBLE);
				  layoutForSchedulesSearch.setVisibility(View.GONE);
			  }
		  }};

	@Override
	public void onClick(final View v) {
		if (v.getId() == R.id.btnHamberger) {
			((InputMethodManager) getBaseContext().getSystemService(
					Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							edittextSearch.getWindowToken(), 0);
			showSecondaryMenu();
		}
		
		if(v.getId() == R.id.backBtn) {
			Intent intentToSchedules = new Intent(SearchScheduleActivity.this, MyScheduleActivity.class);
			intentToSchedules.putExtra("schoolId", "null");
			startActivity(intentToSchedules);
			finish();
		}
		
		//Search edittext box clicks process
		if(v.getId() == R.id.layoutForSearchCenter) {
			layoutForSearchCenter.setVisibility(View.GONE);
			layoutForSearchField.setVisibility(View.VISIBLE);
			edittextSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
	            @Override
	            public void onFocusChange(View v, boolean hasFocus) {
	            	edittextSearch.post(new Runnable() {
	                    @Override
	                    public void run() {
	                    	//Keyboard visibility
	                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	                        imm.showSoftInput(edittextSearch, InputMethodManager.SHOW_IMPLICIT);
	                    }
	                });
	            }
	        });
			edittextSearch.requestFocus();
		}
		
		//Custom view of events list item clciks it redirects to detail screen (MyScheduleEventScreen.class)
		if(v.getId() == R.id.layForCustomView) {
			try {
				String position = v.getTag().toString();
				String spiltid[] = eventsArray.get(Integer.parseInt(position))
						.get("id").split(">");
				String eventId = spiltid[1];
				String timeSplits1[] = eventsArray.get(Integer.parseInt(position)).get("timeStarts").split(">");
				String timeStartsAt = timeStartsAt(timeSplits1[1]);
				Intent intentToEvents = new Intent(SearchScheduleActivity.this,
						MyScheduleEventScreen.class);
				intentToEvents.putExtra("eventId", eventId);
				intentToEvents.putExtra("schoolId", SessionStores.getSchoolId(SearchScheduleActivity.this));
				intentToEvents.putExtra("date", timeStartsAt);
				startActivity(intentToEvents);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		//Add the event as user MySchedules functionality
		if(v.getId() == R.id.imageForSearchSheduleAdd) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					SearchScheduleActivity.this);

			// set title
			alertDialogBuilder.setTitle("Add Schedule");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Add this event to your schedule?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
										String tag = v.getTag().toString();
										int position = Integer.parseInt(tag);
										String eventIdSplits[] = eventsArray.get(position).get("id").split(">");
										String eventId = eventIdSplits[1];
										if(Utils.isOnline()) {
											//AddSchedule api calls
											new AddOtherSchedulesTask(SearchScheduleActivity.this, eventId, eventsArray, position, nameArray, typeFace, typeFaceLight,
													layoutForSchedules, layForCustomView).execute();
										} else {
											Toast.makeText(SearchScheduleActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
										}
								}
					})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialog, int id) {
							// if this button is clicked, just
							// close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
			AlertDialog alertDialog = alertDialogBuilder.create();
		
			// show it
			alertDialog.show();
		}
		
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

	public void onBackPressed() {
		super.onBackPressed();
		Intent intentToSchedules = new Intent(SearchScheduleActivity.this, MyScheduleActivity.class);
		intentToSchedules.putExtra("schoolId", "null");
		startActivity(intentToSchedules);
		finish();
	}

}
