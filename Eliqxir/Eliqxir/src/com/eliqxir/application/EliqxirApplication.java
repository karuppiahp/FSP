package com.eliqxir.application;

import com.eliqxir.utils.Utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class EliqxirApplication extends Application{
	
	private static Context context;
	
	@Override
	public void onCreate(){
		super.onCreate();
		Utils.trackError(getApplicationContext());
		context = null;
		context = getApplicationContext();
	}

	public static Context getGlobalContext(){
		return context;
	}
	
	public static Resources getAppResources(){
		return context.getResources();
	}
	
	public static String getAppString(int resourceId, Object... formatArgs){
		return getAppResources().getString(resourceId, formatArgs);
	}
	
	public static String getAppString(int resourceId){
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
