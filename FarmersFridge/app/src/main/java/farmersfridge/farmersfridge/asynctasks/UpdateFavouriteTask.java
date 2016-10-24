package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import farmersfridge.farmersfridge.adapter.MenuDetailAdapter;
import farmersfridge.farmersfridge.adapter.MenuListAdapter;
import farmersfridge.farmersfridge.database.models.Favourites;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 7/4/2016.
 */
public class UpdateFavouriteTask {
    private Context context;
    private String result, from;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private View view, imageView;
    private String name;

    public UpdateFavouriteTask(Context context, ApiCallParams apiCallParams, View view, View imageView, String name, String from) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.view = view;
        this.imageView = imageView;
        this.name = name;
        this.from = from;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.POST, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { //Error response
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
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { //Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                    }
                    if(jObject.has("result")) {
                        if (jObject.getString("result").equals("true")) {
                            if(from.equals("list")) { //if update came from list UI updated on list screen
                                MenuListAdapter.favImgUpdated(view, imageView, name);
                                Favourites item = new Favourites();
                                int count = item.getAllRow().size();
                                item.remoteId = count;
                                item.favName = name;
                                item.save();
                            } else { //else UI updated on detail screen
                                MenuDetailAdapter.favImgUpdated(view, imageView, name);
                                Favourites item = new Favourites();
                                int count = item.getAllRow().size();
                                item.remoteId = count;
                                item.favName = name;
                                item.save();
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
