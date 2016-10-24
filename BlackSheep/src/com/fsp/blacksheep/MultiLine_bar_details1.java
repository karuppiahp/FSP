package com.fsp.blacksheep;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

class MultiLine_bar_details1 extends BaseAdapter {

	private Activity activity;
	public ImageLoaderOld imageLoader;
	static boolean mBusy = false;
	static View view_name;
	LayoutInflater mInflater;
	static String[] VALUE_eventname, VALUE_eventdate, VALUE_eventdesc,
			VALUE_eventid, VALUE_eventimg;
	static String[] VAL1;
	static String[] VAL2;
	static String[] VAL3;
	static URL img_value = null;
	private String[] VAL4;
	String name;
	String TAG = "MultiLine_bar_details";
	int i = -1;
	int j = -1;
	int k = -1;
	private TextView text1;
	private TextView text2;
	private TextView text3;
	private AsyncImageLoader asyncImageLoader;
	static ImageView img;
	static Bitmap mIcon11;
	public static ViewHolder holder;
	ListView lv;
	Context c;
	String Url;

	public MultiLine_bar_details1(Activity a, Context context, String url2,
			ListView listview) {

	

		c = context;
		mInflater = LayoutInflater.from(c);
		activity = a;
		imageLoader = new ImageLoaderOld(activity.getApplicationContext());
		asyncImageLoader = new AsyncImageLoader();
		Url = url2;
		// Log.v("***************Loader url**************", url2);

		int b = -1;
		int c = -1;
		int d = -1;
		int e2 = -1;
		int e1 = 0;

		// ParsingHandler obj_parse_eventname = new ParsingHandler(url2);
		List<Message_eventname> l_obj_eventname = new ArrayList<Message_eventname>();
		try {
			l_obj_eventname = parse_eventname();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			BS_Main.displayParsingAlert();
			e.printStackTrace();
		}
		VALUE_eventname = new String[l_obj_eventname.size()];
		Iterator<Message_eventname> it_eventname = l_obj_eventname.iterator();
		while (it_eventname.hasNext()) {
			b++;
			VALUE_eventname[b] = it_eventname.next().toString();
			// Log.v("***************VALUE_eventname[b]**************",
			// VALUE_eventname[b]);
		}

		List<Message_eventdate> l_obj_eventdate = new ArrayList<Message_eventdate>();
		try {
			l_obj_eventdate = parse_eventdate();
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			BS_Main.displayParsingAlert();
			e3.printStackTrace();
		}
		VALUE_eventdate = new String[l_obj_eventdate.size()];
		Iterator<Message_eventdate> it_eventdate = l_obj_eventdate.iterator();
		while (it_eventdate.hasNext()) {
			c++;
			VALUE_eventdate[c] = it_eventdate.next().toString();
			// Log.v("***************VALUE_eventdate[c]**************",VALUE_eventdate[c]);
		}
		List<Message_eventdesc> l_obj_eventdesc = new ArrayList<Message_eventdesc>();
		try {
			l_obj_eventdesc = parse_eventdesc();
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			BS_Main.displayParsingAlert();
			e3.printStackTrace();
		}
		VALUE_eventdesc = new String[l_obj_eventdesc.size()];
		Iterator<Message_eventdesc> it_eventdesc = l_obj_eventdesc.iterator();
		while (it_eventdesc.hasNext()) {
			d++;
			VALUE_eventdesc[d] = it_eventdesc.next().toString();
			// Log.v("***************VALUE_eventdesc[d]**************",VALUE_eventdesc[d]);
		}
		List<Message_eventid> l_obj_eventid = new ArrayList<Message_eventid>();
		try {
			l_obj_eventid = parse_eventid();
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			BS_Main.displayParsingAlert();
			e3.printStackTrace();
		}
		VALUE_eventid = new String[l_obj_eventid.size()];
		Iterator<Message_eventid> it_eventid = l_obj_eventid.iterator();
		while (it_eventid.hasNext()) {
			e2++;
			VALUE_eventid[e2] = it_eventid.next().toString();
			// Log.v("***************VALUE_eventid[e2]**************",VALUE_eventid[e2]);
		}

		List<Message_eventimg> l_obj_eventimg = new ArrayList<Message_eventimg>();
		try {
			l_obj_eventimg = parse_eventimg();
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			BS_Main.displayParsingAlert();
			e3.printStackTrace();
		}

		VALUE_eventimg = new String[l_obj_eventimg.size()];
		// Log.d("VALUE_eventimg",Integer.toString(VALUE_eventimg.length));
		Iterator<Message_eventimg> it_eventimg = l_obj_eventimg.iterator();
		while (it_eventimg.hasNext()) {

			VALUE_eventimg[e1] = it_eventimg.next().toString();
			// Log.d("",VALUE_eventimg[e1]);
			e1++;
			// Log.v("***************VALUE_eventimg[e1] **************",VALUE_eventimg[e1]
			// );
		}
		VAL1 = VALUE_eventname;
		VAL2 = VALUE_eventdate;
		VAL3 = VALUE_eventdesc;
		VAL4 = VALUE_eventimg;

		lv = listview;
		for (int n = 0; n < VALUE_eventimg.length; n++) {
			System.out.println("Passed Image Title" + VALUE_eventimg[n]);
		}

		// Log.v("VAL4 Image_Length++++2", Integer.toString(VAL4.length));

		lv.setOnScrollListener(new OnScrollListener() {

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					// Log.d("Multi Line Bar Details","3");
					mBusy = true;

					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					// Log.d("Multi Line Bar Details","4");
					mBusy = true;
					break;
				}
			}
		});

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

	public View getView(int arg0, View convertView, ViewGroup parent) {

		holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.bs_content_list_new,
					null);
			holder.text1 = (TextView) convertView
					.findViewById(R.id.text1_detail1);
			holder.text2 = (TextView) convertView
					.findViewById(R.id.text3_detail1);
			convertView.setTag(holder);

		} else {
			holder.text1 = (TextView) convertView
					.findViewById(R.id.text1_detail1);
			holder.text2 = (TextView) convertView
					.findViewById(R.id.text3_detail1);
			holder = (ViewHolder) convertView.getTag();
		}

		System.out.println("Arg0" + VAL1[arg0]);
		holder.text1.setText(VAL1[arg0]);
		holder.text2.setText(VAL3[arg0]);
		Typeface type1 = Typeface.createFromAsset(c.getAssets(), "Mission Gothic Bold.otf");
		holder.text2.setTypeface(type1);
		holder.text1.setTypeface(type1);
		return convertView;
	}

	static class ViewHolder {
		TextView text1, text2, text3, text4;
//		ImageView icon;

	}

	private List<Message_eventimg> parse_eventimg() {
		// TODO Auto-generated method stub
		List<Message_eventimg> messages = new ArrayList<Message_eventimg>();
		String res = Parsing_JSON.readFeed(Url);
		Log.v("bar details res >>", res);
		try {
			JSONObject obj1 = new JSONObject(res);
			if(obj1 != null) {
				String first_str = obj1.getString("blacksheep");
				JSONArray j_arr1 = new JSONArray(first_str);
	
				JSONObject inn_obj1 = j_arr1.getJSONObject(1);
				String mob_adds = inn_obj1.getString("mobileads");
				Log.v("mob adds >>", mob_adds);
				JSONObject add_obj = new JSONObject(mob_adds);
				String add_url = add_obj.getString("masterdailyadURL");
				Log.v("add url ", add_url);
				JSONObject inn_obj2 = j_arr1.getJSONObject(2);
				String bar_event = inn_obj2.getString("event");
				JSONArray arr_event = new JSONArray(bar_event);
				for (int i = 0; i < arr_event.length(); i++) {
					Message_eventimg message = new Message_eventimg();
					JSONObject obj_event = arr_event.getJSONObject(i);
					String event_name = obj_event.getString("eventname");
					String event_id = obj_event.getString("eventid");
					String event_desc = obj_event.getString("eventdescription");
					String event_img = obj_event.getString("eventimage");
					String event_dt = obj_event.getString("eventDate");
					Log.v("event_name", event_name);
	
					message.set_eventimg(event_img);
	
					messages.add(message);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}

	private List<Message_eventid> parse_eventid() {

		List<Message_eventid> messages = new ArrayList<Message_eventid>();
		String res = Parsing_JSON.readFeed(Url);
		Log.v("bar details res >>", res);
		try {
			JSONObject obj1 = new JSONObject(res);
			if(obj1 != null) {
				String first_str = obj1.getString("blacksheep");
				JSONArray j_arr1 = new JSONArray(first_str);
	
				JSONObject inn_obj1 = j_arr1.getJSONObject(1);
				String mob_adds = inn_obj1.getString("mobileads");
				Log.v("mob adds >>", mob_adds);
				JSONObject add_obj = new JSONObject(mob_adds);
				String add_url = add_obj.getString("masterdailyadURL");
				Log.v("add url ", add_url);
				JSONObject inn_obj2 = j_arr1.getJSONObject(2);
				String bar_event = inn_obj2.getString("event");
				JSONArray arr_event = new JSONArray(bar_event);
				for (int i = 0; i < arr_event.length(); i++) {
					Message_eventid message = new Message_eventid();
					JSONObject obj_event = arr_event.getJSONObject(i);
					String event_name = obj_event.getString("eventname");
					String event_id = obj_event.getString("eventid");
					String event_desc = obj_event.getString("eventdescription");
					String event_img = obj_event.getString("eventimage");
					String event_dt = obj_event.getString("eventDate");
					Log.v("event_name", event_name);
					message.set_eventid(event_id);
	
					messages.add(message);
	
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;
	}

	public List<Message_eventdesc> parse_eventdesc() {
		// TODO Auto-generated method stub
		List<Message_eventdesc> messages = new ArrayList<Message_eventdesc>();
		String res = Parsing_JSON.readFeed(Url);
		Log.v("bar details res >>", res);
		try {
			JSONObject obj1 = new JSONObject(res);
			if(obj1 != null) {
				String first_str = obj1.getString("blacksheep");
				JSONArray j_arr1 = new JSONArray(first_str);
	
				JSONObject inn_obj1 = j_arr1.getJSONObject(1);
				String mob_adds = inn_obj1.getString("mobileads");
				Log.v("mob adds >>", mob_adds);
				JSONObject add_obj = new JSONObject(mob_adds);
				String add_url = add_obj.getString("masterdailyadURL");
				Log.v("add url ", add_url);
				JSONObject inn_obj2 = j_arr1.getJSONObject(2);
				String bar_event = inn_obj2.getString("event");
				JSONArray arr_event = new JSONArray(bar_event);
				for (int i = 0; i < arr_event.length(); i++) {
					Message_eventdesc message = new Message_eventdesc();
					JSONObject obj_event = arr_event.getJSONObject(i);
					String event_name = obj_event.getString("eventname");
					String event_id = obj_event.getString("eventid");
					String event_desc = obj_event.getString("eventdescription");
					String event_img = obj_event.getString("eventimage");
					String event_dt = obj_event.getString("eventDate");
					Log.v("event_name", event_name);
					message.set_eventdesc(event_desc);
					messages.add(message);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;

	}

	public List<Message_eventdate> parse_eventdate() {

		List<Message_eventdate> messages = new ArrayList<Message_eventdate>();

		String res = Parsing_JSON.readFeed(Url);
		Log.v("bar details res >>", res);
		try {
			JSONObject obj1 = new JSONObject(res);
			if(obj1 != null) {
				String first_str = obj1.getString("blacksheep");
				JSONArray j_arr1 = new JSONArray(first_str);
	
				JSONObject inn_obj1 = j_arr1.getJSONObject(1);
				String mob_adds = inn_obj1.getString("mobileads");
				Log.v("mob adds >>", mob_adds);
				JSONObject add_obj = new JSONObject(mob_adds);
				String add_url = add_obj.getString("masterdailyadURL");
				Log.v("add url ", add_url);
				JSONObject inn_obj2 = j_arr1.getJSONObject(2);
				String bar_event = inn_obj2.getString("event");
				JSONArray arr_event = new JSONArray(bar_event);
				for (int i = 0; i < arr_event.length(); i++) {
					Message_eventdate message = new Message_eventdate();
					JSONObject obj_event = arr_event.getJSONObject(i);
					String event_name = obj_event.getString("eventname");
					String event_id = obj_event.getString("eventid");
					String event_desc = obj_event.getString("eventdescription");
					String event_img = obj_event.getString("eventimage");
					String event_dt = obj_event.getString("eventDate");
					Log.v("event_dt", event_dt);
	
					message.set_eventdate(event_dt);
					messages.add(message);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;
	}

	public List<Message_eventname> parse_eventname() {

		// TODO Auto-generated method stub
		List<Message_eventname> messages = new ArrayList<Message_eventname>();
		Log.v("The", "url is=>" + Url);

		String res = Parsing_JSON.readFeed(Url);
		Log.v("bar details res >>", res);
		try {
			JSONObject obj1 = new JSONObject(res);
			if(obj1 != null) {
				String first_str = obj1.getString("blacksheep");
				JSONArray j_arr1 = new JSONArray(first_str);
	
				JSONObject inn_obj1 = j_arr1.getJSONObject(1);
				String mob_adds = inn_obj1.getString("mobileads");
				Log.v("mob adds >>", mob_adds);
				JSONObject add_obj = new JSONObject(mob_adds);
				String add_url = add_obj.getString("masterdailyadURL");
				Log.v("add url ", add_url);
				JSONObject inn_obj2 = j_arr1.getJSONObject(2);
				String bar_event = inn_obj2.getString("event");
				JSONArray arr_event = new JSONArray(bar_event);
				for (int i = 0; i < arr_event.length(); i++) {
					Message_eventname message = new Message_eventname();
					JSONObject obj_event = arr_event.getJSONObject(i);
					String event_name = obj_event.getString("eventname");
					String event_id = obj_event.getString("eventid");
					String event_desc = obj_event.getString("eventdescription");
					String event_img = obj_event.getString("eventimage");
					String event_dt = obj_event.getString("eventDate");
					Log.v("event_name", event_name);
	
					message.set_eventname(event_name);
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