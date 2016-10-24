package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gradapp.au.homescreen.SearchScheduleActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class AddOtherSchedulesTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	SearchScheduleActivity activity;
	String eventId, msg = "", status = "";
	int position;
	ArrayList<HashMap<String, String>> eventsArray = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> nameArray = new ArrayList<HashMap<String, String>>();
	Typeface typeFace, typeFaceLight;
	LinearLayout layoutForList;
	RelativeLayout layForCustomView;
	
	public AddOtherSchedulesTask(SearchScheduleActivity context, String eventid, ArrayList<HashMap<String, String>> eventsArrays, int value, ArrayList<HashMap<String, String>> nameArrays, Typeface typeFaces, Typeface typeFaceLights, LinearLayout layoutSchedules, RelativeLayout layForCustomViews) {
		activity = context;
		eventId = eventid;
		eventsArray = eventsArrays;
		nameArray = nameArrays;
		position = value;
		typeFace = typeFaces;
		typeFaceLight = typeFaceLights;
		layoutForList = layoutSchedules;
		layForCustomView = layForCustomViews;
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
	}
	
	@Override
	public void onPreExecute() {
		super.onPreExecute();

		dialog.show();
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.addOtherSchedule())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				msg = jsonObj.getString("message");
				status = jsonObj.getString("status");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		if(status.length() > 0) {
			if(status.equals("1")) {
				//Event added successfully
				Toast.makeText(activity, "Event has been added", Toast.LENGTH_SHORT).show();
			} else {
				//Event is already added
				Toast.makeText(activity, "Event already added", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
