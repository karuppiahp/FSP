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
import android.widget.Toast;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class ImageUploadStream extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	Activity activity;
	String streamId, imageUrl, galleryId, status = "", msg = "";

	public ImageUploadStream(Activity context, String streamid,
			String imageurl, String galleryid) {
		activity = context;
		streamId = streamid;
		imageUrl = imageurl;
		galleryId = galleryid;
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
		if (imageUrl.contains("thumb_")) {
			imageUrl = imageUrl.replace("thumb_", "");
		}
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		dialog.show();
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("stream_id", streamId));
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		nameValuePairs.add(new BasicNameValuePair("author_id", SessionStores
				.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("image_url", imageUrl));// pass the image URL link
		nameValuePairs.add(new BasicNameValuePair("gallery_id", galleryId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.uploadToStream())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		if (jsonObj != null) {
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
		if (status.length() > 0) {
			Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
