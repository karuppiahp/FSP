package com.fsp.blacksheep;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class Articles extends Activity implements ImageGetter 
{
	RelativeLayout rel,detail_rel,share_Relative, layForImageAvail;
	Button article_Backbtn,article_Sharebtn,email_Btn,facebook_Btn,twitter_Btn,cancel_Btn;
	TextView desc_Text,article_Header, articleTitle, articleDateAuthor, textForNoImage;
	int s,density;
	ListView listViewArticles;
	ArrayList<HashMap<String, String>> articlesArrayList = new ArrayList<HashMap<String,String>>();
	String url = ParserClass.articlesList;
	String school, shortName;
	SharedPreferences schoolDetails;
	public static final String PREFS_NAME = "MyPrefsFile";
	private String image, author, description;
	private ImageView imgForArticleDetail;
	private ImageLoader imageLoader;
	DisplayImageOptions  options;
	private ImageLoaderConfiguration config;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.articles_layout);
		
		rel=(RelativeLayout)findViewById(R.id.articlerelative);
		detail_rel=(RelativeLayout)findViewById(R.id.detailRelative);
		article_Backbtn=(Button)findViewById(R.id.articlebackbtn);		
		desc_Text=(TextView)findViewById(R.id.textdesc);
		article_Header=(TextView)findViewById(R.id.articleheader);
		imgForArticleDetail = (ImageView) findViewById(R.id.mainimage);
		articleTitle = (TextView) findViewById(R.id.articletitle1);
		articleDateAuthor = (TextView) findViewById(R.id.articletitle);
		layForImageAvail = (RelativeLayout) findViewById(R.id.layForImageAvail);
		share_Relative=(RelativeLayout)findViewById(R.id.socialshareRelative);
		article_Sharebtn=(Button)findViewById(R.id.shareText);
		textForNoImage = (TextView) findViewById(R.id.articletitleForNoImage);
		
		email_Btn=(Button)findViewById(R.id.emailbtn);
		facebook_Btn=(Button)findViewById(R.id.facebookbtn);
		twitter_Btn=(Button)findViewById(R.id.twitterbtn);
		cancel_Btn=(Button)findViewById(R.id.cancelbtn);
		
		listViewArticles = (ListView) findViewById(R.id.listForArticles);
		
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.icon)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
		imageLoader = ImageLoader.getInstance();
		config = new ImageLoaderConfiguration.Builder(this)
		.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .memoryCacheSizePercentage(13) // default
        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .imageDownloader(new BaseImageDownloader(this)) // default
        .imageDecoder(new BaseImageDecoder(false)) // default
        .defaultDisplayImageOptions(options) // default
        .writeDebugLogs()
		.build();
		imageLoader.init(config);
		
		schoolDetails = getSharedPreferences(PREFS_NAME, 1);
		school = schoolDetails.getString("school_name", "");
		Log.i("School is?????",""+school);
		if(school.contains(" ")) {
			school = school.replace(" ", "%20");
		}
		
		Typeface type1 = Typeface
				.createFromAsset(getAssets(), "Mission Gothic Regular.otf");
		Typeface type = Typeface
				.createFromAsset(getAssets(), "Mission Gothic Bold.otf");
		desc_Text.setTypeface(type1);
		article_Header.setTypeface(type);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.densityDpi;
		Log.e("Density Value",""+density);
		
		
		listViewArticles.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				detail_rel.setVisibility(View.VISIBLE);
				rel.setVisibility(View.GONE);
				article_Backbtn.setVisibility(View.VISIBLE);
				String articlId = articlesArrayList.get(arg2).get("id");
				Log.e("articlId?????",""+articlId);
				Log.e("articl image URL?????",""+articlesArrayList.get(arg2).get("image"));
				shortName = articlesArrayList.get(arg2).get("shortName");
				
				desc_Text.setText("");
				if(articlesArrayList.get(arg2).get("image").length() > 0) {
					layForImageAvail.setVisibility(View.VISIBLE);
					textForNoImage.setVisibility(View.GONE);
					articleDateAuthor.setText("");
					imgForArticleDetail.setImageResource(R.drawable.articlebg);
					imageLoader.displayImage(articlesArrayList.get(arg2).get("image"), imgForArticleDetail);
					articleTitle.setText("               " + articlesArrayList.get(arg2).get("title"));
				} else {
					layForImageAvail.setVisibility(View.GONE);
					textForNoImage.setVisibility(View.VISIBLE);
					textForNoImage.setText(articlesArrayList.get(arg2).get("title"));
				}
				
				new ArticleDetailedTask(Articles.this, desc_Text, articleDateAuthor, school, articlId, Articles.this, density).execute();
				
			}
		});
		
		
		article_Backbtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				detail_rel.setVisibility(View.GONE);
				rel.setVisibility(View.VISIBLE);
				article_Backbtn.setVisibility(View.GONE);				
			}
		});
		
		article_Sharebtn.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				Utils.ARTICLES_SHARE_VISIBILITY = true;
				share_Relative.setVisibility(View.VISIBLE);				
			}
		});
		
		email_Btn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
			//	Toast.makeText(Articles.this,"Email-Will Update soon.",Toast.LENGTH_SHORT).show();
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				  shareIntent.setType("text/plain");
			//	  shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "BlackSheep");
				  shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.theblacksheeponline.com/" + school + "/" + shortName);
				  //  shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

				   PackageManager pm = v.getContext().getPackageManager();
				   List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
				       for (final ResolveInfo app : activityList) 
				        {
				          if ((app.activityInfo.name).contains("android.gm")) 
				           {
				             final ActivityInfo activity = app.activityInfo;
				             final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
				            shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				             shareIntent.setComponent(name);
				             v.getContext().startActivity(shareIntent);
				             break;
				           }
				       }
			}
		});
		
		facebook_Btn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				   shareIntent.setType("text/plain");
				   shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.theblacksheeponline.com/" + school + "/" + shortName);

				   PackageManager pm = v.getContext().getPackageManager();
				   List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
				     for (final ResolveInfo app : activityList) 
				     {
				         if ((app.activityInfo.name).contains("facebook")) 
				         {
				           final ActivityInfo activity = app.activityInfo;
				           final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
				          shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				          shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				          shareIntent.setComponent(name);
				          v.getContext().startActivity(shareIntent);
				          break;
				        }
				      }
			}
		});
		
		twitter_Btn.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				   shareIntent.setType("text/plain");
				   shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.theblacksheeponline.com/" + school + "/" + shortName);

				   PackageManager pm = v.getContext().getPackageManager();
				   List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
				     for (final ResolveInfo app : activityList) 
				      {
				         String packageName = app.activityInfo.packageName;
				        if (packageName.contains("twitter"))
				          {
				             final ActivityInfo activity = app.activityInfo;
				             final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
				             shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				             shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				             shareIntent.setComponent(name);
				             v.getContext().startActivity(shareIntent);
				            break;
				          }
				        }
			}
		});
		
		cancel_Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.ARTICLES_SHARE_VISIBILITY = false;
				share_Relative.setVisibility(View.GONE);				
			}
		});
		
		share_Relative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.e("Social Relative","Clicked");					
			}
		});
	}
	
	private ArrayList<HashMap<String, String>> parse_articles_List() throws UnknownHostException {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> articlesArray = new ArrayList<HashMap<String,String>>();
		String res = Parsing_JSON.readFeed(url + "" + school);
		Log.e("The", "Articles response is=>>" + res);
		JSONObject job1;
		try {
			job1 = new JSONObject(res);
			String reg = job1.getString("data");
			JSONArray jarr1 = new JSONArray(reg);
			for (int i = 0; i < jarr1.length(); i++) {

				JSONObject inner_obj = jarr1.getJSONObject(i);

				String school = inner_obj.getString("school");
				String title = inner_obj.getString("title");
				String id = inner_obj.getString("article_id");
				String image = inner_obj.getString("image");
				String shortName = inner_obj.getString("article_shortname");
				String author = inner_obj.getString("author");
				String description = inner_obj.getString("description");
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("school", school);
				hashMap.put("title", title);
				hashMap.put("id", id);
				hashMap.put("image", image);
				hashMap.put("shortName", shortName);
				hashMap.put("author", author);
				hashMap.put("description", description);
				articlesArray.add(hashMap);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return articlesArray;
	}

	public Drawable getDrawable(String source) {
		// TODO Auto-generated method stub
		LevelListDrawable d = new LevelListDrawable();
	    Drawable empty = getResources().getDrawable(R.drawable.articlebg);
	    d.addLevel(0, 0, empty);
	    d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

	    new LoadImage().execute(source, d);

	    return d;
	}
	
	class LoadImage extends AsyncTask<Object, Void, Bitmap> {

	    private LevelListDrawable mDrawable;

	    @Override
	    protected Bitmap doInBackground(Object... params) {
	        String source = (String) params[0];
	        mDrawable = (LevelListDrawable) params[1];
	        Log.d("img bg", "doInBackground " + source);
	        try {
	            InputStream is = new URL(source).openStream();
	            return BitmapFactory.decodeStream(is);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        Log.d("img post execute", "onPostExecute drawable " + mDrawable);
	        Log.d("img post execute", "onPostExecute bitmap " + bitmap);
	        if (bitmap != null) {
	            BitmapDrawable d = new BitmapDrawable(bitmap);
	            mDrawable.addLevel(1, 1, d);
	            mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
	            mDrawable.setLevel(1);
	            // i don't know yet a better way to refresh TextView
	            // mTv.invalidate() doesn't work as expected
	            CharSequence t = desc_Text.getText();
	            desc_Text.setText(t);
	        }
	    }
	}
	
	@Override
	public void onResume(){
	    super.onResume();
	    Log.e("Inside OnResume","Method");
	    
	    if(Utils.ARTICLES_SHARE_VISIBILITY == false) {
		    schoolDetails = getSharedPreferences(PREFS_NAME, 1);
			school = schoolDetails.getString("school_name", "");
			Log.e("School is?????",""+school);
			if(school.contains(" ")) {
				school = school.replace(" ", "%20");
			}
			Log.e("School is?????",""+school);
			
			detail_rel.setVisibility(View.GONE);
			rel.setVisibility(View.VISIBLE);
			article_Backbtn.setVisibility(View.GONE);
			share_Relative.setVisibility(View.GONE);
			articlesArrayList.clear();
			new ArticlesListTask(this, articlesArrayList).execute();
		    
	    }
	}
	
	
	public class ArticlesListTask extends AsyncTask<Void, Void, Void> {
		
		private final ProgressDialog dialog;
		ArrayList<HashMap<String, String>> articlesArray = new ArrayList<HashMap<String,String>>();
		String loopEnters = "";
		
		public ArticlesListTask(Context context, ArrayList<HashMap<String, String>> articlesArrayList) {
			dialog = new ProgressDialog(context);
			articlesArray = articlesArrayList;
		}

		protected void onPreExecute() {
			this.dialog.setMessage("Loading...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			schoolDetails = getSharedPreferences(PREFS_NAME, 1);
			school = schoolDetails.getString("school_name", "");
			Log.e("School is?????",""+school);
			if(school.contains(" ")) {
				school = school.replace(" ", "%20");
			}
			Log.e("School is?????",""+school);
			String res = Parsing_JSON.readFeed(url + "" + school);
			Log.e("The", "Articles detail response is=>>" + res);
			JSONObject job1;
			try {
				articlesArray.clear();
				job1 = new JSONObject(res);
				String reg = job1.getString("data");
				loopEnters = "array";
				JSONArray jarr1 = new JSONArray(reg);
				for (int i = 0; i < jarr1.length(); i++) {

					JSONObject inner_obj = jarr1.getJSONObject(i);

					String school = inner_obj.getString("school");
					String title = inner_obj.getString("title");
					String id = inner_obj.getString("article_id");
					String image = inner_obj.getString("image");
					String shortName = inner_obj.getString("article_shortname");
					String author = inner_obj.getString("author");
					String description = inner_obj.getString("description");
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("school", school);
					hashMap.put("title", title);
					hashMap.put("id", id);
					hashMap.put("image", image);
					hashMap.put("shortName", shortName);
					hashMap.put("author", author);
					hashMap.put("description", description);
					articlesArray.add(hashMap);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				loopEnters = "exception";
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			if(articlesArray.size() > 0) {
				listViewArticles.setAdapter(new ArticleListAdapter(Articles.this, articlesArray, imageLoader));
			} else {
				listViewArticles.setAdapter(new ArticleListAdapter(Articles.this, articlesArray, imageLoader));
				if(loopEnters.equals("array")) {
					Toast.makeText(Articles.this, "Articles list is empty", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Articles.this, "Please check your netwrok availability", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
