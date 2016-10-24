package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gradapp.au.activities.PhotoScreenOptions;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.UrlGenerator;

public class ReportTasks extends AsyncTask<Void, Void, Boolean>{
	ProgressDialog dialog;
	PhotoScreenOptions context;
	String reporterId, photoId, photoUserId, reason, message = "Network speed is slow", status = "";
	
	
	public ReportTasks(PhotoScreenOptions mContext, String reporterid, String photoid, String photoUserid, String reasonTxt) {
		context = mContext;
		reporterId = reporterid;
		photoId = photoid;
		photoUserId = photoUserid;
		reason = reasonTxt;
		Log.i("reporterid",""+reporterId);
		Log.i("photoId",""+photoId);
		Log.i("photoUserId",""+photoUserId);
		Log.i("reason",""+reason);
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading..");
	}
	
	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	public void onPreExecute(){
		super.onPreExecute();
        dialog.show();
		dialog.setCancelable(false);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("reporter_id", reporterId));
		nameValuePairs.add(new BasicNameValuePair("photo_id", photoId));
		nameValuePairs.add(new BasicNameValuePair("author_id", photoUserId));
		nameValuePairs.add(new BasicNameValuePair("report_text", reason));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.reportApi()).getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try{
        	if(jsonObj != null)
        	{
        		message = jsonObj.getString("msg");
	        	status = jsonObj.getString("status");
        	}
		} catch(JSONException e)
        {
        	e.printStackTrace();
        }
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result)
	{
		dialog.dismiss();
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
