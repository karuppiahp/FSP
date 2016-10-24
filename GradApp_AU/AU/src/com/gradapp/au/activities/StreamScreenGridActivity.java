package com.gradapp.au.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gradapp.au.AsyncTasks.StreamRetrieveTasks;
import com.gradapp.au.homescreen.CameraActivity;
import com.gradapp.au.slidingmenu.SlidingMenuActivity;
import com.gradapp.au.support.GalleryGridAdapter;
import com.gradapp.au.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class StreamScreenGridActivity extends SlidingMenuActivity implements OnClickListener {

	Typeface typeFace, typeFaceHeader, typeFaceLight;
	ImageButton btnForiconSlider, btnForHamberger, btnForBack;
	TextView textForHeader, txtForStreamName;
	private GridView gridView;
	String imageUrls[], from, streamId, imageCount;
	int i=0;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	ArrayList<HashMap<String, String>> streamGridArray = new ArrayList<HashMap<String,String>>();
	ArrayList<HashMap<String, String>> galleryArray = new ArrayList<HashMap<String,String>>();
	
	 public ArrayList<String> galleryImages = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stream_grid);
		
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Semibold.otf");
		typeFaceHeader = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Regular.otf");
		typeFaceLight = Typeface.createFromAsset(getAssets(), "fonts/ProximaNova-Light.otf");
		
		from = getIntent().getExtras().getString("from");
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				StreamScreenGridActivity.this)
				.denyCacheImageMultipleSizesInMemory()
				.threadPriority(Thread.MAX_PRIORITY)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		
		options = new DisplayImageOptions.Builder()
	    .cacheInMemory(false).cacheOnDisc(false)
	    .imageScaleType(ImageScaleType.EXACTLY)
	    .bitmapConfig(Bitmap.Config.RGB_565)
	    .resetViewBeforeLoading(true).considerExifParams(true).build();

		
		btnForiconSlider = (ImageButton) findViewById(R.id.btnSliderMenu);
		btnForHamberger = (ImageButton) findViewById(R.id.btnHamberger);
		btnForBack = (ImageButton) findViewById(R.id.backBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		gridView = (GridView) findViewById(R.id.gridView);
		txtForStreamName = (TextView) findViewById(R.id.txtForStreamName);
		textForHeader.setTypeface(typeFaceHeader);
		txtForStreamName.setTypeface(typeFaceHeader);
		btnForiconSlider.setVisibility(View.GONE);
		btnForHamberger.setVisibility(View.VISIBLE);
		btnForHamberger.setOnClickListener(this);
		btnForBack.setVisibility(View.VISIBLE);
		btnForBack.setOnClickListener(this);
		textForHeader.setTextColor(Color.parseColor("#FFFFFF"));
		
		//Before creating GalleryScreenCustom activity the gallery gridview is loaded in this class with condition of where it comes from
		//If it comes from stream screen the Stream images will be retrieved or else the device gallery images are retrieved.
		//GalleryScreenCutomActivity is created for load the gallery images as count of 30 images per set after it reaches end it fetches other 30 to avoid memory leaks.
		if(from.equals("stream")) {
			streamId = getIntent().getExtras().getString("streamId");
			new StreamRetrieveTasks(StreamScreenGridActivity.this, streamId, streamGridArray, gridView, imageLoader, options).execute();
		} else {
			String imageCount = getIntent().getExtras().getString("imageCount");
			if(!(imageCount.equals("equal"))) {
				
				imageUrls = Utils.fetchSdCardImages(getBaseContext());
				if(imageUrls != null && imageUrls.length > 0) {
					Toast.makeText(StreamScreenGridActivity.this, "Please wait the images are fetching", Toast.LENGTH_SHORT).show();
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							GalleryGridAdapter customGridAdapter = new GalleryGridAdapter(imageLoader, imageUrls,
									StreamScreenGridActivity.this, options);
							gridView.setAdapter(customGridAdapter);
						}
					}, 200);
				} else {
					Utils.ShowAlertImage(this, "There is no images found");
				}
			} else {
				Utils.ShowAlertImage(this, "There is no images found");
			}
		}
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intentToOptions = new Intent(StreamScreenGridActivity.this, PhotoScreenOptions.class);
				intentToOptions.putExtra("from", "grid");
				if(from.equals("stream")) {
					intentToOptions.putExtra("pathFrom", "url");
					intentToOptions.putExtra("streamId", streamId);
					intentToOptions.putExtra("imagePath", streamGridArray.get(arg2).get("photoUrl"));
					intentToOptions.putExtra("imageName", streamGridArray.get(arg2).get("photoName"));
					intentToOptions.putExtra("imageId", streamGridArray.get(arg2).get("photoId"));
					intentToOptions.putExtra("imageUserId", streamGridArray.get(arg2).get("photoUserId"));
				} else {
					if(imageUrls.length == 1) {
						imageCount = "equal";
					} else {
						imageCount = "greater";
					}
					intentToOptions.putExtra("pathFrom", "sdcard");
					intentToOptions.putExtra("imagePath", imageUrls[arg2]);
					intentToOptions.putExtra("imageName", "");
					intentToOptions.putExtra("imageId", "");
					intentToOptions.putExtra("imageUserId", "");
					intentToOptions.putExtra("imageCount", imageCount);
				}
				startActivity(intentToOptions);
				finish();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnHamberger) {
			showSecondaryMenu();
		}
		
		if(v.getId() == R.id.backBtn) {
			Intent intentToNotify = new Intent(StreamScreenGridActivity.this, CameraActivity.class);
			startActivity(intentToNotify);
			finish();
		}
	}
	
	public void onBackPressed() {
		super.onBackPressed();
		Intent intentToNotify = new Intent(StreamScreenGridActivity.this, CameraActivity.class);
		startActivity(intentToNotify);
		finish();
	}
}
