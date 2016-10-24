package com.fsp.blacksheep;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.flurry.android.FlurryAgent;
import com.fsp.blacksheep.R;
import com.fsp.blacksheep.Articles.LoadImage;
import com.fsp.blacksheep.Registration.Task_Update;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.Spanned;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkGames extends Activity implements ImageGetter {
	Button listBackbtn, webBackbtn, drinks_Sharebtn, drinksemail_Btn,
			drinksfacebook_Btn, drinkstwitter_Btn, drinkscancel_Btn;
	TextView game_4, game_5, view_all, drinks_Desc;
	RelativeLayout rel1, rel2, rel3, socialshare_Rel;
	ListView games_list;
	String game_url4, post, title, title1;
	URL game4_Url;
	ArrayList<String> drink_game_det = new ArrayList<String>();
	ArrayList<String> drink_game_names = new ArrayList<String>();
	ArrayList<String> drink_game_id = new ArrayList<String>();
	ArrayList<String> drink_short_name = new ArrayList<String>();
	GamesAdapter game_adapter;
	TextView game_title, list_headerText, web_headerText, drinkTitle,
			drinksImg, txtForTitleNoImage;
	ImageView imgForDetail;
	String clicked_game, shortName, school;
	SharedPreferences schoolDetails;
	public static final String PREFS_NAME = "MyPrefsFile";
	String bs_api = "7PZR2KPVXCFF6FMKJMW7";
	long milliseconds = 10;
	ProgressDialog dialog;
	private ImageLoader imageLoader;
	DisplayImageOptions options;
	RelativeLayout layForImageAvail;

	// GoogleAnalytics myInstance;
	// Tracker myNewTracker;

	public void onStart() {
		super.onStart();
		FlurryAgent.setContinueSessionMillis(milliseconds);
		FlurryAgent.onStartSession(DrinkGames.this, bs_api);
		FlurryAgent.onPageView();
		FlurryAgent.onEvent("DrinkGames Started");
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);
		// tracker = GoogleAnalyticsTracker.getInstance();
		//
		// // Start the tracker in manual dispatch mode...
		// // tracker.startNewSession("UA-36798680-1", this);
		// tracker.startNewSession("UA-36318648-1", this);
		// tracker.trackPageView("/Rss_Feed_Grid");
		/*
		 * Context mCtx = this; // Get current context. myInstance =
		 * GoogleAnalytics.getInstance(mCtx.getApplicationContext());
		 * myInstance.setDebug(true); myNewTracker =
		 * myInstance.getTracker("UA-36318648-1");
		 * myInstance.setDefaultTracker(myNewTracker);
		 * myNewTracker.trackView("DrinkGames Started");
		 */

		Log.e("onStart Method", "666666666666666666");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("onResume method", "999999999999");
		if (rel2.getVisibility() == View.VISIBLE) {
			Log.e("Inside IF", "Statement");
		} else if (rel3.getVisibility() == View.VISIBLE) {
			Log.e("Inside IF-Else", "Statement");
		}

		if (Utils.DRINKING_GAMES_SHARE_VISIBILITY == false) {
			// -----------Karuppiah changes start------------
			game_4.setVisibility(View.GONE);
			game_5.setVisibility(View.GONE);
			view_all.setVisibility(View.GONE);
			socialshare_Rel.setVisibility(View.GONE);

			listBackbtn.setVisibility(View.GONE);

			rel1.setVisibility(View.GONE);
			rel2.setVisibility(View.VISIBLE);
			rel3.setVisibility(View.GONE);
			game_url4 = ParserClass.drinksGamesAll;
			if (haveInternet(DrinkGames.this) == true) {
				new GamesClass(game_url4).execute();
			} else {
				Toast.makeText(DrinkGames.this,
						"Please Check your Intenet Connection",
						Toast.LENGTH_SHORT).show();
			}
			// ----------karuppiah changes ends------------
		}
	}

	public void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(DrinkGames.this);
		EasyTracker.getInstance().activityStop(this);
		Log.e("onStop Method", "aaaaaaaaaaaaaaa");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.drink_games);

		try {
			/* Enabling strict mode */
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

			Log.e("Oncreate method", "jjjjjjjjjjjjjjjjjj");
			drinks_Desc = (TextView) findViewById(R.id.drinksdesc);
			game_4 = (TextView) findViewById(R.id.img_games_4);
			game_5 = (TextView) findViewById(R.id.img_games_5);
			view_all = (TextView) findViewById(R.id.img_view_all);
			rel1 = (RelativeLayout) findViewById(R.id.rel_gametypes);
			rel2 = (RelativeLayout) findViewById(R.id.rel_game_title);
			rel3 = (RelativeLayout) findViewById(R.id.rel_game_desc);

			socialshare_Rel = (RelativeLayout) findViewById(R.id.socialsharingRelative);
			drinks_Sharebtn = (Button) findViewById(R.id.sharedrinks);

			drinksemail_Btn = (Button) findViewById(R.id.emailBtn);
			drinksfacebook_Btn = (Button) findViewById(R.id.facebookBtn);
			drinkstwitter_Btn = (Button) findViewById(R.id.twitterBtn);
			drinkscancel_Btn = (Button) findViewById(R.id.cancelBtn);

			games_list = (ListView) findViewById(R.id.list_game4);
			web_headerText = (TextView) findViewById(R.id.webtitle_text);
			list_headerText = (TextView) findViewById(R.id.header_text1);
			listBackbtn = (Button) findViewById(R.id.backbtn);
			webBackbtn = (Button) findViewById(R.id.webbackbtn);
			drinkTitle = (TextView) findViewById(R.id.drinktitle);
			imgForDetail = (ImageView) findViewById(R.id.drinksimage);
			txtForTitleNoImage = (TextView) findViewById(R.id.drinktitleNoImage);
			layForImageAvail = (RelativeLayout) findViewById(R.id.layForImageAvail);
			dialog = new ProgressDialog(DrinkGames.this);
			Typeface type = Typeface.createFromAsset(getAssets(),
					"Mission Gothic Regular.otf");
			Typeface type1 = Typeface.createFromAsset(getAssets(),
					"Mission Gothic Bold.otf");
			game_4.setTypeface(type);
			game_5.setTypeface(type);
			view_all.setTypeface(type);
			web_headerText.setTypeface(type1);
			list_headerText.setTypeface(type1);
			drinks_Desc.setTypeface(type);

			// -----------Karuppiah changes start------------
			game_4.setVisibility(View.GONE);
			game_5.setVisibility(View.GONE);
			view_all.setVisibility(View.GONE);

			listBackbtn.setVisibility(View.GONE);

			rel1.setVisibility(View.GONE);
			rel2.setVisibility(View.VISIBLE);

			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(DrinkGames.this));
			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.icon).cacheInMemory(true)
					.cacheOnDisc(true).build();

			schoolDetails = getSharedPreferences(PREFS_NAME, 1);
			school = schoolDetails.getString("school_name", "");
			Log.i("School is?????", "" + school);
			if (school.contains(" ")) {
				school = school.replace(" ", "%20");
			}

			game_url4 = ParserClass.drinksGamesAll;
			if (haveInternet(DrinkGames.this) == true) {
				new GamesClass(game_url4).execute();
			} else {
				Toast.makeText(DrinkGames.this,
						"Please Check your Intenet Connection",
						Toast.LENGTH_SHORT).show();
			}
			// ----------karuppiah changes ends------------

			drinks_Sharebtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Utils.DRINKING_GAMES_SHARE_VISIBILITY = true;
					socialshare_Rel.setVisibility(View.VISIBLE);
				}
			});

			drinksemail_Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent shareIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							"http://www.theblacksheeponline.com/" + school
									+ "/" + shortName);

					PackageManager pm = v.getContext().getPackageManager();
					List<ResolveInfo> activityList = pm.queryIntentActivities(
							shareIntent, 0);
					for (final ResolveInfo app : activityList) {
						if ((app.activityInfo.name).contains("android.gm")) {
							final ActivityInfo activity = app.activityInfo;
							final ComponentName name = new ComponentName(
									activity.applicationInfo.packageName,
									activity.name);
							shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
							shareIntent
									.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							shareIntent.setComponent(name);
							v.getContext().startActivity(shareIntent);
							break;
						}
					}
				}
			});

			drinksfacebook_Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent shareIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							"http://www.theblacksheeponline.com/" + school
									+ "/" + shortName);

					PackageManager pm = v.getContext().getPackageManager();
					List<ResolveInfo> activityList = pm.queryIntentActivities(
							shareIntent, 0);
					for (final ResolveInfo app : activityList) {
						if ((app.activityInfo.name).contains("facebook")) {
							final ActivityInfo activity = app.activityInfo;
							final ComponentName name = new ComponentName(
									activity.applicationInfo.packageName,
									activity.name);
							shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
							shareIntent
									.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							shareIntent.setComponent(name);
							v.getContext().startActivity(shareIntent);
							break;
						}
					}
				}
			});

			drinkstwitter_Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent shareIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					shareIntent.setType("text/plain");
					shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							"http://www.theblacksheeponline.com/" + school
									+ "/" + shortName);

					PackageManager pm = v.getContext().getPackageManager();
					List<ResolveInfo> activityList = pm.queryIntentActivities(
							shareIntent, 0);
					for (final ResolveInfo app : activityList) {
						String packageName = app.activityInfo.packageName;
						if (packageName.contains("twitter")) {
							final ActivityInfo activity = app.activityInfo;
							final ComponentName name = new ComponentName(
									activity.applicationInfo.packageName,
									activity.name);
							shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
							shareIntent
									.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
							shareIntent.setComponent(name);
							v.getContext().startActivity(shareIntent);
							break;
						}
					}
				}
			});

			drinkscancel_Btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Utils.DRINKING_GAMES_SHARE_VISIBILITY = false;
					socialshare_Rel.setVisibility(View.GONE);
				}
			});

			socialshare_Rel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Log.e("Social Relative", "Clicked");
				}
			});

			games_list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v, int pos,
						long arg3) {
					// TODO Auto-generated method stub
					rel2.setVisibility(View.GONE);
					rel1.setVisibility(View.GONE);
					rel3.setVisibility(View.VISIBLE);
					shortName = drink_short_name.get(pos);
					new GamesDetail(drink_game_id.get(pos), drink_game_det
							.get(pos)).execute();
				}
			});

			game_4.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (haveInternet(DrinkGames.this) == true) {
						// TODO Auto-generated method stub
						rel1.setVisibility(View.GONE);
						rel2.setVisibility(View.VISIBLE);
						// changed
						game_url4 = ParserClass.gameUrlOne;
						new GamesClass(game_url4).execute();
					} else {
						Toast.makeText(DrinkGames.this,
								"Please Check your Intenet Connection",
								Toast.LENGTH_SHORT).show();
					}
				}

			});
			game_5.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (haveInternet(DrinkGames.this) == true) {
						// TODO Auto-generated method stub
						rel1.setVisibility(View.GONE);
						rel2.setVisibility(View.VISIBLE);
						// changed
						game_url4 = ParserClass.gameUrlTwo;
						new GamesClass(game_url4).execute();
					} else {
						Toast.makeText(DrinkGames.this,
								"Please Check your Intenet Connection",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			view_all.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (haveInternet(DrinkGames.this) == true) {
						rel1.setVisibility(View.GONE);
						rel2.setVisibility(View.VISIBLE);
						// changed
						game_url4 = ParserClass.drinksGamesAll;

						new GamesClass(game_url4).execute();
					} else {
						Toast.makeText(DrinkGames.this,
								"Please Check your Intenet Connection",
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			webBackbtn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					rel3.setVisibility(View.GONE);
					rel2.setVisibility(View.VISIBLE);
					rel1.setVisibility(View.GONE);
				}
			});

			listBackbtn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					rel3.setVisibility(View.GONE);
					rel2.setVisibility(View.GONE);
					rel1.setVisibility(View.VISIBLE);
				}
			});
		} catch (Exception e) {
			Log.e("Inside Drink Games", "" + e);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public class GamesClass extends AsyncTask<Void, Void, Void> {
		String newURL, res;

		public GamesClass(String game_url4) {
			newURL = game_url4;
		}

		protected void onPreExecute() {
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
			drink_game_names.clear();
			drink_game_det.clear();
			drink_game_id.clear();
			game_adapter = new GamesAdapter(DrinkGames.this, drink_game_det);
			games_list.setAdapter(null);
			game_adapter.notifyDataSetChanged();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			res = Parsing_JSON.readFeed(newURL);
			Log.e("Response Games Drinking", res);
			return null;
		}

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(Void result) {
			Log.e("Response", "Post Execute");
			try {
				JSONObject job1 = new JSONObject(res);
				if (job1 != null) {
					JSONArray inn_arr = job1.getJSONArray("data");
					for (int i = 0; i < inn_arr.length(); i++) {
						JSONObject inn_obj1 = inn_arr.getJSONObject(i);
						title = inn_obj1.getString("title");
						if (title.contains("Drinking Game")) {
							title1 = title.replace("Drinking Game:", "");
						} else {
							title1 = title;
						}
						String id = inn_obj1.getString("article_id");
						String short_name = inn_obj1
								.getString("article_shortname");
						drink_game_det.add(title1);
						drink_game_id.add(id);
						drink_short_name.add(short_name);
					}

					games_list.setAdapter(null);
					game_adapter = new GamesAdapter(DrinkGames.this,
							drink_game_det);
					games_list.setAdapter(game_adapter);
					game_adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(DrinkGames.this,
							"Please Check your Intenet Connection",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	}

	public class GamesDetail extends AsyncTask<Void, Void, Void> {
		String res, url = ParserClass.drinksGamesDetail, articleId, image = "",
				desc = "", title;

		public GamesDetail(String articleId, String title) {
			this.articleId = articleId;
			this.title = title;
		}

		protected void onPreExecute() {
			dialog.setMessage("Loading...");
			dialog.setCancelable(false);
			dialog.show();
			txtForTitleNoImage.setText("");
			drinkTitle.setText("");
			drinks_Desc.setText("");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			res = Parsing_JSON.readFeed(url + articleId);
			try {
				JSONObject job1 = new JSONObject(res);
				if (job1 != null) {
					JSONObject jObj = job1.getJSONObject("data");
					image = jObj.getString("image");
					desc = jObj.getString("article_content");
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (image.length() > 0) {
				layForImageAvail.setVisibility(View.VISIBLE);
				txtForTitleNoImage.setVisibility(View.GONE);
				imgForDetail.setImageResource(R.drawable.articlebg);
				imageLoader.displayImage(image, imgForDetail);
				drinkTitle.setText(title);
			} else {
				layForImageAvail.setVisibility(View.GONE);
				txtForTitleNoImage.setVisibility(View.VISIBLE);
				txtForTitleNoImage.setText(title);
			}
			if (desc.length() > 0) {
				drinks_Desc.setText(Html.fromHtml(desc, DrinkGames.this, null));
			}
		}

	}

	public class GamesAdapter extends BaseAdapter {
		private Activity activity;
		public View pressedView;

		List<String> data = new ArrayList<String>();
		private LayoutInflater inflater = null;

		public GamesAdapter(Activity a, List<String> school_name_List) {
			// TODO Auto-generated constructor stub
			activity = a;
			data = school_name_List;

			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View vi = convertView;

			vi = inflater.inflate(R.layout.game_list, null);
			TextView info = (TextView) vi.findViewById(R.id.game_text);
			Typeface type1 = Typeface.createFromAsset(getAssets(),
					"Mission Gothic Regular.otf");
			info.setTypeface(type1);

			info.setText(data.get(position));
			return vi;
		}

	}

	public static boolean haveInternet(Context ctx) {
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
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
				mDrawable
						.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
				mDrawable.setLevel(1);
				// i don't know yet a better way to refresh TextView
				// mTv.invalidate() doesn't work as expected
				CharSequence t = drinks_Desc.getText();
				drinks_Desc.setText(t);
			}
		}
	}

}
