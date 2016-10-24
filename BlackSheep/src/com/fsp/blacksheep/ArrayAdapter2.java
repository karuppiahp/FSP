package com.fsp.blacksheep;



import com.fsp.blacksheep.R;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class ArrayAdapter2 extends BaseAdapter {

	LayoutInflater mInflater;
	String[] VAL1, VAL2, VAL3;
	String name;
	String TAG = "MultiLine_img1";
	int i = -1;
	Context c;

	public ArrayAdapter2(Context context, String[] url) {
		c=context;
		mInflater = LayoutInflater.from(c);

		VAL1 = url;

	}

	public int getCount() {
		return VAL1.length;
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View text_view_name, ViewGroup arg2) {
		try {
			TextValue obj_text_val;
			if (text_view_name == null) {
				text_view_name = mInflater.inflate(
						R.layout.bars_array_list_item, null);
				obj_text_val = new TextValue();
				obj_text_val.text1 = (TextView) text_view_name
						.findViewById(R.id.bars_arr_text1);

				text_view_name.setTag(obj_text_val);
			} else {
				obj_text_val = (TextValue) text_view_name.getTag();
			}

			obj_text_val.text1.setText(VAL1[arg0]);
			Typeface type1 = Typeface
					.createFromAsset(c.getAssets(), "CORBEL.TTF");
			obj_text_val.text1.setTypeface(type1);
		} catch (ArrayIndexOutOfBoundsException ae) {
		} catch (Exception e) {
			name = "Exception in getView";
			//Log.v(TAG, name + e);
		}
		return text_view_name;
	}

	static class TextValue {
		TextView text1;

	}
}
