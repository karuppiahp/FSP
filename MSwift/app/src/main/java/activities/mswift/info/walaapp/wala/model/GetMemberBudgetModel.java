package activities.mswift.info.walaapp.wala.model;

/**
 * Created by karuppiah on 2/12/2016.
 */
public class GetMemberBudgetModel {

    private String category, subCategory, freq, amnt;

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public void setAmnt(String amnt) {
        this.amnt = amnt;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getFreq() {
        return freq;
    }

    public String getAmnt() {
        return amnt;
    }
}
