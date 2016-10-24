package farmersfridge.farmersfridge.models;

/**
 * Created by karuppiah on 8/25/2016.
 */
public class MyFridgeModelRec {
    private String category;
    private String daysToExpiry;
    private String price;
    private String sortKey;
    private String vendName;
    private String iconName;
    private String iconPath;
    private String nutritionName;
    private String nutritionPath;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDaysExpiry() {
        return daysToExpiry;
    }

    public void setDaysExpiry(String daysToExpiry) {
        this.daysToExpiry = daysToExpiry;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getVendName() {
        return vendName;
    }

    public void setVendName(String vendName) {
        this.vendName = vendName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getNutritionName() {
        return nutritionName;
    }

    public void setNutritionName(String nutritionName) {
        this.nutritionName = nutritionName;
    }

    public String getNutritionPath() {
        return nutritionPath;
    }

    public void setNutritionPath(String nutritionPath) {
        this.nutritionPath = nutritionPath;
    }
}
