package com.gradapp.au.homescreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.BadgeUtils;
import com.gradapp.au.utils.BadgeView;
import com.gradapp.au.utils.SessionStores;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class HomeActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceGraduate, typeFaceProxi, typeFaceHeader;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack,
			imgForMySchedule, imgForCommencementPrg, imgForNotifications,
			imgForSocialMedia, imgForCamera;
	TextView textForHeader, textForBottom;
	RelativeLayout layoutForHeader, layoutForUnivIcon, layoutForMainScreen,
		    headerLay, layForMainOptions, layForBottomText;
	ImageView imgForCollege;
	View headerId;
	float density;
	RelativeLayout.LayoutParams layout_description;

	float x1, x2;
	float y1, y2;
	boolean touch = false, multipleTouch = false;
	
	private Handler mHandler = new Handler();

	Handler h;
	Runnable runnable;
	View badgeView;
	String unreadCount;
	int badgeCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		initUI();
	}

	@SuppressLint("ResourceAsColor")
	private void initUI() {
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface
				.createFromAsset(getAssets(), "fonts/Time-Roman.ttf");
		typeFaceGraduate = Typeface.createFromAsset(getAssets(),
				"fonts/Graduate-Regular.ttf");
		typeFaceProxi = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		unreadCount = SessionStores.getUnreadCount(this);
		//Set badage count in app icon calls BadgeUtils class
		if(unreadCount != null) {
			if(unreadCount.length() > 0) {
				badgeCount = Integer.parseInt(unreadCount);
				if(badgeCount > 0) {
					BadgeUtils.setBadge(HomeActivity.this, badgeCount);
				} else {
					BadgeUtils.setBadge(HomeActivity.this, 0);
				}
			}
		} else {
			BadgeUtils.setBadge(HomeActivity.this, 0);
		}

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForHeader = (RelativeLayout) findViewById(R.id.headerBg);
		textForBottom = (TextView) findViewById(R.id.textForBottom);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutFormainScreenFull);
		headerLay = (RelativeLayout) findViewById(R.id.headerLay);
		imgForMySchedule = (ImageButton) findViewById(R.id.imageForMySchedule);
		imgForCommencementPrg = (ImageButton) findViewById(R.id.imageForCommencementPrg);
		imgForNotifications = (ImageButton) findViewById(R.id.imageForNotifications);
		imgForSocialMedia = (ImageButton) findViewById(R.id.imageForSocialMedia);
		imgForCamera = (ImageButton) findViewById(R.id.imageForCamera);
		imgForCollege = (ImageView) findViewById(R.id.imgForCollege);
		layForMainOptions = (RelativeLayout) findViewById(R.id.layoutForOptions);
		layForBottomText = (RelativeLayout) findViewById(R.id.layForBottomTxt);
		badgeView = findViewById(R.id.invisible_for_badge_view);
		headerId = findViewById(R.id.header);
		textForBottom.setTypeface(typeFaceProxi);
		textForHeader.setTypeface(typeFaceHeader);
		textForHeader.setText("MAIN SCREEN");
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		imgForCommencementPrg.setOnClickListener(this);
		imgForMySchedule.setOnClickListener(this);
		imgForNotifications.setOnClickListener(this);
		imgForSocialMedia.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForHeader.setBackgroundColor(Color.parseColor("#071329"));
		textForHeader.setTextColor(Color.parseColor("#FFFFFF"));
		density = getResources().getDisplayMetrics().density;
		layoutForMainScreen.setClickable(false);
		layoutForUnivIcon.setVisibility(View.GONE);
		imgForCamera.setOnClickListener(this);
		
		//Set badge icon on notification image tab at center
		if(unreadCount != null) {
			if(unreadCount.length() > 0) {
				badgeCount = Integer.parseInt(unreadCount);
				if(badgeCount > 0) {
					BadgeView badge = new BadgeView(this, badgeView);
					badge.setBadgePosition(BadgeView.POSITION_CENTER);
					badge.setText(""+badgeCount);
					badge.show(true);
				}
		}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.btnSliderMenu) {
		}

		//Commencement tab clicks
		if (v.getId() == R.id.imageForCommencementPrg) {
			//to avoid multi-touch of icons at a time maintain boolean value
			if(multipleTouch == false) {
				multipleTouch = true;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.RIGHT);
				Intent intentToCommencement = new Intent(HomeActivity.this,
						CommencementActivity.class);
				startActivity(intentToCommencement);
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	multipleTouch = false;
		            }
		        }, 5000);
			}
			finish();
		}

		//MySchedule tab clicks
		if (v.getId() == R.id.imageForMySchedule) {
			//to avoid multi-touch of icons at a time maintain boolean value
			if(multipleTouch == false) {
				multipleTouch = true;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.RIGHT);
				Intent intentToSchedule = new Intent(HomeActivity.this,
						MyScheduleActivity.class);
				intentToSchedule.putExtra("schoolId", "null");
				startActivity(intentToSchedule);
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	multipleTouch = false;
		            }
		        }, 5000);
			}
			finish();
		}

		//Notification tab clicks
		if (v.getId() == R.id.imageForNotifications) {
			//to avoid multi-touch of icons at a time maintain boolean value
			if(multipleTouch == false) {
				multipleTouch = true;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.RIGHT);
				Intent intentToNotify = new Intent(HomeActivity.this,
						NotificationsActivity.class);
				startActivity(intentToNotify);
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	multipleTouch = false;
		            }
		        }, 5000);
			}
			finish();
		}

		//Social media tab clicks
		if (v.getId() == R.id.imageForSocialMedia) {
			//to avoid multi-touch of icons at a time maintain boolean value
			if(multipleTouch == false) {
				multipleTouch = true;
				getSlidingMenu().setTouchModeAbove(SlidingMenu.RIGHT);
				Intent intentToSocialMedia = new Intent(HomeActivity.this,
						SocialMediaActivity.class);
				startActivity(intentToSocialMedia);
				mHandler.postDelayed(new Runnable() {
		            public void run() {
		            	multipleTouch = false;
		            }
		        }, 5000);
			}
			finish();
		}
		
		//Camera tab clicks
		if(v.getId() == R.id.imageForCamera) {
			getSlidingMenu().setTouchModeAbove(SlidingMenu.RIGHT);
			Intent intentToCamera = new Intent(HomeActivity.this, CameraActivity.class);
			startActivity(intentToCamera);
			finish();
		}
	}

	public void onResume() {
		super.onResume();
		setContentView(R.layout.home);

		initUI();
	}

}
