package com.fsp.blacksheep;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fsp.blacksheep.R;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	String[] VAL1, VAL2, VAL3;
	Context context;
	ViewHolder holder;
	LayoutInflater mInflater;
	int i = -1;
	int j = -1;
	static int k = 0;
	static int k1 = 0;
	String[] bufferarray = new String[422];
	static String[] zoombufferarray1 = new String[422];
	imageclass img = imageclass.getInstance();
	String Url2;
	String query, query1;
	public GridAdapter(Context context, String url) {
		try {
			Log.v("Url here in GA", url);

			Log.v("statup", Integer.toString(img.startup));
			mInflater = LayoutInflater.from(context);

			img.print_thumbimage_array();
			img.print_zoomimage_array();
Url2=url;
			

			List<Message_thbnail2> l_obj_tnail = new ArrayList<Message_thbnail2>();
			l_obj_tnail = parse_tnail2();
			// l_obj_zoom = dom.parse_zoom();

			VAL1 = new String[l_obj_tnail.size()];
			Iterator<Message_thbnail2> it = l_obj_tnail.iterator();

			Log.v("l_obj_tnail in ga", Integer.toString(l_obj_tnail.size()));
			while (it.hasNext()) {
				img.setThumbimage(it.next().toString());
			}
			img.print_thumbimage();
			bufferarray = img.ary_thumpimage();
			for (int s = 0; s < img.thumbcount(); s++) {
				Log.v("buffervalue from GA" + s, bufferarray[s]);
			}
		} catch (Exception e) {
		}

	}

	

	public int getCount() {
		// TODO Auto-generated method stub
		return img.thumbcount();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = mInflater.inflate(R.layout.picture_grid_item, null);

			holder = new ViewHolder();
			holder.icon = (ImageView) arg1.findViewById(R.id.jr_lb_list_icon);

			arg1.setTag(holder);
		} else {
			holder.icon = (ImageView) arg1.findViewById(R.id.jr_lb_list_icon);
		}
		if (holder.icon != null) {
			try {
				new Pictures_DrawableManager().fetchDrawableOnThread(
						bufferarray[arg0], holder.icon);
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.icon.setTag(null);
		} else {
			holder.icon.setImageResource(R.drawable.loading_bs);

			// Non-null tag means the view still needs to load it's data
			holder.icon.setTag(this);
			holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
		return arg1;
	}

	static class ViewHolder {
		ImageView icon;
	}

	protected void finalize() {
		img = null;

	}
	public List<Message_thbnail2> parse_tnail2() {
		// TODO Auto-generated method stub
		List<Message_thbnail2> messages = new ArrayList<Message_thbnail2>();
		String res=Parsing_JSON.readFeed(Url2);
		try {
			JSONObject job1=new JSONObject(res);
			String bl=job1.getString("blacksheep");
			JSONArray j_arr=new JSONArray(bl);
			JSONObject job2=j_arr.getJSONObject(1);
			String first_str=job2.getString("path");
			JSONArray j_arr2=new JSONArray(first_str);
			for (int i = 0; i < j_arr2.length(); i++) {
				Message_thbnail2 message = new Message_thbnail2();
				JSONObject inn_obj=j_arr2.getJSONObject(i);
				String thumb=inn_obj.getString("thumb");
				query = URLEncoder.encode(thumb, "utf-8");
				// //Log.v("Query",query);
				query1 = URLDecoder.decode(query);
				// //Log.v("Query1",query1);
				if (query1.contains("%20%20")) {
					message.setThumbnail(query1);
				} else {
					message.setThumbnail(thumb);
				}

				messages.add(message);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messages;
	}
}
