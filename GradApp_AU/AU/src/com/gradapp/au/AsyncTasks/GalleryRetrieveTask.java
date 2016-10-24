package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.gradapp.au.activities.StreamScreenGridActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.support.StreamGridAdapter;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GalleryRetrieveTask extends AsyncTask<Void, Void, Boolean> {

	ProgressDialog dialog;
	StreamScreenGridActivity activity;
	String msg = "", status = "";
	ArrayList<HashMap<String, String>> galleryArrayList = new ArrayList<HashMap<String, String>>();
	ImageLoader imageLoader;
	DisplayImageOptions options;
	GridView gridView;

	public GalleryRetrieveTask(StreamScreenGridActivity context,
			GridView gridview, ArrayList<HashMap<String, String>> galleryArray,
			ImageLoader imageloader, DisplayImageOptions optionsDisplay) {
		activity = context;
		gridView = gridview;
		galleryArrayList = galleryArray;
		imageLoader = imageloader;
		options = optionsDisplay;
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		Log.i("user id", "" + SessionStores.getUserId(activity));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.galleryRetrieve())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				msg = jsonObj.getString("message");
				status = jsonObj.getString("status");
				JSONArray galleryArray = jsonObj.getJSONArray("UserGallery");
				for (int i = 0; i < galleryArray.length(); i++) {
					String galleryId = galleryArray.getJSONObject(i).getString(
							"Gallery_id");
					String imageUrl = galleryArray.getJSONObject(i).getString(
							"Gallery url");
					String userId = galleryArray.getJSONObject(i).getString(
							"User id");

					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("photoName", "");
					hashMap.put("photoId", galleryId);
					hashMap.put("photoUrl", imageUrl);
					hashMap.put("photoUserId", userId);
					galleryArrayList.add(hashMap);
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
		if (galleryArrayList.size() > 0) {
			//GridView adapter class
			gridView.setAdapter(new StreamGridAdapter(imageLoader,
					galleryArrayList, activity, options));
		} else {
			Utils.ShowAlertImage(activity, "There is no images found");
		}
	}

}
