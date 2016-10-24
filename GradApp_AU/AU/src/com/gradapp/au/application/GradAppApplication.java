package com.gradapp.au.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.gradapp.au.support.IntentReceiver;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class GradAppApplication extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = null;
		context = getApplicationContext();
		
		//UrbanAirship functionalities
		AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
		
		// ------------- Urban Airship production keys --------------------
		options.developmentAppKey="1Da-uM4iS_u2OxSCzlMvOg";
	 	options.developmentAppSecret="Nk8T5AHgS0uYYg_xaaRj7Q";
		options.gcmSender="513573235086";
	 	options.transport="gcm";	 	
	 	options.inProduction=false;	
	 	UAirship.takeOff(this, options);
	    PushManager.enablePush();
	    PushManager.shared().setIntentReceiver(IntentReceiver.class);
	}

	/*
	 * getGlobalContext is the method used to return context of application.
	 */
	public static Context getGlobalContext() {
		return context;
	}

	/*
	 * Resources are used to access the static contents, xml files and more.
	 */
	public static Resources getAppResources() {
		return context.getResources();
	}

	/*
	 * To pass the n no.of objects in the method by return the string using get resources method of to access the xml files and more.
	 */
	public static String getAppString(int resourceId, Object... formatArgs) {
		return getAppResources().getString(resourceId, formatArgs);
	}

	/*
	 * Pass the static, xml files as args to get the string as in return. 
	 */
	public static String getAppString(int resourceId) {
		return getAppResources().getString(resourceId);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
}
