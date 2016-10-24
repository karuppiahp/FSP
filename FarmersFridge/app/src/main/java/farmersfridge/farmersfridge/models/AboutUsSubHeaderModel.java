package farmersfridge.farmersfridge.models;

import java.util.ArrayList;

/**
 * Created by karuppiah on 9/6/2016.
 */
public class AboutUsSubHeaderModel {
    String header;
    ArrayList<AboutUsParaModel> paraArray = new ArrayList<>();

    public void setSubHeader(String header) {
        this.header = header;
    }

    public String getSubHeader() {
        return  header;
    }

    public void setParrayList(ArrayList<AboutUsParaModel> paraArray) {
        this.paraArray = paraArray;
    }

    public ArrayList<AboutUsParaModel> getParaList() {
        return paraArray;
    }
}
