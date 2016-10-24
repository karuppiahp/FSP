package com.fsp.blacksheep;

import com.flurry.android.FlurryAgent;
import com.fsp.blacksheep.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;








import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BlackSheepimage extends Activity implements OnClickListener,
		OnItemClickListener {
	/** Called when the activity is first created. ing */
	public boolean black_netcheck=false;
	String[] VAL1;
	String[] VAL2;
	LayoutInflater mInflater;
	GridAdapter g_adp;
	GridAdapter_thumb g_adpthumb;
	String[] zoombufferarray;
	String[] zoombufferarray1;
	String tonoimage=null;
	int zo = 0;
	int k = -1;
	int zo1 = 0;
	int k1 = -1;
	int pageno = 1;
	String[] thumbnail_disp;
	static GridView lay2_gv;
	private Button prev;
	private LinearLayout web_lay;
	static String url = null;
	static int bufersize = 0;
	public Button but_next;
	public Button submit;
	static int id = 0;
	Intent img_intent = null;
	Intent in = null;
	TextView tv;
	int close=0;
	ArrayList<String> buff = new ArrayList<String>();
	int i = 0;
	imageclass img = imageclass.getInstance();
	String[] bufferarray = new String[2000];
	String xml=null;
	static ArrayList<String> zoom = new ArrayList<String>();
	Cursor actualimagecursor;
	
	String blink=null;
	String bimage=null;
//	String burl="http://50.57.227.117/blacksheep/mobile/partypicBanner.php";
	 Bitmap bm=null;
	 String bs_api = "7PZR2KPVXCFF6FMKJMW7";
		long milliseconds= 10;
		int last_position;
		String get_last_pos="";
	//	 GoogleAnalyticsTracker tracker;
		@Override
		public void onStart()
		{
			 super.onStart();
			 FlurryAgent.setContinueSessionMillis(milliseconds);
			FlurryAgent.onStartSession(BlackSheepimage.this, bs_api);
			FlurryAgent.onPageView();
			FlurryAgent.onEvent("Party pics Started");
			
			EasyTracker.getInstance().setContext(this);
			EasyTracker.getInstance().activityStart(this);


			
//			tracker = GoogleAnalyticsTracker.getInstance();
//
//		    // Start the tracker in manual dispatch mode...
//		   // tracker.startNewSession("UA-36798680-1", this);
//		    tracker.startNewSession("UA-36318648-1",this);
//		    tracker.trackPageView("/BlackSheepimage");
			/*Context mCtx = this; // Get current context.
			GoogleAnalytics myInstance = GoogleAnalytics.getInstance(mCtx.getApplicationContext());
			myInstance.setDebug(true);
			Tracker myNewTracker = myInstance.getTracker("UA-36318648-1");
			myInstance.setDefaultTracker(myNewTracker);
			myNewTracker.trackView("Party pics Started");*/
		}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		String str_ScreenSize = dm.widthPixels + " x " + dm.heightPixels;
		str_ScreenSize = "dd" + " x " + dm.heightPixels;
			setContentView(R.layout.picture_main);
			
			/* Enabling strict mode */
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
        
	   
		lay2_gv = (GridView) findViewById(R.id.jr_lookbook_grid);
	 tv = (TextView) findViewById(R.id.txt_party_pic);
		
		Typeface type1 = Typeface
				.createFromAsset(getAssets(), "Cubano-Regular.otf");
		tv.setTypeface(type1);
		prev = (Button) findViewById(R.id.click);
		prev.setOnClickListener(this);
		but_next = (Button) findViewById(R.id.next);
		but_next.setVisibility(View.GONE);
		web_lay = (LinearLayout) findViewById(R.id.LinearLayout02);
		web_lay.setVisibility(View.GONE);
		 submit=(Button)findViewById(R.id.submitgrid);
		submit.setVisibility(View.VISIBLE);

		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				// photoPickerIntent.setType("image/*");
				// startActivityForResult(photoPickerIntent, 1);
				new Task_displaysdcard().execute();

			}

		});
		//commented
