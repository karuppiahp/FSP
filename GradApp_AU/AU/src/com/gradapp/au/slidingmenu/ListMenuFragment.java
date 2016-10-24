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
import com.gradapp.au.hamburger.menus.FAQActivity;
import com.gradapp.au.hamburger.menus.SettingsActivity;
import com.gradapp.au.hamburger.menus.StaticPages;
import com.gradapp.au.hamburger.menus.SurveyActivity;
import com.gradapp.au.support.MenuListAdapter;
import com.gradapp.au.utils.SessionStores;

public class ListMenuFragment extends Fragment implements OnClickListener {

	View rootView;
	RelativeLayout layoutForFaq, layoutForHandicap, layoutForSurvey,
			layoutForViewOthers;
	TextView textForFaq, textForHandicap, textForSurvey, textForViewOthers;
	SharedPreferences sharedpreferences;
	Editor editor;
	boolean rememberUser;
	ListView listView;
	List<String> idArray = new ArrayList<String>();
	List<String> nameArray = new ArrayList<String>();

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}

		return false;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Layoutinflater the menu_listview xml file is initialized
		rootView = inflater.inflate(R.layout.menu_listview, container, false);
		listView = (ListView) rootView.findViewById(R.id.listViewForMenus);
		nameArray.clear();
		idArray.clear();
		//from session the name and id's are load and saved it in array
		nameArray = SessionStores.loadMenuArray(getActivity());
		idArray = SessionStores.loadMenuIdArray(getActivity());

		listView.setAdapter(new MenuListAdapter(getActivity(), nameArray));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (nameArray.get(arg2).equals("FAQ")) {
					//FAQ screen
					getActivity().finish();
					Intent intentToFaq = new Intent(getActivity(),
							FAQActivity.class);
					startActivity(intentToFaq);
				} else if (nameArray.get(arg2).equals("SURVEY")) {
					//Survey screen
					getActivity().finish();
					Intent intentToFaq = new Intent(getActivity(),
							SurveyActivity.class);
					startActivity(intentToFaq);
				} else if (nameArray.get(arg2).equals("SETTINGS")) {
					//Settings screen
					getActivity().finish();
					Intent intentToFaq = new Intent(getActivity(),
							SettingsActivity.class);
					startActivity(intentToFaq);
				} else {
					//Static pages
					String id = idArray.get(arg2);
					getActivity().finish();
					Intent intentToStaticPages = new Intent(getActivity(),
							StaticPages.class);
					intentToStaticPages.putExtra("staticMenuId", id);
					startActivity(intentToStaticPages);
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
