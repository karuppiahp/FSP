package farmersfridge.farmersfridge.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import farmersfridge.farmersfridge.utils.ApiUrlGenerator;

/**
 * Created by karuppiah on 6/23/2016.
 */
public class MenuModel {

    public ArrayList<MenuItemModels> items = new ArrayList<>();
    public String item;

    public MenuModel(){

    }

    public MenuModel(String item) {
        this.item = item;
    }

    /**
     * Returns API call params for general/get_all_menu
     */
    public ApiCallParams menu(){

        String endpoint= "general/get_all_menu";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }

    /**
     * Returns API call params for GET clients/get_metadata
     */
    public ApiCallParams getFavouritesRes(){

        String endpoint= "clients/get_metadata";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }

    /**
     * Returns API call params for POST clients/favorites
     */
    public ApiCallParams updateFavourites(){

        String endpoint= "clients/favorites";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("item", "" + ""+item);

        return new ApiCallParams(params, url, endpoint);
    }

    /**
     * Returns API call params for POST clients/favorites
     */
    public ApiCallParams deleteFavourites(){

        String endpoint= "clients/favorites";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("item", "" + ""+item);

        return new ApiCallParams(params, url, endpoint);
    }
}
