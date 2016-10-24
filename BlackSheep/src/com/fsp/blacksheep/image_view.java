package com.fsp.blacksheep;

import java.io.File;

import com.flurry.android.FlurryAgent;
import com.fsp.blacksheep.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BS.zoom.ImageZoomView;
import com.BS.zoom.SimpleZoomListener;
import com.BS.zoom.ZoomState;
import com.BS.zoom.SimpleZoomListener.ControlType;

public class image_view extends Activity {

	private ImageZoomView mZoomView;

	/** Zoom state */
	private ZoomState mZoomState;

	/** Decoded bitmap image */
	private Bitmap mBitmap;

	/** On touch listener for zoom view */
	private SimpleZoomListener mZoomListener;

	private ImageView imgvw_next;
	private ImageView imgvw_back;
	private GridView gview;
	private boolean san_netcheck = false;
	GridAdapter g_adp;
	private Button but_next;
	private Button but_back;
	private Bundle b;
	public int image;
	int id = 0;
	int totno = 0;
	static int click = 10;
	LinearLayout f_layout;
	// LinearLayout s_layout;
	LinearLayout t_layout;
	RelativeLayout r_layout;
	RelativeLayout p_layout;
	// String url[]=null;
	WebView webview = null;
	ImageView img_view = null;
	Intent intent = null;
	// ImageView banner = null;
	String e_url = null;
	String urlbanner = "";
	String tonoimage = null;
	static String[] bufferarray = new String[2000];
	private static final String TEMP_PHOTO_FILE = "tempBSPhoto.PNG";
	String filepath = null;
	static String[] zoombufferarray1 = new String[2000];
	String[] VAL1;
	static int zo = 0;
	static int zo1 = 0;
	static int tit = 0;
	static String url = null;
	// String burl="http://50.57.227.117/blacksheep/mobile/partypicBanner.php";
	int pageno = 1;
	String query, query1;
	// private Button prev;
	ArrayList<String> buff = new ArrayList<String>();
	imageclass img = imageclass.getInstance();
	String[] zoombufferarray = new String[2000];
	Bitmap mIcon_val = null;
	Bitmap mIcon_val1 = null;
	Bitmap map = null;
	Button share, submit, load;
	Bitmap bm = null;
	Bitmap bm1 = null;
	Cursor actualimagecursor;
	ContentValues values;
	String img_path = null;
	String state = null;
	String blink = null;
	String bimage = null;
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;

	// GoogleAnalyticsTracker tracker;
	// TextView txt_head;
	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(image_view.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("Party pics Started");
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);

		// tracker = GoogleAnalyticsTracker.getInstance();

