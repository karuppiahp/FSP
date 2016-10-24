package farmersfridge.farmersfridge.models;

import java.util.List;

import farmersfridge.farmersfridge.database.models.MenuItem;

/**
 * Created by karuppiah on 8/23/2016.
 */
public class MenuModelRec {
    private String cathegoryName;
    private List<MenuItem> menuItem;

    public String getCathegoryName() {
        return cathegoryName;
    }

    public void setCathegoryName(String cathegoryName) {
        this.cathegoryName = cathegoryName;
    }

    public List<MenuItem> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(List<MenuItem> menuItem) {
        this.menuItem = menuItem;
    }
}
