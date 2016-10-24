package activities.mswift.info.walaapp.wala.utils;

import android.util.Log;

/**
 * Created by node on 27/6/16.
 */
public class ApiClass {

    private static boolean isInTestMode = false;

    // For production
    public static String BASE_URL_PROD = "https://api.sit.modjadji.org:8243/tipsgo_uat/v1.0.0/";
    public static String BACKEND_PROD="http://dev.getwala.com/";

    // For development
    public static String BASE_URL_TEST = "https://api.dev.modjadji.org:8243/tipsgo_dev/v1.0.0/";
    public static String BACKEND_DEV="http://dev2.getwala.com/";

    public static String getApiUrl(String url) {

        String tmpUrl;

        if (!isInTestMode) {
            tmpUrl = BASE_URL_PROD + url;
            Log.d("Production mode", "------------->");

        } else {
            tmpUrl = BASE_URL_TEST + url;
            Log.d("Test mode", "------------->");
        }
        //return base url + endpoint
        return tmpUrl;
    }


    public static String getBackendApiUrl(String url) {
        Log.d("Local Api call", "------------->");

        String backtmpUrl;

        if (!isInTestMode) {
            backtmpUrl = BACKEND_PROD + url;
            Log.d("Production Development", "------------->");

        } else {
            backtmpUrl = BACKEND_DEV + url;
            Log.d("Test Development", "------------->");
        }
        //return base url + endpoint
        return backtmpUrl;
    }

}
