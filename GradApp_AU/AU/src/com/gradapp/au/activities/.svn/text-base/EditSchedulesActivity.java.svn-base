package com.gradapp.au.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.DeleteSchedulesTasks;
import com.gradapp.au.homescreen.MyScheduleActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class EditSchedulesActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textForName, textForTiming, textFordetails, textForMyScheduleHeader, textForEditSchedules;
	String title, time, content, notifyId;
	LinearLayout layoutForSchedules;
	RelativeLayout layForCustomView;
	boolean value = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_schedules);

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
		
		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		textForMyScheduleHeader = (TextView) findViewById(R.id.textForMyScheduleHeader);
		layoutForSchedules = (LinearLayout) findViewById(R.id.layoutForUserSchedule);
		textForEditSchedules = (TextView) findViewById(R.id.textForEditSchedules);
		textForHeader.setTypeface(typeFaceHeader);
		textForMyScheduleHeader.setTypeface(typeFace);
		textForEditSchedules.setTypeface(typeFace);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		btnForBack.setBackgroundResource(R.drawable.back_arrow_black);
		textForMyScheduleHeader.setText("Edit Schedules");
		
		if(Constant.userEventArrayList.size() > 0) {
			//User selected events are have to be displayed as dynamic so the method has been called in Utils class.
			Utils.UserScheduleList(Constant.userEventArrayList, EditSchedulesActivity.this, typeFace, typeFaceLight, layoutForSchedules, layForCustomView, "Edit");
		} else {
			textForEditSchedules.setVisibility(View.VISIBLE);
			Toast("No Events found");
		}
	}

	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.backBtn) {
			Intent intentToSchedule = new Intent(EditSchedulesActivity.this, MyScheduleActivity.class);
			intentToSchedule.putExtra("schoolId", "null");
			startActivity(intentToSchedule);
			finish();
		}
		
		/*
		 * The list of edit items has been clicked the delete function has been handled.
		 */
		if (v.getId() == R.id.layForCustomView) {
			
			if(value == false) {
				value = true;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					EditSchedulesActivity.this);

			// set title
			alertDialogBuilder.setTitle("Delete Event");

			// set dialog message
			alertDialogBuilder
					.setMessage(
							"Delete this event?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									value = false;
									String tag = v.getTag().toString();
									int position = Integer.parseInt(tag);
									String eventIdSplits[] = Constant.userEventArrayList.get(position).get("id").split(">");
									String eventId = eventIdSplits[1];
									if(Utils.isOnline()) {
										//Delete the event by passing event id as in params.
										new DeleteSchedulesTasks(EditSchedulesActivity.this, eventId, position, typeFace, typeFaceLight, layoutForSchedules, layForCustomView, textForEditSchedules).execute();
									} else {
										Toast.makeText(EditSchedulesActivity.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
									}
								}
					})
			.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(
								DialogInterface dialog, int id) {
							value = false;
							dialog.cancel();
						}
					});
	AlertDialog alertDialog = alertDialogBuilder.create();

	// show it
	alertDialog.show();
			}
		}
	}
	
	public void Toast(final String message)
    {
    	runOnUiThread(new Runnable(){
    	    public void run() {
    	    	Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    	    }
    	 });
    }
	
	public void onBackPressed() {
		Intent intentToSchedule = new Intent(EditSchedulesActivity.this, MyScheduleActivity.class);
		intentToSchedule.putExtra("schoolId", "null");
		startActivity(intentToSchedule);
		finish();
		super.onBackPressed();
	}
}
