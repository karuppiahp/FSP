package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.gradapp.au.homescreen.MyScheduleActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class MyScheduleTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	String univId, schoolId, roleId;
	String message = "Network speed is slow", status = "", timeZoneName;
	MyScheduleActivity context;
	ArrayList<HashMap<String, String>> surveyArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> dateArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> eventsArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> userDateArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> userEventsArray = new ArrayList<HashMap<String, String>>();
	Typeface typeFace, typeFaceLight;
	LinearLayout layoutForList;
	RelativeLayout layForCustomView, layForUserCustomView;
	DBAdapter db;
	boolean chkHeader = false;

	public MyScheduleTasks(MyScheduleActivity contexts, String univ,
			String school, String role,
			ArrayList<HashMap<String, String>> dateArrays,
			ArrayList<HashMap<String, String>> eventArrays, Typeface typeFaces,
			Typeface typeFaceLights, LinearLayout layoutForLists,
			RelativeLayout layForCustomViews, ProgressDialog dialogUserTasks,
			ArrayList<HashMap<String, String>> userdateArrays,
			ArrayList<HashMap<String, String>> usereventArrays,
			RelativeLayout layForUserCustomViews, String timezoneID) {
		univId = univ;
		schoolId = school;
		roleId = role;
		dateArray = dateArrays;
		eventsArray = eventArrays;
		context = contexts;
		typeFace = typeFaces;
		typeFaceLight = typeFaceLights;
		layoutForList = layoutForLists;
		layForCustomView = layForCustomViews;
		userDateArray = userdateArrays;
		userEventsArray = usereventArrays;
		layForUserCustomView = layForUserCustomViews;
		timeZoneName = timezoneID;
		db = new DBAdapter(context);
		dialog = dialogUserTasks;
		dialog.setMessage("Loading..");
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();

		dialog.show();
		dialog.setCancelable(false);
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("university_id", univId));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
		nameValuePairs.add(new BasicNameValuePair("role_id", roleId));
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(context)));
		nameValuePairs.add(new BasicNameValuePair("timezone", timeZoneName));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getEvents())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				dateArray.clear();
				eventsArray.clear();
				Constant.userEventArrayList.clear();
				Constant.userDateArrayList.clear();
				String status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray scheduledDateArray = jsonObj
							.getJSONArray("Scheduled Date");
					JSONObject eventJsonObj = jsonObj.getJSONObject("Event");
					//Date arraylist
					for (int i = 0; i < scheduledDateArray.length(); i++) {
						String currentArray = scheduledDateArray.get(i)
								.toString();
						HashMap<String, String> hashMapHeader = new HashMap<String, String>();
						hashMapHeader.put("header", currentArray);
						dateArray.add(hashMapHeader);

						JSONArray dateHeader = eventJsonObj
								.getJSONArray(scheduledDateArray.get(i)
										.toString());
						//Date as header 
						for (int j = 0; j < dateHeader.length(); j++) {
							String id = dateHeader.getJSONObject(j).getString(
									"id");
							String name = dateHeader.getJSONObject(j)
									.getString("name");
							String timeStarts = dateHeader.getJSONObject(j)
									.getString("time_of");
							String schoolid = dateHeader.getJSONObject(j)
									.getString("school_id");

							//currentArray is date which is added along with event items, because to separate the values according to the date
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
							hashMap.put("type", "normal");
							eventsArray.add(hashMap);
						}
					}

					//User added schedules
					JSONArray userScheduledDateArray = jsonObj
							.getJSONArray("User Scheduled Date");
					if (jsonObj.get("User Event") instanceof JSONObject) {
						JSONObject userEventJsonObj = jsonObj
								.getJSONObject("User Event");
						//Date array
						for (int i = 0; i < userScheduledDateArray.length(); i++) {
							String currentArray = userScheduledDateArray.get(i)
									.toString();
							HashMap<String, String> hashMapHeaders = new HashMap<String, String>();
							hashMapHeaders.put("header", currentArray);
							Constant.userDateArrayList.add(hashMapHeaders);
							for (int k = 0; k < dateArray.size(); k++) {
								//Check the date is equals
								if (currentArray.equals(dateArray.get(k).get(
										"header"))) {
									chkHeader = true;
									break;
								} else {
									chkHeader = false;
								}
							}

							if (chkHeader == false) {
								HashMap<String, String> hashMapHeader = new HashMap<String, String>();
								hashMapHeader.put("header", currentArray);
								dateArray.add(hashMapHeader);
							}

							JSONArray dateHeader = userEventJsonObj
									.getJSONArray(userScheduledDateArray.get(i)
											.toString());
							for (int j = 0; j < dateHeader.length(); j++) {
								String id = dateHeader.getJSONObject(j)
										.getString("id");
								String name = dateHeader.getJSONObject(j)
										.getString("name");
								String timeStarts = dateHeader.getJSONObject(j)
										.getString("time_of");
								String schoolid = dateHeader.getJSONObject(j)
										.getString("school_id");

								//currentArray is date which is added along with event items, because to separate the values according to the date
								String scheduleEventId = currentArray + ">"
										+ id;
								String scheduleEventName = currentArray + ">"
										+ name;
								String scheduleEventTimeStarts = currentArray
										+ ">" + timeStarts;
								String schoolId = currentArray + ">" + schoolid;

								HashMap<String, String> hashMap = new HashMap<String, String>();
								hashMap.put("id", scheduleEventId);
								hashMap.put("name", scheduleEventName);
								hashMap.put("timeStarts",
										scheduleEventTimeStarts);
								hashMap.put("schoolId", schoolId);
								hashMap.put("type", "user");
								eventsArray.add(hashMap);
								Constant.userEventArrayList.add(hashMap);
							}
						}
					}
					//Sort date arraylist
					Collections.sort(dateArray,
							new Comparator<HashMap<String, String>>() {

								@Override
								public int compare(HashMap<String, String> lhs,
										HashMap<String, String> rhs) {
									return lhs.get("header").compareTo(
											rhs.get("header"));
								}
							});

					//Sort events arraylist
					Collections.sort(eventsArray, new MapComparator(
							"timeStarts"));

					//Sort user date arraylist
					Collections.sort(Constant.userDateArrayList,
							new Comparator<HashMap<String, String>>() {

								@Override
								public int compare(HashMap<String, String> lhs,
										HashMap<String, String> rhs) {
									return lhs.get("header").compareTo(
											rhs.get("header"));
								}
							});
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
		dialog.dismiss();
		//Adapter class
		Utils.MyScheduleList(dateArray, eventsArray, context, typeFace,
				typeFaceLight, layoutForList, layForCustomView, userDateArray,
				userEventsArray, layForUserCustomView);
	}

	public class MapComparator implements Comparator<Map<String, String>> {
		private final String key;

		public MapComparator(String key) {
			this.key = key;
		}

		public int compare(Map<String, String> first, Map<String, String> second) {
			String firstValue = first.get(key);
			String secondValue = second.get(key);
			return firstValue.compareTo(secondValue);
		}
	}
}
