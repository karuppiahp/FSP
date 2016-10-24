package com.fsp.blacksheep;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fsp.blacksheep.R;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class Ads extends Activity {
	private Bitmap mIcon1;
	private int SPLASH_DISPLAY_LENGHT = 5000;
	private ImageView ads_close;
	// private ImageView ads_content;
	Bitmap mIcon_val;
	// private WebView ads_content;
	ImageView ads_content;
	private Bundle b;
	String url = null;
	BS_Bars bars = new BS_Bars();
	String bimage = null;
	String[] url_Array, shuffled_Array;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.ads_new);
			
			/* Enabling strict mode */
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			// ads_close = (ImageView) findViewById(R.id.back11);
			// ads_close.setVisibility(View.GONE);
			// ads_content = (WebView) findViewById(R.id.ads_content);
			ads_content = (ImageView) findViewById(R.id.ads_content);

			Bundle b = getIntent().getExtras();
			url_Array = b.getStringArray("img_key");
			for (int i = 0; i < url_Array.length; i++) {
			//	Log.v("url_array >>", url_Array[i]);
			}

			// List<String> wordList = Arrays.asList(url_Array);
			//
			// List<String> list = new ArrayList<String>(url_Array.length);
			// for (String s : url_Array) {
			// list.add(s);
			// }

			// shuffled_Array=url_Array;

			Random random = new Random();
			// Log.v("msg===============",url_Array[random.nextInt(url_Array.length)]);

			url = url_Array[random.nextInt(url_Array.length)];

			// list.remove(random.nextInt(url_Array.length));

			new Task_News_ArticleView().execute();
			// ads_content.setPictureListener( new WebView.PictureListener() {
			// @Override
			// public void onNewPicture(WebView arg0, Picture arg1) {
			// // TODO Auto-generated method stub
			// ads_content.pageDown(true);
			// }
			// });
			ads_content.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					openOptionsMenu();
					return false;
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// ads_close.setVisibility(View.VISIBLE);
		// }
		// }, SPLASH_DISPLAY_LENGHT);
		// ads_close.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuad, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.con:
			//Log.v("before finish >>", "before finish >>");
			finish();
			//Log.v("After finish >>", "After finish >>");
			break;
		case R.id.cancel:
			closeOptionsMenu();
			break;

		}
		return true;

	}

	private class Task_News_ArticleView extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(Ads.this);
		private int mProgressStatus = 0;

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				// while (mProgressStatus < 100) {
				// mProgressStatus = ads_content.getProgress();
				//
				// }
				HttpGet httpRequest = null;

				// httpRequest = new
				// HttpGet("http://www.theblacksheeponline.com/web_ads/somafriday.jpg");
				/*
				 * URL newurl = new URL(url); bimage=newurl.toString();
				 * httpRequest = new HttpGet(bimage); BitmapFactory.Options
				 * bmOptions; bmOptions = new BitmapFactory.Options();
				 * bmOptions.inSampleSize = 1; HttpClient httpclient = new
				 * DefaultHttpClient(); HttpResponse response = (HttpResponse)
				 * httpclient.execute(httpRequest); HttpEntity entity =
				 * response.getEntity(); BufferedHttpEntity bufHttpEntity = new
				 * BufferedHttpEntity(entity); InputStream instream =
				 * bufHttpEntity.getContent();
				 * 
				 * InputStream in = new java.net.URL(bimage).openStream();
				 * mIcon_val = BitmapFactory.decodeStream(new
				 * PatchInputStream(in));
				 */
				// mIcon_val =
				// BitmapFactory.decodeStream(instream,null,bmOptions);

				try {
					InputStream is = new java.net.URL(url).openStream();
					mIcon_val = BitmapFactory
							.decodeStream(new PatchInputStream(is));
					// mIcon_val=ImageHelper.getRoundedCornerBitmap(mIcon_val,
					// 30);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception e) {

			}
			return null;

		}

		protected void onPostExecute(Void result) {
			// ads_content.setImageBitmap(mIcon_val);
			// ads_content.loadData(bimage, "image/jpeg", "utf-8");
			// ads_content.loadUrl(url);
			// ads_content.loadUrl(url);
			Drawable drawable = loadImagefromurl(mIcon_val);
			ads_content.setBackgroundDrawable(drawable);
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	public Drawable loadImagefromurl(Bitmap icon) {
		Drawable d = new BitmapDrawable(icon);
		return d;
	}

}
