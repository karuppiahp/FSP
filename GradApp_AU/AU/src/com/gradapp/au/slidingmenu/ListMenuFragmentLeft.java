package com.gradapp.au.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.CameraActivity;
import com.gradapp.au.homescreen.CommencementActivity;
import com.gradapp.au.homescreen.MyScheduleActivity;
import com.gradapp.au.homescreen.NotificationsActivity;
import com.gradapp.au.homescreen.SocialMediaActivity;
import com.gradapp.au.support.MenuListAdapterLeft;

public class ListMenuFragmentLeft extends Fragment implements OnClickListener {

	View rootView;
	RelativeLayout layoutForFaq, layoutForHandicap, layoutForSurvey,
			layoutForViewOthers;
	TextView textForFaq, textForHandicap, textForSurvey, textForViewOthers;
	SharedPreferences sharedpreferences;
	Editor editor;
	boolean rememberUser;
	ListView listView;
	List<String> nameArray = new ArrayList<String>();

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.left_menu_listview, container, false);
		listView = (ListView) rootView.findViewById(R.id.listViewForMenusLeft);

		//Menu items are static and added in nameArray arraylist
		nameArray.add("My Schedule");
		nameArray.add("Commencement Program");
		nameArray.add("Notifications");
		nameArray.add("Social Media");
		nameArray.add("Camera");
		
		listView.setAdapter(new MenuListAdapterLeft(getActivity(), nameArray));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 == 0) {
					//MySchedule screen
					getActivity().finish();
					Intent intentToSchedule = new Intent(getActivity(),
							MyScheduleActivity.class);
					intentToSchedule.putExtra("schoolId", "null");
					startActivity(intentToSchedule);
				}
				if(arg2 == 1) {
					//Commencement screen
					getActivity().finish();
					Intent intentToCommencement = new Intent(getActivity(),
							CommencementActivity.class);
					startActivity(intentToCommencement);
				}
				if(arg2 == 2) {
					//Notification Srceen
					getActivity().finish();
					Intent intentToNotify = new Intent(getActivity(),
							NotificationsActivity.class);
					startActivity(intentToNotify);
				}
				if(arg2 == 3) {
					//SocialMedia screen
					getActivity().finish();
					Intent intentToSocialMedia = new Intent(getActivity(),
							SocialMediaActivity.class);
					startActivity(intentToSocialMedia);
				}
				if(arg2 == 4) {
					//Camera Screen
					getActivity().finish();
					Intent intentToCamera = new Intent(getActivity(), CameraActivity.class);
					startActivity(intentToCamera);
				}
			}
		});
		return rootView;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
	}

}
