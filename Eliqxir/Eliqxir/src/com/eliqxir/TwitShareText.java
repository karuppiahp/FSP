package com.eliqxir;

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



public class TwitShareText extends AsyncTask<String, String, String> {
	Context context;
	ProgressDialog progress;
	String msgTxt;
/*	SharedPreferences pref;
	ListView listView;
	int paging_count;
	String hashTagValue;
	EditText editTxtForTwet;*/
	

	public TwitShareText(String txt,Context mContext) {
		context = mContext;
		msgTxt = txt;
		/*listView = listview;
		paging_count = pagingcount;
		hashTagValue = hashTag;
		editTxtForTwet = editTxtTwet;*/
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
		builder.setOAuthConsumerSecret(SessionStores.getConsumerSecretKey(context));

		AccessToken accessToken = new AccessToken(SessionStores.getAccessToken(context),
				SessionStores.getAccessTokenSecret(context));
		Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
		try {
			twitter4j.Status response = twitter.updateStatus(msgTxt);
			return response.toString();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	protected void onPostExecute(String res) {
		if (res != null) {
			progress.dismiss();
			Toast.makeText(context, "Tweet Posted Sucessfully",Toast.LENGTH_SHORT).show();			
		} else {
			progress.dismiss();
			Toast.makeText(context, "Tweet Posted Failed !",Toast.LENGTH_SHORT).show();
		}

	}
}
