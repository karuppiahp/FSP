package com.gradapp.au.twitter;

import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.HashTweetCountTask;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;

public class TwitShareText extends AsyncTask<String, String, String> {
	Context context;
	ProgressDialog progress;
	String msgTxt;
	SharedPreferences pref;
	ListView listView;
	int paging_count;
	String hashTagValue;
	EditText editTxtForTwet;
	ArrayList<HashMap<String, String>> hashtagArray = new ArrayList<HashMap<String,String>>();
	

	public TwitShareText(Context mContext, String txt, ListView listview, int pagingcount, String hashTag, EditText editTxtTwet, ArrayList<HashMap<String, String>> hashArray) {
		context = mContext;
		msgTxt = txt;
		listView = listview;
		paging_count = pagingcount;
		hashTagValue = hashTag;
		editTxtForTwet = editTxtTwet;
		hashtagArray = hashArray;
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

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(SessionStores.getConsumerKey(context));
		builder.setOAuthConsumerSecret(SessionStores
				.getConsumerSecretKey(context));
		//AccessToken has to be passed as params while sharing text on feeds
		AccessToken accessToken = new AccessToken(
				SessionStores.getAccessToken(context),
				SessionStores.getAccessTokenSecret(context));
		Twitter twitter = new TwitterFactory(builder.build())
				.getInstance(accessToken);
		try {
			//update the text in twitter feeds
			twitter4j.Status response = twitter.updateStatus(msgTxt);
			return response.toString();
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
			//Tweet count has been maintained in backend so the tweetCount api has been called.
			new HashTweetCountTask(context, Constant.hashTagId).execute();
			//After text has been tweeted the Feeds are fetched and display in screen
			new GetGradAppTweets(context, listView, paging_count,
					Constant.hashTagValue, editTxtForTwet, "edittext", msgTxt, hashtagArray).execute();
		} else {
			progress.dismiss();
			Toast.makeText(context, "Error while tweeting !",
					Toast.LENGTH_SHORT).show();
		}

	}
}
