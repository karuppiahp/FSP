package com.fsp.blacksheep;

import java.util.ArrayList;

import com.fsp.blacksheep.ArrayAdapter1.TextValue;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DateArrayAdapter extends BaseAdapter{

	Context context;
	ArrayList<String> daysArray = new ArrayList<String>();
	String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
	public DateArrayAdapter(Context cntxt,
			ArrayList<String> daysArray) {
		context = cntxt;
		this.daysArray = daysArray;
	}

	public int getCount() {
		Log.e("Adapter size????",""+daysArray.size());
		return daysArray.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextValue obj_text_val;
		RelativeLayout obj_Rel;
		if (v == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(
					R.layout.bars_array_list_item, null);
			obj_text_val = new TextValue();
			obj_text_val.text1 = (TextView) v
					.findViewById(R.id.bars_arr_text1);
			obj_Rel = (RelativeLayout) v
					.findViewById(R.id.daterelative);
			
			v.setTag(obj_text_val);
		} else {
			obj_text_val = (TextValue) v.getTag();
		}
		
		Log.e("Days/////","checking");
		return v;
	}

}
