package com.gradapp.au.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.SocialMediaActivity;
import com.gradapp.au.utils.SessionStores;

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
		View view = inflater.inflate(R.layout.twitter_login_fragment,
				container, false);
		twitter = new TwitterFactory().getInstance();
		//ConsumerKey and secretkey are get from session and passed
		twitter.setOAuthConsumer(SessionStores.getConsumerKey(getActivity()),
				SessionStores.getConsumerSecretKey(getActivity()));

		new TokenGet().execute();//To get access token
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
				e.printStackTrace();
			}
			return oauth_url;
		}

		@SuppressLint("SetJavaScriptEnabled")
		@Override
		protected void onPostExecute(String oauth_url) {
			if (oauth_url != null) {
				progressDialog.dismiss();
				auth_dialog = new Dialog(getActivity());
				auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				auth_dialog.setContentView(R.layout.twitter_auth_dialog);
				web = (WebView) auth_dialog.findViewById(R.id.webv);
				progressBar = (ProgressBar) auth_dialog
						.findViewById(R.id.progressBar);
				web.getSettings().setJavaScriptEnabled(true);
				web.loadUrl(oauth_url);
				web.setWebViewClient(new WebViewClient() {
					boolean authComplete = false;

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						super.onPageStarted(view, url, favicon);
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
						progressBar.setVisibility(View.GONE);
						if (url.contains("oauth_verifier")
								&& authComplete == false) {
							//Verified the credentials
							authComplete = true;
							Uri uri = Uri.parse(url);
							oauth_verifier = uri
									.getQueryParameter("oauth_verifier");

							auth_dialog.dismiss();
							//AccessToken process
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
				//The user details has been fetched and stored in session
				accessToken = twitter.getOAuthAccessToken(requestToken,
						oauth_verifier);
				SessionStores.saveAccessToken(accessToken.getToken(),
						getActivity());
				SessionStores.saveAccessTokenSecret(
						accessToken.getTokenSecret(), getActivity());
				User user = twitter.showUser(accessToken.getUserId());
				profile_url = user.getOriginalProfileImageURL();
				name = user.getName();
				SessionStores
						.saveTwitterUserName(user.getName(), getActivity());
				SessionStores.saveTwitterUserImage(
						user.getOriginalProfileImageURL(), getActivity());
				SessionStores.saveTwitterUserId(
						String.valueOf(accessToken.getUserId()), getActivity());

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
				getActivity().finish();
				//After process finished it redirects to SocialMediaActivity
				Intent intentToHome = new Intent(getActivity(),
						SocialMediaActivity.class);
				startActivity(intentToHome);

			}
		}

	}
}
