package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.NotifyAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class NotificationTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity context;
	String univId, schoolId, amPm, message = "Network speed is slow",
			success = "";
	ArrayList<HashMap<String, String>> notifyArray = new ArrayList<HashMap<String, String>>();
	ListView listView;
	Typeface typeFaceLight, typeFaceHeader;
	DBAdapter db;

	public NotificationTasks(Activity activity, String univ, String school,
			ListView listViews,
			ArrayList<HashMap<String, String>> notificationArray,
			Typeface tpFaceLight, Typeface tpFaceHeader,
			ProgressDialog dialogUserTasks) {
		context = activity;
		univId = univ;
		schoolId = school;
		listView = listViews;
		notifyArray = notificationArray;
		typeFaceLight = tpFaceLight;
		typeFaceHeader = tpFaceHeader;
		db = new DBAdapter(activity);
		dialog = dialogUserTasks;
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("university_id", univId));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
		nameValuePairs.add(new BasicNameValuePair("role_id", SessionStores
				.getRoleType(context)));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getNotifications())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				message = jsonObj.getString("message");
				success = jsonObj.getString("success");
				if (success.equals("1")) {
					db.open();
					db.deleteNotifications();
					db.close();
					JSONArray jsonArray = jsonObj.getJSONArray("Notifications");
					for (int i = 0; i < jsonArray.length(); i++) {
						String id = jsonArray.getJSONObject(i).getString("id");
						String title = jsonArray.getJSONObject(i).getString(
								"title");
						String content = jsonArray.getJSONObject(i).getString(
								"content");
						String color = jsonArray.getJSONObject(i).getString(
								"color");
						String time = jsonArray.getJSONObject(i).getString(
								"time_of");

						String timeSplits[] = time.split(" ");
						String dateValue = timeSplits[0];
						String timeValue = timeSplits[1];
						String dateSplits[] = dateValue.split("-");
						String monthValue = dateSplits[1];
						String day = dateSplits[2];
						String month = Utils.MonthValue(monthValue);
						String timingSplits[] = timeValue.split(":");
						String hour = timingSplits[0];
						String minutes = timingSplits[1];
						int hrs = Integer.parseInt(hour);
						//hour calculation for AM/PM
						if (hrs >= 12) {
							hour = Utils.HourValue(hour);
							amPm = "PM";
						} else {
							amPm = "AM";
						}

						String timing = hour + ":" + minutes + " " + amPm;
						HashMap<String, String> hashValue = new HashMap<String, String>();
						hashValue.put("id", id);
						hashValue.put("title", title);
						hashValue.put("content", content);
						hashValue.put("color", color);
						hashValue.put("day", day);
						hashValue.put("month", month);
						hashValue.put("timing", timing);
						notifyArray.add(hashValue);//Arraylist for Notification

						db.open();
						db.insertToNotification(id, title, content, color, day,
								month, timing);
						db.close();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		if (success.length() > 0) {
			if (success.equals("1")) {
				//Adapter class
				listView.setAdapter(new NotifyAdapter(context, typeFaceLight,
						typeFaceHeader, notifyArray));
			} else {
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}
}
