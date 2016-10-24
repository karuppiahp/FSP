package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class HashTweetCountTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	Context activity;
	String msg = "", status = "", hashTagID, streamId;
	JSONObject jsonObj;

	public HashTweetCountTask(Context context, String hashid) {
		activity = context;
		hashTagID = hashid;
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("hash_id", hashTagID));
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		jsonObj = new ServerResponse(UrlGenerator.tweetCounts())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
	}

}
