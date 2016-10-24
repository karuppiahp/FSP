package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class SettingsEditTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity activity;
	String status, message, lastName, firstName, email, dob, gender,
			studentName, studentTypeId;
	JSONObject jsonObj;

	public SettingsEditTasks(Activity context, String firstNameStr,
			String lastNameStr, String dobSpinnerStr, String genderSpinnerStr,
			String mailIdStr, String studentname, String studenttypeId) {
		activity = context;
		firstName = firstNameStr;
		lastName = lastNameStr;
		email = mailIdStr;
		dob = dobSpinnerStr;
		gender = genderSpinnerStr;
		studentName = studentname;
		studentTypeId = studenttypeId;
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
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		nameValuePairs.add(new BasicNameValuePair("school_id", SessionStores
				.getSchoolId(activity)));
		nameValuePairs.add(new BasicNameValuePair("id", SessionStores
				.getUserId(activity)));
		nameValuePairs.add(new BasicNameValuePair("first_name", firstName));
		nameValuePairs.add(new BasicNameValuePair("last_name", lastName));
		nameValuePairs.add(new BasicNameValuePair("date_of_birth", dob));
		nameValuePairs.add(new BasicNameValuePair("username", email));
		nameValuePairs.add(new BasicNameValuePair("gender", gender));
		nameValuePairs.add(new BasicNameValuePair("student_name", studentName));
		nameValuePairs.add(new BasicNameValuePair("student_number", ""));
		nameValuePairs.add(new BasicNameValuePair("number_of_guest", ""));
		nameValuePairs
				.add(new BasicNameValuePair("grad_type_id", studentTypeId));
		jsonObj = new ServerResponse(UrlGenerator.settingsEdit())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
	}
}
