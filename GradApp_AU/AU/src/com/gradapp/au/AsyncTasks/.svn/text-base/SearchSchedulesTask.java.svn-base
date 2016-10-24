package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gradapp.au.homescreen.SearchScheduleActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class SearchSchedulesTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	SearchScheduleActivity activity;
	ArrayList<HashMap<String, String>> nameArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventsArray = new ArrayList<HashMap<String, String>>();
	Typeface typeFace, typeFaceLight;
	LinearLayout layoutForList;
	RelativeLayout layForCustomView;
	String timeZoneId;

	public SearchSchedulesTask(SearchScheduleActivity context,
			ArrayList<HashMap<String, String>> nameArrays,
			ArrayList<HashMap<String, String>> eventArrays, Typeface typeFaces,
			Typeface typeFaceLights, LinearLayout layoutForLists,
			RelativeLayout layForCustomViews, String timeZone) {
		activity = context;
		nameArray = nameArrays;
		eventsArray = eventArrays;
		typeFace = typeFaces;
		typeFaceLight = typeFaceLights;
		layoutForList = layoutForLists;
		layForCustomView = layForCustomViews;
		timeZoneId = timeZone;
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		dialog.show();
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		nameValuePairs.add(new BasicNameValuePair("timezone", timeZoneId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.searchSchedule())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				String status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray scheduledNameArray = jsonObj
							.getJSONArray("Scheduled firstname");
					JSONObject eventJsonObj = jsonObj.getJSONObject("Event");
					for (int i = 0; i < scheduledNameArray.length(); i++) {
						String currentArray = scheduledNameArray.get(i)
								.toString();
						HashMap<String, String> hashMapHeader = new HashMap<String, String>();
						hashMapHeader.put("header", currentArray);
						nameArray.add(hashMapHeader);// Header arraylist

						JSONArray dateHeader = eventJsonObj
								.getJSONArray(scheduledNameArray.get(i)
										.toString());
						for (int j = 0; j < dateHeader.length(); j++) {
							String id = dateHeader.getJSONObject(j).getString(
									"id");
							String name = dateHeader.getJSONObject(j)
									.getString("name");
							String timeStarts = dateHeader.getJSONObject(j)
									.getString("time_of");
							String schoolid = dateHeader.getJSONObject(j)
									.getString("school_id");

							// currentArray is header has been added along with
							// events, for set values at the time of list
							String scheduleEventId = currentArray + ">" + id;
							String scheduleEventName = currentArray + ">"
									+ name;
							String scheduleEventTimeStarts = currentArray + ">"
									+ timeStarts;
							String schoolId = currentArray + ">" + schoolid;
							
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("id", scheduleEventId);
							hashMap.put("name", scheduleEventName);
							hashMap.put("timeStarts", scheduleEventTimeStarts);
							hashMap.put("schoolId", schoolId);
							eventsArray.add(hashMap);
						}
					}
				}
			} else {
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		Utils.SearchScheduleList(nameArray, eventsArray, activity, typeFace,
				typeFaceLight, layoutForList, layForCustomView);
		dialog.dismiss();
	}
}
