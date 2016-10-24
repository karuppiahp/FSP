package com.gradapp.au.twitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.gradapp.au.support.TweetsAdapter;
import com.gradapp.au.utils.Constant;
import com.gradapp.au.utils.SessionStores;
import com.gradapp.au.utils.Utils;

@SuppressLint("SimpleDateFormat")
public class GetGradAppTweets extends AsyncTask<Void, Void, Void> {

	Context context;
	ListView listView;
	String messageTxt, mediaType, postedTime, name, imageUrl, hashTag,
			fromText, mssageText;
	ArrayList<HashMap<String, String>> twitterArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> hashTagArray = new ArrayList<HashMap<String, String>>();
	ProgressDialog progress;
	int paging_count = 0;
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	String replaced;
	SharedPreferences pref;
	EditText editText;

	public GetGradAppTweets(Context mContext, ListView listViews,
			int pageCount, String hashArray, EditText edittext, String from,
			String msgTxt, ArrayList<HashMap<String, String>> hashListArray) {
		context = mContext;
		listView = listViews;
		paging_count = pageCount;
		progress = new ProgressDialog(context);
		hashTag = hashArray;
		fromText = from;
		mssageText = msgTxt;
		hashTagArray = hashListArray;
		progress.setMessage("Fetching...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		editText = edittext;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress.show();

		if (fromText.equals("edittext")) {
			HashMap<String, String> hashValue = new HashMap<String, String>();
			hashValue.put("messageTxt", mssageText);
			hashValue.put("mediaUrl", "");
			hashValue.put("postedTime", "1 sec");
			hashValue.put("name", SessionStores.getTwitterUserName(context));
			hashValue.put("imageUrl",
					SessionStores.getTwitterUserImage(context));
			hashValue.put("from", "twitter");
			twitterArray.add(hashValue);
		}
	}

	@Override
	protected Void doInBackground(Void... params) {

		ConfigurationBuilder builder1 = new ConfigurationBuilder();
		builder1.setOAuthConsumerKey(SessionStores.getConsumerKey(context));
		builder1.setOAuthConsumerSecret(SessionStores
				.getConsumerSecretKey(context));

		AccessToken acesstoken1 = new AccessToken(
				SessionStores.getAccessToken(context),
				SessionStores.getAccessTokenSecret(context));
		Twitter twitter = new TwitterFactory(builder1.build())
				.getInstance(acesstoken1);
		// hastag are fetch from api and passed here
		for (int i = 0; i < hashTagArray.size(); i++) {
			try {
				Query query = new Query(hashTagArray.get(i).get("hashTag"));
				// get count of feeds as 100 for each hashtag
				query.count(100);
				// According to the hashtag the feeds are fetched
				QueryResult result = twitter.search(query);
				List<twitter4j.Status> home_timeLine = result.getTweets();
				for (twitter4j.Status homeTime : home_timeLine) {
					String mediaUrl = "";
					messageTxt = homeTime.getText();
					for (MediaEntity mediaEntity : homeTime.getMediaEntities()) {
						mediaType = mediaEntity.getType();
						if (mediaType.equals("photo")) {
							mediaUrl = mediaEntity.getMediaURL();
						} else {
							mediaUrl = "";
						}
					}

					postedTime = sdf.format(homeTime.getCreatedAt());
					String finalValue = Utils.calculateHoursPost(postedTime,
							"Twitter");
					name = homeTime.getUser().getName();
					imageUrl = homeTime.getUser().getOriginalProfileImageURL();

					HashMap<String, String> hashValue = new HashMap<String, String>();
					hashValue.put("messageTxt", messageTxt);
					hashValue.put("mediaUrl", mediaUrl);
					hashValue.put("postedTime", finalValue);
					hashValue.put("name", name);
					hashValue.put("imageUrl", imageUrl);
					hashValue.put("from", "twitter");
					twitterArray.add(hashValue);
				}
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Constant.hashTagValue = hashTag;
		Log.i("Twitter array size:", "" + twitterArray.size());
		listView.setAdapter(new TweetsAdapter(context, twitterArray));
		progress.dismiss();
		editText.setHint(hashTag + " will be added automatically");
	}
}
