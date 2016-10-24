package activities.mswift.info.walaapp.wala.tabhost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.home.GoalsActivity;
import activities.mswift.info.walaapp.wala.more.MoreActivity;
import activities.mswift.info.walaapp.wala.prizes.GamesActivity;
import activities.mswift.info.walaapp.wala.progress.BalancesActivity;
import activities.mswift.info.walaapp.wala.signup.LoginActivity;
import activities.mswift.info.walaapp.wala.transactions.TransactionActivity;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

/**
 * Created by karuppiah on 11/21/2015.
 */
public class TabHostFragments extends FragmentActivity implements
        TabHost.OnTabChangeListener {

    private TabHost mTabHost;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabHostFragments.TabInfo>();
    SharedPreferences sharedpreferences;
    Editor editor;
    private TabInfo mLastTab = null;
    private ImageView homeTab, prizesTab, plusTab, progressTab, moreTab;
    private TextView homeTabName, prizesTabName, progressTabName, moreTabName;
    private LinearLayout homeTabNameL, prizesTabNameL, progressTabNameL, moreTabNameL, plusTabNameL;
    private ProgressBar progressBar;
    private RelativeLayout layForTabs;
    private boolean doubleBackToExitPressedOnce = false;

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
    class TabFactory implements TabHost.TabContentFactory {

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
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);

        sharedpreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layForTabs = (RelativeLayout) findViewById(R.id.layForTabs);
        initialiseTabHost(savedInstanceState);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // set  the
        }

        if (Constants.userRegister) {
            setcurrent(4);
        }

        String userEmail = SessionStores.getUserEmail(TabHostFragments.this);
        if (userEmail != null && userEmail.length() > 0) {
            Intercom.client().registerIdentifiedUser(new Registration().withEmail(userEmail));
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
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;

        //home
        View homeIndicator = getLayoutInflater().inflate(R.layout.tab_indicator, null);
        homeTab = (ImageView) homeIndicator.findViewById(R.id.icon);
        homeTabName = (TextView) homeIndicator.findViewById(R.id.tab_name);
        homeTabNameL = (LinearLayout) homeIndicator.findViewById(R.id.tab_content);
        homeTabName.setText("GOALS");
        homeTab.setImageResource(R.drawable.goals);

        //games
        View prizesIndicator = getLayoutInflater().inflate(R.layout.tab_indicator, null);
        prizesTab = (ImageView) prizesIndicator.findViewById(R.id.icon);
        prizesTabName = (TextView) prizesIndicator.findViewById(R.id.tab_name);
        prizesTabNameL = (LinearLayout) prizesIndicator.findViewById(R.id.tab_content);
        prizesTabName.setText("GAMES");
        prizesTab.setImageResource(R.drawable.game_pad);

        //plus
        View mainIndicator = getLayoutInflater().inflate(R.layout.tab_indicator_plus, null);
        plusTabNameL = (LinearLayout) mainIndicator.findViewById(R.id.tab_content);
        plusTabNameL.setBackgroundResource(R.color.pointsBg);
        plusTab = (ImageView) mainIndicator.findViewById(R.id.icon);
        plusTab.setImageResource(R.drawable.tab_cyrcle_selected);

        //balances
        View progressIndicator = getLayoutInflater().inflate(R.layout.tab_indicator, null);
        progressTab = (ImageView) progressIndicator.findViewById(R.id.icon);
        progressTabName = (TextView) progressIndicator.findViewById(R.id.tab_name);
        progressTabNameL = (LinearLayout) progressIndicator.findViewById(R.id.tab_content);
        progressTabName.setText("BALANCES");
        progressTab.setImageResource(R.drawable.balance);

        //more
        View moreIndicator = getLayoutInflater().inflate(R.layout.tab_indicator, null);
        moreTab = (ImageView) moreIndicator.findViewById(R.id.icon);
        moreTabName = (TextView) moreIndicator.findViewById(R.id.tab_name);
        moreTabNameL = (LinearLayout) moreIndicator.findViewById(R.id.tab_content);
        moreTabName.setText("MORE");
        moreTab.setImageResource(R.drawable.more);


        TabHostFragments.addTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("goals").setIndicator(homeIndicator),
                (tabInfo = new TabInfo("goals", GoalsActivity.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabHostFragments.addTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("games").setIndicator(prizesIndicator),
                (tabInfo = new TabInfo("games", GamesActivity.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHostFragments.addTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("transactions").setIndicator(mainIndicator),
                (tabInfo = new TabInfo("transactions", TransactionActivity.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        TabHostFragments.addTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("balances").setIndicator(progressIndicator),
                (tabInfo = new TabInfo("balances", BalancesActivity.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        TabHostFragments.addTab(this, this.mTabHost, this.mTabHost
                        .newTabSpec("more").setIndicator(moreIndicator),
                (tabInfo = new TabInfo("more", MoreActivity.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        this.onTabChanged("transactions");
        mTabHost.setCurrentTab(2);

        mTabHost.setOnTabChangedListener(this);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            if (density == DisplayMetrics.DENSITY_HIGH) {

                mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 80;

            } else if (density == DisplayMetrics.DENSITY_MEDIUM) {

                mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 45;

            } else if (density == DisplayMetrics.DENSITY_LOW) {

                mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 30;

            } else if (density == DisplayMetrics.DENSITY_XHIGH || density == 280) {

                mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 130;

            } else if (density == DisplayMetrics.DENSITY_XXHIGH || density == 360 || density == 400 || density == 420) {

                mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 180;

            } else if (density == DisplayMetrics.DENSITY_XXXHIGH || density == 560) {

                mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 220;

            }
        }
    }

    /**
     * @param activity
     * @param mTabHost
     * @param tabSpec
     * @param tabInfo
     */
    private static void addTab(TabHostFragments activity, TabHost mTabHost,
                               TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        String tag = tabSpec.getTag();
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

        //setting tab selected
        setTabSelected(tag);
        hideKeyboard();

        editor.putString("Selected-Tab", tag.toUpperCase());
        editor.commit();
        TabInfo newTab = this.mapTabInfo.get(tag);
        if (mLastTab != newTab) {
            FragmentTransaction ft = this.getSupportFragmentManager()
                    .beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }

            FrameLayout innerLay = (FrameLayout) findViewById(R.id.realtabcontent);
            if (innerLay.getChildCount() > 0) {
                innerLay.removeAllViews();
            }

            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(this,
                            newTab.clss.getName(), newTab.args);
                    ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
                } else {
                    if (Integer.decode(Build.VERSION.SDK) >= 13) {
                        newTab.fragment = Fragment.instantiate(this,
                                newTab.clss.getName(), newTab.args);
                        ft.add(R.id.realtabcontent, newTab.fragment,
                                newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }
            }

            mLastTab = newTab;
            ft.commit();
            this.getSupportFragmentManager().executePendingTransactions();
        }
    }

    public void setcurrent(int n) {
        mTabHost.setCurrentTab(n);
        mTabHost.setOnTabChangedListener(this);
    }

    public void removeChild() {
        FrameLayout innerLay = (FrameLayout) findViewById(R.id.realtabcontent);
        if (innerLay.getChildCount() > 0) {
            innerLay.removeAllViews();
        }
    }

    public void tabColorMerun() {
        layForTabs.setBackgroundColor(getResources().getColor(R.color.pointsBg));
    }

    public void tabColorWhite() {
        layForTabs.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void progressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void progressBarGone() {
        progressBar.setVisibility(View.GONE);
    }

    public void hideKeyboard() {
        Utils.hideKeyboard(this);
    }

    public void setTabSelected(String tag) {

        homeTabNameL.setBackgroundResource(R.color.balsavingstrans);
        prizesTabNameL.setBackgroundResource(R.color.balsavingstrans);
        progressTabNameL.setBackgroundResource(R.color.balsavingstrans);
        moreTabNameL.setBackgroundResource(R.color.balsavingstrans);
        plusTabNameL.setBackgroundResource(R.color.balsavingstrans);
        plusTab.setImageResource(R.drawable.tab_cyrcle);

        if (tag == "goals") {
            homeTabNameL.setBackgroundResource(R.color.pointsBg);
        }

        if (tag == "games") {
            prizesTabNameL.setBackgroundResource(R.color.pointsBg);
        }

        if (tag == "transactions") {
            plusTabNameL.setBackgroundResource(R.color.pointsBg);
            plusTab.setImageResource(R.drawable.tab_cyrcle_selected);
        }

        if (tag == "balances") {
            progressTabNameL.setBackgroundResource(R.color.pointsBg);
        }

        if (tag == "more") {
            moreTabNameL.setBackgroundResource(R.color.pointsBg);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivity result", "activity result");
        if (requestCode == 1) {
            Log.e("onactivity result 11", "activity result 111");
            if (resultCode == RESULT_OK) {
                Log.e("onactivity result 22", "activity result 22");
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

    public void exitApp() {
        Intent intentToLogin = new Intent(TabHostFragments.this, LoginActivity.class);
        startActivity(intentToLogin);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intercom.client().reset();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, TabHostFragments.this.getResources().getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
