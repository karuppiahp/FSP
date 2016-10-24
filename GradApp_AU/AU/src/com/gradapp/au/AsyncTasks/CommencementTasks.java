package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.gradapp.au.homescreen.CommencementActivity;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.UrlGenerator;

public class CommencementTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	CommencementActivity context;
	String schoolId, status = "", message, commenceUrl;
	ArrayList<HashMap<String, String>> commenceArray = new ArrayList<HashMap<String, String>>();
	WebView webView;
	TextView textView;
	//load PDF api link in webview using ggogle docs.
	String GoogleDocs = "http://docs.google.com/gview?embedded=true&url=";

	public CommencementTasks(CommencementActivity activity, String schoolid,
			WebView webViews, TextView textViewCommences,
			ProgressDialog dialogUserTasks) {
		context = activity;
		schoolId = schoolid;
		webView = webViews;
		textView = textViewCommences;
		Log.i("schoolId is::", "" + schoolId);
		dialog = dialogUserTasks;
	}

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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getCommencement())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				message = jsonObj.getString("msg");
				status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray jsonArray = jsonObj.getJSONArray("Commencement");
					for (int i = 0; i < jsonArray.length(); i++) {
						String commenceId = jsonArray.getJSONObject(i)
								.getString("Id");
						commenceUrl = jsonArray.getJSONObject(i).getString(
								"Commencement_url");
						
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("commenceId", commenceId);
						hashMap.put("commenceUrl", commenceUrl);
						commenceArray.add(hashMap);
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

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onPostExecute(Boolean result) {
		if (commenceArray.size() > 0) {
			//PDF file from response is viewed through webview.
			webView.setVisibility(View.VISIBLE);
			textView.setVisibility(View.GONE);
			WebSettings webSettings = webView.getSettings();
			webSettings.setBuiltInZoomControls(true);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					dialog.dismiss();
				}
			});
			webView.loadUrl(GoogleDocs + commenceUrl);
		} else {
			//textview has been visibled
			dialog.dismiss();
			webView.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
		}
	}

}
