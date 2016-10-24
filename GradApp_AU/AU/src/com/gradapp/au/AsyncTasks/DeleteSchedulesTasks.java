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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.activities.EditSchedulesActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class DeleteSchedulesTasks extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	EditSchedulesActivity activity;
	String eventId, msg = "", status = "", name;
	int position, date = -1, current = 0, count = 0;;
	ArrayList<HashMap<String, String>> eventsArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> nameArray = new ArrayList<HashMap<String, String>>();
	Typeface typeFace, typeFaceLight;
	LinearLayout layoutForList;
	RelativeLayout layForCustomView;
	TextView textForEdit;
	boolean arrayValue = false;
	ArrayList<String> countArray = new ArrayList<String>();

	public DeleteSchedulesTasks(EditSchedulesActivity context, String eventid,
			int value, Typeface typeFaces, Typeface typeFaceLights,
			LinearLayout layoutSchedules, RelativeLayout layForCustomViews,
			TextView textForEditSchedules) {
		activity = context;
		eventId = eventid;
		position = value;
		typeFace = typeFaces;
		typeFaceLight = typeFaceLights;
		layoutForList = layoutSchedules;
		layForCustomView = layForCustomViews;
		textForEdit = textForEditSchedules;
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
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.userEventDelete())
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
		if (status.length() > 0) {
			if (status.equals("1")) {
				//Remove the particular event from arraylist
				Constant.userEventArrayList.remove(position);
				for (int k = 0; k < Constant.userDateArrayList.size(); k++) {
					count = 0;
					for (int i = 0; i < Constant.userEventArrayList.size(); i++) {
						if (Constant.userEventArrayList
								.get(i)
								.get("name")
								.contains(
										Constant.userDateArrayList.get(k).get(
												"header"))) {
							count++;
						}
					}

					countArray.add("" + count);
				}

				for (int i = 0; i < Constant.userDateArrayList.size(); i++) {
					String countValue = countArray.get(i);
					if (Integer.parseInt(countValue) == 0) {
						//Remove the particular event date from arraylist
						Constant.userDateArrayList.remove(i);
						break;
					}
				}
				if (Constant.userEventArrayList.size() > 0) {
					//remove all the children views from layout
					layoutForList.removeAllViews();
					//again the views are loaded according to the current arraylist values.
					Utils.UserScheduleList(Constant.userEventArrayList,
							activity, typeFace, typeFaceLight, layoutForList,
							layForCustomView, "delete");
				} else {
					layoutForList.removeAllViews();
					textForEdit.setVisibility(View.VISIBLE);
				}
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
