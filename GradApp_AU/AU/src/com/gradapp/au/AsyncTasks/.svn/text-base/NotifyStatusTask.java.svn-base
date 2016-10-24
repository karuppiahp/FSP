package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class NotifyStatusTask extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity activity;
	String status;
	JSONObject jsonObj;
	
	public NotifyStatusTask(Activity context, String deviceStatus) {
		activity = context;
		status = deviceStatus;
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
	}
	
	@Override
	public void onPreExecute() {
		super.onPreExecute();
		dialog.show();
		dialog.setCancelable(false);
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("device_status", status));
		jsonObj = new ServerResponse(UrlGenerator.notifyStatus())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
	}

}
