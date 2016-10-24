package com.eliqxir.tabhostfragments;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;

import com.eliqxir.CartActivity;
import com.eliqxir.OrderActivity;
import com.eliqxir.R;
import com.eliqxir.adapter.DBAdapter;
import com.eliqxir.driver.LoginDriverActivity;
import com.eliqxir.driver.OrdersActivity;
import com.eliqxir.slidermenu.SlidingMenuActivity;
import com.eliqxir.utils.Constant;
import com.eliqxir.utils.SessionStore;
import com.eliqxir.utils.Utils;
import com.eliqxir.vendor.AddItemActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TabsFragmentActivity extends SlidingMenuActivity implements
		TabHost.OnTabChangeListener, OnClickListener {

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabsFragmentActivity.TabInfo>();
	private TabInfo mLastTab = null;
	ImageView wineTab, featuredTab, beerTab, extrasTab, liquorTab, img;

	ImageButton btnSliderMenu, backBtn, cartBtn, addItemBtn;
	TextView textForHeader, cartCountTxt;
	RelativeLayout layCartCount;
	SharedPreferences sharedpreferences,customerPreference,storeclosedPreference;
	Editor editor;
	TabWidget tabWidget;
	RelativeLayout layHeaderBack, layHeaderCart, layHeaderSlider,
			layHeaderaddItem;
	String menuPref, add,delivery_status,store_status;
	LocationManager lm;
	AlertDialog alert;
	AlertDialog.Builder builder;
	boolean isOnline;
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(Constant.isVendorAvailable.equals("notAvailable"))
			// {
			// // startActivity(new Intent(TabsFragmentActivity.this,
			// // TabsFragmentActivity.class));
			// }
			// else
			// {
			// finish();
			// }

		}
		


		return false;
	}

	/**
	 * 
	 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	/**
	 * 
	 *
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	public void onResume() {
		super.onResume();		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.setCurrentTab(Constant.selectedTabPosition);
		Log.e("onResume", "onResume");
		Log.e("Constant.cartArray.size()", "" + Constant.cartArray.size());
		updateCart();
	}

	private void updateCart() {
		try {
			if (Constant.cartArray.size() > 0) {
				cartCountTxt.setText(Constant.cartArray.size() + "");
				cartCountTxt.setVisibility(View.VISIBLE);
				layCartCount.setVisibility(View.VISIBLE);
			} else {
				cartCountTxt.setText("");
				cartCountTxt.setVisibility(View.GONE);
				layCartCount.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.trackError(getApplicationContext());
		setContentView(R.layout.activity_main);
		img = (ImageView) findViewById(R.id.imgSplash);

		layHeaderCart = (RelativeLayout) findViewById(R.id.layHeaderCart);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();	
		 
		isOnline = Utils.isOnline();
		Log.e("isOnline", isOnline + "");
		if (isOnline) {
			img.setVisibility(View.GONE);
			backBtn.setVisibility(View.VISIBLE);
			layHeaderCart.setVisibility(View.VISIBLE);

			initialiseTabHost(savedInstanceState);

			if (savedInstanceState != null) {
				mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // set  the
			}

		} else {
			backBtn.setVisibility(View.GONE);
			layHeaderCart.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
			/*Utils.ShowAlert(TabsFragmentActivity.this,
					"Network may be disconnected (or) too slow. Check your Network Settings.");*/
			// finish();
			builder = new AlertDialog.Builder(this);
			builder.setMessage("Network may be disconnected (or) too slow. Check your Network Settings.")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   isOnline = Utils.isOnline();
			        	   Log.e("isOnline bbbbbbbb", isOnline + "");
			        	   if (isOnline) {
			        		   alert.dismiss();
			        		   startActivity(new Intent(TabsFragmentActivity.this,
										TabsFragmentActivity.class));
			        	   }else{
			        		   alert = builder.create();
			       			   alert.show();
			        	   }
			           }
			       });
			alert = builder.create();
			alert.show();
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	/**
	 * Initialise the Tab Host
	 */

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initialiseTabHost(Bundle args) {

		btnSliderMenu = (ImageButton) findViewById(R.id.btnSliderMenu);

		addItemBtn = (ImageButton) findViewById(R.id.addItemBtn);
		addItemBtn.setOnClickListener(this);
		cartBtn = (ImageButton) findViewById(R.id.cartBtn);
		textForHeader = (TextView) findViewById(R.id.textForHeader);
		btnSliderMenu.setVisibility(View.VISIBLE);
		backBtn.setVisibility(View.GONE);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		btnSliderMenu.setOnClickListener(this);
		backBtn.setOnClickListener(this);

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;

		layHeaderBack = (RelativeLayout) findViewById(R.id.layHeaderBack);

		layHeaderSlider = (RelativeLayout) findViewById(R.id.layHeaderslider);
		menuPref = SessionStore.getSlidinMenu(getApplicationContext());
		layHeaderaddItem = (RelativeLayout) findViewById(R.id.layHeaderaddItem);
		layHeaderBack.setOnClickListener(this);
		layHeaderCart.setOnClickListener(this);
		layHeaderSlider.setOnClickListener(this);
		layHeaderBack.setVisibility(View.GONE);
		cartCountTxt = (TextView) findViewById(R.id.cartCountTxt);
		// editTxtForQty = (EditText) findViewById(R.id.editTxtForQuantity);
		layCartCount = (RelativeLayout) findViewById(R.id.layCartCount);
		if (menuPref == null) {
			Log.e("Inside Menu Pref","Null");
			if (Constant.cartArray.size() > 0) {
				Log.e("Inside Menu Pref","Cart Size Greater than 0");
				cartBtn.setVisibility(View.VISIBLE);
				layCartCount.setVisibility(View.VISIBLE);
				layHeaderCart.setVisibility(View.VISIBLE);
				cartCountTxt.setText(Constant.cartArray.size() + "");
			} else {
				Log.e("Inside Menu Pref","Cart size less than zero");
				cartBtn.setVisibility(View.VISIBLE);
				layCartCount.setVisibility(View.GONE);
				layHeaderCart.setVisibility(View.VISIBLE);
			}
		} else if (menuPref.equals("vendor")) {
			Log.e("Inside Menu Pref","Vendor Login");
			cartBtn.setVisibility(View.GONE);
			layCartCount.setVisibility(View.GONE);
			layHeaderCart.setVisibility(View.GONE);
			addItemBtn.setVisibility(View.VISIBLE);
			layHeaderaddItem.setVisibility(View.VISIBLE);
		} else {
			Log.e("Inside Last","Else Part");
			/*if (Constant.cartArray.size() > 0) {
				cartBtn.setVisibility(View.VISIBLE);
				layCartCount.setVisibility(View.VISIBLE);
				layHeaderCart.setVisibility(View.VISIBLE);
				cartCountTxt.setText(Constant.cartArray.size() + "");
			} else {
				cartBtn.setVisibility(View.VISIBLE);
				layCartCount.setVisibility(View.GONE);
				layHeaderCart.setVisibility(View.VISIBLE);
			}*/
			Intent intentToOverview = new Intent(
					TabsFragmentActivity.this, OrdersActivity.class);
			startActivity(intentToOverview);
		}

		btnSliderMenu.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		cartBtn.setOnClickListener(this);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		View wineIndicator = getLayoutInflater().inflate(R.layout.tabindicator,
				null);
		wineTab = (ImageView) wineIndicator.findViewById(R.id.icon);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int density = metrics.densityDpi;

		if (Build.VERSION.SDK_INT >= 16) {

			wineTab.setBackgroundResource(R.drawable.wine_tab);

		} else {

			wineTab.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.wine_tab));
		}

		View featuredIndicator = getLayoutInflater().inflate(
				R.layout.tabindicator, null);
		featuredTab = (ImageView) featuredIndicator.findViewById(R.id.icon);
		if (Build.VERSION.SDK_INT >= 16) {

			featuredTab.setBackgroundResource(R.drawable.featured_tab);

		} else {

			featuredTab.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.featured_tab));
		}

		View beerIndicator = getLayoutInflater().inflate(R.layout.tabindicator,
				null);
		beerTab = (ImageView) beerIndicator.findViewById(R.id.icon);
		if (Build.VERSION.SDK_INT >= 16) {

			beerTab.setBackgroundResource(R.drawable.beer_tab);

		} else {

			beerTab.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.beer_tab));
		}

		View mixersIndicator = getLayoutInflater().inflate(
				R.layout.tabindicator, null);
		extrasTab = (ImageView) mixersIndicator.findViewById(R.id.icon);
		if (Build.VERSION.SDK_INT >= 16) {

			extrasTab.setBackgroundResource(R.drawable.mixers_tab);

		} else {

			extrasTab.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.mixers_tab));
		}

		View liquorIndicator = getLayoutInflater().inflate(
				R.layout.tabindicator, null);
		liquorTab = (ImageView) liquorIndicator.findViewById(R.id.icon);
		if (Build.VERSION.SDK_INT >= 16) {

			liquorTab.setBackgroundResource(R.drawable.liquor_tab);

		} else {

			liquorTab.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.liquor_tab));
		}

		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("featured").setIndicator(featuredIndicator),
				(tabInfo = new TabInfo("featured", FeaturedTab.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("wine").setIndicator(wineIndicator),
				(tabInfo = new TabInfo("wine", WineTab.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("beer").setIndicator(beerIndicator),
				(tabInfo = new TabInfo("beer", BeerTab.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("liquor").setIndicator(liquorIndicator),
				(tabInfo = new TabInfo("liquor", LiquorTab.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("extras").setIndicator(mixersIndicator),
				(tabInfo = new TabInfo("extras", ExtrasTab.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		this.onTabChanged("beer");

		mTabHost.getTabWidget().getChildAt(0)
				.setBackgroundResource(R.drawable.tab_gradient);
		mTabHost.getTabWidget().getChildAt(1)
				.setBackgroundResource(R.drawable.tab_gradient);
		mTabHost.getTabWidget().getChildAt(2)
				.setBackgroundResource(R.drawable.tab_gradient);
		mTabHost.getTabWidget().getChildAt(3)
				.setBackgroundResource(R.drawable.tab_gradient);
		mTabHost.getTabWidget().getChildAt(4)
				.setBackgroundResource(R.drawable.tab_gradient);

		mTabHost.setCurrentTab(2);
		mTabHost.setOnTabChangedListener(this);
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

			if (density == DisplayMetrics.DENSITY_HIGH) {

				mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 80;
				mTabHost.getTabWidget().getChildAt(i).setPadding(7, 7, 7, 0);

			} else if (density == DisplayMetrics.DENSITY_MEDIUM) {

				mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 60;
				mTabHost.getTabWidget().getChildAt(i).setPadding(5, 7, 5, 5);

			} else if (density == DisplayMetrics.DENSITY_LOW) {

				mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
				mTabHost.getTabWidget().getChildAt(i).setPadding(5, 5, 5, 5);

			} else if (density == DisplayMetrics.DENSITY_XHIGH) {

				mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 120;
				mTabHost.getTabWidget().getChildAt(i)
						.setPadding(10, 10, 10, 10);

			} else {
				for (int j = 0; j < mTabHost.getTabWidget().getChildCount(); j++) {
					mTabHost.getTabWidget().getChildAt(j)
							.setPadding(10, 20, 10, 20);
				}
			}
		}
	}

	/**
	 * @param activity
	 * @param mTabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void addTab(TabsFragmentActivity activity, TabHost mTabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();
		Log.e("tab tag", tag);
		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}

		mTabHost.addTab(tabSpec);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	public void onTabChanged(String tag) {

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			alertdialog();
		} else {

			editor.putString("Selected-Tab", tag.toUpperCase());
			editor.commit();
			Log.e("tab tag", tag);
			TabInfo newTab = this.mapTabInfo.get(tag);
			if (mLastTab != newTab) {
				FragmentTransaction ft = this.getSupportFragmentManager()
						.beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						ft.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(this,
								newTab.clss.getName(), newTab.args);
						ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
					} else {
						if (Integer.decode(Build.VERSION.SDK) > 17) {
							newTab.fragment = Fragment.instantiate(this,
									newTab.clss.getName(), newTab.args);
							ft.add(R.id.realtabcontent, newTab.fragment,
									newTab.tag);
						} else {
							ft.attach(newTab.fragment);
						}
					}
				}

				textForHeader.setText(tag.toUpperCase());
				mLastTab = newTab;
				ft.commit();
				this.getSupportFragmentManager().executePendingTransactions();
			}
		}
	}

	@Override
	public void onClick(View v) {
		customerPreference=getSharedPreferences("customerPrefs", Context.MODE_PRIVATE);
		delivery_status = customerPreference.getString("delivery", "");		
		
		 storeclosedPreference=getSharedPreferences("storeclosedPrefs", Context.MODE_PRIVATE);
		 store_status=storeclosedPreference.getString("storeval", "");
		 
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSliderMenu:
			toggle();
			break;
		case R.id.layHeaderCart:
			Log.e("Delivery_status in Tabs Fragment page", delivery_status);
			Log.e("Store_status in Tabs Fragment page", store_status);
			if(delivery_status.equals("0") || store_status.equals("storeclosed")){
				layCartCount.setVisibility(View.GONE);
				clearCart();
				Utils.ShowAlert(TabsFragmentActivity.this,
						"You have no items in your shopping cart.");
			}else{
			if (Constant.cartArray.size() > 0) {
				startActivity(new Intent(TabsFragmentActivity.this,
						CartActivity.class));
			} else {
				Utils.ShowAlert(TabsFragmentActivity.this,
						"You have no items in your shopping cart.");
			}
			}
			break;
		case R.id.cartBtn:			
			Log.e("Delivery_status in Tabs Fragment page", delivery_status);
			Log.e("Store_status in Tabs Fragment page", store_status);
			if(delivery_status.equals("0") || store_status.equals("storeclosed")){
				layCartCount.setVisibility(View.GONE);
				clearCart();
				Utils.ShowAlert(TabsFragmentActivity.this,
						"You have no items in your shopping cart.");
			}else{
			if (Constant.cartArray.size() > 0) {
				startActivity(new Intent(TabsFragmentActivity.this,
						CartActivity.class));
			} else {
				Utils.ShowAlert(TabsFragmentActivity.this,
						"You have no items in your shopping cart.");
			}
		 }
			break;
		case R.id.addItemBtn:
			startActivity(new Intent(TabsFragmentActivity.this,
					AddItemActivity.class));
			break;
		default:
			break;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("comes to onactivity result", "activity result");
		if (requestCode == 1) {
			Log.e("comes to onactivity result 11", "activity result 111");
			if (resultCode == RESULT_OK) {
				Log.e("comes to onactivity result 22", "activity result 22");
				try {
					String position = data.getStringExtra("position");

					Log.e("position in call back", position);

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}

	}// onActivityResult

	private void clearCart() {
		Constant.cartArray.clear();
		DBAdapter db = new DBAdapter(TabsFragmentActivity.this);
		db.open();
		db.deleteRows();
		db.close();
	}
	
	public void alertdialog() {
		// Build the alert dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(
				TabsFragmentActivity.this);
		builder.setTitle("Location Services Not Active");
		builder.setMessage("Hey! Turn on your GPS and Location Services");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				// Show location settings when the user acknowledges the alert
				// dialog
				Intent intent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		Dialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

}