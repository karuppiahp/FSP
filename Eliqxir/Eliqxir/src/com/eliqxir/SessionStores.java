package com.eliqxir;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStores {

	public static boolean saveConsumerKey(String consumerKey,Context context) {
		  Editor editor = context.getSharedPreferences("twitter", 0).edit();
		  editor.putString("consumerKey", consumerKey);
		  return editor.commit();
		 }
		 
		 public static String getConsumerKey(Context context) {
		  SharedPreferences savedSession = context.getSharedPreferences("twitter", 0);
		  return savedSession.getString("consumerKey", null);
		 }
		 
		 public static boolean saveConsumerSecretKey(String secretKey,Context context) {
		  Editor editor = context.getSharedPreferences("twitter", 0).edit();
		  editor.putString("secretKey", secretKey);
		  return editor.commit();
		 }
		 
		 public static String getConsumerSecretKey(Context context) {
		  SharedPreferences savedSession = context.getSharedPreferences("twitter", 0);
		  return savedSession.getString("secretKey", null);
		 }
		 
		 public static boolean saveAccessToken(String accessToken,Context context) {
		  Editor editor = context.getSharedPreferences("twitter", 0).edit();
		  editor.putString("accessToken", accessToken);
		  return editor.commit();
		 }
		 
		 public static String getAccessToken(Context context) {
		  SharedPreferences savedSession = context.getSharedPreferences("twitter", 0);
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
}
