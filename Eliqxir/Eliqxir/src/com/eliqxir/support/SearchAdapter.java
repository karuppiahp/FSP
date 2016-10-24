package com.eliqxir.support;

import java.util.ArrayList;
import java.util.HashMap;

import com.eliqxir.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<HashMap<String, String>> searchList;
	public SearchAdapter(Context context, ArrayList<HashMap<String, String>> searchArray){
		mContext = context;
		searchList = searchArray;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		v = inflater.inflate(R.layout.search_list_item, null);
		TextView textForPdtName = (TextView)v.findViewById(R.id.textForSearchItem);
		textForPdtName.setText(searchList.get(arg0).get("pdtSearchName"));
		return v;
	}

}
