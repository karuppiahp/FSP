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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class TwitterHashTagTask extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity context;
	ListView listView, listViewDailog;
	int paging_count;
	String message, status, hashTag, schoolId, hashTagId;
	ArrayList<HashMap<String, String>> hashTagArray = new ArrayList<HashMap<String, String>>();
	Dialog dialogWindow;
	EditText editText;

	public TwitterHashTagTask(Activity mContext, ProgressDialog dialogUserTasks) {
		context = mContext;
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
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(context)));
		nameValuePairs.add(new BasicNameValuePair("school_id", SessionStores
				.getSchoolId(context)));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getHashTag())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				message = jsonObj.getString("msg");
				status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray jsonArray = jsonObj.getJSONArray("Hastag");
					for (int i = 0; i < jsonArray.length(); i++) {
						hashTag = jsonArray.getJSONObject(i)
								.getString("Hastag");
						hashTagId = jsonArray.getJSONObject(i).getString("id");
						//HashTag saved in Constant to be used by any classes.
						if (i == 0) {
							Constant.hashTagValue = hashTag;
						} else {
							Constant.hashTagValue += " " + hashTag;
						}
						Constant.hashTagId = hashTagId;
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
	}
}
