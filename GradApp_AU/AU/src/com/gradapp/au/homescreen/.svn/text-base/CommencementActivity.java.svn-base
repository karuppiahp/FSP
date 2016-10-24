package com.gradapp.au.homescreen;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.CommencementTasks;
import com.gradapp.au.activities.R;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CommencementActivity extends SlidingMenuActivity implements OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, txtForCommencement;
	RelativeLayout layoutForMainScreen, layoutForUnivIcon;
	boolean type;
	LayoutInflater mInflator;
	float density;
	RelativeLayout.LayoutParams layout_description;
	
	float x1,x2;
    float y1, y2;
    boolean touch = false;
    
    Dialog dialog;
    ArrayList<HashMap<String, String>> schoolIdArray = new ArrayList<HashMap<String,String>>();
    WebView webView;
    
    ProgressDialog progressDialog;
    
    Runnable runnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commencement_screen);
		initUI();
	}
	
	public void initUI() {
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Light.otf");
		
		density = getResources().getDisplayMetrics().density;
		progressDialog = new ProgressDialog(this);
		
		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		layoutForUnivIcon = (RelativeLayout) findViewById(R.id.layoutForunivIconMenus);
		layoutForMainScreen = (RelativeLayout) findViewById(R.id.layoutForCommencementFull);
		txtForCommencement = (TextView) findViewById(R.id.textForCommencement);
		webView = (WebView) findViewById(R.id.webViewForCommencement);
		textForHeader.setTypeface(typeFaceHeader);
		txtForCommencement.setTypeface(typeFace);
		btnForiconSlider.setVisibility(View.VISIBLE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForiconSlider.setOnClickListener(this);
		btnForBack.setVisibility(View.GONE);
		layoutForUnivIcon.setVisibility(View.GONE);
		btnForHamberger.setBackgroundResource(R.drawable.hamberger_black);
		
		if(Utils.isOnline()) {
			// Commencement pdf file has been loaded from commencement api
			new CommencementTasks(CommencementActivity.this, SessionStores.getSchoolId(CommencementActivity.this), webView, txtForCommencement, progressDialog).execute();
		} else {
			Toast("Internet connection is not available");
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}
		
		if(v.getId() == R.id.btnSliderMenu) {
			Intent intentToHome = new Intent(CommencementActivity.this, HomeActivity.class);
			startActivity(intentToHome);
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
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intentToHome = new Intent(CommencementActivity.this, HomeActivity.class);
		startActivity(intentToHome);
		finish();
	}
}
