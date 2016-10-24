package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.gradapp.au.homescreen.HomeActivity;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;

public class HamburgerNameTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	Activity context;
	String status = "", message, fromWhere, unreadCount;
	List<String> nameArray = new ArrayList<String>();
	List<String> idArray = new ArrayList<String>();
	Timer t;
	DBAdapter db;

	public HamburgerNameTasks(Activity mContext, String from,
			ProgressDialog dialog2) {
		context = mContext;
		dialog = dialog2;
		fromWhere = from;
		db = new DBAdapter(mContext);
		dialog.setMessage("Loading..");
		t = new Timer();

		db.open();
		Cursor c = db.getAllHambrgrNames();
		if (c.moveToFirst()) {
			do {
				db.deleteRows();
			} while (c.moveToNext());
		}
		c.close();
		db.close();
	}

	public void onPreExecute() {
		super.onPreExecute();
		if (fromWhere.equals("Register")) {
			dialog.show();
			dialog.setCancelable(false);
		}
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@SuppressLint("DefaultLocale")
	@Override
	protected Boolean doInBackground(Void... arg0) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("school_id", SessionStores
				.getSchoolId(context)));
		nameValuePairs.add(new BasicNameValuePair("role_id", SessionStores
				.getRoleType(context)));
		nameValuePairs.add(new BasicNameValuePair("university_id",
				SessionStores.getUnivId(context)));
		nameValuePairs.add(new BasicNameValuePair("user_id",
				SessionStores.getUserId(context)));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getHamburgerName())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				status = jsonObj.getString("status");
				message = jsonObj.getString("msg");
				unreadCount = jsonObj.getString("Notification unread count");
				if (status.equals("1")) {
					JSONArray jsonArray = jsonObj.getJSONArray("Hamburger");
					for (int i = 0; i < jsonArray.length(); i++) {
						String hamburgerId = jsonArray.getJSONObject(i)
								.getString("Hamburger Id");
						String hamburgerName = jsonArray.getJSONObject(i)
								.getString("Hamburger Name");

						String capitalizeletter = String.valueOf(
								hamburgerName.charAt(0)).toUpperCase()
								+ hamburgerName.substring(1,
										hamburgerName.length());

						db.open();
						db.insertToHambrgr(hamburgerId, hamburgerName);
						db.close();

						nameArray.add(capitalizeletter);
						idArray.add(hamburgerId);
					}
					//Static values FAQ,SURVEY and SETTINGS are added along with the id
					nameArray.add("FAQ");
					idArray.add("FAQ");
					nameArray.add("SURVEY");
					idArray.add("SURVEY");
					nameArray.add("SETTINGS");
					idArray.add("SETTINGS");
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
		if (fromWhere.equals("Register")) {
			dialog.dismiss();
		}
		if (status.length() > 0) {
			if (status.equals("1")) {
				//Menu names and id's are retrieved from SessionStore
				SessionStores.saveMenuArray(context, nameArray);
				SessionStores.saveMenuIdArray(context, idArray);
				if(Integer.parseInt(unreadCount) > 0) {
					SessionStores.saveUnreadCount(unreadCount, context);
				} else {
					SessionStores.saveUnreadCount(unreadCount, context);
				}

				Intent intent = new Intent(context, HomeActivity.class);
				context.startActivity(intent);
				context.finish();
			} else {
				//Task calls from Register screen
				if (fromWhere.equals("Register")) {
					List<String> nameLoadArray = SessionStores.loadMenuArray(context);
					if (!(nameLoadArray.size() > 0)) {
						//Static values are added along with values in arraylist
						nameArray.add("FAQ");
						idArray.add("FAQ");
						nameArray.add("SURVEY");
						idArray.add("SURVEY");
						nameArray.add("SETTINGS");
						idArray.add("SETTINGS");
						SessionStores.saveMenuArray(context, nameArray);
						SessionStores.saveMenuIdArray(context, idArray);
					}
					Intent intent = new Intent(context, HomeActivity.class);
					context.startActivity(intent);
					context.finish();
				} else {
					TimerTask timerTask = new TimerTask() {
						@Override
						public void run() {
							List<String> nameLoadArray = SessionStores
									.loadMenuArray(context);
							if (!(nameLoadArray.size() > 0)) {
								//Static values are added along with values in arraylist
								nameArray.add("FAQ");
								idArray.add("FAQ");
								nameArray.add("SURVEY");
								idArray.add("SURVEY");
								nameArray.add("SETTINGS");
								idArray.add("SETTINGS");
								SessionStores.saveMenuArray(context, nameArray);
								SessionStores.saveMenuIdArray(context, idArray);
							}
							Intent intent = new Intent(context,
									HomeActivity.class);
							context.startActivity(intent);
							context.finish();
						}
					};
					t.schedule(timerTask, 1000);
				}
			}
		} else {
			//Directly redirects to the screen after scheduled time executes
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					List<String> nameLoadArray = SessionStores.loadMenuArray(context);
					if (!(nameLoadArray.size() > 0)) {
						nameArray.add("FAQ");
						idArray.add("FAQ");
						nameArray.add("SURVEY");
						idArray.add("SURVEY");
						nameArray.add("SETTINGS");
						idArray.add("SETTINGS");
						SessionStores.saveMenuArray(context, nameArray);
						SessionStores.saveMenuIdArray(context, idArray);
					}
					Intent intent = new Intent(context, HomeActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			};
			t.schedule(timerTask, 1000);
		}
	}
}
