package com.gradapp.au.support;

import android.app.ProgressDialog;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Callback extends WebViewClient {
	private ProgressDialog pd;
	WebView webview;

	//Load the Weblink in webview using Caallback 
	public Callback(WebView webView, ProgressDialog pd) {
		this.pd = pd;
		webview = webView;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		pd.show();
		webview.loadUrl(url);
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		if (pd.isShowing()) {
			pd.dismiss();
		}
	}
}
