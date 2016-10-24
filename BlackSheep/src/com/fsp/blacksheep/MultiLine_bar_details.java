package com.fsp.blacksheep;










import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class MultiLine_bar_details extends BaseAdapter {

	LayoutInflater mInflater;
	String[] VAL1, VAL2, VAL3;
	String name;
	String TAG = "MultiLine_img1";
	int i = -1;
	int j = -1;
	int k = -1;

	public MultiLine_bar_details(Context context, String[] str1, String[] str2,
			String[] str3) {
		mInflater = LayoutInflater.from(context);
		VAL1 = str1;
		VAL2 = str2;
		VAL3 = str3;
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
						R.layout.bs_content_list_item, null);
				obj_text_val = new TextValue();
				obj_text_val.text1 = (TextView) text_view_name
						.findViewById(R.id.text1);
				obj_text_val.text2 = (TextView) text_view_name
						.findViewById(R.id.text2);
				// obj_text_val.text3 = (TextView) text_view_name
				// .findViewById(R.id.text3);
				text_view_name.setTag(obj_text_val);
			} else {
				obj_text_val = (TextValue) text_view_name.getTag();
			}

			obj_text_val.text1.setText(VAL1[arg0]);
			// obj_text_val.text2.setText(VAL2[arg0]);
			obj_text_val.text3.setText(VAL3[arg0]);

		} catch (ArrayIndexOutOfBoundsException ae) {
		} catch (Exception e) {
			name = "Exception in getView";
			Log.v(TAG, name + e);
		}
		return text_view_name;
	}

	static class TextValue {
		TextView text1, text2, text3;

	}
}