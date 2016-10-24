package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gradapp.au.activities.R;
import com.gradapp.au.homescreen.CameraActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

@SuppressLint("InflateParams")
public class CameraAdapter extends BaseAdapter {

	CameraActivity context;
	Typeface typeFace, typeFaceLight;
	ArrayList<HashMap<String, String>> photoStreamArray = new ArrayList<HashMap<String, String>>();
	ImageLoader imageLoader;
	DisplayImageOptions options;

	public CameraAdapter(CameraActivity activity, Typeface typeface,
			Typeface typefaceLight,
			ArrayList<HashMap<String, String>> streamArray) {
		context = activity;
		typeFace = typeface;
		typeFaceLight = typefaceLight;
		photoStreamArray = streamArray;
		imageLoader = ImageLoader.getInstance();
		//ImageLoader initialize
		imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	@Override
	public int getCount() {
		// Size of ArrayList
		return photoStreamArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		LayoutInflater inflater = LayoutInflater.from(context);
		v = inflater.inflate(R.layout.camera_screen_items, null);
		TextView textViewForName = (TextView) v
				.findViewById(R.id.textForCameraItemName);
		TextView textViewForDate = (TextView) v
				.findViewById(R.id.textForCameraItemDate);
		final ImageView imageForCameraItems = (ImageView) v
				.findViewById(R.id.imageForCameraItems);
		final ProgressBar progressBar = (ProgressBar) v
				.findViewById(R.id.progressBarForImage);
		textViewForName.setText(photoStreamArray.get(arg0).get("streamName"));
		textViewForDate.setText(photoStreamArray.get(arg0).get("streamDate"));
		textViewForName.setTypeface(typeFace);
		textViewForDate.setTypeface(typeFaceLight);
		textViewForDate.setVisibility(View.GONE);
		// Display the stream image as iconic ImageView using ImageLoader
		imageLoader.displayImage(photoStreamArray.get(arg0).get("imageUrl"),
				imageForCameraItems, options, new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
						imageForCameraItems.setImageResource(R.drawable.icon);
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						super.onLoadingStarted(imageUri, view);
						progressBar.setVisibility(View.VISIBLE);
					}

				});
		return v;
	}

}
