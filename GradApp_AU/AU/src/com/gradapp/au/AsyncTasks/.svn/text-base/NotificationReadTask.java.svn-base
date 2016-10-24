package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.BadgeUtils;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class NotificationReadTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	Activity context;
	String notificationId, status = "", count = "";

	public NotificationReadTask(Activity mContext, String notifyId) {
		context = mContext;
		notificationId = notifyId;
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
		nameValuePairs.add(new BasicNameValuePair("notification_id",
				notificationId));
		nameValuePairs.add(new BasicNameValuePair("notification_read", "read"));
		JSONObject jsonObj = new ServerResponse(
				UrlGenerator.notificationReadStatus()).getJSONObjectfromURL(
				RequestType.POST, nameValuePairs);
		if (jsonObj != null) {
			try {
				status = jsonObj.getString("status");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		count = SessionStores.getUnreadCount(context);
		if (status.length() > 0) {
			if (status.equals("1")) {
				if (count != null) {
					// Read count
					if (count.length() > 0) {
						int finalCount = Integer.parseInt(count) - 1;
						SessionStores.saveUnreadCount("" + finalCount, context);
						BadgeUtils.setBadge(context, finalCount);
					} else if (count.length() == 0) {
						SessionStores.saveUnreadCount("0", context);
						BadgeUtils.setBadge(context, 0);
					}
				}
			}
		}
	}
}
