package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class RegisterTask extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity context;
	String status = "", msg = "Network speed is slow", firstName, lastName,
			dob, genderSelects, email, univ, school, role, accessStatus,
			studentName, studentNumber, gradType, noOfGuest;

	public RegisterTask(Activity baseContext, String first, String last,
			String dateOfBirth, String gender, String mailId, String univId,
			String roleId, String schoolId, String statusType,
			String studentname, String studentNo, String gradTypes,
			String gusetNo) {
		context = baseContext;
		firstName = first;
		lastName = last;
		dob = dateOfBirth;
		genderSelects = gender;
		email = mailId;
		univ = univId;
		school = schoolId;
		role = roleId;
		accessStatus = statusType;
		studentName = studentname;
		studentNumber = studentNo;
		gradType = gradTypes;
		noOfGuest = gusetNo;
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
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(12);
		nameValuePairs.add(new BasicNameValuePair("first_name", firstName));
		nameValuePairs.add(new BasicNameValuePair("last_name", lastName));
		nameValuePairs.add(new BasicNameValuePair("username", email));
		nameValuePairs.add(new BasicNameValuePair("date_of_birth", dob));
		nameValuePairs.add(new BasicNameValuePair("gender", genderSelects));
		nameValuePairs.add(new BasicNameValuePair("university_id", univ));
		nameValuePairs.add(new BasicNameValuePair("school_id", school));
		nameValuePairs.add(new BasicNameValuePair("role_id", role));
		nameValuePairs.add(new BasicNameValuePair("app_id", SessionStores
				.getAppId(context)));
		nameValuePairs.add(new BasicNameValuePair("device_type", "android"));
		nameValuePairs.add(new BasicNameValuePair("status", accessStatus));
		nameValuePairs.add(new BasicNameValuePair("student_name", studentName));
		nameValuePairs.add(new BasicNameValuePair("student_number",
				studentNumber));
		nameValuePairs
				.add(new BasicNameValuePair("number_of_guest", noOfGuest));
		nameValuePairs.add(new BasicNameValuePair("grad_type_id", gradType));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.registerUser())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				status = jsonObj.getString("status");
				msg = jsonObj.getString("msg");
				if (status.equals("1")) {
					String userId = jsonObj.getString("parent_id");
					// Save userId in session
					SessionStores.saveUserId(userId, context);
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
		if (status.length() > 0) {
			if (status.equals("1")) {
				SessionStores.saveLoginPref("register", context);
				// HamburgerNameTasks calls
				new HamburgerNameTasks(context, "Register", dialog).execute();
			} else {
				dialog.dismiss();
				Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
			}
		} else {
			dialog.dismiss();
			Toast.makeText(context, "Network speed is slow", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
