package farmersfridge.farmersfridge.database.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import farmersfridge.farmersfridge.models.MenuModelRec;

/**
 * Created by karuppiah on 6/11/2016.
 */
@Table(name = "MenuItems")
public class MenuItem extends Model {
    // This is the unique id given by the server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    // This is a regular fields
    @Column(name = "Category")
    public String category;
    @Column(name = "DaysToExpiry")
    public String daysToExpiry;
    @Column(name = "Price")
    public String price;
    @Column(name = "SortKey")
    public String sortKey;
    @Column(name = "VendName")
    public String vendName;
    @Column(name = "IconName")
    public String iconName;
    @Column(name = "IconPath")
    public String iconPath;
    @Column(name = "NutritionName")
    public String nutritionName;
    @Column(name = "NutritionPath")
    public String nutritionPath;

    // Make sure to have a default constructor for every ActiveAndroid model
    public MenuItem(){
        super();
    }

    public MenuItem(int remoteId, String category, String daysToExpiry, String price, String sortKey, String vendName, String iconName, String iconPath, String nutritionName, String nutritionPath){
        super();
        this.remoteId = remoteId;
        this.category = category;
        this.daysToExpiry = daysToExpiry;
        this.price = price;
        this.sortKey = sortKey;
        this.vendName = vendName;
        this.iconName = iconName;
        this.iconPath = iconPath;
        this.nutritionName = nutritionName;
        this.nutritionPath = nutritionPath;
    }

    //List all items from MenuItem table
    public static List<MenuItem> getAllRow() {
        return new Select().from(MenuItem.class).execute();
    }

    //List all categories from MenuCategory table
    public static List<MenuCategory> getAllcathegorys(){
        return new Select().from(MenuCategory.class).execute();
    }

    //List of all menu items according to all category
    public static List<MenuModelRec> getAllItems(){

        List<MenuModelRec> getAllItems = new ArrayList<>();
        List<MenuCategory> menuCategories = getAllcathegorys();

        for (int i=0; i<menuCategories.size(); i++){

            String cathegoryName = menuCategories.get(i).category;

            MenuModelRec menuModelRec = new MenuModelRec();
            menuModelRec.setCathegoryName(cathegoryName);
            menuModelRec.setMenuItem(new Select().from(MenuItem.class).where("Category = ?", cathegoryName).<MenuItem>execute());
            getAllItems.add(menuModelRec);
        }

        return getAllItems;
    }

    //List of Menu items according to the category
    public static List<MenuItem> getAllItemsByCat(String categoryName){

        List<MenuItem> getAllItems = new ArrayList<>();
        getAllItems = new Select().from(MenuItem.class).where("Category = ?", categoryName).<MenuItem>execute();

        return getAllItems;
    }

    //List of fav menu item fetches
    public static List<MenuItem> getAllItemsByFav(){

        return new Select().from(MenuItem.class).innerJoin(Favourites.class).on("VendName=Favourites.favName").execute();
    }
}
