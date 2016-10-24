package com.fsp.blacksheep;

import java.util.ArrayList;
import java.util.HashMap;

import com.fsp.blacksheep.MultiLine_bar_details1.ViewHolder;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BarsListAdapter extends BaseAdapter {
	
	public static ViewHolder holder;
	LayoutInflater mInflater;
	Context context;
	ArrayList<HashMap<String, String>> barsArray = new ArrayList<HashMap<String,String>>();
	String day;
	
	public BarsListAdapter(Context context, ArrayList<HashMap<String, String>> barsArray, String day) {
		this.context = context;
		this.barsArray = barsArray;
		this.day = day;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return barsArray.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int arg0, View v, ViewGroup arg2) {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		if (v == null) {

			v = mInflater.inflate(R.layout.bs_content_list_new,
					null);
			holder.text1 = (TextView) v
					.findViewById(R.id.text1_detail1);
			holder.text2 = (TextView) v
					.findViewById(R.id.text3_detail1);
			v.setTag(holder);

		} else {
			holder.text1 = (TextView) v
					.findViewById(R.id.text1_detail1);
			holder.text2 = (TextView) v
					.findViewById(R.id.text3_detail1);
			holder = (ViewHolder) v.getTag();
		}
		
		holder.text1.setText(Html.fromHtml(barsArray.get(arg0).get("venue")));
		if(day.equals("SUNDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("sunday")));
		}
		
		if(day.equals("MONDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("monday")));
		}
		
		if(day.equals("TUESDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("tuesday")));
		}
		
		if(day.equals("WEDNESDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("wednesday")));
		}
		
		if(day.equals("THURSDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("thursday")));
		}
		
		if(day.equals("FRIDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("friday")));
		}
		
		if(day.equals("SATURDAY")) {
			holder.text2.setText(Html.fromHtml(barsArray.get(arg0).get("saturday")));
		}
		
		return v;
	}
	
	static class ViewHolder {
		TextView text1, text2, text3, text4;

	}

}
