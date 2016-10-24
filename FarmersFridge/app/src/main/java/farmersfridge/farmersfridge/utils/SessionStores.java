package farmersfridge.farmersfridge.utils;

import android.content.Context;
import android.content.SharedPreferences;

import farmersfridge.farmersfridge.models.FavApiModel;
import farmersfridge.farmersfridge.models.LocationsModel;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.models.SettingsMainModel;
import farmersfridge.farmersfridge.models.UserProfile;
import farmersfridge.farmersfridge.models.XmlParserModel;

/**
 * Created by karuppiah on 6/14/2016.
 */
public class SessionStores {

    private static Context context;
    private static SharedPreferences.Editor editor;
    public static UserProfile USER_PROFILE;
    public static XmlParserModel XML_PARSER_MODEL;
    public static SettingsMainModel SETTINGS_MAIN_MODEL;
    public static MenuModel MENU_MODEL;
    public static FavApiModel FAV_MODEL;
    public static LocationsModel LOC_MODEL;

    public SessionStores(Context context) {
        this.context = context;
        editor = context.getSharedPreferences("user", 0).edit();
    }

    public static boolean saveUserEmail(String mailId) {
        editor = context.getSharedPreferences("user", 0).edit();
        editor.putString("mailId", mailId);
        return editor.commit();
    }

    public static String getUserEmail() {
        SharedPreferences savedSession = context.getSharedPreferences(
                "user", 0);
        return savedSession.getString("mailId", null);
    }

    public static boolean saveUserPhone(String phone) {
        editor = context.getSharedPreferences("user", 0).edit();
        editor.putString("phone", phone);
        return editor.commit();
    }

    public static String getUserPhone() {
        SharedPreferences savedSession = context.getSharedPreferences(
                "user", 0);
        return savedSession.getString("phone", null);
    }

    public static boolean saveAccessToken(String token) {
        editor = context.getSharedPreferences("user", 0).edit();
        editor.putString("accesstoken", token);
        return editor.commit();
    }

    public static String getAccessToken() {
        SharedPreferences savedSession = context.getSharedPreferences(
                "user", 0);
        return savedSession.getString("accesstoken", null);
    }

    public static boolean saveLoginStatus(String status) {
        editor = context.getSharedPreferences("user", 0).edit();
        editor.putString("login", status);
        return editor.commit();
    }

    public static String getLoginStatus() {
        SharedPreferences savedSession = context.getSharedPreferences(
                "user", 0);
        return savedSession.getString("login", null);
    }

    public static boolean saveApiDate(String status) {
        editor = context.getSharedPreferences("api", 0).edit();
        editor.putString("date", status);
        return editor.commit();
    }

    public static String getApiDate() {
        SharedPreferences savedSession = context.getSharedPreferences(
                "api", 0);
        return savedSession.getString("date", null);
    }

    public void clearSession(){
        SessionStores.USER_PROFILE = null;
        SessionStores.XML_PARSER_MODEL = null;
        SessionStores.SETTINGS_MAIN_MODEL = null;
        SessionStores.MENU_MODEL = null;
        SessionStores.FAV_MODEL = null;
        SessionStores.LOC_MODEL = null;
    }
}
