package com.gradapp.au.AsyncTasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.MyScheduleEventScreen;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.UrlGenerator;
import com.gradapp.au.utils.Utils;

public class MyScheduleEventTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	String univId, schoolId, roleId, eventId, amPm, timing, eventHeld,
			eventName, eventDesc, timingStarts, amPmSmall, eventLat, eventLong,
			rsvpStatus, rsvpVisibleStatus;
	MyScheduleEventScreen activity;
	TextView txtForeventName, txtForEventHeld, textForEventTime,
			textForEventTimeMins, textForContent;
	int days, Hours, Mins;
	double eventHeldLat, eventHeldLong;
	String status = "", message = "Network speed is slow", timeZoneId;
	ImageButton btnForYes, btnForNo, btnForDirections, btnForRSVP;
	DBAdapter db;

	public MyScheduleEventTasks(MyScheduleEventScreen actvty, String univ,
			String school, String role, String eventid, TextView txtForName,
			TextView txtForHeld, TextView textForTime,
			TextView textForTimeMins, TextView textContent, double eventlat,
			double eventlong, ImageButton btnYes, ImageButton btnNo,
			ImageButton btnforDirections, ImageButton btnforRsvp,
			String timeZone) {
		univId = univ;
		schoolId = school;
		roleId = role;
		eventId = eventid;
		activity = actvty;
		txtForeventName = txtForName;
		txtForEventHeld = txtForHeld;
		textForEventTime = textForTime;
		textForEventTimeMins = textForTimeMins;
		textForContent = textContent;
		eventHeldLat = eventlat;
		eventHeldLong = eventlong;
		btnForYes = btnYes;
		btnForNo = btnNo;
		btnForDirections = btnforDirections;
		btnForRSVP = btnforRsvp;
		timeZoneId = timeZone;
		db = new DBAdapter(activity);
		dialog = new ProgressDialog(activity);
		dialog.setMessage("Loading..");
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		try {
			dialog.show();
			dialog.setCancelable(false);
		} catch (Exception e) {
		}
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as
	 * argument in Server response constructor for server connection Params are
	 * passed as in POST method
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	protected Boolean doInBackground(Void... params) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("university_id", univId));
		nameValuePairs.add(new BasicNameValuePair("school_id", schoolId));
		nameValuePairs.add(new BasicNameValuePair("role_id", roleId));
		nameValuePairs.add(new BasicNameValuePair("id", eventId));
		nameValuePairs.add(new BasicNameValuePair("timezone", timeZoneId));
		nameValuePairs.add(new BasicNameValuePair("user_id", SessionStores
				.getUserId(activity)));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getEventsDetail())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				status = jsonObj.getString("status");
				message = jsonObj.getString("msg");
				if (status.equals("1")) {
					JSONObject eventObj = jsonObj
							.getJSONObject("event_details");
					eventHeld = eventObj.getString("Event Location");
					eventName = eventObj.getString("Schedule Name");
					eventDesc = eventObj.getString("Event Description");
					eventLat = eventObj.getString("Event Latitude");
					eventLong = eventObj.getString("Event Longitude");
					String timeStarts = eventObj.getString("Date of scheduled");
					rsvpStatus = eventObj.getString("RSVP");
					rsvpVisibleStatus = eventObj.getString("RSVP Status");

					String timeSplits[] = timeStarts.split(" ");
					String timeValue = timeSplits[1];
					String timingSplits[] = timeValue.split(":");
					String hour = timingSplits[0];
					String minutes = timingSplits[1];
					int hrs = Integer.parseInt(hour);
					// According to the 12 hrs format the AM/PM has been
					// calculated
					if (hrs >= 12) {
						hour = Utils.HourValue(hour);
						amPm = "PM";
						amPmSmall = "pm";
					} else {
						amPm = "AM";
						amPmSmall = "am";
					}

					SimpleDateFormat dayFormat = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm");
					String currentDate = dayFormat.format(new Date());
					Date day1 = dayFormat.parse(timeStarts);
					Date day2 = dayFormat.parse(currentDate);
					// Day difference
					long dayDiff = day1.getTime() - day2.getTime();
					String dateFromSplits = timeSplits[0];
					String dateSplits[] = dateFromSplits.split("-");
					String finalDate = dateSplits[2];
					String finalMonth = dateSplits[1];
					String finalYear = dateSplits[0];
					char charc = finalMonth.charAt(0);
					boolean isDigit = (charc == '0');
					if (isDigit) {
						// Month calculation if 0="" because while set the month
						// we add +1 for month
						finalMonth = finalMonth.replace("0", "");
					}

					Calendar now = Calendar.getInstance();
					int currentYear = now.get(Calendar.YEAR);
					int currentMonth = now.get(Calendar.MONTH) + 1;// Add +1 for
																	// month
					// Year calculation
					if (Integer.parseInt(finalYear) >= currentYear) {
						if (Integer.parseInt(finalMonth) >= currentMonth) {
							timing = Utils.timeCalculation(hour, minutes,
									amPmSmall, dayDiff, days, Hours, Mins,
									finalDate);
						} else {
							timing = "Completed";
						}
					} else {
						timing = "Completed";
					}

					timingStarts = hour + ":" + minutes + " " + amPm;
					if (!(timing.equals("Completed"))) {
						db.open();
						Cursor c = db.getMySchedulesDesc(eventId);
						int count = c.getCount();
						if (count > 0) {
							if (c.moveToFirst()) {
								do {
									db.updateMySchedulesDesc(eventId,
											eventName, eventDesc, eventHeld,
											timingStarts, timing);
								} while (c.moveToNext());
							}
						} else {
							db.insertToMyScheduleDetails(eventId, eventName,
									eventDesc, eventHeld, timingStarts, timing);
						}
						c.close();
						db.close();
					}
				}
			} else {
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		dialog.dismiss();
		if (status.length() > 0) {
			if (status.equals("1")) {
				if (eventLat.length() > 0) {
					Constant.eventLatitude = Double.parseDouble(eventLat);
					Constant.eventLongitude = Double.parseDouble(eventLong);
				}

				if (!(timing.equals("finished"))) {
					txtForeventName.setText(eventName);
					txtForEventHeld.setText(eventHeld);
					textForContent.setText(Html.fromHtml(eventDesc));
					textForEventTime.setText("Starts at " + timingStarts);
					textForEventTimeMins.setText(timing);
					if (eventHeld.length() > 0) {
						btnForDirections.setVisibility(View.VISIBLE);
						txtForEventHeld.setVisibility(View.VISIBLE);
					} else {
						btnForDirections.setVisibility(View.GONE);
						txtForEventHeld.setVisibility(View.GONE);
					}

					if (rsvpVisibleStatus.equals("0")) {
						btnForRSVP.setVisibility(View.GONE);
					} else {
						btnForRSVP.setVisibility(View.VISIBLE);
					}

					if (rsvpStatus.length() > 0) {
						if (rsvpStatus.equals("1")) {
							btnForYes.setBackgroundResource(R.drawable.yes_btn);
						} else if (rsvpStatus.equals("0")) {
							btnForNo.setBackgroundResource(R.drawable.no_btn);
						}
					}
				}
			}
		} else {
			Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
			btnForDirections.setVisibility(View.GONE);
			btnForRSVP.setVisibility(View.GONE);
		}
	}

	//Days difference calculation
	public static long calculateDays(Date dateEarly, Date dateLater) {
		return (dateLater.getTime() - dateEarly.getTime())
				/ (24 * 60 * 60 * 1000);
	}
}
