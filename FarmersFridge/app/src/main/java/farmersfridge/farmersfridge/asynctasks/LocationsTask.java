package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.map.MapMain;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.models.LocationsItemModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 8/5/2016.
 */
public class LocationsTask {
    private Context context;
    private String result;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private FragmentManager fragmentManager;
    private MapMain mapMain;

    public LocationsTask(Context context, MapMain mapMain, ApiCallParams apiCallParams, FragmentManager fragmentManager) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.fragmentManager = fragmentManager;
        this.mapMain = mapMain;
        ((FragmentMain) context).setProgressBarVisibile();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONArrayfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                ((FragmentMain) context).setProgressBarGone();
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    ((FragmentMain) context).setProgressBarGone();
                    JSONArray jsonArray = new JSONArray(result);
                    if (jsonArray != null) {
                        for(int i=0; i<jsonArray.length(); i++) { // Json Array
                            String id = jsonArray.getJSONObject(i).getString("id");
                            String prettyName = jsonArray.getJSONObject(i).getString("prettyName");
                            String address = jsonArray.getJSONObject(i).getString("address");
                            String hours = jsonArray.getJSONObject(i).getString("hours");
                            String status = jsonArray.getJSONObject(i).getString("status");
                            String latitude = jsonArray.getJSONObject(i).getString("latitude");
                            String longitude = jsonArray.getJSONObject(i).getString("longitude");

                            // Values saved in LocationsItemModel class
                            LocationsItemModel locationModel = new LocationsItemModel();
                            locationModel.setId(id);
                            locationModel.setPrettyName(prettyName);
                            locationModel.setAddress(address);
                            locationModel.setHours(hours);
                            locationModel.setStatus(status);
                            locationModel.setLat(latitude);
                            locationModel.setLong(longitude);
                            MapMain.locationsArray.add(locationModel); // LocationsItemModel class added in locationsArray on MapMain fragment
                            mapMain.addMArkers(Double.parseDouble(latitude), Double.parseDouble(longitude), id, address); // Markers are set
                        }
                    }
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
