package com.eliqxir.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.application.EliqxirApplication;

public class Utils {
	public static String lineFeed = System.getProperty("line.separator");
	public static final boolean SHOULD_PRINT_LOG = true;

	private static boolean isValid;

	static Date datePref, dateCurrent;
	static String cartTime;

	public static void trackError(Context con) {
		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(con));
	}

	public static void debugLog(String tag, String message) {
		if (SHOULD_PRINT_LOG) {
			Log.d(tag, message);
		}
	}

	public static void errorLog(String tag, String message) {
		if (SHOULD_PRINT_LOG) {
			Log.e(tag, message);
		}
	}

	public static void infoLog(String tag, String message) {
		if (SHOULD_PRINT_LOG) {
			Log.i(tag, message);
		}
	}

	public static void printException(Exception exception) {
		errorLog("Exception", exception.toString());
	}

	public static boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) EliqxirApplication
				.getGlobalContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isEmailValid(String email) {
		// isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public static void timeDiff(Context context, DBAdapter db) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		String dateFrmt = format.format(c.getTime());
		cartTime = SessionStore.getCartTiming(context);
		if (cartTime != null) {
			try {
				datePref = format.parse(cartTime);
				dateCurrent = format.parse(dateFrmt);
				long difference = dateCurrent.getTime() - datePref.getTime();
				int days = (int) (difference / (1000 * 60 * 60 * 24));
				int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
				int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))
						/ (1000 * 60);
				Log.i("System current timing min:::::::::", "" + min);

				if (min >= 30) {
					SessionStore.saveCartTiming(null, context);
					Constant.cartArray.clear();
					db.open();
					db.deleteRows();
					db.close();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			SessionStore.saveCartTiming(dateFrmt, context);
		}
	}

	public static void ShowAlert(Context context, String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setMessage(message);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {				
				dialog.cancel();
			}
		});
		AlertDialog alert = adb.create();
		alert.show();
	}

	public static void ShowNetworkAlert(Context context, String message) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setMessage(message);
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});
		AlertDialog alert = adb.create();
		alert.show();
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

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
	
	public static void writefile(String description, String filename) {
		// TODO Auto-generated method stub

		File sdcard = new File(Environment.getExternalStorageDirectory()
				+ "/Eliqxir");

		if (!sdcard.exists()) {
			sdcard.mkdir();
		}

		File file = new File(sdcard, filename);

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.ENGLISH);
			String formattedDate = df.format(c.getTime());
			bufferedWriter.append(formattedDate);
			bufferedWriter.newLine();
			bufferedWriter
					.append("***********************************************************"
							+ "\n");
			bufferedWriter.newLine();
			bufferedWriter.append(description);
			bufferedWriter.newLine();
			bufferedWriter
					.append("***********************************************************"
							+ "\n");
			bufferedWriter.newLine();
			bufferedWriter.newLine();

			bufferedWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
