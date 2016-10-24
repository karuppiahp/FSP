package com.gradapp.au.support;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gradapp.au.activities.R;
import com.gradapp.au.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GalleryGridAdapter extends BaseAdapter {

	private Activity context;
	ImageLoader imageLoader;
	String[] imageUrls;
	DisplayImageOptions options;

	public GalleryGridAdapter(ImageLoader imageLoader, String[] imageUrls,
			Activity mContext, DisplayImageOptions options) {
		context = mContext;
		this.imageLoader = imageLoader;
		this.imageUrls = imageUrls;
		this.options = options;
	}

	@Override
	public int getCount() {
		return imageUrls.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		View view = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater.inflate(R.layout.row_grid, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.image);
			holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (imageUrls[position] != null && imageUrls[position].contains("#$%")) {
			final Bitmap rotated;
			//Image name contains #$%90
			if (imageUrls[position].contains("#$%90")) {
				try {
					String pathReplace = imageUrls[position].replace("file:/",
							"");
					File file = new File(pathReplace);
					if (file.exists()) {
						Bitmap myBitmap = Utils.getBitmap(pathReplace, context);
						rotated = Utils.rotateImage(myBitmap, 90); //Rotate degrees
						holder.imageView.setImageBitmap(rotated);
					} else {
						holder.imageView.setImageResource(R.drawable.icon);
					}

				} catch (Exception e) {
					e.printStackTrace();
					holder.imageView.setImageResource(R.drawable.icon);
				}

			} else if (imageUrls[position].contains("#$%270")) { //Image name contains #$%270
				try {
					String pathReplace = imageUrls[position].replace("file:/",
							"");
					File file = new File(pathReplace);
					if (file.exists()) {
						Bitmap myBitmap = Utils.getBitmap(pathReplace, context);
						rotated = Utils.rotateImage(myBitmap, 270); // Rotate degrees
						holder.imageView.setImageBitmap(rotated);
					} else {
						holder.imageView.setImageResource(R.drawable.icon);
					}
				} catch (Exception e) {
					e.printStackTrace();
					holder.imageView.setImageResource(R.drawable.icon);
				}

			}
		} else {
			try {
				try {
					imageLoader.displayImage(imageUrls[position],
							holder.imageView, options,
							new SimpleImageLoadingListener() {

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view,
											loadedImage);
									holder.progressBar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
									super.onLoadingFailed(imageUri, view,
											failReason);
									holder.progressBar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
									super.onLoadingStarted(imageUri, view);
									holder.progressBar
											.setVisibility(View.VISIBLE);
								}

							});
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				holder.imageView.setImageResource(R.drawable.icon);
			}
		}

		return view;
	}

	class ViewHolder {
		ImageView imageView;
		ProgressBar progressBar;
	}
}
