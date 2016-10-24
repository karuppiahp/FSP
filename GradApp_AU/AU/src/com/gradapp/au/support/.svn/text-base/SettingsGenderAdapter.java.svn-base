package com.gradapp.au.support;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.gradapp.au.activities.R;

public class SettingsGenderAdapter extends BaseAdapter {

	private TextView text;
	LayoutInflater mInflator;
	Activity context;
	Typeface typeFace;
	ArrayList<String> genderArray = new ArrayList<String>();
	boolean type = false, gender = false;
	String genderString;
	EditText editTxtForFirstName, editTxtForLastName;

	public SettingsGenderAdapter(Activity activity, Typeface typeface,
			ArrayList<String> settingsgenderList, String gender,
			EditText editTxtFirst, EditText editTxtLast) {
		context = activity;
		typeFace = typeface;
		genderArray = settingsgenderList;
		genderString = gender;
		editTxtForFirstName = editTxtFirst;
		editTxtForLastName = editTxtLast;
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
		if (!gender) {
			if (genderString.length() > 0) {
				if (genderString.equals("M")) {
					text.setText("Male");
				} else {
					text.setText("Female");
				}
				gender = true;
				text.setTextColor(Color.parseColor("#FFFFFF"));
			} else {
				text.setText("Gender");
				text.setTextColor(Color.parseColor("#a39ea1"));
			}
		} else {
			text.setText(genderArray.get(position));
			text.setTextColor(Color.parseColor("#FFFFFF"));
		}
		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return genderArray.get(position);
	}

	@Override
	public int getCount() {
		return genderArray.size();
	}

	@SuppressLint("InflateParams")
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		//editext focus checked for keyboard status
		((InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(editTxtForFirstName.getWindowToken(),
						0);
		((InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(editTxtForLastName.getWindowToken(), 0);
		if (convertView == null) {
			convertView = mInflator.inflate(R.layout.custom_spinner_text, null);
		}
		text = (TextView) convertView.findViewById(R.id.textForSpinnerItems);
		text.setText(genderArray.get(position));
		return convertView;
	};
}
