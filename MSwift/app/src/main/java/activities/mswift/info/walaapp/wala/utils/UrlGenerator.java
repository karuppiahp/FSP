package activities.mswift.info.walaapp.wala.utils;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.application.MSwiftApplication;

/**
 * Created by karuppiah on 12/3/2015.
 */
public class UrlGenerator {

    /*
     * The api URL are fetched from xml file using application app resources.
	 */

    public static String getCountries() {
        return MSwiftApplication.getAppString((R.string.baseUrl),
                MSwiftApplication.getAppString(R.string.countryApi));
    }

    public static String signUp() {
        return MSwiftApplication.getAppString((R.string.baseUrl),
                MSwiftApplication.getAppString(R.string.signUpApi));
    }
}
