package farmersfridge.farmersfridge.models;

import java.util.ArrayList;

/**
 * Created by karuppiah on 9/6/2016.
 */
public class AboutUsSectionModel {
    private String header;
    private ArrayList<AboutUsSubHeaderModel> subHeaderArray;

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setSubHeaderList(ArrayList<AboutUsSubHeaderModel> subHeaderArray) {
        this.subHeaderArray = subHeaderArray;
    }

    public ArrayList<AboutUsSubHeaderModel> getSubHeaderList() {
        return subHeaderArray;
    }
}
