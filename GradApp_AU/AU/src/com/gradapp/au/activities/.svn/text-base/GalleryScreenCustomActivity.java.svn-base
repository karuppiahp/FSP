package com.gradapp.au.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.StreamRetrieveTasks;
import com.gradapp.au.homescreen.CameraActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GalleryScreenCustomActivity extends SlidingMenuActivity implements
		OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, txtForStreamName;
	private GridView gridView;
	String imageUrls[], from, streamId, imageCount;
	int i = 0;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	ArrayList<HashMap<String, String>> streamGridArray = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> galleryArray = new ArrayList<HashMap<String, String>>();

	public ArrayList<String> galleryImages = new ArrayList<String>();
	ScrollView scrollView;
	LinearLayout layoutForImages;

	@SuppressWarnings("unused")
	private int minimumImages = 21, count = 0, page = 0;
	Thread thread;
	boolean isAllImageLoaded = false;

	ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_scroll_images_load);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(),
				"fonts/ProximaNova-Light.otf");

		from = getIntent().getExtras().getString("from");

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(GalleryScreenCustomActivity.this));

		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.cacheOnDisc(false).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.resetViewBeforeLoading(true).considerExifParams(true).build();

		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading..");

		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		txtForStreamName = (TextView) findViewById(R.id.txtForStreamName);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		textForHeader.setTypeface(typeFaceHeader);
		txtForStreamName.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		textForHeader.setTextColor(Color.parseColor("#FFFFFF"));
		txtForStreamName.setText("My Gallery");

		/*
		 * The activity comes from stream means the service will be called to fetch the images and else the images are fetched from device gallery
		 */
		if (from.equals("stream")) {
			streamId = getIntent().getExtras().getString("streamId");
			new StreamRetrieveTasks(GalleryScreenCustomActivity.this, streamId,
					streamGridArray, gridView, imageLoader, options).execute();
		} else {
			dialog.setCancelable(false);
			dialog.show();
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					defineWidgets();
				}
			}, 50);
		}

	}

	//Dynamically load the images in scrollview
	private void defineWidgets() {
		try {

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					GalleryScreenCustomActivity.this)
					.denyCacheImageMultipleSizesInMemory()
					.threadPriority(Thread.MAX_PRIORITY)
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

			scrollView = (ScrollView) findViewById(R.id.scrollView);
			layoutForImages = (LinearLayout) findViewById(R.id.layoutForScrollImages);

			scrollView.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					switch (arg1.getAction()) {
					case MotionEvent.ACTION_UP:
						excuteTimer(arg0);
						break;
					}
					return false;
				}
			});

			// the page = 0 initialize first time loading images(first 30 images)
			page = 0;
			imageUrls = Utils
					.fetchSdCardImages(GalleryScreenCustomActivity.this);
			new loadImages().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//If page count reaches 30 the next set of images can be loaded
	private class loadImages extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			int totalItems = imageUrls.length;
			int start = page * minimumImages;
			page++;
			int end;

			if (totalItems >= page * minimumImages) {
				end = page * minimumImages;
			} else {
				end = ((page - 1) * minimumImages)
						+ (imageUrls.length % minimumImages);
			}

			displayPhotos(start, end);
			dialog.dismiss();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			return null;
		}
	}

	//Display the images as dynamic with 3 columns with start and end counts
	@SuppressLint("InflateParams")
	private void displayPhotos(final int start, final int end) {

		count = 0;
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		int i = -1 + start;
		while (i < end) {
			View rowView = inflater.inflate(
					R.layout.custom_scroll_linear_horizontal, null);
			LinearLayout row = (LinearLayout) rowView
					.findViewById(R.id.layoutHorizontal);

			//Columns has been added dynamically with column size of 3
			for (int j = 0; j < 3; j++) {
				i = i + 1;
				if (i < end) {
					View singleImageView = inflater.inflate(
							R.layout.custom_scroll_image_items, null);
					final ImageView imageView = (ImageView) singleImageView
							.findViewById(R.id.scrollImages);

					imageView.setTag(i);

					try {
						String pathReplace = imageUrls[i].replace("file:/", "");
						Bitmap myBitmap = BitmapFactory.decodeFile(pathReplace);
						int height = (myBitmap.getHeight() * 512 / myBitmap
								.getWidth());
						Bitmap scale = Bitmap.createScaledBitmap(myBitmap, 512,
								height, true);
						int rotate = 0;
						ExifInterface exif = null;
						try {
							exif = new ExifInterface(pathReplace);
						} catch (IOException e) {
							e.printStackTrace();
						}
						//Orientation has been checked because in device sometimes the image saved in different orientation according to that the image has been checked and display in potrait mode.
						int orientation = exif.getAttributeInt(
								ExifInterface.TAG_ORIENTATION,
								ExifInterface.ORIENTATION_UNDEFINED);
						switch (orientation) {
						case ExifInterface.ORIENTATION_NORMAL:
							rotate = 0;
							break;
						case ExifInterface.ORIENTATION_ROTATE_270:
							rotate = 270;
							break;
						case ExifInterface.ORIENTATION_ROTATE_180:
							rotate = 180;
							break;
						case ExifInterface.ORIENTATION_ROTATE_90:
							rotate = 90;
							break;
						}

						try {
							Matrix matrix = new Matrix();
							matrix.postRotate(rotate);
							final Bitmap rotateBitmap = Bitmap.createBitmap(
									scale, 0, 0, scale.getWidth(),
									scale.getHeight(), matrix, true);

							imageLoader.loadImage(imageUrls[i], options,
									new SimpleImageLoadingListener() {
										@Override
										public void onLoadingComplete(
												String imageUri, View view,
												Bitmap loadedImage) {
											imageView
													.setImageBitmap(rotateBitmap);
										}

										@Override
										public void onLoadingStarted(
												String imageUri, View view) {
											super.onLoadingStarted(imageUri,
													view);
											imageView
													.setImageResource(R.drawable.icon);
										}
									});

						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					//ImageView clicks it redirects to PhotoOptions screen with string values passed along with intents.
					imageView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							String tag = imageView.getTag().toString();
							int position = Integer.parseInt(tag);

							Intent intentToOptions = new Intent(
									GalleryScreenCustomActivity.this,
									PhotoScreenOptions.class);
							intentToOptions.putExtra("from", "grid");

							if (imageUrls.length == 1) {
								imageCount = "equal";
							} else {
								imageCount = "greater";
							}

							intentToOptions.putExtra("pathFrom", "sdcard");
							intentToOptions.putExtra("imagePath",
									imageUrls[position]);
							intentToOptions.putExtra("imageName", "");
							intentToOptions.putExtra("imageId", "");
							intentToOptions.putExtra("imageUserId", "");
							intentToOptions.putExtra("imageCount", imageCount);
							startActivity(intentToOptions);
							finish();
						}
					});

					row.addView(singleImageView);
				}
			}

			layoutForImages.addView(row);
		}
	}

	// the method execute time used for detecting scroll ends.
	private void excuteTimer(final View arg0) {
		int quotient = imageUrls.length / minimumImages;
		int remainder = imageUrls.length % minimumImages;
		int totalpages = quotient + (remainder == 0 ? 0 : 1);
		if (totalpages > 1) {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					int totalpages = imageUrls.length / minimumImages;
					if (imageUrls.length % minimumImages == 0) {
					} else {
						totalpages++;
					}
					int totalHeight = scrollView.getChildAt(0).getHeight();

					int scrollY = arg0.getScrollY();
					int diff = totalHeight - arg0.getHeight();
					diff = diff - 500;

					if (scrollY >= diff) {
						if (page == totalpages) {
							Toast.makeText(getApplicationContext(),
									"No more items.", Toast.LENGTH_LONG).show();
						} else {
							isAllImageLoaded = false;
							new loadImages().execute();
						}
					}
				}
			}, 500);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}

		if (v.getId() == R.id.backBtn) {
			Intent intentToNotify = new Intent(
					GalleryScreenCustomActivity.this, CameraActivity.class);
			startActivity(intentToNotify);
			finish();
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		Intent intentToNotify = new Intent(GalleryScreenCustomActivity.this,
				CameraActivity.class);
		startActivity(intentToNotify);
		finish();
	}
}
