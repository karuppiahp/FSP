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

import com.gradapp.au.hamburger.menus.SurveyActivity;
import com.gradapp.au.hamburger.menus.SurveyActivity.ViewHolderSurvey;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.support.SurveyAdapter;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class SurveyTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	String univId, schoolId, roleId;
	String message = "", status = "";
	SurveyActivity context;
	ArrayList<HashMap<String, String>> surveyArray = new ArrayList<HashMap<String, String>>();
	Typeface typeFace, typeFaceLight;
	ListView layoutForList;
	ViewHolderSurvey holder;

	public SurveyTasks(SurveyActivity contexts, String univ, String school,
			String role, ArrayList<HashMap<String, String>> surveyArrays,
			Typeface typeFaces, Typeface typeFaceLights,
			ListView layoutForList2, ViewHolderSurvey viewHolder,
			ProgressDialog dialogUserTasks) {
		univId = univ;
		schoolId = school;
		roleId = role;
		surveyArray = surveyArrays;
		context = contexts;
		typeFace = typeFaces;
		typeFaceLight = typeFaceLights;
		layoutForList = layoutForList2;
		holder = viewHolder;
		dialog = dialogUserTasks;
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
		nameValuePairs.add(new BasicNameValuePair("university_id", univId));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
		nameValuePairs.add(new BasicNameValuePair("role_id", roleId));
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(context)));
		JSONObject jsonObj = new ServerResponse(
				UrlGenerator.getSurveyQuestions()).getJSONObjectfromURL(
				RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				message = jsonObj.getString("msg");
				status = jsonObj.getString("status");
				if (status.equals("1")) {
					JSONArray jsonArray = jsonObj
							.getJSONArray("survey_details");
					for (int i = 0; i < jsonArray.length(); i++) {
						String id = jsonArray.getJSONObject(i).getString(
								"Question id");
						String question = jsonArray.getJSONObject(i).getString(
								"question");
						String answer = jsonArray.getJSONObject(i).getString(
								"Textual answer");

						HashMap<String, String> hashValue = new HashMap<String, String>();
						hashValue.put("quesid", id);
						hashValue.put("question", question);
						hashValue.put("answer", answer);
						surveyArray.add(hashValue);
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
		//Adapter class calls
		layoutForList.setAdapter(new SurveyAdapter(context, typeFace,
				surveyArray, holder));
	}
}
