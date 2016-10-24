package farmersfridge.farmersfridge.utils;

import java.util.ArrayList;
import java.util.List;

import farmersfridge.farmersfridge.models.CathegoryModel;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.models.MyFridgeModelRec;

/**
 * Created by karuppiah on 6/14/2016.
 */
public class Constants {

    public static String FIRST_NAME = "";
    public static String LAST_NAME = "";
    public static String PHONE_NO = "";
    public static String MAIL_ID = "";
    public static String REGISTER = "";
    public static boolean FAV_ENTERS = false;
    public static String LOC_HEADER = "";
    public static String LOC_FROM = "";
    public static String VEND_NAME = "";
    public static String API_DATE = "";
    public static int INFO_ITEM_POS = 0;
    public static int MYFRIDGE_INFO_FAV_POS = 0;

 //   public static String ACCESS_TOKEN_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmYXJtZXJzZnJpZGdlYXBpIiwiYXVkIjoiZmFybWVyc2ZyaWRnZSIsImlhdCI6MTQ2NTgwODMzMSwiaW5mbyI6eyJ0eXBlIjoiamF2YS5sYW5nLlN0cmluZyIsImRhdGEiOiJyYWplbmRyYW41NGtAZ21haWwuY29tIn19.c0nIfWXxasmFwT2-QoQhPRr3GGXjhhEUH_gr2olqhF0";

    public static String ACCESS_TOKEN_KEY = "";

    public static List<List<MenuItemModel>> menuListMainArray = new ArrayList<>();
    public static List<CathegoryModel> menuList = new ArrayList<CathegoryModel>();
    public static List<MyFridgeModelRec> favArray = new ArrayList<>();
    public static List<List<MenuItemModel>> locMenuArray = new ArrayList<>();
    public static List<CathegoryModel> locCategoryArray = new ArrayList<>();
}
