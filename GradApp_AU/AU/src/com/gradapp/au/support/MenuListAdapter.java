package com.gradapp.au.support;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gradapp.au.activities.R;

@SuppressLint("ViewHolder")
public class MenuListAdapter extends BaseAdapter {

	Context context;
	TextView textView;
	List<String> menuNames;

	public MenuListAdapter(Activity mContext, List<String> names) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.menu_list_items, null);
		textView = (TextView) v.findViewById(R.id.textForMenuItems);
		textView.setText(menuNames.get(arg0));
		//even numbers of layouts has backgrounds will be changed
		if (arg0 % 2 == 0) {
			textView.setBackgroundResource(R.drawable.faq_bg);
		}
		return v;
	}

}
