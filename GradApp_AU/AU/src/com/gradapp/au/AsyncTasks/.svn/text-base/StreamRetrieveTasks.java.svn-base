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
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.GridView;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.support.StreamGridAdapter;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class StreamRetrieveTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity activity;
	String streamId;
	ArrayList<HashMap<String, String>> streamArray = new ArrayList<HashMap<String, String>>();
	GridView gridView;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public StreamRetrieveTasks(Activity screenActivity, String streamid,
			ArrayList<HashMap<String, String>> streamGridArray,
			GridView gridview, ImageLoader imageloader,
			DisplayImageOptions optionsDisplay) {
		activity = screenActivity;
		streamId = streamid;
		streamArray = streamGridArray;
		gridView = gridview;
		imageLoader = imageloader;
		options = optionsDisplay;
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
		nameValuePairs.add(new BasicNameValuePair("stream_id", streamId));
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.streamRetrieve())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				String status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray cameraImageArray = jsonObj
							.getJSONArray("Camera Screen");
					for (int i = 0; i < cameraImageArray.length(); i++) {
						String photoName = cameraImageArray.getJSONObject(i)
								.getString("Name");
						String photoId = cameraImageArray.getJSONObject(i)
								.getString("Photo Id");
						String photoUrl = cameraImageArray.getJSONObject(i)
								.getString("Image Url");
						String photoUserId = cameraImageArray.getJSONObject(i)
								.getString("Author Id");

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("photoName", photoName);
						hashMap.put("photoId", photoId);
						hashMap.put("photoUrl", photoUrl);
						hashMap.put("photoUserId", photoUserId);
						streamArray.add(hashMap);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		if (streamArray.size() > 0) {
			//GridView adapter class calls
			gridView.setAdapter(new StreamGridAdapter(imageLoader, streamArray,
					activity, options));
		} else {
			Utils.ShowAlertImage(activity, "There is no images found");
		}
	}
}
