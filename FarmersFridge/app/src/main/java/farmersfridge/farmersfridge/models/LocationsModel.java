package farmersfridge.farmersfridge.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import farmersfridge.farmersfridge.utils.ApiUrlGenerator;
import farmersfridge.farmersfridge.utils.Constants;

/**
 * Created by karuppiah on 8/5/2016.
 */
public class LocationsModel {

    private String menu;

    public LocationsModel(){

    }

    public LocationsModel(String menu){
        this.menu = menu;
    }

    /**
     * Returns API call params for locations
     */
    public ApiCallParams locations(){

        String endpoint= "locations";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }

    /**
     * Returns API call params for GET /general/{location}/get_menu
     */
    public ApiCallParams locationsMenu(){

        String endpoint= "general/" + menu + "/get_menu";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }

    /**
     * Returns API call params for GET /general/nearest
     */
    public ApiCallParams locationsNearest(double lat, double lang){

        String endpoint= "general/nearest";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);
        Log.e("lat??",""+lat);
        Log.e("lang??",""+lang);
        Log.e("vand name??",""+Constants.VEND_NAME);

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("latitude", 41.879517);
            jsonObj.put("longitude", -87.6361477);
            jsonObj.put("vendItem", Constants.VEND_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("latitude", lat);
        params.put("longitude", lang);
        params.put("vendItem", ""+ Constants.VEND_NAME);

        return new ApiCallParams(params, url, endpoint, "");
    }
}
