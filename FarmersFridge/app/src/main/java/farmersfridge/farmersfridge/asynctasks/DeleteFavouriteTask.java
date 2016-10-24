package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Delete;

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
 * Created by karuppiah on 7/5/2016.
 */
public class DeleteFavouriteTask {
    private Context context;
    private String result, from;
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private View view, imageView;
    private String name;

    public DeleteFavouriteTask(Context context, ApiCallParams apiCallParams, View view, View imageView, String name, String from) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.view = view;
        this.imageView = imageView;
        this.name = name;
        this.from = from;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.DELETE, apiCallParams.getParams(), context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { // Error response
                try {
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                        if(jObject.has("result")) {
                            if (jObject.getString("result").equals("true")) { // if result contains true in error message then UI updates
                                if(from.equals("list")) { // Update UI on menu list screen
                                    MenuListAdapter.favImgUpdated(view, imageView, name);
                                    new Delete().from(Favourites.class).where("favName = ?", new String[]{name}).execute();
                                } else { // Update UI on menu detail screen
                                    MenuDetailAdapter.favImgUpdated(view, imageView, name);
                                    new Delete().from(Favourites.class).where("favName = ?", new String[]{name}).execute();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) { // Success response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                    }
                    if(jObject.has("result")) {
                        if (jObject.getString("result").equals("true")) {
                            MenuListAdapter.favImgUpdated(view, imageView, name);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
