package com.gradapp.au.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;

import com.gradapp.au.homescreen.SocialMediaActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class TwitterTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	SocialMediaActivity context;
	ListView listView, listViewDailog;
	int paging_count;
	String message, status, hashTag, schoolId, hashTagId;
	ArrayList<HashMap<String, String>> hashTagArray = new ArrayList<HashMap<String, String>>();
	Dialog dialogWindow;
	EditText editText;

	public TwitterTasks(SocialMediaActivity mContext, ListView listview,
			int pagingCount, String schoolids, ProgressDialog dialogUserTasks,
			ListView listViewDialog, Dialog dialog2,
			ArrayList<HashMap<String, String>> hashtagArray, EditText edittext) {
		context = mContext;
		listView = listview;
		listViewDailog = listViewDialog;
		paging_count = pagingCount;
		schoolId = schoolids;
		dialog = dialogUserTasks;
		hashTagArray = hashtagArray;
		dialogWindow = dialog2;
		dialog.setMessage("Loading..");
		editText = edittext;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();

		dialog.show();
		dialog.setCancelable(false);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(context)));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
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
						hashTagId = jsonArray.getJSONObject(i)
								.getString("id");
						String schoolId = jsonArray.getJSONObject(i).getString(
								"School_id");

						if(i == 0) {
							Constant.hashTagValue = hashTag;
						} else {
							Constant.hashTagValue += " "+hashTag;
						}
						
						Constant.hashTagId = hashTagId;

						//HashTag values are fetched and saved in arraylist
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("hashTag", hashTag);
						hashMap.put("schoolId", schoolId);
						hashTagArray.add(hashMap);
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
		if (hashTagArray.size() > 0) {
			//HashTag has been passed to GetGradAppTweets class to fetch the corresponding feeds.
			new GetGradAppTweets(context, listView, paging_count,
					Constant.hashTagValue, editText, "", "", hashTagArray).execute();
		}
	}
}
