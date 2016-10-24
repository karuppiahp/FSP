package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gradapp.au.homescreen.MyScheduleEventScreen;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class RSVPTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	MyScheduleEventScreen context;
	String eventId, rsvpType, message = "Network speed is slow", status = "";

	public RSVPTasks(MyScheduleEventScreen activity, String eventsId,
			String rsvp) {
		context = activity;
		eventId = eventsId;
		rsvpType = rsvp;
		dialog = new ProgressDialog(context);
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(context)));
		nameValuePairs.add(new BasicNameValuePair("event_id", eventId));
		nameValuePairs.add(new BasicNameValuePair("rsvp", rsvpType));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.rsvpDetails())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				message = jsonObj.getString("message");
				status = jsonObj.getString("status");
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
		if (status.length() > 0) {
			if (status.equals("1")) {
				// Sucess message
				Toast.makeText(context, "Successfully updated",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			// Failiure message
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}
}
