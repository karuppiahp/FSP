package farmersfridge.farmersfridge.models;

import android.util.Log;

import java.util.HashMap;

import farmersfridge.farmersfridge.utils.ApiUrlGenerator;

/**
 * Created by karuppiah on 8/1/2016.
 */
public class FavApiModel {

    public String item;

    public FavApiModel(){

    }

    public FavApiModel(String item) {
        this.item = item;
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
