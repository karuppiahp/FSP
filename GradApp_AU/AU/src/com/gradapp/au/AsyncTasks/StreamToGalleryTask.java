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
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class StreamToGalleryTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	Activity activity;
	String msg = "", status = "", photoId, streamId, userFlag;
	
	public StreamToGalleryTask(Activity context, String photoid, String streamid, String userflag) {
		activity = context;
		photoId = photoid;
		streamId = streamid;
		userFlag = userflag;
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("id", photoId));
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("university_id", SessionStores.getUnivId(activity)));
		nameValuePairs.add(new BasicNameValuePair("stream_id", streamId));
		nameValuePairs.add(new BasicNameValuePair("user_flag", userFlag));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.streamToGallery())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		if(jsonObj != null) {
			try {
				msg = jsonObj.getString("message");
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
		if(msg.length() > 0) {
			//AlertDialog with OK button
			Utils.ShowAlert(activity, msg);
		}
	}

}