//		url = "http://104.130.66.20/mobile_new/partypics.php?page=";
		url=ParserClass.BlackSheepUrlOne;
		try {
			new Task_xmlparser().execute();
		} catch (Exception e) {

		}
		lay2_gv.setOnItemClickListener(this);

		zoombufferarray = new String[img.zoomimage.size()];
	}

	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		img.clear();
		if (Environment.getExternalStorageState().equals("mounted")) {
			File file = new File(Environment.getExternalStorageDirectory(),
					"tempBSPhoto.PNG");
		}
		if(bm!=null)
		{
		bm.recycle();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		 FlurryAgent.onEndSession(BlackSheepimage.this);
		 EasyTracker.getInstance().activityStop(this);	
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (0): {
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					Bundle b = data.getExtras();
					String s = b.getString("name");
					pageno = b.getInt("pageno");
                    int imgid=b.getInt("imgid");

					img.print_thumbimage_array();
					img.print_zoomimage_array();
					zoombufferarray = img.ary_zoomimage();
					zoombufferarray1 = img.thumbimage_arrray;
					g_adpthumb = new GridAdapter_thumb(this, zoombufferarray1);
			
					lay2_gv.setAdapter(g_adpthumb);
					lay2_gv.setSelection(imgid);

				}
				break;

			}
		}
		case 1: {
			if (resultCode == RESULT_OK) {
				Uri photoUri = data.getData();

				if (photoUri != null) {
					try {
						String[] filePathColumn = {MediaStore.Images.Media.DATA};
			            Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
			            cursor.moveToFirst();
			            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			            String filePath = cursor.getString(columnIndex);
			            cursor.close();
    					Uri U = Uri.parse("file://" + filePath);
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {"mPics@theblacksheeponline.com"});
						emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Picture From My Android Mobile!");
						//emailIntent.putExtra(android.content.Intent.EXTRA_CC,"Picture from my Android phone!");
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,Html.fromHtml("<br><br>")+"Sent From My Android Mobile");
						emailIntent.setType("image/png");
						emailIntent.putExtra(Intent.EXTRA_STREAM, U);
						emailIntent.setType("vnd.android.cursor.dir/email");
						startActivity(Intent.createChooser(emailIntent,"Email:"));

					} catch (Exception e) {
						//e.printStackTrace();
						Toast.makeText(BlackSheepimage.this,e.toString(),Toast.LENGTH_LONG).show();
					}
				}
				break;
			}

		}
		}
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		pageno++;
		img.startup = 0;
		img.but_starup = 1;
		//commented
//		url = "http://104.130.66.20/mobile_new/partypics.php?page=" + pageno;
		url=ParserClass.BlackSheepUrlTwo+""+pageno;
        if(black_netcheck==true)
        {
        	displayAlert();
        }
        else
        {
        	
        get_last_pos="load_more";
        	
         last_position=lay2_gv.getCount();
		new Task_xmlparser().execute();
        
		g_adp.notifyDataSetChanged();
        }

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if(black_netcheck == true)
		{
			 displayAlert(); 
		}
		else
		{
		try {
			img_intent = new Intent(this, image_view.class);
			img_intent.putExtra("id", arg2);
            img_intent.putExtra("xml", xml);
			img_intent.putExtra("pageno", pageno);
			img_intent.putExtra("tonoimage",tonoimage);
			img_intent.putExtra("banner",bm);
			img_intent.putExtra("bnrlink", blink);
			img_intent.putExtra("zoomurl", zoombufferarray);
			startActivityForResult(img_intent, 0);
		} catch (Exception e) {
			Log.v("error", " " + e);
		}

		new Task_News_ArticleView().execute();
		}
	}

	private class Task_xmlparser extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				BlackSheepimage.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				g_adp = new GridAdapter(BlackSheepimage.this, url);
				zoombufferarray = new ZoomGridAdapter().getZoomGridAdapter(
						BlackSheepimage.this, url);
				
			} catch (Exception e) {
				Log.v("DO_TAG", "error " + e);
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			if(close==0)
				
			lay2_gv.setAdapter(g_adp);
			if(get_last_pos.equals("load_more"))
			{
				lay2_gv.setSelection(last_position);
			}
			else
			{
				lay2_gv.setSelection(1);
			}
			
			g_adp.notifyDataSetChanged();
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	private class Tump_xmlparser extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				BlackSheepimage.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();

		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {

				g_adpthumb = new GridAdapter_thumb(BlackSheepimage.this,
						zoombufferarray1);

			} catch (Exception e) {
				Log.v("DO_TAG", "error " + e);
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			try {
				lay2_gv.setAdapter(g_adpthumb);
				g_adp.notifyDataSetChanged();
			} catch (Exception e) {
				Log.v("ERROR", e.toString());
			}
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	private class Task_News_ArticleView extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				BlackSheepimage.this);
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

					mProgressStatus++;

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

	private class Task_displaysdcard extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				BlackSheepimage.this);
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
	public  boolean netCheck()
    {
   	 ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

	    if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED 
		    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED ) {
		    //notify user you are not online
	    	black_netcheck =true;
		} 
	   return black_netcheck;
    }
    public  void displayAlert()
    {
   	 new AlertDialog.Builder(this).setMessage("Please Check Your Internet Connection and Try Again")  
	       .setTitle("Network Error")  
	       .setCancelable(true)  
	       .setNeutralButton(android.R.string.cancel,  
	          new DialogInterface.OnClickListener() {  
	          public void onClick(DialogInterface dialog, int whichButton){
	        	  finish();
	          }  
	          })  
	       .show(); 
    }


}