		// Start the tracker in manual dispatch mode...
		// tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1", 20,this);
		// tracker.trackPageView("/image_view");
		/*
		 * Context mCtx = this; // Get current context. GoogleAnalytics
		 * myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * myInstance.setDebug(true); Tracker myNewTracker =
		 * myInstance.getTracker("UA-36318648-1");
		 * myInstance.setDefaultTracker(myNewTracker);
		 * myNewTracker.trackView("Party pics Started");
		 */
	}

	public void onCreate(Bundle savedInstanceState) {

		// try {
		//
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// setContentView(R.layout.picture_main);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		if (dm.widthPixels == 480 && dm.heightPixels == 800) {
			Log.v("big", "msg");
			Log.v("width1", Integer.toString(dm.widthPixels));
			setContentView(R.layout.picture_main1);

		} else {
			Log.v("width2", Integer.toString(dm.widthPixels));
			setContentView(R.layout.picture_main);
		}

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		r_layout = (RelativeLayout) findViewById(R.id.LinearLayout01);
		r_layout.setVisibility(View.GONE);
		gview = (GridView) findViewById(R.id.jr_lookbook_grid);
		but_back = (Button) findViewById(R.id.back);
		but_next = (Button) findViewById(R.id.next);
		share = (Button) findViewById(R.id.photoshare);
		load = (Button) findViewById(R.id.click);
		load.setVisibility(View.GONE);
		submit = (Button) findViewById(R.id.submitgrid);
		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Task_displaysdcard().execute();
			}

		});
		share.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				openOptionsMenu();

			}

		});
		mZoomState = new ZoomState();
		mZoomListener = new SimpleZoomListener();
		mZoomListener.setZoomState(mZoomState);
		mZoomView = (ImageZoomView) findViewById(R.id.zoomview);

		Bundle b = getIntent().getExtras();
		tonoimage = b.getString("tonoimage");
		id = b.getInt("id");
		int noimg = id + 1;
		pageno = b.getInt("pageno");
		bm = b.getParcelable("banner");
		blink = b.getString("bnrlink");
		img.print_thumbimage_array();
		img.print_zoomimage_array();
		zoombufferarray = b.getStringArray("zoomurl");
		new Task_img().execute();
		mZoomView.setZoomState(mZoomState);
		but_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (san_netcheck == true) {
					displayAlert();
				} else {
					id--;
					new Task_img().execute();
				}
			}

		});

		but_next.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// netCheck();
				if (san_netcheck == true) {
					displayAlert();
				} else {

					try {
						int limit = id + 1;
						if (limit % 16 == 0 && limit >= zoombufferarray.length) {

							new Task_News_ArticleView().execute();

						} else {
							id++;
							Log.v("id", Integer.toString(id));
							new Task_img().execute();
						}

					} catch (Exception e) {
						Log.v("Error in after 15", e.toString());
					}
				}

			}

		});
	}

	public boolean netCheck() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			// notify user you are not online
			san_netcheck = true;
		}

		return san_netcheck;
	}

	public void displayAlert() {
		new AlertDialog.Builder(this)
				.setMessage(
						"Please Check Your Internet Connection and Try Again")
				.setTitle("Network Error")
				.setCancelable(true)
				.setNeutralButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v("imageview", "destroy");
		if (bm != null)
			bm.recycle();
		mZoomView.setOnTouchListener(null);
		mZoomView.setImage(null);
		mZoomState.deleteObservers();
		if (mBitmap != null)
			mBitmap.recycle();
	}

	@Override
	protected void onStop() {
		Log.v("imageview", "stop");
		super.onStop();
		FlurryAgent.onEndSession(image_view.this);
		EasyTracker.getInstance().activityStop(this);
	}

	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}

	private void imagedestory() {
		Log.v("imgdestorymethod", "destroymethod");
		bm.recycle();
		mZoomView.setOnTouchListener(null);
		mZoomView.setImage(null);
		mZoomState.deleteObservers();
		mBitmap.recycle();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1: {
			if (resultCode == RESULT_OK) {
				Uri photoUri = data.getData();

				if (photoUri != null) {
					try {
						// new code
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = getContentResolver().query(photoUri,
								filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor
								.getColumnIndex(filePathColumn[0]);
						String filePath = cursor.getString(columnIndex);
						cursor.close();
						Uri U = Uri.parse("file://" + filePath);
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						emailIntent
								.putExtra(
										android.content.Intent.EXTRA_EMAIL,
										new String[] { "mPics@theblacksheeponline.com" });
						emailIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT,
								"Picture From My Android Mobile!");
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
								Html.fromHtml("<br><br>")
										+ "Sent From My Android Mobile");
						emailIntent.setType("image/png");
						emailIntent.putExtra(Intent.EXTRA_STREAM, U);
						emailIntent.setType("vnd.android.cursor.dir/email");
						startActivity(Intent.createChooser(emailIntent,
								"Email:"));

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}

		}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.new_game:

			if (Environment.getExternalStorageState().equals("mounted")) {
				new Task_SDSave().execute();
			} else {
				Toast.makeText(image_view.this, "Please Mount Your SD Card..",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.quit:
			if (Environment.getExternalStorageState().equals("mounted")) {

				new Task_SD().execute();
			} else {
				Toast.makeText(image_view.this, "Please Mount Your SD Card..",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.reset:
			resetZoomState();
			break;
		case R.id.pan:
			mZoomListener.setControlType(ControlType.PAN);
			break;
		case R.id.zoom:
			mZoomListener.setControlType(ControlType.ZOOM);
			break;

		}
		return true;

	}

	private class Task_xmlparser extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);

		// can use UI thread here
		protected void onPreExecute() {

			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Log.v("url in thread", url);
				g_adp = new GridAdapter(image_view.this, url);

			} catch (Exception e) {
				Log.v("DO_TAG", "error " + e);
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			gview.setAdapter(g_adp);

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	private class Task_Zoom_Cont extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);
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
				while (mProgressStatus < 100) {
					mProgressStatus = webview.getProgress();

				}
			} catch (Exception e) {

			}
			return null;

		}

		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	private class Task_img extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);
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

				URL newurl = new URL(zoombufferarray[id]);
				Log.d("zoombufferarray", zoombufferarray[id]);
				BitmapFactory.Options bmOptions;
				bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				HttpGet httpRequest = null;
				httpRequest = new HttpGet(zoombufferarray[id]);
				Log.d("zoombufferarray", zoombufferarray[id]);
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient
						.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(
						entity);
				InputStream instream = bufHttpEntity.getContent();
				if (mBitmap != null)
					mBitmap.recycle();
				mBitmap = BitmapFactory.decodeStream(instream, null, bmOptions);
			} catch (Exception e) {
				// bm.recycle();

			}
			return null;

		}

		protected void onPostExecute(Void result) {
			int noimg = id + 1;
			if (noimg > 1) {
				but_back.setVisibility(View.VISIBLE);
			} else
				but_back.setVisibility(View.INVISIBLE);

			if (mZoomView != null)
				mZoomView.setImage(null);
			mZoomView.setImage(mBitmap);
			mZoomView.setOnTouchListener(mZoomListener);
			resetZoomState();

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	private class Task_News_ArticleView extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);
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
				id++;
				pageno++;
				// changed
				url = ParserClass.imageViewUrl + "" + pageno;

				List<Message_thbnail2> l_obj_tnail = new ArrayList<Message_thbnail2>();
				l_obj_tnail = parse_tnail2();

				Iterator<Message_thbnail2> it = l_obj_tnail.iterator();
				while (it.hasNext()) {
					img.setThumbimage(it.next().toString());
				}
				img.print_thumbimage_array();

				List<Message_zoom> l_obj_zoom = new ArrayList<Message_zoom>();
				l_obj_zoom = parse_zoom();
				Iterator<Message_zoom> it_zoom = l_obj_zoom.iterator();

				int i = 0;

				while (it_zoom.hasNext()) {

					img.setZoomimage(it_zoom.next().toString());

				}
				img.print_zoomimage_array();

				zoombufferarray = img.ary_zoomimage();

			} catch (Exception e) {

			}
			return null;

		}

		private List<Message_zoom> parse_zoom() {
			// TODO Auto-generated method stub
			List<Message_zoom> zoommessages = new ArrayList<Message_zoom>();
			String res = Parsing_JSON.readFeed(url);
			try {
				JSONObject job1 = new JSONObject(res);
				String bl = job1.getString("blacksheep");
				JSONArray j_arr = new JSONArray(bl);
				JSONObject job2 = j_arr.getJSONObject(1);
				String first_str = job2.getString("path");
				JSONArray j_arr2 = new JSONArray(first_str);
				for (int i = 0; i < j_arr2.length(); i++) {
					Message_zoom zoommessage = new Message_zoom();
					JSONObject inn_obj = j_arr2.getJSONObject(i);
					String zoom = inn_obj.getString("zoom");
					zoommessage.setZoom(zoom);
					// //Log.v("zoom",message.toString1());
					zoommessages.add(zoommessage);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return zoommessages;
		}

		private List<Message_thbnail2> parse_tnail2() {
			// TODO Auto-generated method stub
			List<Message_thbnail2> messages = new ArrayList<Message_thbnail2>();
			String res = Parsing_JSON.readFeed(url);
			try {
				JSONObject job1 = new JSONObject(res);
				String bl = job1.getString("blacksheep");
				JSONArray j_arr = new JSONArray(bl);
				JSONObject job2 = j_arr.getJSONObject(1);
				String first_str = job2.getString("path");
				JSONArray j_arr2 = new JSONArray(first_str);
				for (int i = 0; i < j_arr2.length(); i++) {
					Message_thbnail2 message = new Message_thbnail2();
					JSONObject inn_obj = j_arr2.getJSONObject(i);
					String thumb = inn_obj.getString("thumb");
					query = URLEncoder.encode(thumb, "utf-8");
					// //Log.v("Query",query);
					query1 = URLDecoder.decode(query);
					// //Log.v("Query1",query1);
					if (query1.contains("%20%20")) {
						message.setThumbnail(query1);
					} else {
						message.setThumbnail(thumb);
					}

					messages.add(message);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return messages;
		}

		protected void onPostExecute(Void result) {

			// img_view.setImageBitmap(mIcon_val);
			new Task_img().execute();
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}

	}

	private class Task_SD extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);
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

				URL url = new URL(zoombufferarray[id]);
				URLConnection ucon = url.openConnection();
				InputStream in = ucon.getInputStream();
				if (bm1 != null)
					bm1.recycle();
				bm1 = BitmapFactory.decodeStream(in);
				in.close();
				OutputStream outStream = null;
				File file = new File(Environment.getExternalStorageDirectory(),
						TEMP_PHOTO_FILE);
				filepath = file.toString();
				outStream = new FileOutputStream(file);
				bm1.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();

			} catch (Exception e) {

			}
			return null;

		}

		protected void onPostExecute(Void result) {
			try {
				Uri U = Uri.parse("file://" + filepath);
				File f = new File(filepath);
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Picture From My Android Mobile!");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						Html.fromHtml("<br><br>")
								+ "Sent From My Android Mobile");
				emailIntent.setType("image/png");
				emailIntent.putExtra(Intent.EXTRA_STREAM, U);
				emailIntent.setType("vnd.android.cursor.dir/email");
				startActivity(Intent.createChooser(emailIntent, "Email:"));
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
			}
		}

	}

	private class Task_SDSave extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				tit++;
				URL url = new URL(zoombufferarray[id]);
				URLConnection ucon = url.openConnection();
				InputStream in = ucon.getInputStream();
				if (bm1 != null)
					bm1.recycle();
				bm1 = BitmapFactory.decodeStream(in);
				in.close();
				ContentValues values = new ContentValues(3);
				values.put(Media.DISPLAY_NAME, "Bs_image" + tit + ".PNG");
				values.put(Media.MIME_TYPE, "image/png");
				Uri uri = getContentResolver().insert(
						Media.EXTERNAL_CONTENT_URI, values);

				OutputStream outStream = getContentResolver().openOutputStream(
						uri);
				bm1.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.close();

			} catch (Exception e) {

			}
			return null;

		}

		protected void onPostExecute(Void result) {
			Toast.makeText(image_view.this, "Saved", Toast.LENGTH_LONG).show();

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}

	}

	private class Task_displaysdcard extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				image_view.this);
		private int mProgressStatus = 0;

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, 1);

			return null;

		}

		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

}
