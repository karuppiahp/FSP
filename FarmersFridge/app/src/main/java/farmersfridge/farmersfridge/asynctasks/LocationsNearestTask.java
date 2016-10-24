package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.map.MapMain;
import farmersfridge.farmersfridge.models.LocationsItemModel;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/9/2016.
 */
public class LocationsNearestTask {
    private Context context;
    private String result;
    private JSONObject jObject = null;
    private FragmentManager fragmentManager;
    private MapMain mapMain;
    private double latitude, longitude;

    public LocationsNearestTask(Context context, MapMain mapMain, double latitude, double longitude, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.mapMain = mapMain;
        this.latitude = latitude;
        this.longitude = longitude;
        ((FragmentMain) context).setProgressBarVisibile();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse("https://p1pptmol1j.execute-api.us-east-1.amazonaws.com/dev/v1/general/nearest?latitude="+latitude+"&longitude="+-longitude+"&vendItem="+ Constants.VEND_NAME.replace(" ","%20")).getJSONArrayfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
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

        /*JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("latitude", 41.879517);
            jsonObj.put("longitude", -87.6361477);
            jsonObj.put("vendItem", Constants.VEND_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("json obj??",""+jsonObj.toString());

        JsonObjectRequest jsonObjReq1 = new JsonObjectRequest(Request.Method.POST,
                "https://p1pptmol1j.execute-api.us-east-1.amazonaws.com/dev/v1/general/nearest", jsonObj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response????", response.toString());
                        result = response.toString();
                        Log.e("2ndginalertelsemsg", "2ndginalertelsemsg------->" + result);

                        try {
                            ((FragmentMain) context).setProgressBarGone();
                            JSONArray jsonArray = new JSONArray(result);
                            if (jsonArray != null) {
                                for(int i=0; i<jsonArray.length(); i++) {
                                    String id = jsonArray.getJSONObject(i).getString("id");
                                    String prettyName = jsonArray.getJSONObject(i).getString("prettyName");
                                    String address = jsonArray.getJSONObject(i).getString("address");
                                    String hours = jsonArray.getJSONObject(i).getString("hours");
                                    String status = jsonArray.getJSONObject(i).getString("status");
                                    String latitude = jsonArray.getJSONObject(i).getString("latitude");
                                    String longitude = jsonArray.getJSONObject(i).getString("longitude");

                                    String name = id + "\n" + prettyName + "\n" + address + "\n";

                                    LocationsItemModel locationModel = new LocationsItemModel();
                                    locationModel.setId(id);
                                    locationModel.setPrettyName(prettyName);
                                    locationModel.setAddress(address);
                                    locationModel.setHours(hours);
                                    locationModel.setStatus(status);
                                    locationModel.setLat(latitude);
                                    locationModel.setLong(longitude);
                                    MapMain.locationsArray.add(locationModel);
                                    mapMain.addMArkers(Double.parseDouble(latitude), Double.parseDouble(longitude), id, address);
                                }
                            }
                        } catch (JSONException e) {
                            ((FragmentMain) context).setProgressBarGone();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ((FragmentMain) context).setProgressBarGone();
                Log.e("error>>",""+error.toString());
                try {
                    jObject = new JSONObject(message.toString());
                    Log.e("json objt>>",""+jObject.toString());
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
        }) {


            *//** Passing some request headers*//*

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                params.put("Content-Type", "application/json");
                params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());
                return params;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjReq1);*/
    }
}
