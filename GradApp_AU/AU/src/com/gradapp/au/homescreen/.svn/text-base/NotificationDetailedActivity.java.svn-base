package com.gradapp.au.homescreen;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gradapp.au.AsyncTasks.NotificationReadTask;
import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NotificationDetailedActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textForName, textForTiming, textFordetails;
	String title, time, content, notifyId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications_detailed);
		
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

		//get the intent values from notification activity
		notifyId = getIntent().getExtras().getString("notifyId");
		title = getIntent().getExtras().getString("title");
		time = getIntent().getExtras().getString("timing");
		content = getIntent().getExtras().getString("content");
		
		//The notification read status will be passed to api to update the read status count
		new NotificationReadTask(this, notifyId).execute();

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		textForName = (TextView) findViewById(R.id.textforNotifyDetailedName);
		textForTiming = (TextView) findViewById(R.id.textForNotifyDetailedTime);
		textFordetails = (TextView) findViewById(R.id.textForDetailedNotification);
		textForHeader.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		btnForBack.setBackgroundResource(R.drawable.back_arrow_black);

		textForName.setText(title);
		textForTiming.setText(time);
		textFordetails.setText(Html.fromHtml(content));
		textForName.setTypeface(typeFace);
		textForTiming.setTypeface(typeFaceLight);
		textFordetails.setTypeface(typeFaceLight);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.backBtn) {
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
