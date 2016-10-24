package com.eliqxir.adapter;

import java.util.ArrayList;

import com.eliqxir.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String>
{
    private Activity context;
    ArrayList<String> data = null;
    int layout;
    String fromWhere;
 //   private int hidingItemIndex;
    public SpinnerAdapter(Activity context, int resource, ArrayList<String> data, String from)
    {
		super(context, resource, data);
		this.context = context;
		this.data = data;
		this.layout=resource;
		this.fromWhere=from;
//		this.hidingItemIndex = hidingItemIndex2;
	}

	@Override
	public int getCount() {
		if(fromWhere.equals("fromTips"))
		{
			return super.getCount();
		}
		else
		{
		return super.getCount() - 1; // This makes the trick: do not show last item
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 

		return initView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) { 
		return initView(position, convertView, parent);
	}

	private View initView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(layout, parent, false);
		}

		String item = data.get(position);
      
	//	Log.e("test ", item);

		if (item != null) {

			TextView spinnerItem = (TextView) row.findViewById(R.id.item_value);

			/* if (position == hidingItemIndex) {
				    //         TextView tv = new TextView(getContext());
				 spinnerItem.setText("");
			 }else{*/
				 if (spinnerItem != null) {
			 
				spinnerItem.setText(item);				
			}
//			 }			
			
		}

		return row;
	}
}