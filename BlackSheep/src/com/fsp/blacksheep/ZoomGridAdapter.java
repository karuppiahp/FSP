package com.fsp.blacksheep;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

public class ZoomGridAdapter {
	static String[] VAL1, VAL2, VAL3;
	Context context;
	LayoutInflater mInflater;
	int i = -1;
	int j = -1;
	static int k = 0;
	static int k1 = 0;
	static String[] bufferarray = new String[422];
	static String[] zoombufferarray = new String[422];
	imageclass img = imageclass.getInstance();
String Url;
	public String[] getZoomGridAdapter(Context context, String url) {

		Log.v("Url herein ZoomGA", url);
		mInflater = LayoutInflater.from(context);
		Url=url;
		
	//	ParsingHandler dom = new ParsingHandler(url);

		List<Message_zoom> l_obj_zoom = new ArrayList<Message_zoom>();
		
			try {
				l_obj_zoom = parse_zoom();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				BS_Main.displayParsingAlert();
				e.printStackTrace();
			}
	
			// TODO Auto-generated catch block
		
			

		VAL1 = new String[l_obj_zoom.size()];
		Iterator<Message_zoom> it = l_obj_zoom.iterator();

		while (it.hasNext()) {

			img.setZoomimage(it.next().toString());
		}
		zoombufferarray = img.ary_zoomimage();
		for (int s = 0; s < img.ary_zoomimage().length; s++) {
			Log.v("zoombuffervalue from ZGA" + s, zoombufferarray[s]);
		}

		return zoombufferarray;

	}
	private List<Message_zoom> parse_zoom() {
		// TODO Auto-generated method stub
		List<Message_zoom> zoommessages = new ArrayList<Message_zoom>();
		String res=Parsing_JSON.readFeed(Url);
		try {
			JSONObject job1=new JSONObject(res);
			String bl=job1.getString("blacksheep");
			JSONArray j_arr=new JSONArray(bl);
			JSONObject job2=j_arr.getJSONObject(1);
			String first_str=job2.getString("path");
			JSONArray j_arr2=new JSONArray(first_str);
			for (int i = 0; i < j_arr2.length(); i++) {
				Message_zoom zoommessage = new Message_zoom();
				JSONObject inn_obj=j_arr2.getJSONObject(i);
				String zoom=inn_obj.getString("zoom");
				zoommessage.setZoom(zoom);
				// //Log.v("zoom",message.toString1());
				zoommessages.add(zoommessage);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zoommessages;
	}

}
