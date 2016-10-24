package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Spinner;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class SettingsTask extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity activity;
	String status, message, lastName, firstName, email, dob, gender;
	Spinner spinnerGender;
	EditText editTxtFirstName, editTxtLastName, spinnerDob, editTxtMail;

	public SettingsTask(Activity context, EditText editTxtForFirstName,
			EditText editTxtForLastName, EditText dobSpinner,
			Spinner genderSpinner, EditText editTxtForMailId) {
		activity = context;
		editTxtFirstName = editTxtForFirstName;
		editTxtLastName = editTxtForLastName;
		editTxtMail = editTxtForMailId;
		spinnerDob = dobSpinner;
		spinnerGender = genderSpinner;
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
	@SuppressLint("SimpleDateFormat")
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(activity)));
		nameValuePairs.add(new BasicNameValuePair("school_id", SessionStores
				.getSchoolId(activity)));
		nameValuePairs.add(new BasicNameValuePair("id", SessionStores
				.getUserId(activity)));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.settingsView())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				status = jsonObj.getString("status");
				message = jsonObj.getString("msg");
				if (status.equals("1")) {
					JSONObject userDetailsObj = jsonObj
							.getJSONObject("User Details");
					firstName = userDetailsObj.getString("Firstname");
					lastName = userDetailsObj.getString("Lastname");
					email = userDetailsObj.getString("Email");
					dob = userDetailsObj.getString("Dob");
					gender = userDetailsObj.getString("Gender");
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
		//set the values
		editTxtFirstName.setText(firstName);
		editTxtLastName.setText(lastName);
		editTxtMail.setText(email);
		spinnerDob.setText(dob);
	}
}
