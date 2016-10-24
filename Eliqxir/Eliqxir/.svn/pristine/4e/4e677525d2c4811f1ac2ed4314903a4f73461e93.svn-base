package com.eliqxir.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStore {

	public static boolean saveSlidinMenu(String menuSlider,Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("NavigateMenu", menuSlider);
		return editor.commit();
	}
	
	public static String getSlidinMenu(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences("session", 0);
		return savedSession.getString("NavigateMenu", null);
	}
	
	public static boolean saveListViewPosition(String position,Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("position", position);
		return editor.commit();
	}
	
	public static String getListViewPosition(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences("session", 0);
		return savedSession.getString("position", null);
	}
	
	public static boolean saveLocation(String position,Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("address", position);
		return editor.commit();
	}
	
	public static String getLocation(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences("session", 0);
		return savedSession.getString("address", null);
	}
	
	public static boolean saveCartTiming(String timing,Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("time", timing);
		return editor.commit();
	}
	
	public static String getCartTiming(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences("session", 0);
		return savedSession.getString("time", null);
	}
	
	public static boolean saveOrderPlaced(String placed,Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("orderPlaced", placed);
		return editor.commit();
	}
	
	public static String getOrderPlaced(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences("session", 0);
		return savedSession.getString("orderPlaced", null);
	}
}
