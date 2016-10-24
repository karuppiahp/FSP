package activities.mswift.info.walaapp.wala.model;

/**
 * Created by karuppiah on 11/25/2015.
 */
public class RecentTransactionsModel {

    private String category;
    private String description;
    private String amount;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }
}
