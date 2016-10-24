package farmersfridge.farmersfridge.database.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by karuppiah on 6/27/2016.
 */
@Table(name = "MenuCategory")
public class MenuCategory extends Model {
    // This is a regular field
    @Column(name = "Category")
    public String category;

    // Make sure to have a default constructor for every ActiveAndroid model
    public MenuCategory(){
        super();
    }

    public MenuCategory(String category) {
        super();
        this.category = category;
    }

    //List all items from Items table
    public static List<MenuCategory> getAllRows() {
        return new Select().from(MenuCategory.class).execute();
    }

}
