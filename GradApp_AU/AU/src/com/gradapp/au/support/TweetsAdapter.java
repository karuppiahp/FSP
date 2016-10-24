package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gradapp.au.activities.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class TweetsAdapter extends BaseAdapter {

	Context context;
	ArrayList<HashMap<String, String>> arrays = new ArrayList<HashMap<String, String>>();
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	String link, imageUrl;

	public TweetsAdapter(Context mContext,
			ArrayList<HashMap<String, String>> twitterArray) {
		context = mContext;
		arrays = twitterArray;
	}

	@Override
	public int getCount() {
		return arrays.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true).build();
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.twits_list_items, null);
		final ImageView imageView = (ImageView) v
				.findViewById(R.id.imgForTweets);
		final ProgressBar progressBar = (ProgressBar) v
				.findViewById(R.id.progress);
		TextView textForName = (TextView) v
				.findViewById(R.id.textForTwitterName);
		TextView textForSecs = (TextView) v
				.findViewById(R.id.textForTwitterSecs);
		TextView textForContent = (TextView) v
				.findViewById(R.id.txtForTweetContent);
		ImageView imageForTweets = (ImageView)v.findViewById(R.id.imageForTwitterTweets); 
		//User image icon link is from has been checked and the respective process will be held
		if (arrays.get(arg0).get("from").equals("fb")) {
			link = "http://graph.facebook.com/"
					+ arrays.get(arg0).get("imageUrl")
					+ "/picture?width=200&height=200";
		} else {
			link = arrays.get(arg0).get("imageUrl");
			imageUrl = arrays.get(arg0).get("mediaUrl");
		}
		
		//Image loaded for ImageIcon
		imageLoader.displayImage(link, imageView, options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						imageView.setImageResource(R.drawable.icon);
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						progressBar.setVisibility(View.GONE);
					}
				});
		
		//Image Loaded for images in the feeds.
		if(imageUrl != null && imageUrl.length() > 0) {
			imageForTweets.setVisibility(View.VISIBLE);
			imageLoader.displayImage(imageUrl, imageForTweets, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							imageView.setImageResource(R.drawable.icon);
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							progressBar.setVisibility(View.GONE);
						}
					});
		}
		textForName.setText(arrays.get(arg0).get("name"));
		textForSecs.setText(arrays.get(arg0).get("postedTime"));
		textForContent.setText(arrays.get(arg0).get("messageTxt"));
		return v;
	}
}
