package com.eliqxir.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.exception.ExceptionUtils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;



public class MyExceptionHandler implements UncaughtExceptionHandler {
	private final Context myContext;

	public MyExceptionHandler(Context context) {

		myContext = context;

	}

	/** This is for catch an exception from any of application */
	public void uncaughtException(Thread thread, Throwable exception) {
		String description = getDescription(thread.toString(), exception);

		// THIS IS FOR WRITING ERRORS AS TEXT FILE AND SAVING IN SD CARD
		writefile(description);

		Log.e("Errorrrrrrrrrr    ","" + description);


		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);

	}

/** THIS IS FOR WRITING ERRORS AS TEXT FILE AND SAVING IN SD CARD */	
	private void writefile(String description) {
		// TODO Auto-generated method stub

		File sdcard = new File(Environment.getExternalStorageDirectory()
				+ "/EliqxirError");

		if (!sdcard.exists()) {
			sdcard.mkdir();
		}

		File file = new File(sdcard, "Exceptions.txt");

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file, true));
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

	public String getDescription(String arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		return "Thread: " + arg0 + ", Exception: "
				+ ExceptionUtils.getStackTrace(arg1);

	}
}
