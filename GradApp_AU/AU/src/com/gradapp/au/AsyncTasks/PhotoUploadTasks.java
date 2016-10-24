package com.gradapp.au.AsyncTasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import com.gradapp.au.activities.PhotoScreenOptions;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class PhotoUploadTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	PhotoScreenOptions context;
	String name, streamId, result = "", msg = "";
	Bitmap image;
	JSONObject jObject;

	public PhotoUploadTasks(PhotoScreenOptions mActivity, String photoName,
			String photoStreamId, Bitmap rotated) {
		context = mActivity;
		name = photoName;
		streamId = photoStreamId;
		image = rotated;
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading..");

	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		dialog.show();
		dialog.setCancelable(false);
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			image.compress(CompressFormat.PNG, 100, bos);
			byte[] data = bos.toByteArray();
			// Client setup
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(UrlGenerator.photoUpload());
			ByteArrayBody bab = new ByteArrayBody(data, ".png");
			// Image file send through MultipartEntity
			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("name", new StringBody(name));
			reqEntity.addPart("stream_id", new StringBody(streamId));
			reqEntity.addPart("author_id",
					new StringBody(SessionStores.getUserId(context)));
			reqEntity.addPart("university_id",
					new StringBody(SessionStores.getUnivId(context)));
			reqEntity.addPart("image_url", bab);
			postRequest.setEntity(reqEntity);
			BasicResponseHandler resHandler = new BasicResponseHandler();
			result = httpClient.execute(postRequest, resHandler);
		} catch (UnsupportedEncodingException e) {
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			SystemClock.sleep(1500);
		} catch (Exception e) {
		}

		try {
			// Json response
			jObject = new JSONObject(result);
		} catch (JSONException e) {
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		if (jObject != null) {
			try {
				msg = jObject.getString("msg");
				Toast.makeText(context, "" + msg, Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, "Network connection is not available",
					Toast.LENGTH_LONG).show();
		}
	}

}
