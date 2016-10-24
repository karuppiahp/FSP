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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.hamburger.menus.FAQActivity.ViewHolder;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.FAQAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.UrlGenerator;

public class FaqTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity context;
	String univId, schoolId, roleId, msg = "Network speed is slow",
			status = "";
	ListView listView;
	ArrayList<HashMap<String, String>> faqArray;
	Typeface typeFace, typeFaceLight;
	ViewHolder holder;
	DBAdapter db;
	TextView textForEmpty;

	public FaqTasks(Activity activity, String univid, String schoolid,
			String roleid, ListView listview,
			ArrayList<HashMap<String, String>> questionArray,
			Typeface typeface, Typeface typefaceLight, ViewHolder holderView,
			ProgressDialog dialogUserTasks, TextView textForFaqEmpty) {
		context = activity;
		univId = univid;
		schoolId = schoolid;
		roleId = roleid;
		listView = listview;
		faqArray = questionArray;
		typeFace = typeface;
		typeFaceLight = typefaceLight;
		holder = holderView;
		textForEmpty = textForFaqEmpty;
		db = new DBAdapter(activity);
		dialog = dialogUserTasks;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();

		dialog.show();
		db.open();
		db.deleteFaq();
		db.close();
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("university_id", univId));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
		nameValuePairs.add(new BasicNameValuePair("role_id", roleId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getFaqDatas())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				msg = jsonObj.getString("msg");
				status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray jsonArray = jsonObj
							.getJSONArray("faqsurvey_details");
					for (int i = 0; i < jsonArray.length(); i++) {
						String faqQues = jsonArray.getJSONObject(i).getString(
								"faq_question");
						String faqAns = jsonArray.getJSONObject(i).getString(
								"faq_answer");

						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("faqQues", faqQues);
						hashMap.put("faqAns", faqAns);
						faqArray.add(hashMap);

						db.open();
						db.insertToFAQ(faqQues, faqAns);
						db.close();
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
		if (status.length() > 0) {
			if (status.equals("1")) {
				// ListView adapter class
				listView.setAdapter(new FAQAdapter(context, faqArray, typeFace,
						typeFaceLight, holder));
			} else {
				textForEmpty.setText("No data available for this screen");
				textForEmpty.setVisibility(View.VISIBLE);
			}
		} else {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}
}
