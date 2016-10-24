package activities.mswift.info.walaapp.wala.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import activities.mswift.info.walaapp.wala.utils.AnalyticsTrackers;
import activities.mswift.info.walaapp.wala.utils.Constants;
import io.intercom.android.sdk.Intercom;

/**
 * Created by karuppiah on 12/2/2015.
 */
public class MSwiftApplication extends Application {

    private static Context context;

    public static final String TAG = MSwiftApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static MSwiftApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = null;
        context = getApplicationContext();

        //initialise Intercom
        Intercom.initialize(this, Constants.API_KEY, Constants.API_ID);

        //initialise google analitics
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }

    public static Context getGlobalContext() {
        return context;
    }

    public static Resources getAppResources() {
        return context.getResources();
    }

    public static String getAppString(int resourceId, Object... formatArgs) {
        return getAppResources().getString(resourceId, formatArgs);
    }

    public static String getAppString(int resourceId) {
        return getAppResources().getString(resourceId);
    }

    public static synchronized MSwiftApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
