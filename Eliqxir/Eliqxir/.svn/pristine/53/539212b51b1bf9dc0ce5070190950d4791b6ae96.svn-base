package com.eliqxir.slidermenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.eliqxir.R;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class SlidingMenuActivity extends SlidingFragmentActivity {

	
	protected Fragment mFrag;
	String menuPref;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());

		setBehindContentView(R.layout.menu_frame);
		menuPref = SessionStore.getSlidinMenu(getApplicationContext());
		if (savedInstanceState == null) {
			try {
				if(menuPref == null)
				{
					FragmentTransaction t = this.getSupportFragmentManager()
							.beginTransaction();
					mFrag = new ListMenuFragment();
					t.replace(R.id.menu_frame, mFrag);
					t.commit();
				}
				else if(menuPref.equals("vendor")){
					FragmentTransaction t = this.getSupportFragmentManager()
							.beginTransaction();
					mFrag = new ListMenuFragmentVendor();
					t.replace(R.id.menu_frame, mFrag);
					t.commit();
				}
				else{
					FragmentTransaction t = this.getSupportFragmentManager()
							.beginTransaction();
					mFrag = new ListMenuFragmentDriver();
					t.replace(R.id.menu_frame, mFrag);
					t.commit();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				if(menuPref == null)
				{
					mFrag = (ListMenuFragment) this.getSupportFragmentManager()
							.findFragmentById(R.id.menu_frame);
				}
				else if(menuPref.equals("vendor")){
					mFrag = (ListMenuFragmentVendor) this.getSupportFragmentManager()
							.findFragmentById(R.id.menu_frame);
				}
				else{
					mFrag = (ListMenuFragmentDriver) this.getSupportFragmentManager()
							.findFragmentById(R.id.menu_frame);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// getActionBar().setDisplayHomeAsUpEnabled(false);
		// getActionBar().setDisplayShowHomeEnabled(false);
		// getActionBar().setDisplayShowTitleEnabled(false);
		// getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().hide();
		
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
