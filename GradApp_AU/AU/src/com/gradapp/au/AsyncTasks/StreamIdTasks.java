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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.widget.ListView;

import com.gradapp.au.homescreen.CameraActivity;
import com.gradapp.au.support.CameraAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class StreamIdTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	CameraActivity context;
	ArrayList<HashMap<String, String>> streamArray = new ArrayList<HashMap<String, String>>();
	ListView listView;
	Typeface typeFace, typeFaceLight;
	String schoolIds;

	public StreamIdTasks(CameraActivity activity, String schoolids,
			ListView listview, Typeface typeface, Typeface typefaceLight,
			ArrayList<HashMap<String, String>> streamarray,
			ProgressDialog dialogUserTasks) {
		context = activity;
		listView = listview;
		typeFace = typeface;
		typeFaceLight = typefaceLight;
		schoolIds = schoolids;
		streamArray = streamarray;
		dialog = dialogUserTasks;
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(context)));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolIds));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getStreamId())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				String status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray photoStream = jsonObj
							.getJSONArray("Photo Stream");
					for (int i = 0; i < photoStream.length(); i++) {
						String streamName = photoStream.getJSONObject(i)
								.getString("stream_name");
						String streamId = photoStream.getJSONObject(i)
								.getString("stream_id");
						String streamDate = photoStream.getJSONObject(i)
								.getString("created_date");
						String schoolId = photoStream.getJSONObject(i)
								.getString("school_id");
						String imageUrl = photoStream.getJSONObject(i)
								.getString("image_url");

						String[] dateSplits = streamDate.split(" ");
						String date = dateSplits[0];
						String dateConvert = Utils.dayCalculation(date);

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("streamName", streamName);
						hashMap.put("streamId", streamId);
						hashMap.put("streamDate", dateConvert);
						hashMap.put("schoolId", schoolId);
						hashMap.put("imageUrl", imageUrl);
						streamArray.add(hashMap);//PhotoStream ArrayList
					}

					//Colleges stream
					JSONArray collegeStream = jsonObj
							.getJSONArray("College Stream");
					for (int i = 0; i < collegeStream.length(); i++) {
						String streamName = collegeStream.getJSONObject(i)
								.getString("stream_name");
						String streamId = collegeStream.getJSONObject(i)
								.getString("stream_id");
						String streamDate = collegeStream.getJSONObject(i)
								.getString("created_date");
						String schoolId = collegeStream.getJSONObject(i)
								.getString("school_id");
						String imageUrl = "";

						String[] dateSplits = streamDate.split(" ");
						String date = dateSplits[0];
						String dateConvert = Utils.dayCalculation(date);

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("streamName", streamName);
						hashMap.put("streamId", streamId);
						hashMap.put("streamDate", dateConvert);
						hashMap.put("schoolId", schoolId);
						hashMap.put("imageUrl", imageUrl);
						streamArray.add(hashMap);//Stream ArrayList
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
		//Adapter class calls
		listView.setAdapter(new CameraAdapter(context, typeFace, typeFaceLight,
				streamArray));
	}

}
