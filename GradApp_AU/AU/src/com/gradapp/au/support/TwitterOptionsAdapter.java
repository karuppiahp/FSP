package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gradapp.au.activities.R;

public class TwitterOptionsAdapter extends BaseAdapter {

	Context context;
	ArrayList<HashMap<String, String>> hashTagList = new ArrayList<HashMap<String, String>>();

	public TwitterOptionsAdapter(Context mContext,
			ArrayList<HashMap<String, String>> hashList) {
		context = mContext;
		hashTagList = hashList;
	}

	@Override
	public int getCount() {
		return hashTagList.size();
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
		v = inflater.inflate(R.layout.filters_list_options, null);
		TextView textView = (TextView) v
				.findViewById(R.id.textForFilterOptions);
		textView.setText(hashTagList.get(arg0).get("hashTag"));
		return v;
	}

}
