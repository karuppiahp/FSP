package com.gradapp.au.support;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gradapp.au.activities.R;

public class MenuListAdapterLeft extends BaseAdapter {

	Context context;
	TextView textView;
	ImageView imageView;
	List<String> menuNames;

	public MenuListAdapterLeft(Activity mContext, List<String> names) {
		context = mContext;
		menuNames = names;
	}

	@Override
	public int getCount() {
		return menuNames.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.left_menu_list_items, null);
		textView = (TextView) v.findViewById(R.id.txtForMySchedule);
		imageView = (ImageView) v.findViewById(R.id.myScheduleImg);
		textView.setText(menuNames.get(arg0));
		//Homescreen tab images 
		if(arg0 == 0) {
			imageView.setImageResource(R.drawable.calendar);
		}
		if(arg0 == 1) {
			imageView.setImageResource(R.drawable.commencement_img);
		}
		if(arg0 == 2) {
			imageView.setImageResource(R.drawable.notifications_img);
		}
		if(arg0 == 3) {
			imageView.setImageResource(R.drawable.social_media_img);
		}
		if(arg0 == 4) {
			imageView.setImageResource(R.drawable.camera_img);
		}
		
		return v;
	}

}
