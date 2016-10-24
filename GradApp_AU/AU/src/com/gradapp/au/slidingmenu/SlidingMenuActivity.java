package com.gradapp.au.slidingmenu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.gradapp.au.activities.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class SlidingMenuActivity extends SlidingFragmentActivity {

	protected Fragment mFrag, mFragLeft;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setBehindContentView(R.layout.left_menu_frame);
		if (savedInstanceState == null) {
			try {
				FragmentTransaction leftFrag = this.getSupportFragmentManager()
						.beginTransaction();
				mFragLeft = new ListMenuFragmentLeft();
				leftFrag.replace(R.id.left_menu_frame, mFragLeft);
				leftFrag.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				mFragLeft = (ListMenuFragmentLeft) this
						.getSupportFragmentManager().findFragmentById(
								R.id.left_menu_frame);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// customize the SlidingMenu with both right and left menus
		SlidingMenu sm = getSlidingMenu();
		sm.setSecondaryMenu(R.layout.menu_frame);
		if (savedInstanceState == null) {
			try {
				// replace fragment with right side menu items
				FragmentTransaction t = this.getSupportFragmentManager()
						.beginTransaction();
				mFrag = new ListMenuFragment();
				t.replace(R.id.menu_frame, mFrag);
				t.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				// left side menu items
				mFrag = (ListMenuFragment) this.getSupportFragmentManager()
						.findFragmentById(R.id.menu_frame);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
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
