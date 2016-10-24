package com.gradapp.au.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStores {

	public static List<String> menuNameList = new ArrayList<String>();
	public static List<String> menuIdList = new ArrayList<String>();

	public static boolean saveLoginPref(String type, Context context) {
		Editor editor = context.getSharedPreferences("login", 0).edit();
		editor.putString("loginas", type);
		return editor.commit();
	}

	public static String getLoginPref(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences("login",
				0);
		return savedSession.getString("loginas", null);
	}

	public static boolean saveConsumerKey(String consumerKey, Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("consumerKey", consumerKey);
		return editor.commit();
	}

	public static String getConsumerKey(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("consumerKey", null);
	}

	public static boolean saveConsumerSecretKey(String secretKey,
			Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("secretKey", secretKey);
		return editor.commit();
	}

	public static String getConsumerSecretKey(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("secretKey", null);
	}

	public static boolean saveAccessToken(String accessToken, Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("accessToken", accessToken);
		return editor.commit();
	}

	public static String getAccessToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("accessToken", null);
	}

	public static boolean saveAccessTokenSecret(String accessTokenSecret,
			Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("accessTokenSecret", accessTokenSecret);
		return editor.commit();
	}

	public static String getAccessTokenSecret(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("accessTokenSecret", null);
	}

	public static boolean saveTwitterUserName(String userName, Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("userName", userName);
		return editor.commit();
	}

	public static String getTwitterUserName(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("userName", null);
	}

	public static boolean saveTwitterUserImage(String userImage, Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("userImage", userImage);
		return editor.commit();
	}

	public static String getTwitterUserImage(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("userImage", null);
	}

	public static boolean saveTwitterUserId(String userId, Context context) {
		Editor editor = context.getSharedPreferences("twitter", 0).edit();
		editor.putString("userId", userId);
		return editor.commit();
	}

	public static String getTwitterUserId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"twitter", 0);
		return savedSession.getString("userId", null);
	}

	public static boolean saveListViewPosition(String position, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("position", position);
		return editor.commit();
	}

	public static String getListViewPosition(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("position", null);
	}

	public static boolean saveUnivId(String position, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("univ", position);
		return editor.commit();
	}

	public static String getUnivId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("univ", null);
	}

	public static boolean saveRoleType(String position, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("role", position);
		return editor.commit();
	}

	public static String getRoleType(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("role", null);
	}
	
	public static boolean saveRoleName(String position, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("roleName", position);
		return editor.commit();
	}

	public static String getRoleName(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("roleName", null);
	}

	public static boolean saveSchoolId(String position, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("school", position);
		return editor.commit();
	}

	public static String getSchoolId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("school", null);
	}
	
	public static boolean saveSchoolName(String schoolName, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("schoolName", schoolName);
		return editor.commit();
	}

	public static String getSchoolName(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("schoolName", null);
	}

	public static boolean saveUserId(String position, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("userId", position);
		return editor.commit();
	}

	public static String getUserId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("userId", null);
	}

	public static boolean saveMenuArray(Context context,
			List<String> hamburgerArrayName) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putInt("menuNameSize", hamburgerArrayName.size());
		for (int i = 0; i < hamburgerArrayName.size(); i++) {
			editor.remove("menuName" + i);
			editor.putString("menuName" + i, hamburgerArrayName.get(i));
		}
		return editor.commit();
	}

	public static List<String> loadMenuArray(Context context) {
		menuNameList.clear();
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		int size = savedSession.getInt("menuNameSize", 0);
		for (int i = 0; i < size; i++) {
			menuNameList.add(savedSession.getString("menuName" + i, null));
		}
		return menuNameList;
	}

	public static boolean saveMenuIdArray(Context context,
			List<String> hamburgerArrayId) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putInt("menuIdSize", hamburgerArrayId.size());
		for (int i = 0; i < hamburgerArrayId.size(); i++) {
			editor.remove("menuId" + i);
			editor.putString("menuId" + i, hamburgerArrayId.get(i));
		}
		return editor.commit();
	}

	public static List<String> loadMenuIdArray(Context context) {
		menuIdList.clear();
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		int size = savedSession.getInt("menuIdSize", 0);
		for (int i = 0; i < size; i++) {
			menuIdList.add(savedSession.getString("menuId" + i, null));
		}
		return menuIdList;
	}

	public static boolean saveAlertOption(String alert, Context context) {
		Editor editor = context.getSharedPreferences("session", 0).edit();
		editor.putString("alert", alert);
		return editor.commit();
	}

	public static String getAlertOption(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"session", 0);
		return savedSession.getString("alert", null);
	}
	
	public static boolean saveAppId(String appId, Context context) {
		Editor editor = context.getSharedPreferences("app_id", 0).edit();
		editor.putString("app_id", appId);
		return editor.commit();
	}

	public static String getAppId(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"app_id", 0);
		return savedSession.getString("app_id", null);
	}
	
	public static boolean saveUnreadCount(String count, Context context) {
		Editor editor = context.getSharedPreferences("count", 0).edit();
		editor.putString("unreadCount", count);
		return editor.commit();
	}

	public static String getUnreadCount(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"count", 0);
		return savedSession.getString("unreadCount", null);
	}
	
	public static boolean saveStudentType(String count, Context context) {
		Editor editor = context.getSharedPreferences("studentType", 0).edit();
		editor.putString("studentType", count);
		return editor.commit();
	}

	public static String getStudentType(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(
				"studentType", 0);
		return savedSession.getString("studentType", null);
	}
}
