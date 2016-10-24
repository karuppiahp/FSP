package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gradapp.au.activities.StreamScreenGridActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class StreamPhotoRemoveTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	Activity activity;
	String msg = "", status = "", photoId, streamId;
	
	public StreamPhotoRemoveTask(Activity context, String photoid, String streamid) {
		activity = context;
		photoId = photoid;
		streamId = streamid;
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("photo_id", photoId));
		nameValuePairs.add(new BasicNameValuePair("university_id", SessionStores.getUnivId(activity)));
		nameValuePairs.add(new BasicNameValuePair("stream_id", streamId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.streamPhotoRemove())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		if(jsonObj != null) {
			try {
				status = jsonObj.getString("status");
				msg = jsonObj.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		if(status.length() > 0) {
			if(status.equals("1")) {
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
				//Intent to StreamScreenGridActivity
				Intent intentToGrid = new Intent(activity, StreamScreenGridActivity.class);
				intentToGrid.putExtra("from", "stream");
				intentToGrid.putExtra("streamId", streamId);
				activity.startActivity(intentToGrid);
				activity.finish();
			} else {
				Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
