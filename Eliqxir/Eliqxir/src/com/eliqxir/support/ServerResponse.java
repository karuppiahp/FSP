package com.eliqxir.support;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
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

import android.util.Log;

import com.eliqxir.utils.Utils;

public class ServerResponse {
	public static final String tag = "ServerResponse Value";

	private String url;

	public ServerResponse(String url) {
		this.url = url;
	//	Utils.debugLog(tag, "Url -> " + url);
	}

	public JSONObject getJSONObjectfromURL(RequestType requestType,
			List<NameValuePair> params) {

		if (params!=null) {
			for (int i = 0; i < params.size(); i++) {
				NameValuePair nv = params.get(i);
				Log.e(nv.getName(), nv.getValue());
			}
		}

		// initialize
		InputStream is = null;
		String result = "";
		JSONObject jObject = null;

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = null;
			if (requestType == RequestType.GET) {
				HttpGet httpGet = new HttpGet(url);
				response = httpclient.execute(httpGet);
			} else if (requestType == RequestType.POST) {
				HttpPost httppost = new HttpPost(url);
				httppost.setEntity(new UrlEncodedFormEntity(params));				
				response = httpclient.execute(httppost);
			}
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Utils.printException(e);
		}

		// convert response to string
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
			Utils.printException(e);
		}

		// try parse the string to a JSON object
		try {
			jObject = new JSONObject(result);
		} catch (JSONException e) {
			Utils.printException(e);
		}
//		Utils.debugLog(tag, "JsonObject -> " + jObject);
		return jObject;
	}

	public JSONArray getJSONArrayfromURL(RequestType requestType) {

		// initialize
		InputStream is = null;
		String result = "";
		JSONArray jArray = null;

		// http post
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
			Utils.printException(e);
		}

		// convert response to string
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
			Utils.printException(e);
		}

		// try parse the string to a JSON object
		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			Utils.printException(e);
		}
//		Utils.debugLog(tag, "JsonObject -> " + jArray);
		return jArray;
	}

	public static enum RequestType {
		GET, POST
	}
}
