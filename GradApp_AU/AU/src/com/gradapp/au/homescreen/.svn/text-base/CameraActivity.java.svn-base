package com.gradapp.au.homescreen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.StreamIdTasks;
import com.gradapp.au.activities.GalleryScreenCustomActivity;
import com.gradapp.au.activities.PhotoScreenActivity;
import com.gradapp.au.activities.R;
import com.gradapp.au.activities.StreamScreenGridActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CameraActivity extends SlidingMenuActivity implements OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, textForCamera, textForStream, textForGallery;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon, btnLayForCamera, btnLayForGallery, 
	               btnLayForStream, layoutForFull, layoutForSwipe;
	ListView listView;
	float density;
	RelativeLayout.LayoutParams layout_description;
	
	float x1,x2;
    float y1, y2;
    boolean touch = false;
    
    Handler h;
    Runnable runnable;
    ProgressDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_screen);
		
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Light.otf");
		
		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForFull = (RelativeLayout) findViewById(R.id.layoutForMaximizeClick);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForCameraScreenFull);
		textForCamera = (TextView) findViewById(R.id.textForCameraCamera);
		textForGallery = (TextView) findViewById(R.id.textForCameraGallery);
		listView = (ListView) findViewById(R.id.listOfCameraItems);
		btnLayForCamera = (RelativeLayout) findViewById(R.id.layForCamera);
		btnLayForGallery = (RelativeLayout) findViewById(R.id.layForMyGallery);
		layoutForSwipe = (RelativeLayout) findViewById(R.id.layoutForSwipe);
		textForHeader.setTypeface(typeFaceHeader);
		textForCamera.setTypeface(typeFaceHeader);
		textForGallery.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnLayForCamera.setOnClickListener(this);
		btnLayForGallery.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForMainScreen.setClickable(false);
		layoutForUnivIcon.setVisibility(View.GONE);
		textForHeader.setTextColor(Color.parseColor("#FFFFFF"));
		
		// According to the streams the listview has been set 
		dialog = new ProgressDialog(this);
		density = getResources().getDisplayMetrics().density;
		//Maintain stream values in Constant beacause the array has been accessed in photooptions screen.
		Constant.streamArray.clear();
		if(Utils.isOnline()) {
			// Streams are fetch from stream api and set the items in listView
			new StreamIdTasks(CameraActivity.this, SessionStores.getSchoolId(this), listView, typeFace, typeFaceLight, Constant.streamArray, dialog).execute();
		} else {
			Toast("Network connection is not available");
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intentToCameraStream = new Intent(CameraActivity.this, StreamScreenGridActivity.class);
				intentToCameraStream.putExtra("from", "stream");
				intentToCameraStream.putExtra("streamId", Constant.streamArray.get(arg2).get("streamId"));
				startActivity(intentToCameraStream);
				finish();
			}
		});
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}
		
		if(v.getId() == R.id.btnSliderMenu) {
			Intent intentToHome = new Intent(CameraActivity.this, HomeActivity.class);
			startActivity(intentToHome);
			finish();
		}
		
		//Redirects to camera activity
		if(v.getId() == R.id.layForCamera) {
			btnLayForCamera.setEnabled(false);
			btnLayForGallery.setEnabled(false);
			btnLayForCamera.setClickable(false);
			btnLayForGallery.setClickable(false);
			Intent intent=new Intent(CameraActivity.this, PhotoScreenActivity.class);
			startActivity(intent);
			finish();
		}
		
		//Redirects to Gallery activity
		if(v.getId() == R.id.layForMyGallery) {
				Intent intent=new Intent(CameraActivity.this, GalleryScreenCustomActivity.class);
				intent.putExtra("from", "gallery");
				intent.putExtra("imageCount", "greater");
				startActivity(intent);
				finish();
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
		super.onBackPressed();
		Intent intentToHome = new Intent(CameraActivity.this, HomeActivity.class);
		startActivity(intentToHome);
		finish();
	}
}
