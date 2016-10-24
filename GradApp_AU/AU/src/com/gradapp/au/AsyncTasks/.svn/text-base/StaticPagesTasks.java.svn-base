package com.gradapp.au.AsyncTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.hamburger.menus.StaticPages;
import com.gradapp.au.support.DBAdapter;
import com.gradapp.au.support.ServerResponse;
import com.gradapp.au.support.ServerResponse.RequestType;
import com.gradapp.au.utils.UrlGenerator;

@SuppressLint("SetJavaScriptEnabled")
public class StaticPagesTasks extends AsyncTask<Void, Void, Boolean> {
	ProgressDialog dialog;
	StaticPages context;
	String status, message, menuId, title = "", content = "";
	TextView textForTitle, textForContent;
	WebView webView;
	String GoogleDocs = "http://docs.google.com/gview?embedded=true&url=";
	DBAdapter db;
	boolean valueInDb = false;
	Matcher matcher;

	public StaticPagesTasks(StaticPages mContext, String menuid,
			TextView textTitle, TextView textContent, WebView webview) {
		context = mContext;
		menuId = menuid;
		textForTitle = textTitle;
		textForContent = textContent;
		webView = webview;
		db = new DBAdapter(context);
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading..");
	}

	public void onPreExecute() {
		super.onPreExecute();
		dialog.show();
		dialog.setCancelable(false);
	}

	/*
	 * API URL has been called from the class URLGenerator and passed as argument in Server response constructor for server connection
	 * Params are passed as in POST method
	 */
	@Override
	protected Boolean doInBackground(Void... arg0) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("hamburger_menu_id", menuId));
		JSONObject jsonObj = new ServerResponse(UrlGenerator.getStaticPages())
				.getJSONObjectfromURL(RequestType.POST, nameValuePairs);
		try {
			if (jsonObj != null) {
				status = jsonObj.getString("status");
				message = jsonObj.getString("msg");
				if (status.equals("1")) {
					JSONArray jsonArray = jsonObj.getJSONArray("Static Page");
					for (int i = 0; i < jsonArray.length(); i++) {
						title = jsonArray.getJSONObject(i).getString("Title");
						content = jsonArray.getJSONObject(i)
								.getString("Conten");

						//Save the datas in database
						db.open();
						Cursor c = db.getAllStaticPages();
						int count = c.getCount();
						if (count > 0) {
							if (c.moveToFirst()) {
								do {
									String hambrgrId = c.getString(1);
									if (hambrgrId.equals(menuId)) {
										db.updateStaticPages(hambrgrId, title,
												content);
										valueInDb = true;
										break;
									}
								} while (c.moveToNext());

								if (valueInDb == false) {
									//insert
									db.insertToStaticPages(menuId, title,
											content);
								}
							}
						} else {
							db.insertToStaticPages(menuId, title, content);
						}
						c.close();
						db.close();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onPostExecute(Boolean result) {
		textForTitle.setText(title);
		if (content.length() > 0) {
			if (content.contains(".pdf")) {
				//page containg PDF file link
				webView.setVisibility(View.VISIBLE);
				WebSettings webSettings = webView.getSettings();
				webSettings.setBuiltInZoomControls(true);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.getSettings().setLoadWithOverviewMode(true);
				webView.getSettings().setUseWideViewPort(true);
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						dialog.dismiss();
					}
				});
				webView.loadUrl(GoogleDocs + content);
			} else {
				dialog.dismiss();
				textForContent.setText(Html.fromHtml(content));
				textForContent.setMovementMethod(LinkMovementMethod
						.getInstance());
			}
		} else {
			dialog.dismiss();
			Toast.makeText(context, "No values are found", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
