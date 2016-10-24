package farmersfridge.farmersfridge.utils;

/**
 * Created by karuppiah on 6/15/2016.
 */
public class ApiUrlGenerator {

    private static boolean isInTestMode = true;
    public static String BASE_URL_PRODUCTION = "";
    public static String BASE_URL_TEST = "https://p1pptmol1j.execute-api.us-east-1.amazonaws.com/dev/v1/";

    public static String getApiUrl(String endpoint) {

        if (endpoint == "") try {
            throw new Exception("endpoint can not be empty string!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isInTestMode) return BASE_URL_TEST + endpoint;
        else return BASE_URL_PRODUCTION + endpoint;
    }
}
