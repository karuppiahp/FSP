package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gradapp.au.activities.R;

public class SettingsRoleTypeAdapter extends BaseAdapter{

	private TextView text;
	LayoutInflater mInflator;
	Activity context;
	Typeface typeFace;
	ArrayList<HashMap<String, String>> roleList = new ArrayList<HashMap<String, String>>();
	boolean type = false;
	
	public SettingsRoleTypeAdapter(Activity activity, Typeface typeface, ArrayList<HashMap<String, String>> settingsRoleList) {
		context = activity;
		typeFace = typeface;
		roleList = settingsRoleList;
		mInflator = LayoutInflater.from(context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.custom_spinner, null);
		}
		text = (TextView) convertView.findViewById(R.id.textForSpinnerItem);
		text.setTypeface(typeFace);
		if (!type) {
			text.setText("Guest/Graduate");
		} else {
			text.setText(roleList.get(position).get("roleName"));
		}
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return roleList.get(position).get("roleName");
	}

	@Override
	public int getCount() {
		return roleList.size();
	}

	@SuppressLint("InflateParams")
	public View getDropDownView(int position, View convertView,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.custom_spinner_text,
					null);
		}
		text = (TextView) convertView
				.findViewById(R.id.textForSpinnerItems);
		text.setText(roleList.get(position).get("roleName"));
		return convertView;
	};
}
