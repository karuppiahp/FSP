package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gradapp.au.activities.R;

public class NotifyAdapter extends BaseAdapter {

	Context context;
	Typeface typeFaceLight, typeFaceHeader;
	ArrayList<HashMap<String, String>> notifyArray = new ArrayList<HashMap<String, String>>();

	public NotifyAdapter(Context mContext, Typeface typeFaceLights,
			Typeface typeFaceHeaders,
			ArrayList<HashMap<String, String>> notifiesArray) {
		context = mContext;
		typeFaceLight = typeFaceLights;
		typeFaceHeader = typeFaceHeaders;
		notifyArray = notifiesArray;
	}

	@Override
	public int getCount() {
		return notifyArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.notify_list_items, null);
		ImageView imageView = (ImageView) v
				.findViewById(R.id.imageForNotifyDate);
		TextView textForMonth = (TextView) v
				.findViewById(R.id.textForNotifyMonth);
		TextView textForDate = (TextView) v
				.findViewById(R.id.textForNotifyDate);
		TextView textForNotifyName = (TextView) v
				.findViewById(R.id.textForNotificationsList);
		textForMonth.setTypeface(typeFaceLight);
		textForDate.setTypeface(typeFaceLight);
		textForNotifyName.setTypeface(typeFaceHeader);
		textForMonth.setText(notifyArray.get(arg0).get("month"));
		textForDate.setText(notifyArray.get(arg0).get("day"));
		textForNotifyName.setText(notifyArray.get(arg0).get("title"));
		//Notify image icon background will be changed to Red
		if (notifyArray.get(arg0).get("color").equals("red")) {
			imageView.setImageResource(R.drawable.notify_red);
			textForMonth.setTextColor(Color.parseColor("#FFFFFF"));
			textForDate.setTextColor(Color.parseColor("#FFFFFF"));
		}
		//Notify image icon background will be changed to Green
		if (notifyArray.get(arg0).get("color").equals("green")) {
			imageView.setImageResource(R.drawable.notify_green);
			textForMonth.setTextColor(Color.parseColor("#FFFFFF"));
			textForDate.setTextColor(Color.parseColor("#FFFFFF"));
		}
		return v;
	}

}
