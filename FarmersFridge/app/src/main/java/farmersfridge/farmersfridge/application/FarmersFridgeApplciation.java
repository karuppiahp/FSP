package farmersfridge.farmersfridge.application;

import android.content.Context;
import android.content.res.Resources;

import com.activeandroid.ActiveAndroid;

/**
 * Created by karuppiah on 6/9/2016.
 */
public class FarmersFridgeApplciation extends com.activeandroid.app.Application {

    private static Context context;

    public static final String TAG = FarmersFridgeApplciation.class
            .getSimpleName();

    private static FarmersFridgeApplciation mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = null;
        context = getApplicationContext();
        ActiveAndroid.initialize(this);
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

    public static synchronized FarmersFridgeApplciation getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
