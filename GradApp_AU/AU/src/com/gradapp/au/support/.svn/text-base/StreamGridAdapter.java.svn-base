package com.gradapp.au.support;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gradapp.au.activities.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class StreamGridAdapter extends BaseAdapter {
	private Activity mContext;
	ImageLoader imageLoader;
	ArrayList<HashMap<String, String>> streamArray = new ArrayList<HashMap<String, String>>();
	DisplayImageOptions options;
	ImageView imageView;
	ProgressBar progressBar;
	int orientation, width, height;

	public StreamGridAdapter(ImageLoader imageLoader,
			ArrayList<HashMap<String, String>> streamarray, Activity context,
			DisplayImageOptions options) {
		mContext = context;
		streamArray = streamarray;
		this.imageLoader = imageLoader;
		this.options = options;
	}

	@Override
	public int getCount() {
		return streamArray.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v = arg1;
		LayoutInflater inflator = LayoutInflater.from(mContext);
		v = inflator.inflate(R.layout.stream_grid_items, null);
		final ProgressBar progressBar = (ProgressBar) v
				.findViewById(R.id.progressBarForImage);
		final ImageView imageView = (ImageView) v
				.findViewById(R.id.imageForStreamGridItem);

		//Stream photo image is loaded
		String subImageKey = streamArray.get(arg0).get("photoUrl");
		imageLoader.displayImage(subImageKey, imageView, options,
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
		return v;
	}

}