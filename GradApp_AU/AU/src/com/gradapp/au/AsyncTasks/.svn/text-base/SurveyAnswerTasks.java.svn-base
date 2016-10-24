package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.UrlGenerator;

public class SurveyAnswerTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	String userId, quesId, answer;
	Context context;
	JSONObject jsonObj;

	public SurveyAnswerTasks(Context mContext, String userid, String quesid,
			String answerTxt) {
		userId = userid;
		quesId = quesid;
		answer = answerTxt;
		context = mContext;
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading..");
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("user_id", userId));
		nameValuePairs.add(new BasicNameValuePair("question_id", quesId));
		nameValuePairs.add(new BasicNameValuePair("textual_answer", answer));
		jsonObj = new ServerResponse(UrlGenerator.surveyAnswer())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		return null;
	}

}
