package com.fsp.blacksheep;




import com.fsp.blacksheep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private String[] VAL1;

	public ImageAdapter(Context c, String[] val1) {
		mContext = LayoutInflater.from(c);
		VAL1 = val1;

	}

	public int getCount() {
		return VAL1.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mContext.inflate(R.layout.tv, null);

			TextView i = (TextView) convertView
					.findViewById(R.id.perioxi_select); // new
			i.setText(VAL1[position]);
		}
		return convertView;
	}

	private LayoutInflater mContext;

}
