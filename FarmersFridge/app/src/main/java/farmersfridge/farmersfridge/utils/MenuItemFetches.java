package farmersfridge.farmersfridge.utils;

import java.util.ArrayList;
import java.util.List;

import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.database.models.MenuItem;
import farmersfridge.farmersfridge.models.CathegoryModel;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.models.MyFridgeModelRec;

/**
 * Created by karuppiah on 6/29/2016.
 */
public class MenuItemFetches {

    //Menu items are fetched for Favourites
    public static void getFavourites() {
        Constants.favArray.clear();
        List<MenuItem> menuItemRows = MenuItem.getAllItemsByFav();
        for (int j = 0; j < menuItemRows.size(); j++) {
            MyFridgeModelRec model = new MyFridgeModelRec();
            model.setVendName(menuItemRows.get(j).vendName);
            model.setCategory(menuItemRows.get(j).category);
            model.setDaysExpiry(menuItemRows.get(j).daysToExpiry);
            model.setPrice(menuItemRows.get(j).price);
            model.setSortKey(menuItemRows.get(j).sortKey);
            model.setIconName(menuItemRows.get(j).iconName);
            model.setIconPath(menuItemRows.get(j).iconPath);
            model.setNutritionName(menuItemRows.get(j).nutritionName);
            model.setNutritionPath(menuItemRows.get(j).nutritionPath);
            Constants.favArray.add(model);
        }
    }

    //Location menu items are categorized
    public static List<List<MenuItemModel>> locMenuItems(List<MenuItemModel> locMenuArray) {
        Constants.locMenuArray.clear();
        for (int i = 0; i < Constants.locCategoryArray.size(); i++) {
            List<MenuItemModel> menuList = new ArrayList<MenuItemModel>();
            for (int j = 0; j < locMenuArray.size(); j++) {
                if (Constants.locCategoryArray.get(i).getName().equals(locMenuArray.get(j).getCategory())) { //According to the category menu items are fetched
                    MenuItemModel menu = new MenuItemModel();
                    menu.setTitle(locMenuArray.get(j).getTitle());
                    menu.setDescription(locMenuArray.get(j).getDescription());
                    menu.setDescription_long(locMenuArray.get(j).getDescription_long());
                    menu.setImageUrl(locMenuArray.get(j).getImageUrl());
                    menu.setInfoPath(locMenuArray.get(j).getInfoPath());
                    menu.setCategory(locMenuArray.get(j).getCategory());
                    menu.setPrice(locMenuArray.get(j).getPrice());
                    if (Constants.FAV_ENTERS == true) { //Checks if favourite presents or not
                        Favourites favourites = new Favourites();
                        List<Favourites> allFavRow = favourites.getAllRow();
                        for (int k = 0; k < allFavRow.size(); k++) {
                            if (allFavRow.get(k).favName.equals(locMenuArray.get(j).getTitle())) {
                                menu.setIsFavourite(true);
                                break;
                            } else {
                                menu.setIsFavourite(false);
                            }
                        }
                    }
                    menuList.add(menu);
                }
            }
            Constants.locMenuArray.add(menuList);
        }
        return Constants.locMenuArray;
    }

    //Categories are fetched
    public static List<CathegoryModel> getLocCathegory(List<CathegoryModel> categoryArray) {
        Constants.locCategoryArray.clear();
        for (int i = 0; i < categoryArray.size(); i++) {

            CathegoryModel menu = new CathegoryModel();
            menu.setName(categoryArray.get(i).getName());
            Constants.locCategoryArray.add(menu);
        }
        return Constants.locCategoryArray;
    }
}
