package com.fsp.blacksheep;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fsp.blacksheep.R;







import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

class ArrayAdapter1 extends BaseAdapter {

	LayoutInflater mInflater;
	String[] VAL1, VAL2, VAL3;
	String name;
	String TAG = "MultiLine_img1";
	int i = -1;
	Context context;
	String Url1,size1, dayOfWeek, selectedDay;
	int selected_Value;
	ArrayList<String> daysArray = new ArrayList<String>();
	
	public ArrayAdapter1(Context cntxt,
			ArrayList<String> daysArray, String dayOfWeek, String selectedDay) {
		context = cntxt;
		this.daysArray = daysArray;
		this.dayOfWeek = dayOfWeek;
		this.selectedDay = selectedDay;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		Log.e("Adapter size????",""+daysArray.size());
		return daysArray.size();
	}

	public Object getItem(int arg0) {
		return arg0;
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View text_view_name, ViewGroup arg2) {
		Log.e("Days>>>>",""+daysArray.get(0));
			TextValue obj_text_val;
			RelativeLayout obj_Rel;
			if (text_view_name == null) {
				text_view_name = mInflater.inflate(
						R.layout.bars_array_list_item, null);
				obj_text_val = new TextValue();
				obj_text_val.text1 = (TextView) text_view_name
						.findViewById(R.id.bars_arr_text1);
				obj_Rel = (RelativeLayout) text_view_name
						.findViewById(R.id.daterelative);
				
				text_view_name.setTag(obj_text_val);
			} else {
				obj_text_val = (TextValue) text_view_name.getTag();
			}
						
			
			Log.e("Days>>>>",""+daysArray.size());
			
			if(dayOfWeek.equals(daysArray.get(arg0))) {
				if(selectedDay.equals(daysArray.get(arg0))) {
					obj_text_val.text1.setText("");
					obj_text_val.text1.setBackgroundResource(R.drawable.todaybg);
				} else {
					obj_text_val.text1.setText("TODAY");
				}
			} else {
				if(selectedDay.equals(daysArray.get(arg0))) {
					obj_text_val.text1.setText(daysArray.get(arg0));
					obj_text_val.text1.setBackgroundResource(R.drawable.withouttoday);
				} else {
					obj_text_val.text1.setText(daysArray.get(arg0));
				}
			}
			
		return text_view_name;
	}

	static class TextValue {
		TextView text1;

	}
	
	public List<Message_bar_date> parse_bar_date() {
		// TODO Auto-generated method stub
		List<Message_bar_date> messages = new ArrayList<Message_bar_date>();
		Log.e("Date List URL value 222222222",Url1);
		try {
			String res=Parsing_JSON.readFeed(Url1);
			JSONObject job1= new JSONObject(res);
			if(job1 != null) {
				String f_str=job1.getString("BarListingDate");
				//Log.v("f_str >>",f_str);
				JSONArray j_arr=new JSONArray(f_str);
				for (int i = 0; i < j_arr.length(); i++) {
					Message_bar_date message = new Message_bar_date();
					JSONObject inner_obj=j_arr.getJSONObject(i);
					String bar_date_id=inner_obj.getString("DateId");
					String bar_date=inner_obj.getString("BarDate") +" ,"+bar_date_id;
					
					message.set_bar_date(bar_date);
					messages.add(message);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return messages;
	}
}
