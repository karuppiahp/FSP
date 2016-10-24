package com.gradapp.au.twitter;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gradapp.au.utils.SessionStores;

public class TwitShareImage extends AsyncTask<String, String, String> {
	Context context;
	ProgressDialog progress;
	String filePath;
	SharedPreferences pref;

	public TwitShareImage(Context mContext, String path) {
		context = mContext;
		filePath = path;
		progress = new ProgressDialog(context);
		progress.setMessage("Posting...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress.show();
	}

	protected String doInBackground(String... args) {

		StatusUpdate status = new StatusUpdate("test");
		status.setMedia(new File(filePath));
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(SessionStores.getConsumerKey(context));
		builder.setOAuthConsumerSecret(SessionStores
				.getConsumerSecretKey(context));
		//AccessToken has to be passed to share on feeds
		AccessToken accessToken = new AccessToken(
				SessionStores.getAccessToken(context),
				SessionStores.getAccessTokenSecret(context));
		Twitter twitter = new TwitterFactory(builder.build())
				.getInstance(accessToken);
		try {
			twitter.updateStatus(status);
			return "success";
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(String res) {
		if (res != null) {
			progress.dismiss();
			Toast.makeText(context, "Tweet Sucessfully Posted",
					Toast.LENGTH_SHORT).show();
		} else {
			progress.dismiss();
			Toast.makeText(context, "Error while tweeting !",
					Toast.LENGTH_SHORT).show();
		}

	}
}
