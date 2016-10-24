package com.eliqxir;

import com.eliqxir.utils.Constant;

import com.eliqxir.utils.Utils;

import twitter4j.Twitter;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;



@SuppressLint("NewApi")
public class TwitterLoginActivity extends Fragment {
	ImageView login;
	Twitter twitter;
	RequestToken requestToken = null;
	AccessToken accessToken;
	String oauth_url, oauth_verifier, profile_url, name;
	Dialog auth_dialog;
	WebView web;
	SharedPreferences pref;
	ProgressDialog progress, progressDialog;
	Bitmap bitmap;
	ProgressBar progressBar;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.twitter_login,
				container, false);
		Utils.trackError(getActivity());
		/*requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.final_screen);*/
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(SessionStores.getConsumerKey(getActivity()),SessionStores.getConsumerSecretKey(getActivity()));

		new TokenGet().execute();
		return view;
	}

	private class TokenGet extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Loading ...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setIndeterminate(true);
			progressDialog.show();
			progressDialog.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				requestToken = twitter.getOAuthRequestToken();
				oauth_url = requestToken.getAuthorizationURL();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return oauth_url;
		}

		@SuppressLint("SetJavaScriptEnabled")
		@Override
		protected void onPostExecute(String oauth_url) {
			if (oauth_url != null) {
				progressDialog.dismiss();
				Log.e("URL", oauth_url);
				auth_dialog = new Dialog(getActivity());
				auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

				auth_dialog.setContentView(R.layout.twitter_auth_dialog);
				web = (WebView) auth_dialog.findViewById(R.id.webv);
			//	progressBar = (ProgressBar) auth_dialog.findViewById(R.id.progressBar);
				progressDialog.setMessage("Loading ...");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.setIndeterminate(true);
				
				web.getSettings().setJavaScriptEnabled(true);
				web.loadUrl(oauth_url);
				web.setWebViewClient(new WebViewClient() {
					boolean authComplete = false;

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						super.onPageStarted(view, url, favicon);
			//			progressBar.setVisibility(View.VISIBLE);
						progressDialog.show();
						progressDialog.setCancelable(false);
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
			//			progressBar.setVisibility(View.GONE);
						progressDialog.dismiss();
						
						if (url.contains("oauth_verifier")
								&& authComplete == false) {
							authComplete = true;
							Log.e("Url", url);
							Uri uri = Uri.parse(url);
							oauth_verifier = uri
									.getQueryParameter("oauth_verifier");

							auth_dialog.dismiss();
							new AccessTokenGet().execute();
						} else if (url.contains("denied")) {
							auth_dialog.dismiss();
							Toast.makeText(getActivity(),
									"Sorry !, Permission Denied",
									Toast.LENGTH_SHORT).show();

						}
					}
				});
				auth_dialog.show();
				auth_dialog.setCancelable(true);

			} else {
				progressDialog.dismiss();
				Toast.makeText(getActivity(),
						"Sorry !, Network Error or Invalid Credentials",
						Toast.LENGTH_SHORT).show();

			}
		}
	}

	private class AccessTokenGet extends AsyncTask<String, String, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Fetching Data ...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.show();

		}

		@Override
		protected Boolean doInBackground(String... args) {

			try {

				accessToken = twitter.getOAuthAccessToken(requestToken,oauth_verifier);
				SessionStores.saveAccessToken(accessToken.getToken(),getActivity());
				SessionStores.saveAccessTokenSecret(accessToken.getTokenSecret(),getActivity());
				User user = twitter.showUser(accessToken.getUserId());
		//		profile_url = user.getOriginalProfileImageURL();
				name = user.getName();
				/*	SessionStores.saveTwitterUserName(user.getName(),TwitterLoginActivity.this);
				SessionStores.saveTwitterUserImage(
						user.getOriginalProfileImageURL(), getActivity());
				SessionStores.saveTwitterUserId(String.valueOf(accessToken.getUserId()),TwitterLoginActivity.this);*/

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean response) {
			if (response) {
				progress.dismiss();
				TwitterShareAlert();
			}
		}
	}
	
	@SuppressLint("InflateParams")
	public void TwitterShareAlert() {
		LayoutInflater li = LayoutInflater.from(getActivity());
		View promptsView = li.inflate(R.layout.prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		userInput.setText(Constant.textToShare);
		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Share",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result
								// edit text
//								new PostToTwitter(userInput.getText()
//										.toString()).execute();
								Log.e("Text To Senddddd",userInput.getText().toString());
								new TwitShareText(userInput.getText().toString(),getActivity()).execute();
							//	finish();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}
