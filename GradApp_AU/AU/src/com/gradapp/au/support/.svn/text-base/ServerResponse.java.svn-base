package com.gradapp.au.support;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

public class ServerResponse {
	public static final String tag = "ServerResponse";

	private String url;

	public ServerResponse(String url) {
		this.url = url;
		Log.i("Url ->", "" + url);
	}

	/*
	 * To return JSONObject type response the following method has been used.
	 */
	public JSONObject getJSONObjectfromURL(RequestType requestType,
			List<NameValuePair> params) {

		InputStream is = null;
		String result = "";
		JSONObject jObject = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = null;
			if (requestType == RequestType.GET) {
				HttpGet httpGet = new HttpGet(url);
				response = httpclient.execute(httpGet);
			} else if (requestType == RequestType.POST) {
				HttpPost httppost = new HttpPost(url);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("User-Agent", "Apache-HttpClient/4.1 (java 1.5)");
				httppost.setHeader("Host", "grad-app.com");
				httppost.setHeader("Authorization",getB64Auth("admin", "admin123"));
				httppost.setEntity(new UrlEncodedFormEntity(params));
				response = httpclient.execute(httppost);
			}
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (IOException e) {
			Log.v("IOException ***********8",""+e);
			SystemClock.sleep(1500);
		       Log.e("", "IOException: " + e.getMessage());
		} catch (Exception e) {
		} 

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
		}

		try {
			jObject = new JSONObject(result);
		} catch (JSONException e) {
		}
		Log.i("string result>>>>>",""+result);
		Log.i("JsonObject->", "" + jObject);
		return jObject;
	}

	/*
	 * To return JSONArray type response the following method has been used.
	 */
	public JSONArray getJSONArrayfromURL(RequestType requestType) {

		InputStream is = null;
		String result = "";
		JSONArray jArray = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = null;
			if (requestType == RequestType.GET) {
				HttpGet httpGet = new HttpGet(url);
				response = httpclient.execute(httpGet);
			} else if (requestType == RequestType.POST) {
				HttpPost httppost = new HttpPost(url);
				response = httpclient.execute(httppost);
			}
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
		}

		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
		}
		return jArray;
	}

	public static enum RequestType {
		GET, POST
	}
	
	/*
	 * getB64Auth is used to encode the string to acces the api by passing username and password in Http request.
	 */
	private String getB64Auth (String login, String pass) {
		   String source=login+":"+pass;
		   String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
		   return ret;
		 }
}
