package com.gradapp.au.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gradapp.au.activities.EditSchedulesActivity;
import com.gradapp.au.activities.R;
import com.gradapp.au.application.GradAppApplication;
import com.gradapp.au.homescreen.CameraActivity;
import com.gradapp.au.homescreen.MyScheduleActivity;
import com.gradapp.au.homescreen.SearchScheduleActivity;
import com.gradapp.au.support.DBAdapter;

@SuppressLint({ "SimpleDateFormat", "InflateParams", "DefaultLocale" })
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Utils {
	public static final boolean SHOULD_PRINT_LOG = true;
	private static boolean isValid;
	private static DBAdapter db;
	private static String[] imageUrls, imageUrlsFinal, dbImagePaths;

	public static void debugLog(String tag, String message) {
		if (SHOULD_PRINT_LOG) {
		}
	}

	public static void errorLog(String tag, String message) {
		if (SHOULD_PRINT_LOG) {
		}
	}

	public static void infoLog(String tag, String message) {
		if (SHOULD_PRINT_LOG) {
		}
	}

	public static void printException(Exception exception) {
		errorLog("Exception", exception.toString());
	}

	// Check network connectivity method
	public static boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) GradAppApplication
				.getGlobalContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	// Check the valid email format method
	public static boolean isEmailValid(String email) {
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	// Alert window with ok button
	public static void ShowAlert(Context context, String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setMessage(message);
		adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});
		AlertDialog alert = adb.create();
		alert.show();
	}

	// Alert window has been called from asynctask process
	public static void ShowAlertImage(final Activity context, String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setMessage(message);
		adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				Intent intentToNotify = new Intent(context,
						CameraActivity.class);
				context.startActivity(intentToNotify);
				context.finish();
			}
		});
		AlertDialog alert = adb.create();
		alert.show();
	}

	// Hide keyboard
	public static void hideKeyboard(Activity activity) {
		// Check if no view has focus:
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	// Show Keyboard
	public static void showKeyboard(Activity activity) {
		// Check if no view has focus:
		View view = activity.getCurrentFocus();
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_SHOWN);
	}

	public static String[] fetchSdCardImages(Context context) {
		// Sdcard path
		File mediaStorageDir = new File(Environment
				.getExternalStorageDirectory().toString(), "/DCIM/Camera/");
		// refresh the gallery
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			MediaScannerConnection.scanFile(context, new String[] {

			mediaStorageDir.getAbsolutePath().toString() },

			null, new MediaScannerConnection.OnScanCompletedListener() {

				public void onScanCompleted(String path, Uri uri)

				{
				}
			});

			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri contentUri = Uri.fromFile(mediaStorageDir);
			mediaScanIntent.setData(contentUri);
			context.sendBroadcast(mediaScanIntent);
		} else {
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse(mediaStorageDir.toString())));
		}
		db = new DBAdapter(context);
		int p = 0, fileNotFoundCount = 0;
		db.open();
		Cursor c = db.getAllImagePaths();
		int count = c.getCount();
		dbImagePaths = new String[c.getCount()];
		if (count > 0) {
			if (c.moveToFirst()) {
				do {
					String path = c.getString(1);
					String pathReplace = path.replace("file:/", "");
					File file = new File(pathReplace);
					if (file.exists()) {
						dbImagePaths[p] = path;
						p++;
					} else {
						fileNotFoundCount++;
						db.open();
						db.deleteImagePath(path);
						db.close();
					}
				} while (c.moveToNext());
			}
		}

		c.close();
		db.close();
		dbImagePaths = Arrays.copyOf(dbImagePaths, dbImagePaths.length
				- fileNotFoundCount);
		int i = 0;
		final String CAMERA_IMAGE_BUCKET_NAME = Environment
				.getExternalStorageDirectory().toString() + "/DCIM/Camera";
		final String CAMERA_IMAGE_BUCKET_ID = getBucketId(CAMERA_IMAGE_BUCKET_NAME);

		String[] projection = { MediaStore.Images.Media.DATA };
		final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
		final String[] selectionArgs = { CAMERA_IMAGE_BUCKET_ID };
		String sortOrder = MediaStore.Images.Media.DATE_TAKEN;
		//Images are get as in order of date taken
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
				selection, selectionArgs, sortOrder + " DESC");
		int notFoundCount = 0;
		if (((cursor != null) && (cursor.getCount() > 0))) {
			int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			imageUrls = new String[cursor.getCount()];
			do {
				String devicePath = "file:/" + cursor.getString(columnIndex);
				File file = new File(cursor.getString(columnIndex));
				if (file.exists()) {
					if (dbImagePaths != null && dbImagePaths.length > 0) {
						for (int q = 0; q < dbImagePaths.length; q++) {
							if (dbImagePaths[q] != null) {
								if (devicePath.equals(dbImagePaths[q])) {
									db.open();
									db.deleteImagePath(dbImagePaths[q]);
									db.close();
								}
							}
						}
					}
					imageUrls[i] = devicePath;
					i++;
				} else {
					notFoundCount++;
				}
			} while (cursor.moveToNext());

		}

		//whether the image path has been presented more than one
		imageUrls = Arrays.copyOf(imageUrls, imageUrls.length - notFoundCount);
		int h = 0, fileNotFoundCount2 = 0;
		db.open();
		Cursor cur = db.getAllImagePaths();
		int count2 = c.getCount();
		dbImagePaths = new String[cur.getCount()];
		if (count2 > 0) {
			if (cur.moveToFirst()) {
				do {
					String path = cur.getString(1);
					String pathReplace = path.replace("file:/", "");
					File file = new File(pathReplace);
					if (file.exists()) {
						dbImagePaths[h] = path;
						h++;
					} else {
						fileNotFoundCount2++;
						db.open();
						db.deleteImagePath(path);
						db.close();
					}
				} while (cur.moveToNext());
			}
		}

		cur.close();
		db.close();
		dbImagePaths = Arrays.copyOf(dbImagePaths, dbImagePaths.length
				- fileNotFoundCount2);
		if (dbImagePaths != null) {
			Arrays.sort(dbImagePaths, Collections.reverseOrder());
		}
		if (imageUrls != null && dbImagePaths != null) {
			imageUrlsFinal = concat(dbImagePaths, imageUrls);
		} else {
			if (imageUrls != null && imageUrls.length > 0) {
				imageUrlsFinal = imageUrls;
			}
			if (dbImagePaths != null && dbImagePaths.length > 0) {
				imageUrlsFinal = dbImagePaths;
			}
		}

		if (imageUrls == null && dbImagePaths == null) {
			imageUrlsFinal = null;
		}
		return imageUrlsFinal;
	}

	public static String getBucketId(String path) {
		return String.valueOf(path.toLowerCase().hashCode());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean deleteViaContentProvider(Context context,
			String fullname) {
		Uri uri = Uri.fromFile(new File(fullname));

		if (uri == null) {
			return false;
		}

		try {
			ContentResolver resolver = context.getContentResolver();
			// change type to image, otherwise nothing will be deleted
			ContentValues contentValues = new ContentValues();
			int media_type = 1;
			contentValues.put("media_type", media_type);
			resolver.update(uri, contentValues, null, null);

			return resolver.delete(uri, null, null) > 0;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressLint("DefaultLocale")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static Uri getFileUri(Context context, String fullname) {
		// Note: check outside this class whether the OS version is >= 11
		Uri uri = null;
		Cursor cursor = null;
		ContentResolver contentResolver = null;

		try {
			contentResolver = context.getContentResolver();
			if (contentResolver == null)
				return null;

			uri = MediaStore.Files.getContentUri("external");
			String[] projection = new String[2];
			projection[0] = "_id";
			projection[1] = "_data";
			String selection = "_data = ? "; // this avoids SQL injection
			String[] selectionParams = new String[1];
			selectionParams[0] = fullname;
			String sortOrder = "_id";
			cursor = contentResolver.query(uri, projection, selection,
					selectionParams, sortOrder);

			if (cursor != null) {
				try {
					if (cursor.getCount() > 0) // file present!
					{
						cursor.moveToFirst();
						int dataColumn = cursor.getColumnIndex("_data");
						String s = cursor.getString(dataColumn);
						if (!s.equals(fullname))
							return null;
						int idColumn = cursor.getColumnIndex("_id");
						long id = cursor.getLong(idColumn);
						uri = MediaStore.Files.getContentUri("external", id);
					} else // file isn't in the media database!
					{
						ContentValues contentValues = new ContentValues();
						contentValues.put("_data", fullname);
						uri = MediaStore.Files.getContentUri("external");
						uri = contentResolver.insert(uri, contentValues);
					}
				} catch (Throwable e) {
					uri = null;
				} finally {
					cursor.close();
				}
			}
		} catch (Throwable e) {
			uri = null;
		}
		return uri;
	}

	private static String[] concat(String[] A, String[] B) {
		int aLen = A.length;
		int bLen = B.length;
		String[] C = new String[aLen + bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}

	public static Bitmap rotateImage(Bitmap src, float degree) {
		try {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
					src.getHeight(), matrix, true);
		} catch (Exception e) {
			return null;
		}
	}

	public static Bitmap getBitmap(String path, Activity act) {
		Uri uri = Uri.fromFile(new File(path));
		InputStream in = null;
		try {
			in = act.getContentResolver().openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();
			int scale = 1;
			if (o.outHeight > 1280 || o.outWidth > 800) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(1280 / (double) Math.max(
								o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			in = act.getContentResolver().openInputStream(uri);
			Bitmap b = BitmapFactory.decodeStream(in, null, o2);
			in.close();
			return b;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}

	// Hour calculation for social media posts
	@SuppressLint("SimpleDateFormat")
	public static String calculateHoursPost(String date3, String type) {
		String postTime = date3;
		String friendly = null;
		SimpleDateFormat dateFormatter;
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

		try {
			Date date = dateFormatter.parse(postTime);
			long then = date.getTime();
			long now = new Date().getTime();
			long seconds = (now - then) / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			long days = hours / 24;
			long num = 0;
			if (days > 0) {
				num = days;
				friendly = days + " day";
			} else if (hours > 0) {
				num = hours;
				friendly = hours + " hr";
			} else if (minutes > 0) {
				num = minutes;
				friendly = minutes + " min";
			} else {
				num = seconds;
				friendly = seconds + " second";
			}
			if (num > 1) {
				friendly += "s";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String postTimeStamp = friendly;
		return postTimeStamp;
	}

	public static boolean validEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}

	//MySchedule process for online
	public static void MyScheduleList(
			ArrayList<HashMap<String, String>> dateArray,
			ArrayList<HashMap<String, String>> eventsArray,
			MyScheduleActivity activity, Typeface typeFace,
			Typeface typeFaceLight, LinearLayout layoutForSchedules,
			RelativeLayout layForCustomView,
			ArrayList<HashMap<String, String>> userDateArray,
			ArrayList<HashMap<String, String>> userEventsArray,
			RelativeLayout layForUserCustomView) {
		//MySchedule items are fetches and displayed as dynamic view
		db = new DBAdapter(activity);
		db.open();
		db.deleteMySchedules();
		db.close();

		for (int i = 0; i < dateArray.size(); i++) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View v = inflater.inflate(R.layout.my_schedule_custom_view, null);
			LinearLayout layForSchedules = (LinearLayout) v
					.findViewById(R.id.layoutForSchedules);
			TextView textForDate = (TextView) v
					.findViewById(R.id.txtForSheduleDate);

			String timeSplits[] = dateArray.get(i).get("header").split(" ");
			String dateValue = timeSplits[0];
			String dateSplits[] = dateValue.split("-");
			String monthValue = dateSplits[1];
			String day = dateSplits[2];
			String month = Utils.MonthValue(monthValue);
			String year = dateSplits[0];

			//date of shedule has been set
			String dateConverted = month + " " + day + ", " + year;
			textForDate.setText(month + " " + day + ", " + year);
			textForDate.setTypeface(typeFaceLight);

			for (int j = 0; j < eventsArray.size(); j++) {
				LayoutInflater inflater2 = LayoutInflater.from(activity);
				View view = inflater2.inflate(R.layout.my_schedule_views, null);
				layForCustomView = (RelativeLayout) view
						.findViewById(R.id.layForCustomView);
				TextView textForScheduleName = (TextView) view
						.findViewById(R.id.textForScheduleName);

				TextView textForScheduleTime = (TextView) view
						.findViewById(R.id.textForScheduleTime);

				if (eventsArray.get(j).get("name")
						.contains(dateArray.get(i).get("header"))) {
					String eventIdSplits[] = eventsArray.get(j).get("id")
							.split(">");
					String eventId = eventIdSplits[1];
					String nameSplits[] = eventsArray.get(j).get("name")
							.split(">");
					String name = nameSplits[1];
					textForScheduleName.setText(name);
					textForScheduleName.setTypeface(typeFace);
					String timeSplits1[] = eventsArray.get(j).get("timeStarts")
							.split(">");
					String timeStartsAt = timeStartsAtSchedule(timeSplits1[1]);
					textForScheduleTime.setText(timeStartsAt);
					textForScheduleTime.setTypeface(typeFaceLight);
					layForCustomView.setTag(j);
					layForCustomView.setOnClickListener(activity);
					layForSchedules.addView(view);

					db.open();
					db.insertToMySchedules(dateConverted, eventId, name,
							timeStartsAt);
					db.close();
				}
			}

			layoutForSchedules.addView(v);
		}
	}

	//MySchedule process for offline
	public static void MyScheduleListOffline(
			ArrayList<HashMap<String, String>> dateArray,
			ArrayList<HashMap<String, String>> eventsArray,
			MyScheduleActivity activity, Typeface typeFace,
			Typeface typeFaceLight, LinearLayout layoutForSchedules,
			RelativeLayout layForCustomView) {
		for (int i = 0; i < dateArray.size(); i++) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View v = inflater.inflate(R.layout.my_schedule_custom_view, null);
			LinearLayout layForSchedules = (LinearLayout) v
					.findViewById(R.id.layoutForSchedules);
			TextView textForDate = (TextView) v
					.findViewById(R.id.txtForSheduleDate);

			String timeSplits = dateArray.get(i).get("header");

			//date has been set in textview
			textForDate.setText(timeSplits);
			textForDate.setTypeface(typeFaceLight);

			for (int j = 0; j < eventsArray.size(); j++) {
				LayoutInflater inflater2 = LayoutInflater.from(activity);
				View view = inflater2.inflate(R.layout.my_schedule_views, null);
				layForCustomView = (RelativeLayout) view
						.findViewById(R.id.layForCustomView);
				TextView textForScheduleName = (TextView) view
						.findViewById(R.id.textForScheduleName);

				TextView textForScheduleTime = (TextView) view
						.findViewById(R.id.textForScheduleTime);

				if (eventsArray.get(j).get("name")
						.contains(dateArray.get(i).get("header"))) {
					String nameSplits[] = eventsArray.get(j).get("name")
							.split(">");
					String name = nameSplits[1];
					textForScheduleName.setText(name);
					textForScheduleName.setTypeface(typeFace);
					String timeSplits1[] = eventsArray.get(j).get("timeStarts")
							.split(">");
					textForScheduleTime.setText(timeSplits1[1]);
					textForScheduleTime.setTypeface(typeFaceLight);
					layForCustomView.setTag(j);
					layForCustomView.setOnClickListener(activity);
					layForSchedules.addView(view);
				}
			}

			layoutForSchedules.addView(v);
		}
	}

	//Search items has been displayed dynamically from arraylist
	public static void SearchScheduleList(
			ArrayList<HashMap<String, String>> nameArray,
			ArrayList<HashMap<String, String>> eventsArray,
			SearchScheduleActivity activity, Typeface typeFace,
			Typeface typeFaceLight, LinearLayout layoutForSchedules,
			RelativeLayout layForCustomView) {

		for (int i = 0; i < nameArray.size(); i++) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View v = inflater.inflate(R.layout.search_schedule_custom_view,
					null);
			LinearLayout layForSchedules = (LinearLayout) v
					.findViewById(R.id.layoutForSchedules);
			TextView textForName = (TextView) v
					.findViewById(R.id.txtForSheduleDate);

			textForName.setText(nameArray.get(i).get("header"));
			textForName.setTypeface(typeFaceLight);

			for (int j = 0; j < eventsArray.size(); j++) {
				LayoutInflater inflater2 = LayoutInflater.from(activity);
				View view = inflater2.inflate(R.layout.search_view, null);
				layForCustomView = (RelativeLayout) view
						.findViewById(R.id.layForCustomView);
				TextView textForScheduleName = (TextView) view
						.findViewById(R.id.textForScheduleName);
				ImageView imageForAddBtn = (ImageView) view
						.findViewById(R.id.imageForSearchSheduleAdd);
				TextView textForScheduleTime = (TextView) view
						.findViewById(R.id.textForScheduleTime);

				//split string to get name
				String eventNameHeaderContains[] = eventsArray.get(j)
						.get("name").split(">");
				String eventHeaderName = eventNameHeaderContains[0];

				if (eventHeaderName.equals(nameArray.get(i).get("header"))) {
					String nameSplits[] = eventsArray.get(j).get("name")
							.split(">");
					String name = nameSplits[1];
					textForScheduleName.setText(name);
					textForScheduleName.setTypeface(typeFace);
					String timeSplits1[] = eventsArray.get(j).get("timeStarts")
							.split(">");

					String timeStartsAt = timeStartsAt(timeSplits1[1]);
					textForScheduleTime.setText(timeStartsAt);
					textForScheduleTime.setTypeface(typeFaceLight);
					layForCustomView.setTag(j);
					layForCustomView.setOnClickListener(activity);
					imageForAddBtn.setTag(j);
					imageForAddBtn.setOnClickListener(activity);
					layForSchedules.addView(view);

				}
			}

			layoutForSchedules.addView(v);
		}
	}

	//According to the edittext entry the schedule list has been listed
	public static void SearchScheduleEditTxtList(
			ArrayList<HashMap<String, String>> nameArray,
			ArrayList<HashMap<String, String>> eventsArray,
			SearchScheduleActivity activity, Typeface typeFace,
			Typeface typeFaceLight, LinearLayout layoutForSchedules,
			RelativeLayout layForCustomView, String header) {

		layoutForSchedules.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(activity);
		View v = inflater.inflate(R.layout.search_schedule_custom_view, null);
		LinearLayout layForSchedules = (LinearLayout) v
				.findViewById(R.id.layoutForSchedules);
		TextView textForName = (TextView) v
				.findViewById(R.id.txtForSheduleDate);

		textForName.setText(header.trim());
		textForName.setTypeface(typeFaceLight);

		for (int j = 0; j < eventsArray.size(); j++) {
			LayoutInflater inflater2 = LayoutInflater.from(activity);
			View view = inflater2.inflate(R.layout.search_view, null);
			layForCustomView = (RelativeLayout) view
					.findViewById(R.id.layForCustomView);
			TextView textForScheduleName = (TextView) view
					.findViewById(R.id.textForScheduleName);
			ImageView imageForAddBtn = (ImageView) view
					.findViewById(R.id.imageForSearchSheduleAdd);
			TextView textForScheduleTime = (TextView) view
					.findViewById(R.id.textForScheduleTime);

			String eventNameHeaderContains[] = eventsArray.get(j).get("name")
					.split(">");
			String eventHeaderName = eventNameHeaderContains[1];

			if (eventHeaderName.toLowerCase().contains(
					header.toLowerCase().trim())) {
				String nameSplits[] = eventsArray.get(j).get("name").split(">");
				String name = nameSplits[1];
				textForScheduleName.setText(name);
				textForScheduleName.setTypeface(typeFace);
				String timeSplits1[] = eventsArray.get(j).get("timeStarts")
						.split(">");
				String timeStartsAt = timeStartsAt(timeSplits1[1]);
				textForScheduleTime.setText(timeStartsAt);
				textForScheduleTime.setTypeface(typeFaceLight);
				layForCustomView.setTag(j);
				layForCustomView.setOnClickListener(activity);
				imageForAddBtn.setTag(j);
				imageForAddBtn.setOnClickListener(activity);
				layForSchedules.addView(view);

			}
		}

		layoutForSchedules.addView(v);
	}

	//User added schedules has been listed
	public static void UserScheduleList(
			ArrayList<HashMap<String, String>> eventsArray,
			EditSchedulesActivity activity, Typeface typeFace,
			Typeface typeFaceLight, LinearLayout layoutForSchedules,
			RelativeLayout layForCustomView, String from) {
		for (int i = 0; i < Constant.userDateArrayList.size(); i++) {
			LayoutInflater inflater = LayoutInflater.from(activity);
			View v = inflater.inflate(R.layout.my_schedule_custom_view, null);
			LinearLayout layForSchedules = (LinearLayout) v
					.findViewById(R.id.layoutForSchedules);
			TextView textForDate = (TextView) v
					.findViewById(R.id.txtForSheduleDate);

			String timeSplits[] = Constant.userDateArrayList.get(i)
					.get("header").split(" ");
			String dateValue = timeSplits[0];
			String dateSplits[] = dateValue.split("-");
			String monthValue = dateSplits[1];
			String day = dateSplits[2];
			String month = Utils.MonthValue(monthValue);
			String year = dateSplits[0];

			//date header has been set
			textForDate.setText(month + " " + day + ", " + year);
			textForDate.setTypeface(typeFaceLight);

			//Event schedules are arranged according to the date
			for (int j = 0; j < eventsArray.size(); j++) {
				LayoutInflater inflater2 = LayoutInflater.from(activity);
				View view = inflater2.inflate(R.layout.my_schedule_views, null);
				layForCustomView = (RelativeLayout) view
						.findViewById(R.id.layForCustomView);
				TextView textForScheduleName = (TextView) view
						.findViewById(R.id.textForScheduleName);
				ImageView imageForClock = (ImageView) view
						.findViewById(R.id.imageForSheduleClock);
				ImageView imageForArrow = (ImageView) view
						.findViewById(R.id.imageForSheduleArrow);
				imageForArrow.setVisibility(View.GONE);
				imageForClock.setImageResource(R.drawable.remove_btn);

				TextView textForScheduleTime = (TextView) view
						.findViewById(R.id.textForScheduleTime);

				if (eventsArray.get(j).get("name").contains(Constant.userDateArrayList.get(i).get("header"))) {
					textForDate.setVisibility(View.VISIBLE);
					String nameSplits[] = eventsArray.get(j).get("name")
							.split(">");
					String name = nameSplits[1];
					textForScheduleName.setText(name);
					textForScheduleName.setTypeface(typeFace);
					String timeSplits1[] = eventsArray.get(j).get("timeStarts")
							.split(">");
					String timeStartsAt = timeStartsAt(timeSplits1[1]);
					textForScheduleTime.setText(timeStartsAt);
					textForScheduleTime.setTypeface(typeFaceLight);
					layForCustomView.setTag(j);
					layForCustomView.setOnClickListener(activity);
					layForSchedules.addView(view);
				} else {
					if (from.equals("delete")) {
					}
				}
			}

			layoutForSchedules.addView(v);
		}
	}

	//Day format 
	public static String dayCalculation(String date) {
		String finalDate = null;
		try {
			SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format = new SimpleDateFormat("EEEE MM, yyyy");
			Date date1 = oldDateFormat.parse(date);
			finalDate = format.format(date1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return finalDate;
	}

	//Time calculation format
	@SuppressLint({ "InflateParams", "DefaultLocale" })
	public static String timeCalculation(String hour, String minutes,
			String amPmSmall, long dayDiff, int days, int Hours, int Mins,
			String finalDate) {
		/*
		 * hour = starting hour of event
		 * minutes = starting minutes of event
		 * amPmSmall = According to the event starting the AM/PM has been passed
		 * dayDiff = no. of days difference between event date and current date
		 * days, Hours and Mins are calculated in this method as current device time
		 */
		String timing = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");
			String currentTime = format.format(new Date());
			Date Date1 = format.parse(hour + ":" + minutes + ":" + "00 "
					+ amPmSmall);
			Date Date2 = format.parse(currentTime);
			Calendar now = Calendar.getInstance();
			int amPmCurrent = now.get(Calendar.AM_PM); //Current format of AM/PM
			int currentHour = now.get(Calendar.HOUR); //Current hour
			int currentMins = now.get(Calendar.MINUTE); // Current minute
			int dateCurrent = now.get(Calendar.DATE); // Current date

			long mills = Date1.getTime() - Date2.getTime();
			days = (int) (dayDiff / (1000 * 60 * 60 * 24)); //days
			Hours = (int) (mills / (1000 * 60 * 60)); // Hours
			Mins = (int) (mills / (1000 * 60)) % 60; // Mins 

			String hourConvert = String.valueOf(Hours);
			String hourCon = hourConvert.replace("-", "");
			Hours = 23 - Integer.parseInt(hourCon);

			String minsConvert = String.valueOf(Mins);
			String minCon = minsConvert.replace("-", "");
			Mins = 60 - Integer.parseInt(minCon);

			if (days <= 0) { //day difference is less than 0 
				if (Integer.parseInt(hour) > 0) { // Event hour is greater than 0

					if (amPmCurrent == 0) { //Current device time format is AM
						Hours = Integer.parseInt(hourCon); 
						Mins = Integer.parseInt(minCon) + 1;
						if (amPmSmall.equals("am")) { // Event time format is equals to AM
							if (Integer.parseInt(hour) > Hours) { // Event time hour is greater than hours calculated time
								if (Hours > 0) { //Current hours greater than 0
									if (Integer.parseInt(hour) > currentHour) { // event time is greater than the current device hour
										if (Integer.parseInt(finalDate) > dateCurrent) { //Date format of event date is before the date of current
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else { // Current date and event date has to be equal
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent == 0) { //AM
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										}
									} else { //less than current device hour
										if (Integer.parseInt(finalDate) > dateCurrent) { // before the current date
											if (Integer.parseInt(finalDate) > dateCurrent) {
												Hours = 23 - Integer
														.parseInt(hourCon);
												Mins = 60 - Integer
														.parseInt(minCon);
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												if (Integer.parseInt(finalDate) == dateCurrent) { // dates are equals 
													if (amPmCurrent == 0) {
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											}
										} else { // dates are equals
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent == 0) { // format is AM
													if (Integer.parseInt(hour) == currentHour) { // Equal hours
														if (Mins > 0) { // Minutes are greater
															if (Integer
																	.parseInt(minutes) > currentMins) {
																timing = "In "
																		+ Mins
																		+ " m";
															} else {
																timing = "Completed";
															}
														} else {
															timing = "Completed";
														}
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											} else { // event date is before current date
												if (Integer.parseInt(finalDate) > dateCurrent) {
													Hours = 23 - Integer
															.parseInt(hourCon);
													Mins = 60 - Integer
															.parseInt(minCon);
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											}
										}
									}
								} else { // Minutes has been displayed
									if (Integer.parseInt(hour) > currentHour) {
										if (Integer.parseInt(finalDate) > dateCurrent) { // event date is before current date
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											if (Integer.parseInt(finalDate) == dateCurrent) { // equal dates
												if (amPmCurrent == 0) {
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										}
									} else {
										if (Integer.parseInt(finalDate) == dateCurrent) { // date are equal
											if (amPmCurrent == 0) { // format is AM
												if (Integer.parseInt(hour) == currentHour) { // Hours are equal
													if (Mins > 0) { // minutes calculation
														if (Integer
																.parseInt(minutes) > currentMins) {
															timing = "In "
																	+ Mins
																	+ " m";
														} else {
															timing = "Completed";
														}
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										} else { // Dates are having before date
											if (Integer.parseInt(finalDate) > dateCurrent) {
												Hours = 23 - Integer
														.parseInt(hourCon);
												Mins = 60 - Integer
														.parseInt(minCon);
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										}
									}
								}
							} else { //event hour is lesser than current time
								if (Integer.parseInt(hour) > currentHour) { // event hour is greater than the calculated hour timing
									if (Integer.parseInt(finalDate) > dateCurrent) { // event date is before the current date
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);
										timing = "In " + Hours + "h " + Mins
												+ " m";
									} else { // Dates are equal
										if (Integer.parseInt(finalDate) == dateCurrent) {
											if (amPmCurrent == 0) { // Format is AM
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										} else {
											timing = "Completed";
										}
									}
								} else { // Event hour is less than the calculated timing
									if (Integer.parseInt(finalDate) == dateCurrent) { // Dates are equal
										if (amPmCurrent == 0) { //Format is AM
											if (Integer.parseInt(hour) == currentHour) { // Hours are equal
												if (Mins > 0) { //Minutes
													if (Integer
															.parseInt(minutes) > currentMins) {
														timing = "In " + Mins
																+ " m";
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										} else {
											timing = "Completed";
										}
									} else { // Event date is before the current date
										if (Integer.parseInt(finalDate) > dateCurrent) {
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											timing = "Completed";
										}
									}
								}
							}
						} else { // Time format is PM
							if (Integer.parseInt(hour) > Hours) { //Event hour is greater than calculated time hour 
								if (Hours > 0) { //Calculated time hour is greater than 0
									if (amPmSmall.equals("pm")) { // Event time format is equals to PM
										if (Integer.parseInt(hour) > currentHour) { // Event hour is greater than current time hour
											if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is before the current date
												Hours = 23 - Integer
														.parseInt(hourCon);
												Mins = 60 - Integer
														.parseInt(minCon);
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else { // Dates are equal
												if (Integer.parseInt(finalDate) == dateCurrent) {
													if (amPmCurrent != 0) {
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											}
										} else {
											if (Integer.parseInt(finalDate) >= dateCurrent) { //Event date is before or equals the current date
												if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is before the curent date
													if (amPmCurrent != 0) { // Format is PM
														Hours = 23 - Integer
																.parseInt(hourCon);
														Mins = 60 - Integer
																.parseInt(minCon);
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													} else { // Format is AM
														Hours = 23 - Integer
																.parseInt(hourCon);
														Mins = 60 - Integer
																.parseInt(minCon);
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													}
												} else { //Event date is equals
													if (Integer.parseInt(finalDate) == dateCurrent) {
														if (amPmCurrent != 0) {
															timing = "In "
																	+ Hours
																	+ "h "
																	+ Mins
																	+ " m";
														} else {
															timing = "Completed";
														}
													} else {
														timing = "Completed";
													}
												}
											} else {
												timing = "Completed";
											}
										}
									} else {
										timing = "Completed";
									}
								} else {
									if (Integer.parseInt(hour) > currentHour) { //Event hour greater than current hours
										if (Integer.parseInt(finalDate) > dateCurrent) {
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											timing = "Completed";
										}
									} else {
										timing = "Completed";
									}
								}
							} else { // Event hour is less than caculated time hour
								if (amPmSmall.equals("am")) { // event time format is equal to AM
									if (Integer.parseInt(hour) > currentHour) { // Event hour is greater than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is before current date
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else { // Dates are equal
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent == 0) { // Format AM
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										}
									} else { //Event hour is less than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) {
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											timing = "Completed";
										}
									}
								} else { //Event time format is PM
									if (Hours > 0) { //Calculated hour time is greater than 0
										if (Integer.parseInt(hour) > currentHour) { // Event hour is greater than current hour
											if (Integer.parseInt(finalDate) >= dateCurrent) { // event date is on or before the current date
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										} else { //Event hour is less than current hour
											if (Integer.parseInt(finalDate) >= dateCurrent) {
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										}
									} else { //Calculated hour is less than 0
										timing = "In " + Mins + " m";
										if (Mins > 0) {
											if (Integer.parseInt(minutes) > currentMins) {
												timing = "In " + Mins + " m";
											} else {
												timing = "Completed";
											}
										} else {
											timing = "Completed";
										}
									}
								}
							}
						}
					} else {
						// device type PM
						Hours = Integer.parseInt(hourCon);
						Mins = Integer.parseInt(minCon) + 1;
						if (amPmSmall.equals("pm")) { //Event time format is equal to PM
							if (Integer.parseInt(hour) > Hours) { // Event hour is greater than calculated hour format
								if (Hours > 0) { // Calcualted hour is greater than 0
									if (Integer.parseInt(hour) > currentHour) { // Event hour is greater than current hour
										timing = "In " + Hours + "h " + Mins
												+ " m";
										if (Integer.parseInt(finalDate) > dateCurrent) { //Event date is before the current date
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else { // dates are equal
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent != 0) {
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										}
									} else { // Event time is less than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is greater than current date
											if (Integer.parseInt(finalDate) > dateCurrent) { // event date is greater than final date
												Hours = 23 - Integer
														.parseInt(hourCon);
												Mins = 60 - Integer
														.parseInt(minCon);
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else { // Dates are equals
												if (Integer.parseInt(finalDate) == dateCurrent) {
													if (amPmCurrent != 0) {
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											}
										} else { // dates are equals
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent != 0) { // Format is PM
													if (Integer.parseInt(hour) == currentHour) { //Event hour is equal to current hour
														if (Mins > 0) { //Minutes calculation
															if (Integer
																	.parseInt(minutes) > currentMins) {
																timing = "In "
																		+ Mins
																		+ " m";
															} else {
																timing = "Completed";
															}
														} else {
															timing = "Completed";
														}
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											} else { //Event date is greater than current date
												if (Integer.parseInt(finalDate) > dateCurrent) {
													Hours = 23 - Integer
															.parseInt(hourCon);
													Mins = 60 - Integer
															.parseInt(minCon);
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											}
										}
									}
								} else { //Calculated hour is less than 0
									if (Integer.parseInt(hour) > currentHour) { //Event hour is greater than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is before the current date
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else { // dates are equal
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent != 0) { // Format is PM
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										}
									} else { //Event hour is less than current hour
										if (Integer.parseInt(finalDate) == dateCurrent) { // Dates are equal
											if (amPmCurrent != 0) { //Format is PM
												if (Integer.parseInt(hour) == currentHour) { // Event hour equals to current hour
													if (Mins > 0) { //Calculated Minutes greater than 0
														if (Integer.parseInt(minutes) > currentMins) { //Event minutes greater than current minutes
															timing = "In "
																	+ Mins
																	+ " m";
														} else {
															timing = "Completed";
														}
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										} else { //event date is before current date or not equals
											if (Integer.parseInt(finalDate) > dateCurrent) {
												Hours = 23 - Integer
														.parseInt(hourCon);
												Mins = 60 - Integer
														.parseInt(minCon);
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										}
									}
								}
							} else { // Event hour is less than calculated hour
								if (Integer.parseInt(hour) > currentHour) { // Event hour is greater than current hour
									if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is greater than current date
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);
										timing = "In " + Hours + "h " + Mins
												+ " m";
									} else { //Event date is less than or equals to current date
										if (Integer.parseInt(finalDate) == dateCurrent) {
											if (amPmCurrent != 0) { //Format is PM
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										} else {
											timing = "Completed";
										}
									}
								} else { // Event hour is less than or equals to current hour
									if (Integer.parseInt(finalDate) == dateCurrent) { // dates are equal
										if (amPmCurrent != 0) { //Format is PM
											if (Integer.parseInt(hour) == currentHour) { // Event hour is equal to current hour
												if (Mins > 0) { //Calculated minutes is greater than 0
													if (Integer.parseInt(minutes) > currentMins) { //Event minutes are greater than current minutes
														timing = "In " + Mins
																+ " m";
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										} else {
											timing = "Completed";
										}
									} else { // Event date is on or before the date current
										if (Integer.parseInt(finalDate) > dateCurrent) {
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											timing = "Completed";
										}
									}
								}
							}
						} else { //Event time format is AM
							if (Integer.parseInt(hour) > Hours) { //Event hour is greater than calculated hours
								if (Hours > 0) { // Calculated hour is greater than 0
									if (amPmSmall.equals("am")) { // event Format is AM
										if (Integer.parseInt(hour) > currentHour) { // Event hour is greater than current hour
											if (Integer.parseInt(finalDate) > dateCurrent) { //Event date is before tha current date
												Hours = 23 - Integer
														.parseInt(hourCon);
												Mins = 60 - Integer
														.parseInt(minCon);
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else { // Dates are equals
												if (Integer.parseInt(finalDate) == dateCurrent) {
													if (amPmCurrent == 0) { // Current formaat is AM
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													} else {
														timing = "Completed";
													}
												} else {
													timing = "Completed";
												}
											}
										} else { // Event hour is less than current hour
											if (Integer.parseInt(finalDate) > dateCurrent) { //Event date is before the current date
												if (Integer.parseInt(finalDate) > dateCurrent) { 
													if (amPmCurrent == 0) { // Current format is AM
														Hours = 23 - Integer
																.parseInt(hourCon);
														Mins = 60 - Integer
																.parseInt(minCon);
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													} else { // Current format is PM
														Hours = 23 - Integer
																.parseInt(hourCon);
														Mins = 60 - Integer
																.parseInt(minCon);
														timing = "In " + Hours
																+ "h " + Mins
																+ " m";
													}
												} else { // Dates are equals
													if (Integer
															.parseInt(finalDate) == dateCurrent) {
														if (amPmCurrent == 0) { //Current format is AM
															timing = "In "
																	+ Hours
																	+ "h "
																	+ Mins
																	+ " m";
														} else {
															timing = "Completed";
														}
													} else {
														timing = "Completed";
													}
												}
											} else {
												timing = "Completed";
											}
										}
									} else {
										timing = "Completed";
									}
								} else { //Calculated hour is less than 0
									if (Integer.parseInt(hour) > currentHour) { //Event hour is greater than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) { //Event date is before the current date
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											timing = "Completed";
										}
									} else {
										timing = "Completed";
									}
								}
							} else { //Event hour less than current hour
								if (amPmSmall.equals("am")) { //Event format is equals to AM
									if (Integer.parseInt(hour) > currentHour) { //Event hour is greater than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) { //Event date is before the current date
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else { //Dates are equal
											if (Integer.parseInt(finalDate) == dateCurrent) {
												if (amPmCurrent == 0) { //Current format is equal to AM
													timing = "In " + Hours
															+ "h " + Mins
															+ " m";
												} else {
													timing = "Completed";
												}
											} else {
												timing = "Completed";
											}
										}
									} else { //Event hour is less than current hour
										if (Integer.parseInt(finalDate) > dateCurrent) { // Event date is before the current date
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In " + Hours + "h "
													+ Mins + " m";
										} else {
											timing = "Completed";
										}
									}

								} else { //Event format is equals to PM
									if (Hours > 0) { //Calculated hour is greater than 0
										if (Integer.parseInt(hour) > currentHour) { //Event hour is greater than current hour
											if (Integer.parseInt(finalDate) >= dateCurrent) { // Event date is on or before the current date
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										} else { //Event hour is less tahn current hour
											if (Integer.parseInt(finalDate) >= dateCurrent) { // Event date is on or before current date
												timing = "In " + Hours + "h "
														+ Mins + " m";
											} else {
												timing = "Completed";
											}
										}
									} else { //Calculated hour is less than 0
										timing = "Completed";
										if (Mins > 0) { //Calculated minutes is greater than 0
											if (Integer.parseInt(minutes) > currentMins) { //Event minutes is greater than current minutes
												timing = "In " + Mins + " m";
											} else {
												timing = "Completed";
											}
										} else {
											timing = "Completed";
										}
									}
								}
							}
						}
					}

				} else { //Event hour is less tahn 0
					if (Integer.parseInt(finalDate) == dateCurrent) { // Dates are equal
						if (amPmCurrent == 0) { // Current format is AM
							if (Integer.parseInt(hour) == 0) { // Event hour equals to 0
								if (currentHour == 0) { // Current hour equals to 0
									if (Integer.parseInt(minutes) > currentMins) { //Event minutes is greater than current minutes
										Mins = Integer.parseInt(minCon) + 1;
										timing = "In 0" + "h " + Mins + "m";
									} else {
										timing = "Completed";
									}
								} else {
									timing = "Completed";
								}
							} else { // Event hour greater than 0
								Hours = 23 - Integer.parseInt(hourCon);
								Mins = 60 - Integer.parseInt(minCon);
								timing = "In " + Hours + "h " + Mins + "m";
							}
						} else {
							timing = "Completed";
						}
					} else { // event date is before the current date
						Hours = 23 - Integer.parseInt(hourCon);
						Mins = 60 - Integer.parseInt(minCon);
						timing = "In " + Hours + "h " + Mins + "m";
					}
				}
			} else { //Day difference is greater than 0
				if (days == 1) { // days difference is equal to 1
					int countOfDays = Integer.parseInt(finalDate) - dateCurrent;
					if (amPmCurrent == 0) { // Current format is  equals to AM
						if (amPmSmall.equals("am")) { // Event format is equals to AM
							if (Integer.parseInt(hour) >= currentHour) { //Event hour is greater than or equals to current hour
								if (Integer.parseInt(minutes) > currentMins) { // Event minutes is greater than current Minutes
									Hours = Integer.parseInt(hourCon);
									Mins = Integer.parseInt(minCon) + 1;
									timing = "In 1d " + Hours + "h " + Mins
											+ "m";
								} else { //Event minutes is less than current minutes
									Hours = Integer.parseInt(hourCon);
									Mins = Integer.parseInt(minCon) + 1;
									timing = "In 1d " + Hours + "h " + Mins
											+ "m";
								}
							} else { //Event hour is less than current hour
								if (Integer.parseInt(hour) == 0) { //Event hour equals to 0
									if (Integer.parseInt(minutes) == 0) { // Event minutes equals to 0
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);

										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									} else { //Minutes is greater than 0
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);

										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									}
								} else { //Event hour greater than 0
									if (Integer.parseInt(minutes) > currentMins) { // Event minutes is greater than current minutes
										if (countOfDays > 1) { //No of days greater than 1
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In 1d " + Hours + "h "
													+ Mins + "m";
										} else { //No of days is 0
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);

											timing = "In " + Hours + "h "
													+ Mins + "m";
										}
									} else { // Event minutes is less than current minutes
										if (Integer.parseInt(hourCon) < 12) { //Hour difference is less than 12
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In 1d " + Hours + "h "
													+ Mins + "m";
										} else {
											Hours = Integer.parseInt(hourCon);
											Mins = Integer.parseInt(minCon) + 1;
											timing = "In 1d " + Hours + "h "
													+ Mins + "m";
										}
									}
								}
							}
						} else { // Event format is equals to PM
							if (Integer.parseInt(hour) >= currentHour) { //Event hour is greater than or equals to current hour
								if (Integer.parseInt(minutes) > currentMins) { // Event minutes is greater than current minutes
									Hours = Integer.parseInt(hourCon);
									Mins = Integer.parseInt(minCon) + 1;
									timing = "In 1d " + Hours + "h " + Mins
											+ "m";
								} else { // Event minutes is less than current minutes
									Hours = Integer.parseInt(hourCon);
									Mins = Integer.parseInt(minCon) + 1;
									timing = "In 1d " + Hours + "h " + Mins
											+ "m";
								}
							} else { //Event hour is less than current hour
								if (Integer.parseInt(minutes) > currentMins) { //Event minutes is greater than current minutes
									if (countOfDays > 1) { //No.of days difference is greater than 1
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);
										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									} else { // No.of days is less than 1
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);

										timing = "In " + Hours + "h " + Mins
												+ "m";
									}
								} else { //Event minutes is less than current minutes
									Hours = Integer.parseInt(hourCon);
									Mins = Integer.parseInt(minCon) + 1;
									timing = "In 1d " + Hours + "h " + Mins
											+ "m";
								}
							}
						}
					} else { // Current format is  equals to PM
						if (amPmSmall.equals("am")) { //Event time format is equal to AM
							if (Integer.parseInt(hour) >= currentHour) { //Event hour is greater than current hour
								if (Integer.parseInt(minutes) > currentMins) { // Event minutes is greater than current minutes
									Hours = 23 - Integer.parseInt(hourCon);
									Mins = 60 - Integer.parseInt(minCon);
									timing = "In " + Hours + "h " + Mins + "m";
								} else { // Event minutes is less than current minutes
									Hours = 23 - Integer.parseInt(hourCon);
									Mins = 60 - Integer.parseInt(minCon);
									timing = "In " + Hours + "h " + Mins + "m";
								}
							} else { //Event hour is less than current hour
								if (Integer.parseInt(hour) == 0) { //Event hour is equals to 0
									if (Integer.parseInt(minutes) == 0) { // Event minutes is equals to 0
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);

										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									} else { // Events minutes is greater or lesser than 0
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);

										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									}
								} else { // Event hour is greater or lesser than 0
									if (Integer.parseInt(minutes) > currentMins) { //Event minutes is greater than current minutes
										if (countOfDays > 1) { //No. of days difference is greater than 1 
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In 1d " + Hours + "h "
													+ Mins + "m";
										} else {
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);

											timing = "In " + Hours + "h "
													+ Mins + "m";
										}
									} else { // No.0f days difference is less than 1
										if (Integer.parseInt(hourCon) < 12) { // hour difference is greater than 12
											Hours = 23 - Integer
													.parseInt(hourCon);
											Mins = 60 - Integer
													.parseInt(minCon);
											timing = "In 1d " + Hours + "h "
													+ Mins + "m";
										} else { // hour difference is less than 12
											Hours = Integer.parseInt(hourCon);
											Mins = Integer.parseInt(minCon) + 1;
											timing = "In 1d " + Hours + "h "
													+ Mins + "m";
										}
									}
								}
							}
						} else { //Event time format is equal to PM
							if (Integer.parseInt(hour) >= currentHour) { // Event hour is greater or equals to current hour
								if (Integer.parseInt(minutes) > currentMins) { // Event minutes is greater than current minutes
									Hours = Integer.parseInt(hourCon);
									Mins = Integer.parseInt(minCon) + 1;
									timing = "In 1d " + Hours + "h " + Mins
											+ "m";
								} else { // Event minutes is less than current minutes
									if (Integer.parseInt(hour) == currentHour) { // event hour equals to current hour
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);
										timing = "In " + Hours + "h " + Mins
												+ "m";
									} else { // event hour is greater or less than current hour
										Hours = Integer.parseInt(hourCon);
										Mins = Integer.parseInt(minCon) + 1;
										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									}
								}
							} else { // event hour is less than current hour
								if (Integer.parseInt(minutes) > currentMins) { //Event minutes is greater than current minutes
									Hours = 23 - Integer.parseInt(hourCon);
									Mins = 60 - Integer.parseInt(minCon);

									timing = "In " + Hours + "h " + Mins + "m";
								} else { // event minutes is less than current minutes
									if (Integer.parseInt(minutes) > currentMins) { //Event minutes is greater than current minutes
										Hours = 23 - Integer.parseInt(hourCon);
										Mins = 60 - Integer.parseInt(minCon);

										timing = "In " + Hours + "h " + Mins
												+ "m";
									} else { // event minutes is less than current minutes
										Hours = Integer.parseInt(hourCon);
										Mins = Integer.parseInt(minCon) + 1;
										timing = "In 1d " + Hours + "h " + Mins
												+ "m";
									}
								}
							}
						}
					}
				} else { //Days difference more than 1
					if (amPmCurrent == 0) { // Current format is AM
						if (Integer.parseInt(hour) >= currentHour) { // Event hour is greater or equals to current hour
							Hours = Integer.parseInt(hourCon);
							Mins = Integer.parseInt(minCon) + 1;
						} else { // Event hour is less than to current hour
							if (amPmSmall.equals("am")) { //Event format is equals to AM
								Hours = 23 - Integer.parseInt(hourCon);
								Mins = 60 - Integer.parseInt(minCon);
							} else { //Event format is equals to PM
								Hours = Integer.parseInt(hourCon);
								Mins = Integer.parseInt(minCon);
							}
						}
					} else { // Current format is PM
						if (Integer.parseInt(hour) >= currentHour) { //Event hour is greater or equals to current hour
							if (amPmSmall.equals("am")) { // Event format is AM
								Hours = 23 - Integer.parseInt(hourCon);
								Mins = 60 - Integer.parseInt(minCon);
								days = days - 1;
							} else { // Event format is PM
								Hours = Integer.parseInt(hourCon);
								Mins = Integer.parseInt(minCon) + 1;
							}
						} else { //Event hour is less than to current hour
							Hours = 23 - Integer.parseInt(hourCon);
							Mins = 60 - Integer.parseInt(minCon);
							if (amPmSmall.equals("am")) { // Event format is AM
							} else { // Event format is PM
								days = days - 1;
							}
						}
					}

					//timing value has been set as days, hours and minutes.
					timing = "In " + (days) + "d " + Hours + "h " + Mins + "m";
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timing;
	}

	//MySchedule the date has been calculated
	@SuppressLint("InflateParams")
	public static String timeStartsAt(String timeStarts) {
		String amPm;
		String timeSplits[] = timeStarts.split(" ");

		String dateValue = timeSplits[0];
		String dateSplits[] = dateValue.split("-");
		String monthValue = dateSplits[1];
		String day = dateSplits[2]; //Split day
		String month = Utils.MonthValue(monthValue); // Split month
		String year = dateSplits[0]; //Split year

		String dateConverted = month + " " + day + ", " + year;

		String timeValue = timeSplits[1];
		String timingSplits[] = timeValue.split(":");
		String hour = timingSplits[0];
		String minutes = timingSplits[1];
		int hrs = Integer.parseInt(hour);
		//Format of AM/PM is calculated
		if (hrs >= 12) {
			hour = Utils.HourValue(hour);
			amPm = "PM";
		} else {
			amPm = "AM";
		}

		if (hour == null) {
			hour = "12";
		}

		return dateConverted + " " + hour + ":" + minutes + " " + amPm;
	}

	//Event hour has been converted to 12 hours format
	@SuppressLint("InflateParams")
	public static String timeStartsAtSchedule(String timeStarts) {
		String amPm;
		String timeSplits[] = timeStarts.split(" ");
		String timeValue = timeSplits[1];
		String timingSplits[] = timeValue.split(":");
		String hour = timingSplits[0];
		String minutes = timingSplits[1];
		int hrs = Integer.parseInt(hour);
		if (hrs >= 12) {
			hour = Utils.HourValue(hour);
			amPm = "PM";
		} else {
			amPm = "AM";
		}

		if (hour == null) {
			hour = "12";
		}

		return hour + ":" + minutes + " " + amPm;
	}

	//on and above 12 hours the hour has been converted 12 hours format
	@SuppressLint("InflateParams")
	public static String HourValue(String hour) {
		String hours = null;
		if (hour.equals("12")) {
			hours = "12";
		}
		if (hour.equals("13")) {
			hours = "1";
		}
		if (hour.equals("14")) {
			hours = "2";
		}
		if (hour.equals("15")) {
			hours = "3";
		}
		if (hour.equals("16")) {
			hours = "4";
		}
		if (hour.equals("17")) {
			hours = "5";
		}
		if (hour.equals("18")) {
			hours = "6";
		}
		if (hour.equals("19")) {
			hours = "7";
		}
		if (hour.equals("20")) {
			hours = "8";
		}
		if (hour.equals("21")) {
			hours = "9";
		}
		if (hour.equals("22")) {
			hours = "10";
		}
		if (hour.equals("23")) {
			hours = "11";
		}
		return hours;
	}

	//Month convertion method
	public static String MonthValue(String month) {
		String monthChanged = null;
		if (month.equals("01")) {
			monthChanged = "Jan";
		} else if (month.equals("02")) {
			monthChanged = "Feb";
		} else if (month.equals("03")) {
			monthChanged = "Mar";
		} else if (month.equals("04")) {
			monthChanged = "Apr";
		} else if (month.equals("05")) {
			monthChanged = "May";
		} else if (month.equals("06")) {
			monthChanged = "Jun";
		} else if (month.equals("07")) {
			monthChanged = "Jul";
		} else if (month.equals("08")) {
			monthChanged = "Aug";
		} else if (month.equals("09")) {
			monthChanged = "Sep";
		} else if (month.equals("10")) {
			monthChanged = "Oct";
		} else if (month.equals("11")) {
			monthChanged = "Nov";
		} else if (month.equals("12")) {
			monthChanged = "Dec";
		}
		return monthChanged;
	}

	public static enum ServerResponseEnum {
		OK, FALSE_NETWORK, NO_NETWORK, PARSER_ERROR;
	}

}
